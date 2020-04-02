package me.panpf.adapter.sample.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import me.panpf.adapter.pager.AssemblyPagerAdapter
import me.panpf.adapter.pager.PagerItemHolder
import me.panpf.adapter.sample.R
import me.panpf.adapter.sample.bean.Header
import me.panpf.adapter.sample.bean.Text
import me.panpf.adapter.sample.item.HeaderPagerItemFactory
import me.panpf.adapter.sample.item.ImagePagerItemFactory
import me.panpf.adapter.sample.item.TextPagerItemFactory

class ViewPagerPagerAdapterSampleFragment : BaseFragment() {
    var headerItemHolder: PagerItemHolder<Any>? = null
    var footerItemHolder: PagerItemHolder<Any>? = null

    override fun onUserVisibleChanged(isVisibleToUser: Boolean) {
        val attachActivity = activity
        if (isVisibleToUser && attachActivity is AppCompatActivity) {
            attachActivity.supportActionBar?.subtitle = "ViewPager - PagerAdapter"
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fm_pager, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val viewPager = view.findViewById(R.id.pagerFm_pager) as ViewPager

        val header = Header("我是小额头呀！\n你敢戳我，我就敢消失！哼！", "https://img.ivsky.com/img/tupian/pre/201909/19/oumei_meinv-008.jpg")
        val dataArray = arrayOf(
                "https://img.ivsky.com/img/tupian/pre/201910/17/yujia-013.jpg",
                "https://img.ivsky.com/img/tupian/pre/201910/17/yujia-016.jpg",
                "https://img.ivsky.com/img/tupian/pre/201910/17/yujia-012.jpg",
                "https://img.ivsky.com/img/tupian/pre/201909/19/oumei_meinv-005.jpg",
                Text("分割线又来了"),
                "https://img.ivsky.com/img/tupian/pre/201909/19/oumei_meinv-007.jpg"
        )
        val footer = Header("我是小尾巴呀！\n你敢戳我，我也敢消失！哼！", "https://img.ivsky.com/img/tupian/pre/201909/19/oumei_meinv-004.jpg")

        val adapter = AssemblyPagerAdapter(dataArray)

        headerItemHolder = adapter.addHeaderItem(HeaderPagerItemFactory().setOnItemClickListener{context, view, position, positionInPart, data ->
            headerItemHolder!!.isEnabled = false
            viewPager.adapter = null
            viewPager.adapter = adapter
        }, header)
        adapter.addItemFactory(ImagePagerItemFactory())
        adapter.addItemFactory(TextPagerItemFactory())

        footerItemHolder = adapter.addFooterItem(HeaderPagerItemFactory().setOnItemClickListener{context, view, position, positionInPart, data ->
            footerItemHolder!!.isEnabled = false
            viewPager.adapter = null
            viewPager.adapter = adapter
        }, footer)

        viewPager.adapter = adapter
    }
}