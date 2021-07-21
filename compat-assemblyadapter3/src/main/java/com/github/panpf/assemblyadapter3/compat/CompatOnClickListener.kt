package com.github.panpf.assemblyadapter3.compat

import android.content.Context
import android.view.View

fun interface CompatOnClickListener<DATA> {
    fun onViewClick(context: Context, view: View, position: Int, positionInPart: Int, data: DATA?)
}