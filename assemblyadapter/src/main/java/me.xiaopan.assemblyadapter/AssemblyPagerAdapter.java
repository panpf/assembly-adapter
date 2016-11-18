/*
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

import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * 通用组合式PagerAdapter，支持组合式多ItemType，支持头、尾巴
 */
public class AssemblyPagerAdapter extends PagerAdapter {
    private static final String TAG = "AssemblyPagerAdapter";

    private final Object itemListLock = new Object();
    private final Object headerItemListLock = new Object();
    private final Object itemFactoryListLock = new Object();
    private final Object footerItemListLock = new Object();

    private int headerItemPosition;
    private int footerItemPosition;
    private boolean itemFactoryLocked;
    private List dataList;
    private ArrayList<FixedPagerItemInfo> headerItemList;
    private ArrayList<FixedPagerItemInfo> footerItemList;
    private ArrayList<AssemblyPagerItemFactory> itemFactoryList;

    private boolean notifyOnChange = true;

    @SuppressWarnings("unused")
    public AssemblyPagerAdapter(List dataList) {
        this.dataList = dataList;
    }

    @SuppressWarnings("unused")
    public AssemblyPagerAdapter(Object[] dataArray) {
        if (dataArray != null && dataArray.length > 0) {
            this.dataList = new ArrayList(dataArray.length);
            Collections.addAll(dataList, dataArray);
        }
    }

    /**
     * 添加一个将按添加顺序显示在列表头部的AssemblyPagerItemFactory
     */
    @SuppressWarnings("unused")
    public FixedPagerItemInfo addHeaderItem(AssemblyPagerItemFactory headerFactory, Object data) {
        if (headerFactory == null || itemFactoryLocked) {
            Log.w(TAG, "headerFactory is nll or locked");
            return null;
        }

        headerFactory.setAdapter(this);
        FixedPagerItemInfo headerItemInfo = new FixedPagerItemInfo(headerFactory, data, true);
        headerItemInfo.setPosition(headerItemPosition++);

        synchronized (headerItemListLock) {
            if (headerItemList == null) {
                headerItemList = new ArrayList<FixedPagerItemInfo>(2);
            }
            headerItemList.add(headerItemInfo);
        }
        return headerItemInfo;
    }

    @SuppressWarnings("unused")
    public void addItemFactory(AssemblyPagerItemFactory itemFactory) {
        if (itemFactory == null || itemFactoryLocked) {
            Log.w(TAG, "itemFactory is nll or locked");
            return;
        }

        itemFactory.setAdapter(this);

        synchronized (itemFactoryListLock) {
            if (itemFactoryList == null) {
                itemFactoryList = new ArrayList<AssemblyPagerItemFactory>(2);
            }
            itemFactoryList.add(itemFactory);
        }
    }

    /**
     * 添加一个将按添加顺序显示在列表尾部的AssemblyPagerItemFactory
     */
    @SuppressWarnings("unused")
    public FixedPagerItemInfo addFooterItem(AssemblyPagerItemFactory footerFactory, Object data) {
        if (footerFactory == null || itemFactoryLocked) {
            Log.w(TAG, "footerFactory is nll or locked");
            return null;
        }

        footerFactory.setAdapter(this);
        FixedPagerItemInfo footerItemInfo = new FixedPagerItemInfo(footerFactory, data, false);
        footerItemInfo.setPosition(footerItemPosition++);

        synchronized (footerItemListLock) {
            if (footerItemList == null) {
                footerItemList = new ArrayList<FixedPagerItemInfo>(2);
            }
            footerItemList.add(footerItemInfo);
        }

        return footerItemInfo;
    }

    /**
     * 批量添加数据
     */
    @SuppressWarnings("unused")
    public void addAll(Collection collection) {
        if (collection == null || collection.size() == 0) {
            return;
        }
        synchronized (itemListLock) {
            if (dataList == null) {
                dataList = new ArrayList(collection.size());
            }
            //noinspection unchecked
            dataList.addAll(collection);
        }

        if (notifyOnChange) {
            notifyDataSetChanged();
        }
    }

    /**
     * 批量添加数据
     */
    @SuppressWarnings("unused")
    public void addAll(Object... items) {
        if (items == null || items.length == 0) {
            return;
        }
        synchronized (itemListLock) {
            if (dataList == null) {
                dataList = new ArrayList(items.length);
            }
            Collections.addAll(dataList, items);
        }

        if (notifyOnChange) {
            notifyDataSetChanged();
        }
    }

