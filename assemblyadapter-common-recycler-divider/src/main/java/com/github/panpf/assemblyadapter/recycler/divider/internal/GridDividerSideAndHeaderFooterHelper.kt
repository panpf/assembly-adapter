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
        dividerType: DividerSide,
        fromOffset: Boolean,
        fromStaggered: Boolean,
    ): ItemDividerWrapper? {
        val finalDividerType = if (params.isVerticalOrientation) {
            dividerType
        } else {
            when (dividerType) {
                DividerSide.START -> DividerSide.TOP
                DividerSide.END -> DividerSide.BOTTOM
                DividerSide.TOP -> DividerSide.START
                DividerSide.BOTTOM -> DividerSide.END
            }
        }
        // fromStaggered && !fromOffset:
        // Since the height of two adjacent items in StaggeredGridLayoutManager may be different,
        // Therefore, it is necessary to draw dividers for both the start and end of the item when drawing,
        // so that two adjacent items with inconsistent heights will always draw a higher divider.
        val dividerConfig = when (finalDividerType) {
            DividerSide.START -> when {
                params.isFirstSpan -> sideHeaderDividerConfig
                fromStaggered && !fromOffset -> sideDividerConfig
                else -> null
            }
            DividerSide.END -> if (params.isLastSpan) sideFooterDividerConfig else sideDividerConfig
            DividerSide.TOP -> if (params.isColumnFirst) headerDividerConfig else null
            DividerSide.BOTTOM -> if (params.isColumnLast) footerDividerConfig else dividerConfig
        }
        return dividerConfig?.get(params.parent, params.position, params.spanIndex)
            ?.let {
                ItemDividerWrapper(it, dividerType)
            }
    }

    override fun getItemOffsets(outRect: Rect, params: GridItemParams, fromStaggered: Boolean) {
        // 当我们希望显示 sideDivider, sideHeaderAndFooterDivider 并且 item 的宽度是 parent 的宽度减去所有 divider 后除以 spanCount 时，
        // 如公式：'val itemSize=(parentWidth - (dividerSize * (spanCount+1))) / spanCount'
        // 按照 GridItemDividerProvider 的逻辑，第一个 item 的 start 和 end 将都会有 divider 显示
        // 因为 GridLayoutManager 强制每个 item 的最大宽度为 parentWidth/spanCount，
        // 所以第一个 item 的宽度会因为加上 start 和 end 的 divider 后超过 GridLayoutManager 限制的最大宽度，
        // 这时 GridLayoutManager 会将 item 的宽度修改为最大宽度减去 start 和 end 的 divider，导致 item 最终的宽度不是我们希望的宽度
        // 所以以下的代码都是为了解决这个问题
        when {
            params.isFullSpan -> {
                val startType =
                    if (params.isLTRDirection) DividerSide.START else DividerSide.END
                val endType =
                    if (params.isLTRDirection) DividerSide.END else DividerSide.START
                val startItemDivider = getItemDivider(params, startType, true, fromStaggered)
                val endItemDivider = getItemDivider(params, endType, true, fromStaggered)
                val topItemDivider =
                    getItemDivider(params, DividerSide.TOP, true, fromStaggered)
                val bottomItemDivider =
                    getItemDivider(params, DividerSide.BOTTOM, true, fromStaggered)
                val left = startItemDivider?.width ?: 0
                val right = endItemDivider?.width ?: 0
                val top = topItemDivider?.height ?: 0
                val bottom = bottomItemDivider?.height ?: 0
                outRect.set(left, top, right, bottom)
            }
            params.isVerticalOrientation -> {
                val sideDivider =
                    sideDividerConfig.get(params.parent, params.position, params.spanIndex)!!
                val sideDividerSize = sideDivider.getWidth(true)
                val multiplier = sideDividerSize / params.spanCount.toFloat()
                val columnStart = if (params.spanSize > 1) {
                    params.spanIndex
                } else {
                    params.spanIndex + params.spanSize - 1
                }
                val columnEnd = params.spanIndex + params.spanSize - 1
                val left = if (params.isFirstSpan) {
                    sideDividerSize
                } else {
                    // This method allows the divider to always display enough width, but it will squeeze the display space of the item
//                    floor(multiplier * (params.spanCount - columnStart)).toInt()
                    (multiplier * (params.spanCount - columnStart)).toInt()
                }
                val right = if (params.isLastSpan) {
                    sideDividerSize
                } else {
                    val cellSideOffset = multiplier * (params.spanCount + 1)
                    // This method allows the divider to always display enough width, but it will squeeze the display space of the item
//                    ceil((cellSideOffset) - (multiplier * (params.spanCount - columnEnd))).toInt()
                    ((cellSideOffset) - (multiplier * (params.spanCount - columnEnd))).toInt()
                }
                val topItemDivider =
                    getItemDivider(params, DividerSide.TOP, true, fromStaggered)
                val bottomItemDivider =
                    getItemDivider(params, DividerSide.BOTTOM, true, fromStaggered)
                val top = topItemDivider?.height ?: 0
                val bottom = bottomItemDivider?.height ?: 0
                outRect.set(left, top, right, bottom)
            }
            else -> {
                val startType =
                    if (params.isLTRDirection) DividerSide.START else DividerSide.END
                val endType =
                    if (params.isLTRDirection) DividerSide.END else DividerSide.START
                val startItemDivider = getItemDivider(params, startType, true, fromStaggered)
                val endItemDivider = getItemDivider(params, endType, true, fromStaggered)
                val left = startItemDivider?.width ?: 0
                val right = endItemDivider?.width ?: 0
                val sideDivider =
                    sideDividerConfig.get(params.parent, params.position, params.spanIndex)!!
                val sideDividerSize = sideDivider.getHeight(false)
                val multiplier = sideDividerSize / params.spanCount.toFloat()
                val columnStart = if (params.spanSize > 1) {
                    params.spanIndex
                } else {
                    params.spanIndex + params.spanSize - 1
                }
                val columnEnd = params.spanIndex + params.spanSize - 1
                val top = if (params.isFirstSpan) {
                    sideDividerSize
                } else {
                    // This method allows the divider to always display enough width, but it will squeeze the display space of the item
//                    floor(multiplier * (params.spanCount - columnStart)).toInt()
                    (multiplier * (params.spanCount - columnStart)).toInt()
                }
                val bottom = if (params.isLastSpan) {
                    sideDividerSize
                } else {
                    // This method allows the divider to always display enough width, but it will squeeze the display space of the item
                    val cellSideOffset = multiplier * (params.spanCount + 1)
//                    ceil((cellSideOffset) - (multiplier * (params.spanCount - columnEnd))).toInt()
                    ((cellSideOffset) - (multiplier * (params.spanCount - columnEnd))).toInt()
                }
                outRect.set(left, top, right, bottom)
            }
        }
    }
}