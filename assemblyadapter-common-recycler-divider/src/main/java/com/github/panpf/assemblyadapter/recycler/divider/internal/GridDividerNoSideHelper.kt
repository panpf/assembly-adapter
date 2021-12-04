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

class GridDividerNoSideHelper(
    val dividerConfig: ItemDividerConfig?,
    val headerDividerConfig: ItemDividerConfig?,
    val footerDividerConfig: ItemDividerConfig?,
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
        val dividerConfig = when (finalDividerType) {
            DividerSide.START -> null
            DividerSide.END -> null
            DividerSide.TOP -> if (params.isColumnFirst) headerDividerConfig else null
            DividerSide.BOTTOM -> if (params.isColumnLast) footerDividerConfig else dividerConfig
        }
        return dividerConfig?.get(params.parent, params.position, params.spanIndex)
            ?.let {
                ItemDividerWrapper(it, dividerType)
            }
    }

    override fun getItemOffsets(outRect: Rect, params: GridItemParams, fromStaggered: Boolean) {
        val startType = if (params.isLTRDirection) DividerSide.START else DividerSide.END
        val endType = if (params.isLTRDirection) DividerSide.END else DividerSide.START

        val startItemDivider = getItemDivider(params, startType, true, fromStaggered)
        val endItemDivider = getItemDivider(params, endType, true, fromStaggered)
        val topItemDivider = getItemDivider(params, DividerSide.TOP, true, fromStaggered)
        val bottomItemDivider = getItemDivider(params, DividerSide.BOTTOM, true, fromStaggered)

        val left = startItemDivider?.width ?: 0
        val right = endItemDivider?.width ?: 0
        val top = topItemDivider?.height ?: 0
        val bottom = bottomItemDivider?.height ?: 0
        outRect.set(left, top, right, bottom)
    }
}