package com.github.panpf.assemblyadapter.pager

import android.util.SparseIntArray

/**
 * Realize that PagerAdapter can refresh correctly when calling notifyDataSetChanged
 */
class PagerAdapterRefreshHelper {

    private var notifyNumber = 0
    private val notifyNumberPool = SparseIntArray()

    fun onNotifyDataSetChanged() {
        notifyNumber++
    }

    fun isItemPositionChanged(item: Any): Boolean {
        val key = item.hashCode()
        val changed = notifyNumberPool[key] != notifyNumber
        if (changed) {
            notifyNumberPool.put(key, notifyNumber)
        }
        return changed
    }
}