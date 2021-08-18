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
import com.github.panpf.assemblyadapter.pager.internal.FragmentStatePagerAdapterRefreshHelper
import java.util.*

/**
 * An implementation of [FragmentStatePagerAdapter], The data is provided by the [Fragment] list passed in from the outside.
 *
 * Warning: The [getItem] method will not directly return the [Fragment] from [currentList], but uses it as a template to create a new Fragment
 */
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
    private var refreshHelper: FragmentStatePagerAdapterRefreshHelper? =
        FragmentStatePagerAdapterRefreshHelper()

    /**
     * Get the current list. If a null list is submitted through [submitList], or no list is submitted, an empty list will be returned.
     * The returned list may not change-changes to the content must be passed through [submitList].
     */
    val currentList: List<Fragment>
        get() = itemDataStorage.readOnlyList

    /**
     * Get the current page title list. If a null list is submitted through [submitPageTitleList], or no list is submitted, an empty list will be returned.
     * The returned list may not change-changes to the content must be passed through [submitPageTitleList].
     */
    val currentPageTitleList: List<CharSequence>
        get() = pageTitleStorage?.readOnlyList ?: Collections.emptyList()

    /**
     * Disable the function of refreshing item when the data set changes.
     *
     * By default, [FragmentStatePagerAdapter] will not refresh the item when the dataset changes.
     *
     * [ArrayFragmentStatePagerAdapter] triggers the refresh of the item by letting the [getItemPosition]
     * method return POSITION_NONE when the dataset changes.
     */
    var isDisableItemRefreshWhenDataSetChanged: Boolean
        get() = refreshHelper != null
        set(disable) {
            if (disable != isDisableItemRefreshWhenDataSetChanged) {
                refreshHelper = if (disable) null else FragmentStatePagerAdapterRefreshHelper()
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
    open fun submitList(list: List<Fragment>?) {
        itemDataStorage.submitList(list)
    }

    /**
     * Set the new page title list to be displayed.
     */
    open fun submitPageTitleList(pageTitleList: List<CharSequence>?) {
        (pageTitleStorage ?: ItemDataStorage<CharSequence>() {
            notifyDataSetChanged()
        }.apply {
            this@ArrayFragmentStatePagerAdapter.pageTitleStorage = this
        }).submitList(pageTitleList)
    }


    override fun getCount(): Int {
        return itemDataStorage.dataCount
    }

    override fun getItem(position: Int): Fragment {
        // Keep the characteristics consistent with ArrayFragmentStateAdapter
        val templateFragment = itemDataStorage.getData(position)
        return templateFragment.javaClass.newInstance().apply {
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