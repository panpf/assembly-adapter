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

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;

import me.panpf.adapter.expandable.ExpandableItemStorage;
import me.panpf.adapter.more.AssemblyLoadMoreItem;
import me.panpf.adapter.more.AssemblyLoadMoreItemFactory;
import me.panpf.adapter.more.LoadMoreFixedItemInfo;

/**
 * 通用组合式 {@link BaseExpandableListAdapter}，支持组合式多 item，支持头、尾巴以及加载更多
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public class AssemblyExpandableAdapter extends BaseExpandableListAdapter implements AssemblyAdapter {

    @NonNull
    private ExpandableItemStorage storage;

    private ExpandCallback expandCallback;

    public AssemblyExpandableAdapter() {
        this.storage = new ExpandableItemStorage(this);
    }

    public AssemblyExpandableAdapter(@Nullable List dataList) {
        this.storage = new ExpandableItemStorage(this, dataList);
    }

    public AssemblyExpandableAdapter(@Nullable Object[] dataArray) {
        this.storage = new ExpandableItemStorage(this, dataArray);
    }


    /* ************************ 数据 ItemFactory *************************** */

    @Override
    public <ITEM extends AssemblyItem> void addItemFactory(@NonNull AssemblyItemFactory<ITEM> itemFactory) {
        storage.addItemFactory(itemFactory);
    }

    public <ITEM extends AssemblyItem> void addGroupItemFactory(@NonNull AssemblyItemFactory<ITEM> groupItemFactory) {
        storage.addItemFactory(groupItemFactory);
    }

    public <ITEM extends AssemblyItem> void addChildItemFactory(@NonNull AssemblyItemFactory<ITEM> childItemFactory) {
        storage.addChildItemFactory(childItemFactory);
    }

    @Nullable
    @Override
    public List<ItemFactory> getItemFactoryList() {
        return storage.getItemFactoryList();
    }

    /**
     * 获取 group {@link ItemFactory} 列表
     */
    @Nullable
    public List<ItemFactory> getGroupItemFactoryList() {
        return storage.getItemFactoryList();
    }

    /**
     * 获取 child {@link ItemFactory} 列表
     */
    @Nullable
    public List<ItemFactory> getChildItemFactoryList() {
        return storage.getChildItemFactoryList();
    }

    @Override
    public int getItemFactoryCount() {
        return storage.getItemFactoryCount();
    }

    public int getGroupItemFactoryCount() {
        return storage.getItemFactoryCount();
    }

    public int getChildItemFactoryCount() {
        return storage.getChildItemFactoryCount();
    }


    /* ************************ 头部 ItemFactory *************************** */

    @NonNull
    @Override
    public <ITEM extends AssemblyItem> FixedItemInfo addHeaderItem(@NonNull AssemblyItemFactory<ITEM> itemFactory, @NonNull Object data) {
        return storage.addHeaderItem(itemFactory, data);
    }

    @NonNull
    @Override
    public <ITEM extends AssemblyItem> FixedItemInfo addHeaderItem(@NonNull AssemblyItemFactory<ITEM> itemFactory) {
        return storage.addHeaderItem(itemFactory);
    }

    @Override
    public void headerEnabledChanged(@NonNull FixedItemInfo headerItemInfo) {
        storage.headerEnabledChanged(headerItemInfo);
    }

    @Override
    public List<FixedItemInfo> getHeaderItemList() {
        return storage.getHeaderItemList();
    }

    @Override
    public int getHeaderItemCount() {
        return storage.getHeaderItemCount();
    }

    @Nullable
    @Override
    public Object getHeaderData(int positionInHeaderList) {
        return storage.getHeaderData(positionInHeaderList);
    }


    /* ************************ 尾巴 ItemFactory *************************** */

    @NonNull
    @Override
    public <ITEM extends AssemblyItem> FixedItemInfo addFooterItem(@NonNull AssemblyItemFactory<ITEM> itemFactory, @NonNull Object data) {
        return storage.addFooterItem(itemFactory, data);
    }

    @NonNull
    @Override
    public <ITEM extends AssemblyItem> FixedItemInfo addFooterItem(@NonNull AssemblyItemFactory<ITEM> itemFactory) {
        return storage.addHeaderItem(itemFactory);
    }

    @Override
    public void footerEnabledChanged(@NonNull FixedItemInfo footerItemInfo) {
        storage.footerEnabledChanged(footerItemInfo);
    }

    @Override
    public List<FixedItemInfo> getFooterItemList() {
        return storage.getFooterItemList();
    }

    @Override
    public int getFooterItemCount() {
        return storage.getFooterItemCount();
    }

    @Nullable
    public Object getFooterData(int positionInFooterList) {
        return storage.getFooterData(positionInFooterList);
    }


    /* ************************ 加载更多 *************************** */

    @NonNull
    @Override
    public <ITEM extends AssemblyLoadMoreItem> LoadMoreFixedItemInfo setLoadMoreItem(@NonNull AssemblyLoadMoreItemFactory<ITEM> itemFactory, @NonNull Object data) {
        return storage.setLoadMoreItem(itemFactory, data);
    }

    @NonNull
    @Override
    public <ITEM extends AssemblyLoadMoreItem> LoadMoreFixedItemInfo setLoadMoreItem(@NonNull AssemblyLoadMoreItemFactory<ITEM> itemFactory) {
        return storage.setLoadMoreItem(itemFactory);
    }

    @Override
    public void setDisableLoadMore(boolean disableLoadMore) {
        storage.setDisableLoadMore(disableLoadMore);
    }

    @Override
    public boolean hasLoadMoreFooter() {
        return storage.hasLoadMoreFooter();
    }

    @Override
    public void loadMoreFinished(boolean loadMoreEnd) {
        storage.loadMoreFinished(loadMoreEnd);
    }

    @Override
    public void loadMoreFailed() {
        storage.loadMoreFailed();
    }


    /* ************************ 数据列表 *************************** */

    @Override
    public List getDataList() {
        return storage.getDataList();
    }

    @Override
    public void setDataList(@Nullable List dataList) {
        storage.setDataList(dataList);
    }

    @Override
    public void addAll(@Nullable Collection collection) {
        storage.addAll(collection);
    }

    @Override
    public void addAll(@Nullable Object... items) {
        storage.addAll(items);
    }

    @Override
    public void insert(@NonNull Object object, int index) {
        storage.insert(object, index);
    }

    @Override
    public void remove(@NonNull Object object) {
        storage.remove(object);
    }

    @Override
    public void clear() {
        storage.clear();
    }

    @Override
    public void sort(@NonNull Comparator comparator) {
        storage.sort(comparator);
    }

    @Override
    public int getDataCount() {
        return storage.getDataCount();
    }

    @Nullable
    @Override
    public Object getData(int positionInDataList) {
        return storage.getData(positionInDataList);
    }


    /* ************************ 完整列表 *************************** */

    @Override
    public int getItemCount() {
        return storage.getItemCount();
    }

    @Nullable
    @Override
    public Object getItem(int position) {
        return storage.getItem(position);
    }

    @Override
    public int getPositionInPart(int position) {
        return storage.getPositionInPart(position);
    }


    /* ************************ 其它 *************************** */

    @Override
    public boolean isNotifyOnChange() {
        return storage.isNotifyOnChange();
    }

    @Override
    public void setNotifyOnChange(boolean notifyOnChange) {
        storage.setNotifyOnChange(notifyOnChange);
    }

    @Override
    public int getSpanSize(int position) {
        return storage.getSpanSize(position);
    }

    @Override
    public int getGroupCount() {
        return storage.getItemCount();
    }

    @Nullable
    @Override
    public Object getGroup(int groupPosition) {
        return storage.getItem(groupPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public int getGroupTypeCount() {
        return storage.getViewTypeCount();
    }

    @Override
    public int getGroupType(int groupPosition) {
        return storage.getItemViewType(groupPosition);
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return storage.getChildrenCount(groupPosition);
    }

    @Nullable
    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return storage.getChild(groupPosition, childPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public int getChildTypeCount() {
        return storage.getChildTypeCount();
    }

    @Override
    public int getChildType(int groupPosition, int childPosition) {
        return storage.getChildType(groupPosition, childPosition);
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

    private Item createGroupItem(ViewGroup parent, int viewType) {
        @Nullable
        Object itemObject = storage.getItemFactoryByViewType(viewType);
        if (itemObject instanceof AssemblyItemFactory) {
            AssemblyItemFactory itemFactory = (AssemblyItemFactory) itemObject;
            return itemFactory.dispatchCreateItem(parent);
        } else if (itemObject instanceof FixedItemInfo) {
            FixedItemInfo fixedItemInfo = (FixedItemInfo) itemObject;
            return fixedItemInfo.getItemFactory().dispatchCreateItem(parent);
        } else {
            throw new IllegalStateException(String.format("Unknown groupViewType: %d, itemFactory: %s",
                    viewType, itemObject != null ? itemObject.getClass().getName() : "null"));
        }
    }

    private void bindGroupItem(@NonNull Item groupItem, boolean isExpanded, int groupPosition) {
        Object group = getGroup(groupPosition);
        if (group != null) {
            groupItem.setExpanded(isExpanded);
            //noinspection unchecked
            groupItem.setData(groupPosition, group);
        }
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
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

    private Item createChildItem(ViewGroup parent, int viewType) {
        @Nullable
        Object itemObject = storage.getChildItemFactoryByViewType(viewType);
        if (itemObject instanceof AssemblyItemFactory) {
            AssemblyItemFactory itemFactory = (AssemblyItemFactory) itemObject;
            return itemFactory.dispatchCreateItem(parent);
        } else {
            throw new IllegalStateException(String.format("Unknown childViewType: %d, itemFactory: %s",
                    viewType, itemObject != null ? itemObject.getClass().getName() : "null"));
        }
    }

    private void bindChildItem(Item childItem, int groupPosition, int childPosition, boolean isLastChild) {
        Object child = getChild(groupPosition, childPosition);
        if (child != null) {
            childItem.setGroupPosition(groupPosition);
            childItem.setLastChild(isLastChild);
            //noinspection unchecked
            childItem.setData(childPosition, child);
        }
    }

    /**
     * 设置扩展回调
     */
    public void setExpandCallback(ExpandCallback expandCallback) {
        this.expandCallback = expandCallback;
    }

    public interface ExpandCallback {
        boolean hasStableIds();

        boolean isChildSelectable(int groupPosition, int childPosition);
    }
}
