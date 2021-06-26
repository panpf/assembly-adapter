package com.github.panpf.assemblyadapter.sample.item.pager

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.panpf.assemblyadapter.pager.AssemblyPagerItemFactory
import com.github.panpf.assemblyadapter.sample.bean.PinyinGroup
import com.github.panpf.assemblyadapter.sample.databinding.FragmentPinyinGroupBinding

class PinyinGroupPagerItemFactory : AssemblyPagerItemFactory<PinyinGroup>() {

    override fun match(data: Any): Boolean {
        return data is PinyinGroup
    }

    override fun createView(
        context: Context, container: ViewGroup, position: Int, data: PinyinGroup
    ): View =
        FragmentPinyinGroupBinding.inflate(LayoutInflater.from(context), container, false).apply {
            pinyinGroupGroupNameText.text = data.title
            pinyinGroupAppCountText.text = data.childSize.toString()
        }.root
}