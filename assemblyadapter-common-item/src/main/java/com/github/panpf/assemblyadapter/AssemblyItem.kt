package com.github.panpf.assemblyadapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

abstract class AssemblyItem<DATA>(
    @JvmField val itemView: View
) : Item<DATA> {

    @JvmField
    val context: Context = itemView.context

    @JvmField
    var data: DATA? = null

    @JvmField
    var position = -1

    constructor(itemLayoutId: Int, parent: ViewGroup) : this(
        LayoutInflater.from(parent.context).inflate(itemLayoutId, parent, false)
    )

    open override fun dispatchBindData(position: Int, data: DATA?) {
        this.position = position
        this.data = data
        bindData(position, data)
    }

    protected abstract fun bindData(position: Int, data: DATA?)

    override fun getData(): DATA? = data

    override fun getItemView(): View = itemView

    override fun getPosition(): Int = position
}