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

class GridDividerSideAndHeaderHelper(
    val dividerConfig: ItemDividerConfig?,
    val headerDividerConfig: ItemDividerConfig?,
    val footerDividerConfig: ItemDividerConfig?,
    val sideDividerConfig: ItemDividerConfig,
    val sideHeaderDividerConfig: ItemDividerConfig,
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
            ItemDivider.Type.START -> if (params.isFirstSpan) sideHeaderDividerConfig else sideDividerConfig
            ItemDivider.Type.END -> if (fromStaggered && !fromOffset) sideDividerConfig else null
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

        val left = startItemDivider?.widthSize ?: 0
        val right = endItemDivider?.widthSize ?: 0
        val top = topItemDivider?.heightSize ?: 0
        val bottom = bottomItemDivider?.heightSize ?: 0
        outRect.set(left, top, right, bottom)
    }
}