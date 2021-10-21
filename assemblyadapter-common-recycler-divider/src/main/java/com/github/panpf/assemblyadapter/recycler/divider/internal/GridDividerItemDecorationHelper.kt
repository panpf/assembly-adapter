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

class GridDividerItemDecorationHelper(private val provider: GridItemDividerProvider) {

    fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        itemCount: Int,
        position: Int,
        spanCount: Int,
        spanSize: Int,
        spanIndex: Int,
        spanGroupCount: Int,
        spanGroupIndex: Int,
        isVerticalOrientation: Boolean,
        isLTRDirection: Boolean
    ) {
        val isFirstGroup = spanGroupIndex == 0
        val isLastGroup = spanGroupIndex == spanGroupCount - 1
        val isFullSpan = spanSize == spanCount
        val isFirstSpan = isFullSpan || spanIndex == 0
        val isLastSpan = isFullSpan || (spanIndex + spanSize) == spanCount

        val startItemDivider = provider.getItemDivider(
            view, parent, itemCount, position, spanCount, spanSize, spanIndex,
            isFullSpan, isFirstSpan, isLastSpan, spanGroupCount, spanGroupIndex,
            isFirstGroup, isLastGroup, isVerticalOrientation,
            if (isLTRDirection) ItemDivider.Type.START else ItemDivider.Type.END, true
        )
        val topItemDivider = provider.getItemDivider(
            view, parent, itemCount, position, spanCount, spanSize, spanIndex,
            isFullSpan, isFirstSpan, isLastSpan, spanGroupCount, spanGroupIndex,
            isFirstGroup, isLastGroup, isVerticalOrientation, ItemDivider.Type.TOP, true
        )
        val endItemDivider = provider.getItemDivider(
            view, parent, itemCount, position, spanCount, spanSize, spanIndex,
            isFullSpan, isFirstSpan, isLastSpan, spanGroupCount, spanGroupIndex,
            isFirstGroup, isLastGroup, isVerticalOrientation,
            if (isLTRDirection) ItemDivider.Type.END else ItemDivider.Type.START, true
        )
        val bottomItemDivider = provider.getItemDivider(
            view, parent, itemCount, position, spanCount, spanSize, spanIndex,
            isFullSpan, isFirstSpan, isLastSpan, spanGroupCount, spanGroupIndex,
            isFirstGroup, isLastGroup, isVerticalOrientation, ItemDivider.Type.BOTTOM, true
        )

        if (provider.isHaveSide && (provider.isHaveSideHeaderAndFooter)) {
            // 当我们希望显示 sideDivider, sideHeaderAndFooterDivider 并且 item 的宽度是 parent 的宽度减去所有 divider 后除以 spanCount 时，
            // 如公式：'val itemSize=(parentWidth - (dividerSize * (spanCount+1))) / spanCount'
            // 按照 GridItemDividerProvider 的逻辑，第一个 item 的 start 和 end 将都会有 divider 显示
            // 因为 GridLayoutManager 强制每个 item 的最大宽度为 parentWidth/spanCount，
            // 所以第一个 item 的宽度会因为加上 start 和 end 的 divider 后超过 GridLayoutManager 限制的最大宽度，
            // 这时 GridLayoutManager 会将 item 的宽度修改为最大宽度减去 start 和 end 的 divider，导致 item 最终的宽度不是我们希望的宽度
            // 所以以下的代码都是为了解决这个问题
            val showHeaderAndFooterSideDivider = provider.isHaveSideHeaderAndFooter
            if (isVerticalOrientation) {
                val sideDividerSize =
                    provider.sideDividerConfig!!.get(parent, position, spanIndex)!!.widthSize
                outRect.setKt(
                    left = when {
                        isFirstSpan -> sideDividerSize
                        else -> normalizedOffsetFromSize(
                            ItemDivider.Type.START, sideDividerSize, spanCount,
                            spanIndex + spanSize - 1, showHeaderAndFooterSideDivider
                        )
                    },
                    right = when {
                        isLastSpan -> sideDividerSize
                        else -> normalizedOffsetFromSize(
                            ItemDivider.Type.END, sideDividerSize, spanCount,
                            spanIndex + spanSize - 1, showHeaderAndFooterSideDivider
                        )
                    },
                    top = topItemDivider?.heightSize ?: 0,
                    bottom = bottomItemDivider?.heightSize ?: 0
                )
            } else {
                val sideDividerSize =
                    provider.sideDividerConfig!!.get(parent, position, spanIndex)!!.heightSize
                outRect.setKt(
                    left = startItemDivider?.widthSize ?: 0,
                    right = endItemDivider?.widthSize ?: 0,
                    top = when {
                        isFirstSpan -> sideDividerSize
                        else -> normalizedOffsetFromSize(
                            ItemDivider.Type.TOP, sideDividerSize, spanCount,
                            spanIndex + spanSize - 1, showHeaderAndFooterSideDivider
                        )
                    },
                    bottom = when {
                        isLastSpan -> sideDividerSize
                        else -> normalizedOffsetFromSize(
                            ItemDivider.Type.BOTTOM, sideDividerSize, spanCount,
                            spanIndex + spanSize - 1, showHeaderAndFooterSideDivider
                        )
                    }
                )
            }
        } else if (provider.isHaveSide && provider.isNoSideHeaderAndFooter) {
            when {
                isFullSpan -> {
                    outRect.setKt(
                        left = startItemDivider?.widthSize ?: 0,
                        top = topItemDivider?.heightSize ?: 0,
                        right = endItemDivider?.widthSize ?: 0,
                        bottom = bottomItemDivider?.heightSize ?: 0
                    )
                }
                isVerticalOrientation -> {
                    val sideDividerSize =
                        provider.sideDividerConfig!!.get(parent, position, spanIndex)!!.widthSize
                    val multiplier = sideDividerSize / spanCount.toFloat()
                    val column = spanIndex + spanSize - 1
                    outRect.setKt(
                        left = ceil(column * multiplier).toInt(),
                        right = floor(sideDividerSize - ((column + 1) * multiplier)).toInt(),
                        top = topItemDivider?.heightSize ?: 0,
                        bottom = bottomItemDivider?.heightSize ?: 0
                    )
                }
                else -> {
                    val sideDividerSize =
                        provider.sideDividerConfig!!.get(parent, position, spanIndex)!!.widthSize
                    val multiplier = sideDividerSize / spanCount.toFloat()
                    val column = spanIndex + spanSize - 1
                    outRect.setKt(
                        left = startItemDivider?.heightSize ?: 0,
                        right = endItemDivider?.heightSize ?: 0,
                        top = ceil(column * multiplier).toInt(),
                        bottom = floor(sideDividerSize - ((column + 1) * multiplier)).toInt()
                    )
                }
            }
        } else {
            outRect.setKt(
                left = startItemDivider?.widthSize ?: 0,
                top = topItemDivider?.heightSize ?: 0,
                right = endItemDivider?.widthSize ?: 0,
                bottom = bottomItemDivider?.heightSize ?: 0
            )
        }
    }

    fun drawItem(
        canvas: Canvas,
        view: View,
        parent: RecyclerView,
        itemCount: Int,
        position: Int,
        spanCount: Int,
        spanSize: Int,
        spanIndex: Int,
        spanGroupCount: Int,
        spanGroupIndex: Int,
        isVerticalOrientation: Boolean,
        isLTRDirection: Boolean
    ) {
        val isFirstGroup = spanGroupIndex == 0
        val isLastGroup = spanGroupIndex == spanGroupCount - 1
        val isFullSpan = spanSize == spanCount
        val isFirstSpan = isFullSpan || spanIndex == 0
        val isLastSpan = isFullSpan || (spanIndex + spanSize) == spanCount

        val startItemDivider = provider.getItemDivider(
            view, parent, itemCount, position, spanCount, spanSize, spanIndex,
            isFullSpan, isFirstSpan, isLastSpan, spanGroupCount, spanGroupIndex,
            isFirstGroup, isLastGroup, isVerticalOrientation,
            if (isLTRDirection) ItemDivider.Type.START else ItemDivider.Type.END, false
        )
        val topItemDivider = provider.getItemDivider(
            view, parent, itemCount, position, spanCount, spanSize, spanIndex,
            isFullSpan, isFirstSpan, isLastSpan, spanGroupCount, spanGroupIndex,
            isFirstGroup, isLastGroup, isVerticalOrientation, ItemDivider.Type.TOP, false
        )
        val endItemDivider = provider.getItemDivider(
            view, parent, itemCount, position, spanCount, spanSize, spanIndex,
            isFullSpan, isFirstSpan, isLastSpan, spanGroupCount, spanGroupIndex,
            isFirstGroup, isLastGroup, isVerticalOrientation,
            if (isLTRDirection) ItemDivider.Type.END else ItemDivider.Type.START, false
        )
        val bottomItemDivider = provider.getItemDivider(
            view, parent, itemCount, position, spanCount, spanSize, spanIndex,
            isFullSpan, isFirstSpan, isLastSpan, spanGroupCount, spanGroupIndex,
            isFirstGroup, isLastGroup, isVerticalOrientation, ItemDivider.Type.BOTTOM, false
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

fun Rect.setKt(left: Int, top: Int, right: Int, bottom: Int) {
    set(left, top, right, bottom)
}