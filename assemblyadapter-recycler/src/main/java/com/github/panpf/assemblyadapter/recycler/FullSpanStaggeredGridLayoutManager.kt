package com.github.panpf.assemblyadapter.recycler

import android.view.View
import com.github.panpf.assemblyadapter.ItemFactory

interface FullSpanStaggeredGridLayoutManager {
    fun setFullSpan(itemView: View, itemFactory: ItemFactory<*>)
}