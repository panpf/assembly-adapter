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

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.paging.LoadState
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.panpf.assemblyadapter.recycler.AssemblyRecyclerAdapter
import com.github.panpf.assemblyadapter.recycler.AssemblyRecyclerLinearDividerItemDecoration
import com.github.panpf.assemblyadapter.recycler.AssemblySingleDataRecyclerAdapter
import com.github.panpf.assemblyadapter.recycler.divider.Decorate
import com.github.panpf.assemblyadapter.sample.base.BaseBindingFragment
import com.github.panpf.assemblyadapter.sample.databinding.FragmentRecyclerDividerHorizontalBinding
import com.github.panpf.assemblyadapter.sample.item.*
import com.github.panpf.assemblyadapter.sample.vm.PinyinFlatAppsViewModel
import com.github.panpf.tools4a.dimen.ktx.dp2px

class RecyclerLinearDividerHorFragment :
    BaseBindingFragment<FragmentRecyclerDividerHorizontalBinding>() {

    private val viewModel by viewModels<PinyinFlatAppsViewModel>()

    override fun createViewBinding(
        inflater: LayoutInflater, parent: ViewGroup?
    ): FragmentRecyclerDividerHorizontalBinding {
        return FragmentRecyclerDividerHorizontalBinding.inflate(inflater, parent, false)
    }

    override fun onInitData(
        binding: FragmentRecyclerDividerHorizontalBinding,
        savedInstanceState: Bundle?
    ) {
        val appsOverviewAdapter =
            AssemblySingleDataRecyclerAdapter(AppsOverviewHorizontalItemFactory(requireActivity()))
        val recyclerAdapter = AssemblyRecyclerAdapter<Any>(
            listOf(
                AppHorizontalItemFactory(requireActivity()),
                ListSeparatorHorizontalItemFactory(activity = requireActivity(), hideDivider = true)
            )
        )
        val footerLoadStateAdapter =
            AssemblySingleDataRecyclerAdapter(LoadStateHorizontalItemFactory(requireActivity()))
        binding.recyclerDividerHorizontalRecycler.apply {
            adapter = ConcatAdapter(appsOverviewAdapter, recyclerAdapter, footerLoadStateAdapter)
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            addItemDecoration(
                AssemblyRecyclerLinearDividerItemDecoration.Builder(requireContext()).apply {
                    divider(Decorate.color(0x33FF0000, 0.5f.dp2px, 20.dp2px, 20.dp2px))
                    firstAndLastDivider(Decorate.color(0xFFFF0000.toInt(), 2.dp2px))
                    personaliseDivider(
                        ListSeparatorHorizontalItemFactory::class,
                        Decorate.color(0x88FF0000.toInt(), 1.dp2px, 20.dp2px, 20.dp2px)
                    )
                    disableDivider(AppsOverviewHorizontalItemFactory::class)

                    startAndEndSide(Decorate.color(0xFF0000FF.toInt(), 2.dp2px))
                    personaliseStartAndEndSide(
                        ListSeparatorHorizontalItemFactory::class, Decorate.space(2.dp2px)
                    )
                    disableStartAndEndSide(AppsOverviewHorizontalItemFactory::class)
                    disableStartAndEndSide(LoadStateHorizontalItemFactory::class)
                }.build()
            )
        }

        viewModel.appsOverviewData.observe(viewLifecycleOwner) {
            appsOverviewAdapter.data = it
        }
        viewModel.pinyinFlatAppListData.observe(viewLifecycleOwner) {
            recyclerAdapter.submitDataList(it)
            footerLoadStateAdapter.data = LoadState.NotLoading(true)
        }
    }
}