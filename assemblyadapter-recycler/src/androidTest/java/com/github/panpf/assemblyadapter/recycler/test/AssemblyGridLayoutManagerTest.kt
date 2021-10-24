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
import androidx.recyclerview.widget.RecyclerView
import androidx.test.platform.app.InstrumentationRegistry
import com.github.panpf.assemblyadapter.ViewItemFactory
import com.github.panpf.assemblyadapter.recycler.AssemblyGridLayoutManager
import com.github.panpf.assemblyadapter.recycler.AssemblyRecyclerAdapter
import com.github.panpf.assemblyadapter.recycler.ItemSpan
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
        AssemblyGridLayoutManager(context, null, 0, 0, mapOf()).apply {
            Assert.assertEquals(1, spanCount)
            Assert.assertEquals(RecyclerView.VERTICAL, orientation)
            Assert.assertEquals(false, reverseLayout)
        }

        AssemblyGridLayoutManager(
            context = context,
            spanCount = 3,
            orientation = RecyclerView.HORIZONTAL,
            reverseLayout = true,
            itemSpanByItemFactoryMap = mapOf()
        ).apply {
            Assert.assertEquals(3, spanCount)
            Assert.assertEquals(RecyclerView.HORIZONTAL, orientation)
            Assert.assertEquals(true, reverseLayout)
        }

        AssemblyGridLayoutManager(
            context = context,
            spanCount = 3,
            itemSpanByItemFactoryMap = mapOf()
        ).apply {
            Assert.assertEquals(3, spanCount)
            Assert.assertEquals(RecyclerView.VERTICAL, orientation)
            Assert.assertEquals(false, reverseLayout)
        }
    }

    @Test
    fun testGetSpanSize() {
        val context = InstrumentationRegistry.getInstrumentation().context
        val recyclerView = RecyclerView(context).apply {
            adapter = AssemblyRecyclerAdapter(
                itemFactoryList = listOf(TextItemFactory(), ImageItemFactory()),
                initDataList = listOf(Text("hello"), Image(android.R.drawable.btn_default))
            )
        }

        AssemblyGridLayoutManager(context, null, 0, 0, mapOf()).apply {
            recyclerView.layoutManager = this
            onAttachedToWindow(recyclerView)
            Assert.assertEquals(1, spanSizeLookup.getSpanSize(0))
            Assert.assertEquals(1, spanSizeLookup.getSpanSize(1))
        }
        AssemblyGridLayoutManager(
            context,
            null,
            0,
            0,
            mapOf(TextItemFactory::class to ItemSpan.span(3))
        ).apply {
            recyclerView.layoutManager = this
            onAttachedToWindow(recyclerView)
            Assert.assertEquals(1, spanSizeLookup.getSpanSize(0))
            Assert.assertEquals(1, spanSizeLookup.getSpanSize(1))
        }
        AssemblyGridLayoutManager(
            context,
            null,
            0,
            0,
            mapOf(ImageItemFactory::class to ItemSpan.span(3))
        ).apply {
            recyclerView.layoutManager = this
            onAttachedToWindow(recyclerView)
            Assert.assertEquals(1, spanSizeLookup.getSpanSize(0))
            Assert.assertEquals(1, spanSizeLookup.getSpanSize(1))
        }

        AssemblyGridLayoutManager(context, 3, RecyclerView.VERTICAL, false, mapOf()).apply {
            recyclerView.layoutManager = this
            onAttachedToWindow(recyclerView)
            Assert.assertEquals(1, spanSizeLookup.getSpanSize(0))
            Assert.assertEquals(1, spanSizeLookup.getSpanSize(1))
        }
        AssemblyGridLayoutManager(
            context,
            3,
            RecyclerView.VERTICAL,
            false,
            mapOf(TextItemFactory::class to ItemSpan.span(2))
        ).apply {
            recyclerView.layoutManager = this
            onAttachedToWindow(recyclerView)
            Assert.assertEquals(2, spanSizeLookup.getSpanSize(0))
            Assert.assertEquals(1, spanSizeLookup.getSpanSize(1))
        }
        AssemblyGridLayoutManager(
            context,
            3,
            RecyclerView.VERTICAL,
            false,
            mapOf(ImageItemFactory::class to ItemSpan.span(2))
        ).apply {
            recyclerView.layoutManager = this
            onAttachedToWindow(recyclerView)
            Assert.assertEquals(1, spanSizeLookup.getSpanSize(0))
            Assert.assertEquals(2, spanSizeLookup.getSpanSize(1))
        }
        AssemblyGridLayoutManager(
            context,
            3,
            RecyclerView.VERTICAL,
            false,
            mapOf(TextItemFactory::class to ItemSpan.fullSpan())
        ).apply {
            recyclerView.layoutManager = this
            onAttachedToWindow(recyclerView)
            Assert.assertEquals(3, spanSizeLookup.getSpanSize(0))
        }
        AssemblyGridLayoutManager(
            context,
            3,
            RecyclerView.VERTICAL,
            false,
            mapOf(TextItemFactory::class to ItemSpan.span(4))
        ).apply {
            recyclerView.layoutManager = this
            onAttachedToWindow(recyclerView)
            Assert.assertEquals(3, spanSizeLookup.getSpanSize(0))
        }

        AssemblyGridLayoutManager(context, 3, mapOf()).apply {
            recyclerView.layoutManager = this
            onAttachedToWindow(recyclerView)
            Assert.assertEquals(1, spanSizeLookup.getSpanSize(0))
            Assert.assertEquals(1, spanSizeLookup.getSpanSize(1))
        }
        AssemblyGridLayoutManager(
            context,
            3,
            mapOf(TextItemFactory::class to ItemSpan.span(2))
        ).apply {
            recyclerView.layoutManager = this
            onAttachedToWindow(recyclerView)
            Assert.assertEquals(2, spanSizeLookup.getSpanSize(0))
            Assert.assertEquals(1, spanSizeLookup.getSpanSize(1))
        }
        AssemblyGridLayoutManager(
            context,
            3,
            mapOf(ImageItemFactory::class to ItemSpan.span(2))
        ).apply {
            recyclerView.layoutManager = this
            onAttachedToWindow(recyclerView)
            Assert.assertEquals(1, spanSizeLookup.getSpanSize(0))
            Assert.assertEquals(2, spanSizeLookup.getSpanSize(1))
        }
        AssemblyGridLayoutManager(
            context,
            3,
            mapOf(TextItemFactory::class to ItemSpan.fullSpan())
        ).apply {
            recyclerView.layoutManager = this
            onAttachedToWindow(recyclerView)
            Assert.assertEquals(3, spanSizeLookup.getSpanSize(0))
        }
        AssemblyGridLayoutManager(
            context,
            3,
            mapOf(TextItemFactory::class to ItemSpan.span(4))
        ).apply {
            recyclerView.layoutManager = this
            onAttachedToWindow(recyclerView)
            Assert.assertEquals(3, spanSizeLookup.getSpanSize(0))
        }
    }
}