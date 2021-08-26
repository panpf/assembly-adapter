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
package com.github.panpf.assemblyadapter.recycler.divider.test

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.core.content.res.ResourcesCompat
import androidx.test.platform.app.InstrumentationRegistry
import com.github.panpf.assemblyadapter.common.recycler.divider.test.R
import com.github.panpf.assemblyadapter.recycler.divider.Divider
import com.github.panpf.assemblyadapter.recycler.divider.Insets
import org.junit.Assert
import org.junit.Test

class DividerAndItemDividerTest {

    @Test
    fun testColor() {
        val context = InstrumentationRegistry.getInstrumentation().context

        /**
         * color
         */
        Divider.color(Color.RED, 5).toItemDivider(context).apply {
            val colorDrawable = this.drawable as ColorDrawable
            Assert.assertEquals(Color.RED, colorDrawable.color)
        }
        Divider.color(Color.BLUE, 5).toItemDivider(context).apply {
            val colorDrawable = this.drawable as ColorDrawable
            Assert.assertEquals(Color.BLUE, colorDrawable.color)
        }

        /**
         * size
         */
        Divider.color(Color.RED, 10).toItemDivider(context).apply {
            Assert.assertEquals(10, drawableWidthSize)
            Assert.assertEquals(10, drawableHeightSize)
            Assert.assertEquals(10, widthSize)
            Assert.assertEquals(10, heightSize)
        }
        Divider.color(Color.RED, 20).toItemDivider(context).apply {
            Assert.assertEquals(20, drawableWidthSize)
            Assert.assertEquals(20, drawableHeightSize)
            Assert.assertEquals(20, widthSize)
            Assert.assertEquals(20, heightSize)
        }

        /**
         * inset size
         */
        Divider.color(Color.RED, 20, Insets.of(1, 2, 3, 4)).toItemDivider(context).apply {
            Assert.assertEquals(1, insetStart)
            Assert.assertEquals(2, insetTop)
            Assert.assertEquals(3, insetEnd)
            Assert.assertEquals(4, insetBottom)
            Assert.assertEquals(4, insetWidthSize)
            Assert.assertEquals(6, insetHeightSize)
            Assert.assertEquals(20, drawableWidthSize)
            Assert.assertEquals(20, drawableHeightSize)
            Assert.assertEquals(24, widthSize)
            Assert.assertEquals(26, heightSize)
        }
        Divider.color(Color.RED, 20, Insets.of(4, 5, 6, 7)).toItemDivider(context).apply {
            Assert.assertEquals(4, insetStart)
            Assert.assertEquals(5, insetTop)
            Assert.assertEquals(6, insetEnd)
            Assert.assertEquals(7, insetBottom)
            Assert.assertEquals(10, insetWidthSize)
            Assert.assertEquals(12, insetHeightSize)
            Assert.assertEquals(20, drawableWidthSize)
            Assert.assertEquals(20, drawableHeightSize)
            Assert.assertEquals(30, widthSize)
            Assert.assertEquals(32, heightSize)
        }
    }

    @Test
    fun testColorRes() {
        val context = InstrumentationRegistry.getInstrumentation().context

        /**
         * color
         */
        Divider.colorRes(R.color.test1, 5).toItemDivider(context).apply {
            val colorDrawable = this.drawable as ColorDrawable
            Assert.assertEquals(
                ResourcesCompat.getColor(context.resources, R.color.test1, null),
                colorDrawable.color
            )
        }
        Divider.colorRes(R.color.test2, 5).toItemDivider(context).apply {
            val colorDrawable = this.drawable as ColorDrawable
            Assert.assertEquals(
                ResourcesCompat.getColor(context.resources, R.color.test2, null),
                colorDrawable.color
            )
        }

        /**
         * size
         */
        Divider.colorRes(R.color.test1, 10).toItemDivider(context).apply {
            Assert.assertEquals(10, drawableWidthSize)
            Assert.assertEquals(10, drawableHeightSize)
            Assert.assertEquals(10, widthSize)
            Assert.assertEquals(10, heightSize)
        }
        Divider.colorRes(R.color.test1, 20).toItemDivider(context).apply {
            Assert.assertEquals(20, drawableWidthSize)
            Assert.assertEquals(20, drawableHeightSize)
            Assert.assertEquals(20, widthSize)
            Assert.assertEquals(20, heightSize)
        }

        /**
         * inset size
         */
        Divider.colorRes(R.color.test1, 20, Insets.of(1, 2, 3, 4)).toItemDivider(context).apply {
            Assert.assertEquals(1, insetStart)
            Assert.assertEquals(2, insetTop)
            Assert.assertEquals(3, insetEnd)
            Assert.assertEquals(4, insetBottom)
            Assert.assertEquals(4, insetWidthSize)
            Assert.assertEquals(6, insetHeightSize)
            Assert.assertEquals(20, drawableWidthSize)
            Assert.assertEquals(20, drawableHeightSize)
            Assert.assertEquals(24, widthSize)
            Assert.assertEquals(26, heightSize)
        }
        Divider.colorRes(R.color.test1, 20, Insets.of(4, 5, 6, 7)).toItemDivider(context).apply {
            Assert.assertEquals(4, insetStart)
            Assert.assertEquals(5, insetTop)
            Assert.assertEquals(6, insetEnd)
            Assert.assertEquals(7, insetBottom)
            Assert.assertEquals(10, insetWidthSize)
            Assert.assertEquals(12, insetHeightSize)
            Assert.assertEquals(20, drawableWidthSize)
            Assert.assertEquals(20, drawableHeightSize)
            Assert.assertEquals(30, widthSize)
            Assert.assertEquals(32, heightSize)
        }
    }

