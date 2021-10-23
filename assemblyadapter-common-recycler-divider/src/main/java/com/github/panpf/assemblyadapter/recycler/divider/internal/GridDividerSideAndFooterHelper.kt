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

class GridDividerSideAndFooterHelper(
    val dividerConfig: ItemDividerConfig?,
    val headerDividerConfig: ItemDividerConfig?,
    val footerDividerConfig: ItemDividerConfig?,
    val sideDividerConfig: ItemDividerConfig,
    val sideFooterDividerConfig: ItemDividerConfig,
) : GridDividerHelper() {

    override fun getItemOffsets(outRect: Rect, params: ItemParams) {
        val isLTRDirection = params.isLTRDirection
        val startType = if (isLTRDirection) ItemDivider.Type.START else ItemDivider.Type.END
        val endType = if (isLTRDirection) ItemDivider.Type.END else ItemDivider.Type.START

        val startItemDivider = getItemDivider(params, startType, true)
        val endItemDivider = getItemDivider(params, endType, true)
        val topItemDivider = getItemDivider(params, ItemDivider.Type.TOP, true)
        val bottomItemDivider = getItemDivider(params, ItemDivider.Type.BOTTOM, true)

        val left = startItemDivider?.widthSize ?: 0
        val right = endItemDivider?.widthSize ?: 0
        val top = topItemDivider?.heightSize ?: 0
        val bottom = bottomItemDivider?.heightSize ?: 0
        outRect.set(left, top, right, bottom)
    }

    override fun getItemDivider(
        params: ItemParams, dividerType: ItemDivider.Type, fromOffset: Boolean
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
        val dividerConfig = when (finalDividerType) {
            ItemDivider.Type.START -> null
            ItemDivider.Type.END -> if (params.isLastSpan) sideFooterDividerConfig else sideDividerConfig
            ItemDivider.Type.TOP -> if (params.isColumnFirst) headerDividerConfig else null
            ItemDivider.Type.BOTTOM -> if (params.isColumnEnd) footerDividerConfig else dividerConfig
        }
        return dividerConfig?.get(params.parent, params.position, params.spanIndex)
    }
}