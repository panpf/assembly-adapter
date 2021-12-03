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
import android.graphics.drawable.Drawable
import androidx.annotation.Px

data class ItemDivider constructor(
    val drawable: Drawable,
    private val size: DividerSize,
    @Px val insetStart: Int,
    @Px val insetTop: Int,
    @Px val insetEnd: Int,
    @Px val insetBottom: Int,
) {
    @Px
    fun getDrawableWidthSize(isStartOrEnd: Boolean): Int =
        size.getWidth(isStartOrEnd)

    @Px
    fun getDrawableHeightSize(isStartOrEnd: Boolean): Int =
        size.getHeight(isStartOrEnd)

    @Px
    val insetWidthSize: Int = insetStart + insetEnd

    @Px
    val insetHeightSize: Int = insetTop + insetBottom

    @Px
    fun getWidthSize(isStartOrEnd: Boolean): Int = getDrawableWidthSize(isStartOrEnd) + insetWidthSize

    @Px
    fun getHeightSize(isStartOrEnd: Boolean): Int = getDrawableHeightSize(isStartOrEnd) + insetHeightSize

    fun draw(canvas: Canvas, left: Int, top: Int, right: Int, bottom: Int) {
        drawable.setBounds(left, top, right, bottom)
        drawable.draw(canvas)
    }

    fun compareSizeAndInsets(other: ItemDivider): Boolean {
        return size == other.size
                && insetStart == other.insetStart
                && insetTop == other.insetTop
                && insetEnd == other.insetEnd
                && insetBottom == other.insetBottom
    }
}