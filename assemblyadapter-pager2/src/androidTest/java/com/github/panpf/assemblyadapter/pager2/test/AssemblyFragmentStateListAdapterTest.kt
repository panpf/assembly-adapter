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
package com.github.panpf.assemblyadapter.pager2.test

import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.AsyncDifferConfig
import com.github.panpf.assemblyadapter.NotFoundMatchedItemFactoryException
import com.github.panpf.assemblyadapter.Placeholder
import com.github.panpf.assemblyadapter.pager.FragmentItemFactory
import com.github.panpf.assemblyadapter.pager.ViewFragmentItemFactory
import com.github.panpf.assemblyadapter.pager2.AssemblyFragmentStateListAdapter
import com.github.panpf.assemblyadapter.recycler.DiffKey
import com.github.panpf.assemblyadapter.recycler.KeyEqualsDiffItemCallback
import com.github.panpf.tools4a.test.ktx.getFragmentSync
import com.github.panpf.tools4a.test.ktx.launchFragmentInContainer
import com.github.panpf.tools4j.test.ktx.assertThrow
import org.junit.Assert
import org.junit.Test
import java.util.Date

class AssemblyFragmentStateListAdapterTest {

    data class Text(val text: String) : DiffKey {
        override val diffKey = "Text:$text"
    }

    class TextFragmentItemFactory : FragmentItemFactory<Text>(Text::class) {
        override fun createFragment(
            bindingAdapterPosition: Int,
            absoluteAdapterPosition: Int,
            data: Text
        ): Fragment = TextFragment()
    }

    class TextFragment : Fragment()

    data class Image(val resId: Int) : DiffKey {
        override val diffKey = "Image:$resId"
    }

    class ImageFragmentItemFactory : FragmentItemFactory<Image>(Image::class) {
        override fun createFragment(
            bindingAdapterPosition: Int,
            absoluteAdapterPosition: Int,
            data: Image
        ): Fragment = ImageFragment()
    }

    class ImageFragment : Fragment()

    class TestFragment : Fragment()

    class PlaceholderFragmentItemFactory :
        ViewFragmentItemFactory<Placeholder>(Placeholder::class, android.R.layout.test_list_item)

