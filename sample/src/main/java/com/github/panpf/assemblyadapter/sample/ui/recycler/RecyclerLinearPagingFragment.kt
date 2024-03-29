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
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.paging.LoadState
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.panpf.assemblyadapter.recycler.AssemblySingleDataRecyclerAdapter
import com.github.panpf.assemblyadapter.recycler.paging.AssemblyPagingDataAdapter
import com.github.panpf.assemblyadapter.sample.base.MyLoadStateAdapter
import com.github.panpf.assemblyadapter.sample.base.ToolbarFragment
import com.github.panpf.assemblyadapter.sample.databinding.FragmentRecyclerBinding
import com.github.panpf.assemblyadapter.sample.item.AppItemFactory
import com.github.panpf.assemblyadapter.sample.item.AppsOverviewItemFactory
import com.github.panpf.assemblyadapter.sample.item.ListSeparatorItemFactory
import com.github.panpf.assemblyadapter.sample.vm.AppsOverviewViewModel
import com.github.panpf.assemblyadapter.sample.vm.PinyinFlatPagingAppListViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class RecyclerLinearPagingFragment : ToolbarFragment<FragmentRecyclerBinding>() {

    private val args: RecyclerLinearPagingFragmentArgs by navArgs()

    private val appsOverviewViewModel by viewModels<AppsOverviewViewModel>()
    private val pinyinFlatPagingAppListViewModel by viewModels<PinyinFlatPagingAppListViewModel>()

    override fun createViewBinding(
        inflater: LayoutInflater, parent: ViewGroup?
    ): FragmentRecyclerBinding {
        return FragmentRecyclerBinding.inflate(inflater, parent, false)
    }

    override fun onInitData(
        toolbar: Toolbar,
        binding: FragmentRecyclerBinding,
        savedInstanceState: Bundle?
    ) {
        toolbar.title = args.title
        toolbar.subtitle = args.subtitle

        val appsOverviewAdapter =
            AssemblySingleDataRecyclerAdapter(AppsOverviewItemFactory(requireActivity()))
        val pagingDataAdapter = AssemblyPagingDataAdapter<Any>(
            listOf(
                AppItemFactory(requireActivity()),
                ListSeparatorItemFactory(requireActivity())
            )
        )
        binding.recyclerRecycler.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = ConcatAdapter(
                appsOverviewAdapter, pagingDataAdapter.withLoadStateFooter(
                    MyLoadStateAdapter(requireActivity())
                )
            )
        }
        binding.recyclerRefreshLayout.setOnRefreshListener {
            pagingDataAdapter.refresh()
        }

        viewLifecycleOwner.lifecycleScope.launch {
            pagingDataAdapter.loadStateFlow.collect {
                binding.recyclerRefreshLayout.isRefreshing = it.refresh is LoadState.Loading
            }
        }

        appsOverviewViewModel.appsOverviewData.observe(viewLifecycleOwner) {
            appsOverviewAdapter.data = it
        }

        viewLifecycleOwner.lifecycleScope.launch {
            pinyinFlatPagingAppListViewModel.pinyinFlatAppListDataFlow.collect {
                pagingDataAdapter.submitData(it)
            }
        }
    }
}