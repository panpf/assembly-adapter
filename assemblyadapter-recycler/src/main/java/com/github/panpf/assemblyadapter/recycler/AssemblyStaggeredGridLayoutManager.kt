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

    private val fullSpanPositionSparseArray: SparseBooleanArray?
    private val fullSpanItemTypeSparseArray: SparseBooleanArray?
    private val fullSpanItemFactoryMap: Map<Class<out ItemFactory<out Any>>, Boolean>?
    private val concatAdapterLocalHelper = ConcatAdapterLocalHelper()

    private var recyclerView: RecyclerView? = null

    /**
     * Constructor used when layout manager is set in XML by RecyclerView attribute
     * "layoutManager". Defaults to single column and vertical.
     *
     * @param fullSpanItemPositionList The position collection that needs to be set to fullSpan
     * @param fullSpanItemTypeList The itemType collection that needs to be set to fullSpan
     * @param fullSpanItemFactoryList The [ItemFactory] collection that needs to be set to fullSpan
     */
    constructor(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int,
        fullSpanItemPositionList: List<Int>?,
        fullSpanItemTypeList: List<Int>?,
        fullSpanItemFactoryList: List<KClass<out ItemFactory<out Any>>>?
    ) : super(context, attrs, defStyleAttr, defStyleRes) {
        this.fullSpanPositionSparseArray = fullSpanItemPositionList?.let { list ->
            SparseBooleanArray().apply {
                list.forEach { position ->
                    put(position, true)
                }
            }
        }
        this.fullSpanItemTypeSparseArray = fullSpanItemTypeList?.let { list ->
            SparseBooleanArray().apply {
                list.forEach { position ->
                    put(position, true)
                }
            }
        }
        this.fullSpanItemFactoryMap = fullSpanItemFactoryList?.let { list ->
            HashMap<Class<out ItemFactory<out Any>>, Boolean>().apply {
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
        fullSpanItemFactoryList: List<KClass<out ItemFactory<out Any>>>
    ) : super(context, attrs, defStyleAttr, defStyleRes) {
        this.fullSpanPositionSparseArray = null
        this.fullSpanItemTypeSparseArray = null
        this.fullSpanItemFactoryMap = fullSpanItemFactoryList.let { list ->
            HashMap<Class<out ItemFactory<out Any>>, Boolean>().apply {
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
     * @param fullSpanItemPositionList The position collection that needs to be set to fullSpan
     * @param fullSpanItemTypeList The itemType collection that needs to be set to fullSpan
     * @param fullSpanItemFactoryList The [ItemFactory] collection that needs to be set to fullSpan
     */
    constructor(
        spanCount: Int,
        orientation: Int,
        fullSpanItemPositionList: List<Int>?,
        fullSpanItemTypeList: List<Int>?,
        fullSpanItemFactoryList: List<KClass<out ItemFactory<out Any>>>?
    ) : super(spanCount, orientation) {
        this.fullSpanPositionSparseArray = fullSpanItemPositionList?.let { list ->
            SparseBooleanArray().apply {
                list.forEach { position ->
                    put(position, true)
                }
            }
        }
        this.fullSpanItemTypeSparseArray = fullSpanItemTypeList?.let { list ->
            SparseBooleanArray().apply {
                list.forEach { position ->
                    put(position, true)
                }
            }
        }
        this.fullSpanItemFactoryMap = fullSpanItemFactoryList?.let { list ->
            HashMap<Class<out ItemFactory<out Any>>, Boolean>().apply {
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
        fullSpanItemFactoryList: List<KClass<out ItemFactory<out Any>>>
    ) : super(spanCount, orientation) {
        this.fullSpanPositionSparseArray = null
        this.fullSpanItemTypeSparseArray = null
        this.fullSpanItemFactoryMap = fullSpanItemFactoryList.let { list ->
            HashMap<Class<out ItemFactory<out Any>>, Boolean>().apply {
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
     * @param fullSpanItemPositionList The position collection that needs to be set to fullSpan
     * @param fullSpanItemTypeList The itemType collection that needs to be set to fullSpan
     * @param fullSpanItemFactoryList The [ItemFactory] collection that needs to be set to fullSpan
     */
    constructor(
        spanCount: Int,
        fullSpanItemPositionList: List<Int>?,
        fullSpanItemTypeList: List<Int>?,
        fullSpanItemFactoryList: List<KClass<out ItemFactory<out Any>>>?
    ) : super(spanCount, VERTICAL) {
        this.fullSpanPositionSparseArray = fullSpanItemPositionList?.let { list ->
            SparseBooleanArray().apply {
                list.forEach { position ->
                    put(position, true)
                }
            }
        }
        this.fullSpanItemTypeSparseArray = fullSpanItemTypeList?.let { list ->
            SparseBooleanArray().apply {
                list.forEach { position ->
                    put(position, true)
                }
            }
        }
        this.fullSpanItemFactoryMap = fullSpanItemFactoryList?.let { list ->
            HashMap<Class<out ItemFactory<out Any>>, Boolean>().apply {
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
        fullSpanItemFactoryList: List<KClass<out ItemFactory<out Any>>>
    ) : super(spanCount, VERTICAL) {
        this.fullSpanPositionSparseArray = null
        this.fullSpanItemTypeSparseArray = null
        this.fullSpanItemFactoryMap = fullSpanItemFactoryList.let { list ->
            HashMap<Class<out ItemFactory<out Any>>, Boolean>().apply {
                list.forEach {
                    put(it.java, true)
                }
            }
        }
    }

    override fun isFullSpanByPosition(position: Int): Boolean {
        if (fullSpanPositionSparseArray?.get(position) == true) {
            return true
        }

        val fullSpanItemTypeSparseArray = fullSpanItemTypeSparseArray
        val fullSpanItemFactoryMap = fullSpanItemFactoryMap
        val adapter = recyclerView?.adapter
        if ((fullSpanItemTypeSparseArray != null || fullSpanItemFactoryMap != null)
            && adapter != null
            && position >= 0
            && position < adapter.itemCount
        ) {
            val (localAdapter, localPosition) =
                concatAdapterLocalHelper.findLocalAdapterAndPosition(adapter, position)

            if (fullSpanItemTypeSparseArray != null) {
                val itemType = localAdapter.getItemViewType(localPosition)
                if (fullSpanItemTypeSparseArray.get(itemType)) {
                    return true
                }
            }

            if (fullSpanItemFactoryMap != null) {
                val itemFactory = if (localAdapter is AssemblyAdapter<*, *>) {
                    localAdapter.getItemFactoryByPosition(localPosition) as ItemFactory<Any>
                } else {
                    throw IllegalArgumentException("RecyclerView.adapter must be ConcatAdapter or implement the interface AssemblyAdapter: ${adapter.javaClass.name}")
                }
                if (fullSpanItemFactoryMap[itemFactory.javaClass] == true) {
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
        private var stackFromEnd: Boolean? = null

        private var itemSpanByPositionList: List<Int>? = null
        private var itemSpanByItemTypeList: List<Int>? = null
        private var itemSpanByItemFactoryList: List<KClass<out ItemFactory<out Any>>>? =
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
        ) {
            this.spanCount = spanCount
            this.orientation = orientation
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
                    itemSpanByPositionList,
                    itemSpanByItemTypeList,
                    itemSpanByItemFactoryList
                )
            } else if (spanCount != null && orientation != null) {
                AssemblyStaggeredGridLayoutManager(
                    spanCount,
                    orientation,
                    itemSpanByPositionList,
                    itemSpanByItemTypeList,
                    itemSpanByItemFactoryList
                )
            } else if (spanCount != null) {
                AssemblyStaggeredGridLayoutManager(
                    spanCount,
                    itemSpanByPositionList,
                    itemSpanByItemTypeList,
                    itemSpanByItemFactoryList
                )
            } else {
                throw  IllegalArgumentException("Unable to create AssemblyStaggeredGridLayoutManager")
            }.apply {
                val orientation1 = this@Builder.orientation
                if (orientation1 != null) {
                    this.orientation = orientation1
                }
                val reverseLayout1 = this@Builder.reverseLayout
                if (reverseLayout1 != null) {
                    this.reverseLayout = reverseLayout1
                }
            }
        }

        fun spanCount(attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int): Builder {
            this.attrs = attrs
            this.defStyleAttr = defStyleAttr
            this.defStyleRes = defStyleRes
            return this
        }

        fun spanCount(spanCount: Int): Builder {
            this.spanCount = spanCount
            return this
        }

        fun orientation(orientation: Int): Builder {
            this.orientation = orientation
            return this
        }

        fun orientation(reverseLayout: Boolean): Builder {
            this.reverseLayout = reverseLayout
            return this
        }

        fun stackFromEnd(stackFromEnd: Boolean): Builder {
            this.stackFromEnd = stackFromEnd
            return this
        }

        fun itemSpanByPosition(vararg positions: Int): Builder {
            itemSpanByPositionList ?: ArrayList<Int>().apply {
                this@Builder.itemSpanByPositionList = this
            }.apply {
                positions.forEach {
                    add(it)
                }
            }
            return this
        }

        fun itemSpanByItemType(vararg itemTypes: Int): Builder {
            itemSpanByItemTypeList ?: ArrayList<Int>().apply {
                this@Builder.itemSpanByItemTypeList = this
            }.apply {
                itemTypes.forEach {
                    add(it)
                }
            }
            return this
        }

        fun itemSpanByItemFactory(vararg itemFactoryArray: KClass<out ItemFactory<out Any>>): Builder {
            itemSpanByItemFactoryList
                ?: ArrayList<KClass<out ItemFactory<out Any>>>().apply {
                    this@Builder.itemSpanByItemFactoryList = this
                }.apply {
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
    block: AssemblyStaggeredGridLayoutManager.Builder.() -> Unit
): AssemblyStaggeredGridLayoutManager {
    return AssemblyStaggeredGridLayoutManager
        .Builder(this, attrs, defStyleAttr, defStyleRes)
        .apply(block)
        .build()
}

fun RecyclerView.newAssemblyStaggeredGridLayoutManager(
    attrs: AttributeSet?,
    defStyleAttr: Int,
    defStyleRes: Int,
    block: AssemblyStaggeredGridLayoutManager.Builder.() -> Unit
): AssemblyStaggeredGridLayoutManager {
    return AssemblyStaggeredGridLayoutManager
        .Builder(context, attrs, defStyleAttr, defStyleRes)
        .apply(block)
        .build()
}

fun RecyclerView.setupAssemblyStaggeredGridLayoutManager(
    attrs: AttributeSet?,
    defStyleAttr: Int,
    defStyleRes: Int,
    block: AssemblyStaggeredGridLayoutManager.Builder.() -> Unit
) {
    layoutManager = AssemblyStaggeredGridLayoutManager
        .Builder(context, attrs, defStyleAttr, defStyleRes)
        .apply(block)
        .build()
}

fun newAssemblyStaggeredGridLayoutManager(
    spanCount: Int,
    orientation: Int? = null,
    block: AssemblyStaggeredGridLayoutManager.Builder.() -> Unit
): AssemblyStaggeredGridLayoutManager {
    return AssemblyStaggeredGridLayoutManager
        .Builder(spanCount, orientation)
        .apply(block)
        .build()
}

fun RecyclerView.setupAssemblyStaggeredGridLayoutManager(
    spanCount: Int,
    orientation: Int? = null,
    block: AssemblyStaggeredGridLayoutManager.Builder.() -> Unit
) {
    layoutManager = AssemblyStaggeredGridLayoutManager
        .Builder(spanCount, orientation)
        .apply(block)
        .build()
}