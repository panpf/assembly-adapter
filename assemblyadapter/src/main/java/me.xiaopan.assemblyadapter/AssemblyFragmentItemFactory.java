package me.xiaopan.assemblyadapter;

import android.support.v4.app.Fragment;

public abstract class AssemblyFragmentItemFactory<BEAN> {

    public abstract boolean isTarget(Object itemObject);

    public abstract Fragment createFragment(int position, BEAN bean);
}
