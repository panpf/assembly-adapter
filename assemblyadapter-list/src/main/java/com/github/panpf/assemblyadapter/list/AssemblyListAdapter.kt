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

import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.github.panpf.assemblyadapter.AssemblyAdapter
import com.github.panpf.assemblyadapter.ItemFactory
import com.github.panpf.assemblyadapter.Placeholder
import com.github.panpf.assemblyadapter.internal.ItemDataStorage
import com.github.panpf.assemblyadapter.internal.ItemFactoryStorage

open class AssemblyListAdapter<DATA>(
    itemFactoryList: List<ItemFactory<*>>,
    dataList: List<DATA>? = null
) : BaseAdapter(), AssemblyAdapter<ItemFactory<*>> {

    private val itemFactoryStorage = ItemFactoryStorage(itemFactoryList)
    private val itemDataStorage = ItemDataStorage(dataList) { notifyDataSetChanged() }
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

    override fun getItem(position: Int): DATA {
        return itemDataStorage.getData(position)
    }

    override fun getCount(): Int {
        return itemDataStorage.dataCount
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getViewTypeCount(): Int {
        return itemFactoryStorage.itemTypeCount
    }

    override fun getItemViewType(position: Int): Int {
        val data = itemDataStorage.getData(position) ?: Placeholder
        return itemFactoryStorage.getItemTypeByData(
            data, "ItemFactory", "AssemblyListAdapter", "itemFactoryList"
        )
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val data = itemDataStorage.getData(position) ?: Placeholder
        val itemView = convertView ?: itemFactoryStorage.getItemFactoryByData(
            data, "ItemFactory", "AssemblyListAdapter", "itemFactoryList"
        ).dispatchCreateItem(parent).apply {
            itemView.setTag(R.id.aa_tag_item, this)
        }.itemView

        @Suppress("UnnecessaryVariable") val bindingAdapterPosition = position
        val absolutePositionObject = parent.getTag(R.id.aa_tag_absoluteAdapterPosition)
        // set tag absoluteAdapterPosition null to support ConcatListAdapter nesting
        parent.setTag(R.id.aa_tag_absoluteAdapterPosition, null)
        val absoluteAdapterPosition = (absolutePositionObject as Int?) ?: bindingAdapterPosition

        @Suppress("UNCHECKED_CAST")
        val item = itemView.getTag(R.id.aa_tag_item) as ItemFactory.Item<Any>
        item.dispatchBindData(bindingAdapterPosition, absoluteAdapterPosition, data)

        return itemView
    }


    override fun getItemFactoryByPosition(position: Int): ItemFactory<*> {
        val data = itemDataStorage.getData(position) ?: Placeholder
        return itemFactoryStorage.getItemFactoryByData(
            data, "ItemFactory", "AssemblyListAdapter", "itemFactoryList"
        )
    }
}