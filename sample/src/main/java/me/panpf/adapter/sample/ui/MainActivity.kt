package me.panpf.adapter.sample.ui

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import kotlinx.android.synthetic.main.at_main.*
import me.panpf.adapter.pager.FragmentArrayPagerAdapter
import me.panpf.adapter.sample.R

class MainActivity : FragmentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.at_main)

        mainAt_pager.adapter = FragmentArrayPagerAdapter(supportFragmentManager, arrayOf(
                ListViewFragment(),
                RecyclerViewFragment(),
                GridRecyclerViewFragment(),
                ExpandableListViewFragment(),
                SpinnerFragment(),
                ViewPagerFragment(),
                PagerAdapterFragment()))

        mainAt_indicator.setViewPager(mainAt_pager)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            actionBar?.elevation = 0f
        }
    }
}