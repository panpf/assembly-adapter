package com.github.panpf.assemblyadapter.list.expandable

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes

class ViewExpandableItemFactory<DATA>(
    private val dataClazz: Class<DATA>,
    private val viewFactory: (inflater: LayoutInflater, parent: ViewGroup) -> View
) : SimpleExpandableItemFactory<DATA>() {

    constructor(dataClazz: Class<DATA>, @LayoutRes layoutResId: Int) : this(
        dataClazz,
        { inflater, parent -> inflater.inflate(layoutResId, parent, false) }
    )

    constructor(dataClazz: Class<DATA>, view: View) : this(dataClazz, { _, _ -> view })

    override fun match(data: Any): Boolean {
        return dataClazz.isInstance(data)
    }

    override fun createItemView(inflater: LayoutInflater, parent: ViewGroup): View {
        return viewFactory(LayoutInflater.from(parent.context), parent)
    }

    override fun bindItemData(
        context: Context,
        itemView: View,
        item: ExpandableItem<DATA>,
        bindingAdapterPosition: Int,
        data: DATA
    ) {
    }
}