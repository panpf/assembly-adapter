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
package com.github.panpf.assemblyadapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.annotation.IdRes
import com.github.panpf.assemblyadapter.common.item.R
import com.github.panpf.assemblyadapter.internal.ClickListenerStorage
import com.github.panpf.assemblyadapter.internal.ClickListenerWrapper
import com.github.panpf.assemblyadapter.internal.ItemFactoryStorage
import com.github.panpf.assemblyadapter.internal.LongClickListenerWrapper
import com.github.panpf.assemblyadapter.internal.Matchable
import kotlin.reflect.KClass

/**
 * [ItemFactory] is responsible creating item view, and binding data.
 *
 * When the Adapter needs to display a data, it will find a matching [ItemFactory] from [ItemFactoryStorage]
 * through the [matchData] method, and then use this [ItemFactory] to create an item view and bind the data
 *
 * It is not recommended to directly inherit [ItemFactory], you can inherit [BindingItemFactory]
 * and [SimpleItemFactory] to implement your own [ItemFactory]
 *
 * @param DATA Define the type of matching data
 * @param dataClass The class of data that can be matched. By default, as long as the given data is an instance of this class,
 * it is considered a match. You can also override the [exactMatchData] method to achieve exact matching
 * @see BindingItemFactory
 * @see SimpleItemFactory
 * @see ViewItemFactory
 */
abstract class ItemFactory<DATA : Any>(final override val dataClass: KClass<DATA>) :
    Matchable<DATA> {

    private var clickListenerStorage: ClickListenerStorage<DATA>? = null

    final override fun matchData(data: Any): Boolean {
        return super.matchData(data)
    }

    /**
     * When the Adapter needs a new [Item] to display data, it will execute this method to create an [Item].
     *
     * @param parent The new created [Item] will be added to this ViewGroup
     * @return A new [Item]
     * @see createItem
     * @see Item
     */
    fun dispatchCreateItem(parent: ViewGroup): Item<DATA> = createItem(parent).apply {
        registerClickListener(this)
    }

    /**
     * Create a new [Item]. This method can only be called by [dispatchCreateItem]
     *
     * @param parent The new created [Item] will be added to this ViewGroup
     * @return A new [Item]
     * @see dispatchCreateItem
     * @see Item
     */
    protected abstract fun createItem(parent: ViewGroup): Item<DATA>

    /**
     * Set the click listener of the specified View in the item view
     *
     * @param viewId Specify the id of the View
     * @param onClickListener Implementation of click listener
     * @return [ItemFactory] itself, easy to implement chain call
     * @see OnClickListener
     */
    fun setOnViewClickListener(
        @IdRes viewId: Int,
        onClickListener: OnClickListener<DATA>
    ): ItemFactory<DATA> {
        getClickListenerManagerOrCreate().add(viewId, onClickListener)
        return this
    }

    /**
     * Set the click listener of the specified View in the item view
     *
     * @param viewId Specify the id of the View
     * @param onClickListener Implementation of click listener
     * @return [ItemFactory] itself, easy to implement chain call
     * @see OnClickListener
     */
    fun setOnViewClickListener(
        @IdRes viewId: Int,
        onClickListener: (
            context: Context,
            view: View,
            bindingAdapterPosition: Int,
            absoluteAdapterPosition: Int,
            data: DATA
        ) -> Unit
    ): ItemFactory<DATA> {
        getClickListenerManagerOrCreate().add(viewId, onClickListener)
        return this
    }

    /**
     * Set the long click listener of the specified View in the item view
     *
     * @param viewId Specify the id of the View
     * @param onLongClickListener Implementation of long click listener
     * @return [ItemFactory] itself, easy to implement chain call
     * @see OnLongClickListener
     */
    fun setOnViewLongClickListener(
        @IdRes viewId: Int,
        onLongClickListener: OnLongClickListener<DATA>
    ): ItemFactory<DATA> {
        getClickListenerManagerOrCreate().add(viewId, onLongClickListener)
        return this
    }

    /**
     * Set the long click listener of the specified View in the item view
     *
     * @param viewId Specify the id of the View
     * @param onLongClickListener Implementation of long click listener
     * @return [ItemFactory] itself, easy to implement chain call
     * @see OnLongClickListener
     */
    fun setOnViewLongClickListener(
        @IdRes viewId: Int,
        onLongClickListener: (
            context: Context,
            view: View,
            bindingAdapterPosition: Int,
            absoluteAdapterPosition: Int,
            data: DATA
        ) -> Boolean
    ): ItemFactory<DATA> {
        getClickListenerManagerOrCreate().add(viewId, onLongClickListener)
        return this
    }

    /**
     * Set the click listener of the item view
     *
     * @param onClickListener Implementation of click listener
     * @return [ItemFactory] itself, easy to implement chain call
     * @see OnClickListener
     */
    fun setOnItemClickListener(onClickListener: OnClickListener<DATA>): ItemFactory<DATA> {
        getClickListenerManagerOrCreate().add(onClickListener)
        return this
    }

    /**
     * Set the click listener of the item view
     *
     * @param onClickListener Implementation of click listener
     * @return [ItemFactory] itself, easy to implement chain call
     * @see OnClickListener
     */
    fun setOnItemClickListener(
        onClickListener: (
            context: Context,
            view: View,
            bindingAdapterPosition: Int,
            absoluteAdapterPosition: Int,
            data: DATA
        ) -> Unit
    ): ItemFactory<DATA> {
        getClickListenerManagerOrCreate().add(onClickListener)
        return this
    }

    /**
     * Set the long click listener of the item view
     *
     * @param onLongClickListener Implementation of click listener
     * @return [ItemFactory] itself, easy to implement chain call
     * @see OnLongClickListener
     */
    fun setOnItemLongClickListener(onLongClickListener: OnLongClickListener<DATA>): ItemFactory<DATA> {
        getClickListenerManagerOrCreate().add(onLongClickListener)
        return this
    }

    /**
     * Set the long click listener of the item view
     *
     * @param onLongClickListener Implementation of click listener
     * @return [ItemFactory] itself, easy to implement chain call
     * @see OnLongClickListener
     */
    fun setOnItemLongClickListener(
        onLongClickListener: (
            context: Context,
            view: View,
            bindingAdapterPosition: Int,
            absoluteAdapterPosition: Int,
            data: DATA
        ) -> Boolean
    ): ItemFactory<DATA> {
        getClickListenerManagerOrCreate().add(onLongClickListener)
        return this
    }

    private fun getClickListenerManagerOrCreate(): ClickListenerStorage<DATA> {
        return (clickListenerStorage ?: (ClickListenerStorage<DATA>().apply {
            this@ItemFactory.clickListenerStorage = this
        }))
    }

    private fun registerClickListener(item: Item<DATA>) {
        val clickListenerManager = clickListenerStorage ?: return
        val itemView = item.itemView
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
                targetView.setTag(R.id.aa_tag_clickBindItem, item)
                targetView.setOnClickListener(ClickListenerWrapper(clickListenerHolder.listener))
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
                targetView.setTag(R.id.aa_tag_clickBindItem, item)
                targetView.setOnLongClickListener(LongClickListenerWrapper(longClickListenerHolder.listener))
            }
        }
    }
}