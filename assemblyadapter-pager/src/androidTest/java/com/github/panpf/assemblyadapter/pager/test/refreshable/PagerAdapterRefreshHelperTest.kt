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
package com.github.panpf.assemblyadapter.pager.test.refreshable

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.test.platform.app.InstrumentationRegistry
import com.github.panpf.assemblyadapter.pager.refreshable.PagerAdapterRefreshHelper
import com.github.panpf.assemblyadapter.pager.test.R
import com.github.panpf.tools4a.test.ktx.launchFragmentInContainerWithOn
import org.junit.Assert
import org.junit.Test

class PagerAdapterRefreshHelperTest {

    @Test
    fun testView() {
        var list = listOf("data0", "data1", "data2")
        val pagerAdapterRefreshHelper = PagerAdapterRefreshHelper(
            { list.size },
            { position -> list[position] }
        )

        val context = InstrumentationRegistry.getInstrumentation().context

        val view0 = TextView(context).apply {
            Assert.assertNull(getTag(R.id.aa_tag_pager_refresh_position))
            Assert.assertNull(getTag(R.id.aa_tag_pager_refresh_data))
            Assert.assertTrue(pagerAdapterRefreshHelper.isItemPositionChanged(this))
        }
        val view1 = TextView(context).apply {
            Assert.assertNull(getTag(R.id.aa_tag_pager_refresh_position))
            Assert.assertNull(getTag(R.id.aa_tag_pager_refresh_data))
            Assert.assertTrue(pagerAdapterRefreshHelper.isItemPositionChanged(this))
        }
        val view2 = TextView(context).apply {
            Assert.assertNull(getTag(R.id.aa_tag_pager_refresh_position))
            Assert.assertNull(getTag(R.id.aa_tag_pager_refresh_data))
            Assert.assertTrue(pagerAdapterRefreshHelper.isItemPositionChanged(this))
        }

        pagerAdapterRefreshHelper.bindPositionAndData(view0, 0, list[0])
        pagerAdapterRefreshHelper.bindPositionAndData(view1, 1, list[1])
        pagerAdapterRefreshHelper.bindPositionAndData(view2, 2, list[2])

        view0.apply {
            Assert.assertEquals(0, getTag(R.id.aa_tag_pager_refresh_position))
            Assert.assertEquals("data0", getTag(R.id.aa_tag_pager_refresh_data))
            Assert.assertFalse(pagerAdapterRefreshHelper.isItemPositionChanged(this))
        }
        view1.apply {
            Assert.assertEquals(1, getTag(R.id.aa_tag_pager_refresh_position))
            Assert.assertEquals("data1", getTag(R.id.aa_tag_pager_refresh_data))
            Assert.assertFalse(pagerAdapterRefreshHelper.isItemPositionChanged(this))
        }
        view2.apply {
            Assert.assertEquals(2, getTag(R.id.aa_tag_pager_refresh_position))
            Assert.assertEquals("data2", getTag(R.id.aa_tag_pager_refresh_data))
            Assert.assertFalse(pagerAdapterRefreshHelper.isItemPositionChanged(this))
        }

        list = listOf("test0", "data1", "test2")

        view0.apply {
            Assert.assertEquals(0, getTag(R.id.aa_tag_pager_refresh_position))
            Assert.assertEquals("data0", getTag(R.id.aa_tag_pager_refresh_data))
            Assert.assertTrue(pagerAdapterRefreshHelper.isItemPositionChanged(this))
        }
        view1.apply {
            Assert.assertEquals(1, getTag(R.id.aa_tag_pager_refresh_position))
            Assert.assertEquals("data1", getTag(R.id.aa_tag_pager_refresh_data))
            Assert.assertFalse(pagerAdapterRefreshHelper.isItemPositionChanged(this))
        }
        view2.apply {
            Assert.assertEquals(2, getTag(R.id.aa_tag_pager_refresh_position))
            Assert.assertEquals("data2", getTag(R.id.aa_tag_pager_refresh_data))
            Assert.assertTrue(pagerAdapterRefreshHelper.isItemPositionChanged(this))
        }

        list = listOf()

        view0.apply {
            Assert.assertEquals(0, getTag(R.id.aa_tag_pager_refresh_position))
            Assert.assertEquals("data0", getTag(R.id.aa_tag_pager_refresh_data))
            Assert.assertTrue(pagerAdapterRefreshHelper.isItemPositionChanged(this))
        }
        view1.apply {
            Assert.assertEquals(1, getTag(R.id.aa_tag_pager_refresh_position))
            Assert.assertEquals("data1", getTag(R.id.aa_tag_pager_refresh_data))
            Assert.assertTrue(pagerAdapterRefreshHelper.isItemPositionChanged(this))
        }
        view2.apply {
            Assert.assertEquals(2, getTag(R.id.aa_tag_pager_refresh_position))
            Assert.assertEquals("data2", getTag(R.id.aa_tag_pager_refresh_data))
            Assert.assertTrue(pagerAdapterRefreshHelper.isItemPositionChanged(this))
        }
    }

