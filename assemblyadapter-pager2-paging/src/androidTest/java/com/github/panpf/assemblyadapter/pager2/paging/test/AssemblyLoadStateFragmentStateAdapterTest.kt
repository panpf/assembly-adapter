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
package com.github.panpf.assemblyadapter.pager2.paging.test

import androidx.fragment.app.Fragment
import androidx.paging.LoadState
import com.github.panpf.assemblyadapter.pager.FragmentItemFactory
import com.github.panpf.assemblyadapter.pager2.paging.AssemblyLoadStateFragmentStateAdapter
import com.github.panpf.tools4a.test.ktx.getFragmentSync
import com.github.panpf.tools4a.test.ktx.launchFragmentInContainer
import com.github.panpf.tools4j.test.ktx.assertThrow
import org.junit.Assert
import org.junit.Test

class AssemblyLoadStateFragmentStateAdapterTest {

    class LoadStateItemFactory : FragmentItemFactory<LoadState>(LoadState::class) {
        override fun createFragment(
            bindingAdapterPosition: Int,
            absoluteAdapterPosition: Int,
            data: LoadState
        ): Fragment = LoadStateFragment()
    }

    class LoadStateFragment : Fragment()

    class TestFragment : Fragment()

    @Test
    fun testConstructor() {
        val fragmentScenario = TestFragment::class.launchFragmentInContainer()
        val fragment = fragmentScenario.getFragmentSync()
        AssemblyLoadStateFragmentStateAdapter(fragment, LoadStateItemFactory())
        AssemblyLoadStateFragmentStateAdapter(fragment.requireActivity(), LoadStateItemFactory())
        AssemblyLoadStateFragmentStateAdapter(
            fragment.childFragmentManager,
            fragment.lifecycle,
            LoadStateItemFactory()
        )

        AssemblyLoadStateFragmentStateAdapter(fragment, LoadStateItemFactory(), true)
        AssemblyLoadStateFragmentStateAdapter(
            fragment.requireActivity(),
            LoadStateItemFactory(),
            true
        )
        AssemblyLoadStateFragmentStateAdapter(
            fragment.childFragmentManager,
            fragment.lifecycle,
            LoadStateItemFactory(),
            true
        )
    }

    @Test
    fun testMethodGetItemData() {
        val fragmentScenario = TestFragment::class.launchFragmentInContainer()
        val fragment = fragmentScenario.getFragmentSync()
        AssemblyLoadStateFragmentStateAdapter(fragment, LoadStateItemFactory()).apply {
            assertThrow(IndexOutOfBoundsException::class) {
                getItemData(-1)
            }
            assertThrow(IndexOutOfBoundsException::class) {
                getItemData(0)
            }
            assertThrow(IndexOutOfBoundsException::class) {
                getItemData(1)
            }

            loadState = LoadState.Loading
            Assert.assertEquals(LoadState.Loading, getItemData(0))
        }
    }

    @Test
    fun testMethodCreateFragment() {
        val fragmentScenario = TestFragment::class.launchFragmentInContainer()
        val fragment = fragmentScenario.getFragmentSync()
        AssemblyLoadStateFragmentStateAdapter(fragment, LoadStateItemFactory()).apply {
            loadState = LoadState.Loading

            assertThrow(IndexOutOfBoundsException::class) {
                createFragment(-1)
            }
            Assert.assertTrue(createFragment(0) is LoadStateFragment)
            assertThrow(IndexOutOfBoundsException::class) {
                createFragment(1)
            }
        }
    }

    @Test
    fun testMethodGetItemFactoryByPosition() {
        val fragmentScenario = TestFragment::class.launchFragmentInContainer()
        val fragment = fragmentScenario.getFragmentSync()
        val itemFactory = LoadStateItemFactory()
        AssemblyLoadStateFragmentStateAdapter(fragment, itemFactory).apply {
            assertThrow(IndexOutOfBoundsException::class) {
                getItemFactoryByPosition(-1)
            }
            assertThrow(IndexOutOfBoundsException::class) {
                getItemFactoryByPosition(0)
            }
            assertThrow(IndexOutOfBoundsException::class) {
                getItemFactoryByPosition(1)
            }

            loadState = LoadState.Loading
            Assert.assertSame(itemFactory, getItemFactoryByPosition(0))
        }
    }

    @Test
    fun testDisplayLoadStateAsItem() {
        val fragmentScenario = TestFragment::class.launchFragmentInContainer()
        val fragment = fragmentScenario.getFragmentSync()

        AssemblyLoadStateFragmentStateAdapter(fragment, LoadStateItemFactory()).apply {
            Assert.assertTrue(displayLoadStateAsItem(LoadState.Loading))
            Assert.assertTrue(displayLoadStateAsItem(LoadState.Error(Exception())))
            Assert.assertFalse(displayLoadStateAsItem(LoadState.NotLoading(false)))
            Assert.assertFalse(displayLoadStateAsItem(LoadState.NotLoading(true)))
        }

        AssemblyLoadStateFragmentStateAdapter(
            fragment,
            itemFactory = LoadStateItemFactory(),
            alwaysShowWhenEndOfPaginationReached = true
        ).apply {
            Assert.assertTrue(displayLoadStateAsItem(LoadState.Loading))
            Assert.assertTrue(displayLoadStateAsItem(LoadState.Error(Exception())))
            Assert.assertFalse(displayLoadStateAsItem(LoadState.NotLoading(false)))
            Assert.assertTrue(displayLoadStateAsItem(LoadState.NotLoading(true)))
        }
    }
}