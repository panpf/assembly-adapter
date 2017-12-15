package me.panpf.adapter.sample

import android.os.Build
import android.os.Bundle
import android.support.v4.app.FragmentActivity
import android.support.v4.view.ViewPager
import me.panpf.adapter.FragmentArrayPagerAdapter
import me.panpf.adapter.sample.fragment.*
import me.xiaopan.psts.PagerSlidingTabStrip

class MainActivity : FragmentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        val viewPager = findViewById(R.id.pager_mainActivity_content) as ViewPager
        viewPager.adapter = FragmentArrayPagerAdapter(supportFragmentManager, arrayOf(
                ListViewFragment(),
                RecyclerViewFragment(),
                GridRecyclerViewFragment(),
                ExpandableListViewFragment(),
                SpinnerFragment(),
                ViewPagerFragment(),
                PagerAdapterFragment()))

        val tabStrip = findViewById(R.id.tabStrip_mainActivity_tabs) as PagerSlidingTabStrip
        tabStrip.setViewPager(viewPager)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            actionBar!!.elevation = 0f
        }
    }
}