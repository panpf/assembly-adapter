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
import android.widget.TextView
import androidx.test.platform.app.InstrumentationRegistry
import com.github.panpf.assemblyadapter.NotFoundMatchedItemFactoryException
import com.github.panpf.assemblyadapter.ViewItemFactory
import com.github.panpf.assemblyadapter.list.AssemblySingleDataListAdapter
import com.github.panpf.tools4j.test.ktx.assertThrow
import org.junit.Assert
import org.junit.Test

class AssemblySingleDataListAdapterTest {

    private data class Text(val text: String)

    private class TextItemFactory : ViewItemFactory<Text>(Text::class, { context, _, _ ->
        TextView(context)
    })

    @Test
    fun testConstructor() {
        AssemblySingleDataListAdapter(TextItemFactory()).apply {
            Assert.assertNull(data)
        }

        AssemblySingleDataListAdapter(TextItemFactory(), Text("hello")).apply {
            Assert.assertEquals(Text("hello"), data)
        }
    }

    @Test
    fun testPropertyData() {
        AssemblySingleDataListAdapter(TextItemFactory()).apply {
            Assert.assertNull(data)
            Assert.assertEquals(0, currentList.size)

            data = Text("hello")
            Assert.assertEquals(Text("hello"), data)
            Assert.assertEquals(1, currentList.size)

            data = Text("world")
            Assert.assertEquals(Text("world"), data)
            Assert.assertEquals(1, currentList.size)

            data = null
            Assert.assertNull(data)
            Assert.assertEquals(0, currentList.size)
        }
    }

    @Test
    fun testMethodSubmitList() {
        AssemblySingleDataListAdapter(TextItemFactory()).apply {
            Assert.assertEquals(0, currentList.size)

            assertThrow(IllegalArgumentException::class) {
                submitList(listOf(Text("good"), Text("bye")))
            }
            Assert.assertEquals(0, currentList.size)

            submitList(listOf(Text("hello")))
            Assert.assertEquals(1, currentList.size)
            Assert.assertEquals(Text("hello"), data)

            submitList(null)
            Assert.assertEquals(0, currentList.size)
        }
    }

    @Test
    fun testMethodGetCount() {
        AssemblySingleDataListAdapter(TextItemFactory()).apply {
            Assert.assertEquals(0, count)
            Assert.assertEquals(0, itemCount)

            data = Text("hello")
            Assert.assertEquals(1, count)
            Assert.assertEquals(1, itemCount)

            data = null
            Assert.assertEquals(0, count)
            Assert.assertEquals(0, itemCount)
        }
    }

    @Test
    fun testMethodGetItem() {
        AssemblySingleDataListAdapter(TextItemFactory()).apply {
            assertThrow(IndexOutOfBoundsException::class) {
                getItem(-1)
            }
            assertThrow(IndexOutOfBoundsException::class) {
                getItemData(-1)
            }
            assertThrow(IndexOutOfBoundsException::class) {
                getItem(0)
            }
            assertThrow(IndexOutOfBoundsException::class) {
                getItemData(0)
            }
            assertThrow(IndexOutOfBoundsException::class) {
                getItem(1)
            }
            assertThrow(IndexOutOfBoundsException::class) {
                getItemData(1)
            }

            data = Text("hello")
            Assert.assertEquals(Text("hello"), getItem(0))
            Assert.assertEquals(Text("hello"), getItemData(0))
        }
    }

    @Test
    fun testMethodGetItemId() {
        AssemblySingleDataListAdapter(TextItemFactory()).apply {
            Assert.assertEquals(-1L, getItemId(-1))
            Assert.assertEquals(-1L, getItemId(0))
            Assert.assertEquals(-1L, getItemId(1))
        }

        AssemblySingleDataListAdapter(TextItemFactory()).apply {
            setHasStableIds(true)
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

        AssemblySingleDataListAdapter(
            TextItemFactory(),
            initData = Text("hello"),
        ).apply {
            setHasStableIds(true)
            assertThrow(IndexOutOfBoundsException::class) {
                getItemId(-1)
            }
            Assert.assertEquals(getItem(0).hashCode().toLong(), getItemId(0))
            assertThrow(IndexOutOfBoundsException::class) {
                getItemId(1)
            }
        }
    }

    @Test
    fun testMethodGetViewTypeCount() {
        AssemblySingleDataListAdapter(TextItemFactory()).apply {
            Assert.assertEquals(1, viewTypeCount)
        }
    }

    @Test
    fun testMethodGetItemViewType() {
        AssemblySingleDataListAdapter(TextItemFactory()).apply {
            assertThrow(IndexOutOfBoundsException::class) {
                getItemViewType(-1)
            }
            assertThrow(IndexOutOfBoundsException::class) {
                getItemViewType(0)
            }
            assertThrow(IndexOutOfBoundsException::class) {
                getItemViewType(1)
            }

            data = Text("hello")
            Assert.assertEquals(0, getItemViewType(0))
        }
    }

    @Test
    fun testMethodGetView() {
        val context = InstrumentationRegistry.getInstrumentation().context
        val parent = FrameLayout(context)
        AssemblySingleDataListAdapter(TextItemFactory()).apply {
            assertThrow(IndexOutOfBoundsException::class) {
                getView(-1, null, parent)
            }
            assertThrow(IndexOutOfBoundsException::class) {
                getView(0, null, parent)
            }
            assertThrow(IndexOutOfBoundsException::class) {
                getView(1, null, parent)
            }

            data = Text("hello")
            val itemView = getView(0, null, parent)
            Assert.assertNotSame(itemView, getView(0, null, parent))
            Assert.assertSame(itemView, getView(0, itemView, parent))
        }
    }

    @Test
    fun testMethodGetItemFactoryByPosition() {
        val itemFactory = TextItemFactory()
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

            data = Text("hello")
            Assert.assertSame(itemFactory, getItemFactoryByPosition(0))
        }
    }

    @Test
    fun testMethodGetItemFactoryByData() {
        val textItemFactory = TextItemFactory()

        AssemblySingleDataListAdapter(textItemFactory).apply {
            Assert.assertSame(textItemFactory, getItemFactoryByData(Text("hello")))
        }
    }

    @Test
    fun testMethodGetItemFactoryByItemFactoryClass() {
        val textItemFactory = TextItemFactory()

        AssemblySingleDataListAdapter(textItemFactory).apply {
            Assert.assertSame(
                textItemFactory,
                getItemFactoryByItemFactoryClass(TextItemFactory::class)
            )
            assertThrow(NotFoundMatchedItemFactoryException::class) {
                getItemFactoryByItemFactoryClass(ViewItemFactory::class)
            }

            Assert.assertSame(
                textItemFactory,
                getItemFactoryByItemFactoryClass(TextItemFactory::class.java)
            )
            assertThrow(NotFoundMatchedItemFactoryException::class) {
                getItemFactoryByItemFactoryClass(ViewItemFactory::class.java)
            }
        }
    }

    @Test
    fun testMethodHasObservers() {
        AssemblySingleDataListAdapter(TextItemFactory()).apply {
            Assert.assertFalse(hasObservers())

            val dataObserver1 = object : DataSetObserver() {}
            val dataObserver2 = object : DataSetObserver() {}

            registerDataSetObserver(dataObserver1)
            Assert.assertTrue(hasObservers())

            registerDataSetObserver(dataObserver2)
            Assert.assertTrue(hasObservers())

            unregisterDataSetObserver(dataObserver1)
            Assert.assertTrue(hasObservers())

            unregisterDataSetObserver(dataObserver2)
            Assert.assertFalse(hasObservers())
        }
    }
}