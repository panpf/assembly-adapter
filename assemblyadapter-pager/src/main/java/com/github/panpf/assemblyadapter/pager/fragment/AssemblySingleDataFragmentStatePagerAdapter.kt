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
package com.github.panpf.assemblyadapter.pager.fragment

import androidx.annotation.IntDef
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.github.panpf.assemblyadapter.AssemblyAdapter
import com.github.panpf.assemblyadapter.pager.AbsoluteAdapterPositionAdapter

@Deprecated(
    message = "Switch to 'androidx.viewpager2.widget.ViewPager2' and use 'com.github.panpf.assemblyadapter.pager2.AssemblySingleDataFragmentStateAdapter' instead.",
    replaceWith = ReplaceWith(
        "AssemblySingleDataFragmentStateAdapter(itemFactory)",
        "com.github.panpf.assemblyadapter.pager2.AssemblySingleDataFragmentStateAdapter"
    )
)
open class AssemblySingleDataFragmentStatePagerAdapter<DATA : Any> :
    FragmentStatePagerAdapter, AssemblyAdapter<FragmentItemFactory<*>>, AbsoluteAdapterPositionAdapter {

    private val itemFactory: FragmentItemFactory<DATA>
    private var refreshHelper: FragmentPagerAdapterRefreshHelper? =
        FragmentPagerAdapterRefreshHelper()
    private var nextItemAbsoluteAdapterPosition: Int? = null

    var data: DATA? = null
        set(value) {
            field = value
            notifyDataSetChanged()
        }
    var isDisableItemRefreshWhenDataSetChanged: Boolean
        get() = refreshHelper != null
        set(disable) {
            if (disable != isDisableItemRefreshWhenDataSetChanged) {
                refreshHelper = if (disable) null else FragmentPagerAdapterRefreshHelper()
            }
        }

    constructor(
        fm: FragmentManager,
        @Behavior behavior: Int,
        itemFactory: FragmentItemFactory<DATA>,
    ) : super(fm, behavior) {
        this.itemFactory = itemFactory
    }

    @Deprecated(
        """use {@link #AssemblyFragmentPagerAdapter(FragmentManager, int)} with
      {@link #BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT}"""
    )
    constructor(
        fm: FragmentManager,
        itemFactory: FragmentItemFactory<DATA>,
    ) : super(fm) {
        this.itemFactory = itemFactory
    }

    constructor(
        fm: FragmentManager,
        @Behavior behavior: Int,
        itemFactory: FragmentItemFactory<DATA>,
        initData: DATA? = null
    ) : super(fm, behavior) {
        this.itemFactory = itemFactory
        this.data = initData
    }

    @Deprecated(
        """use {@link #AssemblyFragmentPagerAdapter(FragmentManager, int, List)} with
      {@link #BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT}"""
    )
    constructor(
        fm: FragmentManager,
        itemFactory: FragmentItemFactory<DATA>,
        initData: DATA? = null
    ) : super(fm) {
        this.itemFactory = itemFactory
        this.data = initData
    }

    override fun getCount(): Int = if (data != null) 1 else 0

    override fun getItem(position: Int): Fragment {
        @Suppress("UnnecessaryVariable") val bindingAdapterPosition = position
        val absoluteAdapterPosition = nextItemAbsoluteAdapterPosition ?: bindingAdapterPosition

        @Suppress("UNCHECKED_CAST")
        val itemFactory = itemFactory as FragmentItemFactory<Any>
        return itemFactory.dispatchCreateFragment(
            bindingAdapterPosition, absoluteAdapterPosition, this.data!!
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
        return itemFactory
    }


    override fun setNextItemAbsoluteAdapterPosition(absoluteAdapterPosition: Int) {
        this.nextItemAbsoluteAdapterPosition = absoluteAdapterPosition
    }


    @kotlin.annotation.Retention(AnnotationRetention.SOURCE)
    @IntDef(BEHAVIOR_SET_USER_VISIBLE_HINT, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT)
    private annotation class Behavior
}