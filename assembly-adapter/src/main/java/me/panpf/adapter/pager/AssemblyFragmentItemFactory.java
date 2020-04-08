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
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.PagerAdapter;

public abstract class AssemblyFragmentItemFactory<DATA> {

    @Nullable
    private PagerAdapter adapter;

    @Nullable
    public PagerAdapter getAdapter() {
        return adapter;
    }

    void attachToAdapter(@NonNull PagerAdapter adapter) {
        this.adapter = adapter;
    }

    @NonNull
    protected Fragment dispatchCreateFragment(int position, @Nullable DATA data) {
        return createFragment(position, data);
    }

    /**
     * 匹配数据
     *
     * @param data 待匹配的数据，通常是使用 instanceof 关键字匹配类型
     * @return 如果返回 true，Adapter 将会使用此 {@link AssemblyFragmentItemFactory} 来处理当前这条数据
     */
    public abstract boolean match(@Nullable Object data);

    /**
     * 创建 {@link Fragment}
     */
    @NonNull
    public abstract Fragment createFragment(int position, @Nullable DATA data);
}
