/*
 * Copyright 2021 panpf <panpfpanpf@outlook.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
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
import androidx.viewpager.widget.PagerAdapter
import com.github.panpf.assemblyadapter.pager.internal.AbsoluteAdapterPositionAdapter
import com.github.panpf.assemblyadapter.pager.internal.ConcatFragmentStatePagerAdapterController
import com.github.panpf.assemblyadapter.pager.refreshable.GetItemDataFragmentStatePagerAdapter
import com.github.panpf.assemblyadapter.pager.refreshable.PagerAdapterRefreshHelper
import java.util.*

/**
 * An [GetItemDataFragmentStatePagerAdapter] implementation that presents the contents of multiple adapters in sequence.
 */
@Deprecated(
    message = "Switch to 'androidx.viewpager2.widget.ViewPager2' and use 'androidx.recyclerview.widget.ConcatAdapter' instead.",
    replaceWith = ReplaceWith(
        "ConcatAdapter(adapters)",
        "androidx.recyclerview.widget.ConcatAdapter"
    )
)
open class ConcatFragmentStatePagerAdapter(
    fm: FragmentManager,
    @Behavior behavior: Int,
    adapters: List<GetItemDataFragmentStatePagerAdapter>
) : GetItemDataFragmentStatePagerAdapter(fm, behavior), AbsoluteAdapterPositionAdapter {

    /**
     * Bulk of the logic is in the controller to keep this class isolated to the public API.
     */
    private val mController: ConcatFragmentStatePagerAdapterController =
        ConcatFragmentStatePagerAdapterController(this)

    private var refreshHelper: PagerAdapterRefreshHelper? = PagerAdapterRefreshHelper(this)

    /**
     * Disable the function of refreshing item when the data set changes.
     *
     * By default, [PagerAdapter] will not refresh the item when the dataset changes.
     *
     * [ArrayPagerAdapter] triggers the refresh of the item by letting the [getItemPosition]
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

    // To support ConcatFragmentStatePagerAdapter nesting
    override var nextItemAbsoluteAdapterPosition: Int? = null

    /**
     * Returns an unmodifiable copy of the list of adapters in this [ConcatFragmentStatePagerAdapter].
     * Note that this is a copy hence future changes in the ConcatPagerAdapter are not reflected in
     * this list.
     *
     * @return A copy of the list of adapters in this ConcatPagerAdapter.
     */
    val adapters: List<GetItemDataFragmentStatePagerAdapter>
        get() = Collections.unmodifiableList(mController.copyOfAdapters)

    /**
     * Creates a ConcatPagerAdapter with the given config and the given adapters in the given order.
     *
     * @param adapters The list of adapters to add
     */
    constructor(
        fm: FragmentManager,
        @Behavior behavior: Int,
        vararg adapters: GetItemDataFragmentStatePagerAdapter
    ) : this(fm, behavior, adapters.toList())

    /**
     * Creates a ConcatPagerAdapter with the given config and the given adapters in the given order.
     *
     * @param adapters The list of adapters to add
     */
    constructor(
        fm: FragmentManager,
        adapters: List<GetItemDataFragmentStatePagerAdapter>
    ) : this(fm, BEHAVIOR_SET_USER_VISIBLE_HINT, adapters)

    /**
     * Creates a ConcatPagerAdapter with the given config and the given adapters in the given order.
     *
     * @param adapters The list of adapters to add
     */
    constructor(
        fm: FragmentManager,
        vararg adapters: GetItemDataFragmentStatePagerAdapter
    ) : this(fm, BEHAVIOR_SET_USER_VISIBLE_HINT, adapters.toList())

    init {
        for (adapter in adapters) {
            addAdapter(adapter)
        }
    }

    /**
     * Appends the given adapter to the existing list of adapters and notifies the observers of
     * this [ConcatFragmentStatePagerAdapter].
     *
     * @param adapter The new adapter to add
     * @return `true` if the adapter is successfully added because it did not already exist,
     * `false` otherwise.
     * @see .addAdapter
     * @see .removeAdapter
     */
    open fun addAdapter(adapter: GetItemDataFragmentStatePagerAdapter): Boolean {
        return mController.addAdapter(adapter)
    }

    /**
     * Adds the given adapter to the given index among other adapters that are already added.
     *
     * @param index   The index into which to insert the adapter. ConcatPagerAdapter will throw an
     * [IndexOutOfBoundsException] if the index is not between 0 and current
     * adapter count (inclusive).
     * @param adapter The new adapter to add to the adapters list.
     * @return `true` if the adapter is successfully added because it did not already exist,
     * `false` otherwise.
     * @see .addAdapter
     * @see .removeAdapter
     */
    open fun addAdapter(index: Int, adapter: GetItemDataFragmentStatePagerAdapter): Boolean {
        return mController.addAdapter(index, adapter)
    }

    /**
     * Removes the given adapter from the adapters list if it exists
     *
     * @param adapter The adapter to remove
     * @return `true` if the adapter was previously added to this `ConcatPagerAdapter` and
     * now removed or `false` if it couldn't be found.
     */
    open fun removeAdapter(adapter: GetItemDataFragmentStatePagerAdapter): Boolean {
        return mController.removeAdapter(adapter)
    }

    override fun getCount(): Int {
        return mController.totalCount
    }

    override fun getItemData(position: Int): Any {
        return mController.getData(position)
    }

    override fun getItem(position: Int): Fragment {
        return mController.getItem(position, nextItemAbsoluteAdapterPosition).apply {
            nextItemAbsoluteAdapterPosition = null
            refreshHelper?.bindPositionAndData(this, position, getItemData(position))
        }
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return mController.getPageTitle(position)
    }

    override fun getPageWidth(position: Int): Float {
        return mController.getPageWidth(position)
    }

    override fun getItemPosition(item: Any): Int {
        if (refreshHelper?.isItemPositionChanged(item as Fragment) == true) {
            return POSITION_NONE
        }
        return super.getItemPosition(item)
    }

    open fun findLocalAdapterAndPosition(position: Int): Pair<GetItemDataFragmentStatePagerAdapter, Int> {
        return mController.findLocalAdapterAndPosition(position)
    }

    @Retention(AnnotationRetention.SOURCE)
    @IntDef(BEHAVIOR_SET_USER_VISIBLE_HINT, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT)
    internal annotation class Behavior
}