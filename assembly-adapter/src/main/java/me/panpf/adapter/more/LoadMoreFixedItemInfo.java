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

package me.panpf.adapter.more;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import me.panpf.adapter.FixedItemInfo;
import me.panpf.adapter.ItemStorage;

/**
 * 加载更多专用的固定尾巴
 */
@SuppressWarnings("unused")
public class LoadMoreFixedItemInfo extends FixedItemInfo {

    public LoadMoreFixedItemInfo(@NonNull ItemStorage storage, @NonNull LoadMoreItemFactoryBridle itemFactory, @Nullable Object data, boolean header) {
        super(storage, itemFactory, data, header);
    }

    @NonNull
    @Override
    public LoadMoreItemFactoryBridle getItemFactory() {
        return (LoadMoreItemFactoryBridle) super.getItemFactory();
    }

    @Override
    protected void enableChanged() {
//        super.enableChanged();
        // no refresh
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        if (enabled) {
            loadMoreFinished(false);
        }
    }

    /**
     * 加载更多完成
     *
     * @param end 已全部加载完毕，切换至结束状态
     */
    public void loadMoreFinished(boolean end) {
        getItemFactory().loadMoreFinished(end);
    }

    /**
     * 加载更多失败
     */
    public void loadMoreFailed() {
        getItemFactory().loadMoreFailed();
    }
}
