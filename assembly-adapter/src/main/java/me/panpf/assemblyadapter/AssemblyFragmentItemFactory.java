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

package me.panpf.assemblyadapter;

import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;

/**
 * AssemblyFragmentPagerAdapter和AssemblyFragmentStatePagerAdapter专用的ItemFactory，
 * 负责匹配数据和创建Fragment
 *
 * @param <DATA> 指定数据类型
 */
public abstract class AssemblyFragmentItemFactory<DATA> {
    private PagerAdapter adapter;

    /**
     * 获取Adapter
     */
    public PagerAdapter getAdapter() {
        return adapter;
    }

    /**
     * 设置Adapter，此方法由Adapter调用
     */
    void setAdapter(PagerAdapter adapter) {
        this.adapter = adapter;
    }

    @SuppressWarnings("WeakerAccess")
    protected Fragment dispatchCreateFragment(int position, DATA data) {
        return createFragment(position, data);
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
    public abstract Fragment createFragment(int position, DATA data);
}
