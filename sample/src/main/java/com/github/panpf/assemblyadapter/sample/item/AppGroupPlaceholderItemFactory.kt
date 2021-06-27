package com.github.panpf.assemblyadapter.sample.item

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import com.github.panpf.assemblyadapter.Placeholder
import com.github.panpf.assemblyadapter.list.expandable.BindingExpandableItemFactory
import com.github.panpf.assemblyadapter.list.expandable.ExpandableItem
import com.github.panpf.assemblyadapter.sample.databinding.ItemAppGroupPlaceholderBinding

class AppGroupPlaceholderItemFactory :
    BindingExpandableItemFactory<Placeholder, ItemAppGroupPlaceholderBinding>() {

    override fun match(data: Any): Boolean {
        return data is Placeholder
    }

    override fun createItemViewBinding(
        inflater: LayoutInflater,
        parent: ViewGroup
    ): ItemAppGroupPlaceholderBinding {
        return ItemAppGroupPlaceholderBinding.inflate(inflater, parent, false)
    }

    override fun bindItemData(
        context: Context,
        binding: ItemAppGroupPlaceholderBinding,
        item: ExpandableItem<Placeholder>,
        bindingAdapterPosition: Int,
        data: Placeholder
    ) {

    }
}