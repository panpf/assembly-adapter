package com.github.panpf.assemblyadapter.recycler.divider.internal

import android.graphics.drawable.Drawable
import androidx.annotation.Px

data class ItemDecorate(
    val drawable: Drawable,
    @Px private val size: Int,
    @Px val insetStart: Int = 0,
    @Px val insetTop: Int = 0,
    @Px val insetEnd: Int = 0,
    @Px val insetBottom: Int = 0,
) {
    @Px
    val widthSize: Int = if (size != -1) size else drawable.intrinsicWidth

    @Px
    val heightSize: Int = if (size != -1) size else drawable.intrinsicHeight

    @Px
    val insetWidthSize: Int = insetStart + insetEnd

    @Px
    val insetHeightSize: Int = insetTop + insetBottom

    enum class Type {
        START, TOP, END, BOTTOM
    }
}