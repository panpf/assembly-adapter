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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import com.github.panpf.assemblyadapter.recycler.divider.internal.ItemDivider
import com.github.panpf.assemblyadapter.recycler.divider.internal.LinearItemDividerProvider

open class LinearDividerItemDecoration(
    private val linearItemDividerProvider: LinearItemDividerProvider,
) : ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State
    ) {
        val layoutManager = parent.layoutManager ?: return
        if (layoutManager !is LinearLayoutManager) {
            throw IllegalArgumentException("layoutManager must be LinearLayoutManager")
        }
        val itemCount = layoutManager.itemCount.takeIf { it != 0 } ?: return
        val childLayoutParams = view.layoutParams as RecyclerView.LayoutParams
        val position = childLayoutParams.absoluteAdapterPosition.takeIf { it != -1 } ?: return
        val verticalOrientation = layoutManager.orientation == LinearLayoutManager.VERTICAL

        val startItemDivider = linearItemDividerProvider.getItemDivider(
            view, parent, itemCount, position, verticalOrientation, ItemDivider.Type.START
        )
        val topItemDivider = linearItemDividerProvider.getItemDivider(
            view, parent, itemCount, position, verticalOrientation, ItemDivider.Type.TOP
        )
        val endItemDivider = linearItemDividerProvider.getItemDivider(
            view, parent, itemCount, position, verticalOrientation, ItemDivider.Type.END
        )
        val bottomItemDivider = linearItemDividerProvider.getItemDivider(
            view, parent, itemCount, position, verticalOrientation, ItemDivider.Type.BOTTOM
        )
        val startItemDividerSize = startItemDivider?.widthSize ?: 0
        val topItemDividerSize = topItemDivider?.heightSize ?: 0
        val endItemDividerSize = endItemDivider?.widthSize ?: 0
        val bottomItemDividerSize = bottomItemDivider?.heightSize ?: 0

        outRect.set(
            startItemDividerSize,
            topItemDividerSize,
            endItemDividerSize,
            bottomItemDividerSize
        )
    }

    override fun onDraw(canvas: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        val layoutManager = parent.layoutManager ?: return
        if (layoutManager !is LinearLayoutManager) {
            throw IllegalArgumentException("layoutManager must be LinearLayoutManager")
        }
        val itemCount = layoutManager.itemCount.takeIf { it != 0 } ?: return
        val childCount = parent.childCount.takeIf { it != 0 } ?: return
        val verticalOrientation = layoutManager.orientation == LinearLayoutManager.VERTICAL

        for (index in 0 until childCount) {
            val view = parent.getChildAt(index)
            val childLayoutParams = view.layoutParams as RecyclerView.LayoutParams
            val position = childLayoutParams.absoluteAdapterPosition.takeIf { it != -1 } ?: continue

            val startItemDivider = linearItemDividerProvider.getItemDivider(
                view, parent, itemCount, position, verticalOrientation, ItemDivider.Type.START
            )
            val topItemDivider = linearItemDividerProvider.getItemDivider(
                view, parent, itemCount, position, verticalOrientation, ItemDivider.Type.TOP
            )
            val endItemDivider = linearItemDividerProvider.getItemDivider(
                view, parent, itemCount, position, verticalOrientation, ItemDivider.Type.END
            )
            val bottomItemDivider = linearItemDividerProvider.getItemDivider(
                view, parent, itemCount, position, verticalOrientation, ItemDivider.Type.BOTTOM
            )
            val startItemDividerSize = startItemDivider?.widthSize ?: 0
            val topItemDividerSize = topItemDivider?.heightSize ?: 0
            val endItemDividerSize = endItemDivider?.widthSize ?: 0
            val bottomItemDividerSize = bottomItemDivider?.heightSize ?: 0

            if (verticalOrientation) {
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

    open class Builder(protected val context: Context) {

        private var dividerConfig: DividerConfig? = null
        private var firstDividerConfig: DividerConfig? = null
        private var lastDividerConfig: DividerConfig? = null
        private var showFirstDivider = false
        private var showLastDivider = false

        private var firstSideDividerConfig: DividerConfig? = null
        private var lastSideDividerConfig: DividerConfig? = null

        open fun build(): LinearDividerItemDecoration {
            return LinearDividerItemDecoration(buildItemDividerProvider())
        }

        protected open fun buildItemDividerProvider(): LinearItemDividerProvider {
            val finalDividerConfig = dividerConfig ?: context.obtainStyledAttributes(
                intArrayOf(android.R.attr.listDivider)
            ).let { array ->
                array.getDrawable(0).apply {
                    array.recycle()
                }
            }!!.let {
                DividerConfig.Builder(Divider.drawable(it)).build()
            }

            return LinearItemDividerProvider(
                dividerConfig = finalDividerConfig.toItemDividerConfig(context),
                firstDividerConfig = (firstDividerConfig
                    ?: if (showFirstDivider) finalDividerConfig else null)
                    ?.toItemDividerConfig(context),
                lastDividerConfig = (lastDividerConfig
                    ?: if (showLastDivider) finalDividerConfig else null)
                    ?.toItemDividerConfig(context),
                firstSideDividerConfig = firstSideDividerConfig?.toItemDividerConfig(context),
                lastSideDividerConfig = lastSideDividerConfig?.toItemDividerConfig(context),
            )
        }


        fun divider(
            divider: Divider,
            configBlock: (DividerConfig.Builder.() -> Unit)? = null
        ): Builder {
            this.dividerConfig = DividerConfig.Builder(divider).apply {
                configBlock?.invoke(this)
            }.build()
            return this
        }

        fun divider(config: DividerConfig): Builder {
            this.dividerConfig = config
            return this
        }


        fun firstDivider(
            divider: Divider,
            configBlock: (DividerConfig.Builder.() -> Unit)? = null
        ): Builder {
            this.firstDividerConfig = DividerConfig.Builder(divider).apply {
                configBlock?.invoke(this)
            }.build()
            return this
        }

        fun firstDivider(config: DividerConfig): Builder {
            this.firstDividerConfig = config
            return this
        }


        fun lastDivider(
            divider: Divider,
            configBlock: (DividerConfig.Builder.() -> Unit)? = null
        ): Builder {
            this.lastDividerConfig = DividerConfig.Builder(divider).apply {
                configBlock?.invoke(this)
            }.build()
            return this
        }

        fun lastDivider(config: DividerConfig): Builder {
            this.lastDividerConfig = config
            return this
        }


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

        fun firstAndLastDivider(config: DividerConfig): Builder {
            this.firstDividerConfig = config
            this.lastDividerConfig = config
            return this
        }


        fun showFirstDivider(show: Boolean = true): Builder {
            this.showFirstDivider = show
            return this
        }

        fun showLastDivider(show: Boolean = true): Builder {
            this.showLastDivider = show
            return this
        }

        fun showFirstAndLastDivider(show: Boolean = true): Builder {
            this.showFirstDivider = show
            this.showLastDivider = show
            return this
        }


        fun firstSideDivider(
            divider: Divider,
            configBlock: (DividerConfig.Builder.() -> Unit)? = null
        ): Builder {
            this.firstSideDividerConfig = DividerConfig.Builder(divider).apply {
                configBlock?.invoke(this)
            }.build()
            return this
        }

        fun firstSideDivider(config: DividerConfig): Builder {
            this.firstSideDividerConfig = config
            return this
        }


        fun lastSideDivider(
            divider: Divider,
            configBlock: (DividerConfig.Builder.() -> Unit)? = null
        ): Builder {
            this.lastSideDividerConfig = DividerConfig.Builder(divider).apply {
                configBlock?.invoke(this)
            }.build()
            return this
        }

        fun lastSideDivider(config: DividerConfig): Builder {
            this.lastSideDividerConfig = config
            return this
        }


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

        fun firstAndLastSideDivider(config: DividerConfig): Builder {
            this.firstSideDividerConfig = config
            this.lastSideDividerConfig = config
            return this
        }
    }
}