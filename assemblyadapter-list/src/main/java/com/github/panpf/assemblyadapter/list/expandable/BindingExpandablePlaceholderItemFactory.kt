package com.github.panpf.assemblyadapter.list.expandable

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.github.panpf.assemblyadapter.Placeholder

abstract class BindingExpandablePlaceholderItemFactory<VIEW_BINDING : ViewBinding> :
    ExpandablePlaceholderItemFactory() {

    override fun createItem(parent: ViewGroup): ExpandablePlaceholderItem {
        val binding = createViewBinding(LayoutInflater.from(parent.context), parent)
        val item = BindingExpandablePlaceholderItem(this, binding)
        initItem(parent.context, binding, item)
        return item
    }

    abstract fun createViewBinding(inflater: LayoutInflater, parent: ViewGroup): VIEW_BINDING

    @Suppress("MemberVisibilityCanBePrivate", "UNUSED_PARAMETER")
    open fun initItem(context: Context, binding: VIEW_BINDING, item: ExpandablePlaceholderItem) {
    }

    open fun bindData(
        context: Context,
        binding: VIEW_BINDING,
        item: ExpandablePlaceholderItem,
        bindingAdapterPosition: Int,
        data: Placeholder
    ) {

    }

    private class BindingExpandablePlaceholderItem<VIEW_BINDING : ViewBinding>(
        private val factory: BindingExpandablePlaceholderItemFactory<VIEW_BINDING>,
        val binding: VIEW_BINDING
    ) : ExpandablePlaceholderItem(binding.root) {

        public override fun bindData(bindingAdapterPosition: Int, data: Placeholder) {
            factory.bindData(context, binding, this, bindingAdapterPosition, data)
        }
    }
}