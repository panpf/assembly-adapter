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
import com.github.panpf.assemblyadapter.internal.ItemDataStorage
import com.github.panpf.assemblyadapter.pager.internal.PagerAdapterRefreshHelper
import java.util.*

/**
 * An implementation of [PagerAdapter], The data is provided by the [View] list passed in from the outside.
 */
open class ArrayPagerAdapter(viewList: List<View>) : PagerAdapter() {

    private val itemDataStorage = ItemDataStorage(viewList) { notifyDataSetChanged() }
    private var pageTitleStorage: ItemDataStorage<CharSequence>? = null
    private var refreshHelper: PagerAdapterRefreshHelper? = PagerAdapterRefreshHelper()

    /**
     * Get the current list. If a null list is submitted through [submitList], or no list is submitted, an empty list will be returned.
     * The returned list may not change-changes to the content must be passed through [submitList].
     */
    val currentList: List<View>
        get() = itemDataStorage.readOnlyList

    /**
     * Get the current page title list. If a null list is submitted through [submitPageTitleList], or no list is submitted, an empty list will be returned.
     * The returned list may not change-changes to the content must be passed through [submitPageTitleList].
     */
    val currentPageTitleList: List<CharSequence>
        get() = pageTitleStorage?.readOnlyList ?: Collections.emptyList()

    /**
     * Disable the function of refreshing item when the data set changes.
     *
     * By default, [PagerAdapter] will not refresh the item when the dataset changes.
     *
     * [ArrayPagerAdapter] triggers the refresh of the item by letting the [getItemPosition]
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
     * Set the new list to be displayed.
     */
    open fun submitList(list: List<View>?) {
        itemDataStorage.submitList(list)
    }

    /**
     * Set the new page title list to be displayed.
     */
    open fun submitPageTitleList(pageTitleList: List<CharSequence>?) {
        (pageTitleStorage ?: ItemDataStorage<CharSequence>() {
            notifyDataSetChanged()
        }.apply {
            this@ArrayPagerAdapter.pageTitleStorage = this
        }).submitList(pageTitleList)
    }


    override fun getCount(): Int {
        return itemDataStorage.dataCount
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        return itemDataStorage.getData(position).apply {
            container.addView(this)
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

    override fun getPageTitle(position: Int): CharSequence? {
        return pageTitleStorage?.getData(position)
    }
}