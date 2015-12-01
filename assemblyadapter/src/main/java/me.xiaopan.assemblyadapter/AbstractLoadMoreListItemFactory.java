package me.xiaopan.assemblyadapter;

import android.content.Context;
import android.view.View;

public abstract class AbstractLoadMoreListItemFactory extends AssemblyItemFactory<AbstractLoadMoreListItemFactory.AbstractLoadMoreListItem> {
    boolean loadMoreRunning;
    boolean end;
    private OnLoadMoreListener eventListener;

    public AbstractLoadMoreListItemFactory(OnLoadMoreListener eventListener) {
        this.eventListener = eventListener;
    }

    @Override
    public boolean isTarget(Object itemObject) {
        return false;
    }

    public abstract static class AbstractLoadMoreListItem extends AssemblyItem<String, AbstractLoadMoreListItemFactory>{
        protected AbstractLoadMoreListItem(View convertView, AbstractLoadMoreListItemFactory baseFactory) {
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
                        setData(getPosition(), getData());
                    }
                }
            });
        }

        @Override
        public void onSetData(int position, String s) {
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
