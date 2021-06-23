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
package com.github.panpf.assemblyadapter.pager

import androidx.annotation.IntDef
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.PagerAdapter

@Deprecated("Switch to {@link androidx.viewpager2.widget.ViewPager2} and use {@link com.github.panpf.assemblyadapter.pager2.AssemblyFragmentStateAdapter} instead.")
class ArrayFragmentPagerAdapter : FragmentPagerAdapter {

    private var fragmentList: List<Fragment>
    private var pageTitleList: List<CharSequence>? = null
    private val itemPositionChangedHelper = PagerAdapterItemPositionChangedHelper()

    var isEnabledPositionNoneOnNotifyDataSetChanged: Boolean
        get() = itemPositionChangedHelper.isEnabledPositionNoneOnNotifyDataSetChanged
        set(enabled) {
            itemPositionChangedHelper.isEnabledPositionNoneOnNotifyDataSetChanged = enabled
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
        return fragmentList[position]
    }

    override fun notifyDataSetChanged() {
        itemPositionChangedHelper.onNotifyDataSetChanged()
        super.notifyDataSetChanged()
    }

    override fun getItemPosition(item: Any): Int {
        return if (itemPositionChangedHelper.isItemPositionChanged(item)) {
            PagerAdapter.POSITION_NONE
        } else {
            super.getItemPosition(item)
        }
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return pageTitleList?.getOrNull(position)
    }


    fun getFragmentsSnapshot(): List<Fragment> {
        return fragmentList.toList()
    }

    fun setFragments(fragments: List<Fragment>?) {
        fragmentList = fragments?.toList() ?: emptyList()
        notifyDataSetChanged()
    }

    fun getPageTitlesSnapshot(): List<CharSequence> {
        return pageTitleList?.toList() ?: emptyList()
    }

    fun setPageTitles(pageTitles: List<CharSequence>?) {
        pageTitleList = pageTitles?.toList() ?: emptyList()
    }


    @kotlin.annotation.Retention(AnnotationRetention.SOURCE)
    @IntDef(BEHAVIOR_SET_USER_VISIBLE_HINT, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT)
    private annotation class Behavior
}