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
import androidx.viewpager.widget.PagerAdapter;

public class FragmentFixedItem<DATA> {

    @NonNull
    private AssemblyFragmentItemFactory<DATA> itemFactory;
    @Nullable
    private DATA data;

    @Nullable
    private FragmentItemManager itemManager;
    private boolean header;
    private int positionInPartItemList;

    public FragmentFixedItem(@NonNull AssemblyFragmentItemFactory<DATA> itemFactory, @Nullable DATA data) {
        this.itemFactory = itemFactory;
        this.data = data;
    }

    public FragmentFixedItem(@NonNull AssemblyFragmentItemFactory<DATA> itemFactory) {
        this.itemFactory = itemFactory;
    }

    @Nullable
    public DATA getData() {
        return data;
    }

    public void setData(@Nullable DATA data) {
        this.data = data;

        PagerAdapter adapter = itemFactory.getAdapter();
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }

    @NonNull
    public AssemblyFragmentItemFactory<DATA> getItemFactory() {
        return itemFactory;
    }

    public boolean isAttached() {
        return itemManager != null;
    }

    public boolean isHeader() {
        return header;
    }

    void attachToAdapter(@NonNull FragmentItemManager itemManager, boolean header) {
        this.itemManager = itemManager;
        this.header = header;
    }

    public int getPositionInPartItemList() {
        return positionInPartItemList;
    }

    void setPositionInPartItemList(int positionInPartItemList) {
        this.positionInPartItemList = positionInPartItemList;
    }
}
