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

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.core.view.MenuCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.paging.LoadState
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.panpf.assemblyadapter.recycler.AssemblyRecyclerAdapter
import com.github.panpf.assemblyadapter.recycler.AssemblySingleDataRecyclerAdapter
import com.github.panpf.assemblyadapter.recycler.divider.Divider
import com.github.panpf.assemblyadapter.recycler.divider.Insets
import com.github.panpf.assemblyadapter.recycler.divider.addAssemblyLinearDividerItemDecoration
import com.github.panpf.assemblyadapter.sample.R
import com.github.panpf.assemblyadapter.sample.base.ToolbarFragment
import com.github.panpf.assemblyadapter.sample.databinding.FragmentRecyclerDividerHorBinding
import com.github.panpf.assemblyadapter.sample.item.*
import com.github.panpf.assemblyadapter.sample.vm.LinearDividerParamsViewModel
import com.github.panpf.assemblyadapter.sample.vm.PinyinFlatAppsViewModel

class RecyclerLinearDividerHorFragment :
    ToolbarFragment<FragmentRecyclerDividerHorBinding>() {

    private val args: RecyclerLinearDividerHorFragmentArgs by navArgs()
    private val viewModel by viewModels<PinyinFlatAppsViewModel>()
    private val dividerParamsViewMode by viewModels<LinearDividerParamsViewModel> {
        LinearDividerParamsViewModel.Factory(
            requireActivity().application,
            "RecyclerLinearDividerHorDividerParams"
        )
    }

    override fun createViewBinding(
        inflater: LayoutInflater, parent: ViewGroup?
    ): FragmentRecyclerDividerHorBinding {
        return FragmentRecyclerDividerHorBinding.inflate(inflater, parent, false)
    }

    override fun onInitViews(
        toolbar: Toolbar,
        binding: FragmentRecyclerDividerHorBinding,
        savedInstanceState: Bundle?
    ) {
        toolbar.menu.apply {
            MenuCompat.setGroupDividerEnabled(this, true)

            dividerParamsViewMode.dividerParamsData.observe(viewLifecycleOwner) { dividerParams ->
                clear()
                dividerParams ?: return@observe

                add(
                    0, 0, 0,
                    if (dividerParams.isShowDivider) "Hide Divider" else "Show Divider"
                ).apply {
                    setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER)
                    setOnMenuItemClickListener {
                        dividerParams.isShowDivider = !dividerParams.isShowDivider
                        dividerParamsViewMode.dividerParamsData.postValue(dividerParams)
                        true
                    }
                }

                add(
                    0, 1, 1,
                    if (dividerParams.isShowHeaderDivider)
                        "Hide Header Divider" else "Show Header Divider"
                ).apply {
                    setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER)
                    setOnMenuItemClickListener {
                        dividerParams.isShowHeaderDivider = !dividerParams.isShowHeaderDivider
                        dividerParamsViewMode.dividerParamsData.postValue(dividerParams)
                        true
                    }
                }

                add(
                    0, 2, 2,
                    if (dividerParams.isShowFooterDivider)
                        "Hide Footer Divider" else "Show Footer Divider"
                ).apply {
                    setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER)
                    setOnMenuItemClickListener {
                        dividerParams.isShowFooterDivider = !dividerParams.isShowFooterDivider
                        dividerParamsViewMode.dividerParamsData.postValue(dividerParams)
                        true
                    }
                }

                add(
                    1, 4, 4,
                    if (dividerParams.isShowSideHeaderDivider)
                        "Hide Side Header Divider" else "Show Side Header Divider"
                ).apply {
                    setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER)
                    setOnMenuItemClickListener {
                        dividerParams.isShowSideHeaderDivider =
                            !dividerParams.isShowSideHeaderDivider
                        dividerParamsViewMode.dividerParamsData.postValue(dividerParams)
                        true
                    }
                }

                add(
                    1, 5, 5,
                    if (dividerParams.isShowSideFooterDivider)
                        "Hide Side Footer Divider" else "Show Side Footer Divider"
                ).apply {
                    setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER)
                    setOnMenuItemClickListener {
                        dividerParams.isShowSideFooterDivider =
                            !dividerParams.isShowSideFooterDivider
                        dividerParamsViewMode.dividerParamsData.postValue(dividerParams)
                        true
                    }
                }

                add(
                    2, 6, 6,
                    if (dividerParams.isBigDivider) "Small Divider" else "Big Divider"
                ).apply {
                    setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER)
                    setOnMenuItemClickListener {
                        dividerParams.isBigDivider = !dividerParams.isBigDivider
                        dividerParamsViewMode.dividerParamsData.postValue(dividerParams)
                        true
                    }
                }

                add(
                    2, 7, 7,
                    if (dividerParams.isShowDividerInsets)
                        "Hide Divider Insets" else "Show Divider Insets"
                ).apply {
                    setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER)
                    setOnMenuItemClickListener {
                        dividerParams.isShowDividerInsets = !dividerParams.isShowDividerInsets
                        dividerParamsViewMode.dividerParamsData.postValue(dividerParams)
                        true
                    }
                }
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onInitData(
        toolbar: Toolbar,
        binding: FragmentRecyclerDividerHorBinding,
        savedInstanceState: Bundle?
    ) {
        toolbar.title = args.title
        toolbar.subtitle = args.subtitle

        val appsOverviewAdapter =
            AssemblySingleDataRecyclerAdapter(AppsOverviewHorItemFactory(requireActivity()))
        val recyclerAdapter = AssemblyRecyclerAdapter<Any>(
            listOf(
                AppStrokeHorItemFactory(
                    requireActivity(),
                    viewLifecycleOwner,
                    dividerParamsViewMode.dividerParamsData
                ),
                ListSeparatorHorItemFactory(requireActivity(), hideDivider = true)
            )
        )
        val footerLoadStateAdapter =
            AssemblySingleDataRecyclerAdapter(LoadStateHorItemFactory(requireActivity()))
        binding.recyclerDividerHorRecycler.apply {
            adapter = ConcatAdapter(appsOverviewAdapter, recyclerAdapter, footerLoadStateAdapter)
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            dividerParamsViewMode.dividerParamsData.observe(viewLifecycleOwner) { dividerParams ->
                dividerParams ?: return@observe
                if (itemDecorationCount > 0) {
                    removeItemDecorationAt(0)
                }
                val insets = Insets.allOf(dividerParams.dividerInsetsSize)
                val dividerSize = dividerParams.dividerSize
                addAssemblyLinearDividerItemDecoration {
                    disableDefaultDivider()
                    if (dividerParams.isShowDivider) {
                        divider(Divider.colorRes(R.color.divider, dividerSize, insets)) {
                            personaliseByItemFactoryClass(
                                ListSeparatorHorItemFactory::class,
                                Divider.colorRes(R.color.divider_personalise, dividerSize, insets)
                            )
                            disableByItemFactoryClass(AppsOverviewHorItemFactory::class)
                        }
                    }
                    if (dividerParams.isShowHeaderDivider) {
                        headerDivider(
                            Divider.colorRes(R.color.divider_header, dividerSize, insets)
                        )
                    }
                    if (dividerParams.isShowFooterDivider) {
                        footerDivider(
                            Divider.colorRes(R.color.divider_header, dividerSize, insets)
                        )
                    }

                    if (dividerParams.isShowSideHeaderDivider) {
                        sideHeaderDivider(
                            Divider.colorRes(R.color.sideDivider_header, dividerSize, insets)
                        ) {
                            personaliseByItemFactoryClass(
                                ListSeparatorHorItemFactory::class,
                                Divider.colorRes(
                                    R.color.sideDivider_personalise, dividerSize, insets
                                )
                            )
                            disableByItemFactoryClass(AppsOverviewHorItemFactory::class)
                        }
                    }

                    if (dividerParams.isShowSideFooterDivider) {
                        sideFooterDivider(
                            Divider.colorRes(R.color.sideDivider_header, dividerSize, insets)
                        ) {
                            personaliseByItemFactoryClass(
                                ListSeparatorHorItemFactory::class,
                                Divider.colorRes(
                                    R.color.sideDivider_personalise, dividerSize, insets
                                )
                            )
                            disableByItemFactoryClass(AppsOverviewHorItemFactory::class)
                        }
                    }
                }
                adapter?.notifyDataSetChanged() // The item width needs to be recalculated and refreshed to take effect
            }
        }

        viewModel.appsOverviewData.observe(viewLifecycleOwner) {
            appsOverviewAdapter.data = it
        }
        viewModel.pinyinFlatAppListData.observe(viewLifecycleOwner) {
            recyclerAdapter.submitList(it)
            footerLoadStateAdapter.data = LoadState.NotLoading(true)
        }
    }
}