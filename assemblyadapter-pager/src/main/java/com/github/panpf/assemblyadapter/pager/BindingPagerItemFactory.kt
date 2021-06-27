package com.github.panpf.assemblyadapter.pager

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding

abstract class BindingPagerItemFactory<DATA, VIEW_BINDING : ViewBinding> :
    PagerItemFactory<DATA>() {

    abstract override fun match(data: Any): Boolean

    abstract fun createItemViewBinding(
        context: Context, inflater: LayoutInflater, parent: ViewGroup, position: Int, data: DATA
    ): VIEW_BINDING

    override fun createItemView(
        context: Context, container: ViewGroup, position: Int, data: DATA
    ): View {
        val inflater = LayoutInflater.from(container.context)
        return createItemViewBinding(context, inflater, container, position, data).root
    }
}