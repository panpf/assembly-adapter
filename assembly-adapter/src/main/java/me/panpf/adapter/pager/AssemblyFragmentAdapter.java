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
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.List;

/**
 * 通用组合式 {@link FragmentStatePagerAdapter}，支持组合式多类型 item，支持头、尾巴以及加载更多
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public interface AssemblyFragmentAdapter {


    /* ************************ 数据 ItemFactory *************************** */

    void addItemFactory(@NonNull AssemblyFragmentItemFactory itemFactory);

    /**
     * 获取 {@link me.panpf.adapter.ItemFactory} 列表
     */
    @Nullable
    List<AssemblyFragmentItemFactory> getItemFactoryList();

    /**
     * 获取 {@link me.panpf.adapter.ItemFactory} 的个数
     */
    int getItemFactoryCount();


    /* ************************ 头部 ItemFactory *************************** */

    /**
     * 添加一个将按添加顺序显示在列表头部的 {@link AssemblyFragmentItemFactory}
     */
    void addHeaderItem(@NonNull AssemblyFragmentItemFactory itemFactory, @NonNull Object data);

    /**
     * 添加一个将按添加顺序显示在列表头部的 {@link AssemblyFragmentItemFactory}
     */
    void addHeaderItem(@NonNull AssemblyFragmentItemFactory itemFactory);

    /**
     * 获取 header 列表
     */
    @Nullable
    List<FixedFragmentItemInfo> getHeaderItemList();

    /**
     * 获取列表头的个数
     */
    int getHeaderItemCount();

    @Nullable
    Object getHeaderData(int positionInHeaderList);


    /* ************************ 尾巴 ItemFactory *************************** */

    /**
     * 添加一个将按添加顺序显示在列表尾部的 {@link AssemblyFragmentItemFactory}
     */
    void addFooterItem(@NonNull AssemblyFragmentItemFactory itemFactory, @NonNull Object data);

    /**
     * 添加一个将按添加顺序显示在列表尾部的 {@link AssemblyFragmentItemFactory}
     */
    void addFooterItem(@NonNull AssemblyFragmentItemFactory itemFactory);

    /**
     * 获取 footer 列表
     */
    @Nullable
    List<FixedFragmentItemInfo> getFooterItemList();

    /**
     * 获取列表头的个数
     */
    int getFooterItemCount();

    @Nullable
    Object getFooterData(int positionInFooterList);


    /* ************************ 数据列表 *************************** */

    /**
     * 获取数据列表
     */
    @Nullable
    List getDataList();

    /**
     * 获取数据列表的长度
     */
    int getDataCount();

    @Nullable
    Object getData(int positionInDataList);


    /* ************************ 完整列表 *************************** */

    /**
     * 获取在各自区域的位置
     */
    int getPositionInPart(int position);

    int getCount();

    Fragment getItem(int position);
}
