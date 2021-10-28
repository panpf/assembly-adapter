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
import android.util.SparseBooleanArray
import androidx.collection.ArrayMap
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.github.panpf.assemblyadapter.AssemblyAdapter
import com.github.panpf.assemblyadapter.ItemFactory
import com.github.panpf.assemblyadapter.recycler.internal.FullSpanSupportByPosition
import kotlin.reflect.KClass

/**
 * An implementation of [AssemblyStaggeredGridLayoutManager]. Set the full span of [AssemblyStaggeredGridLayoutManager] according to position and itemType and [ItemFactory] as the identifier
 */
class AssemblyStaggeredGridLayoutManager : StaggeredGridLayoutManager, FullSpanSupportByPosition {

    private val fullSpanByPositionSparseArray: SparseBooleanArray?
    private val fullSpanByItemTypeSparseArray: SparseBooleanArray?
    private val fullSpanByItemFactoryMap: Map<Class<out ItemFactory<out Any>>, Boolean>?
    private val concatAdapterLocalHelper = ConcatAdapterLocalHelper()

    private var recyclerView: RecyclerView? = null

    /**
     * Constructor used when layout manager is set in XML by RecyclerView attribute
     * "layoutManager". Defaults to single column and vertical.
     *
     * @param fullSpanByItemPositionList The position collection that needs to be set to fullSpan
     * @param fullSpanByItemTypeList The itemType collection that needs to be set to fullSpan
     * @param fullSpanByItemFactoryList The [ItemFactory] collection that needs to be set to fullSpan
     */
    constructor(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int,
        fullSpanByItemPositionList: List<Int>?,
        fullSpanByItemTypeList: List<Int>?,
        fullSpanByItemFactoryList: List<KClass<out ItemFactory<out Any>>>?
    ) : super(context, attrs, defStyleAttr, defStyleRes) {
        this.fullSpanByPositionSparseArray = fullSpanByItemPositionList?.takeIf { it.isNotEmpty() }
            ?.let { list ->
                SparseBooleanArray().apply {
                    list.forEach { position ->
                        put(position, true)
                    }
                }
            }
        this.fullSpanByItemTypeSparseArray = fullSpanByItemTypeList?.takeIf { it.isNotEmpty() }
            ?.let { list ->
                SparseBooleanArray().apply {
                    list.forEach { position ->
                        put(position, true)
                    }
                }
            }
        this.fullSpanByItemFactoryMap = fullSpanByItemFactoryList?.takeIf { it.isNotEmpty() }
            ?.let { list ->
                ArrayMap<Class<out ItemFactory<out Any>>, Boolean>().apply {
                    list.forEach {
                        put(it.java, true)
                    }
                }
            }
    }

    /**
     * Constructor used when layout manager is set in XML by RecyclerView attribute
     * "layoutManager". Defaults to single column and vertical.
     *
     * @param fullSpanItemFactoryList The [ItemFactory] collection that needs to be set to fullSpan
     */
    constructor(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int,
        fullSpanItemFactoryList: List<KClass<out ItemFactory<out Any>>>  // For compatibility reasons, the old parameter names are still maintained here
    ) : super(context, attrs, defStyleAttr, defStyleRes) {
        this.fullSpanByPositionSparseArray = null
        this.fullSpanByItemTypeSparseArray = null
        this.fullSpanByItemFactoryMap = fullSpanItemFactoryList.takeIf { it.isNotEmpty() }
            ?.let { list ->
                ArrayMap<Class<out ItemFactory<out Any>>, Boolean>().apply {
                    list.forEach {
                        put(it.java, true)
                    }
                }
            }
    }

