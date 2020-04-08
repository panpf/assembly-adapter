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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.PagerAdapter;

import java.util.List;

/**
 * Combined {@link FragmentPagerAdapter}, supports combining many types of items, supports head and tail
 */
public class AssemblyFragmentPagerAdapter extends FragmentPagerAdapter implements AssemblyFragmentAdapter {

    @NonNull
    private FragmentItemManager itemManager;

    private int notifyNumber = 0;
    @Nullable
    private SparseIntArray notifyNumberPool;

    // todo 增加对 behavior i支持
    public AssemblyFragmentPagerAdapter(@NonNull FragmentManager fm) {
        super(fm);
        this.itemManager = new FragmentItemManager(this);
    }

    // todo 增加对 behavior i支持
    public AssemblyFragmentPagerAdapter(@NonNull FragmentManager fm, @NonNull List dataList) {
        super(fm);
        this.itemManager = new FragmentItemManager(this, dataList);
    }

    // todo 增加对 behavior i支持
    public AssemblyFragmentPagerAdapter(@NonNull FragmentManager fm, @Nullable Object[] dataArray) {
        super(fm);
        this.itemManager = new FragmentItemManager(this, dataArray);
    }


    @Override
    public void addItemFactory(@NonNull AssemblyFragmentItemFactory itemFactory) {
        itemManager.addItemFactory(itemFactory);
    }


    @Override
    public void addHeaderItem(@NonNull AssemblyFragmentItemFactory headerFactory, @Nullable Object data) {
        itemManager.addHeaderItem(headerFactory, data);
    }

    @Override
    public void addHeaderItem(@NonNull AssemblyFragmentItemFactory headerFactory) {
        itemManager.addHeaderItem(headerFactory);
    }

    @NonNull
    @Override
    public FragmentItemHolderManager getHeaderItemManager() {
        return itemManager.getHeaderItemManager();
    }

    @Override
    public int getHeaderItemCount() {
        return itemManager.getHeaderItemManager().getItemCount();
    }


    @Override
    public void addFooterItem(@NonNull AssemblyFragmentItemFactory footerFactory, @Nullable Object data) {
        itemManager.addFooterItem(footerFactory, data);
    }

    @Override
    public void addFooterItem(@NonNull AssemblyFragmentItemFactory footerFactory) {
        itemManager.addFooterItem(footerFactory);
    }

    @NonNull
    @Override
    public FragmentItemHolderManager getFooterItemManager() {
        return itemManager.getFooterItemManager();
    }

    @Override
    public int getFooterItemCount() {
        return itemManager.getFooterItemManager().getItemCount();
    }


    @Nullable
    @Override
    public List getDataList() {
        return itemManager.getDataList();
    }

    public void setDataList(@Nullable List dataList) {
        itemManager.setDataList(dataList);
    }

    @Override
    public int getDataCount() {
        return itemManager.getDataCount();
    }


    @Override
    public int getPositionInPart(int position) {
        return itemManager.getPositionInPart(position);
    }

    @Override
    public int getCount() {
        return itemManager.getItemCount();
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        AssemblyFragmentItemFactory itemFactory = itemManager.getItemFactoryByPosition(position);
        Object itemData = itemManager.getItemDataByPosition(position);
        //noinspection unchecked
        return itemFactory.dispatchCreateFragment(position, itemData);
    }

    @NonNull
    @Override
    public AssemblyFragmentItemFactory getItemFactoryByPosition(int position) {
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
}
