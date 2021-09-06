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
import com.github.panpf.assemblyadapter.pager.refreshable.RefreshableFragmentStatePagerAdapter
import java.util.*

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
) : RefreshableFragmentStatePagerAdapter<DATA>(fm, behavior),
    AssemblyAdapter<FragmentItemFactory<*>>,
    AbsoluteAdapterPositionAdapter {

    private val itemFactoryStorage = ItemFactoryStorage(itemFactoryList)
    private val itemDataStorage = ItemDataStorage(initDataList) { notifyDataSetChanged() }
    private var pageTitleStorage: ItemDataStorage<CharSequence>? = null

    override var nextItemAbsoluteAdapterPosition: Int? = null

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
    fun submitList(list: List<DATA>?) {
        itemDataStorage.submitList(list)
    }

    /**
     * Set the new page title list to be displayed.
     */
    open fun submitPageTitleList(pageTitleList: List<CharSequence>?) {
        if (pageTitleList != null && pageTitleList.isNotEmpty()) {
            (pageTitleStorage ?: ItemDataStorage<CharSequence> {
                notifyDataSetChanged()
            }.apply {
                this@AssemblyFragmentStatePagerAdapter.pageTitleStorage = this
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

    override fun getFragment(position: Int): Fragment {
        val data = getItemData(position) ?: Placeholder

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


    override fun getItemFactoryByPosition(position: Int): FragmentItemFactory<*> {
        val data = getItemData(position) ?: Placeholder
        return itemFactoryStorage.getItemFactoryByData(
            data, "FragmentItemFactory", "AssemblyFragmentStatePagerAdapter", "itemFactoryList"
        )
    }


    @kotlin.annotation.Retention(AnnotationRetention.SOURCE)
    @IntDef(BEHAVIOR_SET_USER_VISIBLE_HINT, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT)
    private annotation class Behavior
}