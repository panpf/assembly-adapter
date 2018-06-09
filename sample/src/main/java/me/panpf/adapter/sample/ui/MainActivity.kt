package me.panpf.adapter.sample.ui

import android.os.Build
import android.os.Bundle
import android.support.v4.app.FragmentActivity
import kotlinx.android.synthetic.main.activity_main.*
import me.panpf.adapter.pager.FragmentArrayPagerAdapter
import me.panpf.adapter.sample.R
import me.panpf.pagerid.PagerIndicator

class MainActivity : FragmentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        pager_mainActivity_content.adapter = FragmentArrayPagerAdapter(supportFragmentManager, arrayOf(
                ListViewFragment(),
                RecyclerViewFragment(),
                GridRecyclerViewFragment(),
                ExpandableListViewFragment(),
                SpinnerFragment(),
                ViewPagerFragment(),
                PagerAdapterFragment()))

        val tabStrip = findViewById<PagerIndicator>(R.id.tabStrip_mainActivity_tabs)
        tabStrip.setViewPager(pager_mainActivity_content)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            actionBar!!.elevation = 0f
        }
    }
}