package com.github.panpf.assemblyadapter.list.concat;

/**
 * Helper class to hold onto wrapper and local position without allocating objects as this is
 * a very common call.
 */
public class WrapperAndLocalPosition {
    public NestedAdapterWrapper mWrapper;
    public int mLocalPosition;
    public boolean mInUse;
}
