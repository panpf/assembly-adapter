/*
 * Copyright (C) 2021 panpf <panpfpanpf@oulook.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
import com.github.panpf.assemblyadapter.pager2.paging.AssemblyPagingDataFragmentStateAdapter
import com.github.panpf.assemblyadapter.sample.base.BaseBindingFragment
import com.github.panpf.assemblyadapter.sample.base.MyFragmentLoadStateAdapter
import com.github.panpf.assemblyadapter.sample.databinding.FragmentPager2Binding
import com.github.panpf.assemblyadapter.sample.item.pager.AppGroupFragmentItemFactory
import com.github.panpf.assemblyadapter.sample.item.pager.AppsOverviewFragmentItemFactory
import com.github.panpf.assemblyadapter.sample.vm.PagerPinyinGroupPagingAppsViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class Pager2FragmentPagingFragment : BaseBindingFragment<FragmentPager2Binding>() {

    private val viewModel by viewModels<PagerPinyinGroupPagingAppsViewModel>()

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
        val pagingDataAdapter = AssemblyPagingDataFragmentStateAdapter<Any>(
            this, listOf(AppGroupFragmentItemFactory())
        )
        binding.pager2Pager.apply {
            adapter = ConcatAdapter(
                ConcatAdapter.Config.Builder()
                    .setIsolateViewTypes(true)
                    .setStableIdMode(ConcatAdapter.Config.StableIdMode.SHARED_STABLE_IDS)
                    .build(),
                appsOverviewAdapter,
                pagingDataAdapter.withLoadStateFooter(
                    MyFragmentLoadStateAdapter(this@Pager2FragmentPagingFragment)
                )
            )
            registerOnPageChangeCallback(object :
                ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    updatePageNumber(binding)
                }
            })
        }

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
    }

    @SuppressLint("SetTextI18n")
    private fun updatePageNumber(binding: FragmentPager2Binding) {
        val pager = binding.pager2Pager
        binding.pager2PageNumberText.text =
            "${pager.currentItem + 1}/${pager.adapter?.itemCount ?: 0}"
    }
}