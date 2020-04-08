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

/**
 * {@link AssemblyPagerAdapter} 专用的固定位置 item 管理器
 */
public class PagerItemHolder<DATA> {
    @Nullable
    private PagerItemManager itemManager;
    @NonNull
    private AssemblyPagerItemFactory<DATA> itemFactory;
    @Nullable
    private DATA data;
    private boolean header;

    private boolean enabled = true;

    @Deprecated
    public PagerItemHolder(@NonNull PagerItemManager itemManager, @NonNull AssemblyPagerItemFactory<DATA> itemFactory, @Nullable DATA data, boolean header) {
        this.itemManager = itemManager;
        this.itemFactory = itemFactory;
        this.data = data;
        this.header = header;
    }

    public PagerItemHolder(@NonNull AssemblyPagerItemFactory<DATA> itemFactory, @Nullable DATA data) {
        this.itemFactory = itemFactory;
        this.data = data;
    }

    public PagerItemHolder(@NonNull AssemblyPagerItemFactory<DATA> itemFactory) {
        this.itemFactory = itemFactory;
    }

    @Nullable
    public DATA getData() {
        return data;
    }

    public void setData(@Nullable DATA data) {
        this.data = data;

        AssemblyPagerAdapter adapter = itemFactory.getAdapter();
        if (adapter != null && adapter.isNotifyOnChange()) {
            adapter.notifyDataSetChanged();
        }
    }

    @NonNull
    public AssemblyPagerItemFactory<DATA> getItemFactory() {
        return itemFactory;
    }

    public boolean isAttached() {
        return itemManager != null;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        if (this.enabled == enabled) {
            return;
        }
        this.enabled = enabled;
        enableChanged();
    }

    void attachToAdapter(@NonNull PagerItemManager itemManager, boolean header) {
        this.itemManager = itemManager;
        this.header = header;
    }

    private void enableChanged() {
        if (itemManager != null) {
            itemManager.itemHolderEnabledChanged(this);
        }
    }

    public boolean isHeader() {
        return header;
    }
}
