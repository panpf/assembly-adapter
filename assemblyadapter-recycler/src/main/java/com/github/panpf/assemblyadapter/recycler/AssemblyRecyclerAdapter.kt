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
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.github.panpf.assemblyadapter.*
import com.github.panpf.assemblyadapter.internal.ItemDataStorage
import com.github.panpf.assemblyadapter.internal.ItemFactoryStorage
import com.github.panpf.assemblyadapter.recycler.internal.FullSpanSupport
import com.github.panpf.assemblyadapter.recycler.internal.RecyclerViewHolderWrapper

/**
 * An implementation of [RecyclerView.Adapter], which implements multi-type adapters through standardized [ItemFactory].
 * [AssemblyRecyclerAdapter] will use the data corresponding to position to find a matching [ItemFactory] (cannot find an exception will be thrown),
 * and then use [ItemFactory] to create an item view and bind the data
 *
 * @param itemFactoryList The collection of [ItemFactory] passed in from outside, cannot be empty.
 * Each type of data in the data set must have a matching [ItemFactory], otherwise an exception will be thrown
 * @param initDataList Initial data set
 * @see ItemFactory
 */
open class AssemblyRecyclerAdapter<DATA>(
    itemFactoryList: List<ItemFactory<out Any>>,
    initDataList: List<DATA>? = null,
) : RecyclerView.Adapter<RecyclerView.ViewHolder>(), AssemblyAdapter<DATA, ItemFactory<out Any>> {

    private val itemFactoryStorage = ItemFactoryStorage(
        itemFactoryList, "ItemFactory", "AssemblyRecyclerAdapter", "itemFactoryList"
    )
    private val itemDataStorage = ItemDataStorage(initDataList) { oldList, newList ->
        onDataListChanged(oldList, newList)
    }

    /**
     * Get the current list. If a null list is submitted through [submitList], or no list is submitted, an empty list will be returned.
     * The returned list may not change-changes to the content must be passed through [submitList].
     */
    val currentList: List<DATA>
        get() = itemDataStorage.readOnlyList

    init {
        require(itemFactoryList.isNotEmpty()) { "itemFactoryList Can not be empty" }
    }

    /**
     * Set the new list to be displayed.
     */
    open fun submitList(list: List<DATA>?) {
        itemDataStorage.submitList(list)
    }

    override fun getItemCount(): Int {
        return itemDataStorage.dataCount
    }

    fun getItemData(position: Int): DATA {
        return itemDataStorage.getData(position)
    }

    override fun getItemId(position: Int): Long {
        return if (hasStableIds()) {
            val data = getItemData(position) ?: Placeholder
            if (data is ItemId) data.itemId else data.hashCode().toLong()
        } else {
            RecyclerView.NO_ID
        }
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

    protected open fun onDataListChanged(oldList: List<DATA>, newList: List<DATA>) {
        notifyDataSetChanged()
    }
}