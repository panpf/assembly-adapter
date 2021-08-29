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
import com.github.panpf.assemblyadapter.pager.internal.PagerAdapterRefreshHelper

/**
 * Single data version of [AssemblyPagerAdapter]
 *
 * @param itemFactory Can match [data]'s [PagerItemFactory]
 * @param initData Initial data
 * @see PagerItemFactory
 */
open class AssemblySingleDataPagerAdapter<DATA : Any>(
    private val itemFactory: PagerItemFactory<DATA>,
    initData: DATA? = null
) : PagerAdapter(), AssemblyAdapter<PagerItemFactory<*>> {

    private var refreshHelper: PagerAdapterRefreshHelper? = PagerAdapterRefreshHelper()

    /**
     * The only data of the current adapter, [notifyDataSetChanged] will be triggered when the data changes
     */
    var data: DATA? = initData
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    /**
     * Disable the function of refreshing item when the data set changes.
     *
     * By default, [PagerAdapter] will not refresh the item when the dataset changes.
     *
     * [AssemblySingleDataPagerAdapter] triggers the refresh of the item by letting the [getItemPosition]
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

    override fun getCount(): Int = if (data != null) 1 else 0

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val count = count
        if (position < 0 || position >= count) {
            throw IndexOutOfBoundsException("Index: $position, Size: $count")
        }
        @Suppress("UnnecessaryVariable") val bindingAdapterPosition = position
        val absolutePositionObject = container.getTag(R.id.aa_tag_absoluteAdapterPosition)
        // set tag absoluteAdapterPosition null to support ConcatPagerAdapter nesting
        container.setTag(R.id.aa_tag_absoluteAdapterPosition, null)
        val absoluteAdapterPosition = (absolutePositionObject as Int?) ?: bindingAdapterPosition

        @Suppress("UNCHECKED_CAST")
        val itemFactory = itemFactory as PagerItemFactory<Any>
        val itemView = itemFactory.dispatchCreateItemView(
            container.context, container, bindingAdapterPosition, absoluteAdapterPosition, data!!
        )
        container.addView(itemView)
        return itemView.apply {
            refreshHelper?.bindNotifyDataSetChangedNumber(this)
        }
    }

    override fun destroyItem(container: ViewGroup, position: Int, item: Any) {
        val count = count
        if (position < 0 || position >= count) {
            throw IndexOutOfBoundsException("Index: $position, Size: $count")
        }
        container.removeView(item as View)
    }

    override fun isViewFromObject(view: View, item: Any): Boolean = view === item

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
        val count = count
        if (position < 0 || position >= count) {
            throw IndexOutOfBoundsException("Index: $position, Size: $count")
        }
        return itemFactory
    }
}