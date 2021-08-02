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
import androidx.paging.LoadState
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.RecyclerView
import com.github.panpf.assemblyadapter.recycler.*
import com.github.panpf.assemblyadapter.recycler.divider.Decorate
import com.github.panpf.assemblyadapter.sample.base.BaseBindingFragment
import com.github.panpf.assemblyadapter.sample.databinding.FragmentRecyclerDividerHorizontalBinding
import com.github.panpf.assemblyadapter.sample.item.AppGridHorItemFactory
import com.github.panpf.assemblyadapter.sample.item.AppsOverviewHorizontalItemFactory
import com.github.panpf.assemblyadapter.sample.item.ListSeparatorHorizontalItemFactory
import com.github.panpf.assemblyadapter.sample.item.LoadStateHorizontalItemFactory
import com.github.panpf.assemblyadapter.sample.vm.PinyinFlatAppsViewModel
import com.github.panpf.tools4a.dimen.ktx.dp2px

class RecyclerGridDividerHorFragment :
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
            AssemblySingleDataRecyclerAdapter(
                AppsOverviewHorizontalItemFactory(
                    requireActivity(),
                    true
                )
            )
        val recyclerAdapter = AssemblyRecyclerAdapter<Any>(
            listOf(
                AppGridHorItemFactory(requireActivity()),
                ListSeparatorHorizontalItemFactory(requireActivity(), true)
            )
        )
        val footerLoadStateAdapter =
            AssemblySingleDataRecyclerAdapter(LoadStateHorizontalItemFactory(requireActivity()))
        binding.recyclerDividerHorizontalRecycler.apply {
            adapter = ConcatAdapter(appsOverviewAdapter, recyclerAdapter, footerLoadStateAdapter)
            layoutManager = AssemblyGridLayoutManager(
                requireContext(), 3, RecyclerView.HORIZONTAL, false,
                mapOf(
                    AppsOverviewHorizontalItemFactory::class to ItemSpan.fullSpan(),
                    ListSeparatorHorizontalItemFactory::class to ItemSpan.fullSpan(),
                    LoadStateHorizontalItemFactory::class to ItemSpan.fullSpan()
                )
            )
            addItemDecoration(
                AssemblyRecyclerGridDividerItemDecoration.Builder(requireContext()).apply {
                    val inset = 0.dp2px
                    val size = 5.dp2px
                    divider(Decorate.color(0x88FF0000.toInt(), size, inset, inset))
                    firstAndLastDivider(Decorate.color(0xFFFF0000.toInt(), size, inset, inset))
                    side(Decorate.color(0x880000FF.toInt(), size, inset, inset))
                    firstAndLastSide(Decorate.color(0xFF0000FF.toInt(), size, inset, inset))
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