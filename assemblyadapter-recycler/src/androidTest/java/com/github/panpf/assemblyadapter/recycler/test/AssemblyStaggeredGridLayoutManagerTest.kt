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

import android.R
import android.util.SparseBooleanArray
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.test.platform.app.InstrumentationRegistry
import com.github.panpf.assemblyadapter.BindingItemFactory
import com.github.panpf.assemblyadapter.ItemFactory
import com.github.panpf.assemblyadapter.ViewItemFactory
import com.github.panpf.assemblyadapter.recycler.AssemblyRecyclerAdapter
import com.github.panpf.assemblyadapter.recycler.AssemblyStaggeredGridLayoutManager
import com.github.panpf.assemblyadapter.recycler.ItemSpan
import com.github.panpf.assemblyadapter.recycler.newAssemblyStaggeredGridLayoutManager
import com.github.panpf.assemblyadapter.recycler.setupAssemblyStaggeredGridLayoutManager
import com.github.panpf.tools4j.reflect.ktx.getFieldValue
import com.github.panpf.tools4k.lang.asOrThrow
import org.junit.Assert
import org.junit.Test

class AssemblyStaggeredGridLayoutManagerTest {

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
        AssemblyStaggeredGridLayoutManager(
            context = context,
            attrs = null,
            defStyleAttr = 0,
            defStyleRes = 0,
            fullSpanByItemPositionList = null,
            fullSpanByItemTypeList = null,
            fullSpanByItemFactoryList = null
        ).apply {
            Assert.assertEquals(1, spanCount)
            Assert.assertEquals(RecyclerView.VERTICAL, orientation)
            Assert.assertEquals(false, reverseLayout)
            Assert.assertNull(getFieldValue("fullSpanByPositionSparseArray"))
            Assert.assertNull(getFieldValue("fullSpanByItemTypeSparseArray"))
            Assert.assertNull(getFieldValue("fullSpanByItemFactoryMap"))
        }
        AssemblyStaggeredGridLayoutManager(
            context = context,
            attrs = null,
            defStyleAttr = 0,
            defStyleRes = 0,
            fullSpanByItemPositionList = listOf(3),
            fullSpanByItemTypeList = listOf(1),
            fullSpanByItemFactoryList = listOf(TextItemFactory::class)
        ).apply {
            Assert.assertEquals(
                "{3=true}",
                getFieldValue<SparseBooleanArray>("fullSpanByPositionSparseArray")!!.toStringCompat()
            )
            Assert.assertEquals(
                "{1=true}",
                getFieldValue<SparseBooleanArray>("fullSpanByItemTypeSparseArray")!!.toStringCompat()
            )
            Assert.assertEquals(
                "{${TextItemFactory::class.java}=true}",
                getFieldValue<Map<Class<out ItemFactory<out Any>>, ItemSpan>>("fullSpanByItemFactoryMap").toString()
            )
        }

        AssemblyStaggeredGridLayoutManager(
            context = context,
            attrs = null,
            defStyleAttr = 0,
            defStyleRes = 0,
            fullSpanItemFactoryList = listOf()
        ).apply {
            Assert.assertEquals(1, spanCount)
            Assert.assertEquals(RecyclerView.VERTICAL, orientation)
            Assert.assertEquals(false, reverseLayout)
            Assert.assertNull(getFieldValue("fullSpanByPositionSparseArray"))
            Assert.assertNull(getFieldValue("fullSpanByItemTypeSparseArray"))
            Assert.assertNull(getFieldValue("fullSpanByItemFactoryMap"))
        }
        AssemblyStaggeredGridLayoutManager(
            context,
            null,
            0,
            0,
            listOf(TextItemFactory::class)
        ).apply {
            Assert.assertNull(getFieldValue("fullSpanByPositionSparseArray"))
            Assert.assertNull(getFieldValue("fullSpanByItemTypeSparseArray"))
            Assert.assertEquals(
                "{${TextItemFactory::class.java}=true}",
                getFieldValue<Map<Class<out ItemFactory<out Any>>, ItemSpan>>("fullSpanByItemFactoryMap").toString()
            )
        }

