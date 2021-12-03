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
import com.github.panpf.assemblyadapter.recycler.divider.internal.DividerSize
import com.github.panpf.assemblyadapter.recycler.divider.internal.ItemDivider
import com.github.panpf.assemblyadapter.recycler.divider.internal.ClearlyDividerSize
import com.github.panpf.assemblyadapter.recycler.divider.internal.VagueDividerSize

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
         * the intrinsicWidth and intrinsicHeight of the drawable is used as the divider width and height
         * @param insets Define the spacing around Divider
         */
        fun drawable(
            drawable: Drawable,
            @Px size: Int = -1,
            insets: Insets? = null,
        ): Divider = DrawableDivider(drawable, size, insets)

        /**
         * Create a [Divider] with the specified Drawable
         *
         * @param width Define the width of the divider. The default is -1, which means that
         * the intrinsicWidth of the drawable is used as the divider width
         * @param height Define the height of the divider. The default is -1, which means that
         * the intrinsicHeight of the drawable is used as the divider height
         * @param insets Define the spacing around Divider
         */
        fun drawableClearlySize(
            drawable: Drawable,
            @Px width: Int = -1,
            @Px height: Int = -1,
            insets: Insets? = null,
        ): Divider = DrawableDivider2(drawable, width, height, insets)

        /**
         * Create a [Divider] with the specified Drawable resource ID
         *
         * @param size Define the size of the divider. The default is -1, which means that
         * the intrinsicWidth and intrinsicHeight of the drawable is used as the divider width and height
         * @param insets Define the spacing around Divider
         */
        fun drawableRes(
            @DrawableRes drawableResId: Int,
            @Px size: Int = -1,
            insets: Insets? = null,
        ): Divider = DrawableResDivider(drawableResId, size, insets)

        /**
         * Create a [Divider] with the specified Drawable resource ID
         *
         * @param width Define the width of the divider. The default is -1, which means that
         * the intrinsicWidth of the drawable is used as the divider width
         * @param height Define the height of the divider. The default is -1, which means that
         * the intrinsicHeight of the drawable is used as the divider height
         * @param insets Define the spacing around Divider
         */
        fun drawableResClearlySize(
            @DrawableRes drawableResId: Int,
            @Px width: Int = -1,
            @Px height: Int = -1,
            insets: Insets? = null,
        ): Divider = DrawableResDivider2(drawableResId, width, height, insets)

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
        ): Divider = ColorDivider(color, VagueDividerSize(size), insets)

        /**
         * Create a [Divider] with the specified color
         *
         * @param width Define divider width
         * @param height Define divider height
         * @param insets Define the spacing around Divider
         */
        fun colorClearlySize(
            @ColorInt color: Int,
            @Px width: Int,
            @Px height: Int,
            insets: Insets? = null,
        ): Divider = ColorDivider(color, ClearlyDividerSize(width, height), insets)

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
        ): Divider = ColorResDivider(colorResId, VagueDividerSize(size), insets)

        /**
         * Create a [Divider] with the specified color resource ID
         *
         * @param width Define divider width
         * @param height Define divider height
         * @param insets Define the spacing around Divider
         */
        fun colorResClearlySize(
            @ColorRes colorResId: Int,
            @Px width: Int,
            @Px height: Int,
            insets: Insets? = null,
        ): Divider = ColorResDivider(colorResId, ClearlyDividerSize(width, height), insets)

        /**
         * Create a transparent blank [Divider]
         *
         * @param size Define divider size
         * @param insets Define the spacing around Divider
         */
        fun space(
            @Px size: Int,
            insets: Insets? = null,
        ): Divider = ColorDivider(Color.TRANSPARENT, VagueDividerSize(size), insets)
    }

    private class DrawableDivider(
        private val drawable: Drawable,
        @Px private val size: Int,
        private val insets: Insets?,
    ) : Divider {

        override fun toItemDivider(context: Context): ItemDivider = ItemDivider(
            drawable,
            if (size > 0) {
                VagueDividerSize(size)
            } else {
                ClearlyDividerSize(drawable.intrinsicWidth, drawable.intrinsicHeight)
            },
            insets?.start ?: 0,
            insets?.top ?: 0,
            insets?.end ?: 0,
            insets?.bottom ?: 0
        )
    }

    private class DrawableDivider2(
        private val drawable: Drawable,
        @Px private val width: Int,
        @Px private val height: Int,
        private val insets: Insets?,
    ) : Divider {

        override fun toItemDivider(context: Context): ItemDivider = ItemDivider(
            drawable,
            ClearlyDividerSize(
                width.takeIf { it > 0 } ?: drawable.intrinsicWidth,
                height.takeIf { it > 0 } ?: drawable.intrinsicHeight
            ),
            insets?.start ?: 0,
            insets?.top ?: 0,
            insets?.end ?: 0,
            insets?.bottom ?: 0
        )
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
                if (size > 0) {
                    VagueDividerSize(size)
                } else {
                    ClearlyDividerSize(drawable.intrinsicWidth, drawable.intrinsicHeight)
                },
                insets?.start ?: 0,
                insets?.top ?: 0,
                insets?.end ?: 0,
                insets?.bottom ?: 0
            )
        }
    }

    private class DrawableResDivider2(
        @DrawableRes private val drawableResId: Int,
        @Px private val width: Int,
        @Px private val height: Int,
        private val insets: Insets?,
    ) : Divider {

        override fun toItemDivider(context: Context): ItemDivider {
            val drawable = ResourcesCompat.getDrawable(context.resources, drawableResId, null)!!
            return ItemDivider(
                drawable,
                ClearlyDividerSize(
                    width.takeIf { it > 0 } ?: drawable.intrinsicWidth,
                    height.takeIf { it > 0 } ?: drawable.intrinsicHeight
                ),
                insets?.start ?: 0,
                insets?.top ?: 0,
                insets?.end ?: 0,
                insets?.bottom ?: 0
            )
        }
    }

    private class ColorDivider(
        @ColorInt private val color: Int,
        private val dividerSize: DividerSize,
        private val insets: Insets?,
    ) : Divider {

        override fun toItemDivider(context: Context): ItemDivider {
            return ItemDivider(
                ColorDrawable(color),
                dividerSize,
                insets?.start ?: 0,
                insets?.top ?: 0,
                insets?.end ?: 0,
                insets?.bottom ?: 0
            )
        }
    }

    private class ColorResDivider(
        @ColorRes private val colorResId: Int,
        val dividerSize: DividerSize,
        private val insets: Insets?,
    ) : Divider {

        override fun toItemDivider(context: Context): ItemDivider {
            val color = ResourcesCompat.getColor(context.resources, colorResId, null)
            return ItemDivider(
                ColorDrawable(color),
                dividerSize,
                insets?.start ?: 0,
                insets?.top ?: 0,
                insets?.end ?: 0,
                insets?.bottom ?: 0
            )
        }
    }
}