    @Test
    fun testFragment() {
        var list = listOf("data0", "data1", "data2")
        val pagerAdapterRefreshHelper = PagerAdapterRefreshHelper(
            { list.size },
            { position -> list[position] }
        )

        val fragment0 = TestFragment().apply {
            Assert.assertNull(view)
            Assert.assertTrue(pagerAdapterRefreshHelper.isItemPositionChanged(this))
        }
        val fragment1 = TestFragment().apply {
            Assert.assertNull(view)
            Assert.assertTrue(pagerAdapterRefreshHelper.isItemPositionChanged(this))
        }
        val fragment2 = TestFragment().apply {
            Assert.assertNull(view)
            Assert.assertTrue(pagerAdapterRefreshHelper.isItemPositionChanged(this))
        }

        ContainerFragment::class.launchFragmentInContainerWithOn { fragment ->
            pagerAdapterRefreshHelper.bindPositionAndData(fragment0, 0, list[0])
            pagerAdapterRefreshHelper.bindPositionAndData(fragment1, 1, list[1])
            pagerAdapterRefreshHelper.bindPositionAndData(fragment2, 2, list[2])

            fragment0.apply {
                Assert.assertNull(view)
                Assert.assertTrue(pagerAdapterRefreshHelper.isItemPositionChanged(this))
            }
            fragment1.apply {
                Assert.assertNull(view)
                Assert.assertTrue(pagerAdapterRefreshHelper.isItemPositionChanged(this))
            }
            fragment2.apply {
                Assert.assertNull(view)
                Assert.assertTrue(pagerAdapterRefreshHelper.isItemPositionChanged(this))
            }

            fragment.childFragmentManager.beginTransaction()
                .add(android.R.id.content, fragment0, null)
                .add(android.R.id.content, fragment1, null)
                .add(android.R.id.content, fragment2, null)
                .commit()
        }
        Thread.sleep(100)

        fragment0.apply {
            Assert.assertEquals(0, view!!.getTag(R.id.aa_tag_pager_refresh_position))
            Assert.assertEquals("data0", view!!.getTag(R.id.aa_tag_pager_refresh_data))
            Assert.assertFalse(pagerAdapterRefreshHelper.isItemPositionChanged(this))
        }
        fragment1.apply {
            Assert.assertEquals(1, view!!.getTag(R.id.aa_tag_pager_refresh_position))
            Assert.assertEquals("data1", view!!.getTag(R.id.aa_tag_pager_refresh_data))
            Assert.assertFalse(pagerAdapterRefreshHelper.isItemPositionChanged(this))
        }
        fragment2.apply {
            Assert.assertEquals(2, view!!.getTag(R.id.aa_tag_pager_refresh_position))
            Assert.assertEquals("data2", view!!.getTag(R.id.aa_tag_pager_refresh_data))
            Assert.assertFalse(pagerAdapterRefreshHelper.isItemPositionChanged(this))
        }

        list = listOf("test0", "data1", "test2")

        fragment0.apply {
            Assert.assertEquals(0, view!!.getTag(R.id.aa_tag_pager_refresh_position))
            Assert.assertEquals("data0", view!!.getTag(R.id.aa_tag_pager_refresh_data))
            Assert.assertTrue(pagerAdapterRefreshHelper.isItemPositionChanged(this))
        }
        fragment1.apply {
            Assert.assertEquals(1, view!!.getTag(R.id.aa_tag_pager_refresh_position))
            Assert.assertEquals("data1", view!!.getTag(R.id.aa_tag_pager_refresh_data))
            Assert.assertFalse(pagerAdapterRefreshHelper.isItemPositionChanged(this))
        }
        fragment2.apply {
            Assert.assertEquals(2, view!!.getTag(R.id.aa_tag_pager_refresh_position))
            Assert.assertEquals("data2", view!!.getTag(R.id.aa_tag_pager_refresh_data))
            Assert.assertTrue(pagerAdapterRefreshHelper.isItemPositionChanged(this))
        }

        list = listOf()

        fragment0.apply {
            Assert.assertEquals(0, view!!.getTag(R.id.aa_tag_pager_refresh_position))
            Assert.assertEquals("data0", view!!.getTag(R.id.aa_tag_pager_refresh_data))
            Assert.assertTrue(pagerAdapterRefreshHelper.isItemPositionChanged(this))
        }
        fragment1.apply {
            Assert.assertEquals(1, view!!.getTag(R.id.aa_tag_pager_refresh_position))
            Assert.assertEquals("data1", view!!.getTag(R.id.aa_tag_pager_refresh_data))
            Assert.assertTrue(pagerAdapterRefreshHelper.isItemPositionChanged(this))
        }
        fragment2.apply {
            Assert.assertEquals(2, view!!.getTag(R.id.aa_tag_pager_refresh_position))
            Assert.assertEquals("data2", view!!.getTag(R.id.aa_tag_pager_refresh_data))
            Assert.assertTrue(pagerAdapterRefreshHelper.isItemPositionChanged(this))
        }
    }

    class ContainerFragment : Fragment() {
        override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View = FrameLayout(requireContext()).apply {
            id = android.R.id.content
        }
    }

    class TestFragment : Fragment() {
        override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View = TextView(requireContext())
    }
}