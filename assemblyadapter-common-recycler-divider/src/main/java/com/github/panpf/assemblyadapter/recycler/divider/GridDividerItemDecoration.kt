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
import com.github.panpf.assemblyadapter.recycler.divider.internal.GridItemDecorateProvider
import com.github.panpf.assemblyadapter.recycler.divider.internal.GridItemDecorateProviderImpl
import com.github.panpf.assemblyadapter.recycler.divider.internal.ItemDecorate

open class GridDividerItemDecoration(
    private val gridItemDecorateProvider: GridItemDecorateProvider,
) : ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State
    ) {
        val layoutManager = (parent.layoutManager?.takeIf { it is GridLayoutManager }
            ?: IllegalArgumentException("layoutManager must be GridLayoutManager")) as GridLayoutManager
        val spanSizeLookup = layoutManager.spanSizeLookup
        if (!spanSizeLookup.isSpanIndexCacheEnabled) {
            spanSizeLookup.isSpanIndexCacheEnabled = true
        }
        if (!spanSizeLookup.isSpanGroupIndexCacheEnabled) {
            spanSizeLookup.isSpanGroupIndexCacheEnabled = true
        }
        val childLayoutParams = view.layoutParams as RecyclerView.LayoutParams
        val position = childLayoutParams.absoluteAdapterPosition.takeIf { it != -1 } ?: return
        val itemCount = parent.adapter?.itemCount ?: 0
        val verticalOrientation = layoutManager.orientation == GridLayoutManager.VERTICAL
        val spanCount = layoutManager.spanCount
        val spanSize = spanSizeLookup.getSpanSize(position)
        val spanIndex = spanSizeLookup.getSpanIndex(position, spanCount)
        val spanGroupCount = spanSizeLookup.getSpanGroupIndex(itemCount - 1, spanCount) + 1
        val spanGroupIndex = spanSizeLookup.getSpanGroupIndex(position, spanCount)

        val startItemDecorate = gridItemDecorateProvider.getItemDecorate(
            view, parent, itemCount, position, spanCount, spanSize, spanIndex,
            spanGroupCount, spanGroupIndex, verticalOrientation, ItemDecorate.Type.START
        )
        val topItemDecorate = gridItemDecorateProvider.getItemDecorate(
            view, parent, itemCount, position, spanCount, spanSize, spanIndex,
            spanGroupCount, spanGroupIndex, verticalOrientation, ItemDecorate.Type.TOP
        )
        val endItemDecorate = gridItemDecorateProvider.getItemDecorate(
            view, parent, itemCount, position, spanCount, spanSize, spanIndex,
            spanGroupCount, spanGroupIndex, verticalOrientation, ItemDecorate.Type.END
        )
        val bottomItemDecorate = gridItemDecorateProvider.getItemDecorate(
            view, parent, itemCount, position, spanCount, spanSize, spanIndex,
            spanGroupCount, spanGroupIndex, verticalOrientation, ItemDecorate.Type.BOTTOM
        )

        outRect.set(
            startItemDecorate?.run { widthSize + insetWidthSize } ?: 0,
            topItemDecorate?.run { heightSize + insetHeightSize } ?: 0,
            endItemDecorate?.run { widthSize + insetWidthSize } ?: 0,
            bottomItemDecorate?.run { heightSize + insetHeightSize } ?: 0
        )
    }

    override fun onDraw(canvas: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        val layoutManager =
            (parent.layoutManager?.takeIf { it is GridLayoutManager } as GridLayoutManager?)
                ?: return
        val spanSizeLookup = layoutManager.spanSizeLookup
        if (!spanSizeLookup.isSpanIndexCacheEnabled) {
            spanSizeLookup.isSpanIndexCacheEnabled = true
        }
        if (!spanSizeLookup.isSpanGroupIndexCacheEnabled) {
            spanSizeLookup.isSpanGroupIndexCacheEnabled = true
        }
        val childCount = parent.childCount
        val itemCount = parent.adapter?.itemCount ?: 0
        if (childCount == 0 || itemCount == 0) return
        val spanCount = layoutManager.spanCount
        val spanGroupCount =
            layoutManager.spanSizeLookup.getSpanGroupIndex(itemCount - 1, spanCount) + 1
        if (layoutManager.orientation == GridLayoutManager.VERTICAL) {
            drawVerticalDivider(
                canvas, parent, spanSizeLookup, childCount, itemCount, spanCount, spanGroupCount
            )
        } else {
            drawHorizontalDivider(
                canvas, parent, spanSizeLookup, childCount, itemCount, spanCount, spanGroupCount
            )
        }
    }

    private fun drawVerticalDivider(
        canvas: Canvas,
        parent: RecyclerView,
        spanSizeLookup: GridLayoutManager.SpanSizeLookup,
        childCount: Int,
        itemCount: Int,
        spanCount: Int,
        spanGroupCount: Int,
    ) {
        for (index in 0 until childCount) {
            val childView = parent.getChildAt(index)
            val childLayoutParams = childView.layoutParams as RecyclerView.LayoutParams
            val position = childLayoutParams.absoluteAdapterPosition.takeIf { it != -1 } ?: continue
            val spanSize = spanSizeLookup.getSpanSize(position)
            val spanIndex = spanSizeLookup.getSpanIndex(position, spanCount)
            val spanGroupIndex = spanSizeLookup.getSpanGroupIndex(position, spanCount)

            val startItemDecorate = gridItemDecorateProvider.getItemDecorate(
                childView, parent, itemCount, position, spanCount, spanSize, spanIndex,
                spanGroupCount, spanGroupIndex, true, ItemDecorate.Type.START
            )
            val topItemDecorate = gridItemDecorateProvider.getItemDecorate(
                childView, parent, itemCount, position, spanCount, spanSize, spanIndex,
                spanGroupCount, spanGroupIndex, true, ItemDecorate.Type.TOP
            )
            val endItemDecorate = gridItemDecorateProvider.getItemDecorate(
                childView, parent, itemCount, position, spanCount, spanSize, spanIndex,
                spanGroupCount, spanGroupIndex, true, ItemDecorate.Type.END
            )
            val bottomItemDecorate = gridItemDecorateProvider.getItemDecorate(
                childView, parent, itemCount, position, spanCount, spanSize, spanIndex,
                spanGroupCount, spanGroupIndex, true, ItemDecorate.Type.BOTTOM
            )

            startItemDecorate?.apply {
                drawable.setBounds(
                    childView.left - insetEnd - widthSize,
                    childView.top + insetTop,
                    childView.left - insetEnd,
                    childView.bottom - insetBottom
                )
                drawable.draw(canvas)
            }
            topItemDecorate?.apply {
                val haveInset = insetStart > 0 || insetEnd > 0
                if (haveInset) {
                    drawable.setBounds(
                        childView.left + insetStart,
                        childView.top - insetBottom - heightSize,
                        childView.right - insetEnd,
                        childView.top - insetBottom
                    )
                } else {
                    drawable.setBounds(
                        childView.left - (startItemDecorate?.widthSize ?: 0),
                        childView.top - insetBottom - heightSize,
                        childView.right + (endItemDecorate?.widthSize ?: 0),
                        childView.top - insetBottom
                    )
                }
                drawable.draw(canvas)
            }
            endItemDecorate?.apply {
                drawable.setBounds(
                    childView.right + insetStart,
                    childView.top + insetTop,
                    childView.right + insetStart + widthSize,
                    childView.bottom - insetBottom
                )
                drawable.draw(canvas)
            }
            bottomItemDecorate?.apply {
                val haveInset = insetStart > 0 || insetEnd > 0
                if (haveInset) {
                    drawable.setBounds(
                        childView.left + insetStart,
                        childView.bottom + insetTop,
                        childView.right - insetEnd,
                        childView.bottom + insetTop + heightSize
                    )
                } else {
                    drawable.setBounds(
                        childView.left - (startItemDecorate?.widthSize ?: 0),
                        childView.bottom + insetTop,
                        childView.right + (endItemDecorate?.widthSize ?: 0),
                        childView.bottom + insetTop + heightSize
                    )
                }
                drawable.draw(canvas)
            }
        }
    }

    private fun drawHorizontalDivider(
        canvas: Canvas,
        parent: RecyclerView,
        spanSizeLookup: GridLayoutManager.SpanSizeLookup,
        childCount: Int,
        itemCount: Int,
        spanCount: Int,
        spanGroupCount: Int,
    ) {
        for (index in 0 until childCount) {
            val childView = parent.getChildAt(index)
            val childLayoutParams = childView.layoutParams as RecyclerView.LayoutParams
            val position = childLayoutParams.absoluteAdapterPosition.takeIf { it != -1 } ?: continue
            val spanSize = spanSizeLookup.getSpanSize(position)
            val spanIndex = spanSizeLookup.getSpanIndex(position, spanCount)
            val spanGroupIndex = spanSizeLookup.getSpanGroupIndex(position, spanCount)

            val startItemDecorate = gridItemDecorateProvider.getItemDecorate(
                childView, parent, itemCount, position, spanCount, spanSize, spanIndex,
                spanGroupCount, spanGroupIndex, false, ItemDecorate.Type.START
            )
            val topItemDecorate = gridItemDecorateProvider.getItemDecorate(
                childView, parent, itemCount, position, spanCount, spanSize, spanIndex,
                spanGroupCount, spanGroupIndex, false, ItemDecorate.Type.TOP
            )
            val endItemDecorate = gridItemDecorateProvider.getItemDecorate(
                childView, parent, itemCount, position, spanCount, spanSize, spanIndex,
                spanGroupCount, spanGroupIndex, false, ItemDecorate.Type.END
            )
            val bottomItemDecorate = gridItemDecorateProvider.getItemDecorate(
                childView, parent, itemCount, position, spanCount, spanSize, spanIndex,
                spanGroupCount, spanGroupIndex, false, ItemDecorate.Type.BOTTOM
            )

            startItemDecorate?.apply {
                val haveInset = insetStart > 0 || insetEnd > 0
                if (haveInset) {
                    drawable.setBounds(
                        childView.left - insetEnd - widthSize,
                        childView.top + insetTop,
                        childView.left - insetEnd,
                        childView.bottom - insetBottom
                    )
                } else {
                    drawable.setBounds(
                        childView.left - insetEnd - widthSize,
                        childView.top + insetTop - (topItemDecorate?.heightSize ?: 0),
                        childView.left - insetEnd,
                        childView.bottom - insetBottom + (bottomItemDecorate?.heightSize ?: 0)
                    )
                }
                drawable.draw(canvas)
            }
            topItemDecorate?.apply {
                drawable.setBounds(
                    childView.left + insetStart,
                    childView.top - insetBottom - heightSize,
                    childView.right - insetEnd,
                    childView.top - insetBottom
                )
                drawable.draw(canvas)
            }
            endItemDecorate?.apply {
                val haveInset = insetStart > 0 || insetEnd > 0
                if (haveInset) {
                    drawable.setBounds(
                        childView.right + insetStart,
                        childView.top + insetTop,
                        childView.right + insetStart + widthSize,
                        childView.bottom - insetBottom
                    )
                } else {
                    drawable.setBounds(
                        childView.right + insetStart,
                        childView.top + insetTop - (topItemDecorate?.heightSize ?: 0),
                        childView.right + insetStart + widthSize,
                        childView.bottom - insetBottom + (bottomItemDecorate?.heightSize ?: 0)
                    )
                }
                drawable.draw(canvas)
            }
            bottomItemDecorate?.apply {
                drawable.setBounds(
                    childView.left + insetStart,
                    childView.bottom + insetTop,
                    childView.right - insetEnd,
                    childView.bottom + insetTop + heightSize
                )
                drawable.draw(canvas)
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

        open fun build(): GridDividerItemDecoration {
            return GridDividerItemDecoration(buildItemDecorateProvider())
        }

        protected open fun buildItemDecorateProvider(): GridItemDecorateProvider {
            val finalDividerItemDecorate = dividerItemDecorate ?: context.obtainStyledAttributes(
                intArrayOf(android.R.attr.listDivider)
            ).let { array ->
                array.getDrawable(0).apply {
                    array.recycle()
                }
            }!!.let {
                ItemDecorate(it, -1, 0, 0)
            }
            return GridItemDecorateProviderImpl(
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