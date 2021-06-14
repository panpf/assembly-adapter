package com.github.panpf.assemblyadapter.sample.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.viewpager2.widget.ViewPager2
import com.github.panpf.assemblyadapter.pager2.AssemblyFragmentStateAdapter
import com.github.panpf.assemblyadapter.sample.base.BaseBindingFragment
import com.github.panpf.assemblyadapter.sample.bean.AppsOverview
import com.github.panpf.assemblyadapter.sample.databinding.FragmentPager2Binding
import com.github.panpf.assemblyadapter.sample.ui.list.AppGroupFragmentItemFactory
import com.github.panpf.assemblyadapter.sample.ui.list.AppsOverviewFragmentItemFactory
import com.github.panpf.assemblyadapter.sample.vm.InstalledAppPinyinGroupViewModel
import com.github.panpf.assemblyadapter.sample.vm.OverviewInstalledAppPinyinGroupViewModel

class Pager2FragmentFragment : BaseBindingFragment<FragmentPager2Binding>() {

    private val viewModel by viewModels<OverviewInstalledAppPinyinGroupViewModel>()

    override fun createViewBinding(
        inflater: LayoutInflater, parent: ViewGroup?
    ): FragmentPager2Binding {
        return FragmentPager2Binding.inflate(inflater, parent, false)
    }

    override fun onInitData(binding: FragmentPager2Binding, savedInstanceState: Bundle?) {
        val listAdapter = AssemblyFragmentStateAdapter<Any>(
            this,
            listOf(AppGroupFragmentItemFactory(), AppsOverviewFragmentItemFactory())
        )
        binding.pager2Pager.adapter = listAdapter

        viewModel.pinyinGroupAppListData.observe(viewLifecycleOwner) {
            listAdapter.setDataList(it)
            updatePageNumber(binding)
        }

        binding.pager2Pager.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                updatePageNumber(binding)
            }
        })

        viewModel.loadingData.observe(viewLifecycleOwner) {
            binding.pager2ProgressBar.isVisible = it == true
            binding.pager2PageNumberText.isVisible = it != true
        }
    }

    @SuppressLint("SetTextI18n")
    private fun updatePageNumber(binding: FragmentPager2Binding) {
        val pager = binding.pager2Pager
        binding.pager2PageNumberText.text =
            "${pager.currentItem + 1}/${pager.adapter?.itemCount ?: 0}"
    }
}