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
import com.github.panpf.assemblyadapter.ItemFactory
import com.github.panpf.assemblyadapter.recycler.internal.ConcatAdapterLocalHelper
import kotlin.reflect.KClass

/**
 * An implementation of [GridLayoutManager]. Set the spanSize of [GridLayoutManager] according to [ItemFactory] as the identifier
 */
class AssemblyGridLayoutManager : GridLayoutManager {

    private var recyclerView: RecyclerView? = null
    private var concatAdapterLocalHelper: ConcatAdapterLocalHelper? = null
    private val gridLayoutItemSpanMap: Map<Class<out ItemFactory<*>>, ItemSpan>
    private val spanSizeLookup = object : SpanSizeLookup() {
        override fun getSpanSize(position: Int): Int {
            return getSpanSizeImpl(position)
        }
    }

    /**
     * Constructor used when layout manager is set in XML by RecyclerView attribute
     * "layoutManager". If spanCount is not specified in the XML, it defaults to a
     * single column.
     *
     * @param gridLayoutItemSpanMap Map of spanSize corresponding to [ItemFactory]
     * @see androidx.recyclerview.R.attr.spanCount
     */
    constructor(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int,
        gridLayoutItemSpanMap: Map<KClass<out ItemFactory<*>>, ItemSpan>
    ) : super(context, attrs, defStyleAttr, defStyleRes) {
        this.gridLayoutItemSpanMap = gridLayoutItemSpanMap.map { it.key.java to it.value }.toMap()
        super.setSpanSizeLookup(spanSizeLookup)
    }

    /**
     * Creates a GridLayoutManager
     *
     * @param context Current context, will be used to access resources.
     * @param spanCount The number of columns or rows in the grid
     * @param orientation Layout orientation. Should be [GridLayoutManager.HORIZONTAL] or [GridLayoutManager.VERTICAL].
     * @param reverseLayout When set to true, layouts from end to start.
     * @param gridLayoutItemSpanMap Map of spanSize corresponding to [ItemFactory]
     */
    constructor(
        context: Context,
        spanCount: Int,
        orientation: Int,
        reverseLayout: Boolean,
        gridLayoutItemSpanMap: Map<KClass<out ItemFactory<*>>, ItemSpan>
    ) : super(context, spanCount, orientation, reverseLayout) {
        this.gridLayoutItemSpanMap = gridLayoutItemSpanMap.map { it.key.java to it.value }.toMap()
        super.setSpanSizeLookup(spanSizeLookup)
    }


    /**
     * Creates a vertical GridLayoutManager
     *
     * @param context Current context, will be used to access resources.
     * @param spanCount The number of columns in the grid
     * @param gridLayoutItemSpanMap Map of spanSize corresponding to [ItemFactory]
     */
    constructor(
        context: Context,
        spanCount: Int,
        gridLayoutItemSpanMap: Map<KClass<out ItemFactory<*>>, ItemSpan>
    ) : super(context, spanCount) {
        this.gridLayoutItemSpanMap = gridLayoutItemSpanMap.map { it.key.java to it.value }.toMap()
        super.setSpanSizeLookup(spanSizeLookup)
    }

    override fun setSpanSizeLookup(spanSizeLookup: SpanSizeLookup?) {
//        super.setSpanSizeLookup(spanSizeLookup)
        throw UnsupportedOperationException("AssemblyGridLayoutManager does not support setSpanSizeLookup() method")
    }

    override fun onAttachedToWindow(view: RecyclerView) {
        super.onAttachedToWindow(view)
        recyclerView = view
        concatAdapterLocalHelper?.reset()
    }

    override fun onDetachedFromWindow(view: RecyclerView?, recycler: RecyclerView.Recycler?) {
        super.onDetachedFromWindow(view, recycler)
        concatAdapterLocalHelper?.reset()
    }

    override fun onAdapterChanged(
        oldAdapter: RecyclerView.Adapter<*>?, newAdapter: RecyclerView.Adapter<*>?
    ) {
        super.onAdapterChanged(oldAdapter, newAdapter)
        concatAdapterLocalHelper?.reset()
    }

    private fun getSpanSizeImpl(position: Int): Int {
        val adapter = recyclerView?.adapter
        val gridLayoutItemSpanMap = gridLayoutItemSpanMap
        if (adapter != null && position >= 0 && position < adapter.itemCount) {
            val itemFactory = findItemFactory(adapter, position)
            val itemSpan = gridLayoutItemSpanMap[itemFactory.javaClass]
            if (itemSpan != null) {
                return if (itemSpan.isFullSpan()) spanCount else itemSpan.size.coerceAtLeast(1)
            }
        }
        return 1
    }

    private fun findItemFactory(adapter: RecyclerView.Adapter<*>, position: Int): ItemFactory<*> {
        return when (adapter) {
            is AssemblyAdapter<*> -> {
                adapter.getItemFactoryByPosition(position) as ItemFactory<*>
            }
            is ConcatAdapter -> {
                val (childAdapter, childPosition) = (concatAdapterLocalHelper
                    ?: ConcatAdapterLocalHelper().apply {
                        this@AssemblyGridLayoutManager.concatAdapterLocalHelper = this
                    }).findLocalAdapterAndPositionImpl(adapter, position)
                findItemFactory(childAdapter, childPosition)
            }
            else -> {
                throw IllegalArgumentException("RecyclerView.adapter must be ConcatAdapter or implement the interface AssemblyAdapter: ${adapter.javaClass.name}")
            }
        }
    }
}