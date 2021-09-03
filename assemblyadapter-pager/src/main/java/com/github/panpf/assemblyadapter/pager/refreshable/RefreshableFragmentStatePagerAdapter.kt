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
package com.github.panpf.assemblyadapter.pager.refreshable

import androidx.annotation.IntDef
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter

abstract class RefreshableFragmentStatePagerAdapter<DATA>(
    fragmentManager: FragmentManager,
    @Behavior behavior: Int
) : GetItemDataFragmentStatePagerAdapter<DATA>(fragmentManager, behavior) {

    private var refreshHelper: PagerAdapterRefreshHelper<DATA>? = PagerAdapterRefreshHelper(this)

    /**
     * Disable the function of refreshing item when the data set changes.
     *
     * By default, [FragmentStatePagerAdapter] will not refresh the item when the dataset changes.
     *
     * [RefreshableFragmentStatePagerAdapter] triggers the refresh of the item by letting the [getItemPosition]
     * method return POSITION_NONE when the dataset changes.
     */
    var isDisableItemRefreshWhenDataSetChanged: Boolean
        get() = refreshHelper != null
        set(disable) {
            if (disable != isDisableItemRefreshWhenDataSetChanged) {
                refreshHelper = if (disable) null else PagerAdapterRefreshHelper(this)
                notifyDataSetChanged()
            }
        }

    @Deprecated(
        """use {@link #FragmentArrayStatePagerAdapter(FragmentManager, int, List)} with
      {@link #BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT}"""
    )
    constructor(fm: FragmentManager) : this(fm, BEHAVIOR_SET_USER_VISIBLE_HINT)

    final override fun getItem(position: Int): Fragment {
        return getFragment(position).apply {
            refreshHelper?.bindPositionAndData(this, position, getItemData(position))
        }
    }

    abstract fun getFragment(position: Int): Fragment

    override fun getItemPosition(item: Any): Int {
        if (refreshHelper?.isItemPositionChanged(item as Fragment) == true) {
            return POSITION_NONE
        }
        return super.getItemPosition(item)
    }


    @kotlin.annotation.Retention(AnnotationRetention.SOURCE)
    @IntDef(BEHAVIOR_SET_USER_VISIBLE_HINT, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT)
    private annotation class Behavior
}