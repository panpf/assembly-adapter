package com.github.panpf.assemblyadapter

import android.content.Context
import android.view.View

fun interface OnClickListener<DATA> {
    fun onClick(context: Context, view: View, position: Int, data: DATA?)
}