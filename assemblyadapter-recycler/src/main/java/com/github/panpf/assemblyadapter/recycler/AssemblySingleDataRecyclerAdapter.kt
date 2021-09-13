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
import com.github.panpf.assemblyadapter.internal.ItemFactoryStorage
import com.github.panpf.assemblyadapter.recycler.internal.FullSpanSupport
import com.github.panpf.assemblyadapter.recycler.internal.RecyclerViewHolderWrapper
import kotlin.reflect.KClass

/**
 * Single data version of [AssemblyRecyclerAdapter]
 *
 * @param itemFactory Can match [data]'s [ItemFactory]
 * @param initData Initial data
 * @see ItemFactory
 */
open class AssemblySingleDataRecyclerAdapter<DATA : Any>(
    itemFactory: ItemFactory<DATA>,
    initData: DATA? = null
) : RecyclerView.Adapter<RecyclerView.ViewHolder>(), AssemblyAdapter<DATA, ItemFactory<out Any>> {

    private val itemFactoryStorage = ItemFactoryStorage<ItemFactory<out Any>>(
        listOf(itemFactory), "ItemFactory", "AssemblySingleDataRecyclerAdapter", "itemFactory"
    )

    /**
     * The only data of the current adapter, notifyItem\* will be triggered when the data changes
     */
    var data: DATA? = initData
        set(value) {
            val oldItem = field != null
            val newItem = value != null
            field = value
            if (oldItem && !newItem) {
                notifyItemRemoved(0)
            } else if (newItem && !oldItem) {
                notifyItemInserted(0)
            } else if (oldItem && newItem) {
                notifyItemChanged(0)
            }
        }

    override fun getItemCount(): Int = if (data != null) 1 else 0

    fun getItemData(position: Int): DATA {
        val count = itemCount
        if (position < 0 || position >= count) {
            throw IndexOutOfBoundsException("Index: $position, Size: $count")
        }
        return data!!
    }

    override fun getItemId(position: Int): Long {
        return if (hasStableIds()) {
            val data = getItemData(position)
            if (data is ItemId) data.itemId else data.hashCode().toLong()
        } else {
            RecyclerView.NO_ID
        }
    }

    override fun getItemViewType(position: Int): Int {
        val data = getItemData(position)
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
            val data = getItemData(position)
            val absoluteAdapterPosition =
                holder.absoluteAdapterPosition.takeIf { it != -1 } ?: holder.position
            item.dispatchBindData(position, absoluteAdapterPosition, data)
        } else {
            throw IllegalArgumentException("holder must be RecyclerViewHolderWrapper")
        }
    }


    override fun getItemFactoryByPosition(position: Int): ItemFactory<Any> {
        val data = getItemData(position)
        return itemFactoryStorage.getItemFactoryByData(data) as ItemFactory<Any>
    }

    override fun getItemFactoryByData(data: DATA): ItemFactory<Any> {
        return itemFactoryStorage.getItemFactoryByData(data) as ItemFactory<Any>
    }

    override fun <T : ItemFactory<out Any>> getItemFactoryByItemFactoryClass(itemFactoryClass: KClass<T>): T {
        return itemFactoryStorage.getItemFactoryByItemFactoryClass(itemFactoryClass.java)
    }

    override fun <T : ItemFactory<out Any>> getItemFactoryByItemFactoryClass(itemFactoryClass: Class<T>): T {
        return itemFactoryStorage.getItemFactoryByItemFactoryClass(itemFactoryClass)
    }
}