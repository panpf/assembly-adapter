package com.github.panpf.assemblyadapter3.compat

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.panpf.assemblyadapter3.compat.internal.CompatSpanSizeAdapter

class CompatAssemblyGridLayoutManager : GridLayoutManager {

    private var recyclerView: RecyclerView? = null

    constructor(
        context: Context, attrs: AttributeSet,
        defStyleAttr: Int, defStyleRes: Int, recyclerView: RecyclerView
    ) : super(context, attrs, defStyleAttr, defStyleRes) {
        this.recyclerView = recyclerView
    }

    constructor(context: Context, spanCount: Int, recyclerView: RecyclerView) : super(
        context,
        spanCount
    ) {
        this.recyclerView = recyclerView
    }

    constructor(
        context: Context, spanCount: Int, orientation: Int,
        reverseLayout: Boolean, recyclerView: RecyclerView
    ) : super(context, spanCount, orientation, reverseLayout) {
        this.recyclerView = recyclerView
    }

    constructor(
        context: Context, attrs: AttributeSet,
        defStyleAttr: Int, defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes)

    constructor(context: Context, spanCount: Int) : super(
        context,
        spanCount
    )

    constructor(
        context: Context, spanCount: Int, orientation: Int,
        reverseLayout: Boolean
    ) : super(context, spanCount, orientation, reverseLayout)

    override fun onAttachedToWindow(view: RecyclerView?) {
        super.onAttachedToWindow(view)
        if (recyclerView == null) {
            recyclerView = view
        }
    }

    init {
        spanSizeLookup = object : SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                val adapter = recyclerView?.adapter
                return if (adapter is CompatSpanSizeAdapter) {
                    val spanSize =
                        (adapter as CompatSpanSizeAdapter).getSpanSizeByPosition(position)
                    if (spanSize < 0) spanCount else spanSize.coerceAtLeast(1)
                } else 1
            }
        }
    }
}