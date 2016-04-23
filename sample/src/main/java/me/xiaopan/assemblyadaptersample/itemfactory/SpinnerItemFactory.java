package me.xiaopan.assemblyadaptersample.itemfactory;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import me.xiaopan.assemblyadapter.AssemblyItem;
import me.xiaopan.assemblyadapter.AssemblyItemFactory;

public class SpinnerItemFactory extends AssemblyItemFactory<SpinnerItemFactory.SpinnerItem>{

    @Override
    public boolean isTarget(Object itemObject) {
        return itemObject instanceof String;
    }

    @Override
    public SpinnerItem createAssemblyItem(ViewGroup parent) {
        return new SpinnerItem(android.R.layout.simple_list_item_1, parent);
    }

    public static class SpinnerItem extends AssemblyItem<String>{
        TextView textView;

        public SpinnerItem(int itemLayoutId, ViewGroup parent) {
            super(itemLayoutId, parent);
        }

        @Override
        protected void onFindViews(View itemView) {
            textView = (TextView) findViewById(android.R.id.text1);
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
