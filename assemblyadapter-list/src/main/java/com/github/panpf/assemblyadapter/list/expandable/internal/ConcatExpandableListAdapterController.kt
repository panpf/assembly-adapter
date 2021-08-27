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
package com.github.panpf.assemblyadapter.list.expandable.internal

import android.annotation.SuppressLint
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import com.github.panpf.assemblyadapter.list.expandable.ConcatExpandableListAdapter
import com.github.panpf.assemblyadapter.list.R
import java.util.*

/**
 * All logic for the [ConcatExpandableListAdapter] is here so that we can clearly see a separation
 * between an adapter implementation and merging logic.
 */
internal class ConcatExpandableListAdapterController(
    private val mConcatAdapter: ConcatExpandableListAdapter,
    config: ConcatExpandableListAdapter.Config
) : NestedExpandableListAdapterWrapper.Callback {

    /**
     * Holds the mapping from the view type to the adapter which reported that type.
     */
    private val mViewTypeStorage = if (config.isolateViewTypes) {
        ExpandableListViewTypeStorage.IsolatedViewTypeStorage()
    } else {
        ExpandableListViewTypeStorage.SharedIdRangeViewTypeStorage()
    }
    private val mWrappers = ArrayList<NestedExpandableListAdapterWrapper>()

    // keep one of these around so that we can return wrapper & position w/o allocation ¯\_(ツ)_/¯
    private var mReusableHolder = ExpandableListWrapperAndLocalPosition()
    private val mStableIdMode = config.stableIdMode

    /**
     * This is where we keep stable ids, if supported
     */
    private var mStableIdStorage = when {
        config.stableIdMode === ConcatExpandableListAdapter.Config.StableIdMode.NO_STABLE_IDS -> {
            ExpandableListStableIdStorage.NoStableIdStorage()
        }
        config.stableIdMode === ConcatExpandableListAdapter.Config.StableIdMode.ISOLATED_STABLE_IDS -> {
            ExpandableListStableIdStorage.IsolatedStableIdStorage()
        }
        config.stableIdMode === ConcatExpandableListAdapter.Config.StableIdMode.SHARED_STABLE_IDS -> {
            ExpandableListStableIdStorage.SharedPoolStableIdStorage()
        }
        else -> {
            throw IllegalArgumentException("unknown stable id mode")
        }
    }

    private var groupItemViewTypeCount = -1
    private var childItemViewTypeCount = -1

    // should we cache this as well ?
    val groupCount: Int
        get() {
            // should we cache this as well ?
            var total = 0
            for (wrapper in mWrappers) {
                total += wrapper.cachedItemCount
            }
            return total
        }

    val groupTypeCount: Int
        get() {
            if (groupItemViewTypeCount == -1) {
                groupItemViewTypeCount = 0
                for (mWrapper in mWrappers) {
                    groupItemViewTypeCount += mWrapper.adapter.groupTypeCount
                }
            }
            return groupItemViewTypeCount
        }

    val childTypeCount: Int
        get() {
            if (childItemViewTypeCount == -1) {
                childItemViewTypeCount = 0
                for (mWrapper in mWrappers) {
                    childItemViewTypeCount += mWrapper.adapter.childTypeCount
                }
            }
            return childItemViewTypeCount
        }

    val copyOfAdapters: List<BaseExpandableListAdapter>
        get() {
            if (mWrappers.isEmpty()) {
                return emptyList()
            }
            val adapters: MutableList<BaseExpandableListAdapter> = ArrayList(mWrappers.size)
            for (wrapper in mWrappers) {
                adapters.add(wrapper.adapter)
            }
            return adapters
        }

    private fun findWrapperFor(adapter: BaseExpandableListAdapter): NestedExpandableListAdapterWrapper? {
        val index = indexOfWrapper(adapter)
        return if (index == -1) {
            null
        } else mWrappers[index]
    }

    private fun indexOfWrapper(adapter: BaseExpandableListAdapter): Int {
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
     * @see ConcatExpandableListAdapter.addAdapter
     */
    fun addAdapter(adapter: BaseExpandableListAdapter): Boolean {
        return addAdapter(mWrappers.size, adapter)
    }

    /**
     * return true if added, false otherwise.
     * throws exception if index is out of bounds
     *
     * @see ConcatExpandableListAdapter.addAdapter
     */
    @SuppressLint("RestrictedApi", "LongLogTag")
    fun addAdapter(index: Int, adapter: BaseExpandableListAdapter): Boolean {
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
                    ConcatExpandableListAdapter.TAG,
                    "Stable ids in the adapter will be ignored as the ConcatExpandableListAdapter is configured not to have stable ids"
                )
            }
        }
        val existing = findWrapperFor(adapter)
        if (existing != null) {
            return false
        }
        val wrapper = NestedExpandableListAdapterWrapper(
            adapter,
            this,
            mViewTypeStorage,
            mStableIdStorage.createStableIdLookup(),
            mStableIdStorage.createStableIdLookup()
        )
        mWrappers.add(index, wrapper)
        groupItemViewTypeCount = -1
        childItemViewTypeCount = -1
        // new items, notify add for them
        if (wrapper.cachedItemCount > 0) {
            mConcatAdapter.notifyDataSetChanged()
        }
        return true
    }

    fun removeAdapter(adapter: BaseExpandableListAdapter): Boolean {
        val index = indexOfWrapper(adapter)
        if (index == -1) {
            return false
        }
        val wrapper = mWrappers[index]
        mWrappers.removeAt(index)
        groupItemViewTypeCount = -1
        childItemViewTypeCount = -1
        mConcatAdapter.notifyDataSetChanged()
        wrapper.dispose()
        return true
    }

    override fun onChanged(wrapper: NestedExpandableListAdapterWrapper) {
        mConcatAdapter.notifyDataSetChanged()
    }

    fun getGroup(globalGroupPosition: Int): Any? {
        val wrapperAndPos = findWrapperAndLocalPositionInternal(globalGroupPosition)
        val group = wrapperAndPos.mWrapper!!.adapter.getGroup(wrapperAndPos.mLocalPosition)
        releaseWrapperAndLocalPosition(wrapperAndPos)
        return group
    }

    fun getGroupId(globalGroupPosition: Int): Long {
        val wrapperAndPos = findWrapperAndLocalPositionInternal(globalGroupPosition)
        val globalGroupId = wrapperAndPos.mWrapper!!.getGroupId(wrapperAndPos.mLocalPosition)
        releaseWrapperAndLocalPosition(wrapperAndPos)
        return globalGroupId
    }

    fun getGroupType(globalGroupPosition: Int): Int {
        val wrapperAndPos = findWrapperAndLocalPositionInternal(globalGroupPosition)
        val groupType = wrapperAndPos.mWrapper!!.getGroupType(wrapperAndPos.mLocalPosition)
        releaseWrapperAndLocalPosition(wrapperAndPos)
        return groupType
    }

    fun getGroupView(
        globalGroupPosition: Int, isExpanded: Boolean, convertView: View?, parent: ViewGroup
    ): View {
        // tag absoluteAdapterPosition must be null to support ConcatExpandableListAdapter nesting
        if (parent.getTag(R.id.aa_tag_absoluteAdapterPosition) == null) {
            parent.setTag(R.id.aa_tag_absoluteAdapterPosition, globalGroupPosition)
        }

        val wrapperAndPos = findWrapperAndLocalPositionInternal(globalGroupPosition)
        val wrapperAdapter = wrapperAndPos.mWrapper!!.adapter
        val localGroupPosition = wrapperAndPos.mLocalPosition
        val groupView =
            wrapperAdapter.getGroupView(localGroupPosition, isExpanded, convertView, parent)
        releaseWrapperAndLocalPosition(wrapperAndPos)
        return groupView
    }

    fun getChildrenCount(globalGroupPosition: Int): Int {
        val wrapperAndPos = findWrapperAndLocalPositionInternal(globalGroupPosition)
        val childrenCount =
            wrapperAndPos.mWrapper!!.adapter.getChildrenCount(wrapperAndPos.mLocalPosition)
        releaseWrapperAndLocalPosition(wrapperAndPos)
        return childrenCount
    }

    fun getChild(globalGroupPosition: Int, childPosition: Int): Any? {
        val wrapperAndPos = findWrapperAndLocalPositionInternal(globalGroupPosition)
        val group =
            wrapperAndPos.mWrapper!!.adapter.getChild(wrapperAndPos.mLocalPosition, childPosition)
        releaseWrapperAndLocalPosition(wrapperAndPos)
        return group
    }

    fun getChildId(globalGroupPosition: Int, childPosition: Int): Long {
        val wrapperAndPos = findWrapperAndLocalPositionInternal(globalGroupPosition)
        val globalChildId =
            wrapperAndPos.mWrapper!!.getChildId(wrapperAndPos.mLocalPosition, childPosition)
        releaseWrapperAndLocalPosition(wrapperAndPos)
        return globalChildId
    }

    fun getChildType(globalGroupPosition: Int, childPosition: Int): Int {
        val wrapperAndPos = findWrapperAndLocalPositionInternal(globalGroupPosition)
        val childType =
            wrapperAndPos.mWrapper!!.getChildType(wrapperAndPos.mLocalPosition, childPosition)
        releaseWrapperAndLocalPosition(wrapperAndPos)
        return childType
    }

    fun getChildView(
        globalGroupPosition: Int, childPosition: Int, isLastChild: Boolean,
        convertView: View?, parent: ViewGroup
    ): View {
        // tag absoluteAdapterPosition must be null to support ConcatExpandableListAdapter nesting
        if (parent.getTag(R.id.aa_tag_absoluteAdapterPosition) == null) {
            parent.setTag(R.id.aa_tag_absoluteAdapterPosition, globalGroupPosition)
        }

        val wrapperAndPos = findWrapperAndLocalPositionInternal(globalGroupPosition)
        val wrapperAdapter = wrapperAndPos.mWrapper!!.adapter
        val localGroupPosition = wrapperAndPos.mLocalPosition
        val childView = wrapperAdapter.getChildView(
            localGroupPosition, childPosition, isLastChild, convertView, parent
        )
        releaseWrapperAndLocalPosition(wrapperAndPos)
        return childView
    }

    fun isChildSelectable(globalGroupPosition: Int, childPosition: Int): Boolean {
        val wrapperAndPos = findWrapperAndLocalPositionInternal(globalGroupPosition)
        val isChildSelectable =
            wrapperAndPos.mWrapper!!.adapter.isChildSelectable(
                wrapperAndPos.mLocalPosition,
                childPosition
            )
        releaseWrapperAndLocalPosition(wrapperAndPos)
        return isChildSelectable
    }

    fun onGroupCollapsed(globalGroupPosition: Int) {
        val wrapperAndPos = findWrapperAndLocalPositionInternal(globalGroupPosition)
        wrapperAndPos.mWrapper!!.adapter.onGroupCollapsed(wrapperAndPos.mLocalPosition)
        releaseWrapperAndLocalPosition(wrapperAndPos)
    }

    fun onGroupExpanded(globalGroupPosition: Int) {
        val wrapperAndPos = findWrapperAndLocalPositionInternal(globalGroupPosition)
        wrapperAndPos.mWrapper!!.adapter.onGroupExpanded(wrapperAndPos.mLocalPosition)
        releaseWrapperAndLocalPosition(wrapperAndPos)
    }

    fun hasStableIds(): Boolean {
        return mStableIdMode !== ConcatExpandableListAdapter.Config.StableIdMode.NO_STABLE_IDS
    }

    fun findLocalAdapterAndPosition(globalPosition: Int): Pair<BaseExpandableListAdapter, Int> {
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
    private fun findWrapperAndLocalPositionInternal(globalGroupPosition: Int): ExpandableListWrapperAndLocalPosition {
        val result: ExpandableListWrapperAndLocalPosition
        if (mReusableHolder.mInUse) {
            result = ExpandableListWrapperAndLocalPosition()
        } else {
            mReusableHolder.mInUse = true
            result = mReusableHolder
        }
        var localPosition = globalGroupPosition
        for (wrapper in mWrappers) {
            if (wrapper.cachedItemCount > localPosition) {
                result.mWrapper = wrapper
                result.mLocalPosition = localPosition
                break
            }
            localPosition -= wrapper.cachedItemCount
        }
        requireNotNull(result.mWrapper) { "Cannot find wrapper for $globalGroupPosition" }
        return result
    }

    private fun releaseWrapperAndLocalPosition(wrapperAndLocalPosition: ExpandableListWrapperAndLocalPosition) {
        wrapperAndLocalPosition.mInUse = false
        wrapperAndLocalPosition.mWrapper = null
        wrapperAndLocalPosition.mLocalPosition = -1
        mReusableHolder = wrapperAndLocalPosition
    }

    /**
     * Helper class to hold onto wrapper and local position without allocating objects as this is
     * a very common call.
     */
    class ExpandableListWrapperAndLocalPosition {
        var mWrapper: NestedExpandableListAdapterWrapper? = null
        var mLocalPosition = 0
        var mInUse = false
    }
}