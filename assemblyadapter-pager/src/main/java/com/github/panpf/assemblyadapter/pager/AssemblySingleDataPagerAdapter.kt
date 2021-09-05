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
package com.github.panpf.assemblyadapter.pager

import android.view.View
import android.view.ViewGroup
import com.github.panpf.assemblyadapter.AssemblyAdapter
import com.github.panpf.assemblyadapter.internal.ItemFactoryStorage
import com.github.panpf.assemblyadapter.pager.refreshable.RefreshablePagerAdapter

/**
 * Single data version of [AssemblyPagerAdapter]
 *
 * @param itemFactory Can match [data]'s [PagerItemFactory]
 * @param initData Initial data
 * @see PagerItemFactory
 */
open class AssemblySingleDataPagerAdapter<DATA : Any>(
    itemFactory: PagerItemFactory<DATA>,
    initData: DATA? = null
) : RefreshablePagerAdapter<DATA>(), AssemblyAdapter<PagerItemFactory<*>> {

    private val itemFactoryStorage = ItemFactoryStorage(listOf(itemFactory))

    /**
     * The only data of the current adapter, [notifyDataSetChanged] will be triggered when the data changes
     */
    var data: DATA? = initData
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    val itemCount: Int
        get() = if (data != null) 1 else 0

    override fun getItemData(position: Int): DATA {
        val count = count
        if (position < 0 || position >= count) {
            throw IndexOutOfBoundsException("Index: $position, Size: $count")
        }
        return data!!
    }

    override fun getCount(): Int = if (data != null) 1 else 0

    override fun getView(container: ViewGroup, position: Int): View {
        val data = getItemData(position)
        @Suppress("UnnecessaryVariable") val bindingAdapterPosition = position
        val absolutePositionObject = container.getTag(R.id.aa_tag_absoluteAdapterPosition)
        // set tag absoluteAdapterPosition null to support ConcatPagerAdapter nesting
        container.setTag(R.id.aa_tag_absoluteAdapterPosition, null)
        val absoluteAdapterPosition = (absolutePositionObject as Int?) ?: bindingAdapterPosition

        @Suppress("UNCHECKED_CAST")
        return getItemFactoryByPosition(position).dispatchCreateItemView(
            container.context, container, bindingAdapterPosition, absoluteAdapterPosition, data
        )
    }


    override fun getItemFactoryByPosition(position: Int): PagerItemFactory<DATA> {
        val data = getItemData(position)
        return itemFactoryStorage.getItemFactoryByData(
            data, "ItemFactory", "AssemblyRecyclerAdapter", "itemFactoryList"
        )
    }
}