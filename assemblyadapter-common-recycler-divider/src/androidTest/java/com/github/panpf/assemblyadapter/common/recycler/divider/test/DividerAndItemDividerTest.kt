package com.github.panpf.assemblyadapter.common.recycler.divider.test

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.core.content.res.ResourcesCompat
import androidx.test.platform.app.InstrumentationRegistry
import com.github.panpf.assemblyadapter.recycler.divider.Divider
import com.github.panpf.assemblyadapter.recycler.divider.Insets
import com.github.panpf.assemblyadapter.recycler.divider.internal.DividerSide
import com.github.panpf.assemblyadapter.recycler.divider.internal.ItemDividerWrapper
import com.github.panpf.assemblyadapter.recycler.divider.internal.VagueDividerSize
import org.junit.Assert
import org.junit.Test

class DividerAndItemDividerTest {

    private fun Divider.toItemDividerWrapper(
        context: Context,
        side: DividerSide
    ): ItemDividerWrapper =
        ItemDividerWrapper(toItemDivider(context), side)

    @Test
    fun testSpace() {
        val context = InstrumentationRegistry.getInstrumentation().context

        /**
         * space
         */
        Divider.space(5).toItemDividerWrapper(context, DividerSide.START).apply {
            val colorDrawable = this.drawable as ColorDrawable
            Assert.assertEquals(Color.TRANSPARENT, colorDrawable.color)
        }

        /**
         * size
         */
        Divider.space(10).toItemDividerWrapper(context, DividerSide.START).apply {
            Assert.assertEquals(10, drawableWidth)
            Assert.assertEquals(-1, drawableHeight)
            Assert.assertEquals(10, width)
            Assert.assertEquals(-1, height)
        }
        Divider.space(20).toItemDividerWrapper(context, DividerSide.START).apply {
            Assert.assertEquals(20, drawableWidth)
            Assert.assertEquals(-1, drawableHeight)
            Assert.assertEquals(20, width)
            Assert.assertEquals(-1, height)
        }
        Divider.space(10).toItemDividerWrapper(context, DividerSide.TOP).apply {
            Assert.assertEquals(-1, drawableWidth)
            Assert.assertEquals(10, drawableHeight)
            Assert.assertEquals(-1, width)
            Assert.assertEquals(10, height)
        }
        Divider.space(20).toItemDividerWrapper(context, DividerSide.TOP).apply {
            Assert.assertEquals(-1, drawableWidth)
            Assert.assertEquals(20, drawableHeight)
            Assert.assertEquals(-1, width)
            Assert.assertEquals(20, height)
        }

        /**
         * inset size
         */
        Divider.space(20, Insets.of(1, 2, 3, 4)).toItemDividerWrapper(context, DividerSide.START)
            .apply {
                Assert.assertEquals(1, insetStart)
                Assert.assertEquals(2, insetTop)
                Assert.assertEquals(3, insetEnd)
                Assert.assertEquals(4, insetBottom)
                Assert.assertEquals(4, insetWidth)
                Assert.assertEquals(6, insetHeight)
                Assert.assertEquals(20, drawableWidth)
                Assert.assertEquals(-1, drawableHeight)
                Assert.assertEquals(24, width)
                Assert.assertEquals(5, height)
            }
        Divider.space(20, Insets.of(4, 5, 6, 7)).toItemDividerWrapper(context, DividerSide.START)
            .apply {
                Assert.assertEquals(4, insetStart)
                Assert.assertEquals(5, insetTop)
                Assert.assertEquals(6, insetEnd)
                Assert.assertEquals(7, insetBottom)
                Assert.assertEquals(10, insetWidth)
                Assert.assertEquals(12, insetHeight)
                Assert.assertEquals(20, drawableWidth)
                Assert.assertEquals(-1, drawableHeight)
                Assert.assertEquals(30, width)
                Assert.assertEquals(11, height)
            }
        Divider.space(20, Insets.of(1, 2, 3, 4)).toItemDividerWrapper(context, DividerSide.TOP)
            .apply {
                Assert.assertEquals(1, insetStart)
                Assert.assertEquals(2, insetTop)
                Assert.assertEquals(3, insetEnd)
                Assert.assertEquals(4, insetBottom)
                Assert.assertEquals(4, insetWidth)
                Assert.assertEquals(6, insetHeight)
                Assert.assertEquals(-1, drawableWidth)
                Assert.assertEquals(20, drawableHeight)
                Assert.assertEquals(3, width)
                Assert.assertEquals(26, height)
            }
        Divider.space(20, Insets.of(4, 5, 6, 7)).toItemDividerWrapper(context, DividerSide.TOP)
            .apply {
                Assert.assertEquals(4, insetStart)
                Assert.assertEquals(5, insetTop)
                Assert.assertEquals(6, insetEnd)
                Assert.assertEquals(7, insetBottom)
                Assert.assertEquals(10, insetWidth)
                Assert.assertEquals(12, insetHeight)
                Assert.assertEquals(-1, drawableWidth)
                Assert.assertEquals(20, drawableHeight)
                Assert.assertEquals(9, width)
                Assert.assertEquals(32, height)
            }
    }

    @Test
    fun testColor() {
        val context = InstrumentationRegistry.getInstrumentation().context

        /**
         * color
         */
        Divider.color(Color.RED, 5).toItemDividerWrapper(context, DividerSide.START).apply {
            val colorDrawable = this.drawable as ColorDrawable
            Assert.assertEquals(Color.RED, colorDrawable.color)
        }
        Divider.color(Color.BLUE, 5).toItemDividerWrapper(context, DividerSide.START).apply {
            val colorDrawable = this.drawable as ColorDrawable
            Assert.assertEquals(Color.BLUE, colorDrawable.color)
        }

        /**
         * size
         */
        Divider.color(Color.RED, 10).toItemDividerWrapper(context, DividerSide.START).apply {
            Assert.assertEquals(10, drawableWidth)
            Assert.assertEquals(-1, drawableHeight)
            Assert.assertEquals(10, width)
            Assert.assertEquals(-1, height)
        }
        Divider.color(Color.RED, 20).toItemDividerWrapper(context, DividerSide.START).apply {
            Assert.assertEquals(20, drawableWidth)
            Assert.assertEquals(-1, drawableHeight)
            Assert.assertEquals(20, width)
            Assert.assertEquals(-1, height)
        }
        Divider.color(Color.RED, 10).toItemDividerWrapper(context, DividerSide.TOP).apply {
            Assert.assertEquals(-1, drawableWidth)
            Assert.assertEquals(10, drawableHeight)
            Assert.assertEquals(-1, width)
            Assert.assertEquals(10, height)
        }
        Divider.color(Color.RED, 20).toItemDividerWrapper(context, DividerSide.TOP).apply {
            Assert.assertEquals(-1, drawableWidth)
            Assert.assertEquals(20, drawableHeight)
            Assert.assertEquals(-1, width)
            Assert.assertEquals(20, height)
        }

        /**
         * inset size
         */
        Divider.color(Color.RED, 20, Insets.of(1, 2, 3, 4))
            .toItemDividerWrapper(context, DividerSide.START).apply {
                Assert.assertEquals(1, insetStart)
                Assert.assertEquals(2, insetTop)
                Assert.assertEquals(3, insetEnd)
                Assert.assertEquals(4, insetBottom)
                Assert.assertEquals(4, insetWidth)
                Assert.assertEquals(6, insetHeight)
                Assert.assertEquals(20, drawableWidth)
                Assert.assertEquals(-1, drawableHeight)
                Assert.assertEquals(24, width)
                Assert.assertEquals(5, height)
            }
        Divider.color(Color.RED, 20, Insets.of(4, 5, 6, 7))
            .toItemDividerWrapper(context, DividerSide.START).apply {
                Assert.assertEquals(4, insetStart)
                Assert.assertEquals(5, insetTop)
                Assert.assertEquals(6, insetEnd)
                Assert.assertEquals(7, insetBottom)
                Assert.assertEquals(10, insetWidth)
                Assert.assertEquals(12, insetHeight)
                Assert.assertEquals(20, drawableWidth)
                Assert.assertEquals(-1, drawableHeight)
                Assert.assertEquals(30, width)
                Assert.assertEquals(11, height)
            }
        Divider.color(Color.RED, 20, Insets.of(1, 2, 3, 4))
            .toItemDividerWrapper(context, DividerSide.TOP).apply {
                Assert.assertEquals(1, insetStart)
                Assert.assertEquals(2, insetTop)
                Assert.assertEquals(3, insetEnd)
                Assert.assertEquals(4, insetBottom)
                Assert.assertEquals(4, insetWidth)
                Assert.assertEquals(6, insetHeight)
                Assert.assertEquals(-1, drawableWidth)
                Assert.assertEquals(20, drawableHeight)
                Assert.assertEquals(3, width)
                Assert.assertEquals(26, height)
            }
        Divider.color(Color.RED, 20, Insets.of(4, 5, 6, 7))
            .toItemDividerWrapper(context, DividerSide.TOP).apply {
                Assert.assertEquals(4, insetStart)
                Assert.assertEquals(5, insetTop)
                Assert.assertEquals(6, insetEnd)
                Assert.assertEquals(7, insetBottom)
                Assert.assertEquals(10, insetWidth)
                Assert.assertEquals(12, insetHeight)
                Assert.assertEquals(-1, drawableWidth)
                Assert.assertEquals(20, drawableHeight)
                Assert.assertEquals(9, width)
                Assert.assertEquals(32, height)
            }
    }

