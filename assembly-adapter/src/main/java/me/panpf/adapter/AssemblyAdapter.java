package me.panpf.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;

import me.panpf.adapter.more.MoreFixedItem;
import me.panpf.adapter.more.MoreItemFactory;

/**
 * Support to combine multiple items, support head, tail and load more
 */
@SuppressWarnings("rawtypes")
public interface AssemblyAdapter {

    <DATA> void addItemFactory(@NonNull ItemFactory<DATA> itemFactory);

    @NonNull
    List<ItemFactory> getItemFactoryList();


    @NonNull
    <DATA> FixedItem<DATA> addHeaderItem(@NonNull FixedItem<DATA> fixedItem);

    @NonNull
    <DATA> FixedItem<DATA> addHeaderItem(@NonNull ItemFactory<DATA> itemFactory, @Nullable DATA data);

    @NonNull
    <DATA> FixedItem<DATA> addHeaderItem(@NonNull ItemFactory<DATA> itemFactory);

    @NonNull
    <DATA> FixedItem<DATA> getHeaderItemByFactoryClass(@NonNull Class<? extends ItemFactory<DATA>> clazz, int number);

    @NonNull
    <DATA>FixedItem<DATA> getHeaderItemByFactoryClass(@NonNull Class<? extends ItemFactory<DATA>> clazz);

    @NonNull
    FixedItem getHeaderItem(int positionInHeaderItemList);

    @Nullable
    Object getHeaderItemData(int positionInHeaderItemList);

    void setHeaderItemData(int positionInHeaderItemList, @Nullable Object data);

    boolean isHeaderItemEnabled(int positionInHeaderItemList);

    void setHeaderItemEnabled(int positionInHeaderItemList, boolean enabled);

    int getHeaderCount();

    @Nullable
    Object getHeaderData(int positionInHeaderList);


    @NonNull
    <DATA> FixedItem<DATA> addFooterItem(@NonNull FixedItem<DATA> fixedItem);

    @NonNull
    <DATA> FixedItem<DATA> addFooterItem(@NonNull ItemFactory<DATA> itemFactory, @Nullable DATA data);

    @NonNull
    <DATA> FixedItem<DATA> addFooterItem(@NonNull ItemFactory<DATA> itemFactory);

    @NonNull
    <DATA> FixedItem<DATA> getFooterItemByFactoryClass(@NonNull Class<? extends ItemFactory<DATA>> clazz, int number);

    @NonNull
    <DATA> FixedItem<DATA> getFooterItemByFactoryClass(@NonNull Class<? extends ItemFactory<DATA>> clazz);

    @NonNull
    FixedItem getFooterItem(int positionInFooterItemList);

    @Nullable
    Object getFooterItemData(int positionInFooterItemList);

    void setFooterItemData(int positionInFooterItemList, @Nullable Object data);

    boolean isFooterItemEnabled(int positionInFooterItemList);

    void setFooterItemEnabled(int positionInFooterItemList, boolean enabled);

    int getFooterCount();

    @Nullable
    Object getFooterData(int positionFooterList);


    @NonNull
    <DATA> MoreFixedItem<DATA> setMoreItem(@NonNull MoreItemFactory<DATA> itemFactory, @Nullable DATA data);

    @NonNull
    <DATA> MoreFixedItem<DATA> setMoreItem(@NonNull MoreItemFactory<DATA> itemFactory);

    @NonNull
    <DATA> MoreFixedItem<DATA> setMoreItem(@NonNull MoreFixedItem<DATA> moreFixedItem);

    @Nullable
    MoreFixedItem getMoreItem();

    boolean hasMoreFooter();

    void setMoreItemEnabled(boolean enabled);

    void loadMoreFinished(boolean end);

    void loadMoreFailed();


    @Nullable
    List getDataList();

    void setDataList(@Nullable List dataList);

    void addAll(@Nullable Collection collection);

    void addAll(@Nullable Object... items);

    void insert(@NonNull Object object, int index);

    void remove(@NonNull Object object);

    void clear();

    void sort(@NonNull Comparator comparator);

    int getDataCount();

    @Nullable
    Object getData(int positionInDataList);


    int getItemCount();

    @Nullable
    Object getItem(int position);

    boolean isHeaderItem(int position);

    boolean isBodyItem(int position);

    boolean isFooterItem(int position);

    boolean isMoreFooterItem(int position);

    int getPositionInPart(int position);

    /**
     * Get a few columns in the specified location, reserved for {@link RecyclerView}
     */
    int getSpanSize(int position);

    @NonNull
    ItemFactory getItemFactoryByPosition(int position);

    @NonNull
    ItemFactory getItemFactoryByViewType(int viewType);


    boolean isNotifyOnChange();

    void setNotifyOnChange(boolean notifyOnChange);

    void notifyDataSetChanged();
}
