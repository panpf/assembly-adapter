package com.github.panpf.assemblyadapter.sample.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.FragmentPagerAdapter
import androidx.fragment.app.viewModels
import androidx.viewpager.widget.ViewPager
import com.github.panpf.assemblyadapter.pager.AssemblyFragmentPagerAdapter
import com.github.panpf.assemblyadapter.sample.base.BaseBindingFragment
import com.github.panpf.assemblyadapter.sample.databinding.FragmentPagerBinding
import com.github.panpf.assemblyadapter.sample.ui.list.AppsFragmentItemFactory
import com.github.panpf.assemblyadapter.sample.ui.list.AppsOverviewFragmentItemFactory
import com.github.panpf.assemblyadapter.sample.ui.list.PinyinGroupFragmentItemFactory
import com.github.panpf.assemblyadapter.sample.vm.PinyinFlatChunkedAppsViewModel

class PagerFragmentFragment : BaseBindingFragment<FragmentPagerBinding>() {

    private val viewModel by viewModels<PinyinFlatChunkedAppsViewModel>()

    override fun createViewBinding(
        inflater: LayoutInflater, parent: ViewGroup?
    ): FragmentPagerBinding {
        return FragmentPagerBinding.inflate(inflater, parent, false)
    }

    override fun onInitData(binding: FragmentPagerBinding, savedInstanceState: Bundle?) {
        val listAdapter = AssemblyFragmentPagerAdapter<Any>(
            childFragmentManager,
            FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT,
            listOf(
                AppsFragmentItemFactory(),
                PinyinGroupFragmentItemFactory(),
                AppsOverviewFragmentItemFactory()
            )
        )
        binding.pagerPager.adapter = listAdapter

        viewModel.pinyinFlatChunkedAppListData.observe(viewLifecycleOwner) {
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