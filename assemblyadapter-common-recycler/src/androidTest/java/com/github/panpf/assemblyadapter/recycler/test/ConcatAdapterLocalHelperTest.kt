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
import com.github.panpf.assemblyadapter.recycler.ConcatAdapterLocalHelper
import com.github.panpf.assemblyadapter.recycler.InstanceDiffItemCallback
import com.github.panpf.tools4j.test.ktx.assertThrow
import org.junit.Assert
import org.junit.Test

class ConcatAdapterLocalHelperTest {

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

        val localHelper = ConcatAdapterLocalHelper()

        headerAdapter.apply {
            localHelper.findLocalAdapterAndPosition(this, 0).apply {
                Assert.assertTrue(first === headerAdapter)
                Assert.assertEquals(0, second)
            }
            localHelper.findLocalAdapterAndPosition(this, 1).apply {
                Assert.assertTrue(first === headerAdapter)
                Assert.assertEquals(1, second)
            }
            localHelper.findLocalAdapterAndPosition(this, 2).apply {
                Assert.assertTrue(first === headerAdapter)
                Assert.assertEquals(2, second)
            }
            assertThrow(IndexOutOfBoundsException::class) {
                localHelper.findLocalAdapterAndPosition(this, -1)
            }
            assertThrow(IndexOutOfBoundsException::class) {
                localHelper.findLocalAdapterAndPosition(this, 3)
            }
        }

        body1Adapter.apply {
            localHelper.findLocalAdapterAndPosition(this, 0).apply {
                Assert.assertTrue(first === body1Adapter)
                Assert.assertEquals(0, second)
            }
            localHelper.findLocalAdapterAndPosition(this, 1).apply {
                Assert.assertTrue(first === body1Adapter)
                Assert.assertEquals(1, second)
            }
            localHelper.findLocalAdapterAndPosition(this, 2).apply {
                Assert.assertTrue(first === body1Adapter)
                Assert.assertEquals(2, second)
            }
            assertThrow(IndexOutOfBoundsException::class) {
                localHelper.findLocalAdapterAndPosition(this, -1)
            }
            assertThrow(IndexOutOfBoundsException::class) {
                localHelper.findLocalAdapterAndPosition(this, 3)
            }
        }

        body2Adapter.apply {
            localHelper.findLocalAdapterAndPosition(this, 0).apply {
                Assert.assertTrue(first === body2Adapter)
                Assert.assertEquals(0, second)
            }
            localHelper.findLocalAdapterAndPosition(this, 1).apply {
                Assert.assertTrue(first === body2Adapter)
                Assert.assertEquals(1, second)
            }
            localHelper.findLocalAdapterAndPosition(this, 2).apply {
                Assert.assertTrue(first === body2Adapter)
                Assert.assertEquals(2, second)
            }
            assertThrow(IndexOutOfBoundsException::class) {
                localHelper.findLocalAdapterAndPosition(this, -1)
            }
            assertThrow(IndexOutOfBoundsException::class) {
                localHelper.findLocalAdapterAndPosition(this, 3)
            }
        }

        footerAdapter.apply {
            localHelper.findLocalAdapterAndPosition(this, 0).apply {
                Assert.assertTrue(first === footerAdapter)
                Assert.assertEquals(0, second)
            }
            localHelper.findLocalAdapterAndPosition(this, 1).apply {
                Assert.assertTrue(first === footerAdapter)
                Assert.assertEquals(1, second)
            }
            localHelper.findLocalAdapterAndPosition(this, 2).apply {
                Assert.assertTrue(first === footerAdapter)
                Assert.assertEquals(2, second)
            }
            assertThrow(IndexOutOfBoundsException::class) {
                localHelper.findLocalAdapterAndPosition(this, -1)
            }
            assertThrow(IndexOutOfBoundsException::class) {
                localHelper.findLocalAdapterAndPosition(this, 3)
            }
        }

        concatBodyAdapter.apply {
            localHelper.findLocalAdapterAndPosition(this, 0).apply {
                Assert.assertTrue(first === body1Adapter)
                Assert.assertEquals(0, second)
            }
            localHelper.findLocalAdapterAndPosition(this, 1).apply {
                Assert.assertTrue(first === body1Adapter)
                Assert.assertEquals(1, second)
            }
            localHelper.findLocalAdapterAndPosition(this, 2).apply {
                Assert.assertTrue(first === body1Adapter)
                Assert.assertEquals(2, second)
            }
            localHelper.findLocalAdapterAndPosition(this, 3).apply {
                Assert.assertTrue(first === body2Adapter)
                Assert.assertEquals(0, second)
            }
            localHelper.findLocalAdapterAndPosition(this, 4).apply {
                Assert.assertTrue(first === body2Adapter)
                Assert.assertEquals(1, second)
            }
            localHelper.findLocalAdapterAndPosition(this, 5).apply {
                Assert.assertTrue(first === body2Adapter)
                Assert.assertEquals(2, second)
            }
            assertThrow(IndexOutOfBoundsException::class) {
                localHelper.findLocalAdapterAndPosition(this, -1)
            }
            assertThrow(IndexOutOfBoundsException::class) {
                localHelper.findLocalAdapterAndPosition(this, 6)
            }
        }

        concatAdapter.apply {
            localHelper.findLocalAdapterAndPosition(this, 0).apply {
                Assert.assertTrue(first === headerAdapter)
                Assert.assertEquals(0, second)
            }
            localHelper.findLocalAdapterAndPosition(this, 1).apply {
                Assert.assertTrue(first === headerAdapter)
                Assert.assertEquals(1, second)
            }
            localHelper.findLocalAdapterAndPosition(this, 2).apply {
                Assert.assertTrue(first === headerAdapter)
                Assert.assertEquals(2, second)
            }
            localHelper.findLocalAdapterAndPosition(this, 3).apply {
                Assert.assertTrue(first === body1Adapter)
                Assert.assertEquals(0, second)
            }
            localHelper.findLocalAdapterAndPosition(this, 4).apply {
                Assert.assertTrue(first === body1Adapter)
                Assert.assertEquals(1, second)
            }
            localHelper.findLocalAdapterAndPosition(this, 5).apply {
                Assert.assertTrue(first === body1Adapter)
                Assert.assertEquals(2, second)
            }
            localHelper.findLocalAdapterAndPosition(this, 6).apply {
                Assert.assertTrue(first === body2Adapter)
                Assert.assertEquals(0, second)
            }
            localHelper.findLocalAdapterAndPosition(this, 7).apply {
                Assert.assertTrue(first === body2Adapter)
                Assert.assertEquals(1, second)
            }
            localHelper.findLocalAdapterAndPosition(this, 8).apply {
                Assert.assertTrue(first === body2Adapter)
                Assert.assertEquals(2, second)
            }
            localHelper.findLocalAdapterAndPosition(this, 9).apply {
                Assert.assertTrue(first === footerAdapter)
                Assert.assertEquals(0, second)
            }
            localHelper.findLocalAdapterAndPosition(this, 10).apply {
                Assert.assertTrue(first === footerAdapter)
                Assert.assertEquals(1, second)
            }
            localHelper.findLocalAdapterAndPosition(this, 11).apply {
                Assert.assertTrue(first === footerAdapter)
                Assert.assertEquals(2, second)
            }
            assertThrow(IndexOutOfBoundsException::class) {
                localHelper.findLocalAdapterAndPosition(this, -1)
            }
            assertThrow(IndexOutOfBoundsException::class) {
                localHelper.findLocalAdapterAndPosition(this, 12)
            }
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