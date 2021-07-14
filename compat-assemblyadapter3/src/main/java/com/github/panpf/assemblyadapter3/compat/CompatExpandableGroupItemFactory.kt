package com.github.panpf.assemblyadapter3.compat

import android.view.ViewGroup
import com.github.panpf.assemblyadapter.list.ExpandableGroup
import com.github.panpf.assemblyadapter.list.ExpandableGroupItemFactory

class CompatExpandableGroupItemFactory(val itemFactory: CompatAssemblyItemFactory<out CompatAssemblyGroup>) :
    ExpandableGroupItemFactory<ExpandableGroup>(ExpandableGroup::class) {

    override fun exactMatchData(data: ExpandableGroup): Boolean {
        return itemFactory.match(data)
    }

    override fun createItem(parent: ViewGroup): ExpandableGroupItem<ExpandableGroup> {
        return NewExpandableGroupItemCompat(itemFactory.dispatchCreateItem(parent))
    }

    class NewExpandableGroupItemCompat(val item: CompatAssemblyItem<out CompatAssemblyGroup>) :
        ExpandableGroupItem<ExpandableGroup>(item.itemView) {

        override fun bindData(
            isExpanded: Boolean,
            bindingAdapterPosition: Int,
            absoluteAdapterPosition: Int,
            data: ExpandableGroup
        ) {
            @Suppress("UNCHECKED_CAST")
            (item as CompatAssemblyItem<Any>).apply {
                this.isExpanded = isExpanded
                setData(absoluteAdapterPosition, data)
            }
        }
    }
}