    @Test
    fun testColorWithSize() {
        val context = InstrumentationRegistry.getInstrumentation().context

        /**
         * color
         */
        Divider.colorWithSize(Color.RED, VagueDividerSize(5))
            .toItemDividerWrapper(context, DividerSide.START).apply {
                val colorDrawable = this.drawable as ColorDrawable
                Assert.assertEquals(Color.RED, colorDrawable.color)
            }
        Divider.colorWithSize(Color.BLUE, VagueDividerSize(5))
            .toItemDividerWrapper(context, DividerSide.START).apply {
                val colorDrawable = this.drawable as ColorDrawable
                Assert.assertEquals(Color.BLUE, colorDrawable.color)
            }

        /**
         * size
         */
        Divider.colorWithSize(Color.RED, VagueDividerSize(10))
            .toItemDividerWrapper(context, DividerSide.START).apply {
                Assert.assertEquals(10, drawableWidth)
                Assert.assertEquals(-1, drawableHeight)
                Assert.assertEquals(10, width)
                Assert.assertEquals(-1, height)
            }
        Divider.colorWithSize(Color.RED, VagueDividerSize(20))
            .toItemDividerWrapper(context, DividerSide.START).apply {
                Assert.assertEquals(20, drawableWidth)
                Assert.assertEquals(-1, drawableHeight)
                Assert.assertEquals(20, width)
                Assert.assertEquals(-1, height)
            }
        Divider.colorWithSize(Color.RED, VagueDividerSize(10))
            .toItemDividerWrapper(context, DividerSide.TOP).apply {
                Assert.assertEquals(-1, drawableWidth)
                Assert.assertEquals(10, drawableHeight)
                Assert.assertEquals(-1, width)
                Assert.assertEquals(10, height)
            }
        Divider.colorWithSize(Color.RED, VagueDividerSize(20))
            .toItemDividerWrapper(context, DividerSide.TOP).apply {
                Assert.assertEquals(-1, drawableWidth)
                Assert.assertEquals(20, drawableHeight)
                Assert.assertEquals(-1, width)
                Assert.assertEquals(20, height)
            }

        /**
         * inset size
         */
        Divider.colorWithSize(Color.RED, VagueDividerSize(20), Insets.of(1, 2, 3, 4))
            .toItemDividerWrapper(context, DividerSide.START).apply {
                Assert.assertEquals(1, insetStart)
                Assert.assertEquals(2, insetTop)
                Assert.assertEquals(3, insetEnd)
                Assert.assertEquals(4, insetBottom)
                Assert.assertEquals(4, insetWidth)
                Assert.assertEquals(6, insetHeight)
                Assert.assertEquals(20, drawableWidth)
                Assert.assertEquals(-1, drawableHeight)
                Assert.assertEquals(24, width)
                Assert.assertEquals(5, height)
            }
        Divider.colorWithSize(Color.RED, VagueDividerSize(20), Insets.of(4, 5, 6, 7))
            .toItemDividerWrapper(context, DividerSide.START).apply {
                Assert.assertEquals(4, insetStart)
                Assert.assertEquals(5, insetTop)
                Assert.assertEquals(6, insetEnd)
                Assert.assertEquals(7, insetBottom)
                Assert.assertEquals(10, insetWidth)
                Assert.assertEquals(12, insetHeight)
                Assert.assertEquals(20, drawableWidth)
                Assert.assertEquals(-1, drawableHeight)
                Assert.assertEquals(30, width)
                Assert.assertEquals(11, height)
            }
        Divider.colorWithSize(Color.RED, VagueDividerSize(20), Insets.of(1, 2, 3, 4))
            .toItemDividerWrapper(context, DividerSide.TOP).apply {
                Assert.assertEquals(1, insetStart)
                Assert.assertEquals(2, insetTop)
                Assert.assertEquals(3, insetEnd)
                Assert.assertEquals(4, insetBottom)
                Assert.assertEquals(4, insetWidth)
                Assert.assertEquals(6, insetHeight)
                Assert.assertEquals(-1, drawableWidth)
                Assert.assertEquals(20, drawableHeight)
                Assert.assertEquals(3, width)
                Assert.assertEquals(26, height)
            }
        Divider.colorWithSize(Color.RED, VagueDividerSize(20), Insets.of(4, 5, 6, 7))
            .toItemDividerWrapper(context, DividerSide.TOP).apply {
                Assert.assertEquals(4, insetStart)
                Assert.assertEquals(5, insetTop)
                Assert.assertEquals(6, insetEnd)
                Assert.assertEquals(7, insetBottom)
                Assert.assertEquals(10, insetWidth)
                Assert.assertEquals(12, insetHeight)
                Assert.assertEquals(-1, drawableWidth)
                Assert.assertEquals(20, drawableHeight)
                Assert.assertEquals(9, width)
                Assert.assertEquals(32, height)
            }
    }

    @Test
    fun testColorWithClearlySize() {
        val context = InstrumentationRegistry.getInstrumentation().context

        /**
         * color
         */
        Divider.colorWithClearlySize(Color.RED, 5, 5)
            .toItemDividerWrapper(context, DividerSide.START).apply {
                val colorDrawable = this.drawable as ColorDrawable
                Assert.assertEquals(Color.RED, colorDrawable.color)
            }
        Divider.colorWithClearlySize(Color.BLUE, 5, 5)
            .toItemDividerWrapper(context, DividerSide.START).apply {
                val colorDrawable = this.drawable as ColorDrawable
                Assert.assertEquals(Color.BLUE, colorDrawable.color)
            }

        /**
         * size
         */
        Divider.colorWithClearlySize(Color.RED, 10, 20)
            .toItemDividerWrapper(context, DividerSide.START).apply {
                Assert.assertEquals(10, drawableWidth)
                Assert.assertEquals(20, drawableHeight)
                Assert.assertEquals(10, width)
                Assert.assertEquals(20, height)
            }
        Divider.colorWithClearlySize(Color.RED, 20, 40)
            .toItemDividerWrapper(context, DividerSide.START).apply {
                Assert.assertEquals(20, drawableWidth)
                Assert.assertEquals(40, drawableHeight)
                Assert.assertEquals(20, width)
                Assert.assertEquals(40, height)
            }
        Divider.colorWithClearlySize(Color.RED, 20, 10)
            .toItemDividerWrapper(context, DividerSide.TOP).apply {
                Assert.assertEquals(20, drawableWidth)
                Assert.assertEquals(10, drawableHeight)
                Assert.assertEquals(20, width)
                Assert.assertEquals(10, height)
            }
        Divider.colorWithClearlySize(Color.RED, 40, 20)
            .toItemDividerWrapper(context, DividerSide.TOP).apply {
                Assert.assertEquals(40, drawableWidth)
                Assert.assertEquals(20, drawableHeight)
                Assert.assertEquals(40, width)
                Assert.assertEquals(20, height)
            }

        /**
         * inset size
         */
        Divider.colorWithClearlySize(Color.RED, 20, 40, Insets.of(1, 2, 3, 4))
            .toItemDividerWrapper(context, DividerSide.START).apply {
                Assert.assertEquals(1, insetStart)
                Assert.assertEquals(2, insetTop)
                Assert.assertEquals(3, insetEnd)
                Assert.assertEquals(4, insetBottom)
                Assert.assertEquals(4, insetWidth)
                Assert.assertEquals(6, insetHeight)
                Assert.assertEquals(20, drawableWidth)
                Assert.assertEquals(40, drawableHeight)
                Assert.assertEquals(24, width)
                Assert.assertEquals(46, height)
            }
        Divider.colorWithClearlySize(Color.RED, 20, 40, Insets.of(4, 5, 6, 7))
            .toItemDividerWrapper(context, DividerSide.START).apply {
                Assert.assertEquals(4, insetStart)
                Assert.assertEquals(5, insetTop)
                Assert.assertEquals(6, insetEnd)
                Assert.assertEquals(7, insetBottom)
                Assert.assertEquals(10, insetWidth)
                Assert.assertEquals(12, insetHeight)
                Assert.assertEquals(20, drawableWidth)
                Assert.assertEquals(40, drawableHeight)
                Assert.assertEquals(30, width)
                Assert.assertEquals(52, height)
            }
        Divider.colorWithClearlySize(Color.RED, 40, 20, Insets.of(1, 2, 3, 4))
            .toItemDividerWrapper(context, DividerSide.TOP).apply {
                Assert.assertEquals(1, insetStart)
                Assert.assertEquals(2, insetTop)
                Assert.assertEquals(3, insetEnd)
                Assert.assertEquals(4, insetBottom)
                Assert.assertEquals(4, insetWidth)
                Assert.assertEquals(6, insetHeight)
                Assert.assertEquals(40, drawableWidth)
                Assert.assertEquals(20, drawableHeight)
                Assert.assertEquals(44, width)
                Assert.assertEquals(26, height)
            }
        Divider.colorWithClearlySize(Color.RED, 40, 20, Insets.of(4, 5, 6, 7))
            .toItemDividerWrapper(context, DividerSide.TOP).apply {
                Assert.assertEquals(4, insetStart)
                Assert.assertEquals(5, insetTop)
                Assert.assertEquals(6, insetEnd)
                Assert.assertEquals(7, insetBottom)
                Assert.assertEquals(10, insetWidth)
                Assert.assertEquals(12, insetHeight)
                Assert.assertEquals(40, drawableWidth)
                Assert.assertEquals(20, drawableHeight)
                Assert.assertEquals(50, width)
                Assert.assertEquals(32, height)
            }
    }

