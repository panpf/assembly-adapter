package me.xiaopan.assemblyadapter;

import android.view.ViewGroup;

public abstract class AssemblyItemFactory<ITEM extends AssemblyItem> {
    private int itemType;
    private AssemblyAdapter adapter;

    public int getItemType() {
        return itemType;
    }

    public void setItemType(int itemType) {
        this.itemType = itemType;
    }

    public void setAdapter(AssemblyAdapter adapter) {
        this.adapter = adapter;
    }

    public AssemblyAdapter getAdapter() {
        return adapter;
    }

    public abstract boolean isTarget(Object itemObject);

    public abstract ITEM createAssemblyItem(ViewGroup parent);
}
