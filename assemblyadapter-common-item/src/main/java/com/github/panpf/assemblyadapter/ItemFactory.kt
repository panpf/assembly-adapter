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
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.IdRes
import com.github.panpf.assemblyadapter.common.item.R
import com.github.panpf.assemblyadapter.internal.ClickListenerStorage
import kotlin.reflect.KClass

/**
 * ItemFactory is responsible for matching data, creating itemView, and binding data.
 *
 * When the Adapter needs to display a data, it will find a matching ItemFactory from ItemFactoryStorage
 * through the matchData() method, and then use this ItemFactory to create an itemView and bind the data
 *
 * It is not recommended to directly inherit [ItemFactory], you can inherit [BindingItemFactory]
 * and [SimpleItemFactory] to implement your own ItemFactory
 *
 * @param DATA Define the type of matching data
 * @param dataClass The class of data that can be matched. By default, as long as the given data is an instance of this class,
 * it is considered a match. You can also override the [exactMatchData] method to achieve exact matching
 * @see BindingItemFactory
 * @see SimpleItemFactory
 * @see ViewItemFactory
 */
abstract class ItemFactory<DATA : Any>(private val dataClass: KClass<DATA>) : Matchable {

    private var clickListenerStorage: ClickListenerStorage<DATA>? = null

    /**
     * If it returns true, it means that this ItemFactory can handle this [data]
     */
    final override fun matchData(data: Any): Boolean {
        @Suppress("UNCHECKED_CAST")
        return dataClass.isInstance(data) && exactMatchData(data as DATA)
    }

    /**
     * Exactly match this [data], such as checking the value of a specific attribute
     */
    protected open fun exactMatchData(data: DATA): Boolean = true

