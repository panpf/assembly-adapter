package me.xiaopan.assemblyadapter;

import android.view.ViewGroup;

public abstract class AssemblyRecyclerItemFactory<ITEM extends AssemblyRecyclerItem> {
    private int itemType;
    private AssemblyRecyclerAdapter adapter;

    public int getItemType() {
        return itemType;
    }

    public void setItemType(int itemType) {
        this.itemType = itemType;
    }

    public AssemblyRecyclerAdapter getAdapter() {
        return adapter;
    }

    void setAdapter(AssemblyRecyclerAdapter adapter) {
        this.adapter = adapter;
    }

    public int getSpanSize() {
        return 1;
    }

    public abstract boolean isTarget(Object itemObject);

    public abstract ITEM createAssemblyItem(ViewGroup parent);
}
