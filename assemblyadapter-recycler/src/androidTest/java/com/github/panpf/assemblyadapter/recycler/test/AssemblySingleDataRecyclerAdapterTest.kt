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

import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.test.platform.app.InstrumentationRegistry
import com.github.panpf.assemblyadapter.NotFoundMatchedItemFactoryException
import com.github.panpf.assemblyadapter.ViewItemFactory
import com.github.panpf.assemblyadapter.recycler.AssemblySingleDataRecyclerAdapter
import com.github.panpf.tools4j.test.ktx.assertThrow
import org.junit.Assert
import org.junit.Test

class AssemblySingleDataRecyclerAdapterTest {

    private data class Text(val text: String)

    private class TextItemFactory : ViewItemFactory<Text>(Text::class, { context, _, _ ->
        TextView(context)
    })

    @Test
    fun testConstructor() {
        AssemblySingleDataRecyclerAdapter(TextItemFactory()).apply {
            Assert.assertNull(data)
        }

        AssemblySingleDataRecyclerAdapter(TextItemFactory(), Text("hello")).apply {
            Assert.assertEquals(Text("hello"), data)
        }
    }

    @Test
    fun testPropertyData() {
        AssemblySingleDataRecyclerAdapter(TextItemFactory()).apply {
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
        AssemblySingleDataRecyclerAdapter(TextItemFactory()).apply {
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
        AssemblySingleDataRecyclerAdapter(TextItemFactory()).apply {
            Assert.assertEquals(0, itemCount)

            data = Text("hello")
            Assert.assertEquals(1, itemCount)

            data = null
            Assert.assertEquals(0, itemCount)
        }
    }

    @Test
    fun testMethodGetItemData() {
        AssemblySingleDataRecyclerAdapter(TextItemFactory()).apply {
            assertThrow(IndexOutOfBoundsException::class) {
                getItemData(-1)
            }
            assertThrow(IndexOutOfBoundsException::class) {
                getItemData(0)
            }
            assertThrow(IndexOutOfBoundsException::class) {
                getItemData(1)
            }

            data = Text("hello")
            Assert.assertEquals(Text("hello"), getItemData(0))
        }
    }

    @Test
    fun testMethodGetItemId() {
        AssemblySingleDataRecyclerAdapter(TextItemFactory()).apply {
            Assert.assertEquals(-1L, getItemId(-1))
            Assert.assertEquals(-1L, getItemId(0))
            Assert.assertEquals(-1L, getItemId(1))
        }

        AssemblySingleDataRecyclerAdapter(TextItemFactory()).apply {
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

        AssemblySingleDataRecyclerAdapter(
            TextItemFactory(),
            initData = Text("hello"),
        ).apply {
            setHasStableIds(true)
            assertThrow(IndexOutOfBoundsException::class) {
                getItemId(-1)
            }
            Assert.assertEquals(getItemData(0).hashCode().toLong(), getItemId(0))
            assertThrow(IndexOutOfBoundsException::class) {
                getItemId(1)
            }
        }
    }


    @Test
    fun testMethodGetItemViewType() {
        AssemblySingleDataRecyclerAdapter(TextItemFactory()).apply {
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
    fun testMethodCreateAndBindViewHolder() {
        val context = InstrumentationRegistry.getInstrumentation().context
        val parent = RecyclerView(context).apply {
            layoutManager = LinearLayoutManager(context)
        }
        AssemblySingleDataRecyclerAdapter(TextItemFactory()).apply {
            data = Text("hello")

            assertThrow(IllegalArgumentException::class) {
                onCreateViewHolder(parent, -1)
            }
            Assert.assertTrue(onCreateViewHolder(parent, 0).itemView is TextView)

            assertThrow(IllegalArgumentException::class) {
                onBindViewHolder(object : RecyclerView.ViewHolder(TextView(context)) {}, 0)
            }
            onBindViewHolder(onCreateViewHolder(parent, 0), 0)
        }
    }

    @Test
    fun testMethodGetItemFactoryByPosition() {
        val itemFactory = TextItemFactory()
        AssemblySingleDataRecyclerAdapter(itemFactory).apply {
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

        AssemblySingleDataRecyclerAdapter(textItemFactory).apply {
            Assert.assertSame(textItemFactory, getItemFactoryByData(Text("hello")))
        }
    }

    @Test
    fun testMethodGetItemFactoryByItemFactoryClass() {
        val textItemFactory = TextItemFactory()

        AssemblySingleDataRecyclerAdapter(textItemFactory).apply {
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
}