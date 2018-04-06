package me.panpf.adapter.sample.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import me.panpf.adapter.pager.AssemblyFragmentStatePagerAdapter
import me.panpf.adapter.sample.R
import me.panpf.adapter.sample.bean.Header
import me.panpf.adapter.sample.bean.Text
import me.panpf.adapter.sample.itemfactory.HeaderFragmentItemFactory
import me.panpf.adapter.sample.itemfactory.ImageFragmentItemFactory
import me.panpf.adapter.sample.itemfactory.TextFragmentItemFactory
import me.panpf.adapter.sample.bindView

class ViewPagerFragment : Fragment() {
    val viewPager: ViewPager by bindView(R.id.pager_viewPagerFragment_content)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_view_pager, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val header = Header("我是小额头呀！", "http://img4.imgtn.bdimg.com/it/u=16705507,1328875785&fm=21&gp=0.jpg")
        val dataArray = arrayOf(
                "http://news.yzz.cn/public/images/100829/68_184435_2_lit.jpg",
                "http://image-qzone.mamaquan.mama.cn/upload/2015/03/20/9d4e2fda0e904bcca07a_w300X405_w196X264.jpeg",
                "http://img1.imgtn.bdimg.com/it/u=2055412405,1351161078&fm=21&gp=0.jpg",
                Text("华丽的分割线"),
                "http://image.xinmin.cn/2012/03/01/20120301080259557027.jpg",
                "http://img3.cache.netease.com/photo/0026/2013-06-06/90MP0B4N43AJ0026.jpg",
                "http://img3.imgtn.bdimg.com/it/u=533822629,3189843728&fm=21&gp=0.jpg",
                "http://www.ycmhz.com.cn/jiahe/UploadFiles_8568/201110/2011101817440022.jpg"
        )
        val footer = Header("我是小尾巴呀！", "http://img2.imgtn.bdimg.com/it/u=4104440447,1888517305&fm=21&gp=0.jpg")

        val adapter = AssemblyFragmentStatePagerAdapter(childFragmentManager, dataArray)
        adapter.addHeaderItem(HeaderFragmentItemFactory(), header)
        adapter.addItemFactory(ImageFragmentItemFactory())
        adapter.addItemFactory(TextFragmentItemFactory())
        adapter.addFooterItem(HeaderFragmentItemFactory(), footer)
        viewPager.adapter = adapter
    }
}
