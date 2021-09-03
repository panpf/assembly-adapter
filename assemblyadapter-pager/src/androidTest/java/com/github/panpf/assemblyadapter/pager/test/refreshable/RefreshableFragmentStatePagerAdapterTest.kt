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
package com.github.panpf.assemblyadapter.pager.test.refreshable

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.viewpager.widget.PagerAdapter
import com.github.panpf.assemblyadapter.pager.R
import com.github.panpf.assemblyadapter.pager.refreshable.RefreshableFragmentStatePagerAdapter
import com.github.panpf.tools4a.test.ktx.getFragmentSync
import com.github.panpf.tools4a.test.ktx.launchFragmentInContainer
import com.github.panpf.tools4j.test.ktx.assertThrow
import org.junit.Assert
import org.junit.Test

class RefreshableFragmentStatePagerAdapterTest {

    private class TestRefreshableFragmentStatePagerAdapter(
        fragmentManager: FragmentManager, var list: List<String>
    ) : RefreshableFragmentStatePagerAdapter<String>(fragmentManager) {

        override fun getItemData(position: Int): String {
            val count = count
            if (position < 0 || position >= count) {
                throw IndexOutOfBoundsException("Index: $position, Size: $count")
            }
            return list[position]
        }

        override fun getCount(): Int = list.size

        override fun getFragment(position: Int): Fragment {
            return TestFragment.create(getItemData(position))
        }
    }

    class TestFragment : Fragment() {

        companion object {
            fun create(data: String) = TestFragment().apply {
                arguments = Bundle().apply {
                    putString("data", data)
                }
            }
        }

        override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View = TextView(requireContext()).apply {
            text = arguments!!.getString("data")
        }
    }

    class ContainerFragment : Fragment()

    @Test
    fun testPropertyIsDisableItemRefreshWhenDataSetChanged() {
        val fragmentScenario = ContainerFragment::class.launchFragmentInContainer()
        val fragment = fragmentScenario.getFragmentSync()
        TestRefreshableFragmentStatePagerAdapter(
            fragment.childFragmentManager,
            listOf("hello", "world")
        ).apply {
            Assert.assertFalse(isDisableItemRefreshWhenDataSetChanged)

            isDisableItemRefreshWhenDataSetChanged = true
            Assert.assertTrue(isDisableItemRefreshWhenDataSetChanged)

            isDisableItemRefreshWhenDataSetChanged = false
            Assert.assertFalse(isDisableItemRefreshWhenDataSetChanged)
        }
    }

    @Test
    fun testMethodGetItem() {
        val fragmentScenario = ContainerFragment::class.launchFragmentInContainer()
        val fragment = fragmentScenario.getFragmentSync()

        TestRefreshableFragmentStatePagerAdapter(
            fragment.childFragmentManager,
            listOf("hello", "world")
        ).apply {
            assertThrow(IndexOutOfBoundsException::class) {
                getItem(-1)
            }
            var item0: TestFragment? = null
            fragmentScenario.onFragment {
                item0 = (getItem(0) as TestFragment)
            }
            item0!!.apply {
                Assert.assertNull(view)
                fragmentScenario.onFragment {
                    fragment.childFragmentManager.beginTransaction()
                        .add(this, null)
                        .commit()
                }
                Thread.sleep(100)
                (view as TextView).apply {
                    Assert.assertEquals("hello", text.toString())
                    Assert.assertEquals(0, (getTag(R.id.aa_tag_pager_refresh_position) as Int))
                    Assert.assertEquals("hello", (getTag(R.id.aa_tag_pager_refresh_data)))
                }
            }

            var item1: TestFragment? = null
            fragmentScenario.onFragment {
                item1 = (getItem(1) as TestFragment)
            }
            item1!!.apply {
                Assert.assertNull(view)
                fragmentScenario.onFragment {
                    fragment.childFragmentManager.beginTransaction()
                        .add(this, null)
                        .commit()
                }
                Thread.sleep(100)
                (view as TextView).apply {
                    Assert.assertEquals("world", text.toString())
                    Assert.assertEquals(1, (getTag(R.id.aa_tag_pager_refresh_position) as Int))
                    Assert.assertEquals("world", (getTag(R.id.aa_tag_pager_refresh_data)))
                }
            }

            isDisableItemRefreshWhenDataSetChanged = true
            item0 = null
            fragmentScenario.onFragment {
                item0 = (getItem(0) as TestFragment)
            }
            item0!!.apply {
                Assert.assertNull(view)
                fragmentScenario.onFragment {
                    fragment.childFragmentManager.beginTransaction()
                        .add(this, null)
                        .commit()
                }
                Thread.sleep(100)
                (view as TextView).apply {
                    Assert.assertEquals("hello", text.toString())
                    Assert.assertNull(getTag(R.id.aa_tag_pager_refresh_position))
                    Assert.assertNull(getTag(R.id.aa_tag_pager_refresh_data))
                }
            }

            item1 = null
            fragmentScenario.onFragment {
                item1 = (getItem(1) as TestFragment)
            }
            item1!!.apply {
                Assert.assertNull(view)
                fragmentScenario.onFragment {
                    fragment.childFragmentManager.beginTransaction()
                        .add(this, null)
                        .commit()
                }
                Thread.sleep(100)
                (view as TextView).apply {
                    Assert.assertEquals("world", text.toString())
                    Assert.assertNull(getTag(R.id.aa_tag_pager_refresh_position))
                    Assert.assertNull(getTag(R.id.aa_tag_pager_refresh_data))
                }
            }
        }
    }

