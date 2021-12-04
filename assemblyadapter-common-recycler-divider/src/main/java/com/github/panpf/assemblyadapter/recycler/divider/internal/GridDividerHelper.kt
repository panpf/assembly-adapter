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

abstract class GridDividerHelper {

    protected abstract fun getItemDivider(
        params: GridItemParams,
        dividerType: DividerSide,
        fromOffset: Boolean,
        fromStaggered: Boolean,
    ): ItemDividerWrapper?

    abstract fun getItemOffsets(outRect: Rect, params: GridItemParams, fromStaggered: Boolean)

    fun drawItem(canvas: Canvas, params: GridItemParams, fromStaggered: Boolean) {
        val view = params.view
        val startType = if (params.isLTRDirection) DividerSide.START else DividerSide.END
        val endType = if (params.isLTRDirection) DividerSide.END else DividerSide.START
        val startItemDivider = getItemDivider(params, startType, false, fromStaggered)
        val endItemDivider = getItemDivider(params, endType, false, fromStaggered)
        val topItemDivider = getItemDivider(params, DividerSide.TOP, false, fromStaggered)
        val bottomItemDivider =
            getItemDivider(params, DividerSide.BOTTOM, false, fromStaggered)
        val startItemDividerSize = startItemDivider?.width ?: 0
        val endItemDividerSize = endItemDivider?.width ?: 0
        val topItemDividerSize = topItemDivider?.height ?: 0
        val bottomItemDividerSize = bottomItemDivider?.height ?: 0

        if (params.isVerticalOrientation) {
            startItemDivider?.apply {
                val drawableWidthSize = drawableWidth
                val drawableHeightSize = drawableHeight
                if (drawableHeightSize > 0) {
                    val top = view.top + ((view.height - drawableHeightSize) / 2)
                    draw(
                        canvas = canvas,
                        left = view.left - insetEnd - drawableWidthSize,
                        right = view.left - insetEnd,
                        top = top.coerceAtLeast(view.top - topItemDividerSize + insetTop),
                        bottom = (top + drawableHeightSize).coerceAtMost(view.bottom + bottomItemDividerSize - insetBottom)
                    )
                } else {
                    draw(
                        canvas = canvas,
                        left = view.left - insetEnd - drawableWidthSize,
                        right = view.left - insetEnd,
                        top = view.top - topItemDividerSize + insetTop,
                        bottom = view.bottom + bottomItemDividerSize - insetBottom
                    )
                }
            }
            endItemDivider?.apply {
                val drawableWidthSize = drawableWidth
                val drawableHeightSize = drawableHeight
                if (drawableHeightSize > 0) {
                    val top = view.top + ((view.height - drawableHeightSize) / 2)
                    draw(
                        canvas = canvas,
                        left = view.right + insetStart,
                        right = view.right + insetStart + drawableWidthSize,
                        top = top.coerceAtLeast(view.top - topItemDividerSize + insetTop),
                        bottom = (top + drawableHeightSize).coerceAtMost(view.bottom + bottomItemDividerSize - insetBottom)
                    )
                } else {
                    draw(
                        canvas = canvas,
                        left = view.right + insetStart,
                        right = view.right + insetStart + drawableWidthSize,
                        top = view.top - topItemDividerSize + insetTop,
                        bottom = view.bottom + bottomItemDividerSize - insetBottom
                    )
                }
            }
            topItemDivider?.apply {
                val drawableWidthSize = drawableWidth
                val drawableHeightSize = drawableHeight
                if (drawableWidthSize > 0) {
                    val left = view.left + ((view.width - drawableWidthSize) / 2)
                    draw(
                        canvas = canvas,
                        left = left.coerceAtLeast(view.left + insetStart),
                        right = (left + drawableWidthSize).coerceAtMost(view.right - insetEnd),
                        top = view.top - insetBottom - drawableHeightSize,
                        bottom = view.top - insetBottom
                    )
                } else {
                    draw(
                        canvas = canvas,
                        left = view.left + insetStart,
                        right = view.right - insetEnd,
                        top = view.top - insetBottom - drawableHeightSize,
                        bottom = view.top - insetBottom
                    )
                }
            }
            bottomItemDivider?.apply {
                val drawableWidthSize = drawableWidth
                val drawableHeightSize = drawableHeight
                if (drawableWidthSize > 0) {
                    val left = view.left + ((view.width - drawableWidthSize) / 2)
                    draw(
                        canvas = canvas,
                        left = left.coerceAtLeast(view.left + insetStart),
                        right = (left + drawableWidthSize).coerceAtMost(view.right - insetEnd),
                        top = view.bottom + insetTop,
                        bottom = view.bottom + insetTop + drawableHeightSize
                    )
                } else {
                    draw(
                        canvas = canvas,
                        left = view.left + insetStart,
                        right = view.right - insetEnd,
                        top = view.bottom + insetTop,
                        bottom = view.bottom + insetTop + drawableHeightSize
                    )
                }
            }
        } else {
            startItemDivider?.apply {
                val drawableWidthSize = drawableWidth
                val drawableHeightSize = drawableHeight
                if (drawableHeightSize > 0) {
                    val top = view.top + ((view.height - drawableHeightSize) / 2)
                    draw(
                        canvas = canvas,
                        left = view.left - insetEnd - drawableWidthSize,
                        right = view.left - insetEnd,
                        top = top.coerceAtLeast(view.top + insetTop),
                        bottom = (top + drawableHeightSize).coerceAtMost(view.bottom - insetBottom)
                    )
                } else {
                    draw(
                        canvas = canvas,
                        left = view.left - insetEnd - drawableWidthSize,
                        right = view.left - insetEnd,
                        top = view.top + insetTop,
                        bottom = view.bottom - insetBottom
                    )
                }
            }
            endItemDivider?.apply {
                val drawableWidthSize = drawableWidth
                val drawableHeightSize = drawableHeight
                if (drawableHeightSize > 0) {
                    val top = view.top + ((view.height - drawableHeightSize) / 2)
                    draw(
                        canvas = canvas,
                        left = view.right + insetStart,
                        right = view.right + insetStart + drawableWidthSize,
                        top = top.coerceAtLeast(view.top + insetTop),
                        bottom = (top + drawableHeightSize).coerceAtMost(view.bottom - insetBottom)
                    )
                } else {
                    draw(
                        canvas = canvas,
                        left = view.right + insetStart,
                        right = view.right + insetStart + drawableWidthSize,
                        top = view.top + insetTop,
                        bottom = view.bottom - insetBottom
                    )
                }
            }
            topItemDivider?.apply {
                val drawableWidthSize = drawableWidth
                val drawableHeightSize = drawableHeight
                if (drawableWidthSize > 0) {
                    val left = view.left + ((view.width - drawableWidthSize) / 2)
                    draw(
                        canvas = canvas,
                        left = left.coerceAtLeast(view.left - startItemDividerSize + insetStart),
                        right = (left + drawableWidthSize).coerceAtMost(view.right + endItemDividerSize - insetEnd),
                        top = view.top - insetBottom - drawableHeightSize,
                        bottom = view.top - insetBottom
                    )
                } else {
                    draw(
                        canvas = canvas,
                        left = view.left - startItemDividerSize + insetStart,
                        right = view.right + endItemDividerSize - insetEnd,
                        top = view.top - insetBottom - drawableHeightSize,
                        bottom = view.top - insetBottom
                    )
                }
            }
            bottomItemDivider?.apply {
                val drawableWidthSize = drawableWidth
                val drawableHeightSize = drawableHeight
                if (drawableWidthSize > 0) {
                    val left = view.left + ((view.width - drawableWidthSize) / 2)
                    draw(
                        canvas = canvas,
                        left = left.coerceAtLeast(view.left - startItemDividerSize + insetStart),
                        right = (left + drawableWidthSize).coerceAtMost(view.right + endItemDividerSize - insetEnd),
                        top = view.bottom + insetTop,
                        bottom = view.bottom + insetTop + drawableHeightSize
                    )
                } else {
                    draw(
                        canvas = canvas,
                        left = view.left - startItemDividerSize + insetStart,
                        right = view.right + endItemDividerSize - insetEnd,
                        top = view.bottom + insetTop,
                        bottom = view.bottom + insetTop + drawableHeightSize
                    )
                }
            }
        }
    }
}