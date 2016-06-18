package me.xiaopan.assemblyadaptersample.itemfactory;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.TextView;

import me.xiaopan.assemblyadapter.AssemblyGroupItem;
import me.xiaopan.assemblyadapter.AssemblyGroupItemFactory;
import me.xiaopan.assemblyadaptersample.R;

public class LikeFooterGroupItemFactory extends AssemblyGroupItemFactory<LikeFooterGroupItemFactory.LikeFooterGroupItem> {
    @Override
    public boolean isTarget(Object itemObject) {
        return itemObject instanceof String;
    }

    @Override
    public LikeFooterGroupItem createAssemblyItem(ViewGroup parent) {
        return new LikeFooterGroupItem(R.layout.list_item_like, parent);
    }

    public class LikeFooterGroupItem extends AssemblyGroupItem<String> {
        private TextView textView;

        public LikeFooterGroupItem(int itemLayoutId, ViewGroup parent) {
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
