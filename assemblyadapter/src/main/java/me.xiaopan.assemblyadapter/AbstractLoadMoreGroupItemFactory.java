package me.xiaopan.assemblyadapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

public abstract class AbstractLoadMoreGroupItemFactory extends AssemblyGroupItemFactory<AbstractLoadMoreGroupItemFactory.AbstractLoadMoreGroupItem> {
    private boolean loadMoreRunning;
    private boolean end;
    private OnGroupLoadMoreListener eventListener;

    public AbstractLoadMoreGroupItemFactory(OnGroupLoadMoreListener eventListener) {
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

    public abstract class AbstractLoadMoreGroupItem extends AssemblyGroupItem<String>{
        public AbstractLoadMoreGroupItem(int itemLayoutId, ViewGroup parent) {
            super(itemLayoutId, parent);
        }

        public AbstractLoadMoreGroupItem(View convertView) {
            super(convertView);
        }

        public abstract View getErrorRetryView();

        public abstract void showLoading();

        public abstract void showErrorRetry();

        public abstract void showEnd();

        @Override
        public void onConfigViews(Context context) {
            getErrorRetryView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (eventListener != null) {
                        loadMoreRunning = false;
                        setData(getGroupPosition(), isExpanded(), getData());
                    }
                }
            });
        }

        @Override
        public void onSetData(int groupPosition, boolean isExpanded, String s) {
            if(end){
                showEnd();
            }else{
                showLoading();
                if (eventListener != null && !loadMoreRunning) {
                    loadMoreRunning = true;
                    eventListener.onLoadMore(adapter);
                }
            }
        }
    }
}
