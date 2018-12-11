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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.PagerAdapter;
import android.util.SparseIntArray;

import java.util.List;

/**
 * 通用组合式 {@link FragmentStatePagerAdapter}，支持组合式多类型 item，支持头、尾巴以及加载更多
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public class AssemblyFragmentStatePagerAdapter extends FragmentStatePagerAdapter implements AssemblyFragmentAdapter {

    @NonNull
    private FragmentItemStorage storage;
    @NonNull
    private FragmentItemActor actor = new FragmentItemActor(this);

    private int notifyNumber = 0;
    @Nullable
    private SparseIntArray notifyNumberPool;

    public AssemblyFragmentStatePagerAdapter(@NonNull FragmentManager fm) {
        super(fm);
        this.storage = new FragmentItemStorage(this);
    }

    public AssemblyFragmentStatePagerAdapter(@NonNull FragmentManager fm, @NonNull List dataList) {
        super(fm);
        this.storage = new FragmentItemStorage(this, dataList);
    }

    public AssemblyFragmentStatePagerAdapter(@NonNull FragmentManager fm, @Nullable Object[] dataArray) {
        super(fm);
        this.storage = new FragmentItemStorage(this, dataArray);
    }


    /* ************************ 数据 ItemFactory *************************** */

    @Override
    public void addItemFactory(@NonNull AssemblyFragmentItemFactory itemFactory) {
        storage.addItemFactory(itemFactory);
    }

    @Nullable
    @Override
    public List<AssemblyFragmentItemFactory> getItemFactoryList() {
        return storage.getItemFactoryList();
    }

    @Override
    public int getItemFactoryCount() {
        return storage.getItemFactoryCount();
    }


    /* ************************ 头部 ItemFactory *************************** */

    @Override
    public void addHeaderItem(@NonNull AssemblyFragmentItemFactory itemFactory, @Nullable Object data) {
        storage.addHeaderItem(itemFactory, data);
    }

    @Override
    public void addHeaderItem(@NonNull AssemblyFragmentItemFactory itemFactory) {
        storage.addHeaderItem(itemFactory);
    }

    @Nullable
    @Override
    public List<FragmentItemHolder> getHeaderItemList() {
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

    @Override
    public void addFooterItem(@NonNull AssemblyFragmentItemFactory itemFactory, @Nullable Object data) {
        storage.addFooterItem(itemFactory, data);
    }

    @Override
    public void addFooterItem(@NonNull AssemblyFragmentItemFactory itemFactory) {
        storage.addFooterItem(itemFactory);
    }

    @Nullable
    @Override
    public List<FragmentItemHolder> getFooterItemList() {
        return storage.getFooterItemList();
    }

    @Override
    public int getFooterItemCount() {
        return storage.getFooterItemCount();
    }

    @Nullable
    @Override
    public Object getFooterData(int positionInFooterList) {
        return storage.getFooterData(positionInFooterList);
    }


    /* ************************ 数据列表 *************************** */

    @Nullable
    @Override
    public List getDataList() {
        return storage.getDataList();
    }

    /**
     * 设置数据列表
     */
    public void setDataList(@Nullable List dataList) {
        storage.setDataList(dataList);
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
    public int getPositionInPart(int position) {
        return actor.getPositionInPart(position);
    }

    @Override
    public int getCount() {
        return actor.getItemCount();
    }

    @Override
    public Fragment getItem(int position) {
        return actor.getItem(position);
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
