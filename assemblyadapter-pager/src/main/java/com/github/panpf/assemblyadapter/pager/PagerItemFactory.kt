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
import com.github.panpf.assemblyadapter.OnClickListener
import com.github.panpf.assemblyadapter.OnLongClickListener
import com.github.panpf.assemblyadapter.internal.ClickListenerStorage
import com.github.panpf.assemblyadapter.internal.ItemFactoryStorage
import com.github.panpf.assemblyadapter.internal.Matchable
import com.github.panpf.assemblyadapter.pager.internal.PagerClickListenerWrapper
import com.github.panpf.assemblyadapter.pager.internal.PagerLongClickListenerWrapper
import kotlin.reflect.KClass

/**
 * [PagerItemFactory] is responsible creating item view.
 *
 * When the Adapter needs to display a data, it will find a matching [PagerItemFactory] from [ItemFactoryStorage]
 * through the [matchData] method, and then use this [PagerItemFactory] to create an item view
 *
 * It is not recommended to directly inherit [PagerItemFactory], you can inherit [BindingPagerItemFactory] to implement your own [ViewPagerItemFactory]
 *
 * @param DATA Define the type of matching data
 * @param dataClass The class of data that can be matched. By default, as long as the given data is an instance of this class,
 * it is considered a match. You can also override the [exactMatchData] method to achieve exact matching
 * @see BindingPagerItemFactory
 * @see ViewPagerItemFactory
 */
abstract class PagerItemFactory<DATA : Any>(final override val dataClass: KClass<DATA>) :
    Matchable<DATA> {

    private var clickListenerStorage: ClickListenerStorage<DATA>? = null

    final override fun matchData(data: Any): Boolean {
        return super.matchData(data)
    }

    /**
     * When the Adapter needs a new [View] to display data, it will execute this method to create an [View].
     *
     * @param bindingAdapterPosition The position of the current item in its directly bound adapter.
     * For its specific meaning, please refer to the RecyclerView.ViewHolder.getBindingAdapterPosition() method.
     * This value will be different when using Concat*Adapter
     * @param absoluteAdapterPosition The position of the current item in the RecyclerView.adapter adapter.
     * For the specific meaning, please refer to the RecyclerView.ViewHolder.getAbsoluteAdapterPosition() method.
     * This value will be different when using Concat*Adapter
     * @param data Data to be bound
     * @return A new [View]
     * @see createItemView
     * @see View
     */
    fun dispatchCreateItemView(
        context: Context,
        parent: ViewGroup,
        bindingAdapterPosition: Int,
        absoluteAdapterPosition: Int,
        data: DATA
    ): View = createItemView(
        context, parent, bindingAdapterPosition, absoluteAdapterPosition, data
    ).apply {
        registerClickListener(this, bindingAdapterPosition, absoluteAdapterPosition, data)
    }

    /**
     * Create a new [View]. This method can only be called by [dispatchCreateItemView]
     *
     * @param bindingAdapterPosition The position of the current item in its directly bound adapter.
     * For its specific meaning, please refer to the RecyclerView.ViewHolder.getBindingAdapterPosition() method.
     * This value will be different when using Concat*Adapter
     * @param absoluteAdapterPosition The position of the current item in the RecyclerView.adapter adapter.
     * For the specific meaning, please refer to the RecyclerView.ViewHolder.getAbsoluteAdapterPosition() method.
     * This value will be different when using Concat*Adapter
     * @param data Data to be bound
     * @see dispatchCreateItemView
     * @see View
     */
    protected abstract fun createItemView(
        context: Context, parent: ViewGroup,
        bindingAdapterPosition: Int,
        absoluteAdapterPosition: Int,
        data: DATA
    ): View

    /**
     * Set the click listener of the specified View in the item view
     *
     * @param viewId Specify the id of the View
     * @param onClickListener Implementation of click listener
     * @return [PagerItemFactory] itself, easy to implement chain call
     * @see OnClickListener
     */
    fun setOnViewClickListener(
        @IdRes viewId: Int,
        onClickListener: OnClickListener<DATA>
    ): PagerItemFactory<DATA> {
        getClickListenerManagerOrCreate().add(viewId, onClickListener)
        return this
    }

    /**
     * Set the long click listener of the specified View in the item view
     *
     * @param viewId Specify the id of the View
     * @param onLongClickListener Implementation of long click listener
     * @return [PagerItemFactory] itself, easy to implement chain call
     * @see OnLongClickListener
     */
    fun setOnViewLongClickListener(
        @IdRes viewId: Int,
        onLongClickListener: OnLongClickListener<DATA>
    ): PagerItemFactory<DATA> {
        getClickListenerManagerOrCreate().add(viewId, onLongClickListener)
        return this
    }

    /**
     * Set the click listener of the item view
     *
     * @param onClickListener Implementation of click listener
     * @return [PagerItemFactory] itself, easy to implement chain call
     * @see OnClickListener
     */
    fun setOnItemClickListener(onClickListener: OnClickListener<DATA>): PagerItemFactory<DATA> {
        getClickListenerManagerOrCreate().add(onClickListener)
        return this
    }

    /**
     * Set the long click listener of the item view
     *
     * @param onLongClickListener Implementation of click listener
     * @return [PagerItemFactory] itself, easy to implement chain call
     * @see OnLongClickListener
     */
    fun setOnItemLongClickListener(onLongClickListener: OnLongClickListener<DATA>): PagerItemFactory<DATA> {
        getClickListenerManagerOrCreate().add(onLongClickListener)
        return this
    }

    private fun getClickListenerManagerOrCreate(): ClickListenerStorage<DATA> {
        return (clickListenerStorage ?: (ClickListenerStorage<DATA>().apply {
            this@PagerItemFactory.clickListenerStorage = this
        }))
    }

    private fun registerClickListener(
        itemView: View, bindingAdapterPosition: Int, absoluteAdapterPosition: Int, data: DATA
    ) {
        val clickListenerManager = clickListenerStorage ?: return
        for (holder in clickListenerManager.holders) {
            if (holder is ClickListenerStorage.ClickListenerHolder<*>) {
                @Suppress("UNCHECKED_CAST")
                val clickListenerHolder = holder as ClickListenerStorage.ClickListenerHolder<DATA>
                val viewId = clickListenerHolder.viewId
                val targetView = if (viewId != -1) {
                    itemView.findViewById(viewId)
                        ?: throw IllegalArgumentException("Not found click bind target view by id $viewId")
                } else {
                    itemView
                }
                targetView.setOnClickListener(
                    PagerClickListenerWrapper(
                        clickListenerHolder.listener,
                        bindingAdapterPosition,
                        absoluteAdapterPosition,
                        data
                    )
                )
            } else if (holder is ClickListenerStorage.LongClickListenerHolder<*>) {
                @Suppress("UNCHECKED_CAST")
                val longClickListenerHolder =
                    holder as ClickListenerStorage.LongClickListenerHolder<DATA>
                val viewId = longClickListenerHolder.viewId
                val targetView = if (viewId != -1) {
                    itemView.findViewById(viewId)
                        ?: throw IllegalArgumentException("Not found long click bind target view by id $viewId")
                } else {
                    itemView
                }
                targetView.setOnLongClickListener(
                    PagerLongClickListenerWrapper(
                        longClickListenerHolder.listener,
                        bindingAdapterPosition,
                        absoluteAdapterPosition,
                        data
                    )
                )
            }
        }
    }
}