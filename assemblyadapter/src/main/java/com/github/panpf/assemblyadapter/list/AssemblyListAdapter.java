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
import android.widget.BaseAdapter;

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

public class AssemblyListAdapter<DATA> extends BaseAdapter {

    @NonNull
    private final ItemManager<ItemFactory<?>> itemManager;
    @NonNull
    private final DataManager<DATA> dataManager;

    private boolean notifyOnChange = true;

    @NonNull
    private final DataManager.Callback dataCallback = () -> {
        if (isNotifyOnChange()) {
            notifyDataSetChanged();
        }
    };


    public AssemblyListAdapter(@NonNull List<ItemFactory<?>> itemFactoryList) {
        this.itemManager = new ItemManager<>(itemFactoryList);
        this.dataManager = new DataManager<>(dataCallback);
    }

    public AssemblyListAdapter(@NonNull List<ItemFactory<?>> itemFactoryList, @Nullable List<DATA> dataList) {
        this.itemManager = new ItemManager<>(itemFactoryList);
        this.dataManager = new DataManager<>(dataCallback, dataList);
    }

    public AssemblyListAdapter(@NonNull List<ItemFactory<?>> itemFactoryList, @Nullable DATA[] dataArray) {
        this.itemManager = new ItemManager<>(itemFactoryList);
        this.dataManager = new DataManager<>(dataCallback, dataArray);
    }


    public int getItemCount() {
        return dataManager.getDataCount();
    }

    @Nullable
    @Override
    public DATA getItem(int position) {
        return dataManager.getData(position);
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
        return itemManager.getItemTypeCount();
    }

    @Override
    public int getItemViewType(int position) {
        return itemManager.getItemTypeByData(getItem(position));
    }

    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        DATA data = getItem(position);
        Item<Object> item;
        if (convertView == null) {
            //noinspection unchecked
            item = (Item<Object>) itemManager.matchItemFactoryByData(data).dispatchCreateItem(parent);
            convertView = item.getItemView();
            convertView.setTag(R.id.aa_tag_item, item);
        } else {
            //noinspection unchecked
            item = (Item<Object>) convertView.getTag(R.id.aa_tag_item);
        }
        item.dispatchBindData(position, data);
        return convertView;
    }


    @NonNull
    public List<DATA> getDataListSnapshot() {
        return dataManager.getDataListSnapshot();
    }

    public void setDataList(@Nullable List<DATA> datas) {
        dataManager.setDataList(datas);
    }

    public boolean addData(@Nullable DATA data) {
        return dataManager.addData(data);
    }

    public void addData(int index, @Nullable DATA data) {
        dataManager.addData(index, data);
    }

    public boolean addAllData(@Nullable Collection<DATA> datas) {
        return dataManager.addAllData(datas);
    }

    @SafeVarargs
    public final boolean addAllData(@Nullable DATA... datas) {
        return dataManager.addAllData(datas);
    }

    public boolean removeData(@Nullable DATA data) {
        return dataManager.removeData(data);
    }

    public DATA removeData(int index) {
        return dataManager.removeData(index);
    }

    public boolean removeAllData(@NonNull Collection<DATA> datas) {
        return dataManager.removeAllData(datas);
    }

    public void clearData() {
        dataManager.clearData();
    }

    public void sortData(@NonNull Comparator<DATA> comparator) {
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
}
