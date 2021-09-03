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

import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import androidx.test.platform.app.InstrumentationRegistry
import androidx.viewpager.widget.PagerAdapter
import com.github.panpf.assemblyadapter.pager.R
import com.github.panpf.assemblyadapter.pager.refreshable.RefreshablePagerAdapter
import com.github.panpf.tools4j.test.ktx.assertThrow
import org.junit.Assert
import org.junit.Test

class RefreshablePagerAdapterTest {

    private class TestRefreshablePagerAdapter(var list: List<String>) :
        RefreshablePagerAdapter<String>() {

        override fun getItemData(position: Int): String {
            val count = count
            if (position < 0 || position >= count) {
                throw IndexOutOfBoundsException("Index: $position, Size: $count")
            }
            return list[position]
        }

        override fun getCount(): Int = list.size

        override fun getView(container: ViewGroup, position: Int): View {
            return TextView(container.context).apply {
                text = getItemData(position)
            }
        }
    }

    @Test
    fun testPropertyIsDisableItemRefreshWhenDataSetChanged() {
        TestRefreshablePagerAdapter(listOf("hello", "world")).apply {
            Assert.assertFalse(isDisableItemRefreshWhenDataSetChanged)

            isDisableItemRefreshWhenDataSetChanged = true
            Assert.assertTrue(isDisableItemRefreshWhenDataSetChanged)

            isDisableItemRefreshWhenDataSetChanged = false
            Assert.assertFalse(isDisableItemRefreshWhenDataSetChanged)
        }
    }

    @Test
    fun testMethodInstantiateItem() {
        val context = InstrumentationRegistry.getInstrumentation().context
        val container = FrameLayout(context)

        TestRefreshablePagerAdapter(listOf("hello", "world")).apply {
            assertThrow(IndexOutOfBoundsException::class) {
                instantiateItem(container, -1)
            }
            (instantiateItem(container, 0) as TextView).apply {
                Assert.assertEquals("hello", text.toString())
                Assert.assertEquals(0, (getTag(R.id.aa_tag_pager_refresh_position) as Int))
                Assert.assertEquals("hello", (getTag(R.id.aa_tag_pager_refresh_data)))
            }
            (instantiateItem(container, 1) as TextView).apply {
                Assert.assertEquals("world", text.toString())
                Assert.assertEquals(1, (getTag(R.id.aa_tag_pager_refresh_position) as Int))
                Assert.assertEquals("world", (getTag(R.id.aa_tag_pager_refresh_data)))
            }
            assertThrow(IndexOutOfBoundsException::class) {
                instantiateItem(container, 2)
            }

            isDisableItemRefreshWhenDataSetChanged = true
            (instantiateItem(container, 0) as TextView).apply {
                Assert.assertEquals("hello", text.toString())
                Assert.assertNull(getTag(R.id.aa_tag_pager_refresh_position))
                Assert.assertNull(getTag(R.id.aa_tag_pager_refresh_data))
            }
            (instantiateItem(container, 1) as TextView).apply {
                Assert.assertEquals("world", text.toString())
                Assert.assertNull(getTag(R.id.aa_tag_pager_refresh_position))
                Assert.assertNull(getTag(R.id.aa_tag_pager_refresh_data))
            }
        }
    }

    @Test
    fun testMethodDestroyItem() {
        val context = InstrumentationRegistry.getInstrumentation().context
        val container = FrameLayout(context)
        TestRefreshablePagerAdapter(listOf("hello", "world")).apply {
            assertThrow(IndexOutOfBoundsException::class) {
                destroyItem(container, -1, "")
            }
            destroyItem(container, 0, instantiateItem(container, 0))
            destroyItem(container, 1, instantiateItem(container, 1))
            assertThrow(IndexOutOfBoundsException::class) {
                destroyItem(container, 2, "")
            }
        }
    }

    @Test
    fun testMethodIsViewFromObject() {
        val context = InstrumentationRegistry.getInstrumentation().context
        val view = TextView(context)
        TestRefreshablePagerAdapter(listOf("hello", "world")).apply {
            Assert.assertTrue(isViewFromObject(view, view))
            Assert.assertFalse(isViewFromObject(view, ""))
        }
    }

    @Test
    fun testMethodGetItemPosition() {
        val context = InstrumentationRegistry.getInstrumentation().context
        val container = FrameLayout(context)
        TestRefreshablePagerAdapter(listOf("hello", "world")).apply {
            val item0 = instantiateItem(container, 0)
            val item1 = instantiateItem(container, 1)
            Assert.assertEquals(PagerAdapter.POSITION_UNCHANGED, getItemPosition(item0))
            Assert.assertEquals(PagerAdapter.POSITION_UNCHANGED, getItemPosition(item1))

            list = listOf("good", "world")
            Assert.assertEquals(PagerAdapter.POSITION_NONE, getItemPosition(item0))
            Assert.assertEquals(PagerAdapter.POSITION_UNCHANGED, getItemPosition(item1))

            isDisableItemRefreshWhenDataSetChanged = true
            Assert.assertEquals(PagerAdapter.POSITION_UNCHANGED, getItemPosition(item0))
            Assert.assertEquals(PagerAdapter.POSITION_UNCHANGED, getItemPosition(item1))
            isDisableItemRefreshWhenDataSetChanged = false

            list = listOf("hello", "good")
            Assert.assertEquals(PagerAdapter.POSITION_UNCHANGED, getItemPosition(item0))
            Assert.assertEquals(PagerAdapter.POSITION_NONE, getItemPosition(item1))

            isDisableItemRefreshWhenDataSetChanged = true
            Assert.assertEquals(PagerAdapter.POSITION_UNCHANGED, getItemPosition(item0))
            Assert.assertEquals(PagerAdapter.POSITION_UNCHANGED, getItemPosition(item1))
            isDisableItemRefreshWhenDataSetChanged = false
        }
    }
}