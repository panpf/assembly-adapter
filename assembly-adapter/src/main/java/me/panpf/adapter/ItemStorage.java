package me.panpf.adapter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.SparseArray;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import me.panpf.adapter.more.LoadMoreFixedItemInfo;
import me.panpf.adapter.more.LoadMoreItemFactoryBridle;

@SuppressWarnings({"unused", "WeakerAccess"})
public class ItemStorage {

    @NonNull
    private final Object headerItemListLock = new Object();
    @NonNull
    private final Object itemListLock = new Object();
    @NonNull
    private final Object itemFactoryListLock = new Object();
    @NonNull
    private final Object footerItemListLock = new Object();

    @NonNull
    private AssemblyAdapter adapter;

    private int itemTypeIndex = 0;
    private int headerItemPosition;
    private int footerItemPosition;
    private boolean itemFactoryLocked;

    @Nullable
    private ArrayList<FixedItemInfo> headerItemList;
    @Nullable
    private List dataList;
    @Nullable
    private ArrayList<FixedItemInfo> footerItemList;
    @Nullable
    private LoadMoreFixedItemInfo loadMoreFixedItemInfo;

    @Nullable
    private ArrayList<ItemFactory> itemFactoryList;
    @Nullable
    private SparseArray<Object> itemFactoryArray;

    private boolean notifyOnChange = true;

    public ItemStorage(@NonNull AssemblyAdapter adapter) {
        this.adapter = adapter;
    }

    public ItemStorage(@NonNull AssemblyAdapter adapter, @Nullable List dataList) {
        this.adapter = adapter;
        this.dataList = dataList;
    }

    public ItemStorage(@NonNull AssemblyAdapter adapter, @Nullable Object[] dataArray) {
        this.adapter = adapter;
        if (dataArray != null && dataArray.length > 0) {
            this.dataList = new ArrayList(dataArray.length);
            Collections.addAll(dataList, dataArray);
        }
    }

    @NonNull
    public AssemblyAdapter getAdapter() {
        return adapter;
    }


    /* ************************ 数据 ItemFactory *************************** */

    /**
     * 添加一个用来处理并显示 dataList 中的数据的 {@link ItemFactory}
     */
    public void addItemFactory(@NonNull ItemFactory itemFactory) {
        //noinspection ConstantConditions
        if (itemFactory == null || itemFactoryLocked) {
            throw new IllegalArgumentException("itemFactory is null or item factory list locked");
        }

        itemFactory.setAdapter(adapter);
        itemFactory.setItemType(itemTypeIndex++);

        if (itemFactoryArray == null) {
            itemFactoryArray = new SparseArray<Object>();
        }
        itemFactoryArray.put(itemFactory.getItemType(), itemFactory);

        synchronized (itemFactoryListLock) {
            if (itemFactoryList == null) {
                itemFactoryList = new ArrayList<ItemFactory>(5);
            }
            itemFactoryList.add(itemFactory);
        }
    }

    /**
     * 获取数据  {@link ItemFactory} 列表
     */
    @Nullable
    public List<ItemFactory> getItemFactoryList() {
        return itemFactoryList;
    }

    /**
     * 获取数据  {@link ItemFactory} 的个数
     */
    public int getItemFactoryCount() {
        return itemFactoryList != null ? itemFactoryList.size() : 0;
    }


    /* ************************ 头部 ItemFactory *************************** */

