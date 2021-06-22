package com.github.panpf.assemblyadapter.list.concat.expandable;

/**
 * Helper class to hold onto wrapper and local position without allocating objects as this is
 * a very common call.
 */
public class ExpandableWrapperAndLocalPosition {
    public ExpandableNestedAdapterWrapper mWrapper;
    public int mLocalPosition;
    public boolean mInUse;
}
