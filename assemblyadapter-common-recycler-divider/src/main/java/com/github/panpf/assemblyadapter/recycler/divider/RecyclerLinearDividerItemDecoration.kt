package com.github.panpf.assemblyadapter.recycler.divider

import android.content.Context
import android.graphics.Canvas
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import com.github.panpf.assemblyadapter.recycler.divider.internal.ItemDecorate
import com.github.panpf.assemblyadapter.recycler.divider.internal.LinearItemDecorateProvider
import com.github.panpf.assemblyadapter.recycler.divider.internal.LinearItemDecorateProviderImpl

open class RecyclerLinearDividerItemDecoration(
    private val linearItemDecorateProvider: LinearItemDecorateProvider,
) : ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State
    ) {
        val layoutManager = (parent.layoutManager?.takeIf { it is LinearLayoutManager }
            ?: IllegalArgumentException("layoutManager must be LinearLayoutManager")) as LinearLayoutManager
        val position =
            (view.layoutParams as RecyclerView.LayoutParams).viewPosition
        val itemCount = parent.adapter?.itemCount ?: 0
        val verticalOrientation = layoutManager.orientation == LinearLayoutManager.VERTICAL

        val startItemDecorate = linearItemDecorateProvider.getItemDecorate(
            view, parent, itemCount, position, verticalOrientation, ItemDecorate.Type.START
        )
        val topItemDecorate = linearItemDecorateProvider.getItemDecorate(
            view, parent, itemCount, position, verticalOrientation, ItemDecorate.Type.TOP
        )
        val endItemDecorate = linearItemDecorateProvider.getItemDecorate(
            view, parent, itemCount, position, verticalOrientation, ItemDecorate.Type.END
        )
        val bottomItemDecorate = linearItemDecorateProvider.getItemDecorate(
            view, parent, itemCount, position, verticalOrientation, ItemDecorate.Type.BOTTOM
        )

        outRect.set(
            startItemDecorate?.widthSize ?: 0,
            topItemDecorate?.heightSize ?: 0,
            endItemDecorate?.widthSize ?: 0,
            bottomItemDecorate?.heightSize ?: 0
        )
    }

    override fun onDraw(canvas: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        val layoutManager =
            (parent.layoutManager?.takeIf { it is LinearLayoutManager } as LinearLayoutManager?)
                ?: return
        if (layoutManager.orientation == LinearLayoutManager.VERTICAL) {
            drawVerticalDivider(canvas, parent)
        } else {
            drawHorizontalDivider(canvas, parent)
        }
    }

    private fun drawVerticalDivider(canvas: Canvas, parent: RecyclerView) {
        val parentLeft = parent.paddingLeft
        val parentRight = parent.width - parent.paddingRight
        val childCount = parent.childCount
        val itemCount = parent.adapter?.itemCount ?: 0
        for (index in 0 until childCount) {
            val childView = parent.getChildAt(index)
            val position = (childView.layoutParams as RecyclerView.LayoutParams).viewPosition

            val startItemDecorate = linearItemDecorateProvider.getItemDecorate(
                childView, parent, itemCount, position, true, ItemDecorate.Type.START
            )
            val topItemDecorate = linearItemDecorateProvider.getItemDecorate(
                childView, parent, itemCount, position, true, ItemDecorate.Type.TOP
            )
            val endItemDecorate = linearItemDecorateProvider.getItemDecorate(
                childView, parent, itemCount, position, true, ItemDecorate.Type.END
            )
            val bottomItemDecorate = linearItemDecorateProvider.getItemDecorate(
                childView, parent, itemCount, position, true, ItemDecorate.Type.BOTTOM
            )

            startItemDecorate?.apply {
                drawable.setBounds(
                    parentLeft,
                    childView.top + insetStart,
                    parentLeft + widthSize,
                    childView.bottom - insetEnd
                )
                drawable.draw(canvas)
            }
            topItemDecorate?.apply {
                drawable.setBounds(
                    parentLeft + insetStart,
                    childView.top - heightSize,
                    parentRight - insetEnd,
                    childView.top
                )
                drawable.draw(canvas)
            }
            endItemDecorate?.apply {
                drawable.setBounds(
                    parentRight - widthSize,
                    childView.top + insetStart,
                    parentRight,
                    childView.bottom - insetEnd
                )
                drawable.draw(canvas)
            }
            bottomItemDecorate?.apply {
                drawable.setBounds(
                    parentLeft + insetStart,
                    childView.bottom,
                    parentRight - insetEnd,
                    childView.bottom + heightSize
                )
                drawable.draw(canvas)
            }
        }
    }

    private fun drawHorizontalDivider(canvas: Canvas, parent: RecyclerView) {
        val parentTop: Int = parent.paddingTop
        val parentBottom: Int = parent.height - parent.paddingBottom
        val childCount: Int = parent.childCount
        val itemCount = parent.adapter?.itemCount ?: 0
        for (index in 0 until childCount) {
            val childView = parent.getChildAt(index)
            val position = (childView.layoutParams as RecyclerView.LayoutParams).viewPosition

            val startItemDecorate = linearItemDecorateProvider.getItemDecorate(
                childView, parent, itemCount, position, false, ItemDecorate.Type.START
            )
            val topItemDecorate = linearItemDecorateProvider.getItemDecorate(
                childView, parent, itemCount, position, false, ItemDecorate.Type.TOP
            )
            val endItemDecorate = linearItemDecorateProvider.getItemDecorate(
                childView, parent, itemCount, position, false, ItemDecorate.Type.END
            )
            val bottomItemDecorate = linearItemDecorateProvider.getItemDecorate(
                childView, parent, itemCount, position, false, ItemDecorate.Type.BOTTOM
            )

            startItemDecorate?.apply {
                drawable.setBounds(
                    childView.left - widthSize,
                    parentTop + insetStart,
                    childView.left,
                    parentBottom - insetEnd
                )
                drawable.draw(canvas)
            }
            topItemDecorate?.apply {
                drawable.setBounds(
                    childView.left + insetStart,
                    childView.top - heightSize,
                    childView.right - insetEnd,
                    childView.top
                )
                drawable.draw(canvas)
            }
            endItemDecorate?.apply {
                drawable.setBounds(
                    childView.right,
                    parentTop + insetStart,
                    childView.right + widthSize,
                    parentBottom - insetEnd
                )
                drawable.draw(canvas)
            }
            bottomItemDecorate?.apply {
                drawable.setBounds(
                    childView.left + insetStart,
                    childView.bottom,
                    childView.right - insetEnd,
                    childView.bottom + heightSize
                )
                drawable.draw(canvas)
            }
        }
    }

    open class Builder(protected val context: Context) {

        private var dividerItemDecorate: ItemDecorate? = null
        private var firstDividerItemDecorate: ItemDecorate? = null
        private var lastDividerItemDecorate: ItemDecorate? = null
        private var startSideItemDecorate: ItemDecorate? = null
        private var endSideItemDecorate: ItemDecorate? = null

        private var showFirstDivider = false
        private var showLastDivider = false

        open fun build(): RecyclerLinearDividerItemDecoration {
            return RecyclerLinearDividerItemDecoration(buildItemDecorateProvider())
        }

        protected open fun buildItemDecorateProvider(): LinearItemDecorateProvider {
            val finalDividerItemDecorate = dividerItemDecorate ?: context.obtainStyledAttributes(
                intArrayOf(android.R.attr.listDivider)
            ).let { array ->
                array.getDrawable(0).apply {
                    array.recycle()
                }
            }!!.let {
                ItemDecorate(it, -1, 0, 0)
            }
            return LinearItemDecorateProviderImpl(
                finalDividerItemDecorate,
                firstDividerItemDecorate
                    ?: if (showFirstDivider) finalDividerItemDecorate else null,
                lastDividerItemDecorate
                    ?: if (showLastDivider) finalDividerItemDecorate else null,
                startSideItemDecorate,
                endSideItemDecorate,
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


        open fun startSide(decorate: Decorate): Builder {
            this.startSideItemDecorate = decorate.createItemDecorate(context)
            return this
        }

        open fun endSide(decorate: Decorate): Builder {
            this.endSideItemDecorate = decorate.createItemDecorate(context)
            return this
        }

        open fun startAndEndSide(decorate: Decorate): Builder {
            this.startSideItemDecorate = decorate.createItemDecorate(context)
            this.endSideItemDecorate = decorate.createItemDecorate(context)
            return this
        }
    }
}