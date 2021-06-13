package com.github.panpf.assemblyadapter.pager

import android.util.SparseIntArray
import androidx.viewpager.widget.PagerAdapter

/**
 * Realize that PagerAdapter can refresh correctly when calling notifyDataSetChanged
 */
class PagerAdapterNotifyCountHelper {

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

    fun getItemPosition(pagerAdapter: PagerAdapter, item: Any): Int {
        val notifyNumberPool = notifyNumberPool
        val key = item.hashCode()
        return if (notifyNumberPool != null && notifyNumberPool[key] != notifyNumber) {
            notifyNumberPool.put(key, notifyNumber)
            PagerAdapter.POSITION_NONE
        } else {
            pagerAdapter.getItemPosition(item)
        }
    }
}