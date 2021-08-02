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
import com.github.panpf.assemblyadapter.recycler.*
import com.github.panpf.assemblyadapter.recycler.divider.Decorate
import com.github.panpf.assemblyadapter.sample.base.BaseBindingFragment
import com.github.panpf.assemblyadapter.sample.databinding.FragmentRecyclerDividerVerticalBinding
import com.github.panpf.assemblyadapter.sample.item.AppGridItemFactory
import com.github.panpf.assemblyadapter.sample.item.AppsOverviewItemFactory
import com.github.panpf.assemblyadapter.sample.item.ListSeparatorItemFactory
import com.github.panpf.assemblyadapter.sample.item.LoadStateItemFactory
import com.github.panpf.assemblyadapter.sample.vm.PinyinFlatAppsViewModel
import com.github.panpf.tools4a.dimen.ktx.dp2px

class RecyclerGridDividerVerFragment :
    BaseBindingFragment<FragmentRecyclerDividerVerticalBinding>() {

    private val viewModel by viewModels<PinyinFlatAppsViewModel>()

    override fun createViewBinding(
        inflater: LayoutInflater, parent: ViewGroup?
    ): FragmentRecyclerDividerVerticalBinding {
        return FragmentRecyclerDividerVerticalBinding.inflate(inflater, parent, false)
    }

    override fun onInitData(
        binding: FragmentRecyclerDividerVerticalBinding,
        savedInstanceState: Bundle?
    ) {
        val appsOverviewAdapter =
            AssemblySingleDataRecyclerAdapter(AppsOverviewItemFactory(requireActivity(), true))
        val recyclerAdapter = AssemblyRecyclerAdapter<Any>(
            listOf(
                AppGridItemFactory(requireActivity()),
                ListSeparatorItemFactory(requireActivity(), true)
            )
        )
        val footerLoadStateAdapter =
            AssemblySingleDataRecyclerAdapter(LoadStateItemFactory(requireActivity()))
        binding.recyclerDividerVerticalRecycler.apply {
            adapter = ConcatAdapter(appsOverviewAdapter, recyclerAdapter, footerLoadStateAdapter)
            layoutManager = AssemblyGridLayoutManager(
                requireContext(), 3,
                mapOf(
                    AppsOverviewItemFactory::class to ItemSpan.fullSpan(),
                    ListSeparatorItemFactory::class to ItemSpan.fullSpan(),
                    LoadStateItemFactory::class to ItemSpan.fullSpan()
                )
            )
            addItemDecoration(
                AssemblyRecyclerGridDividerItemDecoration.Builder(requireContext()).apply {
                    val smallInset = 2.dp2px
                    val bigInset = 4.dp2px
                    val size = 5.dp2px
                    divider(
                        Decorate.color(
                            0x88FF0000.toInt(),
                            size,
                            insetStart = smallInset,
                            insetTop = bigInset,
                            insetEnd = smallInset,
                            insetBottom = bigInset,
                        )
                    )
                    firstAndLastDivider(
                        Decorate.color(
                            0xFFFF0000.toInt(),
                            size,
                            insetStart = smallInset,
                            insetTop = bigInset,
                            insetEnd = smallInset,
                            insetBottom = bigInset,
                        )
                    )
                    side(
                        Decorate.color(
                            0x880000FF.toInt(),
                            size,
                            insetStart = bigInset,
                            insetTop = smallInset,
                            insetEnd = bigInset,
                            insetBottom = smallInset,
                        )
                    )
                    firstAndLastSide(
                        Decorate.color(
                            0xFF0000FF.toInt(),
                            size,
                            insetStart = bigInset,
                            insetTop = smallInset,
                            insetEnd = bigInset,
                            insetBottom = smallInset,
                        )
                    )
                    personaliseDivider(
                        ListSeparatorItemFactory::class,
                        Decorate.space(
                            size,
                            insetStart = smallInset,
                            insetTop = bigInset,
                            insetEnd = smallInset,
                            insetBottom = bigInset,
                        )
                    )
                    personaliseFirstAndLastSide(
                        ListSeparatorItemFactory::class,
                        Decorate.space(
                            size,
                            insetStart = bigInset,
                            insetTop = smallInset,
                            insetEnd = bigInset,
                            insetBottom = smallInset,
                        )
                    )
                    personaliseDivider(
                        AppsOverviewItemFactory::class,
                        Decorate.color(
                            0x8800FF00.toInt(),
                            size,
                            insetStart = smallInset,
                            insetTop = bigInset,
                            insetEnd = smallInset,
                            insetBottom = bigInset,
                        )
                    )
                    personaliseFirstAndLastSide(
                        AppsOverviewItemFactory::class,
                        Decorate.color(
                            0xFF00FF00.toInt(),
                            size,
                            insetStart = bigInset,
                            insetTop = smallInset,
                            insetEnd = bigInset,
                            insetBottom = smallInset,
                        )
                    )
                    disableFirstAndLastSide(LoadStateItemFactory::class)
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