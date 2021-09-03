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
import com.github.panpf.assemblyadapter.pager.ViewFragmentItemFactory
import com.github.panpf.assemblyadapter.pager2.AssemblySingleDataFragmentStateAdapter
import com.github.panpf.assemblyadapter.recycler.SimpleAdapterDataObserver
import com.github.panpf.tools4a.test.ktx.launchFragmentInContainerWithOn
import com.github.panpf.tools4j.test.ktx.assertThrow
import org.junit.Assert
import org.junit.Test

class AssemblySingleDataFragmentStateAdapterTest {

    private class TestItemFactory :
        ViewFragmentItemFactory<String>(String::class, android.R.layout.activity_list_item)

    class TestFragment : Fragment()

    @Test
    fun testConstructor() {
        TestFragment::class.launchFragmentInContainerWithOn { fragment ->
            AssemblySingleDataFragmentStateAdapter(fragment, TestItemFactory()).apply {
                Assert.assertNull(data)
            }

            AssemblySingleDataFragmentStateAdapter(
                fragment,
                TestItemFactory(),
                "123456"
            ).apply {
                Assert.assertNotNull(data)
                Assert.assertEquals("123456", data)
            }
        }
    }

    @Test
    fun testPropertyData() {
        TestFragment::class.launchFragmentInContainerWithOn { fragment ->
            var dataFromObserver: String? = null
            AssemblySingleDataFragmentStateAdapter(fragment, TestItemFactory()).apply {
                registerAdapterDataObserver(SimpleAdapterDataObserver {
                    dataFromObserver = data
                })

                Assert.assertNull(data)
                Assert.assertNull(dataFromObserver)

                data = "Test data changed notify invoke"
                Assert.assertEquals("Test data changed notify invoke", data)
                Assert.assertEquals("Test data changed notify invoke", dataFromObserver)

                data = "Test data changed notify invoke2"
                Assert.assertEquals("Test data changed notify invoke2", data)
                Assert.assertEquals("Test data changed notify invoke2", dataFromObserver)
            }
        }
    }

    @Test
    fun testMethodGetCount() {
        TestFragment::class.launchFragmentInContainerWithOn { fragment ->
            AssemblySingleDataFragmentStateAdapter(fragment, TestItemFactory()).apply {
                Assert.assertEquals(0, itemCount)

                data = "Test count"
                Assert.assertEquals(1, itemCount)

                data = null
                Assert.assertEquals(0, itemCount)
            }
        }
    }

    @Test
    fun testMethodGetItem() {
        TestFragment::class.launchFragmentInContainerWithOn { fragment ->
            AssemblySingleDataFragmentStateAdapter(fragment, TestItemFactory()).apply {
                assertThrow(IndexOutOfBoundsException::class) {
                    createFragment(-1)
                }
                assertThrow(IndexOutOfBoundsException::class) {
                    createFragment(0)
                }
                assertThrow(IndexOutOfBoundsException::class) {
                    createFragment(1)
                }

                data = "test"
                createFragment(0)
            }
        }
    }

    @Test
    fun testMethodGetItemFactoryByPosition() {
        TestFragment::class.launchFragmentInContainerWithOn { fragment ->
            val itemFactory = TestItemFactory()
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

                data = "test"
                Assert.assertSame(itemFactory, getItemFactoryByPosition(0))
            }
        }
    }
}