package com.github.panpf.assemblyadapter.sample.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.viewpager.widget.ViewPager
import com.github.panpf.assemblyadapter.pager.AssemblyPagerAdapter
import com.github.panpf.assemblyadapter.sample.base.BaseBindingFragment
import com.github.panpf.assemblyadapter.sample.bean.AppsOverview
import com.github.panpf.assemblyadapter.sample.databinding.FragmentPagerBinding
import com.github.panpf.assemblyadapter.sample.ui.list.AppsOverviewPagerItemFactory
import com.github.panpf.assemblyadapter.sample.ui.list.AppGroupPagerItemFactory
import com.github.panpf.assemblyadapter.sample.vm.InstalledAppPinyinGroupViewModel
import com.github.panpf.assemblyadapter.sample.vm.OverviewInstalledAppPinyinGroupViewModel

class PagerFragment : BaseBindingFragment<FragmentPagerBinding>() {

    private val viewModel by viewModels<OverviewInstalledAppPinyinGroupViewModel>()

    override fun createViewBinding(
        inflater: LayoutInflater, parent: ViewGroup?
    ): FragmentPagerBinding {
        return FragmentPagerBinding.inflate(inflater, parent, false)
    }

    override fun onInitData(binding: FragmentPagerBinding, savedInstanceState: Bundle?) {
        val listAdapter = AssemblyPagerAdapter<Any>(
            listOf(AppGroupPagerItemFactory(), AppsOverviewPagerItemFactory())
        )
        binding.pagerPager.adapter = listAdapter

        viewModel.pinyinGroupAppListData.observe(viewLifecycleOwner) {
            listAdapter.setDataList(it)
            updatePageNumber(binding)
        }

        binding.pagerPager.addOnPageChangeListener(object : ViewPager.SimpleOnPageChangeListener() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                updatePageNumber(binding)
            }
        })

        viewModel.loadingData.observe(viewLifecycleOwner) {
            binding.pagerProgressBar.isVisible = it == true
            binding.pagerPageNumberText.isVisible = it != true
        }
    }

    @SuppressLint("SetTextI18n")
    private fun updatePageNumber(binding: FragmentPagerBinding) {
        val pager = binding.pagerPager
        binding.pagerPageNumberText.text =
            "${pager.currentItem + 1}/${pager.adapter?.count ?: 0}"
    }
}