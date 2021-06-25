package com.github.panpf.assemblyadapter.sample.ui.pager

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.paging.LoadState
import androidx.viewpager.widget.ViewPager
import com.github.panpf.assemblyadapter.pager.AssemblyPagerAdapter
import com.github.panpf.assemblyadapter.pager.AssemblySingleDataPagerAdapter
import com.github.panpf.assemblyadapter.sample.base.BaseBindingFragment
import com.github.panpf.assemblyadapter.pager.concat.ConcatPagerAdapter
import com.github.panpf.assemblyadapter.sample.databinding.FragmentPagerBinding
import com.github.panpf.assemblyadapter.sample.item.pager.*
import com.github.panpf.assemblyadapter.sample.vm.PagerPinyinGroupAppsViewModel

class PagerViewFragment : BaseBindingFragment<FragmentPagerBinding>() {

    private val viewModel by viewModels<PagerPinyinGroupAppsViewModel>()

    override fun createViewBinding(
        inflater: LayoutInflater, parent: ViewGroup?
    ): FragmentPagerBinding {
        return FragmentPagerBinding.inflate(inflater, parent, false)
    }

    override fun onInitData(binding: FragmentPagerBinding, savedInstanceState: Bundle?) {
        val appsOverviewAdapter = AssemblySingleDataPagerAdapter(AppsOverviewPagerItemFactory())
        val listAdapter = AssemblyPagerAdapter<Any>(
            listOf(AppGroupPagerItemFactory(requireActivity()))
        )
        val footerLoadStateAdapter = AssemblySingleDataPagerAdapter(LoadStatePagerItemFactory())
        binding.pagerPager.apply {
            adapter = ConcatPagerAdapter(appsOverviewAdapter, listAdapter, footerLoadStateAdapter)
            addOnPageChangeListener(object : ViewPager.SimpleOnPageChangeListener() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    updatePageNumber(binding)
                }
            })
        }

        viewModel.loadingData.observe(viewLifecycleOwner) {
            binding.pagerProgressBar.isVisible = it == true
            binding.pagerPageNumberText.isVisible = it != true
        }

        viewModel.appsOverviewData.observe(viewLifecycleOwner) {
            appsOverviewAdapter.data = it
            updatePageNumber(binding)
        }

        viewModel.pinyinGroupAppListData.observe(viewLifecycleOwner) {
            listAdapter.setDataList(it)
            footerLoadStateAdapter.data = LoadState.NotLoading(true)
            updatePageNumber(binding)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun updatePageNumber(binding: FragmentPagerBinding) {
        val pager = binding.pagerPager
        binding.pagerPageNumberText.text =
            "${pager.currentItem + 1}/${pager.adapter?.count ?: 0}"
    }
}
