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

/**
 * {@link AssemblyFragmentPagerAdapter} 和 {@link AssemblyFragmentStatePagerAdapter} 专用的固定位置 item 管理器
 */
public class FragmentItemHolder<DATA> {

    @NonNull
    private AssemblyFragmentItemFactory itemFactory;
    @Nullable
    private DATA data;

    public FragmentItemHolder(@NonNull AssemblyFragmentItemFactory itemFactory, @Nullable DATA data) {
        this.itemFactory = itemFactory;
        this.data = data;
    }

    public FragmentItemHolder(@NonNull AssemblyFragmentItemFactory itemFactory) {
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
    public AssemblyFragmentItemFactory getItemFactory() {
        return itemFactory;
    }
}
