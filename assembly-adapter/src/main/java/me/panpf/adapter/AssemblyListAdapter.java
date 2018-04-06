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

import android.support.annotation.Nullable;
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

/**
 * 通用组合式 BaseAdapter，支持组合式多 ItemType，支持头、尾巴以及加载更多
 */
public class AssemblyListAdapter extends BaseAdapter {
    private static final String TAG = "AssemblyListAdapter";

    private final Object itemListLock = new Object();
    private final Object headerItemListLock = new Object();
    private final Object itemFactoryListLock = new Object();
    private final Object footerItemListLock = new Object();

    private int itemTypeIndex = 0;
    private int headerItemPosition;
    private int footerItemPosition;
    private boolean itemFactoryLocked;
    private List dataList;
    private ArrayList<FixedListItemInfo> headerItemList;
    private ArrayList<FixedListItemInfo> footerItemList;
    private ArrayList<AssemblyListItemFactory> itemFactoryList;
    private SparseArray<Object> itemFactoryArray;

    private LoadMoreFixedListItemInfo loadMoreFixedListItemInfo;

    private boolean notifyOnChange = true;

    @SuppressWarnings("unused")
    public AssemblyListAdapter(List dataList) {
        this.dataList = dataList;
    }

    @SuppressWarnings("unused")
    public AssemblyListAdapter(Object[] dataArray) {
        if (dataArray != null && dataArray.length > 0) {
            this.dataList = new ArrayList(dataArray.length);
            Collections.addAll(dataList, dataArray);
        }
    }

    /**
     * 添加一个将按添加顺序显示在列表头部的 AssemblyItemFactory
     */
    @SuppressWarnings("unused")
    public FixedListItemInfo addHeaderItem(AssemblyListItemFactory headerFactory, Object data) {
        if (headerFactory == null || itemFactoryLocked) {
            Log.w(TAG, "headerFactory is nll or item factory locked");
            return null;
        }

        headerFactory.setAdapter(this);
        headerFactory.setItemType(itemTypeIndex++);

        FixedListItemInfo headerItemInfo = new FixedListItemInfo(headerFactory, data, true);
        headerItemInfo.setPosition(headerItemPosition++);

        if (itemFactoryArray == null) {
            itemFactoryArray = new SparseArray<Object>();
        }
        itemFactoryArray.put(headerFactory.getItemType(), headerItemInfo);

        synchronized (headerItemListLock) {
            if (headerItemList == null) {
                headerItemList = new ArrayList<FixedListItemInfo>(2);
            }
            headerItemList.add(headerItemInfo);
        }

        return headerItemInfo;
    }

