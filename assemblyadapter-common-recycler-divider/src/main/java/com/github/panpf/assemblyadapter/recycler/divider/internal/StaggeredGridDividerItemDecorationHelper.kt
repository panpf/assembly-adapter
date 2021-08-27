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
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlin.math.ceil
import kotlin.math.floor

class StaggeredGridDividerItemDecorationHelper(private val itemDividerProvider: StaggeredGridItemDividerProvider) {

    fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        itemCount: Int,
        position: Int,
        spanCount: Int,
        isFullSpan: Boolean,
        spanIndex: Int,
        isColumnFirst: Boolean,
        isColumnEnd: Boolean,
        isVerticalOrientation: Boolean,
        isLTRDirection: Boolean
    ) {
        val isFirstSpan = isFullSpan || spanIndex == 0
        val isLastSpan = isFullSpan || spanIndex == spanCount - 1

        val startItemDivider = itemDividerProvider.getItemDivider(
            view, parent, itemCount, position, spanCount, spanIndex, isFullSpan, isFirstSpan,
            isLastSpan, isColumnFirst, isColumnEnd, isVerticalOrientation,
            if (isLTRDirection) ItemDivider.Type.START else ItemDivider.Type.END
        )
        val topItemDivider = itemDividerProvider.getItemDivider(
            view, parent, itemCount, position, spanCount, spanIndex, isFullSpan, isFirstSpan,
            isLastSpan, isColumnFirst, isColumnEnd, isVerticalOrientation, ItemDivider.Type.TOP
        )
        val endItemDivider = itemDividerProvider.getItemDivider(
            view, parent, itemCount, position, spanCount, spanIndex, isFullSpan, isFirstSpan,
            isLastSpan, isColumnFirst, isColumnEnd, isVerticalOrientation,
            if (isLTRDirection) ItemDivider.Type.END else ItemDivider.Type.START
        )
        val bottomItemDivider = itemDividerProvider.getItemDivider(
            view, parent, itemCount, position, spanCount, spanIndex, isFullSpan, isFirstSpan,
            isLastSpan, isColumnFirst, isColumnEnd, isVerticalOrientation, ItemDivider.Type.BOTTOM
        )
        val startItemDividerSize = startItemDivider?.widthSize ?: 0
        val topItemDividerSize = topItemDivider?.heightSize ?: 0
        val endItemDividerSize = endItemDivider?.widthSize ?: 0
        val bottomItemDividerSize = bottomItemDivider?.heightSize ?: 0

        if (isVerticalOrientation) {
            when {
                isFullSpan -> {
                    outRect.set(
                        startItemDividerSize,
                        topItemDividerSize,
                        endItemDividerSize,
                        bottomItemDividerSize
                    )
                }
                isFirstSpan -> {
                    outRect.set(
                        startItemDividerSize,
                        topItemDividerSize,
                        floor(endItemDividerSize / 2f).toInt(),
                        bottomItemDividerSize
                    )
                }
                isLastSpan -> {
                    outRect.set(
                        ceil(startItemDividerSize / 2f).toInt(),
                        topItemDividerSize,
                        endItemDividerSize,
                        bottomItemDividerSize
                    )
                }
                else -> {
                    outRect.set(
                        ceil(startItemDividerSize / 2f).toInt(),
                        topItemDividerSize,
                        floor(endItemDividerSize / 2f).toInt(),
                        bottomItemDividerSize
                    )
                }
            }
        } else {
            when {
                isFullSpan -> {
                    outRect.set(
                        startItemDividerSize,
                        topItemDividerSize,
                        endItemDividerSize,
                        bottomItemDividerSize
                    )
                }
                isFirstSpan -> {
                    outRect.set(
                        startItemDividerSize,
                        topItemDividerSize,
                        endItemDividerSize,
                        floor(bottomItemDividerSize / 2f).toInt()
                    )
                }
                isLastSpan -> {
                    outRect.set(
                        startItemDividerSize,
                        ceil(topItemDividerSize / 2f).toInt(),
                        endItemDividerSize,
                        bottomItemDividerSize
                    )
                }
                else -> {
                    outRect.set(
                        startItemDividerSize,
                        ceil(topItemDividerSize / 2f).toInt(),
                        endItemDividerSize,
                        floor(bottomItemDividerSize / 2f).toInt()
                    )
                }
            }
        }
    }

    fun drawItem(
        canvas: Canvas,
        view: View,
        parent: RecyclerView,
        itemCount: Int,
        position: Int,
        spanCount: Int,
        isFullSpan: Boolean,
        spanIndex: Int,
        isColumnFirst: Boolean,
        isColumnEnd: Boolean,
        isVerticalOrientation: Boolean,
        isLTRDirection: Boolean
    ) {
        val isFirstSpan = isFullSpan || spanIndex == 0
        val isLastSpan = isFullSpan || spanIndex == spanCount - 1

        val startItemDivider = itemDividerProvider.getItemDivider(
            view, parent, itemCount, position, spanCount, spanIndex, isFullSpan, isFirstSpan,
            isLastSpan, isColumnFirst, isColumnEnd, isVerticalOrientation,
            if (isLTRDirection) ItemDivider.Type.START else ItemDivider.Type.END
        )
        val topItemDivider = itemDividerProvider.getItemDivider(
            view, parent, itemCount, position, spanCount, spanIndex, isFullSpan, isFirstSpan,
            isLastSpan, isColumnFirst, isColumnEnd, isVerticalOrientation, ItemDivider.Type.TOP
        )
        val endItemDivider = itemDividerProvider.getItemDivider(
            view, parent, itemCount, position, spanCount, spanIndex, isFullSpan, isFirstSpan,
            isLastSpan, isColumnFirst, isColumnEnd, isVerticalOrientation,
            if (isLTRDirection) ItemDivider.Type.END else ItemDivider.Type.START
        )
        val bottomItemDivider = itemDividerProvider.getItemDivider(
            view,
            parent,
            itemCount,
            position,
            spanCount,
            spanIndex,
            isFullSpan,
            isFirstSpan,
            isLastSpan,
            isColumnFirst,
            isColumnEnd,
            isVerticalOrientation,
            ItemDivider.Type.BOTTOM
        )
        val startItemDividerSize = startItemDivider?.widthSize ?: 0
        val topItemDividerSize = topItemDivider?.heightSize ?: 0
        val endItemDividerSize = endItemDivider?.widthSize ?: 0
        val bottomItemDividerSize = bottomItemDivider?.heightSize ?: 0

        if (isVerticalOrientation) {
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
                draw(
                    canvas,
                    view.left + insetStart,
                    view.bottom + insetTop,
                    view.right - insetEnd,
                    view.bottom + insetTop + drawableHeightSize
                )
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