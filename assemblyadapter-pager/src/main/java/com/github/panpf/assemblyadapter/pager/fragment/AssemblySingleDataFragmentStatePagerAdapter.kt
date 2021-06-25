/*
 * Copyright (C) 2017 Peng fei Pan <sky@panpf.me>
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

@Deprecated(
    message = "Switch to 'androidx.viewpager2.widget.ViewPager2' and use 'com.github.panpf.assemblyadapter.pager2.AssemblySingleDataFragmentStateAdapter' instead.",
    replaceWith = ReplaceWith(
        "AssemblySingleDataFragmentStateAdapter(itemFactory)",
        "com.github.panpf.assemblyadapter.pager2.AssemblySingleDataFragmentStateAdapter"
    )
)
class AssemblySingleDataFragmentStatePagerAdapter<DATA> :
    FragmentStatePagerAdapter, AssemblyAdapter {

    private val itemFactory: AssemblyFragmentItemFactory<DATA>
    private var refreshHelper: FragmentPagerAdapterRefreshHelper? =
        FragmentPagerAdapterRefreshHelper()

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
        itemFactory: AssemblyFragmentItemFactory<DATA>,
    ) : super(fm, behavior) {
        this.itemFactory = itemFactory
    }

    @Deprecated(
        """use {@link #AssemblyFragmentPagerAdapter(FragmentManager, int)} with
      {@link #BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT}"""
    )
    constructor(
        fm: FragmentManager,
        itemFactory: AssemblyFragmentItemFactory<DATA>,
    ) : super(fm) {
        this.itemFactory = itemFactory
    }

    constructor(
        fm: FragmentManager,
        @Behavior behavior: Int,
        itemFactory: AssemblyFragmentItemFactory<DATA>,
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
        itemFactory: AssemblyFragmentItemFactory<DATA>,
        initData: DATA? = null
    ) : super(fm) {
        this.itemFactory = itemFactory
        this.data = initData
    }

    override fun getCount(): Int = if (data != null) 1 else 0

    override fun getItem(position: Int): Fragment {
        val data = data

        @Suppress("UNCHECKED_CAST")
        val itemFactory = itemFactory as AssemblyFragmentItemFactory<Any>
        return itemFactory.dispatchCreateFragment(position, data).apply {
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


    override fun getItemFactoryByPosition(position: Int): AssemblyFragmentItemFactory<*> {
        return itemFactory
    }


    @kotlin.annotation.Retention(AnnotationRetention.SOURCE)
    @IntDef(BEHAVIOR_SET_USER_VISIBLE_HINT, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT)
    private annotation class Behavior
}