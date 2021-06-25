/*
 * Copyright 2020 The Android Open Source Project
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
package com.github.panpf.assemblyadapter.list.concat

import android.annotation.SuppressLint
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.github.panpf.assemblyadapter.list.R
import java.util.*

/**
 * All logic for the [ConcatListAdapter] is here so that we can clearly see a separation
 * between an adapter implementation and merging logic.
 */
internal class ConcatListAdapterController(
    private val mConcatAdapter: ConcatListAdapter,
    config: ConcatListAdapter.Config
) : NestedListAdapterWrapper.Callback {

    /**
     * Holds the mapping from the view type to the adapter which reported that type.
     */
    private val mViewTypeStorage = if (config.isolateViewTypes) {
        ListViewTypeStorage.IsolatedViewTypeStorage()
    } else {
        ListViewTypeStorage.SharedIdRangeViewTypeStorage()
    }
    private val mWrappers = ArrayList<NestedListAdapterWrapper>()

    // keep one of these around so that we can return wrapper & position w/o allocation ¯\_(ツ)_/¯
    private var mReusableHolder = ListWrapperAndLocalPosition()
    private val mStableIdMode: ConcatListAdapter.Config.StableIdMode = config.stableIdMode

    /**
     * This is where we keep stable ids, if supported
     */
    private var mStableIdStorage = when {
        config.stableIdMode === ConcatListAdapter.Config.StableIdMode.NO_STABLE_IDS -> {
            ListStableIdStorage.NoStableIdStorage()
        }
        config.stableIdMode === ConcatListAdapter.Config.StableIdMode.ISOLATED_STABLE_IDS -> {
            ListStableIdStorage.IsolatedStableIdStorage()
        }
        config.stableIdMode === ConcatListAdapter.Config.StableIdMode.SHARED_STABLE_IDS -> {
            ListStableIdStorage.SharedPoolStableIdStorage()
        }
        else -> {
            throw IllegalArgumentException("unknown stable id mode")
        }
    }

    private var itemViewTypeCount = -1

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

    val copyOfAdapters: List<BaseAdapter>
        get() {
            if (mWrappers.isEmpty()) {
                return emptyList()
            }
            val adapters: MutableList<BaseAdapter> = ArrayList(mWrappers.size)
            for (wrapper in mWrappers) {
                adapters.add(wrapper.adapter)
            }
            return adapters
        }

    private fun findWrapperFor(adapter: BaseAdapter): NestedListAdapterWrapper? {
        val index = indexOfWrapper(adapter)
        return if (index == -1) {
            null
        } else mWrappers[index]
    }

    private fun indexOfWrapper(adapter: BaseAdapter): Int {
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
     * @see ConcatListAdapter.addAdapter
     */
    fun addAdapter(adapter: BaseAdapter): Boolean {
        return addAdapter(mWrappers.size, adapter)
    }

    /**
     * return true if added, false otherwise.
     * throws exception if index is out of bounds
     *
     * @see ConcatListAdapter.addAdapter
     */
    @SuppressLint("RestrictedApi")
    fun addAdapter(index: Int, adapter: BaseAdapter): Boolean {
        if (index < 0 || index > mWrappers.size) {
            throw IndexOutOfBoundsException(
                "Index must be between 0 and ${mWrappers.size}. Given:$index"
            )
        }
        if (hasStableIds()) {
            require(adapter.hasStableIds()) {
                "All sub adapters must have stable ids when stable id mode is ISOLATED_STABLE_IDS or SHARED_STABLE_IDS"
            }
        } else {
            if (adapter.hasStableIds()) {
                Log.w(
                    ConcatListAdapter.TAG,
                    "Stable ids in the adapter will be ignored as the ConcatListAdapter is configured not to have stable ids"
                )
            }
        }
        val existing = findWrapperFor(adapter)
        if (existing != null) {
            return false
        }
        val wrapper = NestedListAdapterWrapper(
            adapter, this,
            mViewTypeStorage, mStableIdStorage.createStableIdLookup()
        )
        mWrappers.add(index, wrapper)
        itemViewTypeCount = -1
        // new items, notify add for them
        if (wrapper.cachedItemCount > 0) {
            mConcatAdapter.notifyDataSetChanged()
        }
        return true
    }

    fun removeAdapter(adapter: BaseAdapter): Boolean {
        val index = indexOfWrapper(adapter)
        if (index == -1) {
            return false
        }
        val wrapper = mWrappers[index]
        mWrappers.removeAt(index)
        itemViewTypeCount = -1
        mConcatAdapter.notifyDataSetChanged()
        wrapper.dispose()
        return true
    }

    fun getItemId(globalPosition: Int): Long {
        val wrapperAndPos = findWrapperAndLocalPositionInternal(globalPosition)
        val globalItemId = wrapperAndPos.mWrapper!!.getItemId(wrapperAndPos.mLocalPosition)
        releaseWrapperAndLocalPosition(wrapperAndPos)
        return globalItemId
    }

    override fun onChanged(wrapper: NestedListAdapterWrapper) {
        mConcatAdapter.notifyDataSetChanged()
    }

    fun getItem(globalPosition: Int): Any? {
        val wrapperAndPos = findWrapperAndLocalPositionInternal(globalPosition)
        val item = wrapperAndPos.mWrapper!!.adapter.getItem(wrapperAndPos.mLocalPosition)
        releaseWrapperAndLocalPosition(wrapperAndPos)
        return item
    }

    fun getItemViewTypeCount(): Int {
        if (itemViewTypeCount == -1) {
            itemViewTypeCount = 0
            for (mWrapper in mWrappers) {
                itemViewTypeCount += mWrapper.adapter.viewTypeCount
            }
        }
        return itemViewTypeCount
    }

    fun getItemViewType(globalPosition: Int): Int {
        val wrapperAndPos = findWrapperAndLocalPositionInternal(globalPosition)
        val itemViewType = wrapperAndPos.mWrapper!!.getItemViewType(wrapperAndPos.mLocalPosition)
        releaseWrapperAndLocalPosition(wrapperAndPos)
        return itemViewType
    }

    fun getView(globalPosition: Int, convertView: View?, parent: ViewGroup): View {
        val wrapperAndPos = findWrapperAndLocalPositionInternal(globalPosition)
        val wrapperAdapter = wrapperAndPos.mWrapper!!.adapter
        parent.setTag(R.id.aa_tag_absoluteAdapterPosition, globalPosition)
        val itemView = wrapperAdapter.getView(wrapperAndPos.mLocalPosition, convertView, parent)
        releaseWrapperAndLocalPosition(wrapperAndPos)
        return itemView
    }

    fun hasStableIds(): Boolean {
        return mStableIdMode !== ConcatListAdapter.Config.StableIdMode.NO_STABLE_IDS
    }

    fun findLocalAdapterAndPosition(globalPosition: Int): Pair<BaseAdapter, Int> {
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
    private fun findWrapperAndLocalPositionInternal(globalPosition: Int): ListWrapperAndLocalPosition {
        val result: ListWrapperAndLocalPosition
        if (mReusableHolder.mInUse) {
            result = ListWrapperAndLocalPosition()
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

    private fun releaseWrapperAndLocalPosition(wrapperAndLocalPosition: ListWrapperAndLocalPosition) {
        wrapperAndLocalPosition.mInUse = false
        wrapperAndLocalPosition.mWrapper = null
        wrapperAndLocalPosition.mLocalPosition = -1
        mReusableHolder = wrapperAndLocalPosition
    }

    /**
     * Helper class to hold onto wrapper and local position without allocating objects as this is
     * a very common call.
     */
    class ListWrapperAndLocalPosition {
        var mWrapper: NestedListAdapterWrapper? = null
        var mLocalPosition = 0
        var mInUse = false
    }
}