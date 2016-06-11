package me.xiaopan.assemblyadaptersample.itemfactory;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.TextView;

import me.xiaopan.assemblyadapter.AssemblyItem;
import me.xiaopan.assemblyadapter.AssemblyItemFactory;
import me.xiaopan.assemblyadaptersample.R;

public class LikeFooterItemFactory extends AssemblyItemFactory<LikeFooterItemFactory.LikeFooterItem>{
    @Override
    public boolean isTarget(Object itemObject) {
        return itemObject instanceof String;
    }

    @Override
    public LikeFooterItem createAssemblyItem(ViewGroup parent) {
        return new LikeFooterItem(R.layout.list_item_like, parent);
    }

    public class LikeFooterItem extends AssemblyItem<String>{
        private TextView textView;

        public LikeFooterItem(int itemLayoutId, ViewGroup parent) {
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
