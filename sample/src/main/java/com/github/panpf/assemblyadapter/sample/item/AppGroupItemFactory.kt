package com.github.panpf.assemblyadapter.sample.item

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import com.github.panpf.assemblyadapter.list.expandable.BindingExpandableItemFactory
import com.github.panpf.assemblyadapter.sample.R
import com.github.panpf.assemblyadapter.sample.bean.AppGroup
import com.github.panpf.assemblyadapter.sample.databinding.ItemAppGroupBinding

open class AppGroupItemFactory :
    BindingExpandableItemFactory<AppGroup, ItemAppGroupBinding>() {

    override fun match(data: Any): Boolean {
        return data is AppGroup
    }

    override fun createItemViewBinding(
        inflater: LayoutInflater, parent: ViewGroup
    ): ItemAppGroupBinding {
        return ItemAppGroupBinding.inflate(inflater, parent, false)
    }

    override fun bindItemData(
        context: Context,
        binding: ItemAppGroupBinding,
        item: ExpandableItem<AppGroup>,
        bindingAdapterPosition: Int,
        data: AppGroup
    ) {
        binding.appGroupItemTitleText.text = data.title
        binding.appGroupItemTitleText.setCompoundDrawablesWithIntrinsicBounds(
            0,
            0,
            if (item.isExpanded) R.drawable.ic_arrow_up else R.drawable.ic_arrow_down,
            0
        )
    }
}
