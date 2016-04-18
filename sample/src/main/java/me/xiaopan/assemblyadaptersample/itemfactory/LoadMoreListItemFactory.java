package me.xiaopan.assemblyadaptersample.itemfactory;

import android.view.View;
import android.view.ViewGroup;

import me.xiaopan.assemblyadapter.AbstractLoadMoreListItemFactory;
import me.xiaopan.assemblyadapter.OnLoadMoreListener;
import me.xiaopan.assemblyadaptersample.R;

public class LoadMoreListItemFactory extends AbstractLoadMoreListItemFactory {

    public LoadMoreListItemFactory(OnLoadMoreListener eventListener) {
        super(eventListener);
    }

    @Override
    public AbstractLoadMoreListItem createAssemblyItem(ViewGroup parent) {
        return new LoadMoreListItem(inflateView(R.layout.list_item_load_more, parent), this);
    }

    public static class LoadMoreListItem extends AbstractLoadMoreListItem {
        private View loadingView;
        private View errorView;
        private View endView;

        protected LoadMoreListItem(View convertView, AbstractLoadMoreListItemFactory baseFactory) {
            super(convertView, baseFactory);
        }

        @Override
        protected void onFindViews(View convertView) {
            loadingView = convertView.findViewById(R.id.text_loadMoreListItem_loading);
            errorView = convertView.findViewById(R.id.text_loadMoreListItem_error);
            endView = convertView.findViewById(R.id.text_loadMoreListItem_end);
        }

        @Override
        public View getErrorRetryView() {
            return errorView;
        }

        @Override
        public void showLoading() {
            loadingView.setVisibility(View.VISIBLE);
            errorView.setVisibility(View.INVISIBLE);
            endView.setVisibility(View.INVISIBLE);
        }

        @Override
        public void showErrorRetry() {
            loadingView.setVisibility(View.INVISIBLE);
            errorView.setVisibility(View.VISIBLE);
            endView.setVisibility(View.INVISIBLE);
        }

        @Override
        public void showEnd() {
            loadingView.setVisibility(View.INVISIBLE);
            errorView.setVisibility(View.INVISIBLE);
            endView.setVisibility(View.VISIBLE);
        }
    }
}
