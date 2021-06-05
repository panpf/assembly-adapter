package com.github.panpf.assemblyadapter.sample.old.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.github.panpf.assemblyadapter.sample.old.base.BaseBindingFragment
import com.github.panpf.assemblyadapter.sample.old.bean.Header
import com.github.panpf.assemblyadapter.sample.old.bean.Text
import com.github.panpf.assemblyadapter.sample.old.databinding.FmPagerBinding
import com.github.panpf.assemblyadapter.sample.old.ui.list.HeaderPagerItemFactory
import com.github.panpf.assemblyadapter.sample.old.ui.list.ImagePagerItemFactory
import com.github.panpf.assemblyadapter.sample.old.ui.list.TextPagerItemFactory
import me.panpf.adapter.pager.AssemblyPagerAdapter

class ViewPagerPagerAdapterSampleFragment : BaseBindingFragment<FmPagerBinding>() {

    override fun createViewBinding(inflater: LayoutInflater, parent: ViewGroup?): FmPagerBinding {
        return FmPagerBinding.inflate(inflater, parent, false)
    }

    override fun onInitData(binding: FmPagerBinding, savedInstanceState: Bundle?) {
        val dataArray = arrayOf(
            "https://img.ivsky.com/img/tupian/pre/201910/17/yujia-013.jpg",
            "https://img.ivsky.com/img/tupian/pre/201910/17/yujia-016.jpg",
            "https://img.ivsky.com/img/tupian/pre/201910/17/yujia-012.jpg",
            "https://img.ivsky.com/img/tupian/pre/201909/19/oumei_meinv-005.jpg",
            Text("分割线又来了"),
            "https://img.ivsky.com/img/tupian/pre/201909/19/oumei_meinv-007.jpg"
        )
        binding.pagerFmPager.adapter = AssemblyPagerAdapter(dataArray).apply {
            addHeaderItem(
                HeaderPagerItemFactory().setOnItemClickListener { _, _, _, _, _ ->
                    this.setHeaderItemEnabled(0, false)
                    binding.pagerFmPager.adapter = null
                    binding.pagerFmPager.adapter = this
                },
                Header(
                    "我是小额头呀！\n你敢戳我，我就敢消失！哼！",
                    "https://img.ivsky.com/img/tupian/pre/201909/19/oumei_meinv-008.jpg"
                )
            )

            addItemFactory(ImagePagerItemFactory())
            addItemFactory(TextPagerItemFactory())

            addFooterItem(
                HeaderPagerItemFactory().setOnItemClickListener { _, _, _, _, _ ->
                    this.setFooterItemEnabled(0, false)
                    binding.pagerFmPager.adapter = this
                },
                Header(
                    "我是小尾巴呀！\n你敢戳我，我也敢消失！哼！",
                    "https://img.ivsky.com/img/tupian/pre/201909/19/oumei_meinv-004.jpg"
                )
            )
        }
    }
}