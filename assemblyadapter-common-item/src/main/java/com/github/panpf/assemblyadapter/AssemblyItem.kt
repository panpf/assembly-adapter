package com.github.panpf.assemblyadapter

import android.content.Context
import android.content.res.Resources
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

// todo 直接命名为 Item
abstract class AssemblyItem<DATA>(val itemView: View) {

    private var _data: DATA? = null
    private var _bindingAdapterPosition: Int = -1
    private var _absoluteAdapterPosition: Int = -1

    val context: Context = itemView.context
    val appContext: Context = context.applicationContext
    val resources: Resources = context.resources

    val dataOrNull: DATA?
        get() = _data
    val requireData: DATA
        get() = _data!!
    val bindingAdapterPosition: Int
        get() = _bindingAdapterPosition
    val absoluteAdapterPosition: Int
        get() = _absoluteAdapterPosition

    constructor(itemLayoutId: Int, parent: ViewGroup) : this(
        LayoutInflater.from(parent.context).inflate(itemLayoutId, parent, false)
    )

    open fun dispatchBindData(bindingAdapterPosition: Int, absoluteAdapterPosition: Int, data: DATA) {
        this._data = data
        this._bindingAdapterPosition = bindingAdapterPosition
        this._absoluteAdapterPosition = absoluteAdapterPosition
        bindData(_absoluteAdapterPosition, data)
    }

    protected abstract fun bindData(bindingAdapterPosition: Int, data: DATA)
}