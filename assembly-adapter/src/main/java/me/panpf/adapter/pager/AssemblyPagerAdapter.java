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

package me.panpf.adapter.pager;

import android.util.SparseIntArray;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.PagerAdapter;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;

/**
 * General combination type {@link PagerAdapter}, support combining multiple items, support head and tail
 */
public class AssemblyPagerAdapter extends PagerAdapter {

    @NonNull
    private PagerItemManager itemManager;

    private int notifyNumber = 0;
    @Nullable
    private SparseIntArray notifyNumberPool;

    public AssemblyPagerAdapter() {
        this.itemManager = new PagerItemManager(this);
    }

    public AssemblyPagerAdapter(@Nullable List dataList) {
        this.itemManager = new PagerItemManager(this, dataList);
    }

    public AssemblyPagerAdapter(@Nullable Object[] dataArray) {
        this.itemManager = new PagerItemManager(this, dataArray);
    }


    public void addItemFactory(@NonNull AssemblyPagerItemFactory itemFactory) {
        itemManager.addItemFactory(itemFactory);
    }


    @NonNull
    public PagerFixedItem addHeaderItem(@NonNull AssemblyPagerItemFactory itemFactory, @Nullable Object data) {
        return itemManager.addHeaderItem(itemFactory, data);
    }

    @NonNull
    public PagerFixedItem addHeaderItem(@NonNull AssemblyPagerItemFactory itemFactory) {
        return itemManager.addHeaderItem(itemFactory);
    }

    @NonNull
    public PagerFixedItemManager getHeaderItemManager() {
        return itemManager.getHeaderItemManager();
    }

    public int getHeaderEnabledItemCount() {
        return itemManager.getHeaderItemManager().getEnabledItemCount();
    }


    @NonNull
    public PagerFixedItem addFooterItem(@NonNull AssemblyPagerItemFactory itemFactory, @Nullable Object data) {
        return itemManager.addFooterItem(itemFactory, data);
    }

    @NonNull
    public PagerFixedItem addFooterItem(@NonNull AssemblyPagerItemFactory itemFactory) {
        return itemManager.addFooterItem(itemFactory);
    }

    @NonNull
    public PagerFixedItemManager getFooterItemManager() {
        return itemManager.getFooterItemManager();
    }

    public int getFooterEnabledItemCount() {
        return itemManager.getFooterItemManager().getEnabledItemCount();
    }


    @Nullable
    public List getDataList() {
        return itemManager.getDataList();
    }

    public void setDataList(@Nullable List dataList) {
        itemManager.setDataList(dataList);
    }

    public void addAll(@Nullable Collection collection) {
        itemManager.addAll(collection);
    }

    public void addAll(@Nullable Object... items) {
        itemManager.addAll(items);
    }

    public void insert(@NonNull Object object, int index) {
        itemManager.insert(object, index);
    }

    public void remove(@NonNull Object object) {
        itemManager.remove(object);
    }

    public void clear() {
        itemManager.clear();
    }

    public void sort(@NonNull Comparator comparator) {
        itemManager.sort(comparator);
    }

    public int getDataCount() {
        return itemManager.getDataCount();
    }

    @Override
    public int getCount() {
        return itemManager.getItemCount();
    }

    public int getPositionInPart(int position) {
        return itemManager.getPositionInPart(position);
    }


    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        AssemblyPagerItemFactory itemFactory = itemManager.getItemFactoryByPosition(position);
        Object itemData = itemManager.getItemDataByPosition(position);
        //noinspection unchecked
        View itemView = itemFactory.dispatchCreateView(container.getContext(), container, position, itemData);
        container.addView(itemView);
        return itemView;
    }

    public boolean isNotifyOnChange() {
        return itemManager.isNotifyOnChange();
    }

    public void setNotifyOnChange(boolean notifyOnChange) {
        itemManager.setNotifyOnChange(notifyOnChange);
    }

    public boolean isEnabledPositionNoneOnNotifyDataSetChanged() {
        return notifyNumberPool != null;
    }

    public void setEnabledPositionNoneOnNotifyDataSetChanged(boolean enabled) {
        if (enabled) {
            notifyNumberPool = new SparseIntArray();
            notifyNumber = 0;
        } else {
            notifyNumberPool = null;
        }
    }

    @Override
    public void notifyDataSetChanged() {
        if (notifyNumberPool != null) notifyNumber++;
        super.notifyDataSetChanged();
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        if (notifyNumberPool != null && notifyNumberPool.get(object.hashCode()) != notifyNumber) {
            notifyNumberPool.put(object.hashCode(), notifyNumber);
            return PagerAdapter.POSITION_NONE;
        }
        return super.getItemPosition(object);
    }

    @NonNull
    public AssemblyPagerItemFactory getItemFactoryByPosition(int position) {
        return itemManager.getItemFactoryByPosition(position);
    }

    public boolean isHeaderItem(int position) {
        return itemManager.isHeaderItem(position);
    }

    public boolean isBodyItem(int position) {
        return itemManager.isBodyItem(position);
    }

    public boolean isFooterItem(int position) {
        return itemManager.isFooterItem(position);
    }
}
