package com.github.panpf.assemblyadapter3.compat

import android.view.ViewGroup
import com.github.panpf.assemblyadapter.Item
import com.github.panpf.assemblyadapter.ItemFactory

class CompatItemFactory(val itemFactory: CompatAssemblyItemFactory<*>) :
    ItemFactory<Any>(Any::class) {

    override fun exactMatchData(data: Any): Boolean {
        return itemFactory.match(data)
    }

    override fun createItem(parent: ViewGroup): Item<Any> {
        return CompatItem(itemFactory.dispatchCreateItem(parent))
    }

    class CompatItem(val item: CompatAssemblyItem<*>) :
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