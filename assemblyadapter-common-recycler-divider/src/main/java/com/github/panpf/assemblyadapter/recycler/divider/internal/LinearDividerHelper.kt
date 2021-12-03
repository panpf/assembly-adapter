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

import android.graphics.Canvas
import android.graphics.Rect

class LinearDividerHelper(
    val dividerConfig: ItemDividerConfig?,
    val headerDividerConfig: ItemDividerConfig?,
    val footerDividerConfig: ItemDividerConfig?,
    val sideHeaderDividerConfig: ItemDividerConfig?,
    val sideFooterDividerConfig: ItemDividerConfig?,
) {

    private fun getItemDivider(
        params: LinearItemParams,
        dividerType: DividerSide
    ): ItemDividerWrapper? {
        return if (params.isVerticalOrientation) {
            when (dividerType) {
                DividerSide.START -> sideHeaderDividerConfig
                DividerSide.TOP -> if (params.isFirst) headerDividerConfig else null
                DividerSide.END -> sideFooterDividerConfig
                DividerSide.BOTTOM -> if (params.isLast) footerDividerConfig else dividerConfig
            }
        } else {
            when (dividerType) {
                DividerSide.START -> if (params.isFirst) headerDividerConfig else null
                DividerSide.TOP -> sideHeaderDividerConfig
                DividerSide.END -> if (params.isLast) footerDividerConfig else dividerConfig
                DividerSide.BOTTOM -> sideFooterDividerConfig
            }
        }?.get(params.parent, params.position, 0)
            ?.let {
                ItemDividerWrapper(it, dividerType)
            }
    }

    fun getItemOffsets(outRect: Rect, params: LinearItemParams) {
        val startType = if (params.isLTRDirection) DividerSide.START else DividerSide.END
        val endType = if (params.isLTRDirection) DividerSide.END else DividerSide.START
        val startItemDivider = getItemDivider(params, startType)
        val endItemDivider = getItemDivider(params, endType)
        val topItemDivider = getItemDivider(params, DividerSide.TOP)
        val bottomItemDivider = getItemDivider(params, DividerSide.BOTTOM)
        val startItemDividerSize = startItemDivider?.widthSize ?: 0
        val endItemDividerSize = endItemDivider?.widthSize ?: 0
        val topItemDividerSize = topItemDivider?.heightSize ?: 0
        val bottomItemDividerSize = bottomItemDivider?.heightSize ?: 0
        outRect.set(
            startItemDividerSize,
            topItemDividerSize,
            endItemDividerSize,
            bottomItemDividerSize
        )
    }

    fun drawItem(canvas: Canvas, params: LinearItemParams) {
        val view = params.view
        val startType = if (params.isLTRDirection) DividerSide.START else DividerSide.END
        val endType = if (params.isLTRDirection) DividerSide.END else DividerSide.START
        val startItemDivider = getItemDivider(params, startType)
        val endItemDivider = getItemDivider(params, endType)
        val topItemDivider = getItemDivider(params, DividerSide.TOP)
        val bottomItemDivider = getItemDivider(params, DividerSide.BOTTOM)
        val startItemDividerSize = startItemDivider?.widthSize ?: 0
        val endItemDividerSize = endItemDivider?.widthSize ?: 0
        val topItemDividerSize = topItemDivider?.heightSize ?: 0
        val bottomItemDividerSize = bottomItemDivider?.heightSize ?: 0

        if (params.isVerticalOrientation) {
            startItemDivider?.apply {
                draw(
                    canvas,
                    view.left - insetEnd - drawableWidthSize,
                    view.top - topItemDividerSize + insetTop,
                    view.left - insetEnd,
                    view.bottom + bottomItemDividerSize - insetBottom
                )
            }
            endItemDivider?.apply {
                draw(
                    canvas,
                    view.right + insetStart,
                    view.top - topItemDividerSize + insetTop,
                    view.right + insetStart + drawableWidthSize,
                    view.bottom + bottomItemDividerSize - insetBottom
                )
            }
            topItemDivider?.apply {
                draw(
                    canvas,
                    view.left + insetStart,
                    view.top - insetBottom - drawableHeightSize,
                    view.right - insetEnd,
                    view.top - insetBottom
                )
            }
            bottomItemDivider?.apply {
                // todo support shortDivider
//                val drawableWidthSize = drawableWidthSize
//                val drawableHeightSize = drawableHeightSize
//                if (drawableWidthSize > 0) {
//                    val left = view.left + ((view.width - drawableWidthSize) / 2)
//                    draw(
//                        canvas,
//                        left.coerceAtLeast(view.left + insetStart),
//                        view.bottom + insetTop,
//                        (left + drawableWidthSize).coerceAtMost(view.right - insetEnd),
//                        view.bottom + insetTop + drawableHeightSize
//                    )
//                } else {
                    draw(
                        canvas,
                        view.left + insetStart,
                        view.bottom + insetTop,
                        view.right - insetEnd,
                        view.bottom + insetTop + drawableHeightSize
                    )
//                }
            }
        } else {
            startItemDivider?.apply {
                draw(
                    canvas,
                    view.left - insetEnd - drawableWidthSize,
                    view.top + insetTop,
                    view.left - insetEnd,
                    view.bottom - insetBottom
                )
            }
            endItemDivider?.apply {
                draw(
                    canvas,
                    view.right + insetStart,
                    view.top + insetTop,
                    view.right + insetStart + drawableWidthSize,
                    view.bottom - insetBottom
                )
            }
            topItemDivider?.apply {
                draw(
                    canvas,
                    view.left - startItemDividerSize + insetStart,
                    view.top - insetBottom - drawableHeightSize,
                    view.right + endItemDividerSize - insetEnd,
                    view.top - insetBottom
                )
            }
            bottomItemDivider?.apply {
                draw(
                    canvas,
                    view.left - startItemDividerSize + insetStart,
                    view.bottom + insetTop,
                    view.right + endItemDividerSize - insetEnd,
                    view.bottom + insetTop + drawableHeightSize
                )
            }
        }
    }
}