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
 * Item 负责创建、初始化 View 并设置数据
 * @param <DATA> 指定需要的数据的类型
 */
public abstract class AssemblyChildItem<DATA> {
    private View itemView;
    private int position;
    private int groupPosition;
    private boolean isLastChild;
    private DATA data;

    public AssemblyChildItem(int itemLayoutId, ViewGroup parent) {
        this(LayoutInflater.from(parent.getContext()).inflate(itemLayoutId, parent, false));
    }

    @SuppressWarnings("WeakerAccess")
    public AssemblyChildItem(View itemView) {
        if (itemView == null) {
            throw new IllegalArgumentException("itemView may not be null");
        }
        this.itemView = itemView;
        this.itemView.setTag(this);
    }

    /**
     * 设置数据，这个方法由 Adapter 调用
     *
     * @param groupPosition 位置
     * @param childPosition 位置
     * @param isLastChild 位置
     * @param data     数据
     */
    public void setData(int groupPosition, int childPosition, boolean isLastChild, DATA data) {
        this.groupPosition = groupPosition;
        this.position = childPosition;
        this.isLastChild = isLastChild;
        this.data = data;
        onSetData(childPosition, data);
    }

    /**
     * 根据 id 查找 View
     */
    @SuppressWarnings("unused")
    public View findViewById(int id) {
        return itemView.findViewById(id);
    }

    /**
     * 根据 tag 查找 View
     */
    @SuppressWarnings("unused")
    public View findViewWithTag(Object tag) {
        return itemView.findViewWithTag(tag);
    }

    /**
     * 专门用来 find view，只会执行一次
     */
    @SuppressWarnings("WeakerAccess")
    protected void onFindViews() {

    }

    /**
     * 专门用来配置 View，你可在在这里设置 View 的样式以及尺寸，只会执行一次
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
     * 获取当前子 Item 的 View
     */
    @SuppressWarnings("WeakerAccess")
    public final View getItemView() {
        return this.itemView;
    }

    /**
     * 获取当前子 Item 的位置
     */
    public int getPosition() {
        return position;
    }

    /**
     * 获取当前子 Item 的数据
     */
    public DATA getData() {
        return data;
    }

    /**
     * 获取所属 Group 的位置
     */
    @SuppressWarnings("unused")
    public int getGroupPosition() {
        return groupPosition;
    }

    /**
     * 是最后一个子 Item
     */
    @SuppressWarnings("unused")
    public boolean isLastChild() {
        return isLastChild;
    }
}