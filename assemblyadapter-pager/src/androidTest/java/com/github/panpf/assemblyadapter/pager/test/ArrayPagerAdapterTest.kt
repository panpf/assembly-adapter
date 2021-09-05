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
package com.github.panpf.assemblyadapter.pager.test

import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.test.platform.app.InstrumentationRegistry
import com.github.panpf.assemblyadapter.pager.ArrayPagerAdapter
import com.github.panpf.tools4j.test.ktx.assertThrow
import org.junit.Assert
import org.junit.Test

class ArrayPagerAdapterTest {

    @Test
    fun testConstructor() {
        val context = InstrumentationRegistry.getInstrumentation().context
        ArrayPagerAdapter().apply {
            Assert.assertEquals(0, currentList.size)
        }
        ArrayPagerAdapter(
            listOf(TextView(context), ImageView(context))
        ).apply {
            Assert.assertEquals(2, currentList.size)
        }
    }

    @Test
    fun testPropertyCurrentListAndSubmitList() {
        val context = InstrumentationRegistry.getInstrumentation().context
        ArrayPagerAdapter().apply {
            Assert.assertEquals(0, currentList.size)

            submitList(listOf(TextView(context)))
            Assert.assertEquals(1, currentList.size)

            submitList(listOf(TextView(context), ImageView(context)))
            Assert.assertEquals(2, currentList.size)

            submitList(null)
            Assert.assertEquals(0, currentList.size)
        }
    }

    @Test
    fun testPropertyCurrentPageTitleListAndSubmitPageTitleList() {
        ArrayPagerAdapter().apply {
            Assert.assertEquals(0, currentPageTitleList.size)

            submitPageTitleList(listOf("hello"))
            Assert.assertEquals(1, currentPageTitleList.size)

            submitPageTitleList(listOf("hello", "world"))
            Assert.assertEquals(2, currentPageTitleList.size)

            submitPageTitleList(null)
            Assert.assertEquals(0, currentPageTitleList.size)
        }
    }

    @Test
    fun testMethodGetCount() {
        val context = InstrumentationRegistry.getInstrumentation().context
        ArrayPagerAdapter().apply {
            Assert.assertEquals(0, count)
            Assert.assertEquals(0, itemCount)

            submitList(listOf(TextView(context)))
            Assert.assertEquals(1, count)
            Assert.assertEquals(1, itemCount)

            submitList(listOf(TextView(context), ImageView(context)))
            Assert.assertEquals(2, count)
            Assert.assertEquals(2, itemCount)

            submitList(null)
            Assert.assertEquals(0, count)
            Assert.assertEquals(0, itemCount)
        }
    }

    @Test
    fun testMethodGetItemData() {
        val context = InstrumentationRegistry.getInstrumentation().context
        ArrayPagerAdapter().apply {
            assertThrow(IndexOutOfBoundsException::class) {
                getItemData(-1)
            }
            assertThrow(IndexOutOfBoundsException::class) {
                getItemData(0)
            }
            assertThrow(IndexOutOfBoundsException::class) {
                getItemData(1)
            }

            submitList(listOf(TextView(context), ImageView(context)))
            Assert.assertTrue(getItemData(0) is TextView)
            Assert.assertTrue(getItemData(1) is ImageView)
        }
    }

    @Test
    fun testMethodInstantiateItem() {
        val context = InstrumentationRegistry.getInstrumentation().context
        val parent = FrameLayout(context)
        ArrayPagerAdapter().apply {
            submitList(listOf(TextView(context), ImageView(context)))

            Assert.assertTrue(instantiateItem(parent, 0) is TextView)
            Assert.assertTrue(instantiateItem(parent, 1) is ImageView)
        }
    }
}