    /**
     * 添加一个将按添加顺序显示在列表头部的 {@link ItemFactory}
     */
    @NonNull
    public FixedItemInfo addHeaderItem(@NonNull ItemFactory itemFactory, @Nullable Object data) {
        //noinspection ConstantConditions
        if (itemFactory == null || itemFactoryLocked) {
            throw new IllegalArgumentException("itemFactory is null or item factory list locked");
        }

        itemFactory.setAdapter(adapter);
        itemFactory.setItemType(itemTypeIndex++);

        FixedItemInfo fixedItemInfo = new FixedItemInfo(itemFactory, data, true);
        fixedItemInfo.setPosition(headerItemPosition++);

        if (itemFactoryArray == null) {
            itemFactoryArray = new SparseArray<Object>();
        }
        itemFactoryArray.put(itemFactory.getItemType(), fixedItemInfo);

        synchronized (headerItemListLock) {
            if (headerItemList == null) {
                headerItemList = new ArrayList<FixedItemInfo>(1);
            }
            headerItemList.add(fixedItemInfo);
        }

        return fixedItemInfo;
    }

    /**
     * header 状态变化处理，不可用时从 header 列表中移除，可用时加回 header 列表中，并根据 position 排序来恢复其原本所在的位置
     */
    public void headerEnabledChanged(@NonNull FixedItemInfo fixedItemInfo) {
        //noinspection ConstantConditions
        if (fixedItemInfo == null || fixedItemInfo.getItemFactory().getAdapter() != this) {
            return;
        }

        if (fixedItemInfo.isEnabled()) {
            synchronized (headerItemListLock) {
                if (headerItemList == null) {
                    headerItemList = new ArrayList<FixedItemInfo>(1);
                }
                headerItemList.add(fixedItemInfo);
                Collections.sort(headerItemList, new Comparator<FixedItemInfo>() {
                    @Override
                    public int compare(FixedItemInfo lhs, FixedItemInfo rhs) {
                        return lhs.getPosition() - rhs.getPosition();
                    }
                });
            }

            if (notifyOnChange) {
                adapter.notifyDataSetChanged();
            }
        } else {
            synchronized (headerItemListLock) {
                if (headerItemList != null && headerItemList.remove(fixedItemInfo)) {
                    if (notifyOnChange) {
                        adapter.notifyDataSetChanged();
                    }
                }
            }
        }
    }

    /**
     * 获取 header 列表
     */
    @Nullable
    public List<FixedItemInfo> getHeaderItemList() {
        return headerItemList;
    }

    /**
     * 获取列表头的个数
     */
    public int getHeaderItemCount() {
        return headerItemList != null ? headerItemList.size() : 0;
    }

    @Nullable
    public Object getHeaderData(int positionInHeaderList) {
        return headerItemList != null ? headerItemList.get(positionInHeaderList).getData() : null;
    }


    /* ************************ 尾巴 ItemFactory *************************** */

    /**
     * 添加一个将按添加顺序显示在列表尾部的 {@link ItemFactory}
     */
    @NonNull
    public FixedItemInfo addFooterItem(@NonNull ItemFactory itemFactory, @Nullable Object data) {
        //noinspection ConstantConditions
        if (itemFactory == null || itemFactoryLocked) {
            throw new IllegalArgumentException("itemFactory is null or item factory list locked");
        }

        itemFactory.setAdapter(adapter);
        itemFactory.setItemType(itemTypeIndex++);

        FixedItemInfo fixedItemInfo = new FixedItemInfo(itemFactory, data, false);
        fixedItemInfo.setPosition(footerItemPosition++);

        if (itemFactoryArray == null) {
            itemFactoryArray = new SparseArray<Object>();
        }
        itemFactoryArray.put(itemFactory.getItemType(), fixedItemInfo);

        synchronized (footerItemListLock) {
            if (footerItemList == null) {
                footerItemList = new ArrayList<FixedItemInfo>(1);
            }
            footerItemList.add(fixedItemInfo);
        }

        return fixedItemInfo;
    }

