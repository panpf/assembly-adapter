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
package com.github.panpf.assemblyadapter.recycler.divider.test

import androidx.recyclerview.widget.RecyclerView
import androidx.test.platform.app.InstrumentationRegistry
import com.github.panpf.assemblyadapter.recycler.divider.Divider
import com.github.panpf.assemblyadapter.recycler.divider.DividerConfig
import org.junit.Assert
import org.junit.Test

class DividerConfigAndItemDividerConfigTest {

    @Test
    fun test() {
        val context = InstrumentationRegistry.getInstrumentation().context
        val recyclerView = RecyclerView(context)

        DividerConfig.Builder(Divider.space(10)).apply {
            disableByPosition(0)
            disableByPosition(5)
            disableBySpanIndex(1)
            personaliseByPosition(3, Divider.space(3))
            personaliseByPosition(7, Divider.space(7))
            personaliseBySpanIndex(3, Divider.space(3))
        }.build().toItemDividerConfig(context).apply {
            Assert.assertEquals(0, get(recyclerView, position = 0, spanIndex = 0)?.widthSize ?: 0)
            Assert.assertEquals(0, get(recyclerView, position = 1, spanIndex = 1)?.widthSize ?: 0)
            Assert.assertEquals(10, get(recyclerView, position = 2, spanIndex = 2)?.widthSize ?: 0)
            Assert.assertEquals(3, get(recyclerView, position = 3, spanIndex = 3)?.widthSize ?: 0)
            Assert.assertEquals(10, get(recyclerView, position = 4, spanIndex = 4)?.widthSize ?: 0)

            Assert.assertEquals(0, get(recyclerView, position = 5, spanIndex = 0)?.widthSize ?: 0)
            Assert.assertEquals(0, get(recyclerView, position = 6, spanIndex = 1)?.widthSize ?: 0)
            Assert.assertEquals(7, get(recyclerView, position = 7, spanIndex = 2)?.widthSize ?: 0)
            Assert.assertEquals(3, get(recyclerView, position = 8, spanIndex = 3)?.widthSize ?: 0)
            Assert.assertEquals(10, get(recyclerView, position = 9, spanIndex = 4)?.widthSize ?: 0)
        }
    }
}