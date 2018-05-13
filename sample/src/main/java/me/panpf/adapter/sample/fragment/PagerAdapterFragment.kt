package me.panpf.adapter.sample.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import me.panpf.adapter.pager.AssemblyPagerAdapter
import me.panpf.adapter.pager.PagerItemHolder
import me.panpf.adapter.sample.R
import me.panpf.adapter.sample.bean.Header
import me.panpf.adapter.sample.bean.Text
import me.panpf.adapter.sample.itemfactory.HeaderPagerItemFactory
import me.panpf.adapter.sample.itemfactory.ImagePagerItemFactory
import me.panpf.adapter.sample.itemfactory.TextPagerItemFactory

class PagerAdapterFragment : Fragment() {
    var headerItemHolder: PagerItemHolder<Any>? = null
    var footerItemHolder: PagerItemHolder<Any>? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_view_pager, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val viewPager = view.findViewById(R.id.pager_viewPagerFragment_content) as ViewPager

        val header = Header("我是小额头呀！\n你敢戳我，我就敢消失！哼！", "http://www.bz55.com/uploads/allimg/150605/139-150605153433-50.jpg")
        val dataArray = arrayOf(
                "http://img4q.duitang.com/uploads/item/201506/12/20150612095354_5w3sj.jpeg",
                "http://img5q.duitang.com/uploads/blog/201412/11/20141211160750_hFrss.jpeg",
                "http://pic.yesky.com/uploadImages/2015/147/00/0VC1P4UAR2V6.jpg",
                "http://img5q.duitang.com/uploads/item/201504/10/20150410H1528_F4rEf.jpeg",
                Text("分割线又来了"),
                "http://img4.duitang.com/uploads/item/201509/19/20150919212952_femTB.thumb.700_0.jpeg"
        )
        val footer = Header("我是小尾巴呀！\n你敢戳我，我也敢消失！哼！", "http://www.bz55.com/uploads/allimg/150720/139-150H0110925.jpg")

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