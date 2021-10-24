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
import android.util.SparseArray
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.panpf.assemblyadapter.AssemblyAdapter
import com.github.panpf.assemblyadapter.ItemFactory
import kotlin.reflect.KClass

/**
 * An implementation of [GridLayoutManager]. Set the spanSize of [GridLayoutManager] according to [ItemFactory] as the identifier
 */
class AssemblyGridLayoutManager : GridLayoutManager {

    private val concatAdapterLocalHelper = ConcatAdapterLocalHelper()
    private val itemSpanByPositionSparseArray: SparseArray<ItemSpan>?
    private val itemSpanByItemFactoryMap: Map<Class<out ItemFactory<out Any>>, ItemSpan>?
    private val spanSizeLookup = object : SpanSizeLookup() {
        override fun getSpanSize(position: Int): Int {
            return getSpanSizeImpl(position)
        }
    }

    private var recyclerView: RecyclerView? = null

    /**
     * Constructor used when layout manager is set in XML by RecyclerView attribute
     * "layoutManager". If spanCount is not specified in the XML, it defaults to a
     * single column.
     *
     * @param itemSpanByPositionMap Map of spanSize corresponding to position
     * @param itemSpanByItemFactoryMap Map of spanSize corresponding to [ItemFactory]
     * @see androidx.recyclerview.R.attr.spanCount
     */
    constructor(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int,
        itemSpanByPositionMap: Map<Int, ItemSpan>?,
        itemSpanByItemFactoryMap: Map<KClass<out ItemFactory<out Any>>, ItemSpan>?
    ) : super(context, attrs, defStyleAttr, defStyleRes) {
        this.itemSpanByPositionSparseArray = itemSpanByPositionMap?.let { map ->
            SparseArray<ItemSpan>().apply {
                map.entries.forEach { mapItem ->
                    put(mapItem.key, mapItem.value)
                }
            }
        }
        this.itemSpanByItemFactoryMap =
            itemSpanByItemFactoryMap?.map { it.key.java to it.value }?.toMap()
        super.setSpanSizeLookup(spanSizeLookup)
    }

    /**
     * Constructor used when layout manager is set in XML by RecyclerView attribute
     * "layoutManager". If spanCount is not specified in the XML, it defaults to a
     * single column.
     *
     * @param itemSpanByItemFactoryMap Map of spanSize corresponding to [ItemFactory]
     * @see androidx.recyclerview.R.attr.spanCount
     */
    constructor(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int,
        itemSpanByItemFactoryMap: Map<KClass<out ItemFactory<out Any>>, ItemSpan>
    ) : super(context, attrs, defStyleAttr, defStyleRes) {
        this.itemSpanByPositionSparseArray = null
        this.itemSpanByItemFactoryMap =
            itemSpanByItemFactoryMap.map { it.key.java to it.value }.toMap()
        super.setSpanSizeLookup(spanSizeLookup)
    }

    /**
     * Creates a GridLayoutManager
     *
     * @param context Current context, will be used to access resources.
     * @param spanCount The number of columns or rows in the grid
     * @param orientation Layout orientation. Should be [GridLayoutManager.HORIZONTAL] or [GridLayoutManager.VERTICAL].
     * @param reverseLayout When set to true, layouts from end to start.
     * @param itemSpanByPositionMap Map of spanSize corresponding to position
     * @param itemSpanByItemFactoryMap Map of spanSize corresponding to [ItemFactory]
     */
    constructor(
        context: Context,
        spanCount: Int,
        orientation: Int,
        reverseLayout: Boolean,
        itemSpanByPositionMap: Map<Int, ItemSpan>?,
        itemSpanByItemFactoryMap: Map<KClass<out ItemFactory<out Any>>, ItemSpan>?,
    ) : super(context, spanCount, orientation, reverseLayout) {
        this.itemSpanByPositionSparseArray = itemSpanByPositionMap?.let { map ->
            SparseArray<ItemSpan>().apply {
                map.entries.forEach { mapItem ->
                    put(mapItem.key, mapItem.value)
                }
            }
        }
        this.itemSpanByItemFactoryMap =
            itemSpanByItemFactoryMap?.map { it.key.java to it.value }?.toMap()
        super.setSpanSizeLookup(spanSizeLookup)
    }

    /**
     * Creates a GridLayoutManager
     *
     * @param context Current context, will be used to access resources.
     * @param spanCount The number of columns or rows in the grid
     * @param orientation Layout orientation. Should be [GridLayoutManager.HORIZONTAL] or [GridLayoutManager.VERTICAL].
     * @param reverseLayout When set to true, layouts from end to start.
     * @param itemSpanByItemFactoryMap Map of spanSize corresponding to [ItemFactory]
     */
    constructor(
        context: Context,
        spanCount: Int,
        orientation: Int,
        reverseLayout: Boolean,
        itemSpanByItemFactoryMap: Map<KClass<out ItemFactory<out Any>>, ItemSpan>,
    ) : super(context, spanCount, orientation, reverseLayout) {
        this.itemSpanByPositionSparseArray = null
        this.itemSpanByItemFactoryMap =
            itemSpanByItemFactoryMap.map { it.key.java to it.value }.toMap()
        super.setSpanSizeLookup(spanSizeLookup)
    }


