package me.panpf.adapter.sample.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import me.panpf.adapter.pager.AssemblyFragmentStatePagerAdapter
import me.panpf.adapter.sample.bean.Header
import me.panpf.adapter.sample.bean.Text
import me.panpf.adapter.sample.databinding.FmPagerBinding
import me.panpf.adapter.sample.item.HeaderFragmentItemFactory
import me.panpf.adapter.sample.item.ImageFragmentItemFactory
import me.panpf.adapter.sample.item.TextFragmentItemFactory

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

    override fun onResume() {
        super.onResume()
        (activity as AppCompatActivity?)?.supportActionBar?.subtitle =
            "ViewPager - FragmentStatePagerAdapter"
    }
}

