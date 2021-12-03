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
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.paging.LoadState
import com.github.panpf.assemblyadapter.pager2.paging.LoadStateFragmentStateAdapter
import com.github.panpf.tools4a.test.ktx.getFragmentSync
import com.github.panpf.tools4a.test.ktx.launchFragmentInContainer
import org.junit.Assert
import org.junit.Test

class LoadStateFragmentStateAdapterTest {

    class LoadStateFragment : Fragment()

    class TestFragment : Fragment()

    class TestLoadStateFragmentStateAdapter : LoadStateFragmentStateAdapter {
        constructor(fragmentManager: FragmentManager, lifecycle: Lifecycle) : super(
            fragmentManager,
            lifecycle
        )

        constructor(fragmentActivity: FragmentActivity) : super(fragmentActivity)
        constructor(fragment: Fragment) : super(fragment)

        override fun onCreateFragment(position: Int, loadState: LoadState): Fragment {
            return LoadStateFragment()
        }
    }


    @Test
    fun testConstructor() {
        val fragmentScenario = TestFragment::class.launchFragmentInContainer()
        val fragment = fragmentScenario.getFragmentSync()
        TestLoadStateFragmentStateAdapter(fragment)
        TestLoadStateFragmentStateAdapter(fragment.requireActivity())
        TestLoadStateFragmentStateAdapter(fragment.childFragmentManager, fragment.lifecycle)
    }

    @Test
    fun testMethodGetItemViewType() {
        val fragmentScenario = TestFragment::class.launchFragmentInContainer()
        val fragment = fragmentScenario.getFragmentSync()
        TestLoadStateFragmentStateAdapter(fragment).apply {
            Assert.assertEquals(0, getItemViewType(-1))
            Assert.assertEquals(0, getItemViewType(0))
            Assert.assertEquals(0, getItemViewType(1))

            loadState = LoadState.Loading
            Assert.assertEquals(0, getItemViewType(-1))
            Assert.assertEquals(0, getItemViewType(0))
            Assert.assertEquals(0, getItemViewType(1))
        }
    }

    @Test
    fun testMethodCreateFragment() {
        val fragmentScenario = TestFragment::class.launchFragmentInContainer()
        val fragment = fragmentScenario.getFragmentSync()
        TestLoadStateFragmentStateAdapter(fragment).apply {
            loadState = LoadState.Loading

            Assert.assertTrue(createFragment(-1) is LoadStateFragment)
            Assert.assertTrue(createFragment(0) is LoadStateFragment)
            Assert.assertTrue(createFragment(1) is LoadStateFragment)
        }
    }

    @Test
    fun testDisplayLoadStateAsItem() {
        val fragmentScenario = TestFragment::class.launchFragmentInContainer()
        val fragment = fragmentScenario.getFragmentSync()

        TestLoadStateFragmentStateAdapter(fragment).apply {
            Assert.assertTrue(displayLoadStateAsItem(LoadState.Loading))
            Assert.assertTrue(displayLoadStateAsItem(LoadState.Error(Exception())))
            Assert.assertFalse(displayLoadStateAsItem(LoadState.NotLoading(false)))
            Assert.assertFalse(displayLoadStateAsItem(LoadState.NotLoading(true)))
        }
    }
}