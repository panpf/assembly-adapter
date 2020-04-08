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

import java.util.List;

public interface AssemblyFragmentAdapter {

    void addItemFactory(@NonNull AssemblyFragmentItemFactory itemFactory);

    @NonNull
    List<AssemblyFragmentItemFactory> getItemFactoryList();


    @NonNull
    <DATA> FragmentFixedItem<DATA> addHeaderItem(@NonNull FragmentFixedItem<DATA> fixedItem);

    @NonNull
    <DATA> FragmentFixedItem<DATA> addHeaderItem(@NonNull AssemblyFragmentItemFactory<DATA> itemFactory, @Nullable DATA data);

    @NonNull
    <DATA> FragmentFixedItem<DATA> addHeaderItem(@NonNull AssemblyFragmentItemFactory<DATA> itemFactory);

    @NonNull
    FragmentFixedItem getHeaderItemByClass(@NonNull Class clazz, int number);

    @NonNull
    FragmentFixedItem getHeaderItemByClass(@NonNull Class clazz);

    @NonNull
    FragmentFixedItem getHeaderItem(int positionInHeaderItemList);

    @Nullable
    Object getHeaderItemData(int positionInHeaderItemList);

    void setHeaderItemData(int positionInHeaderItemList, @Nullable Object data);

    int getHeaderCount();

    @Nullable
    Object getHeaderData(int positionInHeaderList);


    @NonNull
    <DATA> FragmentFixedItem<DATA> addFooterItem(@NonNull FragmentFixedItem<DATA> fixedItem);

    @NonNull
    <DATA> FragmentFixedItem<DATA> addFooterItem(@NonNull AssemblyFragmentItemFactory<DATA> itemFactory, @Nullable DATA data);

    @NonNull
    <DATA> FragmentFixedItem<DATA> addFooterItem(@NonNull AssemblyFragmentItemFactory<DATA> itemFactory);

    @NonNull
    FragmentFixedItem getFooterItemByClass(@NonNull Class clazz, int number);

    @NonNull
    FragmentFixedItem getFooterItemByClass(@NonNull Class clazz);

    @NonNull
    FragmentFixedItem getFooterItem(int positionInFooterItemList);

    @Nullable
    Object getFooterItemData(int positionInFooterItemList);

    void setFooterItemData(int positionInFooterItemList, @Nullable Object data);

    int getFooterCount();

    @Nullable
    Object getFooterData(int positionFooterList);


    @Nullable
    List getDataList();

    void setDataList(@Nullable List dataList);

    int getDataCount();

    @Nullable
    Object getData(int positionInDataList);

    boolean isHeaderItem(int position);

    boolean isBodyItem(int position);

    boolean isFooterItem(int position);

    int getPositionInPart(int position);

    int getCount();

    Fragment getItem(int position);

    @NonNull
    AssemblyFragmentItemFactory getItemFactoryByPosition(int position);
}
