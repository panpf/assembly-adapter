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
import androidx.viewpager.widget.PagerAdapter
import com.github.panpf.assemblyadapter.AssemblyAdapter
import com.github.panpf.assemblyadapter.Placeholder
import com.github.panpf.assemblyadapter.internal.ItemDataStorage
import com.github.panpf.assemblyadapter.internal.ItemFactoryStorage
import com.github.panpf.assemblyadapter.pager.refreshable.RefreshablePagerAdapter

/**
 * An implementation of [PagerAdapter], which implements multi-type adapters through standardized [PagerItemFactory].
 * [AssemblyPagerAdapter] will use the data corresponding to position to find a matching [PagerItemFactory] (cannot find an exception will be thrown),
 * and then use [PagerItemFactory] to create an item view
 *
 * @param itemFactoryList The collection of [PagerItemFactory] passed in from outside, cannot be empty.
 * Each type of data in the data set must have a matching [PagerItemFactory], otherwise an exception will be thrown
 * @param initDataList Initial data set
 * @see PagerItemFactory
 */
open class AssemblyPagerAdapter<DATA>(
    itemFactoryList: List<PagerItemFactory<*>>,
    initDataList: List<DATA>? = null
) : RefreshablePagerAdapter(), AssemblyAdapter<PagerItemFactory<*>> {

    private val itemFactoryStorage = ItemFactoryStorage(itemFactoryList)
    private val itemDataStorage = ItemDataStorage(initDataList) { notifyDataSetChanged() }

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
    fun submitList(list: List<DATA>?) {
        itemDataStorage.submitList(list)
    }

    override fun getCount(): Int {
        return itemDataStorage.dataCount
    }

    override fun getItemData(position: Int): Any {
        return itemDataStorage.getData(position) ?: Placeholder
    }

    override fun getView(container: ViewGroup, position: Int): View {
        val data = itemDataStorage.getData(position) ?: Placeholder

        @Suppress("UnnecessaryVariable") val bindingAdapterPosition = position
        val absolutePositionObject = container.getTag(R.id.aa_tag_absoluteAdapterPosition)
        // set tag absoluteAdapterPosition null to support ConcatPagerAdapter nesting
        container.setTag(R.id.aa_tag_absoluteAdapterPosition, null)
        val absoluteAdapterPosition = (absolutePositionObject as Int?) ?: bindingAdapterPosition

        @Suppress("UNCHECKED_CAST")
        val itemFactory = itemFactoryStorage.getItemFactoryByData(
            data, "PagerItemFactory", "AssemblyPagerAdapter", "itemFactoryList"
        ) as PagerItemFactory<Any>
        return itemFactory.dispatchCreateItemView(
            container.context, container, bindingAdapterPosition, absoluteAdapterPosition, data
        )
    }


    override fun getItemFactoryByPosition(position: Int): PagerItemFactory<*> {
        val data = itemDataStorage.getData(position) ?: Placeholder
        return itemFactoryStorage.getItemFactoryByData(
            data, "PagerItemFactory", "AssemblyPagerAdapter", "itemFactoryList"
        )
    }
}