    @Test
    fun testConstructor() {
        val fragmentScenario = TestFragment::class.launchFragmentInContainer()
        val fragment = fragmentScenario.getFragmentSync()

        assertThrow(IllegalArgumentException::class) {
            AssemblyFragmentStateListAdapter<Any>(
                fragment, listOf(),
            )
        }
        AssemblyFragmentStateListAdapter<Any>(
            fragment, listOf(TextFragmentItemFactory(), ImageFragmentItemFactory())
        ).apply {
            Assert.assertEquals(0, currentList.size)
        }
        AssemblyFragmentStateListAdapter<Any>(
            fragment,
            listOf(TextFragmentItemFactory(), ImageFragmentItemFactory()),
            listOf(Text("hello"), Image(android.R.drawable.btn_default))
        ).apply {
            Assert.assertEquals(2, currentList.size)
        }

        assertThrow(IllegalArgumentException::class) {
            AssemblyFragmentStateListAdapter<Any>(
                fragment.requireActivity(), listOf(),
            )
        }
        AssemblyFragmentStateListAdapter<Any>(
            fragment.requireActivity(),
            listOf(TextFragmentItemFactory(), ImageFragmentItemFactory())
        ).apply {
            Assert.assertEquals(0, currentList.size)
        }
        AssemblyFragmentStateListAdapter<Any>(
            fragment.requireActivity(),
            listOf(TextFragmentItemFactory(), ImageFragmentItemFactory()),
            listOf(Text("hello"), Image(android.R.drawable.btn_default))
        ).apply {
            Assert.assertEquals(2, currentList.size)
        }

        assertThrow(IllegalArgumentException::class) {
            AssemblyFragmentStateListAdapter<Any>(
                fragment.childFragmentManager, fragment.lifecycle, listOf(),
            )
        }
        AssemblyFragmentStateListAdapter<Any>(
            fragment.childFragmentManager,
            fragment.lifecycle,
            listOf(TextFragmentItemFactory(), ImageFragmentItemFactory())
        ).apply {
            Assert.assertEquals(0, currentList.size)
        }
        AssemblyFragmentStateListAdapter<Any>(
            fragment.childFragmentManager,
            fragment.lifecycle,
            listOf(TextFragmentItemFactory(), ImageFragmentItemFactory()),
            listOf(Text("hello"), Image(android.R.drawable.btn_default))
        ).apply {
            Assert.assertEquals(2, currentList.size)
        }

        assertThrow(IllegalArgumentException::class) {
            AssemblyFragmentStateListAdapter<Any>(
                fragment,
                listOf(),
                null,
                AsyncDifferConfig.Builder<Any>(KeyEqualsDiffItemCallback()).build()
            )
        }
        AssemblyFragmentStateListAdapter<Any>(
            fragment,
            listOf(TextFragmentItemFactory(), ImageFragmentItemFactory()),
            null,
            AsyncDifferConfig.Builder<Any>(KeyEqualsDiffItemCallback()).build()
        ).apply {
            Assert.assertEquals(0, currentList.size)
        }
        AssemblyFragmentStateListAdapter<Any>(
            fragment,
            listOf(TextFragmentItemFactory(), ImageFragmentItemFactory()),
            listOf(Text("hello"), Image(android.R.drawable.btn_default)),
            AsyncDifferConfig.Builder<Any>(KeyEqualsDiffItemCallback()).build()
        ).apply {
            Assert.assertEquals(2, currentList.size)
        }

        assertThrow(IllegalArgumentException::class) {
            AssemblyFragmentStateListAdapter<Any>(
                fragment.requireActivity(),
                listOf(),
                null,
                AsyncDifferConfig.Builder<Any>(KeyEqualsDiffItemCallback()).build()
            )
        }
        AssemblyFragmentStateListAdapter<Any>(
            fragment.requireActivity(),
            listOf(TextFragmentItemFactory(), ImageFragmentItemFactory()),
            null,
            AsyncDifferConfig.Builder<Any>(KeyEqualsDiffItemCallback()).build()
        ).apply {
            Assert.assertEquals(0, currentList.size)
        }
        AssemblyFragmentStateListAdapter<Any>(
            fragment.requireActivity(),
            listOf(TextFragmentItemFactory(), ImageFragmentItemFactory()),
            listOf(Text("hello"), Image(android.R.drawable.btn_default)),
            AsyncDifferConfig.Builder<Any>(KeyEqualsDiffItemCallback()).build()

        ).apply {
            Assert.assertEquals(2, currentList.size)
        }

        assertThrow(IllegalArgumentException::class) {
            AssemblyFragmentStateListAdapter<Any>(
                fragment.childFragmentManager,
                fragment.lifecycle,
                listOf(),
                null,
                AsyncDifferConfig.Builder<Any>(KeyEqualsDiffItemCallback()).build()
            )
        }
        AssemblyFragmentStateListAdapter<Any>(
            fragment.childFragmentManager,
            fragment.lifecycle,
            listOf(TextFragmentItemFactory(), ImageFragmentItemFactory()),
            null,
            AsyncDifferConfig.Builder<Any>(KeyEqualsDiffItemCallback()).build()
        ).apply {
            Assert.assertEquals(0, currentList.size)
        }
        AssemblyFragmentStateListAdapter<Any>(
            fragment.childFragmentManager,
            fragment.lifecycle,
            listOf(TextFragmentItemFactory(), ImageFragmentItemFactory()),
            listOf(Text("hello"), Image(android.R.drawable.btn_default)),
            AsyncDifferConfig.Builder<Any>(KeyEqualsDiffItemCallback()).build()
        ).apply {
            Assert.assertEquals(2, currentList.size)
        }
    }

