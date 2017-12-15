/*
 * Copyright (C) 2017 Peng fei Pan <sky@panpf.me>
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package me.panpf.adapter;

/**
 * AssemblyExpandableListAdapter专用的固定位置Item管理器
 */
public class FixedGroupItemInfo {
    private AssemblyGroupItemFactory itemFactory;
    private Object data;
    private boolean enabled;
    private int position;
    private boolean header;

    public FixedGroupItemInfo(AssemblyGroupItemFactory itemFactory, Object data, boolean header) {
        this.data = data;
        this.itemFactory = itemFactory;
        this.enabled = true;
        this.header = header;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;

        AssemblyExpandableAdapter adapter = itemFactory.getAdapter();
        if (adapter.isNotifyOnChange()) {
            adapter.notifyDataSetChanged();
        }
    }

    @SuppressWarnings("unused")
    public AssemblyGroupItemFactory getItemFactory() {
        return itemFactory;
    }

    public boolean isEnabled() {
        return enabled;
    }

    @SuppressWarnings("unused")
    public void setEnabled(boolean enabled) {
        if (this.enabled == enabled) {
            return;
        }
        this.enabled = enabled;
        enableChanged();
    }

    protected void enableChanged() {
        if (header) {
            itemFactory.getAdapter().headerEnabledChanged(this);
        } else {
            itemFactory.getAdapter().footerEnabledChanged(this);
        }
    }

    public int getPosition() {
        return position;
    }

    void setPosition(int position) {
        this.position = position;
    }

    @SuppressWarnings("unused")
    public boolean isHeader() {
        return header;
    }
}
