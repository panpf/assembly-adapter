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

package me.panpf.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Item负责创建、初始化View并设置数据
 * @param <DATA> 指定需要的数据的类型
 */
public abstract class AssemblyListItem<DATA> {
    private View itemView;
    private int position;
    private DATA data;

    public AssemblyListItem(int itemLayoutId, ViewGroup parent) {
        this(LayoutInflater.from(parent.getContext()).inflate(itemLayoutId, parent, false));
    }

    @SuppressWarnings("WeakerAccess")
    public AssemblyListItem(View itemView) {
        if (itemView == null) {
            throw new IllegalArgumentException("itemView may not be null");
        }
        this.itemView = itemView;
    }

    /**
     * 设置数据，这个方法由Adapter调用
     *
     * @param position 位置
     * @param data     数据
     */
    public void setData(int position, DATA data) {
        this.position = position;
        this.data = data;
        onSetData(position, data);
    }

    /**
     * 根据id查找View
     */
    @SuppressWarnings("unused")
    public View findViewById(int id) {
        return itemView.findViewById(id);
    }

    /**
     * 根据tag查找View
     */
    @SuppressWarnings("unused")
    public View findViewWithTag(Object tag) {
        return itemView.findViewWithTag(tag);
    }

    /**
     * 专门用来find view，只会执行一次
     */
    @SuppressWarnings("WeakerAccess")
    protected void onFindViews() {

    }

    /**
     * 专门用来配置View，你可在在这里设置View的样式以及尺寸，只会执行一次
     */
    protected abstract void onConfigViews(Context context);

    /**
     * 设置数据
     *
     * @param position 位置
     * @param data     数据
     */
    protected abstract void onSetData(int position, DATA data);

    /**
     * 获取当前Item的View
     */
    @SuppressWarnings("WeakerAccess")
    public final View getItemView() {
        return this.itemView;
    }

    /**
     * 获取当前Item的数据
     */
    public DATA getData() {
        return data;
    }

    /**
     * 获取当前Item的位置
     */
    public int getPosition() {
        return position;
    }
}