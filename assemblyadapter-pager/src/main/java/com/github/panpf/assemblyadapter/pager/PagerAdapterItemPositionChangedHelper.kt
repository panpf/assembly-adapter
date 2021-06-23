package com.github.panpf.assemblyadapter.pager

import android.util.SparseIntArray

/**
 * Realize that PagerAdapter can refresh correctly when calling notifyDataSetChanged
 */
class PagerAdapterItemPositionChangedHelper {

    private var notifyNumber = 0
    private var notifyNumberPool: SparseIntArray? = null

    var isEnabledPositionNoneOnNotifyDataSetChanged: Boolean
        get() = notifyNumberPool != null
        set(enabled) {
            if (enabled) {
                notifyNumberPool = SparseIntArray()
                notifyNumber = 0
            } else {
                notifyNumberPool = null
            }
        }

    fun onNotifyDataSetChanged() {
        if (notifyNumberPool != null) {
            notifyNumber++
        }
    }

    fun isItemPositionChanged(item: Any): Boolean {
        val notifyNumberPool = notifyNumberPool ?: return false
        val key = item.hashCode()
        val changed = notifyNumberPool[key] != notifyNumber
        if (changed) {
            notifyNumberPool.put(key, notifyNumber)
        }
        return changed
    }
}