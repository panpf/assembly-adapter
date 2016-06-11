package me.xiaopan.assemblyadapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

public class AssemblyRecyclerAdapter extends RecyclerView.Adapter {
    private static final String TAG = "AssemblyRecyclerAdapter";

    private final Object mLock = new Object();
    private List dataList;
    private List<AssemblyRecyclerItemFactory> itemFactoryList;
    private AssemblyLoadMoreRecyclerItemFactory loadMoreRecyclerItemFactory;
    private AssemblyLoadMoreRecyclerItemFactory.AssemblyLoadMoreRecyclerItem loadMoreRecyclerItem;
    private boolean itemFactoryLocked;  // 锁定之后就不能再添加ItemFactory了
    private boolean setEnableLoadMore;  // 已经设置过开启加载功能后就不能再添加ItemFactory了

    public AssemblyRecyclerAdapter(List dataList) {
        this.dataList = dataList;
    }

    @SuppressWarnings("unused")
    public AssemblyRecyclerAdapter(Object[] dataArray) {
        if (dataArray != null && dataArray.length > 0) {
            this.dataList = new ArrayList(dataArray.length);
            Collections.addAll(dataList, dataArray);
        }
    }

    public void addItemFactory(AssemblyRecyclerItemFactory itemFactory) {
        if (itemFactoryLocked) {
            throw new IllegalStateException("item factory list locked");
        }
        if (setEnableLoadMore) {
            throw new IllegalStateException("Call a enableLoadMore () method can be not call again after addItemFactory () method");
        }

        if (itemFactoryList == null) {
            itemFactoryList = new LinkedList<AssemblyRecyclerItemFactory>();
        }
        itemFactory.setAdapter(this);
        itemFactory.setItemType(itemFactoryList.size());
        itemFactoryList.add(itemFactory);
    }

    @SuppressWarnings("unused")
    public List<AssemblyRecyclerItemFactory> getItemFactoryList() {
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
     * @param loadMoreRecyclerItemFactory 加载更多ItemFactory
     */
    public void enableLoadMore(AssemblyLoadMoreRecyclerItemFactory loadMoreRecyclerItemFactory) {
        if (loadMoreRecyclerItemFactory != null) {
            if (itemFactoryList == null || itemFactoryList.size() == 0) {
                throw new IllegalStateException("You need to configure AssemblyRecyclerItem use addItemFactory method");
            }
            setEnableLoadMore = true;
            this.loadMoreRecyclerItemFactory = loadMoreRecyclerItemFactory;
            this.loadMoreRecyclerItemFactory.setLoadMoreRunning(false);
            this.loadMoreRecyclerItemFactory.setEnd(false);
            this.loadMoreRecyclerItemFactory.setAdapter(this);
            this.loadMoreRecyclerItemFactory.setItemType(itemFactoryList.size());
            notifyDataSetChanged();
        }
    }

    /**
     * 关闭加载更多功能
     */
    @SuppressWarnings("unused")
    public void disableLoadMore() {
        if (loadMoreRecyclerItemFactory != null) {
            loadMoreRecyclerItemFactory.setLoadMoreRunning(false);
            loadMoreRecyclerItemFactory.setEnd(false);
            loadMoreRecyclerItemFactory = null;
            notifyDataSetChanged();
        }
    }

    /**
     * 加载更多失败，请求失败的时候需要调用此方法，会显示错误提示，并可点击重新加载
     */
    @SuppressWarnings("unused")
    public void loadMoreFailed() {
        if (loadMoreRecyclerItemFactory != null) {
            loadMoreRecyclerItemFactory.setLoadMoreRunning(false);
        }
        if (loadMoreRecyclerItem != null) {
            loadMoreRecyclerItem.showErrorRetry();
        }
    }

    /**
     * 加载更多完成后设置加载更多是否结束，为true时会显示结束的文案并且不再加载更多
     *
     * @param end 加载更多是否结束
     */
    @SuppressWarnings("unused")
    public void setLoadMoreEnd(boolean end) {
        if (loadMoreRecyclerItemFactory != null) {
            loadMoreRecyclerItemFactory.setLoadMoreRunning(false);
            loadMoreRecyclerItemFactory.setEnd(end);
        }
        if (loadMoreRecyclerItem != null) {
            if (end) {
                loadMoreRecyclerItem.showEnd();
            } else {
                loadMoreRecyclerItem.showLoading();
            }
        }
    }

    @Override
    public int getItemCount() {
        if (dataList == null || dataList.size() == 0) {
            return 0;
        }
        return dataList.size() + (loadMoreRecyclerItemFactory != null ? 1 : 0);
    }

    @Override
    public int getItemViewType(int position) {
        if (itemFactoryList == null || itemFactoryList.size() == 0) {
            throw new IllegalStateException("You need to configure AssemblyRecyclerItem use addItemFactory method");
        }

        itemFactoryLocked = true;
        if (loadMoreRecyclerItemFactory != null && position == getItemCount() - 1) {
            return loadMoreRecyclerItemFactory.getItemType();
        }

        Object itemObject = getItem(position);
        for (AssemblyRecyclerItemFactory itemFactory : itemFactoryList) {
            if (itemFactory.isTarget(itemObject)) {
                return itemFactory.getItemType();
            }
        }

        Log.e(TAG, "getItemViewType() - Didn't find suitable AssemblyRecyclerItemFactory. position=" + position + ", itemObject=" + (itemObject != null ? itemObject.getClass().getName() : "null"));
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
        if (itemFactoryList == null || itemFactoryList.size() == 0) {
            throw new IllegalStateException("You need to configure AssemblyRecyclerItem use addItemFactory method");
        }

        if (loadMoreRecyclerItemFactory != null && viewType == loadMoreRecyclerItemFactory.getItemType()) {
            AssemblyRecyclerItem recyclerItem = loadMoreRecyclerItemFactory.createAssemblyItem(parent);
            if (recyclerItem == null) {
                Log.e(TAG, "onCreateViewHolder() - Create AssemblyRecyclerItem failed. ItemFactory=" + loadMoreRecyclerItemFactory.getClass().getName());
                return null;
            }
            return recyclerItem;
        }

        for (AssemblyRecyclerItemFactory itemFactory : itemFactoryList) {
            if (itemFactory.getItemType() != viewType) {
                continue;
            }

            AssemblyRecyclerItem recyclerItem = itemFactory.createAssemblyItem(parent);
            if (recyclerItem == null) {
                Log.e(TAG, "onCreateViewHolder() - Create AssemblyRecyclerItem failed. ItemFactory=" + itemFactory.getClass().getName());
            }
            return recyclerItem;
        }

        Log.e(TAG, "onCreateViewHolder() - Didn't find suitable AssemblyRecyclerItemFactory. viewType=" + viewType);
        return null;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        if (viewHolder instanceof AssemblyRecyclerItem) {
            if (viewHolder instanceof AssemblyLoadMoreRecyclerItemFactory.AssemblyLoadMoreRecyclerItem) {
                this.loadMoreRecyclerItem = (AssemblyLoadMoreRecyclerItemFactory.AssemblyLoadMoreRecyclerItem) viewHolder;
            }
            ((AssemblyRecyclerItem) viewHolder).setData(position, getItem(position));
        }
    }
}
