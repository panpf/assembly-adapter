package me.xiaopan.assemblyadaptersample.itemfactory;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;
import android.widget.TextView;

import me.xiaopan.assemblyadapter.AssemblyRecyclerItem;
import me.xiaopan.assemblyadapter.AssemblyRecyclerItemFactory;
import me.xiaopan.assemblyadaptersample.R;

public class AppListHeaderItemFactory extends AssemblyRecyclerItemFactory<AppListHeaderItemFactory.AppListHeaderItem> {
    private RecyclerView recyclerView;

    public AppListHeaderItemFactory(RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
    }

    @Override
    public boolean isTarget(Object data) {
        return data instanceof String;
    }

    @Override
    public AppListHeaderItem createAssemblyItem(ViewGroup viewGroup) {
        return new AppListHeaderItem(R.layout.list_item_app_list_header, viewGroup);
    }

    @Override
    public int getSpanSize() {
        if (recyclerView == null) {
            return 1;
        }
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if (layoutManager == null || !(layoutManager instanceof GridLayoutManager)) {
            return 1;
        }

        GridLayoutManager gridLayoutManager = (GridLayoutManager) layoutManager;
        return gridLayoutManager.getSpanCount();
    }

    public class AppListHeaderItem extends AssemblyRecyclerItem<String> {
        TextView textView;

        public AppListHeaderItem(int itemLayoutId, ViewGroup parent) {
            super(itemLayoutId, parent);
        }

        @Override
        protected void onFindViews() {
            textView = findViewById(R.id.text_appListHeaderItem);
        }

        @Override
        protected void onConfigViews(Context context) {

        }

        @Override
        protected void onSetData(int i, String s) {
            textView.setText(s);
        }
    }
}