    /**
     * When the Adapter needs a new [Item] to display data, it will execute this method to create an [Item].
     *
     * @param parent The new created [Item] will be added to this ViewGroup
     * @return A new [Item]
     * @see createItem
     * @see Item
     */
    fun dispatchCreateItem(parent: ViewGroup): Item<DATA> {
        return createItem(parent).apply {
            registerClickListener(this)
        }
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
     * Set the click listener of the specified View in the itemView
     *
     * @param viewId Specify the id of the View
     * @param onClickListener Implementation of click listener
     * @return [ItemFactory] itself, easy to implement chain call
     * @see OnClickListener
     */
    open fun setOnViewClickListener(
        @IdRes viewId: Int,
        onClickListener: OnClickListener<DATA>
    ): ItemFactory<DATA> {
        getClickListenerManagerOrCreate().add(viewId, onClickListener)
        return this
    }


    /**
     * Set the long click listener of the specified View in the itemView
     *
     * @param viewId Specify the id of the View
     * @param onLongClickListener Implementation of long click listener
     * @return [ItemFactory] itself, easy to implement chain call
     * @see OnLongClickListener
     */
    open fun setOnViewLongClickListener(
        @IdRes viewId: Int,
        onLongClickListener: OnLongClickListener<DATA>
    ): ItemFactory<DATA> {
        getClickListenerManagerOrCreate().add(viewId, onLongClickListener)
        return this
    }

    /**
     * Set the click listener of the itemView
     *
     * @param onClickListener Implementation of click listener
     * @return [ItemFactory] itself, easy to implement chain call
     * @see OnClickListener
     */
    open fun setOnItemClickListener(onClickListener: OnClickListener<DATA>): ItemFactory<DATA> {
        getClickListenerManagerOrCreate().add(onClickListener)
        return this
    }

    /**
     * Set the long click listener of the itemView
     *
     * @param onLongClickListener Implementation of click listener
     * @return [ItemFactory] itself, easy to implement chain call
     * @see OnLongClickListener
     */
    open fun setOnItemLongClickListener(onLongClickListener: OnLongClickListener<DATA>): ItemFactory<DATA> {
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
                val targetView = if (viewId > 0) {
                    itemView.findViewById(viewId)
                        ?: throw IllegalArgumentException("Not found click bind target view by id $viewId")
                } else {
                    itemView
                }
                targetView.setTag(R.id.aa_tag_clickBindItem, item)
                targetView.setOnClickListener { view ->
                    @Suppress("UNCHECKED_CAST")
                    val bindItem = view.getTag(R.id.aa_tag_clickBindItem) as Item<DATA>
                    clickListenerHolder.listener.onClick(
                        view.context,
                        view,
                        bindItem.bindingAdapterPosition,
                        bindItem.absoluteAdapterPosition,
                        bindItem.dataOrThrow
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
                targetView.setTag(R.id.aa_tag_clickBindItem, item)
                targetView.setOnLongClickListener { view ->
                    @Suppress("UNCHECKED_CAST")
                    val bindItem = view.getTag(R.id.aa_tag_clickBindItem) as Item<DATA>
                    longClickListenerHolder.listener.onLongClick(
                        view.context,
                        view,
                        bindItem.bindingAdapterPosition,
                        bindItem.absoluteAdapterPosition,
                        bindItem.dataOrThrow
                    )
                }
            }
        }
    }

    /**
     * Item is similar to ViewHolder, responsible for holding itemView and binding data
     */
    abstract class Item<DATA>(val itemView: View) {

        private var _data: DATA? = null
        private var _bindingAdapterPosition: Int = -1
        private var _absoluteAdapterPosition: Int = -1

        val context: Context = itemView.context

        /**
         * Get the bound data, or null if there is none
         */
        val dataOrNull: DATA?
            get() = _data


        /**
         * Get the bound data, if not, throw an exception
         */
        val dataOrThrow: DATA
            get() = _data!!

        /**
         * The position of the current item in its directly bound adapter.
         * For its specific meaning, please refer to the RecyclerView.ViewHolder.getBindingAdapterPosition() method
         *
         * This value will be different when using Concat*Adapter
         */
        val bindingAdapterPosition: Int
            get() = _bindingAdapterPosition

        /**
         * The position of the current item in the RecyclerView.adapter adapter.
         * For the specific meaning, please refer to the RecyclerView.ViewHolder.getAbsoluteAdapterPosition() method
         *
         * This value will be different when using Concat*Adapter
         */
        val absoluteAdapterPosition: Int
            get() = _absoluteAdapterPosition

        /**
         * Create Item by layout id and parent ViewGroup
         */
        constructor(itemLayoutId: Int, parent: ViewGroup) : this(
            LayoutInflater.from(parent.context).inflate(itemLayoutId, parent, false)
        )

        /**
         * Bind data to itemView
         *
         * @param bindingAdapterPosition The position of the current item in its directly bound adapter.
         * For its specific meaning, please refer to the RecyclerView.ViewHolder.getBindingAdapterPosition() method.
         * This value will be different when using Concat*Adapter
         * @param absoluteAdapterPosition The position of the current item in the RecyclerView.adapter adapter.
         * For the specific meaning, please refer to the RecyclerView.ViewHolder.getAbsoluteAdapterPosition() method.
         * This value will be different when using Concat*Adapter
         * @param data Data to be bound
         */
        fun dispatchBindData(
            bindingAdapterPosition: Int,
            absoluteAdapterPosition: Int,
            data: DATA
        ) {
            this._data = data
            this._bindingAdapterPosition = bindingAdapterPosition
            this._absoluteAdapterPosition = absoluteAdapterPosition
            bindData(_absoluteAdapterPosition, data)
        }

        /**
         * Bind data to itemView
         *
         * @param bindingAdapterPosition The position of the current item in its directly bound adapter.
         * For its specific meaning, please refer to the RecyclerView.ViewHolder.getBindingAdapterPosition() method.
         * This value will be different when using Concat*Adapter
         * @param data Data to be bound
         */
        // todo add absoluteAdapterPosition
        protected abstract fun bindData(bindingAdapterPosition: Int, data: DATA)
    }
}