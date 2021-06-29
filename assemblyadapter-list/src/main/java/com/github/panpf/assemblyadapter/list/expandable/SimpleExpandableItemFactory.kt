package com.github.panpf.assemblyadapter.list.expandable

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

abstract class SimpleExpandableItemFactory<DATA> : ExpandableItemFactory<DATA>() {

    override fun createItem(parent: ViewGroup): ExpandableItem<DATA> {
        val context = parent.context
        val itemView = createItemView(context, LayoutInflater.from(context), parent)
        return SimpleExpandableItem(this, itemView).apply {
            initItem(parent.context, itemView, this)
        }
    }

    protected abstract fun createItemView(
        context: Context, inflater: LayoutInflater, parent: ViewGroup
    ): View

    protected open fun initItem(context: Context, itemView: View, item: ExpandableItem<DATA>) {
    }

    protected abstract fun bindItemData(
        context: Context,
        itemView: View,
        item: ExpandableItem<DATA>,
        bindingAdapterPosition: Int,
        data: DATA
    )

    private class SimpleExpandableItem<DATA>(
        private val factory: SimpleExpandableItemFactory<DATA>,
        itemView: View
    ) : ExpandableItem<DATA>(itemView) {

        override fun bindData(bindingAdapterPosition: Int, data: DATA) {
            factory.bindItemData(context, itemView, this, bindingAdapterPosition, data)
        }
    }
}