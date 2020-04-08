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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class PagerItemManager {

    @NonNull
    private AssemblyPagerAdapter adapter;
    @NonNull
    private PagerItemHolderManager headerItemManager = new PagerItemHolderManager();
    @NonNull
    private ArrayList<AssemblyPagerItemFactory> itemFactoryList = new ArrayList<>();
    @NonNull
    private PagerItemHolderManager footerItemManager = new PagerItemHolderManager();

    @Nullable
    private List dataList;
    private boolean notifyOnChange = true;

    public PagerItemManager(@NonNull AssemblyPagerAdapter adapter) {
        this.adapter = adapter;
    }

    public PagerItemManager(@NonNull AssemblyPagerAdapter adapter, @Nullable List dataList) {
        this.adapter = adapter;
        this.dataList = dataList;
    }

    public PagerItemManager(@NonNull AssemblyPagerAdapter adapter, @Nullable Object[] dataArray) {
        this.adapter = adapter;
        if (dataArray != null && dataArray.length > 0) {
            this.dataList = new ArrayList(dataArray.length);
            Collections.addAll(dataList, dataArray);
        }
    }


    public void addItemFactory(@NonNull AssemblyPagerItemFactory itemFactory) {
        //noinspection ConstantConditions
        if (itemFactory == null) {
            throw new IllegalArgumentException("itemFactory is null");
        }

        itemFactoryList.add(itemFactory);

        itemFactory.attachToAdapter(adapter);
    }


    @NonNull
    public <DATA> PagerItemHolder<DATA> addHeaderItem(@NonNull AssemblyPagerItemFactory<DATA> itemFactory, @Nullable DATA data) {
        //noinspection ConstantConditions
        if (itemFactory == null) {
            throw new IllegalArgumentException("itemFactory is null");
        }

        PagerItemHolder<DATA> itemHolder = new PagerItemHolder<>(itemFactory, data);
        headerItemManager.add(itemHolder);

        itemFactory.attachToAdapter(adapter);
        itemHolder.attachToAdapter(this, true);
        return itemHolder;
    }

    @NonNull
    public <DATA> PagerItemHolder<DATA> addHeaderItem(@NonNull AssemblyPagerItemFactory<DATA> itemFactory) {
        return addHeaderItem(itemFactory, null);
    }

    @NonNull
    public PagerItemHolderManager getHeaderItemManager() {
        return headerItemManager;
    }


    @NonNull
    public <DATA> PagerItemHolder<DATA> addFooterItem(@NonNull AssemblyPagerItemFactory<DATA> itemFactory, @Nullable DATA data) {
        //noinspection ConstantConditions
        if (itemFactory == null) {
            throw new IllegalArgumentException("itemFactory is null");
        }

        PagerItemHolder<DATA> itemHolder = new PagerItemHolder<>(itemFactory, data);
        footerItemManager.add(itemHolder);

        itemFactory.attachToAdapter(adapter);
        itemHolder.attachToAdapter(this, false);
        return itemHolder;
    }

    @NonNull
    public <DATA> PagerItemHolder<DATA> addFooterItem(@NonNull AssemblyPagerItemFactory<DATA> itemFactory) {
        return addFooterItem(itemFactory, null);
    }

    @NonNull
    public PagerItemHolderManager getFooterItemManager() {
        return footerItemManager;
    }


    void itemHolderEnabledChanged(@NonNull PagerItemHolder itemHolder) {
        if (itemHolder.getItemFactory().getAdapter() != adapter) {
            return;
        }

        if (itemHolder.isHeader()) {
            if (headerItemManager.itemEnabledChanged() && notifyOnChange) {
                adapter.notifyDataSetChanged();
            }
        } else {
            if (footerItemManager.itemEnabledChanged() && notifyOnChange) {
                adapter.notifyDataSetChanged();
            }
        }
    }


    @Nullable
    public List getDataList() {
        return dataList;
    }

    public void setDataList(@Nullable List dataList) {
        synchronized (this) {
            this.dataList = dataList;
        }

        if (notifyOnChange) {
            adapter.notifyDataSetChanged();
        }
    }

    public void addAll(@Nullable Collection collection) {
        if (collection == null || collection.size() == 0) {
            return;
        }
        synchronized (this) {
            if (dataList == null) {
                dataList = new ArrayList(collection.size());
            }
            //noinspection unchecked
            dataList.addAll(collection);
        }

        if (notifyOnChange) {
            adapter.notifyDataSetChanged();
        }
    }

    public void addAll(@Nullable Object... items) {
        if (items == null || items.length == 0) {
            return;
        }
        synchronized (this) {
            if (dataList == null) {
                dataList = new ArrayList(items.length);
            }
            Collections.addAll(dataList, items);
        }

        if (notifyOnChange) {
            adapter.notifyDataSetChanged();
        }
    }

    public void insert(@NonNull Object object, int index) {
        //noinspection ConstantConditions
        if (object == null) {
            return;
        }
        synchronized (this) {
            if (dataList == null) {
                dataList = new ArrayList();
            }
            //noinspection unchecked
            dataList.add(index, object);
        }

        if (notifyOnChange) {
            adapter.notifyDataSetChanged();
        }
    }

    public void remove(@NonNull Object object) {
        //noinspection ConstantConditions
        if (object == null) {
            return;
        }
        synchronized (this) {
            if (dataList != null) {
                dataList.remove(object);
            }
        }

        if (notifyOnChange) {
            adapter.notifyDataSetChanged();
        }
    }

    public void clear() {
        synchronized (this) {
            if (dataList != null) {
                dataList.clear();
            }
        }

        if (notifyOnChange) {
            adapter.notifyDataSetChanged();
        }
    }

    public void sort(@NonNull Comparator comparator) {
        synchronized (this) {
            if (dataList != null) {
                Collections.sort(dataList, comparator);
            }
        }

        if (notifyOnChange) {
            adapter.notifyDataSetChanged();
        }
    }

    public int getDataCount() {
        return dataList != null ? dataList.size() : 0;
    }

    @Nullable
    public Object getData(int positionInDataList) {
        return dataList != null ? dataList.get(positionInDataList) : null;
    }


    public boolean isNotifyOnChange() {
        return notifyOnChange;
    }

    public void setNotifyOnChange(boolean notifyOnChange) {
        this.notifyOnChange = notifyOnChange;
    }

    public int getItemCount() {
        return headerItemManager.getEnabledItemCount() + getDataCount() + footerItemManager.getEnabledItemCount();
    }

    @NonNull
    public AssemblyPagerItemFactory getItemFactoryByPosition(int position) {
        // header
        int headerItemCount = headerItemManager.getEnabledItemCount();
        int headerStartPosition = 0;
        int headerEndPosition = headerItemCount - 1;
        if (position >= headerStartPosition && position <= headerEndPosition && headerItemCount > 0) {
            //noinspection UnnecessaryLocalVariable
            int positionInHeaderList = position;
            return headerItemManager.getItemInEnabledList(positionInHeaderList).getItemFactory();
        }

        // body
        int dataCount = getDataCount();
        int dataStartPosition = headerEndPosition + 1;
        int dataEndPosition = headerEndPosition + dataCount;
        if (position >= dataStartPosition && position <= dataEndPosition && dataCount > 0) {
            int positionInDataList = position - headerItemCount;
            Object dataObject = getData(positionInDataList);

            AssemblyPagerItemFactory itemFactory;
            for (int w = 0, size = itemFactoryList.size(); w < size; w++) {
                itemFactory = itemFactoryList.get(w);
                if (itemFactory.match(dataObject)) {
                    return itemFactory;
                }
            }

            throw new IllegalStateException(String.format("Didn't find suitable AssemblyPagerItemFactory. position=%d, dataObject=%s",
                    position, dataObject != null ? dataObject.getClass().getName() : null));
        }

        // footer
        int footerItemCount = footerItemManager.getEnabledItemCount();
        int footerStartPosition = dataEndPosition + 1;
        int footerEndPosition = dataEndPosition + footerItemCount;
        if (position >= footerStartPosition && position <= footerEndPosition && footerItemCount > 0) {
            int positionInFooterList = position - headerItemCount - dataCount;
            return footerItemManager.getItemInEnabledList(positionInFooterList).getItemFactory();
        }

        throw new IllegalStateException("Not found PagerItemFactory by position: " + position);
    }

    @Nullable
    public Object getItemDataByPosition(int position) {
        // header
        int headerItemCount = headerItemManager.getEnabledItemCount();
        int headerStartPosition = 0;
        int headerEndPosition = headerItemCount - 1;
        if (position >= headerStartPosition && position <= headerEndPosition && headerItemCount > 0) {
            //noinspection UnnecessaryLocalVariable
            int positionInHeaderList = position;
            return headerItemManager.getItemInEnabledList(positionInHeaderList).getData();
        }

        // body
        int dataCount = getDataCount();
        int dataStartPosition = headerEndPosition + 1;
        int dataEndPosition = headerEndPosition + dataCount;
        if (position >= dataStartPosition && position <= dataEndPosition && dataCount > 0) {
            int positionInDataList = position - headerItemCount;
            return getData(positionInDataList);
        }

        // footer
        int footerItemCount = footerItemManager.getEnabledItemCount();
        int footerStartPosition = dataEndPosition + 1;
        int footerEndPosition = dataEndPosition + footerItemCount;
        if (position >= footerStartPosition && position <= footerEndPosition && footerItemCount > 0) {
            int positionInFooterList = position - headerItemCount - dataCount;
            return footerItemManager.getItemInEnabledList(positionInFooterList).getData();
        }

        throw new IllegalArgumentException("Not found item data by position: " + position);
    }

    public int getPositionInPart(int position) {
        // header
        int headerItemCount = headerItemManager.getEnabledItemCount();
        int headerStartPosition = 0;
        int headerEndPosition = headerItemCount - 1;
        if (position >= headerStartPosition && position <= headerEndPosition && headerItemCount > 0) {
            return position;
        }

        // body
        int dataCount = getDataCount();
        int dataStartPosition = headerEndPosition + 1;
        int dataEndPosition = headerEndPosition + dataCount;
        if (position >= dataStartPosition && position <= dataEndPosition && dataCount > 0) {
            return position - headerItemCount;
        }

        // footer
        int footerItemCount = footerItemManager.getEnabledItemCount();
        int footerStartPosition = dataEndPosition + 1;
        int footerEndPosition = dataEndPosition + footerItemCount;
        if (position >= footerStartPosition && position <= footerEndPosition && footerItemCount > 0) {
            return position - headerItemCount - dataCount;
        }

        throw new IllegalArgumentException("Illegal position: " + position);
    }

    public boolean isHeaderItem(int position) {
        int headerItemCount = headerItemManager.getEnabledItemCount();
        int headerStartPosition = 0;
        int headerEndPosition = headerItemCount - 1;
        return position >= headerStartPosition && position <= headerEndPosition && headerItemCount > 0;
    }

    public boolean isBodyItem(int position) {
        int headerItemCount = headerItemManager.getEnabledItemCount();
        int headerEndPosition = headerItemCount - 1;
        int dataCount = getDataCount();
        int dataStartPosition = headerEndPosition + 1;
        int dataEndPosition = headerEndPosition + dataCount;
        return position >= dataStartPosition && position <= dataEndPosition && dataCount > 0;
    }

    public boolean isFooterItem(int position) {
        int headerItemCount = headerItemManager.getEnabledItemCount();
        int headerEndPosition = headerItemCount - 1;
        int dataCount = getDataCount();
        int dataEndPosition = headerEndPosition + dataCount;
        int footerItemCount = footerItemManager.getEnabledItemCount();
        int footerStartPosition = dataEndPosition + 1;
        int footerEndPosition = dataEndPosition + footerItemCount;
        return position >= footerStartPosition && position <= footerEndPosition && footerItemCount > 0;
    }
}
