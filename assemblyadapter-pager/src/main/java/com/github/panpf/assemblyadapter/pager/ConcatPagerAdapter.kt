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

import android.os.Parcelable
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter
import com.github.panpf.assemblyadapter.pager.internal.ConcatPagerAdapterController
import com.github.panpf.assemblyadapter.pager.internal.PagerAdapterRefreshHelper
import java.util.*

open class ConcatPagerAdapter(adapters: List<PagerAdapter>) : PagerAdapter() {

    /**
     * Bulk of the logic is in the controller to keep this class isolated to the public API.
     */
    private val mController: ConcatPagerAdapterController = ConcatPagerAdapterController(this)
    private var refreshHelper: PagerAdapterRefreshHelper? = PagerAdapterRefreshHelper()

    var isDisableItemRefreshWhenDataSetChanged: Boolean
        get() = refreshHelper != null
        set(disable) {
            if (disable != isDisableItemRefreshWhenDataSetChanged) {
                refreshHelper = if (disable) null else PagerAdapterRefreshHelper()
            }
        }

    /**
     * Returns an unmodifiable copy of the list of adapters in this [ConcatPagerAdapter].
     * Note that this is a copy hence future changes in the ConcatPagerAdapter are not reflected in
     * this list.
     *
     * @return A copy of the list of adapters in this ConcatPagerAdapter.
     */
    val adapters: List<PagerAdapter>
        get() = Collections.unmodifiableList(mController.copyOfAdapters)

    /**
     * Creates a ConcatPagerAdapter with the given config and the given adapters in the given order.
     *
     * @param adapters The list of adapters to add
     */
    constructor(vararg adapters: PagerAdapter) : this(adapters.toList())

    init {
        for (adapter in adapters) {
            addAdapter(adapter)
        }
    }

    /**
     * Appends the given adapter to the existing list of adapters and notifies the observers of
     * this [ConcatPagerAdapter].
     *
     * @param adapter The new adapter to add
     * @return `true` if the adapter is successfully added because it did not already exist,
     * `false` otherwise.
     * @see .addAdapter
     * @see .removeAdapter
     */
    open fun addAdapter(adapter: PagerAdapter): Boolean {
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
    open fun addAdapter(index: Int, adapter: PagerAdapter): Boolean {
        return mController.addAdapter(index, adapter)
    }

    /**
     * Removes the given adapter from the adapters list if it exists
     *
     * @param adapter The adapter to remove
     * @return `true` if the adapter was previously added to this `ConcatPagerAdapter` and
     * now removed or `false` if it couldn't be found.
     */
    open fun removeAdapter(adapter: PagerAdapter): Boolean {
        return mController.removeAdapter(adapter)
    }

    override fun getCount(): Int {
        return mController.totalCount
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        return mController.instantiateItem(container, position).apply {
            refreshHelper?.bindNotifyDataSetChangedNumber(this as View)
        }
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        mController.destroyItem(container, position, `object`)
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object`
    }

    override fun startUpdate(container: ViewGroup) {
        mController.startUpdate(container)
    }

    override fun finishUpdate(container: ViewGroup) {
        mController.finishUpdate(container)
    }

    override fun setPrimaryItem(container: ViewGroup, position: Int, `object`: Any) {
        mController.setPrimaryItem(container, position, `object`)
    }

    override fun saveState(): Parcelable {
        return mController.saveState()
    }

    override fun restoreState(state: Parcelable?, loader: ClassLoader?) {
        mController.restoreState(state, loader)
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return mController.getPageTitle(position)
    }

    override fun getPageWidth(position: Int): Float {
        return mController.getPageWidth(position)
    }

    override fun notifyDataSetChanged() {
        refreshHelper?.onNotifyDataSetChanged()
        super.notifyDataSetChanged()
    }

    override fun getItemPosition(item: Any): Int {
        if (refreshHelper?.isItemPositionChanged(item as View) == true) {
            return POSITION_NONE
        }
        return super.getItemPosition(item)
    }

    open fun findLocalAdapterAndPosition(position: Int): Pair<PagerAdapter, Int> {
        return mController.findLocalAdapterAndPosition(position)
    }
}