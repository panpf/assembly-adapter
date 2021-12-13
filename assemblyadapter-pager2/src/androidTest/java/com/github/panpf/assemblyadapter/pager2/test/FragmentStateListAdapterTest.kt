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
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.DiffUtil
import com.github.panpf.assemblyadapter.pager.FragmentItemFactory
import com.github.panpf.assemblyadapter.pager2.FragmentStateListAdapter
import com.github.panpf.assemblyadapter.recycler.DiffKey
import com.github.panpf.assemblyadapter.recycler.KeyEqualsDiffItemCallback
import com.github.panpf.tools4a.test.ktx.getFragmentSync
import com.github.panpf.tools4a.test.ktx.launchFragmentInContainer
import com.github.panpf.tools4j.test.ktx.assertThrow
import org.junit.Assert
import org.junit.Test

class FragmentStateListAdapterTest {

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

    class TestFragment : Fragment()

    class TestFragmentStateListAdapter : FragmentStateListAdapter<Text> {

        constructor(
            fragmentManager: FragmentManager,
            lifecycle: Lifecycle,
            initDataList: List<Text>? = null,
            diffCallback: DiffUtil.ItemCallback<Text> = KeyEqualsDiffItemCallback()
        ) : super(fragmentManager, lifecycle, diffCallback) {
            submitList(initDataList)
        }

        constructor(
            fragment: Fragment,
            initDataList: List<Text>? = null,
            diffCallback: DiffUtil.ItemCallback<Text> = KeyEqualsDiffItemCallback()
        ) : super(
            fragment,
            diffCallback
        ) {
            submitList(initDataList)
        }

        constructor(
            activity: FragmentActivity,
            initDataList: List<Text>? = null,
            diffCallback: DiffUtil.ItemCallback<Text> = KeyEqualsDiffItemCallback()
        ) : super(
            activity,
            diffCallback
        ) {
            submitList(initDataList)
        }

        constructor(
            fragmentManager: FragmentManager,
            lifecycle: Lifecycle,
            initDataList: List<Text>? = null,
            config: AsyncDifferConfig<Text>
        ) : super(fragmentManager, lifecycle, config) {
            submitList(initDataList)
        }

        constructor(
            fragment: Fragment,
            initDataList: List<Text>? = null,
            config: AsyncDifferConfig<Text>
        ) : super(fragment, config) {
            submitList(initDataList)
        }

        constructor(
            activity: FragmentActivity,
            initDataList: List<Text>? = null,
            config: AsyncDifferConfig<Text>
        ) : super(
            activity,
            config
        ) {
            submitList(initDataList)
        }

        override fun createFragment(position: Int): Fragment {
            return TextFragmentItemFactory().dispatchCreateFragment(0, 1, getItem(position))
        }
    }

