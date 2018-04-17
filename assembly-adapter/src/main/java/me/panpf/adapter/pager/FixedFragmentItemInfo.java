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
import android.support.v4.view.PagerAdapter;

/**
 * {@link AssemblyFragmentPagerAdapter} 和 {@link AssemblyFragmentStatePagerAdapter} 专用的固定位置 item 管理器
 */
@SuppressWarnings("WeakerAccess")
public class FixedFragmentItemInfo {

    @NonNull
    private AssemblyFragmentItemFactory itemFactory;
    @NonNull
    private Object data;

    public FixedFragmentItemInfo(@NonNull AssemblyFragmentItemFactory itemFactory, @NonNull Object data) {
        this.data = data;
        this.itemFactory = itemFactory;
    }

    @NonNull
    public Object getData() {
        return data;
    }

    public void setData(@NonNull Object data) {
        //noinspection ConstantConditions
        if (data == null) {
            throw new IllegalArgumentException("data is null");
        }
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
