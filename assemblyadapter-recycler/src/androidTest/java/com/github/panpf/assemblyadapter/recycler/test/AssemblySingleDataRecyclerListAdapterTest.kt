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

import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.test.platform.app.InstrumentationRegistry
import com.github.panpf.assemblyadapter.ViewItemFactory
import com.github.panpf.assemblyadapter.recycler.*
import com.github.panpf.tools4j.test.ktx.assertThrow
import org.junit.Assert
import org.junit.Test

class AssemblySingleDataRecyclerListAdapterTest {

    private data class Text(val text: String) : DiffKey {
        override val diffKey: Any = "Text:$text"
    }

    private class TextItemFactory : ViewItemFactory<Text>(Text::class, { context, _, _ ->
        TextView(context)
    })

    private data class NoDiffKey(val name: String = "")

    private class NoDiffKeyItemFactory :
        ViewItemFactory<NoDiffKey>(NoDiffKey::class, { context, _, _ ->
            ImageView(context)
        })

    @Test
    fun testConstructor() {
        /**
         * ItemFactory, List, ItemCallback
         */
        // ItemFactory
        AssemblySingleDataRecyclerListAdapter(
            TextItemFactory(),
            null,
            KeyEqualsDiffItemCallback()
        )

        // List
        AssemblySingleDataRecyclerListAdapter(
            TextItemFactory(),
            null,
            KeyEqualsDiffItemCallback()
        ).apply {
            Assert.assertEquals(0, itemCount)
        }
        AssemblySingleDataRecyclerListAdapter(
            TextItemFactory(),
            Text("hello"),
            KeyEqualsDiffItemCallback()
        ).apply {
            Assert.assertEquals(1, itemCount)
        }

        // ItemCallback
        assertThrow(IllegalArgumentException::class) {
            AssemblySingleDataRecyclerListAdapter(
                NoDiffKeyItemFactory(),
                null,
                KeyEqualsDiffItemCallback()
            )
        }
        AssemblySingleDataRecyclerListAdapter(
            NoDiffKeyItemFactory(),
            null,
            InstanceDiffItemCallback()
        )

        // ItemFactory, List
        AssemblySingleDataRecyclerListAdapter(TextItemFactory(), null)
        assertThrow(IllegalArgumentException::class) {
            AssemblySingleDataRecyclerListAdapter(NoDiffKeyItemFactory(), null)
        }

        // ItemFactory
        AssemblySingleDataRecyclerListAdapter(TextItemFactory())
        assertThrow(IllegalArgumentException::class) {
            AssemblySingleDataRecyclerListAdapter(NoDiffKeyItemFactory())
        }


        /**
         * ItemFactory ItemCallback
         */
        // ItemFactory
        AssemblySingleDataRecyclerListAdapter(
            TextItemFactory(),
            KeyEqualsDiffItemCallback()
        )

        // ItemCallback
        assertThrow(IllegalArgumentException::class) {
            AssemblySingleDataRecyclerListAdapter(
                NoDiffKeyItemFactory(),
                KeyEqualsDiffItemCallback()
            )
        }
        AssemblySingleDataRecyclerListAdapter(NoDiffKeyItemFactory(), InstanceDiffItemCallback())


        /**
         * ItemFactory, List, AsyncDifferConfig<DATA>
         */
        // ItemFactory
        AssemblySingleDataRecyclerListAdapter(
            TextItemFactory(),
            null,
            AsyncDifferConfig.Builder<Text>(KeyEqualsDiffItemCallback()).build()
        )

        // List
        AssemblySingleDataRecyclerListAdapter(
            TextItemFactory(),
            null,
            AsyncDifferConfig.Builder<Text>(KeyEqualsDiffItemCallback()).build()
        ).apply {
            Assert.assertEquals(0, itemCount)
        }
        AssemblySingleDataRecyclerListAdapter(
            TextItemFactory(),
            Text("hello"),
            AsyncDifferConfig.Builder<Text>(KeyEqualsDiffItemCallback()).build()
        ).apply {
            Assert.assertEquals(1, itemCount)
        }

        // ItemCallback
        assertThrow(IllegalArgumentException::class) {
            AssemblySingleDataRecyclerListAdapter(
                NoDiffKeyItemFactory(),
                null,
                AsyncDifferConfig.Builder<NoDiffKey>(KeyEqualsDiffItemCallback()).build()
            )
        }
        AssemblySingleDataRecyclerListAdapter(
            NoDiffKeyItemFactory(),
            null,
            AsyncDifferConfig.Builder<NoDiffKey>(InstanceDiffItemCallback()).build()
        )


        /**
         * ItemFactory DiffUtil.AsyncDifferConfig<DATA>
         */
        // ItemFactory
        AssemblySingleDataRecyclerListAdapter(
            TextItemFactory(),
            AsyncDifferConfig.Builder<Text>(KeyEqualsDiffItemCallback()).build()
        )

        // ItemCallback
        assertThrow(IllegalArgumentException::class) {
            AssemblySingleDataRecyclerListAdapter(
                NoDiffKeyItemFactory(),
                AsyncDifferConfig.Builder<NoDiffKey>(KeyEqualsDiffItemCallback()).build()
            )
        }
        AssemblySingleDataRecyclerListAdapter(
            NoDiffKeyItemFactory(),
            AsyncDifferConfig.Builder<NoDiffKey>(InstanceDiffItemCallback()).build()
        )
    }