    /**
     * footer 状态变化处理，不可用时从 footer 列表中移除，可用时加回 footer 列表中，并根据 position 排序来恢复其原本所在的位置
     */
    public void footerEnabledChanged(@NonNull FixedItemInfo fixedItemInfo) {
        //noinspection ConstantConditions
        if (fixedItemInfo == null || fixedItemInfo.getItemFactory().getAdapter() != this) {
            return;
        }

        if (fixedItemInfo.isEnabled()) {
            synchronized (footerItemListLock) {
                if (footerItemList == null) {
                    footerItemList = new ArrayList<FixedItemInfo>(1);
                }
                footerItemList.add(fixedItemInfo);
                Collections.sort(footerItemList, new Comparator<FixedItemInfo>() {
                    @Override
                    public int compare(FixedItemInfo lhs, FixedItemInfo rhs) {
                        return lhs.getPosition() - rhs.getPosition();
                    }
                });
            }

            if (notifyOnChange) {
                adapter.notifyDataSetChanged();
            }
        } else {
            synchronized (footerItemListLock) {
                if (footerItemList != null && footerItemList.remove(fixedItemInfo)) {
                    if (notifyOnChange) {
                        adapter.notifyDataSetChanged();
                    }
                }
            }
        }
    }

    /**
     * 获取 footer 列表
     */
    @Nullable
    public List<FixedItemInfo> getFooterItemList() {
        return footerItemList;
    }

    /**
     * 获取列表尾的个数
     */
    public int getFooterItemCount() {
        return footerItemList != null ? footerItemList.size() : 0;
    }

    @Nullable
    public Object getFooterData(int positionInFooterList) {
        return footerItemList != null ? footerItemList.get(positionInFooterList).getData() : null;
    }


    /* ************************ 加载更多 *************************** */

    /**
     * 设置一个将显示在列表最后（在 footer 的后面）的加载更多尾巴
     */
    @NonNull
    public LoadMoreFixedItemInfo setLoadMoreItem(@NonNull LoadMoreItemFactoryBridle itemFactory, @Nullable Object data) {
        //noinspection ConstantConditions
        if (itemFactory == null || itemFactoryLocked) {
            throw new IllegalArgumentException("itemFactory is null or item factory list locked");
        }

        itemFactory.setAdapter(adapter);
        if (this.loadMoreFixedItemInfo != null) {
            itemFactory.setItemType(this.loadMoreFixedItemInfo.getItemFactory().getItemType());
        } else {
            itemFactory.setItemType(itemTypeIndex++);
        }

        itemFactory.loadMoreFinished(false);
        LoadMoreFixedItemInfo loadMoreFixedItemInfo = new LoadMoreFixedItemInfo(itemFactory, data, false);

        if (itemFactoryArray == null) {
            itemFactoryArray = new SparseArray<Object>();
        }
        itemFactoryArray.put(itemFactory.getItemType(), loadMoreFixedItemInfo);

        return this.loadMoreFixedItemInfo = loadMoreFixedItemInfo;
    }

    /**
     * 设置一个将显示在列表最后（在 footer 的后面）的加载更多尾巴
     */
    @NonNull
    public LoadMoreFixedItemInfo setLoadMoreItem(@NonNull LoadMoreItemFactoryBridle itemFactory) {
        return setLoadMoreItem(itemFactory, null);
    }

    /**
     * 设置禁用加载更多
     */
    public void setDisableLoadMore(boolean disableLoadMore) {
        if (loadMoreFixedItemInfo != null) {
            loadMoreFixedItemInfo.setEnabled(!disableLoadMore);
        }
    }

    /**
     * 是否有加载更多尾巴
     */
    public boolean hasLoadMoreFooter() {
        return loadMoreFixedItemInfo != null && loadMoreFixedItemInfo.isEnabled();
    }

    /**
     * 加载更多完成时调用
     *
     * @param loadMoreEnd 全部加载完毕，为 true 会显示结束的文案并且不再触发加载更多
     */
    public void loadMoreFinished(boolean loadMoreEnd) {
        if (loadMoreFixedItemInfo != null) {
            loadMoreFixedItemInfo.loadMoreFinished(loadMoreEnd);
        }
    }

