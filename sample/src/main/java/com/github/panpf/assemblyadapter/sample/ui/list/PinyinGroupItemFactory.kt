package com.github.panpf.assemblyadapter.sample.ui.list

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import com.github.panpf.assemblyadapter.BindingAssemblyItemFactory
import com.github.panpf.assemblyadapter.sample.base.StickyAssemblyRecyclerAdapter
import com.github.panpf.assemblyadapter.sample.bean.PinyinGroup
import com.github.panpf.assemblyadapter.sample.databinding.ItemPinyinGroupBinding

open class PinyinGroupItemFactory :
    BindingAssemblyItemFactory<PinyinGroup, ItemPinyinGroupBinding>(),
    StickyAssemblyRecyclerAdapter.StickyItemFactory {

    override fun match(data: Any?): Boolean {
        return data is PinyinGroup
    }

    override fun createViewBinding(
        inflater: LayoutInflater, parent: ViewGroup
    ): ItemPinyinGroupBinding {
        return ItemPinyinGroupBinding.inflate(inflater, parent, false)
    }

    override fun bindData(
        context: Context, binding: ItemPinyinGroupBinding, position: Int, data: PinyinGroup?
    ) {
        binding.pinyinGroupItemTitleText.text = data?.title
    }
}
