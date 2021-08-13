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
package com.github.panpf.assemblyadapter.sample.ui.recycler

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.ConcatAdapter
import com.github.panpf.assemblyadapter.recycler.AssemblyGridLayoutManager
import com.github.panpf.assemblyadapter.recycler.AssemblySingleDataRecyclerAdapter
import com.github.panpf.assemblyadapter.recycler.ItemSpan
import com.github.panpf.assemblyadapter.recycler.addAssemblyGridDividerItemDecoration
import com.github.panpf.assemblyadapter.recycler.divider.Decorate
import com.github.panpf.assemblyadapter.recycler.paging.AssemblyPagingDataAdapter
import com.github.panpf.assemblyadapter.sample.base.BaseBindingFragment
import com.github.panpf.assemblyadapter.sample.base.MyLoadStateAdapter
import com.github.panpf.assemblyadapter.sample.base.sticky.AssemblyStickyItemDecoration
import com.github.panpf.assemblyadapter.sample.databinding.FragmentRecyclerBinding
import com.github.panpf.assemblyadapter.sample.item.AppCardGridItemFactory
import com.github.panpf.assemblyadapter.sample.item.AppsOverviewItemFactory
import com.github.panpf.assemblyadapter.sample.item.ListSeparatorItemFactory
import com.github.panpf.assemblyadapter.sample.item.LoadStateItemFactory
import com.github.panpf.assemblyadapter.sample.vm.PinyinFlatPagingAppsViewModel
import com.github.panpf.tools4a.dimen.ktx.dp2px
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class RecyclerGridPagingFragment : BaseBindingFragment<FragmentRecyclerBinding>() {

    private val viewModel by viewModels<PinyinFlatPagingAppsViewModel>()

    override fun createViewBinding(
        inflater: LayoutInflater, parent: ViewGroup?
    ): FragmentRecyclerBinding {
        return FragmentRecyclerBinding.inflate(inflater, parent, false)
    }

    override fun onInitData(binding: FragmentRecyclerBinding, savedInstanceState: Bundle?) {
        val appsOverviewAdapter =
            AssemblySingleDataRecyclerAdapter(AppsOverviewItemFactory(requireActivity()))
        val pagingDataAdapter = AssemblyPagingDataAdapter<Any>(
            listOf(
                AppCardGridItemFactory(requireActivity()),
                ListSeparatorItemFactory(requireActivity())
            )
        )
        binding.recyclerRecycler.apply {
            adapter = ConcatAdapter(
                appsOverviewAdapter,
                pagingDataAdapter.withLoadStateFooter(MyLoadStateAdapter(requireActivity()))
            )
            layoutManager = AssemblyGridLayoutManager(
                requireContext(), 3,
                mapOf(
                    AppsOverviewItemFactory::class to ItemSpan.fullSpan(),
                    ListSeparatorItemFactory::class to ItemSpan.fullSpan(),
                    LoadStateItemFactory::class to ItemSpan.fullSpan()
                )
            )
            addAssemblyGridDividerItemDecoration {
                divider(Decorate.space(20.dp2px)).showLastDivider()
                side(Decorate.space(20.dp2px)).showFirstAndLastSide()
                disableDivider(AppsOverviewItemFactory::class)
                disableFirstAndLastSide(AppsOverviewItemFactory::class)
                disableFirstAndLastSide(ListSeparatorItemFactory::class)
            }
            addItemDecoration(
                AssemblyStickyItemDecoration(
                    binding.recyclerStickyContainer, ListSeparatorItemFactory::class
                )
            )
        }
        binding.recyclerRefreshLayout.setOnRefreshListener {
            viewModel.refresh()
            pagingDataAdapter.refresh()
        }

        viewLifecycleOwner.lifecycleScope.launch {
            pagingDataAdapter.loadStateFlow.collect {
                binding.recyclerRefreshLayout.isRefreshing = it.refresh is LoadState.Loading
            }
        }

        viewModel.appsOverviewData.observe(viewLifecycleOwner) {
            appsOverviewAdapter.data = it
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.pinyinFlatAppListDataFlow.collect {
                pagingDataAdapter.submitData(it)
            }
        }
    }
}