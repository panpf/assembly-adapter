package me.xiaopan.assemblyadapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseArray;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import me.xiaopan.assemblyadapter.AssemblyLoadMoreRecyclerItemFactory.AssemblyLoadMoreRecyclerItem;

public class AssemblyRecyclerAdapter extends RecyclerView.Adapter {
    private static final String TAG = "AssemblyRecyclerAdapter";

    private final Object mLock = new Object();
    private List dataList;

    private int itemTypeIndex = 0;
    private boolean itemFactoryLocked;
    private ArrayList<FixedItemInfo> headerItemList;
    private ArrayList<FixedItemInfo> footerItemList;
    private List<AssemblyRecyclerItemFactory> itemFactoryList = new ArrayList<AssemblyRecyclerItemFactory>(5);
    private SparseArray<Object> itemFactoryArray = new SparseArray<Object>();

    private boolean disableLoadMore;
    private AssemblyLoadMoreRecyclerItem loadMoreItem;
    private AssemblyLoadMoreRecyclerItemFactory loadMoreItemFactory;

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

    /**
     * 添加一个将按添加顺序显示在列表头部的AssemblyRecyclerItemFactory
     */
    @SuppressWarnings("unused")
    public FixedItemInfo addHeaderItem(AssemblyRecyclerItemFactory headerFactory, Object data) {
        if (headerFactory == null || itemFactoryLocked) {
            Log.w(TAG, "headerFactory is nll or locked");
            return null;
        }

        headerFactory.setAdapter(this);
        headerFactory.setItemType(itemTypeIndex++);

        FixedItemInfo headerItemInfo = new FixedItemInfo(headerFactory, data);
        itemFactoryArray.put(headerFactory.getItemType(), headerItemInfo);

        if (headerItemList == null) {
            headerItemList = new ArrayList<FixedItemInfo>(2);
        }
        headerItemList.add(headerItemInfo);
        return headerItemInfo;
    }

    /**
     * 添加一个用来处理并显示dataList中的数据的AssemblyRecyclerItemFactory
     */
    public void addItemFactory(AssemblyRecyclerItemFactory itemFactory) {
        if (itemFactory == null || itemFactoryLocked) {
            Log.w(TAG, "itemFactory is nll or locked");
            return;
        }

        itemFactory.setAdapter(this);
        itemFactory.setItemType(itemTypeIndex++);

        itemFactoryArray.put(itemFactory.getItemType(), itemFactory);

        itemFactoryList.add(itemFactory);
    }

    /**
     * 添加一个将按添加顺序显示在列表尾部的AssemblyRecyclerItemFactory
     */
    @SuppressWarnings("unused")
    public FixedItemInfo addFooterItem(AssemblyRecyclerItemFactory footerFactory, Object data) {
        if (footerFactory == null || itemFactoryLocked) {
            Log.w(TAG, "footerFactory is nll or locked");
            return null;
        }

        footerFactory.setAdapter(this);
        footerFactory.setItemType(itemTypeIndex++);

        FixedItemInfo footerItemInfo = new FixedItemInfo(footerFactory, data);
        itemFactoryArray.put(footerFactory.getItemType(), footerItemInfo);

        if (footerItemList == null) {
            footerItemList = new ArrayList<FixedItemInfo>(2);
        }
        footerItemList.add(footerItemInfo);
        return footerItemInfo;
    }

    /**
     * 设置一个将显示在列表最后（在Footer的后面）的加载更多尾巴
     */
    @SuppressWarnings("unused")
    public void setLoadMoreItemFactory(AssemblyLoadMoreRecyclerItemFactory loadMoreItemFactory) {
        if (loadMoreItemFactory == null || itemFactoryLocked) {
            Log.w(TAG, "loadMoreItemFactory is null or locked");
            return;
        }

        loadMoreItemFactory.setEnd(false);
        loadMoreItemFactory.setAdapter(this);
        if (this.loadMoreItemFactory != null) {
            loadMoreItemFactory.setItemType(this.loadMoreItemFactory.getItemType());
        } else {
            loadMoreItemFactory.setItemType(itemTypeIndex++);
        }
        loadMoreItemFactory.setLoadMoreRunning(false);

        itemFactoryArray.put(loadMoreItemFactory.getItemType(), loadMoreItemFactory);

        this.loadMoreItemFactory = loadMoreItemFactory;
    }

