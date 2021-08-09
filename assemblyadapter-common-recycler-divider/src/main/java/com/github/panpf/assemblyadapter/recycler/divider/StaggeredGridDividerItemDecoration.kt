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
import com.github.panpf.assemblyadapter.recycler.divider.internal.StaggeredGridItemDecorateProviderImpl
import kotlin.math.ceil
import kotlin.math.floor

fun RecyclerView.staggeredGridDividerItemDecorationBuilder(): StaggeredGridDividerItemDecoration.Builder {
    return StaggeredGridDividerItemDecoration.Builder(context)
}

open class StaggeredGridDividerItemDecoration(
    private val staggeredGridItemDecorateProvider: StaggeredGridItemDecorateProvider,
) : ItemDecoration() {

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
        val verticalOrientation = layoutManager.orientation == StaggeredGridLayoutManager.VERTICAL
        val spanCount = layoutManager.spanCount
        val spanIndex = childLayoutParams.spanIndex
        val isFillSpan = childLayoutParams.isFullSpan
        val isFirstSpan = spanIndex == 0
        val isLastSpan = spanIndex == spanCount - 1

        val startItemDecorate = staggeredGridItemDecorateProvider.getItemDecorate(
            view, parent, itemCount, position, spanCount, isFillSpan,
            spanIndex, verticalOrientation, ItemDecorate.Type.START
        )
        val topItemDecorate = staggeredGridItemDecorateProvider.getItemDecorate(
            view, parent, itemCount, position, spanCount, isFillSpan,
            spanIndex, verticalOrientation, ItemDecorate.Type.TOP
        )
        val endItemDecorate = staggeredGridItemDecorateProvider.getItemDecorate(
            view, parent, itemCount, position, spanCount, isFillSpan,
            spanIndex, verticalOrientation, ItemDecorate.Type.END
        )
        val bottomItemDecorate = staggeredGridItemDecorateProvider.getItemDecorate(
            view, parent, itemCount, position, spanCount, isFillSpan,
            spanIndex, verticalOrientation, ItemDecorate.Type.BOTTOM
        )
        val startItemDecorateSize = startItemDecorate?.widthSize ?: 0
        val topItemDecorateSize = topItemDecorate?.heightSize ?: 0
        val endItemDecorateSize = endItemDecorate?.widthSize ?: 0
        val bottomItemDecorateSize = bottomItemDecorate?.heightSize ?: 0

        if (verticalOrientation) {
            when {
                isFillSpan -> {
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
                isFillSpan -> {
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
        val verticalOrientation = layoutManager.orientation == GridLayoutManager.VERTICAL
        val spanCount = layoutManager.spanCount

        for (index in 0 until childCount) {
            val view = parent.getChildAt(index)
            val childLayoutParams =
                view.layoutParams as StaggeredGridLayoutManager.LayoutParams
            val position = childLayoutParams.absoluteAdapterPosition.takeIf { it != -1 } ?: continue
            val spanIndex = childLayoutParams.spanIndex
            val isFillSpan = childLayoutParams.isFullSpan

            val startItemDecorate = staggeredGridItemDecorateProvider.getItemDecorate(
                view, parent, itemCount, position, spanCount, isFillSpan,
                spanIndex, verticalOrientation, ItemDecorate.Type.START
            )
            val topItemDecorate = staggeredGridItemDecorateProvider.getItemDecorate(
                view, parent, itemCount, position, spanCount, isFillSpan,
                spanIndex, verticalOrientation, ItemDecorate.Type.TOP
            )
            val endItemDecorate = staggeredGridItemDecorateProvider.getItemDecorate(
                view, parent, itemCount, position, spanCount, isFillSpan,
                spanIndex, verticalOrientation, ItemDecorate.Type.END
            )
            val bottomItemDecorate = staggeredGridItemDecorateProvider.getItemDecorate(
                view, parent, itemCount, position, spanCount, isFillSpan,
                spanIndex, verticalOrientation, ItemDecorate.Type.BOTTOM
            )
            val startItemDecorateSize = startItemDecorate?.widthSize ?: 0
            val topItemDecorateSize = topItemDecorate?.heightSize ?: 0
            val endItemDecorateSize = endItemDecorate?.widthSize ?: 0
            val bottomItemDecorateSize = bottomItemDecorate?.heightSize ?: 0

            if (verticalOrientation) {
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

    open class Builder(protected val context: Context) {

        private var dividerItemDecorate: ItemDecorate? = null
        private var firstDividerItemDecorate: ItemDecorate? = null
        private var lastDividerItemDecorate: ItemDecorate? = null
        private var sideItemDecorate: ItemDecorate? = null
        private var firstSideItemDecorate: ItemDecorate? = null
        private var lastSideItemDecorate: ItemDecorate? = null

        private var showFirstDivider = false
        private var showLastDivider = false
        private var showFirstSide = false
        private var showLastSide = false

        open fun build(): StaggeredGridDividerItemDecoration {
            return StaggeredGridDividerItemDecoration(buildItemDecorateProvider())
        }

        protected open fun buildItemDecorateProvider(): StaggeredGridItemDecorateProvider {
            val finalDividerItemDecorate = dividerItemDecorate ?: context.obtainStyledAttributes(
                intArrayOf(android.R.attr.listDivider)
            ).let { array ->
                array.getDrawable(0).apply {
                    array.recycle()
                }
            }!!.let {
                Decorate.drawable(it).createItemDecorate(context)
            }
            return StaggeredGridItemDecorateProviderImpl(
                finalDividerItemDecorate,
                firstDividerItemDecorate
                    ?: if (showFirstDivider) finalDividerItemDecorate else null,
                lastDividerItemDecorate
                    ?: if (showLastDivider) finalDividerItemDecorate else null,
                sideItemDecorate,
                firstSideItemDecorate
                    ?: if (showFirstSide) sideItemDecorate else null,
                lastSideItemDecorate
                    ?: if (showFirstSide) sideItemDecorate else null,
            )
        }


        open fun divider(decorate: Decorate): Builder {
            this.dividerItemDecorate = decorate.createItemDecorate(context)
            return this
        }

        open fun firstDivider(decorate: Decorate): Builder {
            this.firstDividerItemDecorate = decorate.createItemDecorate(context)
            return this
        }

        open fun lastDivider(decorate: Decorate): Builder {
            this.lastDividerItemDecorate = decorate.createItemDecorate(context)
            return this
        }

        open fun firstAndLastDivider(decorate: Decorate): Builder {
            this.firstDividerItemDecorate = decorate.createItemDecorate(context)
            this.lastDividerItemDecorate = decorate.createItemDecorate(context)
            return this
        }

        open fun showFirstDivider(showFirstDivider: Boolean = true): Builder {
            this.showFirstDivider = showFirstDivider
            return this
        }

        open fun showLastDivider(showLastDivider: Boolean = true): Builder {
            this.showLastDivider = showLastDivider
            return this
        }

        open fun showFirstAndLastDivider(showFirstAndLastDivider: Boolean = true): Builder {
            this.showFirstDivider = showFirstAndLastDivider
            this.showLastDivider = showFirstAndLastDivider
            return this
        }


        open fun side(decorate: Decorate): Builder {
            this.sideItemDecorate = decorate.createItemDecorate(context)
            return this
        }

        open fun firstSide(decorate: Decorate): Builder {
            this.firstSideItemDecorate = decorate.createItemDecorate(context)
            return this
        }

        open fun lastSide(decorate: Decorate): Builder {
            this.lastSideItemDecorate = decorate.createItemDecorate(context)
            return this
        }

        open fun firstAndLastSide(decorate: Decorate): Builder {
            this.firstSideItemDecorate = decorate.createItemDecorate(context)
            this.lastSideItemDecorate = decorate.createItemDecorate(context)
            return this
        }

        open fun showFirstSide(showFirstSide: Boolean = true): Builder {
            this.showFirstSide = showFirstSide
            return this
        }

        open fun showLastSide(showLastSide: Boolean = true): Builder {
            this.showLastSide = showLastSide
            return this
        }

        open fun showFirstAndLastSide(showFirstAndLastSide: Boolean = true): Builder {
            this.showFirstSide = showFirstAndLastSide
            this.showLastSide = showFirstAndLastSide
            return this
        }
    }
}