        AssemblyStaggeredGridLayoutManager(
            spanCount = 3,
            orientation = RecyclerView.HORIZONTAL,
            fullSpanByItemPositionList = null,
            fullSpanByItemTypeList = null,
            fullSpanByItemFactoryList = null
        ).apply {
            Assert.assertEquals(3, spanCount)
            Assert.assertEquals(RecyclerView.HORIZONTAL, orientation)
            Assert.assertNull(getFieldValue("fullSpanByPositionSparseArray"))
            Assert.assertNull(getFieldValue("fullSpanByItemTypeSparseArray"))
            Assert.assertNull(getFieldValue("fullSpanByItemFactoryMap"))
        }
        AssemblyStaggeredGridLayoutManager(
            spanCount = 3,
            orientation = RecyclerView.HORIZONTAL,
            fullSpanByItemPositionList = listOf(3),
            fullSpanByItemTypeList = listOf(1),
            fullSpanByItemFactoryList = listOf(TextItemFactory::class)
        ).apply {
            Assert.assertEquals(
                "{3=true}",
                getFieldValue<SparseBooleanArray>("fullSpanByPositionSparseArray")!!.toStringCompat()
            )
            Assert.assertEquals(
                "{1=true}",
                getFieldValue<SparseBooleanArray>("fullSpanByItemTypeSparseArray")!!.toStringCompat()
            )
            Assert.assertEquals(
                "{${TextItemFactory::class.java}=true}",
                getFieldValue<Map<Class<out ItemFactory<out Any>>, ItemSpan>>("fullSpanByItemFactoryMap").toString()
            )
        }

        AssemblyStaggeredGridLayoutManager(
            spanCount = 3,
            orientation = RecyclerView.HORIZONTAL,
            fullSpanItemFactoryList = listOf(),
        ).apply {
            Assert.assertEquals(3, spanCount)
            Assert.assertEquals(RecyclerView.HORIZONTAL, orientation)
            Assert.assertNull(getFieldValue("fullSpanByPositionSparseArray"))
            Assert.assertNull(getFieldValue("fullSpanByItemTypeSparseArray"))
            Assert.assertNull(getFieldValue("fullSpanByItemFactoryMap"))
        }
        AssemblyStaggeredGridLayoutManager(
            spanCount = 3,
            orientation = RecyclerView.HORIZONTAL,
            fullSpanItemFactoryList = listOf(TextItemFactory::class)
        ).apply {
            Assert.assertNull(getFieldValue("fullSpanByPositionSparseArray"))
            Assert.assertNull(getFieldValue("fullSpanByItemTypeSparseArray"))
            Assert.assertEquals(
                "{${TextItemFactory::class.java}=true}",
                getFieldValue<Map<Class<out ItemFactory<out Any>>, ItemSpan>>("fullSpanByItemFactoryMap").toString()
            )
        }

        AssemblyStaggeredGridLayoutManager(
            spanCount = 4,
            fullSpanByItemPositionList = null,
            fullSpanByItemTypeList = null,
            fullSpanByItemFactoryList = null
        ).apply {
            Assert.assertEquals(4, spanCount)
            Assert.assertEquals(RecyclerView.VERTICAL, orientation)
            Assert.assertEquals(false, reverseLayout)
            Assert.assertNull(getFieldValue("fullSpanByPositionSparseArray"))
            Assert.assertNull(getFieldValue("fullSpanByItemTypeSparseArray"))
            Assert.assertNull(getFieldValue("fullSpanByItemFactoryMap"))
        }
        AssemblyStaggeredGridLayoutManager(
            spanCount = 4,
            fullSpanByItemPositionList = listOf(3),
            fullSpanByItemTypeList = listOf(1),
            fullSpanByItemFactoryList = listOf(TextItemFactory::class)
        ).apply {
            Assert.assertEquals(4, spanCount)
            Assert.assertEquals(RecyclerView.VERTICAL, orientation)
            Assert.assertEquals(false, reverseLayout)
            Assert.assertEquals(
                "{3=true}",
                getFieldValue<SparseBooleanArray>("fullSpanByPositionSparseArray")!!.toStringCompat()
            )
            Assert.assertEquals(
                "{1=true}",
                getFieldValue<SparseBooleanArray>("fullSpanByItemTypeSparseArray")!!.toStringCompat()
            )
            Assert.assertEquals(
                "{${TextItemFactory::class.java}=true}",
                getFieldValue<Map<Class<out ItemFactory<out Any>>, ItemSpan>>("fullSpanByItemFactoryMap").toString()
            )
        }

