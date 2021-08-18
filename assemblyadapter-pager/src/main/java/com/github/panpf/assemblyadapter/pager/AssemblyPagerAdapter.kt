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
import com.github.panpf.assemblyadapter.pager.internal.PagerAdapterRefreshHelper

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
) : PagerAdapter(), AssemblyAdapter<PagerItemFactory<*>> {

    private val itemFactoryStorage = ItemFactoryStorage(itemFactoryList)
    private val itemDataStorage = ItemDataStorage(initDataList) { notifyDataSetChanged() }
    private var refreshHelper: PagerAdapterRefreshHelper? = PagerAdapterRefreshHelper()

    /**
     * Disable the function of refreshing item when the data set changes.
     *
     * By default, [PagerAdapter] will not refresh the item when the dataset changes.
     *
     * [AssemblyPagerAdapter] triggers the refresh of the item by letting the [getItemPosition]
     * method return POSITION_NONE when the dataset changes.
     */
    var isDisableItemRefreshWhenDataSetChanged: Boolean
        get() = refreshHelper != null
        set(disable) {
            if (disable != isDisableItemRefreshWhenDataSetChanged) {
                refreshHelper = if (disable) null else PagerAdapterRefreshHelper()
                notifyDataSetChanged()
            }
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
    fun submitList(list: List<DATA>?) {
        itemDataStorage.submitList(list)
    }

    override fun getCount(): Int {
        return itemDataStorage.dataCount
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
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
        val itemView = itemFactory.dispatchCreateItemView(
            container.context, container, bindingAdapterPosition, absoluteAdapterPosition, data
        )
        container.addView(itemView)
        return itemView.apply {
            refreshHelper?.bindNotifyDataSetChangedNumber(this)
        }
    }

    override fun destroyItem(container: ViewGroup, position: Int, item: Any) {
        container.removeView(item as View)
    }

    override fun isViewFromObject(view: View, item: Any): Boolean {
        return view === item
    }

    override fun notifyDataSetChanged() {
        refreshHelper?.onNotifyDataSetChanged()
        super.notifyDataSetChanged()
    }

    override fun getItemPosition(item: Any): Int {
        if (refreshHelper?.isItemPositionChanged(item as View) == true) {
            return POSITION_NONE
        }
        return super.getItemPosition(item)
    }


    override fun getItemFactoryByPosition(position: Int): PagerItemFactory<*> {
        val data = itemDataStorage.getData(position) ?: Placeholder
        return itemFactoryStorage.getItemFactoryByData(
            data, "PagerItemFactory", "AssemblyPagerAdapter", "itemFactoryList"
        )
    }
}