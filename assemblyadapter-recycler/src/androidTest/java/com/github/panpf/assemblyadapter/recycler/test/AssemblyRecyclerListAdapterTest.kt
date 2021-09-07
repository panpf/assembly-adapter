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

import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.RecyclerView
import androidx.test.platform.app.InstrumentationRegistry
import com.github.panpf.assemblyadapter.NotFoundMatchedItemFactoryException
import com.github.panpf.assemblyadapter.Placeholder
import com.github.panpf.assemblyadapter.ViewItemFactory
import com.github.panpf.assemblyadapter.recycler.*
import com.github.panpf.tools4j.test.ktx.assertThrow
import org.junit.Assert
import org.junit.Test

class AssemblyRecyclerListAdapterTest {

    private data class Text(val text: String) : DiffKey {
        override val diffKey: Any = "Text:$text"
    }

    private class TextItemFactory : ViewItemFactory<Text>(Text::class, { context, _, _ ->
        TextView(context)
    })

    private data class Image(val resId: Int) : DiffKey {
        override val diffKey: Any = "Image:$resId"
    }

    private class ImageItemFactory : ViewItemFactory<Image>(Image::class, { context, _, _ ->
        ImageView(context)
    })

    private data class NoDiffKey(val name: String = "")

    private class NoDiffKeyItemFactory :
        ViewItemFactory<NoDiffKey>(NoDiffKey::class, { context, _, _ ->
            ImageView(context)
        })

    private class PlaceholderItemFactory :
        ViewItemFactory<Placeholder>(Placeholder::class, android.R.layout.test_list_item)

