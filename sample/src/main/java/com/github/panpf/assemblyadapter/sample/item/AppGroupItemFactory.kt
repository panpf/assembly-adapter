package com.github.panpf.assemblyadapter.sample.item

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import com.github.panpf.assemblyadapter.list.expandable.BindingAssemblyExpandableItemFactory
import com.github.panpf.assemblyadapter.sample.R
import com.github.panpf.assemblyadapter.sample.bean.AppGroup
import com.github.panpf.assemblyadapter.sample.databinding.ItemAppGroupBinding

open class AppGroupItemFactory :
    BindingAssemblyExpandableItemFactory<AppGroup, ItemAppGroupBinding>() {

    override fun match(data: Any?): Boolean {
        return data is AppGroup
    }

    override fun createViewBinding(
        inflater: LayoutInflater, parent: ViewGroup
    ): ItemAppGroupBinding {
        return ItemAppGroupBinding.inflate(inflater, parent, false)
    }

    override fun bindData(
        context: Context, binding: ItemAppGroupBinding,
        item: BindingAssemblyExpandableItem<AppGroup, ItemAppGroupBinding>,
        bindingAdapterPosition: Int, data: AppGroup?
    ) {
        binding.appGroupItemTitleText.text = data?.title
        binding.appGroupItemTitleText.setCompoundDrawablesWithIntrinsicBounds(
            0,
            0,
            if (item.isExpanded) R.drawable.ic_arrow_up else R.drawable.ic_arrow_down,
            0
        )
    }
}
