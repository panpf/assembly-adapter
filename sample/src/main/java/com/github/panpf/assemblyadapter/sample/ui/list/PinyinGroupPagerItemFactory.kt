package com.github.panpf.assemblyadapter.sample.ui.list

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.fondesa.recyclerviewdivider.dividerBuilder
import com.github.panpf.assemblyadapter.pager.AssemblyPagerItemFactory
import com.github.panpf.assemblyadapter.recycler.AssemblyRecyclerAdapter
import com.github.panpf.assemblyadapter.sample.bean.PinyinGroup
import com.github.panpf.assemblyadapter.sample.databinding.FragmentPinyinGroupBinding
import com.github.panpf.tools4a.dimen.ktx.dp2px

class PinyinGroupPagerItemFactory : AssemblyPagerItemFactory<PinyinGroup>() {

    override fun match(data: Any?): Boolean {
        return data is PinyinGroup
    }

    override fun createView(
        context: Context, container: ViewGroup, position: Int, data: PinyinGroup?
    ): View =
        FragmentPinyinGroupBinding.inflate(LayoutInflater.from(context), container, false).apply {
            pinyinGroupGroupNameText.text = data?.title
            pinyinGroupAppCountText.text = data?.childSize?.toString()
        }.root
}