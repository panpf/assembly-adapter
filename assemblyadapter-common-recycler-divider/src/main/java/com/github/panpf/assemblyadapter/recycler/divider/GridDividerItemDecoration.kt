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
import com.github.panpf.assemblyadapter.recycler.divider.internal.ItemDecorate

open class GridDividerItemDecoration(
    private val gridItemDecorateProvider: GridItemDecorateProvider,
) : ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State
    ) {
        val layoutManager = parent.layoutManager ?: return
        if (layoutManager !is GridLayoutManager) {
            throw IllegalArgumentException("layoutManager must be GridLayoutManager")
        }
        val itemCount = layoutManager.itemCount.takeIf { it != 0 } ?: return
        val childLayoutParams = view.layoutParams as GridLayoutManager.LayoutParams
        val position = childLayoutParams.absoluteAdapterPosition.takeIf { it != -1 } ?: return
        val vertical = layoutManager.orientation == GridLayoutManager.VERTICAL
        val spanSizeLookup = layoutManager.spanSizeLookup
        if (!spanSizeLookup.isSpanGroupIndexCacheEnabled) {
            spanSizeLookup.isSpanGroupIndexCacheEnabled = true
        }
        val spanCount = layoutManager.spanCount
        val spanSize = childLayoutParams.spanSize
        val spanIndex = childLayoutParams.spanIndex
        val spanGroupCount = spanSizeLookup.getSpanGroupIndex(itemCount - 1, spanCount) + 1
        val spanGroupIndex = spanSizeLookup.getSpanGroupIndex(position, spanCount)
        val isFirstGroup = spanGroupIndex == 0
        val isLastGroup = spanGroupIndex == spanGroupCount - 1
        val isFullSpan = spanSize == spanCount
        val isFirstSpan = isFullSpan || spanIndex == 0
        val isLastSpan = isFullSpan || spanIndex == spanCount - 1

        val startItemDecorate = gridItemDecorateProvider.getItemDecorate(
            view, parent, itemCount, position, spanCount, spanSize, spanIndex,
            isFullSpan, isFirstSpan, isLastSpan, spanGroupCount, spanGroupIndex,
            isFirstGroup, isLastGroup, vertical, ItemDecorate.Type.START
        )
        val topItemDecorate = gridItemDecorateProvider.getItemDecorate(
            view, parent, itemCount, position, spanCount, spanSize, spanIndex,
            isFullSpan, isFirstSpan, isLastSpan, spanGroupCount, spanGroupIndex,
            isFirstGroup, isLastGroup, vertical, ItemDecorate.Type.TOP
        )
        val endItemDecorate = gridItemDecorateProvider.getItemDecorate(
            view, parent, itemCount, position, spanCount, spanSize, spanIndex,
            isFullSpan, isFirstSpan, isLastSpan, spanGroupCount, spanGroupIndex,
            isFirstGroup, isLastGroup, vertical, ItemDecorate.Type.END
        )
        val bottomItemDecorate = gridItemDecorateProvider.getItemDecorate(
            view, parent, itemCount, position, spanCount, spanSize, spanIndex,
            isFullSpan, isFirstSpan, isLastSpan, spanGroupCount, spanGroupIndex,
            isFirstGroup, isLastGroup, vertical, ItemDecorate.Type.BOTTOM
        )
        val startItemDecorateSize = startItemDecorate?.widthSize ?: 0
        val topItemDecorateSize = topItemDecorate?.heightSize ?: 0
        val endItemDecorateSize = endItemDecorate?.widthSize ?: 0
        val bottomItemDecorateSize = bottomItemDecorate?.heightSize ?: 0

        outRect.set(
            startItemDecorateSize,
            topItemDecorateSize,
            endItemDecorateSize,
            bottomItemDecorateSize
        )
    }

    override fun onDraw(canvas: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        val layoutManager = parent.layoutManager ?: return
        if (layoutManager !is GridLayoutManager) {
            throw IllegalArgumentException("layoutManager must be GridLayoutManager")
        }
        val itemCount = layoutManager.itemCount.takeIf { it != 0 } ?: return
        val childCount = parent.childCount.takeIf { it != 0 } ?: return
        val vertical = layoutManager.orientation == GridLayoutManager.VERTICAL
        val spanSizeLookup = layoutManager.spanSizeLookup
        if (!spanSizeLookup.isSpanGroupIndexCacheEnabled) {
            spanSizeLookup.isSpanGroupIndexCacheEnabled = true
        }
        val spanCount = layoutManager.spanCount
        val spanGroupCount =
            layoutManager.spanSizeLookup.getSpanGroupIndex(itemCount - 1, spanCount) + 1

        for (index in 0 until childCount) {
            val view = parent.getChildAt(index)
            val childLayoutParams = view.layoutParams as GridLayoutManager.LayoutParams
            val position = childLayoutParams.absoluteAdapterPosition.takeIf { it != -1 } ?: continue
            val spanSize = childLayoutParams.spanSize
            val spanIndex = childLayoutParams.spanIndex
            val spanGroupIndex = spanSizeLookup.getSpanGroupIndex(position, spanCount)
            val isFirstGroup = spanGroupIndex == 0
            val isLastGroup = spanGroupIndex == spanGroupCount - 1
            val isFullSpan = spanSize == spanCount
            val isFirstSpan = isFullSpan || spanIndex == 0
            val isLastSpan = isFullSpan || spanIndex == spanCount - 1

            val startItemDecorate = gridItemDecorateProvider.getItemDecorate(
                view, parent, itemCount, position, spanCount, spanSize, spanIndex,
                isFullSpan, isFirstSpan, isLastSpan, spanGroupCount, spanGroupIndex,
                isFirstGroup, isLastGroup, vertical, ItemDecorate.Type.START
            )
            val topItemDecorate = gridItemDecorateProvider.getItemDecorate(
                view, parent, itemCount, position, spanCount, spanSize, spanIndex,
                isFullSpan, isFirstSpan, isLastSpan, spanGroupCount, spanGroupIndex,
                isFirstGroup, isLastGroup, vertical, ItemDecorate.Type.TOP
            )
            val endItemDecorate = gridItemDecorateProvider.getItemDecorate(
                view, parent, itemCount, position, spanCount, spanSize, spanIndex,
                isFullSpan, isFirstSpan, isLastSpan, spanGroupCount, spanGroupIndex,
                isFirstGroup, isLastGroup, vertical, ItemDecorate.Type.END
            )
            val bottomItemDecorate = gridItemDecorateProvider.getItemDecorate(
                view, parent, itemCount, position, spanCount, spanSize, spanIndex,
                isFullSpan, isFirstSpan, isLastSpan, spanGroupCount, spanGroupIndex,
                isFirstGroup, isLastGroup, vertical, ItemDecorate.Type.BOTTOM
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

    open class Builder(protected val context: Context) {

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

        open fun build(): GridDividerItemDecoration {
            return GridDividerItemDecoration(buildItemDecorateProvider())
        }

        protected open fun buildItemDecorateProvider(): GridItemDecorateProvider {
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
            return GridItemDecorateProvider(
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
    }
}