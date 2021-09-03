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
package com.github.panpf.assemblyadapter.recycler.test

import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.github.panpf.assemblyadapter.ViewItemFactory
import com.github.panpf.assemblyadapter.recycler.AssemblyRecyclerAdapter
import com.github.panpf.assemblyadapter.recycler.AssemblySingleDataRecyclerAdapter
import com.github.panpf.assemblyadapter.recycler.internal.RecyclerViewHolderWrapper
import com.github.panpf.tools4j.test.ktx.assertThrow
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ConcatAdapterTest {

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
        ConcatAdapter.Config.DEFAULT.apply {
            Assert.assertEquals(true, this.isolateViewTypes)
            Assert.assertEquals(
                ConcatAdapter.Config.StableIdMode.NO_STABLE_IDS,
                this.stableIdMode
            )
        }

        ConcatAdapter.Config.Builder().build().apply {
            Assert.assertEquals(true, this.isolateViewTypes)
            Assert.assertEquals(
                ConcatAdapter.Config.StableIdMode.NO_STABLE_IDS,
                this.stableIdMode
            )
        }

        ConcatAdapter.Config.Builder().setIsolateViewTypes(false).build().apply {
            Assert.assertEquals(false, this.isolateViewTypes)
            Assert.assertEquals(
                ConcatAdapter.Config.StableIdMode.NO_STABLE_IDS,
                this.stableIdMode
            )
        }

        ConcatAdapter.Config.Builder()
            .setStableIdMode(ConcatAdapter.Config.StableIdMode.ISOLATED_STABLE_IDS).build()
            .apply {
                Assert.assertEquals(true, this.isolateViewTypes)
                Assert.assertEquals(
                    ConcatAdapter.Config.StableIdMode.ISOLATED_STABLE_IDS,
                    this.stableIdMode
                )
            }

        ConcatAdapter.Config.Builder()
            .setStableIdMode(ConcatAdapter.Config.StableIdMode.SHARED_STABLE_IDS).build()
            .apply {
                Assert.assertEquals(true, this.isolateViewTypes)
                Assert.assertEquals(
                    ConcatAdapter.Config.StableIdMode.SHARED_STABLE_IDS,
                    this.stableIdMode
                )
            }
    }

    @Test
    fun testConstructor() {
        ConcatAdapter(AssemblySingleDataRecyclerAdapter(TextItemFactory())).apply {
            Assert.assertEquals(1, adapters.size)
        }
        ConcatAdapter(
            AssemblySingleDataRecyclerAdapter(TextItemFactory()),
            AssemblySingleDataRecyclerAdapter(TextItemFactory())
        ).apply {
            Assert.assertEquals(2, adapters.size)
        }

        ConcatAdapter(
            ConcatAdapter.Config.DEFAULT,
            AssemblySingleDataRecyclerAdapter(TextItemFactory())
        ).apply {
            Assert.assertEquals(1, adapters.size)
        }
        ConcatAdapter(
            ConcatAdapter.Config.DEFAULT,
            AssemblySingleDataRecyclerAdapter(TextItemFactory()),
            AssemblySingleDataRecyclerAdapter(TextItemFactory())
        ).apply {
            Assert.assertEquals(2, adapters.size)
        }

        ConcatAdapter(listOf(AssemblySingleDataRecyclerAdapter(TextItemFactory()))).apply {
            Assert.assertEquals(1, adapters.size)
        }
        ConcatAdapter(
            listOf(
                AssemblySingleDataRecyclerAdapter(TextItemFactory()),
                AssemblySingleDataRecyclerAdapter(TextItemFactory())
            )
        ).apply {
            Assert.assertEquals(2, adapters.size)
        }

        ConcatAdapter(
            ConcatAdapter.Config.DEFAULT,
            listOf(AssemblySingleDataRecyclerAdapter(TextItemFactory()))
        ).apply {
            Assert.assertEquals(1, adapters.size)
        }
        ConcatAdapter(
            ConcatAdapter.Config.DEFAULT,
            listOf(
                AssemblySingleDataRecyclerAdapter(TextItemFactory()),
                AssemblySingleDataRecyclerAdapter(TextItemFactory())
            )
        ).apply {
            Assert.assertEquals(2, adapters.size)
        }
    }

    @Test
    fun testMethodAddAndRemoveAdapter() {
        ConcatAdapter().apply {
            Assert.assertEquals(0, adapters.size)
            Assert.assertEquals(0, itemCount)
            Assert.assertEquals("", adapters.joinToString { it.itemCount.toString() })

            val adapter1 = AssemblySingleDataRecyclerAdapter(TextItemFactory(), Text("a"))
            val adapter2 =
                AssemblyRecyclerAdapter(listOf(TextItemFactory()), listOf(Text("b"), Text("c")))
            val adapter3 =
                AssemblyRecyclerAdapter(
                    listOf(TextItemFactory()),
                    listOf(Text("d"), Text("e"), Text("f"))
                )

            addAdapter(adapter1)
            Assert.assertEquals(1, adapters.size)
            Assert.assertEquals(1, itemCount)
            Assert.assertEquals("1", adapters.joinToString { it.itemCount.toString() })

            addAdapter(adapter3)
            Assert.assertEquals(2, adapters.size)
            Assert.assertEquals(4, itemCount)
            Assert.assertEquals("1, 3", adapters.joinToString { it.itemCount.toString() })

            addAdapter(1, adapter2)
            Assert.assertEquals(3, adapters.size)
            Assert.assertEquals(6, itemCount)
            Assert.assertEquals("1, 2, 3", adapters.joinToString { it.itemCount.toString() })

            removeAdapter(adapter1)
            Assert.assertEquals(2, adapters.size)
            Assert.assertEquals(5, itemCount)
            Assert.assertEquals("2, 3", adapters.joinToString { it.itemCount.toString() })

            removeAdapter(adapter3)
            Assert.assertEquals(1, adapters.size)
            Assert.assertEquals(2, itemCount)
            Assert.assertEquals("2", adapters.joinToString { it.itemCount.toString() })
        }
    }

    @Test
    fun testMethodGetItemViewType() {
        // IsolateViewTypes true
        ConcatAdapter(
            AssemblySingleDataRecyclerAdapter(TextItemFactory(), Text("a")),
            AssemblyRecyclerAdapter(
                listOf(TextItemFactory(), ImageItemFactory()),
                listOf(
                    Image(android.R.drawable.alert_dark_frame),
                    Text("c"),
                    Image(android.R.drawable.arrow_up_float)
                )
            ),
            AssemblySingleDataRecyclerAdapter(
                ImageItemFactory(),
                Image(android.R.drawable.btn_plus)
            ),
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
        ConcatAdapter(
            ConcatAdapter.Config.Builder()
                .setIsolateViewTypes(false)
                .build(),
            AssemblySingleDataRecyclerAdapter(TextItemFactory(), Text("a")),
            AssemblyRecyclerAdapter(
                listOf(TextItemFactory(), ImageItemFactory()),
                listOf(
                    Image(android.R.drawable.alert_dark_frame),
                    Text("c"),
                    Image(android.R.drawable.arrow_up_float)
                )
            ),
            AssemblySingleDataRecyclerAdapter(
                ImageItemFactory(),
                Image(android.R.drawable.btn_plus)
            ),
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
        ConcatAdapter(
            AssemblySingleDataRecyclerAdapter(
                itemFactory = TextItemFactory(),
                initData = data0
            ),
            AssemblyRecyclerAdapter<Any>(
                itemFactoryList = listOf(TextItemFactory()),
                initDataList = listOf(data2, data1, data2)
            ),
            AssemblySingleDataRecyclerAdapter(
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
        ConcatAdapter(
            ConcatAdapter.Config.Builder()
                .setStableIdMode(ConcatAdapter.Config.StableIdMode.ISOLATED_STABLE_IDS)
                .build(),
            AssemblySingleDataRecyclerAdapter(
                itemFactory = TextItemFactory(),
                initData = data0,
            ).apply { setHasStableIds(true) },
            AssemblyRecyclerAdapter<Any>(
                itemFactoryList = listOf(TextItemFactory()),
                initDataList = listOf(data2, data1, data2),
            ).apply { setHasStableIds(true) },
            AssemblySingleDataRecyclerAdapter(
                itemFactory = TextItemFactory(),
                initData = data1,
            ).apply { setHasStableIds(true) }
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
        ConcatAdapter(
            ConcatAdapter.Config.Builder()
                .setStableIdMode(ConcatAdapter.Config.StableIdMode.SHARED_STABLE_IDS)
                .build(),
            AssemblySingleDataRecyclerAdapter(
                itemFactory = TextItemFactory(),
                initData = data0,
            ).apply { setHasStableIds(true) },
            AssemblyRecyclerAdapter<Any>(
                itemFactoryList = listOf(TextItemFactory()),
                initDataList = listOf(data2, data1, data2),
            ).apply { setHasStableIds(true) },
            AssemblySingleDataRecyclerAdapter(
                itemFactory = TextItemFactory(),
                initData = data1,
            ).apply { setHasStableIds(true) },
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
    fun testMethodCreateAndBindViewHolder() {
        val context = InstrumentationRegistry.getInstrumentation().context
        val parent = FrameLayout(context)
        ConcatAdapter(
            AssemblySingleDataRecyclerAdapter(
                itemFactory = TextItemFactory(),
                initData = Text("a"),
            ),
            AssemblyRecyclerAdapter(
                itemFactoryList = listOf(TextItemFactory(), ImageItemFactory()),
                initDataList = listOf(
                    Image(android.R.drawable.bottom_bar),
                    Text("b"),
                    Image(android.R.drawable.btn_plus)
                ),
            ),
            AssemblySingleDataRecyclerAdapter(
                itemFactory = ImageItemFactory(),
                initData = Image(android.R.drawable.alert_dark_frame),
            ),
        ).apply {
            assertThrow(IllegalArgumentException::class) {
                onCreateViewHolder(parent, -1)
            }
            Assert.assertTrue(onCreateViewHolder(parent, getItemViewType(0)).itemView is TextView)
            Assert.assertTrue(onCreateViewHolder(parent, getItemViewType(1)).itemView is ImageView)
            Assert.assertTrue(onCreateViewHolder(parent, getItemViewType(2)).itemView is TextView)
            Assert.assertTrue(onCreateViewHolder(parent, getItemViewType(3)).itemView is ImageView)
            Assert.assertTrue(onCreateViewHolder(parent, getItemViewType(4)).itemView is ImageView)

            assertThrow(IllegalArgumentException::class) {
                onBindViewHolder(object : RecyclerView.ViewHolder(TextView(context)) {}, 0)
            }
            onBindViewHolder(onCreateViewHolder(parent, getItemViewType(0)), 0)
            onBindViewHolder(onCreateViewHolder(parent, getItemViewType(1)), 1)
            onBindViewHolder(onCreateViewHolder(parent, getItemViewType(2)), 2)
            onBindViewHolder(onCreateViewHolder(parent, getItemViewType(3)), 3)
            onBindViewHolder(onCreateViewHolder(parent, getItemViewType(4)), 4)
        }
    }

    @Test
    fun testMethodGetCount() {
        val headerAdapter = AssemblySingleDataRecyclerAdapter(itemFactory = TextItemFactory())
        val bodyAdapter =
            AssemblyRecyclerAdapter<Any>(listOf(TextItemFactory(), ImageItemFactory()))
        val footerHeader = AssemblySingleDataRecyclerAdapter(itemFactory = ImageItemFactory())
        ConcatAdapter(headerAdapter, bodyAdapter, footerHeader).apply {
            Assert.assertEquals(0, itemCount)

            headerAdapter.data = Text("hello")
            Assert.assertEquals(1, itemCount)

            bodyAdapter.submitList(
                listOf(
                    Image(android.R.drawable.bottom_bar),
                    Text("world"),
                    Image(android.R.drawable.btn_plus)
                )
            )
            Assert.assertEquals(4, itemCount)

            footerHeader.data = Image(android.R.drawable.btn_default)
            Assert.assertEquals(5, itemCount)

            bodyAdapter.submitList(listOf(Text("world")))
            Assert.assertEquals(3, itemCount)

            bodyAdapter.submitList(null)
            Assert.assertEquals(2, itemCount)

            footerHeader.data = null
            Assert.assertEquals(1, itemCount)

            headerAdapter.data = null
            Assert.assertEquals(0, itemCount)
        }
    }

    @Test
    fun testMethodHasStableIds() {
        ConcatAdapter(AssemblySingleDataRecyclerAdapter(TextItemFactory())).apply {
            Assert.assertFalse(hasStableIds())
        }

        ConcatAdapter(
            ConcatAdapter.Config.Builder()
                .setStableIdMode(ConcatAdapter.Config.StableIdMode.SHARED_STABLE_IDS)
                .build(),
            AssemblySingleDataRecyclerAdapter(
                itemFactory = TextItemFactory(),
            ).apply { setHasStableIds(true) }
        ).apply {
            Assert.assertTrue(hasStableIds())
        }

        ConcatAdapter(
            ConcatAdapter.Config.Builder()
                .setStableIdMode(ConcatAdapter.Config.StableIdMode.ISOLATED_STABLE_IDS)
                .build(),
            AssemblySingleDataRecyclerAdapter(
                itemFactory = TextItemFactory(),
            ).apply { setHasStableIds(true) }
        ).apply {
            Assert.assertTrue(hasStableIds())
        }
    }

    @Test
    fun testNestedAdapterPosition() {
        val count1Adapter = AssemblySingleDataRecyclerAdapter(TextItemFactory(), Text("a"))
        val count3Adapter = AssemblyRecyclerAdapter(
            listOf(TextItemFactory()),
            listOf(Text("a"), Text("a"), Text("a"))
        )
        val count5Adapter = AssemblyRecyclerAdapter(
            listOf(TextItemFactory()),
            listOf(Text("a"), Text("a"), Text("a"), Text("a"), Text("a"))
        )
        val count7Adapter = AssemblyRecyclerAdapter(
            listOf(TextItemFactory()),
            listOf(Text("a"), Text("a"), Text("a"), Text("a"), Text("a"), Text("a"), Text("a"))
        )
        val concatCount9Adapter = ConcatAdapter(count1Adapter, count3Adapter, count5Adapter)
        val concatCount11Adapter = ConcatAdapter(count1Adapter, count3Adapter, count7Adapter)
        val concatCount13Adapter = ConcatAdapter(count1Adapter, count5Adapter, count7Adapter)
        val concatNestingCount16Adapter = ConcatAdapter(
            count1Adapter, ConcatAdapter(count3Adapter, count5Adapter), count7Adapter
        )

        Assert.assertEquals("count1Adapter.itemCount", 1, count1Adapter.itemCount)
        Assert.assertEquals("count3Adapter.itemCount", 3, count3Adapter.itemCount)
        Assert.assertEquals("count5Adapter.itemCount", 5, count5Adapter.itemCount)
        Assert.assertEquals("count7Adapter.itemCount", 7, count7Adapter.itemCount)
        Assert.assertEquals("count7Adapter.itemCount", 9, concatCount9Adapter.itemCount)
        Assert.assertEquals("count7Adapter.itemCount", 11, concatCount11Adapter.itemCount)
        Assert.assertEquals("count12Adapter.itemCount", 13, concatCount13Adapter.itemCount)
        Assert.assertEquals("count15Adapter.itemCount", 16, concatNestingCount16Adapter.itemCount)

        val context = InstrumentationRegistry.getInstrumentation().context
        val parent = RecyclerView(context).apply {
            layoutManager = LinearLayoutManager(context)
        }
        val verifyAdapterPosition: (RecyclerView.Adapter<RecyclerView.ViewHolder>, Int, Int, Int) -> Unit =
            { adapter, position, expectedBindingAdapterPosition, expectedAbsoluteAdapterPosition ->
                val itemType = adapter.getItemViewType(position)
                val viewHolder = adapter.createViewHolder(parent, itemType)
                adapter.bindViewHolder(viewHolder, position)
                val item = (viewHolder as RecyclerViewHolderWrapper<*>).wrappedItem
                Assert.assertEquals(
                    "count${adapter.itemCount}Adapter. position(${position}). item.bindingAdapterPosition",
                    expectedBindingAdapterPosition, item.bindingAdapterPosition
                )
                Assert.assertEquals(
                    "count${adapter.itemCount}Adapter. position(${position}). item.absoluteAdapterPosition",
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