/*
 * Copyright (C) 2017 Peng fei Pan <sky@panpf.me>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package me.panpf.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;

import me.panpf.adapter.more.MoreFixedItem;
import me.panpf.adapter.more.MoreItemFactory;

/**
 * Combined {@link BaseAdapter}, supports combination of multiple items, supports head, tail and more
 */
@SuppressWarnings("rawtypes")
public class AssemblyListAdapter extends BaseAdapter implements AssemblyAdapter {

    @NonNull
    private final ItemManager itemManager;
    @NonNull
    private final DataManager dataManager;

    private boolean notifyOnChange = true;

    private final ItemManager.Callback itemCallback = new ItemManager.Callback() {
        @Override
        public void onItemEnabledChanged() {
            if (isNotifyOnChange()) {
                notifyDataSetChanged();
            }
        }

        @Override
        public int getDataCount() {
            return dataManager.getDataCount();
        }

        @Override
        public Object getData(int position) {
            return dataManager.getData(position);
        }
    };

    private final DataManager.Callback dataCallback = () -> {
        if (isNotifyOnChange()) {
            notifyDataSetChanged();
        }
    };

    public AssemblyListAdapter() {
        this.itemManager = new ItemManager(itemCallback);
        this.dataManager = new DataManager(dataCallback);
    }

    public AssemblyListAdapter(@Nullable List dataList) {
        this.itemManager = new ItemManager(itemCallback);
        this.dataManager = new DataManager(dataCallback, dataList);
    }

    @SuppressWarnings("unused")
    public AssemblyListAdapter(@Nullable Object[] dataArray) {
        this.itemManager = new ItemManager(itemCallback);
        this.dataManager = new DataManager(dataCallback, dataArray);
    }


    @Override
    public <DATA> void addItemFactory(@NonNull ItemFactory<DATA> itemFactory) {
        itemManager.addItemFactory(itemFactory, this);
    }

    @NonNull
    @Override
    public List<ItemFactory> getItemFactoryList() {
        return itemManager.getItemFactoryList();
    }


    @NonNull
    @Override
    public <DATA> FixedItem<DATA> addHeaderItem(@NonNull FixedItem<DATA> fixedItem) {
        return itemManager.addHeaderItem(fixedItem, this);
    }

    @NonNull
    @Override
    public <DATA> FixedItem<DATA> addHeaderItem(@NonNull ItemFactory<DATA> itemFactory, @Nullable DATA data) {
        return itemManager.addHeaderItem(itemFactory, data, this);
    }

    @NonNull
    @Override
    public <DATA> FixedItem<DATA> addHeaderItem(@NonNull ItemFactory<DATA> itemFactory) {
        return itemManager.addHeaderItem(itemFactory, this);
    }

    @NonNull
    @Override
    public <DATA> FixedItem<DATA> getHeaderItemByFactoryClass(@NonNull Class<? extends ItemFactory<DATA>> clazz, int number) {
        return itemManager.getHeaderItemManager().getItemByFactoryClass(clazz, number);
    }

    @NonNull
    @Override
    public <DATA> FixedItem<DATA> getHeaderItemByFactoryClass(@NonNull Class<? extends ItemFactory<DATA>> clazz) {
        return itemManager.getHeaderItemManager().getItemByFactoryClass(clazz);
    }

    @NonNull
    @Override
    public FixedItem getHeaderItem(int positionInHeaderItemList) {
        return itemManager.getHeaderItemManager().getItem(positionInHeaderItemList);
    }

    @Nullable
    @Override
    public Object getHeaderItemData(int positionInHeaderItemList) {
        return itemManager.getHeaderItemManager().getItem(positionInHeaderItemList).getData();
    }

    @Override
    public void setHeaderItemData(int positionInHeaderItemList, @Nullable Object data) {
        itemManager.getHeaderItemManager().setItemData(positionInHeaderItemList, data);
    }

    @Override
    public boolean isHeaderItemEnabled(int positionInHeaderItemList) {
        return itemManager.getHeaderItemManager().isItemEnabled(positionInHeaderItemList);
    }

