package com.github.panpf.assemblyadapter.recycler.internal

import android.view.View
import com.github.panpf.assemblyadapter.ItemFactory

interface FullSpanStaggeredGridLayoutManager {
    fun setFullSpan(itemView: View, itemFactory: ItemFactory<*>)
}