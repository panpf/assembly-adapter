package com.github.panpf.assemblyadapter.list.expandable

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding

abstract class BindingExpandableItemFactory<DATA, VIEW_BINDING : ViewBinding> :
    ExpandableItemFactory<DATA>() {

    override fun createItem(parent: ViewGroup): ExpandableItem<DATA> {
        val context = parent.context
        val binding = createItemViewBinding(context, LayoutInflater.from(context), parent)
        return BindingExpandableItem(this, binding).apply {
            initItem(parent.context, binding, this)
        }
    }

    protected abstract fun createItemViewBinding(
        context: Context, inflater: LayoutInflater, parent: ViewGroup
    ): VIEW_BINDING

    protected open fun initItem(
        context: Context, binding: VIEW_BINDING, item: ExpandableItem<DATA>
    ) {
    }

    protected abstract fun bindItemData(
        context: Context,
        binding: VIEW_BINDING,
        item: ExpandableItem<DATA>,
        bindingAdapterPosition: Int,
        data: DATA
    )

    private class BindingExpandableItem<DATA, VIEW_BINDING : ViewBinding>(
        private val factory: BindingExpandableItemFactory<DATA, VIEW_BINDING>,
        private val binding: VIEW_BINDING
    ) : ExpandableItem<DATA>(binding.root) {

        override fun bindData(bindingAdapterPosition: Int, data: DATA) {
            factory.bindItemData(context, binding, this, bindingAdapterPosition, data)
        }
    }
}