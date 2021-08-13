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
import com.github.panpf.assemblyadapter.recycler.divider.internal.ItemDecorate
import com.github.panpf.assemblyadapter.recycler.divider.internal.StaggeredGridItemDecorateProvider
import kotlin.math.ceil
import kotlin.math.floor

open class StaggeredGridDividerItemDecoration(
    private val itemDecorateProvider: StaggeredGridItemDecorateProvider,
    private val isFullSpanByPosition: IsFullSpanByPosition?,
) : ItemDecoration() {

    init {
        if (itemDecorateProvider.hasFirstOrLastDivider() && isFullSpanByPosition == null) {
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

        val startItemDecorate = itemDecorateProvider.getItemDecorate(
            view, parent, itemCount, position, spanCount, spanIndex, isFullSpan, isFirstSpan,
            isLastSpan, isColumnFirst, isColumnEnd, vertical, ItemDecorate.Type.START
        )
        val topItemDecorate = itemDecorateProvider.getItemDecorate(
            view, parent, itemCount, position, spanCount, spanIndex, isFullSpan, isFirstSpan,
            isLastSpan, isColumnFirst, isColumnEnd, vertical, ItemDecorate.Type.TOP
        )
        val endItemDecorate = itemDecorateProvider.getItemDecorate(
            view, parent, itemCount, position, spanCount, spanIndex, isFullSpan, isFirstSpan,
            isLastSpan, isColumnFirst, isColumnEnd, vertical, ItemDecorate.Type.END
        )
        val bottomItemDecorate = itemDecorateProvider.getItemDecorate(
            view, parent, itemCount, position, spanCount, spanIndex, isFullSpan, isFirstSpan,
            isLastSpan, isColumnFirst, isColumnEnd, vertical, ItemDecorate.Type.BOTTOM
        )
        val startItemDecorateSize = startItemDecorate?.widthSize ?: 0
        val topItemDecorateSize = topItemDecorate?.heightSize ?: 0
        val endItemDecorateSize = endItemDecorate?.widthSize ?: 0
        val bottomItemDecorateSize = bottomItemDecorate?.heightSize ?: 0

        if (vertical) {
            when {
                isFullSpan -> {
                    outRect.set(
                        startItemDecorateSize,
                        topItemDecorateSize,
                        endItemDecorateSize,
                        bottomItemDecorateSize
                    )
                }
                isFirstSpan -> {
                    outRect.set(
                        startItemDecorateSize,
                        topItemDecorateSize,
                        floor(endItemDecorateSize / 2f).toInt(),
                        bottomItemDecorateSize
                    )
                }
                isLastSpan -> {
                    outRect.set(
                        ceil(startItemDecorateSize / 2f).toInt(),
                        topItemDecorateSize,
                        endItemDecorateSize,
                        bottomItemDecorateSize
                    )
                }
                else -> {
                    outRect.set(
                        ceil(startItemDecorateSize / 2f).toInt(),
                        topItemDecorateSize,
                        floor(endItemDecorateSize / 2f).toInt(),
                        bottomItemDecorateSize
                    )
                }
            }
        } else {
            when {
                isFullSpan -> {
                    outRect.set(
                        startItemDecorateSize,
                        topItemDecorateSize,
                        endItemDecorateSize,
                        bottomItemDecorateSize
                    )
                }
                isFirstSpan -> {
                    outRect.set(
                        startItemDecorateSize,
                        topItemDecorateSize,
                        endItemDecorateSize,
                        floor(bottomItemDecorateSize / 2f).toInt()
                    )
                }
                isLastSpan -> {
                    outRect.set(
                        startItemDecorateSize,
                        ceil(topItemDecorateSize / 2f).toInt(),
                        endItemDecorateSize,
                        bottomItemDecorateSize
                    )
                }
                else -> {
                    outRect.set(
                        startItemDecorateSize,
                        ceil(topItemDecorateSize / 2f).toInt(),
                        endItemDecorateSize,
                        floor(bottomItemDecorateSize / 2f).toInt()
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

            val startItemDecorate = itemDecorateProvider.getItemDecorate(
                view, parent, itemCount, position, spanCount, spanIndex, isFullSpan, isFirstSpan,
                isLastSpan, isColumnFirst, isColumnEnd, vertical, ItemDecorate.Type.START
            )
            val topItemDecorate = itemDecorateProvider.getItemDecorate(
                view, parent, itemCount, position, spanCount, spanIndex, isFullSpan, isFirstSpan,
                isLastSpan, isColumnFirst, isColumnEnd, vertical, ItemDecorate.Type.TOP
            )
            val endItemDecorate = itemDecorateProvider.getItemDecorate(
                view, parent, itemCount, position, spanCount, spanIndex, isFullSpan, isFirstSpan,
                isLastSpan, isColumnFirst, isColumnEnd, vertical, ItemDecorate.Type.END
            )
            val bottomItemDecorate = itemDecorateProvider.getItemDecorate(
                view, parent, itemCount, position, spanCount, spanIndex, isFullSpan, isFirstSpan,
                isLastSpan, isColumnFirst, isColumnEnd, vertical, ItemDecorate.Type.BOTTOM
            )
            val startItemDecorateSize = startItemDecorate?.widthSize ?: 0
            val topItemDecorateSize = topItemDecorate?.heightSize ?: 0
            val endItemDecorateSize = endItemDecorate?.widthSize ?: 0
            val bottomItemDecorateSize = bottomItemDecorate?.heightSize ?: 0

            if (vertical) {
                startItemDecorate?.apply {
                    draw(
                        canvas,
                        view.left - insetEnd - drawableWidthSize,
                        view.top - topItemDecorateSize + insetTop,
                        view.left - insetEnd,
                        view.bottom + bottomItemDecorateSize - insetBottom
                    )
                }
                endItemDecorate?.apply {
                    draw(
                        canvas,
                        view.right + insetStart,
                        view.top - topItemDecorateSize + insetTop,
                        view.right + insetStart + drawableWidthSize,
                        view.bottom + bottomItemDecorateSize - insetBottom
                    )
                }
                topItemDecorate?.apply {
                    draw(
                        canvas,
                        view.left + insetStart,
                        view.top - insetBottom - drawableHeightSize,
                        view.right - insetEnd,
                        view.top - insetBottom
                    )
                }
                bottomItemDecorate?.apply {
                    draw(
                        canvas,
                        view.left + insetStart,
                        view.bottom + insetTop,
                        view.right - insetEnd,
                        view.bottom + insetTop + drawableHeightSize
                    )
                }
            } else {
                startItemDecorate?.apply {
                    draw(
                        canvas,
                        view.left - insetEnd - drawableWidthSize,
                        view.top + insetTop,
                        view.left - insetEnd,
                        view.bottom - insetBottom
                    )
                }
                endItemDecorate?.apply {
                    draw(
                        canvas,
                        view.right + insetStart,
                        view.top + insetTop,
                        view.right + insetStart + drawableWidthSize,
                        view.bottom - insetBottom
                    )
                }
                topItemDecorate?.apply {
                    draw(
                        canvas,
                        view.left - startItemDecorateSize + insetStart,
                        view.top - insetBottom - drawableHeightSize,
                        view.right + endItemDecorateSize - insetEnd,
                        view.top - insetBottom
                    )
                }
                bottomItemDecorate?.apply {
                    draw(
                        canvas,
                        view.left - startItemDecorateSize + insetStart,
                        view.bottom + insetTop,
                        view.right + endItemDecorateSize - insetEnd,
                        view.bottom + insetTop + drawableHeightSize
                    )
                }
            }
        }
    }

    class Builder(private val context: Context) {

        private var dividerDecorateConfig: DecorateConfig? = null
        private var firstDividerDecorateConfig: DecorateConfig? = null
        private var lastDividerDecorateConfig: DecorateConfig? = null
        private var showFirstDivider = false
        private var showLastDivider = false

        private var sideDecorateConfig: DecorateConfig? = null
        private var firstSideDecorateConfig: DecorateConfig? = null
        private var lastSideDecorateConfig: DecorateConfig? = null
        private var showFirstSide = false
        private var showLastSide = false

        private var isFullSpanByPosition: IsFullSpanByPosition? = null

        fun build(): StaggeredGridDividerItemDecoration {
            return StaggeredGridDividerItemDecoration(
                buildItemDecorateProvider(),
                isFullSpanByPosition
            )
        }

        private fun buildItemDecorateProvider(): StaggeredGridItemDecorateProvider {
            val finalDividerDecorateConfig =
                dividerDecorateConfig ?: context.obtainStyledAttributes(
                    intArrayOf(android.R.attr.listDivider)
                ).let { array ->
                    array.getDrawable(0).apply {
                        array.recycle()
                    }
                }!!.let {
                    DecorateConfig.Builder(Decorate.drawable(it)).build()
                }

            val finalDividerItemDecorateConfig =
                finalDividerDecorateConfig.toItemDecorateHolder(context)
            val firstDividerItemDecorate = (firstDividerDecorateConfig
                ?: if (showFirstDivider) finalDividerDecorateConfig else null)
                ?.toItemDecorateHolder(context)
            val lastDividerItemDecorate = (lastDividerDecorateConfig
                ?: if (showLastDivider) finalDividerDecorateConfig else null)
                ?.toItemDecorateHolder(context)

            val sideItemDecorate = sideDecorateConfig?.toItemDecorateHolder(context)
            val firstSideItemDecorate = (firstSideDecorateConfig
                ?: if (showFirstSide) sideDecorateConfig else null)
                ?.toItemDecorateHolder(context)
            val lastSideItemDecorate = (lastSideDecorateConfig
                ?: if (showLastSide) sideDecorateConfig else null)
                ?.toItemDecorateHolder(context)

            return StaggeredGridItemDecorateProvider(
                finalDividerItemDecorateConfig,
                firstDividerItemDecorate,
                lastDividerItemDecorate,
                sideItemDecorate,
                firstSideItemDecorate,
                lastSideItemDecorate,
            )
        }


        fun divider(decorate: Decorate): Builder {
            this.dividerDecorateConfig = DecorateConfig.Builder(decorate).build()
            return this
        }

        fun divider(
            decorate: Decorate,
            configBlock: (DecorateConfig.Builder.() -> Unit)? = null
        ): Builder {
            this.dividerDecorateConfig = DecorateConfig.Builder(decorate).apply {
                configBlock?.invoke(this)
            }.build()
            return this
        }

        fun divider(decorateConfig: DecorateConfig): Builder {
            this.dividerDecorateConfig = decorateConfig
            return this
        }


        fun firstDivider(decorate: Decorate): Builder {
            this.firstDividerDecorateConfig = DecorateConfig.Builder(decorate).build()
            return this
        }

        fun firstDivider(
            decorate: Decorate,
            configBlock: (DecorateConfig.Builder.() -> Unit)? = null
        ): Builder {
            this.firstDividerDecorateConfig = DecorateConfig.Builder(decorate).apply {
                configBlock?.invoke(this)
            }.build()
            return this
        }

        fun firstDivider(decorateConfig: DecorateConfig): Builder {
            this.firstDividerDecorateConfig = decorateConfig
            return this
        }


        fun lastDivider(decorate: Decorate): Builder {
            this.lastDividerDecorateConfig = DecorateConfig.Builder(decorate).build()
            return this
        }

        fun lastDivider(
            decorate: Decorate,
            configBlock: (DecorateConfig.Builder.() -> Unit)? = null
        ): Builder {
            this.lastDividerDecorateConfig = DecorateConfig.Builder(decorate).apply {
                configBlock?.invoke(this)
            }.build()
            return this
        }

        fun lastDivider(decorateConfig: DecorateConfig): Builder {
            this.lastDividerDecorateConfig = decorateConfig
            return this
        }


        fun firstAndLastDivider(decorate: Decorate): Builder {
            this.firstDividerDecorateConfig = DecorateConfig.Builder(decorate).build()
            this.lastDividerDecorateConfig = DecorateConfig.Builder(decorate).build()
            return this
        }

        fun firstAndLastDivider(
            decorate: Decorate,
            configBlock: (DecorateConfig.Builder.() -> Unit)? = null
        ): Builder {
            this.firstDividerDecorateConfig = DecorateConfig.Builder(decorate).apply {
                configBlock?.invoke(this)
            }.build()
            this.lastDividerDecorateConfig = DecorateConfig.Builder(decorate).apply {
                configBlock?.invoke(this)
            }.build()
            return this
        }

        fun firstAndLastDivider(decorateConfig: DecorateConfig): Builder {
            this.firstDividerDecorateConfig = decorateConfig
            this.lastDividerDecorateConfig = decorateConfig
            return this
        }


        fun showFirstDivider(showFirstDivider: Boolean = true): Builder {
            this.showFirstDivider = showFirstDivider
            return this
        }

        fun showLastDivider(showLastDivider: Boolean = true): Builder {
            this.showLastDivider = showLastDivider
            return this
        }

        fun showFirstAndLastDivider(showFirstAndLastDivider: Boolean = true): Builder {
            this.showFirstDivider = showFirstAndLastDivider
            this.showLastDivider = showFirstAndLastDivider
            return this
        }


        fun side(decorate: Decorate): Builder {
            this.sideDecorateConfig = DecorateConfig.Builder(decorate).build()
            return this
        }

        fun side(
            decorate: Decorate,
            configBlock: (DecorateConfig.Builder.() -> Unit)? = null
        ): Builder {
            this.sideDecorateConfig = DecorateConfig.Builder(decorate).apply {
                configBlock?.invoke(this)
            }.build()
            return this
        }

        fun side(decorateConfig: DecorateConfig): Builder {
            this.sideDecorateConfig = decorateConfig
            return this
        }


        fun firstSide(decorate: Decorate): Builder {
            this.firstSideDecorateConfig = DecorateConfig.Builder(decorate).build()
            return this
        }

        fun firstSide(
            decorate: Decorate,
            configBlock: (DecorateConfig.Builder.() -> Unit)? = null
        ): Builder {
            this.firstSideDecorateConfig = DecorateConfig.Builder(decorate).apply {
                configBlock?.invoke(this)
            }.build()
            return this
        }

        fun firstSide(decorateConfig: DecorateConfig): Builder {
            this.firstSideDecorateConfig = decorateConfig
            return this
        }


        fun lastSide(decorate: Decorate): Builder {
            this.lastSideDecorateConfig = DecorateConfig.Builder(decorate).build()
            return this
        }

        fun lastSide(
            decorate: Decorate,
            configBlock: (DecorateConfig.Builder.() -> Unit)? = null
        ): Builder {
            this.lastSideDecorateConfig = DecorateConfig.Builder(decorate).apply {
                configBlock?.invoke(this)
            }.build()
            return this
        }

        fun lastSide(decorateConfig: DecorateConfig): Builder {
            this.lastSideDecorateConfig = decorateConfig
            return this
        }


        fun firstAndLastSide(decorate: Decorate): Builder {
            this.firstSideDecorateConfig = DecorateConfig.Builder(decorate).build()
            this.lastSideDecorateConfig = DecorateConfig.Builder(decorate).build()
            return this
        }

        fun firstAndLastSide(
            decorate: Decorate,
            configBlock: (DecorateConfig.Builder.() -> Unit)? = null
        ): Builder {
            this.firstSideDecorateConfig = DecorateConfig.Builder(decorate).apply {
                configBlock?.invoke(this)
            }.build()
            this.lastSideDecorateConfig = DecorateConfig.Builder(decorate).apply {
                configBlock?.invoke(this)
            }.build()
            return this
        }

        fun firstAndLastSide(decorateConfig: DecorateConfig): Builder {
            this.firstSideDecorateConfig = decorateConfig
            this.lastSideDecorateConfig = decorateConfig
            return this
        }


        fun showFirstSide(showFirstSide: Boolean = true): Builder {
            this.showFirstSide = showFirstSide
            return this
        }

        fun showLastSide(showLastSide: Boolean = true): Builder {
            this.showLastSide = showLastSide
            return this
        }

        fun showFirstAndLastSide(showFirstAndLastSide: Boolean = true): Builder {
            this.showFirstSide = showFirstAndLastSide
            this.showLastSide = showFirstAndLastSide
            return this
        }


        fun isFullSpanByPosition(isFullSpanByPosition: IsFullSpanByPosition?): Builder {
            this.isFullSpanByPosition = isFullSpanByPosition
            return this
        }
    }
}