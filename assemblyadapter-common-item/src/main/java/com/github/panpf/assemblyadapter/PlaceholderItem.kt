package com.github.panpf.assemblyadapter

import android.view.View
import android.view.ViewGroup

abstract class PlaceholderItem : Item<Placeholder> {

    constructor(itemView: View) : super(itemView)

    constructor(itemLayoutId: Int, parent: ViewGroup) : super(itemLayoutId, parent)

    override fun bindData(bindingAdapterPosition: Int, data: Placeholder) {

    }
}