    /**
     * 插入一条数据
     */
    @SuppressWarnings("unused")
    public void insert(Object object, int index) {
        if (object == null) {
            return;
        }
        synchronized (itemListLock) {
            if (dataList == null) {
                dataList = new ArrayList();
            }
            //noinspection unchecked
            dataList.add(index, object);
        }

        if (notifyOnChange) {
            notifyDataSetChanged();
        }
    }

    /**
     * 删除一条数据
     */
    @SuppressWarnings("unused")
    public void remove(Object object) {
        if (object == null) {
            return;
        }
        synchronized (itemListLock) {
            if (dataList != null) {
                dataList.remove(object);
            }
        }

        if (notifyOnChange) {
            notifyDataSetChanged();
        }
    }

    /**
     * 清空数据
     */
    @SuppressWarnings("unused")
    public void clear() {
        synchronized (itemListLock) {
            if (dataList != null) {
                dataList.clear();
            }
        }

        if (notifyOnChange) {
            notifyDataSetChanged();
        }
    }

    /**
     * 对数据排序
     */
    @SuppressWarnings("unused")
    public void sort(Comparator comparator) {
        synchronized (itemListLock) {
            if (dataList != null) {
                Collections.sort(dataList, comparator);
            }
        }

        if (notifyOnChange) {
            notifyDataSetChanged();
        }
    }

    /**
     * header状态变化处理，不可用时从header列表中移除，可用时加回header列表中，并根据position排序来恢复其原本所在的位置
     */
    void headerEnabledChanged(FixedPagerItemInfo headerItemInfo) {
        if (headerItemInfo.getItemFactory().getAdapter() != this) {
            return;
        }

        if (headerItemInfo.isEnabled()) {
            synchronized (headerItemListLock) {
                headerItemList.add(headerItemInfo);
                Collections.sort(headerItemList, new Comparator<FixedPagerItemInfo>() {
                    @Override
                    public int compare(FixedPagerItemInfo lhs, FixedPagerItemInfo rhs) {
                        return lhs.getPosition() - rhs.getPosition();
                    }
                });
            }

            if (notifyOnChange) {
                notifyDataSetChanged();
            }
        } else {
            synchronized (headerItemListLock) {
                if (headerItemList.remove(headerItemInfo)) {
                    if (notifyOnChange) {
                        notifyDataSetChanged();
                    }
                }
            }
        }
    }

    /**
     * footer状态变化处理，不可用时从footer列表中移除，可用时加回footer列表中，并根据position排序来恢复其原本所在的位置
     */
    void footerEnabledChanged(FixedPagerItemInfo footerItemInfo) {
        if (footerItemInfo.getItemFactory().getAdapter() != this) {
            return;
        }

        if (footerItemInfo.isEnabled()) {
            synchronized (footerItemListLock) {
                footerItemList.add(footerItemInfo);
                Collections.sort(footerItemList, new Comparator<FixedPagerItemInfo>() {
                    @Override
                    public int compare(FixedPagerItemInfo lhs, FixedPagerItemInfo rhs) {
                        return lhs.getPosition() - rhs.getPosition();
                    }
                });
            }

            if (notifyOnChange) {
                notifyDataSetChanged();
            }
        } else {
            synchronized (footerItemListLock) {
                if (footerItemList.remove(footerItemInfo)) {
                    if (notifyOnChange) {
                        notifyDataSetChanged();
                    }
                }
            }
        }
    }

    /**
     * 删除一个HeaderItem
     *
     * @deprecated Use FixedItemInfo.setEnabled(false) instead
     */
    @SuppressWarnings("unused")
    @Deprecated
    public void removeHeaderItem(FixedPagerItemInfo headerItemInfo) {
        if (headerItemInfo == null) {
            return;
        }

        if (headerItemInfo.getItemFactory().getAdapter() != this) {
            return;
        }

        synchronized (headerItemListLock) {
            if (headerItemList != null && headerItemList.remove(headerItemInfo)) {
                if (notifyOnChange) {
                    notifyDataSetChanged();
                }
            }
        }
    }

    /**
     * 删除一个FooterItem
     *
     * @deprecated Use FixedItemInfo.setEnabled(false) instead
     */
    @SuppressWarnings("unused")
    @Deprecated
    public void removeFooterItem(FixedPagerItemInfo footerItemInfo) {
        if (footerItemInfo == null) {
            return;
        }

        if (footerItemInfo.getItemFactory().getAdapter() != this) {
            return;
        }

        synchronized (footerItemListLock) {
            if (footerItemList != null && footerItemList.remove(footerItemInfo)) {
                if (notifyOnChange) {
                    notifyDataSetChanged();
                }
            }
        }
    }

