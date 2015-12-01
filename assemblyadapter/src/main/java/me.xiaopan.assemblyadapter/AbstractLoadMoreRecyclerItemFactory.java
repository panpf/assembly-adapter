package me.xiaopan.assemblyadapter;

import android.content.Context;
import android.view.View;

public abstract class AbstractLoadMoreRecyclerItemFactory extends AssemblyRecyclerItemFactory<AbstractLoadMoreRecyclerItemFactory.AbstractLoadMoreRecyclerItem> {
    private AdapterCallback adapterCallback;
    public boolean loadMoreRunning;
    private EventListener eventListener;

    public AbstractLoadMoreRecyclerItemFactory(EventListener eventListener) {
        this.eventListener = eventListener;
    }

    public void setAdapterCallback(AdapterCallback adapterCallback) {
        this.adapterCallback = adapterCallback;
    }

    @Override
    public boolean isTarget(Object itemObject) {
        return false;
    }

    public interface EventListener {
		void onLoadMore(AdapterCallback adapterCallback);
	}

    public interface AdapterCallback{
        void loading();
        void loadMoreFinished();
        void loadMoreFailed();
	}

    public abstract static class AbstractLoadMoreRecyclerItem extends AssemblyRecyclerItem<String, AbstractLoadMoreRecyclerItemFactory> {
        protected AbstractLoadMoreRecyclerItem(View convertView, AbstractLoadMoreRecyclerItemFactory baseFactory) {
            super(convertView, baseFactory);
        }

        public abstract void showErrorRetry();

        public abstract void showLoading();

        public abstract View getErrorRetryView();

        @Override
        public void onConfigViews(Context context) {
            getErrorRetryView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (getItemFactory().eventListener != null) {
                        getItemFactory().loadMoreRunning = false;
                        setData(getLayoutPosition(), getData());
                    }
                }
            });
        }

        @Override
        public void onSetData(int position, String s) {
            showLoading();
            if (getItemFactory().eventListener != null && !getItemFactory().loadMoreRunning) {
                getItemFactory().adapterCallback.loading();
                getItemFactory().eventListener.onLoadMore(getItemFactory().adapterCallback);
            }
        }
    }
}
