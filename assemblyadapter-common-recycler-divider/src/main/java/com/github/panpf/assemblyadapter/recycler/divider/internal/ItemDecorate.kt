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

import android.graphics.drawable.Drawable
import androidx.annotation.Px

data class ItemDecorate(
    val drawable: Drawable,
    @Px private val size: Int,
    @Px val insetStart: Int,
    @Px val insetTop: Int,
    @Px val insetEnd: Int,
    @Px val insetBottom: Int,
) {
    @Px
    val drawableWidthSize: Int = if (size != -1) size else drawable.intrinsicWidth

    @Px
    val drawableHeightSize: Int = if (size != -1) size else drawable.intrinsicHeight

    @Px
    val insetWidthSize: Int = insetStart + insetEnd

    @Px
    val insetHeightSize: Int = insetTop + insetBottom

    @Px
    val widthSize: Int = drawableWidthSize + insetWidthSize

    @Px
    val heightSize: Int = drawableHeightSize + insetHeightSize

    enum class Type {
        START, TOP, END, BOTTOM
    }
}