    /**
     * Creates a StaggeredGridLayoutManager with given parameters.
     *
     * @param spanCount If [orientation] is vertical, [spanCount] is number of columns. If [orientation] is horizontal, [spanCount] is number of rows.
     * @param orientation [StaggeredGridLayoutManager.VERTICAL] or [StaggeredGridLayoutManager.HORIZONTAL]
     * @param fullSpanByItemPositionList The position collection that needs to be set to fullSpan
     * @param fullSpanByItemTypeList The itemType collection that needs to be set to fullSpan
     * @param fullSpanByItemFactoryList The [ItemFactory] collection that needs to be set to fullSpan
     */
    constructor(
        spanCount: Int,
        orientation: Int,
        fullSpanByItemPositionList: List<Int>?,
        fullSpanByItemTypeList: List<Int>?,
        fullSpanByItemFactoryList: List<KClass<out ItemFactory<out Any>>>?
    ) : super(spanCount, orientation) {
        this.fullSpanByPositionSparseArray = fullSpanByItemPositionList?.takeIf { it.isNotEmpty() }
            ?.let { list ->
                SparseBooleanArray().apply {
                    list.forEach { position ->
                        put(position, true)
                    }
                }
            }
        this.fullSpanByItemTypeSparseArray = fullSpanByItemTypeList?.takeIf { it.isNotEmpty() }
            ?.let { list ->
                SparseBooleanArray().apply {
                    list.forEach { position ->
                        put(position, true)
                    }
                }
            }
        this.fullSpanByItemFactoryMap = fullSpanByItemFactoryList?.takeIf { it.isNotEmpty() }
            ?.let { list ->
                ArrayMap<Class<out ItemFactory<out Any>>, Boolean>().apply {
                    list.forEach {
                        put(it.java, true)
                    }
                }
            }
    }

    /**
     * Creates a StaggeredGridLayoutManager with given parameters.
     *
     * @param spanCount If [orientation] is vertical, [spanCount] is number of columns. If [orientation] is horizontal, [spanCount] is number of rows.
     * @param orientation [StaggeredGridLayoutManager.VERTICAL] or [StaggeredGridLayoutManager.HORIZONTAL]
     * @param fullSpanItemFactoryList The [ItemFactory] collection that needs to be set to fullSpan
     */
    constructor(
        spanCount: Int,
        orientation: Int,
        fullSpanItemFactoryList: List<KClass<out ItemFactory<out Any>>>  // For compatibility reasons, the old parameter names are still maintained here
    ) : super(spanCount, orientation) {
        this.fullSpanByPositionSparseArray = null
        this.fullSpanByItemTypeSparseArray = null
        this.fullSpanByItemFactoryMap = fullSpanItemFactoryList.takeIf { it.isNotEmpty() }
            ?.let { list ->
                ArrayMap<Class<out ItemFactory<out Any>>, Boolean>().apply {
                    list.forEach {
                        put(it.java, true)
                    }
                }
            }
    }

    /**
     * Creates a vertical StaggeredGridLayoutManager with given parameters.
     *
     * @param spanCount spanCount is number of columns.
     * @param fullSpanByItemPositionList The position collection that needs to be set to fullSpan
     * @param fullSpanByItemTypeList The itemType collection that needs to be set to fullSpan
     * @param fullSpanByItemFactoryList The [ItemFactory] collection that needs to be set to fullSpan
     */
    constructor(
        spanCount: Int,
        fullSpanByItemPositionList: List<Int>?,
        fullSpanByItemTypeList: List<Int>?,
        fullSpanByItemFactoryList: List<KClass<out ItemFactory<out Any>>>?
    ) : super(spanCount, VERTICAL) {
        this.fullSpanByPositionSparseArray = fullSpanByItemPositionList?.takeIf { it.isNotEmpty() }
            ?.let { list ->
                SparseBooleanArray().apply {
                    list.forEach { position ->
                        put(position, true)
                    }
                }
            }
        this.fullSpanByItemTypeSparseArray = fullSpanByItemTypeList?.takeIf { it.isNotEmpty() }
            ?.let { list ->
                SparseBooleanArray().apply {
                    list.forEach { position ->
                        put(position, true)
                    }
                }
            }
        this.fullSpanByItemFactoryMap = fullSpanByItemFactoryList?.takeIf { it.isNotEmpty() }
            ?.let { list ->
                ArrayMap<Class<out ItemFactory<out Any>>, Boolean>().apply {
                    list.forEach {
                        put(it.java, true)
                    }
                }
            }
    }