    @Test
    fun testColorRes() {
        val context = InstrumentationRegistry.getInstrumentation().context

        /**
         * color
         */
        Divider.colorRes(R.color.test1, 5).toItemDividerWrapper(context, DividerSide.START).apply {
            val colorDrawable = this.drawable as ColorDrawable
            Assert.assertEquals(
                ResourcesCompat.getColor(context.resources, R.color.test1, null),
                colorDrawable.color
            )
        }
        Divider.colorRes(R.color.test2, 5).toItemDividerWrapper(context, DividerSide.START).apply {
            val colorDrawable = this.drawable as ColorDrawable
            Assert.assertEquals(
                ResourcesCompat.getColor(context.resources, R.color.test2, null),
                colorDrawable.color
            )
        }

        /**
         * size
         */
        Divider.colorRes(R.color.test1, 10).toItemDividerWrapper(context, DividerSide.START).apply {
            Assert.assertEquals(10, drawableWidth)
            Assert.assertEquals(-1, drawableHeight)
            Assert.assertEquals(10, width)
            Assert.assertEquals(-1, height)
        }
        Divider.colorRes(R.color.test1, 20).toItemDividerWrapper(context, DividerSide.START).apply {
            Assert.assertEquals(20, drawableWidth)
            Assert.assertEquals(-1, drawableHeight)
            Assert.assertEquals(20, width)
            Assert.assertEquals(-1, height)
        }
        Divider.colorRes(R.color.test1, 10).toItemDividerWrapper(context, DividerSide.TOP).apply {
            Assert.assertEquals(-1, drawableWidth)
            Assert.assertEquals(10, drawableHeight)
            Assert.assertEquals(-1, width)
            Assert.assertEquals(10, height)
        }
        Divider.colorRes(R.color.test1, 20).toItemDividerWrapper(context, DividerSide.TOP).apply {
            Assert.assertEquals(-1, drawableWidth)
            Assert.assertEquals(20, drawableHeight)
            Assert.assertEquals(-1, width)
            Assert.assertEquals(20, height)
        }

        /**
         * inset size
         */
        Divider.colorRes(R.color.test1, 20, Insets.of(1, 2, 3, 4))
            .toItemDividerWrapper(context, DividerSide.START).apply {
                Assert.assertEquals(1, insetStart)
                Assert.assertEquals(2, insetTop)
                Assert.assertEquals(3, insetEnd)
                Assert.assertEquals(4, insetBottom)
                Assert.assertEquals(4, insetWidth)
                Assert.assertEquals(6, insetHeight)
                Assert.assertEquals(20, drawableWidth)
                Assert.assertEquals(-1, drawableHeight)
                Assert.assertEquals(24, width)
                Assert.assertEquals(5, height)
            }
        Divider.colorRes(R.color.test1, 20, Insets.of(4, 5, 6, 7))
            .toItemDividerWrapper(context, DividerSide.START).apply {
                Assert.assertEquals(4, insetStart)
                Assert.assertEquals(5, insetTop)
                Assert.assertEquals(6, insetEnd)
                Assert.assertEquals(7, insetBottom)
                Assert.assertEquals(10, insetWidth)
                Assert.assertEquals(12, insetHeight)
                Assert.assertEquals(20, drawableWidth)
                Assert.assertEquals(-1, drawableHeight)
                Assert.assertEquals(30, width)
                Assert.assertEquals(11, height)
            }
        Divider.colorRes(R.color.test1, 20, Insets.of(1, 2, 3, 4))
            .toItemDividerWrapper(context, DividerSide.TOP).apply {
                Assert.assertEquals(1, insetStart)
                Assert.assertEquals(2, insetTop)
                Assert.assertEquals(3, insetEnd)
                Assert.assertEquals(4, insetBottom)
                Assert.assertEquals(4, insetWidth)
                Assert.assertEquals(6, insetHeight)
                Assert.assertEquals(-1, drawableWidth)
                Assert.assertEquals(20, drawableHeight)
                Assert.assertEquals(3, width)
                Assert.assertEquals(26, height)
            }
        Divider.colorRes(R.color.test1, 20, Insets.of(4, 5, 6, 7))
            .toItemDividerWrapper(context, DividerSide.TOP).apply {
                Assert.assertEquals(4, insetStart)
                Assert.assertEquals(5, insetTop)
                Assert.assertEquals(6, insetEnd)
                Assert.assertEquals(7, insetBottom)
                Assert.assertEquals(10, insetWidth)
                Assert.assertEquals(12, insetHeight)
                Assert.assertEquals(-1, drawableWidth)
                Assert.assertEquals(20, drawableHeight)
                Assert.assertEquals(9, width)
                Assert.assertEquals(32, height)
            }
    }

