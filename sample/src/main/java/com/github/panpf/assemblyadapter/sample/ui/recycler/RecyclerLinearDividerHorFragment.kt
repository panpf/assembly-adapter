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
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import androidx.paging.LoadState
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.panpf.assemblyadapter.recycler.AssemblyRecyclerAdapter
import com.github.panpf.assemblyadapter.recycler.AssemblySingleDataRecyclerAdapter
import com.github.panpf.assemblyadapter.recycler.divider.Divider
import com.github.panpf.assemblyadapter.recycler.divider.Insets
import com.github.panpf.assemblyadapter.recycler.divider.internal.DividerSize
import com.github.panpf.assemblyadapter.recycler.divider.newAssemblyLinearDividerItemDecoration
import com.github.panpf.assemblyadapter.sample.R
import com.github.panpf.assemblyadapter.sample.base.ToolbarFragment
import com.github.panpf.assemblyadapter.sample.bean.LinearDividerParams
import com.github.panpf.assemblyadapter.sample.databinding.FragmentRecyclerDividerHorBinding
import com.github.panpf.assemblyadapter.sample.item.AppStrokeHorItemFactory
import com.github.panpf.assemblyadapter.sample.item.AppsOverviewHorItemFactory
import com.github.panpf.assemblyadapter.sample.item.ListSeparatorHorItemFactory
import com.github.panpf.assemblyadapter.sample.item.LoadStateHorItemFactory
import com.github.panpf.assemblyadapter.sample.vm.AppListViewModel
import com.github.panpf.assemblyadapter.sample.vm.AppsOverviewViewModel
import com.github.panpf.assemblyadapter.sample.vm.LinearDividerParamsViewModel
import com.github.panpf.assemblyadapter.sample.vm.PinyinFlatAppListViewModel
import com.github.panpf.tools4a.dimen.ktx.dp2px
import com.github.panpf.tools4a.display.ktx.getScreenHeight
import com.github.panpf.tools4a.display.ktx.getScreenWidth

class RecyclerLinearDividerHorFragment :
    ToolbarFragment<FragmentRecyclerDividerHorBinding>() {

    private val args: RecyclerLinearDividerHorFragmentArgs by navArgs()
    private val appListViewModel by viewModels<AppListViewModel>()
    private val appsOverviewViewModel by viewModels<AppsOverviewViewModel>()
    private val pinyinFlatAppListViewModel by viewModels<PinyinFlatAppListViewModel>()
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
        initMenu(toolbar)
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
                addItemDecoration(buildDividerItemDecoration(dividerParams))
                adapter?.notifyDataSetChanged() // The item width needs to be recalculated and refreshed to take effect
            }
        }

        appsOverviewViewModel.appsOverviewData.observe(viewLifecycleOwner) {
            appsOverviewAdapter.data = it
        }

        val observer = Observer<List<Any>> {
            recyclerAdapter.submitList(it)
            footerLoadStateAdapter.data = LoadState.NotLoading(true)
        }
        dividerParamsViewMode.dividerParamsData.observe(viewLifecycleOwner) { dividerParams ->
            dividerParams ?: return@observe
            pinyinFlatAppListViewModel.pinyinFlatAppListData.removeObserver(observer)
            appListViewModel.appListData.removeObserver(observer)

            if (dividerParams.isShowListSeparator) {
                pinyinFlatAppListViewModel.pinyinFlatAppListData.observe(
                    viewLifecycleOwner,
                    observer
                )
            } else {
                appListViewModel.appListData.observe(viewLifecycleOwner, observer)
            }
        }
    }

    private fun buildDividerItemDecoration(dividerParams: LinearDividerParams): RecyclerView.ItemDecoration {
        val insets = Insets.allOf(dividerParams.dividerInsetsSize)
        val dividerSize = if (dividerParams.isShortDivider) {
            DividerSize.clearly(dividerParams.dividerSize, requireContext().getScreenHeight() / 2)
        } else {
            DividerSize.vague(dividerParams.dividerSize)
        }
        val sideDividerSize = if (dividerParams.isShortDivider) {
            DividerSize.clearly(20.dp2px, dividerParams.dividerSize)
        } else {
            DividerSize.vague(dividerParams.dividerSize)
        }
        return requireContext().newAssemblyLinearDividerItemDecoration {
            disableDefaultDivider()
            if (dividerParams.isShowDivider) {
                divider(Divider.colorResWithSize(R.color.divider, dividerSize, insets)) {
                    personaliseByItemFactoryClass(
                        ListSeparatorHorItemFactory::class,
                        Divider.colorResWithSize(R.color.divider_personalise, dividerSize, insets)
                    )
                    disableByItemFactoryClass(AppsOverviewHorItemFactory::class)
                }
            }
            if (dividerParams.isShowHeaderDivider) {
                headerDivider(
                    Divider.colorResWithSize(R.color.divider_header, dividerSize, insets)
                )
            }
            if (dividerParams.isShowFooterDivider) {
                footerDivider(
                    Divider.colorResWithSize(R.color.divider_header, dividerSize, insets)
                )
            }

            if (dividerParams.isShowSideHeaderDivider) {
                sideHeaderDivider(
                    Divider.colorResWithSize(R.color.sideDivider_header, sideDividerSize, insets)
                ) {
                    personaliseByItemFactoryClass(
                        ListSeparatorHorItemFactory::class,
                        Divider.colorResWithSize(
                            R.color.sideDivider_personalise, sideDividerSize, insets
                        )
                    )
                    disableByItemFactoryClass(AppsOverviewHorItemFactory::class)
                }
            }

            if (dividerParams.isShowSideFooterDivider) {
                sideFooterDivider(
                    Divider.colorResWithSize(R.color.sideDivider_header, sideDividerSize, insets)
                ) {
                    personaliseByItemFactoryClass(
                        ListSeparatorHorItemFactory::class,
                        Divider.colorResWithSize(
                            R.color.sideDivider_personalise, sideDividerSize, insets
                        )
                    )
                    disableByItemFactoryClass(AppsOverviewHorItemFactory::class)
                }
            }
        }
    }

    private fun initMenu(toolbar: Toolbar) {
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
                    if (dividerParams.isShortDivider) "Long Divider" else "Short Divider"
                ).apply {
                    setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER)
                    setOnMenuItemClickListener {
                        dividerParams.isShortDivider = !dividerParams.isShortDivider
                        dividerParamsViewMode.dividerParamsData.postValue(dividerParams)
                        true
                    }
                }

                add(
                    2, 8, 8,
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

                add(
                    3, 9, 9,
                    if (dividerParams.isShowListSeparator)
                        "Hide List Separator" else "Show List Separator"
                ).apply {
                    setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER)
                    setOnMenuItemClickListener {
                        dividerParams.isShowListSeparator = !dividerParams.isShowListSeparator
                        dividerParamsViewMode.dividerParamsData.postValue(dividerParams)
                        true
                    }
                }
            }
        }
    }
}