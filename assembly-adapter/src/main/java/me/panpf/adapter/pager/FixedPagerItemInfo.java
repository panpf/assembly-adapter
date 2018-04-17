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

/**
 * {@link AssemblyPagerAdapter} 专用的固定位置 item 管理器
 */
@SuppressWarnings("WeakerAccess")
public class FixedPagerItemInfo {

    @NonNull
    private AssemblyPagerItemFactory itemFactory;
    @NonNull
    private Object data;
    private boolean enabled;
    private int position;
    private boolean header;

    public FixedPagerItemInfo(@NonNull AssemblyPagerItemFactory itemFactory, @NonNull Object data, boolean header) {
        this.data = data;
        this.itemFactory = itemFactory;
        this.enabled = true;
        this.header = header;
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

        AssemblyPagerAdapter adapter = itemFactory.getAdapter();
        if (adapter != null && adapter.isNotifyOnChange()) {
            adapter.notifyDataSetChanged();
        }
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
