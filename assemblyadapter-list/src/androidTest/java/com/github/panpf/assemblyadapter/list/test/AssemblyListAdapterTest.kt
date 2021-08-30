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
import com.github.panpf.assemblyadapter.NotFoundMatchedItemFactoryException
import com.github.panpf.assemblyadapter.Placeholder
import com.github.panpf.assemblyadapter.ViewItemFactory
import com.github.panpf.assemblyadapter.list.AssemblyListAdapter
import com.github.panpf.tools4j.test.ktx.assertThrow
import org.junit.Assert
import org.junit.Test
import java.util.*

class AssemblyListAdapterTest {

    private class StringItemFactory :
        ViewItemFactory<String>(String::class, android.R.layout.activity_list_item)

    private class DateItemFactory :
        ViewItemFactory<Date>(Date::class, android.R.layout.activity_list_item)

    private class PlaceholderItemFactory :
        ViewItemFactory<Placeholder>(Placeholder::class, android.R.layout.activity_list_item)

    @Test
    fun testConstructor() {
        AssemblyListAdapter<String>(listOf(StringItemFactory())).apply {
            Assert.assertEquals("", currentList.joinToString())
        }

        AssemblyListAdapter(listOf(StringItemFactory()), listOf("hello")).apply {
            Assert.assertEquals("hello", currentList.joinToString())
        }

        AssemblyListAdapter(listOf(StringItemFactory()), listOf("hello", "world")).apply {
            Assert.assertEquals("hello, world", currentList.joinToString())
        }

        assertThrow(IllegalArgumentException::class) {
            AssemblyListAdapter<String>(listOf())
        }
    }

    @Test
    fun testPropertyCurrentListAndSubmitList() {
        var dataFromObserver: List<String>? = null
        AssemblyListAdapter<String>(listOf(StringItemFactory())).apply {
            registerDataSetObserver(object : DataSetObserver() {
                override fun onChanged() {
                    super.onChanged()
                    dataFromObserver = currentList
                }
            })

            Assert.assertEquals("", currentList.joinToString())
            Assert.assertNull(dataFromObserver)

            submitList(listOf("hello"))
            Assert.assertEquals("hello", currentList.joinToString())

            submitList(listOf("hello", "world"))
            Assert.assertEquals("hello, world", currentList.joinToString())

            submitList(null)
            Assert.assertEquals("", currentList.joinToString())
        }
    }

    @Test
    fun testMethodGetCount() {
        AssemblyListAdapter<String>(listOf(StringItemFactory())).apply {
            Assert.assertEquals(0, count)

            submitList(listOf("hello"))
            Assert.assertEquals(1, count)

            submitList(listOf("hello", "world"))
            Assert.assertEquals(2, count)

            submitList(null)
            Assert.assertEquals(0, count)
        }
    }

    @Test
    fun testMethodGetItem() {
        AssemblyListAdapter<String>(listOf(StringItemFactory())).apply {
            assertThrow(IndexOutOfBoundsException::class) {
                getItem(-1)
            }
            assertThrow(IndexOutOfBoundsException::class) {
                getItem(0)
            }
            assertThrow(IndexOutOfBoundsException::class) {
                getItem(1)
            }

            submitList(listOf("hello", "world"))
            Assert.assertEquals("hello", getItem(0))
            Assert.assertEquals("world", getItem(1))
        }
    }

    @Test
    fun testMethodGetItemId() {
        AssemblyListAdapter<String>(listOf(StringItemFactory())).apply {
            Assert.assertEquals(-1L, getItemId(-1))
            Assert.assertEquals(-1L, getItemId(0))
            Assert.assertEquals(-1L, getItemId(1))
        }

        AssemblyListAdapter<String>(listOf(StringItemFactory()), hasStableIds = true).apply {
            assertThrow(IndexOutOfBoundsException::class) {
                getItemId(-1)
            }
            assertThrow(IndexOutOfBoundsException::class) {
                getItemId(0)
            }
            assertThrow(IndexOutOfBoundsException::class) {
                getItemId(1)
            }
        }

        AssemblyListAdapter(
            listOf(StringItemFactory()),
            initDataList = listOf("hello", "world"),
            hasStableIds = true
        ).apply {
            assertThrow(IndexOutOfBoundsException::class) {
                getItemId(-1)
            }
            Assert.assertEquals(getItem(0).hashCode().toLong(), getItemId(0))
            Assert.assertEquals(getItem(1).hashCode().toLong(), getItemId(1))
            assertThrow(IndexOutOfBoundsException::class) {
                getItemId(2)
            }
        }
    }

