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
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;

import me.panpf.adapter.OnClickListener;
import me.panpf.adapter.OnLongClickListener;

/**
 * {@link AssemblyPagerAdapter} 专用的 item factory，负责匹配数据和创建 item
 *
 * @param <DATA> 指定数据类型
 */
@SuppressWarnings("unused")
public abstract class AssemblyPagerItemFactory<DATA> {

    @Nullable
    private AssemblyPagerAdapter adapter;
    @Nullable
    private PagerClickListenerManager<DATA> clickListenerManager;

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

    /**
     * 监听指定 id 的 view 的点击事件
     *
     * @param viewId          view 的 id
     * @param onClickListener 点击监听
     */
    public AssemblyPagerItemFactory setOnViewClickListener(@IdRes int viewId, @NonNull OnClickListener onClickListener) {
        if (clickListenerManager == null) {
            clickListenerManager = new PagerClickListenerManager<DATA>();
        }
        clickListenerManager.add(viewId, onClickListener);
        return this;
    }

    /**
     * 监听 item 的点击事件
     *
     * @param onClickListener 点击监听
     */
    public AssemblyPagerItemFactory setOnItemClickListener(@NonNull OnClickListener onClickListener) {
        if (clickListenerManager == null) {
            clickListenerManager = new PagerClickListenerManager<DATA>();
        }
        clickListenerManager.add(onClickListener);
        return this;
    }

    /**
     * 监听指定 id 的 view 的长按事件
     *
     * @param viewId          view 的 id
     * @param onClickListener 长按监听
     */
    public AssemblyPagerItemFactory setOnViewLongClickListener(@IdRes int viewId, @NonNull OnLongClickListener onClickListener) {
        if (clickListenerManager == null) {
            clickListenerManager = new PagerClickListenerManager<DATA>();
        }
        clickListenerManager.add(viewId, onClickListener);
        return this;
    }

    /**
     * 监听 item 的长按事件
     *
     * @param onClickListener 长按监听
     */
    public AssemblyPagerItemFactory setOnItemLongClickListener(@NonNull OnLongClickListener onClickListener) {
        if (clickListenerManager == null) {
            clickListenerManager = new PagerClickListenerManager<DATA>();
        }
        clickListenerManager.add(onClickListener);
        return this;
    }

    protected View dispatchCreateView(@NonNull Context context, @NonNull ViewGroup container, int position, @Nullable DATA data) {
        View itemView = createView(context, container, position, data);

        if (clickListenerManager != null) {
            clickListenerManager.register(this, itemView, position, data);
        }

        return itemView;
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
