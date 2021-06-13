package com.github.panpf.assemblyadapter.recycler

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class AssemblyGridLayoutManager : GridLayoutManager {

    private var recyclerView: RecyclerView? = null

    constructor(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes) {
        super.setSpanSizeLookup(AssemblySpanSizeLookup(this))
    }

    constructor(
        context: Context,
        spanCount: Int,
        orientation: Int,
        reverseLayout: Boolean
    ) : super(context, spanCount, orientation, reverseLayout) {
        super.setSpanSizeLookup(AssemblySpanSizeLookup(this))
    }

    constructor(context: Context, spanCount: Int) : super(context, spanCount) {
        super.setSpanSizeLookup(AssemblySpanSizeLookup(this))
    }

    override fun setSpanSizeLookup(spanSizeLookup: SpanSizeLookup?) {
//        super.setSpanSizeLookup(spanSizeLookup)
        throw UnsupportedOperationException("AssemblyGridLayoutManager does not support setSpanSizeLookup() method")
    }

    override fun onAttachedToWindow(view: RecyclerView) {
        super.onAttachedToWindow(view)
        recyclerView = view
    }

    private class AssemblySpanSizeLookup(
        private val assemblyGridLayoutManager: AssemblyGridLayoutManager
    ) : SpanSizeLookup() {
        override fun getSpanSize(position: Int): Int {
            val recyclerView = assemblyGridLayoutManager.recyclerView ?: return 1

            val adapter = recyclerView.adapter
            if (adapter !is GridLayoutItemSpanAdapter) return 1

            val itemSpanMap = adapter.getGridLayoutItemSpanMap()
            if (itemSpanMap == null || itemSpanMap.isEmpty()) return 1

            val itemFactory = adapter.getItemFactoryByPosition(position)
            val itemSpan = itemSpanMap[itemFactory.javaClass]
            val spanSize = itemSpan?.span ?: 1
            return if (spanSize < 0) {
                assemblyGridLayoutManager.spanCount
            } else {
                spanSize.coerceAtLeast(1)
            }
        }
    }
}