    /**
     * 加载更多失败的时候调用此方法显示错误提示，并可点击重新加载
     */
    public void loadMoreFailed() {
        if (loadMoreFixedItemInfo != null) {
            loadMoreFixedItemInfo.loadMoreFailed();
        }
    }


    /* ************************ 数据列表 *************************** */

    /**
     * 获取数据列表
     */
    @Nullable
    public List getDataList() {
        return dataList;
    }

    /**
     * 设置数据列表
     */
    public void setDataList(@Nullable List dataList) {
        synchronized (itemListLock) {
            this.dataList = dataList;
        }

        if (notifyOnChange) {
            adapter.notifyDataSetChanged();
        }
    }

    /**
     * 批量添加数据
     */
    public void addAll(@Nullable Collection collection) {
        //noinspection ConstantConditions
        if (collection == null || collection.size() == 0) {
            return;
        }
        synchronized (itemListLock) {
            if (dataList == null) {
                dataList = new ArrayList(collection.size());
            }
            //noinspection unchecked
            dataList.addAll(collection);
        }

        if (notifyOnChange) {
            adapter.notifyDataSetChanged();
        }
    }

    /**
     * 批量添加数据
     */
    public void addAll(@Nullable Object... items) {
        //noinspection ConstantConditions
        if (items == null || items.length == 0) {
            return;
        }
        synchronized (itemListLock) {
            if (dataList == null) {
                dataList = new ArrayList(items.length);
            }
            Collections.addAll(dataList, items);
        }

        if (notifyOnChange) {
            adapter.notifyDataSetChanged();
        }
    }

    /**
     * 插入一条数据
     */
    public void insert(@Nullable Object object, int index) {
        if (object == null) {
            return;
        }
        synchronized (itemListLock) {
            if (dataList == null) {
                dataList = new ArrayList();
            }
            //noinspection unchecked
            dataList.add(index, object);
        }

        if (notifyOnChange) {
            adapter.notifyDataSetChanged();
        }
    }

    /**
     * 删除一条数据
     */
    public void remove(@NonNull Object object) {
        //noinspection ConstantConditions
        if (object == null) {
            return;
        }
        synchronized (itemListLock) {
            if (dataList != null) {
                dataList.remove(object);
            }
        }

        if (notifyOnChange) {
            adapter.notifyDataSetChanged();
        }
    }

    /**
     * 清空数据
     */
    public void clear() {
        synchronized (itemListLock) {
            if (dataList != null) {
                dataList.clear();
            }
        }

        if (notifyOnChange) {
            adapter.notifyDataSetChanged();
        }
    }

    /**
     * 对数据排序
     */
    public void sort(@NonNull Comparator comparator) {
        synchronized (itemListLock) {
            if (dataList != null) {
                Collections.sort(dataList, comparator);
            }
        }

        if (notifyOnChange) {
            adapter.notifyDataSetChanged();
        }
    }

    /**
     * 获取数据列表的长度
     */
    public int getDataCount() {
        return dataList != null ? dataList.size() : 0;
    }

    @Nullable
    public Object getData(int positionInDataList) {
        return dataList != null ? dataList.get(positionInDataList) : null;
    }


    /* ************************ 完整列表 *************************** */

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

    @Nullable
    public Object getItem(int position) {
        // 头
        int headerItemCount = getHeaderItemCount();
        int headerStartPosition = 0;
        int headerEndPosition = headerItemCount - 1;
        if (position >= headerStartPosition && position <= headerEndPosition && headerItemCount > 0) {
            //noinspection UnnecessaryLocalVariable
            int positionInHeaderList = position;
            return getHeaderData(positionInHeaderList);
        }

        // 数据
        int dataCount = getDataCount();
        int dataStartPosition = headerEndPosition + 1;
        int dataEndPosition = headerEndPosition + dataCount;
        if (position >= dataStartPosition && position <= dataEndPosition && dataCount > 0) {
            int positionInDataList = position - headerItemCount;
            return getData(positionInDataList);
        }

        // 尾巴
        int footerItemCount = getFooterItemCount();
        int footerStartPosition = dataEndPosition + 1;
        int footerEndPosition = dataEndPosition + footerItemCount;
        if (position >= footerStartPosition && position <= footerEndPosition && footerItemCount > 0) {
            int positionInFooterList = position - headerItemCount - dataCount;
            return getFooterData(positionInFooterList);
        }

        // 加载更多尾巴
        if (dataCount > 0 && hasLoadMoreFooter() && position == getItemCount() - 1) {
            return loadMoreFixedItemInfo != null ? loadMoreFixedItemInfo.getData() : null;
        }

        return null;
    }

