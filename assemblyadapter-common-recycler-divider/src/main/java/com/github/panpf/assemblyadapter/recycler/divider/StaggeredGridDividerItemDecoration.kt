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
import com.github.panpf.assemblyadapter.recycler.divider.internal.ItemDivider
import com.github.panpf.assemblyadapter.recycler.divider.internal.StaggeredGridItemDividerProvider
import kotlin.math.ceil
import kotlin.math.floor

/**
 * [StaggeredGridLayoutManager] dedicated divider ItemDecoration. Support divider、header and footer divider、side divider、header and footer side divider
 */
open class StaggeredGridDividerItemDecoration(
    private val itemDividerProvider: StaggeredGridItemDividerProvider,
    private val isFullSpanByPosition: IsFullSpanByPosition?,
) : ItemDecoration() {

    init {
        if (itemDividerProvider.hasFirstOrFooterDivider() && isFullSpanByPosition == null) {
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
        val isFirstSpan = isFullSpan || spanIndex == 0
        val isLastSpan = isFullSpan || spanIndex == spanCount - 1
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
        val isColumnEnd = if (isFullSpanByPosition != null && position >= itemCount - spanCount) {
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

        val startItemDivider = itemDividerProvider.getItemDivider(
            view, parent, itemCount, position, spanCount, spanIndex, isFullSpan, isFirstSpan,
            isLastSpan, isColumnFirst, isColumnEnd, isVerticalOrientation,
            if (isLTRDirection) ItemDivider.Type.START else ItemDivider.Type.END
        )
        val topItemDivider = itemDividerProvider.getItemDivider(
            view, parent, itemCount, position, spanCount, spanIndex, isFullSpan, isFirstSpan,
            isLastSpan, isColumnFirst, isColumnEnd, isVerticalOrientation, ItemDivider.Type.TOP
        )
        val endItemDivider = itemDividerProvider.getItemDivider(
            view, parent, itemCount, position, spanCount, spanIndex, isFullSpan, isFirstSpan,
            isLastSpan, isColumnFirst, isColumnEnd, isVerticalOrientation,
            if (isLTRDirection) ItemDivider.Type.END else ItemDivider.Type.START
        )
        val bottomItemDivider = itemDividerProvider.getItemDivider(
            view, parent, itemCount, position, spanCount, spanIndex, isFullSpan, isFirstSpan,
            isLastSpan, isColumnFirst, isColumnEnd, isVerticalOrientation, ItemDivider.Type.BOTTOM
        )
        val startItemDividerSize = startItemDivider?.widthSize ?: 0
        val topItemDividerSize = topItemDivider?.heightSize ?: 0
        val endItemDividerSize = endItemDivider?.widthSize ?: 0
        val bottomItemDividerSize = bottomItemDivider?.heightSize ?: 0

        if (isVerticalOrientation) {
            when {
                isFullSpan -> {
                    outRect.set(
                        startItemDividerSize,
                        topItemDividerSize,
                        endItemDividerSize,
                        bottomItemDividerSize
                    )
                }
                isFirstSpan -> {
                    outRect.set(
                        startItemDividerSize,
                        topItemDividerSize,
                        floor(endItemDividerSize / 2f).toInt(),
                        bottomItemDividerSize
                    )
                }
                isLastSpan -> {
                    outRect.set(
                        ceil(startItemDividerSize / 2f).toInt(),
                        topItemDividerSize,
                        endItemDividerSize,
                        bottomItemDividerSize
                    )
                }
                else -> {
                    outRect.set(
                        ceil(startItemDividerSize / 2f).toInt(),
                        topItemDividerSize,
                        floor(endItemDividerSize / 2f).toInt(),
                        bottomItemDividerSize
                    )
                }
            }
        } else {
            when {
                isFullSpan -> {
                    outRect.set(
                        startItemDividerSize,
                        topItemDividerSize,
                        endItemDividerSize,
                        bottomItemDividerSize
                    )
                }
                isFirstSpan -> {
                    outRect.set(
                        startItemDividerSize,
                        topItemDividerSize,
                        endItemDividerSize,
                        floor(bottomItemDividerSize / 2f).toInt()
                    )
                }
                isLastSpan -> {
                    outRect.set(
                        startItemDividerSize,
                        ceil(topItemDividerSize / 2f).toInt(),
                        endItemDividerSize,
                        bottomItemDividerSize
                    )
                }
                else -> {
                    outRect.set(
                        startItemDividerSize,
                        ceil(topItemDividerSize / 2f).toInt(),
                        endItemDividerSize,
                        floor(bottomItemDividerSize / 2f).toInt()
                    )
                }
            }
        }
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
            val isFirstSpan = isFullSpan || spanIndex == 0
            val isLastSpan = isFullSpan || spanIndex == spanCount - 1
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

            val startItemDivider = itemDividerProvider.getItemDivider(
                view, parent, itemCount, position, spanCount, spanIndex, isFullSpan, isFirstSpan,
                isLastSpan, isColumnFirst, isColumnEnd, isVerticalOrientation,
                if (isLTRDirection) ItemDivider.Type.START else ItemDivider.Type.END
            )
            val topItemDivider = itemDividerProvider.getItemDivider(
                view, parent, itemCount, position, spanCount, spanIndex, isFullSpan, isFirstSpan,
                isLastSpan, isColumnFirst, isColumnEnd, isVerticalOrientation, ItemDivider.Type.TOP
            )
            val endItemDivider = itemDividerProvider.getItemDivider(
                view, parent, itemCount, position, spanCount, spanIndex, isFullSpan, isFirstSpan,
                isLastSpan, isColumnFirst, isColumnEnd, isVerticalOrientation,
                if (isLTRDirection) ItemDivider.Type.END else ItemDivider.Type.START
            )
            val bottomItemDivider = itemDividerProvider.getItemDivider(
                view,
                parent,
                itemCount,
                position,
                spanCount,
                spanIndex,
                isFullSpan,
                isFirstSpan,
                isLastSpan,
                isColumnFirst,
                isColumnEnd,
                isVerticalOrientation,
                ItemDivider.Type.BOTTOM
            )
            val startItemDividerSize = startItemDivider?.widthSize ?: 0
            val topItemDividerSize = topItemDivider?.heightSize ?: 0
            val endItemDividerSize = endItemDivider?.widthSize ?: 0
            val bottomItemDividerSize = bottomItemDivider?.heightSize ?: 0

            if (isVerticalOrientation) {
                startItemDivider?.apply {
                    draw(
                        canvas,
                        view.left - insetEnd - drawableWidthSize,
                        view.top - topItemDividerSize + insetTop,
                        view.left - insetEnd,
                        view.bottom + bottomItemDividerSize - insetBottom
                    )
                }
                endItemDivider?.apply {
                    draw(
                        canvas,
                        view.right + insetStart,
                        view.top - topItemDividerSize + insetTop,
                        view.right + insetStart + drawableWidthSize,
                        view.bottom + bottomItemDividerSize - insetBottom
                    )
                }
                topItemDivider?.apply {
                    draw(
                        canvas,
                        view.left + insetStart,
                        view.top - insetBottom - drawableHeightSize,
                        view.right - insetEnd,
                        view.top - insetBottom
                    )
                }
                bottomItemDivider?.apply {
                    draw(
                        canvas,
                        view.left + insetStart,
                        view.bottom + insetTop,
                        view.right - insetEnd,
                        view.bottom + insetTop + drawableHeightSize
                    )
                }
            } else {
                startItemDivider?.apply {
                    draw(
                        canvas,
                        view.left - insetEnd - drawableWidthSize,
                        view.top + insetTop,
                        view.left - insetEnd,
                        view.bottom - insetBottom
                    )
                }
                endItemDivider?.apply {
                    draw(
                        canvas,
                        view.right + insetStart,
                        view.top + insetTop,
                        view.right + insetStart + drawableWidthSize,
                        view.bottom - insetBottom
                    )
                }
                topItemDivider?.apply {
                    draw(
                        canvas,
                        view.left - startItemDividerSize + insetStart,
                        view.top - insetBottom - drawableHeightSize,
                        view.right + endItemDividerSize - insetEnd,
                        view.top - insetBottom
                    )
                }
                bottomItemDivider?.apply {
                    draw(
                        canvas,
                        view.left - startItemDividerSize + insetStart,
                        view.bottom + insetTop,
                        view.right + endItemDividerSize - insetEnd,
                        view.bottom + insetTop + drawableHeightSize
                    )
                }
            }
        }
    }

    class Builder(val context: Context) {

        private var dividerConfig: DividerConfig? = null
        private var headerDividerConfig: DividerConfig? = null
        private var footerDividerConfig: DividerConfig? = null
        private var useDividerAsHeaderDivider = false
        private var useDividerAsFooterDivider = false

        private var sideDividerConfig: DividerConfig? = null
        private var headerSideDividerConfig: DividerConfig? = null
        private var footerSideDividerConfig: DividerConfig? = null
        private var useSideDividerAsHeaderSideDivider = false
        private var useSideDividerAsFooterSideDivider = false

        private var isFullSpanByPosition: IsFullSpanByPosition? = null

        fun build(): StaggeredGridDividerItemDecoration {
            return StaggeredGridDividerItemDecoration(
                buildItemDividerProvider(),
                isFullSpanByPosition
            )
        }

        private fun buildItemDividerProvider(): StaggeredGridItemDividerProvider {
            if ((useSideDividerAsHeaderSideDivider || useSideDividerAsFooterSideDivider) && sideDividerConfig == null) {
                throw IllegalArgumentException("Must call the sideDivider() method to configure the sideDivider")
            }

            val finalDividerConfig =
                dividerConfig ?: context.obtainStyledAttributes(
                    intArrayOf(android.R.attr.listDivider)
                ).let { array ->
                    array.getDrawable(0).apply {
                        array.recycle()
                    }
                }!!.let {
                    DividerConfig.Builder(Divider.drawable(it)).build()
                }

            return StaggeredGridItemDividerProvider(
                dividerConfig = finalDividerConfig.toItemDividerConfig(context),
                headerDividerConfig = (headerDividerConfig
                    ?: if (useDividerAsHeaderDivider) finalDividerConfig else null)
                    ?.toItemDividerConfig(context),
                footerDividerConfig = (footerDividerConfig
                    ?: if (useDividerAsFooterDivider) finalDividerConfig else null)
                    ?.toItemDividerConfig(context),
                sideDividerConfig = sideDividerConfig?.toItemDividerConfig(context),
                headerSideDividerConfig = (headerSideDividerConfig
                    ?: if (useSideDividerAsHeaderSideDivider) sideDividerConfig else null)
                    ?.toItemDividerConfig(context),
                footerSideDividerConfig = (footerSideDividerConfig
                    ?: if (useSideDividerAsFooterSideDivider) sideDividerConfig else null)
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
        fun headerSideDivider(
            divider: Divider,
            configBlock: (DividerConfig.Builder.() -> Unit)? = null
        ): Builder {
            this.headerSideDividerConfig = DividerConfig.Builder(divider).apply {
                configBlock?.invoke(this)
            }.build()
            return this
        }

        /**
         * Set the header divider on the side of the item
         */
        fun headerSideDivider(config: DividerConfig): Builder {
            this.headerSideDividerConfig = config
            return this
        }


        /**
         * Set the footer divider on the side of the item. You can configure to disable the divider or
         * provide a personalized divider in some cases through the [configBlock] function
         */
        fun footerSideDivider(
            divider: Divider,
            configBlock: (DividerConfig.Builder.() -> Unit)? = null
        ): Builder {
            this.footerSideDividerConfig = DividerConfig.Builder(divider).apply {
                configBlock?.invoke(this)
            }.build()
            return this
        }

        /**
         * Set the footer divider on the side of the item
         */
        fun footerSideDivider(config: DividerConfig): Builder {
            this.footerSideDividerConfig = config
            return this
        }


        /**
         * Set the header and footer divider on the side of the item. You can configure to disable the divider or
         * provide a personalized divider in some cases through the [configBlock] function
         */
        fun headerAndFooterSideDivider(
            divider: Divider,
            configBlock: (DividerConfig.Builder.() -> Unit)? = null
        ): Builder {
            this.headerSideDividerConfig = DividerConfig.Builder(divider).apply {
                configBlock?.invoke(this)
            }.build()
            this.footerSideDividerConfig = DividerConfig.Builder(divider).apply {
                configBlock?.invoke(this)
            }.build()
            return this
        }

        /**
         * Set the header and footer divider on the side of the item
         */
        fun headerAndFooterSideDivider(config: DividerConfig): Builder {
            this.headerSideDividerConfig = config
            this.footerSideDividerConfig = config
            return this
        }


        /**
         * Use side divider as the header side divider
         */
        fun useSideDividerAsHeaderSideDivider(use: Boolean = true): Builder {
            this.useSideDividerAsHeaderSideDivider = use
            return this
        }

        /**
         * Use side divider as the footer side divider
         */
        fun useSideDividerAsFooterSideDivider(use: Boolean = true): Builder {
            this.useSideDividerAsFooterSideDivider = use
            return this
        }

        /**
         * Use side divider as the header and footer side divider
         */
        fun useSideDividerAsHeaderAndFooterSideDivider(use: Boolean = true): Builder {
            this.useSideDividerAsHeaderSideDivider = use
            this.useSideDividerAsFooterSideDivider = use
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