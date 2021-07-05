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
package com.github.panpf.assemblyadapter.pager

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.annotation.IdRes
import com.github.panpf.assemblyadapter.Matchable
import com.github.panpf.assemblyadapter.OnClickListener
import com.github.panpf.assemblyadapter.OnLongClickListener
import com.github.panpf.assemblyadapter.internal.ClickListenerStorage
import kotlin.reflect.KClass

/**
 * @see BindingPagerItemFactory
 * @see ViewPagerItemFactory
 */
abstract class PagerItemFactory<DATA : Any>(private val dataClass: KClass<DATA>) : Matchable {

    private var clickListenerStorage: ClickListenerStorage<DATA>? = null

    final override fun matchData(data: Any): Boolean {
        @Suppress("UNCHECKED_CAST")
        return dataClass.isInstance(data) && carefullyMatchData(data as DATA)
    }

    open fun carefullyMatchData(data: DATA): Boolean = true

    fun dispatchCreateItemView(
        context: Context,
        parent: ViewGroup,
        bindingAdapterPosition: Int,
        absoluteAdapterPosition: Int,
        data: DATA
    ): View {
        return createItemView(
            context, parent, bindingAdapterPosition, absoluteAdapterPosition, data
        ).apply {
            registerItemClickListener(this, bindingAdapterPosition, absoluteAdapterPosition, data)
        }
    }

    protected abstract fun createItemView(
        context: Context, parent: ViewGroup,
        bindingAdapterPosition: Int,
        absoluteAdapterPosition: Int,
        data: DATA
    ): View

    open fun setOnViewClickListener(
        @IdRes viewId: Int,
        onClickListener: OnClickListener<DATA>
    ): PagerItemFactory<DATA> {
        getClickListenerManagerOrCreate().add(viewId, onClickListener)
        return this
    }

    open fun setOnViewLongClickListener(
        @IdRes viewId: Int,
        onLongClickListener: OnLongClickListener<DATA>
    ): PagerItemFactory<DATA> {
        getClickListenerManagerOrCreate().add(viewId, onLongClickListener)
        return this
    }

    open fun setOnItemClickListener(onClickListener: OnClickListener<DATA>): PagerItemFactory<DATA> {
        getClickListenerManagerOrCreate().add(onClickListener)
        return this
    }

    open fun setOnItemLongClickListener(onLongClickListener: OnLongClickListener<DATA>): PagerItemFactory<DATA> {
        getClickListenerManagerOrCreate().add(onLongClickListener)
        return this
    }

    private fun getClickListenerManagerOrCreate(): ClickListenerStorage<DATA> {
        return (clickListenerStorage ?: (ClickListenerStorage<DATA>().apply {
            this@PagerItemFactory.clickListenerStorage = this
        }))
    }

    private fun registerItemClickListener(
        itemView: View, bindingAdapterPosition: Int, absoluteAdapterPosition: Int, data: DATA
    ) {
        val clickListenerManager = clickListenerStorage ?: return
        for (holder in clickListenerManager.holders) {
            if (holder is ClickListenerStorage.ClickListenerHolder<*>) {
                @Suppress("UNCHECKED_CAST")
                val clickListenerHolder = holder as ClickListenerStorage.ClickListenerHolder<DATA>
                val viewId = clickListenerHolder.viewId
                val targetView = if (viewId > 0) {
                    itemView.findViewById(viewId)
                        ?: throw IllegalArgumentException("Not found click bind target view by id $viewId")
                } else {
                    itemView
                }
                targetView.setOnClickListener { view ->
                    clickListenerHolder.listener.onClick(
                        view.context, view, bindingAdapterPosition, absoluteAdapterPosition, data
                    )
                }
            } else if (holder is ClickListenerStorage.LongClickListenerHolder<*>) {
                @Suppress("UNCHECKED_CAST")
                val longClickListenerHolder =
                    holder as ClickListenerStorage.LongClickListenerHolder<DATA>
                val viewId = longClickListenerHolder.viewId
                val targetView = if (viewId > 0) {
                    itemView.findViewById(viewId)
                        ?: throw IllegalArgumentException("Not found long click bind target view by id $viewId")
                } else {
                    itemView
                }
                targetView.setOnLongClickListener { view ->
                    longClickListenerHolder.listener.onLongClick(
                        view.context, view, bindingAdapterPosition, absoluteAdapterPosition, data
                    )
                }
            }
        }
    }
}