package com.github.panpf.assemblyadapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes

class ViewItemFactory<DATA>(
    private val dataClazz: Class<DATA>,
    private val viewFactory: (context: Context, inflater: LayoutInflater, parent: ViewGroup) -> View
) : SimpleItemFactory<DATA>() {

    constructor(dataClazz: Class<DATA>, @LayoutRes layoutResId: Int) : this(
        dataClazz,
        { _, inflater, parent -> inflater.inflate(layoutResId, parent, false) }
    )

    constructor(dataClazz: Class<DATA>, view: View) : this(dataClazz, { _, _, _ -> view })

    override fun match(data: Any): Boolean {
        return dataClazz.isInstance(data)
    }

    override fun createItemView(
        context: Context, inflater: LayoutInflater, parent: ViewGroup
    ): View {
        return viewFactory(context, inflater, parent)
    }

    override fun bindItemData(
        context: Context, itemView: View, item: Item<DATA>, bindingAdapterPosition: Int, data: DATA
    ) {
    }
}