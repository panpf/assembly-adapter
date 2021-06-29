package com.github.panpf.assemblyadapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

abstract class SimpleItemFactory<DATA> : ItemFactory<DATA>() {

    override fun createItem(parent: ViewGroup): Item<DATA> {
        val context = parent.context
        val itemView = createItemView(context, LayoutInflater.from(context), parent)
        return SimpleItem(this, itemView).apply {
            initItem(parent.context, itemView, this)
        }
    }

    protected abstract fun createItemView(
        context: Context, inflater: LayoutInflater, parent: ViewGroup
    ): View

    protected open fun initItem(context: Context, itemView: View, item: Item<DATA>) {
    }

    protected abstract fun bindItemData(
        context: Context, itemView: View, item: Item<DATA>, bindingAdapterPosition: Int, data: DATA
    )

    private class SimpleItem<DATA>(
        private val factory: SimpleItemFactory<DATA>,
        itemView: View
    ) : Item<DATA>(itemView) {

        override fun bindData(bindingAdapterPosition: Int, data: DATA) {
            factory.bindItemData(context, itemView, this, bindingAdapterPosition, data)
        }
    }
}