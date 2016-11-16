package me.xiaopan.assemblyadaptersample.itemfactory;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.TextView;

import me.xiaopan.assemblyadapter.AssemblyRecyclerItem;
import me.xiaopan.assemblyadapter.AssemblyRecyclerItemFactory;
import me.xiaopan.assemblyadaptersample.R;

public class HeaderRecyclerItemFactory extends AssemblyRecyclerItemFactory<HeaderRecyclerItemFactory.HeaderRecyclerItem> {
    @Override
    public boolean isTarget(Object data) {
        return data instanceof String;
    }

    @Override
    public HeaderRecyclerItem createAssemblyItem(ViewGroup parent) {
        return new HeaderRecyclerItem(R.layout.list_item_header, parent);
    }

    public class HeaderRecyclerItem extends AssemblyRecyclerItem<String> {
        private TextView textView;

        public HeaderRecyclerItem(int itemLayoutId, ViewGroup parent) {
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
