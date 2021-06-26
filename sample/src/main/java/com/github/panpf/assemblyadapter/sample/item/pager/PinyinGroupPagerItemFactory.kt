package com.github.panpf.assemblyadapter.sample.item.pager

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import com.github.panpf.assemblyadapter.pager.BindingPagerItemFactory
import com.github.panpf.assemblyadapter.sample.bean.PinyinGroup
import com.github.panpf.assemblyadapter.sample.databinding.FragmentPinyinGroupBinding

class PinyinGroupPagerItemFactory :
    BindingPagerItemFactory<PinyinGroup, FragmentPinyinGroupBinding>() {

    override fun match(data: Any): Boolean {
        return data is PinyinGroup
    }

    override fun createViewBinding(
        context: Context,
        inflater: LayoutInflater,
        parent: ViewGroup,
        position: Int,
        data: PinyinGroup
    ): FragmentPinyinGroupBinding =
        FragmentPinyinGroupBinding.inflate(inflater, parent, false).apply {
            pinyinGroupGroupNameText.text = data.title
            pinyinGroupAppCountText.text = data.childSize.toString()
        }
}