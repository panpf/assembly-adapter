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
 * Single data version of [AssemblyRecyclerListAdapter]
 */
open class AssemblySingleDataRecyclerListAdapter<DATA : Any> :
    ListAdapter<DATA, RecyclerView.ViewHolder>, AssemblyAdapter<ItemFactory<*>> {

    private val itemFactoryStorage: ItemFactoryStorage<ItemFactory<DATA>>

    /**
     * The only data of the current adapter, notifyItem\* will be triggered when the data changes
     */
    var data: DATA?
        set(value) {
            if (value != null) {
                super.submitList(listOf(value))
            } else {
                super.submitList(null)
            }
        }
        get() = if (itemCount > 0) getItemData(0) else null

    /**
     * Create an AssemblySingleDataRecyclerListAdapter that provides DiffUtil.ItemCallback externally
     *
     * @param itemFactory Can match data's [ItemFactory]
     * @param initData Initial data
     * @param diffCallback DiffUtil comparison data callback, the default is [KeyEqualsDiffItemCallback]
     * @see ItemFactory
     * @see KeyEqualsDiffItemCallback
     */
    constructor(
        itemFactory: ItemFactory<DATA>,
        initData: DATA? = null,
        diffCallback: DiffUtil.ItemCallback<DATA> = KeyEqualsDiffItemCallback(),
    ) : super(diffCallback) {
        if (diffCallback is KeyEqualsDiffItemCallback) {
            KeyEqualsDiffItemCallback.checkDataClass(
                listOf(itemFactory.dataClass).filter { it.java != Placeholder::class.java }
            )
        }
        this.itemFactoryStorage = ItemFactoryStorage(listOf(itemFactory))
        this.data = initData
    }

    /**
     * Create an AssemblySingleDataRecyclerListAdapter that provides DiffUtil.ItemCallback externally
     *
     *
     * @param itemFactory Can match data's [ItemFactory]
     * @param diffCallback DiffUtil comparison data callback, the default is [KeyEqualsDiffItemCallback]
     * @see ItemFactory
     * @see KeyEqualsDiffItemCallback
     */
    constructor(
        itemFactory: ItemFactory<DATA>,
        diffCallback: DiffUtil.ItemCallback<DATA> = KeyEqualsDiffItemCallback(),
    ) : this(itemFactory, null, diffCallback)

    /**
     * Create an AssemblySingleDataRecyclerListAdapter that provides AsyncDifferConfig externally
     *
     * @param itemFactory Can match data's [ItemFactory]
     * @param initData Initial data
     * @param config AsyncDifferConfig
     * @see ItemFactory
     * @see AsyncDifferConfig
     */
    constructor(
        itemFactory: ItemFactory<DATA>,
        initData: DATA?,
        config: AsyncDifferConfig<DATA>,
    ) : super(config) {
        if (config.diffCallback is KeyEqualsDiffItemCallback) {
            KeyEqualsDiffItemCallback.checkDataClass(
                listOf(itemFactory.dataClass).filter { it.java != Placeholder::class.java }
            )
        }
        this.itemFactoryStorage = ItemFactoryStorage(listOf(itemFactory))
        this.data = initData
    }

    /**
     * Create an AssemblySingleDataRecyclerListAdapter that provides AsyncDifferConfig externally
     *
     * @param itemFactory Can match data's [ItemFactory]
     * @param config AsyncDifferConfig
     * @see ItemFactory
     * @see AsyncDifferConfig
     */
    constructor(
        itemFactory: ItemFactory<DATA>,
        config: AsyncDifferConfig<DATA>,
    ) : this(itemFactory, null, config)

    override fun submitList(list: List<DATA>?) {
        require(list?.size ?: 0 > 1) {
            "Cannot submit a list with size greater than 1"
        }
        super.submitList(list)
    }

    override fun submitList(list: List<DATA>?, commitCallback: Runnable?) {
        require(list?.size ?: 0 > 1) {
            "Cannot submit a list with size greater than 1"
        }
        super.submitList(list, commitCallback)
    }

    fun getItemData(position: Int): DATA {
        return getItem(position)
    }
    /**
     * Note: [getItemId] is final, because stable IDs are unnecessary and therefore unsupported.
     *
     * [AssemblySingleDataRecyclerListAdapter]'s async diffing means that efficient change animations are handled for
     * you, without the performance drawbacks of [RecyclerView.Adapter.notifyDataSetChanged].
     * Instead, the diffCallback parameter of the [AssemblySingleDataRecyclerListAdapter] serves the same
     * functionality - informing the adapter and [RecyclerView] how items are changed and moved.
     */
    final override fun getItemId(position: Int): Long {
        return super.getItemId(position)
    }

    /**
     * Stable ids are unsupported by [AssemblySingleDataRecyclerListAdapter]. Calling this method is an error and will
     * result in an [UnsupportedOperationException].
     *
     * @param hasStableIds Whether items in data set have unique identifiers or not.
     *
     * @throws UnsupportedOperationException Always thrown, since this is unsupported by
     * [AssemblySingleDataRecyclerListAdapter].
     */
    final override fun setHasStableIds(hasStableIds: Boolean) {
        throw UnsupportedOperationException("Stable ids are unsupported on PagingDataAdapter.")
    }

    override fun getItemViewType(position: Int): Int {
        val data = getItemData(position)
        return itemFactoryStorage.getItemTypeByData(
            data, "ItemFactory", "AssemblySingleDataRecyclerListAdapter", "itemFactory"
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
                    .isFullSpan = layoutManager.isFullSpanByItemFactoryClass(itemFactory.javaClass)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is RecyclerViewHolderWrapper<*>) {
            @Suppress("UNCHECKED_CAST")
            val item = holder.wrappedItem as Item<Any>
            val data = getItemData(position)
            val absoluteAdapterPosition =
                holder.absoluteAdapterPosition.takeIf { it != -1 } ?: holder.position
            item.dispatchBindData(position, absoluteAdapterPosition, data)
        } else {
            throw IllegalArgumentException("holder must be RecyclerViewHolderWrapper")
        }
    }


    override fun getItemFactoryByPosition(position: Int): ItemFactory<DATA> {
        val data = getItemData(position)
        return itemFactoryStorage.getItemFactoryByData(
            data, "ItemFactory", "AssemblySingleDataRecyclerListAdapter", "itemFactory"
        )
    }
}