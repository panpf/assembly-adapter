package me.xiaopan.assemblyadaptersample.itemfactory;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import me.xiaopan.assemblyadaptersample.R;
import me.xiaopan.assemblyadapter.AbstractLoadMoreRecyclerItemFactory;

public class LoadMoreRecyclerItemFactory extends AbstractLoadMoreRecyclerItemFactory {

    public LoadMoreRecyclerItemFactory(EventListener eventListener) {
        super(eventListener);
    }

    @Override
    public AbstractLoadMoreRecyclerItem createAssemblyItem(ViewGroup parent) {
        return new LoadMoreRecyclerItem(parent, this);
    }

    public static class LoadMoreRecyclerItem extends AbstractLoadMoreRecyclerItem {
        private View loadingView;
        private View errorView;

        protected LoadMoreRecyclerItem(ViewGroup parent, AbstractLoadMoreRecyclerItemFactory baseFactory) {
            super(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_load_more, parent, false), baseFactory);
        }

        @Override
        protected void onFindViews(View convertView) {
            loadingView = convertView.findViewById(R.id.text_loadMoreListItem_loading);
            errorView = convertView.findViewById(R.id.text_loadMoreListItem_error);
        }

        @Override
        public void showErrorRetry() {
            loadingView.setVisibility(View.GONE);
            errorView.setVisibility(View.VISIBLE);
        }

        @Override
        public void showLoading() {
            loadingView.setVisibility(View.VISIBLE);
            errorView.setVisibility(View.GONE);
        }

        @Override
        public View getErrorRetryView() {
            return errorView;
        }
    }
}
