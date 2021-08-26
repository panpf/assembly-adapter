/*
 * Copyright (C) 2021 panpf <panpfpanpf@outlook.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.panpf.assemblyadapter.recycler.test

import android.view.ViewGroup
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.github.panpf.assemblyadapter.recycler.ConcatAdapterWrapperAdaptersCache
import com.github.panpf.assemblyadapter.recycler.InstanceDiffItemCallback
import org.junit.Assert
import org.junit.Test

class ConcatAdapterWrapperAdaptersCacheTest {

    @Test
    fun test() {
        val headerAdapter = TestListAdapter().apply {
            submitList(listOf(1, 2, 3))
        }
        val body1Adapter = TestListAdapter().apply {
            submitList(listOf(4, 5, 6))
        }
        val body2Adapter = TestListAdapter().apply {
            submitList(listOf(7, 8, 9))
        }
        val footerAdapter = TestListAdapter().apply {
            submitList(listOf(10, 11, 12))
        }
        val concatBodyAdapter = ConcatAdapter(body1Adapter, body2Adapter)
        val concatAdapter = ConcatAdapter(headerAdapter, concatBodyAdapter, footerAdapter)

        val adaptersCache = ConcatAdapterWrapperAdaptersCache()

        adaptersCache.getAdapterCache(headerAdapter).apply {
            Assert.assertTrue(this.adapter === headerAdapter)
            Assert.assertTrue(this is ConcatAdapterWrapperAdaptersCache.NormalAdapterCache)
        }

        adaptersCache.getAdapterCache(body1Adapter).apply {
            Assert.assertTrue(this.adapter === body1Adapter)
            Assert.assertTrue(this is ConcatAdapterWrapperAdaptersCache.NormalAdapterCache)
        }

        adaptersCache.getAdapterCache(body2Adapter).apply {
            Assert.assertTrue(this.adapter === body2Adapter)
            Assert.assertTrue(this is ConcatAdapterWrapperAdaptersCache.NormalAdapterCache)
        }

        adaptersCache.getAdapterCache(footerAdapter).apply {
            Assert.assertTrue(this.adapter === footerAdapter)
            Assert.assertTrue(this is ConcatAdapterWrapperAdaptersCache.NormalAdapterCache)
        }

        adaptersCache.getAdapterCache(concatBodyAdapter).apply {
            Assert.assertTrue(this.adapter === concatBodyAdapter)
            val concatAdapterCache = this as ConcatAdapterWrapperAdaptersCache.ConcatAdapterCache
            Assert.assertEquals(2, concatAdapterCache.childAdapterCaches.size)
            Assert.assertTrue(concatAdapterCache.childAdapterCaches[0].adapter === body1Adapter)
            Assert.assertTrue(concatAdapterCache.childAdapterCaches[0] is ConcatAdapterWrapperAdaptersCache.NormalAdapterCache)
            Assert.assertTrue(concatAdapterCache.childAdapterCaches[1].adapter === body2Adapter)
            Assert.assertTrue(concatAdapterCache.childAdapterCaches[1] is ConcatAdapterWrapperAdaptersCache.NormalAdapterCache)
        }

        adaptersCache.getAdapterCache(concatAdapter).apply {
            Assert.assertTrue(this.adapter === concatAdapter)
            val concatAdapterCache = this as ConcatAdapterWrapperAdaptersCache.ConcatAdapterCache
            Assert.assertEquals(3, concatAdapterCache.childAdapterCaches.size)
            Assert.assertTrue(concatAdapterCache.childAdapterCaches[0].adapter === headerAdapter)
            Assert.assertTrue(concatAdapterCache.childAdapterCaches[0] is ConcatAdapterWrapperAdaptersCache.NormalAdapterCache)
            Assert.assertTrue(concatAdapterCache.childAdapterCaches[1].adapter === concatBodyAdapter)
            Assert.assertTrue(concatAdapterCache.childAdapterCaches[1] is ConcatAdapterWrapperAdaptersCache.ConcatAdapterCache)
            Assert.assertTrue(concatAdapterCache.childAdapterCaches[2].adapter === footerAdapter)
            Assert.assertTrue(concatAdapterCache.childAdapterCaches[2] is ConcatAdapterWrapperAdaptersCache.NormalAdapterCache)
        }
    }

    private class TestListAdapter :
        ListAdapter<Any, RecyclerView.ViewHolder>(InstanceDiffItemCallback()) {
        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): RecyclerView.ViewHolder {
            TODO("Not yet implemented")
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            TODO("Not yet implemented")
        }
    }
}