    @Test
    fun testMethodGetItemPosition() {
        val fragmentScenario = ContainerFragment::class.launchFragmentInContainer()
        val fragment = fragmentScenario.getFragmentSync()
        TestRefreshableFragmentStatePagerAdapter(
            fragment.childFragmentManager,
            listOf("hello", "world")
        ).apply {
            var item0: TestFragment? = null
            fragmentScenario.onFragment {
                item0 = (getItem(0) as TestFragment)
            }
            var item1: TestFragment? = null
            fragmentScenario.onFragment {
                item1 = (getItem(1) as TestFragment)
            }
            Assert.assertNull(item0!!.view)
            Assert.assertNull(item1!!.view)
            Assert.assertEquals(PagerAdapter.POSITION_NONE, getItemPosition(item0!!))
            Assert.assertEquals(PagerAdapter.POSITION_NONE, getItemPosition(item1!!))

            fragmentScenario.onFragment {
                fragment.childFragmentManager.beginTransaction()
                    .add(item0!!, null)
                    .add(item1!!, null)
                    .commit()
            }
            Thread.sleep(100)
            Assert.assertEquals(PagerAdapter.POSITION_UNCHANGED, getItemPosition(item0!!))
            Assert.assertEquals(PagerAdapter.POSITION_UNCHANGED, getItemPosition(item1!!))

            list = listOf("good", "world")
            Assert.assertEquals(PagerAdapter.POSITION_NONE, getItemPosition(item0!!))
            Assert.assertEquals(PagerAdapter.POSITION_UNCHANGED, getItemPosition(item1!!))

            isDisableItemRefreshWhenDataSetChanged = true
            Assert.assertEquals(PagerAdapter.POSITION_UNCHANGED, getItemPosition(item0!!))
            Assert.assertEquals(PagerAdapter.POSITION_UNCHANGED, getItemPosition(item1!!))
            isDisableItemRefreshWhenDataSetChanged = false

            list = listOf("hello", "good")
            Assert.assertEquals(PagerAdapter.POSITION_UNCHANGED, getItemPosition(item0!!))
            Assert.assertEquals(PagerAdapter.POSITION_NONE, getItemPosition(item1!!))

            isDisableItemRefreshWhenDataSetChanged = true
            Assert.assertEquals(PagerAdapter.POSITION_UNCHANGED, getItemPosition(item0!!))
            Assert.assertEquals(PagerAdapter.POSITION_UNCHANGED, getItemPosition(item1!!))
            isDisableItemRefreshWhenDataSetChanged = false
        }
    }
}