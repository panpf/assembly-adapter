package com.github.panpf.assemblyadapter.recycler.divider.internal

import android.graphics.Rect
import kotlin.math.ceil
import kotlin.math.floor

class GridDividerOnlySideHelper(
    val dividerConfig: ItemDividerConfig?,
    val headerDividerConfig: ItemDividerConfig?,
    val footerDividerConfig: ItemDividerConfig?,
    val sideDividerConfig: ItemDividerConfig,
) : GridDividerHelper() {

    override fun getItemOffsets(outRect: Rect, params: ItemParams) {
        val isLTRDirection = params.isLTRDirection
        val startType = if (isLTRDirection) ItemDivider.Type.START else ItemDivider.Type.END
        val endType = if (isLTRDirection) ItemDivider.Type.END else ItemDivider.Type.START

        val startItemDivider = getItemDivider(params, startType, true)
        val endItemDivider = getItemDivider(params, endType, true)
        val topItemDivider = getItemDivider(params, ItemDivider.Type.TOP, true)
        val bottomItemDivider = getItemDivider(params, ItemDivider.Type.BOTTOM, true)

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
                val column = params.spanIndex + params.spanSize - 1
                val left = ceil(column * multiplier).toInt()
                val right = floor(sideDividerSize - ((column + 1) * multiplier)).toInt()
                val top = topItemDivider?.heightSize ?: 0
                val bottom = bottomItemDivider?.heightSize ?: 0
                outRect.set(left, top, right, bottom)
            }
            else -> {
                val sideDivider =
                    sideDividerConfig.get(params.parent, params.position, params.spanIndex)!!
                val sideDividerSize = sideDivider.heightSize
                val multiplier = sideDividerSize / params.spanCount.toFloat()
                val column = params.spanIndex + params.spanSize - 1
                val left = startItemDivider?.heightSize ?: 0
                val right = endItemDivider?.heightSize ?: 0
                val top = ceil(column * multiplier).toInt()
                val bottom = floor(sideDividerSize - ((column + 1) * multiplier)).toInt()
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
        val dividerConfig = if (fromOffset) {
            when (finalDividerType) {
                ItemDivider.Type.START -> if (params.isFirstSpan) null else sideDividerConfig
                ItemDivider.Type.END -> if (params.isLastSpan) null else sideDividerConfig
                ItemDivider.Type.TOP -> if (params.isColumnFirst) headerDividerConfig else null
                ItemDivider.Type.BOTTOM -> if (params.isColumnEnd) footerDividerConfig else dividerConfig
            }
        } else {
            when (finalDividerType) {
                ItemDivider.Type.START -> if (params.isFirstSpan) null else null
                ItemDivider.Type.END -> if (params.isLastSpan) null else sideDividerConfig
                ItemDivider.Type.TOP -> if (params.isColumnFirst) headerDividerConfig else null
                ItemDivider.Type.BOTTOM -> if (params.isColumnEnd) footerDividerConfig else dividerConfig
            }
        }
        return dividerConfig?.get(params.parent, params.position, params.spanIndex)
    }
}