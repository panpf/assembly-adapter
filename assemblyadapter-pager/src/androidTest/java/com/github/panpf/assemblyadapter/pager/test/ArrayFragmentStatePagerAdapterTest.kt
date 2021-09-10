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

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentStatePagerAdapter
import com.github.panpf.assemblyadapter.pager.ArrayFragmentStatePagerAdapter
import com.github.panpf.tools4a.test.ktx.getFragmentSync
import com.github.panpf.tools4a.test.ktx.launchFragmentInContainer
import com.github.panpf.tools4j.test.ktx.assertThrow
import org.junit.Assert
import org.junit.Test

class ArrayFragmentStatePagerAdapterTest {

    class TextFragment : Fragment()

    class ImageFragment : Fragment()

    class TestFragment : Fragment()

    @Test
    fun testConstructor() {
        val fragmentScenario =
            TestFragment::class.launchFragmentInContainer()
        val fragment = fragmentScenario.getFragmentSync()

        ArrayFragmentStatePagerAdapter(fragment.childFragmentManager).apply {
            Assert.assertEquals(0, currentList.size)
        }
        ArrayFragmentStatePagerAdapter(
            fragment.childFragmentManager,
            listOf(TextFragment(), ImageFragment())
        ).apply {
            Assert.assertEquals(2, currentList.size)
        }
        ArrayFragmentStatePagerAdapter(
            fragment.childFragmentManager,
            arrayOf(TextFragment(), ImageFragment())
        ).apply {
            Assert.assertEquals(2, currentList.size)
        }

        ArrayFragmentStatePagerAdapter(
            fragment.childFragmentManager,
            FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT,
            listOf()
        ).apply {
            Assert.assertEquals(0, currentList.size)
        }
        ArrayFragmentStatePagerAdapter(
            fragment.childFragmentManager,
            FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT,
            listOf(TextFragment(), ImageFragment())
        ).apply {
            Assert.assertEquals(2, currentList.size)
        }
        ArrayFragmentStatePagerAdapter(
            fragment.childFragmentManager,
            FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT,
            arrayOf(TextFragment(), ImageFragment())
        ).apply {
            Assert.assertEquals(2, currentList.size)
        }
    }

    @Test
    fun testPropertyCurrentListAndSubmitList() {
        val fragmentScenario = TestFragment::class.launchFragmentInContainer()
        val fragment = fragmentScenario.getFragmentSync()
        ArrayFragmentStatePagerAdapter(fragment.childFragmentManager).apply {
            Assert.assertEquals(0, currentList.size)

            submitList(listOf(TextFragment()))
            Assert.assertEquals(1, currentList.size)

            submitList(listOf(TextFragment(), ImageFragment()))
            Assert.assertEquals(2, currentList.size)

            submitList(null)
            Assert.assertEquals(0, currentList.size)
        }
    }

    @Test
    fun testMethodGetCount() {
        val fragmentScenario = TestFragment::class.launchFragmentInContainer()
        val fragment = fragmentScenario.getFragmentSync()
        ArrayFragmentStatePagerAdapter(fragment.childFragmentManager).apply {
            Assert.assertEquals(0, count)
            Assert.assertEquals(0, itemCount)

            submitList(listOf(TextFragment()))
            Assert.assertEquals(1, count)
            Assert.assertEquals(1, itemCount)

            submitList(listOf(TextFragment(), ImageFragment()))
            Assert.assertEquals(2, count)
            Assert.assertEquals(2, itemCount)

            submitList(null)
            Assert.assertEquals(0, count)
            Assert.assertEquals(0, itemCount)
        }
    }

    @Test
    fun testMethodGetItemData() {
        val fragmentScenario = TestFragment::class.launchFragmentInContainer()
        val fragment = fragmentScenario.getFragmentSync()
        ArrayFragmentStatePagerAdapter(fragment.childFragmentManager).apply {
            assertThrow(IndexOutOfBoundsException::class) {
                getItemData(-1)
            }
            assertThrow(IndexOutOfBoundsException::class) {
                getItemData(0)
            }
            assertThrow(IndexOutOfBoundsException::class) {
                getItemData(1)
            }

            submitList(listOf(TextFragment(), ImageFragment()))
            Assert.assertTrue(getItemData(0) is TextFragment)
            Assert.assertTrue(getItemData(1) is ImageFragment)
        }
    }

    @Test
    fun testMethodGetItem() {
        val fragmentScenario = TestFragment::class.launchFragmentInContainer()
        val fragment = fragmentScenario.getFragmentSync()
        ArrayFragmentStatePagerAdapter(fragment.childFragmentManager).apply {
            submitList(listOf(TextFragment(), ImageFragment()))

            var item0: Fragment? = null
            fragmentScenario.onFragment {
                item0 = getItem(0)
            }
            item0!!.apply {
                Assert.assertTrue(this is TextFragment)
                Assert.assertNotSame(getItemData(0), this)
            }

            var item1: Fragment? = null
            fragmentScenario.onFragment {
                item1 = getItem(1)
            }
            item1!!.apply {
                Assert.assertTrue(this is ImageFragment)
                Assert.assertNotSame(getItemData(1), this)
            }
        }
    }

    @Test
    fun testMethodGetPageTitle() {
        val fragmentScenario = TestFragment::class.launchFragmentInContainer()
        val fragment = fragmentScenario.getFragmentSync()
        ArrayFragmentStatePagerAdapter(fragment.childFragmentManager).apply {
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
        }
    }
}