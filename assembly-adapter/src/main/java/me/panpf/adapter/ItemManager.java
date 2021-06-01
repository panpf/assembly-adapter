package me.panpf.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

import me.panpf.adapter.more.MoreFixedItem;
import me.panpf.adapter.more.MoreItemFactory;

@SuppressWarnings("rawtypes")
public class ItemManager {

    @NonNull
    private final Callback callback;
    @NonNull
    private final ViewTypeManager<Object> viewTypeManager = new ViewTypeManager<>();
    @NonNull
    private final FixedItemManager headerItemManager = new FixedItemManager();
    @NonNull
    private final ArrayList<ItemFactory> itemFactoryList = new ArrayList<>();
    @NonNull
    private final FixedItemManager footerItemManager = new FixedItemManager();
    @Nullable
    private MoreFixedItem moreFixedItem;

    public ItemManager(@NonNull Callback callback) {
        this.callback = callback;
    }


    public void addItemFactory(@NonNull ItemFactory itemFactory, @NonNull AssemblyAdapter adapter) {
        //noinspection ConstantConditions
        if (itemFactory == null || viewTypeManager.isLocked()) {
            throw new IllegalArgumentException("itemFactory is null or item factory list locked");
        }

        itemFactoryList.add(itemFactory);
        int viewType = viewTypeManager.add(itemFactory);

        itemFactory.attachToAdapter(adapter, viewType);
    }

    @NonNull
    public ArrayList<ItemFactory> getItemFactoryList() {
        return itemFactoryList;
    }

    @NonNull
    public <DATA> FixedItem<DATA> addHeaderItem(@NonNull FixedItem<DATA> fixedItem, @NonNull AssemblyAdapter adapter) {
        //noinspection ConstantConditions
        if (fixedItem == null || viewTypeManager.isLocked()) {
            throw new IllegalArgumentException("item is null or item factory list locked");
        }
        if (fixedItem.isAttached()) {
            throw new IllegalArgumentException("Cannot be added repeatedly");
        }

        int viewType = viewTypeManager.add(fixedItem);
        headerItemManager.add(fixedItem);

        ItemFactory itemFactory = fixedItem.getItemFactory();
        itemFactory.attachToAdapter(adapter, viewType);
        fixedItem.attachToAdapter(this, true);
        return fixedItem;
    }

    @NonNull
    public <DATA> FixedItem<DATA> addHeaderItem(@NonNull ItemFactory<DATA> itemFactory, @Nullable DATA data, @NonNull AssemblyAdapter adapter) {
        return addHeaderItem(new FixedItem<>(itemFactory, data), adapter);
    }

    @NonNull
    public <DATA> FixedItem<DATA> addHeaderItem(@NonNull ItemFactory<DATA> itemFactory, @NonNull AssemblyAdapter adapter) {
        return addHeaderItem(new FixedItem<>(itemFactory, null), adapter);
    }

    @NonNull
    public FixedItemManager getHeaderItemManager() {
        return headerItemManager;
    }


    @NonNull
    public <DATA> FixedItem<DATA> addFooterItem(@NonNull FixedItem<DATA> fixedItem, @NonNull AssemblyAdapter adapter) {
        if (viewTypeManager.isLocked()) {
            throw new IllegalArgumentException("item is null or item factory list locked");
        }
        if (fixedItem.isAttached()) {
            throw new IllegalArgumentException("Cannot be added repeatedly");
        }

        int viewType = viewTypeManager.add(fixedItem);
        footerItemManager.add(fixedItem);

        ItemFactory itemFactory = fixedItem.getItemFactory();
        itemFactory.attachToAdapter(adapter, viewType);
        fixedItem.attachToAdapter(this, false);
        return fixedItem;
    }

    @NonNull
    public <DATA> FixedItem<DATA> addFooterItem(@NonNull ItemFactory<DATA> itemFactory, @Nullable DATA data, @NonNull AssemblyAdapter adapter) {
        return addFooterItem(new FixedItem<>(itemFactory, data), adapter);
    }

    @NonNull
    public <DATA> FixedItem<DATA> addFooterItem(@NonNull ItemFactory<DATA> itemFactory, @NonNull AssemblyAdapter adapter) {
        return addFooterItem(new FixedItem<>(itemFactory, null), adapter);
    }

    @NonNull
    public FixedItemManager getFooterItemManager() {
        return footerItemManager;
    }


    void fixedItemEnabledChanged(@NonNull FixedItem fixedItem) {
        if (fixedItem.isHeader()) {
            headerItemManager.itemEnabledChanged();
        } else {
            footerItemManager.itemEnabledChanged();
        }
        callback.onItemEnabledChanged();
    }


    @NonNull
    public <DATA> MoreFixedItem<DATA> setMoreItem(@NonNull MoreFixedItem<DATA> fixedItem, @NonNull AssemblyAdapter adapter) {
        if (viewTypeManager.isLocked()) {
            throw new IllegalArgumentException("item is null or item factory list locked");
        }
        if (fixedItem.isAttached()) {
            throw new IllegalArgumentException("Cannot be added repeatedly");
        }
        if (this.moreFixedItem != null) {
            throw new IllegalStateException("MoreItem cannot be set repeatedly");
        }

        int viewType = viewTypeManager.add(fixedItem);
        MoreItemFactory itemFactory = fixedItem.getItemFactory();
        itemFactory.loadMoreFinished(false);

        itemFactory.attachToAdapter(adapter, viewType);
        ((FixedItem) fixedItem).attachToAdapter(this, false);
        this.moreFixedItem = fixedItem;
        return fixedItem;
    }

