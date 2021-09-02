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

    private val itemFactoryStorage: ItemFactoryStorage<ItemFactory<*>>

    /**
     * The only data of the current adapter, [notifyDataSetChanged] will be triggered when the data changes
     */
    var data: DATA?
        set(value) {
            if (value != null) {
                super.submitList(listOf(value))
            } else {
                super.submitList(null)
            }
        }
        get() = if (itemCount > 0) getItem(0) else null

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
        this.itemFactoryStorage = ItemFactoryStorage(listOf(itemFactory))
        data = initData
        if (diffCallback is KeyEqualsDiffItemCallback) {
            KeyEqualsDiffItemCallback.checkDataClass(
                listOf(itemFactory.dataClass).filter { it.java != Placeholder::class.java }
            )
        }
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
    ) : super(diffCallback) {
        this.itemFactoryStorage = ItemFactoryStorage(listOf(itemFactory))
        if (diffCallback is KeyEqualsDiffItemCallback) {
            KeyEqualsDiffItemCallback.checkDataClass(
                listOf(itemFactory.dataClass).filter { it.java != Placeholder::class.java }
            )
        }
    }

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
        data = initData
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
    ) : super(config) {
        if (config.diffCallback is KeyEqualsDiffItemCallback) {
            KeyEqualsDiffItemCallback.checkDataClass(
                listOf(itemFactory.dataClass).filter { it.java != Placeholder::class.java }
            )
        }
        this.itemFactoryStorage = ItemFactoryStorage(listOf(itemFactory))
    }

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

    public override fun getItem(position: Int): DATA {
        return super.getItem(position)
    }

    override fun getItemId(position: Int): Long {
        return if (hasStableIds()) {
            val data = getItem(position)
            if (data is ItemId) data.itemId else data.hashCode().toLong()
        } else {
            RecyclerView.NO_ID
        }
    }

    override fun getItemViewType(position: Int): Int {
        val data = getItem(position)
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
            val data = getItem(position)
            val absoluteAdapterPosition =
                holder.absoluteAdapterPosition.takeIf { it != -1 } ?: holder.position
            item.dispatchBindData(position, absoluteAdapterPosition, data)
        } else {
            throw IllegalArgumentException("holder must be RecyclerViewHolderWrapper")
        }
    }


    override fun getItemFactoryByPosition(position: Int): ItemFactory<*> {
        val data = getItem(position)
        return itemFactoryStorage.getItemFactoryByData(
            data, "ItemFactory", "AssemblyRecyclerAdapter", "itemFactoryList"
        )
    }
}