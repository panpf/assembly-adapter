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
package com.github.panpf.assemblyadapter.recycler.divider.internal

import android.graphics.Rect

class GridDividerSideAndHeaderFooterHelper(
    val dividerConfig: ItemDividerConfig?,
    val headerDividerConfig: ItemDividerConfig?,
    val footerDividerConfig: ItemDividerConfig?,
    val sideDividerConfig: ItemDividerConfig,
    val sideHeaderDividerConfig: ItemDividerConfig,
    val sideFooterDividerConfig: ItemDividerConfig,
) : GridDividerHelper() {

    override fun getItemDivider(
        params: GridItemParams,
        dividerType: ItemDivider.Type,
        fromOffset: Boolean,
        fromStaggered: Boolean,
    ): ItemDivider? {
        val finalDividerType = if (params.isVerticalOrientation) {
            dividerType
        } else {
            when (dividerType) {
                ItemDivider.Type.START -> ItemDivider.Type.TOP
                ItemDivider.Type.END -> ItemDivider.Type.BOTTOM
                ItemDivider.Type.TOP -> ItemDivider.Type.START
                ItemDivider.Type.BOTTOM -> ItemDivider.Type.END
            }
        }
        // fromStaggered && !fromOffset:
        // Since the height of two adjacent items in StaggeredGridLayoutManager may be different,
        // Therefore, it is necessary to draw dividers for both the start and end of the item when drawing,
        // so that two adjacent items with inconsistent heights will always draw a higher divider.
        val dividerConfig = when (finalDividerType) {
            ItemDivider.Type.START -> when {
                params.isFirstSpan -> sideHeaderDividerConfig
                fromStaggered && !fromOffset -> sideDividerConfig
                else -> null
            }
            ItemDivider.Type.END -> if (params.isLastSpan) sideFooterDividerConfig else sideDividerConfig
            ItemDivider.Type.TOP -> if (params.isColumnFirst) headerDividerConfig else null
            ItemDivider.Type.BOTTOM -> if (params.isColumnLast) footerDividerConfig else dividerConfig
        }
        return dividerConfig?.get(params.parent, params.position, params.spanIndex)
    }

    override fun getItemOffsets(outRect: Rect, params: GridItemParams, fromStaggered: Boolean) {
        val startType = if (params.isLTRDirection) ItemDivider.Type.START else ItemDivider.Type.END
        val endType = if (params.isLTRDirection) ItemDivider.Type.END else ItemDivider.Type.START
        val startItemDivider = getItemDivider(params, startType, true, fromStaggered)
        val endItemDivider = getItemDivider(params, endType, true, fromStaggered)
        val topItemDivider = getItemDivider(params, ItemDivider.Type.TOP, true, fromStaggered)
        val bottomItemDivider = getItemDivider(params, ItemDivider.Type.BOTTOM, true, fromStaggered)

// 当我们希望显示 sideDivider, sideHeaderAndFooterDivider 并且 item 的宽度是 parent 的宽度减去所有 divider 后除以 spanCount 时，
        // 如公式：'val itemSize=(parentWidth - (dividerSize * (spanCount+1))) / spanCount'
        // 按照 GridItemDividerProvider 的逻辑，第一个 item 的 start 和 end 将都会有 divider 显示
        // 因为 GridLayoutManager 强制每个 item 的最大宽度为 parentWidth/spanCount，
        // 所以第一个 item 的宽度会因为加上 start 和 end 的 divider 后超过 GridLayoutManager 限制的最大宽度，
        // 这时 GridLayoutManager 会将 item 的宽度修改为最大宽度减去 start 和 end 的 divider，导致 item 最终的宽度不是我们希望的宽度
        // 所以以下的代码都是为了解决这个问题
        val column = params.spanIndex + params.spanSize - 1
        when {
            params.isFullSpan -> {
                val left = startItemDivider?.widthSize ?: 0
                val right = endItemDivider?.widthSize ?: 0
                val top = topItemDivider?.heightSize ?: 0
                val bottom = bottomItemDivider?.heightSize ?: 0
                outRect.set(left, top, right, bottom)
            }
            params.isVerticalOrientation -> {
                val sideDivider =
                    sideDividerConfig.get(params.parent, params.position, params.spanIndex)!!
                val sideDividerSize = sideDivider.widthSize
                val multiplier = sideDividerSize / params.spanCount.toFloat()
                val left = if (params.isFirstSpan) {
                    sideDividerSize
                } else {
                    // This method allows the divider to always display enough width, but it will squeeze the display space of the item
//                    floor(multiplier * (params.spanCount - column)).toInt()
                    (multiplier * (params.spanCount - column)).toInt()
                }
                val right = if (params.isLastSpan) {
                    sideDividerSize
                } else {
                    val cellSideOffset = multiplier * (params.spanCount + 1)
                    // This method allows the divider to always display enough width, but it will squeeze the display space of the item
//                    ceil((cellSideOffset) - (multiplier * (params.spanCount - column))).toInt()
                    ((cellSideOffset) - (multiplier * (params.spanCount - column))).toInt()
                }
                val top = topItemDivider?.heightSize ?: 0
                val bottom = bottomItemDivider?.heightSize ?: 0
                outRect.set(left, top, right, bottom)
            }
            else -> {
                val sideDivider =
                    sideDividerConfig.get(params.parent, params.position, params.spanIndex)!!
                val sideDividerSize = sideDivider.heightSize
                val multiplier = sideDividerSize / params.spanCount.toFloat()
                val left = startItemDivider?.widthSize ?: 0
                val right = endItemDivider?.widthSize ?: 0
                val top = if (params.isFirstSpan) {
                    sideDividerSize
                } else {
                    // This method allows the divider to always display enough width, but it will squeeze the display space of the item
//                    floor(multiplier * (params.spanCount - column)).toInt()
                    (multiplier * (params.spanCount - column)).toInt()
                }
                val bottom = if (params.isLastSpan) {
                    sideDividerSize
                } else {
                    // This method allows the divider to always display enough width, but it will squeeze the display space of the item
                    val cellSideOffset = multiplier * (params.spanCount + 1)
//                    ceil((cellSideOffset) - (multiplier * (params.spanCount - column))).toInt()
                    ((cellSideOffset) - (multiplier * (params.spanCount - column))).toInt()
                }
                outRect.set(left, top, right, bottom)
            }
        }
    }
}