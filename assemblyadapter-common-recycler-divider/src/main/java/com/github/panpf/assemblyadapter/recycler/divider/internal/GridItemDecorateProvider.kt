package com.github.panpf.assemblyadapter.recycler.divider.internal

import android.view.View
import androidx.recyclerview.widget.RecyclerView

interface GridItemDecorateProvider {

    fun getItemDecorate(
        view: View,
        parent: RecyclerView,
        itemCount: Int,
        position: Int,
        spanCount: Int,
        spanSize: Int,
        spanIndex: Int,
        spanGroupCount: Int,
        spanGroupIndex: Int,
        verticalOrientation: Boolean,
        decorateType: ItemDecorate.Type,
    ): ItemDecorate?
}