    @Test
    fun testColorResWithSize() {
        val context = InstrumentationRegistry.getInstrumentation().context

        /**
         * color
         */
        Divider.colorResWithSize(R.color.test1, VagueDividerSize(5))
            .toItemDividerWrapper(context, DividerSide.START).apply {
                val colorDrawable = this.drawable as ColorDrawable
                Assert.assertEquals(
                    ResourcesCompat.getColor(context.resources, R.color.test1, null),
                    colorDrawable.color
                )
            }
        Divider.colorResWithSize(R.color.test2, VagueDividerSize(5))
            .toItemDividerWrapper(context, DividerSide.START).apply {
                val colorDrawable = this.drawable as ColorDrawable
                Assert.assertEquals(
                    ResourcesCompat.getColor(context.resources, R.color.test2, null),
                    colorDrawable.color
                )
            }

        /**
         * size
         */
        Divider.colorResWithSize(R.color.test1, VagueDividerSize(10))
            .toItemDividerWrapper(context, DividerSide.START).apply {
                Assert.assertEquals(10, drawableWidth)
                Assert.assertEquals(-1, drawableHeight)
                Assert.assertEquals(10, width)
                Assert.assertEquals(-1, height)
            }
        Divider.colorResWithSize(R.color.test1, VagueDividerSize(20))
            .toItemDividerWrapper(context, DividerSide.START).apply {
                Assert.assertEquals(20, drawableWidth)
                Assert.assertEquals(-1, drawableHeight)
                Assert.assertEquals(20, width)
                Assert.assertEquals(-1, height)
            }
        Divider.colorResWithSize(R.color.test1, VagueDividerSize(10))
            .toItemDividerWrapper(context, DividerSide.TOP).apply {
                Assert.assertEquals(-1, drawableWidth)
                Assert.assertEquals(10, drawableHeight)
                Assert.assertEquals(-1, width)
                Assert.assertEquals(10, height)
            }
        Divider.colorResWithSize(R.color.test1, VagueDividerSize(20))
            .toItemDividerWrapper(context, DividerSide.TOP).apply {
                Assert.assertEquals(-1, drawableWidth)
                Assert.assertEquals(20, drawableHeight)
                Assert.assertEquals(-1, width)
                Assert.assertEquals(20, height)
            }

        /**
         * inset size
         */
        Divider.colorResWithSize(R.color.test1, VagueDividerSize(20), Insets.of(1, 2, 3, 4))
            .toItemDividerWrapper(context, DividerSide.START).apply {
                Assert.assertEquals(1, insetStart)
                Assert.assertEquals(2, insetTop)
                Assert.assertEquals(3, insetEnd)
                Assert.assertEquals(4, insetBottom)
                Assert.assertEquals(4, insetWidth)
                Assert.assertEquals(6, insetHeight)
                Assert.assertEquals(20, drawableWidth)
                Assert.assertEquals(-1, drawableHeight)
                Assert.assertEquals(24, width)
                Assert.assertEquals(5, height)
            }
        Divider.colorResWithSize(R.color.test1, VagueDividerSize(20), Insets.of(4, 5, 6, 7))
            .toItemDividerWrapper(context, DividerSide.START).apply {
                Assert.assertEquals(4, insetStart)
                Assert.assertEquals(5, insetTop)
                Assert.assertEquals(6, insetEnd)
                Assert.assertEquals(7, insetBottom)
                Assert.assertEquals(10, insetWidth)
                Assert.assertEquals(12, insetHeight)
                Assert.assertEquals(20, drawableWidth)
                Assert.assertEquals(-1, drawableHeight)
                Assert.assertEquals(30, width)
                Assert.assertEquals(11, height)
            }
        Divider.colorResWithSize(R.color.test1, VagueDividerSize(20), Insets.of(1, 2, 3, 4))
            .toItemDividerWrapper(context, DividerSide.TOP).apply {
                Assert.assertEquals(1, insetStart)
                Assert.assertEquals(2, insetTop)
                Assert.assertEquals(3, insetEnd)
                Assert.assertEquals(4, insetBottom)
                Assert.assertEquals(4, insetWidth)
                Assert.assertEquals(6, insetHeight)
                Assert.assertEquals(-1, drawableWidth)
                Assert.assertEquals(20, drawableHeight)
                Assert.assertEquals(3, width)
                Assert.assertEquals(26, height)
            }
        Divider.colorResWithSize(R.color.test1, VagueDividerSize(20), Insets.of(4, 5, 6, 7))
            .toItemDividerWrapper(context, DividerSide.TOP).apply {
                Assert.assertEquals(4, insetStart)
                Assert.assertEquals(5, insetTop)
                Assert.assertEquals(6, insetEnd)
                Assert.assertEquals(7, insetBottom)
                Assert.assertEquals(10, insetWidth)
                Assert.assertEquals(12, insetHeight)
                Assert.assertEquals(-1, drawableWidth)
                Assert.assertEquals(20, drawableHeight)
                Assert.assertEquals(9, width)
                Assert.assertEquals(32, height)
            }
    }

    @Test
    fun testColorResWithClearlySize() {
        val context = InstrumentationRegistry.getInstrumentation().context

        /**
         * color
         */
        Divider.colorResWithClearlySize(R.color.test1, 5, 5)
            .toItemDividerWrapper(context, DividerSide.START).apply {
                val colorDrawable = this.drawable as ColorDrawable
                Assert.assertEquals(
                    ResourcesCompat.getColor(context.resources, R.color.test1, null),
                    colorDrawable.color
                )
            }
        Divider.colorResWithClearlySize(R.color.test2, 5, 5)
            .toItemDividerWrapper(context, DividerSide.START).apply {
                val colorDrawable = this.drawable as ColorDrawable
                Assert.assertEquals(
                    ResourcesCompat.getColor(context.resources, R.color.test2, null),
                    colorDrawable.color
                )
            }

        /**
         * size
         */
        Divider.colorResWithClearlySize(R.color.test1, 10, 20)
            .toItemDividerWrapper(context, DividerSide.START).apply {
                Assert.assertEquals(10, drawableWidth)
                Assert.assertEquals(20, drawableHeight)
                Assert.assertEquals(10, width)
                Assert.assertEquals(20, height)
            }
        Divider.colorResWithClearlySize(R.color.test1, 20, 40)
            .toItemDividerWrapper(context, DividerSide.START).apply {
                Assert.assertEquals(20, drawableWidth)
                Assert.assertEquals(40, drawableHeight)
                Assert.assertEquals(20, width)
                Assert.assertEquals(40, height)
            }
        Divider.colorResWithClearlySize(R.color.test1, 20, 10)
            .toItemDividerWrapper(context, DividerSide.TOP).apply {
                Assert.assertEquals(20, drawableWidth)
                Assert.assertEquals(10, drawableHeight)
                Assert.assertEquals(20, width)
                Assert.assertEquals(10, height)
            }
        Divider.colorResWithClearlySize(R.color.test1, 40, 20)
            .toItemDividerWrapper(context, DividerSide.TOP).apply {
                Assert.assertEquals(40, drawableWidth)
                Assert.assertEquals(20, drawableHeight)
                Assert.assertEquals(40, width)
                Assert.assertEquals(20, height)
            }

        /**
         * inset size
         */
        Divider.colorResWithClearlySize(R.color.test1, 20, 40, Insets.of(1, 2, 3, 4))
            .toItemDividerWrapper(context, DividerSide.START).apply {
                Assert.assertEquals(1, insetStart)
                Assert.assertEquals(2, insetTop)
                Assert.assertEquals(3, insetEnd)
                Assert.assertEquals(4, insetBottom)
                Assert.assertEquals(4, insetWidth)
                Assert.assertEquals(6, insetHeight)
                Assert.assertEquals(20, drawableWidth)
                Assert.assertEquals(40, drawableHeight)
                Assert.assertEquals(24, width)
                Assert.assertEquals(46, height)
            }
        Divider.colorResWithClearlySize(R.color.test1, 20, 40, Insets.of(4, 5, 6, 7))
            .toItemDividerWrapper(context, DividerSide.START).apply {
                Assert.assertEquals(4, insetStart)
                Assert.assertEquals(5, insetTop)
                Assert.assertEquals(6, insetEnd)
                Assert.assertEquals(7, insetBottom)
                Assert.assertEquals(10, insetWidth)
                Assert.assertEquals(12, insetHeight)
                Assert.assertEquals(20, drawableWidth)
                Assert.assertEquals(40, drawableHeight)
                Assert.assertEquals(30, width)
                Assert.assertEquals(52, height)
            }
        Divider.colorResWithClearlySize(R.color.test1, 40, 20, Insets.of(1, 2, 3, 4))
            .toItemDividerWrapper(context, DividerSide.TOP).apply {
                Assert.assertEquals(1, insetStart)
                Assert.assertEquals(2, insetTop)
                Assert.assertEquals(3, insetEnd)
                Assert.assertEquals(4, insetBottom)
                Assert.assertEquals(4, insetWidth)
                Assert.assertEquals(6, insetHeight)
                Assert.assertEquals(40, drawableWidth)
                Assert.assertEquals(20, drawableHeight)
                Assert.assertEquals(44, width)
                Assert.assertEquals(26, height)
            }
        Divider.colorResWithClearlySize(R.color.test1, 40, 20, Insets.of(4, 5, 6, 7))
            .toItemDividerWrapper(context, DividerSide.TOP).apply {
                Assert.assertEquals(4, insetStart)
                Assert.assertEquals(5, insetTop)
                Assert.assertEquals(6, insetEnd)
                Assert.assertEquals(7, insetBottom)
                Assert.assertEquals(10, insetWidth)
                Assert.assertEquals(12, insetHeight)
                Assert.assertEquals(40, drawableWidth)
                Assert.assertEquals(20, drawableHeight)
                Assert.assertEquals(50, width)
                Assert.assertEquals(32, height)
            }
    }

