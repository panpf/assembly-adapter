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
import com.github.panpf.assemblyadapter.list.expandable.AssemblyExpandableListAdapter
import com.github.panpf.assemblyadapter.list.expandable.AssemblySingleDataExpandableListAdapter
import com.github.panpf.assemblyadapter.list.expandable.ConcatExpandableListAdapter
import com.github.panpf.assemblyadapter.sample.base.ToolbarFragment
import com.github.panpf.assemblyadapter.sample.bean.AppGroup
import com.github.panpf.assemblyadapter.sample.bean.AppInfo
import com.github.panpf.assemblyadapter.sample.bean.AppsOverview
import com.github.panpf.assemblyadapter.sample.databinding.FragmentExpandableListBinding
import com.github.panpf.assemblyadapter.sample.item.AppChildItemFactory
import com.github.panpf.assemblyadapter.sample.item.AppGroupItemFactory
import com.github.panpf.assemblyadapter.sample.item.AppsOverviewItemFactory
import com.github.panpf.assemblyadapter.sample.item.LoadStateItemFactory
import com.github.panpf.assemblyadapter.sample.vm.PinyinGroupAppsViewModel

class ExpandableListFragment : ToolbarFragment<FragmentExpandableListBinding>() {

    private val args: ExpandableListFragmentArgs by navArgs()

    private val viewModel by viewModels<PinyinGroupAppsViewModel>()

    override fun createViewBinding(
        inflater: LayoutInflater, parent: ViewGroup?
    ): FragmentExpandableListBinding {
        return FragmentExpandableListBinding.inflate(inflater, parent, false)
    }

    override fun onInitData(
        toolbar: Toolbar,
        binding: FragmentExpandableListBinding,
        savedInstanceState: Bundle?
    ) {
        toolbar.title = args.title
        toolbar.subtitle = args.subtitle

        val appsOverviewAdapter = AssemblySingleDataExpandableListAdapter<AppsOverview, Any>(
            AppsOverviewItemFactory(requireActivity())
        )
        val listAdapter = AssemblyExpandableListAdapter<AppGroup, AppInfo>(
            listOf(AppGroupItemFactory(), AppChildItemFactory(requireActivity()))
        )
        val footerLoadStateAdapter = AssemblySingleDataExpandableListAdapter<LoadState, Any>(
            LoadStateItemFactory(requireActivity())
        )
        binding.expandableListList.setAdapter(
            ConcatExpandableListAdapter(
                appsOverviewAdapter,
                listAdapter,
                footerLoadStateAdapter
            )
        )

        binding.expandableListRefreshLayout.setOnRefreshListener {
            viewModel.refresh()
        }

        viewModel.loadingData.observe(viewLifecycleOwner) {
            binding.expandableListRefreshLayout.isRefreshing = it == true
        }

        viewModel.appsOverviewData.observe(viewLifecycleOwner) {
            appsOverviewAdapter.data = it
        }

        viewModel.pinyinGroupAppListData.observe(viewLifecycleOwner) {
            listAdapter.submitList(it)
            footerLoadStateAdapter.data = LoadState.NotLoading(true)
        }
    }
}