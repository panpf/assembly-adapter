package com.github.panpf.assemblyadapter.recycler.divider.internal

import android.view.View
import androidx.recyclerview.widget.RecyclerView

class ItemParams(
    var view: View,
    var parent: RecyclerView,
    var itemCount: Int,
    var position: Int,
    var spanCount: Int,
    var spanSize: Int,
    var spanIndex: Int,
    var isFullSpan: Boolean,
    var isFirstSpan: Boolean,
    var isLastSpan: Boolean,
    var isColumnFirst: Boolean,
    var isColumnEnd: Boolean,
    var isVerticalOrientation: Boolean,
    var isLTRDirection: Boolean
) {
    fun set(
        view: View,
        parent: RecyclerView,
        itemCount: Int,
        position: Int,
        spanCount: Int,
        spanSize: Int,
        spanIndex: Int,
        isFullSpan: Boolean,
        isFirstSpan: Boolean,
        isLastSpan: Boolean,
        isColumnFirst: Boolean,
        isColumnEnd: Boolean,
        isVerticalOrientation: Boolean,
        isLTRDirection: Boolean
    ) {
        this.view = view
        this.parent = parent
        this.itemCount = itemCount
        this.position = position
        this.spanCount = spanCount
        this.spanSize = spanSize
        this.spanIndex = spanIndex
        this.isFullSpan = isFullSpan
        this.isFirstSpan = isFirstSpan
        this.isLastSpan = isLastSpan
        this.isColumnFirst = isColumnFirst
        this.isColumnEnd = isColumnEnd
        this.isVerticalOrientation = isVerticalOrientation
        this.isLTRDirection = isLTRDirection
    }
}