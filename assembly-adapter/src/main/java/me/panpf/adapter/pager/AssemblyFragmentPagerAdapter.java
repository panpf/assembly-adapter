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
public class AssemblyFragmentPagerAdapter extends FragmentPagerAdapter {

    @NonNull
    private FragmentItemStorage storage;

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

    public void addItemFactory(@NonNull AssemblyFragmentItemFactory itemFactory) {
        storage.addItemFactory(itemFactory);
    }

    /**
     * 获取 {@link me.panpf.adapter.ItemFactory} 列表
     */
    @Nullable
    public List<AssemblyFragmentItemFactory> getItemFactoryList() {
        return storage.getItemFactoryList();
    }

    /**
     * 获取 {@link me.panpf.adapter.ItemFactory} 的个数
     */
    public int getItemFactoryCount() {
        return storage.getItemFactoryCount();
    }


    /* ************************ 头部 ItemFactory *************************** */

    /**
     * 添加一个将按添加顺序显示在列表头部的 {@link AssemblyFragmentItemFactory}
     */
    public void addHeaderItem(@NonNull AssemblyFragmentItemFactory headerFactory, @NonNull Object data) {
        storage.addHeaderItem(headerFactory, data);
    }

    /**
     * 添加一个将按添加顺序显示在列表头部的 {@link AssemblyFragmentItemFactory}
     */
    public void addHeaderItem(@NonNull AssemblyFragmentItemFactory headerFactory) {
        storage.addHeaderItem(headerFactory);
    }

    /**
     * 获取 header 列表
     */
    @Nullable
    public List<FixedFragmentItemInfo> getHeaderItemList() {
        return storage.getHeaderItemList();
    }

    /**
     * 获取列表头的个数
     */
    public int getHeaderItemCount() {
        return storage.getHeaderItemCount();
    }

    @Nullable
    public Object getHeaderData(int positionInHeaderList) {
        return storage.getHeaderData(positionInHeaderList);
    }


    /* ************************ 尾巴 ItemFactory *************************** */

    /**
     * 添加一个将按添加顺序显示在列表尾部的 {@link AssemblyFragmentItemFactory}
     */
    public void addFooterItem(@NonNull AssemblyFragmentItemFactory footerFactory, @NonNull Object data) {
        storage.addFooterItem(footerFactory, data);
    }

    /**
     * 添加一个将按添加顺序显示在列表尾部的 {@link AssemblyFragmentItemFactory}
     */
    public void addFooterItem(@NonNull AssemblyFragmentItemFactory footerFactory) {
        storage.addFooterItem(footerFactory);
    }

    /**
     * 获取 footer 列表
     */
    @Nullable
    public List<FixedFragmentItemInfo> getFooterItemList() {
        return storage.getFooterItemList();
    }

    /**
     * 获取列表头的个数
     */
    public int getFooterItemCount() {
        return storage.getFooterItemCount();
    }

    @Nullable
    public Object getFooterData(int positionInFooterList) {
        return storage.getFooterData(positionInFooterList);
    }


    /* ************************ 数据列表 *************************** */

    /**
     * 获取数据列表
     */
    @Nullable
    public List getDataList() {
        return storage.getDataList();
    }

    /**
     * 获取数据列表的长度
     */
    public int getDataCount() {
        return storage.getDataCount();
    }

    @Nullable
    public Object getData(int positionInDataList) {
        return storage.getData(positionInDataList);
    }


    /* ************************ 完整列表 *************************** */

    /**
     * 获取在各自区域的位置
     */
    public int getPositionInPart(int position) {
        return storage.getPositionInPart(position);
    }

    @Override
    public int getCount() {
        return storage.getItemCount();
    }

    @Override
    public Fragment getItem(int position) {
        return storage.getItem(position);
    }
}