        AssemblyStaggeredGridLayoutManager(
            spanCount = 4,
            fullSpanItemFactoryList = listOf(),
        ).apply {
            Assert.assertEquals(4, spanCount)
            Assert.assertEquals(RecyclerView.VERTICAL, orientation)
            Assert.assertEquals(false, reverseLayout)
            Assert.assertNull(getFieldValue("fullSpanByPositionSparseArray"))
            Assert.assertNull(getFieldValue("fullSpanByItemTypeSparseArray"))
            Assert.assertNull(getFieldValue("fullSpanByItemFactoryMap"))
        }
        AssemblyStaggeredGridLayoutManager(
            spanCount = 4,
            fullSpanItemFactoryList = listOf(TextItemFactory::class)
        ).apply {
            Assert.assertEquals(4, spanCount)
            Assert.assertEquals(RecyclerView.VERTICAL, orientation)
            Assert.assertEquals(false, reverseLayout)
            Assert.assertNull(getFieldValue("fullSpanByPositionSparseArray"))
            Assert.assertNull(getFieldValue("fullSpanByItemTypeSparseArray"))
            Assert.assertEquals(
                "{${TextItemFactory::class.java}=true}",
                getFieldValue<Map<Class<out ItemFactory<out Any>>, ItemSpan>>("fullSpanByItemFactoryMap").toString()
            )
        }
    }

    @Test
    fun testBuilder() {
        val context = InstrumentationRegistry.getInstrumentation().context
        AssemblyStaggeredGridLayoutManager.Builder(context, null, 0, 0).build().apply {
            Assert.assertEquals(1, spanCount)
            Assert.assertEquals(RecyclerView.VERTICAL, orientation)
            Assert.assertEquals(false, reverseLayout)
            Assert.assertNull(getFieldValue("fullSpanByPositionSparseArray"))
            Assert.assertNull(getFieldValue("fullSpanByItemTypeSparseArray"))
            Assert.assertNull(getFieldValue("fullSpanByItemFactoryMap"))
        }

        AssemblyStaggeredGridLayoutManager.Builder(4, RecyclerView.HORIZONTAL, true).build().apply {
            Assert.assertEquals(4, spanCount)
            Assert.assertEquals(RecyclerView.HORIZONTAL, orientation)
            Assert.assertEquals(true, reverseLayout)
            Assert.assertNull(getFieldValue("fullSpanByPositionSparseArray"))
            Assert.assertNull(getFieldValue("fullSpanByItemTypeSparseArray"))
            Assert.assertNull(getFieldValue("fullSpanByItemFactoryMap"))
        }

        AssemblyStaggeredGridLayoutManager.Builder(4).build().apply {
            Assert.assertEquals(4, spanCount)
            Assert.assertEquals(RecyclerView.VERTICAL, orientation)
            Assert.assertEquals(false, reverseLayout)
            Assert.assertNull(getFieldValue("fullSpanByPositionSparseArray"))
            Assert.assertNull(getFieldValue("fullSpanByItemTypeSparseArray"))
            Assert.assertNull(getFieldValue("fullSpanByItemFactoryMap"))
        }

        AssemblyStaggeredGridLayoutManager.Builder(4).apply {
            fullSpanByPosition(5, 4, 3)
            fullSpanByItemType(3, 4, 1)
            fullSpanByItemFactory(
                ViewItemFactory::class,
                BindingItemFactory::class,
                TextItemFactory::class
            )
        }.build().apply {
            Assert.assertEquals(4, spanCount)
            Assert.assertEquals(RecyclerView.VERTICAL, orientation)
            Assert.assertEquals(false, reverseLayout)
            Assert.assertEquals(
                "{3=true, 4=true, 5=true}",
                getFieldValue<SparseBooleanArray>("fullSpanByPositionSparseArray")!!.toStringCompat()
            )
            Assert.assertEquals(
                "{1=true, 3=true, 4=true}",
                getFieldValue<SparseBooleanArray>("fullSpanByItemTypeSparseArray")!!.toStringCompat()
            )
            getFieldValue<Map<Class<out ItemFactory<out Any>>, ItemSpan>>("fullSpanByItemFactoryMap")!!.apply {
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

        context.newAssemblyStaggeredGridLayoutManager(null, 0, 0).apply {
            Assert.assertEquals(1, spanCount)
            Assert.assertEquals(RecyclerView.VERTICAL, orientation)
            Assert.assertEquals(false, reverseLayout)
            Assert.assertNull(getFieldValue("fullSpanByPositionSparseArray"))
            Assert.assertNull(getFieldValue("fullSpanByItemTypeSparseArray"))
            Assert.assertNull(getFieldValue("fullSpanByItemFactoryMap"))
        }
        context.newAssemblyStaggeredGridLayoutManager(null, 0, 0) {
            fullSpanByPosition(3)
            fullSpanByItemType(1)
            fullSpanByItemFactory(TextItemFactory::class)
        }.apply {
            Assert.assertEquals(1, spanCount)
            Assert.assertEquals(RecyclerView.VERTICAL, orientation)
            Assert.assertEquals(false, reverseLayout)
            Assert.assertEquals(
                "{3=true}",
                getFieldValue<SparseBooleanArray>("fullSpanByPositionSparseArray")!!.toStringCompat()
            )
            Assert.assertEquals(
                "{1=true}",
                getFieldValue<SparseBooleanArray>("fullSpanByItemTypeSparseArray")!!.toStringCompat()
            )
            Assert.assertEquals(
                "{${TextItemFactory::class.java}=true}",
                getFieldValue<Map<Class<out ItemFactory<out Any>>, ItemSpan>>("fullSpanByItemFactoryMap").toString()
            )
        }

        newAssemblyStaggeredGridLayoutManager(4, RecyclerView.HORIZONTAL, true).apply {
            Assert.assertEquals(4, spanCount)
            Assert.assertEquals(RecyclerView.HORIZONTAL, orientation)
            Assert.assertEquals(true, reverseLayout)
            Assert.assertNull(getFieldValue("fullSpanByPositionSparseArray"))
            Assert.assertNull(getFieldValue("fullSpanByItemTypeSparseArray"))
            Assert.assertNull(getFieldValue("fullSpanByItemFactoryMap"))
        }
        newAssemblyStaggeredGridLayoutManager(4, RecyclerView.HORIZONTAL, true) {
            fullSpanByPosition(3)
            fullSpanByItemType(1)
            fullSpanByItemFactory(TextItemFactory::class)
        }.apply {
            Assert.assertEquals(4, spanCount)
            Assert.assertEquals(RecyclerView.HORIZONTAL, orientation)
            Assert.assertEquals(true, reverseLayout)
            Assert.assertEquals(
                "{3=true}",
                getFieldValue<SparseBooleanArray>("fullSpanByPositionSparseArray")!!.toStringCompat()
            )
            Assert.assertEquals(
                "{1=true}",
                getFieldValue<SparseBooleanArray>("fullSpanByItemTypeSparseArray")!!.toStringCompat()
            )
            Assert.assertEquals(
                "{${TextItemFactory::class.java}=true}",
                getFieldValue<Map<Class<out ItemFactory<out Any>>, ItemSpan>>("fullSpanByItemFactoryMap").toString()
            )
        }

        recyclerView.newAssemblyStaggeredGridLayoutManager(null, 0, 0).apply {
            Assert.assertEquals(1, spanCount)
            Assert.assertEquals(RecyclerView.VERTICAL, orientation)
            Assert.assertEquals(false, reverseLayout)
            Assert.assertNull(getFieldValue("fullSpanByPositionSparseArray"))
            Assert.assertNull(getFieldValue("fullSpanByItemTypeSparseArray"))
            Assert.assertNull(getFieldValue("fullSpanByItemFactoryMap"))
        }
        recyclerView.newAssemblyStaggeredGridLayoutManager(null, 0, 0) {
            fullSpanByPosition(3)
            fullSpanByItemType(1)
            fullSpanByItemFactory(TextItemFactory::class)
        }.apply {
            Assert.assertEquals(1, spanCount)
            Assert.assertEquals(RecyclerView.VERTICAL, orientation)
            Assert.assertEquals(false, reverseLayout)
            Assert.assertEquals(
                "{3=true}",
                getFieldValue<SparseBooleanArray>("fullSpanByPositionSparseArray")!!.toStringCompat()
            )
            Assert.assertEquals(
                "{1=true}",
                getFieldValue<SparseBooleanArray>("fullSpanByItemTypeSparseArray")!!.toStringCompat()
            )
            Assert.assertEquals(
                "{${TextItemFactory::class.java}=true}",
                getFieldValue<Map<Class<out ItemFactory<out Any>>, ItemSpan>>("fullSpanByItemFactoryMap").toString()
            )
        }

        recyclerView.setupAssemblyStaggeredGridLayoutManager(null, 0, 0)
        recyclerView.layoutManager!!.asOrThrow<AssemblyStaggeredGridLayoutManager>().apply {
            Assert.assertEquals(1, spanCount)
            Assert.assertEquals(RecyclerView.VERTICAL, orientation)
            Assert.assertEquals(false, reverseLayout)
            Assert.assertNull(getFieldValue("fullSpanByPositionSparseArray"))
            Assert.assertNull(getFieldValue("fullSpanByItemTypeSparseArray"))
            Assert.assertNull(getFieldValue("fullSpanByItemFactoryMap"))
        }
        recyclerView.setupAssemblyStaggeredGridLayoutManager(null, 0, 0) {
            fullSpanByPosition(3)
            fullSpanByItemType(1)
            fullSpanByItemFactory(TextItemFactory::class)
        }
        recyclerView.layoutManager!!.asOrThrow<AssemblyStaggeredGridLayoutManager>().apply {
            Assert.assertEquals(1, spanCount)
            Assert.assertEquals(RecyclerView.VERTICAL, orientation)
            Assert.assertEquals(false, reverseLayout)
            Assert.assertEquals(
                "{3=true}",
                getFieldValue<SparseBooleanArray>("fullSpanByPositionSparseArray")!!.toStringCompat()
            )
            Assert.assertEquals(
                "{1=true}",
                getFieldValue<SparseBooleanArray>("fullSpanByItemTypeSparseArray")!!.toStringCompat()
            )
            Assert.assertEquals(
                "{${TextItemFactory::class.java}=true}",
                getFieldValue<Map<Class<out ItemFactory<out Any>>, ItemSpan>>("fullSpanByItemFactoryMap").toString()
            )
        }
        recyclerView.setupAssemblyStaggeredGridLayoutManager(4, RecyclerView.HORIZONTAL, true)
        recyclerView.layoutManager!!.asOrThrow<AssemblyStaggeredGridLayoutManager>().apply {
            Assert.assertEquals(4, spanCount)
            Assert.assertEquals(RecyclerView.HORIZONTAL, orientation)
            Assert.assertEquals(true, reverseLayout)
            Assert.assertNull(getFieldValue("fullSpanByPositionSparseArray"))
            Assert.assertNull(getFieldValue("fullSpanByItemTypeSparseArray"))
            Assert.assertNull(getFieldValue("fullSpanByItemFactoryMap"))
        }
        recyclerView.setupAssemblyStaggeredGridLayoutManager(4, RecyclerView.HORIZONTAL, true) {
            fullSpanByPosition(3)
            fullSpanByItemType(1)
            fullSpanByItemFactory(TextItemFactory::class)
        }
        recyclerView.layoutManager!!.asOrThrow<AssemblyStaggeredGridLayoutManager>().apply {
            Assert.assertEquals(4, spanCount)
            Assert.assertEquals(RecyclerView.HORIZONTAL, orientation)
            Assert.assertEquals(true, reverseLayout)
            Assert.assertEquals(
                "{3=true}",
                getFieldValue<SparseBooleanArray>("fullSpanByPositionSparseArray")!!.toStringCompat()
            )
            Assert.assertEquals(
                "{1=true}",
                getFieldValue<SparseBooleanArray>("fullSpanByItemTypeSparseArray")!!.toStringCompat()
            )
            Assert.assertEquals(
                "{${TextItemFactory::class.java}=true}",
                getFieldValue<Map<Class<out ItemFactory<out Any>>, ItemSpan>>("fullSpanByItemFactoryMap").toString()
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
                    ViewItemFactory(Int::class, R.layout.simple_list_item_1)
                ),
                initDataList = listOf(
                    3,
                    Text("hello"),
                    Image(R.drawable.btn_default),
                    Text("world"),
                    4
                )
            )
        }

        newAssemblyStaggeredGridLayoutManager(3).apply {
            recyclerView.layoutManager = this
            onAttachedToWindow(recyclerView)
        }.apply {
            Assert.assertEquals(false, isFullSpanByPosition(0))
            Assert.assertEquals(false, isFullSpanByPosition(1))
            Assert.assertEquals(false, isFullSpanByPosition(2))
            Assert.assertEquals(false, isFullSpanByPosition(3))
            Assert.assertEquals(false, isFullSpanByPosition(4))
        }

        newAssemblyStaggeredGridLayoutManager(3) {
            fullSpanByPosition(2, 4)
        }.apply {
            recyclerView.layoutManager = this
            onAttachedToWindow(recyclerView)
        }.apply {
            Assert.assertEquals(false, isFullSpanByPosition(0))
            Assert.assertEquals(false, isFullSpanByPosition(1))
            Assert.assertEquals(true, isFullSpanByPosition(2))
            Assert.assertEquals(false, isFullSpanByPosition(3))
            Assert.assertEquals(true, isFullSpanByPosition(4))
        }

        newAssemblyStaggeredGridLayoutManager(3) {
            fullSpanByItemType(0, 2)
        }.apply {
            recyclerView.layoutManager = this
            onAttachedToWindow(recyclerView)
        }.apply {
            Assert.assertEquals(true, isFullSpanByPosition(0))
            Assert.assertEquals(true, isFullSpanByPosition(1))
            Assert.assertEquals(false, isFullSpanByPosition(2))
            Assert.assertEquals(true, isFullSpanByPosition(3))
            Assert.assertEquals(true, isFullSpanByPosition(4))
        }

        newAssemblyStaggeredGridLayoutManager(3) {
            fullSpanByItemFactory(ImageItemFactory::class)
        }.apply {
            recyclerView.layoutManager = this
            onAttachedToWindow(recyclerView)
        }.apply {
            Assert.assertEquals(false, isFullSpanByPosition(0))
            Assert.assertEquals(false, isFullSpanByPosition(1))
            Assert.assertEquals(true, isFullSpanByPosition(2))
            Assert.assertEquals(false, isFullSpanByPosition(3))
            Assert.assertEquals(false, isFullSpanByPosition(4))
        }

        newAssemblyStaggeredGridLayoutManager(3) {
            fullSpanByPosition(2, 3)
            fullSpanByItemFactory(TextItemFactory::class)
        }.apply {
            recyclerView.layoutManager = this
            onAttachedToWindow(recyclerView)
        }.apply {
            Assert.assertEquals(false, isFullSpanByPosition(0))
            Assert.assertEquals(true, isFullSpanByPosition(1))
            Assert.assertEquals(true, isFullSpanByPosition(2))
            Assert.assertEquals(true, isFullSpanByPosition(3))
            Assert.assertEquals(false, isFullSpanByPosition(4))
        }
    }
}

fun SparseBooleanArray.toStringCompat(): String {
    if (size() <= 0) {
        return "{}"
    }
    val buffer = StringBuilder(size() * 28)
    buffer.append('{')
    for (i in 0 until size()) {
        if (i > 0) {
            buffer.append(", ")
        }
        val key: Int = keyAt(i)
        buffer.append(key)
        buffer.append('=')
        val value: Boolean = valueAt(i)
        buffer.append(value)
    }
    buffer.append('}')
    return buffer.toString()
}