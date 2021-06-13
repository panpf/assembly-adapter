package com.github.panpf.assemblyadapter

import android.content.Context
import android.view.View

fun interface OnClickListener<DATA> {
    fun onViewClick(context: Context, view: View, position: Int, data: DATA?)
}