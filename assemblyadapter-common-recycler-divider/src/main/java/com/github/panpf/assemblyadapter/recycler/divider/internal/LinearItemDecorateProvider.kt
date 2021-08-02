package com.github.panpf.assemblyadapter.recycler.divider.internal

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.github.panpf.assemblyadapter.recycler.divider.internal.ItemDecorate

interface LinearItemDecorateProvider {

    fun getItemDecorate(
        view: View,
        parent: RecyclerView,
        itemCount: Int,
        position: Int,
        verticalOrientation: Boolean,
        decorateType: ItemDecorate.Type,
    ): ItemDecorate?
}