package com.github.panpf.assemblyadapter.sample.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.ConcatAdapter
import androidx.viewpager2.widget.ViewPager2
import com.github.panpf.assemblyadapter.pager2.AssemblyFragmentStateAdapter
import com.github.panpf.assemblyadapter.pager2.AssemblySingleDataFragmentStateAdapter
import com.github.panpf.assemblyadapter.sample.base.BaseBindingFragment
import com.github.panpf.assemblyadapter.sample.databinding.FragmentPager2Binding
import com.github.panpf.assemblyadapter.sample.ui.list.AppsFragmentItemFactory
import com.github.panpf.assemblyadapter.sample.ui.list.AppsOverviewFragmentItemFactory
import com.github.panpf.assemblyadapter.sample.ui.list.PinyinGroupFragmentItemFactory
import com.github.panpf.assemblyadapter.sample.vm.PinyinFlatChunkedAppsViewModel

class Pager2FragmentFragment : BaseBindingFragment<FragmentPager2Binding>() {

    private val viewModel by viewModels<PinyinFlatChunkedAppsViewModel>()

    override fun createViewBinding(
        inflater: LayoutInflater, parent: ViewGroup?
    ): FragmentPager2Binding {
        return FragmentPager2Binding.inflate(inflater, parent, false)
    }

    override fun onInitData(binding: FragmentPager2Binding, savedInstanceState: Bundle?) {
        val appsOverviewAdapter = AssemblySingleDataFragmentStateAdapter(
            this,
            AppsOverviewFragmentItemFactory()
        )
        val listAdapter = AssemblyFragmentStateAdapter<Any>(
            this,
            listOf(
                AppsFragmentItemFactory(),
                PinyinGroupFragmentItemFactory(),
                AppsOverviewFragmentItemFactory()
            )
        )
        binding.pager2Pager.adapter = ConcatAdapter(
            ConcatAdapter.Config.Builder()
                .setIsolateViewTypes(true)
                .setStableIdMode(ConcatAdapter.Config.StableIdMode.SHARED_STABLE_IDS)
                .build(),
            appsOverviewAdapter,
            listAdapter
        )

        viewModel.pinyinFlatChunkedAppListData.observe(viewLifecycleOwner) {
            listAdapter.setDataList(it)
            updatePageNumber(binding)
        }

        viewModel.appsOverviewData.observe(viewLifecycleOwner) {
            appsOverviewAdapter.data = it
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