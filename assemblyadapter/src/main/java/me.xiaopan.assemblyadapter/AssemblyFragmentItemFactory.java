package me.xiaopan.assemblyadapter;

import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;

public abstract class AssemblyFragmentItemFactory<BEAN> {
    private PagerAdapter adapter;

    void setAdapter(PagerAdapter adapter) {
        this.adapter = adapter;
    }

    public PagerAdapter getAdapter() {
        return adapter;
    }

    public abstract boolean isTarget(Object itemObject);

    public abstract Fragment createFragment(int position, BEAN bean);
}