    @NonNull
    public <DATA> MoreFixedItem<DATA> setMoreItem(@NonNull MoreItemFactory<DATA> itemFactory, @Nullable DATA data, @NonNull AssemblyAdapter adapter) {
        return setMoreItem(new MoreFixedItem<>(itemFactory, data), adapter);
    }

    @NonNull
    public <DATA> MoreFixedItem<DATA> setMoreItem(@NonNull MoreItemFactory<DATA> itemFactory, @NonNull AssemblyAdapter adapter) {
        return setMoreItem(new MoreFixedItem<>(itemFactory, null), adapter);
    }

    @Nullable
    public MoreFixedItem getMoreFixedItem() {
        return moreFixedItem;
    }

    public boolean hasMoreFooter() {
        return moreFixedItem != null && moreFixedItem.isEnabled();
    }

    public int getHeaderAndFooterCount() {
        int headerItemCount = headerItemManager.getEnabledItemCount();
        int dataCount = callback.getDataCount();
        int footerItemCount = footerItemManager.getEnabledItemCount();
        return headerItemCount + footerItemCount + (dataCount > 0 && hasMoreFooter() ? 1 : 0);
    }

    public int getViewTypeCount() {
        // 只要访问了 getViewTypeCount() 方法就认为开始显示了，需要锁定 itemFactory 列表，因为 ListView 不允许 getViewTypeCount() 改变
        if (!viewTypeManager.isLocked()) {
            viewTypeManager.lock();
        }
        // 1 来自 BaseAdapter.getViewTypeCount()
        return viewTypeManager.getCount();
    }

    @NonNull
    public ItemFactory getItemFactoryByViewType(int viewType) {
        Object value = viewTypeManager.get(viewType);
        if (value instanceof ItemFactory) {
            return (ItemFactory) value;
        } else if (value instanceof FixedItem) {
            return ((FixedItem) value).getItemFactory();
        } else if (value != null) {
            throw new IllegalArgumentException("Unknown viewType value. viewType=" + viewType + ", value=" + value);
        } else {
            throw new IllegalArgumentException("Unknown viewType. viewType=" + viewType);
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
        int dataCount = callback.getDataCount();
        int dataStartPosition = headerEndPosition + 1;
        int dataEndPosition = headerEndPosition + dataCount;
        if (position >= dataStartPosition && position <= dataEndPosition && dataCount > 0) {
            int positionInDataList = position - headerItemCount;
            Object dataObject = callback.getData(positionInDataList);
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
        if (hasMoreFooter()) {
            int morePosition = headerItemCount + dataCount + footerItemCount + (dataCount > 0 ? 1 : 0) - 1;
            if (moreFixedItem != null && dataCount > 0 && position == morePosition) {
                return moreFixedItem.getItemFactory();
            }
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
        int dataCount = callback.getDataCount();
        int dataStartPosition = headerEndPosition + 1;
        int dataEndPosition = headerEndPosition + dataCount;
        if (position >= dataStartPosition && position <= dataEndPosition && dataCount > 0) {
            int positionInDataList = position - headerItemCount;
            return callback.getData(positionInDataList);
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
        if (hasMoreFooter()) {
            int morePosition = headerItemCount + dataCount + footerItemCount + (dataCount > 0 ? 1 : 0) - 1;
            if (dataCount > 0 && position == morePosition) {
                return moreFixedItem != null ? moreFixedItem.getData() : null;
            }
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
        int dataCount = callback.getDataCount();
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
        if (hasMoreFooter()) {
            int morePosition = headerItemCount + dataCount + footerItemCount + (dataCount > 0 ? 1 : 0) - 1;
            if (dataCount > 0 && position == morePosition) {
                return 0;
            }
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
        int dataCount = callback.getDataCount();
        int dataStartPosition = headerEndPosition + 1;
        int dataEndPosition = headerEndPosition + dataCount;
        return position >= dataStartPosition && position <= dataEndPosition && dataCount > 0;
    }

    public boolean isFooterItem(int position) {
        int headerItemCount = headerItemManager.getEnabledItemCount();
        int headerEndPosition = headerItemCount - 1;
        int dataCount = callback.getDataCount();
        int dataEndPosition = headerEndPosition + dataCount;
        int footerItemCount = footerItemManager.getEnabledItemCount();
        int footerStartPosition = dataEndPosition + 1;
        int footerEndPosition = dataEndPosition + footerItemCount;
        return position >= footerStartPosition && position <= footerEndPosition && footerItemCount > 0;
    }

    public boolean isMoreFooterItem(int position) {
        if (hasMoreFooter()) {
            int headerItemCount = headerItemManager.getEnabledItemCount();
            int dataCount = callback.getDataCount();
            int footerItemCount = footerItemManager.getEnabledItemCount();
            int morePosition = headerItemCount + dataCount + footerItemCount + (dataCount > 0 ? 1 : 0) - 1;
            return dataCount > 0 && position == morePosition;
        } else {
            return false;
        }
    }

    public interface Callback {
        void onItemEnabledChanged();

        int getDataCount();

        Object getData(int position);
    }
}
