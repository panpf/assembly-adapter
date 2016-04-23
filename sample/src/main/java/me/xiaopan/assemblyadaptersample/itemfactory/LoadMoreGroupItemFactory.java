package me.xiaopan.assemblyadaptersample.itemfactory;

import android.view.View;
import android.view.ViewGroup;

import me.xiaopan.assemblyadapter.AbstractLoadMoreGroupItemFactory;
import me.xiaopan.assemblyadapter.OnGroupLoadMoreListener;
import me.xiaopan.assemblyadaptersample.R;

public class LoadMoreGroupItemFactory extends AbstractLoadMoreGroupItemFactory {

    public LoadMoreGroupItemFactory(OnGroupLoadMoreListener eventListener) {
        super(eventListener);
    }

    @Override
    public AbstractLoadMoreGroupItem createAssemblyItem(ViewGroup parent) {
        return new LoadMoreGroupItem(R.layout.list_item_load_more, parent);
    }

    public class LoadMoreGroupItem extends AbstractLoadMoreGroupItem {
        private View loadingView;
        private View errorView;
        private View endView;

        public LoadMoreGroupItem(int itemLayoutId, ViewGroup parent) {
            super(itemLayoutId, parent);
        }

        @Override
        protected void onFindViews(View itemView) {
            loadingView = findViewById(R.id.text_loadMoreListItem_loading);
            errorView = findViewById(R.id.text_loadMoreListItem_error);
            endView = findViewById(R.id.text_loadMoreListItem_end);
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
