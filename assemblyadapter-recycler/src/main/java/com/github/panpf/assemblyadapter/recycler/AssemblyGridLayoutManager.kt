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
import androidx.collection.ArrayMap
import androidx.collection.SparseArrayCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.panpf.assemblyadapter.AssemblyAdapter
import com.github.panpf.assemblyadapter.ItemFactory
import kotlin.reflect.KClass

/**
 * An implementation of [GridLayoutManager]. Set the spanSize of [GridLayoutManager] according to position and itemType and [ItemFactory] as the identifier
 */
class AssemblyGridLayoutManager : GridLayoutManager {

    private val concatAdapterLocalHelper = ConcatAdapterLocalHelper()
    private val itemSpanByPositionSparseArray: SparseArrayCompat<ItemSpan>?
    private val itemSpanByItemTypeSparseArray: SparseArrayCompat<ItemSpan>?
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
     * @param itemSpanByItemTypeMap Map of spanSize corresponding to itemType
     * @param itemSpanByItemFactoryMap Map of spanSize corresponding to [ItemFactory]
     * @see androidx.recyclerview.R.attr.spanCount
     */
    constructor(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int,
        itemSpanByPositionMap: Map<Int, ItemSpan>?,
        itemSpanByItemTypeMap: Map<Int, ItemSpan>?,
        itemSpanByItemFactoryMap: Map<KClass<out ItemFactory<out Any>>, ItemSpan>?
    ) : super(context, attrs, defStyleAttr, defStyleRes) {
        this.itemSpanByPositionSparseArray = itemSpanByPositionMap?.takeIf { it.isNotEmpty() }
            ?.let { map ->
                SparseArrayCompat<ItemSpan>().apply {
                    map.entries.forEach { mapItem ->
                        put(mapItem.key, mapItem.value)
                    }
                }
            }
        this.itemSpanByItemTypeSparseArray = itemSpanByItemTypeMap?.takeIf { it.isNotEmpty() }
            ?.let { map ->
                SparseArrayCompat<ItemSpan>().apply {
                    map.entries.forEach { mapItem ->
                        put(mapItem.key, mapItem.value)
                    }
                }
            }
        this.itemSpanByItemFactoryMap = itemSpanByItemFactoryMap?.takeIf { it.isNotEmpty() }
            ?.let { map ->
                ArrayMap<Class<out ItemFactory<out Any>>, ItemSpan>().apply {
                    map.entries.forEach { mapItem ->
                        put(mapItem.key.java, mapItem.value)
                    }
                }
            }
        super.setSpanSizeLookup(spanSizeLookup)
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
        gridLayoutItemSpanMap: Map<KClass<out ItemFactory<out Any>>, ItemSpan>  // For compatibility reasons, the old parameter names are still maintained here
    ) : super(context, attrs, defStyleAttr, defStyleRes) {
        this.itemSpanByPositionSparseArray = null
        this.itemSpanByItemTypeSparseArray = null
        this.itemSpanByItemFactoryMap = gridLayoutItemSpanMap.takeIf { it.isNotEmpty() }
            ?.let { map ->
                ArrayMap<Class<out ItemFactory<out Any>>, ItemSpan>().apply {
                    map.entries.forEach { mapItem ->
                        put(mapItem.key.java, mapItem.value)
                    }
                }
            }
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
     * @param itemSpanByItemTypeMap Map of spanSize corresponding to itemType
     * @param itemSpanByItemFactoryMap Map of spanSize corresponding to [ItemFactory]
     */
    constructor(
        context: Context,
        spanCount: Int,
        orientation: Int,
        reverseLayout: Boolean,
        itemSpanByPositionMap: Map<Int, ItemSpan>?,
        itemSpanByItemTypeMap: Map<Int, ItemSpan>?,
        itemSpanByItemFactoryMap: Map<KClass<out ItemFactory<out Any>>, ItemSpan>?,
    ) : super(context, spanCount, orientation, reverseLayout) {
        this.itemSpanByPositionSparseArray = itemSpanByPositionMap?.takeIf { it.isNotEmpty() }
            ?.let { map ->
                SparseArrayCompat<ItemSpan>().apply {
                    map.entries.forEach { mapItem ->
                        put(mapItem.key, mapItem.value)
                    }
                }
            }
        this.itemSpanByItemTypeSparseArray = itemSpanByItemTypeMap?.takeIf { it.isNotEmpty() }
            ?.let { map ->
                SparseArrayCompat<ItemSpan>().apply {
                    map.entries.forEach { mapItem ->
                        put(mapItem.key, mapItem.value)
                    }
                }
            }
        this.itemSpanByItemFactoryMap = itemSpanByItemFactoryMap?.takeIf { it.isNotEmpty() }
            ?.let { map ->
                ArrayMap<Class<out ItemFactory<out Any>>, ItemSpan>().apply {
                    map.entries.forEach { mapItem ->
                        put(mapItem.key.java, mapItem.value)
                    }
                }
            }
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
        gridLayoutItemSpanMap: Map<KClass<out ItemFactory<out Any>>, ItemSpan>,  // For compatibility reasons, the old parameter names are still maintained here
    ) : super(context, spanCount, orientation, reverseLayout) {
        this.itemSpanByPositionSparseArray = null
        this.itemSpanByItemTypeSparseArray = null
        this.itemSpanByItemFactoryMap = gridLayoutItemSpanMap.takeIf { it.isNotEmpty() }
            ?.let { map ->
                ArrayMap<Class<out ItemFactory<out Any>>, ItemSpan>().apply {
                    map.entries.forEach { mapItem ->
                        put(mapItem.key.java, mapItem.value)
                    }
                }
            }
        super.setSpanSizeLookup(spanSizeLookup)
    }


    /**
     * Creates a vertical GridLayoutManager
     *
     * @param context Current context, will be used to access resources.
     * @param spanCount The number of columns in the grid
     * @param itemSpanByPositionMap Map of spanSize corresponding to position
     * @param itemSpanByItemTypeMap Map of spanSize corresponding to itemType
     * @param itemSpanByItemFactoryMap Map of spanSize corresponding to [ItemFactory]
     */
    constructor(
        context: Context,
        spanCount: Int,
        itemSpanByPositionMap: Map<Int, ItemSpan>?,
        itemSpanByItemTypeMap: Map<Int, ItemSpan>?,
        itemSpanByItemFactoryMap: Map<KClass<out ItemFactory<out Any>>, ItemSpan>?
    ) : super(context, spanCount) {
        this.itemSpanByPositionSparseArray = itemSpanByPositionMap?.takeIf { it.isNotEmpty() }
            ?.let { map ->
                SparseArrayCompat<ItemSpan>().apply {
                    map.entries.forEach { mapItem ->
                        put(mapItem.key, mapItem.value)
                    }
                }
            }
        this.itemSpanByItemTypeSparseArray = itemSpanByItemTypeMap?.takeIf { it.isNotEmpty() }
            ?.let { map ->
                SparseArrayCompat<ItemSpan>().apply {
                    map.entries.forEach { mapItem ->
                        put(mapItem.key, mapItem.value)
                    }
                }
            }
        this.itemSpanByItemFactoryMap = itemSpanByItemFactoryMap?.takeIf { it.isNotEmpty() }
            ?.let { map ->
                ArrayMap<Class<out ItemFactory<out Any>>, ItemSpan>().apply {
                    map.entries.forEach { mapItem ->
                        put(mapItem.key.java, mapItem.value)
                    }
                }
            }
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
        gridLayoutItemSpanMap: Map<KClass<out ItemFactory<out Any>>, ItemSpan>  // For compatibility reasons, the old parameter names are still maintained here
    ) : super(context, spanCount) {
        this.itemSpanByPositionSparseArray = null
        this.itemSpanByItemTypeSparseArray = null
        this.itemSpanByItemFactoryMap = gridLayoutItemSpanMap.takeIf { it.isNotEmpty() }
            ?.map { it.key.java to it.value }?.toMap()
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
        val itemSpanByPositionSparseArray = itemSpanByPositionSparseArray
        if (itemSpanByPositionSparseArray != null) {
            val itemSpan = itemSpanByPositionSparseArray[position]
            if (itemSpan != null) {
                return if (itemSpan.isFullSpan()) {
                    spanCount
                } else {
                    itemSpan.size.coerceAtLeast(1).coerceAtMost(spanCount)
                }
            }
        }

        val itemSpanByItemTypeSparseArray = itemSpanByItemTypeSparseArray
        val itemSpanByItemFactoryMap = itemSpanByItemFactoryMap
        val adapter = recyclerView?.adapter
        if ((itemSpanByItemTypeSparseArray != null || itemSpanByItemFactoryMap != null)
            && adapter != null
            && position >= 0
            && position < adapter.itemCount
        ) {
            val (localAdapter, localPosition) =
                concatAdapterLocalHelper.findLocalAdapterAndPosition(adapter, position)

            if (itemSpanByItemTypeSparseArray != null) {
                val itemType = localAdapter.getItemViewType(localPosition)
                val itemSpan = itemSpanByItemTypeSparseArray[itemType]
                if (itemSpan != null) {
                    return if (itemSpan.isFullSpan()) {
                        spanCount
                    } else {
                        itemSpan.size.coerceAtLeast(1).coerceAtMost(spanCount)
                    }
                }
            }

            if (itemSpanByItemFactoryMap != null) {
                val itemFactory = if (localAdapter is AssemblyAdapter<*, *>) {
                    localAdapter.getItemFactoryByPosition(localPosition) as ItemFactory<Any>
                } else {
                    throw IllegalArgumentException("RecyclerView.adapter must be ConcatAdapter or implement the interface AssemblyAdapter: ${adapter.javaClass.name}")
                }
                val itemSpan = itemSpanByItemFactoryMap[itemFactory.javaClass]
                if (itemSpan != null) {
                    return if (itemSpan.isFullSpan()) {
                        spanCount
                    } else {
                        itemSpan.size.coerceAtLeast(1).coerceAtMost(spanCount)
                    }
                }
            }
        }

        return 1
    }


    class Builder(val context: Context) {

        private var attrs: AttributeSet? = null
        private var defStyleAttr: Int? = null
        private var defStyleRes: Int? = null

        private var spanCount: Int? = null
        private var orientation: Int? = null
        private var reverseLayout: Boolean? = null

        private var itemSpanByPositionMap: MutableMap<Int, ItemSpan>? = null
        private var itemSpanByItemTypeMap: MutableMap<Int, ItemSpan>? = null
        private var itemSpanByItemFactoryMap: MutableMap<KClass<out ItemFactory<out Any>>, ItemSpan>? =
            null

        constructor(
            context: Context,
            attrs: AttributeSet?,
            defStyleAttr: Int,
            defStyleRes: Int
        ) : this(context) {
            this.attrs = attrs
            this.defStyleAttr = defStyleAttr
            this.defStyleRes = defStyleRes
        }

        constructor(
            context: Context,
            spanCount: Int,
            orientation: Int? = null,
            reverseLayout: Boolean? = null
        ) : this(context) {
            this.spanCount = spanCount
            this.orientation = orientation
            this.reverseLayout = reverseLayout
        }

        fun build(): AssemblyGridLayoutManager {
            val defStyleAttr = defStyleAttr
            val defStyleRes = defStyleRes
            val spanCount = spanCount
            val orientation = orientation
            val reverseLayout = reverseLayout
            return if (defStyleAttr != null && defStyleRes != null) {
                AssemblyGridLayoutManager(
                    context,
                    attrs,
                    defStyleAttr,
                    defStyleRes,
                    itemSpanByPositionMap,
                    itemSpanByItemTypeMap,
                    itemSpanByItemFactoryMap
                )
            } else if (spanCount != null) {
                AssemblyGridLayoutManager(
                    context,
                    spanCount,
                    orientation ?: RecyclerView.VERTICAL,
                    reverseLayout ?: false,
                    itemSpanByPositionMap,
                    itemSpanByItemTypeMap,
                    itemSpanByItemFactoryMap
                )
            } else {
                throw  IllegalArgumentException("Unable to create AssemblyGridLayoutManager")
            }
        }

        fun itemSpanByPosition(position: Int, itemSpan: ItemSpan): Builder {
            (itemSpanByPositionMap ?: ArrayMap<Int, ItemSpan>().apply {
                this@Builder.itemSpanByPositionMap = this
            })[position] = itemSpan
            return this
        }

        fun itemSpanByPosition(map: Map<Int, ItemSpan>): Builder {
            (itemSpanByPositionMap ?: ArrayMap<Int, ItemSpan>().apply {
                this@Builder.itemSpanByPositionMap = this
            }).putAll(map)
            return this
        }

        fun itemSpanByPosition(vararg pair: Pair<Int, ItemSpan>): Builder {
            (itemSpanByPositionMap ?: ArrayMap<Int, ItemSpan>().apply {
                this@Builder.itemSpanByPositionMap = this
            }).apply {
                pair.forEach {
                    put(it.first, it.second)
                }
            }
            return this
        }

        fun itemSpanByItemType(itemType: Int, itemSpan: ItemSpan): Builder {
            (itemSpanByItemTypeMap ?: ArrayMap<Int, ItemSpan>().apply {
                this@Builder.itemSpanByItemTypeMap = this
            })[itemType] = itemSpan
            return this
        }

        fun itemSpanByItemType(map: Map<Int, ItemSpan>): Builder {
            (itemSpanByItemTypeMap ?: ArrayMap<Int, ItemSpan>().apply {
                this@Builder.itemSpanByItemTypeMap = this
            }).putAll(map)
            return this
        }

        fun itemSpanByItemType(vararg pair: Pair<Int, ItemSpan>): Builder {
            (itemSpanByItemTypeMap ?: ArrayMap<Int, ItemSpan>().apply {
                this@Builder.itemSpanByItemTypeMap = this
            }).apply {
                pair.forEach {
                    put(it.first, it.second)
                }
            }
            return this
        }

        fun itemSpanByItemFactory(
            itemFactory: KClass<out ItemFactory<out Any>>,
            itemSpan: ItemSpan
        ): Builder {
            (itemSpanByItemFactoryMap ?: ArrayMap<KClass<out ItemFactory<out Any>>, ItemSpan>()
                .apply {
                    this@Builder.itemSpanByItemFactoryMap = this
                })[itemFactory] = itemSpan
            return this
        }

        fun itemSpanByItemFactory(map: Map<KClass<out ItemFactory<out Any>>, ItemSpan>): Builder {
            (itemSpanByItemFactoryMap ?: ArrayMap<KClass<out ItemFactory<out Any>>, ItemSpan>()
                .apply {
                    this@Builder.itemSpanByItemFactoryMap = this
                }).putAll(map)
            return this
        }

        fun itemSpanByItemFactory(vararg pair: Pair<KClass<out ItemFactory<out Any>>, ItemSpan>): Builder {
            (itemSpanByItemFactoryMap ?: ArrayMap<KClass<out ItemFactory<out Any>>, ItemSpan>()
                .apply { this@Builder.itemSpanByItemFactoryMap = this })
                .apply {
                    pair.forEach {
                        put(it.first, it.second)
                    }
                }
            return this
        }
    }
}


fun Context.newAssemblyGridLayoutManager(
    attrs: AttributeSet?,
    defStyleAttr: Int,
    defStyleRes: Int,
    block: (AssemblyGridLayoutManager.Builder.() -> Unit)? = null
): AssemblyGridLayoutManager {
    return AssemblyGridLayoutManager
        .Builder(this, attrs, defStyleAttr, defStyleRes).apply {
            block?.invoke(this)
        }.build()
}

fun RecyclerView.newAssemblyGridLayoutManager(
    attrs: AttributeSet?,
    defStyleAttr: Int,
    defStyleRes: Int,
    block: (AssemblyGridLayoutManager.Builder.() -> Unit)? = null
): AssemblyGridLayoutManager {
    return AssemblyGridLayoutManager
        .Builder(context, attrs, defStyleAttr, defStyleRes).apply {
            block?.invoke(this)
        }.build()
}

fun RecyclerView.setupAssemblyGridLayoutManager(
    attrs: AttributeSet?,
    defStyleAttr: Int,
    defStyleRes: Int,
    block: (AssemblyGridLayoutManager.Builder.() -> Unit)? = null
) {
    layoutManager = AssemblyGridLayoutManager
        .Builder(context, attrs, defStyleAttr, defStyleRes).apply {
            block?.invoke(this)
        }.build()
}

fun Context.newAssemblyGridLayoutManager(
    spanCount: Int,
    orientation: Int? = null,
    reverseLayout: Boolean? = null,
    block: (AssemblyGridLayoutManager.Builder.() -> Unit)? = null
): AssemblyGridLayoutManager {
    return AssemblyGridLayoutManager
        .Builder(this, spanCount, orientation, reverseLayout).apply {
            block?.invoke(this)
        }.build()
}

fun RecyclerView.newAssemblyGridLayoutManager(
    spanCount: Int,
    orientation: Int? = null,
    reverseLayout: Boolean? = null,
    block: (AssemblyGridLayoutManager.Builder.() -> Unit)? = null
): AssemblyGridLayoutManager {
    return AssemblyGridLayoutManager
        .Builder(context, spanCount, orientation, reverseLayout).apply {
            block?.invoke(this)
        }.build()
}

fun RecyclerView.setupAssemblyGridLayoutManager(
    spanCount: Int,
    orientation: Int? = null,
    reverseLayout: Boolean? = null,
    block: (AssemblyGridLayoutManager.Builder.() -> Unit)? = null
) {
    layoutManager = AssemblyGridLayoutManager
        .Builder(context, spanCount, orientation, reverseLayout).apply {
            block?.invoke(this)
        }.build()
}