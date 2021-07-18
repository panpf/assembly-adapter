package com.github.panpf.assemblyadapter3.compat

import android.view.ViewGroup
import com.github.panpf.assemblyadapter.list.ExpandableChildItem
import com.github.panpf.assemblyadapter.list.ExpandableChildItemFactory
import com.github.panpf.assemblyadapter.list.ExpandableGroup

class CompatExpandableChildItemFactory(val itemFactory: CompatAssemblyItemFactory<*>) :
    ExpandableChildItemFactory<ExpandableGroup, Any>(Any::class) {

    override fun exactMatchData(data: Any): Boolean {
        return itemFactory.match(data)
    }

    override fun createItem(parent: ViewGroup): ExpandableChildItem<ExpandableGroup, Any> {
        return NewExpandableChildItemCompat(itemFactory.dispatchCreateItem(parent))
    }

    class NewExpandableChildItemCompat(val item: CompatAssemblyItem<*>) :
        ExpandableChildItem<ExpandableGroup, Any>(item.itemView) {

        override fun bindData(
            groupBindingAdapterPosition: Int,
            groupAbsoluteAdapterPosition: Int,
            groupData: ExpandableGroup,
            isLastChild: Boolean,
            bindingAdapterPosition: Int,
            absoluteAdapterPosition: Int,
            data: Any
        ) {
            @Suppress("UNCHECKED_CAST")
            (item as CompatAssemblyItem<Any>).apply {
                isExpanded = true
                groupPosition = groupAbsoluteAdapterPosition
                this.isLastChild = isLastChild
                setData(absoluteAdapterPosition, data)
            }
        }
    }
}