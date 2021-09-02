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
import com.github.panpf.assemblyadapter.recycler.AssemblyStaggeredGridLayoutManager
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
        AssemblyStaggeredGridLayoutManager(context, null, 0, 0, listOf()).apply {
            Assert.assertEquals(1, spanCount)
            Assert.assertEquals(RecyclerView.VERTICAL, orientation)
            Assert.assertEquals(false, reverseLayout)
        }

        AssemblyStaggeredGridLayoutManager(
            spanCount = 3,
            orientation = RecyclerView.HORIZONTAL,
            fullSpanItemFactoryList = listOf()
        ).apply {
            Assert.assertEquals(3, spanCount)
            Assert.assertEquals(RecyclerView.HORIZONTAL, orientation)
            Assert.assertEquals(false, reverseLayout)
        }

        AssemblyStaggeredGridLayoutManager(
            spanCount = 3,
            fullSpanItemFactoryList = listOf()
        ).apply {
            Assert.assertEquals(3, spanCount)
            Assert.assertEquals(RecyclerView.VERTICAL, orientation)
            Assert.assertEquals(false, reverseLayout)
        }
    }

    @Test
    fun testGetSpanSize() {
        val context = InstrumentationRegistry.getInstrumentation().context
        AssemblyStaggeredGridLayoutManager(context, null, 0, 0, listOf()).apply {
            Assert.assertEquals(false, isFullSpanByItemFactoryClass(TextItemFactory::class.java))
            Assert.assertEquals(false, isFullSpanByItemFactoryClass(ImageItemFactory::class.java))
        }
        AssemblyStaggeredGridLayoutManager(
            context, null, 0, 0, listOf(TextItemFactory::class)
        ).apply {
            Assert.assertEquals(true, isFullSpanByItemFactoryClass(TextItemFactory::class.java))
            Assert.assertEquals(false, isFullSpanByItemFactoryClass(ImageItemFactory::class.java))
        }
        AssemblyStaggeredGridLayoutManager(
            context, null, 0, 0, listOf(ImageItemFactory::class)
        ).apply {
            Assert.assertEquals(false, isFullSpanByItemFactoryClass(TextItemFactory::class.java))
            Assert.assertEquals(true, isFullSpanByItemFactoryClass(ImageItemFactory::class.java))
        }
        AssemblyStaggeredGridLayoutManager(
            context, null, 0, 0, listOf(TextItemFactory::class, ImageItemFactory::class)
        ).apply {
            Assert.assertEquals(true, isFullSpanByItemFactoryClass(TextItemFactory::class.java))
            Assert.assertEquals(true, isFullSpanByItemFactoryClass(ImageItemFactory::class.java))
        }

        AssemblyStaggeredGridLayoutManager(3, RecyclerView.VERTICAL, listOf()).apply {
            Assert.assertEquals(false, isFullSpanByItemFactoryClass(TextItemFactory::class.java))
            Assert.assertEquals(false, isFullSpanByItemFactoryClass(ImageItemFactory::class.java))
        }
        AssemblyStaggeredGridLayoutManager(
            3, RecyclerView.VERTICAL, listOf(TextItemFactory::class)
        ).apply {
            Assert.assertEquals(true, isFullSpanByItemFactoryClass(TextItemFactory::class.java))
            Assert.assertEquals(false, isFullSpanByItemFactoryClass(ImageItemFactory::class.java))
        }
        AssemblyStaggeredGridLayoutManager(
            3, RecyclerView.VERTICAL, listOf(ImageItemFactory::class)
        ).apply {
            Assert.assertEquals(false, isFullSpanByItemFactoryClass(TextItemFactory::class.java))
            Assert.assertEquals(true, isFullSpanByItemFactoryClass(ImageItemFactory::class.java))
        }
        AssemblyStaggeredGridLayoutManager(
            3, RecyclerView.VERTICAL, listOf(TextItemFactory::class, ImageItemFactory::class)
        ).apply {
            Assert.assertEquals(true, isFullSpanByItemFactoryClass(TextItemFactory::class.java))
            Assert.assertEquals(true, isFullSpanByItemFactoryClass(ImageItemFactory::class.java))
        }

        AssemblyStaggeredGridLayoutManager(3, listOf()).apply {
            Assert.assertEquals(false, isFullSpanByItemFactoryClass(TextItemFactory::class.java))
            Assert.assertEquals(false, isFullSpanByItemFactoryClass(ImageItemFactory::class.java))
        }
        AssemblyStaggeredGridLayoutManager(3, listOf(TextItemFactory::class)).apply {
            Assert.assertEquals(true, isFullSpanByItemFactoryClass(TextItemFactory::class.java))
            Assert.assertEquals(false, isFullSpanByItemFactoryClass(ImageItemFactory::class.java))
        }
        AssemblyStaggeredGridLayoutManager(3, listOf(ImageItemFactory::class)).apply {
            Assert.assertEquals(false, isFullSpanByItemFactoryClass(TextItemFactory::class.java))
            Assert.assertEquals(true, isFullSpanByItemFactoryClass(ImageItemFactory::class.java))
        }
        AssemblyStaggeredGridLayoutManager(
            3,
            listOf(TextItemFactory::class, ImageItemFactory::class)
        ).apply {
            Assert.assertEquals(true, isFullSpanByItemFactoryClass(TextItemFactory::class.java))
            Assert.assertEquals(true, isFullSpanByItemFactoryClass(ImageItemFactory::class.java))
        }
    }
}