    @Test
    fun testMethodGetViewTypeCount() {
        AssemblyListAdapter<String>(listOf(StringItemFactory())).apply {
            Assert.assertEquals(1, viewTypeCount)
        }

        AssemblyListAdapter<String>(listOf(StringItemFactory(), DateItemFactory())).apply {
            Assert.assertEquals(2, viewTypeCount)
        }
    }

    @Test
    fun testMethodGetItemViewType() {
        AssemblyListAdapter<Any>(listOf(StringItemFactory(), DateItemFactory())).apply {
            assertThrow(IndexOutOfBoundsException::class) {
                getItemViewType(-1)
            }
            assertThrow(IndexOutOfBoundsException::class) {
                getItemViewType(0)
            }
            assertThrow(IndexOutOfBoundsException::class) {
                getItemViewType(1)
            }

            submitList(listOf(Date(), "hello"))
            Assert.assertEquals(1, getItemViewType(0))
            Assert.assertEquals(0, getItemViewType(1))
        }
    }

    @Test
    fun testMethodGetView() {
        val context = InstrumentationRegistry.getInstrumentation().context
        val parent = FrameLayout(context)
        AssemblyListAdapter<String>(listOf(StringItemFactory())).apply {
            assertThrow(IndexOutOfBoundsException::class) {
                getView(-1, null, parent)
            }
            assertThrow(IndexOutOfBoundsException::class) {
                getView(0, null, parent)
            }
            assertThrow(IndexOutOfBoundsException::class) {
                getView(1, null, parent)
            }

            submitList(listOf("hello"))
            val itemView = getView(0, null, parent)
            Assert.assertNotSame(itemView, getView(0, null, parent))
            Assert.assertSame(itemView, getView(0, itemView, parent))
        }
    }

    @Test
    fun testMethodGetItemFactoryByPosition() {
        val stringItemFactory = StringItemFactory()
        val dateItemFactory = DateItemFactory()
        AssemblyListAdapter<Any>(listOf(stringItemFactory, dateItemFactory)).apply {
            assertThrow(IndexOutOfBoundsException::class) {
                getItemFactoryByPosition(-1)
            }
            assertThrow(IndexOutOfBoundsException::class) {
                getItemFactoryByPosition(0)
            }
            assertThrow(IndexOutOfBoundsException::class) {
                getItemFactoryByPosition(1)
            }

            submitList(listOf(Date(), "hello"))
            Assert.assertSame(dateItemFactory, getItemFactoryByPosition(0))
            Assert.assertSame(stringItemFactory, getItemFactoryByPosition(1))
        }
    }

    @Test
    fun testPlaceholder() {
        val context = InstrumentationRegistry.getInstrumentation().context
        val parent = FrameLayout(context)

        AssemblyListAdapter<Any?>(listOf(StringItemFactory())).apply {
            submitList(listOf("hello", null))

            Assert.assertEquals(0, getItemViewType(0))
            assertThrow(NotFoundMatchedItemFactoryException::class) {
                Assert.assertEquals(0, getItemViewType(1))
            }

            getView(0, null, parent)
            assertThrow(NotFoundMatchedItemFactoryException::class) {
                getView(1, null, parent)
            }

            getItemFactoryByPosition(0)
            assertThrow(NotFoundMatchedItemFactoryException::class) {
                getItemFactoryByPosition(1)
            }
        }

        AssemblyListAdapter<Any?>(listOf(StringItemFactory(), PlaceholderItemFactory())).apply {
            submitList(listOf("hello", null))

            Assert.assertEquals(0, getItemViewType(0))
            Assert.assertEquals(1, getItemViewType(1))

            getView(0, null, parent)
            getView(1, null, parent)

            getItemFactoryByPosition(0)
            getItemFactoryByPosition(1)
        }
    }
}