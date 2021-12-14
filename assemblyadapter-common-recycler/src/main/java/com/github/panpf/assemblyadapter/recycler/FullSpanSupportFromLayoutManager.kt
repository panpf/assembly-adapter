package com.github.panpf.assemblyadapter.recycler

import androidx.recyclerview.widget.RecyclerView

class FullSpanSupportFromLayoutManager : FullSpanSupport {

    override fun isFullSpan(parent: RecyclerView, position: Int): Boolean {
        parent.adapter ?: return false
        val layoutManager = parent.layoutManager
        return if (layoutManager is FullSpanSupport) {
            layoutManager.isFullSpan(parent, position)
        } else {
            false
        }
    }
}