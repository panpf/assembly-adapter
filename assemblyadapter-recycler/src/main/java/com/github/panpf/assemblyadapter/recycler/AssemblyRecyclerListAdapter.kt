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

import android.view.ViewGroup
import androidx.recyclerview.widget.*
import com.github.panpf.assemblyadapter.*
import com.github.panpf.assemblyadapter.internal.ItemFactoryStorage
import com.github.panpf.assemblyadapter.recycler.internal.FullSpanSupport
import com.github.panpf.assemblyadapter.recycler.internal.RecyclerViewHolderWrapper

/**
 * An implementation of [ListAdapter], which implements multi-type adapters through standardized [ItemFactory].
 * [AssemblyRecyclerListAdapter] will use the data corresponding to position to find a matching [ItemFactory] (cannot find an exception will be thrown),
 * and then use [ItemFactory] to create an item view and bind the data
 *
 * @see ItemFactory
 */
open class AssemblyRecyclerListAdapter<DATA>
    : ListAdapter<DATA, RecyclerView.ViewHolder>, AssemblyAdapter<ItemFactory<*>> {

    private val itemFactoryStorage: ItemFactoryStorage<ItemFactory<*>>

    /**
     * Create an AssemblyRecyclerListAdapter that provides DiffUtil.ItemCallback externally
     *
     * @param itemFactoryList The collection of [ItemFactory] passed in from outside, cannot be empty.
     * Each type of data in the data set must have a matching [ItemFactory], otherwise an exception will be thrown
     * @param initDataList Initial data set
     * @param diffCallback DiffUtil comparison data callback, the default is [KeyEqualsDiffItemCallback]
     * @see ItemFactory
     * @see KeyEqualsDiffItemCallback
     */
    @Suppress("LeakingThis")
    constructor(
        itemFactoryList: List<ItemFactory<*>>,
        initDataList: List<DATA>? = null,
        diffCallback: DiffUtil.ItemCallback<DATA> = KeyEqualsDiffItemCallback(),
    ) : super(diffCallback) {
        require(itemFactoryList.isNotEmpty()) { "itemFactoryList Can not be empty" }
        if (diffCallback is KeyEqualsDiffItemCallback) {
            KeyEqualsDiffItemCallback.checkDataClass(
                itemFactoryList.map { it.dataClass }.filter { it.java != Placeholder::class.java }
            )
        }
        itemFactoryStorage = ItemFactoryStorage(itemFactoryList)
        submitList(initDataList)
    }

    /**
     * Create an AssemblyRecyclerListAdapter that provides DiffUtil.ItemCallback externally
     *
     * @param itemFactoryList The collection of [ItemFactory] passed in from outside, cannot be empty.
     * Each type of data in the data set must have a matching [ItemFactory], otherwise an exception will be thrown
     * @param diffCallback DiffUtil comparison data callback, the default is [KeyEqualsDiffItemCallback]
     * @see ItemFactory
     * @see KeyEqualsDiffItemCallback
     */
    constructor(
        itemFactoryList: List<ItemFactory<*>>,
        diffCallback: DiffUtil.ItemCallback<DATA> = KeyEqualsDiffItemCallback(),
    ) : super(diffCallback) {
        require(itemFactoryList.isNotEmpty()) { "itemFactoryList Can not be empty" }
        if (diffCallback is KeyEqualsDiffItemCallback) {
            KeyEqualsDiffItemCallback.checkDataClass(
                itemFactoryList.map { it.dataClass }.filter { it.java != Placeholder::class.java }
            )
        }
        itemFactoryStorage = ItemFactoryStorage(itemFactoryList)
    }

    /**
     * Create an AssemblyRecyclerListAdapter that provides AsyncDifferConfig externally
     *
     * @param itemFactoryList The collection of [ItemFactory] passed in from outside, cannot be empty.
     * Each type of data in the data set must have a matching [ItemFactory], otherwise an exception will be thrown
     * @param initDataList Initial data set
     * @param config AsyncDifferConfig
     * @see ItemFactory
     * @see AsyncDifferConfig
     */
    @Suppress("LeakingThis")
    constructor(
        itemFactoryList: List<ItemFactory<*>>,
        initDataList: List<DATA>?,
        config: AsyncDifferConfig<DATA>,
    ) : super(config) {
        require(itemFactoryList.isNotEmpty()) { "itemFactoryList Can not be empty" }
        if (config.diffCallback is KeyEqualsDiffItemCallback) {
            KeyEqualsDiffItemCallback.checkDataClass(
                itemFactoryList.map { it.dataClass }.filter { it.java != Placeholder::class.java }
            )
        }
        itemFactoryStorage = ItemFactoryStorage(itemFactoryList)
        submitList(initDataList)
    }

    /**
     * Create an AssemblyRecyclerListAdapter that provides AsyncDifferConfig externally
     *
     * @param itemFactoryList The collection of [ItemFactory] passed in from outside, cannot be empty.
     * Each type of data in the data set must have a matching [ItemFactory], otherwise an exception will be thrown
     * @param config AsyncDifferConfig
     * @see ItemFactory
     * @see AsyncDifferConfig
     */
    constructor(
        itemFactoryList: List<ItemFactory<*>>,
        config: AsyncDifferConfig<DATA>,
    ) : super(config) {
        require(itemFactoryList.isNotEmpty()) { "itemFactoryList Can not be empty" }
        if (config.diffCallback is KeyEqualsDiffItemCallback) {
            KeyEqualsDiffItemCallback.checkDataClass(
                itemFactoryList.map { it.dataClass }.filter { it.java != Placeholder::class.java }
            )
        }
        itemFactoryStorage = ItemFactoryStorage(itemFactoryList)
    }

    public override fun getItem(position: Int): DATA {
        return super.getItem(position)
    }

    override fun getItemId(position: Int): Long {
        return if (hasStableIds()) {
            val data = getItem(position) ?: Placeholder
            if (data is ItemId) data.itemId else data.hashCode().toLong()
        } else {
            RecyclerView.NO_ID
        }
    }

    override fun getItemViewType(position: Int): Int {
        val data = getItem(position) ?: Placeholder
        return itemFactoryStorage.getItemTypeByData(
            data, "ItemFactory", "AssemblyRecyclerAdapter", "itemFactoryList"
        )
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val itemFactory = itemFactoryStorage.getItemFactoryByItemType(viewType)
        val item = itemFactory.dispatchCreateItem(parent)
        return RecyclerViewHolderWrapper(item).apply {
            val layoutManager =
                (parent.takeIf { it is RecyclerView } as RecyclerView?)?.layoutManager
            if (layoutManager is StaggeredGridLayoutManager && layoutManager is FullSpanSupport) {
                (itemView.layoutParams as StaggeredGridLayoutManager.LayoutParams)
                    .isFullSpan = layoutManager.isFullSpanByItemFactory(itemFactory)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is RecyclerViewHolderWrapper<*>) {
            @Suppress("UNCHECKED_CAST")
            val item = holder.wrappedItem as Item<Any>
            val data = getItem(position) ?: Placeholder
            val absoluteAdapterPosition =
                holder.absoluteAdapterPosition.takeIf { it != -1 } ?: holder.position
            item.dispatchBindData(position, absoluteAdapterPosition, data)
        } else {
            throw IllegalArgumentException("holder must be RecyclerViewHolderWrapper")
        }
    }


    override fun getItemFactoryByPosition(position: Int): ItemFactory<*> {
        val data = getItem(position) ?: Placeholder
        return itemFactoryStorage.getItemFactoryByData(
            data, "ItemFactory", "AssemblyRecyclerAdapter", "itemFactoryList"
        )
    }
}