    @Override
    public void setHeaderItemEnabled(int positionInHeaderItemList, boolean enabled) {
        itemManager.getHeaderItemManager().setItemEnabled(positionInHeaderItemList, enabled);
    }

    @Override
    public int getHeaderCount() {
        return itemManager.getHeaderItemManager().getEnabledItemCount();
    }

    @Nullable
    @Override
    public Object getHeaderData(int positionInHeaderList) {
        return itemManager.getHeaderItemManager().getItemInEnabledList(positionInHeaderList).getData();
    }


    @NonNull
    @Override
    public <DATA> FixedItem<DATA> addFooterItem(@NonNull FixedItem<DATA> fixedItem) {
        return itemManager.addFooterItem(fixedItem, this);
    }

    @NonNull
    @Override
    public <DATA> FixedItem<DATA> addFooterItem(@NonNull ItemFactory<DATA> itemFactory, @Nullable DATA data) {
        return itemManager.addFooterItem(itemFactory, data, this);
    }

    @NonNull
    @Override
    public <DATA> FixedItem<DATA> addFooterItem(@NonNull ItemFactory<DATA> itemFactory) {
        return itemManager.addFooterItem(itemFactory, this);
    }

    @NonNull
    @Override
    public <DATA> FixedItem<DATA> getFooterItemByFactoryClass(@NonNull Class<? extends ItemFactory<DATA>> clazz, int number) {
        return itemManager.getFooterItemManager().getItemByFactoryClass(clazz, number);
    }

    @NonNull
    @Override
    public <DATA> FixedItem<DATA> getFooterItemByFactoryClass(@NonNull Class<? extends ItemFactory<DATA>> clazz) {
        return itemManager.getFooterItemManager().getItemByFactoryClass(clazz);
    }

    @NonNull
    @Override
    public FixedItem getFooterItem(int positionInFooterItemList) {
        return itemManager.getFooterItemManager().getItem(positionInFooterItemList);
    }

    @Nullable
    @Override
    public Object getFooterItemData(int positionInFooterItemList) {
        return itemManager.getFooterItemManager().getItem(positionInFooterItemList).getData();
    }

    @Override
    public void setFooterItemData(int positionInFooterItemList, @Nullable Object data) {
        itemManager.getFooterItemManager().setItemData(positionInFooterItemList, data);
    }

    @Override
    public boolean isFooterItemEnabled(int positionInFooterItemList) {
        return itemManager.getFooterItemManager().isItemEnabled(positionInFooterItemList);
    }

    @Override
    public void setFooterItemEnabled(int positionInFooterItemList, boolean enabled) {
        itemManager.getFooterItemManager().setItemEnabled(positionInFooterItemList, enabled);
    }

    @Override
    public int getFooterCount() {
        return itemManager.getFooterItemManager().getEnabledItemCount();
    }

    @Nullable
    @Override
    public Object getFooterData(int positionInFooterList) {
        return itemManager.getFooterItemManager().getItemInEnabledList(positionInFooterList).getData();
    }


    @NonNull
    @Override
    public <DATA> MoreFixedItem<DATA> setMoreItem(@NonNull MoreItemFactory<DATA> itemFactory, @Nullable DATA data) {
        return itemManager.setMoreItem(itemFactory, data, this);
    }

    @NonNull
    @Override
    public <DATA> MoreFixedItem<DATA> setMoreItem(@NonNull MoreItemFactory<DATA> itemFactory) {
        return itemManager.setMoreItem(itemFactory, this);
    }

    @NonNull
    @Override
    public <DATA> MoreFixedItem<DATA> setMoreItem(@NonNull MoreFixedItem<DATA> moreFixedItem) {
        return itemManager.setMoreItem(moreFixedItem, this);
    }

    @Nullable
    @Override
    public MoreFixedItem getMoreItem() {
        return itemManager.getMoreFixedItem();
    }

    @Override
    public boolean hasMoreFooter() {
        return itemManager.hasMoreFooter();
    }

