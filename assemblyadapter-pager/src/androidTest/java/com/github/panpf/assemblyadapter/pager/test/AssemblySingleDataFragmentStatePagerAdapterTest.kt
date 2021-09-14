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

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.PagerAdapter
import com.github.panpf.assemblyadapter.NotFoundMatchedItemFactoryException
import com.github.panpf.assemblyadapter.pager.AssemblySingleDataFragmentStatePagerAdapter
import com.github.panpf.assemblyadapter.pager.FragmentItemFactory
import com.github.panpf.assemblyadapter.pager.GetPageTitle
import com.github.panpf.assemblyadapter.pager.ViewFragmentItemFactory
import com.github.panpf.tools4a.test.ktx.getFragmentSync
import com.github.panpf.tools4a.test.ktx.launchFragmentInContainer
import com.github.panpf.tools4j.test.ktx.assertThrow
import org.junit.Assert
import org.junit.Test

class AssemblySingleDataFragmentStatePagerAdapterTest {

    data class Text(val text: String) : GetPageTitle {
        override val pageTitle: CharSequence = "PageTitle-Text-$text"
    }

    class TextFragmentItemFactory : FragmentItemFactory<Text>(Text::class) {
        override fun createFragment(
            bindingAdapterPosition: Int,
            absoluteAdapterPosition: Int,
            data: Text
        ): Fragment = TextFragment()
    }

    class TextFragment : Fragment() {
        override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View = TextView(requireContext())
    }

    class TestFragment : Fragment()

    @Test
    fun testConstructor() {
        val fragmentScenario = TestFragment::class.launchFragmentInContainer()
        val fragment = fragmentScenario.getFragmentSync()
        AssemblySingleDataFragmentStatePagerAdapter(
            fragment.childFragmentManager,
            TextFragmentItemFactory()
        ).apply {
            Assert.assertNull(data)
        }

        AssemblySingleDataFragmentStatePagerAdapter(
            fragment.childFragmentManager,
            TextFragmentItemFactory(),
            Text("hello")
        ).apply {
            Assert.assertNotNull(data)
            Assert.assertEquals(Text("hello"), data)
        }
    }

    @Test
    fun testPropertyData() {
        val fragmentScenario = TestFragment::class.launchFragmentInContainer()
        val fragment = fragmentScenario.getFragmentSync()
        AssemblySingleDataFragmentStatePagerAdapter(
            fragment.childFragmentManager,
            TextFragmentItemFactory()
        ).apply {
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
        val fragmentScenario = TestFragment::class.launchFragmentInContainer()
        val fragment = fragmentScenario.getFragmentSync()
        AssemblySingleDataFragmentStatePagerAdapter(
            fragment.childFragmentManager,
            TextFragmentItemFactory()
        ).apply {
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
        val fragmentScenario = TestFragment::class.launchFragmentInContainer()
        val fragment = fragmentScenario.getFragmentSync()
        AssemblySingleDataFragmentStatePagerAdapter(
            fragment.childFragmentManager,
            TextFragmentItemFactory()
        ).apply {
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
        val fragmentScenario = TestFragment::class.launchFragmentInContainer()
        val fragment = fragmentScenario.getFragmentSync()
        AssemblySingleDataFragmentStatePagerAdapter(
            fragment.childFragmentManager,
            TextFragmentItemFactory()
        ).apply {
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
            fragmentScenario.onFragment {
                getItem(0)
            }
        }
    }

    @Test
    fun testMethodGetItemPosition() {
        val fragmentScenario = TestFragment::class.launchFragmentInContainer()
        val fragment = fragmentScenario.getFragmentSync()
        AssemblySingleDataFragmentStatePagerAdapter(
            fragment.childFragmentManager,
            TextFragmentItemFactory(),
            Text("hello")
        ).apply {
            var item: Fragment? = null
            fragmentScenario.onFragment {
                item = getItem(0)
            }
            Assert.assertEquals(PagerAdapter.POSITION_NONE, getItemPosition(item!!))

            fragmentScenario.onFragment {
                fragment.childFragmentManager.beginTransaction()
                    .add(item!!, null)
                    .commit()
            }
            Thread.sleep(100)

            Assert.assertEquals(PagerAdapter.POSITION_UNCHANGED, getItemPosition(item!!))

            data = Text("world")
            Assert.assertEquals(PagerAdapter.POSITION_NONE, getItemPosition(item!!))

            data = null
            Assert.assertEquals(PagerAdapter.POSITION_NONE, getItemPosition(item!!))
        }
    }

    @Test
    fun testMethodGetItemFactoryByPosition() {
        val fragmentScenario = TestFragment::class.launchFragmentInContainer()
        val fragment = fragmentScenario.getFragmentSync()
        val itemFactory = TextFragmentItemFactory()
        AssemblySingleDataFragmentStatePagerAdapter(
            fragment.childFragmentManager,
            itemFactory
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

            data = Text("hello")
            Assert.assertSame(itemFactory, getItemFactoryByPosition(0))
        }
    }

    @Test
    fun testMethodGetItemFactoryByData() {
        val fragmentScenario = TestFragment::class.launchFragmentInContainer()
        val fragment = fragmentScenario.getFragmentSync()
        val textItemFactory = TextFragmentItemFactory()

        AssemblySingleDataFragmentStatePagerAdapter(
            fragment.childFragmentManager,
            textItemFactory
        ).apply {
            Assert.assertSame(textItemFactory, getItemFactoryByData(Text("hello")))
        }
    }

    @Test
    fun testMethodGetItemFactoryByClass() {
        val fragmentScenario = TestFragment::class.launchFragmentInContainer()
        val fragment = fragmentScenario.getFragmentSync()
        val textItemFactory = TextFragmentItemFactory()

        AssemblySingleDataFragmentStatePagerAdapter(
            fragment.childFragmentManager,
            textItemFactory
        ).apply {
            Assert.assertSame(
                textItemFactory,
                getItemFactoryByClass(TextFragmentItemFactory::class.java)
            )
            assertThrow(NotFoundMatchedItemFactoryException::class) {
                getItemFactoryByClass(ViewFragmentItemFactory::class.java)
            }
        }
    }

    @Test
    fun testMethodGetPageTitle() {
        val fragmentScenario = TestFragment::class.launchFragmentInContainer()
        val fragment = fragmentScenario.getFragmentSync()
        AssemblySingleDataFragmentStatePagerAdapter(
            fragment.childFragmentManager,
            TextFragmentItemFactory()
        ).apply {
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