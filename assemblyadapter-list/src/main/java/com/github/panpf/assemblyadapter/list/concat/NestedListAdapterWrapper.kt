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
package com.github.panpf.assemblyadapter.list.concat

import android.database.DataSetObserver
import android.widget.BaseAdapter
import com.github.panpf.assemblyadapter.list.concat.NestedListAdapterWrapper

/**
 * Wrapper for each adapter in [ConcatListAdapter].
 */
internal class NestedListAdapterWrapper(
    val adapter: BaseAdapter,
    private val mCallback: Callback,
    viewTypeStorage: ListViewTypeStorage,
    private val mStableIdLookup: ListStableIdStorage.StableIdLookup
) {

    private val mViewTypeLookup: ListViewTypeStorage.ViewTypeLookup =
        viewTypeStorage.createViewTypeWrapper(this)
    private val mAdapterObserver: DataSetObserver = object : DataSetObserver() {
        override fun onChanged() {
            cachedItemCount = adapter.count
            mCallback.onChanged(this@NestedListAdapterWrapper)
        }
    }

    /**
     * we cache this value so that we can know the previous size when change happens
     * this is also important as getting real size while an adapter is dispatching possibly a
     * a chain of events might create inconsistencies (as it happens in DiffUtil).
     * Instead, we always calculate this value based on notify events.
     */
    var cachedItemCount: Int = adapter.count
        private set

    init {
        adapter.registerDataSetObserver(mAdapterObserver)
    }

    fun dispose() {
        adapter.unregisterDataSetObserver(mAdapterObserver)
        mViewTypeLookup.dispose()
    }

    fun getItemViewType(localPosition: Int): Int {
        return mViewTypeLookup.localToGlobal(adapter.getItemViewType(localPosition))
    }

    fun getItemId(localPosition: Int): Long {
        return mStableIdLookup.localToGlobal(adapter.getItemId(localPosition))
    }

    internal interface Callback {
        fun onChanged(wrapper: NestedListAdapterWrapper)
    }
}