package com.github.panpf.assemblyadapter3.compat.sample.item

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.updateLayoutParams
import com.github.panpf.assemblyadapter3.compat.CompatAssemblyItem
import com.github.panpf.assemblyadapter3.compat.CompatAssemblyItemFactory
import com.github.panpf.assemblyadapter3.compat.sample.bean.ListSeparator
import com.github.panpf.assemblyadapter3.compat.sample.databinding.ItemListSeparatorBinding

class ListSeparatorItem(
    val binding: ItemListSeparatorBinding,
    private val hideStartMargin: Boolean = false
) :
    CompatAssemblyItem<ListSeparator>(binding.root) {

    override fun onConfigViews(context: Context) {
        super.onConfigViews(context)
        if (hideStartMargin) {
            binding.listSeparatorItemTitleText.updateLayoutParams<ViewGroup.MarginLayoutParams> {
                leftMargin = 0
                rightMargin = 0
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    marginStart = 0
                    marginEnd = 0
                }
            }
        }
    }

    override fun onSetData(position: Int, data: ListSeparator?) {
        data ?: return
        binding.listSeparatorItemTitleText.text = data.title
    }

    class Factory(
        private val hideStartMargin: Boolean = false
    ) : CompatAssemblyItemFactory<ListSeparator>() {

        override fun match(data: Any?): Boolean {
            return data is ListSeparator
        }

        override fun createAssemblyItem(parent: ViewGroup): ListSeparatorItem {
            return ListSeparatorItem(
                ItemListSeparatorBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                ),
                hideStartMargin
            )
        }
    }
}
