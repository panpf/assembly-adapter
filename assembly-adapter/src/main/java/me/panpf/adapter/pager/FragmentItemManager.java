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
import androidx.viewpager.widget.PagerAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FragmentItemManager {

    @NonNull
    private PagerAdapter adapter;

    @NonNull
    private FragmentFixedItemManager headerItemManager = new FragmentFixedItemManager();
    @NonNull
    private ArrayList<AssemblyFragmentItemFactory> itemFactoryList = new ArrayList<>();
    @NonNull
    private FragmentFixedItemManager footerItemManager = new FragmentFixedItemManager();

    @Nullable
    private List dataList;

    public FragmentItemManager(@NonNull PagerAdapter adapter) {
        this.adapter = adapter;
    }

    public FragmentItemManager(@NonNull PagerAdapter adapter, @NonNull List dataList) {
        this.adapter = adapter;
        this.dataList = dataList;
    }

    public FragmentItemManager(@NonNull PagerAdapter adapter, @Nullable Object[] dataArray) {
        this.adapter = adapter;
        if (dataArray != null && dataArray.length > 0) {
            this.dataList = new ArrayList(dataArray.length);
            Collections.addAll(dataList, dataArray);
        }
    }


    public void addItemFactory(@NonNull AssemblyFragmentItemFactory itemFactory) {
        //noinspection ConstantConditions
        if (itemFactory == null) {
            throw new IllegalArgumentException("itemFactory is null or item factory list locked");
        }

        itemFactoryList.add(itemFactory);

        itemFactory.attachToAdapter(adapter);
    }


    public <DATA> void addHeaderItem(@NonNull AssemblyFragmentItemFactory<DATA> itemFactory, @Nullable Object data) {
        //noinspection ConstantConditions
        if (itemFactory == null) {
            throw new IllegalArgumentException("itemFactory is null or item factory list locked");
        }

        headerItemManager.add(new FragmentFixedItem<>(itemFactory, data));

        itemFactory.attachToAdapter(adapter);
    }

    public void addHeaderItem(@NonNull AssemblyFragmentItemFactory itemFactory) {
        addHeaderItem(itemFactory, null);
    }

    @NonNull
    public FragmentFixedItemManager getHeaderItemManager() {
        return headerItemManager;
    }


    public <DATA> void addFooterItem(@NonNull AssemblyFragmentItemFactory<DATA> itemFactory, @Nullable Object data) {
        //noinspection ConstantConditions
        if (itemFactory == null) {
            throw new IllegalArgumentException("itemFactory is null or item factory list locked");
        }

        footerItemManager.add(new FragmentFixedItem<>(itemFactory, data));

        itemFactory.attachToAdapter(adapter);
    }

    public void addFooterItem(@NonNull AssemblyFragmentItemFactory itemFactory) {
        addFooterItem(itemFactory, null);
    }

    @NonNull
    public FragmentFixedItemManager getFooterItemManager() {
        return footerItemManager;
    }


    @Nullable
    public List getDataList() {
        return dataList;
    }

    public void setDataList(@Nullable List dataList) {
        synchronized (this) {
            this.dataList = dataList;
        }

        adapter.notifyDataSetChanged();
    }

    public int getDataCount() {
        return dataList != null ? dataList.size() : 0;
    }

    @Nullable
    public Object getData(int positionInDataList) {
        return dataList != null ? dataList.get(positionInDataList) : null;
    }

    public int getItemCount() {
        return headerItemManager.getItemCount() + getDataCount() + footerItemManager.getItemCount();
    }

    @NonNull
    public AssemblyFragmentItemFactory getItemFactoryByPosition(int position) {
        // header
        int headerItemCount = headerItemManager.getItemCount();
        int headerStartPosition = 0;
        int headerEndPosition = headerItemCount - 1;
        if (position >= headerStartPosition && position <= headerEndPosition && headerItemCount > 0) {
            //noinspection UnnecessaryLocalVariable
            int positionInHeaderList = position;
            return headerItemManager.getItem(positionInHeaderList).getItemFactory();
        }

        // body
        int dataCount = getDataCount();
        int dataStartPosition = headerEndPosition + 1;
        int dataEndPosition = headerEndPosition + dataCount;
        if (position >= dataStartPosition && position <= dataEndPosition && dataCount > 0) {
            int positionInDataList = position - headerItemCount;
            Object dataObject = getData(positionInDataList);

            AssemblyFragmentItemFactory itemFactory;
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
        int footerItemCount = footerItemManager.getItemCount();
        int footerStartPosition = dataEndPosition + 1;
        int footerEndPosition = dataEndPosition + footerItemCount;
        if (position >= footerStartPosition && position <= footerEndPosition && footerItemCount > 0) {
            int positionInFooterList = position - headerItemCount - dataCount;
            return footerItemManager.getItem(positionInFooterList).getItemFactory();
        }

        throw new IllegalStateException("Not found PagerItemFactory by position: " + position);
    }

    @Nullable
    public Object getItemDataByPosition(int position) {
        // header
        int headerItemCount = headerItemManager.getItemCount();
        int headerStartPosition = 0;
        int headerEndPosition = headerItemCount - 1;
        if (position >= headerStartPosition && position <= headerEndPosition && headerItemCount > 0) {
            //noinspection UnnecessaryLocalVariable
            int positionInHeaderList = position;
            return headerItemManager.getItem(positionInHeaderList).getData();
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
        int footerItemCount = footerItemManager.getItemCount();
        int footerStartPosition = dataEndPosition + 1;
        int footerEndPosition = dataEndPosition + footerItemCount;
        if (position >= footerStartPosition && position <= footerEndPosition && footerItemCount > 0) {
            int positionInFooterList = position - headerItemCount - dataCount;
            return footerItemManager.getItem(positionInFooterList).getData();
        }

        throw new IllegalArgumentException("Not found item data by position: " + position);
    }

    public int getPositionInPart(int position) {
        // header
        int headerItemCount = headerItemManager.getItemCount();
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
        int footerItemCount = footerItemManager.getItemCount();
        int footerStartPosition = dataEndPosition + 1;
        int footerEndPosition = dataEndPosition + footerItemCount;
        if (position >= footerStartPosition && position <= footerEndPosition && footerItemCount > 0) {
            return position - headerItemCount - dataCount;
        }

        throw new IllegalArgumentException("Illegal position: " + position);
    }

    public boolean isHeaderItem(int position) {
        int headerItemCount = headerItemManager.getItemCount();
        int headerStartPosition = 0;
        int headerEndPosition = headerItemCount - 1;
        return position >= headerStartPosition && position <= headerEndPosition && headerItemCount > 0;
    }

    public boolean isBodyItem(int position) {
        int headerItemCount = headerItemManager.getItemCount();
        int headerEndPosition = headerItemCount - 1;
        int dataCount = getDataCount();
        int dataStartPosition = headerEndPosition + 1;
        int dataEndPosition = headerEndPosition + dataCount;
        return position >= dataStartPosition && position <= dataEndPosition && dataCount > 0;
    }

    public boolean isFooterItem(int position) {
        int headerItemCount = headerItemManager.getItemCount();
        int headerEndPosition = headerItemCount - 1;
        int dataCount = getDataCount();
        int dataEndPosition = headerEndPosition + dataCount;
        int footerItemCount = footerItemManager.getItemCount();
        int footerStartPosition = dataEndPosition + 1;
        int footerEndPosition = dataEndPosition + footerItemCount;
        return position >= footerStartPosition && position <= footerEndPosition && footerItemCount > 0;
    }
}
