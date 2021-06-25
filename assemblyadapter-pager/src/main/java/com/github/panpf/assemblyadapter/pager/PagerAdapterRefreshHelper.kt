package com.github.panpf.assemblyadapter.pager

import android.view.View

/**
 * Realize that PagerAdapter can refresh correctly when calling notifyDataSetChanged
 */
class PagerAdapterRefreshHelper {

    private var notifyDataSetChangedNumber = 0

    fun onNotifyDataSetChanged() {
        if (notifyDataSetChangedNumber == Int.MAX_VALUE) {
            notifyDataSetChangedNumber = 0
        }
        notifyDataSetChangedNumber++
    }

    fun bindNotifyDataSetChangedNumber(view: View) {
        view.setTag(R.id.aa_tag_pagerItem_notifyDataSetChangedNumber, notifyDataSetChangedNumber)
    }

    fun isItemPositionChanged(view: View): Boolean {
        val currentNumber = notifyDataSetChangedNumber
        val bindNumber = view.getTag(R.id.aa_tag_pagerItem_notifyDataSetChangedNumber)?.toString()?.toInt()
        return bindNumber != currentNumber
    }
}