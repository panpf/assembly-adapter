package me.xiaopan.assemblyadapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

public abstract class AssemblyPagerItemFactory<BEAN> {
    private AssemblyPagerAdapter adapter;

    void setAdapter(AssemblyPagerAdapter adapter) {
        this.adapter = adapter;
    }

    public AssemblyPagerAdapter getAdapter() {
        return adapter;
    }

    public abstract boolean isTarget(Object itemObject);

    public abstract View createView(Context context, ViewGroup container, int position, BEAN bean);
}