    @Test
    fun testConstructor() {
        val fragmentScenario = TestFragment::class.launchFragmentInContainer()
        val fragment = fragmentScenario.getFragmentSync()

        TestFragmentStateListAdapter(fragment).apply {
            Assert.assertEquals(0, currentList.size)
        }
        TestFragmentStateListAdapter(
            fragment,
            listOf(Text("hello"), Text("world"))
        ).apply {
            Assert.assertEquals(2, currentList.size)
        }

        TestFragmentStateListAdapter(
            fragment.requireActivity(),
        ).apply {
            Assert.assertEquals(0, currentList.size)
        }
        TestFragmentStateListAdapter(
            fragment.requireActivity(),
            listOf(Text("hello"), Text("world"))
        ).apply {
            Assert.assertEquals(2, currentList.size)
        }

        TestFragmentStateListAdapter(
            fragment.childFragmentManager,
            fragment.lifecycle,
        ).apply {
            Assert.assertEquals(0, currentList.size)
        }
        TestFragmentStateListAdapter(
            fragment.childFragmentManager,
            fragment.lifecycle,
            listOf(Text("hello"), Text("world"))
        ).apply {
            Assert.assertEquals(2, currentList.size)
        }

        TestFragmentStateListAdapter(
            fragment,
            null,
            AsyncDifferConfig.Builder<Text>(KeyEqualsDiffItemCallback()).build()
        ).apply {
            Assert.assertEquals(0, currentList.size)
        }
        TestFragmentStateListAdapter(
            fragment,
            listOf(Text("hello"), Text("world")),
            AsyncDifferConfig.Builder<Text>(KeyEqualsDiffItemCallback()).build()
        ).apply {
            Assert.assertEquals(2, currentList.size)
        }

        TestFragmentStateListAdapter(
            fragment.requireActivity(),
            null,
            AsyncDifferConfig.Builder<Text>(KeyEqualsDiffItemCallback()).build()
        ).apply {
            Assert.assertEquals(0, currentList.size)
        }
        TestFragmentStateListAdapter(
            fragment.requireActivity(),
            listOf(Text("hello"), Text("world")),
            AsyncDifferConfig.Builder<Text>(KeyEqualsDiffItemCallback()).build()
        ).apply {
            Assert.assertEquals(2, currentList.size)
        }

        TestFragmentStateListAdapter(
            fragment.childFragmentManager,
            fragment.lifecycle,
            null,
            AsyncDifferConfig.Builder<Text>(KeyEqualsDiffItemCallback()).build()
        ).apply {
            Assert.assertEquals(0, currentList.size)
        }
        TestFragmentStateListAdapter(
            fragment.childFragmentManager,
            fragment.lifecycle,
            listOf(Text("hello"), Text("world")),
            AsyncDifferConfig.Builder<Text>(KeyEqualsDiffItemCallback()).build()
        ).apply {
            Assert.assertEquals(2, currentList.size)
        }
    }

    @Test
    fun testPropertyCurrentListAndSubmitList() {
        val fragmentScenario = TestFragment::class.launchFragmentInContainer()
        val fragment = fragmentScenario.getFragmentSync()
        TestFragmentStateListAdapter(fragment).apply {
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
        TestFragmentStateListAdapter(fragment).apply {
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
    fun testMethodGetItemId() {
        val fragmentScenario = TestFragment::class.launchFragmentInContainer()
        val fragment = fragmentScenario.getFragmentSync()

        TestFragmentStateListAdapter(fragment).apply {
            assertThrow(UnsupportedOperationException::class) {
                setHasStableIds(true)
            }
            Assert.assertEquals(-1L, getItemId(-1))
            Assert.assertEquals(0L, getItemId(0))
            Assert.assertEquals(1L, getItemId(1))
        }

        TestFragmentStateListAdapter(fragment).apply {
            assertThrow(UnsupportedOperationException::class) {
                setHasStableIds(true)
            }
            Assert.assertEquals(-1L, getItemId(-1))
            Assert.assertEquals(0L, getItemId(0))
            Assert.assertEquals(1L, getItemId(1))

            submitList(
                listOf(
                    Text("hello"),
                    Text("world")
                )
            )
            Thread.sleep(100)    // ListAdapter internal asynchronous thread updates data, it takes a while to take effect
            Assert.assertEquals(-1L, getItemId(-1))
            Assert.assertEquals(0L, getItemId(0))
            Assert.assertEquals(1L, getItemId(1))
            Assert.assertEquals(2L, getItemId(2))
        }
    }

    @Test
    fun testMethodCreateFragment() {
        val fragmentScenario = TestFragment::class.launchFragmentInContainer()
        val fragment = fragmentScenario.getFragmentSync()
        TestFragmentStateListAdapter(fragment).apply {
            submitList(listOf(Text("hello"), Text("world")))
            Thread.sleep(100)    // ListAdapter internal asynchronous thread updates data, it takes a while to take effect

            Assert.assertTrue(createFragment(0) is TextFragment)
            Assert.assertTrue(createFragment(1) is TextFragment)
        }
    }
}