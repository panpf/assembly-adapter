package me.xiaopan.assemblyadaptersample.itemfactory;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import me.xiaopan.assemblyadapter.AbstractLoadMoreListItemFactory;
import me.xiaopan.assemblyadaptersample.R;

public class LoadMoreListItemFactory extends AbstractLoadMoreListItemFactory {

    public LoadMoreListItemFactory(EventListener eventListener) {
        super(eventListener);
    }

    @Override
    public AbstractLoadMoreListItem createAssemblyItem(ViewGroup parent) {
        return new LoadMoreListItem(parent, this);
    }

    public static class LoadMoreListItem extends AbstractLoadMoreListItem {
        private View loadingView;
        private View errorView;

        protected LoadMoreListItem(ViewGroup parent, AbstractLoadMoreListItemFactory baseFactory) {
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
