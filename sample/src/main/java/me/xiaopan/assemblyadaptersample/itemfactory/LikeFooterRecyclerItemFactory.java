package me.xiaopan.assemblyadaptersample.itemfactory;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.TextView;

import me.xiaopan.assemblyadapter.AssemblyRecyclerItem;
import me.xiaopan.assemblyadapter.AssemblyRecyclerItemFactory;
import me.xiaopan.assemblyadaptersample.R;

public class LikeFooterRecyclerItemFactory extends AssemblyRecyclerItemFactory<LikeFooterRecyclerItemFactory.LikeFooterRecyclerItem> {
    @Override
    public boolean isTarget(Object itemObject) {
        return itemObject instanceof String;
    }

    @Override
    public LikeFooterRecyclerItem createAssemblyItem(ViewGroup parent) {
        return new LikeFooterRecyclerItem(R.layout.list_item_like, parent);
    }

    public class LikeFooterRecyclerItem extends AssemblyRecyclerItem<String> {
        private TextView textView;

        public LikeFooterRecyclerItem(int itemLayoutId, ViewGroup parent) {
            super(itemLayoutId, parent);
        }

        @Override
        protected void onFindViews() {
            textView = (TextView) getItemView();
        }

        @Override
        protected void onConfigViews(Context context) {

        }

        @Override
        protected void onSetData(int position, String s) {
            textView.setText(s);
        }
    }
}