    /**
     * 获取在各自区域的位置
     */
    public int getPositionInPart(int position) {
        // 头
        int headerItemCount = getHeaderItemCount();
        int headerStartPosition = 0;
        int headerEndPosition = headerItemCount - 1;
        if (position >= headerStartPosition && position <= headerEndPosition && headerItemCount > 0) {
            return position;
        }

        // 数据
        int dataCount = getDataCount();
        int dataStartPosition = headerEndPosition + 1;
        int dataEndPosition = headerEndPosition + dataCount;
        if (position >= dataStartPosition && position <= dataEndPosition && dataCount > 0) {
            return position - headerItemCount;
        }

        // 尾巴
        int footerItemCount = getFooterItemCount();
        int footerStartPosition = dataEndPosition + 1;
        int footerEndPosition = dataEndPosition + footerItemCount;
        if (position >= footerStartPosition && position <= footerEndPosition && footerItemCount > 0) {
            return position - headerItemCount - dataCount;
        }

        // 加载更多尾巴
        if (dataCount > 0 && hasLoadMoreFooter() && position == getItemCount() - 1) {
            return 0;
        }

        throw new IllegalArgumentException("illegal position: " + position);
    }


    /* ************************ 其它 *************************** */

    /**
     * 数据变更时是否立即刷新列表
     */
    public boolean isNotifyOnChange() {
        return notifyOnChange;
    }

    /**
     * 设置当数据发生改变时是否立即调用 notifyDataSetChanged() 刷新列表，默认 true。
     * 当你需要连续多次修改数据的时候，你应该将 notifyOnChange 设为 false，然后在最后主动调用 notifyDataSetChanged() 刷新列表，最后再将 notifyOnChange 设为 true
     */
    public void setNotifyOnChange(boolean notifyOnChange) {
        this.notifyOnChange = notifyOnChange;
    }

    /**
     * 获取指定位置占几列
     */
    public int getSpanSize(int position) {
        // 头
        int headerItemCount = getHeaderItemCount();
        int headerStartPosition = 0;
        int headerEndPosition = headerItemCount - 1;
        if (headerItemList != null && position >= headerStartPosition && position <= headerEndPosition && headerItemCount > 0) {
            //noinspection UnnecessaryLocalVariable
            int positionInHeaderList = position;
            return headerItemList.get(positionInHeaderList).getItemFactory().getSpanSize();
        }

        // 数据
        int dataCount = getDataCount();
        int dataStartPosition = headerEndPosition + 1;
        int dataEndPosition = headerEndPosition + dataCount;
        if (itemFactoryList != null && position >= dataStartPosition && position <= dataEndPosition && dataCount > 0) {
            int positionInDataList = position - headerItemCount;
            Object dataObject = getData(positionInDataList);

            ItemFactory itemFactory;
            for (int w = 0, size = itemFactoryList.size(); w < size; w++) {
                itemFactory = itemFactoryList.get(w);
                if (itemFactory.isTarget(dataObject)) {
                    return itemFactory.getSpanSize();
                }
            }

            throw new IllegalStateException(String.format(
                    "Didn't find suitable ItemFactory. positionInDataList=%d, dataObject=%s",
                    positionInDataList, dataObject != null ? dataObject.getClass().getName() : "null"));
        }

        // 尾巴
        int footerItemCount = getFooterItemCount();
        int footerStartPosition = dataEndPosition + 1;
        int footerEndPosition = dataEndPosition + footerItemCount;
        if (footerItemList != null && position >= footerStartPosition && position <= footerEndPosition && footerItemCount > 0) {
            int positionInFooterList = position - headerItemCount - dataCount;
            return footerItemList.get(positionInFooterList).getItemFactory().getSpanSize();
        }

        // 加载更多尾巴
        if (loadMoreFixedItemInfo != null && dataCount > 0 && hasLoadMoreFooter() && position == getItemCount() - 1) {
            return loadMoreFixedItemInfo.getItemFactory().getSpanSize();
        }

        return 1;
    }

