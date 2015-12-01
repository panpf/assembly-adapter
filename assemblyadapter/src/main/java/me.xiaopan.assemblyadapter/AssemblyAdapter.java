package me.xiaopan.assemblyadapter;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class AssemblyAdapter extends BaseAdapter implements AbstractLoadMoreListItemFactory.AdapterCallback {
	private static final String TAG = "AssemblyAdapter";

	private List dataList;
	private List<AssemblyItemFactory> itemFactoryList;
	private AbstractLoadMoreListItemFactory loadMoreListItemFactory;
	private AbstractLoadMoreListItemFactory.AbstractLoadMoreListItem loadMoreListItem;
	private boolean itemFactoryLocked;  // 锁定之后就不能再添加ItemFactory了
	private boolean setEnableLoadMore;  // 已经设置过开启加载功能后就不能再添加ItemFactory了

	public AssemblyAdapter(List dataList) {
		this.dataList = dataList;
	}

	public AssemblyAdapter(Object... dataArray) {
		if(dataArray != null && dataArray.length > 0){
			this.dataList = new ArrayList(dataArray.length);
			Collections.addAll(dataList, dataArray);
		}
	}

	public void addItemFactory(AssemblyItemFactory assemblyItemFactory){
		if (itemFactoryLocked) {
			throw new IllegalStateException("item factory list locked");
		}
		if (setEnableLoadMore) {
			throw new IllegalStateException("Call a enableLoadMore () method can be not call again after addItemFactory () method");
		}

		if(itemFactoryList == null){
			itemFactoryList = new LinkedList<AssemblyItemFactory>();
		}
		assemblyItemFactory.setAdapter(this);
		assemblyItemFactory.setItemType(itemFactoryList.size());
		itemFactoryList.add(assemblyItemFactory);
	}

	public List getDataList() {
		return dataList;
	}

	@SuppressWarnings("unchecked")
	public void append(List dataList){
		if(dataList == null || dataList.size() == 0){
			return;
		}

		if(this.dataList == null){
			this.dataList = dataList;
		}else{
			this.dataList.addAll(dataList);
		}
		notifyDataSetChanged();
	}

	public void enableLoadMore(AbstractLoadMoreListItemFactory loadMoreListItemFactory) {
		if(loadMoreListItemFactory != null){
			if(itemFactoryList == null || itemFactoryList.size() == 0){
				throw new IllegalStateException("You need to configure AssemblyItemFactory use addItemFactory method");
			}
			setEnableLoadMore = true;
			this.loadMoreListItemFactory = loadMoreListItemFactory;
			this.loadMoreListItemFactory.setAdapterCallback(this);
			this.loadMoreListItemFactory.setAdapter(this);
			this.loadMoreListItemFactory.setItemType(itemFactoryList.size());
			notifyDataSetChanged();
		}
	}

	public void disableLoadMore() {
		if(loadMoreListItemFactory != null){
			loadMoreListItemFactory.loadMoreRunning = false;
			loadMoreListItemFactory = null;
			notifyDataSetChanged();
		}
	}

	@Override
	public void loading() {
		if(loadMoreListItemFactory != null){
			loadMoreListItemFactory.loadMoreRunning = true;
		}
	}

	@Override
	public void loadMoreFinished(){
		if(loadMoreListItemFactory != null){
			loadMoreListItemFactory.loadMoreRunning = false;
		}
	}

	@Override
	public void loadMoreFailed(){
		if(loadMoreListItemFactory != null){
			loadMoreListItemFactory.loadMoreRunning = false;
		}
		if(loadMoreListItem != null){
			loadMoreListItem.showErrorRetry();
		}
	}

	@Override
	public int getCount() {
		if(dataList == null || dataList.size() == 0){
			return 0;
		}
		return dataList.size() + (loadMoreListItemFactory != null ? 1 : 0);
	}

	@Override
	public Object getItem(int position) {
		return dataList != null && position < dataList.size() ? dataList.get(position) : null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public int getViewTypeCount() {
		if(itemFactoryList == null || itemFactoryList.size() == 0){
			throw new IllegalStateException("You need to configure AssemblyItemFactory use addItemFactory method");
		}
		itemFactoryLocked = true;
		return itemFactoryList.size() + (loadMoreListItemFactory != null ? 1 : 0);
	}

	@Override
	public int getItemViewType(int position) {
		if(itemFactoryList == null || itemFactoryList.size() == 0){
			throw new IllegalStateException("You need to configure AssemblyItemFactory use addItemFactory method");
		}

		itemFactoryLocked = true;
		if(loadMoreListItemFactory != null && position == getCount()-1){
			return loadMoreListItemFactory.getItemType();
		}

		Object itemObject = getItem(position);
		for(AssemblyItemFactory itemFactory : itemFactoryList){
			if(itemFactory.isTarget(itemObject)){
				return itemFactory.getItemType();
			}
		}

		Log.e(TAG, "getItemViewType() - Didn't find suitable AssemblyItemFactory. position="+position+", itemObject="+(itemObject!=null?itemObject.getClass().getName():"null"));
		return -1;
	}

	@Override
	@SuppressWarnings("unchecked")
	public View getView(final int position, View convertView, ViewGroup parent) {
		if(itemFactoryList == null || itemFactoryList.size() == 0){
			throw new IllegalStateException("You need to configure AssemblyItemFactory use addItemFactory method");
		}

		// position是最后一位，说明是加载更多尾巴
		if(loadMoreListItemFactory != null && position == getCount()-1){
			if(convertView == null){
				AssemblyItem assemblyItem = loadMoreListItemFactory.createAssemblyItem(parent);
				if(assemblyItem == null){
					Log.e(TAG, "getView() - Create AssemblyItem failed. position="+position+", ItemFactory="+loadMoreListItemFactory.getClass().getName());
					return null;
				}
				convertView = assemblyItem.getConvertView();
			}

			this.loadMoreListItem = (AbstractLoadMoreListItemFactory.AbstractLoadMoreListItem) convertView.getTag();
			this.loadMoreListItem.setData(position, null);
			return convertView;
		}

		Object itemObject = getItem(position);
		for(AssemblyItemFactory itemFactory : itemFactoryList){
			if(!itemFactory.isTarget(itemObject)){
				continue;
			}

			if (convertView == null) {
				AssemblyItem assemblyItem = itemFactory.createAssemblyItem(parent);
				if(assemblyItem == null){
					Log.e(TAG, "getView() - Create AssemblyItem failed. position="+position+", ItemFactory"+itemFactory.getClass().getName());
					return null;
				}
				convertView = assemblyItem.getConvertView();
			}

			((AssemblyItem) convertView.getTag()).setData(position, itemObject);
			return convertView;
		}

		Log.e(TAG, "getView() - Didn't find suitable AssemblyItemFactory. position="+position+", itemObject="+(itemObject!=null?itemObject.getClass().getName():"null"));
		return null;
	}
}
