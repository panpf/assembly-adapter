package com.github.panpf.assemblyadapter3.compat.sample.item

import android.view.LayoutInflater
import android.view.ViewGroup
import com.github.panpf.assemblyadapter3.compat.CompatAssemblyItem
import com.github.panpf.assemblyadapter3.compat.CompatAssemblyItemFactory
import com.github.panpf.assemblyadapter3.compat.sample.R
import com.github.panpf.assemblyadapter3.compat.sample.bean.AppGroup
import com.github.panpf.assemblyadapter3.compat.sample.databinding.ItemAppGroupBinding

class AppGroupItem(val binding: ItemAppGroupBinding) : CompatAssemblyItem<AppGroup>(binding.root) {

    override fun onSetData(position: Int, data: AppGroup?) {
        binding.appGroupItemTitleText.apply {
            text = data?.title
            val icon = if (isExpanded) {
                R.drawable.ic_arrow_up
            } else {
                R.drawable.ic_arrow_down
            }
            setCompoundDrawablesWithIntrinsicBounds(0, 0, icon, 0)
        }
    }

    class Factory : CompatAssemblyItemFactory<AppGroup>() {

        override fun match(data: Any?): Boolean {
            return data is AppGroup
        }

        override fun createAssemblyItem(parent: ViewGroup): AppGroupItem {
            return AppGroupItem(
                ItemAppGroupBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        }
    }
}
