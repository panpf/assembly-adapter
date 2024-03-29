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
package com.github.panpf.assemblyadapter.sample.ui.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.paging.LoadState
import com.github.panpf.assemblyadapter.list.AssemblyListAdapter
import com.github.panpf.assemblyadapter.list.AssemblySingleDataListAdapter
import com.github.panpf.assemblyadapter.list.ConcatListAdapter
import com.github.panpf.assemblyadapter.sample.base.ToolbarFragment
import com.github.panpf.assemblyadapter.sample.databinding.FragmentListBinding
import com.github.panpf.assemblyadapter.sample.item.AppItemFactory
import com.github.panpf.assemblyadapter.sample.item.AppsOverviewItemFactory
import com.github.panpf.assemblyadapter.sample.item.ListSeparatorItemFactory
import com.github.panpf.assemblyadapter.sample.item.LoadStateItemFactory
import com.github.panpf.assemblyadapter.sample.vm.AppsOverviewViewModel
import com.github.panpf.assemblyadapter.sample.vm.PinyinFlatAppListViewModel

class ListFragment : ToolbarFragment<FragmentListBinding>() {

    private val args: ListFragmentArgs by navArgs()

    private val appsOverviewViewModel by viewModels<AppsOverviewViewModel>()
    private val pinyinFlatAppListViewModel by viewModels<PinyinFlatAppListViewModel>()

    override fun createViewBinding(
        inflater: LayoutInflater, parent: ViewGroup?
    ): FragmentListBinding {
        return FragmentListBinding.inflate(inflater, parent, false)
    }

    override fun onInitData(
        toolbar: Toolbar,
        binding: FragmentListBinding,
        savedInstanceState: Bundle?
    ) {
        toolbar.title = args.title
        toolbar.subtitle = args.subtitle

        val appsOverviewAdapter =
            AssemblySingleDataListAdapter(AppsOverviewItemFactory(requireActivity()))
        val listAdapter = AssemblyListAdapter<Any>(
            listOf(AppItemFactory(requireActivity()), ListSeparatorItemFactory(requireActivity()))
        )
        val footerLoadStateAdapter =
            AssemblySingleDataListAdapter(LoadStateItemFactory(requireActivity()))
        binding.listList.adapter =
            ConcatListAdapter(appsOverviewAdapter, listAdapter, footerLoadStateAdapter)

        binding.listRefreshLayout.setOnRefreshListener {
            pinyinFlatAppListViewModel.refresh()
        }

        pinyinFlatAppListViewModel.loadingData.observe(viewLifecycleOwner) {
            binding.listRefreshLayout.isRefreshing = it == true
        }

        appsOverviewViewModel.appsOverviewData.observe(viewLifecycleOwner) {
            appsOverviewAdapter.data = it
        }

        pinyinFlatAppListViewModel.pinyinFlatAppListData.observe(viewLifecycleOwner) {
            listAdapter.submitList(it)
            footerLoadStateAdapter.data = LoadState.NotLoading(true)
        }
    }
}