    /**
     * Creates a vertical StaggeredGridLayoutManager with given parameters.
     *
     * @param spanCount spanCount is number of columns.
     * @param fullSpanItemFactoryList The [ItemFactory] collection that needs to be set to fullSpan
     */
    constructor(
        spanCount: Int,
        fullSpanItemFactoryList: List<KClass<out ItemFactory<out Any>>>  // For compatibility reasons, the old parameter names are still maintained here
    ) : super(spanCount, VERTICAL) {
        this.fullSpanByPositionSparseArray = null
        this.fullSpanByItemTypeSparseArray = null
        this.fullSpanByItemFactoryMap = fullSpanItemFactoryList.takeIf { it.isNotEmpty() }
            ?.let { list ->
                ArrayMap<Class<out ItemFactory<out Any>>, Boolean>().apply {
                    list.forEach {
                        put(it.java, true)
                    }
                }
            }
    }

    override fun isFullSpanByPosition(position: Int): Boolean {
        if (fullSpanByPositionSparseArray?.get(position) == true) {
            return true
        }

        val fullSpanByItemTypeSparseArray = fullSpanByItemTypeSparseArray
        val fullSpanByItemFactoryMap = fullSpanByItemFactoryMap
        val adapter = recyclerView?.adapter
        if ((fullSpanByItemTypeSparseArray != null || fullSpanByItemFactoryMap != null)
            && adapter != null
            && position >= 0
            && position < adapter.itemCount
        ) {
            val (localAdapter, localPosition) =
                concatAdapterLocalHelper.findLocalAdapterAndPosition(adapter, position)

            if (fullSpanByItemTypeSparseArray != null) {
                val itemType = localAdapter.getItemViewType(localPosition)
                if (fullSpanByItemTypeSparseArray.get(itemType)) {
                    return true
                }
            }

            if (fullSpanByItemFactoryMap != null) {
                val itemFactory = if (localAdapter is AssemblyAdapter<*, *>) {
                    localAdapter.getItemFactoryByPosition(localPosition) as ItemFactory<Any>
                } else {
                    throw IllegalArgumentException("RecyclerView.adapter must be ConcatAdapter or implement the interface AssemblyAdapter: ${adapter.javaClass.name}")
                }
                if (fullSpanByItemFactoryMap[itemFactory.javaClass] == true) {
                    return true
                }
            }
        }

        return false
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


    class Builder {
        private var context: Context? = null
        private var attrs: AttributeSet? = null
        private var defStyleAttr: Int? = null
        private var defStyleRes: Int? = null

        private var spanCount: Int? = null
        private var orientation: Int? = null
        private var reverseLayout: Boolean? = null

        private var fullSpanByPositionList: MutableList<Int>? = null
        private var fullSpanByItemTypeList: MutableList<Int>? = null
        private var fullSpanByItemFactoryList: MutableList<KClass<out ItemFactory<out Any>>>? =
            null

        constructor(
            context: Context,
            attrs: AttributeSet?,
            defStyleAttr: Int,
            defStyleRes: Int
        ) {
            this.context = context
            this.attrs = attrs
            this.defStyleAttr = defStyleAttr
            this.defStyleRes = defStyleRes
        }

        constructor(
            spanCount: Int,
            orientation: Int? = null,
            reverseLayout: Boolean? = null,
        ) {
            this.spanCount = spanCount
            this.orientation = orientation
            this.reverseLayout = reverseLayout
        }

        fun build(): AssemblyStaggeredGridLayoutManager {
            val context = context
            val defStyleAttr = defStyleAttr
            val defStyleRes = defStyleRes
            val spanCount = spanCount
            val orientation = orientation
            return if (context != null && defStyleAttr != null && defStyleRes != null) {
                AssemblyStaggeredGridLayoutManager(
                    context,
                    attrs,
                    defStyleAttr,
                    defStyleRes,
                    fullSpanByPositionList,
                    fullSpanByItemTypeList,
                    fullSpanByItemFactoryList
                )
            } else if (spanCount != null) {
                AssemblyStaggeredGridLayoutManager(
                    spanCount,
                    orientation ?: RecyclerView.VERTICAL,
                    fullSpanByPositionList,
                    fullSpanByItemTypeList,
                    fullSpanByItemFactoryList
                )
            } else {
                throw  IllegalArgumentException("Unable to create AssemblyStaggeredGridLayoutManager")
            }.apply {
                val reverseLayout1 = this@Builder.reverseLayout
                if (reverseLayout1 != null) {
                    this.reverseLayout = reverseLayout1
                }
            }
        }

        fun fullSpanByPosition(vararg positions: Int): Builder {
            (fullSpanByPositionList ?: ArrayList<Int>().apply {
                this@Builder.fullSpanByPositionList = this
            }).apply {
                positions.forEach {
                    add(it)
                }
            }
            return this
        }

        fun fullSpanByItemType(vararg itemTypes: Int): Builder {
            (fullSpanByItemTypeList ?: ArrayList<Int>().apply {
                this@Builder.fullSpanByItemTypeList = this
            }).apply {
                itemTypes.forEach {
                    add(it)
                }
            }
            return this
        }

        fun fullSpanByItemFactory(vararg itemFactoryArray: KClass<out ItemFactory<out Any>>): Builder {
            (fullSpanByItemFactoryList ?: ArrayList<KClass<out ItemFactory<out Any>>>().apply {
                this@Builder.fullSpanByItemFactoryList = this
            }).apply {
                itemFactoryArray.forEach {
                    add(it)
                }
            }
            return this
        }
    }
}


fun Context.newAssemblyStaggeredGridLayoutManager(
    attrs: AttributeSet?,
    defStyleAttr: Int,
    defStyleRes: Int,
    block: (AssemblyStaggeredGridLayoutManager.Builder.() -> Unit)? = null
): AssemblyStaggeredGridLayoutManager {
    return AssemblyStaggeredGridLayoutManager
        .Builder(this, attrs, defStyleAttr, defStyleRes).apply {
            block?.invoke(this)
        }.build()
}

fun RecyclerView.newAssemblyStaggeredGridLayoutManager(
    attrs: AttributeSet?,
    defStyleAttr: Int,
    defStyleRes: Int,
    block: (AssemblyStaggeredGridLayoutManager.Builder.() -> Unit)? = null
): AssemblyStaggeredGridLayoutManager {
    return AssemblyStaggeredGridLayoutManager
        .Builder(context, attrs, defStyleAttr, defStyleRes).apply {
            block?.invoke(this)
        }.build()
}

fun RecyclerView.setupAssemblyStaggeredGridLayoutManager(
    attrs: AttributeSet?,
    defStyleAttr: Int,
    defStyleRes: Int,
    block: (AssemblyStaggeredGridLayoutManager.Builder.() -> Unit)? = null
) {
    layoutManager = AssemblyStaggeredGridLayoutManager
        .Builder(context, attrs, defStyleAttr, defStyleRes).apply {
            block?.invoke(this)
        }.build()
}

fun newAssemblyStaggeredGridLayoutManager(
    spanCount: Int,
    orientation: Int? = null,
    reverseLayout: Boolean? = null,
    block: (AssemblyStaggeredGridLayoutManager.Builder.() -> Unit)? = null
): AssemblyStaggeredGridLayoutManager {
    return AssemblyStaggeredGridLayoutManager
        .Builder(spanCount, orientation, reverseLayout).apply {
            block?.invoke(this)
        }.build()
}

fun RecyclerView.setupAssemblyStaggeredGridLayoutManager(
    spanCount: Int,
    orientation: Int? = null,
    reverseLayout: Boolean? = null,
    block: (AssemblyStaggeredGridLayoutManager.Builder.() -> Unit)? = null
) {
    layoutManager = AssemblyStaggeredGridLayoutManager
        .Builder(spanCount, orientation, reverseLayout).apply {
            block?.invoke(this)
        }.build()
}