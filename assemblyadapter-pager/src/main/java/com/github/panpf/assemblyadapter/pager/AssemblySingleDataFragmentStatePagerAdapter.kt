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
import com.github.panpf.assemblyadapter.AssemblyAdapter
import com.github.panpf.assemblyadapter.internal.ItemFactoryStorage
import com.github.panpf.assemblyadapter.pager.internal.AbsoluteAdapterPositionAdapter
import com.github.panpf.assemblyadapter.pager.refreshable.RefreshableFragmentStatePagerAdapter

/**
 * Single data version of [AssemblyFragmentStatePagerAdapter]
 *
 * @param itemFactory Can match [data]'s [FragmentItemFactory]
 * @param initData Initial data
 * @see FragmentItemFactory
 */
@Deprecated(
    message = "Switch to 'androidx.viewpager2.widget.ViewPager2' and use 'com.github.panpf.assemblyadapter.pager2.AssemblySingleDataFragmentStateAdapter' instead.",
    replaceWith = ReplaceWith(
        "AssemblySingleDataFragmentStateAdapter(itemFactory)",
        "com.github.panpf.assemblyadapter.pager2.AssemblySingleDataFragmentStateAdapter"
    )
)
open class AssemblySingleDataFragmentStatePagerAdapter<DATA : Any>(
    fm: FragmentManager,
    @Behavior behavior: Int,
    itemFactory: FragmentItemFactory<DATA>,
    initData: DATA? = null
) : RefreshableFragmentStatePagerAdapter<DATA>(fm, behavior),
    AssemblyAdapter<FragmentItemFactory<*>>,
    AbsoluteAdapterPositionAdapter {

    private val itemFactoryStorage = ItemFactoryStorage(listOf(itemFactory))

    override var nextItemAbsoluteAdapterPosition: Int? = null

    /**
     * The only data of the current adapter, [notifyDataSetChanged] will be triggered when the data changes
     */
    var data: DATA? = initData
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    /**
     * Get the current page title.
     */
    var currentPageTitle: CharSequence? = null
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    @Deprecated(
        """use {@link #AssemblyFragmentPagerAdapter(FragmentManager, int, List)} with
      {@link #BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT}"""
    )
    constructor(
        fm: FragmentManager,
        itemFactory: FragmentItemFactory<DATA>,
        initData: DATA? = null
    ) : this(fm, BEHAVIOR_SET_USER_VISIBLE_HINT, itemFactory, initData)

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

    override fun getFragment(position: Int): Fragment {
        val data = getItemData(position)
        @Suppress("UnnecessaryVariable") val bindingAdapterPosition = position
        val absoluteAdapterPosition = nextItemAbsoluteAdapterPosition ?: bindingAdapterPosition
        // set nextItemAbsoluteAdapterPosition null to support ConcatFragmentStatePagerAdapter nesting
        nextItemAbsoluteAdapterPosition = null

        @Suppress("UNCHECKED_CAST")
        return getItemFactoryByPosition(position).dispatchCreateFragment(
            bindingAdapterPosition, absoluteAdapterPosition, data
        )
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return if (position == 0) {
            val currentPageTitle = currentPageTitle
            if (currentPageTitle != null) {
                currentPageTitle
            } else {
                val data = data
                if (data is GetPageTitle) data.pageTitle else null
            }
        } else {
            null
        }
    }


    override fun getItemFactoryByPosition(position: Int): FragmentItemFactory<DATA> {
        val data = getItemData(position)
        return itemFactoryStorage.getItemFactoryByData(
            data, "ItemFactory", "AssemblyRecyclerAdapter", "itemFactoryList"
        )
    }


    @kotlin.annotation.Retention(AnnotationRetention.SOURCE)
    @IntDef(BEHAVIOR_SET_USER_VISIBLE_HINT, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT)
    private annotation class Behavior
}