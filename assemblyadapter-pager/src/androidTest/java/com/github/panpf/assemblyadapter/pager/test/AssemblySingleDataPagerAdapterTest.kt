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
package com.github.panpf.assemblyadapter.pager.test

import android.database.DataSetObserver
import android.view.View
import android.widget.FrameLayout
import androidx.test.platform.app.InstrumentationRegistry
import androidx.viewpager.widget.PagerAdapter
import com.github.panpf.assemblyadapter.pager.AssemblySingleDataPagerAdapter
import com.github.panpf.assemblyadapter.pager.ViewPagerItemFactory
import com.github.panpf.tools4j.test.ktx.assertThrow
import org.junit.Assert
import org.junit.Test

class AssemblySingleDataPagerAdapterTest {

    private class TestItemFactory :
        ViewPagerItemFactory<String>(String::class, android.R.layout.activity_list_item)

    @Test
    fun testConstructor() {
        AssemblySingleDataPagerAdapter(TestItemFactory()).apply {
            Assert.assertNull(data)
        }

        AssemblySingleDataPagerAdapter(
            TestItemFactory(),
            "123456"
        ).apply {
            Assert.assertNotNull(data)
            Assert.assertEquals("123456", data)
        }
    }

    @Test
    fun testPropertyData() {
        var dataFromObserver: String? = null
        AssemblySingleDataPagerAdapter(TestItemFactory()).apply {
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
        AssemblySingleDataPagerAdapter(TestItemFactory()).apply {
            Assert.assertEquals(0, count)
            Assert.assertEquals(0, itemCount)

            data = "Test count"
            Assert.assertEquals(1, count)
            Assert.assertEquals(1, itemCount)

            data = null
            Assert.assertEquals(0, count)
            Assert.assertEquals(0, itemCount)
        }
    }

    @Test
    fun testMethodInstantiateItem() {
        val context = InstrumentationRegistry.getInstrumentation().context
        val parent = FrameLayout(context)
        AssemblySingleDataPagerAdapter(TestItemFactory()).apply {
            assertThrow(IndexOutOfBoundsException::class) {
                instantiateItem(parent, -1)
            }
            assertThrow(IndexOutOfBoundsException::class) {
                instantiateItem(parent, 0)
            }
            assertThrow(IndexOutOfBoundsException::class) {
                instantiateItem(parent, 1)
            }

            data = "test"
            Assert.assertTrue(instantiateItem(parent, 0) is View)
        }
    }

    @Test
    fun testMethodDestroyItem() {
        val context = InstrumentationRegistry.getInstrumentation().context
        val parent = FrameLayout(context)
        AssemblySingleDataPagerAdapter(TestItemFactory()).apply {
            data = "test"
            val item = instantiateItem(parent, 0)
            Assert.assertTrue(item is View)

            assertThrow(IndexOutOfBoundsException::class) {
                destroyItem(parent, -1, item)
            }
            destroyItem(parent, 0, item)
            assertThrow(IndexOutOfBoundsException::class) {
                destroyItem(parent, 1, item)
            }
            assertThrow(ClassCastException::class) {
                destroyItem(parent, 0, "fake item")
            }
        }
    }

    @Test
    fun testMethodIsViewFromObject() {
        val context = InstrumentationRegistry.getInstrumentation().context
        val parent = FrameLayout(context)
        AssemblySingleDataPagerAdapter(TestItemFactory()).apply {
            data = "test"
            val item = instantiateItem(parent, 0)
            Assert.assertTrue(item is View)
            Assert.assertTrue(isViewFromObject(item as View, item))
            Assert.assertFalse(isViewFromObject(item, "fake item"))
        }
    }

    @Test
    fun testMethodGetItemPosition() {
        val context = InstrumentationRegistry.getInstrumentation().context
        val parent = FrameLayout(context)
        AssemblySingleDataPagerAdapter(TestItemFactory()).apply {
            data = "test"
            val item = instantiateItem(parent, 0)
            Assert.assertEquals(PagerAdapter.POSITION_UNCHANGED, getItemPosition(item))

            notifyDataSetChanged()
            Assert.assertEquals(PagerAdapter.POSITION_UNCHANGED, getItemPosition(item))

            data = "test1"
            Assert.assertEquals(PagerAdapter.POSITION_NONE, getItemPosition(item))

            data = null
            Assert.assertEquals(PagerAdapter.POSITION_NONE, getItemPosition(item))
        }
    }

    @Test
    fun testMethodGetItemFactoryByPosition() {
        val itemFactory = TestItemFactory()
        AssemblySingleDataPagerAdapter(itemFactory).apply {
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