    @Test
    fun testConstructor() {
        /**
         * ItemFactory, List, ItemCallback
         */
        // ItemFactory
        assertThrow(IllegalArgumentException::class) {
            AssemblyRecyclerListAdapter<Text>(listOf(), null, KeyEqualsDiffItemCallback())
        }
        AssemblyRecyclerListAdapter<Text>(
            listOf(TextItemFactory()),
            null,
            KeyEqualsDiffItemCallback()
        )

        // List
        AssemblyRecyclerListAdapter<Text>(
            listOf(TextItemFactory()),
            null,
            KeyEqualsDiffItemCallback()
        ).apply {
            Assert.assertEquals(0, itemCount)
        }
        AssemblyRecyclerListAdapter(
            listOf(TextItemFactory()),
            listOf(Text("hello")),
            KeyEqualsDiffItemCallback()
        ).apply {
            Assert.assertEquals(1, itemCount)
        }

        // ItemCallback
        assertThrow(IllegalArgumentException::class) {
            AssemblyRecyclerListAdapter<Any>(
                listOf(NoDiffKeyItemFactory()),
                null,
                KeyEqualsDiffItemCallback()
            )
        }
        AssemblyRecyclerListAdapter<Any>(
            listOf(NoDiffKeyItemFactory()),
            null,
            InstanceDiffItemCallback()
        )

        // ItemFactory, List
        AssemblyRecyclerListAdapter<Any>(listOf(TextItemFactory()), null)
        assertThrow(IllegalArgumentException::class) {
            AssemblyRecyclerListAdapter<Any>(listOf(NoDiffKeyItemFactory()), null)
        }

        // ItemFactory
        AssemblyRecyclerListAdapter<Any>(listOf(TextItemFactory()))
        assertThrow(IllegalArgumentException::class) {
            AssemblyRecyclerListAdapter<Any>(listOf(NoDiffKeyItemFactory()))
        }


        /**
         * ItemFactory ItemCallback
         */
        // ItemFactory
        assertThrow(IllegalArgumentException::class) {
            AssemblyRecyclerListAdapter<Text>(listOf(), KeyEqualsDiffItemCallback())
        }
        AssemblyRecyclerListAdapter<Text>(listOf(TextItemFactory()), KeyEqualsDiffItemCallback())

        // ItemCallback
        assertThrow(IllegalArgumentException::class) {
            AssemblyRecyclerListAdapter<Any>(
                listOf(NoDiffKeyItemFactory()),
                KeyEqualsDiffItemCallback()
            )
        }
        AssemblyRecyclerListAdapter<Any>(listOf(NoDiffKeyItemFactory()), InstanceDiffItemCallback())


        /**
         * ItemFactory, List, AsyncDifferConfig<DATA>
         */
        // ItemFactory
        assertThrow(IllegalArgumentException::class) {
            AssemblyRecyclerListAdapter<Text>(
                listOf(),
                null,
                AsyncDifferConfig.Builder<Text>(KeyEqualsDiffItemCallback()).build()
            )
        }
        AssemblyRecyclerListAdapter<Text>(
            listOf(TextItemFactory()),
            null,
            AsyncDifferConfig.Builder<Text>(KeyEqualsDiffItemCallback()).build()
        )

        // List
        AssemblyRecyclerListAdapter<Text>(
            listOf(TextItemFactory()),
            null,
            AsyncDifferConfig.Builder<Text>(KeyEqualsDiffItemCallback()).build()
        ).apply {
            Assert.assertEquals(0, itemCount)
        }
        AssemblyRecyclerListAdapter(
            listOf(TextItemFactory()),
            listOf(Text("hello")),
            AsyncDifferConfig.Builder<Text>(KeyEqualsDiffItemCallback()).build()
        ).apply {
            Assert.assertEquals(1, itemCount)
        }

        // ItemCallback
        assertThrow(IllegalArgumentException::class) {
            AssemblyRecyclerListAdapter<Any>(
                listOf(NoDiffKeyItemFactory()),
                null,
                AsyncDifferConfig.Builder<Any>(KeyEqualsDiffItemCallback()).build()
            )
        }
        AssemblyRecyclerListAdapter<Any>(
            listOf(NoDiffKeyItemFactory()),
            null,
            AsyncDifferConfig.Builder<Any>(InstanceDiffItemCallback()).build()
        )


        /**
         * ItemFactory DiffUtil.AsyncDifferConfig<DATA>
         */
        // ItemFactory
        assertThrow(IllegalArgumentException::class) {
            AssemblyRecyclerListAdapter<Text>(
                listOf(),
                AsyncDifferConfig.Builder<Text>(KeyEqualsDiffItemCallback()).build()
            )
        }
        AssemblyRecyclerListAdapter<Text>(
            listOf(TextItemFactory()),
            AsyncDifferConfig.Builder<Text>(KeyEqualsDiffItemCallback()).build()
        )

        // ItemCallback
        assertThrow(IllegalArgumentException::class) {
            AssemblyRecyclerListAdapter<Any>(
                listOf(NoDiffKeyItemFactory()),
                AsyncDifferConfig.Builder<Any>(KeyEqualsDiffItemCallback()).build()
            )
        }
        AssemblyRecyclerListAdapter<Any>(
            listOf(NoDiffKeyItemFactory()),
            AsyncDifferConfig.Builder<Any>(InstanceDiffItemCallback()).build()
        )
    }

    @Test
    fun testPropertyCurrentListAndSubmitList() {
        var dataFromObserver: List<Text>? = null
        AssemblyRecyclerListAdapter<Text>(listOf(TextItemFactory())).apply {
            registerAdapterDataObserver(SimpleAdapterDataObserver {
                dataFromObserver = currentList
            })

            Assert.assertEquals("", currentList.joinToString { it.text })
            Assert.assertEquals("", (dataFromObserver ?: emptyList()).joinToString { it.text })

            submitList(listOf(Text("hello")))
            Thread.sleep(10)    // ListAdapter internal asynchronous thread updates data, it takes a while to take effect
            Assert.assertEquals("hello", currentList.joinToString { it.text })
            Assert.assertEquals("hello", (dataFromObserver ?: emptyList()).joinToString { it.text })

            submitList(listOf(Text("hello"), Text("world")))
            Thread.sleep(10)    // ListAdapter internal asynchronous thread updates data, it takes a while to take effect
            Assert.assertEquals("hello, world", currentList.joinToString { it.text })
            Assert.assertEquals(
                "hello, world",
                (dataFromObserver ?: emptyList()).joinToString { it.text })

            submitList(null)
            Thread.sleep(10)    // ListAdapter internal asynchronous thread updates data, it takes a while to take effect
            Assert.assertEquals("", currentList.joinToString())
            Assert.assertEquals("", (dataFromObserver ?: emptyList()).joinToString())
        }
    }

