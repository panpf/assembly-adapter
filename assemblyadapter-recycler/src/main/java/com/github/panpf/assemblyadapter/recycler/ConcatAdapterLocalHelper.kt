package com.github.panpf.assemblyadapter.recycler

import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.RecyclerView

class ConcatAdapterLocalHelper {

    private val concatAdapterAdaptersCacheMap =
        HashMap<ConcatAdapter, ConcatAdapterWrapperAdaptersCache>()

    fun reset() {
        concatAdapterAdaptersCacheMap.clear()
    }

    fun findLocalAdapterAndPositionImpl(
        adapter: ConcatAdapter, position: Int
    ): Pair<RecyclerView.Adapter<*>, Int> {
        val wrapperAdapters = concatAdapterAdaptersCacheMap.getOrPut(adapter) {
            ConcatAdapterWrapperAdaptersCache(adapter)
        }.getCachedAdapters()
        var childAdapterStartPosition = 0
        val childAdapter = wrapperAdapters.find { childAdapter ->
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
            return childAdapter to childPosition
        } else {
            throw IndexOutOfBoundsException("Index: $position, Size: ${adapter.itemCount}")
        }
    }

    private class ConcatAdapterWrapperAdaptersCache(val concatAdapter: ConcatAdapter) {

        private var cachedAdapters: List<RecyclerView.Adapter<*>>? = null
        private val cachedAdapterDataSetChangedCallback = AnyAdapterDataObserver {
            cachedAdapters = null
        }

        init {
            try {
                concatAdapter.registerAdapterDataObserver(cachedAdapterDataSetChangedCallback)
            } catch (e: IllegalStateException) {
                // already registered
            }
        }

        fun getCachedAdapters(): List<RecyclerView.Adapter<*>> {
            /*
             * Because concatAdapter.adapters creates a new List every time it is called, consider caching it for list sliding performance
             */
            return cachedAdapters ?: concatAdapter.adapters.apply {
                this@ConcatAdapterWrapperAdaptersCache.cachedAdapters = this
            }
        }

        private class AnyAdapterDataObserver(val onAnyChanged: () -> Unit) :
            RecyclerView.AdapterDataObserver() {
            override fun onChanged() {
                super.onChanged()
                onAnyChanged()
            }

            override fun onItemRangeChanged(positionStart: Int, itemCount: Int) {
                super.onItemRangeChanged(positionStart, itemCount)
                onAnyChanged()
            }

            override fun onItemRangeChanged(positionStart: Int, itemCount: Int, payload: Any?) {
                super.onItemRangeChanged(positionStart, itemCount, payload)
                onAnyChanged()
            }

            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                super.onItemRangeInserted(positionStart, itemCount)
                onAnyChanged()
            }

            override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {
                super.onItemRangeRemoved(positionStart, itemCount)
                onAnyChanged()
            }

            override fun onItemRangeMoved(fromPosition: Int, toPosition: Int, itemCount: Int) {
                super.onItemRangeMoved(fromPosition, toPosition, itemCount)
                onAnyChanged()
            }
        }
    }
}