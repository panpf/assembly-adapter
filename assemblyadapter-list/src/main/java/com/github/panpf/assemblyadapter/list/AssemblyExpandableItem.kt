package com.github.panpf.assemblyadapter.list

import android.view.View
import android.view.ViewGroup
import com.github.panpf.assemblyadapter.AssemblyItem

abstract class AssemblyExpandableItem<DATA> : AssemblyItem<DATA> {

    var groupPosition = -1
    var childPosition = -1
    var isExpanded = false
    var isLastChild = false

    constructor(itemView: View) : super(itemView)
    constructor(itemLayoutId: Int, parent: ViewGroup) : super(itemLayoutId, parent)

    val isGroup: Boolean
        get() = childPosition == -1
    val isChild: Boolean
        get() = childPosition != -1
}