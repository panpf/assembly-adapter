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
import androidx.fragment.app.FragmentPagerAdapter

@Deprecated(
    message = "Switch to 'androidx.viewpager2.widget.ViewPager2' and use 'com.github.panpf.assemblyadapter.pager2.ArrayFragmentStateAdapter' instead.",
    replaceWith = ReplaceWith(
        "ArrayFragmentStateAdapter",
        "com.github.panpf.assemblyadapter.pager2.ArrayFragmentStateAdapter"
    )
)
open class ArrayFragmentPagerAdapter : FragmentPagerAdapter {

    private var fragmentList: List<Fragment>
    private var pageTitleList: List<CharSequence>? = null
    private var refreshHelper: FragmentPagerAdapterRefreshHelper? =
        FragmentPagerAdapterRefreshHelper()

    var isDisableItemRefreshWhenDataSetChanged: Boolean
        get() = refreshHelper != null
        set(disable) {
            if (disable != isDisableItemRefreshWhenDataSetChanged) {
                refreshHelper = if (disable) null else FragmentPagerAdapterRefreshHelper()
            }
        }

    constructor(
        fm: FragmentManager, @Behavior behavior: Int, fragments: List<Fragment>
    ) : super(fm, behavior) {
        fragmentList = fragments.toList()
    }

    @Deprecated("use {@link #FragmentArrayPagerAdapter(FragmentManager, int, List)} with {@link #BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT}")
    constructor(fm: FragmentManager, fragments: List<Fragment>) : super(fm) {
        fragmentList = fragments.toList()
    }


    override fun getCount(): Int {
        return fragmentList.size
    }

    override fun getItem(position: Int): Fragment {
        return fragmentList[position].apply {
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

    override fun getPageTitle(position: Int): CharSequence? {
        return pageTitleList?.getOrNull(position)
    }


    open fun getFragmentsSnapshot(): List<Fragment> {
        return fragmentList.toList()
    }

    open fun setFragments(fragments: List<Fragment>?) {
        fragmentList = fragments?.toList() ?: emptyList()
        notifyDataSetChanged()
    }

    open fun getPageTitlesSnapshot(): List<CharSequence> {
        return pageTitleList?.toList() ?: emptyList()
    }

    open fun setPageTitles(pageTitles: List<CharSequence>?) {
        pageTitleList = pageTitles?.toList() ?: emptyList()
    }


    @kotlin.annotation.Retention(AnnotationRetention.SOURCE)
    @IntDef(BEHAVIOR_SET_USER_VISIBLE_HINT, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT)
    private annotation class Behavior
}