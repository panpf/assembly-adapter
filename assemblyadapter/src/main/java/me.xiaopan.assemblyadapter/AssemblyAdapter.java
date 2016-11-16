/**
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

import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import me.xiaopan.assemblyadapter.AssemblyLoadMoreItemFactory.AssemblyLoadMoreItem;

/**
 * 通用组合式BaseAdapter，支持组合式多ItemType，支持头、尾巴以及加载更多
 */
public class AssemblyAdapter extends BaseAdapter {
    private static final String TAG = "AssemblyAdapter";

    private final Object itemListLock = new Object();
    private final Object headerItemListLock = new Object();
    private final Object itemFactoryListLock = new Object();
    private final Object footerItemListLock = new Object();

    private int itemTypeIndex = 0;
    private int headerItemPosition;
    private int footerItemPosition;
    private boolean itemFactoryLocked;
    private List dataList;
    private ArrayList<FixedItemInfo> headerItemList;
    private ArrayList<FixedItemInfo> footerItemList;
    private ArrayList<AssemblyItemFactory> itemFactoryList;
    private SparseArray<Object> itemFactoryArray;

    private boolean disableLoadMore;
    private AssemblyLoadMoreItem loadMoreItem;
    private AssemblyLoadMoreItemFactory loadMoreItemFactory;

    private boolean notifyOnChange = true;

    @SuppressWarnings("unused")
    public AssemblyAdapter(List dataList) {
        this.dataList = dataList;
    }

    @SuppressWarnings("unused")
    public AssemblyAdapter(Object[] dataArray) {
        if (dataArray != null && dataArray.length > 0) {
            this.dataList = new ArrayList(dataArray.length);
            Collections.addAll(dataList, dataArray);
        }
    }

    /**
     * 添加一个将按添加顺序显示在列表头部的AssemblyItemFactory
     */
    @SuppressWarnings("unused")
    public FixedItemInfo addHeaderItem(AssemblyItemFactory headerFactory, Object data) {
        if (headerFactory == null || itemFactoryLocked) {
            Log.w(TAG, "headerFactory is nll or locked");
            return null;
        }

        headerFactory.setAdapter(this);
        headerFactory.setItemType(itemTypeIndex++);
        FixedItemInfo headerItemInfo = new FixedItemInfo(headerFactory, data, true);
        headerItemInfo.setPosition(headerItemPosition++);

        if (itemFactoryArray == null) {
            itemFactoryArray = new SparseArray<Object>();
        }
        itemFactoryArray.put(headerFactory.getItemType(), headerItemInfo);

        synchronized (headerItemListLock) {
            if (headerItemList == null) {
                headerItemList = new ArrayList<FixedItemInfo>(2);
            }
            headerItemList.add(headerItemInfo);
        }

        return headerItemInfo;
    }

    /**
     * 添加一个用来处理并显示dataList中的数据的AssemblyItemFactory
     */
    public void addItemFactory(AssemblyItemFactory itemFactory) {
        if (itemFactory == null || itemFactoryLocked) {
            Log.w(TAG, "itemFactory is nll or locked");
            return;
        }

        itemFactory.setAdapter(this);
        itemFactory.setItemType(itemTypeIndex++);

        if (itemFactoryArray == null) {
            itemFactoryArray = new SparseArray<Object>();
        }
        itemFactoryArray.put(itemFactory.getItemType(), itemFactory);

        synchronized (itemFactoryListLock) {
            if (itemFactoryList == null) {
                itemFactoryList = new ArrayList<AssemblyItemFactory>(5);
            }
            itemFactoryList.add(itemFactory);
        }
    }

    /**
     * 添加一个将按添加顺序显示在列表尾部的AssemblyItemFactory
     */
    @SuppressWarnings("unused")
    public FixedItemInfo addFooterItem(AssemblyItemFactory footerFactory, Object data) {
        if (footerFactory == null || itemFactoryLocked) {
            Log.w(TAG, "footerFactory is nll or locked");
            return null;
        }

        footerFactory.setAdapter(this);
        footerFactory.setItemType(itemTypeIndex++);
        FixedItemInfo footerItemInfo = new FixedItemInfo(footerFactory, data, false);
        footerItemInfo.setPosition(footerItemPosition++);

        if (itemFactoryArray == null) {
            itemFactoryArray = new SparseArray<Object>();
        }
        itemFactoryArray.put(footerFactory.getItemType(), footerItemInfo);

        synchronized (footerItemListLock) {
            if (footerItemList == null) {
                footerItemList = new ArrayList<FixedItemInfo>(2);
            }
            footerItemList.add(footerItemInfo);
        }

        return footerItemInfo;
    }

