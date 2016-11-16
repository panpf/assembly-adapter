package me.xiaopan.assemblyadapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

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

    public void setLoadMoreRunning(boolean loadMoreRunning) {
        this.loadMoreRunning = loadMoreRunning;
    }

    @Override
    public boolean isTarget(Object itemObject) {
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

        public abstract View getErrorRetryView();

        public abstract void showLoading();

        public abstract void showErrorRetry();

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
