package com.github.panpf.assemblyadapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes

class ViewItemFactory<DATA>(
    private val dataClazz: Class<DATA>,
    private val viewFactory: (inflater: LayoutInflater, parent: ViewGroup) -> View
) : ItemFactory<DATA>() {

    constructor(dataClazz: Class<DATA>, @LayoutRes layoutResId: Int) : this(
        dataClazz,
        { inflater, parent -> inflater.inflate(layoutResId, parent, false) }
    )

    constructor(dataClazz: Class<DATA>, view: View) : this(dataClazz, { _, _ -> view })

    override fun match(data: Any): Boolean {
        return dataClazz.isInstance(data)
    }

    override fun createItem(parent: ViewGroup): Item<DATA> {
        return ViewItem(viewFactory(LayoutInflater.from(parent.context), parent))
    }

    private class ViewItem<DATA>(itemView: View) : Item<DATA>(itemView) {

        override fun bindData(bindingAdapterPosition: Int, data: DATA) {

        }
    }
}