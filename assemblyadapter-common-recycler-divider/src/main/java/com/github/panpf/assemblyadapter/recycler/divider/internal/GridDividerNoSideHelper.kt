package com.github.panpf.assemblyadapter.recycler.divider.internal

import android.graphics.Rect

class GridDividerNoSideHelper(
    val dividerConfig: ItemDividerConfig?,
    val headerDividerConfig: ItemDividerConfig?,
    val footerDividerConfig: ItemDividerConfig?,
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
            ItemDivider.Type.END -> null
            ItemDivider.Type.TOP -> if (params.isColumnFirst) headerDividerConfig else null
            ItemDivider.Type.BOTTOM -> if (params.isColumnEnd) footerDividerConfig else dividerConfig
        }
        return dividerConfig?.get(params.parent, params.position, params.spanIndex)
    }
}