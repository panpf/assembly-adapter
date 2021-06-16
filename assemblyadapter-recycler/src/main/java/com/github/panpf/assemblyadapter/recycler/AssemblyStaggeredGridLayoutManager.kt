package com.github.panpf.assemblyadapter.recycler

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.github.panpf.assemblyadapter.recycler.internal.AssemblyRecyclerItem

class AssemblyStaggeredGridLayoutManager : StaggeredGridLayoutManager {

    constructor(
        context: Context, attrs: AttributeSet?,
        defStyleAttr: Int, defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes)

    constructor(spanCount: Int, orientation: Int) : super(spanCount, orientation) {}

    constructor(spanCount: Int) : super(spanCount, VERTICAL)

    fun setSpanSize(
        gridLayoutItemSpanAdapter: GridLayoutItemSpanAdapter<*>,
        recyclerItem: AssemblyRecyclerItem<*>,
        itemType: Int
    ) {
        val itemSpan = gridLayoutItemSpanAdapter.getItemSpanByItemType(itemType)
        if (itemSpan != null && itemSpan.isFullSpan()) {
            val itemView: View = recyclerItem.getItemView()
            val layoutParams = itemView.layoutParams
            if (layoutParams is LayoutParams) {
                layoutParams.isFullSpan = true
                itemView.layoutParams = layoutParams
            }
        }
    }
}