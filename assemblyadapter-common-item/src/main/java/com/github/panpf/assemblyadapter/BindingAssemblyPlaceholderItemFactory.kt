package com.github.panpf.assemblyadapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding

abstract class BindingAssemblyPlaceholderItemFactory<VIEW_BINDING : ViewBinding> :
    AssemblyPlaceholderItemFactory() {

    override fun createItem(parent: ViewGroup): AssemblyPlaceholderItem {
        val binding = createViewBinding(LayoutInflater.from(parent.context), parent)
        val item = BindingAssemblyPlaceholderItem(this, binding)
        initItem(parent.context, binding, item)
        return item
    }

    abstract fun createViewBinding(inflater: LayoutInflater, parent: ViewGroup): VIEW_BINDING

    open fun initItem(
        context: Context,
        binding: VIEW_BINDING,
        item: BindingAssemblyPlaceholderItem<VIEW_BINDING>,
    ) {
    }

    open fun bindItemData(
        context: Context,
        binding: VIEW_BINDING,
        item: AssemblyPlaceholderItem,
        bindingAdapterPosition: Int,
        data: Placeholder
    ) {

    }

    class BindingAssemblyPlaceholderItem<VIEW_BINDING : ViewBinding>(
        private val factory: BindingAssemblyPlaceholderItemFactory<VIEW_BINDING>,
        val binding: VIEW_BINDING
    ) : AssemblyPlaceholderItem(binding.root) {

        override fun bindData(bindingAdapterPosition: Int, data: Placeholder) {
            factory.bindItemData(context, binding, this, bindingAdapterPosition, data)
        }
    }
}