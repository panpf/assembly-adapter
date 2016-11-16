package me.xiaopan.assemblyadaptersample.itemfactory;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.TextView;

import me.xiaopan.assemblyadapter.AssemblyGroupItem;
import me.xiaopan.assemblyadapter.AssemblyGroupItemFactory;
import me.xiaopan.assemblyadaptersample.R;

public class HeaderGroupItemFactory extends AssemblyGroupItemFactory<HeaderGroupItemFactory.HeaderGroupItem> {
    @Override
    public boolean isTarget(Object data) {
        return data instanceof String;
    }

    @Override
    public HeaderGroupItem createAssemblyItem(ViewGroup parent) {
        return new HeaderGroupItem(R.layout.list_item_header, parent);
    }

    public class HeaderGroupItem extends AssemblyGroupItem<String> {
        private TextView textView;

        public HeaderGroupItem(int itemLayoutId, ViewGroup parent) {
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
