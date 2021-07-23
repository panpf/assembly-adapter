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
import android.view.View
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.github.panpf.assemblyadapter.ItemFactory
import com.github.panpf.assemblyadapter.recycler.internal.FullSpanStaggeredGridLayoutManager
import kotlin.reflect.KClass

/**
 * An implementation of [AssemblyStaggeredGridLayoutManager]. Set the full span of [AssemblyStaggeredGridLayoutManager] according to [ItemFactory] as the identifier
 */
class AssemblyStaggeredGridLayoutManager : StaggeredGridLayoutManager,
    FullSpanStaggeredGridLayoutManager {

    private val fullSpanItemFactoryList: List<Class<out ItemFactory<*>>>

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
        fullSpanItemFactoryList: List<KClass<out ItemFactory<*>>>
    ) : super(context, attrs, defStyleAttr, defStyleRes) {
        this.fullSpanItemFactoryList = fullSpanItemFactoryList.map { it.java }
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
        fullSpanItemFactoryList: List<KClass<out ItemFactory<*>>>
    ) : super(spanCount, orientation) {
        this.fullSpanItemFactoryList = fullSpanItemFactoryList.map { it.java }
    }

    /**
     * Creates a vertical StaggeredGridLayoutManager with given parameters.
     *
     * @param spanCount spanCount is number of columns.
     * @param fullSpanItemFactoryList The [ItemFactory] collection that needs to be set to fullSpan
     */
    constructor(
        spanCount: Int,
        fullSpanItemFactoryList: List<KClass<out ItemFactory<*>>>
    ) : super(spanCount, VERTICAL) {
        this.fullSpanItemFactoryList = fullSpanItemFactoryList.map { it.java }
    }

    override fun setFullSpan(itemView: View, itemFactory: ItemFactory<*>) {
        val layoutParams = itemView.layoutParams
        if (layoutParams is LayoutParams && fullSpanItemFactoryList.contains(itemFactory.javaClass)) {
            layoutParams.isFullSpan = true
        }
    }
}