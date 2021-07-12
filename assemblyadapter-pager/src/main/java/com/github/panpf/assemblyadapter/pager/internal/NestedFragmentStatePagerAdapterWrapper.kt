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

import android.database.DataSetObserver
import androidx.fragment.app.FragmentStatePagerAdapter

/**
 * Wrapper for each adapter in [ConcatFragmentStatePagerAdapter].
 */
internal class NestedFragmentStatePagerAdapterWrapper(
    val adapter: FragmentStatePagerAdapter,
    private val mCallback: Callback,
) {

    private val mAdapterObserver: DataSetObserver = object : DataSetObserver() {
        override fun onChanged() {
            cachedItemCount = adapter.count
            mCallback.onChanged(this@NestedFragmentStatePagerAdapterWrapper)
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
        cachedItemCount = adapter.count
        adapter.registerDataSetObserver(mAdapterObserver)
    }

    fun dispose() {
        adapter.unregisterDataSetObserver(mAdapterObserver)
    }

    internal interface Callback {
        fun onChanged(wrapper: NestedFragmentStatePagerAdapterWrapper)
    }
}