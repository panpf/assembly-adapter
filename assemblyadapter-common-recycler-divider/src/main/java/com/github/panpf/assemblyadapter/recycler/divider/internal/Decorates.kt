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

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.Px
import androidx.core.content.res.ResourcesCompat
import com.github.panpf.assemblyadapter.recycler.divider.Decorate
import com.github.panpf.assemblyadapter.recycler.divider.Insets

internal class DrawableDecorate(
    private val drawable: Drawable,
    @Px private val size: Int,
    private val insets: Insets?,
) : Decorate {

    override fun createItemDecorate(context: Context): ItemDecorate {
        return ItemDecorate(
            drawable,
            size,
            insets?.start ?: 0,
            insets?.top ?: 0,
            insets?.end ?: 0,
            insets?.bottom ?: 0
        )
    }
}

internal class DrawableResDecorate(
    @DrawableRes private val drawableResId: Int,
    @Px private val size: Int,
    private val insets: Insets?,
) : Decorate {

    override fun createItemDecorate(context: Context): ItemDecorate {
        val drawable = ResourcesCompat.getDrawable(context.resources, drawableResId, null)!!
        return ItemDecorate(
            drawable,
            size,
            insets?.start ?: 0,
            insets?.top ?: 0,
            insets?.end ?: 0,
            insets?.bottom ?: 0
        )
    }
}

internal class ColorDecorate(
    @ColorInt private val color: Int,
    @Px private val size: Int,
    private val insets: Insets?,
) : Decorate {

    override fun createItemDecorate(context: Context): ItemDecorate {
        return ItemDecorate(
            ColorDrawable(color),
            size,
            insets?.start ?: 0,
            insets?.top ?: 0,
            insets?.end ?: 0,
            insets?.bottom ?: 0
        )
    }
}

internal class ColorResDecorate(
    @ColorRes private val colorResId: Int,
    @Px private val size: Int,
    private val insets: Insets?,
) : Decorate {

    override fun createItemDecorate(context: Context): ItemDecorate {
        val color = ResourcesCompat.getColor(context.resources, colorResId, null)
        return ItemDecorate(
            ColorDrawable(color),
            size,
            insets?.start ?: 0,
            insets?.top ?: 0,
            insets?.end ?: 0,
            insets?.bottom ?: 0
        )
    }
}

internal class SpaceDecorate(
    @Px private val size: Int,
    private val insets: Insets?,
) : Decorate {

    override fun createItemDecorate(context: Context): ItemDecorate {
        return ItemDecorate(
            ColorDrawable(Color.TRANSPARENT),
            size,
            insets?.start ?: 0,
            insets?.top ?: 0,
            insets?.end ?: 0,
            insets?.bottom ?: 0
        )
    }
}