    @Test
    fun testDrawable() {
        val context = InstrumentationRegistry.getInstrumentation().context
        val drawable = ResourcesCompat.getDrawable(context.resources, R.drawable.dvider, null)!!

        /**
         * drawable
         */
        Divider.drawable(drawable).toItemDividerWrapper(context, DividerSide.START).apply {
            Assert.assertEquals(100, this.drawable.intrinsicWidth)
            Assert.assertEquals(5, this.drawable.intrinsicHeight)
        }

        /**
         * size
         */
        Divider.drawable(drawable, 10).toItemDividerWrapper(context, DividerSide.START).apply {
            Assert.assertEquals(100, this.drawable.intrinsicWidth)
            Assert.assertEquals(5, this.drawable.intrinsicHeight)
            Assert.assertEquals(10, drawableWidth)
            Assert.assertEquals(-1, drawableHeight)
            Assert.assertEquals(10, width)
            Assert.assertEquals(-1, height)
        }
        Divider.drawable(drawable, 20).toItemDividerWrapper(context, DividerSide.START).apply {
            Assert.assertEquals(100, this.drawable.intrinsicWidth)
            Assert.assertEquals(5, this.drawable.intrinsicHeight)
            Assert.assertEquals(20, drawableWidth)
            Assert.assertEquals(-1, drawableHeight)
            Assert.assertEquals(20, width)
            Assert.assertEquals(-1, height)
        }
        Divider.drawable(drawable, 10).toItemDividerWrapper(context, DividerSide.TOP).apply {
            Assert.assertEquals(100, this.drawable.intrinsicWidth)
            Assert.assertEquals(5, this.drawable.intrinsicHeight)
            Assert.assertEquals(-1, drawableWidth)
            Assert.assertEquals(10, drawableHeight)
            Assert.assertEquals(-1, width)
            Assert.assertEquals(10, height)
        }
        Divider.drawable(drawable, 20).toItemDividerWrapper(context, DividerSide.TOP).apply {
            Assert.assertEquals(100, this.drawable.intrinsicWidth)
            Assert.assertEquals(5, this.drawable.intrinsicHeight)
            Assert.assertEquals(-1, drawableWidth)
            Assert.assertEquals(20, drawableHeight)
            Assert.assertEquals(-1, width)
            Assert.assertEquals(20, height)
        }

        /**
         * inset size
         */
        Divider.drawable(drawable, 20, Insets.of(1, 2, 3, 4))
            .toItemDividerWrapper(context, DividerSide.START).apply {
                Assert.assertEquals(100, this.drawable.intrinsicWidth)
                Assert.assertEquals(5, this.drawable.intrinsicHeight)
                Assert.assertEquals(1, insetStart)
                Assert.assertEquals(2, insetTop)
                Assert.assertEquals(3, insetEnd)
                Assert.assertEquals(4, insetBottom)
                Assert.assertEquals(4, insetWidth)
                Assert.assertEquals(6, insetHeight)
                Assert.assertEquals(20, drawableWidth)
                Assert.assertEquals(-1, drawableHeight)
                Assert.assertEquals(24, width)
                Assert.assertEquals(5, height)
            }
        Divider.drawable(drawable, 20, Insets.of(4, 5, 6, 7))
            .toItemDividerWrapper(context, DividerSide.START).apply {
                Assert.assertEquals(100, this.drawable.intrinsicWidth)
                Assert.assertEquals(5, this.drawable.intrinsicHeight)
                Assert.assertEquals(4, insetStart)
                Assert.assertEquals(5, insetTop)
                Assert.assertEquals(6, insetEnd)
                Assert.assertEquals(7, insetBottom)
                Assert.assertEquals(10, insetWidth)
                Assert.assertEquals(12, insetHeight)
                Assert.assertEquals(20, drawableWidth)
                Assert.assertEquals(-1, drawableHeight)
                Assert.assertEquals(30, width)
                Assert.assertEquals(11, height)
            }
        Divider.drawable(drawable, 20, Insets.of(1, 2, 3, 4))
            .toItemDividerWrapper(context, DividerSide.TOP).apply {
                Assert.assertEquals(100, this.drawable.intrinsicWidth)
                Assert.assertEquals(5, this.drawable.intrinsicHeight)
                Assert.assertEquals(1, insetStart)
                Assert.assertEquals(2, insetTop)
                Assert.assertEquals(3, insetEnd)
                Assert.assertEquals(4, insetBottom)
                Assert.assertEquals(4, insetWidth)
                Assert.assertEquals(6, insetHeight)
                Assert.assertEquals(-1, drawableWidth)
                Assert.assertEquals(20, drawableHeight)
                Assert.assertEquals(3, width)
                Assert.assertEquals(26, height)
            }
        Divider.drawable(drawable, 20, Insets.of(4, 5, 6, 7))
            .toItemDividerWrapper(context, DividerSide.TOP).apply {
                Assert.assertEquals(100, this.drawable.intrinsicWidth)
                Assert.assertEquals(5, this.drawable.intrinsicHeight)
                Assert.assertEquals(4, insetStart)
                Assert.assertEquals(5, insetTop)
                Assert.assertEquals(6, insetEnd)
                Assert.assertEquals(7, insetBottom)
                Assert.assertEquals(10, insetWidth)
                Assert.assertEquals(12, insetHeight)
                Assert.assertEquals(-1, drawableWidth)
                Assert.assertEquals(20, drawableHeight)
                Assert.assertEquals(9, width)
                Assert.assertEquals(32, height)
            }
    }

    @Test
    fun testDrawableWithSize() {
        val context = InstrumentationRegistry.getInstrumentation().context
        val drawable = ResourcesCompat.getDrawable(context.resources, R.drawable.dvider, null)!!

        /**
         * drawable
         */
        Divider.drawableWithSize(drawable, VagueDividerSize(5))
            .toItemDividerWrapper(context, DividerSide.START).apply {
                Assert.assertEquals(100, this.drawable.intrinsicWidth)
                Assert.assertEquals(5, this.drawable.intrinsicHeight)
            }

        /**
         * size
         */
        Divider.drawableWithSize(drawable, VagueDividerSize(10))
            .toItemDividerWrapper(context, DividerSide.START).apply {
                Assert.assertEquals(100, this.drawable.intrinsicWidth)
                Assert.assertEquals(5, this.drawable.intrinsicHeight)
                Assert.assertEquals(10, drawableWidth)
                Assert.assertEquals(-1, drawableHeight)
                Assert.assertEquals(10, width)
                Assert.assertEquals(-1, height)
            }
        Divider.drawableWithSize(drawable, VagueDividerSize(20))
            .toItemDividerWrapper(context, DividerSide.START).apply {
                Assert.assertEquals(100, this.drawable.intrinsicWidth)
                Assert.assertEquals(5, this.drawable.intrinsicHeight)
                Assert.assertEquals(20, drawableWidth)
                Assert.assertEquals(-1, drawableHeight)
                Assert.assertEquals(20, width)
                Assert.assertEquals(-1, height)
            }
        Divider.drawableWithSize(drawable, VagueDividerSize(10))
            .toItemDividerWrapper(context, DividerSide.TOP).apply {
                Assert.assertEquals(100, this.drawable.intrinsicWidth)
                Assert.assertEquals(5, this.drawable.intrinsicHeight)
                Assert.assertEquals(-1, drawableWidth)
                Assert.assertEquals(10, drawableHeight)
                Assert.assertEquals(-1, width)
                Assert.assertEquals(10, height)
            }
        Divider.drawableWithSize(drawable, VagueDividerSize(20))
            .toItemDividerWrapper(context, DividerSide.TOP).apply {
                Assert.assertEquals(100, this.drawable.intrinsicWidth)
                Assert.assertEquals(5, this.drawable.intrinsicHeight)
                Assert.assertEquals(-1, drawableWidth)
                Assert.assertEquals(20, drawableHeight)
                Assert.assertEquals(-1, width)
                Assert.assertEquals(20, height)
            }

        /**
         * inset size
         */
        Divider.drawableWithSize(drawable, VagueDividerSize(20), Insets.of(1, 2, 3, 4))
            .toItemDividerWrapper(context, DividerSide.START).apply {
                Assert.assertEquals(100, this.drawable.intrinsicWidth)
                Assert.assertEquals(5, this.drawable.intrinsicHeight)
                Assert.assertEquals(1, insetStart)
                Assert.assertEquals(2, insetTop)
                Assert.assertEquals(3, insetEnd)
                Assert.assertEquals(4, insetBottom)
                Assert.assertEquals(4, insetWidth)
                Assert.assertEquals(6, insetHeight)
                Assert.assertEquals(20, drawableWidth)
                Assert.assertEquals(-1, drawableHeight)
                Assert.assertEquals(24, width)
                Assert.assertEquals(5, height)
            }
        Divider.drawableWithSize(drawable, VagueDividerSize(20), Insets.of(4, 5, 6, 7))
            .toItemDividerWrapper(context, DividerSide.START).apply {
                Assert.assertEquals(100, this.drawable.intrinsicWidth)
                Assert.assertEquals(5, this.drawable.intrinsicHeight)
                Assert.assertEquals(4, insetStart)
                Assert.assertEquals(5, insetTop)
                Assert.assertEquals(6, insetEnd)
                Assert.assertEquals(7, insetBottom)
                Assert.assertEquals(10, insetWidth)
                Assert.assertEquals(12, insetHeight)
                Assert.assertEquals(20, drawableWidth)
                Assert.assertEquals(-1, drawableHeight)
                Assert.assertEquals(30, width)
                Assert.assertEquals(11, height)
            }
        Divider.drawableWithSize(drawable, VagueDividerSize(20), Insets.of(1, 2, 3, 4))
            .toItemDividerWrapper(context, DividerSide.TOP).apply {
                Assert.assertEquals(100, this.drawable.intrinsicWidth)
                Assert.assertEquals(5, this.drawable.intrinsicHeight)
                Assert.assertEquals(1, insetStart)
                Assert.assertEquals(2, insetTop)
                Assert.assertEquals(3, insetEnd)
                Assert.assertEquals(4, insetBottom)
                Assert.assertEquals(4, insetWidth)
                Assert.assertEquals(6, insetHeight)
                Assert.assertEquals(-1, drawableWidth)
                Assert.assertEquals(20, drawableHeight)
                Assert.assertEquals(3, width)
                Assert.assertEquals(26, height)
            }
        Divider.drawableWithSize(drawable, VagueDividerSize(20), Insets.of(4, 5, 6, 7))
            .toItemDividerWrapper(context, DividerSide.TOP).apply {
                Assert.assertEquals(100, this.drawable.intrinsicWidth)
                Assert.assertEquals(5, this.drawable.intrinsicHeight)
                Assert.assertEquals(4, insetStart)
                Assert.assertEquals(5, insetTop)
                Assert.assertEquals(6, insetEnd)
                Assert.assertEquals(7, insetBottom)
                Assert.assertEquals(10, insetWidth)
                Assert.assertEquals(12, insetHeight)
                Assert.assertEquals(-1, drawableWidth)
                Assert.assertEquals(20, drawableHeight)
                Assert.assertEquals(9, width)
                Assert.assertEquals(32, height)
            }
    }

