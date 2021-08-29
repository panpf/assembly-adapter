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
import com.github.panpf.assemblyadapter.Item
import com.github.panpf.assemblyadapter.ItemFactory

/**
 * Single data version of [AssemblyListAdapter]
 *
 * @param itemFactory Can match [data]'s [ItemFactory]
 * @param initData Initial data
 * @see ItemFactory
 */
open class AssemblySingleDataListAdapter<DATA : Any>(
    private val itemFactory: ItemFactory<DATA>,
    initData: DATA? = null
) : BaseAdapter(), AssemblyAdapter<ItemFactory<*>> {

    var data: DATA? = initData
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun getCount(): Int = if (data != null) 1 else 0

    override fun getItem(position: Int): Any {
        val count = count
        if (position < 0 || position >= count) {
            throw IndexOutOfBoundsException("Index: $position, Size: $count")
        }
        return data!!
    }

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getViewTypeCount(): Int = 1

    override fun getItemViewType(position: Int): Int {
        val count = count
        if (position < 0 || position >= count) {
            throw IndexOutOfBoundsException("Index: $position, Size: $count")
        }
        return 0
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val count = count
        if (position < 0 || position >= count) {
            throw IndexOutOfBoundsException("Index: $position, Size: $count")
        }
        val itemView = convertView ?: itemFactory
            .dispatchCreateItem(parent).apply {
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
        item.dispatchBindData(bindingAdapterPosition, absoluteAdapterPosition, data!!)
        return itemView
    }


    override fun getItemFactoryByPosition(position: Int): ItemFactory<*> {
        val count = count
        if (position < 0 || position >= count) {
            throw IndexOutOfBoundsException("Index: $position, Size: $count")
        }
        return itemFactory
    }
}