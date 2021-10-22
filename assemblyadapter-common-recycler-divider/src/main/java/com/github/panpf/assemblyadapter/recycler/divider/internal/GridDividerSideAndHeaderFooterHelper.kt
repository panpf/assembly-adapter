package com.github.panpf.assemblyadapter.recycler.divider.internal

import android.graphics.Rect
import kotlin.math.ceil
import kotlin.math.floor

class GridDividerSideAndHeaderFooterHelper(
    val dividerConfig: ItemDividerConfig?,
    val headerDividerConfig: ItemDividerConfig?,
    val footerDividerConfig: ItemDividerConfig?,
    val sideDividerConfig: ItemDividerConfig,
    val sideHeaderDividerConfig: ItemDividerConfig,
    val sideFooterDividerConfig: ItemDividerConfig
) : GridDividerHelper() {

    override fun getItemOffsets(outRect: Rect, params: ItemParams) {
        val isLTRDirection = params.isLTRDirection
        val startType = if (isLTRDirection) ItemDivider.Type.START else ItemDivider.Type.END
        val endType = if (isLTRDirection) ItemDivider.Type.END else ItemDivider.Type.START
        val startItemDivider = getItemDivider(params, startType, true)
        val endItemDivider = getItemDivider(params, endType, true)
        val topItemDivider = getItemDivider(params, ItemDivider.Type.TOP, true)
        val bottomItemDivider = getItemDivider(params, ItemDivider.Type.BOTTOM, true)

// 当我们希望显示 sideDivider, sideHeaderAndFooterDivider 并且 item 的宽度是 parent 的宽度减去所有 divider 后除以 spanCount 时，
        // 如公式：'val itemSize=(parentWidth - (dividerSize * (spanCount+1))) / spanCount'
        // 按照 GridItemDividerProvider 的逻辑，第一个 item 的 start 和 end 将都会有 divider 显示
        // 因为 GridLayoutManager 强制每个 item 的最大宽度为 parentWidth/spanCount，
        // 所以第一个 item 的宽度会因为加上 start 和 end 的 divider 后超过 GridLayoutManager 限制的最大宽度，
        // 这时 GridLayoutManager 会将 item 的宽度修改为最大宽度减去 start 和 end 的 divider，导致 item 最终的宽度不是我们希望的宽度
        // 所以以下的代码都是为了解决这个问题
        val column = params.spanIndex + params.spanSize - 1
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
                val left = if (params.isFirstSpan) {
                    sideDividerSize
                } else {
                    floor(multiplier * (params.spanCount - column)).toInt()
                }
                val right = if (params.isLastSpan) {
                    sideDividerSize
                } else {
                    val cellSideOffset = multiplier * (params.spanCount + 1)
                    ceil((cellSideOffset) - (multiplier * (params.spanCount - column))).toInt()
                }
                val top = topItemDivider?.heightSize ?: 0
                val bottom = bottomItemDivider?.heightSize ?: 0
                outRect.set(left, top, right, bottom)
            }
            else -> {
                val sideDivider =
                    sideDividerConfig.get(params.parent, params.position, params.spanIndex)!!
                val sideDividerSize = sideDivider.heightSize
                val multiplier = sideDividerSize / params.spanCount.toFloat()
                val left = startItemDivider?.widthSize ?: 0
                val right = endItemDivider?.widthSize ?: 0
                val top = if (params.isFirstSpan) {
                    sideDividerSize
                } else {
                    floor(multiplier * (params.spanCount - column)).toInt()
                }
                val bottom = if (params.isLastSpan) {
                    sideDividerSize
                } else {
                    val cellSideOffset = multiplier * (params.spanCount + 1)
                    ceil((cellSideOffset) - (multiplier * (params.spanCount - column))).toInt()
                }
                outRect.set(left, top, right, bottom)
            }
        }
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
            ItemDivider.Type.START -> if (params.isFirstSpan) sideHeaderDividerConfig else null
            ItemDivider.Type.END -> if (params.isLastSpan) sideFooterDividerConfig else sideDividerConfig
            ItemDivider.Type.TOP -> if (params.isColumnFirst) headerDividerConfig else null
            ItemDivider.Type.BOTTOM -> if (params.isColumnEnd) footerDividerConfig else dividerConfig
        }
        return dividerConfig?.get(params.parent, params.position, params.spanIndex)
    }
}