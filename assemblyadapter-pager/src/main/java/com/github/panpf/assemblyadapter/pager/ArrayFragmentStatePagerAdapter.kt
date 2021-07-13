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
import com.github.panpf.assemblyadapter.internal.ItemDataStorage
import com.github.panpf.assemblyadapter.pager.internal.FragmentPagerAdapterRefreshHelper
import java.util.*

@Deprecated(
    message = "Switch to 'androidx.viewpager2.widget.ViewPager2' and use 'com.github.panpf.assemblyadapter.pager2.ArrayFragmentStateAdapter' instead.",
    replaceWith = ReplaceWith(
        "ArrayFragmentStateAdapter",
        "com.github.panpf.assemblyadapter.pager2.ArrayFragmentStateAdapter"
    )
)
open class ArrayFragmentStatePagerAdapter(
    fragmentManager: FragmentManager,
    @Behavior behavior: Int,
    templateFragmentList: List<Fragment>
) : FragmentStatePagerAdapter(fragmentManager, behavior) {

    private val itemDataStorage = ItemDataStorage(templateFragmentList) { notifyDataSetChanged() }
    private var pageTitleStorage: ItemDataStorage<CharSequence>? = null
    private var refreshHelper: FragmentPagerAdapterRefreshHelper? =
        FragmentPagerAdapterRefreshHelper()

    /**
     * Get the current list. If a null list is submitted through [submitFragmentList], or no list is submitted, an empty list will be returned.
     * The returned list may not change-changes to the content must be passed through [submitFragmentList].
     */
    val fragmentList: List<Fragment>
        get() = itemDataStorage.readOnlyDataList

    /**
     * Get the current page title list. If a null list is submitted through [submitPageTitleList], or no list is submitted, an empty list will be returned.
     * The returned list may not change-changes to the content must be passed through [submitPageTitleList].
     */
    val pageTitleList: List<CharSequence>
        get() = pageTitleStorage?.readOnlyDataList ?: Collections.emptyList()

    var isDisableItemRefreshWhenDataSetChanged: Boolean
        get() = refreshHelper != null
        set(disable) {
            if (disable != isDisableItemRefreshWhenDataSetChanged) {
                refreshHelper = if (disable) null else FragmentPagerAdapterRefreshHelper()
                notifyDataSetChanged()
            }
        }

    @Deprecated(
        """use {@link #FragmentArrayStatePagerAdapter(FragmentManager, int, List)} with
      {@link #BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT}"""
    )
    constructor(fm: FragmentManager, fragments: List<Fragment>) : this(
        fm, BEHAVIOR_SET_USER_VISIBLE_HINT, fragments
    )

    /**
     * Set the new list to be displayed.
     */
    open fun submitFragmentList(fragmentList: List<Fragment>?) {
        itemDataStorage.submitDataList(fragmentList)
    }

    /**
     * Set the new page title list to be displayed.
     */
    open fun submitPageTitleList(pageTitleList: List<CharSequence>?) {
        (pageTitleStorage ?: ItemDataStorage<CharSequence>() {
            notifyDataSetChanged()
        }.apply {
            this@ArrayFragmentStatePagerAdapter.pageTitleStorage = this
        }).submitDataList(pageTitleList)
    }


    override fun getCount(): Int {
        return itemDataStorage.dataCount
    }

    override fun getItem(position: Int): Fragment {
        // Keep the characteristics consistent with ArrayFragmentStateAdapter
        val templateFragment = itemDataStorage.getData(position)
        return templateFragment::class.java.newInstance().apply {
            arguments = templateFragment.arguments
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
        return pageTitleStorage?.getData(position)
    }


    @kotlin.annotation.Retention(AnnotationRetention.SOURCE)
    @IntDef(BEHAVIOR_SET_USER_VISIBLE_HINT, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT)
    private annotation class Behavior
}