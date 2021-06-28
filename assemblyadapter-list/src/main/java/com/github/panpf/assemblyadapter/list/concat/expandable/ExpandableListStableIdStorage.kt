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

import androidx.collection.LongSparseArray

/**
 * Used by [ConcatExpandableListAdapter] to isolate item ids between nested adapters, if necessary.
 */
internal interface ExpandableListStableIdStorage {
    fun createStableIdLookup(): StableIdLookup

    /**
     * Interface that provides [NestedExpandableListAdapterWrapper]s a way to map their local stable ids
     * into global stable ids, based on the configuration of the [ConcatExpandableListAdapter].
     */
    interface StableIdLookup {
        fun localToGlobal(localId: Long): Long
    }

    /**
     * Returns [ConcatExpandableListAdapter.NO_ID] for all positions. In other words, stable ids are not
     * supported.
     */
    class NoStableIdStorage : ExpandableListStableIdStorage {
        private val mNoIdLookup: StableIdLookup = object : StableIdLookup {
            override fun localToGlobal(localId: Long): Long {
                return ConcatExpandableListAdapter.NO_ID
            }
        }

        override fun createStableIdLookup(): StableIdLookup {
            return mNoIdLookup
        }
    }

    /**
     * A pass-through implementation that reports the stable id in sub adapters as is.
     */
    class SharedPoolStableIdStorage : ExpandableListStableIdStorage {
        private val mSameIdLookup: StableIdLookup = object : StableIdLookup {
            override fun localToGlobal(localId: Long): Long {
                return localId
            }
        }

        override fun createStableIdLookup(): StableIdLookup {
            return mSameIdLookup
        }
    }

    /**
     * An isolating implementation that ensures the stable ids among adapters do not conflict with
     * each-other. It keeps a mapping for each adapter from its local stable ids to a global domain
     * and always replaces the local id w/ a globally available ID to be consistent.
     */
    class IsolatedStableIdStorage : ExpandableListStableIdStorage {
        var mNextStableId: Long = 0
        fun obtainId(): Long {
            return mNextStableId++
        }

        override fun createStableIdLookup(): StableIdLookup {
            return WrapperStableIdLookup()
        }

        internal inner class WrapperStableIdLookup : StableIdLookup {
            private val mLocalToGlobalLookup = LongSparseArray<Long>()
            override fun localToGlobal(localId: Long): Long {
                var globalId = mLocalToGlobalLookup[localId]
                if (globalId == null) {
                    globalId = obtainId()
                    mLocalToGlobalLookup.put(localId, globalId)
                }
                return globalId
            }
        }
    }
}