    @Test
    fun testPropertyCurrentListAndSubmitList() {
        val fragmentScenario = TestFragment::class.launchFragmentInContainer()
        val fragment = fragmentScenario.getFragmentSync()
        AssemblyFragmentStateListAdapter<Any>(
            fragment, listOf(TextFragmentItemFactory(), ImageFragmentItemFactory())
        ).apply {
            Assert.assertEquals("", currentList.joinToString())

            submitList(listOf(Text("hello")))
            Thread.sleep(100)    // ListAdapter internal asynchronous thread updates data, it takes a while to take effect
            Assert.assertEquals("Text(text=hello)", currentList.joinToString())

            submitList(listOf(Text("hello"), Text("world")))
            Thread.sleep(100)    // ListAdapter internal asynchronous thread updates data, it takes a while to take effect
            Assert.assertEquals("Text(text=hello), Text(text=world)", currentList.joinToString())

            submitList(null)
            Thread.sleep(100)    // ListAdapter internal asynchronous thread updates data, it takes a while to take effect
            Assert.assertEquals("", currentList.joinToString())
        }
    }

    @Test
    fun testMethodGetCount() {
        val fragmentScenario = TestFragment::class.launchFragmentInContainer()
        val fragment = fragmentScenario.getFragmentSync()
        AssemblyFragmentStateListAdapter<Any>(
            fragment, listOf(TextFragmentItemFactory(), ImageFragmentItemFactory())
        ).apply {
            Assert.assertEquals(0, itemCount)

            submitList(listOf(Text("hello")))
            Thread.sleep(100)    // ListAdapter internal asynchronous thread updates data, it takes a while to take effect
            Assert.assertEquals(1, itemCount)

            submitList(listOf(Text("hello"), Text("world")))
            Thread.sleep(100)    // ListAdapter internal asynchronous thread updates data, it takes a while to take effect
            Assert.assertEquals(2, itemCount)

            submitList(null)
            Thread.sleep(100)    // ListAdapter internal asynchronous thread updates data, it takes a while to take effect
            Assert.assertEquals(0, itemCount)
        }
    }

    @Test
    fun testMethodGetItemData() {
        val fragmentScenario = TestFragment::class.launchFragmentInContainer()
        val fragment = fragmentScenario.getFragmentSync()
        AssemblyFragmentStateListAdapter<Any>(
            fragment, listOf(TextFragmentItemFactory(), ImageFragmentItemFactory())
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
            Thread.sleep(100)    // ListAdapter internal asynchronous thread updates data, it takes a while to take effect
            Assert.assertEquals(Text("hello"), getItemData(0))
            Assert.assertEquals(Text("world"), getItemData(1))
        }
    }

    @Test
    fun testMethodGetItemId() {
        val fragmentScenario = TestFragment::class.launchFragmentInContainer()
        val fragment = fragmentScenario.getFragmentSync()

        AssemblyFragmentStateListAdapter<Text>(
            fragment,
            listOf(TextFragmentItemFactory())
        ).apply {
            assertThrow(UnsupportedOperationException::class) {
                setHasStableIds(true)
            }
            Assert.assertEquals(-1L, getItemId(-1))
            Assert.assertEquals(0L, getItemId(0))
            Assert.assertEquals(1L, getItemId(1))
        }

        AssemblyFragmentStateListAdapter<Any>(
            fragment, listOf(TextFragmentItemFactory(), ImageFragmentItemFactory())
        ).apply {
            assertThrow(UnsupportedOperationException::class) {
                setHasStableIds(true)
            }
            Assert.assertEquals(-1L, getItemId(-1))
            Assert.assertEquals(0L, getItemId(0))
            Assert.assertEquals(1L, getItemId(1))

            submitList(listOf(Text("hello"), Text("world")))
            Thread.sleep(100)    // ListAdapter internal asynchronous thread updates data, it takes a while to take effect
            Assert.assertEquals(-1L, getItemId(-1))
            Assert.assertEquals(0L, getItemId(0))
            Assert.assertEquals(1L, getItemId(1))
            Assert.assertEquals(2L, getItemId(2))
        }
    }

    @Test
    fun testMethodGetItemViewType() {
        val fragmentScenario = TestFragment::class.launchFragmentInContainer()
        val fragment = fragmentScenario.getFragmentSync()
        AssemblyFragmentStateListAdapter<Any>(
            fragment, listOf(TextFragmentItemFactory(), ImageFragmentItemFactory())
        ).apply {
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
            Thread.sleep(100)    // ListAdapter internal asynchronous thread updates data, it takes a while to take effect
            Assert.assertEquals(1, getItemViewType(0))
            Assert.assertEquals(0, getItemViewType(1))
        }
    }

