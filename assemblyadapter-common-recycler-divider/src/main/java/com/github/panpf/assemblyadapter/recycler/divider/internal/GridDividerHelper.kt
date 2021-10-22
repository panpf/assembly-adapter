package com.github.panpf.assemblyadapter.recycler.divider.internal

import android.graphics.Canvas
import android.graphics.Rect

abstract class GridDividerHelper {

    protected abstract fun getItemDivider(
        params: ItemParams, dividerType: ItemDivider.Type, fromOffset: Boolean
    ): ItemDivider?

    abstract fun getItemOffsets(outRect: Rect, params: ItemParams)

    fun drawItem(canvas: Canvas, params: ItemParams) {
        val isLTRDirection = params.isLTRDirection
        val view = params.view
        val startType = if (isLTRDirection) ItemDivider.Type.START else ItemDivider.Type.END
        val endType = if (isLTRDirection) ItemDivider.Type.END else ItemDivider.Type.START
        val startItemDivider = getItemDivider(params, startType, false)
        val endItemDivider = getItemDivider(params, endType, false)
        val topItemDivider = getItemDivider(params, ItemDivider.Type.TOP, false)
        val bottomItemDivider = getItemDivider(params, ItemDivider.Type.BOTTOM, false)
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