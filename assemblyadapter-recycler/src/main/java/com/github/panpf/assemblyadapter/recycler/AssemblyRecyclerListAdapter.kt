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
import com.github.panpf.assemblyadapter.AssemblyAdapter
import com.github.panpf.assemblyadapter.Item
import com.github.panpf.assemblyadapter.ItemFactory
import com.github.panpf.assemblyadapter.Placeholder
import com.github.panpf.assemblyadapter.internal.ItemFactoryStorage
import com.github.panpf.assemblyadapter.recycler.internal.IsFullSpanByItemFactory
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
     * @param diffCallback DiffUtil comparison data callback, the default is [KeyDiffItemCallback]
     * @see ItemFactory
     * @see KeyDiffItemCallback
     */
    constructor(
        itemFactoryList: List<ItemFactory<*>>,
        diffCallback: DiffUtil.ItemCallback<DATA> = KeyDiffItemCallback(),
    ) : super(diffCallback) {
        itemFactoryStorage = ItemFactoryStorage(itemFactoryList)
        require(itemFactoryList.isNotEmpty()) { "itemFactoryList Can not be empty" }
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
        itemFactoryStorage = ItemFactoryStorage(itemFactoryList)
        require(itemFactoryList.isNotEmpty()) { "itemFactoryList Can not be empty" }
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
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
            if (layoutManager is StaggeredGridLayoutManager && layoutManager is IsFullSpanByItemFactory) {
                (itemView.layoutParams as StaggeredGridLayoutManager.LayoutParams)
                    .isFullSpan = layoutManager.isFullSpan(itemFactory)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is RecyclerViewHolderWrapper<*>) {
            @Suppress("UNCHECKED_CAST")
            val item = holder.wrappedItem as Item<Any>
            val data = getItem(position) ?: Placeholder
            item.dispatchBindData(position, holder.absoluteAdapterPosition, data)
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