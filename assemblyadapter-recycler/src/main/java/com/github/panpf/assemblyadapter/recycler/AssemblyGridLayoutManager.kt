package com.github.panpf.assemblyadapter.recycler

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.ConcatAdapter
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
            val adapter = assemblyGridLayoutManager.recyclerView?.adapter
            if (adapter != null && position >= 0 && position < adapter.itemCount) {
                val itemSpan = findItemSpan(adapter, position)
                if (itemSpan != null) {
                    return if (itemSpan.isFullSpan()) {
                        assemblyGridLayoutManager.spanCount
                    } else {
                        itemSpan.size.coerceAtLeast(1)
                    }
                }
            }
            return 1
        }

        private fun findItemSpan(adapter: RecyclerView.Adapter<*>, position: Int): ItemSpan? {
            return when (adapter) {
                is GridLayoutItemSpanAdapter<*> -> {
                    adapter.getItemSpanByPosition(position)
                }
                is ConcatAdapter -> {
                    var childAdapterStartPosition = 0
                    val childAdapter = adapter.adapters.find { childAdapter ->
                        val childAdapterEndPosition =
                            childAdapterStartPosition + childAdapter.itemCount - 1
                        @Suppress("ConvertTwoComparisonsToRangeCheck")
                        if (position >= childAdapterStartPosition && position <= childAdapterEndPosition) {
                            true
                        } else {
                            childAdapterStartPosition = childAdapterEndPosition + 1
                            false
                        }
                    }
                    if (childAdapter != null) {
                        val childPosition = position - childAdapterStartPosition
                        findItemSpan(childAdapter, childPosition)
                    } else {
                        throw IndexOutOfBoundsException("Index: $position, Size: ${adapter.itemCount}")
                    }
                }
                else -> {
                    null
                }
            }
        }
    }
}