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
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.github.panpf.assemblyadapter.pager.AssemblyFragmentStatePagerAdapter
import com.github.panpf.assemblyadapter.pager.AssemblySingleDataFragmentStatePagerAdapter
import com.github.panpf.assemblyadapter.pager.ConcatFragmentStatePagerAdapter
import com.github.panpf.assemblyadapter.pager.ViewFragmentItemFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import java.util.*

@RunWith(AndroidJUnit4::class)
class ConcatFragmentStatePagerAdapterTest {

    private data class Text(val text: String)

    private class TextFragmentItemFactory : ViewFragmentItemFactory<Text>(
        Text::class, android.R.layout.activity_list_item
    ) {
        override fun createFragment(
            bindingAdapterPosition: Int,
            absoluteAdapterPosition: Int,
            data: Text
        ): Fragment {
            return super.createFragment(bindingAdapterPosition, absoluteAdapterPosition, data)
                .apply {
                    arguments = Bundle().apply {
                        putInt("bindingAdapterPosition", bindingAdapterPosition)
                        putInt("absoluteAdapterPosition", absoluteAdapterPosition)
                    }
                }
        }
    }

    @Test
    fun testNestedAdapterPosition() {
        val activity = runBlocking {
            withContext(Dispatchers.Main) {
                FragmentActivity()
            }
        }

        val count1Adapter = AssemblySingleDataFragmentStatePagerAdapter(
            activity.supportFragmentManager,
            TextFragmentItemFactory(),
            Text("a")
        )
        val count3Adapter = AssemblyFragmentStatePagerAdapter(
            activity.supportFragmentManager,
            listOf(TextFragmentItemFactory()),
            listOf(Text("a"), Text("a"), Text("a"))
        )
        val count5Adapter = AssemblyFragmentStatePagerAdapter(
            activity.supportFragmentManager,
            listOf(TextFragmentItemFactory()),
            listOf(Text("a"), Text("a"), Text("a"), Text("a"), Text("a"))
        )
        val count7Adapter = AssemblyFragmentStatePagerAdapter(
            activity.supportFragmentManager,
            listOf(TextFragmentItemFactory()),
            listOf(Text("a"), Text("a"), Text("a"), Text("a"), Text("a"), Text("a"), Text("a"))
        )
        val concatCount9Adapter = ConcatFragmentStatePagerAdapter(
            activity.supportFragmentManager, count1Adapter, count3Adapter, count5Adapter
        )
        val concatCount11Adapter = ConcatFragmentStatePagerAdapter(
            activity.supportFragmentManager, count1Adapter, count3Adapter, count7Adapter
        )
        val concatCount13Adapter = ConcatFragmentStatePagerAdapter(
            activity.supportFragmentManager, count1Adapter, count5Adapter, count7Adapter
        )
        val concatNestingCount16Adapter = ConcatFragmentStatePagerAdapter(
            activity.supportFragmentManager,
            count1Adapter,
            ConcatFragmentStatePagerAdapter(
                activity.supportFragmentManager, count3Adapter, count5Adapter
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

        val context = InstrumentationRegistry.getInstrumentation().context
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