    @Test
    fun testDrawable() {
        val context = InstrumentationRegistry.getInstrumentation().context
        val drawable = ResourcesCompat.getDrawable(context.resources, R.drawable.dvider, null)!!

        /**
         * drawable
         */
        Divider.drawable(drawable).toItemDivider(context).apply {
            Assert.assertEquals(100, this.drawable.intrinsicWidth)
            Assert.assertEquals(5, this.drawable.intrinsicHeight)
        }

        /**
         * size
         */
        Divider.drawable(drawable, 10).toItemDivider(context).apply {
            Assert.assertEquals(100, this.drawable.intrinsicWidth)
            Assert.assertEquals(5, this.drawable.intrinsicHeight)
            Assert.assertEquals(10, drawableWidthSize)
            Assert.assertEquals(10, drawableHeightSize)
            Assert.assertEquals(10, widthSize)
            Assert.assertEquals(10, heightSize)
        }
        Divider.drawable(drawable, 20).toItemDivider(context).apply {
            Assert.assertEquals(100, this.drawable.intrinsicWidth)
            Assert.assertEquals(5, this.drawable.intrinsicHeight)
            Assert.assertEquals(20, drawableWidthSize)
            Assert.assertEquals(20, drawableHeightSize)
            Assert.assertEquals(20, widthSize)
            Assert.assertEquals(20, heightSize)
        }

        /**
         * inset size
         */
        Divider.drawable(drawable, 20, Insets.of(1, 2, 3, 4)).toItemDivider(context).apply {
            Assert.assertEquals(100, this.drawable.intrinsicWidth)
            Assert.assertEquals(5, this.drawable.intrinsicHeight)
            Assert.assertEquals(1, insetStart)
            Assert.assertEquals(2, insetTop)
            Assert.assertEquals(3, insetEnd)
            Assert.assertEquals(4, insetBottom)
            Assert.assertEquals(4, insetWidthSize)
            Assert.assertEquals(6, insetHeightSize)
            Assert.assertEquals(20, drawableWidthSize)
            Assert.assertEquals(20, drawableHeightSize)
            Assert.assertEquals(24, widthSize)
            Assert.assertEquals(26, heightSize)
        }
        Divider.drawable(drawable, 20, Insets.of(4, 5, 6, 7)).toItemDivider(context).apply {
            Assert.assertEquals(100, this.drawable.intrinsicWidth)
            Assert.assertEquals(5, this.drawable.intrinsicHeight)
            Assert.assertEquals(4, insetStart)
            Assert.assertEquals(5, insetTop)
            Assert.assertEquals(6, insetEnd)
            Assert.assertEquals(7, insetBottom)
            Assert.assertEquals(10, insetWidthSize)
            Assert.assertEquals(12, insetHeightSize)
            Assert.assertEquals(20, drawableWidthSize)
            Assert.assertEquals(20, drawableHeightSize)
            Assert.assertEquals(30, widthSize)
            Assert.assertEquals(32, heightSize)
        }
    }

