package me.panpf.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import me.panpf.adapter.more.MoreItemFactory;
import me.panpf.adapter.more.MoreItemHolder;

public class ItemStorage {

    @NonNull
    private AssemblyAdapter adapter;
    @NonNull
    private ViewTypeManager viewTypeManager = new ViewTypeManager();

    @NonNull
    private ItemHolderManager headerItemHolderManager = new ItemHolderManager();
    @NonNull
    private ArrayList<ItemFactory> itemFactoryList = new ArrayList<>();
    @NonNull
    private ItemHolderManager footerItemHolderManager = new ItemHolderManager();
    @Nullable
    private MoreItemHolder moreItemHolder;

    @Nullable
    private List dataList;

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
        if (itemFactory == null || viewTypeManager.isLocked()) {
            throw new IllegalArgumentException("itemFactory is null or item factory list locked");
        }

        itemFactoryList.add(itemFactory);
        int viewType = viewTypeManager.add(itemFactory);

        itemFactory.attachToAdapter(adapter, viewType);
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
        return itemFactoryList.size();
    }


    /* ************************ 头部 ItemFactory *************************** */

    /**
     * 添加一个将按添加顺序显示在列表头部的 {@link ItemFactory}
     */
    @NonNull
    public <DATA> ItemHolder<DATA> addHeaderItem(@NonNull ItemFactory<DATA> itemFactory, @Nullable DATA data) {
        //noinspection ConstantConditions
        if (itemFactory == null || viewTypeManager.isLocked()) {
            throw new IllegalArgumentException("itemFactory is null or item factory list locked");
        }

        ItemHolder<DATA> itemHolder = new ItemHolder<>(itemFactory, data);
        int viewType = viewTypeManager.add(itemHolder);
        headerItemHolderManager.add(itemHolder);

        itemFactory.attachToAdapter(adapter, viewType);
        itemHolder.attachToAdapter(this, true);
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
        if (itemHolder == null || viewTypeManager.isLocked()) {
            throw new IllegalArgumentException("itemHolder is null or item factory list locked");
        }
        if (itemHolder.getItemStorage() != null) {
            throw new IllegalArgumentException("Cannot be added repeatedly");
        }

        int viewType = viewTypeManager.add(itemHolder);
        headerItemHolderManager.add(itemHolder);

        ItemFactory itemFactory = itemHolder.getItemFactory();
        itemFactory.attachToAdapter(adapter, viewType);
        itemHolder.attachToAdapter(this, true);
        return itemHolder;
    }

    /**
     * header 状态变化处理，不可用时从 header 列表中移除，可用时加回 header 列表中，并根据 position 排序来恢复其原本所在的位置
     */
    void headerEnabledChanged(@NonNull ItemHolder itemHolder) {
        //noinspection ConstantConditions
        if (itemHolder == null || itemHolder.getItemFactory().getAdapter() != adapter) {
            return;
        }

        if (headerItemHolderManager.itemHolderEnabledChanged(itemHolder) && notifyOnChange) {
            adapter.notifyDataSetChanged();
        }
    }

    /**
     * 获取 header 列表
     */
    @NonNull
    public ItemHolderManager getHeaderItemHolderManager() {
        return headerItemHolderManager;
    }


    /* ************************ 尾巴 ItemFactory *************************** */

    /**
     * 添加一个将按添加顺序显示在列表尾部的 {@link ItemFactory}
     */
    @NonNull
    public <DATA> ItemHolder<DATA> addFooterItem(@NonNull ItemFactory<DATA> itemFactory, @Nullable DATA data) {
        //noinspection ConstantConditions
        if (itemFactory == null || viewTypeManager.isLocked()) {
            throw new IllegalArgumentException("itemFactory is null or item factory list locked");
        }

        ItemHolder<DATA> itemHolder = new ItemHolder<>(itemFactory, data);
        int viewType = viewTypeManager.add(itemHolder);
        footerItemHolderManager.add(itemHolder);

        itemFactory.attachToAdapter(adapter, viewType);
        itemHolder.attachToAdapter(this, false);
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
        if (itemHolder == null || viewTypeManager.isLocked()) {
            throw new IllegalArgumentException("itemHolder is null or item factory list locked");
        }
        if (itemHolder.getItemStorage() != null) {
            throw new IllegalArgumentException("Cannot be added repeatedly");
        }

        int viewType = viewTypeManager.add(itemHolder);
        footerItemHolderManager.add(itemHolder);

        ItemFactory itemFactory = itemHolder.getItemFactory();
        itemFactory.attachToAdapter(adapter, viewType);
        itemHolder.attachToAdapter(this, false);
        return itemHolder;
    }

    /**
     * footer 状态变化处理，不可用时从 footer 列表中移除，可用时加回 footer 列表中，并根据 position 排序来恢复其原本所在的位置
     */
    void footerEnabledChanged(@NonNull ItemHolder itemHolder) {
        //noinspection ConstantConditions
        if (itemHolder == null || itemHolder.getItemFactory().getAdapter() != adapter) {
            return;
        }

        if (footerItemHolderManager.itemHolderEnabledChanged(itemHolder) && notifyOnChange) {
            adapter.notifyDataSetChanged();
        }
    }

    /**
     * 获取 footer 列表
     */
    @NonNull
    public ItemHolderManager getFooterItemHolderManager() {
        return footerItemHolderManager;
    }


    /* ************************ 加载更多 *************************** */

    /**
     * 设置一个将显示在列表最后（在 footer 的后面）的加载更多尾巴
     */
    @NonNull
    public <DATA> MoreItemHolder<DATA> setMoreItem(@NonNull MoreItemFactory<DATA> itemFactory, @Nullable DATA data) {
        //noinspection ConstantConditions
        if (itemFactory == null || viewTypeManager.isLocked()) {
            throw new IllegalArgumentException("itemFactory is null or item factory list locked");
        }

        if (this.moreItemHolder != null) {
            throw new IllegalStateException("MoreItem cannot be set repeatedly");
        }

        itemFactory.loadMoreFinished(false);
        MoreItemHolder<DATA> moreItemHolder = new MoreItemHolder<>(itemFactory, data);
        int viewType = viewTypeManager.add(moreItemHolder);

        itemFactory.attachToAdapter(adapter, viewType);
        ((ItemHolder) moreItemHolder).attachToAdapter(this, false);
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
    public <DATA> MoreItemHolder<DATA> setMoreItem(@NonNull MoreItemHolder<DATA> moreItemHolder) {
        //noinspection ConstantConditions
        if (moreItemHolder == null || viewTypeManager.isLocked()) {
            throw new IllegalArgumentException("itemHolder is null or item factory list locked");
        }

        if (this.moreItemHolder != null) {
            throw new IllegalStateException("MoreItem cannot be set repeatedly");
        }

        int viewType = viewTypeManager.add(moreItemHolder);
        MoreItemFactory itemFactory = moreItemHolder.getItemFactory();
        itemFactory.loadMoreFinished(false);

        itemFactory.attachToAdapter(adapter, viewType);
        ((ItemHolder) moreItemHolder).attachToAdapter(this, false);
        this.moreItemHolder = moreItemHolder;
        return moreItemHolder;
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
        synchronized (this) {
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
        if (collection == null || collection.size() == 0) {
            return;
        }
        synchronized (this) {
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
        if (items == null || items.length == 0) {
            return;
        }
        synchronized (this) {
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
        synchronized (this) {
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
        synchronized (this) {
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
        synchronized (this) {
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
        synchronized (this) {
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
        // 只要访问了 getViewTypeCount() 方法就认为开始显示了，需要锁定 itemFactory 列表，因为 ListView 不允许 getViewTypeCount() 改变
        if (!viewTypeManager.isLocked()) {
            viewTypeManager.lock();
        }
        // 1 来自 BaseAdapter.getViewTypeCount()
        return viewTypeManager.getCount();
    }

    /**
     * 根据 view 类型获取 {@link ItemFactory} 或 {@link ItemHolder}
     *
     * @param viewType view 类型
     * @return null：没有；{@link ItemFactory} 或 {@link ItemHolder}
     */
    @Nullable
    public ItemFactory getItemFactoryByViewType(int viewType) {
        Object value = viewTypeManager.get(viewType);
        if (value instanceof ItemFactory) {
            return (ItemFactory) value;
        } else if (value instanceof ItemHolder) {
            return ((ItemHolder) value).getItemFactory();
        } else if (value != null) {
            throw new IllegalArgumentException("Unknown viewType value. viewType=" + viewType + ", value=" + value);
        } else {
            return null;
        }
    }
}
