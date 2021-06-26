package com.github.panpf.assemblyadapter.list.expandable

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding

abstract class BindingAssemblyExpandableItemFactory<DATA, VIEW_BINDING : ViewBinding> :
    AssemblyExpandableItemFactory<DATA>() {

    override fun createItem(parent: ViewGroup): AssemblyExpandableItem<DATA> {
        val binding = createViewBinding(LayoutInflater.from(parent.context), parent)
        val item = BindingAssemblyExpandableItem(this, binding)
        initItem(parent.context, binding, item)
        return item
    }

    abstract fun createViewBinding(inflater: LayoutInflater, parent: ViewGroup): VIEW_BINDING

    @Suppress("MemberVisibilityCanBePrivate", "UNUSED_PARAMETER")
    open fun initItem(
        context: Context,
        binding: VIEW_BINDING,
        item: BindingAssemblyExpandableItem<DATA, VIEW_BINDING>
    ) {
    }

    abstract fun bindItemData(
        context: Context,
        binding: VIEW_BINDING,
        item: AssemblyExpandableItem<DATA>,
        bindingAdapterPosition: Int,
        data: DATA
    )

    class BindingAssemblyExpandableItem<DATA, VIEW_BINDING : ViewBinding>(
        private val factory: BindingAssemblyExpandableItemFactory<DATA, VIEW_BINDING>,
        val binding: VIEW_BINDING
    ) : AssemblyExpandableItem<DATA>(binding.root) {

        public override fun bindData(bindingAdapterPosition: Int, data: DATA) {
            factory.bindItemData(context, binding, this, bindingAdapterPosition, data)
        }
    }
}