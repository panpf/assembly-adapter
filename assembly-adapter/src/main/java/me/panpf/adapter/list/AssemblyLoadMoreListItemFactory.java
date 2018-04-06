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

package me.panpf.adapter.list;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

/**
 * {@link AssemblyListAdapter} 专用的加载更多 ItemFactory
 */
public abstract class AssemblyLoadMoreListItemFactory extends AssemblyListItemFactory<AssemblyLoadMoreListItemFactory.AssemblyLoadMoreListItem> {
    private boolean paused;
    private boolean end;
    private OnListLoadMoreListener eventListener;
    private AssemblyLoadMoreListItem loadMoreItem;

    public AssemblyLoadMoreListItemFactory(OnListLoadMoreListener eventListener) {
        this.eventListener = eventListener;
    }

    /**
     * 加载更多完成
     * @param end 已全部加载完毕，切换至结束状态
     */
    @SuppressWarnings("WeakerAccess")
    public void loadMoreFinished(boolean end){
        this.paused = false;
        this.end = end;

        if (loadMoreItem != null) {
            if (end) {
                loadMoreItem.showEnd();
            } else {
                loadMoreItem.showLoading();
            }
        }
    }

    /**
     * 加载更多失败
     */
    @SuppressWarnings("WeakerAccess")
    public void loadMoreFailed(){
        paused = false;
        if (loadMoreItem != null) {
            loadMoreItem.showErrorRetry();
        }
    }

    @Override
    public boolean isTarget(Object data) {
        return true;
    }

    public abstract class AssemblyLoadMoreListItem<T> extends AssemblyListItem<T> {
        public AssemblyLoadMoreListItem(int itemLayoutId, ViewGroup parent) {
            super(itemLayoutId, parent);
            loadMoreItem = this;
        }

        @SuppressWarnings("unused")
        public AssemblyLoadMoreListItem(View convertView) {
            super(convertView);
            loadMoreItem = this;
        }

        /**
         * 获取错误重试View，实现点击重试功能
         */
        public abstract View getErrorRetryView();

        /**
         * 显示加载中状态
         */
        public abstract void showLoading();

        /**
         * 显示错误重试状态
         */
        public abstract void showErrorRetry();

        /**
         * 显示全部加载完毕已结束状态
         */
        public abstract void showEnd();

        @Override
        public void onConfigViews(Context context) {
            View errorView = getErrorRetryView();
            if (errorView != null) {
                errorView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (eventListener != null) {
                            paused = false;
                            setData(getPosition(), getData());
                        }
                    }
                });
            }
        }

        @Override
        public void onSetData(int position, T t) {
            if (end) {
                showEnd();
            } else {
                showLoading();
                if (eventListener != null && !paused) {
                    paused = true;
                    eventListener.onLoadMore(getAdapter());
                }
            }
        }
    }
}
