package me.xiaopan.assemblyadaptersample.itemfactory;

import android.content.Context;
import android.view.LayoutInflater;
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
        return new SpinnerItem(LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, parent, false), this);
    }

    public static class SpinnerItem extends AssemblyItem<String, SpinnerItemFactory>{
        TextView textView;

        protected SpinnerItem(View convertView, SpinnerItemFactory itemFactory) {
            super(convertView, itemFactory);
        }

        @Override
        protected void onFindViews(View convertView) {
            textView = (TextView) convertView.findViewById(android.R.id.text1);
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
