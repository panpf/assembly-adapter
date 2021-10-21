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
import android.graphics.Canvas
import android.graphics.Rect
import android.view.View
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.github.panpf.assemblyadapter.recycler.divider.internal.StaggeredGridDividerItemDecorationHelper
import com.github.panpf.assemblyadapter.recycler.divider.internal.StaggeredGridItemDividerProvider

/**
 * [StaggeredGridLayoutManager] dedicated divider ItemDecoration. Support divider、header and footer divider、side divider、header and footer side divider
 */
open class StaggeredGridDividerItemDecoration(
    val itemDividerProvider: StaggeredGridItemDividerProvider,
    val isFullSpanByPosition: IsFullSpanByPosition?,
) : ItemDecoration() {

    private val itemDecorationHelper =
        StaggeredGridDividerItemDecorationHelper(itemDividerProvider)

    init {
        if (itemDividerProvider.hasHeaderOrFooterDivider() && isFullSpanByPosition == null) {
            throw IllegalArgumentException(
                "Must be set the 'isFullSpanByPosition' property, because you configured 'headerDivider' or 'footerDivider'"
            )
        }
    }

    override fun getItemOffsets(
        outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State
    ) {
        val layoutManager = parent.layoutManager ?: return
        if (layoutManager !is StaggeredGridLayoutManager) {
            throw IllegalArgumentException("layoutManager must be StaggeredGridLayoutManager")
        }
        val itemCount = layoutManager.itemCount.takeIf { it != 0 } ?: return
        val childLayoutParams = view.layoutParams as StaggeredGridLayoutManager.LayoutParams
        val position = childLayoutParams.absoluteAdapterPosition.takeIf { it != -1 } ?: return
        val isVerticalOrientation = layoutManager.orientation == StaggeredGridLayoutManager.VERTICAL
        val isLTRDirection = layoutManager.layoutDirection == ViewCompat.LAYOUT_DIRECTION_LTR
        val spanCount = layoutManager.spanCount
        val spanIndex = childLayoutParams.spanIndex
        val isFullSpan = childLayoutParams.isFullSpan
        val isColumnFirst = if (isFullSpanByPosition != null && position < spanCount) {
            when {
                isFullSpan -> position == 0
                0.rangeTo(position).all { // The position before is all not fullSpan
                    !isFullSpanByPosition.isFullSpan(parent, it)
                } -> true
                else -> false
            }
        } else {
            false
        }
        val isColumnEnd =
            if (isFullSpanByPosition != null && position >= itemCount - spanCount) {
                when {
                    isFullSpan -> position == itemCount - 1
                    position.until(itemCount).all { // The position back is all not fullSpan
                        !isFullSpanByPosition.isFullSpan(parent, it)
                    } -> true
                    else -> false
                }
            } else {
                false
            }
        itemDecorationHelper.getItemOffsets(
            outRect,
            view,
            parent,
            itemCount,
            position,
            spanCount,
            isFullSpan,
            spanIndex,
            isColumnFirst,
            isColumnEnd,
            isVerticalOrientation,
            isLTRDirection
        )
    }

    override fun onDraw(canvas: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        val layoutManager = parent.layoutManager ?: return
        if (layoutManager !is StaggeredGridLayoutManager) {
            throw IllegalArgumentException("layoutManager must be StaggeredGridLayoutManager")
        }
        val itemCount = layoutManager.itemCount.takeIf { it != 0 } ?: return
        val childCount = parent.childCount.takeIf { it != 0 } ?: return
        val isVerticalOrientation = layoutManager.orientation == GridLayoutManager.VERTICAL
        val isLTRDirection = layoutManager.layoutDirection == ViewCompat.LAYOUT_DIRECTION_LTR
        val spanCount = layoutManager.spanCount

        for (index in 0 until childCount) {
            val view = parent.getChildAt(index)
            val childLayoutParams = view.layoutParams as StaggeredGridLayoutManager.LayoutParams
            val position = childLayoutParams.absoluteAdapterPosition.takeIf { it != -1 } ?: continue
            val spanIndex = childLayoutParams.spanIndex
            val isFullSpan = childLayoutParams.isFullSpan
            val isColumnFirst = if (isFullSpanByPosition != null && position < spanCount) {
                when {
                    isFullSpan -> position == 0
                    0.rangeTo(position).all { // The position before is all not fullSpan
                        !isFullSpanByPosition.isFullSpan(parent, it)
                    } -> true
                    else -> false
                }
            } else {
                false
            }
            val isColumnEnd =
                if (isFullSpanByPosition != null && position >= itemCount - spanCount) {
                    when {
                        isFullSpan -> position == itemCount - 1
                        position.until(itemCount).all { // The position back is all not fullSpan
                            !isFullSpanByPosition.isFullSpan(parent, it)
                        } -> true
                        else -> false
                    }
                } else {
                    false
                }
            itemDecorationHelper.drawItem(
                canvas,
                view,
                parent,
                itemCount,
                position,
                spanCount,
                isFullSpan,
                spanIndex,
                isColumnFirst,
                isColumnEnd,
                isVerticalOrientation,
                isLTRDirection
            )
        }
    }

    class Builder(val context: Context) {

        private var dividerConfig: DividerConfig? = null
        private var headerDividerConfig: DividerConfig? = null
        private var footerDividerConfig: DividerConfig? = null
        private var useDividerAsHeaderDivider = false
        private var useDividerAsFooterDivider = false

        private var sideDividerConfig: DividerConfig? = null
        private var sideHeaderAndFooterDividerConfig: DividerConfig? = null
        private var useSideDividerAsSideHeaderAndFooterDivider = false

        private var disableDefaultDivider = false
        private var isFullSpanByPosition: IsFullSpanByPosition? = null

        fun build(): StaggeredGridDividerItemDecoration {
            return StaggeredGridDividerItemDecoration(
                buildItemDividerProvider(),
                isFullSpanByPosition
            )
        }

        private fun buildItemDividerProvider(): StaggeredGridItemDividerProvider {
            if ((useSideDividerAsSideHeaderAndFooterDivider) && sideDividerConfig == null) {
                throw IllegalArgumentException("Must call the sideDivider() method to configure the sideDivider")
            }
            // todo Ensure that the size of sideDividerConfig is consistent with the size of sideHeaderAndFooterDividerConfig
            // todo side Does not support disable

            val finalDividerConfig = when {
                dividerConfig != null -> dividerConfig
                !disableDefaultDivider -> context.obtainStyledAttributes(
                    intArrayOf(android.R.attr.listDivider)
                ).let { array ->
                    array.getDrawable(0).apply {
                        array.recycle()
                    }
                }!!.let {
                    DividerConfig.Builder(Divider.drawable(it)).build()
                }
                else -> null
            }

            return StaggeredGridItemDividerProvider(
                dividerConfig = finalDividerConfig?.toItemDividerConfig(context),
                headerDividerConfig = (headerDividerConfig
                    ?: if (useDividerAsHeaderDivider) finalDividerConfig else null)
                    ?.toItemDividerConfig(context),
                footerDividerConfig = (footerDividerConfig
                    ?: if (useDividerAsFooterDivider) finalDividerConfig else null)
                    ?.toItemDividerConfig(context),
                sideDividerConfig = sideDividerConfig?.toItemDividerConfig(context),
                sideHeaderAndFooterDividerConfig = (sideHeaderAndFooterDividerConfig
                    ?: if (useSideDividerAsSideHeaderAndFooterDivider) sideDividerConfig else null)
                    ?.toItemDividerConfig(context),
            )
        }


        /**
         * Set the divider of the item. You can configure to disable the divider or
         * provide a personalized divider in some cases through the [configBlock] function
         */
        fun divider(
            divider: Divider,
            configBlock: (DividerConfig.Builder.() -> Unit)? = null
        ): Builder {
            this.dividerConfig = DividerConfig.Builder(divider).apply {
                configBlock?.invoke(this)
            }.build()
            return this
        }

        /**
         * Set the divider of the item
         */
        fun divider(config: DividerConfig): Builder {
            this.dividerConfig = config
            return this
        }


        /**
         * Set the header divider of the item. You can configure to disable the divider or
         * provide a personalized divider in some cases through the [configBlock] function.
         *
         * To use this method, you need to set [isFullSpanByPosition], otherwise an exception will be thrown
         * @see isFullSpanByPosition
         */
        fun headerDivider(
            divider: Divider,
            configBlock: (DividerConfig.Builder.() -> Unit)? = null
        ): Builder {
            this.headerDividerConfig = DividerConfig.Builder(divider).apply {
                configBlock?.invoke(this)
            }.build()
            return this
        }

        /**
         * Set the header divider of the item.
         *
         * To use this method, you need to set [isFullSpanByPosition], otherwise an exception will be thrown
         * @see isFullSpanByPosition
         */
        fun headerDivider(config: DividerConfig): Builder {
            this.headerDividerConfig = config
            return this
        }


        /**
         * Set the footer divider of the item. You can configure to disable the divider or
         * provide a personalized divider in some cases through the [configBlock] function.
         *
         * To use this method, you need to set [isFullSpanByPosition], otherwise an exception will be thrown
         * @see isFullSpanByPosition
         */
        fun footerDivider(
            divider: Divider,
            configBlock: (DividerConfig.Builder.() -> Unit)? = null
        ): Builder {
            this.footerDividerConfig = DividerConfig.Builder(divider).apply {
                configBlock?.invoke(this)
            }.build()
            return this
        }

        /**
         * Set the footer divider of the item.
         *
         * To use this method, you need to set [isFullSpanByPosition], otherwise an exception will be thrown
         * @see isFullSpanByPosition
         */
        fun footerDivider(config: DividerConfig): Builder {
            this.footerDividerConfig = config
            return this
        }


        /**
         * Set the header and footer divider of the item. You can configure to disable the divider or
         * provide a personalized divider in some cases through the [configBlock] function.
         *
         * To use this method, you need to set [isFullSpanByPosition], otherwise an exception will be thrown
         * @see isFullSpanByPosition
         */
        fun headerAndFooterDivider(
            divider: Divider,
            configBlock: (DividerConfig.Builder.() -> Unit)? = null
        ): Builder {
            this.headerDividerConfig = DividerConfig.Builder(divider).apply {
                configBlock?.invoke(this)
            }.build()
            this.footerDividerConfig = DividerConfig.Builder(divider).apply {
                configBlock?.invoke(this)
            }.build()
            return this
        }

        /**
         * Set the header and footer divider of the item.
         *
         * To use this method, you need to set [isFullSpanByPosition], otherwise an exception will be thrown
         * @see isFullSpanByPosition
         */
        fun headerAndFooterDivider(config: DividerConfig): Builder {
            this.headerDividerConfig = config
            this.footerDividerConfig = config
            return this
        }


        /**
         * Use divider as the header divider.
         *
         * To use this method, you need to set [isFullSpanByPosition], otherwise an exception will be thrown
         * @see isFullSpanByPosition
         */
        fun useDividerAsHeaderDivider(use: Boolean = true): Builder {
            this.useDividerAsHeaderDivider = use
            return this
        }

        /**
         * Use divider as the footer divider.
         *
         * To use this method, you need to set [isFullSpanByPosition], otherwise an exception will be thrown
         * @see isFullSpanByPosition
         */
        fun useDividerAsFooterDivider(use: Boolean = true): Builder {
            this.useDividerAsFooterDivider = use
            return this
        }

        /**
         * Use divider as the header and footer divider.
         *
         * To use this method, you need to set [isFullSpanByPosition], otherwise an exception will be thrown
         * @see isFullSpanByPosition
         */
        fun useDividerAsHeaderAndFooterDivider(use: Boolean = true): Builder {
            this.useDividerAsHeaderDivider = use
            this.useDividerAsFooterDivider = use
            return this
        }


        /**
         * Set the divider on the side of the item. You can configure to disable the divider or
         * provide a personalized divider in some cases through the [configBlock] function
         */
        fun sideDivider(
            divider: Divider,
            configBlock: (DividerConfig.Builder.() -> Unit)? = null
        ): Builder {
            this.sideDividerConfig = DividerConfig.Builder(divider).apply {
                configBlock?.invoke(this)
            }.build()
            return this
        }

        /**
         * Set the divider on the side of the item
         */
        fun sideDivider(config: DividerConfig): Builder {
            this.sideDividerConfig = config
            return this
        }


        /**
         * Set the header divider on the side of the item. You can configure to disable the divider or
         * provide a personalized divider in some cases through the [configBlock] function
         */
        @Deprecated(
            "Please use sideHeaderAndFooterDivider instead",
            ReplaceWith("sideHeaderAndFooterDivider")
        )
        fun sideHeaderDivider(
            divider: Divider,
            configBlock: (DividerConfig.Builder.() -> Unit)? = null
        ): Builder {
            return sideHeaderAndFooterDivider(divider, configBlock)
        }

        /**
         * Set the header divider on the side of the item
         */
        @Deprecated(
            "Please use sideHeaderAndFooterDivider instead",
            ReplaceWith("sideHeaderAndFooterDivider")
        )
        fun sideHeaderDivider(config: DividerConfig): Builder {
            return sideHeaderAndFooterDivider(config)
        }


        /**
         * Set the footer divider on the side of the item. You can configure to disable the divider or
         * provide a personalized divider in some cases through the [configBlock] function
         */
        @Deprecated(
            "Please use sideHeaderAndFooterDivider instead",
            ReplaceWith("sideHeaderAndFooterDivider")
        )
        fun sideFooterDivider(
            divider: Divider,
            configBlock: (DividerConfig.Builder.() -> Unit)? = null
        ): Builder {
            return sideHeaderAndFooterDivider(divider, configBlock)
        }

        /**
         * Set the footer divider on the side of the item
         */
        @Deprecated(
            "Please use sideHeaderAndFooterDivider instead",
            ReplaceWith("sideHeaderAndFooterDivider")
        )
        fun sideFooterDivider(config: DividerConfig): Builder {
            return sideHeaderAndFooterDivider(config)
        }


        /**
         * Set the header and footer divider on the side of the item. You can configure to disable the divider or
         * provide a personalized divider in some cases through the [configBlock] function
         */
        fun sideHeaderAndFooterDivider(
            divider: Divider,
            configBlock: (DividerConfig.Builder.() -> Unit)? = null
        ): Builder {
            this.sideHeaderAndFooterDividerConfig = DividerConfig.Builder(divider).apply {
                configBlock?.invoke(this)
            }.build()
            return this
        }

        /**
         * Set the header and footer divider on the side of the item
         */
        fun sideHeaderAndFooterDivider(config: DividerConfig): Builder {
            this.sideHeaderAndFooterDividerConfig = config
            return this
        }


        /**
         * Use side divider as the header side divider
         */
        @Deprecated(
            "Please use useSideDividerAsSideHeaderAndFooterDivider instead",
            ReplaceWith("useSideDividerAsSideHeaderAndFooterDivider")
        )
        fun useSideDividerAsSideHeaderDivider(use: Boolean = true): Builder {
            return useSideDividerAsSideHeaderAndFooterDivider(use)
        }

        /**
         * Use side divider as the footer side divider
         */
        @Deprecated(
            "Please use useSideDividerAsSideHeaderAndFooterDivider instead",
            ReplaceWith("useSideDividerAsSideHeaderAndFooterDivider")
        )
        fun useSideDividerAsSideFooterDivider(use: Boolean = true): Builder {
            return useSideDividerAsSideHeaderAndFooterDivider(use)
        }

        /**
         * Use side divider as the header and footer side divider
         */
        fun useSideDividerAsSideHeaderAndFooterDivider(use: Boolean = true): Builder {
            this.useSideDividerAsSideHeaderAndFooterDivider = use
            return this
        }


        /**
         * Prohibit using the system default divider when no divider is specified
         */
        fun disableDefaultDivider(disableDefaultDivider: Boolean = true): Builder {
            this.disableDefaultDivider = disableDefaultDivider
            return this
        }

        /**
         * Set the interface for determining FullSpan based on position.
         * If you use [headerDivider], [footerDivider], [headerAndFooterDivider], [useDividerAsHeaderDivider],
         * [useDividerAsFooterDivider], [useDividerAsHeaderAndFooterDivider] then you must set this interface,
         * otherwise an exception will be thrown
         *
         * @see headerDivider
         * @see footerDivider
         * @see headerAndFooterDivider
         * @see useDividerAsHeaderDivider
         * @see useDividerAsFooterDivider
         * @see useDividerAsHeaderAndFooterDivider
         */
        fun isFullSpanByPosition(isFullSpanByPosition: IsFullSpanByPosition?): Builder {
            this.isFullSpanByPosition = isFullSpanByPosition
            return this
        }
    }
}