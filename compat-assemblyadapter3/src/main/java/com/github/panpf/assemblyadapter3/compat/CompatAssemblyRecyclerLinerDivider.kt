package com.github.panpf.assemblyadapter3.compat

import android.R
import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.view.View
import androidx.annotation.DrawableRes
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration

/**
 * [RecyclerView] dedicated divider, head and tail do not display divider
 */
class CompatAssemblyRecyclerLinerDivider : ItemDecoration {

    private var mDivider: Drawable?
    private var recyclerView: RecyclerView

    constructor(recyclerView: RecyclerView) {
        this.recyclerView = recyclerView
        val a = recyclerView.context.obtainStyledAttributes(intArrayOf(R.attr.listDivider))
        mDivider = a.getDrawable(0)
        a.recycle()
    }

    constructor(recyclerView: RecyclerView, dividerDrawable: Drawable?) {
        this.recyclerView = recyclerView
        mDivider = dividerDrawable
    }

    constructor(recyclerView: RecyclerView, @DrawableRes dividerDrawableResId: Int) : this(
        recyclerView,
        recyclerView.resources.getDrawable(dividerDrawableResId)
    ) {
    }

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val layoutManager = recyclerView.layoutManager
        if (layoutManager !is LinearLayoutManager) {
            outRect[0, 0, 0] = 0
            return
        }
        val mOrientation = layoutManager.orientation
        var itemPosition = 0
        var firstDataItemPosition = 0
        var lastDataItemPosition = 0
        val adapter = recyclerView.adapter
        var recyclerAdapter: CompatAssemblyAdapter? = null
        if (adapter is CompatAssemblyAdapter) {
            recyclerAdapter = adapter
            itemPosition = (view.layoutParams as RecyclerView.LayoutParams).viewLayoutPosition
            firstDataItemPosition = adapter.headerCount
            lastDataItemPosition = firstDataItemPosition + (recyclerAdapter.dataCount - 1)
        }
        if (recyclerAdapter == null || itemPosition >= firstDataItemPosition && itemPosition <= lastDataItemPosition) {
            if (mOrientation == LinearLayoutManager.VERTICAL) {
                outRect[0, 0, 0] = mDivider!!.intrinsicHeight
            } else {
                outRect[0, 0, mDivider!!.intrinsicWidth] = 0
            }
        } else {
            outRect[0, 0, 0] = 0
        }
    }

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        val layoutManager = recyclerView.layoutManager as? LinearLayoutManager ?: return
        val mOrientation = layoutManager.orientation
        if (mOrientation == LinearLayoutManager.VERTICAL) {
            drawVertical(c, parent)
        } else {
            drawHorizontal(c, parent)
        }
    }

    private fun drawVertical(c: Canvas, parent: RecyclerView) {
        var firstDataItemPosition = 0
        var lastDataItemPosition = 0
        val adapter = recyclerView.adapter
        var recyclerAdapter: CompatAssemblyAdapter? = null
        if (adapter is CompatAssemblyAdapter) {
            recyclerAdapter = adapter
            firstDataItemPosition = adapter.headerCount
            lastDataItemPosition = firstDataItemPosition + (adapter.dataCount - 1)
        }
        val left = parent.paddingLeft
        val right = parent.width - parent.paddingRight
        val childCount = parent.childCount
        for (i in 0 until childCount) {
            val child = parent.getChildAt(i)
            val itemPosition = (child.layoutParams as RecyclerView.LayoutParams).viewLayoutPosition
            val bottomMargin = (child.layoutParams as RecyclerView.LayoutParams).bottomMargin
            @Suppress("ConvertTwoComparisonsToRangeCheck")
            if (recyclerAdapter == null || itemPosition >= firstDataItemPosition && itemPosition <= lastDataItemPosition) {
                val top = child.bottom + bottomMargin
                val bottom = top + mDivider!!.intrinsicHeight
                mDivider!!.setBounds(left, top, right, bottom)
                mDivider!!.draw(c)
            }
        }
    }

    private fun drawHorizontal(c: Canvas, parent: RecyclerView) {
        var firstDataItemPosition = 0
        var lastDataItemPosition = 0
        val adapter = recyclerView.adapter
        var recyclerAdapter: CompatAssemblyAdapter? = null
        if (adapter is CompatAssemblyAdapter) {
            recyclerAdapter = adapter
            firstDataItemPosition = adapter.headerCount
            lastDataItemPosition = firstDataItemPosition + (recyclerAdapter.dataCount - 1)
        }
        val top = parent.paddingTop
        val bottom = parent.height - parent.paddingBottom
        val childCount = parent.childCount
        for (i in 0 until childCount) {
            val child = parent.getChildAt(i)
            val itemPosition = (child.layoutParams as RecyclerView.LayoutParams).viewLayoutPosition
            val rightMargin = (child.layoutParams as RecyclerView.LayoutParams).rightMargin
            @Suppress("ConvertTwoComparisonsToRangeCheck")
            if (recyclerAdapter == null || itemPosition >= firstDataItemPosition && itemPosition <= lastDataItemPosition) {
                val left = child.right + rightMargin
                val right = left + mDivider!!.intrinsicHeight
                mDivider!!.setBounds(left, top, right, bottom)
                mDivider!!.draw(c)
            }
        }
    }
}