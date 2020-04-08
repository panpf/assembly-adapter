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


    void addHeaderItem(@NonNull AssemblyFragmentItemFactory itemFactory, @Nullable Object data);

    void addHeaderItem(@NonNull AssemblyFragmentItemFactory itemFactory);

    @NonNull
    FragmentFixedItemManager getHeaderItemManager();

    int getHeaderCount();


    void addFooterItem(@NonNull AssemblyFragmentItemFactory itemFactory, @Nullable Object data);

    void addFooterItem(@NonNull AssemblyFragmentItemFactory itemFactory);

    @NonNull
    FragmentFixedItemManager getFooterItemManager();

    int getFooterCount();


    @Nullable
    List getDataList();

    int getDataCount();


    int getPositionInPart(int position);

    int getCount();

    Fragment getItem(int position);

    @NonNull
    AssemblyFragmentItemFactory getItemFactoryByPosition(int position);

    boolean isHeaderItem(int position);

    boolean isBodyItem(int position);

    boolean isFooterItem(int position);
}
