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
package com.github.panpf.assemblyadapter.recycler.test.divider.internal

import android.R
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.ConcatAdapter
import com.github.panpf.assemblyadapter.ViewItemFactory
import com.github.panpf.assemblyadapter.recycler.AssemblyRecyclerAdapter
import com.github.panpf.assemblyadapter.recycler.AssemblySingleDataRecyclerAdapter
import com.github.panpf.assemblyadapter.recycler.divider.internal.AssemblyFindItemFactoryClassSupport
import com.github.panpf.assemblyadapter.recycler.divider.internal.ConcatFindItemFactoryClassSupport
import com.github.panpf.tools4j.test.ktx.assertThrow
import org.junit.Assert
import org.junit.Test
import java.util.*

class ConcatFindItemFactoryClassSupportTest {
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
        val adapter = ConcatAdapter(
            AssemblyRecyclerAdapter(
                itemFactoryList = listOf(TextItemFactory(), ImageItemFactory()),
                initDataList = listOf(Text("hello"), Image(R.drawable.btn_default))
            ),
            AssemblySingleDataRecyclerAdapter(
                itemFactory = DateItemFactory(),
                initData = Date()
            )
        )

        ConcatFindItemFactoryClassSupport(AssemblyFindItemFactoryClassSupport()).apply {
            assertThrow(IndexOutOfBoundsException::class) {
                findItemFactoryClassByPosition(adapter, -1)
            }
            Assert.assertSame(
                TextItemFactory::class.java,
                findItemFactoryClassByPosition(adapter, 0)
            )
            Assert.assertSame(
                ImageItemFactory::class.java,
                findItemFactoryClassByPosition(adapter, 1)
            )
            Assert.assertSame(
                DateItemFactory::class.java,
                findItemFactoryClassByPosition(adapter, 2)
            )
            assertThrow(IndexOutOfBoundsException::class) {
                findItemFactoryClassByPosition(adapter, 3)
            }
        }
    }
}