    /**
     * Creates a vertical GridLayoutManager
     *
     * @param context Current context, will be used to access resources.
     * @param spanCount The number of columns in the grid
     * @param itemSpanByPositionMap Map of spanSize corresponding to position
     * @param itemSpanByItemFactoryMap Map of spanSize corresponding to [ItemFactory]
     */
    constructor(
        context: Context,
        spanCount: Int,
        itemSpanByPositionMap: Map<Int, ItemSpan>?,
        itemSpanByItemFactoryMap: Map<KClass<out ItemFactory<out Any>>, ItemSpan>?
    ) : super(context, spanCount) {
        this.itemSpanByPositionSparseArray = itemSpanByPositionMap?.let { map ->
            SparseArray<ItemSpan>().apply {
                map.entries.forEach { mapItem ->
                    put(mapItem.key, mapItem.value)
                }
            }
        }
        this.itemSpanByItemFactoryMap =
            itemSpanByItemFactoryMap?.map { it.key.java to it.value }?.toMap()
        super.setSpanSizeLookup(spanSizeLookup)
    }

    /**
     * Creates a vertical GridLayoutManager
     *
     * @param context Current context, will be used to access resources.
     * @param spanCount The number of columns in the grid
     * @param itemSpanByItemFactoryMap Map of spanSize corresponding to [ItemFactory]
     */
    constructor(
        context: Context,
        spanCount: Int,
        itemSpanByItemFactoryMap: Map<KClass<out ItemFactory<out Any>>, ItemSpan>
    ) : super(context, spanCount) {
        this.itemSpanByPositionSparseArray = null
        this.itemSpanByItemFactoryMap =
            itemSpanByItemFactoryMap.map { it.key.java to it.value }.toMap()
        super.setSpanSizeLookup(spanSizeLookup)
    }

    override fun setSpanSizeLookup(spanSizeLookup: SpanSizeLookup?) {
//        super.setSpanSizeLookup(spanSizeLookup)
        throw UnsupportedOperationException("AssemblyGridLayoutManager does not support setSpanSizeLookup() method")
    }

    override fun onAttachedToWindow(view: RecyclerView) {
        super.onAttachedToWindow(view)
        recyclerView = view
        concatAdapterLocalHelper.reset()
    }

    override fun onDetachedFromWindow(view: RecyclerView?, recycler: RecyclerView.Recycler?) {
        super.onDetachedFromWindow(view, recycler)
        concatAdapterLocalHelper.reset()
    }

    override fun onAdapterChanged(
        oldAdapter: RecyclerView.Adapter<*>?, newAdapter: RecyclerView.Adapter<*>?
    ) {
        super.onAdapterChanged(oldAdapter, newAdapter)
        concatAdapterLocalHelper.reset()
    }

    private fun getSpanSizeImpl(position: Int): Int {
        val itemSpanByPosition = itemSpanByPositionSparseArray
        if (itemSpanByPosition != null) {
            val itemSpan = itemSpanByPosition[position]
            if (itemSpan != null) {
                return if (itemSpan.isFullSpan()) {
                    spanCount
                } else {
                    itemSpan.size.coerceAtLeast(1).coerceAtMost(spanCount)
                }
            }
        }

        val itemSpanByItemFactoryMap = itemSpanByItemFactoryMap
        val adapter = recyclerView?.adapter
        if (itemSpanByItemFactoryMap != null
            && adapter != null
            && position >= 0
            && position < adapter.itemCount
        ) {
            val itemFactory = findItemFactory(adapter, position)
            val itemSpan = itemSpanByItemFactoryMap[itemFactory.javaClass]
            if (itemSpan != null) {
                return if (itemSpan.isFullSpan()) {
                    spanCount
                } else {
                    itemSpan.size.coerceAtLeast(1).coerceAtMost(spanCount)
                }
            }
        }

        return 1
    }

    private fun findItemFactory(adapter: RecyclerView.Adapter<*>, position: Int): ItemFactory<Any> {
        val (localAdapter, localPosition) = concatAdapterLocalHelper.findLocalAdapterAndPosition(
            adapter,
            position
        )
        return if (localAdapter is AssemblyAdapter<*, *>) {
            localAdapter.getItemFactoryByPosition(localPosition) as ItemFactory<Any>
        } else {
            throw IllegalArgumentException("RecyclerView.adapter must be ConcatAdapter or implement the interface AssemblyAdapter: ${adapter.javaClass.name}")
        }
    }
}