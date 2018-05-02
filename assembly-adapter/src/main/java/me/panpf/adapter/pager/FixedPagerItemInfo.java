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

import me.panpf.adapter.ItemStorage;

/**
 * {@link AssemblyPagerAdapter} 专用的固定位置 item 管理器
 */
@SuppressWarnings({"WeakerAccess", "unused"})
public class FixedPagerItemInfo {

    @NonNull
    private AssemblyPagerItemFactory itemFactory;
    @NonNull
    private Object data;
    private boolean enabled;
    private int position;
    private boolean header;

    public FixedPagerItemInfo(@NonNull AssemblyPagerItemFactory itemFactory, @Nullable Object data, boolean header) {
        this.data = data != null ? data : ItemStorage.NONE_DATA;
        this.itemFactory = itemFactory;
        this.enabled = true;
        this.header = header;
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

        AssemblyPagerAdapter adapter = itemFactory.getAdapter();
        if (adapter != null && adapter.isNotifyOnChange()) {
            adapter.notifyDataSetChanged();
        }
    }

    public boolean isNoneData() {
        return data == ItemStorage.NONE_DATA;
    }

    @NonNull
    public AssemblyPagerItemFactory getItemFactory() {
        return itemFactory;
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

    protected void enableChanged() {
        if (header) {
            AssemblyPagerAdapter adapter = itemFactory.getAdapter();
            if (adapter != null) {
                adapter.headerEnabledChanged(this);
            }
        } else {
            AssemblyPagerAdapter adapter = itemFactory.getAdapter();
            if (adapter != null) {
                adapter.footerEnabledChanged(this);
            }
        }
    }

    public int getPosition() {
        return position;
    }

    void setPosition(int position) {
        this.position = position;
    }

    public boolean isHeader() {
        return header;
    }
}
