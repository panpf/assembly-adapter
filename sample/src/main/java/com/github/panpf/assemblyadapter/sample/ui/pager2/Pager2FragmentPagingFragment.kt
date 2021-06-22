package com.github.panpf.assemblyadapter.sample.ui.pager2

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.ConcatAdapter
import androidx.viewpager2.widget.ViewPager2
import com.github.panpf.assemblyadapter.pager2.AssemblySingleDataFragmentStateAdapter
import com.github.panpf.assemblyadapter.pager2.paging.AssemblyPagingFragmentStateAdapter
import com.github.panpf.assemblyadapter.recycler.paging.KeyDiffItemCallback
import com.github.panpf.assemblyadapter.sample.base.BaseBindingFragment
import com.github.panpf.assemblyadapter.sample.databinding.FragmentPager2Binding
import com.github.panpf.assemblyadapter.sample.item.pager.AppsFragmentItemFactory
import com.github.panpf.assemblyadapter.sample.item.pager.AppsOverviewFragmentItemFactory
import com.github.panpf.assemblyadapter.sample.base.MyFragmentLoadStateAdapter
import com.github.panpf.assemblyadapter.sample.item.pager.PinyinGroupFragmentItemFactory
import com.github.panpf.assemblyadapter.sample.vm.PinyinFlatChunkedPagingAppsViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class Pager2FragmentPagingFragment : BaseBindingFragment<FragmentPager2Binding>() {

    private val viewModel by viewModels<PinyinFlatChunkedPagingAppsViewModel>()

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
        val pagingDataAdapter = AssemblyPagingFragmentStateAdapter(
            this, KeyDiffItemCallback(),
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
            pagingDataAdapter.withLoadStateFooter(
                MyFragmentLoadStateAdapter(this)
            )
        )

        viewLifecycleOwner.lifecycleScope.launch {
            pagingDataAdapter.loadStateFlow.collect {
                binding.pager2ProgressBar.isVisible = it.refresh is LoadState.Loading
                binding.pager2PageNumberText.isVisible = it.refresh !is LoadState.Loading
            }
        }

        viewModel.appsOverviewData.observe(viewLifecycleOwner) {
            appsOverviewAdapter.data = it
            updatePageNumber(binding)
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.pinyinFlatChunkedAppListDataFlow.collect {
                pagingDataAdapter.submitData(it)
                updatePageNumber(binding)
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