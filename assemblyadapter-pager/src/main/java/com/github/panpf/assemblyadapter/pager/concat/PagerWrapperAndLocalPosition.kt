package com.github.panpf.assemblyadapter.pager.concat

/**
 * Helper class to hold onto wrapper and local position without allocating objects as this is
 * a very common call.
 */
internal class PagerWrapperAndLocalPosition {
    internal var mWrapper: NestedPagerAdapterWrapper? = null
    internal var mLocalPosition = 0
    internal var mInUse = false
}