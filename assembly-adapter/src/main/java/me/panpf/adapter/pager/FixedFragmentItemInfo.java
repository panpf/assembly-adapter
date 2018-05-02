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
import android.support.v4.view.PagerAdapter;

import me.panpf.adapter.ItemStorage;

/**
 * {@link AssemblyFragmentPagerAdapter} 和 {@link AssemblyFragmentStatePagerAdapter} 专用的固定位置 item 管理器
 */
@SuppressWarnings({"WeakerAccess", "unused"})
public class FixedFragmentItemInfo {

    @NonNull
    private AssemblyFragmentItemFactory itemFactory;
    @NonNull
    private Object data;

    public FixedFragmentItemInfo(@NonNull AssemblyFragmentItemFactory itemFactory, @Nullable Object data) {
        this.data = data != null ? data : ItemStorage.NONE_DATA;
        this.itemFactory = itemFactory;
    }

    @NonNull
    public Object getData() {
        return data;
    }

    /**
     * @param data 如果 data 为 null 将用 {@link ItemStorage#NONE_DATA} 代替
     */
    public void setData(@Nullable Object data) {
        this.data = data != null ? data : ItemStorage.NONE_DATA;

        PagerAdapter adapter = itemFactory.getAdapter();
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }

    public boolean isNoneData() {
        return data == ItemStorage.NONE_DATA;
    }

    @NonNull
    public AssemblyFragmentItemFactory getItemFactory() {
        return itemFactory;
    }
}
