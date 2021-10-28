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

import android.widget.ImageView
import android.widget.TextView
import androidx.collection.SparseArrayCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.test.platform.app.InstrumentationRegistry
import com.github.panpf.assemblyadapter.BindingItemFactory
import com.github.panpf.assemblyadapter.ItemFactory
import com.github.panpf.assemblyadapter.ViewItemFactory
import com.github.panpf.assemblyadapter.recycler.*
import com.github.panpf.tools4j.reflect.ktx.getFieldValue
import com.github.panpf.tools4k.lang.asOrThrow
import org.junit.Assert
import org.junit.Test

class AssemblyGridLayoutManagerTest {

    private data class Text(val text: String)

    private class TextItemFactory : ViewItemFactory<Text>(Text::class, { context, _, _ ->
        TextView(context)
    })

    private data class Image(val resId: Int)

    private class ImageItemFactory : ViewItemFactory<Image>(Image::class, { context, _, _ ->
        ImageView(context)
    })

    @Test
    fun testConstructor() {
        val context = InstrumentationRegistry.getInstrumentation().context
        AssemblyGridLayoutManager(
            context = context,
            attrs = null,
            defStyleAttr = 0,
            defStyleRes = 0,
            itemSpanByPositionMap = null,
            itemSpanByItemTypeMap = null,
            itemSpanByItemFactoryMap = null
        ).apply {
            Assert.assertEquals(1, spanCount)
            Assert.assertEquals(RecyclerView.VERTICAL, orientation)
            Assert.assertEquals(false, reverseLayout)
            Assert.assertNull(getFieldValue("itemSpanByPositionSparseArray"))
            Assert.assertNull(getFieldValue("itemSpanByItemTypeSparseArray"))
            Assert.assertNull(getFieldValue("itemSpanByItemFactoryMap"))
        }
        AssemblyGridLayoutManager(
            context = context,
            attrs = null,
            defStyleAttr = 0,
            defStyleRes = 0,
            itemSpanByPositionMap = mapOf(3 to ItemSpan.span(2)),
            itemSpanByItemTypeMap = mapOf(1 to ItemSpan.span(4)),
            itemSpanByItemFactoryMap = mapOf(TextItemFactory::class to ItemSpan.span(3))
        ).apply {
            Assert.assertEquals(
                "{3=ItemSpan(size=2)}",
                getFieldValue<SparseArrayCompat<ItemSpan>>("itemSpanByPositionSparseArray").toString()
            )
            Assert.assertEquals(
                "{1=ItemSpan(size=4)}",
                getFieldValue<SparseArrayCompat<ItemSpan>>("itemSpanByItemTypeSparseArray").toString()
            )
            Assert.assertEquals(
                "{${TextItemFactory::class.java}=ItemSpan(size=3)}",
                getFieldValue<Map<Class<out ItemFactory<out Any>>, ItemSpan>>("itemSpanByItemFactoryMap").toString()
            )
        }

        AssemblyGridLayoutManager(
            context = context,
            attrs = null,
            defStyleAttr = 0,
            defStyleRes = 0,
            gridLayoutItemSpanMap = mapOf()
        ).apply {
            Assert.assertEquals(1, spanCount)
            Assert.assertEquals(RecyclerView.VERTICAL, orientation)
            Assert.assertEquals(false, reverseLayout)
            Assert.assertNull(getFieldValue("itemSpanByPositionSparseArray"))
            Assert.assertNull(getFieldValue("itemSpanByItemTypeSparseArray"))
            Assert.assertNull(getFieldValue("itemSpanByItemFactoryMap"))
        }
        AssemblyGridLayoutManager(
            context = context,
            attrs = null,
            defStyleAttr = 0,
            defStyleRes = 0,
            gridLayoutItemSpanMap = mapOf(TextItemFactory::class to ItemSpan.span(3))
        ).apply {
            Assert.assertNull(getFieldValue("itemSpanByPositionSparseArray"))
            Assert.assertNull(getFieldValue("itemSpanByItemTypeSparseArray"))
            Assert.assertEquals(
                "{${TextItemFactory::class.java}=ItemSpan(size=3)}",
                getFieldValue<Map<Class<out ItemFactory<out Any>>, ItemSpan>>("itemSpanByItemFactoryMap").toString()
            )
        }

        AssemblyGridLayoutManager(
            context = context,
            spanCount = 3,
            orientation = RecyclerView.HORIZONTAL,
            reverseLayout = true,
            itemSpanByPositionMap = null,
            itemSpanByItemTypeMap = null,
            itemSpanByItemFactoryMap = null
        ).apply {
            Assert.assertEquals(3, spanCount)
            Assert.assertEquals(RecyclerView.HORIZONTAL, orientation)
            Assert.assertEquals(true, reverseLayout)
            Assert.assertNull(getFieldValue("itemSpanByPositionSparseArray"))
            Assert.assertNull(getFieldValue("itemSpanByItemTypeSparseArray"))
            Assert.assertNull(getFieldValue("itemSpanByItemFactoryMap"))
        }
        AssemblyGridLayoutManager(
            context = context,
            spanCount = 3,
            orientation = RecyclerView.HORIZONTAL,
            reverseLayout = true,
            itemSpanByPositionMap = mapOf(3 to ItemSpan.span(2)),
            itemSpanByItemTypeMap = mapOf(1 to ItemSpan.span(4)),
            itemSpanByItemFactoryMap = mapOf(TextItemFactory::class to ItemSpan.span(3))
        ).apply {
            Assert.assertEquals(
                "{3=ItemSpan(size=2)}",
                getFieldValue<SparseArrayCompat<ItemSpan>>("itemSpanByPositionSparseArray").toString()
            )
            Assert.assertEquals(
                "{1=ItemSpan(size=4)}",
                getFieldValue<SparseArrayCompat<ItemSpan>>("itemSpanByItemTypeSparseArray").toString()
            )
            Assert.assertEquals(
                "{${TextItemFactory::class.java}=ItemSpan(size=3)}",
                getFieldValue<Map<Class<out ItemFactory<out Any>>, ItemSpan>>("itemSpanByItemFactoryMap").toString()
            )
        }

        AssemblyGridLayoutManager(
            context = context,
            spanCount = 4,
            itemSpanByPositionMap = null,
            itemSpanByItemTypeMap = null,
            itemSpanByItemFactoryMap = null
        ).apply {
            Assert.assertEquals(4, spanCount)
            Assert.assertEquals(RecyclerView.VERTICAL, orientation)
            Assert.assertEquals(false, reverseLayout)
            Assert.assertNull(getFieldValue("itemSpanByPositionSparseArray"))
            Assert.assertNull(getFieldValue("itemSpanByItemTypeSparseArray"))
            Assert.assertNull(getFieldValue("itemSpanByItemFactoryMap"))
        }
        AssemblyGridLayoutManager(
            context = context,
            spanCount = 4,
            itemSpanByPositionMap = mapOf(3 to ItemSpan.span(2)),
            itemSpanByItemTypeMap = mapOf(1 to ItemSpan.span(4)),
            itemSpanByItemFactoryMap = mapOf(TextItemFactory::class to ItemSpan.span(3))
        ).apply {
            Assert.assertEquals(4, spanCount)
            Assert.assertEquals(RecyclerView.VERTICAL, orientation)
            Assert.assertEquals(false, reverseLayout)
            Assert.assertEquals(
                "{3=ItemSpan(size=2)}",
                getFieldValue<SparseArrayCompat<ItemSpan>>("itemSpanByPositionSparseArray").toString()
            )
            Assert.assertEquals(
                "{1=ItemSpan(size=4)}",
                getFieldValue<SparseArrayCompat<ItemSpan>>("itemSpanByItemTypeSparseArray").toString()
            )
            Assert.assertEquals(
                "{${TextItemFactory::class.java}=ItemSpan(size=3)}",
                getFieldValue<Map<Class<out ItemFactory<out Any>>, ItemSpan>>("itemSpanByItemFactoryMap").toString()
            )
        }

        AssemblyGridLayoutManager(
            context = context,
            spanCount = 4,
            gridLayoutItemSpanMap = mapOf(),
        ).apply {
            Assert.assertEquals(4, spanCount)
            Assert.assertEquals(RecyclerView.VERTICAL, orientation)
            Assert.assertEquals(false, reverseLayout)
            Assert.assertNull(getFieldValue("itemSpanByPositionSparseArray"))
            Assert.assertNull(getFieldValue("itemSpanByItemTypeSparseArray"))
            Assert.assertNull(getFieldValue("itemSpanByItemFactoryMap"))
        }
        AssemblyGridLayoutManager(
            context = context,
            spanCount = 4,
            gridLayoutItemSpanMap = mapOf(TextItemFactory::class to ItemSpan.span(3))
        ).apply {
            Assert.assertEquals(4, spanCount)
            Assert.assertEquals(RecyclerView.VERTICAL, orientation)
            Assert.assertEquals(false, reverseLayout)
            Assert.assertNull(getFieldValue("itemSpanByPositionSparseArray"))
            Assert.assertNull(getFieldValue("itemSpanByItemTypeSparseArray"))
            Assert.assertEquals(
                "{${TextItemFactory::class.java}=ItemSpan(size=3)}",
                getFieldValue<Map<Class<out ItemFactory<out Any>>, ItemSpan>>("itemSpanByItemFactoryMap").toString()
            )
        }
    }

