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

class GridDividerOnlySideHelper(
    val dividerConfig: ItemDividerConfig?,
    val headerDividerConfig: ItemDividerConfig?,
    val footerDividerConfig: ItemDividerConfig?,
    val sideDividerConfig: ItemDividerConfig,
) : GridDividerHelper() {

    override fun getItemDivider(
        params: ItemParams,
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
                params.isFirstSpan -> null
                !fromStaggered && fromOffset -> sideDividerConfig
                fromStaggered && !fromOffset -> sideDividerConfig
                else -> null
            }
            ItemDivider.Type.END -> if (params.isLastSpan) null else sideDividerConfig
            ItemDivider.Type.TOP -> if (params.isColumnFirst) headerDividerConfig else null
            ItemDivider.Type.BOTTOM -> if (params.isColumnEnd) footerDividerConfig else dividerConfig
        }
        return dividerConfig?.get(params.parent, params.position, params.spanIndex)
    }

    override fun getItemOffsets(outRect: Rect, params: ItemParams, fromStaggered: Boolean) {
        val isLTRDirection = params.isLTRDirection
        val startType = if (isLTRDirection) ItemDivider.Type.START else ItemDivider.Type.END
        val endType = if (isLTRDirection) ItemDivider.Type.END else ItemDivider.Type.START

        val startItemDivider = getItemDivider(params, startType, true, fromStaggered)
        val endItemDivider = getItemDivider(params, endType, true, fromStaggered)
        val topItemDivider = getItemDivider(params, ItemDivider.Type.TOP, true, fromStaggered)
        val bottomItemDivider = getItemDivider(params, ItemDivider.Type.BOTTOM, true, fromStaggered)

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
                val column = params.spanIndex + params.spanSize - 1
                // This method allows the divider to always display enough width, but it will squeeze the display space of the item
//                val left = ceil(column * multiplier).toInt()
//                val right = floor(sideDividerSize - ((column + 1) * multiplier)).toInt()
                val left = (column * multiplier).toInt()
                val right = (sideDividerSize - ((column + 1) * multiplier)).toInt()
                val top = topItemDivider?.heightSize ?: 0
                val bottom = bottomItemDivider?.heightSize ?: 0
                outRect.set(left, top, right, bottom)
            }
            else -> {
                val sideDivider =
                    sideDividerConfig.get(params.parent, params.position, params.spanIndex)!!
                val sideDividerSize = sideDivider.heightSize
                val multiplier = sideDividerSize / params.spanCount.toFloat()
                val column = params.spanIndex + params.spanSize - 1
                val left = startItemDivider?.heightSize ?: 0
                val right = endItemDivider?.heightSize ?: 0
                // This method allows the divider to always display enough width, but it will squeeze the display space of the item
//                val top = ceil(column * multiplier).toInt()
//                val bottom = floor(sideDividerSize - ((column + 1) * multiplier)).toInt()
                val top = (column * multiplier).toInt()
                val bottom = (sideDividerSize - ((column + 1) * multiplier)).toInt()
                outRect.set(left, top, right, bottom)
            }
        }
    }
}