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
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;

import me.panpf.adapter.more.AssemblyLoadMoreItemFactory;
import me.panpf.adapter.more.LoadMoreFixedItemInfo;
import me.panpf.adapter.recycler.RecyclerItemFactoryWrapper;
import me.panpf.adapter.recycler.RecyclerLoadMoreItemFactoryWrapper;

/**
 * 通用组合式 {@link RecyclerView.Adapter}，支持组合式多类型 item，支持头、尾巴以及加载更多
 */
@SuppressWarnings("unused")
public class AssemblyRecyclerAdapter extends RecyclerView.Adapter implements AssemblyAdapter {
    @NonNull
    private ItemStorage storage;

    public AssemblyRecyclerAdapter() {
        this.storage = new ItemStorage(this);
    }

    public AssemblyRecyclerAdapter(@Nullable List dataList) {
        this.storage = new ItemStorage(this, dataList);
    }

    public AssemblyRecyclerAdapter(@Nullable Object[] dataArray) {
        this.storage = new ItemStorage(this, dataArray);
    }

    @NonNull
    @Override
    public <ITEM extends AssemblyItem> FixedItemInfo addHeaderItem(@NonNull AssemblyItemFactory<ITEM> itemFactory, @Nullable Object data) {
        return storage.addHeaderItem(new RecyclerItemFactoryWrapper(itemFactory), data);
    }

    @Override
    public <ITEM extends AssemblyItem> void addItemFactory(@NonNull AssemblyItemFactory<ITEM> itemFactory) {
        storage.addItemFactory(new RecyclerItemFactoryWrapper(itemFactory));
    }

    @NonNull
    @Override
    public <ITEM extends AssemblyItem> FixedItemInfo addFooterItem(@NonNull AssemblyItemFactory<ITEM> itemFactory, @Nullable Object data) {
        return storage.addFooterItem(new RecyclerItemFactoryWrapper(itemFactory), data);
    }

    @NonNull
    @Override
    public LoadMoreFixedItemInfo setLoadMoreItem(@NonNull AssemblyLoadMoreItemFactory itemFactory, @Nullable Object data) {
        return storage.setLoadMoreItem(new RecyclerLoadMoreItemFactoryWrapper(itemFactory), data);
    }

    @NonNull
    @Override
    public LoadMoreFixedItemInfo setLoadMoreItem(@NonNull AssemblyLoadMoreItemFactory itemFactory) {
        return storage.setLoadMoreItem(new RecyclerLoadMoreItemFactoryWrapper(itemFactory));
    }

    @Override
    public void addAll(@NonNull Collection collection) {
        storage.addAll(collection);
    }

    @Override
    public void addAll(@NonNull Object... items) {
        storage.addAll(items);
    }

    @Override
    public void insert(@Nullable Object object, int index) {
        storage.insert(object, index);
    }

    @Override
    public void remove(@Nullable Object object) {
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
    public void setDisableLoadMore(boolean disableLoadMore) {
        storage.setDisableLoadMore(disableLoadMore);
    }

    @Override
    public void loadMoreFinished(boolean loadMoreEnd) {
        storage.loadMoreFinished(loadMoreEnd);
    }

    @Override
    public void loadMoreFailed() {
        storage.loadMoreFailed();
    }

    @Override
    public void headerEnabledChanged(@NonNull FixedItemInfo fixedItemInfo) {
        storage.headerEnabledChanged(fixedItemInfo);
    }

    @Override
    public void footerEnabledChanged(@NonNull FixedItemInfo fixedItemInfo) {
        storage.footerEnabledChanged(fixedItemInfo);
    }

    @Nullable
    @Override
    public List<FixedItemInfo> getHeaderItemList() {
        return storage.getHeaderItemList();
    }

    @Nullable
    @Override
    public List<ItemFactory> getItemFactoryList() {
        return storage.getItemFactoryList();
    }

    @Nullable
    @Override
    public List<FixedItemInfo> getFooterItemList() {
        return storage.getFooterItemList();
    }

    @Nullable
    @Override
    public List getDataList() {
        return storage.getDataList();
    }

    @Override
    public void setDataList(@Nullable List dataList) {
        storage.setDataList(dataList);
    }

    @Override
    public int getHeaderItemCount() {
        return storage.getHeaderItemCount();
    }

    @Override
    public int getItemFactoryCount() {
        return storage.getItemFactoryCount();
    }

    @Override
    public int getFooterItemCount() {
        return storage.getFooterItemCount();
    }

    @Override
    public boolean hasLoadMoreFooter() {
        return storage.hasLoadMoreFooter();
    }

    @Override
    public int getDataCount() {
        return storage.getDataCount();
    }

    @Override
    public boolean isNotifyOnChange() {
        return storage.isNotifyOnChange();
    }

    @Override
    public void setNotifyOnChange(boolean notifyOnChange) {
        storage.setNotifyOnChange(notifyOnChange);
    }

    @Override
    public int getPositionInPart(int position) {
        return storage.getPositionInPart(position);
    }

    @Override
    public int getItemCount() {
        return storage.getCount();
    }

    @Nullable
    @Override
    public Object getItem(int position) {
        return storage.getItem(position);
    }

    @Nullable
    @Override
    public Object getHeaderItem(int positionInHeaderList) {
        return storage.getHeaderItem(positionInHeaderList);
    }

    @Nullable
    @Override
    public Object getDataItem(int positionInDataList) {
        return storage.getDataItem(positionInDataList);
    }

    @Nullable
    @Override
    public Object getFooterItem(int positionInFooterList) {
        return storage.getFooterItem(positionInFooterList);
    }

    @Override
    public int getSpanSize(int position) {
        return storage.getSpanSize(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return storage.getItemViewType(position);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        @Nullable
        Object itemObject = storage.getItemFactoryByViewType(viewType);
        if (itemObject instanceof RecyclerItemFactoryWrapper) {
            RecyclerItemFactoryWrapper itemFactory = (RecyclerItemFactoryWrapper) itemObject;
            return itemFactory.dispatchCreateItem(parent);
        } else if (itemObject instanceof FixedItemInfo) {
            ItemFactory itemFactory = ((FixedItemInfo) itemObject).getItemFactory();
            if (itemFactory instanceof RecyclerItemFactoryWrapper) {
                return ((RecyclerItemFactoryWrapper) itemFactory).dispatchCreateItem(parent);
            } else if (itemFactory instanceof RecyclerLoadMoreItemFactoryWrapper) {
                return ((RecyclerLoadMoreItemFactoryWrapper) itemFactory).dispatchCreateItem(parent);
            } else {
                throw new IllegalStateException(String.format("Unknown itemFactory: %s", itemFactory.toString()));
            }
        } else {
            throw new IllegalStateException(String.format("Unknown viewType: %d, itemFactory: %s", viewType, itemObject != null ? itemObject.toString() : "null"));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        if (viewHolder instanceof Item) {
            //noinspection unchecked
            ((Item) viewHolder).setData(position, getItem(position));
        }
    }
}