    @Test
    fun testBuilder() {
        val context = InstrumentationRegistry.getInstrumentation().context
        AssemblyGridLayoutManager.Builder(context, null, 0, 0).build().apply {
            Assert.assertEquals(1, spanCount)
            Assert.assertEquals(RecyclerView.VERTICAL, orientation)
            Assert.assertEquals(false, reverseLayout)
            Assert.assertNull(getFieldValue("itemSpanByPositionSparseArray"))
            Assert.assertNull(getFieldValue("itemSpanByItemTypeSparseArray"))
            Assert.assertNull(getFieldValue("itemSpanByItemFactoryMap"))
        }

        AssemblyGridLayoutManager.Builder(context, 4, RecyclerView.HORIZONTAL, true).build().apply {
            Assert.assertEquals(4, spanCount)
            Assert.assertEquals(RecyclerView.HORIZONTAL, orientation)
            Assert.assertEquals(true, reverseLayout)
            Assert.assertNull(getFieldValue("itemSpanByPositionSparseArray"))
            Assert.assertNull(getFieldValue("itemSpanByItemTypeSparseArray"))
            Assert.assertNull(getFieldValue("itemSpanByItemFactoryMap"))
        }

        AssemblyGridLayoutManager.Builder(context, 4).build().apply {
            Assert.assertEquals(4, spanCount)
            Assert.assertEquals(RecyclerView.VERTICAL, orientation)
            Assert.assertEquals(false, reverseLayout)
            Assert.assertNull(getFieldValue("itemSpanByPositionSparseArray"))
            Assert.assertNull(getFieldValue("itemSpanByItemTypeSparseArray"))
            Assert.assertNull(getFieldValue("itemSpanByItemFactoryMap"))
        }

        AssemblyGridLayoutManager.Builder(context, 4).apply {
            itemSpanByPosition(5, ItemSpan.span(4))
            itemSpanByPosition(4 to ItemSpan.span(3))
            itemSpanByPosition(mapOf(3 to ItemSpan.span(2)))
            itemSpanByItemType(3, ItemSpan.span(2))
            itemSpanByItemType(4 to ItemSpan.span(1))
            itemSpanByItemType(mapOf(1 to ItemSpan.span(4)))
            itemSpanByItemFactory(ViewItemFactory::class, ItemSpan.span(1))
            itemSpanByItemFactory(BindingItemFactory::class to ItemSpan.span(2))
            itemSpanByItemFactory(mapOf(TextItemFactory::class to ItemSpan.span(3)))
        }.build().apply {
            Assert.assertEquals(4, spanCount)
            Assert.assertEquals(RecyclerView.VERTICAL, orientation)
            Assert.assertEquals(false, reverseLayout)
            Assert.assertEquals(
                "{3=ItemSpan(size=2), 4=ItemSpan(size=3), 5=ItemSpan(size=4)}",
                getFieldValue<SparseArrayCompat<ItemSpan>>("itemSpanByPositionSparseArray").toString()
            )
            Assert.assertEquals(
                "{1=ItemSpan(size=4), 3=ItemSpan(size=2), 4=ItemSpan(size=1)}",
                getFieldValue<SparseArrayCompat<ItemSpan>>("itemSpanByItemTypeSparseArray").toString()
            )
            getFieldValue<Map<Class<out ItemFactory<out Any>>, ItemSpan>>("itemSpanByItemFactoryMap")!!.apply {
                Assert.assertTrue(contains(ViewItemFactory::class.java))
                Assert.assertTrue(contains(BindingItemFactory::class.java))
                Assert.assertTrue(contains(TextItemFactory::class.java))
            }
        }
    }


