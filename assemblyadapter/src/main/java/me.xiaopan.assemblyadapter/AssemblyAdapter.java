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

import me.xiaopan.assemblyadapter.AssemblyLoadMoreItemFactory.AssemblyLoadMoreItem;

public class AssemblyAdapter extends BaseAdapter {
    private static final String TAG = "AssemblyAdapter";

    private final Object mLock = new Object();
    private List dataList;

    private int itemTypeIndex = 0;
    private boolean itemFactoryLocked;
    private List<ItemFactoryHolder> headerItemList;
    private List<ItemFactoryHolder> footerItemList;
    private List<AssemblyItemFactory> itemFactoryList;

    private boolean disableLoadMore;
    private AssemblyLoadMoreItem loadMoreItem;
    private AssemblyLoadMoreItemFactory loadMoreItemFactory;

    @SuppressWarnings("unused")
    public AssemblyAdapter(List dataList) {
        this.dataList = dataList;
    }

    @SuppressWarnings("unused")
    public AssemblyAdapter(Object[] dataArray) {
        if (dataArray != null && dataArray.length > 0) {
            this.dataList = new ArrayList(dataArray.length);
            Collections.addAll(dataList, dataArray);
        }
    }

    @SuppressWarnings("unused")
    public void addHeaderFactory(AssemblyItemFactory headerFactory, Object data) {
        if (headerFactory == null || itemFactoryLocked) {
            Log.w(TAG, "headerFactory is nll or locked");
            return;
        }

        headerFactory.setAdapter(this);
        headerFactory.setItemType(itemTypeIndex++);

        if (headerItemList == null) {
            headerItemList = new ArrayList<ItemFactoryHolder>();
        }
        headerItemList.add(new ItemFactoryHolder(headerFactory, data));
    }

    public void addItemFactory(AssemblyItemFactory itemFactory) {
        if (itemFactory == null || itemFactoryLocked) {
            Log.w(TAG, "itemFactory is nll or locked");
            return;
        }

        itemFactory.setAdapter(this);
        itemFactory.setItemType(itemTypeIndex++);

        if (itemFactoryList == null) {
            itemFactoryList = new LinkedList<AssemblyItemFactory>();
        }
        itemFactoryList.add(itemFactory);
    }

    @SuppressWarnings("unused")
    public void addFooterFactory(AssemblyItemFactory footerFactory, Object data) {
        if (footerFactory == null || itemFactoryLocked) {
            Log.w(TAG, "footerFactory is nll or locked");
            return;
        }

        footerFactory.setAdapter(this);
        footerFactory.setItemType(itemTypeIndex++);

        if (footerItemList == null) {
            footerItemList = new ArrayList<ItemFactoryHolder>();
        }
        footerItemList.add(new ItemFactoryHolder(footerFactory, data));
    }

    @SuppressWarnings("unused")
    public void setLoadMoreItemFactory(AssemblyLoadMoreItemFactory loadMoreItemFactory) {
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

        this.loadMoreItemFactory = loadMoreItemFactory;
    }

    @SuppressWarnings("unused")
    public List<ItemFactoryHolder> getHeaderItemList() {
        return headerItemList;
    }

    @SuppressWarnings("unused")
    public List<AssemblyItemFactory> getItemFactoryList() {
        return itemFactoryList;
    }

