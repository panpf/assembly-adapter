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
        val startItemDividerSize = startItemDivider?.width ?: 0
        val endItemDividerSize = endItemDivider?.width ?: 0
        val topItemDividerSize = topItemDivider?.height ?: 0
        val bottomItemDividerSize = bottomItemDivider?.height ?: 0
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