    @Test
    fun testExtensions() {
        val context = InstrumentationRegistry.getInstrumentation().context
        val recyclerView = RecyclerView(context)

        context.newAssemblyGridLayoutManager(null, 0, 0).apply {
            Assert.assertEquals(1, spanCount)
            Assert.assertEquals(RecyclerView.VERTICAL, orientation)
            Assert.assertEquals(false, reverseLayout)
            Assert.assertNull(getFieldValue("itemSpanByPositionSparseArray"))
            Assert.assertNull(getFieldValue("itemSpanByItemTypeSparseArray"))
            Assert.assertNull(getFieldValue("itemSpanByItemFactoryMap"))
        }
        context.newAssemblyGridLayoutManager(null, 0, 0) {
            itemSpanByPosition(3 to ItemSpan.span(2))
            itemSpanByItemType(1 to ItemSpan.span(4))
            itemSpanByItemFactory(TextItemFactory::class to ItemSpan.span(3))
        }.apply {
            Assert.assertEquals(1, spanCount)
            Assert.assertEquals(RecyclerView.VERTICAL, orientation)
            Assert.assertEquals(false, reverseLayout)
            Assert.assertEquals(
                "{3=ItemSpan(size=2)}",
                getFieldValue<SparseArrayCompat<ItemSpan>>("itemSpanByPositionSparseArray").toString()
            )
            Assert.assertEquals(
                "{1=ItemSpan(size=4)}",
                getFieldValue<SparseArrayCompat<ItemSpan>>("itemSpanByItemTypeSparseArray").toString()
            )
            Assert.assertEquals(
                "{${TextItemFactory::class.java}=ItemSpan(size=3)}",
                getFieldValue<Map<Class<out ItemFactory<out Any>>, ItemSpan>>("itemSpanByItemFactoryMap").toString()
            )
        }
        context.newAssemblyGridLayoutManager(4, RecyclerView.HORIZONTAL, true).apply {
            Assert.assertEquals(4, spanCount)
            Assert.assertEquals(RecyclerView.HORIZONTAL, orientation)
            Assert.assertEquals(true, reverseLayout)
            Assert.assertNull(getFieldValue("itemSpanByPositionSparseArray"))
            Assert.assertNull(getFieldValue("itemSpanByItemTypeSparseArray"))
            Assert.assertNull(getFieldValue("itemSpanByItemFactoryMap"))
        }
        context.newAssemblyGridLayoutManager(4, RecyclerView.HORIZONTAL, true) {
            itemSpanByPosition(3 to ItemSpan.span(2))
            itemSpanByItemType(1 to ItemSpan.span(4))
            itemSpanByItemFactory(TextItemFactory::class to ItemSpan.span(3))
        }.apply {
            Assert.assertEquals(4, spanCount)
            Assert.assertEquals(RecyclerView.HORIZONTAL, orientation)
            Assert.assertEquals(true, reverseLayout)
            Assert.assertEquals(
                "{3=ItemSpan(size=2)}",
                getFieldValue<SparseArrayCompat<ItemSpan>>("itemSpanByPositionSparseArray").toString()
            )
            Assert.assertEquals(
                "{1=ItemSpan(size=4)}",
                getFieldValue<SparseArrayCompat<ItemSpan>>("itemSpanByItemTypeSparseArray").toString()
            )
            Assert.assertEquals(
                "{${TextItemFactory::class.java}=ItemSpan(size=3)}",
                getFieldValue<Map<Class<out ItemFactory<out Any>>, ItemSpan>>("itemSpanByItemFactoryMap").toString()
            )
        }

        recyclerView.newAssemblyGridLayoutManager(null, 0, 0).apply {
            Assert.assertEquals(1, spanCount)
            Assert.assertEquals(RecyclerView.VERTICAL, orientation)
            Assert.assertEquals(false, reverseLayout)
            Assert.assertNull(getFieldValue("itemSpanByPositionSparseArray"))
            Assert.assertNull(getFieldValue("itemSpanByItemTypeSparseArray"))
            Assert.assertNull(getFieldValue("itemSpanByItemFactoryMap"))
        }
        recyclerView.newAssemblyGridLayoutManager(null, 0, 0) {
            itemSpanByPosition(3 to ItemSpan.span(2))
            itemSpanByItemType(1 to ItemSpan.span(4))
            itemSpanByItemFactory(TextItemFactory::class to ItemSpan.span(3))
        }.apply {
            Assert.assertEquals(1, spanCount)
            Assert.assertEquals(RecyclerView.VERTICAL, orientation)
            Assert.assertEquals(false, reverseLayout)
            Assert.assertEquals(
                "{3=ItemSpan(size=2)}",
                getFieldValue<SparseArrayCompat<ItemSpan>>("itemSpanByPositionSparseArray").toString()
            )
            Assert.assertEquals(
                "{1=ItemSpan(size=4)}",
                getFieldValue<SparseArrayCompat<ItemSpan>>("itemSpanByItemTypeSparseArray").toString()
            )
            Assert.assertEquals(
                "{${TextItemFactory::class.java}=ItemSpan(size=3)}",
                getFieldValue<Map<Class<out ItemFactory<out Any>>, ItemSpan>>("itemSpanByItemFactoryMap").toString()
            )
        }
        recyclerView.newAssemblyGridLayoutManager(4, RecyclerView.HORIZONTAL, true).apply {
            Assert.assertEquals(4, spanCount)
            Assert.assertEquals(RecyclerView.HORIZONTAL, orientation)
            Assert.assertEquals(true, reverseLayout)
            Assert.assertNull(getFieldValue("itemSpanByPositionSparseArray"))
            Assert.assertNull(getFieldValue("itemSpanByItemTypeSparseArray"))
            Assert.assertNull(getFieldValue("itemSpanByItemFactoryMap"))
        }
        recyclerView.newAssemblyGridLayoutManager(4, RecyclerView.HORIZONTAL, true) {
            itemSpanByPosition(3 to ItemSpan.span(2))
            itemSpanByItemType(1 to ItemSpan.span(4))
            itemSpanByItemFactory(TextItemFactory::class to ItemSpan.span(3))
        }.apply {
            Assert.assertEquals(4, spanCount)
            Assert.assertEquals(RecyclerView.HORIZONTAL, orientation)
            Assert.assertEquals(true, reverseLayout)
            Assert.assertEquals(
                "{3=ItemSpan(size=2)}",
                getFieldValue<SparseArrayCompat<ItemSpan>>("itemSpanByPositionSparseArray").toString()
            )
            Assert.assertEquals(
                "{1=ItemSpan(size=4)}",
                getFieldValue<SparseArrayCompat<ItemSpan>>("itemSpanByItemTypeSparseArray").toString()
            )
            Assert.assertEquals(
                "{${TextItemFactory::class.java}=ItemSpan(size=3)}",
                getFieldValue<Map<Class<out ItemFactory<out Any>>, ItemSpan>>("itemSpanByItemFactoryMap").toString()
            )
        }

        recyclerView.setupAssemblyGridLayoutManager(null, 0, 0)
        recyclerView.layoutManager!!.asOrThrow<AssemblyGridLayoutManager>().apply {
            Assert.assertEquals(1, spanCount)
            Assert.assertEquals(RecyclerView.VERTICAL, orientation)
            Assert.assertEquals(false, reverseLayout)
            Assert.assertNull(getFieldValue("itemSpanByPositionSparseArray"))
            Assert.assertNull(getFieldValue("itemSpanByItemTypeSparseArray"))
            Assert.assertNull(getFieldValue("itemSpanByItemFactoryMap"))
        }
        recyclerView.setupAssemblyGridLayoutManager(null, 0, 0) {
            itemSpanByPosition(3 to ItemSpan.span(2))
            itemSpanByItemType(1 to ItemSpan.span(4))
            itemSpanByItemFactory(TextItemFactory::class to ItemSpan.span(3))
        }
        recyclerView.layoutManager!!.asOrThrow<AssemblyGridLayoutManager>().apply {
            Assert.assertEquals(1, spanCount)
            Assert.assertEquals(RecyclerView.VERTICAL, orientation)
            Assert.assertEquals(false, reverseLayout)
            Assert.assertEquals(
                "{3=ItemSpan(size=2)}",
                getFieldValue<SparseArrayCompat<ItemSpan>>("itemSpanByPositionSparseArray").toString()
            )
            Assert.assertEquals(
                "{1=ItemSpan(size=4)}",
                getFieldValue<SparseArrayCompat<ItemSpan>>("itemSpanByItemTypeSparseArray").toString()
            )
            Assert.assertEquals(
                "{${TextItemFactory::class.java}=ItemSpan(size=3)}",
                getFieldValue<Map<Class<out ItemFactory<out Any>>, ItemSpan>>("itemSpanByItemFactoryMap").toString()
            )
        }
        recyclerView.setupAssemblyGridLayoutManager(4, RecyclerView.HORIZONTAL, true)
        recyclerView.layoutManager!!.asOrThrow<AssemblyGridLayoutManager>().apply {
            Assert.assertEquals(4, spanCount)
            Assert.assertEquals(RecyclerView.HORIZONTAL, orientation)
            Assert.assertEquals(true, reverseLayout)
            Assert.assertNull(getFieldValue("itemSpanByPositionSparseArray"))
            Assert.assertNull(getFieldValue("itemSpanByItemTypeSparseArray"))
            Assert.assertNull(getFieldValue("itemSpanByItemFactoryMap"))
        }
        recyclerView.setupAssemblyGridLayoutManager(4, RecyclerView.HORIZONTAL, true) {
            itemSpanByPosition(3 to ItemSpan.span(2))
            itemSpanByItemType(1 to ItemSpan.span(4))
            itemSpanByItemFactory(TextItemFactory::class to ItemSpan.span(3))
        }
        recyclerView.layoutManager!!.asOrThrow<AssemblyGridLayoutManager>().apply {
            Assert.assertEquals(4, spanCount)
            Assert.assertEquals(RecyclerView.HORIZONTAL, orientation)
            Assert.assertEquals(true, reverseLayout)
            Assert.assertEquals(
                "{3=ItemSpan(size=2)}",
                getFieldValue<SparseArrayCompat<ItemSpan>>("itemSpanByPositionSparseArray").toString()
            )
            Assert.assertEquals(
                "{1=ItemSpan(size=4)}",
                getFieldValue<SparseArrayCompat<ItemSpan>>("itemSpanByItemTypeSparseArray").toString()
            )
            Assert.assertEquals(
                "{${TextItemFactory::class.java}=ItemSpan(size=3)}",
                getFieldValue<Map<Class<out ItemFactory<out Any>>, ItemSpan>>("itemSpanByItemFactoryMap").toString()
            )
        }
    }

