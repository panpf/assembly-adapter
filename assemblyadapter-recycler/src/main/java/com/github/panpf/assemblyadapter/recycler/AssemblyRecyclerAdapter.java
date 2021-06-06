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

package com.github.panpf.assemblyadapter.recycler;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.github.panpf.assemblyadapter.DataManager;
import com.github.panpf.assemblyadapter.Item;
import com.github.panpf.assemblyadapter.ItemFactory;
import com.github.panpf.assemblyadapter.ItemManager;
import com.github.panpf.assemblyadapter.recycler.internal.AssemblyRecyclerItem;

import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AssemblyRecyclerAdapter<DATA> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    @NonNull
    private final ItemManager<ItemFactory<?>> itemManager;
    @NonNull
    private final DataManager<DATA> dataManager;

    @Nullable
    private Map<Class<? extends ItemFactory<?>>, ItemSpan> itemSpanMapInGridLayoutManager;
    private boolean notifyOnChange = true;

    @NonNull
    private final DataManager.Callback dataCallback = () -> {
        if (notifyOnChange) {
            notifyDataSetChanged();
        }
    };


    public AssemblyRecyclerAdapter(@NonNull List<ItemFactory<?>> itemFactoryList) {
        this.itemManager = new ItemManager<>(itemFactoryList);
        this.dataManager = new DataManager<>(dataCallback);
    }

    public AssemblyRecyclerAdapter(@NonNull List<ItemFactory<?>> itemFactoryList, @Nullable List<DATA> dataList) {
        this.itemManager = new ItemManager<>(itemFactoryList);
        this.dataManager = new DataManager<>(dataCallback, dataList);
    }

    public AssemblyRecyclerAdapter(@NonNull List<ItemFactory<?>> itemFactoryList, @Nullable DATA[] dataArray) {
        this.itemManager = new ItemManager<>(itemFactoryList);
        this.dataManager = new DataManager<>(dataCallback, dataArray);
    }


    @Override
    public int getItemCount() {
        return dataManager.getDataCount();
    }

    @Nullable
    public DATA getItem(int position) {
        return dataManager.getData(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return itemManager.getItemTypeByData(getItem(position));
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemFactory<?> recyclerItemFactory = itemManager.getItemFactoryByItemType(viewType);
        Item<?> item = recyclerItemFactory.dispatchCreateItem(parent);
        AssemblyRecyclerItem<?> recyclerItem = new AssemblyRecyclerItem<>(item);
        applyItemSpanInGridLayoutManager(parent, recyclerItemFactory, recyclerItem);
        return recyclerItem;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        if (viewHolder instanceof AssemblyRecyclerItem) {
            //noinspection unchecked
            ((AssemblyRecyclerItem<Object>) viewHolder).dispatchBindData(position, getItem(position));
        }
    }


    @NonNull
    public AssemblyRecyclerAdapter<DATA> setItemSpanInGridLayoutManager(@NonNull Class<? extends ItemFactory<?>> itemFactoryClass, @NonNull ItemSpan itemSpan) {
        if (itemSpanMapInGridLayoutManager == null) {
            itemSpanMapInGridLayoutManager = new HashMap<>();
        }
        itemSpanMapInGridLayoutManager.put(itemFactoryClass, itemSpan);
        if (notifyOnChange) {
            notifyDataSetChanged();
        }
        return this;
    }

    @NonNull
    public AssemblyRecyclerAdapter<DATA> setItemSpanMapInGridLayoutManager(@Nullable Map<Class<? extends ItemFactory<?>>, ItemSpan> itemSpanMap) {
        this.itemSpanMapInGridLayoutManager = itemSpanMap;
        if (notifyOnChange) {
            notifyDataSetChanged();
        }
        return this;
    }

    @Nullable
    public Map<Class<? extends ItemFactory<?>>, ItemSpan> getItemSpanMapInGridLayoutManager() {
        return itemSpanMapInGridLayoutManager;
    }

    private void applyItemSpanInGridLayoutManager(@NonNull ViewGroup parent,
                                                  @NonNull ItemFactory<?> recyclerItemFactory,
                                                  @NonNull AssemblyRecyclerItem<?> recyclerItem) {
        if (itemSpanMapInGridLayoutManager != null && !itemSpanMapInGridLayoutManager.isEmpty() && parent instanceof RecyclerView) {
            RecyclerView.LayoutManager layoutManager = ((RecyclerView) parent).getLayoutManager();
            //noinspection StatementWithEmptyBody
            if (layoutManager instanceof AssemblyGridLayoutManager) {
                // No need to do
            } else if (layoutManager instanceof AssemblyStaggeredGridLayoutManager) {
                ItemSpan itemSpan = itemSpanMapInGridLayoutManager.get(recyclerItemFactory.getClass());
                if (itemSpan != null && itemSpan.span < 0) {
                    View itemView = recyclerItem.getItemView();
                    ViewGroup.LayoutParams layoutParams = itemView.getLayoutParams();
                    if (layoutParams instanceof StaggeredGridLayoutManager.LayoutParams) {
                        ((StaggeredGridLayoutManager.LayoutParams) layoutParams).setFullSpan(true);
                        itemView.setLayoutParams(layoutParams);
                    }
                }
            } else {
                throw new IllegalArgumentException("Since itemSpan is set, the layoutManager of RecyclerView must be AssemblyGridLayoutManager or AssemblyStaggeredGridLayoutManager");
            }
        }
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
