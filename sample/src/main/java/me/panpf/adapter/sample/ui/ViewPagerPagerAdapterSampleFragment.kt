package me.panpf.adapter.sample.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import me.panpf.adapter.pager.AssemblyPagerAdapter
import me.panpf.adapter.sample.R
import me.panpf.adapter.sample.bean.Header
import me.panpf.adapter.sample.bean.Text
import me.panpf.adapter.sample.item.HeaderPagerItemFactory
import me.panpf.adapter.sample.item.ImagePagerItemFactory
import me.panpf.adapter.sample.item.TextPagerItemFactory

class ViewPagerPagerAdapterSampleFragment : BaseFragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fm_pager, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val viewPager = view.findViewById(R.id.pagerFm_pager) as ViewPager

        val dataArray = arrayOf(
                "https://img.ivsky.com/img/tupian/pre/201910/17/yujia-013.jpg",
                "https://img.ivsky.com/img/tupian/pre/201910/17/yujia-016.jpg",
                "https://img.ivsky.com/img/tupian/pre/201910/17/yujia-012.jpg",
                "https://img.ivsky.com/img/tupian/pre/201909/19/oumei_meinv-005.jpg",
                Text("分割线又来了"),
                "https://img.ivsky.com/img/tupian/pre/201909/19/oumei_meinv-007.jpg"
        )
        viewPager.adapter = AssemblyPagerAdapter(dataArray).apply {
            addHeaderItem(HeaderPagerItemFactory().setOnItemClickListener { _, _, _, _, _ ->
                this.setHeaderItemEnabled(0, false)
                viewPager.adapter = null
                viewPager.adapter = this
            }, Header("我是小额头呀！\n你敢戳我，我就敢消失！哼！", "https://img.ivsky.com/img/tupian/pre/201909/19/oumei_meinv-008.jpg"))

            addItemFactory(ImagePagerItemFactory())
            addItemFactory(TextPagerItemFactory())

            addFooterItem(HeaderPagerItemFactory().setOnItemClickListener { _, _, _, _, _ ->
                this.setFooterItemEnabled(0, false)
                viewPager.adapter = this
            }, Header("我是小尾巴呀！\n你敢戳我，我也敢消失！哼！", "https://img.ivsky.com/img/tupian/pre/201909/19/oumei_meinv-004.jpg"))
        }
    }

    override fun onResume() {
        super.onResume()
        (activity as AppCompatActivity?)?.supportActionBar?.subtitle = "ViewPager - PagerAdapter"
    }
}