    @Test
    fun testMethodCreateFragment() {
        val fragmentScenario = TestFragment::class.launchFragmentInContainer()
        val fragment = fragmentScenario.getFragmentSync()
        AssemblyFragmentStateListAdapter<Any>(
            fragment, listOf(TextFragmentItemFactory(), ImageFragmentItemFactory())
        ).apply {
            submitList(listOf(Text("hello"), Image(android.R.drawable.alert_dark_frame)))
            Thread.sleep(100)    // ListAdapter internal asynchronous thread updates data, it takes a while to take effect

            Assert.assertTrue(createFragment(0) is TextFragment)
            Assert.assertTrue(createFragment(1) is ImageFragment)
        }
    }

    @Test
    fun testMethodGetItemFactoryByPosition() {
        val fragmentScenario = TestFragment::class.launchFragmentInContainer()
        val fragment = fragmentScenario.getFragmentSync()
        AssemblyFragmentStateListAdapter<Any>(
            fragment, listOf(TextFragmentItemFactory(), ImageFragmentItemFactory())
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

            submitList(listOf(Image(android.R.drawable.alert_dark_frame), Text("hello")))
            Thread.sleep(100)    // ListAdapter internal asynchronous thread updates data, it takes a while to take effect
            Assert.assertEquals(ImageFragmentItemFactory::class, getItemFactoryByPosition(0)::class)
            Assert.assertEquals(TextFragmentItemFactory::class, getItemFactoryByPosition(1)::class)
        }
    }

    @Test
    fun testMethodGetItemFactoryByData() {
        val fragmentScenario = TestFragment::class.launchFragmentInContainer()
        val fragment = fragmentScenario.getFragmentSync()
        val textItemFactory = TextFragmentItemFactory()
        val imageItemFactory = ImageFragmentItemFactory()
        val placeholderItemFactory = PlaceholderFragmentItemFactory()

        AssemblyFragmentStateListAdapter<Any>(
            fragment,
            listOf(textItemFactory, imageItemFactory)
        ).apply {
            Assert.assertSame(
                imageItemFactory,
                getItemFactoryByData(Image(android.R.drawable.alert_dark_frame))
            )
            Assert.assertSame(
                textItemFactory, getItemFactoryByData(
                    Text(
                        "hello"
                    )
                )
            )
            assertThrow(NotFoundMatchedItemFactoryException::class) {
                getItemFactoryByData(Date())
            }
        }

        AssemblyFragmentStateListAdapter<Any?>(
            fragment,
            listOf(textItemFactory, imageItemFactory)
        ).apply {
            assertThrow(NotFoundMatchedItemFactoryException::class) {
                getItemFactoryByData(null)
            }
        }
        AssemblyFragmentStateListAdapter<Any?>(
            fragment,
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
        val fragmentScenario = TestFragment::class.launchFragmentInContainer()
        val fragment = fragmentScenario.getFragmentSync()
        val textItemFactory = TextFragmentItemFactory()
        val imageItemFactory = ImageFragmentItemFactory()
        val placeholderItemFactory = PlaceholderFragmentItemFactory()

        AssemblyFragmentStateListAdapter<Any>(
            fragment,
            listOf(textItemFactory, imageItemFactory)
        ).apply {
            Assert.assertSame(
                imageItemFactory,
                getItemFactoryByClass(ImageFragmentItemFactory::class.java)
            )
            Assert.assertSame(
                textItemFactory,
                getItemFactoryByClass(TextFragmentItemFactory::class.java)
            )
            assertThrow(NotFoundMatchedItemFactoryException::class) {
                getItemFactoryByClass(ViewFragmentItemFactory::class.java)
            }
        }
        AssemblyFragmentStateListAdapter<Any?>(
            fragment,
            listOf(textItemFactory, imageItemFactory, placeholderItemFactory)
        ).apply {
            Assert.assertSame(
                placeholderItemFactory,
                getItemFactoryByClass(PlaceholderFragmentItemFactory::class.java)
            )
        }
    }
}