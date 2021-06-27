package com.github.panpf.assemblyadapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding

abstract class BindingItemFactory<DATA, VIEW_BINDING : ViewBinding> :
    ItemFactory<DATA>() {

    override fun createItem(parent: ViewGroup): Item<DATA> {
        val binding = createItemViewBinding(LayoutInflater.from(parent.context), parent)
        val item = BindingItem(this, binding)
        initItem(parent.context, binding, item)
        return item
    }

    abstract fun createItemViewBinding(inflater: LayoutInflater, parent: ViewGroup): VIEW_BINDING

    open fun initItem(context: Context, binding: VIEW_BINDING, item: Item<DATA>) {
    }

    abstract fun bindItemData(
        context: Context,
        binding: VIEW_BINDING,
        item: Item<DATA>,
        bindingAdapterPosition: Int,
        data: DATA
    )

    private class BindingItem<DATA, VIEW_BINDING : ViewBinding>(
        private val factory: BindingItemFactory<DATA, VIEW_BINDING>,
        val binding: VIEW_BINDING
    ) : Item<DATA>(binding.root) {

        override fun bindData(bindingAdapterPosition: Int, data: DATA) {
            factory.bindItemData(context, binding, this, bindingAdapterPosition, data)
        }
    }
}