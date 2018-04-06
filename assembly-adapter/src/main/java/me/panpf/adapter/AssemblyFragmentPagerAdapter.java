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

import android.annotation.SuppressLint;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 通用组合式FragmentPagerAdapter，支持组合式多ItemType，支持头、尾巴
 */
@SuppressWarnings("unused")
public class AssemblyFragmentPagerAdapter extends FragmentPagerAdapter {
    private static final String TAG = "AssemblyFragmentPagerAdapter";
    private List dataList;

    private boolean itemFactoryLocked;
    private ArrayList<FixedFragmentItemInfo> headerItemList;
    private ArrayList<FixedFragmentItemInfo> footerItemList;
    private ArrayList<AssemblyFragmentItemFactory> itemFactoryList;

    @SuppressWarnings("unused")
    public AssemblyFragmentPagerAdapter(FragmentManager fm, List dataList) {
        super(fm);
        this.dataList = dataList;
    }

    @SuppressWarnings("unused")
    public AssemblyFragmentPagerAdapter(FragmentManager fm, Object[] dataArray) {
        super(fm);
        if (dataArray != null && dataArray.length > 0) {
            this.dataList = new ArrayList(dataArray.length);
            Collections.addAll(dataList, dataArray);
        }
    }

    /**
     * 添加一个将按添加顺序显示在列表头部的AssemblyFragmentItemFactory
     */
    @SuppressLint("LongLogTag")
    @SuppressWarnings("unused")
    public void addHeaderItem(AssemblyFragmentItemFactory headerFactory, Object data) {
        if (headerFactory == null || itemFactoryLocked) {
            Log.w(TAG, "headerFactory is nll or locked");
            return;
        }

        headerFactory.setAdapter(this);

        if (headerItemList == null) {
            headerItemList = new ArrayList<FixedFragmentItemInfo>(2);
        }
        headerItemList.add(new FixedFragmentItemInfo(headerFactory, data));
    }

    @SuppressLint("LongLogTag")
    @SuppressWarnings("unused")
    public void addItemFactory(AssemblyFragmentItemFactory itemFactory) {
        if(itemFactory == null || itemFactoryLocked){
            Log.w(TAG, "itemFactory is nll or locked");
            return;
        }

        itemFactory.setAdapter(this);

        if (itemFactoryList == null) {
            itemFactoryList = new ArrayList<AssemblyFragmentItemFactory>(2);
        }
        itemFactoryList.add(itemFactory);
    }

    /**
     * 添加一个将按添加顺序显示在列表尾部的AssemblyFragmentItemFactory
     */
    @SuppressLint("LongLogTag")
    @SuppressWarnings("unused")
    public void addFooterItem(AssemblyFragmentItemFactory footerFactory, Object data) {
        if (footerFactory == null || itemFactoryLocked) {
            Log.w(TAG, "footerFactory is nll or locked");
            return;
        }

        footerFactory.setAdapter(this);

        if (footerItemList == null) {
            footerItemList = new ArrayList<FixedFragmentItemInfo>(2);
        }
        footerItemList.add(new FixedFragmentItemInfo(footerFactory, data));
    }

    /**
     * 获取Header列表
     */
    @SuppressWarnings("unused")
    public List<FixedFragmentItemInfo> getHeaderItemList() {
        return headerItemList;
    }

    /**
     * 获取ItemFactory列表
     */
    @SuppressWarnings("unused")
    public List<AssemblyFragmentItemFactory> getItemFactoryList() {
        return itemFactoryList;
    }

    /**
     * 获取Footer列表
     */
    @SuppressWarnings("unused")
    public List<FixedFragmentItemInfo> getFooterItemList() {
        return footerItemList;
    }

    /**
     * 获取数据列表
     */
    @SuppressWarnings("unused")
    public List getDataList() {
        return dataList;
    }

    /**
     * 获取列表头的个数
     */
    public int getHeaderItemCount() {
        return headerItemList != null ? headerItemList.size() : 0;
    }

    /**
     * 获取ItemFactory的个数
     */
    public int getItemFactoryCount() {
        return itemFactoryList != null ? itemFactoryList.size() : 0;
    }

