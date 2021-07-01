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
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.paging.LoadState
import com.github.panpf.assemblyadapter.Placeholder
import com.github.panpf.assemblyadapter.ViewItemFactory
import com.github.panpf.assemblyadapter.list.concat.expandable.ConcatExpandableListAdapter
import com.github.panpf.assemblyadapter.list.expandable.AssemblyExpandableListAdapter
import com.github.panpf.assemblyadapter.list.expandable.AssemblySingleDataExpandableListAdapter
import com.github.panpf.assemblyadapter.sample.R
import com.github.panpf.assemblyadapter.sample.base.BaseBindingFragment
import com.github.panpf.assemblyadapter.sample.bean.AppGroup
import com.github.panpf.assemblyadapter.sample.bean.AppInfo
import com.github.panpf.assemblyadapter.sample.bean.AppsOverview
import com.github.panpf.assemblyadapter.sample.databinding.FragmentExpandableListBinding
import com.github.panpf.assemblyadapter.sample.item.*
import com.github.panpf.assemblyadapter.sample.vm.PinyinGroupAppsViewModel

class ExpandableListPlaceholderFragment : BaseBindingFragment<FragmentExpandableListBinding>() {

    private val viewModel by viewModels<PinyinGroupAppsViewModel>()
    private var registered = false

    override fun createViewBinding(
        inflater: LayoutInflater, parent: ViewGroup?
    ): FragmentExpandableListBinding {
        return FragmentExpandableListBinding.inflate(inflater, parent, false)
    }

    override fun onInitData(binding: FragmentExpandableListBinding, savedInstanceState: Bundle?) {
        val appsOverviewAdapter = AssemblySingleDataExpandableListAdapter<AppsOverview, Any>(
            AppsOverviewItemFactory(requireActivity())
        )
        val listAdapter = AssemblyExpandableListAdapter<AppGroup?, AppInfo>(
            listOf(AppGroupItemFactory(), AppItemFactory(requireActivity())),
            ViewItemFactory(Placeholder::class.java, R.layout.item_app_group_placeholder),
            arrayOfNulls<AppGroup?>(100).toList()
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

        registered = false
        binding.expandableListRefreshLayout.setOnRefreshListener {
            if (!registered) {
                registered = true

                viewModel.loadingData.observe(viewLifecycleOwner) {
                    binding.expandableListRefreshLayout.isRefreshing = it == true
                }

                viewModel.appsOverviewData.observe(viewLifecycleOwner) {
                    appsOverviewAdapter.data = it
                }

                viewModel.pinyinGroupAppListData.observe(viewLifecycleOwner) {
                    listAdapter.setDataList(it)
                    footerLoadStateAdapter.data = LoadState.NotLoading(true)
                }
            }
            viewModel.refresh()
        }

        Toast.makeText(
            requireContext(),
            "Pull down to refresh to load real data",
            Toast.LENGTH_LONG
        ).show()
    }
}