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

import android.database.DataSetObserver
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.PagerAdapter
import com.github.panpf.assemblyadapter.pager.AssemblySingleDataFragmentStatePagerAdapter
import com.github.panpf.assemblyadapter.pager.FragmentItemFactory
import com.github.panpf.assemblyadapter.pager.GetPageTitle
import com.github.panpf.tools4a.test.ktx.launchFragmentInContainerWithOn
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
        TestFragment::class.launchFragmentInContainerWithOn { fragment ->
            val fragmentManager = fragment.childFragmentManager

            AssemblySingleDataFragmentStatePagerAdapter(
                fragmentManager,
                TextFragmentItemFactory()
            ).apply {
                Assert.assertNull(data)
            }

            AssemblySingleDataFragmentStatePagerAdapter(
                fragmentManager,
                TextFragmentItemFactory(),
                Text("hello")
            ).apply {
                Assert.assertNotNull(data)
                Assert.assertEquals(Text("hello"), data)
            }
        }
    }

    @Test
    fun testPropertyData() {
        TestFragment::class.launchFragmentInContainerWithOn { fragment ->
            val fragmentManager = fragment.childFragmentManager

            var dataFromObserver: Text? = null
            AssemblySingleDataFragmentStatePagerAdapter(
                fragmentManager,
                TextFragmentItemFactory()
            ).apply {
                registerDataSetObserver(object : DataSetObserver() {
                    override fun onChanged() {
                        super.onChanged()
                        dataFromObserver = data
                    }
                })

                Assert.assertNull(data)
                Assert.assertNull(dataFromObserver)

                data = Text("hello")
                Assert.assertEquals(Text("hello"), data)
                Assert.assertEquals(Text("hello"), dataFromObserver)

                data = Text("world")
                Assert.assertEquals(Text("world"), data)
                Assert.assertEquals(Text("world"), dataFromObserver)
            }
        }
    }

    @Test
    fun testMethodGetCount() {
        TestFragment::class.launchFragmentInContainerWithOn { fragment ->
            val fragmentManager = fragment.childFragmentManager

            AssemblySingleDataFragmentStatePagerAdapter(
                fragmentManager,
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
    }

    @Test
    fun testMethodGetItem() {
        TestFragment::class.launchFragmentInContainerWithOn { fragment ->
            val fragmentManager = fragment.childFragmentManager

            AssemblySingleDataFragmentStatePagerAdapter(
                fragmentManager,
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
                getItem(0)
            }
        }
    }

    @Test
    fun testMethodGetItemPosition() {
        var item: Fragment? = null
        var adapter: AssemblySingleDataFragmentStatePagerAdapter<Text>? = null

        TestFragment::class.launchFragmentInContainerWithOn { fragment ->
            val fragmentManager = fragment.childFragmentManager

            val newAdapter = AssemblySingleDataFragmentStatePagerAdapter(
                fragmentManager,
                TextFragmentItemFactory(),
                Text("hello")
            )
            adapter = newAdapter

            val newItem = newAdapter.getItem(0)
            item = newItem
            Assert.assertEquals(PagerAdapter.POSITION_NONE, newAdapter.getItemPosition(newItem))

            fragmentManager.beginTransaction()
                .add(newItem, null)
                .commit()
        }
        Thread.sleep(100)

        Assert.assertEquals(PagerAdapter.POSITION_UNCHANGED, adapter!!.getItemPosition(item!!))

        adapter!!.data = Text("world")
        Assert.assertEquals(PagerAdapter.POSITION_NONE, adapter!!.getItemPosition(item!!))

        adapter!!.data = null
        Assert.assertEquals(PagerAdapter.POSITION_NONE, adapter!!.getItemPosition(item!!))
    }

    @Test
    fun testMethodGetItemFactoryByPosition() {
        TestFragment::class.launchFragmentInContainerWithOn { fragment ->
            val fragmentManager = fragment.childFragmentManager

            val itemFactory = TextFragmentItemFactory()
            AssemblySingleDataFragmentStatePagerAdapter(fragmentManager, itemFactory).apply {
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
    }

    @Test
    fun testMethodGetPageTitle() {
        TestFragment::class.launchFragmentInContainerWithOn { fragment ->
            val fragmentManager = fragment.childFragmentManager
            AssemblySingleDataFragmentStatePagerAdapter(
                fragmentManager,
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
}