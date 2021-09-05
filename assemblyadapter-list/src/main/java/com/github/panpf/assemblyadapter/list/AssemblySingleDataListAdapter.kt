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
package com.github.panpf.assemblyadapter.list

import android.database.DataSetObserver
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.github.panpf.assemblyadapter.AssemblyAdapter
import com.github.panpf.assemblyadapter.Item
import com.github.panpf.assemblyadapter.ItemFactory
import com.github.panpf.assemblyadapter.ItemId
import com.github.panpf.assemblyadapter.internal.ItemFactoryStorage
import com.github.panpf.assemblyadapter.list.internal.AdapterDataObservable

/**
 * Single data version of [AssemblyListAdapter]
 *
 * @param itemFactory Can match [data]'s [ItemFactory]
 * @param initData Initial data
 * @see ItemFactory
 */
open class AssemblySingleDataListAdapter<DATA : Any>(
    itemFactory: ItemFactory<DATA>,
    initData: DATA? = null,
) : BaseAdapter(), AssemblyAdapter<ItemFactory<*>> {

    private var hasStableIds = false
    private val adapterDataObservable = AdapterDataObservable()
    private val itemFactoryStorage = ItemFactoryStorage(listOf(itemFactory))

    var data: DATA? = initData
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    val itemCount: Int
        get() = if (data != null) 1 else 0

    fun getItemData(position: Int): DATA {
        val count = count
        if (position < 0 || position >= count) {
            throw IndexOutOfBoundsException("Index: $position, Size: $count")
        }
        return data!!
    }

    override fun getCount(): Int = if (data != null) 1 else 0

    override fun getItem(position: Int): DATA {
        return getItemData(position)
    }

    /**
     * Indicates whether each item in the data set can be represented with a unique identifier
     * of type [java.lang.Long].
     *
     * @param hasStableIds Whether items in data set have unique identifiers or not.
     * @see hasStableIds
     * @see getItemId
     */
    fun setHasStableIds(hasStableIds: Boolean) {
        if (hasObservers()) {
            throw IllegalStateException(
                "Cannot change whether this adapter has "
                        + "stable IDs while the adapter has registered observers."
            )
        }
        this.hasStableIds = hasStableIds
    }

    override fun hasStableIds(): Boolean {
        return hasStableIds
    }

    override fun getItemId(position: Int): Long {
        return if (hasStableIds()) {
            val data = getItemData(position)
            if (data is ItemId) data.itemId else data.hashCode().toLong()
        } else {
            -1
        }
    }

    override fun getViewTypeCount(): Int = 1

    override fun getItemViewType(position: Int): Int {
        val data = getItemData(position)
        return itemFactoryStorage.getItemTypeByData(
            data, "ItemFactory", "AssemblyListAdapter", "itemFactoryList"
        )
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val data = getItemData(position)
        val itemView = convertView ?: itemFactoryStorage.getItemFactoryByData(
            data, "ItemFactory", "AssemblyListAdapter", "itemFactoryList"
        ).dispatchCreateItem(parent).apply {
            itemView.setTag(R.id.aa_tag_item, this)
        }.itemView

        @Suppress("UnnecessaryVariable")
        val bindingAdapterPosition = position
        val absolutePositionObject = parent.getTag(R.id.aa_tag_absoluteAdapterPosition)
        // set tag absoluteAdapterPosition null to support ConcatListAdapter nesting
        parent.setTag(R.id.aa_tag_absoluteAdapterPosition, null)
        val absoluteAdapterPosition = (absolutePositionObject as Int?) ?: bindingAdapterPosition

        @Suppress("UNCHECKED_CAST")
        val item = itemView.getTag(R.id.aa_tag_item) as Item<Any>
        item.dispatchBindData(bindingAdapterPosition, absoluteAdapterPosition, data)
        return itemView
    }


    override fun getItemFactoryByPosition(position: Int): ItemFactory<DATA> {
        val data = getItemData(position)
        return itemFactoryStorage.getItemFactoryByData(
            data, "ItemFactory", "AssemblyListAdapter", "itemFactoryList"
        )
    }

    override fun registerDataSetObserver(observer: DataSetObserver) {
        super.registerDataSetObserver(observer)
        adapterDataObservable.registerObserver(observer)
    }

    override fun unregisterDataSetObserver(observer: DataSetObserver) {
        super.unregisterDataSetObserver(observer)
        adapterDataObservable.unregisterObserver(observer)
    }

    /**
     * Returns true if one or more observers are attached to this adapter.
     *
     * @return true if this adapter has observers
     */
    fun hasObservers(): Boolean {
        return adapterDataObservable.hasObservers()
    }
}