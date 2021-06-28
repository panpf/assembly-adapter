package com.github.panpf.assemblyadapter

import android.content.Context
import android.view.View

fun interface OnClickListener<DATA> {
    fun onClick(
        context: Context,
        view: View,
        bindingAdapterPosition: Int,
        absoluteAdapterPosition: Int,
        data: DATA
    )
}