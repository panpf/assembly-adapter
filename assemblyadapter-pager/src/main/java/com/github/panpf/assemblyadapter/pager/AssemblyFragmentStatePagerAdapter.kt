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

import androidx.annotation.IntDef
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.github.panpf.assemblyadapter.AssemblyAdapter
import com.github.panpf.assemblyadapter.Placeholder
import com.github.panpf.assemblyadapter.internal.ItemDataStorage
import com.github.panpf.assemblyadapter.internal.ItemFactoryStorage
import com.github.panpf.assemblyadapter.pager.internal.AbsoluteAdapterPositionAdapter
import com.github.panpf.assemblyadapter.pager.internal.FragmentStatePagerAdapterRefreshHelper

/**
 * An implementation of [FragmentStatePagerAdapter], which implements multi-type adapters through standardized [FragmentItemFactory].
 * [AssemblyFragmentStatePagerAdapter] will use the data corresponding to position to find a matching [FragmentItemFactory] (cannot find an exception will be thrown),
 * and then use [FragmentItemFactory] to create an [Fragment]
 *
 * @param itemFactoryList The collection of [FragmentItemFactory] passed in from outside, cannot be empty.
 * Each type of data in the data set must have a matching [FragmentItemFactory], otherwise an exception will be thrown
 * @param initDataList Initial data set
 * @see FragmentItemFactory
 */
@Deprecated(
    message = "Switch to 'androidx.viewpager2.widget.ViewPager2' and use 'com.github.panpf.assemblyadapter.pager2.AssemblyFragmentStateAdapter' instead.",
    replaceWith = ReplaceWith(
        "AssemblyFragmentStateAdapter(itemFactoryList)",
        "com.github.panpf.assemblyadapter.pager2.AssemblyFragmentStateAdapter"
    )
)
open class AssemblyFragmentStatePagerAdapter<DATA>(
    fm: FragmentManager,
    @Behavior behavior: Int,
    itemFactoryList: List<FragmentItemFactory<*>>,
    initDataList: List<DATA>? = null
) : FragmentStatePagerAdapter(fm, behavior),
    AssemblyAdapter<FragmentItemFactory<*>>,
    AbsoluteAdapterPositionAdapter {

    private val itemFactoryStorage = ItemFactoryStorage(itemFactoryList)
    private val itemDataStorage = ItemDataStorage(initDataList) { notifyDataSetChanged() }
    private var refreshHelper: FragmentStatePagerAdapterRefreshHelper? =
        FragmentStatePagerAdapterRefreshHelper()

    override var nextItemAbsoluteAdapterPosition: Int? = null

    /**
     * Disable the function of refreshing item when the data set changes.
     *
     * By default, [FragmentStatePagerAdapter] will not refresh the item when the dataset changes.
     *
     * [AssemblyFragmentStatePagerAdapter] triggers the refresh of the item by letting the [getItemPosition]
     * method return POSITION_NONE when the dataset changes.
     */
    var isDisableItemRefreshWhenDataSetChanged: Boolean
        get() = refreshHelper != null
        set(disable) {
            if (disable != isDisableItemRefreshWhenDataSetChanged) {
                refreshHelper = if (disable) null else FragmentStatePagerAdapterRefreshHelper()
                notifyDataSetChanged()
            }
        }

    /**
     * Get the current list. If a null list is submitted through [submitDataList], or no list is submitted, an empty list will be returned.
     * The returned list may not change-changes to the content must be passed through [submitDataList].
     */
    val dataList: List<DATA>
        get() = itemDataStorage.readOnlyDataList

    @Deprecated(
        """use {@link #AssemblyFragmentPagerAdapter(FragmentManager, int, List<AssemblyFragmentItemFactory<*>>, List<DATA>)} with
      {@link #BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT}"""
    )
    constructor(
        fm: FragmentManager,
        itemFactoryList: List<FragmentItemFactory<*>>,
        initDataList: List<DATA>? = null
    ) : this(fm, BEHAVIOR_SET_USER_VISIBLE_HINT, itemFactoryList, initDataList)

    init {
        require(itemFactoryList.isNotEmpty()) { "itemFactoryList Can not be empty" }
    }

    /**
     * Set the new list to be displayed.
     */
    fun submitDataList(dataList: List<DATA>?) {
        itemDataStorage.submitDataList(dataList)
    }

    override fun getCount(): Int {
        return itemDataStorage.dataCount
    }

    override fun getItem(position: Int): Fragment {
        val data = itemDataStorage.getData(position) ?: Placeholder

        @Suppress("UnnecessaryVariable") val bindingAdapterPosition = position
        val absoluteAdapterPosition = nextItemAbsoluteAdapterPosition ?: bindingAdapterPosition
        // set nextItemAbsoluteAdapterPosition null to support ConcatFragmentStatePagerAdapter nesting
        nextItemAbsoluteAdapterPosition = null

        @Suppress("UNCHECKED_CAST")
        val itemFactory = itemFactoryStorage.getItemFactoryByData(
            data, "FragmentItemFactory", "AssemblyFragmentStatePagerAdapter", "itemFactoryList"
        ) as FragmentItemFactory<Any>
        return itemFactory.dispatchCreateFragment(
            bindingAdapterPosition, absoluteAdapterPosition, data
        ).apply {
            refreshHelper?.bindNotifyDataSetChangedNumber(this)
        }
    }

    override fun notifyDataSetChanged() {
        refreshHelper?.onNotifyDataSetChanged()
        super.notifyDataSetChanged()
    }

    override fun getItemPosition(item: Any): Int {
        if (refreshHelper?.isItemPositionChanged(item as Fragment) == true) {
            return POSITION_NONE
        }
        return super.getItemPosition(item)
    }


    override fun getItemFactoryByPosition(position: Int): FragmentItemFactory<*> {
        val data = itemDataStorage.getData(position) ?: Placeholder
        return itemFactoryStorage.getItemFactoryByData(
            data, "FragmentItemFactory", "AssemblyFragmentStatePagerAdapter", "itemFactoryList"
        )
    }


    @kotlin.annotation.Retention(AnnotationRetention.SOURCE)
    @IntDef(BEHAVIOR_SET_USER_VISIBLE_HINT, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT)
    private annotation class Behavior
}