    /**
     * 获取Header列表
     */
    @SuppressWarnings("unused")
    public List<FixedPagerItemInfo> getHeaderItemList() {
        return headerItemList;
    }

    /**
     * 获取ItemFactory列表
     */
    @SuppressWarnings("unused")
    public List<AssemblyPagerItemFactory> getItemFactoryList() {
        return itemFactoryList;
    }

    /**
     * 获取Footer列表
     */
    @SuppressWarnings("unused")
    public List<FixedPagerItemInfo> getFooterItemList() {
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
     * 设置数据列表
     */
    @SuppressWarnings("unused")
    public void setDataList(List dataList) {
        synchronized (itemListLock) {
            this.dataList = dataList;
        }

        if (notifyOnChange) {
            notifyDataSetChanged();
        }
    }

    /**
     * 获取列表头的个数
     */
    @SuppressWarnings("WeakerAccess")
    public int getHeaderItemCount() {
        return headerItemList != null ? headerItemList.size() : 0;
    }

    /**
     * 获取ItemFactory的个数
     */
    @SuppressWarnings("WeakerAccess")
    public int getItemFactoryCount() {
        return itemFactoryList != null ? itemFactoryList.size() : 0;
    }

    /**
     * 获取列表头的个数
     */
    @SuppressWarnings("WeakerAccess")
    public int getFooterItemCount() {
        return footerItemList != null ? footerItemList.size() : 0;
    }

    /**
     * 获取数据列表的长度
     */
    @SuppressWarnings("WeakerAccess")
    public int getDataCount() {
        return dataList != null ? dataList.size() : 0;
    }

    /**
     * 数据变更时是否立即刷新列表
     */
    @SuppressWarnings({"unused", "WeakerAccess"})
    public boolean isNotifyOnChange() {
        return notifyOnChange;
    }

    /**
     * 设置当数据源发生改变时是否立即调用notifyDataSetChanged()刷新列表，默认true。
     * 当你需要连续多次修改数据的时候，你应该将notifyOnChange设为false，然后在最后主动调用notifyDataSetChanged()刷新列表，最后再将notifyOnChange设为true
     */
    @SuppressWarnings("unused")
    public void setNotifyOnChange(boolean notifyOnChange) {
        this.notifyOnChange = notifyOnChange;
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

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        if (getItemFactoryCount() <= 0) {
            throw new IllegalStateException("You need to configure AssemblyPagerItemFactory use addItemFactory method");
        }

        // 头
        int headerItemCount = getHeaderItemCount();
        int headerStartPosition = 0;
        int headerEndPosition = headerItemCount - 1;
        if (position >= headerStartPosition && position <= headerEndPosition && headerItemCount > 0) {
            FixedPagerItemInfo fixedItemInfo = headerItemList.get(position);
            //noinspection unchecked
            View itemView = fixedItemInfo.getItemFactory().createView(container.getContext(), container, position, fixedItemInfo.getData());
            container.addView(itemView);
            return itemView;
        }

        // 数据
        int dataCount = getDataCount();
        int dataStartPosition = headerEndPosition + 1;
        int dataEndPosition = headerEndPosition + dataCount;
        if (position >= dataStartPosition && position <= dataEndPosition && dataCount > 0) {
            int positionInDataList = position - headerItemCount;
            Object dataObject = dataList.get(positionInDataList);

            AssemblyPagerItemFactory itemFactory;
            for (int w = 0, size = itemFactoryList.size(); w < size; w++) {
                itemFactory = itemFactoryList.get(w);
                if (itemFactory.isTarget(dataObject)) {
                    //noinspection unchecked
                    View itemView = itemFactory.createView(container.getContext(), container, position, dataObject);
                    container.addView(itemView);
                    return itemView;
                }
            }

            throw new IllegalStateException("Didn't find suitable AssemblyPagerItemFactory. " +
                    "position=" + position + ", " +
                    "dataObject=" + (dataObject != null ? dataObject.getClass().getName() : "null"));
        }

        // 尾巴
        int footerItemCount = getFooterItemCount();
        int footerStartPosition = dataEndPosition + 1;
        int footerEndPosition = dataEndPosition + footerItemCount;
        if (position >= footerStartPosition && position <= footerEndPosition && footerItemCount > 0) {
            int positionInFooterList = position - headerItemCount - dataCount;
            FixedPagerItemInfo fixedItemInfo = footerItemList.get(positionInFooterList);
            //noinspection unchecked
            View itemView = fixedItemInfo.getItemFactory().createView(container.getContext(), container, position, fixedItemInfo.getData());
            container.addView(itemView);
            return itemView;
        }

        throw new IllegalArgumentException("illegal position: " + position);
    }
}