    /**
     * 批量添加数据
     */
    @SuppressWarnings("unused")
    public void addAll(Collection collection) {
        if (collection == null || collection.size() == 0) {
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

    /**
     * 批量添加数据
     */
    @SuppressWarnings("unused")
    public void addAll(Object... items) {
        if (items == null || items.length == 0) {
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

    /**
     * 插入一条数据
     */
    @SuppressWarnings("unused")
    public void insert(Object object, int index) {
        if (object == null) {
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

    /**
     * 删除一条数据
     */
    @SuppressWarnings("unused")
    public void remove(Object object) {
        if (object == null) {
            return;
        }
        synchronized (mLock) {
            if (dataList != null) {
                dataList.remove(object);
            }
        }
        notifyDataSetChanged();
    }

    /**
     * 清空数据
     */
    @SuppressWarnings("unused")
    public void clear() {
        synchronized (mLock) {
            if (dataList != null) {
                dataList.clear();
            }
        }
        notifyDataSetChanged();
    }

    /**
     * 对数据排序
     */
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
     * 设置是否禁用加载更多
     */
    @SuppressWarnings("unused")
    public void setDisableLoadMore(boolean disableLoadMore) {
        this.disableLoadMore = disableLoadMore;

        if (loadMoreItemFactory != null) {
            loadMoreItemFactory.setEnd(false);
            loadMoreItemFactory.setLoadMoreRunning(false);
        }

        notifyDataSetChanged();
    }

    /**
     * 设置加载更多是否已结束，已结束会显示结束的文案并且不再触发加载更多
     */
    @SuppressWarnings("unused")
    public void setLoadMoreEnd(boolean loadMoreEnd) {
        if (loadMoreItemFactory != null) {
            loadMoreItemFactory.setLoadMoreRunning(false);
            loadMoreItemFactory.setEnd(loadMoreEnd);
        }
        if (loadMoreItem != null) {
            if (loadMoreEnd) {
                loadMoreItem.showEnd();
            } else {
                loadMoreItem.showLoading();
            }
        }
    }

    /**
     * 加载更多失败的时候调用此方法显示错误提示，并可点击重新加载
     */
    @SuppressWarnings("unused")
    public void loadMoreFailed() {
        if (loadMoreItemFactory != null) {
            loadMoreItemFactory.setLoadMoreRunning(false);
        }
        if (loadMoreItem != null) {
            loadMoreItem.showErrorRetry();
        }
    }

    /**
     * 删除一个HeaderItem
     */
    @SuppressWarnings("unused")
    public void removeHeaderItem(FixedItemInfo headerItemInfo) {
        if (headerItemList != null && headerItemInfo != null) {
            Iterator<FixedItemInfo> iterator = headerItemList.iterator();
            FixedItemInfo fixedItemInfo;
            while (iterator.hasNext()) {
                fixedItemInfo = iterator.next();
                if (fixedItemInfo == headerItemInfo) {
                    iterator.remove();
                    notifyDataSetChanged();
                    return;
                }
            }
        }
    }

    /**
     * 删除一个FooterItem
     */
    @SuppressWarnings("unused")
    public void removeFooterItem(FixedItemInfo footerItemInfo) {
        if (footerItemList != null && footerItemInfo != null) {
            Iterator<FixedItemInfo> iterator = footerItemList.iterator();
            FixedItemInfo fixedItemInfo;
            while (iterator.hasNext()) {
                fixedItemInfo = iterator.next();
                if (fixedItemInfo == footerItemInfo) {
                    iterator.remove();
                    notifyDataSetChanged();
                    return;
                }
            }
        }
    }

    /**
     * 获取Header列表
     */
    @SuppressWarnings("unused")
    public List<FixedItemInfo> getHeaderItemList() {
        return headerItemList;
    }

    /**
     * 获取ItemFactory列表
     */
    @SuppressWarnings("unused")
    public List<AssemblyRecyclerItemFactory> getItemFactoryList() {
        return itemFactoryList;
    }

    /**
     * 获取Footer列表
     */
    @SuppressWarnings("unused")
    public List<FixedItemInfo> getFooterItemList() {
        return footerItemList;
    }

    @SuppressWarnings("unused")
    public List getDataList() {
        return dataList;
    }

    /**
     * 设置数据列表
     */
    @SuppressWarnings("unused")
    public void setDataList(List dataList) {
        this.dataList = dataList;
        notifyDataSetChanged();
    }

    /**
     * 获取列表头的个数
     */
    public int getHeaderItemCount() {
        return headerItemList != null ? headerItemList.size() : 0;
    }

    /**
     * 获取ItemFactory的个数
     */
    public int getItemFactoryCount() {
        return itemFactoryList != null ? itemFactoryList.size() : 0;
    }

    /**
     * 获取列表头的个数
     */
    public int getFooterItemCount() {
        return footerItemList != null ? footerItemList.size() : 0;
    }

    /**
     * 是否有加载更多尾巴
     */
    public boolean hasLoadMoreFooter() {
        return !disableLoadMore && loadMoreItemFactory != null;
    }

    /**
     * 获取数据列表的长度
     */
    public int getDataCount() {
        return dataList != null ? dataList.size() : 0;
    }

    @Override
    public int getItemCount() {
        int headerItemCount = getHeaderItemCount();
        int dataCount = getDataCount();
        int footerItemCount = getFooterItemCount();

        if (dataCount > 0) {
            return headerItemCount + dataCount + footerItemCount + (hasLoadMoreFooter() ? 1 : 0);
        } else {
            return headerItemCount + footerItemCount;
        }
    }

    public Object getItem(int position) {
        // 头
        int headerItemCount = getHeaderItemCount();
        int headerStartPosition = 0;
        int headerEndPosition = headerItemCount - 1;
        if (position >= headerStartPosition && position <= headerEndPosition && headerItemCount > 0) {
            //noinspection UnnecessaryLocalVariable
            int positionInHeaderList = position;
            return headerItemList.get(positionInHeaderList).data;
        }

        // 数据
        int dataCount = getDataCount();
        int dataStartPosition = headerEndPosition + 1;
        int dataEndPosition = headerEndPosition + dataCount;
        if (position >= dataStartPosition && position <= dataEndPosition && dataCount > 0) {
            int positionInDataList = position - headerItemCount;
            return dataList.get(positionInDataList);
        }

        // 尾巴
        int footerItemCount = getFooterItemCount();
        int footerStartPosition = dataEndPosition + 1;
        int footerEndPosition = dataEndPosition + footerItemCount;
        if (position >= footerStartPosition && position <= footerEndPosition && footerItemCount > 0) {
            int positionInFooterList = position - headerItemCount - dataCount;
            return footerItemList.get(positionInFooterList).data;
        }

        // 加载更多尾巴
        if (dataCount > 0 && hasLoadMoreFooter() && position == getItemCount() - 1) {
            return null;
        }

        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        if (getItemFactoryCount() <= 0) {
            throw new IllegalStateException("You need to configure AssemblyItemFactory use addItemFactory method");
        }
        itemFactoryLocked = true;

        // 头
        int headerItemCount = getHeaderItemCount();
        int headerStartPosition = 0;
        int headerEndPosition = headerItemCount - 1;
        if (position >= headerStartPosition && position <= headerEndPosition && headerItemCount > 0) {
            //noinspection UnnecessaryLocalVariable
            int positionInHeaderList = position;
            return headerItemList.get(positionInHeaderList).itemFactory.getItemType();
        }

        // 数据
        int dataCount = getDataCount();
        int dataStartPosition = headerEndPosition + 1;
        int dataEndPosition = headerEndPosition + dataCount;
        if (position >= dataStartPosition && position <= dataEndPosition && dataCount > 0) {
            int positionInDataList = position - headerItemCount;
            Object itemObject = dataList.get(positionInDataList);

            AssemblyRecyclerItemFactory itemFactory;
            for (int w = 0, size = itemFactoryList.size(); w < size; w++) {
                itemFactory = itemFactoryList.get(w);
                if (itemFactory.isTarget(itemObject)) {
                    return itemFactory.getItemType();
                }
            }

            throw new IllegalStateException("Didn't find suitable AssemblyItemFactory. position=" + positionInDataList + ", itemObject=" + (itemObject != null ? itemObject.getClass().getName() : "null"));
        }

        // 尾巴
        int footerItemCount = getFooterItemCount();
        int footerStartPosition = dataEndPosition + 1;
        int footerEndPosition = dataEndPosition + footerItemCount;
        if (position >= footerStartPosition && position <= footerEndPosition && footerItemCount > 0) {
            int positionInFooterList = position - headerItemCount - dataCount;
            return footerItemList.get(positionInFooterList).itemFactory.getItemType();
        }

        // 加载更多尾巴
        if (dataCount > 0 && hasLoadMoreFooter() && position == getItemCount() - 1) {
            return loadMoreItemFactory.getItemType();
        }

        throw new IllegalStateException("not found match viewType, position: " + position);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Object item = itemFactoryArray.get(viewType);

        // Item或加载更多尾巴
        if (item instanceof AssemblyRecyclerItemFactory) {
            AssemblyRecyclerItemFactory itemFactory = (AssemblyRecyclerItemFactory) item;
            AssemblyRecyclerItem assemblyItem = itemFactory.createAssemblyItem(parent);
            if(assemblyItem instanceof AssemblyLoadMoreRecyclerItem){
                this.loadMoreItem = (AssemblyLoadMoreRecyclerItem) assemblyItem;
            }
            return assemblyItem;
        }

        // 头或尾巴
        if (item instanceof FixedItemInfo) {
            FixedItemInfo fixedItemInfo = (FixedItemInfo) item;
            return fixedItemInfo.itemFactory.createAssemblyItem(parent);
        }

        throw new IllegalStateException("unknown viewType: " + viewType+", " +
                "itemFactory: " + (item != null ? item.getClass().getName() : "null"));
    }

    @Override
    @SuppressWarnings("unchecked")
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        if (viewHolder instanceof AssemblyRecyclerItem) {
            AssemblyRecyclerItem assemblyRecyclerItem = (AssemblyRecyclerItem) viewHolder;
            assemblyRecyclerItem.setPositionInAdapter(position);
            assemblyRecyclerItem.setData(position, getItem(position));
        }
    }

    public static class FixedItemInfo {
        private AssemblyRecyclerItemFactory itemFactory;
        private Object data;

        public FixedItemInfo(AssemblyRecyclerItemFactory itemFactory, Object data) {
            this.data = data;
            this.itemFactory = itemFactory;
        }
    }
}
