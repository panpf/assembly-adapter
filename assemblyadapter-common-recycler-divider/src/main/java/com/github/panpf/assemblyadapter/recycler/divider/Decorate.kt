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
package com.github.panpf.assemblyadapter.recycler.divider

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.Px
import androidx.core.content.res.ResourcesCompat
import com.github.panpf.assemblyadapter.recycler.divider.internal.ItemDecorate

interface Decorate {

    companion object {
        fun drawable(
            drawable: Drawable,
            @Px size: Int = -1,
            @Px insetStart: Int = 0,
            @Px insetTop: Int = 0,
            @Px insetEnd: Int = 0,
            @Px insetBottom: Int = 0,
        ): Decorate = DrawableDecorate(drawable, size, insetStart, insetTop, insetEnd, insetBottom)

        fun drawableRes(
            @DrawableRes drawableResId: Int,
            @Px size: Int = -1,
            @Px insetStart: Int = 0,
            @Px insetTop: Int = 0,
            @Px insetEnd: Int = 0,
            @Px insetBottom: Int = 0,
        ): Decorate =
            DrawableResDecorate(drawableResId, size, insetStart, insetTop, insetEnd, insetBottom)

        fun color(
            @ColorInt color: Int,
            @Px size: Int = -1,
            @Px insetStart: Int = 0,
            @Px insetTop: Int = 0,
            @Px insetEnd: Int = 0,
            @Px insetBottom: Int = 0,
        ): Decorate = ColorDecorate(color, size, insetStart, insetTop, insetEnd, insetBottom)

        fun colorRes(
            @ColorRes colorResId: Int,
            @Px size: Int = -1,
            @Px insetStart: Int = 0,
            @Px insetTop: Int = 0,
            @Px insetEnd: Int = 0,
            @Px insetBottom: Int = 0,
        ): Decorate =
            ColorResDecorate(colorResId, size, insetStart, insetTop, insetEnd, insetBottom)

        fun space(
            @Px size: Int = -1,
            @Px insetStart: Int = 0,
            @Px insetTop: Int = 0,
            @Px insetEnd: Int = 0,
            @Px insetBottom: Int = 0,
        ): Decorate = SpaceDecorate(size, insetStart, insetTop, insetEnd, insetBottom)
    }

    fun createItemDecorate(context: Context): ItemDecorate
}

internal class DrawableDecorate(
    private val drawable: Drawable,
    @Px private val size: Int = -1,
    @Px private val insetStart: Int = 0,
    @Px private val insetTop: Int = 0,
    @Px private val insetEnd: Int = 0,
    @Px private val insetBottom: Int = 0,
) : Decorate {

    override fun createItemDecorate(context: Context): ItemDecorate {
        return ItemDecorate(drawable, size, insetStart, insetTop, insetEnd, insetBottom)
    }
}

internal class DrawableResDecorate(
    @DrawableRes private val drawableResId: Int,
    @Px private val size: Int = -1,
    @Px private val insetStart: Int = 0,
    @Px private val insetTop: Int = 0,
    @Px private val insetEnd: Int = 0,
    @Px private val insetBottom: Int = 0,
) : Decorate {

    override fun createItemDecorate(context: Context): ItemDecorate {
        val drawable = ResourcesCompat.getDrawable(context.resources, drawableResId, null)!!
        return ItemDecorate(drawable, size, insetStart, insetTop, insetEnd, insetBottom)
    }
}

internal class ColorDecorate(
    @ColorInt private val color: Int,
    @Px private val size: Int,
    @Px private val insetStart: Int = 0,
    @Px private val insetTop: Int = 0,
    @Px private val insetEnd: Int = 0,
    @Px private val insetBottom: Int = 0,
) : Decorate {

    override fun createItemDecorate(context: Context): ItemDecorate {
        return ItemDecorate(ColorDrawable(color), size, insetStart, insetTop, insetEnd, insetBottom)
    }
}

internal class ColorResDecorate(
    @ColorRes private val colorResId: Int,
    @Px private val size: Int,
    @Px private val insetStart: Int = 0,
    @Px private val insetTop: Int = 0,
    @Px private val insetEnd: Int = 0,
    @Px private val insetBottom: Int = 0,
) : Decorate {

    override fun createItemDecorate(context: Context): ItemDecorate {
        val color = ResourcesCompat.getColor(context.resources, colorResId, null)
        return ItemDecorate(ColorDrawable(color), size, insetStart, insetTop, insetEnd, insetBottom)
    }
}

internal class SpaceDecorate(
    @Px private val size: Int,
    @Px private val insetStart: Int = 0,
    @Px private val insetTop: Int = 0,
    @Px private val insetEnd: Int = 0,
    @Px private val insetBottom: Int = 0,
) : Decorate {

    override fun createItemDecorate(context: Context): ItemDecorate {
        return ItemDecorate(
            ColorDrawable(Color.TRANSPARENT),
            size,
            insetStart,
            insetTop,
            insetEnd,
            insetBottom
        )
    }
}