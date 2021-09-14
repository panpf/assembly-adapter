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
    : ListAdapter<DATA, RecyclerView.ViewHolder>, AssemblyAdapter<DATA, ItemFactory<out Any>> {

    private val itemFactoryStorage: ItemFactoryStorage<ItemFactory<out Any>>

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
        itemFactoryList: List<ItemFactory<out Any>>,
        initDataList: List<DATA>? = null,
        diffCallback: DiffUtil.ItemCallback<DATA> = KeyEqualsDiffItemCallback(),
    ) : super(diffCallback) {
        require(itemFactoryList.isNotEmpty()) { "itemFactoryList Can not be empty" }
        if (diffCallback is KeyEqualsDiffItemCallback) {
            KeyEqualsDiffItemCallback.checkDataClass(
                itemFactoryList.map { it.dataClass }.filter { it.java != Placeholder::class.java }
            )
        }
        itemFactoryStorage = ItemFactoryStorage(
            itemFactoryList, "ItemFactory", "AssemblyRecyclerListAdapter", "itemFactoryList"
        )
        if (initDataList?.isNotEmpty() == true) {
            submitList(initDataList)
        }
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
        itemFactoryList: List<ItemFactory<out Any>>,
        initDataList: List<DATA>? = null,
        config: AsyncDifferConfig<DATA>,
    ) : super(config) {
        require(itemFactoryList.isNotEmpty()) { "itemFactoryList Can not be empty" }
        if (config.diffCallback is KeyEqualsDiffItemCallback) {
            KeyEqualsDiffItemCallback.checkDataClass(
                itemFactoryList.map { it.dataClass }.filter { it.java != Placeholder::class.java }
            )
        }
        itemFactoryStorage = ItemFactoryStorage(
            itemFactoryList, "ItemFactory", "AssemblyRecyclerListAdapter", "itemFactoryList"
        )
        if (initDataList?.isNotEmpty() == true) {
            submitList(initDataList)
        }
    }

    fun getItemData(position: Int): DATA {
        return getItem(position)
    }

    /**
     * Note: [getItemId] is final, because stable IDs are unnecessary and therefore unsupported.
     *
     * [AssemblyRecyclerListAdapter]'s async diffing means that efficient change animations are handled for
     * you, without the performance drawbacks of [RecyclerView.Adapter.notifyDataSetChanged].
     * Instead, the diffCallback parameter of the [AssemblyRecyclerListAdapter] serves the same
     * functionality - informing the adapter and [RecyclerView] how items are changed and moved.
     */
    final override fun getItemId(position: Int): Long {
        return super.getItemId(position)
    }

    /**
     * Stable ids are unsupported by [AssemblyRecyclerListAdapter]. Calling this method is an error and will
     * result in an [UnsupportedOperationException].
     *
     * @param hasStableIds Whether items in data set have unique identifiers or not.
     *
     * @throws UnsupportedOperationException Always thrown, since this is unsupported by
     * [AssemblyRecyclerListAdapter].
     */
    final override fun setHasStableIds(hasStableIds: Boolean) {
        throw UnsupportedOperationException("Stable ids are unsupported on PagingDataAdapter.")
    }

    override fun getItemViewType(position: Int): Int {
        val data = getItemData(position) ?: Placeholder
        return itemFactoryStorage.getItemTypeByData(data)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val itemFactory = itemFactoryStorage.getItemFactoryByItemType(viewType)
        val item = itemFactory.dispatchCreateItem(parent)
        return RecyclerViewHolderWrapper(item).apply {
            val layoutManager =
                (parent.takeIf { it is RecyclerView } as RecyclerView?)?.layoutManager
            if (layoutManager is StaggeredGridLayoutManager && layoutManager is FullSpanSupport) {
                (itemView.layoutParams as StaggeredGridLayoutManager.LayoutParams)
                    .isFullSpan = layoutManager.isFullSpanByItemFactoryClass(itemFactory.javaClass)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is RecyclerViewHolderWrapper<*>) {
            @Suppress("UNCHECKED_CAST")
            val item = holder.wrappedItem as Item<Any>
            val data = getItemData(position) ?: Placeholder
            val absoluteAdapterPosition =
                holder.absoluteAdapterPosition.takeIf { it != -1 } ?: holder.position
            item.dispatchBindData(position, absoluteAdapterPosition, data)
        } else {
            throw IllegalArgumentException("holder must be RecyclerViewHolderWrapper")
        }
    }


    override fun getItemFactoryByPosition(position: Int): ItemFactory<Any> {
        val data = getItemData(position) ?: Placeholder
        return itemFactoryStorage.getItemFactoryByData(data) as ItemFactory<Any>
    }

    override fun getItemFactoryByData(data: DATA): ItemFactory<Any> {
        return itemFactoryStorage.getItemFactoryByData(data ?: Placeholder) as ItemFactory<Any>
    }

    override fun <T : ItemFactory<out Any>> getItemFactoryByClass(itemFactoryClass: Class<T>): T {
        return itemFactoryStorage.getItemFactoryByClass(itemFactoryClass)
    }
}