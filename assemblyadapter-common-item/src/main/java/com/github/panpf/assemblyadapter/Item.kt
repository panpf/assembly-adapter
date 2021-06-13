package com.github.panpf.assemblyadapter

import android.view.View

interface Item<DATA> {

    fun dispatchBindData(position: Int, data: DATA?)

    fun getData(): DATA?

    fun getItemView(): View

    fun getPosition(): Int

    // todo getPosition 、getAdapterPosition、getLayoutPosition、getBindingAdapterPosition、getAbsoluteAdapterPosition
}