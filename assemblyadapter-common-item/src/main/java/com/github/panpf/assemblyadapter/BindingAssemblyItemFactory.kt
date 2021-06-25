package com.github.panpf.assemblyadapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding

abstract class BindingAssemblyItemFactory<DATA, VIEW_BINDING : ViewBinding> :
    AssemblyItemFactory<DATA>() {

    override fun createItem(parent: ViewGroup): AssemblyItem<DATA> {
        val binding = createViewBinding(LayoutInflater.from(parent.context), parent)
        val item = BindingAssemblyItem(this, binding)
        initItem(parent.context, binding, item)
        return item
    }

    abstract fun createViewBinding(inflater: LayoutInflater, parent: ViewGroup): VIEW_BINDING

    open fun initItem(
        context: Context,
        binding: VIEW_BINDING,
        item: BindingAssemblyItem<DATA, VIEW_BINDING>
    ) {
    }

    abstract fun bindData(
        context: Context,
        binding: VIEW_BINDING,
        item: BindingAssemblyItem<DATA, VIEW_BINDING>,
        bindingAdapterPosition: Int,
        data: DATA?
    )

    class BindingAssemblyItem<DATA, VIEW_BINDING : ViewBinding>(
        private val factory: BindingAssemblyItemFactory<DATA, VIEW_BINDING>,
        val binding: VIEW_BINDING
    ) : AssemblyItem<DATA>(binding.root) {

        public override fun bindData(bindingAdapterPosition: Int, data: DATA?) {
            factory.bindData(context, binding, this, bindingAdapterPosition, data)
        }
    }
}