    /**
     * 获取列表头的个数
     */
    public int getFooterItemCount() {
        return footerItemList != null ? footerItemList.size() : 0;
    }

    /**
     * 获取数据列表的长度
     */
    public int getDataCount() {
        return dataList != null ? dataList.size() : 0;
    }

    /**
     * 获取在各自区域的位置
     */
    @SuppressWarnings("unused")
    public int getPositionInPart(int position) {
        // 头
        int headerItemCount = getHeaderItemCount();
        int headerStartPosition = 0;
        int headerEndPosition = headerItemCount - 1;
        if (position >= headerStartPosition && position <= headerEndPosition && headerItemCount > 0) {
            return position;
        }

        // 数据
        int dataCount = getDataCount();
        int dataStartPosition = headerEndPosition + 1;
        int dataEndPosition = headerEndPosition + dataCount;
        if (position >= dataStartPosition && position <= dataEndPosition && dataCount > 0) {
            return position - headerItemCount;
        }

        // 尾巴
        int footerItemCount = getFooterItemCount();
        int footerStartPosition = dataEndPosition + 1;
        int footerEndPosition = dataEndPosition + footerItemCount;
        if (position >= footerStartPosition && position <= footerEndPosition && footerItemCount > 0) {
            return position - headerItemCount - dataCount;
        }

        throw new IllegalArgumentException("illegal position: " + position);
    }

    @Override
    public int getCount() {
        itemFactoryLocked = true;
        return getHeaderItemCount() + getDataCount() + getFooterItemCount();
    }

    @Nullable
    public Object getHeaderItem(int positionInHeaderList){
        return headerItemList != null ? headerItemList.get(positionInHeaderList).getData() : null;
    }

    @Nullable
    public Object getDataItem(int positionInDataList){
        return dataList != null ? dataList.get(positionInDataList) : null;
    }

    @Nullable
    public Object getFooterItem(int positionInFooterList){
        return footerItemList != null ? footerItemList.get(positionInFooterList).getData() : null;
    }

    @Override
    public Fragment getItem(int position) {
        // 头
        int headerItemCount = getHeaderItemCount();
        int headerStartPosition = 0;
        int headerEndPosition = headerItemCount - 1;
        if (position >= headerStartPosition && position <= headerEndPosition && headerItemCount > 0) {
            //noinspection UnnecessaryLocalVariable
            int positionInHeaderList = position;
            FixedFragmentItemInfo fixedItemInfo = headerItemList.get(positionInHeaderList);
            //noinspection unchecked
            return fixedItemInfo.getItemFactory().dispatchCreateFragment(position, fixedItemInfo.getData());
        }

        // 数据
        int dataCount = getDataCount();
        int dataStartPosition = headerEndPosition + 1;
        int dataEndPosition = headerEndPosition + dataCount;
        if (position >= dataStartPosition && position <= dataEndPosition && dataCount > 0) {
            int positionInDataList = position - headerItemCount;
            Object dataObject = getDataItem(positionInDataList);

            AssemblyFragmentItemFactory itemFactory;
            for (int w = 0, size = itemFactoryList.size(); w < size; w++) {
                itemFactory = itemFactoryList.get(w);
                if (itemFactory.isTarget(dataObject)) {
                    //noinspection unchecked
                    return itemFactory.dispatchCreateFragment(position, dataObject);
                }
            }

            throw new IllegalStateException("Didn't find suitable AssemblyFragmentItemFactory. " +
                    "position=" + position + ", " +
                    "dataObject=" + (dataObject != null ? dataObject.getClass().getName() : "null"));
        }

        // 尾巴
        int footerItemCount = getFooterItemCount();
        int footerStartPosition = dataEndPosition + 1;
        int footerEndPosition = dataEndPosition + footerItemCount;
        if (position >= footerStartPosition && position <= footerEndPosition && footerItemCount > 0) {
            int positionInFooterList = position - headerItemCount - dataCount;
            FixedFragmentItemInfo fixedItemInfo = footerItemList.get(positionInFooterList);
            //noinspection unchecked
            return fixedItemInfo.getItemFactory().dispatchCreateFragment(position, fixedItemInfo.getData());
        }

        throw new IllegalArgumentException("illegal position: " + position);
    }
}
