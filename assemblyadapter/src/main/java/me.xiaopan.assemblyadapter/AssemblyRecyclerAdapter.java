package me.xiaopan.assemblyadapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class AssemblyRecyclerAdapter extends RecyclerView.Adapter implements AbstractLoadMoreRecyclerItemFactory.AdapterCallback {
    private static final String TAG = "AssemblyRecyclerAdapter";

    private List dataList;
    private List<AssemblyRecyclerItemFactory> itemFactoryList;
    private AbstractLoadMoreRecyclerItemFactory loadMoreRecyclerItemFactory;
    private AbstractLoadMoreRecyclerItemFactory.AbstractLoadMoreRecyclerItem loadMoreRecyclerItem;
    private boolean itemFactoryLocked;  // 锁定之后就不能再添加ItemFactory了
    private boolean setEnableLoadMore;  // 已经设置过开启加载功能后就不能再添加ItemFactory了

    public AssemblyRecyclerAdapter(List dataList) {
        this.dataList = dataList;
    }

    public AssemblyRecyclerAdapter(Object... dataArray) {
        if(dataArray != null && dataArray.length > 0){
            this.dataList = new ArrayList(dataArray.length);
            Collections.addAll(dataList, dataArray);
        }
    }

    public void addItemFactory(AssemblyRecyclerItemFactory itemFactory){
        if (itemFactoryLocked) {
            throw new IllegalStateException("item factory list locked");
        }
        if (setEnableLoadMore) {
            throw new IllegalStateException("Call a enableLoadMore () method can be not call again after addItemFactory () method");
        }

        if(itemFactoryList == null){
            itemFactoryList = new LinkedList<AssemblyRecyclerItemFactory>();
        }
        itemFactory.setAdapter(this);
        itemFactory.setItemType(itemFactoryList.size());
        itemFactoryList.add(itemFactory);
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

    public void enableLoadMore(AbstractLoadMoreRecyclerItemFactory loadMoreRecyclerItemFactory) {
        if(loadMoreRecyclerItemFactory != null){
            if(itemFactoryList == null || itemFactoryList.size() == 0){
                throw new IllegalStateException("You need to configure AssemblyRecyclerItem use addItemFactory method");
            }
            setEnableLoadMore = true;
            this.loadMoreRecyclerItemFactory = loadMoreRecyclerItemFactory;
            this.loadMoreRecyclerItemFactory.setAdapterCallback(this);
            this.loadMoreRecyclerItemFactory.setAdapter(this);
            this.loadMoreRecyclerItemFactory.setItemType(itemFactoryList.size());
            notifyDataSetChanged();
        }
    }

    public void disableLoadMore() {
        if(loadMoreRecyclerItemFactory != null){
            loadMoreRecyclerItemFactory.loadMoreRunning = false;
            loadMoreRecyclerItemFactory = null;
            notifyDataSetChanged();
        }
    }

    @Override
    public void loading() {
        if(loadMoreRecyclerItemFactory != null){
            loadMoreRecyclerItemFactory.loadMoreRunning = true;
        }
    }

    @Override
    public void loadMoreFinished(){
        if(loadMoreRecyclerItemFactory != null){
            loadMoreRecyclerItemFactory.loadMoreRunning = false;
        }
    }

    @Override
    public void loadMoreFailed(){
        if(loadMoreRecyclerItemFactory != null){
            loadMoreRecyclerItemFactory.loadMoreRunning = false;
        }
        if(loadMoreRecyclerItem != null){
            loadMoreRecyclerItem.showErrorRetry();
        }
    }

    @Override
    public int getItemCount() {
        if(dataList == null || dataList.size() == 0){
            return 0;
        }
        return dataList.size() + (loadMoreRecyclerItemFactory != null ? 1 : 0);
    }

    @Override
    public int getItemViewType(int position) {
        if(itemFactoryList == null || itemFactoryList.size() == 0) {
            throw new IllegalStateException("You need to configure AssemblyRecyclerItem use addItemFactory method");
        }

        itemFactoryLocked = true;
        if(loadMoreRecyclerItemFactory != null && position == getItemCount()-1){
            return loadMoreRecyclerItemFactory.getItemType();
        }

        Object itemObject = getItem(position);
        for(AssemblyRecyclerItemFactory itemFactory : itemFactoryList){
            if(itemFactory.isTarget(itemObject)){
                return itemFactory.getItemType();
            }
        }

        Log.e(TAG, "getItemViewType() - Didn't find suitable AssemblyRecyclerItemFactory. position="+position+", itemObject="+(itemObject!=null?itemObject.getClass().getName():"null"));
        return -1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public Object getItem(int position) {
        return dataList != null && position < dataList.size() ? dataList.get(position) : null;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(itemFactoryList == null || itemFactoryList.size() == 0){
            throw new IllegalStateException("You need to configure AssemblyRecyclerItem use addItemFactory method");
        }

        if(loadMoreRecyclerItemFactory != null && viewType == loadMoreRecyclerItemFactory.getItemType()){
            AssemblyRecyclerItem recyclerItem = loadMoreRecyclerItemFactory.createAssemblyItem(parent);
            if(recyclerItem == null){
                Log.e(TAG, "onCreateViewHolder() - Create AssemblyRecyclerItem failed. ItemFactory="+ loadMoreRecyclerItemFactory.getClass().getName());
                return null;
            }
            return recyclerItem;
        }

        for(AssemblyRecyclerItemFactory itemFactory : itemFactoryList){
            if(itemFactory.getItemType() != viewType){
                continue;
            }

            AssemblyRecyclerItem recyclerItem = itemFactory.createAssemblyItem(parent);
            if(recyclerItem == null){
                Log.e(TAG, "onCreateViewHolder() - Create AssemblyRecyclerItem failed. ItemFactory="+itemFactory.getClass().getName());
            }
            return recyclerItem;
        }

        Log.e(TAG, "onCreateViewHolder() - Didn't find suitable AssemblyRecyclerItemFactory. viewType="+viewType);
        return null;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        if(viewHolder instanceof AssemblyRecyclerItem){
            if(viewHolder instanceof AbstractLoadMoreRecyclerItemFactory.AbstractLoadMoreRecyclerItem){
                this.loadMoreRecyclerItem = (AbstractLoadMoreRecyclerItemFactory.AbstractLoadMoreRecyclerItem) viewHolder;
            }
            ((AssemblyRecyclerItem) viewHolder).setData(position, getItem(position));
        }
    }
}
