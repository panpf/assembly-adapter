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
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * 通用组合式 {@link PagerAdapter}，支持组合式多类型 item，支持头、尾巴
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public class AssemblyPagerAdapter extends PagerAdapter {

    @NonNull
    private final Object itemListLock = new Object();
    @NonNull
    private final Object headerItemListLock = new Object();
    @NonNull
    private final Object itemFactoryListLock = new Object();
    @NonNull
    private final Object footerItemListLock = new Object();

    private int headerItemPosition;
    private int footerItemPosition;
    private boolean itemFactoryLocked;

    @Nullable
    private List dataList;
    @Nullable
    private ArrayList<FixedPagerItemInfo> headerItemList;
    @Nullable
    private ArrayList<FixedPagerItemInfo> footerItemList;
    @Nullable
    private ArrayList<AssemblyPagerItemFactory> itemFactoryList;

    private boolean notifyOnChange = true;

    public AssemblyPagerAdapter() {
    }

    public AssemblyPagerAdapter(@Nullable List dataList) {
        this.dataList = dataList;
    }

    public AssemblyPagerAdapter(@Nullable Object[] dataArray) {
        if (dataArray != null && dataArray.length > 0) {
            this.dataList = new ArrayList(dataArray.length);
            Collections.addAll(dataList, dataArray);
        }
    }

    public void addItemFactory(@NonNull AssemblyPagerItemFactory itemFactory) {
        //noinspection ConstantConditions
        if (itemFactory == null || itemFactoryLocked) {
            throw new IllegalArgumentException("itemFactory is null or item factory list locked");
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
     * 添加一个将按添加顺序显示在列表头部的AssemblyPagerItemFactory
     */
    @NonNull
    public FixedPagerItemInfo addHeaderItem(@NonNull AssemblyPagerItemFactory headerFactory, @Nullable Object data) {
        //noinspection ConstantConditions
        if (headerFactory == null || itemFactoryLocked) {
            throw new IllegalArgumentException("itemFactory is null or item factory list locked");
        }

        headerFactory.setAdapter(this);
        FixedPagerItemInfo headerItemInfo = new FixedPagerItemInfo(headerFactory, data, true);
        headerItemInfo.setPosition(headerItemPosition++);

        synchronized (headerItemListLock) {
            if (headerItemList == null) {
                headerItemList = new ArrayList<FixedPagerItemInfo>(1);
            }
            headerItemList.add(headerItemInfo);
        }
        return headerItemInfo;
    }

    /**
     * 添加一个将按添加顺序显示在列表尾部的AssemblyPagerItemFactory
     */
    @NonNull
    public FixedPagerItemInfo addFooterItem(@NonNull AssemblyPagerItemFactory footerFactory, @Nullable Object data) {
        //noinspection ConstantConditions
        if (footerFactory == null || itemFactoryLocked) {
            throw new IllegalArgumentException("itemFactory is null or item factory list locked");
        }

        footerFactory.setAdapter(this);
        FixedPagerItemInfo footerItemInfo = new FixedPagerItemInfo(footerFactory, data, false);
        footerItemInfo.setPosition(footerItemPosition++);

        synchronized (footerItemListLock) {
            if (footerItemList == null) {
                footerItemList = new ArrayList<FixedPagerItemInfo>(1);
            }
            footerItemList.add(footerItemInfo);
        }

        return footerItemInfo;
    }

    /**
     * 批量添加数据
     */
    public void addAll(@NonNull Collection collection) {
        //noinspection ConstantConditions
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
    public void addAll(@NonNull Object... items) {
        //noinspection ConstantConditions
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
    public void insert(@NonNull Object object, int index) {
        //noinspection ConstantConditions
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
    public void remove(@NonNull Object object) {
        //noinspection ConstantConditions
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
    public void sort(@NonNull Comparator comparator) {
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
     * header 状态变化处理，不可用时从 header 列表中移除，可用时加回 header 列表中，并根据 position 排序来恢复其原本所在的位置
     */
    void headerEnabledChanged(@NonNull FixedPagerItemInfo headerItemInfo) {
        if (headerItemInfo.getItemFactory().getAdapter() != this) {
            return;
        }

        if (headerItemInfo.isEnabled()) {
            synchronized (headerItemListLock) {
                if (headerItemList == null) {
                    headerItemList = new ArrayList<FixedPagerItemInfo>(1);
                }
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
                if (headerItemList != null && headerItemList.remove(headerItemInfo)) {
                    if (notifyOnChange) {
                        notifyDataSetChanged();
                    }
                }
            }
        }
    }

    /**
     * footer 状态变化处理，不可用时从 footer 列表中移除，可用时加回 footer 列表中，并根据 position 排序来恢复其原本所在的位置
     */
    void footerEnabledChanged(@NonNull FixedPagerItemInfo footerItemInfo) {
        if (footerItemInfo.getItemFactory().getAdapter() != this) {
            return;
        }

        if (footerItemInfo.isEnabled()) {
            synchronized (footerItemListLock) {
                if (footerItemList == null) {
                    footerItemList = new ArrayList<FixedPagerItemInfo>(1);
                }
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
                if (footerItemList != null && footerItemList.remove(footerItemInfo)) {
                    if (notifyOnChange) {
                        notifyDataSetChanged();
                    }
                }
            }
        }
    }

    /**
     * 获取 header 列表
     */
    @Nullable
    public List<FixedPagerItemInfo> getHeaderItemList() {
        return headerItemList;
    }

    /**
     * 获取 {@link AssemblyPagerItemFactory} 列表
     */
    @Nullable
    public List<AssemblyPagerItemFactory> getItemFactoryList() {
        return itemFactoryList;
    }

    /**
     * 获取 Footer 列表
     */
    @Nullable
    public List<FixedPagerItemInfo> getFooterItemList() {
        return footerItemList;
    }

    /**
     * 获取数据列表
     */
    @Nullable
    public List getDataList() {
        return dataList;
    }

    /**
     * 设置数据列表
     */
    public void setDataList(@Nullable List dataList) {
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
    public int getHeaderItemCount() {
        return headerItemList != null ? headerItemList.size() : 0;
    }

    /**
     * 获取 {@link AssemblyPagerItemFactory} 的个数
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
     * 数据变更时是否立即刷新列表
     */
    public boolean isNotifyOnChange() {
        return notifyOnChange;
    }

    /**
     * 设置当数据源发生改变时是否立即调用 notifyDataSetChanged() 刷新列表，默认 true。
     * 当你需要连续多次修改数据的时候，你应该将 notifyOnChange 设为 false，然后在最后主动调用 notifyDataSetChanged() 刷新列表，最后再将 notifyOnChange 设为 true
     */
    public void setNotifyOnChange(boolean notifyOnChange) {
        this.notifyOnChange = notifyOnChange;
    }

    /**
     * 获取在各自区域的位置
     */
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
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    @Nullable
    public Object getHeaderItem(int positionInHeaderList) {
        return headerItemList != null ? headerItemList.get(positionInHeaderList).getData() : null;
    }

    @Nullable
    public Object getDataItem(int positionInDataList) {
        return dataList != null ? dataList.get(positionInDataList) : null;
    }

    @Nullable
    public Object getFooterItem(int positionInFooterList) {
        return footerItemList != null ? footerItemList.get(positionInFooterList).getData() : null;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        // 头
        int headerItemCount = getHeaderItemCount();
        int headerStartPosition = 0;
        int headerEndPosition = headerItemCount - 1;
        if (headerItemList != null && position >= headerStartPosition && position <= headerEndPosition && headerItemCount > 0) {
            //noinspection UnnecessaryLocalVariable
            int positionInHeaderList = position;
            FixedPagerItemInfo fixedItemInfo = headerItemList.get(positionInHeaderList);
            //noinspection unchecked
            View itemView = fixedItemInfo.getItemFactory().dispatchCreateView(container.getContext(), container, position, fixedItemInfo.getData());
            container.addView(itemView);
            return itemView;
        }

        // 数据
        int dataCount = getDataCount();
        int dataStartPosition = headerEndPosition + 1;
        int dataEndPosition = headerEndPosition + dataCount;
        if (itemFactoryList != null && position >= dataStartPosition && position <= dataEndPosition && dataCount > 0) {
            int positionInDataList = position - headerItemCount;
            Object dataObject = getDataItem(positionInDataList);

            AssemblyPagerItemFactory itemFactory;
            for (int w = 0, size = itemFactoryList.size(); w < size; w++) {
                itemFactory = itemFactoryList.get(w);
                if (itemFactory.isTarget(dataObject)) {
                    //noinspection unchecked
                    View itemView = itemFactory.dispatchCreateView(container.getContext(), container, position, dataObject);
                    container.addView(itemView);
                    return itemView;
                }
            }

            throw new IllegalStateException(String.format("Didn't find suitable AssemblyPagerItemFactory. position=%d, dataObject=%s",
                    position, dataObject != null ? dataObject.getClass().getName() : "null"));
        }

        // 尾巴
        int footerItemCount = getFooterItemCount();
        int footerStartPosition = dataEndPosition + 1;
        int footerEndPosition = dataEndPosition + footerItemCount;
        if (footerItemList != null && position >= footerStartPosition && position <= footerEndPosition && footerItemCount > 0) {
            int positionInFooterList = position - headerItemCount - dataCount;
            FixedPagerItemInfo fixedItemInfo = footerItemList.get(positionInFooterList);
            //noinspection unchecked
            View itemView = fixedItemInfo.getItemFactory().dispatchCreateView(container.getContext(), container, position, fixedItemInfo.getData());
            container.addView(itemView);
            return itemView;
        }

        throw new IllegalArgumentException("Illegal position: " + position);
    }
}
