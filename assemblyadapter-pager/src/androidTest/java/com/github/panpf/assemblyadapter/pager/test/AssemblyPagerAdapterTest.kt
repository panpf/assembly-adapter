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
package com.github.panpf.assemblyadapter.pager.test

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.test.platform.app.InstrumentationRegistry
import com.github.panpf.assemblyadapter.NotFoundMatchedItemFactoryException
import com.github.panpf.assemblyadapter.Placeholder
import com.github.panpf.assemblyadapter.pager.AssemblyPagerAdapter
import com.github.panpf.assemblyadapter.pager.GetPageTitle
import com.github.panpf.assemblyadapter.pager.PagerItemFactory
import com.github.panpf.assemblyadapter.pager.ViewPagerItemFactory
import com.github.panpf.tools4j.test.ktx.assertThrow
import org.junit.Assert
import org.junit.Test
import java.util.Date

class AssemblyPagerAdapterTest {

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

    data class Image(val resId: Int) : GetPageTitle {
        override val pageTitle: CharSequence = "PageTitle-Image-$resId"
    }

    class ImagePagerItemFactory : PagerItemFactory<Image>(Image::class) {

        override fun createItemView(
            context: Context,
            parent: ViewGroup,
            bindingAdapterPosition: Int,
            absoluteAdapterPosition: Int,
            data: Image
        ): View = ImageView(context)
    }

    private class PlaceholderPagerItemFactory :
        ViewPagerItemFactory<Placeholder>(Placeholder::class, android.R.layout.test_list_item)

    @Test
    fun testConstructor() {
        assertThrow(IllegalArgumentException::class) {
            AssemblyPagerAdapter<Any>(listOf())
        }
        AssemblyPagerAdapter<Any>(listOf(TextPagerItemFactory(), ImagePagerItemFactory())).apply {
            Assert.assertEquals(0, currentList.size)
        }
        AssemblyPagerAdapter<Any>(
            listOf(TextPagerItemFactory(), ImagePagerItemFactory()),
            listOf(Text("hello"), Image(android.R.drawable.btn_default))
        ).apply {
            Assert.assertEquals(2, currentList.size)
        }
    }

    @Test
    fun testPropertyCurrentListAndSubmitList() {
        AssemblyPagerAdapter<Any>(
            listOf(TextPagerItemFactory(), ImagePagerItemFactory())
        ).apply {
            Assert.assertEquals("", currentList.joinToString())

            submitList(listOf(Text("hello")))
            Assert.assertEquals("Text(text=hello)", currentList.joinToString())

            submitList(listOf(Text("hello"), Text("world")))
            Assert.assertEquals("Text(text=hello), Text(text=world)", currentList.joinToString())

            submitList(null)
            Assert.assertEquals("", currentList.joinToString())
        }
    }

    @Test
    fun testMethodGetCount() {
        AssemblyPagerAdapter<Any>(
            listOf(TextPagerItemFactory(), ImagePagerItemFactory())
        ).apply {
            Assert.assertEquals(0, count)
            Assert.assertEquals(0, itemCount)

            submitList(listOf(Text("hello")))
            Assert.assertEquals(1, count)
            Assert.assertEquals(1, itemCount)

            submitList(listOf(Text("hello"), Text("world")))
            Assert.assertEquals(2, count)
            Assert.assertEquals(2, itemCount)

            submitList(null)
            Assert.assertEquals(0, count)
            Assert.assertEquals(0, itemCount)
        }
    }

    @Test
    fun testMethodGetItemData() {
        AssemblyPagerAdapter<Any>(
            listOf(TextPagerItemFactory(), ImagePagerItemFactory())
        ).apply {
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
            Assert.assertEquals(Text("hello"), getItemData(0))
            Assert.assertEquals(Text("world"), getItemData(1))
        }
    }

    @Test
    fun testMethodInstantiateItem() {
        val context = InstrumentationRegistry.getInstrumentation().context
        val parent = FrameLayout(context)
        AssemblyPagerAdapter<Any>(
            listOf(TextPagerItemFactory(), ImagePagerItemFactory())
        ).apply {
            submitList(listOf(Text("hello"), Image(android.R.drawable.alert_dark_frame)))

            Assert.assertTrue(instantiateItem(parent, 0) is TextView)
            Assert.assertTrue(instantiateItem(parent, 1) is ImageView)
        }
    }

    @Test
    fun testMethodGetItemFactoryByPosition() {
        AssemblyPagerAdapter<Any>(
            listOf(TextPagerItemFactory(), ImagePagerItemFactory())
        ).apply {
            assertThrow(IndexOutOfBoundsException::class) {
                getItemFactoryByPosition(-1)
            }
            assertThrow(IndexOutOfBoundsException::class) {
                getItemFactoryByPosition(0)
            }
            assertThrow(IndexOutOfBoundsException::class) {
                getItemFactoryByPosition(1)
            }

            submitList(
                listOf(
                    Image(android.R.drawable.alert_dark_frame),
                    Text("hello")
                )
            )
            Assert.assertEquals(ImagePagerItemFactory::class, getItemFactoryByPosition(0)::class)
            Assert.assertEquals(TextPagerItemFactory::class, getItemFactoryByPosition(1)::class)
        }
    }

