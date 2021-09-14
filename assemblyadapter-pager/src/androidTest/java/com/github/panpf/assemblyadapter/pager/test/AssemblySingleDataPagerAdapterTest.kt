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

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import androidx.test.platform.app.InstrumentationRegistry
import androidx.viewpager.widget.PagerAdapter
import com.github.panpf.assemblyadapter.NotFoundMatchedItemFactoryException
import com.github.panpf.assemblyadapter.pager.AssemblySingleDataPagerAdapter
import com.github.panpf.assemblyadapter.pager.GetPageTitle
import com.github.panpf.assemblyadapter.pager.PagerItemFactory
import com.github.panpf.assemblyadapter.pager.ViewPagerItemFactory
import com.github.panpf.tools4j.test.ktx.assertThrow
import org.junit.Assert
import org.junit.Test

class AssemblySingleDataPagerAdapterTest {

    data class Text(val text: String) : GetPageTitle {
        override val pageTitle: CharSequence = "PageTitle-Text-$text"
    }

    class TextPagerItemFactory : PagerItemFactory<Text>(Text::class) {

        override fun createItemView(
            context: Context,
            parent: ViewGroup,
            bindingAdapterPosition: Int,
            absoluteAdapterPosition: Int,
            data: Text
        ): View = TextView(context)
    }

    @Test
    fun testConstructor() {
        AssemblySingleDataPagerAdapter(TextPagerItemFactory()).apply {
            Assert.assertNull(data)
        }

        AssemblySingleDataPagerAdapter(
            TextPagerItemFactory(),
            Text("hello")
        ).apply {
            Assert.assertNotNull(data)
            Assert.assertEquals(Text("hello"), data)
        }
    }

    @Test
    fun testPropertyData() {
        AssemblySingleDataPagerAdapter(TextPagerItemFactory()).apply {
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
        AssemblySingleDataPagerAdapter(TextPagerItemFactory()).apply {
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
        AssemblySingleDataPagerAdapter(TextPagerItemFactory()).apply {
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
    fun testMethodInstantiateItem() {
        val context = InstrumentationRegistry.getInstrumentation().context
        val parent = FrameLayout(context)
        AssemblySingleDataPagerAdapter(TextPagerItemFactory()).apply {
            assertThrow(IndexOutOfBoundsException::class) {
                instantiateItem(parent, -1)
            }
            assertThrow(IndexOutOfBoundsException::class) {
                instantiateItem(parent, 0)
            }
            assertThrow(IndexOutOfBoundsException::class) {
                instantiateItem(parent, 1)
            }

            data = Text("hello")
            Assert.assertTrue(instantiateItem(parent, 0) is View)
        }
    }

    @Test
    fun testMethodDestroyItem() {
        val context = InstrumentationRegistry.getInstrumentation().context
        val parent = FrameLayout(context)
        AssemblySingleDataPagerAdapter(TextPagerItemFactory()).apply {
            data = Text("hello")
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
        AssemblySingleDataPagerAdapter(TextPagerItemFactory()).apply {
            data = Text("hello")
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
        AssemblySingleDataPagerAdapter(TextPagerItemFactory()).apply {
            data = Text("hello")
            val item = instantiateItem(parent, 0)
            Assert.assertEquals(PagerAdapter.POSITION_UNCHANGED, getItemPosition(item))

            notifyDataSetChanged()
            Assert.assertEquals(PagerAdapter.POSITION_UNCHANGED, getItemPosition(item))

            data = Text("world")
            Assert.assertEquals(PagerAdapter.POSITION_NONE, getItemPosition(item))

            data = null
            Assert.assertEquals(PagerAdapter.POSITION_NONE, getItemPosition(item))
        }
    }

    @Test
    fun testMethodGetItemFactoryByPosition() {
        val itemFactory = TextPagerItemFactory()
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

            data = Text("hello")
            Assert.assertSame(itemFactory, getItemFactoryByPosition(0))
        }
    }

    @Test
    fun testMethodGetItemFactoryByData() {
        val textItemFactory = TextPagerItemFactory()

        AssemblySingleDataPagerAdapter(textItemFactory).apply {
            Assert.assertSame(textItemFactory, getItemFactoryByData(Text("hello")))
        }
    }

    @Test
    fun testMethodGetItemFactoryByClass() {
        val textItemFactory = TextPagerItemFactory()

        AssemblySingleDataPagerAdapter(textItemFactory).apply {
            Assert.assertSame(
                textItemFactory,
                getItemFactoryByClass(TextPagerItemFactory::class.java)
            )
            assertThrow(NotFoundMatchedItemFactoryException::class) {
                getItemFactoryByClass(ViewPagerItemFactory::class.java)
            }
        }
    }

    @Test
    fun testMethodGetPageTitle() {
        AssemblySingleDataPagerAdapter(TextPagerItemFactory()).apply {
            Assert.assertNull(currentPageTitle)
            Assert.assertNull(getPageTitle(0))

            currentPageTitle = "hello"
            Assert.assertNotNull(currentPageTitle)
            Assert.assertEquals("hello", getPageTitle(0))
            Assert.assertNull(getPageTitle(1))

            currentPageTitle = null
            Assert.assertNull(currentPageTitle)
            Assert.assertNull(getPageTitle(0))

            data = Text("hello")
            Assert.assertNotNull(data)
            Assert.assertNull(currentPageTitle)
            Assert.assertEquals("PageTitle-Text-hello", getPageTitle(0))
            Assert.assertNull(getPageTitle(1))

            data = null
            Assert.assertNull(data)
            Assert.assertNull(currentPageTitle)
            Assert.assertNull(getPageTitle(0))
        }
    }
}