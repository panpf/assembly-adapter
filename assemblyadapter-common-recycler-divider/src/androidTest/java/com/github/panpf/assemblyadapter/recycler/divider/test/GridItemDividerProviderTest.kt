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

import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.test.platform.app.InstrumentationRegistry
import com.github.panpf.assemblyadapter.recycler.divider.Divider
import com.github.panpf.assemblyadapter.recycler.divider.DividerConfig
import com.github.panpf.assemblyadapter.recycler.divider.internal.GridItemDividerProvider
import com.github.panpf.assemblyadapter.recycler.divider.internal.ItemDivider
import org.junit.Assert
import org.junit.Test

class GridItemDividerProviderTest {

    @Test
    fun test() {
        val context = InstrumentationRegistry.getInstrumentation().context
        val parent = RecyclerView(context)
        val view = TextView(context)

        val provider = GridItemDividerProvider(
            dividerConfig = DividerConfig.Builder(Divider.space(5)).build()
                .toItemDividerConfig(context),
            headerDividerConfig = DividerConfig.Builder(Divider.space(10)).build()
                .toItemDividerConfig(context),
            footerDividerConfig = DividerConfig.Builder(Divider.space(15)).build()
                .toItemDividerConfig(context),
            sideDividerConfig = DividerConfig.Builder(Divider.space(20)).build()
                .toItemDividerConfig(context),
            headerSideDividerConfig = DividerConfig.Builder(Divider.space(25)).build()
                .toItemDividerConfig(context),
            footerSideDividerConfig = DividerConfig.Builder(Divider.space(30)).build()
                .toItemDividerConfig(context),
        )

        val itemCount = 7
        var position = -1
        val spanCount = 3
        val spanGroupCount = 4
        var isVerticalOrientation = true
        val getDivider: (ItemDivider.Type) -> ItemDivider? = {
            val spanSize = when (position) {
                1 -> 2
                2 -> spanCount
                else -> 1
            }
            val spanIndex = when (position) {
                0 -> 0
                1 -> 1
                2 -> 0
                3 -> 0
                4 -> 1
                5 -> 2
                6 -> 0
                else -> throw IllegalArgumentException("position Out of range: $position")
            }
            val spanGroupIndex = when (position) {
                0, 1 -> 0
                2 -> 1
                3, 4, 5 -> 2
                6 -> 3
                else -> throw IllegalArgumentException("position Out of range: $position")
            }
            val isFirstGroup = spanGroupIndex == 0
            val isLastGroup = spanGroupIndex == spanGroupCount - 1
            val isFullSpan = spanSize == spanCount
            val isFirstSpan = isFullSpan || spanIndex == 0
            val isLastSpan = isFullSpan || (spanIndex + spanSize) == spanCount
            provider.getItemDivider(
                view = view,
                parent = parent,
                itemCount = itemCount,
                position = position,
                spanCount = spanCount,
                spanSize = spanSize,
                spanIndex = spanIndex,
                isFullSpan = isFullSpan,
                isFirstSpan = isFirstSpan,
                isLastSpan = isLastSpan,
                spanGroupCount = spanGroupCount,
                spanGroupIndex = spanGroupIndex,
                isFirstGroup = isFirstGroup,
                isLastGroup = isLastGroup,
                isVerticalOrientation = isVerticalOrientation,
                dividerType = it
            )
        }

        position = 0
        Assert.assertEquals(25, getDivider(ItemDivider.Type.START)?.widthSize ?: 0)
        Assert.assertEquals(10, getDivider(ItemDivider.Type.TOP)?.widthSize ?: 0)
        Assert.assertEquals(20, getDivider(ItemDivider.Type.END)?.widthSize ?: 0)
        Assert.assertEquals(5, getDivider(ItemDivider.Type.BOTTOM)?.widthSize ?: 0)

        position = 1
        Assert.assertEquals(0, getDivider(ItemDivider.Type.START)?.widthSize ?: 0)
        Assert.assertEquals(10, getDivider(ItemDivider.Type.TOP)?.widthSize ?: 0)
        Assert.assertEquals(30, getDivider(ItemDivider.Type.END)?.widthSize ?: 0)
        Assert.assertEquals(5, getDivider(ItemDivider.Type.BOTTOM)?.widthSize ?: 0)

        position = 2
        Assert.assertEquals(25, getDivider(ItemDivider.Type.START)?.widthSize ?: 0)
        Assert.assertEquals(0, getDivider(ItemDivider.Type.TOP)?.widthSize ?: 0)
        Assert.assertEquals(30, getDivider(ItemDivider.Type.END)?.widthSize ?: 0)
        Assert.assertEquals(5, getDivider(ItemDivider.Type.BOTTOM)?.widthSize ?: 0)

        position = 3
        Assert.assertEquals(25, getDivider(ItemDivider.Type.START)?.widthSize ?: 0)
        Assert.assertEquals(0, getDivider(ItemDivider.Type.TOP)?.widthSize ?: 0)
        Assert.assertEquals(20, getDivider(ItemDivider.Type.END)?.widthSize ?: 0)
        Assert.assertEquals(5, getDivider(ItemDivider.Type.BOTTOM)?.widthSize ?: 0)

        position = 4
        Assert.assertEquals(0, getDivider(ItemDivider.Type.START)?.widthSize ?: 0)
        Assert.assertEquals(0, getDivider(ItemDivider.Type.TOP)?.widthSize ?: 0)
        Assert.assertEquals(20, getDivider(ItemDivider.Type.END)?.widthSize ?: 0)
        Assert.assertEquals(5, getDivider(ItemDivider.Type.BOTTOM)?.widthSize ?: 0)

        position = 5
        Assert.assertEquals(0, getDivider(ItemDivider.Type.START)?.widthSize ?: 0)
        Assert.assertEquals(0, getDivider(ItemDivider.Type.TOP)?.widthSize ?: 0)
        Assert.assertEquals(30, getDivider(ItemDivider.Type.END)?.widthSize ?: 0)
        Assert.assertEquals(5, getDivider(ItemDivider.Type.BOTTOM)?.widthSize ?: 0)

        position = 6
        Assert.assertEquals(25, getDivider(ItemDivider.Type.START)?.widthSize ?: 0)
        Assert.assertEquals(0, getDivider(ItemDivider.Type.TOP)?.widthSize ?: 0)
        Assert.assertEquals(20, getDivider(ItemDivider.Type.END)?.widthSize ?: 0)
        Assert.assertEquals(15, getDivider(ItemDivider.Type.BOTTOM)?.widthSize ?: 0)



        isVerticalOrientation = false

        position = 0
        Assert.assertEquals(10, getDivider(ItemDivider.Type.START)?.widthSize ?: 0)
        Assert.assertEquals(25, getDivider(ItemDivider.Type.TOP)?.widthSize ?: 0)
        Assert.assertEquals(5, getDivider(ItemDivider.Type.END)?.widthSize ?: 0)
        Assert.assertEquals(20, getDivider(ItemDivider.Type.BOTTOM)?.widthSize ?: 0)

        position = 1
        Assert.assertEquals(10, getDivider(ItemDivider.Type.START)?.widthSize ?: 0)
        Assert.assertEquals(0, getDivider(ItemDivider.Type.TOP)?.widthSize ?: 0)
        Assert.assertEquals(5, getDivider(ItemDivider.Type.END)?.widthSize ?: 0)
        Assert.assertEquals(30, getDivider(ItemDivider.Type.BOTTOM)?.widthSize ?: 0)

        position = 2
        Assert.assertEquals(0, getDivider(ItemDivider.Type.START)?.widthSize ?: 0)
        Assert.assertEquals(25, getDivider(ItemDivider.Type.TOP)?.widthSize ?: 0)
        Assert.assertEquals(5, getDivider(ItemDivider.Type.END)?.widthSize ?: 0)
        Assert.assertEquals(30, getDivider(ItemDivider.Type.BOTTOM)?.widthSize ?: 0)

        position = 3
        Assert.assertEquals(0, getDivider(ItemDivider.Type.START)?.widthSize ?: 0)
        Assert.assertEquals(25, getDivider(ItemDivider.Type.TOP)?.widthSize ?: 0)
        Assert.assertEquals(5, getDivider(ItemDivider.Type.END)?.widthSize ?: 0)
        Assert.assertEquals(20, getDivider(ItemDivider.Type.BOTTOM)?.widthSize ?: 0)

        position = 4
        Assert.assertEquals(0, getDivider(ItemDivider.Type.START)?.widthSize ?: 0)
        Assert.assertEquals(0, getDivider(ItemDivider.Type.TOP)?.widthSize ?: 0)
        Assert.assertEquals(5, getDivider(ItemDivider.Type.END)?.widthSize ?: 0)
        Assert.assertEquals(20, getDivider(ItemDivider.Type.BOTTOM)?.widthSize ?: 0)

        position = 5
        Assert.assertEquals(0, getDivider(ItemDivider.Type.START)?.widthSize ?: 0)
        Assert.assertEquals(0, getDivider(ItemDivider.Type.TOP)?.widthSize ?: 0)
        Assert.assertEquals(5, getDivider(ItemDivider.Type.END)?.widthSize ?: 0)
        Assert.assertEquals(30, getDivider(ItemDivider.Type.BOTTOM)?.widthSize ?: 0)

        position = 6
        Assert.assertEquals(0, getDivider(ItemDivider.Type.START)?.widthSize ?: 0)
        Assert.assertEquals(25, getDivider(ItemDivider.Type.TOP)?.widthSize ?: 0)
        Assert.assertEquals(15, getDivider(ItemDivider.Type.END)?.widthSize ?: 0)
        Assert.assertEquals(20, getDivider(ItemDivider.Type.BOTTOM)?.widthSize ?: 0)
    }
}