    @Test
    fun testDrawableWithClearlySize() {
        val context = InstrumentationRegistry.getInstrumentation().context
        val drawable = ResourcesCompat.getDrawable(context.resources, R.drawable.dvider, null)!!

        /**
         * drawable
         */
        Divider.drawableWithClearlySize(drawable, 10, 20)
            .toItemDividerWrapper(context, DividerSide.START).apply {
                Assert.assertEquals(100, this.drawable.intrinsicWidth)
                Assert.assertEquals(5, this.drawable.intrinsicHeight)
            }
        Divider.drawableWithClearlySize(drawable, 10, 20)
            .toItemDividerWrapper(context, DividerSide.TOP).apply {
                Assert.assertEquals(100, this.drawable.intrinsicWidth)
                Assert.assertEquals(5, this.drawable.intrinsicHeight)
            }

        /**
         * size
         */
        Divider.drawableWithClearlySize(drawable, 10, 20)
            .toItemDividerWrapper(context, DividerSide.START).apply {
                Assert.assertEquals(100, this.drawable.intrinsicWidth)
                Assert.assertEquals(5, this.drawable.intrinsicHeight)
                Assert.assertEquals(10, drawableWidth)
                Assert.assertEquals(20, drawableHeight)
                Assert.assertEquals(10, width)
                Assert.assertEquals(20, height)
            }
        Divider.drawableWithClearlySize(drawable, 20, 40)
            .toItemDividerWrapper(context, DividerSide.START).apply {
                Assert.assertEquals(100, this.drawable.intrinsicWidth)
                Assert.assertEquals(5, this.drawable.intrinsicHeight)
                Assert.assertEquals(20, drawableWidth)
                Assert.assertEquals(40, drawableHeight)
                Assert.assertEquals(20, width)
                Assert.assertEquals(40, height)
            }
        Divider.drawableWithClearlySize(drawable, 10, 20)
            .toItemDividerWrapper(context, DividerSide.TOP).apply {
                Assert.assertEquals(100, this.drawable.intrinsicWidth)
                Assert.assertEquals(5, this.drawable.intrinsicHeight)
                Assert.assertEquals(10, drawableWidth)
                Assert.assertEquals(20, drawableHeight)
                Assert.assertEquals(10, width)
                Assert.assertEquals(20, height)
            }
        Divider.drawableWithClearlySize(drawable, 20, 40)
            .toItemDividerWrapper(context, DividerSide.TOP).apply {
                Assert.assertEquals(100, this.drawable.intrinsicWidth)
                Assert.assertEquals(5, this.drawable.intrinsicHeight)
                Assert.assertEquals(20, drawableWidth)
                Assert.assertEquals(40, drawableHeight)
                Assert.assertEquals(20, width)
                Assert.assertEquals(40, height)
            }

        /**
         * inset size
         */
        Divider.drawableWithClearlySize(drawable, 20, 40, Insets.of(1, 2, 3, 4))
            .toItemDividerWrapper(context, DividerSide.START).apply {
                Assert.assertEquals(100, this.drawable.intrinsicWidth)
                Assert.assertEquals(5, this.drawable.intrinsicHeight)
                Assert.assertEquals(1, insetStart)
                Assert.assertEquals(2, insetTop)
                Assert.assertEquals(3, insetEnd)
                Assert.assertEquals(4, insetBottom)
                Assert.assertEquals(4, insetWidth)
                Assert.assertEquals(6, insetHeight)
                Assert.assertEquals(20, drawableWidth)
                Assert.assertEquals(40, drawableHeight)
                Assert.assertEquals(24, width)
                Assert.assertEquals(46, height)
            }
        Divider.drawableWithClearlySize(drawable, 20, 40, Insets.of(4, 5, 6, 7))
            .toItemDividerWrapper(context, DividerSide.START).apply {
                Assert.assertEquals(100, this.drawable.intrinsicWidth)
                Assert.assertEquals(5, this.drawable.intrinsicHeight)
                Assert.assertEquals(4, insetStart)
                Assert.assertEquals(5, insetTop)
                Assert.assertEquals(6, insetEnd)
                Assert.assertEquals(7, insetBottom)
                Assert.assertEquals(10, insetWidth)
                Assert.assertEquals(12, insetHeight)
                Assert.assertEquals(20, drawableWidth)
                Assert.assertEquals(40, drawableHeight)
                Assert.assertEquals(30, width)
                Assert.assertEquals(52, height)
            }
        Divider.drawableWithClearlySize(drawable, 20, 40, Insets.of(1, 2, 3, 4))
            .toItemDividerWrapper(context, DividerSide.TOP).apply {
                Assert.assertEquals(100, this.drawable.intrinsicWidth)
                Assert.assertEquals(5, this.drawable.intrinsicHeight)
                Assert.assertEquals(1, insetStart)
                Assert.assertEquals(2, insetTop)
                Assert.assertEquals(3, insetEnd)
                Assert.assertEquals(4, insetBottom)
                Assert.assertEquals(4, insetWidth)
                Assert.assertEquals(6, insetHeight)
                Assert.assertEquals(20, drawableWidth)
                Assert.assertEquals(40, drawableHeight)
                Assert.assertEquals(24, width)
                Assert.assertEquals(46, height)
            }
        Divider.drawableWithClearlySize(drawable, 20, 40, Insets.of(4, 5, 6, 7))
            .toItemDividerWrapper(context, DividerSide.TOP).apply {
                Assert.assertEquals(100, this.drawable.intrinsicWidth)
                Assert.assertEquals(5, this.drawable.intrinsicHeight)
                Assert.assertEquals(4, insetStart)
                Assert.assertEquals(5, insetTop)
                Assert.assertEquals(6, insetEnd)
                Assert.assertEquals(7, insetBottom)
                Assert.assertEquals(10, insetWidth)
                Assert.assertEquals(12, insetHeight)
                Assert.assertEquals(20, drawableWidth)
                Assert.assertEquals(40, drawableHeight)
                Assert.assertEquals(30, width)
                Assert.assertEquals(52, height)
            }
    }

