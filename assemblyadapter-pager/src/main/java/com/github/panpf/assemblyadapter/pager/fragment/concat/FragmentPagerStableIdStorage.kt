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

import androidx.collection.LongSparseArray

/**
 * Used by [ConcatFragmentPagerAdapter] to isolate item ids between nested adapters, if necessary.
 */
internal interface FragmentPagerStableIdStorage {
    fun createStableIdLookup(): StableIdLookup

    /**
     * Interface that provides [NestedFragmentPagerAdapterWrapper]s a way to map their local stable ids
     * into global stable ids, based on the configuration of the [ConcatFragmentPagerAdapter].
     */
    interface StableIdLookup {
        fun localToGlobal(localId: Long): Long
    }

    /**
     * An isolating implementation that ensures the stable ids among adapters do not conflict with
     * each-other. It keeps a mapping for each adapter from its local stable ids to a global domain
     * and always replaces the local id w/ a globally available ID to be consistent.
     */
    class IsolatedStableIdStorage : FragmentPagerStableIdStorage {
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