package com.github.panpf.assemblyadapter.pager2

import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.RecyclerView

class ConcatAdapterAbsoluteHelper {

    private val concatAdapterAdaptersCacheMap =
        HashMap<ConcatAdapter, ConcatAdapterWrapperAdaptersCache>()

    fun findAbsoluteAdapterPosition(
        adapter: RecyclerView.Adapter<*>, localAdapter: RecyclerView.Adapter<*>, localPosition: Int
    ): Int {
        return findAbsoluteAdapterPositionReal(
            adapter, localAdapter, localPosition
        ) ?: throw IndexOutOfBoundsException(
            "Not found childAdapterStartPosition by " +
                    "localPosition: $localPosition, localAdapter: ${localAdapter::class.java.name}"
        )
    }

    private fun findAbsoluteAdapterPositionReal(
        adapter: RecyclerView.Adapter<*>, localAdapter: RecyclerView.Adapter<*>, localPosition: Int
    ): Int? {
        return when {
            localAdapter === adapter -> {
                localPosition
            }
            adapter is ConcatAdapter -> {
                val wrapperAdapters = concatAdapterAdaptersCacheMap.getOrPut(adapter) {
                    ConcatAdapterWrapperAdaptersCache(adapter)
                }.getCachedAdapters()

                var childAdapterStartPosition = 0
                wrapperAdapters.forEach { childAdapter ->
                    val childPosition = findAbsoluteAdapterPositionReal(
                        childAdapter, localAdapter, localPosition
                    )
                    if (childPosition != null) {
                        return childAdapterStartPosition + childPosition
                    }
                    childAdapterStartPosition = childAdapter.itemCount
                }
                null
            }
            else -> {
                null
            }
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