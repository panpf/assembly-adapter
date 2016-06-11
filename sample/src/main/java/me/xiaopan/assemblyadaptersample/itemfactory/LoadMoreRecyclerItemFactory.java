package me.xiaopan.assemblyadaptersample.itemfactory;

import android.view.View;
import android.view.ViewGroup;

import me.xiaopan.assemblyadapter.AssemblyLoadMoreRecyclerItemFactory;
import me.xiaopan.assemblyadapter.OnRecyclerLoadMoreListener;
import me.xiaopan.assemblyadaptersample.R;

public class LoadMoreRecyclerItemFactory extends AssemblyLoadMoreRecyclerItemFactory {

    public LoadMoreRecyclerItemFactory(OnRecyclerLoadMoreListener eventListener) {
        super(eventListener);
    }

    @Override
    public AssemblyLoadMoreRecyclerItem createAssemblyItem(ViewGroup parent) {
        return new LoadMoreRecyclerItem(R.layout.list_item_load_more, parent);
    }

    public class LoadMoreRecyclerItem extends AssemblyLoadMoreRecyclerItem {
        private View loadingView;
        private View errorView;
        private View endView;

        public LoadMoreRecyclerItem(int itemLayoutId, ViewGroup parent) {
            super(itemLayoutId, parent);
        }

        @Override
        protected void onFindViews() {
            loadingView = itemView.findViewById(R.id.text_loadMoreListItem_loading);
            errorView = itemView.findViewById(R.id.text_loadMoreListItem_error);
            endView = itemView.findViewById(R.id.text_loadMoreListItem_end);
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
