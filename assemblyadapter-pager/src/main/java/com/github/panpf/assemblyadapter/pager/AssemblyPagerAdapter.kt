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
import java.util.Collections

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
    itemFactoryList: List<PagerItemFactory<out Any>>,
    initDataList: List<DATA>? = null
) : RefreshablePagerAdapter<DATA>(), AssemblyAdapter<DATA, PagerItemFactory<out Any>> {

    private val itemFactoryStorage = ItemFactoryStorage(
        itemFactoryList, "PagerItemFactory", "AssemblyPagerAdapter", "itemFactoryList"
    )
    private val itemDataStorage = ItemDataStorage(initDataList) { _, _ -> notifyDataSetChanged() }
    private var pageTitleStorage: ItemDataStorage<CharSequence>? = null

    /**
     * Get the current list. If a null list is submitted through [submitList], or no list is submitted, an empty list will be returned.
     * The returned list may not change-changes to the content must be passed through [submitList].
     */
    val currentList: List<DATA>
        get() = itemDataStorage.readOnlyList

    /**
     * Get the current page title list. If a null list is submitted through [submitPageTitleList], or no list is submitted, an empty list will be returned.
     * The returned list may not change-changes to the content must be passed through [submitPageTitleList].
     */
    val currentPageTitleList: List<CharSequence>
        get() = pageTitleStorage?.readOnlyList ?: Collections.emptyList()

    init {
        require(itemFactoryList.isNotEmpty()) { "itemFactoryList Can not be empty" }
    }

    /**
     * Set the new list to be displayed.
     */
    open fun submitList(list: List<DATA>?) {
        itemDataStorage.submitList(list)
    }

    /**
     * Set the new page title list to be displayed.
     */
    open fun submitPageTitleList(pageTitleList: List<CharSequence>?) {
        if (pageTitleList != null && pageTitleList.isNotEmpty()) {
            (pageTitleStorage ?: ItemDataStorage<CharSequence> { _, _ ->
                notifyDataSetChanged()
            }.apply {
                this@AssemblyPagerAdapter.pageTitleStorage = this
            }).submitList(pageTitleList)
        } else {
            pageTitleStorage = null
            notifyDataSetChanged()
        }
    }

    val itemCount: Int
        get() = itemDataStorage.dataCount

    override fun getItemData(position: Int): DATA {
        return itemDataStorage.getData(position)
    }

    override fun getCount(): Int {
        return itemDataStorage.dataCount
    }

    override fun getView(container: ViewGroup, position: Int): View {
        val data = getItemData(position) ?: Placeholder

        @Suppress("UnnecessaryVariable") val bindingAdapterPosition = position
        val absolutePositionObject = container.getTag(R.id.aa_tag_absoluteAdapterPosition)
        // set tag absoluteAdapterPosition null to support ConcatPagerAdapter nesting
        container.setTag(R.id.aa_tag_absoluteAdapterPosition, null)
        val absoluteAdapterPosition = (absolutePositionObject as Int?) ?: bindingAdapterPosition

        @Suppress("UNCHECKED_CAST")
        val itemFactory = itemFactoryStorage.getItemFactoryByData(data) as PagerItemFactory<Any>
        return itemFactory.dispatchCreateItemView(
            container.context, container, bindingAdapterPosition, absoluteAdapterPosition, data
        )
    }

    override fun getPageTitle(position: Int): CharSequence? {
        val pageTitleStorage = pageTitleStorage
        return if (pageTitleStorage != null) {
            pageTitleStorage.getDataOrNull(position)
        } else {
            val itemData = itemDataStorage.getDataOrNull(position)
            if (itemData is GetPageTitle) itemData.pageTitle else null
        }
    }


    override fun getItemFactoryByPosition(position: Int): PagerItemFactory<Any> {
        val data = getItemData(position) ?: Placeholder
        return itemFactoryStorage.getItemFactoryByData(data) as PagerItemFactory<Any>
    }

    override fun getItemFactoryByData(data: DATA): PagerItemFactory<Any> {
        return itemFactoryStorage.getItemFactoryByData(data ?: Placeholder) as PagerItemFactory<Any>
    }

    override fun <T : PagerItemFactory<out Any>> getItemFactoryByClass(itemFactoryClass: Class<T>): T {
        return itemFactoryStorage.getItemFactoryByClass(itemFactoryClass)
    }
}