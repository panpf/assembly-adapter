package com.github.panpf.assemblyadapter3.compat

import android.view.ViewGroup
import com.github.panpf.assemblyadapter.Item

class CompatItemFactory(val itemFactory: CompatAssemblyItemFactory<*>) :
    com.github.panpf.assemblyadapter.ItemFactory<Any>(Any::class) {

    override fun exactMatchData(data: Any): Boolean {
        return itemFactory.match(data)
    }

    override fun createItem(parent: ViewGroup): Item<Any> {
        return NewItemCompat(itemFactory.dispatchCreateItem(parent))
    }

    class NewItemCompat(val item: CompatAssemblyItem<*>) :
        Item<Any>(item.itemView) {
        override fun bindData(
            bindingAdapterPosition: Int,
            absoluteAdapterPosition: Int,
            data: Any
        ) {
            @Suppress("UNCHECKED_CAST")
            (item as CompatAssemblyItem<Any>).setData(absoluteAdapterPosition, data)
        }
    }
}