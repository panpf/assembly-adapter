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
package com.github.panpf.assemblyadapter.recycler.divider.test.internal

import android.graphics.Rect
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.test.platform.app.InstrumentationRegistry
import com.github.panpf.assemblyadapter.recycler.divider.Divider
import com.github.panpf.assemblyadapter.recycler.divider.DividerConfig
import com.github.panpf.assemblyadapter.recycler.divider.internal.LinearDividerItemDecorationHelper
import com.github.panpf.assemblyadapter.recycler.divider.internal.LinearItemDividerProvider
import org.junit.Assert
import org.junit.Test

class LinearDividerItemDecorationHelperTest {

    @Test
    fun testGetItemOffsets() {
        val context = InstrumentationRegistry.getInstrumentation().context
        val parent = RecyclerView(context)
        val view = TextView(context)

        val provider = LinearItemDividerProvider(
            dividerConfig = DividerConfig.Builder(Divider.space(5)).build()
                .toItemDividerConfig(context),
            headerDividerConfig = DividerConfig.Builder(Divider.space(10)).build()
                .toItemDividerConfig(context),
            footerDividerConfig = DividerConfig.Builder(Divider.space(15)).build()
                .toItemDividerConfig(context),
            sideHeaderDividerConfig = DividerConfig.Builder(Divider.space(25)).build()
                .toItemDividerConfig(context),
            sideFooterDividerConfig = DividerConfig.Builder(Divider.space(30)).build()
                .toItemDividerConfig(context),
        )
        val helper = LinearDividerItemDecorationHelper(provider)

        val itemCount = 5
        var isVerticalOrientation = true
        var isLTRDirection = true
        val outRect = Rect()
        val getItemOffsets: (position: Int) -> Rect = { position ->
            outRect.set(0, 0, 0, 0)
            helper.getItemOffsets(
                outRect, view, parent, itemCount, position, isVerticalOrientation, isLTRDirection
            )
            outRect
        }

        Assert.assertEquals("Rect(25, 10 - 30, 5)", getItemOffsets(0).toString())
        Assert.assertEquals("Rect(25, 0 - 30, 5)", getItemOffsets(1).toString())
        Assert.assertEquals("Rect(25, 0 - 30, 5)", getItemOffsets(2).toString())
        Assert.assertEquals("Rect(25, 0 - 30, 5)", getItemOffsets(3).toString())
        Assert.assertEquals("Rect(25, 0 - 30, 15)", getItemOffsets(4).toString())

        isVerticalOrientation = false

        Assert.assertEquals("Rect(10, 25 - 5, 30)", getItemOffsets(0).toString())
        Assert.assertEquals("Rect(0, 25 - 5, 30)", getItemOffsets(1).toString())
        Assert.assertEquals("Rect(0, 25 - 5, 30)", getItemOffsets(2).toString())
        Assert.assertEquals("Rect(0, 25 - 5, 30)", getItemOffsets(3).toString())
        Assert.assertEquals("Rect(0, 25 - 15, 30)", getItemOffsets(4).toString())


        isLTRDirection = false
        isVerticalOrientation = true

        Assert.assertEquals("Rect(30, 10 - 25, 5)", getItemOffsets(0).toString())
        Assert.assertEquals("Rect(30, 0 - 25, 5)", getItemOffsets(1).toString())
        Assert.assertEquals("Rect(30, 0 - 25, 5)", getItemOffsets(2).toString())
        Assert.assertEquals("Rect(30, 0 - 25, 5)", getItemOffsets(3).toString())
        Assert.assertEquals("Rect(30, 0 - 25, 15)", getItemOffsets(4).toString())

        isVerticalOrientation = false
        Assert.assertEquals("Rect(5, 25 - 10, 30)", getItemOffsets(0).toString())
        Assert.assertEquals("Rect(5, 25 - 0, 30)", getItemOffsets(1).toString())
        Assert.assertEquals("Rect(5, 25 - 0, 30)", getItemOffsets(2).toString())
        Assert.assertEquals("Rect(5, 25 - 0, 30)", getItemOffsets(3).toString())
        Assert.assertEquals("Rect(15, 25 - 0, 30)", getItemOffsets(4).toString())
    }
}