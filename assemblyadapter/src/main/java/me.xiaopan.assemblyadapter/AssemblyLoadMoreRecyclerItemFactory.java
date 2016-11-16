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

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

/**
 * AssemblyRecyclerAdapter专用的加载更多ItemFactory
 */
public abstract class AssemblyLoadMoreRecyclerItemFactory extends AssemblyRecyclerItemFactory<AssemblyLoadMoreRecyclerItemFactory.AssemblyLoadMoreRecyclerItem> {
    private boolean loadMoreRunning;
    private boolean end;
    private OnRecyclerLoadMoreListener eventListener;

    public AssemblyLoadMoreRecyclerItemFactory(OnRecyclerLoadMoreListener eventListener) {
        this.eventListener = eventListener;
    }

    public void setEnd(boolean end) {
        this.end = end;
    }

    void setLoadMoreRunning(boolean loadMoreRunning) {
        this.loadMoreRunning = loadMoreRunning;
    }

    @Override
    public boolean isTarget(Object data) {
        return false;
    }

    public abstract class AssemblyLoadMoreRecyclerItem extends AssemblyRecyclerItem<String> {
        public AssemblyLoadMoreRecyclerItem(int itemLayoutId, ViewGroup parent) {
            super(itemLayoutId, parent);
            fullSpanInStaggeredGrid();
        }

        @SuppressWarnings("unused")
        public AssemblyLoadMoreRecyclerItem(View convertView) {
            super(convertView);
            fullSpanInStaggeredGrid();
        }

        /**
         * 获取错误重试View，用户实现点击重试功能
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
                            loadMoreRunning = false;
                            setData(getLayoutPosition(), getData());
                        }
                    }
                });
            }
        }

        @Override
        public void onSetData(int position, String s) {
            if (end) {
                showEnd();
            } else {
                showLoading();
                if (eventListener != null && !loadMoreRunning) {
                    loadMoreRunning = true;
                    eventListener.onLoadMore(getAdapter());
                }
            }
        }
    }
}
