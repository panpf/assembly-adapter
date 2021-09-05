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
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.viewpager.widget.PagerAdapter
import com.github.panpf.assemblyadapter.pager.AssemblyFragmentStatePagerAdapter
import com.github.panpf.assemblyadapter.pager.AssemblySingleDataFragmentStatePagerAdapter
import com.github.panpf.assemblyadapter.pager.ConcatFragmentStatePagerAdapter
import com.github.panpf.assemblyadapter.pager.FragmentItemFactory
import com.github.panpf.tools4a.test.ktx.getFragmentSync
import com.github.panpf.tools4a.test.ktx.launchFragmentInContainer
import com.github.panpf.tools4j.test.ktx.assertThrow
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ConcatFragmentStatePagerAdapterTest {

    data class Text(val text: String)

    class TextFragmentItemFactory : FragmentItemFactory<Text>(Text::class) {
        override fun createFragment(
            bindingAdapterPosition: Int,
            absoluteAdapterPosition: Int,
            data: Text
        ): Fragment = TextFragment().apply {
            arguments = Bundle().apply {
                putInt("bindingAdapterPosition", bindingAdapterPosition)
                putInt("absoluteAdapterPosition", absoluteAdapterPosition)
            }
        }
    }

    class TextFragment : Fragment() {
        override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? = TextView(requireContext())
    }

    data class Image(val resId: Int)

    class ImageFragmentItemFactory : FragmentItemFactory<Image>(Image::class) {
        override fun createFragment(
            bindingAdapterPosition: Int,
            absoluteAdapterPosition: Int,
            data: Image
        ): Fragment = ImageFragment()
    }

    class ImageFragment : Fragment() {
        override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? = ImageView(requireContext())
    }

    class TestFragment : Fragment()


    @Test
    fun testConstructor() {
        val fragmentScenario = TestFragment::class.launchFragmentInContainer()
        val fragment = fragmentScenario.getFragmentSync()

        ConcatFragmentStatePagerAdapter(
            fragment.childFragmentManager,
            AssemblySingleDataFragmentStatePagerAdapter(
                fragment.childFragmentManager,
                TextFragmentItemFactory()
            )
        ).apply {
            Assert.assertEquals(1, adapters.size)
        }
        ConcatFragmentStatePagerAdapter(
            fragment.childFragmentManager,
            AssemblySingleDataFragmentStatePagerAdapter(
                fragment.childFragmentManager,
                TextFragmentItemFactory()
            ),
            AssemblySingleDataFragmentStatePagerAdapter(
                fragment.childFragmentManager,
                TextFragmentItemFactory()
            )
        ).apply {
            Assert.assertEquals(2, adapters.size)
        }

        ConcatFragmentStatePagerAdapter(
            fragment.childFragmentManager,
            listOf(
                AssemblySingleDataFragmentStatePagerAdapter(
                    fragment.childFragmentManager,
                    TextFragmentItemFactory()
                )
            )
        ).apply {
            Assert.assertEquals(1, adapters.size)
        }
        ConcatFragmentStatePagerAdapter(
            fragment.childFragmentManager,
            listOf(
                AssemblySingleDataFragmentStatePagerAdapter(
                    fragment.childFragmentManager,
                    TextFragmentItemFactory()
                ),
                AssemblySingleDataFragmentStatePagerAdapter(
                    fragment.childFragmentManager,
                    TextFragmentItemFactory()
                )
            )
        ).apply {
            Assert.assertEquals(2, adapters.size)
        }

        ConcatFragmentStatePagerAdapter(
            fragment.childFragmentManager,
            FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT,
            AssemblySingleDataFragmentStatePagerAdapter(
                fragment.childFragmentManager,
                TextFragmentItemFactory()
            )
        ).apply {
            Assert.assertEquals(1, adapters.size)
        }
        ConcatFragmentStatePagerAdapter(
            fragment.childFragmentManager,
            FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT,
            AssemblySingleDataFragmentStatePagerAdapter(
                fragment.childFragmentManager,
                TextFragmentItemFactory()
            ),
            AssemblySingleDataFragmentStatePagerAdapter(
                fragment.childFragmentManager,
                TextFragmentItemFactory()
            )
        ).apply {
            Assert.assertEquals(2, adapters.size)
        }

        ConcatFragmentStatePagerAdapter(
            fragment.childFragmentManager,
            FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT,
            listOf(
                AssemblySingleDataFragmentStatePagerAdapter(
                    fragment.childFragmentManager,
                    TextFragmentItemFactory()
                )
            )
        ).apply {
            Assert.assertEquals(1, adapters.size)
        }
        ConcatFragmentStatePagerAdapter(
            fragment.childFragmentManager,
            FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT,
            listOf(
                AssemblySingleDataFragmentStatePagerAdapter(
                    fragment.childFragmentManager,
                    TextFragmentItemFactory()
                ),
                AssemblySingleDataFragmentStatePagerAdapter(
                    fragment.childFragmentManager,
                    TextFragmentItemFactory()
                )
            )
        ).apply {
            Assert.assertEquals(2, adapters.size)
        }
    }

    @Test
    fun testPropertyIsDisableItemRefreshWhenDataSetChanged() {
        val fragmentScenario = TestFragment::class.launchFragmentInContainer()
        val fragment = fragmentScenario.getFragmentSync()

        ConcatFragmentStatePagerAdapter(fragment.childFragmentManager).apply {
            Assert.assertFalse(isDisableItemRefreshWhenDataSetChanged)

            isDisableItemRefreshWhenDataSetChanged = true
            Assert.assertTrue(isDisableItemRefreshWhenDataSetChanged)

            isDisableItemRefreshWhenDataSetChanged = false
            Assert.assertFalse(isDisableItemRefreshWhenDataSetChanged)
        }
    }

    @Test
    fun testMethodAddAndRemoveAdapter() {
        val fragmentScenario = TestFragment::class.launchFragmentInContainer()
        val fragment = fragmentScenario.getFragmentSync()

        ConcatFragmentStatePagerAdapter(fragment.childFragmentManager).apply {
            Assert.assertEquals(0, adapters.size)
            Assert.assertEquals(0, count)
            Assert.assertEquals("", adapters.joinToString { it.count.toString() })

            val adapter1 = AssemblySingleDataFragmentStatePagerAdapter(
                fragment.childFragmentManager,
                TextFragmentItemFactory(),
                Text("a")
            )
            val adapter2 = AssemblyFragmentStatePagerAdapter(
                fragment.childFragmentManager,
                listOf(TextFragmentItemFactory()),
                listOf(Text("b"), Text("c"))
            )
            val adapter3 = AssemblyFragmentStatePagerAdapter(
                fragment.childFragmentManager,
                listOf(TextFragmentItemFactory()),
                listOf(Text("d"), Text("e"), Text("f"))
            )

            addAdapter(adapter1)
            Assert.assertEquals(1, adapters.size)
            Assert.assertEquals(1, count)
            Assert.assertEquals("1", adapters.joinToString { it.count.toString() })

            addAdapter(adapter3)
            Assert.assertEquals(2, adapters.size)
            Assert.assertEquals(4, count)
            Assert.assertEquals("1, 3", adapters.joinToString { it.count.toString() })

            addAdapter(1, adapter2)
            Assert.assertEquals(3, adapters.size)
            Assert.assertEquals(6, count)
            Assert.assertEquals("1, 2, 3", adapters.joinToString { it.count.toString() })

            removeAdapter(adapter1)
            Assert.assertEquals(2, adapters.size)
            Assert.assertEquals(5, count)
            Assert.assertEquals("2, 3", adapters.joinToString { it.count.toString() })

            removeAdapter(adapter3)
            Assert.assertEquals(1, adapters.size)
            Assert.assertEquals(2, count)
            Assert.assertEquals("2", adapters.joinToString { it.count.toString() })
        }
    }

    @Test
    fun testMethodGetCount() {
        val fragmentScenario = TestFragment::class.launchFragmentInContainer()
        val fragment = fragmentScenario.getFragmentSync()

        val headerAdapter =
            AssemblySingleDataFragmentStatePagerAdapter(
                fragment.childFragmentManager,
                TextFragmentItemFactory()
            )
        val bodyAdapter = AssemblyFragmentStatePagerAdapter<Any>(
            fragment.childFragmentManager,
            listOf(
                TextFragmentItemFactory(),
                ImageFragmentItemFactory()
            )
        )
        val footerHeader =
            AssemblySingleDataFragmentStatePagerAdapter(
                fragment.childFragmentManager,
                ImageFragmentItemFactory()
            )
        ConcatFragmentStatePagerAdapter(
            fragment.childFragmentManager,
            headerAdapter,
            bodyAdapter,
            footerHeader
        ).apply {
            Assert.assertEquals(0, count)
            Assert.assertEquals(0, itemCount)

            headerAdapter.data = Text("hello")
            Assert.assertEquals(1, count)
            Assert.assertEquals(1, itemCount)

            bodyAdapter.submitList(
                listOf(
                    Image(android.R.drawable.bottom_bar),
                    Text("world"),
                    Image(android.R.drawable.btn_plus)
                )
            )
            Assert.assertEquals(4, count)
            Assert.assertEquals(4, itemCount)

            footerHeader.data = Image(android.R.drawable.btn_default)
            Assert.assertEquals(5, count)
            Assert.assertEquals(5, itemCount)

            bodyAdapter.submitList(listOf(Text("world")))
            Assert.assertEquals(3, count)
            Assert.assertEquals(3, itemCount)

            bodyAdapter.submitList(null)
            Assert.assertEquals(2, count)
            Assert.assertEquals(2, itemCount)

            footerHeader.data = null
            Assert.assertEquals(1, count)
            Assert.assertEquals(1, itemCount)

            headerAdapter.data = null
            Assert.assertEquals(0, count)
            Assert.assertEquals(0, itemCount)
        }
    }

    @Test
    fun testMethodGetItemData() {
        val fragmentScenario = TestFragment::class.launchFragmentInContainer()
        val fragment = fragmentScenario.getFragmentSync()

        ConcatFragmentStatePagerAdapter(
            fragment.childFragmentManager,
            AssemblySingleDataFragmentStatePagerAdapter(
                fragment.childFragmentManager,
                itemFactory = TextFragmentItemFactory(),
                initData = Text("hello"),
            ),
            AssemblyFragmentStatePagerAdapter(
                fragment.childFragmentManager,
                itemFactoryList = listOf(
                    TextFragmentItemFactory(),
                    ImageFragmentItemFactory()
                ),
                initDataList = listOf(
                    Image(android.R.drawable.bottom_bar),
                    Text("world"),
                    Image(android.R.drawable.btn_plus)
                ),
            ),
            AssemblySingleDataFragmentStatePagerAdapter(
                fragment.childFragmentManager,
                itemFactory = ImageFragmentItemFactory(),
                initData = Image(android.R.drawable.alert_dark_frame),
            ),
        ).apply {
            assertThrow(IndexOutOfBoundsException::class) {
                getItemData(-1)
            }
            Assert.assertEquals(Text("hello"), getItemData(0))
            Assert.assertEquals(Image(android.R.drawable.bottom_bar), getItemData(1))
            Assert.assertEquals(Text("world"), getItemData(2))
            Assert.assertEquals(Image(android.R.drawable.btn_plus), getItemData(3))
            Assert.assertEquals(Image(android.R.drawable.alert_dark_frame), getItemData(4))
            assertThrow(IllegalArgumentException::class) {
                getItemData(5)
            }
        }
    }

    @Test
    fun testMethodGetItem() {
        val fragmentScenario = TestFragment::class.launchFragmentInContainer()
        val fragment = fragmentScenario.getFragmentSync()
        val context = InstrumentationRegistry.getInstrumentation().context
        ConcatFragmentStatePagerAdapter(
            fragment.childFragmentManager,
            AssemblySingleDataFragmentStatePagerAdapter(
                fragment.childFragmentManager,
                itemFactory = TextFragmentItemFactory(),
                initData = Text("a"),
            ),
            AssemblyFragmentStatePagerAdapter(
                fragment.childFragmentManager,
                itemFactoryList = listOf(
                    TextFragmentItemFactory(),
                    ImageFragmentItemFactory()
                ),
                initDataList = listOf(
                    Image(android.R.drawable.bottom_bar),
                    Text("b"),
                    Image(android.R.drawable.btn_plus)
                ),
            ),
            AssemblySingleDataFragmentStatePagerAdapter(
                fragment.childFragmentManager,
                itemFactory = ImageFragmentItemFactory(),
                initData = Image(android.R.drawable.alert_dark_frame),
            ),
        ).apply {
            assertThrow(IndexOutOfBoundsException::class) {
                getItem(-1)
            }
            var item0: Fragment? = null
            var item1: Fragment? = null
            var item2: Fragment? = null
            var item3: Fragment? = null
            var item4: Fragment? = null
            fragmentScenario.onFragment {
                item0 = getItem(0)
                item1 = getItem(1)
                item2 = getItem(2)
                item4 = getItem(4)
                item3 = getItem(3)
            }
            Assert.assertTrue(item0 is TextFragment)
            Assert.assertTrue(item1 is ImageFragment)
            Assert.assertTrue(item2 is TextFragment)
            Assert.assertTrue(item3 is ImageFragment)
            Assert.assertTrue(item4 is ImageFragment)
            assertThrow(IllegalArgumentException::class) {
                getItem(5)
            }
        }
    }

    @Test
    fun testMethodFindLocalAdapterAndPosition() {
        val fragmentScenario = TestFragment::class.launchFragmentInContainer()
        val fragment = fragmentScenario.getFragmentSync()

        val headerAdapter = AssemblySingleDataFragmentStatePagerAdapter(
            fragment.childFragmentManager,
            itemFactory = TextFragmentItemFactory(),
            initData = Text("hello"),
        )
        val bodyAdapter = AssemblyFragmentStatePagerAdapter(
            fragment.childFragmentManager,
            itemFactoryList = listOf(
                TextFragmentItemFactory(),
                ImageFragmentItemFactory()
            ),
            initDataList = listOf(
                Image(android.R.drawable.bottom_bar),
                Text("world"),
                Image(android.R.drawable.btn_plus)
            ),
        )
        val footerAdapter = AssemblySingleDataFragmentStatePagerAdapter(
            fragment.childFragmentManager,
            itemFactory = ImageFragmentItemFactory(),
            initData = Image(android.R.drawable.alert_dark_frame),
        )
        ConcatFragmentStatePagerAdapter(
            fragment.childFragmentManager,
            headerAdapter,
            bodyAdapter,
            footerAdapter,
        ).apply {
            findLocalAdapterAndPosition(-1).apply {
                Assert.assertSame(headerAdapter, first)
                Assert.assertEquals(-1, second)
            }
            findLocalAdapterAndPosition(0).apply {
                Assert.assertSame(headerAdapter, first)
                Assert.assertEquals(0, second)
            }
            findLocalAdapterAndPosition(1).apply {
                Assert.assertSame(bodyAdapter, first)
                Assert.assertEquals(0, second)
            }
            findLocalAdapterAndPosition(2).apply {
                Assert.assertSame(bodyAdapter, first)
                Assert.assertEquals(1, second)
            }
            findLocalAdapterAndPosition(3).apply {
                Assert.assertSame(bodyAdapter, first)
                Assert.assertEquals(2, second)
            }
            findLocalAdapterAndPosition(4).apply {
                Assert.assertSame(footerAdapter, first)
                Assert.assertEquals(0, second)
            }
            assertThrow(IllegalArgumentException::class) {
                findLocalAdapterAndPosition(5)
            }
        }
    }

    @Test
    fun testMethodGetItemPosition() {
        val fragmentScenario = TestFragment::class.launchFragmentInContainer()
        val fragment = fragmentScenario.getFragmentSync()
        val headerAdapter = AssemblySingleDataFragmentStatePagerAdapter(
            fragment.childFragmentManager,
            itemFactory = TextFragmentItemFactory(),
            initData = Text("a"),
        )
        val bodyAdapter = AssemblyFragmentStatePagerAdapter(
            fragment.childFragmentManager,
            itemFactoryList = listOf(
                TextFragmentItemFactory(),
                ImageFragmentItemFactory()
            ),
            initDataList = listOf(
                Image(android.R.drawable.bottom_bar),
                Text("b"),
                Image(android.R.drawable.btn_plus)
            ),
        )
        val footerAdapter = AssemblySingleDataFragmentStatePagerAdapter(
            fragment.childFragmentManager,
            itemFactory = ImageFragmentItemFactory(),
            initData = Image(android.R.drawable.alert_dark_frame),
        )
        ConcatFragmentStatePagerAdapter(
            fragment.childFragmentManager,
            headerAdapter,
            bodyAdapter,
            footerAdapter
        ).apply {
            var item0: Fragment? = null
            var item1: Fragment? = null
            var item2: Fragment? = null
            var item3: Fragment? = null
            var item4: Fragment? = null
            fragmentScenario.onFragment {
                item0 = getItem(0)
                item1 = getItem(1)
                item2 = getItem(2)
                item3 = getItem(3)
                item4 = getItem(4)
            }
            Assert.assertEquals(PagerAdapter.POSITION_NONE, getItemPosition(item0!!))
            Assert.assertEquals(PagerAdapter.POSITION_NONE, getItemPosition(item1!!))
            Assert.assertEquals(PagerAdapter.POSITION_NONE, getItemPosition(item2!!))
            Assert.assertEquals(PagerAdapter.POSITION_NONE, getItemPosition(item3!!))
            Assert.assertEquals(PagerAdapter.POSITION_NONE, getItemPosition(item4!!))

            fragmentScenario.onFragment {
                fragment.childFragmentManager.beginTransaction()
                    .add(item0!!, null)
                    .add(item1!!, null)
                    .add(item2!!, null)
                    .add(item3!!, null)
                    .add(item4!!, null)
                    .commit()
            }
            Thread.sleep(100)

            Assert.assertEquals(PagerAdapter.POSITION_UNCHANGED, getItemPosition(item0!!))
            Assert.assertEquals(PagerAdapter.POSITION_UNCHANGED, getItemPosition(item1!!))
            Assert.assertEquals(PagerAdapter.POSITION_UNCHANGED, getItemPosition(item2!!))
            Assert.assertEquals(PagerAdapter.POSITION_UNCHANGED, getItemPosition(item3!!))
            Assert.assertEquals(PagerAdapter.POSITION_UNCHANGED, getItemPosition(item4!!))

            headerAdapter.data = Text("good")
            Assert.assertEquals(PagerAdapter.POSITION_NONE, getItemPosition(item0!!))
            Assert.assertEquals(PagerAdapter.POSITION_UNCHANGED, getItemPosition(item1!!))
            Assert.assertEquals(PagerAdapter.POSITION_UNCHANGED, getItemPosition(item2!!))
            Assert.assertEquals(PagerAdapter.POSITION_UNCHANGED, getItemPosition(item3!!))
            Assert.assertEquals(PagerAdapter.POSITION_UNCHANGED, getItemPosition(item4!!))

            bodyAdapter.submitList(
                listOf(
                    Image(android.R.drawable.bottom_bar),
                    Text("b"),
                    Image(android.R.drawable.btn_dialog)
                )
            )
            Assert.assertEquals(PagerAdapter.POSITION_NONE, getItemPosition(item0!!))
            Assert.assertEquals(PagerAdapter.POSITION_UNCHANGED, getItemPosition(item1!!))
            Assert.assertEquals(PagerAdapter.POSITION_UNCHANGED, getItemPosition(item2!!))
            Assert.assertEquals(PagerAdapter.POSITION_NONE, getItemPosition(item3!!))
            Assert.assertEquals(PagerAdapter.POSITION_UNCHANGED, getItemPosition(item4!!))

            isDisableItemRefreshWhenDataSetChanged = true
            Assert.assertEquals(PagerAdapter.POSITION_UNCHANGED, getItemPosition(item0!!))
            Assert.assertEquals(PagerAdapter.POSITION_UNCHANGED, getItemPosition(item1!!))
            Assert.assertEquals(PagerAdapter.POSITION_UNCHANGED, getItemPosition(item2!!))
            Assert.assertEquals(PagerAdapter.POSITION_UNCHANGED, getItemPosition(item3!!))
            Assert.assertEquals(PagerAdapter.POSITION_UNCHANGED, getItemPosition(item4!!))

            isDisableItemRefreshWhenDataSetChanged = false
            Assert.assertEquals(PagerAdapter.POSITION_NONE, getItemPosition(item0!!))
            Assert.assertEquals(PagerAdapter.POSITION_UNCHANGED, getItemPosition(item1!!))
            Assert.assertEquals(PagerAdapter.POSITION_UNCHANGED, getItemPosition(item2!!))
            Assert.assertEquals(PagerAdapter.POSITION_NONE, getItemPosition(item3!!))
            Assert.assertEquals(PagerAdapter.POSITION_UNCHANGED, getItemPosition(item4!!))
        }
    }

    @Test
    fun testMethodGetItemPosition2() {
        val fragmentScenario = TestFragment::class.launchFragmentInContainer()
        val fragment = fragmentScenario.getFragmentSync()
        val headerAdapter = AssemblySingleDataFragmentStatePagerAdapter(
            fragment.childFragmentManager,
            itemFactory = TextFragmentItemFactory(),
        )
        val bodyAdapter = AssemblyFragmentStatePagerAdapter(
            fragment.childFragmentManager,
            itemFactoryList = listOf(TextFragmentItemFactory()),
            initDataList = listOf(Text("b"), Text("c"), Text("d")),
        )
        val footerAdapter = AssemblySingleDataFragmentStatePagerAdapter(
            fragment.childFragmentManager,
            itemFactory = TextFragmentItemFactory(),
        )
        ConcatFragmentStatePagerAdapter(
            fragment.childFragmentManager,
            headerAdapter,
            bodyAdapter,
            footerAdapter
        ).apply {
            var item0: Fragment? = null
            var item1: Fragment? = null
            var item2: Fragment? = null
            fragmentScenario.onFragment {
                item0 = getItem(0)
                item1 = getItem(1)
                item2 = getItem(2)
            }
            Assert.assertEquals(PagerAdapter.POSITION_NONE, getItemPosition(item0!!))
            Assert.assertEquals(PagerAdapter.POSITION_NONE, getItemPosition(item1!!))
            Assert.assertEquals(PagerAdapter.POSITION_NONE, getItemPosition(item2!!))

            fragmentScenario.onFragment {
                fragment.childFragmentManager.beginTransaction()
                    .add(item0!!, null)
                    .add(item1!!, null)
                    .add(item2!!, null)
                    .commit()
            }
            Thread.sleep(100)

            Assert.assertEquals(PagerAdapter.POSITION_UNCHANGED, getItemPosition(item0!!))
            Assert.assertEquals(PagerAdapter.POSITION_UNCHANGED, getItemPosition(item1!!))
            Assert.assertEquals(PagerAdapter.POSITION_UNCHANGED, getItemPosition(item2!!))

            headerAdapter.data = Text("a")
            Assert.assertEquals(PagerAdapter.POSITION_NONE, getItemPosition(item0!!))
            Assert.assertEquals(PagerAdapter.POSITION_NONE, getItemPosition(item1!!))
            Assert.assertEquals(PagerAdapter.POSITION_NONE, getItemPosition(item2!!))
        }
    }

    @Test
    fun testNestedAdapterPosition() {
        val fragmentScenario = TestFragment::class.launchFragmentInContainer()
        val fragment = fragmentScenario.getFragmentSync()


        val count1Adapter = AssemblySingleDataFragmentStatePagerAdapter(
            fragment.childFragmentManager,
            TextFragmentItemFactory(),
            Text("a")
        )
        val count3Adapter = AssemblyFragmentStatePagerAdapter(
            fragment.childFragmentManager,
            listOf(TextFragmentItemFactory()),
            listOf(Text("a"), Text("a"), Text("a"))
        )
        val count5Adapter = AssemblyFragmentStatePagerAdapter(
            fragment.childFragmentManager,
            listOf(TextFragmentItemFactory()),
            listOf(Text("a"), Text("a"), Text("a"), Text("a"), Text("a"))
        )
        val count7Adapter = AssemblyFragmentStatePagerAdapter(
            fragment.childFragmentManager,
            listOf(TextFragmentItemFactory()),
            listOf(Text("a"), Text("a"), Text("a"), Text("a"), Text("a"), Text("a"), Text("a"))
        )
        val concatCount9Adapter = ConcatFragmentStatePagerAdapter(
            fragment.childFragmentManager, count1Adapter, count3Adapter, count5Adapter
        )
        val concatCount11Adapter = ConcatFragmentStatePagerAdapter(
            fragment.childFragmentManager, count1Adapter, count3Adapter, count7Adapter
        )
        val concatCount13Adapter = ConcatFragmentStatePagerAdapter(
            fragment.childFragmentManager, count1Adapter, count5Adapter, count7Adapter
        )
        val concatNestingCount16Adapter = ConcatFragmentStatePagerAdapter(
            fragment.childFragmentManager,
            count1Adapter,
            ConcatFragmentStatePagerAdapter(
                fragment.childFragmentManager, count3Adapter, count5Adapter
            ),
            count7Adapter
        )

        Assert.assertEquals("count1Adapter.count", 1, count1Adapter.count)
        Assert.assertEquals("count3Adapter.count", 3, count3Adapter.count)
        Assert.assertEquals("count5Adapter.count", 5, count5Adapter.count)
        Assert.assertEquals("count7Adapter.count", 7, count7Adapter.count)
        Assert.assertEquals("count7Adapter.count", 9, concatCount9Adapter.count)
        Assert.assertEquals("count7Adapter.count", 11, concatCount11Adapter.count)
        Assert.assertEquals("count12Adapter.count", 13, concatCount13Adapter.count)
        Assert.assertEquals("count15Adapter.count", 16, concatNestingCount16Adapter.count)

        val verifyAdapterPosition: (FragmentStatePagerAdapter, Int, Int, Int) -> Unit =
            { adapter, position, expectedBindingAdapterPosition, expectedAbsoluteAdapterPosition ->
                val itemFragment = runBlocking {
                    withContext(Dispatchers.Main) {
                        adapter.getItem(position)
                    }
                }
                val bindingAdapterPosition =
                    itemFragment.arguments!!.getInt("bindingAdapterPosition")
                val absoluteAdapterPosition =
                    itemFragment.arguments!!.getInt("absoluteAdapterPosition")
                Assert.assertEquals(
                    "count${adapter.count}Adapter. position(${position}). itemFragment.bindingAdapterPosition",
                    expectedBindingAdapterPosition, bindingAdapterPosition
                )

                Assert.assertEquals(
                    "count${adapter.count}Adapter. position(${position}). itemFragment.absoluteAdapterPosition",
                    expectedAbsoluteAdapterPosition, absoluteAdapterPosition
                )
            }

        /* adapter, position, bindingAdapterPosition, absoluteAdapterPosition */
        verifyAdapterPosition(count1Adapter, 0, 0, 0)

        verifyAdapterPosition(count3Adapter, 0, 0, 0)
        verifyAdapterPosition(count3Adapter, 1, 1, 1)
        verifyAdapterPosition(count3Adapter, 2, 2, 2)

        verifyAdapterPosition(count5Adapter, 0, 0, 0)
        verifyAdapterPosition(count5Adapter, 1, 1, 1)
        verifyAdapterPosition(count5Adapter, 2, 2, 2)
        verifyAdapterPosition(count5Adapter, 3, 3, 3)
        verifyAdapterPosition(count5Adapter, 4, 4, 4)

        verifyAdapterPosition(count7Adapter, 0, 0, 0)
        verifyAdapterPosition(count7Adapter, 1, 1, 1)
        verifyAdapterPosition(count7Adapter, 2, 2, 2)
        verifyAdapterPosition(count7Adapter, 3, 3, 3)
        verifyAdapterPosition(count7Adapter, 4, 4, 4)
        verifyAdapterPosition(count7Adapter, 5, 5, 5)
        verifyAdapterPosition(count7Adapter, 6, 6, 6)

        verifyAdapterPosition(concatCount9Adapter, 0, 0, 0)
        verifyAdapterPosition(concatCount9Adapter, 1, 0, 1)
        verifyAdapterPosition(concatCount9Adapter, 2, 1, 2)
        verifyAdapterPosition(concatCount9Adapter, 3, 2, 3)
        verifyAdapterPosition(concatCount9Adapter, 4, 0, 4)
        verifyAdapterPosition(concatCount9Adapter, 5, 1, 5)
        verifyAdapterPosition(concatCount9Adapter, 6, 2, 6)
        verifyAdapterPosition(concatCount9Adapter, 7, 3, 7)
        verifyAdapterPosition(concatCount9Adapter, 8, 4, 8)

        verifyAdapterPosition(concatCount11Adapter, 0, 0, 0)
        verifyAdapterPosition(concatCount11Adapter, 1, 0, 1)
        verifyAdapterPosition(concatCount11Adapter, 2, 1, 2)
        verifyAdapterPosition(concatCount11Adapter, 3, 2, 3)
        verifyAdapterPosition(concatCount11Adapter, 4, 0, 4)
        verifyAdapterPosition(concatCount11Adapter, 5, 1, 5)
        verifyAdapterPosition(concatCount11Adapter, 6, 2, 6)
        verifyAdapterPosition(concatCount11Adapter, 7, 3, 7)
        verifyAdapterPosition(concatCount11Adapter, 8, 4, 8)
        verifyAdapterPosition(concatCount11Adapter, 9, 5, 9)
        verifyAdapterPosition(concatCount11Adapter, 10, 6, 10)

        verifyAdapterPosition(concatCount13Adapter, 0, 0, 0)
        verifyAdapterPosition(concatCount13Adapter, 1, 0, 1)
        verifyAdapterPosition(concatCount13Adapter, 2, 1, 2)
        verifyAdapterPosition(concatCount13Adapter, 3, 2, 3)
        verifyAdapterPosition(concatCount13Adapter, 4, 3, 4)
        verifyAdapterPosition(concatCount13Adapter, 5, 4, 5)
        verifyAdapterPosition(concatCount13Adapter, 6, 0, 6)
        verifyAdapterPosition(concatCount13Adapter, 7, 1, 7)
        verifyAdapterPosition(concatCount13Adapter, 8, 2, 8)
        verifyAdapterPosition(concatCount13Adapter, 9, 3, 9)
        verifyAdapterPosition(concatCount13Adapter, 10, 4, 10)
        verifyAdapterPosition(concatCount13Adapter, 11, 5, 11)
        verifyAdapterPosition(concatCount13Adapter, 12, 6, 12)

        verifyAdapterPosition(concatNestingCount16Adapter, 0, 0, 0)
        verifyAdapterPosition(concatNestingCount16Adapter, 1, 0, 1)
        verifyAdapterPosition(concatNestingCount16Adapter, 2, 1, 2)
        verifyAdapterPosition(concatNestingCount16Adapter, 3, 2, 3)
        verifyAdapterPosition(concatNestingCount16Adapter, 4, 0, 4)
        verifyAdapterPosition(concatNestingCount16Adapter, 5, 1, 5)
        verifyAdapterPosition(concatNestingCount16Adapter, 6, 2, 6)
        verifyAdapterPosition(concatNestingCount16Adapter, 7, 3, 7)
        verifyAdapterPosition(concatNestingCount16Adapter, 8, 4, 8)
        verifyAdapterPosition(concatNestingCount16Adapter, 9, 0, 9)
        verifyAdapterPosition(concatNestingCount16Adapter, 10, 1, 10)
        verifyAdapterPosition(concatNestingCount16Adapter, 11, 2, 11)
        verifyAdapterPosition(concatNestingCount16Adapter, 12, 3, 12)
        verifyAdapterPosition(concatNestingCount16Adapter, 13, 4, 13)
        verifyAdapterPosition(concatNestingCount16Adapter, 14, 5, 14)
        verifyAdapterPosition(concatNestingCount16Adapter, 15, 6, 15)
    }
}