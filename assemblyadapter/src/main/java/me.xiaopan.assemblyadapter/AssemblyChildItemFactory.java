package me.xiaopan.assemblyadapter;

import android.view.ViewGroup;

public abstract class AssemblyChildItemFactory<ITEM extends AssemblyChildItem> {
    private int itemType;
    private AssemblyExpandableAdapter adapter;

    public int getItemType() {
        return itemType;
    }

    public void setItemType(int itemType) {
        this.itemType = itemType;
    }

    public AssemblyExpandableAdapter getAdapter() {
        return adapter;
    }

    public void setAdapter(AssemblyExpandableAdapter adapter) {
        this.adapter = adapter;
    }

    public abstract boolean isTarget(Object itemObject);

    public abstract ITEM createAssemblyItem(ViewGroup parent);
}
