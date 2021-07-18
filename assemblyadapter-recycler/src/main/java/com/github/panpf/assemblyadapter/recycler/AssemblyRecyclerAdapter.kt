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
import com.github.panpf.assemblyadapter.AssemblyAdapter
import com.github.panpf.assemblyadapter.Item
import com.github.panpf.assemblyadapter.ItemFactory
import com.github.panpf.assemblyadapter.Placeholder
import com.github.panpf.assemblyadapter.internal.ItemDataStorage
import com.github.panpf.assemblyadapter.internal.ItemFactoryStorage
import com.github.panpf.assemblyadapter.recycler.internal.FullSpanStaggeredGridLayoutManager
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
    itemFactoryList: List<ItemFactory<*>>,
    initDataList: List<DATA>? = null
) : RecyclerView.Adapter<RecyclerView.ViewHolder>(), AssemblyAdapter<ItemFactory<*>> {

    private val itemFactoryStorage = ItemFactoryStorage(itemFactoryList)
    private val itemDataStorage = ItemDataStorage(initDataList) { notifyDataSetChanged() }

    /**
     * Get the current list. If a null list is submitted through [submitDataList], or no list is submitted, an empty list will be returned.
     * The returned list may not change-changes to the content must be passed through [submitDataList].
     */
    val dataList: List<DATA>
        get() = itemDataStorage.readOnlyDataList

    init {
        require(itemFactoryList.isNotEmpty()) { "itemFactoryList Can not be empty" }
    }

    /**
     * Set the new list to be displayed.
     */
    fun submitDataList(dataList: List<DATA>?) {
        itemDataStorage.submitDataList(dataList)
    }

    override fun getItemCount(): Int {
        return itemDataStorage.dataCount
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        val data = itemDataStorage.getData(position) ?: Placeholder
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
            if (layoutManager is FullSpanStaggeredGridLayoutManager) {
                layoutManager.setFullSpan(itemView, itemFactory)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is RecyclerViewHolderWrapper<*>) {
            @Suppress("UNCHECKED_CAST")
            val item = holder.wrappedItem as Item<Any>
            val data = itemDataStorage.getData(position) ?: Placeholder
            item.dispatchBindData(position, holder.position, data)
        } else {
            throw IllegalArgumentException("holder must be RecyclerViewHolderWrapper")
        }
    }


    override fun getItemFactoryByPosition(position: Int): ItemFactory<*> {
        val data = itemDataStorage.getData(position) ?: Placeholder
        return itemFactoryStorage.getItemFactoryByData(
            data, "ItemFactory", "AssemblyRecyclerAdapter", "itemFactoryList"
        )
    }
}