    /**
     * 添加一个用来处理并显示 dataList 中的数据的 AssemblyItemFactory
     */
    public void addItemFactory(AssemblyListItemFactory itemFactory) {
        if (itemFactory == null || itemFactoryLocked) {
            Log.w(TAG, "itemFactory is nll or item factory locked");
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
                itemFactoryList = new ArrayList<AssemblyListItemFactory>(5);
            }
            itemFactoryList.add(itemFactory);
        }
    }

    /**
     * 添加一个将按添加顺序显示在列表尾部的 AssemblyItemFactory
     */
    @SuppressWarnings("unused")
    public FixedListItemInfo addFooterItem(AssemblyListItemFactory footerFactory, Object data) {
        if (footerFactory == null || itemFactoryLocked) {
            Log.w(TAG, "footerFactory is nll or item factory locked");
            return null;
        }

        footerFactory.setAdapter(this);
        footerFactory.setItemType(itemTypeIndex++);

        FixedListItemInfo footerItemInfo = new FixedListItemInfo(footerFactory, data, false);
        footerItemInfo.setPosition(footerItemPosition++);

        if (itemFactoryArray == null) {
            itemFactoryArray = new SparseArray<Object>();
        }
        itemFactoryArray.put(footerFactory.getItemType(), footerItemInfo);

        synchronized (footerItemListLock) {
            if (footerItemList == null) {
                footerItemList = new ArrayList<FixedListItemInfo>(2);
            }
            footerItemList.add(footerItemInfo);
        }

        return footerItemInfo;
    }

    /**
     * 设置一个将显示在列表最后（在 Footer 的后面）的加载更多尾巴
     */
    @SuppressWarnings({"unused", "WeakerAccess"})
    public LoadMoreFixedListItemInfo setLoadMoreItem(AssemblyLoadMoreListItemFactory loadMoreItemFactory, Object data) {
        if (loadMoreItemFactory == null || itemFactoryLocked) {
            Log.w(TAG, "loadMoreItemFactory is null or item factory locked");
            return null;
        }

        loadMoreItemFactory.setAdapter(this);
        if (this.loadMoreFixedListItemInfo != null) {
            loadMoreItemFactory.setItemType(this.loadMoreFixedListItemInfo.getItemFactory().getItemType());
        } else {
            loadMoreItemFactory.setItemType(itemTypeIndex++);
        }

        loadMoreItemFactory.loadMoreFinished(false);
        LoadMoreFixedListItemInfo loadMoreFixedListItemInfo = new LoadMoreFixedListItemInfo(loadMoreItemFactory, data, false);

        if (itemFactoryArray == null) {
            itemFactoryArray = new SparseArray<Object>();
        }
        itemFactoryArray.put(loadMoreItemFactory.getItemType(), loadMoreFixedListItemInfo);

        return this.loadMoreFixedListItemInfo = loadMoreFixedListItemInfo;
    }

    /**
     * 设置一个将显示在列表最后（在 Footer 的后面）的加载更多尾巴
     */
    @SuppressWarnings("unused")
    public LoadMoreFixedListItemInfo setLoadMoreItem(AssemblyLoadMoreListItemFactory loadMoreItemFactory) {
        return setLoadMoreItem(loadMoreItemFactory, null);
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
     * 设置禁用加载更多
     */
    @SuppressWarnings("unused")
    public void setDisableLoadMore(boolean disableLoadMore) {
        if (loadMoreFixedListItemInfo != null) {
            loadMoreFixedListItemInfo.setEnabled(!disableLoadMore);
        }
    }

    /**
     * 设置加载更多是否已结束，已结束会显示结束的文案并且不再触发加载更多
     *
     * @deprecated 使用 loadMoreFinished(boolean) 替代
     */
    @SuppressWarnings("unused")
    @Deprecated
    public void setLoadMoreEnd(boolean loadMoreEnd) {
        if (loadMoreFixedListItemInfo != null) {
            loadMoreFixedListItemInfo.loadMoreFinished(loadMoreEnd);
        }
    }

    /**
     * 加载更多完成时调用
     *
     * @param loadMoreEnd 全部加载完毕，为 true 会显示结束的文案并且不再触发加载更多
     */
    @SuppressWarnings("unused")
    public void loadMoreFinished(boolean loadMoreEnd) {
        if (loadMoreFixedListItemInfo != null) {
            loadMoreFixedListItemInfo.loadMoreFinished(loadMoreEnd);
        }
    }

    /**
     * 加载更多失败的时候调用此方法显示错误提示，并可点击重新加载
     */
    @SuppressWarnings("unused")
    public void loadMoreFailed() {
        if (loadMoreFixedListItemInfo != null) {
            loadMoreFixedListItemInfo.loadMoreFailed();
        }
    }

    /**
     * header状态变化处理，不可用时从 header 列表中移除，可用时加回 header 列表中，并根据 position 排序来恢复其原本所在的位置
     */
    void headerEnabledChanged(FixedListItemInfo headerItemInfo) {
        if (headerItemInfo.getItemFactory().getAdapter() != this) {
            return;
        }

        if (headerItemInfo.isEnabled()) {
            synchronized (headerItemListLock) {
                headerItemList.add(headerItemInfo);
                Collections.sort(headerItemList, new Comparator<FixedListItemInfo>() {
                    @Override
                    public int compare(FixedListItemInfo lhs, FixedListItemInfo rhs) {
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
     * footer 状态变化处理，不可用时从 footer 列表中移除，可用时加回 footer 列表中，并根据 position 排序来恢复其原本所在的位置
     */
    void footerEnabledChanged(FixedListItemInfo footerItemInfo) {
        if (footerItemInfo.getItemFactory().getAdapter() != this) {
            return;
        }

        if (footerItemInfo.isEnabled()) {
            synchronized (footerItemListLock) {
                footerItemList.add(footerItemInfo);
                Collections.sort(footerItemList, new Comparator<FixedListItemInfo>() {
                    @Override
                    public int compare(FixedListItemInfo lhs, FixedListItemInfo rhs) {
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
     * 删除一个 HeaderItem
     *
     * @deprecated Use FixedItemInfo.setEnabled(false) instead
     */
    @SuppressWarnings("unused")
    @Deprecated
    public void removeHeaderItem(FixedListItemInfo headerItemInfo) {
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
     * 删除一个 FooterItem
     *
     * @deprecated Use FixedItemInfo.setEnabled(false) instead
     */
    @SuppressWarnings("unused")
    @Deprecated
    public void removeFooterItem(FixedListItemInfo footerItemInfo) {
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
     * 获取 Header 列表
     */
    @SuppressWarnings("unused")
    public List<FixedListItemInfo> getHeaderItemList() {
        return headerItemList;
    }

    /**
     * 获取 ItemFactory 列表
     */
    @SuppressWarnings("unused")
    public List<AssemblyListItemFactory> getItemFactoryList() {
        return itemFactoryList;
    }

    /**
     * 获取 Footer 列表
     */
    @SuppressWarnings("unused")
    public List<FixedListItemInfo> getFooterItemList() {
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
     * 获取 ItemFactory 的个数
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
        return loadMoreFixedListItemInfo != null && loadMoreFixedListItemInfo.isEnabled();
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
     * 设置当数据发生改变时是否立即调用 notifyDataSetChanged() 刷新列表，默认 true。
     * 当你需要连续多次修改数据的时候，你应该将 notifyOnChange 设为 false，然后在最后主动调用 notifyDataSetChanged() 刷新列表，最后再将 notifyOnChange 设为 true
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
            return getHeaderItem(positionInHeaderList);
        }

        // 数据
        int dataCount = getDataCount();
        int dataStartPosition = headerEndPosition + 1;
        int dataEndPosition = headerEndPosition + dataCount;
        if (position >= dataStartPosition && position <= dataEndPosition && dataCount > 0) {
            int positionInDataList = position - headerItemCount;
            return getDataItem(positionInDataList);
        }

        // 尾巴
        int footerItemCount = getFooterItemCount();
        int footerStartPosition = dataEndPosition + 1;
        int footerEndPosition = dataEndPosition + footerItemCount;
        if (position >= footerStartPosition && position <= footerEndPosition && footerItemCount > 0) {
            int positionInFooterList = position - headerItemCount - dataCount;
            return getFooterItem(positionInFooterList);
        }

        // 加载更多尾巴
        if (dataCount > 0 && hasLoadMoreFooter() && position == getCount() - 1) {
            return loadMoreFixedListItemInfo.getData();
        }

        return null;
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
            Object dataObject = getDataItem(positionInDataList);

            AssemblyListItemFactory itemFactory;
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
            return loadMoreFixedListItemInfo.getItemFactory().getItemType();
        }

        throw new IllegalStateException("not found match viewType, position: " + position);
    }

    @Override
    @SuppressWarnings("unchecked")
    public View getView(final int position, View convertView, ViewGroup parent) {
        AssemblyListItem assemblyListItem;
        if (convertView == null) {
            assemblyListItem = createItem(parent, getItemViewType(position));
            convertView = assemblyListItem.getItemView();
            convertView.setTag(assemblyListItem);
        } else {
            assemblyListItem = (AssemblyListItem) convertView.getTag();
        }
        bindItem(assemblyListItem, position);
        return convertView;
    }

    private AssemblyListItem createItem(ViewGroup parent, int viewType) {
        Object item = itemFactoryArray.get(viewType);

        // Item
        if (item instanceof AssemblyListItemFactory) {
            AssemblyListItemFactory itemFactory = (AssemblyListItemFactory) item;
            return itemFactory.dispatchCreateAssemblyItem(parent);
        }

        // 头或尾巴或加载更多尾巴
        if (item instanceof FixedListItemInfo) {
            FixedListItemInfo fixedListItemInfo = (FixedListItemInfo) item;
            return fixedListItemInfo.getItemFactory().dispatchCreateAssemblyItem(parent);
        }

        throw new IllegalStateException("unknown viewType: " + viewType + ", " +
                "itemFactory: " + (item != null ? item.getClass().getName() : "null"));
    }

    private void bindItem(AssemblyListItem assemblyListItem, int position) {
        //noinspection unchecked
        assemblyListItem.setData(position, getItem(position));
    }
}
