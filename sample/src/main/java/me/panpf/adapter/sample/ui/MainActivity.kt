package me.panpf.adapter.sample.ui

import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import me.panpf.adapter.sample.R
import me.panpf.adapter.sample.databinding.AtMainBinding

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = AtMainBinding.inflate(LayoutInflater.from(this), null, false)
        setContentView(binding.root)

        setSupportActionBar(binding.mainAtToolbar)

        val toggle = ActionBarDrawerToggle(
            this,
            binding.mainAtDrawer,
            binding.mainAtToolbar,
            R.string.drawer_open_desc,
            R.string.drawer_close_desc
        )
        binding.mainAtDrawer.addDrawerListener(toggle)
        toggle.syncState()

        binding.mainAtNavigation.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.nav_recyclerLinear -> supportFragmentManager.beginTransaction()
                    .replace(R.id.mainAt_frame_container, RecyclerLinearLayoutSampleFragment())
                    .commit()
                R.id.nav_recyclerGrid -> supportFragmentManager.beginTransaction()
                    .replace(R.id.mainAt_frame_container, RecyclerGridLayoutSampleFragment())
                    .commit()
                R.id.nav_recyclerPaged -> supportFragmentManager.beginTransaction()
                    .replace(R.id.mainAt_frame_container, RecyclerPagedSampleFragment()).commit()
                R.id.nav_fragmentStatePagerAdapter -> supportFragmentManager.beginTransaction()
                    .replace(
                        R.id.mainAt_frame_container,
                        ViewPagerFragmentStatePagerAdapterSampleFragment()
                    ).commit()
                R.id.nav_pagerAdapter -> supportFragmentManager.beginTransaction()
                    .replace(R.id.mainAt_frame_container, ViewPagerPagerAdapterSampleFragment())
                    .commit()
                R.id.nav_list -> supportFragmentManager.beginTransaction()
                    .replace(R.id.mainAt_frame_container, ListViewFragment()).commit()
                R.id.nav_expandableList -> supportFragmentManager.beginTransaction()
                    .replace(R.id.mainAt_frame_container, ExpandableListViewFragment()).commit()
                R.id.nav_spinner -> supportFragmentManager.beginTransaction()
                    .replace(R.id.mainAt_frame_container, SpinnerFragment()).commit()
                else -> throw IllegalArgumentException()
            }
            binding.mainAtDrawer.closeDrawers()
            return@setNavigationItemSelectedListener true
        }

        supportFragmentManager.beginTransaction()
            .replace(R.id.mainAt_frame_container, RecyclerLinearLayoutSampleFragment()).commit()
    }
}