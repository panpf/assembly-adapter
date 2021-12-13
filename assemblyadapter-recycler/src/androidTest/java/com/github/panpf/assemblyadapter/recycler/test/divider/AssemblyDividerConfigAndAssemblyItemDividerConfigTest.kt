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
package com.github.panpf.assemblyadapter.recycler.test.divider

import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.test.platform.app.InstrumentationRegistry
import com.github.panpf.assemblyadapter.ViewItemFactory
import com.github.panpf.assemblyadapter.recycler.AssemblyRecyclerAdapter
import com.github.panpf.assemblyadapter.recycler.divider.AssemblyDividerConfig
import com.github.panpf.assemblyadapter.recycler.divider.Divider
import com.github.panpf.assemblyadapter.recycler.divider.internal.AssemblyFindItemFactoryClassSupport
import org.junit.Assert
import org.junit.Test
import java.util.Date

class AssemblyDividerConfigAndAssemblyItemDividerConfigTest {

    private data class Text(val text: String)

    private class TextItemFactory : ViewItemFactory<Text>(Text::class, { context, _, _ ->
        TextView(context)
    })

    private data class Image(val resId: Int)

    private class ImageItemFactory : ViewItemFactory<Image>(Image::class, { context, _, _ ->
        ImageView(context)
    })

    private class DateItemFactory : ViewItemFactory<Date>(Date::class, { context, _, _ ->
        ImageView(context)
    })

    @Test
    fun test() {
        val context = InstrumentationRegistry.getInstrumentation().context
        val recyclerView = RecyclerView(context).apply {
            adapter = AssemblyRecyclerAdapter(
                listOf(TextItemFactory(), ImageItemFactory(), DateItemFactory()),
                listOf(
                    Date(), Date(), Date(), Date(), Date(),
                    Date(), Date(), Date(), Date(), Date(),
                    Text(""), Date(), Image(android.R.drawable.btn_default), Date()
                )
            )
        }

        AssemblyDividerConfig.Builder(Divider.space(10)).apply {
            disableByPosition(0)
            disableByPosition(5)
            disableBySpanIndex(1)
            disableByItemFactoryClass(TextItemFactory::class)
            personaliseByPosition(3, Divider.space(3))
            personaliseByPosition(7, Divider.space(7))
            personaliseBySpanIndex(3, Divider.space(3))
            personaliseByItemFactoryClass(ImageItemFactory::class, Divider.space(20))
        }.build()
            .toAssemblyItemDividerConfig(context, AssemblyFindItemFactoryClassSupport())
            .apply {
                Assert.assertEquals(
                    0,
                    get(recyclerView, position = 0, spanIndex = 0)?.getWidth(true) ?: 0
                )
                Assert.assertEquals(
                    0,
                    get(recyclerView, position = 1, spanIndex = 1)?.getWidth(true) ?: 0
                )
                Assert.assertEquals(
                    10,
                    get(recyclerView, position = 2, spanIndex = 2)?.getWidth(true) ?: 0
                )
                Assert.assertEquals(
                    3,
                    get(recyclerView, position = 3, spanIndex = 3)?.getWidth(true) ?: 0
                )
                Assert.assertEquals(
                    10,
                    get(recyclerView, position = 4, spanIndex = 4)?.getWidth(true) ?: 0
                )

                Assert.assertEquals(
                    0,
                    get(recyclerView, position = 5, spanIndex = 0)?.getWidth(true) ?: 0
                )
                Assert.assertEquals(
                    0,
                    get(recyclerView, position = 6, spanIndex = 1)?.getWidth(true) ?: 0
                )
                Assert.assertEquals(
                    7,
                    get(recyclerView, position = 7, spanIndex = 2)?.getWidth(true) ?: 0
                )
                Assert.assertEquals(
                    3,
                    get(recyclerView, position = 8, spanIndex = 3)?.getWidth(true) ?: 0
                )
                Assert.assertEquals(
                    10,
                    get(recyclerView, position = 9, spanIndex = 4)?.getWidth(true) ?: 0
                )

                Assert.assertEquals(
                    0,
                    get(recyclerView, position = 10, spanIndex = 0)?.getWidth(true) ?: 0
                )
                Assert.assertEquals(
                    0,
                    get(recyclerView, position = 11, spanIndex = 1)?.getWidth(true) ?: 0
                )
                Assert.assertEquals(
                    20,
                    get(recyclerView, position = 12, spanIndex = 2)?.getWidth(true) ?: 0
                )
                Assert.assertEquals(
                    3,
                    get(recyclerView, position = 13, spanIndex = 3)?.getWidth(true) ?: 0
                )
            }
    }

    @Test
    fun testPriority() {
        val context = InstrumentationRegistry.getInstrumentation().context
        val recyclerView = RecyclerView(context).apply {
            adapter = AssemblyRecyclerAdapter(
                listOf(TextItemFactory(), ImageItemFactory(), DateItemFactory()),
                listOf(
                    Date(), Date(), Date(), Date(), Date(),
                    Date(), Date(), Date(), Date(), Date(),
                    Text(""), Date(), Image(android.R.drawable.btn_default), Date()
                )
            )
        }

        AssemblyDividerConfig.Builder(Divider.space(10)).apply {
            personaliseByPosition(9, Divider.space(9))
            personaliseByItemFactoryClass(DateItemFactory::class, Divider.space(20))
        }.build()
            .toAssemblyItemDividerConfig(context, AssemblyFindItemFactoryClassSupport())
            .apply {
                Assert.assertEquals(
                    9,
                    get(recyclerView, position = 9, spanIndex = 4)?.getWidth(true) ?: 0
                )
            }
    }
}