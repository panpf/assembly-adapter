package me.xiaopan.assemblyadapter;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

public class AssemblyAdapter extends BaseAdapter {
    private static final String TAG = "AssemblyAdapter";

    private final Object mLock = new Object();
    private List dataList;
    private List<AssemblyItemFactory> itemFactoryList;
    private AbstractLoadMoreListItemFactory loadMoreListItemFactory;
    private AbstractLoadMoreListItemFactory.AbstractLoadMoreListItem loadMoreListItem;
    private boolean itemFactoryLocked;  // 锁定之后就不能再添加ItemFactory了
    private boolean setEnableLoadMore;  // 已经设置过开启加载功能后就不能再添加ItemFactory了

    @SuppressWarnings("unused")
    public AssemblyAdapter(List dataList) {
        this.dataList = dataList;
    }

    @SuppressWarnings("unused")
    public AssemblyAdapter(Object... dataArray) {
        if (dataArray != null && dataArray.length > 0) {
            this.dataList = new ArrayList(dataArray.length);
            Collections.addAll(dataList, dataArray);
        }
    }

    public void addItemFactory(AssemblyItemFactory assemblyItemFactory) {
        if (itemFactoryLocked) {
            throw new IllegalStateException("item factory list locked");
        }
        if (setEnableLoadMore) {
            throw new IllegalStateException("Call a enableLoadMore () method can be not call again after addItemFactory () method");
        }

        if (itemFactoryList == null) {
            itemFactoryList = new LinkedList<AssemblyItemFactory>();
        }
        assemblyItemFactory.setAdapter(this);
        assemblyItemFactory.setItemType(itemFactoryList.size());
        itemFactoryList.add(assemblyItemFactory);
    }

    @SuppressWarnings("unused")
    public List<AssemblyItemFactory> getItemFactoryList() {
        return itemFactoryList;
    }

    @SuppressWarnings("unused")
    public List getDataList() {
        return dataList;
    }

    @SuppressWarnings("unused")
    public void setDataList(List dataList) {
        this.dataList = dataList;
        notifyDataSetChanged();
    }

    @SuppressWarnings("unused")
    public void addAll(Collection collection) {
        if(collection == null || collection.size() == 0){
            return;
        }
        synchronized (mLock) {
            if (dataList == null) {
                dataList = new ArrayList(collection.size());
            }
            //noinspection unchecked
            dataList.addAll(collection);
        }
        notifyDataSetChanged();
    }

    @SuppressWarnings("unused")
    public void addAll(Object ... items) {
        if(items == null || items.length == 0){
            return;
        }
        synchronized (mLock) {
            if (dataList == null) {
                dataList = new ArrayList(items.length);
            }
            Collections.addAll(dataList, items);
        }
        notifyDataSetChanged();
    }

    @SuppressWarnings("unused")
    public void insert(Object object, int index) {
        if(object == null){
            return;
        }
        synchronized (mLock) {
            if (dataList == null) {
                dataList = new ArrayList();
            }
            //noinspection unchecked
            dataList.add(index, object);
        }
        notifyDataSetChanged();
    }

    @SuppressWarnings("unused")
    public void remove(Object object) {
        if(object == null){
            return;
        }
        synchronized (mLock) {
            if (dataList != null) {
                dataList.remove(object);
            }
        }
        notifyDataSetChanged();
    }

    @SuppressWarnings("unused")
    public void clear() {
        synchronized (mLock) {
            if (dataList != null) {
                dataList.clear();
            }
        }
        notifyDataSetChanged();
    }

    @SuppressWarnings("unused")
    public void sort(Comparator comparator) {
        synchronized (mLock) {
            if (dataList != null) {
                Collections.sort(dataList, comparator);
            }
        }
        notifyDataSetChanged();
    }

    /**
     * 开启加载更多功能
     *
     * @param loadMoreListItemFactory 加载更多ItemFactory
     */
    public void enableLoadMore(AbstractLoadMoreListItemFactory loadMoreListItemFactory) {
        if (loadMoreListItemFactory != null) {
            if (itemFactoryList == null || itemFactoryList.size() == 0) {
                throw new IllegalStateException("You need to configure AssemblyItemFactory use addItemFactory method");
            }
            setEnableLoadMore = true;
            this.loadMoreListItemFactory = loadMoreListItemFactory;
            this.loadMoreListItemFactory.setLoadMoreRunning(false);
            this.loadMoreListItemFactory.setEnd(false);
            this.loadMoreListItemFactory.setAdapter(this);
            this.loadMoreListItemFactory.setItemType(itemFactoryList.size());
            notifyDataSetChanged();
        }
    }

