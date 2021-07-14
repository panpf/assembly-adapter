package com.github.panpf.assemblyadapter3.compat.sample.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.github.panpf.assemblyadapter3.compat.sample.R
import com.github.panpf.assemblyadapter3.compat.sample.base.BaseBindingFragment
import com.github.panpf.assemblyadapter3.compat.sample.base.FragmentContainerActivity
import com.github.panpf.assemblyadapter3.compat.sample.databinding.FragmentMainBinding

class MainFragment : BaseBindingFragment<FragmentMainBinding>() {

    override fun createViewBinding(
        inflater: LayoutInflater,
        parent: ViewGroup?
    ): FragmentMainBinding = FragmentMainBinding.inflate(inflater, parent, false)

    override fun onInitData(binding: FragmentMainBinding, savedInstanceState: Bundle?) {
        binding.mainNavigation.setNavigationItemSelectedListener {
            val itemTitle = it.title.toString()
            val title = itemTitle.substringBefore(" - ", itemTitle)
            val subTitle = itemTitle.substringAfter(" - ", "")
            val fragment = when (it.itemId) {
                R.id.menuNavRecyclerLinear -> RecyclerLinearFragment()
                R.id.menuNavRecyclerGrid -> RecyclerGridFragment()
                R.id.menuNavRecyclerPaging -> RecyclerPagingFragment()
                R.id.menuNavList -> ListFragment()
                R.id.menuNavListExpandable -> ExpandableListFragment()
                else -> throw IllegalArgumentException()
            }
            startActivity(
                FragmentContainerActivity.createIntent(
                    requireActivity(), title, subTitle, fragment
                )
            )
            return@setNavigationItemSelectedListener true
        }
    }
}