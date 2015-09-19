package me.xiaopan.assemblyexpandableadapter;

import android.content.Context;
import android.view.View;

public abstract class AbstractLoadMoreGroupItemFactory extends AssemblyGroupItemFactory<AbstractLoadMoreGroupItemFactory.AbstractLoadMoreGroupItem> {
    private AdapterCallback adapterCallback;
    public boolean loadMoreRunning;
    private EventListener eventListener;

    public AbstractLoadMoreGroupItemFactory(EventListener eventListener) {
        this.eventListener = eventListener;
    }

    public void setAdapterCallback(AdapterCallback adapterCallback) {
        this.adapterCallback = adapterCallback;
    }

    @Override
    public Class<?> getBeanClass() {
        return String.class;
    }

    public interface EventListener {
		void onLoadMore(AdapterCallback adapterCallback);
	}

    public interface AdapterCallback{
        void loading();
        void loadMoreFinished();
        void loadMoreFailed();
	}

    public abstract static class AbstractLoadMoreGroupItem extends AssemblyGroupItem<String, AbstractLoadMoreGroupItemFactory>{
        protected AbstractLoadMoreGroupItem(View convertView, AbstractLoadMoreGroupItemFactory baseFactory) {
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
                        setData(getGroupPosition(), isExpanded(), getData());
                    }
                }
            });
        }

        @Override
        public void onSetData(int groupPosition, boolean isExpanded, String s) {
            showLoading();
            if (getItemFactory().eventListener != null && !getItemFactory().loadMoreRunning) {
                getItemFactory().adapterCallback.loading();
                getItemFactory().eventListener.onLoadMore(getItemFactory().adapterCallback);
            }
        }
    }
}
