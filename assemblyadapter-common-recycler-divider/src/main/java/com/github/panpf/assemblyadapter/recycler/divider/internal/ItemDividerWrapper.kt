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
import androidx.annotation.Px

data class ItemDividerWrapper constructor(
    private val itemDivider: ItemDivider,
    private val dividerSide: DividerSide,
) {
    @get:Px
    val drawableWidthSize: Int
        get() = itemDivider.getDrawableWidthSize(dividerSide == DividerSide.START || dividerSide == DividerSide.END)

    @get:Px
    val drawableHeightSize: Int
        get() = itemDivider.getDrawableHeightSize(dividerSide == DividerSide.START || dividerSide == DividerSide.END)


    @get:Px
    val insetStart: Int
        get() = itemDivider.insetStart

    @get:Px
    val insetTop: Int
        get() = itemDivider.insetTop

    @get:Px
    val insetEnd: Int
        get() = itemDivider.insetEnd

    @get:Px
    val insetBottom: Int
        get() = itemDivider.insetBottom

    @get:Px
    val insetWidthSize: Int
        get() = itemDivider.insetWidthSize

    @get:Px
    val insetHeightSize: Int
        get() = itemDivider.insetHeightSize

    @get:Px
    val widthSize: Int
        get() = itemDivider.getWidthSize(dividerSide == DividerSide.START || dividerSide == DividerSide.END)

    @get:Px
    val heightSize: Int
        get() = itemDivider.getHeightSize(dividerSide == DividerSide.START || dividerSide == DividerSide.END)

    fun draw(canvas: Canvas, left: Int, top: Int, right: Int, bottom: Int) {
        itemDivider.draw(canvas, left, top, right, bottom)
    }
}