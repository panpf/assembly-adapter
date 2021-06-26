package com.github.panpf.assemblyadapter.sample.item

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import com.github.panpf.assemblyadapter.BindingItemFactory
import com.github.panpf.assemblyadapter.Item
import com.github.panpf.assemblyadapter.Placeholder
import com.github.panpf.assemblyadapter.sample.databinding.ItemAppPlaceholderBinding

class AppPlaceholderItemFactory :
    BindingItemFactory<Placeholder, ItemAppPlaceholderBinding>() {

    override fun match(data: Any): Boolean {
        return data is Placeholder
    }

    override fun createViewBinding(
        inflater: LayoutInflater,
        parent: ViewGroup
    ): ItemAppPlaceholderBinding {
        return ItemAppPlaceholderBinding.inflate(inflater, parent, false)
    }

    override fun bindItemData(
        context: Context,
        binding: ItemAppPlaceholderBinding,
        item: Item<Placeholder>,
        bindingAdapterPosition: Int,
        data: Placeholder
    ) {
    }
}