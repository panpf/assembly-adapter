package com.github.panpf.assemblyadapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding

abstract class BindingPlaceholderItemFactory<VIEW_BINDING : ViewBinding> :
    PlaceholderItemFactory() {

    override fun createItem(parent: ViewGroup): PlaceholderItem {
        val binding = createViewBinding(LayoutInflater.from(parent.context), parent)
        val item = BindingPlaceholderItem(this, binding)
        initItem(parent.context, binding, item)
        return item
    }

    abstract fun createViewBinding(inflater: LayoutInflater, parent: ViewGroup): VIEW_BINDING

    open fun initItem(context: Context, binding: VIEW_BINDING, item: PlaceholderItem) {
    }

    open fun bindItemData(
        context: Context,
        binding: VIEW_BINDING,
        item: PlaceholderItem,
        bindingAdapterPosition: Int,
        data: Placeholder
    ) {

    }

    private class BindingPlaceholderItem<VIEW_BINDING : ViewBinding>(
        private val factory: BindingPlaceholderItemFactory<VIEW_BINDING>,
        val binding: VIEW_BINDING
    ) : PlaceholderItem(binding.root) {

        override fun bindData(bindingAdapterPosition: Int, data: Placeholder) {
            factory.bindItemData(context, binding, this, bindingAdapterPosition, data)
        }
    }
}