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

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

/**
 * AssemblyPagerAdapter专用的ItemFactory，负责匹配数据好和创建Item View
 *
 * @param <DATA> 指定数据类型
 */
public abstract class AssemblyPagerItemFactory<DATA> {
    private AssemblyPagerAdapter adapter;

    /**
     * 获取Adapter
     */
    public AssemblyPagerAdapter getAdapter() {
        return adapter;
    }

    /**
     * 设置Adapter，此方法由Adapter调用
     */
    void setAdapter(AssemblyPagerAdapter adapter) {
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
     * 创建Fragment
     */
    public abstract View createView(Context context, ViewGroup container, int position, DATA data);
}
