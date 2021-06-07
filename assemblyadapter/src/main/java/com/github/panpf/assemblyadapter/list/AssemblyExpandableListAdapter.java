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

package com.github.panpf.assemblyadapter.list;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.github.panpf.assemblyadapter.Item;
import com.github.panpf.assemblyadapter.ItemFactory;
import com.github.panpf.assemblyadapter.R;
import com.github.panpf.assemblyadapter.internal.DataManager;
import com.github.panpf.assemblyadapter.internal.ItemManager;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;

public class AssemblyExpandableListAdapter<GROUP_DATA, CHILD_DATA> extends BaseExpandableListAdapter {

    @NonNull
    private final ItemManager<ItemFactory<?>> itemManager;
    @NonNull
    private final DataManager<GROUP_DATA> dataManager;
    @Nullable
    private Callback callback;

    private boolean notifyOnChange = true;

    private final DataManager.Callback dataCallback = () -> {
        if (isNotifyOnChange()) {
            notifyDataSetChanged();
        }
    };


    public AssemblyExpandableListAdapter(@NonNull List<ItemFactory<?>> itemFactoryList) {
        this.itemManager = new ItemManager<>(itemFactoryList);
        this.dataManager = new DataManager<>(dataCallback);
    }

    public AssemblyExpandableListAdapter(@NonNull List<ItemFactory<?>> itemFactoryList, @Nullable List<GROUP_DATA> dataList) {
        this.itemManager = new ItemManager<>(itemFactoryList);
        this.dataManager = new DataManager<>(dataCallback, dataList);
    }

    public AssemblyExpandableListAdapter(@NonNull List<ItemFactory<?>> itemFactoryList, @Nullable GROUP_DATA[] dataArray) {
        this.itemManager = new ItemManager<>(itemFactoryList);
        this.dataManager = new DataManager<>(dataCallback, dataArray);
    }


    public int getItemCount() {
        return dataManager.getDataCount();
    }

    @Nullable
    public GROUP_DATA getItem(int position) {
        return dataManager.getData(position);
    }


    @Override
    public int getGroupCount() {
        return getItemCount();
    }

    @Nullable
    @Override
    public GROUP_DATA getGroup(int groupPosition) {
        return getItem(groupPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public int getGroupTypeCount() {
        return itemManager.getItemTypeCount();
    }

    @Override
    public int getGroupType(int groupPosition) {
        return itemManager.getItemTypeByData(getGroup(groupPosition));
    }


    @Override
    public int getChildrenCount(int groupPosition) {
        GROUP_DATA group = getGroup(groupPosition);
        return group instanceof AssemblyExpandableGroup ? ((AssemblyExpandableGroup) group).getChildCount() : 0;
    }

    @Nullable
    @Override
    public CHILD_DATA getChild(int groupPosition, int childPosition) {
        GROUP_DATA group = getGroup(groupPosition);
        //noinspection unchecked
        return group instanceof AssemblyExpandableGroup ? (CHILD_DATA) ((AssemblyExpandableGroup) group).getChild(childPosition) : null;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public int getChildTypeCount() {
        return itemManager.getItemTypeCount();
    }

    @Override
    public int getChildType(int groupPosition, int childPosition) {
        return itemManager.getItemTypeByData(getChild(groupPosition, childPosition));
    }

    @Override
    public boolean hasStableIds() {
        return callback != null && callback.hasStableIds();
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return callback != null && callback.isChildSelectable(groupPosition, childPosition);
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        GROUP_DATA groupData = getGroup(groupPosition);
        Item<Object> item;
        if (convertView == null) {
            //noinspection unchecked
            item = (Item<Object>) itemManager.matchItemFactoryByData(groupData).dispatchCreateItem(parent);
            convertView = item.getItemView();
            convertView.setTag(R.id.aa_tag_item, item);
        } else {
            //noinspection unchecked
            item = (Item<Object>) convertView.getTag(R.id.aa_tag_item);
        }
        if (item instanceof AssemblyExpandableItem) {
            AssemblyExpandableItem<Object> assemblyExpandableItem = (AssemblyExpandableItem<Object>) item;
            assemblyExpandableItem.setGroupPosition(groupPosition);
            assemblyExpandableItem.setExpanded(isExpanded);
            assemblyExpandableItem.setChildPosition(-1);
            assemblyExpandableItem.setLastChild(false);
        }
        item.dispatchBindData(groupPosition, groupData);
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, @Nullable View convertView, @NonNull ViewGroup parent) {
        CHILD_DATA childData = getChild(groupPosition, childPosition);
        Item<Object> item;
        if (convertView == null) {
            //noinspection unchecked
            item = (Item<Object>) itemManager.matchItemFactoryByData(childData).dispatchCreateItem(parent);
            convertView = item.getItemView();
            convertView.setTag(R.id.aa_tag_item, item);
        } else {
            //noinspection unchecked
            item = (Item<Object>) convertView.getTag(R.id.aa_tag_item);
        }
        if (item instanceof AssemblyExpandableItem) {
            AssemblyExpandableItem<Object> assemblyExpandableItem = (AssemblyExpandableItem<Object>) item;
            assemblyExpandableItem.setGroupPosition(groupPosition);
            assemblyExpandableItem.setExpanded(false);
            assemblyExpandableItem.setChildPosition(childPosition);
            assemblyExpandableItem.setLastChild(isLastChild);
        }
        item.dispatchBindData(childPosition, childData);
        return convertView;
    }


    @NonNull
    public List<GROUP_DATA> getDataListSnapshot() {
        return dataManager.getDataListSnapshot();
    }

    public void setDataList(@Nullable List<GROUP_DATA> datas) {
        dataManager.setDataList(datas);
    }

    public boolean addData(@Nullable GROUP_DATA data) {
        return dataManager.addData(data);
    }

    public void addData(int index, @Nullable GROUP_DATA data) {
        dataManager.addData(index, data);
    }

    public boolean addAllData(@Nullable Collection<GROUP_DATA> datas) {
        return dataManager.addAllData(datas);
    }

    @SafeVarargs
    public final boolean addAllData(@Nullable GROUP_DATA... datas) {
        return dataManager.addAllData(datas);
    }

    public boolean removeData(@Nullable GROUP_DATA data) {
        return dataManager.removeData(data);
    }

    public GROUP_DATA removeData(int index) {
        return dataManager.removeData(index);
    }

    public boolean removeAllData(@NonNull Collection<GROUP_DATA> datas) {
        return dataManager.removeAllData(datas);
    }

    public void clearData() {
        dataManager.clearData();
    }

    public void sortData(@NonNull Comparator<GROUP_DATA> comparator) {
        dataManager.sortData(comparator);
    }


    public boolean isNotifyOnChange() {
        return notifyOnChange;
    }

    public void setNotifyOnChange(boolean notifyOnChange) {
        this.notifyOnChange = notifyOnChange;
    }


    @NonNull
    public ItemFactory<?> getItemFactoryByItemType(int itemType) {
        return itemManager.getItemFactoryByItemType(itemType);
    }

    @NonNull
    public ItemFactory<?> getItemFactoryByPosition(int position) {
        return getItemFactoryByItemType(itemManager.getItemTypeByData(getItem(position)));
    }


    public void setCallback(@Nullable Callback callback) {
        this.callback = callback;
    }

    public interface Callback {
        boolean hasStableIds();

        boolean isChildSelectable(int groupPosition, int childPosition);
    }
}