    @SuppressWarnings("unused")
    public List<ItemFactoryHolder> getFooterItemList() {
        return footerItemList;
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
    public void setLoadMoreEnd(boolean end) {
        if (loadMoreItemFactory != null) {
            loadMoreItemFactory.setLoadMoreRunning(false);
            loadMoreItemFactory.setEnd(end);
        }
        if (loadMoreItem != null) {
            if (end) {
                loadMoreItem.showEnd();
            } else {
                loadMoreItem.showLoading();
            }
        }
    }

    /**
     * 加载更多失败，请求失败的时候需要调用此方法，会显示错误提示，并可点击重新加载
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

    public int getDataSize() {
        return dataList != null ? dataList.size() : 0;
    }

    public int getFooterSize() {
        return footerItemList != null ? footerItemList.size() : 0;
    }

    public int getHeaderSize() {
        return headerItemList != null ? headerItemList.size() : 0;
    }

    public boolean hasLoadMoreFooter() {
        return !disableLoadMore && loadMoreItemFactory != null;
    }

    @Override
    public int getCount() {
        int headerSize = getHeaderSize();
        int dataSize = getDataSize();
        int footerSize = getFooterSize();

        if (dataSize > 0) {
            return headerSize + dataSize + footerSize + (hasLoadMoreFooter() ? 1 : 0);
        } else {
            return headerSize + footerSize;
        }
    }

    @Override
    public Object getItem(int position) {
        // 头
        int headerSize = getHeaderSize();
        int headerStartPosition = 0;
        int headerEndPosition = headerSize - 1;
        if (position >= headerStartPosition && position <= headerEndPosition && headerSize > 0) {
            //noinspection UnnecessaryLocalVariable
            int positionInHeaderList = position;
            return headerItemList.get(positionInHeaderList).data;
        }

        // 数据
        int dataSize = getDataSize();
        int dataStartPosition = headerEndPosition + 1;
        int dataEndPosition = headerEndPosition + dataSize;
        if (position >= dataStartPosition && position <= dataEndPosition && dataSize > 0) {
            int positionInDataList = position - headerSize;
            return dataList.get(positionInDataList);
        }

        // 尾巴
        int footerSize = getFooterSize();
        int footerStartPosition = dataEndPosition + 1;
        int footerEndPosition = dataEndPosition + footerSize;
        if (position >= footerStartPosition && position <= footerEndPosition && footerSize > 0) {
            int positionInFooterList = position - headerSize - dataSize;
            return footerItemList.get(positionInFooterList).data;
        }

        // 加载更多尾巴
        if (dataSize > 0 && hasLoadMoreFooter() && position == getCount() - 1) {
            return null;
        }

        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getViewTypeCount() {
        itemFactoryLocked = true;

        return itemTypeIndex > 0 ? itemTypeIndex : super.getViewTypeCount();
    }

    @Override
    public int getItemViewType(int position) {
        if (itemFactoryList == null || itemFactoryList.size() == 0) {
            throw new IllegalStateException("You need to configure AssemblyItemFactory use addItemFactory method");
        }
        itemFactoryLocked = true;

        // 头
        int headerSize = getHeaderSize();
        int headerStartPosition = 0;
        int headerEndPosition = headerSize - 1;
        if (position >= headerStartPosition && position <= headerEndPosition && headerSize > 0) {
            //noinspection UnnecessaryLocalVariable
            int positionInHeaderList = position;
            return headerItemList.get(positionInHeaderList).itemFactory.getItemType();
        }

        // 数据
        int dataSize = getDataSize();
        int dataStartPosition = headerEndPosition + 1;
        int dataEndPosition = headerEndPosition + dataSize;
        if (position >= dataStartPosition && position <= dataEndPosition && dataSize > 0) {
            int positionInDataList = position - headerSize;
            Object itemObject = dataList.get(positionInDataList);

            for (AssemblyItemFactory itemFactory : itemFactoryList) {
                if (itemFactory.isTarget(itemObject)) {
                    return itemFactory.getItemType();
                }
            }

            throw new IllegalStateException("Didn't find suitable AssemblyItemFactory. position=" + positionInDataList + ", itemObject=" + (itemObject != null ? itemObject.getClass().getName() : "null"));
        }

        // 尾巴
        int footerSize = getFooterSize();
        int footerStartPosition = dataEndPosition + 1;
        int footerEndPosition = dataEndPosition + footerSize;
        if (position >= footerStartPosition && position <= footerEndPosition && footerSize > 0) {
            int positionInFooterList = position - headerSize - dataSize;
            return footerItemList.get(positionInFooterList).itemFactory.getItemType();
        }

        // 加载更多尾巴
        if (dataSize > 0 && hasLoadMoreFooter() && position == getCount() - 1) {
            return loadMoreItemFactory.getItemType();
        }

        return super.getItemViewType(position);
    }

    @Override
    @SuppressWarnings("unchecked")
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (itemFactoryList == null || itemFactoryList.size() == 0) {
            throw new IllegalStateException("You need to configure AssemblyItemFactory use addItemFactory method");
        }
        itemFactoryLocked = true;

        // 头
        int headerSize = getHeaderSize();
        int headerStartPosition = 0;
        int headerEndPosition = headerSize - 1;
        if (position >= headerStartPosition && position <= headerEndPosition && headerSize > 0) {
            //noinspection UnnecessaryLocalVariable
            int positionInHeaderList = position;
            ItemFactoryHolder itemFactoryHolder = headerItemList.get(positionInHeaderList);

            if (convertView == null) {
                AssemblyItem assemblyItem = itemFactoryHolder.itemFactory.createAssemblyItem(parent);
                convertView = assemblyItem.getItemView();
            }

            AssemblyItem assemblyItem = (AssemblyItem) convertView.getTag();
            assemblyItem.setPositionInList(position);
            assemblyItem.setData(positionInHeaderList, itemFactoryHolder.data);
            return convertView;
        }

        // 数据
        int dataSize = getDataSize();
        int dataStartPosition = headerEndPosition + 1;
        int dataEndPosition = headerEndPosition + dataSize;
        if (position >= dataStartPosition && position <= dataEndPosition && dataSize > 0) {
            int positionInDataList = position - headerSize;
            Object itemObject = dataList.get(positionInDataList);

            for (AssemblyItemFactory itemFactory : itemFactoryList) {
                if (!itemFactory.isTarget(itemObject)) {
                    continue;
                }

                if (convertView == null) {
                    AssemblyItem assemblyItem = itemFactory.createAssemblyItem(parent);
                    convertView = assemblyItem.getItemView();
                }

                AssemblyItem assemblyItem = (AssemblyItem) convertView.getTag();
                assemblyItem.setPositionInList(position);
                assemblyItem.setData(positionInDataList, itemObject);
                return convertView;
            }

            throw new IllegalStateException("Didn't find suitable AssemblyItemFactory. position=" + positionInDataList + ", itemObject=" + (itemObject != null ? itemObject.getClass().getName() : "null"));
        }

        // 尾巴
        int footerSize = getFooterSize();
        int footerStartPosition = dataEndPosition + 1;
        int footerEndPosition = dataEndPosition + footerSize;
        if (position >= footerStartPosition && position <= footerEndPosition && footerSize > 0) {
            int positionInFooterList = position - headerSize - dataSize;
            ItemFactoryHolder itemFactoryHolder = footerItemList.get(positionInFooterList);

            if (convertView == null) {
                AssemblyItem assemblyItem = itemFactoryHolder.itemFactory.createAssemblyItem(parent);
                convertView = assemblyItem.getItemView();
            }

            AssemblyItem assemblyItem = (AssemblyItem) convertView.getTag();
            assemblyItem.setPositionInList(position);
            assemblyItem.setData(positionInFooterList, itemFactoryHolder.data);
            return convertView;
        }

        // 加载更多尾巴
        if (dataSize > 0 && hasLoadMoreFooter() && position == getCount() - 1) {
            if (convertView == null) {
                AssemblyItem assemblyItem = loadMoreItemFactory.createAssemblyItem(parent);
                convertView = assemblyItem.getItemView();
            }

            int positionInLoadMore = 0;
            loadMoreItem = (AssemblyLoadMoreItemFactory.AssemblyLoadMoreItem) convertView.getTag();
            loadMoreItem.setPositionInList(position);
            loadMoreItem.setData(positionInLoadMore, null);
            return convertView;
        }

        throw new IllegalStateException("weird position is" + position);
    }

    private static class ItemFactoryHolder {
        private AssemblyItemFactory itemFactory;
        private Object data;

        public ItemFactoryHolder(AssemblyItemFactory itemFactory, Object data) {
            this.data = data;
            this.itemFactory = itemFactory;
        }
    }
}
