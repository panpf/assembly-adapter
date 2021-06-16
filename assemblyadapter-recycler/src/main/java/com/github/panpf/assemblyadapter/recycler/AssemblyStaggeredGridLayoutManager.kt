package com.github.panpf.assemblyadapter.recycler

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.StaggeredGridLayoutManager

class AssemblyStaggeredGridLayoutManager : StaggeredGridLayoutManager {

    constructor(
        context: Context, attrs: AttributeSet?,
        defStyleAttr: Int, defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes)

    constructor(spanCount: Int, orientation: Int) : super(spanCount, orientation) {}

    constructor(spanCount: Int) : super(spanCount, VERTICAL)

    // todo 设置 itemSpan 的放到这里来，这样就更加直观了
}