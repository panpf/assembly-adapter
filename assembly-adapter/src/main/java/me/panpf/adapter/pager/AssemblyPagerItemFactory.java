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
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import me.panpf.adapter.OnClickListener;
import me.panpf.adapter.OnLongClickListener;

public abstract class AssemblyPagerItemFactory<DATA> {

    @Nullable
    private AssemblyPagerAdapter adapter;
    @Nullable
    private PagerClickListenerManager<DATA> clickListenerManager;

    @Nullable
    public AssemblyPagerAdapter getAdapter() {
        return adapter;
    }

    void attachToAdapter(@Nullable AssemblyPagerAdapter adapter) {
        this.adapter = adapter;
    }

    public AssemblyPagerItemFactory<DATA> setOnViewClickListener(@IdRes int viewId, @NonNull OnClickListener<DATA> onClickListener) {
        if (clickListenerManager == null) {
            clickListenerManager = new PagerClickListenerManager<DATA>();
        }
        clickListenerManager.add(viewId, onClickListener);
        return this;
    }

    public AssemblyPagerItemFactory<DATA> setOnItemClickListener(@NonNull OnClickListener<DATA> onClickListener) {
        if (clickListenerManager == null) {
            clickListenerManager = new PagerClickListenerManager<DATA>();
        }
        clickListenerManager.add(onClickListener);
        return this;
    }

    public AssemblyPagerItemFactory<DATA> setOnViewLongClickListener(@IdRes int viewId, @NonNull OnLongClickListener<DATA> onClickListener) {
        if (clickListenerManager == null) {
            clickListenerManager = new PagerClickListenerManager<DATA>();
        }
        clickListenerManager.add(viewId, onClickListener);
        return this;
    }

    public AssemblyPagerItemFactory<DATA> setOnItemLongClickListener(@NonNull OnLongClickListener<DATA> onClickListener) {
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
    public abstract boolean match(@Nullable Object data);

    /**
     * 创建 View
     */
    public abstract View createView(@NonNull Context context, @NonNull ViewGroup container, int position, @Nullable DATA data);
}
