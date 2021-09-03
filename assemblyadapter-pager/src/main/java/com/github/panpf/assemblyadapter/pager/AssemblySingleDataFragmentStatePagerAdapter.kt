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
    private val itemFactory: FragmentItemFactory<DATA>,
    initData: DATA? = null
) : RefreshableFragmentStatePagerAdapter(fm, behavior),
    AssemblyAdapter<FragmentItemFactory<*>>,
    AbsoluteAdapterPositionAdapter {

    override var nextItemAbsoluteAdapterPosition: Int? = null

    /**
     * The only data of the current adapter, [notifyDataSetChanged] will be triggered when the data changes
     */
    var data: DATA? = initData
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

    override fun getCount(): Int = if (data != null) 1 else 0

    override fun getItemData(position: Int): Any {
        val count = count
        if (position < 0 || position >= count) {
            throw IndexOutOfBoundsException("Index: $position, Size: $count")
        }
        return data!!
    }

    override fun getFragment(position: Int): Fragment {
        val count = count
        if (position < 0 || position >= count) {
            throw IndexOutOfBoundsException("Index: $position, Size: $count")
        }

        val data = data!!
        @Suppress("UnnecessaryVariable") val bindingAdapterPosition = position
        val absoluteAdapterPosition = nextItemAbsoluteAdapterPosition ?: bindingAdapterPosition
        // set nextItemAbsoluteAdapterPosition null to support ConcatFragmentStatePagerAdapter nesting
        nextItemAbsoluteAdapterPosition = null

        @Suppress("UNCHECKED_CAST")
        val itemFactory = itemFactory as FragmentItemFactory<Any>
        return itemFactory.dispatchCreateFragment(
            bindingAdapterPosition, absoluteAdapterPosition, data
        )
    }


    override fun getItemFactoryByPosition(position: Int): FragmentItemFactory<*> {
        val count = count
        if (position < 0 || position >= count) {
            throw IndexOutOfBoundsException("Index: $position, Size: $count")
        }
        return itemFactory
    }


    @kotlin.annotation.Retention(AnnotationRetention.SOURCE)
    @IntDef(BEHAVIOR_SET_USER_VISIBLE_HINT, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT)
    private annotation class Behavior
}