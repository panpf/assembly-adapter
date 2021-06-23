package com.github.panpf.assemblyadapter.list.concat;

/**
 * Helper class to hold onto wrapper and local position without allocating objects as this is
 * a very common call.
 */
public class ListWrapperAndLocalPosition {
    public NestedListAdapterWrapper mWrapper;
    public int mLocalPosition;
    public boolean mInUse;
}