    /**
     * 关闭加载更多功能
     */
    @SuppressWarnings("unused")
    public void disableLoadMore() {
        if (loadMoreListItemFactory != null) {
            loadMoreListItemFactory.setLoadMoreRunning(false);
            loadMoreListItemFactory.setEnd(false);
            loadMoreListItemFactory = null;
            notifyDataSetChanged();
        }
    }

    /**
     * 加载更多失败，请求失败的时候需要调用此方法，会显示错误提示，并可点击重新加载
     */
    @SuppressWarnings("unused")
    public void loadMoreFailed() {
        if (loadMoreListItemFactory != null) {
            loadMoreListItemFactory.setLoadMoreRunning(false);
        }
        if (loadMoreListItem != null) {
            loadMoreListItem.showErrorRetry();
        }
    }

    /**
     * 加载更多完成后设置加载更多是否结束，为true时会显示结束的文案并且不再加载更多
     *
     * @param end 加载更多是否结束
     */
    @SuppressWarnings("unused")
    public void setLoadMoreEnd(boolean end) {
        if (loadMoreListItemFactory != null) {
            loadMoreListItemFactory.setLoadMoreRunning(false);
            loadMoreListItemFactory.setEnd(end);
        }
        if (loadMoreListItem != null) {
            if (end) {
                loadMoreListItem.showEnd();
            } else {
                loadMoreListItem.showLoading();
            }
        }
    }

    @Override
    public int getCount() {
        if (dataList == null || dataList.size() == 0) {
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
        if (itemFactoryList == null || itemFactoryList.size() == 0) {
            throw new IllegalStateException("You need to configure AssemblyItemFactory use addItemFactory method");
        }
        itemFactoryLocked = true;
        return itemFactoryList.size() + (loadMoreListItemFactory != null ? 1 : 0);
    }

    @Override
    public int getItemViewType(int position) {
        if (itemFactoryList == null || itemFactoryList.size() == 0) {
            throw new IllegalStateException("You need to configure AssemblyItemFactory use addItemFactory method");
        }

        itemFactoryLocked = true;
        if (loadMoreListItemFactory != null && position == getCount() - 1) {
            return loadMoreListItemFactory.getItemType();
        }

        Object itemObject = getItem(position);
        for (AssemblyItemFactory itemFactory : itemFactoryList) {
            if (itemFactory.isTarget(itemObject)) {
                return itemFactory.getItemType();
            }
        }

        Log.e(TAG, "getItemViewType() - Didn't find suitable AssemblyItemFactory. position=" + position + ", itemObject=" + (itemObject != null ? itemObject.getClass().getName() : "null"));
        return -1;
    }

    @Override
    @SuppressWarnings("unchecked")
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (itemFactoryList == null || itemFactoryList.size() == 0) {
            throw new IllegalStateException("You need to configure AssemblyItemFactory use addItemFactory method");
        }

        // position是最后一位，说明是加载更多尾巴
        if (loadMoreListItemFactory != null && position == getCount() - 1) {
            if (convertView == null) {
                AssemblyItem assemblyItem = loadMoreListItemFactory.createAssemblyItem(parent);
                if (assemblyItem == null) {
                    Log.e(TAG, "getView() - Create AssemblyItem failed. position=" + position + ", ItemFactory=" + loadMoreListItemFactory.getClass().getName());
                    return null;
                }
                convertView = assemblyItem.getItemView();
            }

            this.loadMoreListItem = (AbstractLoadMoreListItemFactory.AbstractLoadMoreListItem) convertView.getTag();
            this.loadMoreListItem.setData(position, null);
            return convertView;
        }

        Object itemObject = getItem(position);
        for (AssemblyItemFactory itemFactory : itemFactoryList) {
            if (!itemFactory.isTarget(itemObject)) {
                continue;
            }

            if (convertView == null) {
                AssemblyItem assemblyItem = itemFactory.createAssemblyItem(parent);
                if (assemblyItem == null) {
                    Log.e(TAG, "getView() - Create AssemblyItem failed. position=" + position + ", ItemFactory" + itemFactory.getClass().getName());
                    return null;
                }
                convertView = assemblyItem.getItemView();
            }

            ((AssemblyItem) convertView.getTag()).setData(position, itemObject);
            return convertView;
        }

        Log.e(TAG, "getView() - Didn't find suitable AssemblyItemFactory. position=" + position + ", itemObject=" + (itemObject != null ? itemObject.getClass().getName() : "null"));
        return null;
    }
}
