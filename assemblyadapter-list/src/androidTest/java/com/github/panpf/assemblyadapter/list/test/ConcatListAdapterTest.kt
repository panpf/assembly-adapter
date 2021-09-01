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
package com.github.panpf.assemblyadapter.list.test

import android.widget.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.github.panpf.assemblyadapter.Item
import com.github.panpf.assemblyadapter.ViewItemFactory
import com.github.panpf.assemblyadapter.list.AssemblyListAdapter
import com.github.panpf.assemblyadapter.list.AssemblySingleDataListAdapter
import com.github.panpf.assemblyadapter.list.ConcatListAdapter
import com.github.panpf.tools4j.test.ktx.assertThrow
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ConcatListAdapterTest {

    private data class Text(val text: String)

    private class TextItemFactory : ViewItemFactory<Text>(Text::class, { context, _, _ ->
        TextView(context)
    })

    private data class Image(val resId: Int)

    private class ImageItemFactory : ViewItemFactory<Image>(Image::class, { context, _, _ ->
        ImageView(context)
    })

    @Test
    fun testConfig() {
        ConcatListAdapter.Config.DEFAULT.apply {
            Assert.assertEquals(true, this.isolateViewTypes)
            Assert.assertEquals(
                ConcatListAdapter.Config.StableIdMode.NO_STABLE_IDS,
                this.stableIdMode
            )
        }

        ConcatListAdapter.Config.Builder().build().apply {
            Assert.assertEquals(true, this.isolateViewTypes)
            Assert.assertEquals(
                ConcatListAdapter.Config.StableIdMode.NO_STABLE_IDS,
                this.stableIdMode
            )
        }

        ConcatListAdapter.Config.Builder().setIsolateViewTypes(false).build().apply {
            Assert.assertEquals(false, this.isolateViewTypes)
            Assert.assertEquals(
                ConcatListAdapter.Config.StableIdMode.NO_STABLE_IDS,
                this.stableIdMode
            )
        }

        ConcatListAdapter.Config.Builder()
            .setStableIdMode(ConcatListAdapter.Config.StableIdMode.ISOLATED_STABLE_IDS).build()
            .apply {
                Assert.assertEquals(true, this.isolateViewTypes)
                Assert.assertEquals(
                    ConcatListAdapter.Config.StableIdMode.ISOLATED_STABLE_IDS,
                    this.stableIdMode
                )
            }

        ConcatListAdapter.Config.Builder()
            .setStableIdMode(ConcatListAdapter.Config.StableIdMode.SHARED_STABLE_IDS).build()
            .apply {
                Assert.assertEquals(true, this.isolateViewTypes)
                Assert.assertEquals(
                    ConcatListAdapter.Config.StableIdMode.SHARED_STABLE_IDS,
                    this.stableIdMode
                )
            }
    }

    @Test
    fun testConstructor() {
        ConcatListAdapter(AssemblySingleDataListAdapter(TextItemFactory())).apply {
            Assert.assertEquals(1, adapters.size)
        }
        ConcatListAdapter(
            AssemblySingleDataListAdapter(TextItemFactory()),
            AssemblySingleDataListAdapter(TextItemFactory())
        ).apply {
            Assert.assertEquals(2, adapters.size)
        }

        ConcatListAdapter(
            ConcatListAdapter.Config.DEFAULT,
            AssemblySingleDataListAdapter(TextItemFactory())
        ).apply {
            Assert.assertEquals(1, adapters.size)
        }
        ConcatListAdapter(
            ConcatListAdapter.Config.DEFAULT,
            AssemblySingleDataListAdapter(TextItemFactory()),
            AssemblySingleDataListAdapter(TextItemFactory())
        ).apply {
            Assert.assertEquals(2, adapters.size)
        }

        ConcatListAdapter(listOf(AssemblySingleDataListAdapter(TextItemFactory()))).apply {
            Assert.assertEquals(1, adapters.size)
        }
        ConcatListAdapter(
            listOf(
                AssemblySingleDataListAdapter(TextItemFactory()),
                AssemblySingleDataListAdapter(TextItemFactory())
            )
        ).apply {
            Assert.assertEquals(2, adapters.size)
        }

        ConcatListAdapter(
            ConcatListAdapter.Config.DEFAULT,
            listOf(AssemblySingleDataListAdapter(TextItemFactory()))
        ).apply {
            Assert.assertEquals(1, adapters.size)
        }
        ConcatListAdapter(
            ConcatListAdapter.Config.DEFAULT,
            listOf(
                AssemblySingleDataListAdapter(TextItemFactory()),
                AssemblySingleDataListAdapter(TextItemFactory())
            )
        ).apply {
            Assert.assertEquals(2, adapters.size)
        }
    }

    @Test
    fun testMethodAddAndRemoveAdapter() {
        ConcatListAdapter().apply {
            Assert.assertEquals(0, adapters.size)
            Assert.assertEquals(0, count)
            Assert.assertEquals("", adapters.joinToString { it.count.toString() })

            val adapter1 = AssemblySingleDataListAdapter(TextItemFactory(), Text("a"))
            val adapter2 =
                AssemblyListAdapter(listOf(TextItemFactory()), listOf(Text("b"), Text("c")))
            val adapter3 =
                AssemblyListAdapter(
                    listOf(TextItemFactory()),
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
    fun testMethodGetViewTypeCount() {
        // IsolateViewTypes true
        ConcatListAdapter().apply {
            Assert.assertEquals(0, viewTypeCount)

            addAdapter(AssemblySingleDataListAdapter(TextItemFactory()))
            Assert.assertEquals(1, viewTypeCount)

            addAdapter(AssemblySingleDataListAdapter(TextItemFactory()))
            Assert.assertEquals(2, viewTypeCount)

            addAdapter(AssemblyListAdapter<Any>(listOf(TextItemFactory(), TextItemFactory())))
            Assert.assertEquals(4, viewTypeCount)
        }

        // IsolateViewTypes false
        ConcatListAdapter(
            ConcatListAdapter.Config.Builder()
                .setIsolateViewTypes(false)
                .build()
        ).apply {
            Assert.assertEquals(0, viewTypeCount)

            addAdapter(AssemblySingleDataListAdapter(TextItemFactory()))
            Assert.assertEquals(1, viewTypeCount)

            addAdapter(AssemblySingleDataListAdapter(TextItemFactory()))
            Assert.assertEquals(2, viewTypeCount)

            addAdapter(AssemblyListAdapter<Any>(listOf(TextItemFactory(), TextItemFactory())))
            Assert.assertEquals(4, viewTypeCount)
        }
    }

    @Test
    fun testMethodGetItemViewType() {
        // IsolateViewTypes true
        ConcatListAdapter(
            AssemblySingleDataListAdapter(TextItemFactory(), Text("a")),
            AssemblyListAdapter(
                listOf(TextItemFactory(), ImageItemFactory()),
                listOf(
                    Image(android.R.drawable.alert_dark_frame),
                    Text("c"),
                    Image(android.R.drawable.arrow_up_float)
                )
            ),
            AssemblySingleDataListAdapter(ImageItemFactory(), Image(android.R.drawable.btn_plus)),
        ).apply {
            assertThrow(IndexOutOfBoundsException::class) {
                getItemViewType(-1)
            }
            Assert.assertEquals(0, getItemViewType(0))
            Assert.assertEquals(1, getItemViewType(1))
            Assert.assertEquals(2, getItemViewType(2))
            Assert.assertEquals(1, getItemViewType(3))
            Assert.assertEquals(3, getItemViewType(4))
            assertThrow(IllegalArgumentException::class) {
                getItemViewType(5)
            }
        }

        // IsolateViewTypes false
        ConcatListAdapter(
            ConcatListAdapter.Config.Builder()
                .setIsolateViewTypes(false)
                .build(),
            AssemblySingleDataListAdapter(TextItemFactory(), Text("a")),
            AssemblyListAdapter(
                listOf(TextItemFactory(), ImageItemFactory()),
                listOf(
                    Image(android.R.drawable.alert_dark_frame),
                    Text("c"),
                    Image(android.R.drawable.arrow_up_float)
                )
            ),
            AssemblySingleDataListAdapter(ImageItemFactory(), Image(android.R.drawable.btn_plus)),
        ).apply {
            assertThrow(IndexOutOfBoundsException::class) {
                getItemViewType(-1)
            }
            Assert.assertEquals(0, getItemViewType(0))
            Assert.assertEquals(1, getItemViewType(1))
            Assert.assertEquals(0, getItemViewType(2))
            Assert.assertEquals(1, getItemViewType(3))
            Assert.assertEquals(0, getItemViewType(4))
            assertThrow(IllegalArgumentException::class) {
                getItemViewType(5)
            }
        }
    }

    @Test
    fun testMethodGetItemId() {
        val currentTimeMillis = System.currentTimeMillis()
        val data0 = Text(currentTimeMillis.toString())
        val data1 = Text((currentTimeMillis + 1).toString())
        val data2 = Text((currentTimeMillis + 2).toString())
        val data0ItemId = data0.hashCode().toLong()
        val data1ItemId = data1.hashCode().toLong()
        val data2ItemId = data2.hashCode().toLong()

        // NO_STABLE_IDS
        ConcatListAdapter(
            AssemblySingleDataListAdapter(
                itemFactory = TextItemFactory(),
                initData = data0
            ),
            AssemblyListAdapter<Any>(
                itemFactoryList = listOf(TextItemFactory()),
                initDataList = listOf(data2, data1, data2)
            ),
            AssemblySingleDataListAdapter(
                itemFactory = TextItemFactory(),
                initData = data1
            ),
        ).apply {
            Assert.assertEquals(-1L, getItemId(-1))
            Assert.assertEquals(-1L, getItemId(0))
            Assert.assertEquals(-1L, getItemId(1))
            Assert.assertEquals(-1L, getItemId(2))
            Assert.assertEquals(-1L, getItemId(3))
            Assert.assertEquals(-1L, getItemId(4))
            assertThrow(IllegalArgumentException::class) {
                getItemId(5)
            }
        }

        // ISOLATED_STABLE_IDS
        ConcatListAdapter(
            ConcatListAdapter.Config.Builder()
                .setStableIdMode(ConcatListAdapter.Config.StableIdMode.ISOLATED_STABLE_IDS)
                .build(),
            AssemblySingleDataListAdapter(
                itemFactory = TextItemFactory(),
                initData = data0,
                hasStableIds = true
            ),
            AssemblyListAdapter<Any>(
                itemFactoryList = listOf(TextItemFactory()),
                initDataList = listOf(data2, data1, data2),
                hasStableIds = true
            ),
            AssemblySingleDataListAdapter(
                itemFactory = TextItemFactory(),
                initData = data1,
                hasStableIds = true
            )
        ).apply {
            assertThrow(IndexOutOfBoundsException::class) {
                getItemId(-1)
            }
            Assert.assertEquals(0L, getItemId(0))
            Assert.assertEquals(1L, getItemId(1))
            Assert.assertEquals(2L, getItemId(2))
            Assert.assertEquals(1L, getItemId(3))
            Assert.assertEquals(3L, getItemId(4))
            assertThrow(IllegalArgumentException::class) {
                getItemId(5)
            }
        }

        // SHARED_STABLE_IDS
        ConcatListAdapter(
            ConcatListAdapter.Config.Builder()
                .setStableIdMode(ConcatListAdapter.Config.StableIdMode.SHARED_STABLE_IDS)
                .build(),
            AssemblySingleDataListAdapter(
                itemFactory = TextItemFactory(),
                initData = data0,
                hasStableIds = true
            ),
            AssemblyListAdapter<Any>(
                itemFactoryList = listOf(TextItemFactory()),
                initDataList = listOf(data2, data1, data2),
                hasStableIds = true
            ),
            AssemblySingleDataListAdapter(
                itemFactory = TextItemFactory(),
                initData = data1,
                hasStableIds = true
            ),
        ).apply {
            assertThrow(IndexOutOfBoundsException::class) {
                getItemId(-1)
            }
            Assert.assertEquals(data0ItemId, getItemId(0))
            Assert.assertEquals(data2ItemId, getItemId(1))
            Assert.assertEquals(data1ItemId, getItemId(2))
            Assert.assertEquals(data2ItemId, getItemId(3))
            Assert.assertEquals(data1ItemId, getItemId(4))
            assertThrow(IllegalArgumentException::class) {
                getItemId(5)
            }
        }
    }

    @Test
    fun testMethodGetView() {
        val context = InstrumentationRegistry.getInstrumentation().context
        val parent = FrameLayout(context)
        ConcatListAdapter(
            ConcatListAdapter.Config.Builder()
                .setStableIdMode(ConcatListAdapter.Config.StableIdMode.SHARED_STABLE_IDS)
                .build(),
            AssemblySingleDataListAdapter(
                itemFactory = TextItemFactory(),
                initData = Text("a"),
                hasStableIds = true
            ),
            AssemblyListAdapter(
                itemFactoryList = listOf(TextItemFactory(), ImageItemFactory()),
                initDataList = listOf(
                    Image(android.R.drawable.bottom_bar),
                    Text("b"),
                    Image(android.R.drawable.btn_plus)
                ),
                hasStableIds = true
            ),
            AssemblySingleDataListAdapter(
                itemFactory = ImageItemFactory(),
                initData = Image(android.R.drawable.alert_dark_frame),
                hasStableIds = true
            ),
        ).apply {
            assertThrow(IndexOutOfBoundsException::class) {
                getView(-1, null, parent)
            }
            Assert.assertTrue(getView(0, null, parent) is TextView)
            Assert.assertTrue(getView(1, null, parent) is ImageView)
            Assert.assertTrue(getView(2, null, parent) is TextView)
            Assert.assertTrue(getView(3, null, parent) is ImageView)
            Assert.assertTrue(getView(4, null, parent) is ImageView)
            assertThrow(IllegalArgumentException::class) {
                getView(5, null, parent)
            }

            val itemView = getView(0, null, parent)
            Assert.assertNotSame(itemView, getView(0, null, parent))
            Assert.assertSame(itemView, getView(0, itemView, parent))
        }
    }

    @Test
    fun testMethodGetCount() {
        val headerAdapter = AssemblySingleDataListAdapter(itemFactory = TextItemFactory())
        val bodyAdapter = AssemblyListAdapter<Any>(listOf(TextItemFactory(), ImageItemFactory()))
        val footerHeader = AssemblySingleDataListAdapter(itemFactory = ImageItemFactory())
        ConcatListAdapter(headerAdapter, bodyAdapter, footerHeader).apply {
            Assert.assertEquals(0, count)

            headerAdapter.data = Text("hello")
            Assert.assertEquals(1, count)

            bodyAdapter.submitList(
                listOf(
                    Image(android.R.drawable.bottom_bar),
                    Text("world"),
                    Image(android.R.drawable.btn_plus)
                )
            )
            Assert.assertEquals(4, count)

            footerHeader.data = Image(android.R.drawable.btn_default)
            Assert.assertEquals(5, count)

            bodyAdapter.submitList(listOf(Text("world")))
            Assert.assertEquals(3, count)

            bodyAdapter.submitList(null)
            Assert.assertEquals(2, count)

            footerHeader.data = null
            Assert.assertEquals(1, count)

            headerAdapter.data = null
            Assert.assertEquals(0, count)
        }
    }

    @Test
    fun testMethodGetItem() {
        ConcatListAdapter(
            AssemblySingleDataListAdapter(
                itemFactory = TextItemFactory(),
                initData = Text("hello"),
            ),
            AssemblyListAdapter(
                itemFactoryList = listOf(TextItemFactory(), ImageItemFactory()),
                initDataList = listOf(
                    Image(android.R.drawable.bottom_bar),
                    Text("world"),
                    Image(android.R.drawable.btn_plus)
                ),
            ),
            AssemblySingleDataListAdapter(
                itemFactory = ImageItemFactory(),
                initData = Image(android.R.drawable.alert_dark_frame),
            ),
        ).apply {
            assertThrow(IndexOutOfBoundsException::class) {
                getItem(-1)
            }
            Assert.assertEquals(Text("hello"), getItem(0))
            Assert.assertEquals(Image(android.R.drawable.bottom_bar), getItem(1))
            Assert.assertEquals(Text("world"), getItem(2))
            Assert.assertEquals(Image(android.R.drawable.btn_plus), getItem(3))
            Assert.assertEquals(Image(android.R.drawable.alert_dark_frame), getItem(4))
            assertThrow(IllegalArgumentException::class) {
                getItem(5)
            }
        }
    }

    @Test
    fun testMethodHasStableIds() {
        ConcatListAdapter(AssemblySingleDataListAdapter(TextItemFactory())).apply {
            Assert.assertFalse(hasStableIds())
        }

        ConcatListAdapter(
            ConcatListAdapter.Config.Builder()
                .setStableIdMode(ConcatListAdapter.Config.StableIdMode.SHARED_STABLE_IDS)
                .build(),
            AssemblySingleDataListAdapter(
                itemFactory = TextItemFactory(),
                hasStableIds = true
            )
        ).apply {
            Assert.assertTrue(hasStableIds())
        }

        ConcatListAdapter(
            ConcatListAdapter.Config.Builder()
                .setStableIdMode(ConcatListAdapter.Config.StableIdMode.ISOLATED_STABLE_IDS)
                .build(),
            AssemblySingleDataListAdapter(
                itemFactory = TextItemFactory(),
                hasStableIds = true
            )
        ).apply {
            Assert.assertTrue(hasStableIds())
        }
    }

    @Test
    fun testMethodFindLocalAdapterAndPosition() {
        val headerAdapter = AssemblySingleDataListAdapter(
            itemFactory = TextItemFactory(),
            initData = Text("hello"),
        )
        val bodyAdapter = AssemblyListAdapter(
            itemFactoryList = listOf(TextItemFactory(), ImageItemFactory()),
            initDataList = listOf(
                Image(android.R.drawable.bottom_bar),
                Text("world"),
                Image(android.R.drawable.btn_plus)
            ),
        )
        val footerAdapter = AssemblySingleDataListAdapter(
            itemFactory = ImageItemFactory(),
            initData = Image(android.R.drawable.alert_dark_frame),
        )
        ConcatListAdapter(
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
    fun testNestedAdapterPosition() {
        val count1Adapter = AssemblySingleDataListAdapter(TextItemFactory(), Text("a"))
        val count3Adapter = AssemblyListAdapter(
            listOf(TextItemFactory()),
            listOf(Text("a"), Text("a"), Text("a"))
        )
        val count5Adapter = AssemblyListAdapter(
            listOf(TextItemFactory()),
            listOf(Text("a"), Text("a"), Text("a"), Text("a"), Text("a"))
        )
        val count7Adapter = AssemblyListAdapter(
            listOf(TextItemFactory()),
            listOf(Text("a"), Text("a"), Text("a"), Text("a"), Text("a"), Text("a"), Text("a"))
        )
        val concatCount9Adapter = ConcatListAdapter(count1Adapter, count3Adapter, count5Adapter)
        val concatCount11Adapter = ConcatListAdapter(count1Adapter, count3Adapter, count7Adapter)
        val concatCount13Adapter = ConcatListAdapter(count1Adapter, count5Adapter, count7Adapter)
        val concatNestingCount16Adapter = ConcatListAdapter(
            count1Adapter, ConcatListAdapter(count3Adapter, count5Adapter), count7Adapter
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
        val parent = ListView(context)
        val verifyAdapterPosition: (BaseAdapter, Int, Int, Int) -> Unit =
            { adapter, position, expectedBindingAdapterPosition, expectedAbsoluteAdapterPosition ->
                val itemView = adapter.getView(position, null, parent)
                val item = itemView.getTag(R.id.aa_tag_item) as Item<*>
                Assert.assertEquals(
                    "count${adapter.count}Adapter. position(${position}). item.bindingAdapterPosition",
                    expectedBindingAdapterPosition, item.bindingAdapterPosition
                )
                Assert.assertEquals(
                    "count${adapter.count}Adapter. position(${position}). item.absoluteAdapterPosition",
                    expectedAbsoluteAdapterPosition, item.absoluteAdapterPosition
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