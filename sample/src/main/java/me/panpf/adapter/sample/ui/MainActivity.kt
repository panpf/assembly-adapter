package me.panpf.adapter.sample.ui

import android.os.Bundle
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.at_main.*
import me.panpf.adapter.sample.R

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.at_main)

        setSupportActionBar(mainAt_toolbar)

        val toggle = ActionBarDrawerToggle(this, mainAt_drawer, mainAt_toolbar, R.string.drawer_open_desc, R.string.drawer_close_desc)
        mainAt_drawer.addDrawerListener(toggle)
        toggle.syncState()

        mainAt_navigation.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.nav_recyclerLinear -> supportFragmentManager.beginTransaction().replace(R.id.mainAt_frame_container, RecyclerLinearLayoutSampleFragment()).commit()
                R.id.nav_recyclerGrid -> supportFragmentManager.beginTransaction().replace(R.id.mainAt_frame_container, RecyclerGridLayoutSampleFragment()).commit()
                R.id.nav_recyclerPaged -> supportFragmentManager.beginTransaction().replace(R.id.mainAt_frame_container, RecyclerPagedSampleFragment()).commit()
                R.id.nav_fragmentStatePagerAdapter -> supportFragmentManager.beginTransaction().replace(R.id.mainAt_frame_container, ViewPagerFragmentStatePagerAdapterSampleFragment()).commit()
                R.id.nav_pagerAdapter -> supportFragmentManager.beginTransaction().replace(R.id.mainAt_frame_container, ViewPagerPagerAdapterSampleFragment()).commit()
                R.id.nav_list -> supportFragmentManager.beginTransaction().replace(R.id.mainAt_frame_container, ListViewFragment()).commit()
                R.id.nav_expandableList -> supportFragmentManager.beginTransaction().replace(R.id.mainAt_frame_container, ExpandableListViewFragment()).commit()
                R.id.nav_spinner -> supportFragmentManager.beginTransaction().replace(R.id.mainAt_frame_container, SpinnerFragment()).commit()
                else -> throw IllegalArgumentException()
            }
            mainAt_drawer.closeDrawers()
            return@setNavigationItemSelectedListener true
        }

        supportFragmentManager.beginTransaction().replace(R.id.mainAt_frame_container, RecyclerLinearLayoutSampleFragment()).commit()
    }
}