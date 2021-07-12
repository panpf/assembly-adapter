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
package com.github.panpf.assemblyadapter.pager.fragment.concat

import android.annotation.SuppressLint
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentStatePagerAdapter
import com.github.panpf.assemblyadapter.pager.AbsoluteAdapterPositionAdapter
import java.util.*

/**
 * All logic for the [ConcatFragmentStatePagerAdapter] is here so that we can clearly see a separation
 * between an adapter implementation and merging logic.
 */
internal class ConcatFragmentStatePagerAdapterController(private val mConcatAdapter: ConcatFragmentStatePagerAdapter) :
    NestedFragmentStatePagerAdapterWrapper.Callback {

    private val mWrappers = ArrayList<NestedFragmentStatePagerAdapterWrapper>()

    // keep one of these around so that we can return wrapper & position w/o allocation ¯\_(ツ)_/¯
    private var mReusableHolder = FragmentStatePagerWrapperAndLocalPosition()

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

    val copyOfAdapters: List<FragmentStatePagerAdapter>
        get() {
            if (mWrappers.isEmpty()) {
                return emptyList()
            }
            val adapters: MutableList<FragmentStatePagerAdapter> = ArrayList(mWrappers.size)
            for (wrapper in mWrappers) {
                adapters.add(wrapper.adapter)
            }
            return adapters
        }

    private fun findWrapperFor(adapter: FragmentStatePagerAdapter): NestedFragmentStatePagerAdapterWrapper? {
        val index = indexOfWrapper(adapter)
        return if (index == -1) {
            null
        } else mWrappers[index]
    }

    private fun indexOfWrapper(adapter: FragmentStatePagerAdapter): Int {
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
     * @see ConcatFragmentStatePagerAdapter.addAdapter
     */
    fun addAdapter(adapter: FragmentStatePagerAdapter): Boolean {
        return addAdapter(mWrappers.size, adapter)
    }

    /**
     * return true if added, false otherwise.
     * throws exception if index is out of bounds
     *
     * @see ConcatFragmentStatePagerAdapter.addAdapter
     */
    @SuppressLint("RestrictedApi")
    fun addAdapter(index: Int, adapter: FragmentStatePagerAdapter): Boolean {
        if (index < 0 || index > mWrappers.size) {
            throw IndexOutOfBoundsException(
                "Index must be between 0 and ${mWrappers.size}. Given:$index"
            )
        }
        val existing = findWrapperFor(adapter)
        if (existing != null) {
            return false
        }
        val wrapper = NestedFragmentStatePagerAdapterWrapper(adapter, this)
        mWrappers.add(index, wrapper)
        // new items, notify add for them
        if (wrapper.cachedItemCount > 0) {
            mConcatAdapter.notifyDataSetChanged()
        }
        return true
    }

    fun removeAdapter(adapter: FragmentStatePagerAdapter): Boolean {
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

    override fun onChanged(wrapper: NestedFragmentStatePagerAdapterWrapper) {
        mConcatAdapter.notifyDataSetChanged()
    }

    fun getPageTitle(globalPosition: Int): CharSequence? {
        val wrapperAndPos = findWrapperAndLocalPositionInternal(globalPosition)
        val pageTitle = wrapperAndPos.mWrapper!!.adapter.getPageTitle(wrapperAndPos.mLocalPosition)
        releaseWrapperAndLocalPosition(wrapperAndPos)
        return pageTitle
    }

    fun getItem(globalPosition: Int, nextItemAbsoluteAdapterPosition: Int?): Fragment {
        val wrapperAndPos = findWrapperAndLocalPositionInternal(globalPosition)
        val wrapperAdapter = wrapperAndPos.mWrapper!!.adapter
        // wrapperAdapter.nextItemAbsoluteAdapterPosition must be null to support ConcatFragmentStatePagerAdapter nesting
        if (wrapperAdapter is AbsoluteAdapterPositionAdapter && wrapperAdapter.nextItemAbsoluteAdapterPosition == null) {
            wrapperAdapter.nextItemAbsoluteAdapterPosition =
                nextItemAbsoluteAdapterPosition ?: globalPosition
        }
        val fragment = wrapperAdapter.getItem(wrapperAndPos.mLocalPosition)
        releaseWrapperAndLocalPosition(wrapperAndPos)
        return fragment
    }

    fun getPageWidth(globalPosition: Int): Float {
        val wrapperAndPos = findWrapperAndLocalPositionInternal(globalPosition)
        val pageWidth = wrapperAndPos.mWrapper!!.adapter.getPageWidth(wrapperAndPos.mLocalPosition)
        releaseWrapperAndLocalPosition(wrapperAndPos)
        return pageWidth
    }

    fun findLocalAdapterAndPosition(globalPosition: Int): Pair<FragmentStatePagerAdapter, Int> {
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
    private fun findWrapperAndLocalPositionInternal(globalPosition: Int): FragmentStatePagerWrapperAndLocalPosition {
        val result: FragmentStatePagerWrapperAndLocalPosition
        if (mReusableHolder.mInUse) {
            result = FragmentStatePagerWrapperAndLocalPosition()
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

    private fun releaseWrapperAndLocalPosition(wrapperAndLocalPosition: FragmentStatePagerWrapperAndLocalPosition) {
        wrapperAndLocalPosition.mInUse = false
        wrapperAndLocalPosition.mWrapper = null
        wrapperAndLocalPosition.mLocalPosition = -1
        mReusableHolder = wrapperAndLocalPosition
    }

    /**
     * Helper class to hold onto wrapper and local position without allocating objects as this is
     * a very common call.
     */
    class FragmentStatePagerWrapperAndLocalPosition {
        var mWrapper: NestedFragmentStatePagerAdapterWrapper? = null
        var mLocalPosition = 0
        var mInUse = false
    }
}