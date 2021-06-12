package com.github.panpf.assemblyadapter.sample.ui.list

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.updateLayoutParams
import com.github.panpf.assemblyadapter.BindingAssemblyItemFactory
import com.github.panpf.assemblyadapter.sample.base.StickyItemFactory
import com.github.panpf.assemblyadapter.sample.bean.PinyinGroup
import com.github.panpf.assemblyadapter.sample.databinding.ItemPinyinGroupBinding

open class PinyinGroupItemFactory(private val hideStartMargin: Boolean = false) :
    BindingAssemblyItemFactory<PinyinGroup, ItemPinyinGroupBinding>(),
    StickyItemFactory {

    override fun match(data: Any?): Boolean {
        return data is PinyinGroup
    }

    override fun createViewBinding(
        inflater: LayoutInflater, parent: ViewGroup
    ): ItemPinyinGroupBinding {
        return ItemPinyinGroupBinding.inflate(inflater, parent, false)
    }

    override fun initItem(
        context: Context,
        binding: ItemPinyinGroupBinding,
        item: BindingAssemblyItem<PinyinGroup, ItemPinyinGroupBinding>
    ) {
        super.initItem(context, binding, item)
        if (hideStartMargin) {
            binding.pinyinGroupItemTitleText.updateLayoutParams<ViewGroup.MarginLayoutParams> {
                leftMargin = 0
                rightMargin = 0
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    marginStart = 0
                    marginEnd = 0
                }
            }
        }
    }

    override fun bindData(
        context: Context, binding: ItemPinyinGroupBinding,
        item: BindingAssemblyItem<PinyinGroup, ItemPinyinGroupBinding>,
        position: Int, data: PinyinGroup?
    ) {
        binding.pinyinGroupItemTitleText.text = data?.title
    }
}