    @Test
    fun testMethodGetCount() {
        AssemblyRecyclerListAdapter<Text>(listOf(TextItemFactory())).apply {
            Assert.assertEquals(0, itemCount)

            submitList(listOf(Text("hello")))
            Thread.sleep(10)    // ListAdapter internal asynchronous thread updates data, it takes a while to take effect
            Assert.assertEquals(1, itemCount)

            submitList(listOf(Text("hello"), Text("world")))
            Thread.sleep(10)    // ListAdapter internal asynchronous thread updates data, it takes a while to take effect
            Assert.assertEquals(2, itemCount)

            submitList(null)
            Thread.sleep(10)    // ListAdapter internal asynchronous thread updates data, it takes a while to take effect
            Assert.assertEquals(0, itemCount)
        }
    }

    @Test
    fun testMethodGetItemData() {
        AssemblyRecyclerListAdapter<Text>(listOf(TextItemFactory())).apply {
            assertThrow(IndexOutOfBoundsException::class) {
                getItemData(-1)
            }
            assertThrow(IndexOutOfBoundsException::class) {
                getItemData(0)
            }
            assertThrow(IndexOutOfBoundsException::class) {
                getItemData(1)
            }

            submitList(listOf(Text("hello"), Text("world")))
            Thread.sleep(10)    // ListAdapter internal asynchronous thread updates data, it takes a while to take effect
            Assert.assertEquals(Text("hello"), getItemData(0))
            Assert.assertEquals(Text("world"), getItemData(1))
        }
    }

    @Test
    fun testMethodGetItemId() {
        AssemblyRecyclerListAdapter<Text>(listOf(TextItemFactory())).apply {
            Assert.assertEquals(-1L, getItemId(-1))
            Assert.assertEquals(-1L, getItemId(0))
            Assert.assertEquals(-1L, getItemId(1))

            assertThrow(UnsupportedOperationException::class) {
                setHasStableIds(true)
            }
            Assert.assertEquals(-1L, getItemId(-1))
            Assert.assertEquals(-1L, getItemId(0))
            Assert.assertEquals(-1L, getItemId(1))

            submitList(listOf(Text("hello"), Text("world")))
            Assert.assertEquals(-1L, getItemId(-1))
            Assert.assertEquals(-1L, getItemId(0))
            Assert.assertEquals(-1L, getItemId(1))
            Assert.assertEquals(-1L, getItemId(2))
        }
    }

    @Test
    fun testMethodGetItemViewType() {
        AssemblyRecyclerListAdapter<Any>(listOf(TextItemFactory(), ImageItemFactory())).apply {
            assertThrow(IndexOutOfBoundsException::class) {
                getItemViewType(-1)
            }
            assertThrow(IndexOutOfBoundsException::class) {
                getItemViewType(0)
            }
            assertThrow(IndexOutOfBoundsException::class) {
                getItemViewType(1)
            }

            submitList(listOf(Image(android.R.drawable.alert_dark_frame), Text("hello")))
            Thread.sleep(10)    // ListAdapter internal asynchronous thread updates data, it takes a while to take effect
            Assert.assertEquals(1, getItemViewType(0))
            Assert.assertEquals(0, getItemViewType(1))
        }
    }

