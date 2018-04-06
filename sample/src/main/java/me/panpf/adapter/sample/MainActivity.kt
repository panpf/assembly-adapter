package me.panpf.adapter.sample

import android.os.Build
import android.os.Bundle
import android.support.v4.app.FragmentActivity
import android.support.v4.view.ViewPager
import me.panpf.adapter.pager.FragmentArrayPagerAdapter
import me.panpf.adapter.sample.fragment.*
import me.panpf.pagerid.PagerIndicator

class MainActivity : FragmentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        val viewPager = findViewById<ViewPager>(R.id.pager_mainActivity_content)
        viewPager.adapter = FragmentArrayPagerAdapter(supportFragmentManager, arrayOf(
                ListViewFragment(),
                RecyclerViewFragment(),
                GridRecyclerViewFragment(),
                ExpandableListViewFragment(),
                SpinnerFragment(),
                ViewPagerFragment(),
                PagerAdapterFragment()))

        val tabStrip = findViewById<PagerIndicator>(R.id.tabStrip_mainActivity_tabs)
        tabStrip.setViewPager(viewPager)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            actionBar!!.elevation = 0f
        }
    }
}