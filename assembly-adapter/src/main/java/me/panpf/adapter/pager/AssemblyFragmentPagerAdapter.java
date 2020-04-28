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

import androidx.annotation.IntDef;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.PagerAdapter;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.List;

/**
 * Combined {@link FragmentPagerAdapter}, supports combining many types of items, supports head and tail
 */
// todo support title
public class AssemblyFragmentPagerAdapter extends FragmentPagerAdapter implements AssemblyFragmentAdapter {

    @NonNull
    private FragmentItemManager itemManager;

    private int notifyNumber = 0;
    @Nullable
    private SparseIntArray notifyNumberPool;

    public AssemblyFragmentPagerAdapter(@NonNull FragmentManager fm, @Behavior int behavior) {
        super(fm, behavior);
        this.itemManager = new FragmentItemManager(this);
    }

    /**
     * @deprecated use {@link #AssemblyFragmentPagerAdapter(FragmentManager, int)} with
     * {@link #BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT}
     */
    @Deprecated
    public AssemblyFragmentPagerAdapter(@NonNull FragmentManager fm) {
        super(fm);
        this.itemManager = new FragmentItemManager(this);
    }

    public AssemblyFragmentPagerAdapter(@NonNull FragmentManager fm, @Behavior int behavior, @NonNull List dataList) {
        super(fm, behavior);
        this.itemManager = new FragmentItemManager(this, dataList);
    }

    /**
     * @deprecated use {@link #AssemblyFragmentPagerAdapter(FragmentManager, int, List)} with
     * {@link #BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT}
     */
    @Deprecated
    public AssemblyFragmentPagerAdapter(@NonNull FragmentManager fm, @NonNull List dataList) {
        super(fm);
        this.itemManager = new FragmentItemManager(this, dataList);
    }

    public AssemblyFragmentPagerAdapter(@NonNull FragmentManager fm, @Behavior int behavior, @Nullable Object[] dataArray) {
        super(fm, behavior);
        this.itemManager = new FragmentItemManager(this, dataArray);
    }

    /**
     * @deprecated use {@link #AssemblyFragmentPagerAdapter(FragmentManager, int, Object[])} with
     * {@link #BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT}
     */
    @Deprecated
    public AssemblyFragmentPagerAdapter(@NonNull FragmentManager fm, @Nullable Object[] dataArray) {
        super(fm);
        this.itemManager = new FragmentItemManager(this, dataArray);
    }


    @Override
    public void addItemFactory(@NonNull AssemblyFragmentItemFactory itemFactory) {
        itemManager.addItemFactory(itemFactory);
    }

    @NonNull
    @Override
    public List<AssemblyFragmentItemFactory> getItemFactoryList() {
        return itemManager.getItemFactoryList();
    }

    @NonNull
    @Override
    public <DATA> FragmentFixedItem<DATA> addHeaderItem(@NonNull FragmentFixedItem<DATA> fixedItem) {
        return itemManager.addHeaderItem(fixedItem);
    }

    @NonNull
    @Override
    public <DATA> FragmentFixedItem<DATA> addHeaderItem(@NonNull AssemblyFragmentItemFactory<DATA> headerFactory, @Nullable DATA data) {
        return itemManager.addHeaderItem(headerFactory, data);
    }

    @NonNull
    @Override
    public <DATA> FragmentFixedItem<DATA> addHeaderItem(@NonNull AssemblyFragmentItemFactory<DATA> headerFactory) {
        return itemManager.addHeaderItem(headerFactory);
    }

    @NonNull
    @Override
    public <DATA> FragmentFixedItem<DATA> getHeaderItemByFactoryClass(@NonNull Class<? extends AssemblyFragmentItemFactory<DATA>> clazz, int number) {
        return itemManager.getHeaderItemManager().getItemByFactoryClass(clazz, number);
    }

    @NonNull
    @Override
    public <DATA> FragmentFixedItem<DATA> getHeaderItemByFactoryClass(@NonNull Class<? extends AssemblyFragmentItemFactory<DATA>> clazz) {
        return itemManager.getHeaderItemManager().getItemByFactoryClass(clazz);
    }

    @NonNull
    @Override
    public FragmentFixedItem getHeaderItem(int positionInHeaderItemList) {
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
    public int getHeaderCount() {
        return itemManager.getHeaderItemManager().getItemCount();
    }

    @Nullable
    @Override
    public Object getHeaderData(int positionInHeaderList) {
        return itemManager.getHeaderItemManager().getItem(positionInHeaderList).getData();
    }

    @NonNull
    @Override
    public <DATA> FragmentFixedItem<DATA> addFooterItem(@NonNull FragmentFixedItem<DATA> fixedItem) {
        return itemManager.addHeaderItem(fixedItem);
    }

    @NonNull
    @Override
    public <DATA> FragmentFixedItem<DATA> addFooterItem(@NonNull AssemblyFragmentItemFactory<DATA> footerFactory, @Nullable DATA data) {
        return itemManager.addFooterItem(footerFactory, data);
    }

    @NonNull
    @Override
    public <DATA> FragmentFixedItem<DATA> addFooterItem(@NonNull AssemblyFragmentItemFactory<DATA> footerFactory) {
        return itemManager.addFooterItem(footerFactory);
    }

    @NonNull
    @Override
    public <DATA> FragmentFixedItem<DATA> getFooterItemByFactoryClass(@NonNull Class<? extends AssemblyFragmentItemFactory<DATA>> clazz, int number) {
        return itemManager.getFooterItemManager().getItemByFactoryClass(clazz, number);
    }

    @NonNull
    @Override
    public <DATA> FragmentFixedItem<DATA> getFooterItemByFactoryClass(@NonNull Class<? extends AssemblyFragmentItemFactory<DATA>> clazz) {
        return itemManager.getFooterItemManager().getItemByFactoryClass(clazz);
    }

    @NonNull
    @Override
    public FragmentFixedItem getFooterItem(int positionInFooterItemList) {
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
    public int getFooterCount() {
        return itemManager.getFooterItemManager().getItemCount();
    }

    @Nullable
    @Override
    public Object getFooterData(int positionInFooterList) {
        return itemManager.getFooterItemManager().getItem(positionInFooterList).getData();
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
    public int getDataCount() {
        return itemManager.getDataCount();
    }

    @Nullable
    @Override
    public Object getData(int positionInDataList) {
        return itemManager.getData(positionInDataList);
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
    public int getPositionInPart(int position) {
        return itemManager.getPositionInPart(position);
    }

    @NonNull
    @Override
    public AssemblyFragmentItemFactory getItemFactoryByPosition(int position) {
        return itemManager.getItemFactoryByPosition(position);
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

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({BEHAVIOR_SET_USER_VISIBLE_HINT, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT})
    private @interface Behavior {
    }
}
