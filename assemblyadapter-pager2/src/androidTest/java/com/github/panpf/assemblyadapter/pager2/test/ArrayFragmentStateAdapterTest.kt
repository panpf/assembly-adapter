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
import com.github.panpf.assemblyadapter.pager2.ArrayFragmentStateAdapter
import com.github.panpf.tools4a.test.ktx.getFragmentSync
import com.github.panpf.tools4a.test.ktx.launchFragmentInContainer
import com.github.panpf.tools4j.test.ktx.assertThrow
import org.junit.Assert
import org.junit.Test

class ArrayFragmentStateAdapterTest {

    class TextFragment : Fragment()

    class ImageFragment : Fragment()

    class TestFragment : Fragment()

    @Test
    fun testConstructor() {
        val fragmentScenario =
            TestFragment::class.launchFragmentInContainer()
        val fragment = fragmentScenario.getFragmentSync()

        ArrayFragmentStateAdapter(fragment, listOf()).apply {
            Assert.assertEquals(0, currentList.size)
        }
        ArrayFragmentStateAdapter(fragment, listOf(TextFragment(), ImageFragment())).apply {
            Assert.assertEquals(2, currentList.size)
        }

        ArrayFragmentStateAdapter(fragment.requireActivity(), listOf()).apply {
            Assert.assertEquals(0, currentList.size)
        }
        ArrayFragmentStateAdapter(
            fragment.requireActivity(),
            listOf(TextFragment(), ImageFragment())
        ).apply {
            Assert.assertEquals(2, currentList.size)
        }

        ArrayFragmentStateAdapter(
            fragment.childFragmentManager,
            fragment.lifecycle,
            listOf()
        ).apply {
            Assert.assertEquals(0, currentList.size)
        }
        ArrayFragmentStateAdapter(
            fragment.childFragmentManager,
            fragment.lifecycle,
            listOf(TextFragment(), ImageFragment())
        ).apply {
            Assert.assertEquals(2, currentList.size)
        }
    }

    @Test
    fun testPropertyCurrentListAndSubmitList() {
        val fragmentScenario = TestFragment::class.launchFragmentInContainer()
        val fragment = fragmentScenario.getFragmentSync()
        ArrayFragmentStateAdapter(fragment, listOf()).apply {
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
        ArrayFragmentStateAdapter(fragment, listOf()).apply {
            Assert.assertEquals(0, itemCount)

            submitList(listOf(TextFragment()))
            Assert.assertEquals(1, itemCount)

            submitList(listOf(TextFragment(), ImageFragment()))
            Assert.assertEquals(2, itemCount)

            submitList(null)
            Assert.assertEquals(0, itemCount)
        }
    }

    @Test
    fun testMethodGetItem() {
        val fragmentScenario = TestFragment::class.launchFragmentInContainer()
        val fragment = fragmentScenario.getFragmentSync()
        ArrayFragmentStateAdapter(fragment, listOf()).apply {
            assertThrow(IndexOutOfBoundsException::class) {
                getItem(-1)
            }
            assertThrow(IndexOutOfBoundsException::class) {
                getItem(0)
            }
            assertThrow(IndexOutOfBoundsException::class) {
                getItem(1)
            }

            submitList(listOf(TextFragment(), ImageFragment()))
            Assert.assertTrue(getItem(0) is TextFragment)
            Assert.assertTrue(getItem(1) is ImageFragment)
        }
    }

    @Test
    fun testMethodGetItemId() {
        val fragmentScenario = TestFragment::class.launchFragmentInContainer()
        val fragment = fragmentScenario.getFragmentSync()

        ArrayFragmentStateAdapter(fragment, listOf()).apply {
            assertThrow(UnsupportedOperationException::class) {
                setHasStableIds(true)
            }
            Assert.assertEquals(-1L, getItemId(-1))
            Assert.assertEquals(0L, getItemId(0))
            Assert.assertEquals(1L, getItemId(1))
        }

        ArrayFragmentStateAdapter(fragment, listOf()).apply {
            assertThrow(UnsupportedOperationException::class) {
                setHasStableIds(true)
            }
            Assert.assertEquals(-1L, getItemId(-1))
            Assert.assertEquals(0L, getItemId(0))
            Assert.assertEquals(1L, getItemId(1))

            submitList(listOf(TextFragment(), ImageFragment()))
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
        ArrayFragmentStateAdapter(fragment, listOf()).apply {
            getItemViewType(0)
            getItemViewType(0)
            getItemViewType(0)

            submitList(listOf(TextFragment(), ImageFragment()))
            Assert.assertEquals(0, getItemViewType(0))
            Assert.assertEquals(0, getItemViewType(1))
        }
    }

    @Test
    fun testMethodCreateFragment() {
        val fragmentScenario = TestFragment::class.launchFragmentInContainer()
        val fragment = fragmentScenario.getFragmentSync()
        ArrayFragmentStateAdapter(fragment, listOf()).apply {
            submitList(listOf(TextFragment(), ImageFragment()))

            Assert.assertTrue(createFragment(0) is TextFragment)
            Assert.assertTrue(createFragment(1) is ImageFragment)
        }
    }
}