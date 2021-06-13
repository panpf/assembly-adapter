/*
 * Copyright (C) 2017 Peng fei Pan <sky@panpf.me>
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
import com.github.panpf.assemblyadapter.OnClickListener
import com.github.panpf.assemblyadapter.OnLongClickListener
import com.github.panpf.assemblyadapter.internal.BaseItemFactory
import com.github.panpf.assemblyadapter.internal.ClickListenerManager

abstract class AssemblyPagerItemFactory<DATA> : BaseItemFactory {

    private var clickListenerManager: ClickListenerManager<DATA>? = null

    fun dispatchCreateView(
        context: Context, container: ViewGroup, position: Int, data: DATA?
    ): View {
        return createView(context, container, position, data).apply {
            registerItemClickListener(this, position, data)
        }
    }

    abstract override fun match(data: Any?): Boolean

    abstract fun createView(
        context: Context, container: ViewGroup, position: Int, data: DATA?
    ): View

    fun setOnViewClickListener(
        @IdRes viewId: Int, onClickListener: OnClickListener<DATA>
    ): AssemblyPagerItemFactory<DATA> {
        getClickListenerManagerOrCreate().add(viewId, onClickListener)
        return this
    }

    fun setOnItemClickListener(onClickListener: OnClickListener<DATA>): AssemblyPagerItemFactory<DATA> {
        getClickListenerManagerOrCreate().add(onClickListener)
        return this
    }

    fun setOnViewLongClickListener(
        @IdRes viewId: Int, onClickListener: OnLongClickListener<DATA>
    ): AssemblyPagerItemFactory<DATA> {
        getClickListenerManagerOrCreate().add(viewId, onClickListener)
        return this
    }

    fun setOnItemLongClickListener(onClickListener: OnLongClickListener<DATA>): AssemblyPagerItemFactory<DATA> {
        getClickListenerManagerOrCreate().add(onClickListener)
        return this
    }

    private fun getClickListenerManagerOrCreate(): ClickListenerManager<DATA> {
        return (clickListenerManager ?: (ClickListenerManager<DATA>().apply {
            this@AssemblyPagerItemFactory.clickListenerManager = this
        }))
    }

    private fun registerItemClickListener(itemView: View, position: Int, data: DATA?) {
        val clickListenerManager = clickListenerManager ?: return
        for (holder in clickListenerManager.holders) {
            if (holder is ClickListenerManager.ClickListenerHolder<*>) {
                @Suppress("UNCHECKED_CAST")
                val clickListenerHolder = holder as ClickListenerManager.ClickListenerHolder<DATA>
                val viewId: Int = clickListenerHolder.viewId
                val targetView: View = if (viewId > 0) {
                    itemView.findViewById(viewId)
                        ?: throw IllegalArgumentException("Not found click bind target view by id $viewId")
                } else {
                    itemView
                }
                targetView.setOnClickListener { view ->
                    clickListenerHolder.listener.onClick(view.context, view, position, data)
                }
            } else if (holder is ClickListenerManager.LongClickListenerHolder<*>) {
                @Suppress("UNCHECKED_CAST")
                val longClickListenerHolder =
                    holder as ClickListenerManager.LongClickListenerHolder<DATA>
                val viewId: Int = longClickListenerHolder.viewId
                val targetView: View = if (viewId > 0) {
                    itemView.findViewById(viewId)
                        ?: throw IllegalArgumentException("Not found click bind target view by id $viewId")
                } else {
                    itemView
                }
                targetView.setOnLongClickListener { view ->
                    longClickListenerHolder.listener.onLongClick(view.context, view, position, data)
                }
            }
        }
    }
}