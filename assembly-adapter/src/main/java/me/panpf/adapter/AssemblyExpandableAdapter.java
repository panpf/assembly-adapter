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
import android.widget.BaseExpandableListAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;

import me.panpf.adapter.expandable.ExpandableItemManager;
import me.panpf.adapter.more.MoreItemFactory;
import me.panpf.adapter.more.MoreFixedItem;

/**
 * Combined {@link BaseExpandableListAdapter}, support to combine multiple items, support head, tail and load more
 */
public class AssemblyExpandableAdapter extends BaseExpandableListAdapter implements AssemblyAdapter {

    @NonNull
    private ExpandableItemManager itemManager;
    @Nullable
    private ExpandCallback expandCallback;


    public AssemblyExpandableAdapter() {
        this.itemManager = new ExpandableItemManager(this);
    }

    public AssemblyExpandableAdapter(@Nullable List dataList) {
        this.itemManager = new ExpandableItemManager(this, dataList);
    }

    public AssemblyExpandableAdapter(@Nullable Object[] dataArray) {
        this.itemManager = new ExpandableItemManager(this, dataArray);
    }


    @Override
    public <DATA> void addItemFactory(@NonNull ItemFactory<DATA> itemFactory) {
        itemManager.addItemFactory(itemFactory);
    }

    public <DATA> void addGroupItemFactory(@NonNull ItemFactory<DATA> groupItemFactory) {
        itemManager.addItemFactory(groupItemFactory);
    }

    public <DATA> void addChildItemFactory(@NonNull ItemFactory<DATA> childItemFactory) {
        itemManager.addChildItemFactory(childItemFactory);
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
        return itemManager.addHeaderItem(itemFactory);
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
    public int getGroupCount() {
        return itemManager.getItemCount();
    }

    @Nullable
    @Override
    public Object getGroup(int groupPosition) {
        return itemManager.getItemDataByPosition(groupPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public int getGroupTypeCount() {
        return itemManager.getViewTypeCount();
    }

    @Override
    public int getGroupType(int groupPosition) {
        return itemManager.getItemFactoryByPosition(groupPosition).getViewType();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return itemManager.getChildrenCount(groupPosition);
    }

    @Nullable
    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return itemManager.getChildDataByPosition(groupPosition, childPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public int getChildTypeCount() {
        return itemManager.getChildTypeCount();
    }

    @Override
    public int getChildType(int groupPosition, int childPosition) {
        return itemManager.getChildViewType(groupPosition, childPosition);
    }

    @Override
    public boolean hasStableIds() {
        return expandCallback != null && expandCallback.hasStableIds();
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return expandCallback != null && expandCallback.isChildSelectable(groupPosition, childPosition);
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        Item groupItem;
        if (convertView == null) {
            groupItem = createGroupItem(parent, getGroupType(groupPosition));
            convertView = groupItem.getItemView();
            convertView.setTag(groupItem);
        } else {
            groupItem = (Item) convertView.getTag();
        }
        bindGroupItem(groupItem, isExpanded, groupPosition);
        return convertView;
    }

    @NonNull
    private Item createGroupItem(@NonNull ViewGroup parent, int viewType) {
        @Nullable
        ItemFactory itemFactory = itemManager.getItemFactoryByViewType(viewType);
        if (itemFactory != null) {
            return itemFactory.dispatchCreateItem(parent);
        } else {
            throw new IllegalStateException(String.format("Not found ItemFactory by groupViewType: %d", viewType));
        }
    }

    private void bindGroupItem(@NonNull Item groupItem, boolean isExpanded, int groupPosition) {
        Object group = getGroup(groupPosition);
        groupItem.setExpanded(isExpanded);
        //noinspection unchecked
        groupItem.setData(groupPosition, group);
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, @Nullable View convertView, @NonNull ViewGroup parent) {
        Item childItem;
        if (convertView == null) {
            childItem = createChildItem(parent, getChildType(groupPosition, childPosition));
            convertView = childItem.getItemView();
            convertView.setTag(childItem);
        } else {
            childItem = (Item) convertView.getTag();
        }
        bindChildItem(childItem, groupPosition, childPosition, isLastChild);
        return convertView;
    }

    @NonNull
    private Item createChildItem(@NonNull ViewGroup parent, int viewType) {
        @Nullable
        Object itemObject = itemManager.getChildItemFactoryByViewType(viewType);
        if (itemObject instanceof ItemFactory) {
            ItemFactory itemFactory = (ItemFactory) itemObject;
            return itemFactory.dispatchCreateItem(parent);
        } else {
            throw new IllegalStateException(String.format("Unknown childViewType: %d, itemFactory: %s",
                    viewType, itemObject != null ? itemObject.getClass().getName() : "null"));
        }
    }

    private void bindChildItem(@NonNull Item childItem, int groupPosition, int childPosition, boolean isLastChild) {
        Object child = getChild(groupPosition, childPosition);
        childItem.setGroupPosition(groupPosition);
        childItem.setLastChild(isLastChild);
        //noinspection unchecked
        childItem.setData(childPosition, child);
    }

    public void setExpandCallback(@Nullable ExpandCallback expandCallback) {
        this.expandCallback = expandCallback;
    }

    public interface ExpandCallback {
        boolean hasStableIds();

        boolean isChildSelectable(int groupPosition, int childPosition);
    }
}