    /**
     * 设置一个将显示在列表最后（在Footer的后面）的加载更多尾巴
     */
    @SuppressWarnings("unused")
    public void setLoadMoreItem(AssemblyLoadMoreItemFactory newLoadMoreItemFactory) {
        if (newLoadMoreItemFactory == null || itemFactoryLocked) {
            Log.w(TAG, "loadMoreItemFactory is null or locked");
            return;
        }

        newLoadMoreItemFactory.setEnd(false);
        newLoadMoreItemFactory.setAdapter(this);
        if (loadMoreItemFactory != null) {
            newLoadMoreItemFactory.setItemType(loadMoreItemFactory.getItemType());
        } else {
            newLoadMoreItemFactory.setItemType(itemTypeIndex++);
        }
        newLoadMoreItemFactory.setLoadMoreRunning(false);

        if (itemFactoryArray == null) {
            itemFactoryArray = new SparseArray<Object>();
        }
        itemFactoryArray.put(newLoadMoreItemFactory.getItemType(), newLoadMoreItemFactory);

        loadMoreItemFactory = newLoadMoreItemFactory;
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
     * 设置是否禁用加载更多
     */
    @SuppressWarnings("unused")
    public void setDisableLoadMore(boolean disableLoadMore) {
        this.disableLoadMore = disableLoadMore;

        if (loadMoreItemFactory != null) {
            loadMoreItemFactory.setEnd(false);
            loadMoreItemFactory.setLoadMoreRunning(false);
        }

        if (notifyOnChange) {
            notifyDataSetChanged();
        }
    }

    /**
     * 设置加载更多是否已结束，已结束会显示结束的文案并且不再触发加载更多
     */
    @SuppressWarnings("unused")
    public void setLoadMoreEnd(boolean loadMoreEnd) {
        if (loadMoreItemFactory != null) {
            loadMoreItemFactory.setLoadMoreRunning(false);
            loadMoreItemFactory.setEnd(loadMoreEnd);
        }
        if (loadMoreItem != null) {
            if (loadMoreEnd) {
                loadMoreItem.showEnd();
            } else {
                loadMoreItem.showLoading();
            }
        }
    }

    /**
     * 加载更多失败的时候调用此方法显示错误提示，并可点击重新加载
     */
    @SuppressWarnings("unused")
    public void loadMoreFailed() {
        if (loadMoreItemFactory != null) {
            loadMoreItemFactory.setLoadMoreRunning(false);
        }
        if (loadMoreItem != null) {
            loadMoreItem.showErrorRetry();
        }
    }

    /**
     * header状态变化处理，不可用时从header列表中移除，可用时加回header列表中，并根据position排序来恢复其原本所在的位置
     */
    void headerEnabledChanged(FixedItemInfo headerItemInfo) {
        if (headerItemInfo.getItemFactory().getAdapter() != this) {
            return;
        }

        if (headerItemInfo.isEnabled()) {
            synchronized (headerItemListLock) {
                headerItemList.add(headerItemInfo);
                Collections.sort(headerItemList, new Comparator<FixedItemInfo>() {
                    @Override
                    public int compare(FixedItemInfo lhs, FixedItemInfo rhs) {
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
    void footerEnabledChanged(FixedItemInfo footerItemInfo) {
        if (footerItemInfo.getItemFactory().getAdapter() != this) {
            return;
        }

        if (footerItemInfo.isEnabled()) {
            synchronized (footerItemListLock) {
                footerItemList.add(footerItemInfo);
                Collections.sort(footerItemList, new Comparator<FixedItemInfo>() {
                    @Override
                    public int compare(FixedItemInfo lhs, FixedItemInfo rhs) {
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
    public void removeHeaderItem(FixedItemInfo headerItemInfo) {
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
    public void removeFooterItem(FixedItemInfo footerItemInfo) {
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
    public List<FixedItemInfo> getHeaderItemList() {
        return headerItemList;
    }

    /**
     * 获取ItemFactory列表
     */
    @SuppressWarnings("unused")
    public List<AssemblyItemFactory> getItemFactoryList() {
        return itemFactoryList;
    }

    /**
     * 获取Footer列表
     */
    @SuppressWarnings("unused")
    public List<FixedItemInfo> getFooterItemList() {
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
     * 是否有加载更多尾巴
     */
    @SuppressWarnings("WeakerAccess")
    public boolean hasLoadMoreFooter() {
        return !disableLoadMore && loadMoreItemFactory != null;
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
     * 设置当数据发生改变时是否立即调用notifyDataSetChanged()刷新列表，默认true。
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

        // 加载更多尾巴
        if (dataCount > 0 && hasLoadMoreFooter() && position == getCount() - 1) {
            return 0;
        }

        throw new IllegalArgumentException("illegal position: " + position);
    }

    @Override
    public int getCount() {
        int headerItemCount = getHeaderItemCount();
        int dataCount = getDataCount();
        int footerItemCount = getFooterItemCount();

        if (dataCount > 0) {
            return headerItemCount + dataCount + footerItemCount + (hasLoadMoreFooter() ? 1 : 0);
        } else {
            return headerItemCount + footerItemCount;
        }
    }

    @Override
    public Object getItem(int position) {
        // 头
        int headerItemCount = getHeaderItemCount();
        int headerStartPosition = 0;
        int headerEndPosition = headerItemCount - 1;
        if (position >= headerStartPosition && position <= headerEndPosition && headerItemCount > 0) {
            //noinspection UnnecessaryLocalVariable
            int positionInHeaderList = position;
            return headerItemList.get(positionInHeaderList).getData();
        }

        // 数据
        int dataCount = getDataCount();
        int dataStartPosition = headerEndPosition + 1;
        int dataEndPosition = headerEndPosition + dataCount;
        if (position >= dataStartPosition && position <= dataEndPosition && dataCount > 0) {
            int positionInDataList = position - headerItemCount;
            return dataList.get(positionInDataList);
        }

        // 尾巴
        int footerItemCount = getFooterItemCount();
        int footerStartPosition = dataEndPosition + 1;
        int footerEndPosition = dataEndPosition + footerItemCount;
        if (position >= footerStartPosition && position <= footerEndPosition && footerItemCount > 0) {
            int positionInFooterList = position - headerItemCount - dataCount;
            return footerItemList.get(positionInFooterList).getData();
        }

        // 加载更多尾巴
        if (dataCount > 0 && hasLoadMoreFooter() && position == getCount() - 1) {
            return null;
        }

        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getViewTypeCount() {
        itemFactoryLocked = true;
        return itemTypeIndex > 0 ? itemTypeIndex : super.getViewTypeCount();
    }

    @Override
    public int getItemViewType(int position) {
        if (getItemFactoryCount() <= 0) {
            throw new IllegalStateException("You need to configure AssemblyItemFactory use addItemFactory method");
        }
        itemFactoryLocked = true;

        // 头
        int headerItemCount = getHeaderItemCount();
        int headerStartPosition = 0;
        int headerEndPosition = headerItemCount - 1;
        if (position >= headerStartPosition && position <= headerEndPosition && headerItemCount > 0) {
            //noinspection UnnecessaryLocalVariable
            int positionInHeaderList = position;
            return headerItemList.get(positionInHeaderList).getItemFactory().getItemType();
        }

        // 数据
        int dataCount = getDataCount();
        int dataStartPosition = headerEndPosition + 1;
        int dataEndPosition = headerEndPosition + dataCount;
        if (position >= dataStartPosition && position <= dataEndPosition && dataCount > 0) {
            int positionInDataList = position - headerItemCount;
            Object dataObject = dataList.get(positionInDataList);

            AssemblyItemFactory itemFactory;
            for (int w = 0, size = itemFactoryList.size(); w < size; w++) {
                itemFactory = itemFactoryList.get(w);
                if (itemFactory.isTarget(dataObject)) {
                    return itemFactory.getItemType();
                }
            }

            throw new IllegalStateException("Didn't find suitable AssemblyItemFactory. " +
                    "positionInDataList=" + positionInDataList + ", " +
                    "dataObject=" + (dataObject != null ? dataObject.getClass().getName() : "null"));
        }

        // 尾巴
        int footerItemCount = getFooterItemCount();
        int footerStartPosition = dataEndPosition + 1;
        int footerEndPosition = dataEndPosition + footerItemCount;
        if (position >= footerStartPosition && position <= footerEndPosition && footerItemCount > 0) {
            int positionInFooterList = position - headerItemCount - dataCount;
            return footerItemList.get(positionInFooterList).getItemFactory().getItemType();
        }

        // 加载更多尾巴
        if (dataCount > 0 && hasLoadMoreFooter() && position == getCount() - 1) {
            return loadMoreItemFactory.getItemType();
        }

        throw new IllegalStateException("not found match viewType, position: " + position);
    }

    @Override
    @SuppressWarnings("unchecked")
    public View getView(final int position, View convertView, ViewGroup parent) {
        AssemblyItem assemblyItem;
        if (convertView == null) {
            assemblyItem = createItem(parent, getItemViewType(position));
            convertView = assemblyItem.getItemView();
            convertView.setTag(assemblyItem);
        } else {
            assemblyItem = (AssemblyItem) convertView.getTag();
        }
        bindItem(assemblyItem, position);
        return convertView;
    }

    private AssemblyItem createItem(ViewGroup parent, int viewType) {
        Object item = itemFactoryArray.get(viewType);

        // Item或加载更多尾巴
        if (item instanceof AssemblyItemFactory) {
            AssemblyItemFactory itemFactory = (AssemblyItemFactory) item;
            AssemblyItem assemblyItem = itemFactory.createAssemblyItem(parent);
            if (assemblyItem instanceof AssemblyLoadMoreItem) {
                this.loadMoreItem = (AssemblyLoadMoreItem) assemblyItem;
            }
            return assemblyItem;
        }

        // 头或尾巴
        if (item instanceof FixedItemInfo) {
            FixedItemInfo fixedItemInfo = (FixedItemInfo) item;
            return fixedItemInfo.getItemFactory().createAssemblyItem(parent);
        }

        throw new IllegalStateException("unknown viewType: " + viewType + ", " +
                "itemFactory: " + (item != null ? item.getClass().getName() : "null"));
    }

    @SuppressWarnings({"unchecked", "WeakerAccess"})
    public void bindItem(AssemblyItem assemblyItem, int position) {
        assemblyItem.setData(position, getItem(position));
    }
}
