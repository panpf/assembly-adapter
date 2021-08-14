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
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.github.panpf.assemblyadapter.recycler.divider.internal.ItemDivider
import com.github.panpf.assemblyadapter.recycler.divider.internal.StaggeredGridItemDividerProvider
import kotlin.math.ceil
import kotlin.math.floor

/**
 * [StaggeredGridLayoutManager] dedicated divider ItemDecoration. Support divider、first or last divider、side divider、fist or last side divider
 */
open class StaggeredGridDividerItemDecoration(
    private val itemDividerProvider: StaggeredGridItemDividerProvider,
    private val isFullSpanByPosition: IsFullSpanByPosition?,
) : ItemDecoration() {

    init {
        if (itemDividerProvider.hasFirstOrLastDivider() && isFullSpanByPosition == null) {
            throw IllegalArgumentException(
                "Must be set the 'isFullSpanByPosition' property, because you configured 'firstDivider' or 'lastDivider'"
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
        val vertical = layoutManager.orientation == StaggeredGridLayoutManager.VERTICAL
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
            isLastSpan, isColumnFirst, isColumnEnd, vertical, ItemDivider.Type.START
        )
        val topItemDivider = itemDividerProvider.getItemDivider(
            view, parent, itemCount, position, spanCount, spanIndex, isFullSpan, isFirstSpan,
            isLastSpan, isColumnFirst, isColumnEnd, vertical, ItemDivider.Type.TOP
        )
        val endItemDivider = itemDividerProvider.getItemDivider(
            view, parent, itemCount, position, spanCount, spanIndex, isFullSpan, isFirstSpan,
            isLastSpan, isColumnFirst, isColumnEnd, vertical, ItemDivider.Type.END
        )
        val bottomItemDivider = itemDividerProvider.getItemDivider(
            view, parent, itemCount, position, spanCount, spanIndex, isFullSpan, isFirstSpan,
            isLastSpan, isColumnFirst, isColumnEnd, vertical, ItemDivider.Type.BOTTOM
        )
        val startItemDividerSize = startItemDivider?.widthSize ?: 0
        val topItemDividerSize = topItemDivider?.heightSize ?: 0
        val endItemDividerSize = endItemDivider?.widthSize ?: 0
        val bottomItemDividerSize = bottomItemDivider?.heightSize ?: 0

        if (vertical) {
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
        val vertical = layoutManager.orientation == GridLayoutManager.VERTICAL
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
                isLastSpan, isColumnFirst, isColumnEnd, vertical, ItemDivider.Type.START
            )
            val topItemDivider = itemDividerProvider.getItemDivider(
                view, parent, itemCount, position, spanCount, spanIndex, isFullSpan, isFirstSpan,
                isLastSpan, isColumnFirst, isColumnEnd, vertical, ItemDivider.Type.TOP
            )
            val endItemDivider = itemDividerProvider.getItemDivider(
                view, parent, itemCount, position, spanCount, spanIndex, isFullSpan, isFirstSpan,
                isLastSpan, isColumnFirst, isColumnEnd, vertical, ItemDivider.Type.END
            )
            val bottomItemDivider = itemDividerProvider.getItemDivider(
                view, parent, itemCount, position, spanCount, spanIndex, isFullSpan, isFirstSpan,
                isLastSpan, isColumnFirst, isColumnEnd, vertical, ItemDivider.Type.BOTTOM
            )
            val startItemDividerSize = startItemDivider?.widthSize ?: 0
            val topItemDividerSize = topItemDivider?.heightSize ?: 0
            val endItemDividerSize = endItemDivider?.widthSize ?: 0
            val bottomItemDividerSize = bottomItemDivider?.heightSize ?: 0

            if (vertical) {
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
        private var firstDividerConfig: DividerConfig? = null
        private var lastDividerConfig: DividerConfig? = null
        private var showFirstDivider = false
        private var showLastDivider = false

        private var sideDividerConfig: DividerConfig? = null
        private var firstSideDividerConfig: DividerConfig? = null
        private var lastSideDividerConfig: DividerConfig? = null
        private var showFirstSideDivider = false
        private var showLastSideDivider = false

        private var isFullSpanByPosition: IsFullSpanByPosition? = null

        fun build(): StaggeredGridDividerItemDecoration {
            return StaggeredGridDividerItemDecoration(
                buildItemDividerProvider(),
                isFullSpanByPosition
            )
        }

        private fun buildItemDividerProvider(): StaggeredGridItemDividerProvider {
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
                firstDividerConfig = (firstDividerConfig
                    ?: if (showFirstDivider) finalDividerConfig else null)
                    ?.toItemDividerConfig(context),
                lastDividerConfig = (lastDividerConfig
                    ?: if (showLastDivider) finalDividerConfig else null)
                    ?.toItemDividerConfig(context),
                sideDividerConfig = sideDividerConfig?.toItemDividerConfig(context),
                firstSideDividerConfig = (firstSideDividerConfig
                    ?: if (showFirstSideDivider) sideDividerConfig else null)
                    ?.toItemDividerConfig(context),
                lastSideDividerConfig = (lastSideDividerConfig
                    ?: if (showLastSideDivider) sideDividerConfig else null)
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
         * Set the first divider of the item. You can configure to disable the divider or
         * provide a personalized divider in some cases through the [configBlock] function.
         *
         * To use this method, you need to set [isFullSpanByPosition], otherwise an exception will be thrown
         * @see isFullSpanByPosition
         */
        fun firstDivider(
            divider: Divider,
            configBlock: (DividerConfig.Builder.() -> Unit)? = null
        ): Builder {
            this.firstDividerConfig = DividerConfig.Builder(divider).apply {
                configBlock?.invoke(this)
            }.build()
            return this
        }

        /**
         * Set the first divider of the item.
         *
         * To use this method, you need to set [isFullSpanByPosition], otherwise an exception will be thrown
         * @see isFullSpanByPosition
         */
        fun firstDivider(config: DividerConfig): Builder {
            this.firstDividerConfig = config
            return this
        }


        /**
         * Set the last divider of the item. You can configure to disable the divider or
         * provide a personalized divider in some cases through the [configBlock] function.
         *
         * To use this method, you need to set [isFullSpanByPosition], otherwise an exception will be thrown
         * @see isFullSpanByPosition
         */
        fun lastDivider(
            divider: Divider,
            configBlock: (DividerConfig.Builder.() -> Unit)? = null
        ): Builder {
            this.lastDividerConfig = DividerConfig.Builder(divider).apply {
                configBlock?.invoke(this)
            }.build()
            return this
        }

        /**
         * Set the last divider of the item.
         *
         * To use this method, you need to set [isFullSpanByPosition], otherwise an exception will be thrown
         * @see isFullSpanByPosition
         */
        fun lastDivider(config: DividerConfig): Builder {
            this.lastDividerConfig = config
            return this
        }


        /**
         * Set the first and last divider of the item. You can configure to disable the divider or
         * provide a personalized divider in some cases through the [configBlock] function.
         *
         * To use this method, you need to set [isFullSpanByPosition], otherwise an exception will be thrown
         * @see isFullSpanByPosition
         */
        fun firstAndLastDivider(
            divider: Divider,
            configBlock: (DividerConfig.Builder.() -> Unit)? = null
        ): Builder {
            this.firstDividerConfig = DividerConfig.Builder(divider).apply {
                configBlock?.invoke(this)
            }.build()
            this.lastDividerConfig = DividerConfig.Builder(divider).apply {
                configBlock?.invoke(this)
            }.build()
            return this
        }

        /**
         * Set the first and last divider of the item.
         *
         * To use this method, you need to set [isFullSpanByPosition], otherwise an exception will be thrown
         * @see isFullSpanByPosition
         */
        fun firstAndLastDivider(config: DividerConfig): Builder {
            this.firstDividerConfig = config
            this.lastDividerConfig = config
            return this
        }


        /**
         * Use divider as the first divider.
         *
         * To use this method, you need to set [isFullSpanByPosition], otherwise an exception will be thrown
         * @see isFullSpanByPosition
         */
        fun showFirstDivider(show: Boolean = true): Builder {
            this.showFirstDivider = show
            return this
        }

        /**
         * Use divider as the last divider.
         *
         * To use this method, you need to set [isFullSpanByPosition], otherwise an exception will be thrown
         * @see isFullSpanByPosition
         */
        fun showLastDivider(show: Boolean = true): Builder {
            this.showLastDivider = show
            return this
        }

        /**
         * Use divider as the first and last divider.
         *
         * To use this method, you need to set [isFullSpanByPosition], otherwise an exception will be thrown
         * @see isFullSpanByPosition
         */
        fun showFirstAndLastDivider(show: Boolean = true): Builder {
            this.showFirstDivider = show
            this.showLastDivider = show
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
         * Set the first divider on the side of the item. You can configure to disable the divider or
         * provide a personalized divider in some cases through the [configBlock] function
         */
        fun firstSideDivider(
            divider: Divider,
            configBlock: (DividerConfig.Builder.() -> Unit)? = null
        ): Builder {
            this.firstSideDividerConfig = DividerConfig.Builder(divider).apply {
                configBlock?.invoke(this)
            }.build()
            return this
        }

        /**
         * Set the first divider on the side of the item
         */
        fun firstSideDivider(config: DividerConfig): Builder {
            this.firstSideDividerConfig = config
            return this
        }


        /**
         * Set the last divider on the side of the item. You can configure to disable the divider or
         * provide a personalized divider in some cases through the [configBlock] function
         */
        fun lastSideDivider(
            divider: Divider,
            configBlock: (DividerConfig.Builder.() -> Unit)? = null
        ): Builder {
            this.lastSideDividerConfig = DividerConfig.Builder(divider).apply {
                configBlock?.invoke(this)
            }.build()
            return this
        }

        /**
         * Set the last divider on the side of the item
         */
        fun lastSideDivider(config: DividerConfig): Builder {
            this.lastSideDividerConfig = config
            return this
        }


        /**
         * Set the first and last divider on the side of the item. You can configure to disable the divider or
         * provide a personalized divider in some cases through the [configBlock] function
         */
        fun firstAndLastSideDivider(
            divider: Divider,
            configBlock: (DividerConfig.Builder.() -> Unit)? = null
        ): Builder {
            this.firstSideDividerConfig = DividerConfig.Builder(divider).apply {
                configBlock?.invoke(this)
            }.build()
            this.lastSideDividerConfig = DividerConfig.Builder(divider).apply {
                configBlock?.invoke(this)
            }.build()
            return this
        }

        /**
         * Set the first and last divider on the side of the item
         */
        fun firstAndLastSideDivider(config: DividerConfig): Builder {
            this.firstSideDividerConfig = config
            this.lastSideDividerConfig = config
            return this
        }


        /**
         * Use side divider as the first side divider
         */
        fun showFirstSideDivider(show: Boolean = true): Builder {
            this.showFirstSideDivider = show
            return this
        }

        /**
         * Use side divider as the last side divider
         */
        fun showLastSideDivider(show: Boolean = true): Builder {
            this.showLastSideDivider = show
            return this
        }

        /**
         * Use side divider as the first and last side  divider
         */
        fun showFirstAndLastSideDivider(show: Boolean = true): Builder {
            this.showFirstSideDivider = show
            this.showLastSideDivider = show
            return this
        }


        /**
         * Set the interface for determining FullSpan based on position.
         * If you use [firstDivider], [lastDivider], [firstAndLastDivider], [showFirstDivider],
         * [showLastDivider], [showFirstAndLastDivider] then you must set this interface,
         * otherwise an exception will be thrown
         *
         * @see firstDivider
         * @see lastDivider
         * @see firstAndLastDivider
         * @see showFirstDivider
         * @see showLastDivider
         * @see showFirstAndLastDivider
         */
        fun isFullSpanByPosition(isFullSpanByPosition: IsFullSpanByPosition?): Builder {
            this.isFullSpanByPosition = isFullSpanByPosition
            return this
        }
    }
}