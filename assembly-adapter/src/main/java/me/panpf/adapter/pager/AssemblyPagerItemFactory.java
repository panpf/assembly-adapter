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

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;

/**
 * {@link AssemblyPagerAdapter} 专用的 item factory，负责匹配数据和创建 item
 *
 * @param <DATA> 指定数据类型
 */
public abstract class AssemblyPagerItemFactory<DATA> {

    @Nullable
    private AssemblyPagerAdapter adapter;

    /**
     * 获取 {@link AssemblyPagerAdapter}
     */
    @Nullable
    public AssemblyPagerAdapter getAdapter() {
        return adapter;
    }

    /**
     * 设置 {@link AssemblyPagerAdapter}，此方法由 Adapter 调用
     */
    void setAdapter(@Nullable AssemblyPagerAdapter adapter) {
        this.adapter = adapter;
    }

    protected View dispatchCreateView(@NonNull Context context, @NonNull ViewGroup container, int position, @Nullable DATA data) {
        return createView(context, container, position, data);
    }

    /**
     * 匹配数据
     *
     * @param data 待匹配的数据，通常是使用 instanceof 关键字匹配类型
     * @return 如果返回 true，Adapter 将会使用此 item factory 来处理当前这条数据
     */
    public abstract boolean isTarget(@Nullable Object data);

    /**
     * 创建 View
     */
    public abstract View createView(@NonNull Context context, @NonNull ViewGroup container, int position, @Nullable DATA data);
}
