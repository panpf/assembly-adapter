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
package com.github.panpf.assemblyadapter.list.test

import android.database.DataSetObserver
import android.widget.FrameLayout
import androidx.test.platform.app.InstrumentationRegistry
import com.github.panpf.assemblyadapter.ViewItemFactory
import com.github.panpf.assemblyadapter.list.AssemblySingleDataListAdapter
import com.github.panpf.tools4j.test.ktx.assertThrow
import org.junit.Assert
import org.junit.Test

class AssemblySingleDataListAdapterTest {

    private class TestItemFactory :
        ViewItemFactory<String>(String::class, android.R.layout.activity_list_item)

    @Test
    fun testConstructor() {
        AssemblySingleDataListAdapter(TestItemFactory()).apply {
            Assert.assertNull(data)
        }

        AssemblySingleDataListAdapter(TestItemFactory(), "123456").apply {
            Assert.assertNotNull(data)
            Assert.assertEquals("123456", data)
        }
    }

    @Test
    fun testPropertyData() {
        var dataFromObserver: String? = null
        AssemblySingleDataListAdapter(TestItemFactory()).apply {
            registerDataSetObserver(object : DataSetObserver() {
                override fun onChanged() {
                    super.onChanged()
                    dataFromObserver = data
                }
            })

            Assert.assertNull(data)
            Assert.assertNull(dataFromObserver)

            data = "Test data changed notify invoke"
            Assert.assertEquals("Test data changed notify invoke", data)
            Assert.assertEquals("Test data changed notify invoke", dataFromObserver)

            data = "Test data changed notify invoke2"
            Assert.assertEquals("Test data changed notify invoke2", data)
            Assert.assertEquals("Test data changed notify invoke2", dataFromObserver)
        }
    }

    @Test
    fun testMethodGetCount() {
        AssemblySingleDataListAdapter(TestItemFactory()).apply {
            Assert.assertEquals(0, count)

            data = "Test count"
            Assert.assertEquals(1, count)

            data = null
            Assert.assertEquals(0, count)
        }
    }

    @Test
    fun testMethodGetItem() {
        AssemblySingleDataListAdapter(TestItemFactory()).apply {
            assertThrow(IndexOutOfBoundsException::class) {
                getItem(-1)
            }
            assertThrow(IndexOutOfBoundsException::class) {
                getItem(0)
            }
            assertThrow(IndexOutOfBoundsException::class) {
                getItem(1)
            }

            data = "test"
            Assert.assertEquals("test", getItem(0))
        }
    }

    @Test
    fun testMethodGetItemId() {
        AssemblySingleDataListAdapter(TestItemFactory()).apply {
            Assert.assertEquals(-1L, getItemId(-1))
            Assert.assertEquals(0L, getItemId(0))
            Assert.assertEquals(1L, getItemId(1))
            Assert.assertEquals(Int.MAX_VALUE.toLong(), getItemId(Int.MAX_VALUE))
            Assert.assertEquals(Int.MIN_VALUE.toLong(), getItemId(Int.MIN_VALUE))
        }
    }

    @Test
    fun testMethodGetViewTypeCount() {
        AssemblySingleDataListAdapter(TestItemFactory()).apply {
            Assert.assertEquals(1, viewTypeCount)
        }
    }

    @Test
    fun testMethodGetItemViewType() {
        AssemblySingleDataListAdapter(TestItemFactory()).apply {
            assertThrow(IndexOutOfBoundsException::class) {
                getItemViewType(-1)
            }
            assertThrow(IndexOutOfBoundsException::class) {
                getItemViewType(0)
            }
            assertThrow(IndexOutOfBoundsException::class) {
                getItemViewType(1)
            }

            data = "test"
            Assert.assertEquals(0, getItemViewType(0))
        }
    }

    @Test
    fun testMethodGetView() {
        val context = InstrumentationRegistry.getInstrumentation().context
        val parent = FrameLayout(context)
        AssemblySingleDataListAdapter(TestItemFactory()).apply {
            assertThrow(IndexOutOfBoundsException::class) {
                getView(-1, null, parent)
            }
            assertThrow(IndexOutOfBoundsException::class) {
                getView(0, null, parent)
            }
            assertThrow(IndexOutOfBoundsException::class) {
                getView(1, null, parent)
            }

            data = "test"
            val itemView = getView(0, null, parent)
            Assert.assertNotSame(itemView, getView(0, null, parent))
            Assert.assertSame(itemView, getView(0, itemView, parent))
        }
    }

    @Test
    fun testMethodGetItemFactoryByPosition() {
        val itemFactory = TestItemFactory()
        AssemblySingleDataListAdapter(itemFactory).apply {
            assertThrow(IndexOutOfBoundsException::class) {
                getItemFactoryByPosition(-1)
            }
            assertThrow(IndexOutOfBoundsException::class) {
                getItemFactoryByPosition(0)
            }
            assertThrow(IndexOutOfBoundsException::class) {
                getItemFactoryByPosition(1)
            }

            data = "test"
            Assert.assertSame(itemFactory, getItemFactoryByPosition(0))
        }
    }
}