    @Test
    fun testPropertyData() {
        var dataFromObserver: Text? = null
        AssemblySingleDataRecyclerListAdapter(TextItemFactory()).apply {
            registerAdapterDataObserver(SimpleAdapterDataObserver {
                dataFromObserver = data
            })

            Assert.assertNull(data)
            Assert.assertNull(dataFromObserver)

            data = Text("hello")
            Thread.sleep(10)    // ListAdapter internal asynchronous thread updates data, it takes a while to take effect
            Assert.assertEquals(Text("hello"), data)
            Assert.assertEquals(Text("hello"), dataFromObserver)

            data = Text("world")
            Thread.sleep(10)    // ListAdapter internal asynchronous thread updates data, it takes a while to take effect
            Assert.assertEquals(Text("world"), data)
            Assert.assertEquals(Text("world"), dataFromObserver)
        }
    }

    @Test
    fun testMethodGetCount() {
        AssemblySingleDataRecyclerListAdapter(TextItemFactory()).apply {
            Assert.assertEquals(0, itemCount)

            data = Text("hello")
            Thread.sleep(10)    // ListAdapter internal asynchronous thread updates data, it takes a while to take effect
            Assert.assertEquals(1, itemCount)

            data = null
            Thread.sleep(10)    // ListAdapter internal asynchronous thread updates data, it takes a while to take effect
            Assert.assertEquals(0, itemCount)
        }
    }

    @Test
    fun testMethodGetItem() {
        AssemblySingleDataRecyclerListAdapter(TextItemFactory()).apply {
            assertThrow(IndexOutOfBoundsException::class) {
                getItem(-1)
            }
            assertThrow(IndexOutOfBoundsException::class) {
                getItem(0)
            }
            assertThrow(IndexOutOfBoundsException::class) {
                getItem(1)
            }

            data = Text("hello")
            Thread.sleep(10)    // ListAdapter internal asynchronous thread updates data, it takes a while to take effect
            Assert.assertEquals(Text("hello"), getItem(0))
        }
    }

    @Test
    fun testMethodGetItemId() {
        AssemblySingleDataRecyclerListAdapter(TextItemFactory()).apply {
            Assert.assertEquals(-1L, getItemId(-1))
            Assert.assertEquals(-1L, getItemId(0))
            Assert.assertEquals(-1L, getItemId(1))
        }

        AssemblySingleDataRecyclerListAdapter(TextItemFactory()).apply {
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

        AssemblySingleDataRecyclerListAdapter(
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
    fun testMethodGetItemViewType() {
        AssemblySingleDataRecyclerListAdapter(TextItemFactory()).apply {
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
            Thread.sleep(10)    // ListAdapter internal asynchronous thread updates data, it takes a while to take effect
            Assert.assertEquals(0, getItemViewType(0))
        }
    }

    @Test
    fun testMethodCreateAndBindViewHolder() {
        val context = InstrumentationRegistry.getInstrumentation().context
        val parent = RecyclerView(context).apply {
            layoutManager = LinearLayoutManager(context)
        }
        AssemblySingleDataRecyclerListAdapter(TextItemFactory()).apply {
            data = Text("hello")
            Thread.sleep(10)    // ListAdapter internal asynchronous thread updates data, it takes a while to take effect

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
        AssemblySingleDataRecyclerListAdapter(itemFactory).apply {
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
            Thread.sleep(10)    // ListAdapter internal asynchronous thread updates data, it takes a while to take effect
            Assert.assertSame(itemFactory, getItemFactoryByPosition(0))
        }
    }
}