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
import com.github.panpf.assemblyadapter.Placeholder
import com.github.panpf.assemblyadapter.internal.ItemDataStorage
import com.github.panpf.assemblyadapter.internal.ItemFactoryStorage
import com.github.panpf.assemblyadapter.list.internal.AdapterDataObservable

/**
 * An implementation of [BaseAdapter], which implements multi-type adapters through standardized [ItemFactory].
 * [AssemblyListAdapter] will use the data corresponding to position to find a matching [ItemFactory] (cannot find an exception will be thrown),
 * and then use [ItemFactory] to create an item view and bind the data
 *
 * @param itemFactoryList The collection of [ItemFactory] passed in from outside, cannot be empty.
 * Each type of data in the data set must have a matching [ItemFactory], otherwise an exception will be thrown
 * @param initDataList Initial data set
 * @see ItemFactory
 */
open class AssemblyListAdapter<DATA>(
    itemFactoryList: List<ItemFactory<out Any>>,
    initDataList: List<DATA>? = null,
) : BaseAdapter(), AssemblyAdapter<DATA, ItemFactory<out Any>> {

    private val itemFactoryStorage = ItemFactoryStorage(
        itemFactoryList, "ItemFactory", "AssemblyListAdapter", "itemFactoryList"
    )
    private val itemDataStorage = ItemDataStorage(initDataList) { _, _ -> notifyDataSetChanged() }
    private var hasStableIds = false
    private val adapterDataObservable = AdapterDataObservable()

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

    val itemCount: Int
        get() = itemDataStorage.dataCount

    fun getItemData(position: Int): DATA {
        return itemDataStorage.getData(position)
    }

    override fun getCount(): Int {
        return itemDataStorage.dataCount
    }

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
            val data = getItemData(position) ?: Placeholder
            if (data is ItemId) data.itemId else data.hashCode().toLong()
        } else {
            -1
        }
    }

    override fun getViewTypeCount(): Int {
        return itemFactoryStorage.itemTypeCount
    }

    override fun getItemViewType(position: Int): Int {
        val data = getItemData(position) ?: Placeholder
        return itemFactoryStorage.getItemTypeByData(data)
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val data = getItemData(position) ?: Placeholder
        val itemView = convertView ?: itemFactoryStorage.getItemFactoryByData(data)
            .dispatchCreateItem(parent).apply {
                itemView.setTag(R.id.aa_tag_item, this)
            }.itemView

        @Suppress("UnnecessaryVariable") val bindingAdapterPosition = position
        val absolutePositionObject = parent.getTag(R.id.aa_tag_absoluteAdapterPosition)
        // set tag absoluteAdapterPosition null to support ConcatListAdapter nesting
        parent.setTag(R.id.aa_tag_absoluteAdapterPosition, null)
        val absoluteAdapterPosition = (absolutePositionObject as Int?) ?: bindingAdapterPosition

        @Suppress("UNCHECKED_CAST")
        val item = itemView.getTag(R.id.aa_tag_item) as Item<Any>
        item.dispatchBindData(bindingAdapterPosition, absoluteAdapterPosition, data)

        return itemView
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