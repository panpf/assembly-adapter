package com.github.panpf.assemblyadapter

import android.content.Context
import android.view.View

fun interface OnLongClickListener<DATA> {
    fun onLongClick(
        context: Context,
        view: View,
        bindingAdapterPosition: Int,
        absoluteAdapterPosition: Int,
        data: DATA
    ): Boolean
}