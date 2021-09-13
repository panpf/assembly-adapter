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
import com.github.panpf.assemblyadapter.NotFoundMatchedItemFactoryException
import com.github.panpf.assemblyadapter.pager.FragmentItemFactory
import com.github.panpf.assemblyadapter.pager.ViewFragmentItemFactory
import com.github.panpf.assemblyadapter.pager2.AssemblySingleDataFragmentStateAdapter
import com.github.panpf.assemblyadapter.recycler.SimpleAdapterDataObserver
import com.github.panpf.tools4a.test.ktx.getFragmentSync
import com.github.panpf.tools4a.test.ktx.launchFragmentInContainer
import com.github.panpf.tools4j.test.ktx.assertThrow
import org.junit.Assert
import org.junit.Test

class AssemblySingleDataFragmentStateAdapterTest {

    data class Text(val text: String)

    class TextFragmentItemFactory : FragmentItemFactory<Text>(Text::class) {
        override fun createFragment(
            bindingAdapterPosition: Int,
            absoluteAdapterPosition: Int,
            data: Text
        ): Fragment = TextFragment()
    }

    class TextFragment : Fragment()

    class TestFragment : Fragment()

    @Test
    fun testConstructor() {
        val fragmentScenario = TestFragment::class.launchFragmentInContainer()
        val fragment = fragmentScenario.getFragmentSync()
        AssemblySingleDataFragmentStateAdapter(
            fragment,
            TextFragmentItemFactory()
        ).apply {
            Assert.assertNull(data)
        }

        AssemblySingleDataFragmentStateAdapter(
            fragment,
            TextFragmentItemFactory(),
            Text("hello")
        ).apply {
            Assert.assertNotNull(data)
            Assert.assertEquals(Text("hello"), data)
        }

        AssemblySingleDataFragmentStateAdapter(
            fragment.requireActivity(),
            TextFragmentItemFactory()
        ).apply {
            Assert.assertNull(data)
        }

        AssemblySingleDataFragmentStateAdapter(
            fragment.requireActivity(),
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
        var dataFromObserver: Text? = null
        AssemblySingleDataFragmentStateAdapter(fragment, TextFragmentItemFactory()).apply {
            registerAdapterDataObserver(SimpleAdapterDataObserver {
                dataFromObserver = data
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

    @Test
    fun testMethodGetItemCount() {
        val fragmentScenario = TestFragment::class.launchFragmentInContainer()
        val fragment = fragmentScenario.getFragmentSync()
        AssemblySingleDataFragmentStateAdapter(fragment, TextFragmentItemFactory()).apply {
            Assert.assertEquals(0, itemCount)

            data = Text("hello")
            Assert.assertEquals(1, itemCount)

            data = null
            Assert.assertEquals(0, itemCount)
        }
    }

    @Test
    fun testMethodGetItemData() {
        val fragmentScenario = TestFragment::class.launchFragmentInContainer()
        val fragment = fragmentScenario.getFragmentSync()
        AssemblySingleDataFragmentStateAdapter(fragment, TextFragmentItemFactory()).apply {
            assertThrow(IndexOutOfBoundsException::class) {
                getItemData(-1)
            }
            assertThrow(IndexOutOfBoundsException::class) {
                getItemData(0)
            }
            assertThrow(IndexOutOfBoundsException::class) {
                getItemData(1)
            }

            data = Text("hello")
            Assert.assertEquals(Text("hello"), getItemData(0))
        }
    }

    @Test
    fun testMethodCreateFragment() {
        val fragmentScenario = TestFragment::class.launchFragmentInContainer()
        val fragment = fragmentScenario.getFragmentSync()
        AssemblySingleDataFragmentStateAdapter(fragment, TextFragmentItemFactory()).apply {
            assertThrow(IndexOutOfBoundsException::class) {
                createFragment(-1)
            }
            assertThrow(IndexOutOfBoundsException::class) {
                createFragment(0)
            }
            assertThrow(IndexOutOfBoundsException::class) {
                createFragment(1)
            }

            data = Text("hello")
            createFragment(0)
        }
    }

    @Test
    fun testMethodGetItemFactoryByPosition() {
        val fragmentScenario = TestFragment::class.launchFragmentInContainer()
        val fragment = fragmentScenario.getFragmentSync()
        val itemFactory = TextFragmentItemFactory()
        AssemblySingleDataFragmentStateAdapter(fragment, itemFactory).apply {
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

        AssemblySingleDataFragmentStateAdapter(fragment, textItemFactory).apply {
            Assert.assertSame(textItemFactory, getItemFactoryByData(Text("hello")))
        }
    }

    @Test
    fun testMethodGetItemFactoryByItemFactoryClass() {
        val fragmentScenario = TestFragment::class.launchFragmentInContainer()
        val fragment = fragmentScenario.getFragmentSync()
        val textItemFactory = TextFragmentItemFactory()

        AssemblySingleDataFragmentStateAdapter(fragment, textItemFactory).apply {
            Assert.assertSame(
                textItemFactory,
                getItemFactoryByItemFactoryClass(TextFragmentItemFactory::class)
            )
            assertThrow(NotFoundMatchedItemFactoryException::class) {
                getItemFactoryByItemFactoryClass(ViewFragmentItemFactory::class)
            }

            Assert.assertSame(
                textItemFactory,
                getItemFactoryByItemFactoryClass(TextFragmentItemFactory::class.java)
            )
            assertThrow(NotFoundMatchedItemFactoryException::class) {
                getItemFactoryByItemFactoryClass(ViewFragmentItemFactory::class.java)
            }
        }
    }
}