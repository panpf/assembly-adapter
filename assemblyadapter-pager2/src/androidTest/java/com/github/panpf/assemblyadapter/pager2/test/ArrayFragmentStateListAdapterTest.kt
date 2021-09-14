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
import com.github.panpf.assemblyadapter.pager2.ArrayFragmentStateListAdapter
import com.github.panpf.tools4a.test.ktx.getFragmentSync
import com.github.panpf.tools4a.test.ktx.launchFragmentInContainer
import com.github.panpf.tools4j.test.ktx.assertThrow
import org.junit.Assert
import org.junit.Test

class ArrayFragmentStateListAdapterTest {

    class TextFragment : Fragment()

    class ImageFragment : Fragment()

    class TestFragment : Fragment()

    @Test
    fun testConstructor() {
        val fragmentScenario =
            TestFragment::class.launchFragmentInContainer()
        val fragment = fragmentScenario.getFragmentSync()

        ArrayFragmentStateListAdapter(fragment).apply {
            Assert.assertEquals(0, currentList.size)
        }
        ArrayFragmentStateListAdapter(fragment, listOf(TextFragment(), ImageFragment())).apply {
            Assert.assertEquals(2, currentList.size)
        }
        ArrayFragmentStateListAdapter(fragment, arrayOf(TextFragment(), ImageFragment())).apply {
            Assert.assertEquals(2, currentList.size)
        }

        ArrayFragmentStateListAdapter(fragment.requireActivity()).apply {
            Assert.assertEquals(0, currentList.size)
        }
        ArrayFragmentStateListAdapter(
            fragment.requireActivity(),
            listOf(TextFragment(), ImageFragment())
        ).apply {
            Assert.assertEquals(2, currentList.size)
        }
        ArrayFragmentStateListAdapter(
            fragment.requireActivity(),
            arrayOf(TextFragment(), ImageFragment())
        ).apply {
            Assert.assertEquals(2, currentList.size)
        }

        ArrayFragmentStateListAdapter(fragment.childFragmentManager, fragment.lifecycle).apply {
            Assert.assertEquals(0, currentList.size)
        }
        ArrayFragmentStateListAdapter(
            fragment.childFragmentManager,
            fragment.lifecycle,
            listOf(TextFragment(), ImageFragment())
        ).apply {
            Assert.assertEquals(2, currentList.size)
        }
        ArrayFragmentStateListAdapter(
            fragment.childFragmentManager,
            fragment.lifecycle,
            arrayOf(TextFragment(), ImageFragment())
        ).apply {
            Assert.assertEquals(2, currentList.size)
        }
    }

    @Test
    fun testPropertyCurrentListAndSubmitList() {
        val fragmentScenario = TestFragment::class.launchFragmentInContainer()
        val fragment = fragmentScenario.getFragmentSync()
        ArrayFragmentStateListAdapter(fragment).apply {
            Assert.assertEquals(0, currentList.size)

            submitList(listOf(TextFragment()))
            Thread.sleep(50)    // ListAdapter internal asynchronous thread updates data, it takes a while to take effect
            Assert.assertEquals(1, currentList.size)

            submitList(listOf(TextFragment(), ImageFragment()))
            Thread.sleep(50)    // ListAdapter internal asynchronous thread updates data, it takes a while to take effect
            Assert.assertEquals(2, currentList.size)

            submitList(null)
            Thread.sleep(50)    // ListAdapter internal asynchronous thread updates data, it takes a while to take effect
            Assert.assertEquals(0, currentList.size)
        }
    }

    @Test
    fun testMethodGetCount() {
        val fragmentScenario = TestFragment::class.launchFragmentInContainer()
        val fragment = fragmentScenario.getFragmentSync()
        ArrayFragmentStateListAdapter(fragment).apply {
            Assert.assertEquals(0, itemCount)

            submitList(listOf(TextFragment()))
            Thread.sleep(50)    // ListAdapter internal asynchronous thread updates data, it takes a while to take effect
            Assert.assertEquals(1, itemCount)

            submitList(listOf(TextFragment(), ImageFragment()))
            Thread.sleep(50)    // ListAdapter internal asynchronous thread updates data, it takes a while to take effect
            Assert.assertEquals(2, itemCount)

            submitList(null)
            Thread.sleep(50)    // ListAdapter internal asynchronous thread updates data, it takes a while to take effect
            Assert.assertEquals(0, itemCount)
        }
    }

    @Test
    fun testMethodGetItemData() {
        val fragmentScenario = TestFragment::class.launchFragmentInContainer()
        val fragment = fragmentScenario.getFragmentSync()
        ArrayFragmentStateListAdapter(fragment).apply {
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
            Thread.sleep(50)    // ListAdapter internal asynchronous thread updates data, it takes a while to take effect
            Assert.assertTrue(getItemData(0) is TextFragment)
            Assert.assertTrue(getItemData(1) is ImageFragment)
        }
    }

    @Test
    fun testMethodGetItemId() {
        val fragmentScenario = TestFragment::class.launchFragmentInContainer()
        val fragment = fragmentScenario.getFragmentSync()

        ArrayFragmentStateListAdapter(fragment).apply {
            assertThrow(UnsupportedOperationException::class) {
                setHasStableIds(true)
            }
            Assert.assertEquals(-1L, getItemId(-1))
            Assert.assertEquals(0L, getItemId(0))
            Assert.assertEquals(1L, getItemId(1))
        }

        ArrayFragmentStateListAdapter(fragment).apply {
            assertThrow(UnsupportedOperationException::class) {
                setHasStableIds(true)
            }
            Assert.assertEquals(-1L, getItemId(-1))
            Assert.assertEquals(0L, getItemId(0))
            Assert.assertEquals(1L, getItemId(1))

            submitList(listOf(TextFragment(), ImageFragment()))
            Thread.sleep(50)    // ListAdapter internal asynchronous thread updates data, it takes a while to take effect
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
        ArrayFragmentStateListAdapter(fragment).apply {
            assertThrow(IndexOutOfBoundsException::class) {
                getItemViewType(-1)
            }
            assertThrow(IndexOutOfBoundsException::class) {
                getItemViewType(0)
            }
            assertThrow(IndexOutOfBoundsException::class) {
                getItemViewType(1)
            }

            submitList(listOf(TextFragment(), ImageFragment()))
            Thread.sleep(50)    // ListAdapter internal asynchronous thread updates data, it takes a while to take effect
            Assert.assertEquals(0, getItemViewType(0))
            Assert.assertEquals(0, getItemViewType(1))
        }
    }

    @Test
    fun testMethodCreateFragment() {
        val fragmentScenario = TestFragment::class.launchFragmentInContainer()
        val fragment = fragmentScenario.getFragmentSync()
        ArrayFragmentStateListAdapter(fragment).apply {
            submitList(listOf(TextFragment(), ImageFragment()))
            Thread.sleep(50)    // ListAdapter internal asynchronous thread updates data, it takes a while to take effect

            createFragment(0).apply {
                Assert.assertTrue(this is TextFragment)
                Assert.assertNotSame(getItemData(0), this)
            }
            createFragment(1).apply {
                Assert.assertTrue(this is ImageFragment)
                Assert.assertNotSame(getItemData(1), this)
            }
        }
    }
}