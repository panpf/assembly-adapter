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

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.util.SparseIntArray;
import android.view.View;
import android.view.ViewGroup;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;

/**
 * 通用组合式 {@link PagerAdapter}，支持组合式多类型 item，支持头、尾巴
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public class AssemblyPagerAdapter extends PagerAdapter {

    @NonNull
    private PagerItemStorage storage;
    @NonNull
    private PagerItemActor actor = new PagerItemActor(this);

    private int notifyNumber = 0;
    @Nullable
    private SparseIntArray notifyNumberPool;

    public AssemblyPagerAdapter() {
        this.storage = new PagerItemStorage(this);
    }

    public AssemblyPagerAdapter(@Nullable List dataList) {
        this.storage = new PagerItemStorage(this, dataList);
    }

    public AssemblyPagerAdapter(@Nullable Object[] dataArray) {
        this.storage = new PagerItemStorage(this, dataArray);
    }


    /* ************************ 数据 ItemFactory *************************** */

    public void addItemFactory(@NonNull AssemblyPagerItemFactory itemFactory) {
        storage.addItemFactory(itemFactory);
    }

    /**
     * 获取 {@link AssemblyPagerItemFactory} 列表
     */
    @Nullable
    public List<AssemblyPagerItemFactory> getItemFactoryList() {
        return storage.getItemFactoryList();
    }

    /**
     * 获取 {@link AssemblyPagerItemFactory} 的个数
     */
    public int getItemFactoryCount() {
        return storage.getItemFactoryCount();
    }


    /* ************************ 头部 ItemFactory *************************** */

    /**
     * 添加一个将按添加顺序显示在列表头部的 {@link AssemblyPagerItemFactory}
     */
    @NonNull
    public PagerItemHolder addHeaderItem(@NonNull AssemblyPagerItemFactory itemFactory, @Nullable Object data) {
        return storage.addHeaderItem(itemFactory, data);
    }

    /**
     * 添加一个将按添加顺序显示在列表头部的 {@link AssemblyPagerItemFactory}
     */
    @NonNull
    public PagerItemHolder addHeaderItem(@NonNull AssemblyPagerItemFactory itemFactory) {
        return storage.addHeaderItem(itemFactory);
    }

    /**
     * 获取 header 列表
     */
    @Nullable
    public List<PagerItemHolder> getHeaderItemList() {
        return storage.getHeaderItemList();
    }

    /**
     * 获取列表头的个数
     */
    public int getHeaderItemCount() {
        return storage.getHeaderItemCount();
    }

    @Nullable
    public Object getHeaderData(int positionInHeaderList) {
        return storage.getHeaderData(positionInHeaderList);
    }


    /* ************************ 尾巴 ItemFactory *************************** */

    /**
     * 添加一个将按添加顺序显示在列表尾部的 {@link AssemblyPagerItemFactory}
     */
    @NonNull
    public PagerItemHolder addFooterItem(@NonNull AssemblyPagerItemFactory itemFactory, @Nullable Object data) {
        return storage.addFooterItem(itemFactory, data);
    }

    /**
     * 添加一个将按添加顺序显示在列表尾部的 {@link AssemblyPagerItemFactory}
     */
    @NonNull
    public PagerItemHolder addFooterItem(@NonNull AssemblyPagerItemFactory itemFactory) {
        return storage.addFooterItem(itemFactory);
    }

    /**
     * 获取 footer 列表
     */
    @Nullable
    public List<PagerItemHolder> getFooterItemList() {
        return storage.getFooterItemList();
    }

    /**
     * 获取列表头的个数
     */
    public int getFooterItemCount() {
        return storage.getFooterItemCount();
    }

    @Nullable
    public Object getFooterData(int positionInFooterList) {
        return storage.getFooterData(positionInFooterList);
    }


    /* ************************ 数据列表 *************************** */

    /**
     * 获取数据列表
     */
    @Nullable
    public List getDataList() {
        return storage.getDataList();
    }

    /**
     * 设置数据列表
     */
    public void setDataList(@Nullable List dataList) {
        storage.setDataList(dataList);
    }

    /**
     * 批量添加数据
     */
    public void addAll(@Nullable Collection collection) {
        storage.addAll(collection);
    }

    /**
     * 批量添加数据
     */
    public void addAll(@Nullable Object... items) {
        storage.addAll(items);
    }

    /**
     * 插入一条数据
     */
    public void insert(@NonNull Object object, int index) {
        storage.insert(object, index);
    }

    /**
     * 删除一条数据
     */
    public void remove(@NonNull Object object) {
        storage.remove(object);
    }

    /**
     * 清空数据
     */
    public void clear() {
        storage.clear();
    }

    /**
     * 对数据排序
     */
    public void sort(@NonNull Comparator comparator) {
        storage.sort(comparator);
    }

    /**
     * 获取数据列表的长度
     */
    public int getDataCount() {
        return storage.getDataCount();
    }

    @Nullable
    public Object getData(int positionInDataList) {
        return storage.getData(positionInDataList);
    }


    /* ************************ 完整列表 *************************** */

    @Override
    public int getCount() {
        return actor.getItemCount();
    }

    /**
     * 获取在各自区域的位置
     */
    public int getPositionInPart(int position) {
        return actor.getPositionInPart(position);
    }


    /* ************************ 其它 *************************** */

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        return actor.getItem(container, position);
    }

    /**
     * 数据变更时是否立即刷新列表
     */
    public boolean isNotifyOnChange() {
        return storage.isNotifyOnChange();
    }

    /**
     * 设置当数据源发生改变时是否立即调用 notifyDataSetChanged() 刷新列表，默认 true。
     * 当你需要连续多次修改数据的时候，你应该将 notifyOnChange 设为 false，然后在最后主动调用 notifyDataSetChanged() 刷新列表，最后再将 notifyOnChange 设为 true
     */
    public void setNotifyOnChange(boolean notifyOnChange) {
        storage.setNotifyOnChange(notifyOnChange);
    }

    public boolean isEnabledPositionNoneOnNotifyDataSetChanged() {
        return notifyNumberPool != null;
    }

    public void setEnabledPositionNoneOnNotifyDataSetChanged(boolean enabled) {
        if (enabled) {
            notifyNumberPool = new SparseIntArray();
            notifyNumber = 0;
        } else {
            notifyNumberPool = null;
        }
    }

    @Override
    public void notifyDataSetChanged() {
        if (notifyNumberPool != null) notifyNumber++;
        super.notifyDataSetChanged();
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        if (notifyNumberPool != null && notifyNumberPool.get(object.hashCode()) != notifyNumber) {
            notifyNumberPool.put(object.hashCode(), notifyNumber);
            return PagerAdapter.POSITION_NONE;
        }
        return super.getItemPosition(object);
    }
}
