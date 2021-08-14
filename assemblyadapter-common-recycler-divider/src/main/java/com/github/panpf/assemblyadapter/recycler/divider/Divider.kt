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
import com.github.panpf.assemblyadapter.recycler.divider.internal.ItemDivider

/**
 * Used to allow users to define the style of the divider of the item
 */
interface Divider {

    /**
     * Convert to [ItemDivider]
     */
    fun toItemDivider(context: Context): ItemDivider

    companion object {
        /**
         * Create a [Divider] with the specified Drawable
         *
         * @param size Define the size of the divider. The default is -1, which means that
         * the intrinsicWidth or intrinsicHeight of the drawable is used as the divider size
         * @param insets Define the spacing around Divider
         */
        fun drawable(
            drawable: Drawable,
            @Px size: Int = -1,
            insets: Insets? = null,
        ): Divider = DrawableDivider(drawable, size, insets)

        /**
         * Create a [Divider] with the specified Drawable resource ID
         *
         * @param size Define the size of the divider. The default is -1, which means that
         * the intrinsicWidth or intrinsicHeight of the drawable is used as the divider size
         * @param insets Define the spacing around Divider
         */
        fun drawableRes(
            @DrawableRes drawableResId: Int,
            @Px size: Int = -1,
            insets: Insets? = null,
        ): Divider = DrawableResDivider(drawableResId, size, insets)

        /**
         * Create a [Divider] with the specified color
         *
         * @param size Define divider size
         * @param insets Define the spacing around Divider
         */
        fun color(
            @ColorInt color: Int,
            @Px size: Int,
            insets: Insets? = null,
        ): Divider = ColorDivider(color, size, insets)

        /**
         * Create a [Divider] with the specified color resource ID
         *
         * @param size Define divider size
         * @param insets Define the spacing around Divider
         */
        fun colorRes(
            @ColorRes colorResId: Int,
            @Px size: Int,
            insets: Insets? = null,
        ): Divider = ColorResDivider(colorResId, size, insets)

        /**
         * Create a transparent blank [Divider]
         *
         * @param size Define divider size
         * @param insets Define the spacing around Divider
         */
        fun space(
            @Px size: Int,
            insets: Insets? = null,
        ): Divider = ColorDivider(Color.TRANSPARENT, size, insets)
    }

    private class DrawableDivider(
        private val drawable: Drawable,
        @Px private val size: Int,
        private val insets: Insets?,
    ) : Divider {

        override fun toItemDivider(context: Context): ItemDivider {
            return ItemDivider(
                drawable,
                size,
                insets?.start ?: 0,
                insets?.top ?: 0,
                insets?.end ?: 0,
                insets?.bottom ?: 0
            )
        }
    }

    private class DrawableResDivider(
        @DrawableRes private val drawableResId: Int,
        @Px private val size: Int,
        private val insets: Insets?,
    ) : Divider {

        override fun toItemDivider(context: Context): ItemDivider {
            val drawable = ResourcesCompat.getDrawable(context.resources, drawableResId, null)!!
            return ItemDivider(
                drawable,
                size,
                insets?.start ?: 0,
                insets?.top ?: 0,
                insets?.end ?: 0,
                insets?.bottom ?: 0
            )
        }
    }

    private class ColorDivider(
        @ColorInt private val color: Int,
        @Px private val size: Int,
        private val insets: Insets?,
    ) : Divider {

        override fun toItemDivider(context: Context): ItemDivider {
            return ItemDivider(
                ColorDrawable(color),
                size,
                insets?.start ?: 0,
                insets?.top ?: 0,
                insets?.end ?: 0,
                insets?.bottom ?: 0
            )
        }
    }

    private class ColorResDivider(
        @ColorRes private val colorResId: Int,
        @Px private val size: Int,
        private val insets: Insets?,
    ) : Divider {

        override fun toItemDivider(context: Context): ItemDivider {
            val color = ResourcesCompat.getColor(context.resources, colorResId, null)
            return ItemDivider(
                ColorDrawable(color),
                size,
                insets?.start ?: 0,
                insets?.top ?: 0,
                insets?.end ?: 0,
                insets?.bottom ?: 0
            )
        }
    }
}