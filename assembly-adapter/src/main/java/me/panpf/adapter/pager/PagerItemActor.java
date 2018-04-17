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
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

@SuppressWarnings({"unused", "WeakerAccess"})
public class PagerItemActor {

    @NonNull
    private AssemblyPagerAdapter adapter;

    public PagerItemActor(@NonNull AssemblyPagerAdapter adapter) {
        this.adapter = adapter;
    }


    /* ************************ 完整列表 *************************** */

    public int getItemCount() {
        return adapter.getHeaderItemCount() + adapter.getDataCount() + adapter.getFooterItemCount();
    }

    @NonNull
    public View getItem(@NonNull ViewGroup container, int position) {
        // 头
        int headerItemCount = adapter.getHeaderItemCount();
        int headerStartPosition = 0;
        int headerEndPosition = headerItemCount - 1;
        List<FixedPagerItemInfo> headerItemList = adapter.getHeaderItemList();
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
        int dataCount = adapter.getDataCount();
        int dataStartPosition = headerEndPosition + 1;
        int dataEndPosition = headerEndPosition + dataCount;
        List<AssemblyPagerItemFactory> itemFactoryList = adapter.getItemFactoryList();
        if (itemFactoryList != null && position >= dataStartPosition && position <= dataEndPosition && dataCount > 0) {
            int positionInDataList = position - headerItemCount;
            Object dataObject = adapter.getData(positionInDataList);
            if (dataObject == null) {
                throw new IllegalArgumentException("data is null, position is " + position + ", positionInDataList is " + positionInDataList);
            }

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
                    position, dataObject.getClass().getName()));
        }

        // 尾巴
        int footerItemCount = adapter.getFooterItemCount();
        int footerStartPosition = dataEndPosition + 1;
        int footerEndPosition = dataEndPosition + footerItemCount;
        List<FixedPagerItemInfo> footerItemList = adapter.getFooterItemList();
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

    /**
     * 获取在各自区域的位置
     */
    public int getPositionInPart(int position) {
        // 头
        int headerItemCount = adapter.getHeaderItemCount();
        int headerStartPosition = 0;
        int headerEndPosition = headerItemCount - 1;
        if (position >= headerStartPosition && position <= headerEndPosition && headerItemCount > 0) {
            return position;
        }

        // 数据
        int dataCount = adapter.getDataCount();
        int dataStartPosition = headerEndPosition + 1;
        int dataEndPosition = headerEndPosition + dataCount;
        if (position >= dataStartPosition && position <= dataEndPosition && dataCount > 0) {
            return position - headerItemCount;
        }

        // 尾巴
        int footerItemCount = adapter.getFooterItemCount();
        int footerStartPosition = dataEndPosition + 1;
        int footerEndPosition = dataEndPosition + footerItemCount;
        if (position >= footerStartPosition && position <= footerEndPosition && footerItemCount > 0) {
            return position - headerItemCount - dataCount;
        }

        throw new IllegalArgumentException("illegal position: " + position);
    }
}
