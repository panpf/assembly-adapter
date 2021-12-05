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
import com.github.panpf.assemblyadapter.recycler.divider.internal.ClearlyDividerSize
import com.github.panpf.assemblyadapter.recycler.divider.internal.DividerSize
import com.github.panpf.assemblyadapter.recycler.divider.internal.ItemDivider
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
         * Create a transparent blank [Divider]. The size is specified by [vagueSize]
         *
         * @param vagueSize Define divider size. Please refer to [VagueDividerSize] for specific functions
         * @param insets Define the spacing around Divider
         */
        fun space(
            @Px vagueSize: Int,
            insets: Insets? = null,
        ): Divider = DividerImpl(
            { ColorDrawable(Color.TRANSPARENT) },
            { VagueDividerSize(vagueSize) },
            insets
        )

        /**
         * Create a [Divider] with the specified color. The size is specified by [vagueSize]
         *
         * @param vagueSize Define divider size. Please refer to [VagueDividerSize] for specific functions
         * @param insets Define the spacing around Divider
         */
        fun color(
            @ColorInt color: Int,
            @Px vagueSize: Int,
            insets: Insets? = null,
        ): Divider = DividerImpl(
            { ColorDrawable(color) },
            { VagueDividerSize(vagueSize) },
            insets
        )

        /**
         * Create a [Divider] with the specified color
         *
         * @param size Define divider size
         * @param insets Define the spacing around Divider
         */
        fun colorWithSize(
            @ColorInt color: Int,
            size: DividerSize,
            insets: Insets? = null,
        ): Divider = DividerImpl(
            { ColorDrawable(color) },
            { size },
            insets
        )

        /**
         * Create a [Divider] with the specified color
         *
         * @param width Define divider width
         * @param height Define divider height
         * @param insets Define the spacing around Divider
         */
        fun colorWithClearlySize(
            @ColorInt color: Int,
            @Px width: Int,
            @Px height: Int,
            insets: Insets? = null,
        ): Divider = DividerImpl(
            { ColorDrawable(color) },
            { ClearlyDividerSize(width, height) },
            insets
        )

        /**
         * Create a [Divider] with the specified color resource ID. The size is specified by [vagueSize]
         *
         * @param vagueSize Define divider size. Please refer to [VagueDividerSize] for specific functions
         * @param insets Define the spacing around Divider
         */
        fun colorRes(
            @ColorRes colorResId: Int,
            @Px vagueSize: Int,
            insets: Insets? = null,
        ): Divider = DividerImpl(
            { context ->
                ColorDrawable(ResourcesCompat.getColor(context.resources, colorResId, null))
            },
            { VagueDividerSize(vagueSize) },
            insets
        )

        /**
         * Create a [Divider] with the specified color resource ID
         *
         * @param size Define divider size
         * @param insets Define the spacing around Divider
         */
        fun colorResWithSize(
            @ColorRes colorResId: Int,
            size: DividerSize,
            insets: Insets? = null,
        ): Divider = DividerImpl(
            { context ->
                ColorDrawable(ResourcesCompat.getColor(context.resources, colorResId, null))
            },
            { size },
            insets
        )

        /**
         * Create a [Divider] with the specified color resource ID
         *
         * @param width Define divider width
         * @param height Define divider height
         * @param insets Define the spacing around Divider
         */
        fun colorResWithClearlySize(
            @ColorRes colorResId: Int,
            @Px width: Int,
            @Px height: Int,
            insets: Insets? = null,
        ): Divider = DividerImpl(
            { context ->
                ColorDrawable(ResourcesCompat.getColor(context.resources, colorResId, null))
            },
            { ClearlyDividerSize(width, height) },
            insets
        )

        /**
         * Create a [Divider] with the specified Drawable. The size is specified by [vagueSize]
         *
         * @param vagueSize Define divider size. Please refer to [VagueDividerSize] for specific functions
         * @param insets Define the spacing around Divider
         */
        fun drawable(
            dividerDrawable: Drawable,
            @Px vagueSize: Int = -1,
            insets: Insets? = null,
        ): Divider = DividerImpl(
            { dividerDrawable },
            { drawable ->
                if (vagueSize > 0) {
                    VagueDividerSize(vagueSize)
                } else {
                    ClearlyDividerSize(drawable.intrinsicWidth, drawable.intrinsicHeight)
                }
            },
            insets
        )

        /**
         * Create a [Divider] with the specified Drawable
         *
         * @param size Define the size of the divider
         * @param insets Define the spacing around Divider
         */
        fun drawableWithSize(
            dividerDrawable: Drawable,
            size: DividerSize,
            insets: Insets? = null,
        ): Divider = DividerImpl(
            { dividerDrawable },
            { size },
            insets
        )

        /**
         * Create a [Divider] with the specified Drawable
         *
         * @param width Define the width of the divider. The default is -1, which means that
         * the intrinsicWidth of the drawable is used as the divider width
         * @param height Define the height of the divider. The default is -1, which means that
         * the intrinsicHeight of the drawable is used as the divider height
         * @param insets Define the spacing around Divider
         */
        fun drawableWithClearlySize(
            dividerDrawable: Drawable,
            @Px width: Int = -1,
            @Px height: Int = -1,
            insets: Insets? = null,
        ): Divider = DividerImpl(
            { dividerDrawable },
            { drawable ->
                ClearlyDividerSize(
                    width.takeIf { it > 0 } ?: drawable.intrinsicWidth,
                    height.takeIf { it > 0 } ?: drawable.intrinsicHeight
                )
            },
            insets
        )

        /**
         * Create a [Divider] with the specified Drawable resource ID. The size is specified by [vagueSize]
         *
         * @param vagueSize Define divider size. Please refer to [VagueDividerSize] for specific functions
         * @param insets Define the spacing around Divider
         */
        fun drawableRes(
            @DrawableRes drawableResId: Int,
            @Px vagueSize: Int = -1,
            insets: Insets? = null,
        ): Divider = DividerImpl(
            { context ->
                ResourcesCompat.getDrawable(context.resources, drawableResId, null)!!
            },
            { drawable ->
                if (vagueSize > 0) {
                    VagueDividerSize(vagueSize)
                } else {
                    ClearlyDividerSize(drawable.intrinsicWidth, drawable.intrinsicHeight)
                }
            },
            insets
        )

        /**
         * Create a [Divider] with the specified Drawable resource ID
         *
         * @param size Define the size of the divider
         * @param insets Define the spacing around Divider
         */
        fun drawableResWithSize(
            @DrawableRes drawableResId: Int,
            size: DividerSize,
            insets: Insets? = null,
        ): Divider = DividerImpl(
            { context ->
                ResourcesCompat.getDrawable(context.resources, drawableResId, null)!!
            },
            { size },
            insets
        )

        /**
         * Create a [Divider] with the specified Drawable resource ID
         *
         * @param width Define the width of the divider. The default is -1, which means that
         * the intrinsicWidth of the drawable is used as the divider width
         * @param height Define the height of the divider. The default is -1, which means that
         * the intrinsicHeight of the drawable is used as the divider height
         * @param insets Define the spacing around Divider
         */
        fun drawableResWithClearlySize(
            @DrawableRes drawableResId: Int,
            @Px width: Int = -1,
            @Px height: Int = -1,
            insets: Insets? = null,
        ): Divider = DividerImpl(
            { context ->
                ResourcesCompat.getDrawable(context.resources, drawableResId, null)!!
            },
            { drawable ->
                ClearlyDividerSize(
                    width.takeIf { it > 0 } ?: drawable.intrinsicWidth,
                    height.takeIf { it > 0 } ?: drawable.intrinsicHeight
                )
            },
            insets
        )
    }

    private class DividerImpl(
        private val createDrawable: (context: Context) -> Drawable,
        private val createSize: (drawable: Drawable) -> DividerSize,
        private val insets: Insets?,
    ) : Divider {

        override fun toItemDivider(context: Context): ItemDivider {
            val drawable = createDrawable(context)
            val dividerSize = createSize(drawable)
            return ItemDivider(
                drawable,
                dividerSize,
                insets?.start ?: 0,
                insets?.top ?: 0,
                insets?.end ?: 0,
                insets?.bottom ?: 0
            )
        }
    }
}