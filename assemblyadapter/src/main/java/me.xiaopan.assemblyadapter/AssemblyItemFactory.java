package me.xiaopan.assemblyadapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public abstract class AssemblyItemFactory<ITEM extends AssemblyItem>{
    protected int itemType;
    protected AssemblyAdapter adapter;

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

    public View inflateView(int itemLayoutId, ViewGroup parent){
        return LayoutInflater.from(parent.getContext()).inflate(itemLayoutId, parent, false);
    }
}