    @Test
    fun testDrawableRes() {
        val context = InstrumentationRegistry.getInstrumentation().context

        /**
         * drawable
         */
        Divider.drawableRes(R.drawable.dvider).toItemDivider(context).apply {
            Assert.assertEquals(100, this.drawable.intrinsicWidth)
            Assert.assertEquals(5, this.drawable.intrinsicHeight)
        }

        /**
         * size
         */
        Divider.drawableRes(R.drawable.dvider, 10).toItemDivider(context).apply {
            Assert.assertEquals(100, this.drawable.intrinsicWidth)
            Assert.assertEquals(5, this.drawable.intrinsicHeight)
            Assert.assertEquals(10, drawableWidthSize)
            Assert.assertEquals(10, drawableHeightSize)
            Assert.assertEquals(10, widthSize)
            Assert.assertEquals(10, heightSize)
        }
        Divider.drawableRes(R.drawable.dvider, 20).toItemDivider(context).apply {
            Assert.assertEquals(100, this.drawable.intrinsicWidth)
            Assert.assertEquals(5, this.drawable.intrinsicHeight)
            Assert.assertEquals(20, drawableWidthSize)
            Assert.assertEquals(20, drawableHeightSize)
            Assert.assertEquals(20, widthSize)
            Assert.assertEquals(20, heightSize)
        }

        /**
         * inset size
         */
        Divider.drawableRes(R.drawable.dvider, 20, Insets.of(1, 2, 3, 4))
            .toItemDivider(context)
            .apply {
                Assert.assertEquals(100, this.drawable.intrinsicWidth)
                Assert.assertEquals(5, this.drawable.intrinsicHeight)
                Assert.assertEquals(1, insetStart)
                Assert.assertEquals(2, insetTop)
                Assert.assertEquals(3, insetEnd)
                Assert.assertEquals(4, insetBottom)
                Assert.assertEquals(4, insetWidthSize)
                Assert.assertEquals(6, insetHeightSize)
                Assert.assertEquals(20, drawableWidthSize)
                Assert.assertEquals(20, drawableHeightSize)
                Assert.assertEquals(24, widthSize)
                Assert.assertEquals(26, heightSize)
            }
        Divider.drawableRes(R.drawable.dvider, 20, Insets.of(4, 5, 6, 7))
            .toItemDivider(context)
            .apply {
                Assert.assertEquals(100, this.drawable.intrinsicWidth)
                Assert.assertEquals(5, this.drawable.intrinsicHeight)
                Assert.assertEquals(4, insetStart)
                Assert.assertEquals(5, insetTop)
                Assert.assertEquals(6, insetEnd)
                Assert.assertEquals(7, insetBottom)
                Assert.assertEquals(10, insetWidthSize)
                Assert.assertEquals(12, insetHeightSize)
                Assert.assertEquals(20, drawableWidthSize)
                Assert.assertEquals(20, drawableHeightSize)
                Assert.assertEquals(30, widthSize)
                Assert.assertEquals(32, heightSize)
            }
    }

    @Test
    fun testSpace() {
        val context = InstrumentationRegistry.getInstrumentation().context

        /**
         * space
         */
        Divider.space(5).toItemDivider(context).apply {
            val colorDrawable = this.drawable as ColorDrawable
            Assert.assertEquals(Color.TRANSPARENT, colorDrawable.color)
        }

        /**
         * size
         */
        Divider.space(10).toItemDivider(context).apply {
            Assert.assertEquals(10, drawableWidthSize)
            Assert.assertEquals(10, drawableHeightSize)
            Assert.assertEquals(10, widthSize)
            Assert.assertEquals(10, heightSize)
        }
        Divider.space(20).toItemDivider(context).apply {
            Assert.assertEquals(20, drawableWidthSize)
            Assert.assertEquals(20, drawableHeightSize)
            Assert.assertEquals(20, widthSize)
            Assert.assertEquals(20, heightSize)
        }

        /**
         * inset size
         */
        Divider.space(20, Insets.of(1, 2, 3, 4)).toItemDivider(context).apply {
            Assert.assertEquals(1, insetStart)
            Assert.assertEquals(2, insetTop)
            Assert.assertEquals(3, insetEnd)
            Assert.assertEquals(4, insetBottom)
            Assert.assertEquals(4, insetWidthSize)
            Assert.assertEquals(6, insetHeightSize)
            Assert.assertEquals(20, drawableWidthSize)
            Assert.assertEquals(20, drawableHeightSize)
            Assert.assertEquals(24, widthSize)
            Assert.assertEquals(26, heightSize)
        }
        Divider.space(20, Insets.of(4, 5, 6, 7)).toItemDivider(context).apply {
            Assert.assertEquals(4, insetStart)
            Assert.assertEquals(5, insetTop)
            Assert.assertEquals(6, insetEnd)
            Assert.assertEquals(7, insetBottom)
            Assert.assertEquals(10, insetWidthSize)
            Assert.assertEquals(12, insetHeightSize)
            Assert.assertEquals(20, drawableWidthSize)
            Assert.assertEquals(20, drawableHeightSize)
            Assert.assertEquals(30, widthSize)
            Assert.assertEquals(32, heightSize)
        }
    }
}