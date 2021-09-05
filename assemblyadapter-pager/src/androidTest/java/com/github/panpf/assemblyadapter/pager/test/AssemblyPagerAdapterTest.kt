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

import android.R
import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.test.platform.app.InstrumentationRegistry
import com.github.panpf.assemblyadapter.pager.AssemblyPagerAdapter
import com.github.panpf.assemblyadapter.pager.PagerItemFactory
import com.github.panpf.tools4j.test.ktx.assertThrow
import org.junit.Assert
import org.junit.Test

class AssemblyPagerAdapterTest {

    data class Text(val text: String)

    class TextPagerItemFactory : PagerItemFactory<Text>(Text::class) {

        override fun createItemView(
            context: Context,
            parent: ViewGroup,
            bindingAdapterPosition: Int,
            absoluteAdapterPosition: Int,
            data: Text
        ): View = TextView(context)
    }

    data class Image(val resId: Int)

    class ImagePagerItemFactory : PagerItemFactory<Image>(Image::class) {

        override fun createItemView(
            context: Context,
            parent: ViewGroup,
            bindingAdapterPosition: Int,
            absoluteAdapterPosition: Int,
            data: Image
        ): View = ImageView(context)
    }

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
            listOf(Text("hello"), Image(R.drawable.btn_default))
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

            submitList(listOf(Text("hello")))
            Assert.assertEquals(1, count)

            submitList(listOf(Text("hello"), Text("world")))
            Assert.assertEquals(2, count)

            submitList(null)
            Assert.assertEquals(0, count)
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
            submitList(listOf(Text("hello"), Image(R.drawable.alert_dark_frame)))

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
                    Image(R.drawable.alert_dark_frame),
                    Text("hello")
                )
            )
            Assert.assertEquals(ImagePagerItemFactory::class, getItemFactoryByPosition(0)::class)
            Assert.assertEquals(TextPagerItemFactory::class, getItemFactoryByPosition(1)::class)
        }
    }
}