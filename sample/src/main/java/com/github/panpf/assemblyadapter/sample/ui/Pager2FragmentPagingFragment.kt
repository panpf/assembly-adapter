package com.github.panpf.assemblyadapter.sample.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.viewpager2.widget.ViewPager2
import com.github.panpf.assemblyadapter.pager2.paging.AssemblyPagingFragmentStateAdapter
import com.github.panpf.assemblyadapter.recycler.paging.KeyDiffItemCallback
import com.github.panpf.assemblyadapter.sample.base.BaseBindingFragment
import com.github.panpf.assemblyadapter.sample.databinding.FragmentPager2Binding
import com.github.panpf.assemblyadapter.sample.ui.list.AppGroupFragmentItemFactory
import com.github.panpf.assemblyadapter.sample.ui.list.AppsOverviewFragmentItemFactory
import com.github.panpf.assemblyadapter.sample.ui.list.MyFragmentLoadStateAdapter
import com.github.panpf.assemblyadapter.sample.vm.OverviewInstalledAppPinyinGroupPagingViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class Pager2FragmentPagingFragment : BaseBindingFragment<FragmentPager2Binding>() {

    private val viewModel by viewModels<OverviewInstalledAppPinyinGroupPagingViewModel>()

    override fun createViewBinding(
        inflater: LayoutInflater, parent: ViewGroup?
    ): FragmentPager2Binding {
        return FragmentPager2Binding.inflate(inflater, parent, false)
    }

    override fun onInitData(binding: FragmentPager2Binding, savedInstanceState: Bundle?) {
        val pagingDataAdapter = AssemblyPagingFragmentStateAdapter(
            this, KeyDiffItemCallback(),
            listOf(AppGroupFragmentItemFactory(), AppsOverviewFragmentItemFactory())
        )
        binding.pager2Pager.adapter = pagingDataAdapter
        binding.pager2Pager.adapter = pagingDataAdapter.withLoadStateFooter(
            MyFragmentLoadStateAdapter(this)
        )

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.pinyinGroupAppListDataFlow.collect {
                pagingDataAdapter.submitData(it)
                updatePageNumber(binding)
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            pagingDataAdapter.loadStateFlow.collect {
                binding.pager2ProgressBar.isVisible = it.refresh is LoadState.Loading
                binding.pager2PageNumberText.isVisible = it.refresh !is LoadState.Loading
            }
        }

        binding.pager2Pager.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                updatePageNumber(binding)
            }
        })
    }

    @SuppressLint("SetTextI18n")
    private fun updatePageNumber(binding: FragmentPager2Binding) {
        val pager = binding.pager2Pager
        binding.pager2PageNumberText.text =
            "${pager.currentItem + 1}/${pager.adapter?.itemCount ?: 0}"
    }
}