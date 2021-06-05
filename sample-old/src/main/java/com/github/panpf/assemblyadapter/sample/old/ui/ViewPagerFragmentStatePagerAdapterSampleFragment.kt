package com.github.panpf.assemblyadapter.sample.old.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.github.panpf.assemblyadapter.sample.old.base.BaseBindingFragment
import com.github.panpf.assemblyadapter.sample.old.bean.Header
import com.github.panpf.assemblyadapter.sample.old.bean.Text
import com.github.panpf.assemblyadapter.sample.old.databinding.FmPagerBinding
import com.github.panpf.assemblyadapter.sample.old.ui.list.HeaderFragmentItemFactory
import com.github.panpf.assemblyadapter.sample.old.ui.list.ImageFragmentItemFactory
import com.github.panpf.assemblyadapter.sample.old.ui.list.TextFragmentItemFactory
import me.panpf.adapter.pager.AssemblyFragmentStatePagerAdapter

class ViewPagerFragmentStatePagerAdapterSampleFragment : BaseBindingFragment<FmPagerBinding>() {

    override fun createViewBinding(inflater: LayoutInflater, parent: ViewGroup?): FmPagerBinding {
        return FmPagerBinding.inflate(inflater, parent, false)
    }

    override fun onInitData(binding: FmPagerBinding, savedInstanceState: Bundle?) {
        val dataArray = arrayOf(
            "https://img.ivsky.com/img/tupian/pre/201909/19/oumei_meinv-007.jpg",
            "https://img.ivsky.com/img/tupian/pre/201909/19/oumei_meinv-005.jpg",
            Text("华丽的分割线"),
            "https://img.ivsky.com/img/tupian/pre/201910/17/yujia-012.jpg",
            "https://img.ivsky.com/img/tupian/pre/201910/17/yujia-016.jpg"
        )
        binding.pagerFmPager.adapter = AssemblyFragmentStatePagerAdapter(
            childFragmentManager,
            AssemblyFragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT, dataArray
        ).apply {
            addHeaderItem(
                HeaderFragmentItemFactory(),
                Header(
                    "我是小额头呀！",
                    "https://img.ivsky.com/img/tupian/pre/201909/19/oumei_meinv-004.jpg"
                )
            )
            addItemFactory(ImageFragmentItemFactory())
            addItemFactory(TextFragmentItemFactory())
            addFooterItem(
                HeaderFragmentItemFactory(),
                Header("我是小尾巴呀！", "https://img.ivsky.com/img/tupian/pre/201910/17/yujia-013.jpg")
            )
        }
    }
}

