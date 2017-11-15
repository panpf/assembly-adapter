package me.xiaopan.assemblyadaptersample.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import me.xiaopan.assemblyadapter.AssemblyPagerAdapter
import me.xiaopan.assemblyadapter.FixedPagerItemInfo
import me.xiaopan.assemblyadaptersample.R
import me.xiaopan.assemblyadaptersample.bean.Header
import me.xiaopan.assemblyadaptersample.bean.Text
import me.xiaopan.assemblyadaptersample.itemfactory.HeaderPagerItemFactory
import me.xiaopan.assemblyadaptersample.itemfactory.ImagePagerItemFactory
import me.xiaopan.assemblyadaptersample.itemfactory.TextPagerItemFactory

class PagerAdapterFragment : Fragment() {
    var headerItemInfo: FixedPagerItemInfo? = null
    var footerItemInfo: FixedPagerItemInfo? = null

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_view_pager, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val viewPager = view!!.findViewById(R.id.pager_viewPagerFragment_content) as ViewPager

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

        headerItemInfo = adapter.addHeaderItem(HeaderPagerItemFactory(View.OnClickListener {
            headerItemInfo!!.isEnabled = false
            viewPager.adapter = null
            viewPager.adapter = adapter
        }), header)
        adapter.addItemFactory(ImagePagerItemFactory())
        adapter.addItemFactory(TextPagerItemFactory())

        footerItemInfo = adapter.addFooterItem(HeaderPagerItemFactory(View.OnClickListener {
            footerItemInfo!!.isEnabled = false
            viewPager.adapter = null
            viewPager.adapter = adapter
        }), footer)

        viewPager.adapter = adapter
    }
}