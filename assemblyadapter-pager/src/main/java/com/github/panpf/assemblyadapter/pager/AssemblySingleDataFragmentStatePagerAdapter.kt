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
import androidx.fragment.app.FragmentManager

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
    val itemFactory: FragmentItemFactory<DATA>,
    initData: DATA? = null
) : AssemblyFragmentStatePagerAdapter<DATA>(fm, behavior, listOf(itemFactory)) {

    @Deprecated(
        """use {@link #AssemblyFragmentPagerAdapter(FragmentManager, int, List)} with
      {@link #BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT}"""
    )
    constructor(
        fm: FragmentManager,
        itemFactory: FragmentItemFactory<DATA>,
        initData: DATA? = null
    ) : this(fm, BEHAVIOR_SET_USER_VISIBLE_HINT, itemFactory, initData)

    /**
     * The only data of the current adapter, [notifyDataSetChanged] will be triggered when the data changes
     */
    var data: DATA?
        set(value) {
            if (value != null) {
                super.submitList(listOf(value))
            } else {
                super.submitList(null)
            }
        }
        get() = if (itemCount > 0) getItemData(0) else null

    /**
     * Get the current page title.
     */
    var currentPageTitle: CharSequence?
        set(value) {
            if (value != null) {
                super.submitPageTitleList(listOf(value))
            } else {
                super.submitPageTitleList(null)
            }
        }
        get() = currentPageTitleList.firstOrNull()

    init {
        if (initData != null) {
            this.data = initData
        }
    }

    override fun submitList(list: List<DATA>?) {
        require(list?.size ?: 0 <= 1) {
            "Cannot submit a list with size greater than 1"
        }
        super.submitList(list)
    }

    override fun submitPageTitleList(pageTitleList: List<CharSequence>?) {
        require(pageTitleList?.size ?: 0 <= 1) {
            "Cannot submit a pageTitleList with size greater than 1"
        }
        super.submitPageTitleList(pageTitleList)
    }


    @kotlin.annotation.Retention(AnnotationRetention.SOURCE)
    @IntDef(
        BEHAVIOR_SET_USER_VISIBLE_HINT,
        BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
    )
    private annotation class Behavior
}