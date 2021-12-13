package com.github.panpf.assemblyadapter.recycler.divider

import androidx.recyclerview.widget.RecyclerView
import com.github.panpf.assemblyadapter.recycler.FullSpanSupport

class IsFullSpanByPositionImpl : IsFullSpanByPosition {

    override fun isFullSpan(parent: RecyclerView, position: Int): Boolean {
        val adapter = parent.adapter
        return if (adapter != null) {
            val layoutManager = parent.layoutManager
            if (layoutManager is FullSpanSupport) {
                layoutManager.isFullSpan(parent, position)
            } else {
                false
            }
        } else {
            false
        }
    }
}