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
package com.github.panpf.assemblyadapter.pager.internal

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Parcelable
import android.view.ViewGroup
import com.github.panpf.assemblyadapter.pager.ConcatPagerAdapter
import com.github.panpf.assemblyadapter.pager.R
import com.github.panpf.assemblyadapter.pager.refreshable.GetItemDataPagerAdapter
import java.util.*

/**
 * All logic for the [ConcatPagerAdapter] is here so that we can clearly see a separation
 * between an adapter implementation and merging logic.
 */
internal class ConcatPagerAdapterController(
    private val mConcatAdapter: ConcatPagerAdapter,
    adapters: List<GetItemDataPagerAdapter<*>>
) : NestedPagerAdapterWrapper.Callback {

    private val mWrappers = ArrayList<NestedPagerAdapterWrapper>()

    // keep one of these around so that we can return wrapper & position w/o allocation ¯\_(ツ)_/¯
    private var mReusableHolder = PagerWrapperAndLocalPosition()

    val copyOfAdapters: List<GetItemDataPagerAdapter<*>>
        get() {
            if (mWrappers.isEmpty()) {
                return emptyList()
            }
            val adapters: MutableList<GetItemDataPagerAdapter<*>> = ArrayList(mWrappers.size)
            for (wrapper in mWrappers) {
                adapters.add(wrapper.adapter)
            }
            return adapters
        }

    init {
        for (adapter in adapters) {
            addAdapter(adapter)
        }
    }

    fun getData(globalPosition: Int): Any? {
        val (localAdapter, localPosition) = findLocalAdapterAndPosition(globalPosition)
        return localAdapter.getItemData(localPosition)
    }

    private fun findWrapperFor(adapter: GetItemDataPagerAdapter<*>): NestedPagerAdapterWrapper? {
        val index = indexOfWrapper(adapter)
        return if (index == -1) {
            null
        } else mWrappers[index]
    }

    private fun indexOfWrapper(adapter: GetItemDataPagerAdapter<*>): Int {
        val limit = mWrappers.size
        for (i in 0 until limit) {
            if (mWrappers[i].adapter === adapter) {
                return i
            }
        }
        return -1
    }

    /**
     * return true if added, false otherwise.
     *
     * @see ConcatPagerAdapter.addAdapter
     */
    fun addAdapter(adapter: GetItemDataPagerAdapter<*>): Boolean {
        return addAdapter(mWrappers.size, adapter)
    }

    /**
     * return true if added, false otherwise.
     * throws exception if index is out of bounds
     *
     * @see ConcatPagerAdapter.addAdapter
     */
    @SuppressLint("RestrictedApi")
    fun addAdapter(index: Int, adapter: GetItemDataPagerAdapter<*>): Boolean {
        if (index < 0 || index > mWrappers.size) {
            throw IndexOutOfBoundsException(
                "Index must be between 0 and ${mWrappers.size}. Given:$index"
            )
        }
        val existing = findWrapperFor(adapter)
        if (existing != null) {
            return false
        }
        val wrapper = NestedPagerAdapterWrapper(adapter, this)
        mWrappers.add(index, wrapper)
        // new items, notify add for them
        if (wrapper.cachedItemCount > 0) {
            mConcatAdapter.notifyDataSetChanged()
        }
        return true
    }

    fun removeAdapter(adapter: GetItemDataPagerAdapter<*>): Boolean {
        val index = indexOfWrapper(adapter)
        if (index == -1) {
            return false
        }
        val wrapper = mWrappers[index]
        mWrappers.removeAt(index)
        mConcatAdapter.notifyDataSetChanged()
        wrapper.dispose()
        return true
    }

    override fun onChanged(wrapper: NestedPagerAdapterWrapper) {
        mConcatAdapter.notifyDataSetChanged()
    }

    // should we cache this as well ?
    val totalCount: Int
        get() {
            // should we cache this as well ?
            var total = 0
            for (wrapper in mWrappers) {
                total += wrapper.cachedItemCount
            }
            return total
        }

    fun instantiateItem(container: ViewGroup, globalPosition: Int): Any {
        // tag absoluteAdapterPosition must be null to support ConcatPagerAdapter nesting
        if (container.getTag(R.id.aa_tag_absoluteAdapterPosition) == null) {
            container.setTag(R.id.aa_tag_absoluteAdapterPosition, globalPosition)
        }

        val wrapperAndPos = findWrapperAndLocalPositionInternal(globalPosition)
        val wrapperAdapter = wrapperAndPos.mWrapper!!.adapter
        val itemView = wrapperAdapter.instantiateItem(container, wrapperAndPos.mLocalPosition)
        releaseWrapperAndLocalPosition(wrapperAndPos)
        return itemView
    }

    fun destroyItem(container: ViewGroup, globalPosition: Int, `object`: Any) {
        val wrapperAndPos = findWrapperAndLocalPositionInternal(globalPosition)
        wrapperAndPos.mWrapper!!.adapter.destroyItem(
            container,
            wrapperAndPos.mLocalPosition,
            `object`
        )
        releaseWrapperAndLocalPosition(wrapperAndPos)
    }

    fun startUpdate(container: ViewGroup) {
        for (mWrapper in mWrappers) {
            mWrapper.adapter.startUpdate(container)
        }
    }

    fun finishUpdate(container: ViewGroup) {
        for (mWrapper in mWrappers) {
            mWrapper.adapter.finishUpdate(container)
        }
    }

    fun setPrimaryItem(container: ViewGroup, globalPosition: Int, `object`: Any) {
        val wrapperAndPos = findWrapperAndLocalPositionInternal(globalPosition)
        wrapperAndPos.mWrapper!!.adapter.setPrimaryItem(
            container,
            wrapperAndPos.mLocalPosition,
            `object`
        )
        releaseWrapperAndLocalPosition(wrapperAndPos)
    }

    fun saveState(): Parcelable {
        val states = arrayOfNulls<Parcelable>(mWrappers.size)
        for (i in mWrappers.indices) {
            states[i] = mWrappers[i].adapter.saveState()
        }
        val bundle = Bundle()
        bundle.putParcelableArray("states", states)
        return bundle
    }

    fun restoreState(state: Parcelable?, loader: ClassLoader?) {
        val bundle = state as Bundle?
        if (bundle != null) {
            bundle.classLoader = loader
            val states = bundle.getParcelableArray("states")
            if (states != null && states.size == mWrappers.size) {
                for (i in states.indices) {
                    mWrappers[i].adapter.restoreState(states[i], loader)
                }
            }
        }
    }

    fun getPageTitle(globalPosition: Int): CharSequence? {
        val wrapperAndPos = findWrapperAndLocalPositionInternal(globalPosition)
        val pageTitle = wrapperAndPos.mWrapper!!.adapter.getPageTitle(wrapperAndPos.mLocalPosition)
        releaseWrapperAndLocalPosition(wrapperAndPos)
        return pageTitle
    }

    fun getPageWidth(globalPosition: Int): Float {
        val wrapperAndPos = findWrapperAndLocalPositionInternal(globalPosition)
        val pageWidth = wrapperAndPos.mWrapper!!.adapter.getPageWidth(wrapperAndPos.mLocalPosition)
        releaseWrapperAndLocalPosition(wrapperAndPos)
        return pageWidth
    }

    fun findLocalAdapterAndPosition(globalPosition: Int): Pair<GetItemDataPagerAdapter<*>, Int> {
        var localPosition = globalPosition
        for (wrapper in mWrappers) {
            if (wrapper.cachedItemCount > localPosition) {
                return wrapper.adapter to localPosition
            }
            localPosition -= wrapper.cachedItemCount
        }
        throw IllegalArgumentException("Cannot find local adapter for $globalPosition")
    }

    /**
     * Always call [.releaseWrapperAndLocalPosition] when you are
     * done with it
     */
    private fun findWrapperAndLocalPositionInternal(globalPosition: Int): PagerWrapperAndLocalPosition {
        val result: PagerWrapperAndLocalPosition
        if (mReusableHolder.mInUse) {
            result = PagerWrapperAndLocalPosition()
        } else {
            mReusableHolder.mInUse = true
            result = mReusableHolder
        }
        var localPosition = globalPosition
        for (wrapper in mWrappers) {
            if (wrapper.cachedItemCount > localPosition) {
                result.mWrapper = wrapper
                result.mLocalPosition = localPosition
                break
            }
            localPosition -= wrapper.cachedItemCount
        }
        requireNotNull(result.mWrapper) { "Cannot find wrapper for $globalPosition" }
        return result
    }

    private fun releaseWrapperAndLocalPosition(wrapperAndLocalPosition: PagerWrapperAndLocalPosition) {
        wrapperAndLocalPosition.mInUse = false
        wrapperAndLocalPosition.mWrapper = null
        wrapperAndLocalPosition.mLocalPosition = -1
        mReusableHolder = wrapperAndLocalPosition
    }

    /**
     * Helper class to hold onto wrapper and local position without allocating objects as this is
     * a very common call.
     */
    class PagerWrapperAndLocalPosition {
        internal var mWrapper: NestedPagerAdapterWrapper? = null
        internal var mLocalPosition = 0
        internal var mInUse = false
    }
}