package me.panpf.adapter;

import android.util.SparseArray;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import me.panpf.adapter.more.MoreItemFactory;
import me.panpf.adapter.more.MoreItemHolder;

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
    private ArrayList<ItemHolder> headerItemList;
    @Nullable
    private List dataList;
    @Nullable
    private ArrayList<ItemHolder> footerItemList;
    @Nullable
    private MoreItemHolder moreItemHolder;

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
    public <DATA> ItemHolder<DATA> addHeaderItem(@NonNull ItemFactory<DATA> itemFactory, @Nullable DATA data) {
        //noinspection ConstantConditions
        if (itemFactory == null || itemFactoryLocked) {
            throw new IllegalArgumentException("itemFactory is null or item factory list locked");
        }

        itemFactory.setAdapter(adapter);
        itemFactory.setItemType(itemTypeIndex++);

        ItemHolder<DATA> itemHolder = new ItemHolder<DATA>(this, itemFactory, data, true);
        itemHolder.setPosition(headerItemPosition++);

        if (itemFactoryArray == null) {
            itemFactoryArray = new SparseArray<Object>();
        }
        itemFactoryArray.put(itemFactory.getItemType(), itemHolder);

        synchronized (headerItemListLock) {
            if (headerItemList == null) {
                headerItemList = new ArrayList<ItemHolder>(1);
            }
            headerItemList.add(itemHolder);
        }

        return itemHolder;
    }

    @NonNull
    public <DATA> ItemHolder<DATA> addHeaderItem(@NonNull ItemFactory<DATA> itemFactory) {
        return addHeaderItem(itemFactory, null);
    }

    /**
     * 添加一个将按添加顺序显示在列表头部的 {@link ItemFactory}
     */
    @NonNull
    public <DATA> ItemHolder<DATA> addHeaderItem(@NonNull ItemHolder<DATA> itemHolder) {
        //noinspection ConstantConditions
        if (itemHolder == null || itemFactoryLocked) {
            throw new IllegalArgumentException("itemHolder is null or item factory list locked");
        }
        //noinspection ConstantConditions
        if (itemHolder.getItemStorage() != null) {
            throw new IllegalArgumentException("Cannot be added repeatedly");
        }

        itemHolder.getItemFactory().setAdapter(adapter);
        itemHolder.getItemFactory().setItemType(itemTypeIndex++);

        itemHolder.setItemStorage(this);
        itemHolder.setHeader(true);
        itemHolder.setPosition(headerItemPosition++);

        if (itemFactoryArray == null) {
            itemFactoryArray = new SparseArray<Object>();
        }
        itemFactoryArray.put(itemHolder.getItemFactory().getItemType(), itemHolder);

        synchronized (headerItemListLock) {
            if (headerItemList == null) {
                headerItemList = new ArrayList<ItemHolder>(1);
            }
            headerItemList.add(itemHolder);
        }

        return itemHolder;
    }

    /**
     * header 状态变化处理，不可用时从 header 列表中移除，可用时加回 header 列表中，并根据 position 排序来恢复其原本所在的位置
     */
    public void headerEnabledChanged(@NonNull ItemHolder itemHolder) {
        //noinspection ConstantConditions
        if (itemHolder == null || itemHolder.getItemFactory().getAdapter() != adapter) {
            return;
        }

        if (itemHolder.isEnabled()) {
            synchronized (headerItemListLock) {
                if (headerItemList == null) {
                    headerItemList = new ArrayList<ItemHolder>(1);
                }
                headerItemList.add(itemHolder);
                Collections.sort(headerItemList, new Comparator<ItemHolder>() {
                    @Override
                    public int compare(ItemHolder lhs, ItemHolder rhs) {
                        return lhs.getPosition() - rhs.getPosition();
                    }
                });
            }

            if (notifyOnChange) {
                adapter.notifyDataSetChanged();
            }
        } else {
            synchronized (headerItemListLock) {
                if (headerItemList != null && headerItemList.remove(itemHolder)) {
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
    public List<ItemHolder> getHeaderItemList() {
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
    public <DATA> ItemHolder<DATA> addFooterItem(@NonNull ItemFactory<DATA> itemFactory, @Nullable DATA data) {
        //noinspection ConstantConditions
        if (itemFactory == null || itemFactoryLocked) {
            throw new IllegalArgumentException("itemFactory is null or item factory list locked");
        }

        itemFactory.setAdapter(adapter);
        itemFactory.setItemType(itemTypeIndex++);

        ItemHolder<DATA> itemHolder = new ItemHolder<DATA>(this, itemFactory, data, false);
        itemHolder.setPosition(footerItemPosition++);

        if (itemFactoryArray == null) {
            itemFactoryArray = new SparseArray<Object>();
        }
        itemFactoryArray.put(itemFactory.getItemType(), itemHolder);

        synchronized (footerItemListLock) {
            if (footerItemList == null) {
                footerItemList = new ArrayList<ItemHolder>(1);
            }
            footerItemList.add(itemHolder);
        }

        return itemHolder;
    }

    @NonNull
    public <DATA> ItemHolder<DATA> addFooterItem(@NonNull ItemFactory<DATA> itemFactory) {
        return addFooterItem(itemFactory, null);
    }

    /**
     * 添加一个将按添加顺序显示在列表尾部的 {@link ItemFactory}
     */
    @NonNull
    public <DATA> ItemHolder<DATA> addFooterItem(@NonNull ItemHolder<DATA> itemHolder) {
        //noinspection ConstantConditions
        if (itemHolder == null || itemFactoryLocked) {
            throw new IllegalArgumentException("itemHolder is null or item factory list locked");
        }
        //noinspection ConstantConditions
        if (itemHolder.getItemStorage() != null) {
            throw new IllegalArgumentException("Cannot be added repeatedly");
        }

        itemHolder.getItemFactory().setAdapter(adapter);
        itemHolder.getItemFactory().setItemType(itemTypeIndex++);

        itemHolder.setItemStorage(this);
        itemHolder.setHeader(false);
        itemHolder.setPosition(footerItemPosition++);

        if (itemFactoryArray == null) {
            itemFactoryArray = new SparseArray<Object>();
        }
        itemFactoryArray.put(itemHolder.getItemFactory().getItemType(), itemHolder);

        synchronized (footerItemListLock) {
            if (footerItemList == null) {
                footerItemList = new ArrayList<ItemHolder>(1);
            }
            footerItemList.add(itemHolder);
        }

        return itemHolder;
    }

    /**
     * footer 状态变化处理，不可用时从 footer 列表中移除，可用时加回 footer 列表中，并根据 position 排序来恢复其原本所在的位置
     */
    public void footerEnabledChanged(@NonNull ItemHolder itemHolder) {
        //noinspection ConstantConditions
        if (itemHolder == null || itemHolder.getItemFactory().getAdapter() != adapter) {
            return;
        }

        if (itemHolder.isEnabled()) {
            synchronized (footerItemListLock) {
                if (footerItemList == null) {
                    footerItemList = new ArrayList<ItemHolder>(1);
                }
                footerItemList.add(itemHolder);
                Collections.sort(footerItemList, new Comparator<ItemHolder>() {
                    @Override
                    public int compare(ItemHolder lhs, ItemHolder rhs) {
                        return lhs.getPosition() - rhs.getPosition();
                    }
                });
            }

            if (notifyOnChange) {
                adapter.notifyDataSetChanged();
            }
        } else {
            synchronized (footerItemListLock) {
                if (footerItemList != null && footerItemList.remove(itemHolder)) {
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
    public List<ItemHolder> getFooterItemList() {
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
    public <DATA> MoreItemHolder<DATA> setMoreItem(@NonNull MoreItemFactory<DATA> itemFactory, @Nullable DATA data) {
        //noinspection ConstantConditions
        if (itemFactory == null || itemFactoryLocked) {
            throw new IllegalArgumentException("itemFactory is null or item factory list locked");
        }

        itemFactory.setAdapter(adapter);
        if (this.moreItemHolder != null) {
            itemFactory.setItemType(this.moreItemHolder.getItemFactory().getItemType());
        } else {
            itemFactory.setItemType(itemTypeIndex++);
        }

        itemFactory.loadMoreFinished(false);
        MoreItemHolder<DATA> moreItemHolder = new MoreItemHolder<DATA>(this, itemFactory, data, false);

        if (itemFactoryArray == null) {
            itemFactoryArray = new SparseArray<Object>();
        }
        itemFactoryArray.put(itemFactory.getItemType(), moreItemHolder);

        this.moreItemHolder = moreItemHolder;
        return moreItemHolder;
    }

    @NonNull
    public <DATA> MoreItemHolder<DATA> setMoreItem(@NonNull MoreItemFactory<DATA> itemFactory) {
        return setMoreItem(itemFactory, null);
    }

    /**
     * 设置一个将显示在列表最后（在 footer 的后面）的加载更多尾巴
     */
    @NonNull
    public <DATA> MoreItemHolder<DATA> setMoreItem(@NonNull MoreItemHolder<DATA> newItemHolder) {
        //noinspection ConstantConditions
        if (newItemHolder == null || itemFactoryLocked) {
            throw new IllegalArgumentException("itemHolder is null or item factory list locked");
        }

        newItemHolder.getItemFactory().setAdapter(adapter);
        if (this.moreItemHolder != null) {
            newItemHolder.getItemFactory().setItemType(this.moreItemHolder.getItemFactory().getItemType());
        } else {
            newItemHolder.getItemFactory().setItemType(itemTypeIndex++);
        }

        newItemHolder.getItemFactory().loadMoreFinished(false);
        ((ItemHolder) newItemHolder).setItemStorage(this);
        ((ItemHolder) newItemHolder).setHeader(false);

        if (itemFactoryArray == null) {
            itemFactoryArray = new SparseArray<Object>();
        }
        itemFactoryArray.put(newItemHolder.getItemFactory().getItemType(), newItemHolder);

        this.moreItemHolder = newItemHolder;
        return newItemHolder;
    }

    @Nullable
    public MoreItemHolder getMoreItemHolder() {
        return moreItemHolder;
    }

    /**
     * 是否有加载更多尾巴
     */
    public boolean hasMoreFooter() {
        return moreItemHolder != null && moreItemHolder.isEnabled();
    }

    /**
     * 设置禁用加载更多
     */
    public void setEnabledMoreItem(boolean enabledMoreItem) {
        if (moreItemHolder != null) {
            moreItemHolder.setEnabled(enabledMoreItem);
        }
    }

    /**
     * 加载更多完成时调用
     *
     * @param loadMoreEnd 全部加载完毕，为 true 会显示结束的文案并且不再触发加载更多
     */
    public void loadMoreFinished(boolean loadMoreEnd) {
        if (moreItemHolder != null) {
            moreItemHolder.loadMoreFinished(loadMoreEnd);
        }
    }

    /**
     * 加载更多失败的时候调用此方法显示错误提示，并可点击重新加载
     */
    public void loadMoreFailed() {
        if (moreItemHolder != null) {
            moreItemHolder.loadMoreFailed();
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

    public int getViewTypeCount() {
        // 只要访问了 getViewTypeCount() 方法就认为开始显示了，需要锁定 itemFactory 列表
        itemFactoryLocked = true;
        // 1 来自 BaseAdapter.getViewTypeCount()
        return itemTypeIndex > 0 ? itemTypeIndex : 1;
    }

    /**
     * 根据 view 类型获取 {@link ItemFactory} 或 {@link ItemHolder}
     *
     * @param viewType view 类型
     * @return null：没有；{@link ItemFactory} 或 {@link ItemHolder}
     */
    @Nullable
    public Object getItemFactoryByViewType(int viewType) {
        return itemFactoryArray != null ? itemFactoryArray.get(viewType) : null;
    }
}