    @Test
    fun testDrawableRes() {
        val context = InstrumentationRegistry.getInstrumentation().context

        /**
         * drawable
         */
        Divider.drawableRes(R.drawable.dvider).toItemDividerWrapper(context, DividerSide.START)
            .apply {
                Assert.assertEquals(100, this.drawable.intrinsicWidth)
                Assert.assertEquals(5, this.drawable.intrinsicHeight)
            }

        /**
         * size
         */
        Divider.drawableRes(R.drawable.dvider, 10).toItemDividerWrapper(context, DividerSide.START)
            .apply {
                Assert.assertEquals(100, this.drawable.intrinsicWidth)
                Assert.assertEquals(5, this.drawable.intrinsicHeight)
                Assert.assertEquals(10, drawableWidth)
                Assert.assertEquals(-1, drawableHeight)
                Assert.assertEquals(10, width)
                Assert.assertEquals(-1, height)
            }
        Divider.drawableRes(R.drawable.dvider, 20).toItemDividerWrapper(context, DividerSide.START)
            .apply {
                Assert.assertEquals(100, this.drawable.intrinsicWidth)
                Assert.assertEquals(5, this.drawable.intrinsicHeight)
                Assert.assertEquals(20, drawableWidth)
                Assert.assertEquals(-1, drawableHeight)
                Assert.assertEquals(20, width)
                Assert.assertEquals(-1, height)
            }
        Divider.drawableRes(R.drawable.dvider, 10).toItemDividerWrapper(context, DividerSide.TOP)
            .apply {
                Assert.assertEquals(100, this.drawable.intrinsicWidth)
                Assert.assertEquals(5, this.drawable.intrinsicHeight)
                Assert.assertEquals(-1, drawableWidth)
                Assert.assertEquals(10, drawableHeight)
                Assert.assertEquals(-1, width)
                Assert.assertEquals(10, height)
            }
        Divider.drawableRes(R.drawable.dvider, 20).toItemDividerWrapper(context, DividerSide.TOP)
            .apply {
                Assert.assertEquals(100, this.drawable.intrinsicWidth)
                Assert.assertEquals(5, this.drawable.intrinsicHeight)
                Assert.assertEquals(-1, drawableWidth)
                Assert.assertEquals(20, drawableHeight)
                Assert.assertEquals(-1, width)
                Assert.assertEquals(20, height)
            }

        /**
         * inset size
         */
        Divider.drawableRes(R.drawable.dvider, 20, Insets.of(1, 2, 3, 4))
            .toItemDividerWrapper(context, DividerSide.START)
            .apply {
                Assert.assertEquals(100, this.drawable.intrinsicWidth)
                Assert.assertEquals(5, this.drawable.intrinsicHeight)
                Assert.assertEquals(1, insetStart)
                Assert.assertEquals(2, insetTop)
                Assert.assertEquals(3, insetEnd)
                Assert.assertEquals(4, insetBottom)
                Assert.assertEquals(4, insetWidth)
                Assert.assertEquals(6, insetHeight)
                Assert.assertEquals(20, drawableWidth)
                Assert.assertEquals(-1, drawableHeight)
                Assert.assertEquals(24, width)
                Assert.assertEquals(5, height)
            }
        Divider.drawableRes(R.drawable.dvider, 20, Insets.of(4, 5, 6, 7))
            .toItemDividerWrapper(context, DividerSide.START)
            .apply {
                Assert.assertEquals(100, this.drawable.intrinsicWidth)
                Assert.assertEquals(5, this.drawable.intrinsicHeight)
                Assert.assertEquals(4, insetStart)
                Assert.assertEquals(5, insetTop)
                Assert.assertEquals(6, insetEnd)
                Assert.assertEquals(7, insetBottom)
                Assert.assertEquals(10, insetWidth)
                Assert.assertEquals(12, insetHeight)
                Assert.assertEquals(20, drawableWidth)
                Assert.assertEquals(-1, drawableHeight)
                Assert.assertEquals(30, width)
                Assert.assertEquals(11, height)
            }
        Divider.drawableRes(R.drawable.dvider, 20, Insets.of(1, 2, 3, 4))
            .toItemDividerWrapper(context, DividerSide.TOP)
            .apply {
                Assert.assertEquals(100, this.drawable.intrinsicWidth)
                Assert.assertEquals(5, this.drawable.intrinsicHeight)
                Assert.assertEquals(1, insetStart)
                Assert.assertEquals(2, insetTop)
                Assert.assertEquals(3, insetEnd)
                Assert.assertEquals(4, insetBottom)
                Assert.assertEquals(4, insetWidth)
                Assert.assertEquals(6, insetHeight)
                Assert.assertEquals(-1, drawableWidth)
                Assert.assertEquals(20, drawableHeight)
                Assert.assertEquals(3, width)
                Assert.assertEquals(26, height)
            }
        Divider.drawableRes(R.drawable.dvider, 20, Insets.of(4, 5, 6, 7))
            .toItemDividerWrapper(context, DividerSide.TOP)
            .apply {
                Assert.assertEquals(100, this.drawable.intrinsicWidth)
                Assert.assertEquals(5, this.drawable.intrinsicHeight)
                Assert.assertEquals(4, insetStart)
                Assert.assertEquals(5, insetTop)
                Assert.assertEquals(6, insetEnd)
                Assert.assertEquals(7, insetBottom)
                Assert.assertEquals(10, insetWidth)
                Assert.assertEquals(12, insetHeight)
                Assert.assertEquals(-1, drawableWidth)
                Assert.assertEquals(20, drawableHeight)
                Assert.assertEquals(9, width)
                Assert.assertEquals(32, height)
            }
    }

    @Test
    fun testDrawableResWithSize() {
        val context = InstrumentationRegistry.getInstrumentation().context

        /**
         * drawable
         */
        Divider.drawableResWithSize(R.drawable.dvider, VagueDividerSize(5))
            .toItemDividerWrapper(context, DividerSide.START)
            .apply {
                Assert.assertEquals(100, this.drawable.intrinsicWidth)
                Assert.assertEquals(5, this.drawable.intrinsicHeight)
            }

        /**
         * size
         */
        Divider.drawableResWithSize(R.drawable.dvider, VagueDividerSize(10))
            .toItemDividerWrapper(context, DividerSide.START)
            .apply {
                Assert.assertEquals(100, this.drawable.intrinsicWidth)
                Assert.assertEquals(5, this.drawable.intrinsicHeight)
                Assert.assertEquals(10, drawableWidth)
                Assert.assertEquals(-1, drawableHeight)
                Assert.assertEquals(10, width)
                Assert.assertEquals(-1, height)
            }
        Divider.drawableResWithSize(R.drawable.dvider, VagueDividerSize(20))
            .toItemDividerWrapper(context, DividerSide.START)
            .apply {
                Assert.assertEquals(100, this.drawable.intrinsicWidth)
                Assert.assertEquals(5, this.drawable.intrinsicHeight)
                Assert.assertEquals(20, drawableWidth)
                Assert.assertEquals(-1, drawableHeight)
                Assert.assertEquals(20, width)
                Assert.assertEquals(-1, height)
            }
        Divider.drawableResWithSize(R.drawable.dvider, VagueDividerSize(10))
            .toItemDividerWrapper(context, DividerSide.TOP)
            .apply {
                Assert.assertEquals(100, this.drawable.intrinsicWidth)
                Assert.assertEquals(5, this.drawable.intrinsicHeight)
                Assert.assertEquals(-1, drawableWidth)
                Assert.assertEquals(10, drawableHeight)
                Assert.assertEquals(-1, width)
                Assert.assertEquals(10, height)
            }
        Divider.drawableResWithSize(R.drawable.dvider, VagueDividerSize(20))
            .toItemDividerWrapper(context, DividerSide.TOP)
            .apply {
                Assert.assertEquals(100, this.drawable.intrinsicWidth)
                Assert.assertEquals(5, this.drawable.intrinsicHeight)
                Assert.assertEquals(-1, drawableWidth)
                Assert.assertEquals(20, drawableHeight)
                Assert.assertEquals(-1, width)
                Assert.assertEquals(20, height)
            }

        /**
         * inset size
         */
        Divider.drawableResWithSize(R.drawable.dvider, VagueDividerSize(20), Insets.of(1, 2, 3, 4))
            .toItemDividerWrapper(context, DividerSide.START)
            .apply {
                Assert.assertEquals(100, this.drawable.intrinsicWidth)
                Assert.assertEquals(5, this.drawable.intrinsicHeight)
                Assert.assertEquals(1, insetStart)
                Assert.assertEquals(2, insetTop)
                Assert.assertEquals(3, insetEnd)
                Assert.assertEquals(4, insetBottom)
                Assert.assertEquals(4, insetWidth)
                Assert.assertEquals(6, insetHeight)
                Assert.assertEquals(20, drawableWidth)
                Assert.assertEquals(-1, drawableHeight)
                Assert.assertEquals(24, width)
                Assert.assertEquals(5, height)
            }
        Divider.drawableResWithSize(R.drawable.dvider, VagueDividerSize(20), Insets.of(4, 5, 6, 7))
            .toItemDividerWrapper(context, DividerSide.START)
            .apply {
                Assert.assertEquals(100, this.drawable.intrinsicWidth)
                Assert.assertEquals(5, this.drawable.intrinsicHeight)
                Assert.assertEquals(4, insetStart)
                Assert.assertEquals(5, insetTop)
                Assert.assertEquals(6, insetEnd)
                Assert.assertEquals(7, insetBottom)
                Assert.assertEquals(10, insetWidth)
                Assert.assertEquals(12, insetHeight)
                Assert.assertEquals(20, drawableWidth)
                Assert.assertEquals(-1, drawableHeight)
                Assert.assertEquals(30, width)
                Assert.assertEquals(11, height)
            }
        Divider.drawableResWithSize(R.drawable.dvider, VagueDividerSize(20), Insets.of(1, 2, 3, 4))
            .toItemDividerWrapper(context, DividerSide.TOP)
            .apply {
                Assert.assertEquals(100, this.drawable.intrinsicWidth)
                Assert.assertEquals(5, this.drawable.intrinsicHeight)
                Assert.assertEquals(1, insetStart)
                Assert.assertEquals(2, insetTop)
                Assert.assertEquals(3, insetEnd)
                Assert.assertEquals(4, insetBottom)
                Assert.assertEquals(4, insetWidth)
                Assert.assertEquals(6, insetHeight)
                Assert.assertEquals(-1, drawableWidth)
                Assert.assertEquals(20, drawableHeight)
                Assert.assertEquals(3, width)
                Assert.assertEquals(26, height)
            }
        Divider.drawableResWithSize(R.drawable.dvider, VagueDividerSize(20), Insets.of(4, 5, 6, 7))
            .toItemDividerWrapper(context, DividerSide.TOP)
            .apply {
                Assert.assertEquals(100, this.drawable.intrinsicWidth)
                Assert.assertEquals(5, this.drawable.intrinsicHeight)
                Assert.assertEquals(4, insetStart)
                Assert.assertEquals(5, insetTop)
                Assert.assertEquals(6, insetEnd)
                Assert.assertEquals(7, insetBottom)
                Assert.assertEquals(10, insetWidth)
                Assert.assertEquals(12, insetHeight)
                Assert.assertEquals(-1, drawableWidth)
                Assert.assertEquals(20, drawableHeight)
                Assert.assertEquals(9, width)
                Assert.assertEquals(32, height)
            }
    }

