package com.github.panpf.assemblyadapter.list.expandable

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.github.panpf.assemblyadapter.Placeholder

abstract class BindingAssemblyExpandablePlaceholderItemFactory<VIEW_BINDING : ViewBinding> :
    AssemblyExpandablePlaceholderItemFactory() {

    override fun createItem(parent: ViewGroup): AssemblyExpandablePlaceholderItem {
        val binding = createViewBinding(LayoutInflater.from(parent.context), parent)
        val item = BindingAssemblyExpandablePlaceholderItem(this, binding)
        initItem(parent.context, binding, item)
        return item
    }

    abstract fun createViewBinding(inflater: LayoutInflater, parent: ViewGroup): VIEW_BINDING

    @Suppress("MemberVisibilityCanBePrivate", "UNUSED_PARAMETER")
    open fun initItem(
        context: Context,
        binding: VIEW_BINDING,
        item: BindingAssemblyExpandablePlaceholderItem<VIEW_BINDING>
    ) {
    }

    open fun bindData(
        context: Context,
        binding: VIEW_BINDING,
        item: AssemblyExpandablePlaceholderItem,
        bindingAdapterPosition: Int,
        data: Placeholder
    ) {

    }

    class BindingAssemblyExpandablePlaceholderItem<VIEW_BINDING : ViewBinding>(
        private val factory: BindingAssemblyExpandablePlaceholderItemFactory<VIEW_BINDING>,
        val binding: VIEW_BINDING
    ) : AssemblyExpandablePlaceholderItem(binding.root) {

        public override fun bindData(bindingAdapterPosition: Int, data: Placeholder) {
            factory.bindData(context, binding, this, bindingAdapterPosition, data)
        }
    }
}