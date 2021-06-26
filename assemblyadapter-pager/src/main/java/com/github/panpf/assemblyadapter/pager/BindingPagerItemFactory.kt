package com.github.panpf.assemblyadapter.pager

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding

abstract class BindingPagerItemFactory<DATA, VIEW_BINDING : ViewBinding> :
    PagerItemFactory<DATA>() {

    abstract override fun match(data: Any): Boolean

    abstract fun createViewBinding(
        context: Context,
        inflater: LayoutInflater,
        parent: ViewGroup,
        position: Int,
        data: DATA
    ): VIEW_BINDING

    override fun createView(
        context: Context,
        container: ViewGroup,
        position: Int,
        data: DATA
    ): View {
        return createViewBinding(
            context,
            LayoutInflater.from(container.context),
            container,
            position,
            data
        ).root
    }
}