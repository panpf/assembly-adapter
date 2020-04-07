package me.panpf.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import me.panpf.adapter.more.MoreItemHolder;
import me.panpf.adapter.more.MoreItemFactory;

public class ItemManager {

    @NonNull
    private AssemblyAdapter adapter;
    @NonNull
    private ViewTypeManager viewTypeManager = new ViewTypeManager();
    @NonNull
    private ItemHolderManager headerItemManager = new ItemHolderManager();
    @NonNull
    private ArrayList<ItemFactory> itemFactoryList = new ArrayList<>();
    @NonNull
    private ItemHolderManager footerItemManager = new ItemHolderManager();
    @Nullable
    private MoreItemHolder moreItemHolder;

    @Nullable
    private List dataList;
    private boolean notifyOnChange = true;

    public ItemManager(@NonNull AssemblyAdapter adapter) {
        this.adapter = adapter;
    }

    public ItemManager(@NonNull AssemblyAdapter adapter, @Nullable List dataList) {
        this.adapter = adapter;
        this.dataList = dataList;
    }

    public ItemManager(@NonNull AssemblyAdapter adapter, @Nullable Object[] dataArray) {
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


    public void addItemFactory(@NonNull ItemFactory itemFactory) {
        //noinspection ConstantConditions
        if (itemFactory == null || viewTypeManager.isLocked()) {
            throw new IllegalArgumentException("itemFactory is null or item factory list locked");
        }

        itemFactoryList.add(itemFactory);
        int viewType = viewTypeManager.add(itemFactory);

        itemFactory.attachToAdapter(adapter, viewType);
    }


    @NonNull
    public <DATA> ItemHolder<DATA> addHeaderItem(@NonNull ItemFactory<DATA> itemFactory, @Nullable DATA data) {
        //noinspection ConstantConditions
        if (itemFactory == null || viewTypeManager.isLocked()) {
            throw new IllegalArgumentException("itemFactory is null or item factory list locked");
        }

        ItemHolder<DATA> itemHolder = new ItemHolder<>(itemFactory, data);
        int viewType = viewTypeManager.add(itemHolder);
        headerItemManager.add(itemHolder);

        itemFactory.attachToAdapter(adapter, viewType);
        itemHolder.attachToAdapter(this, true);
        return itemHolder;
    }

    @NonNull
    public <DATA> ItemHolder<DATA> addHeaderItem(@NonNull ItemFactory<DATA> itemFactory) {
        return addHeaderItem(itemFactory, null);
    }

    @NonNull
    public <DATA> ItemHolder<DATA> addHeaderItem(@NonNull ItemHolder<DATA> itemHolder) {
        //noinspection ConstantConditions
        if (itemHolder == null || viewTypeManager.isLocked()) {
            throw new IllegalArgumentException("itemHolder is null or item factory list locked");
        }
        if (itemHolder.isAttached()) {
            throw new IllegalArgumentException("Cannot be added repeatedly");
        }

        int viewType = viewTypeManager.add(itemHolder);
        headerItemManager.add(itemHolder);

        ItemFactory itemFactory = itemHolder.getItemFactory();
        itemFactory.attachToAdapter(adapter, viewType);
        itemHolder.attachToAdapter(this, true);
        return itemHolder;
    }

    @NonNull
    public ItemHolderManager getHeaderItemManager() {
        return headerItemManager;
    }


    @NonNull
    public <DATA> ItemHolder<DATA> addFooterItem(@NonNull ItemFactory<DATA> itemFactory, @Nullable DATA data) {
        //noinspection ConstantConditions
        if (itemFactory == null || viewTypeManager.isLocked()) {
            throw new IllegalArgumentException("itemFactory is null or item factory list locked");
        }

        ItemHolder<DATA> itemHolder = new ItemHolder<>(itemFactory, data);
        int viewType = viewTypeManager.add(itemHolder);
        footerItemManager.add(itemHolder);

        itemFactory.attachToAdapter(adapter, viewType);
        itemHolder.attachToAdapter(this, false);
        return itemHolder;
    }

    @NonNull
    public <DATA> ItemHolder<DATA> addFooterItem(@NonNull ItemFactory<DATA> itemFactory) {
        return addFooterItem(itemFactory, null);
    }

    @NonNull
    public <DATA> ItemHolder<DATA> addFooterItem(@NonNull ItemHolder<DATA> itemHolder) {
        //noinspection ConstantConditions
        if (itemHolder == null || viewTypeManager.isLocked()) {
            throw new IllegalArgumentException("itemHolder is null or item factory list locked");
        }
        if (itemHolder.isAttached()) {
            throw new IllegalArgumentException("Cannot be added repeatedly");
        }

        int viewType = viewTypeManager.add(itemHolder);
        footerItemManager.add(itemHolder);

        ItemFactory itemFactory = itemHolder.getItemFactory();
        itemFactory.attachToAdapter(adapter, viewType);
        itemHolder.attachToAdapter(this, false);
        return itemHolder;
    }

    @NonNull
    public ItemHolderManager getFooterItemManager() {
        return footerItemManager;
    }

    
    void itemHolderEnabledChanged(@NonNull ItemHolder itemHolder) {
        //noinspection ConstantConditions
        if (itemHolder == null || itemHolder.getItemFactory().getAdapter() != adapter) {
            return;
        }

        if (itemHolder.isHeader()) {
            if (headerItemManager.itemEnabledChanged() && notifyOnChange) {
                adapter.notifyDataSetChanged();
            }
        } else {
            if (footerItemManager.itemEnabledChanged() && notifyOnChange) {
                adapter.notifyDataSetChanged();
            }
        }
    }


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

    public boolean hasMoreFooter() {
        return moreItemHolder != null && moreItemHolder.isEnabled();
    }


    @Nullable
    public List getDataList() {
        return dataList;
    }

    public void setDataList(@Nullable List dataList) {
        synchronized (this) {
            this.dataList = dataList;
        }

        if (notifyOnChange) {
            adapter.notifyDataSetChanged();
        }
    }

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

    public int getDataCount() {
        return dataList != null ? dataList.size() : 0;
    }

    @Nullable
    public Object getData(int positionInDataList) {
        return dataList != null ? dataList.get(positionInDataList) : null;
    }


    public boolean isNotifyOnChange() {
        return notifyOnChange;
    }

    public void setNotifyOnChange(boolean notifyOnChange) {
        this.notifyOnChange = notifyOnChange;
    }

    public int getItemCount() {
        int headerItemCount = headerItemManager.getEnabledItemCount();
        int dataCount = getDataCount();
        int footerItemCount = footerItemManager.getEnabledItemCount();

        if (dataCount > 0) {
            return headerItemCount + dataCount + footerItemCount + (hasMoreFooter() ? 1 : 0);
        } else {
            return headerItemCount + footerItemCount;
        }
    }

    public int getViewTypeCount() {
        // 只要访问了 getViewTypeCount() 方法就认为开始显示了，需要锁定 itemFactory 列表，因为 ListView 不允许 getViewTypeCount() 改变
        if (!viewTypeManager.isLocked()) {
            viewTypeManager.lock();
        }
        // 1 来自 BaseAdapter.getViewTypeCount()
        return viewTypeManager.getCount();
    }

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

    @NonNull
    public ItemFactory getItemFactoryByPosition(int position) {
        int headerItemCount = headerItemManager.getEnabledItemCount();
        int headerStartPosition = 0;
        int headerEndPosition = headerItemCount - 1;

        // header
        if (position >= headerStartPosition && position <= headerEndPosition && headerItemCount > 0) {
            //noinspection UnnecessaryLocalVariable
            int positionInHeaderList = position;
            return headerItemManager.getItemInEnabledList(positionInHeaderList).getItemFactory();
        }

        // data
        int dataCount = getDataCount();
        int dataStartPosition = headerEndPosition + 1;
        int dataEndPosition = headerEndPosition + dataCount;
        if (position >= dataStartPosition && position <= dataEndPosition && dataCount > 0) {
            int positionInDataList = position - headerItemCount;
            Object dataObject = getData(positionInDataList);
            for (ItemFactory itemFactory : itemFactoryList) {
                if (itemFactory.match(dataObject)) {
                    return itemFactory;
                }
            }
            throw new IllegalStateException(String.format("Didn't find suitable ItemFactory. positionInDataList=%d, dataObject=%s",
                    positionInDataList, dataObject != null ? dataObject.toString() : null));
        }

        // footer
        int footerItemCount = footerItemManager.getEnabledItemCount();
        int footerStartPosition = dataEndPosition + 1;
        int footerEndPosition = dataEndPosition + footerItemCount;
        if (position >= footerStartPosition && position <= footerEndPosition && footerItemCount > 0) {
            int positionInFooterList = position - headerItemCount - dataCount;
            return footerItemManager.getItemInEnabledList(positionInFooterList).getItemFactory();
        }

        // more footer
        if (moreItemHolder != null && dataCount > 0 && hasMoreFooter() && position == getItemCount() - 1) {
            return moreItemHolder.getItemFactory();
        }

        throw new IllegalStateException("Not found ItemFactory by position: " + position);
    }

    @Nullable
    public Object getItemDataByPosition(int position) {
        // header
        int headerItemCount = headerItemManager.getEnabledItemCount();
        int headerStartPosition = 0;
        int headerEndPosition = headerItemCount - 1;
        if (position >= headerStartPosition && position <= headerEndPosition && headerItemCount > 0) {
            //noinspection UnnecessaryLocalVariable
            int positionInHeaderList = position;
            return headerItemManager.getItemInEnabledList(positionInHeaderList).getData();
        }

        // body
        int dataCount = getDataCount();
        int dataStartPosition = headerEndPosition + 1;
        int dataEndPosition = headerEndPosition + dataCount;
        if (position >= dataStartPosition && position <= dataEndPosition && dataCount > 0) {
            int positionInDataList = position - headerItemCount;
            return getData(positionInDataList);
        }

        // footer
        int footerItemCount = footerItemManager.getEnabledItemCount();
        int footerStartPosition = dataEndPosition + 1;
        int footerEndPosition = dataEndPosition + footerItemCount;
        if (position >= footerStartPosition && position <= footerEndPosition && footerItemCount > 0) {
            int positionInFooterList = position - headerItemCount - dataCount;
            return footerItemManager.getItemInEnabledList(positionInFooterList).getData();
        }

        // more footer
        if (dataCount > 0 && hasMoreFooter() && position == getItemCount() - 1) {
            return moreItemHolder != null ? moreItemHolder.getData() : null;
        }

        throw new IllegalArgumentException("Not found item data by position: " + position);
    }

    public int getPositionInPart(int position) {
        // header
        int headerItemCount = headerItemManager.getEnabledItemCount();
        int headerStartPosition = 0;
        int headerEndPosition = headerItemCount - 1;
        if (position >= headerStartPosition && position <= headerEndPosition && headerItemCount > 0) {
            return position;
        }

        // body
        int dataCount = getDataCount();
        int dataStartPosition = headerEndPosition + 1;
        int dataEndPosition = headerEndPosition + dataCount;
        if (position >= dataStartPosition && position <= dataEndPosition && dataCount > 0) {
            return position - headerItemCount;
        }

        // footer
        int footerItemCount = footerItemManager.getEnabledItemCount();
        int footerStartPosition = dataEndPosition + 1;
        int footerEndPosition = dataEndPosition + footerItemCount;
        if (position >= footerStartPosition && position <= footerEndPosition && footerItemCount > 0) {
            return position - headerItemCount - dataCount;
        }

        // more footer
        if (dataCount > 0 && hasMoreFooter() && position == getItemCount() - 1) {
            return 0;
        }

        throw new IllegalArgumentException("Illegal position: " + position);
    }

    public boolean isHeaderItem(int position) {
        int headerItemCount = headerItemManager.getEnabledItemCount();
        int headerStartPosition = 0;
        int headerEndPosition = headerItemCount - 1;
        return position >= headerStartPosition && position <= headerEndPosition && headerItemCount > 0;
    }

    public boolean isBodyItem(int position) {
        int headerItemCount = headerItemManager.getEnabledItemCount();
        int headerEndPosition = headerItemCount - 1;
        int dataCount = getDataCount();
        int dataStartPosition = headerEndPosition + 1;
        int dataEndPosition = headerEndPosition + dataCount;
        return position >= dataStartPosition && position <= dataEndPosition && dataCount > 0;
    }

    public boolean isFooterItem(int position) {
        int headerItemCount = headerItemManager.getEnabledItemCount();
        int headerEndPosition = headerItemCount - 1;
        int dataCount = getDataCount();
        int dataEndPosition = headerEndPosition + dataCount;
        int footerItemCount = footerItemManager.getEnabledItemCount();
        int footerStartPosition = dataEndPosition + 1;
        int footerEndPosition = dataEndPosition + footerItemCount;
        return position >= footerStartPosition && position <= footerEndPosition && footerItemCount > 0;
    }

    public boolean isMoreFooterItem(int position) {
        int dataCount = getDataCount();
        return dataCount > 0 && hasMoreFooter() && position == getItemCount() - 1;
    }
}
