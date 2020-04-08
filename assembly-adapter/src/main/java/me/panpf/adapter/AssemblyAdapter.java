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
public interface AssemblyAdapter {


    <DATA> void addItemFactory(@NonNull ItemFactory<DATA> itemFactory);


    @NonNull
    <DATA> FixedItem<DATA> addHeaderItem(@NonNull ItemFactory<DATA> itemFactory, @Nullable DATA data);

    @NonNull
    <DATA> FixedItem<DATA> addHeaderItem(@NonNull ItemFactory<DATA> itemFactory);

    @NonNull
    <DATA> FixedItem<DATA> addHeaderItem(@NonNull FixedItem<DATA> fixedItem);

    @NonNull
    FixedItemManager getHeaderItemManager();

    int getHeaderEnabledItemCount();


    @NonNull
    <DATA> FixedItem<DATA> addFooterItem(@NonNull ItemFactory<DATA> itemFactory, @Nullable DATA data);

    @NonNull
    <DATA> FixedItem<DATA> addFooterItem(@NonNull ItemFactory<DATA> itemFactory);

    @NonNull
    <DATA> FixedItem<DATA> addFooterItem(@NonNull FixedItem<DATA> fixedItem);

    @NonNull
    FixedItemManager getFooterItemManager();

    int getFooterEnabledItemCount();


    @NonNull
    <DATA> MoreFixedItem<DATA> setMoreItem(@NonNull MoreItemFactory<DATA> itemFactory, @Nullable DATA data);

    @NonNull
    <DATA> MoreFixedItem<DATA> setMoreItem(@NonNull MoreItemFactory<DATA> itemFactory);

    @NonNull
    <DATA> MoreFixedItem<DATA> setMoreItem(@NonNull MoreFixedItem<DATA> moreFixedItem);

    @Nullable
    MoreFixedItem getMoreItem();

    boolean hasMoreFooter();

    void setEnabledMoreItem(boolean enabledMoreItem);

    void loadMoreFinished(boolean loadMoreEnd);

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


    int getItemCount();

    @Nullable
    Object getItem(int position);

    int getPositionInPart(int position);


    boolean isNotifyOnChange();

    void setNotifyOnChange(boolean notifyOnChange);

    void notifyDataSetChanged();

    /**
     * Get a few columns in the specified location, reserved for {@link RecyclerView}
     */
    int getSpanSize(int position);

    @Nullable
    ItemFactory getItemFactoryByViewType(int viewType);

    @NonNull
    ItemFactory getItemFactoryByPosition(int position);

    boolean isHeaderItem(int position);

    boolean isBodyItem(int position);

    boolean isFooterItem(int position);

    boolean isMoreFooterItem(int position);
}
