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
 * An implementation of [AssemblyStaggeredGridLayoutManager]. Set the full span of [AssemblyStaggeredGridLayoutManager] according to [ItemFactory] as the identifier
 */
class AssemblyStaggeredGridLayoutManager : StaggeredGridLayoutManager, FullSpanSupportByPosition {

    private val fullSpanPositionSparseArray: SparseBooleanArray?
    private val fullSpanItemFactoryMap: Map<Class<out ItemFactory<out Any>>, Boolean>?
    private val concatAdapterLocalHelper = ConcatAdapterLocalHelper()

    private var recyclerView: RecyclerView? = null

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
        fullSpanItemPositionList: List<Int>?,
        fullSpanItemFactoryList: List<KClass<out ItemFactory<out Any>>>?
    ) : super(context, attrs, defStyleAttr, defStyleRes) {
        this.fullSpanPositionSparseArray = fullSpanItemPositionList?.let { list ->
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
     * @param fullSpanItemFactoryList The [ItemFactory] collection that needs to be set to fullSpan
     */
    constructor(
        spanCount: Int,
        orientation: Int,
        fullSpanItemPositionList: List<Int>?,
        fullSpanItemFactoryList: List<KClass<out ItemFactory<out Any>>>?
    ) : super(spanCount, orientation) {
        this.fullSpanPositionSparseArray = fullSpanItemPositionList?.let { list ->
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
     * @param fullSpanItemFactoryList The [ItemFactory] collection that needs to be set to fullSpan
     */
    constructor(
        spanCount: Int,
        fullSpanItemPositionList: List<Int>?,
        fullSpanItemFactoryList: List<KClass<out ItemFactory<out Any>>>?
    ) : super(spanCount, VERTICAL) {
        this.fullSpanPositionSparseArray = fullSpanItemPositionList?.let { list ->
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
        this.fullSpanItemFactoryMap = fullSpanItemFactoryList?.let { list ->
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

        val fullSpanItemFactoryMap = fullSpanItemFactoryMap
        val adapter = recyclerView?.adapter
        if (fullSpanItemFactoryMap != null
            && adapter != null
            && position >= 0
            && position < adapter.itemCount
        ) {
            val itemFactory = findItemFactory(adapter, position)
            if (fullSpanItemFactoryMap[itemFactory.javaClass] == true) {
                return true
            }
        }

        return false
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

    override fun onAttachedToWindow(view: RecyclerView) {
        super.onAttachedToWindow(view)
        recyclerView = view
        concatAdapterLocalHelper.reset()
    }

    override fun onDetachedFromWindow(view: RecyclerView?, recycler: RecyclerView.Recycler?) {
        super.onDetachedFromWindow(view, recycler)
        concatAdapterLocalHelper.reset()
    }
}