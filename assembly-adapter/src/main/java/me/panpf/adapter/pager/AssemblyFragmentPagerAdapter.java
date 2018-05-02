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

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * 通用组合式 {@link FragmentPagerAdapter}，支持组合式多类型 item，支持头、尾巴
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public class AssemblyFragmentPagerAdapter extends FragmentPagerAdapter implements AssemblyFragmentAdapter {

    @NonNull
    private FragmentItemStorage storage;
    @NonNull
    private FragmentItemActor actor = new FragmentItemActor(this);

    public AssemblyFragmentPagerAdapter(@NonNull FragmentManager fm) {
        super(fm);
        this.storage = new FragmentItemStorage(this);
    }

    public AssemblyFragmentPagerAdapter(@NonNull FragmentManager fm, @NonNull List dataList) {
        super(fm);
        this.storage = new FragmentItemStorage(this, dataList);
    }

    public AssemblyFragmentPagerAdapter(@NonNull FragmentManager fm, @Nullable Object[] dataArray) {
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
    public void addHeaderItem(@NonNull AssemblyFragmentItemFactory headerFactory, @Nullable Object data) {
        storage.addHeaderItem(headerFactory, data);
    }

    @Override
    public void addHeaderItem(@NonNull AssemblyFragmentItemFactory headerFactory) {
        storage.addHeaderItem(headerFactory);
    }

    @Nullable
    @Override
    public List<FixedFragmentItemInfo> getHeaderItemList() {
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
    public void addFooterItem(@NonNull AssemblyFragmentItemFactory footerFactory, @Nullable Object data) {
        storage.addFooterItem(footerFactory, data);
    }

    @Override
    public void addFooterItem(@NonNull AssemblyFragmentItemFactory footerFactory) {
        storage.addFooterItem(footerFactory);
    }

    @Nullable
    @Override
    public List<FixedFragmentItemInfo> getFooterItemList() {
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
}