    @Test
    fun testGetSpanSize() {
        val context = InstrumentationRegistry.getInstrumentation().context
        val recyclerView = RecyclerView(context).apply {
            adapter = AssemblyRecyclerAdapter(
                itemFactoryList = listOf(
                    TextItemFactory(),
                    ImageItemFactory(),
                    ViewItemFactory(Int::class, android.R.layout.simple_list_item_1)
                ),
                initDataList = listOf(
                    3,
                    Text("hello"),
                    Image(android.R.drawable.btn_default),
                    Text("world"),
                    4
                )
            )
        }

        recyclerView.newAssemblyGridLayoutManager(3).apply {
            recyclerView.layoutManager = this
            onAttachedToWindow(recyclerView)
        }.apply {
            Assert.assertEquals(1, spanSizeLookup.getSpanSize(0))
            Assert.assertEquals(1, spanSizeLookup.getSpanSize(1))
            Assert.assertEquals(1, spanSizeLookup.getSpanSize(2))
            Assert.assertEquals(1, spanSizeLookup.getSpanSize(3))
            Assert.assertEquals(1, spanSizeLookup.getSpanSize(4))
        }

        recyclerView.newAssemblyGridLayoutManager(3) {
            itemSpanByPosition(2, ItemSpan.span(2))
            itemSpanByPosition(4, ItemSpan.fullSpan())
        }.apply {
            recyclerView.layoutManager = this
            onAttachedToWindow(recyclerView)
        }.apply {
            Assert.assertEquals(1, spanSizeLookup.getSpanSize(0))
            Assert.assertEquals(1, spanSizeLookup.getSpanSize(1))
            Assert.assertEquals(2, spanSizeLookup.getSpanSize(2))
            Assert.assertEquals(1, spanSizeLookup.getSpanSize(3))
            Assert.assertEquals(3, spanSizeLookup.getSpanSize(4))
        }

        recyclerView.newAssemblyGridLayoutManager(3) {
            itemSpanByItemType(0, ItemSpan.fullSpan())
            itemSpanByItemType(2, ItemSpan.span(2))
        }.apply {
            recyclerView.layoutManager = this
            onAttachedToWindow(recyclerView)
        }.apply {
            Assert.assertEquals(2, spanSizeLookup.getSpanSize(0))
            Assert.assertEquals(3, spanSizeLookup.getSpanSize(1))
            Assert.assertEquals(1, spanSizeLookup.getSpanSize(2))
            Assert.assertEquals(3, spanSizeLookup.getSpanSize(3))
            Assert.assertEquals(2, spanSizeLookup.getSpanSize(4))
        }

        recyclerView.newAssemblyGridLayoutManager(3) {
            itemSpanByItemFactory(ImageItemFactory::class, ItemSpan.fullSpan())
        }.apply {
            recyclerView.layoutManager = this
            onAttachedToWindow(recyclerView)
        }.apply {
            Assert.assertEquals(1, spanSizeLookup.getSpanSize(0))
            Assert.assertEquals(1, spanSizeLookup.getSpanSize(1))
            Assert.assertEquals(3, spanSizeLookup.getSpanSize(2))
            Assert.assertEquals(1, spanSizeLookup.getSpanSize(3))
            Assert.assertEquals(1, spanSizeLookup.getSpanSize(4))
        }

        recyclerView.newAssemblyGridLayoutManager(3) {
            itemSpanByPosition(2, ItemSpan.span(2))
            itemSpanByPosition(3, ItemSpan.span(2))
            itemSpanByItemType(2, ItemSpan.span(1))
            itemSpanByItemFactory(TextItemFactory::class, ItemSpan.fullSpan())
        }.apply {
            recyclerView.layoutManager = this
            onAttachedToWindow(recyclerView)
        }.apply {
            Assert.assertEquals(1, spanSizeLookup.getSpanSize(0))
            Assert.assertEquals(3, spanSizeLookup.getSpanSize(1))
            Assert.assertEquals(2, spanSizeLookup.getSpanSize(2))
            Assert.assertEquals(2, spanSizeLookup.getSpanSize(3))
            Assert.assertEquals(1, spanSizeLookup.getSpanSize(4))
        }
    }
}