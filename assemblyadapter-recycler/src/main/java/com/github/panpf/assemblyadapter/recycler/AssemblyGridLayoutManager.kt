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
                    return if (itemSpan.size < 0) {
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
                    val (childAdapter, childPosition) =
                        findChildAdapterAndChildPosition(adapter, position)
                    findItemSpan(childAdapter, childPosition)
                }
                else -> {
                    null
                }
            }
        }

        private fun findChildAdapterAndChildPosition(
            concatAdapter: ConcatAdapter, position: Int
        ): Pair<RecyclerView.Adapter<*>, Int> {
            var childAdapterStartPosition = 0
            val childAdapter = concatAdapter.adapters.find { childAdapter ->
                val childAdapterEndPosition = childAdapterStartPosition + childAdapter.itemCount - 1
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
                return childAdapter to childPosition
            } else {
                throw IndexOutOfBoundsException("Index: $position, Size: ${concatAdapter.itemCount}")
            }
        }
    }
}