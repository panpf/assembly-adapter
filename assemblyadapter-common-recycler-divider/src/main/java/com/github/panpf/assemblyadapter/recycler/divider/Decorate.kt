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
            @Px insetEnd: Int = 0,
        ): Decorate = DrawableDecorate(drawable, size, insetStart, insetEnd)

        fun drawableRes(
            @DrawableRes drawableResId: Int,
            @Px size: Int = -1,
            @Px insetStart: Int = 0,
            @Px insetEnd: Int = 0,
        ): Decorate = DrawableResDecorate(drawableResId, size, insetStart, insetEnd)

        fun color(
            @ColorInt color: Int,
            @Px size: Int = -1,
            @Px insetStart: Int = 0,
            @Px insetEnd: Int = 0,
        ): Decorate = ColorDecorate(color, size, insetStart, insetEnd)

        fun colorRes(
            @ColorRes colorResId: Int,
            @Px size: Int = -1,
            @Px insetStart: Int = 0,
            @Px insetEnd: Int = 0,
        ): Decorate = ColorResDecorate(colorResId, size, insetStart, insetEnd)

        fun space(@Px size: Int = -1): Decorate = SpaceDecorate(size)
    }

    fun createItemDecorate(context: Context): ItemDecorate
}

internal class DrawableDecorate(
    private val drawable: Drawable,
    @Px private val size: Int = -1,
    @Px private val insetStart: Int = 0,
    @Px private val insetEnd: Int = 0,
) : Decorate {

    override fun createItemDecorate(context: Context): ItemDecorate {
        return ItemDecorate(drawable, size, insetStart, insetEnd)
    }
}

internal class DrawableResDecorate(
    @DrawableRes private val drawableResId: Int,
    @Px private val size: Int = -1,
    @Px private val insetStart: Int = 0,
    @Px private val insetEnd: Int = 0,
) : Decorate {

    override fun createItemDecorate(context: Context): ItemDecorate {
        val drawable = ResourcesCompat.getDrawable(context.resources, drawableResId, null)!!
        return ItemDecorate(drawable, size, insetStart, insetEnd)
    }
}

internal class ColorDecorate(
    @ColorInt private val color: Int,
    @Px private val size: Int,
    @Px private val insetStart: Int = 0,
    @Px private val insetEnd: Int = 0,
) : Decorate {

    override fun createItemDecorate(context: Context): ItemDecorate {
        return ItemDecorate(ColorDrawable(color), size, insetStart, insetEnd)
    }
}

internal class ColorResDecorate(
    @ColorRes private val colorResId: Int,
    @Px private val size: Int,
    @Px private val insetStart: Int = 0,
    @Px private val insetEnd: Int = 0,
) : Decorate {

    override fun createItemDecorate(context: Context): ItemDecorate {
        val color = ResourcesCompat.getColor(context.resources, colorResId, null)
        return ItemDecorate(ColorDrawable(color), size, insetStart, insetEnd)
    }
}

internal class SpaceDecorate(@Px private val size: Int) : Decorate {

    override fun createItemDecorate(context: Context): ItemDecorate {
        return ItemDecorate(ColorDrawable(Color.TRANSPARENT), size, 0, 0)
    }
}