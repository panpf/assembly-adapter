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
import androidx.fragment.app.Fragment
import com.github.panpf.assemblyadapter.pager.AssemblyFragmentStatePagerAdapter
import com.github.panpf.assemblyadapter.pager.FragmentItemFactory
import com.github.panpf.tools4a.test.ktx.getFragmentSync
import com.github.panpf.tools4a.test.ktx.launchFragmentInContainer
import com.github.panpf.tools4j.test.ktx.assertThrow
import org.junit.Assert
import org.junit.Test

class AssemblyFragmentStatePagerAdapterTest {

    data class Text(val text: String)

    class TextFragmentItemFactory : FragmentItemFactory<Text>(Text::class) {
        override fun createFragment(
            bindingAdapterPosition: Int,
            absoluteAdapterPosition: Int,
            data: Text
        ): Fragment = TextFragment()
    }

    class TextFragment : Fragment()

    data class Image(val resId: Int)

    class ImageFragmentItemFactory : FragmentItemFactory<Image>(Image::class) {
        override fun createFragment(
            bindingAdapterPosition: Int,
            absoluteAdapterPosition: Int,
            data: Image
        ): Fragment = ImageFragment()
    }

    class ImageFragment : Fragment()

    class TestFragment : Fragment()

    @Test
    fun testConstructor() {
        val fragmentScenario = TestFragment::class.launchFragmentInContainer()
        val fragment = fragmentScenario.getFragmentSync()

        assertThrow(IllegalArgumentException::class) {
            AssemblyFragmentStatePagerAdapter<Any>(
                fragment.childFragmentManager, listOf(),
            )
        }
        AssemblyFragmentStatePagerAdapter<Any>(
            fragment.childFragmentManager, listOf(TextFragmentItemFactory(), ImageFragmentItemFactory())
        ).apply {
            Assert.assertEquals(0, currentList.size)
        }
        AssemblyFragmentStatePagerAdapter<Any>(
            fragment.childFragmentManager,
            listOf(TextFragmentItemFactory(), ImageFragmentItemFactory()),
            listOf(Text("hello"), Image(R.drawable.btn_default))
        ).apply {
            Assert.assertEquals(2, currentList.size)
        }
    }

    @Test
    fun testPropertyCurrentListAndSubmitList() {
        val fragmentScenario = TestFragment::class.launchFragmentInContainer()
        val fragment = fragmentScenario.getFragmentSync()
        AssemblyFragmentStatePagerAdapter<Any>(
            fragment.childFragmentManager, listOf(TextFragmentItemFactory(), ImageFragmentItemFactory())
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
        val fragmentScenario = TestFragment::class.launchFragmentInContainer()
        val fragment = fragmentScenario.getFragmentSync()
        AssemblyFragmentStatePagerAdapter<Any>(
            fragment.childFragmentManager, listOf(TextFragmentItemFactory(), ImageFragmentItemFactory())
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
        val fragmentScenario = TestFragment::class.launchFragmentInContainer()
        val fragment = fragmentScenario.getFragmentSync()
        AssemblyFragmentStatePagerAdapter<Any>(
            fragment.childFragmentManager, listOf(TextFragmentItemFactory(), ImageFragmentItemFactory())
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
    fun testMethodGetItem() {
        val fragmentScenario = TestFragment::class.launchFragmentInContainer()
        val fragment = fragmentScenario.getFragmentSync()
        AssemblyFragmentStatePagerAdapter<Any>(
            fragment.childFragmentManager, listOf(TextFragmentItemFactory(), ImageFragmentItemFactory())
        ).apply {
            submitList(listOf(Text("hello"), Image(R.drawable.alert_dark_frame)))

            var item0: Fragment? = null
            fragmentScenario.onFragment {
                item0 = getItem(0)
            }
            Assert.assertTrue(item0 is TextFragment)

            var item1: Fragment? = null
            fragmentScenario.onFragment {
                item1 = getItem(1)
            }
            Assert.assertTrue(item1 is ImageFragment)
        }
    }

    @Test
    fun testMethodGetItemFactoryByPosition() {
        val fragmentScenario = TestFragment::class.launchFragmentInContainer()
        val fragment = fragmentScenario.getFragmentSync()
        AssemblyFragmentStatePagerAdapter<Any>(
            fragment.childFragmentManager, listOf(TextFragmentItemFactory(), ImageFragmentItemFactory())
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

            submitList(listOf(Image(R.drawable.alert_dark_frame), Text("hello")))
            Assert.assertEquals(ImageFragmentItemFactory::class, getItemFactoryByPosition(0)::class)
            Assert.assertEquals(TextFragmentItemFactory::class, getItemFactoryByPosition(1)::class)
        }
    }
}