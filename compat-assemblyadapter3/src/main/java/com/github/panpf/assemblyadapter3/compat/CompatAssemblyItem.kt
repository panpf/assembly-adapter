package com.github.panpf.assemblyadapter3.compat

import android.app.Application
import android.content.Context
import android.content.res.Resources
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

abstract class CompatAssemblyItem<DATA>(val itemView: View) {

    protected var context: Context = itemView.context
    protected var application: Application = itemView.context.applicationContext as Application
    protected var resources: Resources = itemView.resources
    private var _data: DATA? = null
    private var _position = 0
    var isExpanded = false // ExpandableListView Dedicated field
    var groupPosition = 0 // ExpandableListView Dedicated field
    var isLastChild = false // ExpandableListView Dedicated field

    constructor(itemLayoutId: Int, parent: ViewGroup) : this(
        LayoutInflater.from(parent.context).inflate(itemLayoutId, parent, false)
    )

    fun onInit(context: Context) {
        onFindViews()
        onConfigViews(context)
    }

    val data: DATA?
        get() = _data

    val position: Int
        get() = _position

    val adapterPosition: Int
        get() = _position

    fun setData(position: Int, data: DATA?) {
        this._position = position
        this._data = data
        onSetData(position, data)
    }

    fun <T : View?> findViewById(id: Int): T? {
        return itemView.findViewById(id)
    }

    fun <T : View?> findViewWithTag(tag: Any): T? {
        return itemView.findViewWithTag(tag)
    }

    /**
     * 专门用来 find view，只会执行一次
     */
    protected fun onFindViews() {}

    /**
     * 专门用来配置 View，你可在在这里设置 View 的样式以及尺寸，只会执行一次
     */
    protected open fun onConfigViews(context: Context) {}

    /**
     * 设置数据
     *
     * @param position 位置
     * @param data     数据
     */
    protected abstract fun onSetData(position: Int, data: DATA?)
}