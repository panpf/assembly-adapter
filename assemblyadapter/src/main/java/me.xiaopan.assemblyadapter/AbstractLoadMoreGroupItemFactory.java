package me.xiaopan.assemblyadapter;

import android.content.Context;
import android.view.View;

public abstract class AbstractLoadMoreGroupItemFactory extends AssemblyGroupItemFactory<AbstractLoadMoreGroupItemFactory.AbstractLoadMoreGroupItem> {
    boolean loadMoreRunning;
    boolean end;
    private OnGroupLoadMoreListener eventListener;

    public AbstractLoadMoreGroupItemFactory(OnGroupLoadMoreListener eventListener) {
        this.eventListener = eventListener;
    }

    @Override
    public boolean isTarget(Object itemObject) {
        return false;
    }

    public abstract static class AbstractLoadMoreGroupItem extends AssemblyGroupItem<String, AbstractLoadMoreGroupItemFactory>{
        protected AbstractLoadMoreGroupItem(View convertView, AbstractLoadMoreGroupItemFactory baseFactory) {
            super(convertView, baseFactory);
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
                    if (getItemFactory().eventListener != null) {
                        getItemFactory().loadMoreRunning = false;
                        setData(groupPosition, isExpanded, data);
                    }
                }
            });
        }

        @Override
        public void onSetData(int groupPosition, boolean isExpanded, String s) {
            if(itemFactory.end){
                showEnd();
            }else{
                showLoading();
                if (itemFactory.eventListener != null && !itemFactory.loadMoreRunning) {
                    itemFactory.loadMoreRunning = true;
                    itemFactory.eventListener.onLoadMore(itemFactory.adapter);
                }
            }
        }
    }
}
