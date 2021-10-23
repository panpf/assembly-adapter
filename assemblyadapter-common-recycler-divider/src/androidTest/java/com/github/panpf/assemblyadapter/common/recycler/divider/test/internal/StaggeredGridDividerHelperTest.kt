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
package com.github.panpf.assemblyadapter.common.recycler.divider.test.internal

import android.graphics.Rect
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.test.platform.app.InstrumentationRegistry
import com.github.panpf.assemblyadapter.recycler.divider.Divider
import com.github.panpf.assemblyadapter.recycler.divider.DividerConfig
import com.github.panpf.assemblyadapter.recycler.divider.StaggeredGridDividerItemDecoration
import com.github.panpf.assemblyadapter.recycler.divider.internal.GridItemParams
import org.junit.Assert
import org.junit.Test

class StaggeredGridDividerItemDecorationHelperTest {

    @Test
    fun test() {
        val context = InstrumentationRegistry.getInstrumentation().context
        val parent = RecyclerView(context)
        val view = TextView(context)

        val provider = StaggeredGridDividerItemDecoration(
            dividerConfig = DividerConfig.Builder(Divider.space(5)).build()
                .toItemDividerConfig(context),
            headerDividerConfig = DividerConfig.Builder(Divider.space(10)).build()
                .toItemDividerConfig(context),
            footerDividerConfig = DividerConfig.Builder(Divider.space(15)).build()
                .toItemDividerConfig(context),
            sideDividerConfig = DividerConfig.Builder(Divider.space(20)).build()
                .toItemDividerConfig(context),
            sideHeaderDividerConfig = DividerConfig.Builder(Divider.space(25)).build()
                .toItemDividerConfig(context),
            sideFooterDividerConfig = DividerConfig.Builder(Divider.space(30)).build()
                .toItemDividerConfig(context)
        ) { parent, position ->
            false
        }
        val helper = provider.dividerHelper

        val itemCount = 7
        val spanCount = 3
        var isVerticalOrientation = true
        var isLTRDirection = true
        val outRect = Rect()
        val getItemOffsets: (position: Int) -> Rect = { position ->
            val spanIndex = when (position) {
                0 -> 0
                1 -> 0
                2 -> 0
                3 -> 1
                4 -> 2
                5 -> 0
                6 -> 1
                else -> throw IllegalArgumentException("position Out of range: $position")
            }
            val isFullSpan = position == 1
            val isColumnFirst = position == 0
            val isColumnLast = when (position) {
                4, 5, 6 -> true
                else -> false
            }
            val isFirstSpan = isFullSpan || spanIndex == 0
            val isLastSpan = isFullSpan || spanIndex == spanCount - 1
            val spanSize = if (isFullSpan) spanCount else 1
            outRect.set(0, 0, 0, 0)
            helper.getItemOffsets(
                outRect,
                GridItemParams(
                    view, parent, itemCount, position, spanCount, spanSize, spanIndex,
                    isFullSpan, isFirstSpan, isLastSpan, isColumnFirst, isColumnLast,
                    isVerticalOrientation, isLTRDirection
                ),
                true
            )
            outRect
        }

        // todo Fix test errors

        Assert.assertEquals("Rect(25, 10 - 10, 5)", getItemOffsets(0).toString())
        Assert.assertEquals("Rect(25, 0 - 30, 5)", getItemOffsets(1).toString())
        Assert.assertEquals("Rect(25, 0 - 10, 5)", getItemOffsets(2).toString())
        Assert.assertEquals("Rect(10, 0 - 10, 5)", getItemOffsets(3).toString())
        Assert.assertEquals("Rect(10, 0 - 30, 15)", getItemOffsets(4).toString())
        Assert.assertEquals("Rect(25, 0 - 10, 15)", getItemOffsets(5).toString())
        Assert.assertEquals("Rect(10, 0 - 10, 15)", getItemOffsets(6).toString())

        isVerticalOrientation = false

        Assert.assertEquals("Rect(10, 25 - 5, 10)", getItemOffsets(0).toString())
        Assert.assertEquals("Rect(0, 25 - 5, 30)", getItemOffsets(1).toString())
        Assert.assertEquals("Rect(0, 25 - 5, 10)", getItemOffsets(2).toString())
        Assert.assertEquals("Rect(0, 10 - 5, 10)", getItemOffsets(3).toString())
        Assert.assertEquals("Rect(0, 10 - 15, 30)", getItemOffsets(4).toString())
        Assert.assertEquals("Rect(0, 25 - 15, 10)", getItemOffsets(5).toString())
        Assert.assertEquals("Rect(0, 10 - 15, 10)", getItemOffsets(6).toString())

        isVerticalOrientation = true
        isLTRDirection = false

        Assert.assertEquals("Rect(20, 10 - 12, 5)", getItemOffsets(0).toString())
        Assert.assertEquals("Rect(30, 0 - 25, 5)", getItemOffsets(1).toString())
        Assert.assertEquals("Rect(20, 0 - 12, 5)", getItemOffsets(2).toString())
        Assert.assertEquals("Rect(10, 0 - 10, 5)", getItemOffsets(3).toString())
        Assert.assertEquals("Rect(15, 0 - 20, 15)", getItemOffsets(4).toString())
        Assert.assertEquals("Rect(20, 0 - 12, 15)", getItemOffsets(5).toString())
        Assert.assertEquals("Rect(10, 0 - 10, 15)", getItemOffsets(6).toString())

        isVerticalOrientation = false

        Assert.assertEquals("Rect(5, 25 - 10, 10)", getItemOffsets(0).toString())
        Assert.assertEquals("Rect(5, 25 - 0, 30)", getItemOffsets(1).toString())
        Assert.assertEquals("Rect(5, 25 - 0, 10)", getItemOffsets(2).toString())
        Assert.assertEquals("Rect(5, 10 - 0, 10)", getItemOffsets(3).toString())
        Assert.assertEquals("Rect(15, 10 - 0, 30)", getItemOffsets(4).toString())
        Assert.assertEquals("Rect(15, 25 - 0, 10)", getItemOffsets(5).toString())
        Assert.assertEquals("Rect(15, 10 - 0, 10)", getItemOffsets(6).toString())
    }
}