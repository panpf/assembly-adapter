package me.xiaopan.assemblyadapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

public abstract class AssemblyPagerItemFactory<BEAN> {
    private PagerAdapter adapter;

    void setAdapter(PagerAdapter adapter) {
        this.adapter = adapter;
    }

    public PagerAdapter getAdapter() {
        return adapter;
    }

    public abstract boolean isTarget(Object itemObject);

    public abstract View createView(Context context, ViewGroup container, int position, BEAN bean);
}