    @Override
    public void setMoreItemEnabled(boolean enabled) {
        MoreFixedItem moreFixedItem = itemManager.getMoreFixedItem();
        if (moreFixedItem != null) {
            moreFixedItem.setEnabled(enabled);
        }
    }

    @Override
    public void loadMoreFinished(boolean end) {
        MoreFixedItem moreFixedItem = itemManager.getMoreFixedItem();
        if (moreFixedItem != null) {
            moreFixedItem.loadMoreFinished(end);
        }
    }

    @Override
    public void loadMoreFailed() {
        MoreFixedItem moreFixedItem = itemManager.getMoreFixedItem();
        if (moreFixedItem != null) {
            moreFixedItem.loadMoreFailed();
        }
    }


    @Nullable
    @Override
    public List getDataList() {
        return dataManager.getDataList();
    }

    @Override
    public void setDataList(@Nullable List dataList) {
        dataManager.setDataList(dataList);
    }

    @Override
    public void addAll(@Nullable Collection collection) {
        dataManager.addAll(collection);
    }

    @Override
    public void addAll(@Nullable Object... items) {
        dataManager.addAll(items);
    }

    @Override
    public void insert(@NonNull Object object, int index) {
        dataManager.insert(object, index);
    }

    @Override
    public void remove(@NonNull Object object) {
        dataManager.remove(object);
    }

    @Override
    public void clear() {
        dataManager.clear();
    }

    @Override
    public void sort(@NonNull Comparator comparator) {
        dataManager.sort(comparator);
    }

    @Override
    public int getDataCount() {
        return dataManager.getDataCount();
    }

    @Nullable
    @Override
    public Object getData(int positionInDataList) {
        return dataManager.getData(positionInDataList);
    }


    @Override
    public int getItemCount() {
        return getDataCount() + itemManager.getHeaderAndFooterCount();
    }

    @Nullable
    @Override
    public Object getItem(int position) {
        return itemManager.getItemDataByPosition(position);
    }

    @Override
    public int getPositionInPart(int position) {
        return itemManager.getPositionInPart(position);
    }

    @Override
    public boolean isHeaderItem(int position) {
        return itemManager.isHeaderItem(position);
    }

    @Override
    public boolean isBodyItem(int position) {
        return itemManager.isBodyItem(position);
    }

    @Override
    public boolean isFooterItem(int position) {
        return itemManager.isFooterItem(position);
    }

    @Override
    public boolean isMoreFooterItem(int position) {
        return itemManager.isMoreFooterItem(position);
    }

    @Override
    public int getSpanSize(int position) {
        return itemManager.getItemFactoryByPosition(position).getSpanSize();
    }

    @NonNull
    @Override
    public ItemFactory getItemFactoryByPosition(int position) {
        return itemManager.getItemFactoryByPosition(position);
    }

    @NonNull
    @Override
    public ItemFactory getItemFactoryByViewType(int viewType) {
        return itemManager.getItemFactoryByViewType(viewType);
    }


    @Override
    public boolean isNotifyOnChange() {
        return notifyOnChange;
    }

    @Override
    public void setNotifyOnChange(boolean notifyOnChange) {
        this.notifyOnChange = notifyOnChange;
    }


    @Override
    public int getCount() {
        return getItemCount();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getViewTypeCount() {
        return itemManager.getViewTypeCount();
    }

    @Override
    public int getItemViewType(int position) {
        return itemManager.getItemFactoryByPosition(position).getViewType();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        Item item;
        if (convertView == null) {
            item = createItem(parent, getItemViewType(position));
            convertView = item.getItemView();
            convertView.setTag(item);
        } else {
            item = (Item) convertView.getTag();
        }
        bindItem(item, position);
        return convertView;
    }

    @NonNull
    private Item createItem(@NonNull ViewGroup parent, int viewType) {
        ItemFactory itemFactory = itemManager.getItemFactoryByViewType(viewType);
        return itemFactory.dispatchCreateItem(parent);
    }

    private void bindItem(@NonNull Item item, int position) {
        Object itemObject = getItem(position);
        //noinspection unchecked
        item.setData(position, itemObject);
    }
}
