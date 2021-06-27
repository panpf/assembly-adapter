package com.github.panpf.assemblyadapter.list.expandable

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding

abstract class BindingExpandableItemFactory<DATA, VIEW_BINDING : ViewBinding> :
    ExpandableItemFactory<DATA>() {

    override fun createItem(parent: ViewGroup): ExpandableItem<DATA> {
        val binding = createItemViewBinding(LayoutInflater.from(parent.context), parent)
        val item = BindingExpandableItem(this, binding)
        initItem(parent.context, binding, item)
        return item
    }

    abstract fun createItemViewBinding(inflater: LayoutInflater, parent: ViewGroup): VIEW_BINDING

    open fun initItem(context: Context, binding: VIEW_BINDING, item: ExpandableItem<DATA>) {
    }

    abstract fun bindItemData(
        context: Context,
        binding: VIEW_BINDING,
        item: ExpandableItem<DATA>,
        bindingAdapterPosition: Int,
        data: DATA
    )

    private class BindingExpandableItem<DATA, VIEW_BINDING : ViewBinding>(
        private val factory: BindingExpandableItemFactory<DATA, VIEW_BINDING>,
        val binding: VIEW_BINDING
    ) : ExpandableItem<DATA>(binding.root) {

        public override fun bindData(bindingAdapterPosition: Int, data: DATA) {
            factory.bindItemData(context, binding, this, bindingAdapterPosition, data)
        }
    }
}