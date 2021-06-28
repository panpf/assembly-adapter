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
package com.github.panpf.assemblyadapter.list.concat.expandable

import android.database.DataSetObserver
import android.widget.BaseExpandableListAdapter

/**
 * Wrapper for each adapter in [ConcatExpandableListAdapter].
 */
internal class NestedExpandableListAdapterWrapper(
    val adapter: BaseExpandableListAdapter,
    private val mCallback: Callback,
    viewTypeStorage: ExpandableListViewTypeStorage,
    private val mGroupStableIdLookup: ExpandableListStableIdStorage.StableIdLookup,
    private val mChildStableIdLookup: ExpandableListStableIdStorage.StableIdLookup
) {

    private val mGroupViewTypeLookup = viewTypeStorage.createViewTypeWrapper(this)
    private val mChildViewTypeLookup = viewTypeStorage.createViewTypeWrapper(this)
    private val mAdapterObserver: DataSetObserver = object : DataSetObserver() {
        override fun onChanged() {
            cachedItemCount = adapter.groupCount
            mCallback.onChanged(this@NestedExpandableListAdapterWrapper)
        }
    }

    /**
     * we cache this value so that we can know the previous size when change happens
     * this is also important as getting real size while an adapter is dispatching possibly a
     * a chain of events might create inconsistencies (as it happens in DiffUtil).
     * Instead, we always calculate this value based on notify events.
     */
    var cachedItemCount: Int
        private set

    init {
        cachedItemCount = adapter.groupCount
        adapter.registerDataSetObserver(mAdapterObserver)
    }

    fun dispose() {
        adapter.unregisterDataSetObserver(mAdapterObserver)
        mGroupViewTypeLookup.dispose()
    }

    fun getGroupId(groupPosition: Int): Long {
        return mGroupStableIdLookup.localToGlobal(adapter.getGroupId(groupPosition))
    }

    fun getGroupType(groupPosition: Int): Int {
        return mGroupViewTypeLookup.localToGlobal(adapter.getGroupType(groupPosition))
    }

    fun getChildId(groupPosition: Int, childPosition: Int): Long {
        return mChildStableIdLookup.localToGlobal(adapter.getChildId(groupPosition, childPosition))
    }

    fun getChildType(groupPosition: Int, childPosition: Int): Int {
        return mChildViewTypeLookup.localToGlobal(
            adapter.getChildType(groupPosition, childPosition)
        )
    }

    interface Callback {
        fun onChanged(wrapper: NestedExpandableListAdapterWrapper)
    }
}