    @Test
    fun testMethodCreateAndBindViewHolder() {
        val context = InstrumentationRegistry.getInstrumentation().context
        val parent = FrameLayout(context)
        AssemblyRecyclerListAdapter<Any>(listOf(TextItemFactory(), ImageItemFactory())).apply {
            submitList(listOf(Text("hello"), Image(android.R.drawable.alert_dark_frame)))
            Thread.sleep(10)    // ListAdapter internal asynchronous thread updates data, it takes a while to take effect

            assertThrow(IllegalArgumentException::class) {
                onCreateViewHolder(parent, -1)
            }
            Assert.assertTrue(onCreateViewHolder(parent, getItemViewType(0)).itemView is TextView)
            Assert.assertTrue(onCreateViewHolder(parent, getItemViewType(1)).itemView is ImageView)

            assertThrow(IllegalArgumentException::class) {
                onBindViewHolder(object : RecyclerView.ViewHolder(TextView(context)) {}, 0)
            }
            onBindViewHolder(onCreateViewHolder(parent, getItemViewType(0)), 0)
            onBindViewHolder(onCreateViewHolder(parent, getItemViewType(1)), 1)
        }
    }

    @Test
    fun testMethodGetItemFactoryByPosition() {
        val textItemFactory = TextItemFactory()
        val imageItemFactory = ImageItemFactory()
        AssemblyRecyclerListAdapter<Any>(listOf(textItemFactory, imageItemFactory)).apply {
            assertThrow(IndexOutOfBoundsException::class) {
                getItemFactoryByPosition(-1)
            }
            assertThrow(IndexOutOfBoundsException::class) {
                getItemFactoryByPosition(0)
            }
            assertThrow(IndexOutOfBoundsException::class) {
                getItemFactoryByPosition(1)
            }

            submitList(listOf(Image(android.R.drawable.alert_dark_frame), Text("hello")))
            Thread.sleep(10)    // ListAdapter internal asynchronous thread updates data, it takes a while to take effect
            Assert.assertSame(imageItemFactory, getItemFactoryByPosition(0))
            Assert.assertSame(textItemFactory, getItemFactoryByPosition(1))
        }

        AssemblyRecyclerListAdapter(
            listOf(textItemFactory),
            listOf(Image(android.R.drawable.alert_dark_frame), Text("hello"))
        ).apply {
            assertThrow(NotFoundMatchedItemFactoryException::class) {
                getItemFactoryByPosition(0)
            }
            Assert.assertSame(textItemFactory, getItemFactoryByPosition(1))
        }

        AssemblyRecyclerListAdapter(
            listOf(imageItemFactory),
            listOf(Image(android.R.drawable.alert_dark_frame), Text("hello"))
        ).apply {
            Assert.assertSame(imageItemFactory, getItemFactoryByPosition(0))
            assertThrow(NotFoundMatchedItemFactoryException::class) {
                getItemFactoryByPosition(1)
            }
        }
    }

    @Test
    fun testPlaceholder() {
        val context = InstrumentationRegistry.getInstrumentation().context
        val parent = FrameLayout(context)

        AssemblyRecyclerListAdapter<Any?>(listOf(TextItemFactory())).apply {
            submitList(listOf(Text("hello"), null))
            Thread.sleep(10)    // ListAdapter internal asynchronous thread updates data, it takes a while to take effect

            Assert.assertEquals(0, getItemViewType(0))
            assertThrow(NotFoundMatchedItemFactoryException::class) {
                Assert.assertEquals(0, getItemViewType(1))
            }

            onCreateViewHolder(parent, getItemViewType(0))
            assertThrow(NotFoundMatchedItemFactoryException::class) {
                onCreateViewHolder(parent, getItemViewType(1))
            }

            getItemFactoryByPosition(0)
            assertThrow(NotFoundMatchedItemFactoryException::class) {
                getItemFactoryByPosition(1)
            }
        }

        AssemblyRecyclerListAdapter<Any?>(
            listOf(
                TextItemFactory(),
                PlaceholderItemFactory()
            )
        ).apply {
            submitList(listOf(Text("hello"), null))
            Thread.sleep(10)    // ListAdapter internal asynchronous thread updates data, it takes a while to take effect

            Assert.assertEquals(0, getItemViewType(0))
            Assert.assertEquals(1, getItemViewType(1))

            onCreateViewHolder(parent, getItemViewType(0))
            onCreateViewHolder(parent, getItemViewType(1))

            getItemFactoryByPosition(0)
            getItemFactoryByPosition(1)
        }
    }
}