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
import android.widget.ImageView
import android.widget.TextView
import androidx.test.platform.app.InstrumentationRegistry
import com.github.panpf.assemblyadapter.NotFoundMatchedItemFactoryException
import com.github.panpf.assemblyadapter.Placeholder
import com.github.panpf.assemblyadapter.ViewItemFactory
import com.github.panpf.assemblyadapter.list.AssemblyListAdapter
import com.github.panpf.tools4j.test.ktx.assertThrow
import org.junit.Assert
import org.junit.Test

class AssemblyListAdapterTest {

    private class TextItemFactory : ViewItemFactory<String>(String::class, { context, _, _ ->
        TextView(context)
    })

    private data class Image(val redId: Int)

    private class ImageItemFactory : ViewItemFactory<Image>(Image::class, { context, _, _ ->
        ImageView(context)
    })

    private class PlaceholderItemFactory :
        ViewItemFactory<Placeholder>(Placeholder::class, android.R.layout.test_list_item)

    @Test
    fun testConstructor() {
        AssemblyListAdapter<String>(listOf(TextItemFactory())).apply {
            Assert.assertEquals("", currentList.joinToString())
        }

        AssemblyListAdapter(listOf(TextItemFactory()), listOf("hello")).apply {
            Assert.assertEquals("hello", currentList.joinToString())
        }

        AssemblyListAdapter(listOf(TextItemFactory()), listOf("hello", "world")).apply {
            Assert.assertEquals("hello, world", currentList.joinToString())
        }

        assertThrow(IllegalArgumentException::class) {
            AssemblyListAdapter<String>(listOf())
        }
    }

    @Test
    fun testPropertyCurrentListAndSubmitList() {
        var dataFromObserver: List<String>? = null
        AssemblyListAdapter<String>(listOf(TextItemFactory())).apply {
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
        AssemblyListAdapter<String>(listOf(TextItemFactory())).apply {
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
        AssemblyListAdapter<String>(listOf(TextItemFactory())).apply {
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
        AssemblyListAdapter<String>(listOf(TextItemFactory())).apply {
            Assert.assertEquals(-1L, getItemId(-1))
            Assert.assertEquals(-1L, getItemId(0))
            Assert.assertEquals(-1L, getItemId(1))
        }

        AssemblyListAdapter<String>(listOf(TextItemFactory()), hasStableIds = true).apply {
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
            listOf(TextItemFactory()),
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
        AssemblyListAdapter<String>(listOf(TextItemFactory())).apply {
            Assert.assertEquals(1, viewTypeCount)
        }

        AssemblyListAdapter<String>(listOf(TextItemFactory(), ImageItemFactory())).apply {
            Assert.assertEquals(2, viewTypeCount)
        }
    }

    @Test
    fun testMethodGetItemViewType() {
        AssemblyListAdapter<Any>(listOf(TextItemFactory(), ImageItemFactory())).apply {
            assertThrow(IndexOutOfBoundsException::class) {
                getItemViewType(-1)
            }
            assertThrow(IndexOutOfBoundsException::class) {
                getItemViewType(0)
            }
            assertThrow(IndexOutOfBoundsException::class) {
                getItemViewType(1)
            }

            submitList(listOf(Image(android.R.drawable.alert_dark_frame), "hello"))
            Assert.assertEquals(1, getItemViewType(0))
            Assert.assertEquals(0, getItemViewType(1))
        }
    }

    @Test
    fun testMethodGetView() {
        val context = InstrumentationRegistry.getInstrumentation().context
        val parent = FrameLayout(context)
        AssemblyListAdapter<Any>(listOf(TextItemFactory(), ImageItemFactory())).apply {
            assertThrow(IndexOutOfBoundsException::class) {
                getView(-1, null, parent)
            }
            assertThrow(IndexOutOfBoundsException::class) {
                getView(0, null, parent)
            }
            assertThrow(IndexOutOfBoundsException::class) {
                getView(1, null, parent)
            }

            submitList(listOf("hello", Image(android.R.drawable.alert_dark_frame)))

            Assert.assertTrue(getView(0, null, parent) is TextView)
            Assert.assertTrue(getView(1, null, parent) is ImageView)

            val itemView = getView(0, null, parent)
            Assert.assertNotSame(itemView, getView(0, null, parent))
            Assert.assertSame(itemView, getView(0, itemView, parent))
        }
    }

    @Test
    fun testMethodGetItemFactoryByPosition() {
        val stringItemFactory = TextItemFactory()
        val dateItemFactory = ImageItemFactory()
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

            submitList(listOf(Image(android.R.drawable.alert_dark_frame), "hello"))
            Assert.assertSame(dateItemFactory, getItemFactoryByPosition(0))
            Assert.assertSame(stringItemFactory, getItemFactoryByPosition(1))
        }
    }

    @Test
    fun testPlaceholder() {
        val context = InstrumentationRegistry.getInstrumentation().context
        val parent = FrameLayout(context)

        AssemblyListAdapter<Any?>(listOf(TextItemFactory())).apply {
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

        AssemblyListAdapter<Any?>(listOf(TextItemFactory(), PlaceholderItemFactory())).apply {
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