/**
 * Copyright (C) 2016 Peng fei Pan <sky@xiaopan.me>
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

package me.xiaopan.assemblyadapter;

import android.view.ViewGroup;

/**
 * AssemblyAdapter专用的ItemFactory，负责匹配数据和创建Item
 *
 * @param <ITEM> 指定Item类型，防止createAssemblyItem()方法返回错误的类型
 */
public abstract class AssemblyChildItemFactory<ITEM extends AssemblyChildItem> {
    private int itemType;
    private AssemblyExpandableAdapter adapter;

    /**
     * 获取Item类型
     */
    @SuppressWarnings("WeakerAccess")
    public int getItemType() {
        return itemType;
    }

    /**
     * 设置Item类型，此方法由Adapter调用
     */
    void setItemType(int itemType) {
        this.itemType = itemType;
    }

    /**
     * 获取Adapter
     */
    public AssemblyExpandableAdapter getAdapter() {
        return adapter;
    }

    /**
     * 设置Adapter，此方法由Adapter调用
     */
    void setAdapter(AssemblyExpandableAdapter adapter) {
        this.adapter = adapter;
    }

    /**
     * 匹配数据
     *
     * @param data 待匹配的数据，通常是使用instanceof关键字匹配类型
     * @return 如果返回true，Adapter将会使用此ItemFactory来处理当前这条数据
     */
    public abstract boolean isTarget(Object data);

    /**
     * 创建Item
     */
    public abstract ITEM createAssemblyItem(ViewGroup parent);
}
