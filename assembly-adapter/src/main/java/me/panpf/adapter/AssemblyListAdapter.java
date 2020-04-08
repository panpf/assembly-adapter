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

import me.panpf.adapter.more.MoreItemFactory;
import me.panpf.adapter.more.MoreFixedItem;

/**
 * Combined {@link BaseAdapter}, supports combination of multiple items, supports head, tail and more
 */
public class AssemblyListAdapter extends BaseAdapter implements AssemblyAdapter {

    @NonNull
    private ItemManager itemManager;


    public AssemblyListAdapter() {
        this.itemManager = new ItemManager(this);
    }

    public AssemblyListAdapter(@Nullable List dataList) {
        this.itemManager = new ItemManager(this, dataList);
    }

    public AssemblyListAdapter(@Nullable Object[] dataArray) {
        this.itemManager = new ItemManager(this, dataArray);
    }


    @Override
    public <DATA> void addItemFactory(@NonNull ItemFactory<DATA> itemFactory) {
        itemManager.addItemFactory(itemFactory);
    }


    @NonNull
    @Override
    public <DATA> FixedItem<DATA> addHeaderItem(@NonNull ItemFactory<DATA> itemFactory, @Nullable DATA data) {
        return itemManager.addHeaderItem(itemFactory, data);
    }

    @NonNull
    @Override
    public <DATA> FixedItem<DATA> addHeaderItem(@NonNull ItemFactory<DATA> itemFactory) {
        return itemManager.addHeaderItem(itemFactory);
    }

    @NonNull
    @Override
    public <DATA> FixedItem<DATA> addHeaderItem(@NonNull FixedItem<DATA> fixedItem) {
        return itemManager.addHeaderItem(fixedItem);
    }

    @NonNull
    @Override
    public FixedItemManager getHeaderItemManager() {
        return itemManager.getHeaderItemManager();
    }

    @Override
    public int getHeaderEnabledItemCount() {
        return itemManager.getHeaderItemManager().getEnabledItemCount();
    }


    @NonNull
    @Override
    public <DATA> FixedItem<DATA> addFooterItem(@NonNull ItemFactory<DATA> itemFactory, @Nullable DATA data) {
        return itemManager.addFooterItem(itemFactory, data);
    }

    @NonNull
    @Override
    public <DATA> FixedItem<DATA> addFooterItem(@NonNull ItemFactory<DATA> itemFactory) {
        return itemManager.addFooterItem(itemFactory);
    }

    @NonNull
    @Override
    public <DATA> FixedItem<DATA> addFooterItem(@NonNull FixedItem<DATA> fixedItem) {
        return itemManager.addFooterItem(fixedItem);
    }

    @NonNull
    @Override
    public FixedItemManager getFooterItemManager() {
        return itemManager.getFooterItemManager();
    }

    @Override
    public int getFooterEnabledItemCount() {
        return itemManager.getFooterItemManager().getEnabledItemCount();
    }


    @NonNull
    @Override
    public <DATA> MoreFixedItem<DATA> setMoreItem(@NonNull MoreItemFactory<DATA> itemFactory, @Nullable DATA data) {
        return itemManager.setMoreItem(itemFactory, data);
    }

    @NonNull
    @Override
    public <DATA> MoreFixedItem<DATA> setMoreItem(@NonNull MoreItemFactory<DATA> itemFactory) {
        return itemManager.setMoreItem(itemFactory);
    }

    @NonNull
    @Override
    public <DATA> MoreFixedItem<DATA> setMoreItem(@NonNull MoreFixedItem<DATA> moreFixedItem) {
        return itemManager.setMoreItem(moreFixedItem);
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
    public void setEnabledMoreItem(boolean enabledMoreItem) {
        MoreFixedItem moreFixedItem = itemManager.getMoreFixedItem();
        if (moreFixedItem != null) {
            moreFixedItem.setEnabled(enabledMoreItem);
        }
    }

    @Override
    public void loadMoreFinished(boolean loadMoreEnd) {
        MoreFixedItem moreFixedItem = itemManager.getMoreFixedItem();
        if (moreFixedItem != null) {
            moreFixedItem.loadMoreFinished(loadMoreEnd);
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
        return itemManager.getDataList();
    }

    @Override
    public void setDataList(@Nullable List dataList) {
        itemManager.setDataList(dataList);
    }

    @Override
    public void addAll(@Nullable Collection collection) {
        itemManager.addAll(collection);
    }

    @Override
    public void addAll(@Nullable Object... items) {
        itemManager.addAll(items);
    }

    @Override
    public void insert(@NonNull Object object, int index) {
        itemManager.insert(object, index);
    }

    @Override
    public void remove(@NonNull Object object) {
        itemManager.remove(object);
    }

    @Override
    public void clear() {
        itemManager.clear();
    }

    @Override
    public void sort(@NonNull Comparator comparator) {
        itemManager.sort(comparator);
    }

    @Override
    public int getDataCount() {
        return itemManager.getDataCount();
    }


    @Override
    public int getItemCount() {
        return itemManager.getItemCount();
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
    public int getCount() {
        return itemManager.getItemCount();
    }


    @Override
    public boolean isNotifyOnChange() {
        return itemManager.isNotifyOnChange();
    }

    @Override
    public void setNotifyOnChange(boolean notifyOnChange) {
        itemManager.setNotifyOnChange(notifyOnChange);
    }

    @Override
    public int getSpanSize(int position) {
        return itemManager.getItemFactoryByPosition(position).getSpanSize();
    }

    @Nullable
    @Override
    public ItemFactory getItemFactoryByViewType(int viewType) {
        return itemManager.getItemFactoryByViewType(viewType);
    }

    @NonNull
    @Override
    public ItemFactory getItemFactoryByPosition(int position) {
        return itemManager.getItemFactoryByPosition(position);
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
        @Nullable
        ItemFactory itemFactory = itemManager.getItemFactoryByViewType(viewType);
        if (itemFactory != null) {
            return itemFactory.dispatchCreateItem(parent);
        } else {
            throw new IllegalStateException(String.format("Not found ItemFactory by viewType: %d", viewType));
        }
    }

    private void bindItem(@NonNull Item item, int position) {
        Object itemObject = getItem(position);
        //noinspection unchecked
        item.setData(position, itemObject);
    }
}