    @Test
    fun testMethodGetItemFactoryByData() {
        val textItemFactory = TextPagerItemFactory()
        val imageItemFactory = ImagePagerItemFactory()
        val placeholderItemFactory = PlaceholderPagerItemFactory()

        AssemblyPagerAdapter<Any>(listOf(textItemFactory, imageItemFactory)).apply {
            Assert.assertSame(
                imageItemFactory,
                getItemFactoryByData(Image(android.R.drawable.alert_dark_frame))
            )
            Assert.assertSame(textItemFactory, getItemFactoryByData(Text("hello")))
            assertThrow(NotFoundMatchedItemFactoryException::class) {
                getItemFactoryByData(Date())
            }
        }

        AssemblyPagerAdapter<Any?>(listOf(textItemFactory, imageItemFactory)).apply {
            assertThrow(NotFoundMatchedItemFactoryException::class) {
                getItemFactoryByData(null)
            }
        }
        AssemblyPagerAdapter<Any?>(
            listOf(
                textItemFactory,
                imageItemFactory,
                placeholderItemFactory
            )
        ).apply {
            Assert.assertSame(placeholderItemFactory, getItemFactoryByData(null))
        }
    }

    @Test
    fun testMethodGetItemFactoryByClass() {
        val textItemFactory = TextPagerItemFactory()
        val imageItemFactory = ImagePagerItemFactory()
        val placeholderItemFactory = PlaceholderPagerItemFactory()

        AssemblyPagerAdapter<Any>(listOf(textItemFactory, imageItemFactory)).apply {
            Assert.assertSame(
                imageItemFactory,
                getItemFactoryByClass(ImagePagerItemFactory::class.java)
            )
            Assert.assertSame(
                textItemFactory,
                getItemFactoryByClass(TextPagerItemFactory::class.java)
            )
            assertThrow(NotFoundMatchedItemFactoryException::class) {
                getItemFactoryByClass(ViewPagerItemFactory::class.java)
            }
        }
        AssemblyPagerAdapter<Any?>(
            listOf(textItemFactory, imageItemFactory, placeholderItemFactory)
        ).apply {
            Assert.assertSame(
                placeholderItemFactory,
                getItemFactoryByClass(PlaceholderPagerItemFactory::class.java)
            )
        }
    }

    @Test
    fun testMethodGetPageTitle() {
        AssemblyPagerAdapter<Any>(
            listOf(TextPagerItemFactory(), ImagePagerItemFactory())
        ).apply {
            Assert.assertEquals(0, currentPageTitleList.size)
            Assert.assertNull(getPageTitle(0))

            submitPageTitleList(listOf("hello"))
            Assert.assertEquals(1, currentPageTitleList.size)
            Assert.assertEquals("hello", getPageTitle(0))
            Assert.assertNull(getPageTitle(1))

            submitPageTitleList(listOf("hello", "world"))
            Assert.assertEquals(2, currentPageTitleList.size)
            Assert.assertEquals("hello", getPageTitle(0))
            Assert.assertEquals("world", getPageTitle(1))
            Assert.assertNull(getPageTitle(2))

            submitPageTitleList(null)
            Assert.assertEquals(0, currentPageTitleList.size)
            Assert.assertNull(getPageTitle(0))

            submitList(listOf(Text("hello")))
            Assert.assertEquals(0, currentPageTitleList.size)
            Assert.assertEquals(1, currentList.size)
            Assert.assertEquals(Text("hello"), getItemData(0))
            Assert.assertEquals("PageTitle-Text-hello", getPageTitle(0))
            Assert.assertNull(getPageTitle(1))

            submitList(listOf(Text("hello"), Image(android.R.drawable.btn_default)))
            Assert.assertEquals(0, currentPageTitleList.size)
            Assert.assertEquals(2, currentList.size)
            Assert.assertEquals(Text("hello"), getItemData(0))
            Assert.assertEquals("PageTitle-Text-hello", getPageTitle(0))
            Assert.assertEquals(Image(android.R.drawable.btn_default), getItemData(1))
            Assert.assertEquals(
                "PageTitle-Image-${android.R.drawable.btn_default}",
                getPageTitle(1)
            )
            Assert.assertNull(getPageTitle(2))

            submitList(null)
            Assert.assertEquals(0, currentPageTitleList.size)
            Assert.assertEquals(0, currentList.size)
            Assert.assertNull(getPageTitle(0))
        }
    }
}