    public int getViewTypeCount() {
        // 只要访问了 getViewTypeCount() 方法就认为开始显示了，需要锁定 itemFactory 列表
        itemFactoryLocked = true;
        // 1 来自 BaseAdapter.getViewTypeCount()
        return itemTypeIndex > 0 ? itemTypeIndex : 1;
    }

    public int getItemViewType(int position) {
        // 只要访问了 getItemViewType(int) 方法就认为开始显示了，需要锁定 itemFactory 列表
        itemFactoryLocked = true;

        int headerItemCount = getHeaderItemCount();
        int headerStartPosition = 0;
        int headerEndPosition = headerItemCount - 1;

        // 头
        if (headerItemList != null && position >= headerStartPosition && position <= headerEndPosition && headerItemCount > 0) {
            //noinspection UnnecessaryLocalVariable
            int positionInHeaderList = position;
            return headerItemList.get(positionInHeaderList).getItemFactory().getItemType();
        }

        // 数据
        int dataCount = getDataCount();
        int dataStartPosition = headerEndPosition + 1;
        int dataEndPosition = headerEndPosition + dataCount;
        if (itemFactoryList != null && position >= dataStartPosition && position <= dataEndPosition && dataCount > 0) {
            int positionInDataList = position - headerItemCount;
            Object dataObject = getData(positionInDataList);

            ItemFactory itemFactory;
            for (int w = 0, size = itemFactoryList.size(); w < size; w++) {
                itemFactory = itemFactoryList.get(w);
                if (itemFactory.isTarget(dataObject)) {
                    return itemFactory.getItemType();
                }
            }

            throw new IllegalStateException(String.format(
                    "Didn't find suitable ItemFactory. positionInDataList=%d, dataObject=%s",
                    positionInDataList, dataObject != null ? dataObject.toString() : "null"));
        }

        // 尾巴
        int footerItemCount = getFooterItemCount();
        int footerStartPosition = dataEndPosition + 1;
        int footerEndPosition = dataEndPosition + footerItemCount;
        if (footerItemList != null && position >= footerStartPosition && position <= footerEndPosition && footerItemCount > 0) {
            int positionInFooterList = position - headerItemCount - dataCount;
            return footerItemList.get(positionInFooterList).getItemFactory().getItemType();
        }

        // 加载更多尾巴
        if (loadMoreFixedItemInfo != null && dataCount > 0 && hasLoadMoreFooter() && position == getItemCount() - 1) {
            return loadMoreFixedItemInfo.getItemFactory().getItemType();
        }

        throw new IllegalStateException("Not found match viewType, position: " + position);
    }

    /**
     * 根据 view 类型获取 {@link ItemFactory} 或 {@link FixedItemInfo}
     *
     * @param viewType view 类型，参见 {@link #getItemViewType(int)} 方法
     * @return null：没有；{@link ItemFactory} 或 {@link FixedItemInfo}
     */
    @Nullable
    public Object getItemFactoryByViewType(int viewType) {
        return itemFactoryArray != null ? itemFactoryArray.get(viewType) : null;
    }
}
