/*
 * Copyright (C) 2021 panpf <panpfpanpf@outlook.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.panpf.assemblyadapter.recycler

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.panpf.assemblyadapter.AssemblyAdapter
import com.github.panpf.assemblyadapter.MatchItemFactory
import kotlin.reflect.KClass

class AssemblyGridLayoutManager : GridLayoutManager {

    private var recyclerView: RecyclerView? = null
    private val gridLayoutItemSpanMap: Map<KClass<out MatchItemFactory>, ItemSpan>
    private val spanSizeLookup = object : SpanSizeLookup() {
        override fun getSpanSize(position: Int): Int {
            return getSpanSizeImpl(position)
        }
    }
    private val concatAdapterAdaptersCacheMap =
        HashMap<ConcatAdapter, ConcatAdapterWrapperAdaptersCache>()

    constructor(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int,
        gridLayoutItemSpanMap: Map<KClass<out MatchItemFactory>, ItemSpan>
    ) : super(context, attrs, defStyleAttr, defStyleRes) {
        this.gridLayoutItemSpanMap = gridLayoutItemSpanMap
        super.setSpanSizeLookup(spanSizeLookup)
    }

    constructor(
        context: Context,
        spanCount: Int,
        orientation: Int,
        reverseLayout: Boolean,
        gridLayoutItemSpanMap: Map<KClass<out MatchItemFactory>, ItemSpan>
    ) : super(context, spanCount, orientation, reverseLayout) {
        this.gridLayoutItemSpanMap = gridLayoutItemSpanMap
        super.setSpanSizeLookup(spanSizeLookup)
    }

    constructor(
        context: Context,
        spanCount: Int,
        gridLayoutItemSpanMap: Map<KClass<out MatchItemFactory>, ItemSpan>
    ) : super(context, spanCount) {
        this.gridLayoutItemSpanMap = gridLayoutItemSpanMap
        super.setSpanSizeLookup(spanSizeLookup)
    }

    override fun setSpanSizeLookup(spanSizeLookup: SpanSizeLookup?) {
//        super.setSpanSizeLookup(spanSizeLookup)
        throw UnsupportedOperationException("AssemblyGridLayoutManager does not support setSpanSizeLookup() method")
    }

    override fun onAttachedToWindow(view: RecyclerView) {
        super.onAttachedToWindow(view)
        recyclerView = view
    }

    private fun getSpanSizeImpl(position: Int): Int {
        val adapter = recyclerView?.adapter
        val gridLayoutItemSpanMap = gridLayoutItemSpanMap
        if (adapter != null && position >= 0 && position < adapter.itemCount) {
            val itemFactory = findItemFactory(adapter, position)
            val itemSpan = gridLayoutItemSpanMap[itemFactory::class]
            if (itemSpan != null) {
                return if (itemSpan.isFullSpan()) spanCount else itemSpan.size.coerceAtLeast(1)
            }
        }
        return 1
    }

    private fun findItemFactory(adapter: RecyclerView.Adapter<*>, position: Int): MatchItemFactory {
        return when (adapter) {
            is AssemblyAdapter -> {
                adapter.getItemFactoryByPosition(position)
            }
            is ConcatAdapter -> {
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
                    findItemFactory(childAdapter, childPosition)
                } else {
                    throw IndexOutOfBoundsException("Index: $position, Size: ${adapter.itemCount}")
                }
            }
            else -> {
                throw IllegalArgumentException("RecyclerView.adapter must be ConcatAdapter or implement the interface AssemblyAdapter: ${adapter.javaClass.name}")
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
    }

    class AnyAdapterDataObserver(val onAnyChanged: () -> Unit) :
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