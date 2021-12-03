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

import android.util.SparseIntArray
import androidx.collection.SparseArrayCompat
import com.github.panpf.assemblyadapter.list.expandable.ConcatExpandableListAdapter
import java.util.ArrayList

/**
 * Used by [ConcatExpandableListAdapter] to isolate view types between nested adapters, if necessary.
 */
internal interface ExpandableListViewTypeStorage {
    fun getWrapperForGlobalType(globalViewType: Int): NestedExpandableListAdapterWrapper
    fun createViewTypeWrapper(
        wrapper: NestedExpandableListAdapterWrapper
    ): ViewTypeLookup

    /**
     * Api given to [NestedExpandableListAdapterWrapper]s.
     */
    interface ViewTypeLookup {
        fun localToGlobal(localType: Int): Int
        fun globalToLocal(globalType: Int): Int
        fun dispose()
    }

    class SharedIdRangeViewTypeStorage : ExpandableListViewTypeStorage {
        // we keep a list of nested wrappers here even though we only need 1 to create because
        // they might be removed.
        var mGlobalTypeToWrapper =
            SparseArrayCompat<MutableList<NestedExpandableListAdapterWrapper>>()

        override fun getWrapperForGlobalType(globalViewType: Int): NestedExpandableListAdapterWrapper {
            val nestedExpandableAdapterWrappers: List<NestedExpandableListAdapterWrapper>? =
                mGlobalTypeToWrapper[globalViewType]
            require(!(nestedExpandableAdapterWrappers == null || nestedExpandableAdapterWrappers.isEmpty())) {
                "Cannot find the wrapper for global view type $globalViewType"
            }
            // just return the first one since they are shared
            return nestedExpandableAdapterWrappers[0]
        }

        override fun createViewTypeWrapper(
            wrapper: NestedExpandableListAdapterWrapper
        ): ViewTypeLookup {
            return WrapperViewTypeLookup(wrapper)
        }

        fun removeWrapper(wrapper: NestedExpandableListAdapterWrapper) {
            for (i in mGlobalTypeToWrapper.size() - 1 downTo 0) {
                val wrappers = mGlobalTypeToWrapper.valueAt(i)
                if (wrappers.remove(wrapper)) {
                    if (wrappers.isEmpty()) {
                        mGlobalTypeToWrapper.removeAt(i)
                    }
                }
            }
        }

        internal inner class WrapperViewTypeLookup(private val mWrapper: NestedExpandableListAdapterWrapper) :
            ViewTypeLookup {
            override fun localToGlobal(localType: Int): Int {
                // register it first
                val wrappers = mGlobalTypeToWrapper[localType]
                    ?: ArrayList<NestedExpandableListAdapterWrapper>().apply {
                        mGlobalTypeToWrapper.put(localType, this)
                    }
                if (!wrappers.contains(mWrapper)) {
                    wrappers.add(mWrapper)
                }
                return localType
            }

            override fun globalToLocal(globalType: Int): Int {
                return globalType
            }

            override fun dispose() {
                removeWrapper(mWrapper)
            }
        }
    }

    class IsolatedViewTypeStorage : ExpandableListViewTypeStorage {

        private var mGlobalTypeToWrapper = SparseArrayCompat<NestedExpandableListAdapterWrapper>()
        private var mNextViewType = 0

        fun obtainViewType(wrapper: NestedExpandableListAdapterWrapper): Int {
            val nextId = mNextViewType++
            mGlobalTypeToWrapper.put(nextId, wrapper)
            return nextId
        }

        override fun getWrapperForGlobalType(globalViewType: Int): NestedExpandableListAdapterWrapper {
            return mGlobalTypeToWrapper[globalViewType]
                ?: throw IllegalArgumentException(
                    "Cannot find the wrapper for global view type $globalViewType"
                )
        }

        override fun createViewTypeWrapper(
            wrapper: NestedExpandableListAdapterWrapper
        ): ViewTypeLookup {
            return WrapperViewTypeLookup(wrapper)
        }

        fun removeWrapper(wrapper: NestedExpandableListAdapterWrapper) {
            for (i in mGlobalTypeToWrapper.size() - 1 downTo 0) {
                val existingWrapper = mGlobalTypeToWrapper.valueAt(i)
                if (existingWrapper === wrapper) {
                    mGlobalTypeToWrapper.removeAt(i)
                }
            }
        }

        internal inner class WrapperViewTypeLookup(private val mWrapper: NestedExpandableListAdapterWrapper) :
            ViewTypeLookup {
            private val mLocalToGlobalMapping = SparseIntArray(1)
            private val mGlobalToLocalMapping = SparseIntArray(1)
            override fun localToGlobal(localType: Int): Int {
                val index = mLocalToGlobalMapping.indexOfKey(localType)
                if (index > -1) {
                    return mLocalToGlobalMapping.valueAt(index)
                }
                // get a new key.
                val globalType = obtainViewType(mWrapper)
                mLocalToGlobalMapping.put(localType, globalType)
                mGlobalToLocalMapping.put(globalType, localType)
                return globalType
            }

            override fun globalToLocal(globalType: Int): Int {
                val index = mGlobalToLocalMapping.indexOfKey(globalType)
                check(index >= 0) {
                    "requested global type $globalType does not belong to the adapter:${mWrapper.adapter}"
                }
                return mGlobalToLocalMapping.valueAt(index)
            }

            override fun dispose() {
                removeWrapper(mWrapper)
            }
        }
    }
}