    @Test
    fun testDrawableResWithClearlySize() {
        val context = InstrumentationRegistry.getInstrumentation().context

        /**
         * drawable
         */
        Divider.drawableResWithClearlySize(R.drawable.dvider, 10, 20)
            .toItemDividerWrapper(context, DividerSide.START).apply {
                Assert.assertEquals(100, this.drawable.intrinsicWidth)
                Assert.assertEquals(5, this.drawable.intrinsicHeight)
            }
        Divider.drawableResWithClearlySize(R.drawable.dvider, 10, 20)
            .toItemDividerWrapper(context, DividerSide.TOP).apply {
                Assert.assertEquals(100, this.drawable.intrinsicWidth)
                Assert.assertEquals(5, this.drawable.intrinsicHeight)
            }

        /**
         * size
         */
        Divider.drawableResWithClearlySize(R.drawable.dvider, 10, 20)
            .toItemDividerWrapper(context, DividerSide.START).apply {
                Assert.assertEquals(100, this.drawable.intrinsicWidth)
                Assert.assertEquals(5, this.drawable.intrinsicHeight)
                Assert.assertEquals(10, drawableWidth)
                Assert.assertEquals(20, drawableHeight)
                Assert.assertEquals(10, width)
                Assert.assertEquals(20, height)
            }
        Divider.drawableResWithClearlySize(R.drawable.dvider, 20, 40)
            .toItemDividerWrapper(context, DividerSide.START).apply {
                Assert.assertEquals(100, this.drawable.intrinsicWidth)
                Assert.assertEquals(5, this.drawable.intrinsicHeight)
                Assert.assertEquals(20, drawableWidth)
                Assert.assertEquals(40, drawableHeight)
                Assert.assertEquals(20, width)
                Assert.assertEquals(40, height)
            }
        Divider.drawableResWithClearlySize(R.drawable.dvider, 10, 20)
            .toItemDividerWrapper(context, DividerSide.TOP).apply {
                Assert.assertEquals(100, this.drawable.intrinsicWidth)
                Assert.assertEquals(5, this.drawable.intrinsicHeight)
                Assert.assertEquals(10, drawableWidth)
                Assert.assertEquals(20, drawableHeight)
                Assert.assertEquals(10, width)
                Assert.assertEquals(20, height)
            }
        Divider.drawableResWithClearlySize(R.drawable.dvider, 20, 40)
            .toItemDividerWrapper(context, DividerSide.TOP).apply {
                Assert.assertEquals(100, this.drawable.intrinsicWidth)
                Assert.assertEquals(5, this.drawable.intrinsicHeight)
                Assert.assertEquals(20, drawableWidth)
                Assert.assertEquals(40, drawableHeight)
                Assert.assertEquals(20, width)
                Assert.assertEquals(40, height)
            }

        /**
         * inset size
         */
        Divider.drawableResWithClearlySize(R.drawable.dvider, 20, 40, Insets.of(1, 2, 3, 4))
            .toItemDividerWrapper(context, DividerSide.START).apply {
                Assert.assertEquals(100, this.drawable.intrinsicWidth)
                Assert.assertEquals(5, this.drawable.intrinsicHeight)
                Assert.assertEquals(1, insetStart)
                Assert.assertEquals(2, insetTop)
                Assert.assertEquals(3, insetEnd)
                Assert.assertEquals(4, insetBottom)
                Assert.assertEquals(4, insetWidth)
                Assert.assertEquals(6, insetHeight)
                Assert.assertEquals(20, drawableWidth)
                Assert.assertEquals(40, drawableHeight)
                Assert.assertEquals(24, width)
                Assert.assertEquals(46, height)
            }
        Divider.drawableResWithClearlySize(R.drawable.dvider, 20, 40, Insets.of(4, 5, 6, 7))
            .toItemDividerWrapper(context, DividerSide.START).apply {
                Assert.assertEquals(100, this.drawable.intrinsicWidth)
                Assert.assertEquals(5, this.drawable.intrinsicHeight)
                Assert.assertEquals(4, insetStart)
                Assert.assertEquals(5, insetTop)
                Assert.assertEquals(6, insetEnd)
                Assert.assertEquals(7, insetBottom)
                Assert.assertEquals(10, insetWidth)
                Assert.assertEquals(12, insetHeight)
                Assert.assertEquals(20, drawableWidth)
                Assert.assertEquals(40, drawableHeight)
                Assert.assertEquals(30, width)
                Assert.assertEquals(52, height)
            }
        Divider.drawableResWithClearlySize(R.drawable.dvider, 20, 40, Insets.of(1, 2, 3, 4))
            .toItemDividerWrapper(context, DividerSide.TOP).apply {
                Assert.assertEquals(100, this.drawable.intrinsicWidth)
                Assert.assertEquals(5, this.drawable.intrinsicHeight)
                Assert.assertEquals(1, insetStart)
                Assert.assertEquals(2, insetTop)
                Assert.assertEquals(3, insetEnd)
                Assert.assertEquals(4, insetBottom)
                Assert.assertEquals(4, insetWidth)
                Assert.assertEquals(6, insetHeight)
                Assert.assertEquals(20, drawableWidth)
                Assert.assertEquals(40, drawableHeight)
                Assert.assertEquals(24, width)
                Assert.assertEquals(46, height)
            }
        Divider.drawableResWithClearlySize(R.drawable.dvider, 20, 40, Insets.of(4, 5, 6, 7))
            .toItemDividerWrapper(context, DividerSide.TOP).apply {
                Assert.assertEquals(100, this.drawable.intrinsicWidth)
                Assert.assertEquals(5, this.drawable.intrinsicHeight)
                Assert.assertEquals(4, insetStart)
                Assert.assertEquals(5, insetTop)
                Assert.assertEquals(6, insetEnd)
                Assert.assertEquals(7, insetBottom)
                Assert.assertEquals(10, insetWidth)
                Assert.assertEquals(12, insetHeight)
                Assert.assertEquals(20, drawableWidth)
                Assert.assertEquals(40, drawableHeight)
                Assert.assertEquals(30, width)
                Assert.assertEquals(52, height)
            }
    }
}