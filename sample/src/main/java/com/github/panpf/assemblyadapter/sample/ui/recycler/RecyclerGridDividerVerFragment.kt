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
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.core.view.MenuCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import androidx.paging.LoadState
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.RecyclerView
import com.github.panpf.assemblyadapter.recycler.AssemblyRecyclerAdapter
import com.github.panpf.assemblyadapter.recycler.AssemblySingleDataRecyclerAdapter
import com.github.panpf.assemblyadapter.recycler.ItemSpan
import com.github.panpf.assemblyadapter.recycler.divider.Divider
import com.github.panpf.assemblyadapter.recycler.divider.Insets
import com.github.panpf.assemblyadapter.recycler.divider.newAssemblyGridDividerItemDecoration
import com.github.panpf.assemblyadapter.recycler.newAssemblyGridLayoutManager
import com.github.panpf.assemblyadapter.sample.R
import com.github.panpf.assemblyadapter.sample.base.ToolbarFragment
import com.github.panpf.assemblyadapter.sample.bean.AppsOverview
import com.github.panpf.assemblyadapter.sample.bean.GridDividerParams
import com.github.panpf.assemblyadapter.sample.databinding.FragmentRecyclerDividerVerBinding
import com.github.panpf.assemblyadapter.sample.item.AppGridStrokeItemFactory
import com.github.panpf.assemblyadapter.sample.item.AppsOverviewItemFactory
import com.github.panpf.assemblyadapter.sample.item.ListSeparatorItemFactory
import com.github.panpf.assemblyadapter.sample.item.LoadStateItemFactory
import com.github.panpf.assemblyadapter.sample.vm.AppListViewModel
import com.github.panpf.assemblyadapter.sample.vm.AppsOverviewViewModel
import com.github.panpf.assemblyadapter.sample.vm.GridDividerParamsViewModel
import com.github.panpf.assemblyadapter.sample.vm.PinyinFlatAppListViewModel

class RecyclerGridDividerVerFragment : ToolbarFragment<FragmentRecyclerDividerVerBinding>() {

    private val args: RecyclerGridDividerVerFragmentArgs by navArgs()
    private val appListViewModel by viewModels<AppListViewModel>()
    private val appsOverviewViewModel by viewModels<AppsOverviewViewModel>()
    private val pinyinFlatAppListViewModel by viewModels<PinyinFlatAppListViewModel>()
    private val dividerParamsViewMode by viewModels<GridDividerParamsViewModel> {
        GridDividerParamsViewModel.Factory(
            requireActivity().application,
            "RecyclerGridDividerVerDividerParams"
        )
    }

    override fun createViewBinding(
        inflater: LayoutInflater, parent: ViewGroup?
    ): FragmentRecyclerDividerVerBinding {
        return FragmentRecyclerDividerVerBinding.inflate(inflater, parent, false)
    }

    override fun onInitViews(
        toolbar: Toolbar,
        binding: FragmentRecyclerDividerVerBinding,
        savedInstanceState: Bundle?
    ) {
        initMenu(toolbar)
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onInitData(
        toolbar: Toolbar,
        binding: FragmentRecyclerDividerVerBinding,
        savedInstanceState: Bundle?
    ) {
        toolbar.title = args.title
        toolbar.subtitle = args.subtitle

        val appsOverviewAdapter = AssemblySingleDataRecyclerAdapter(
            AppsOverviewItemFactory(requireActivity())
        )
        val recyclerAdapter = AssemblyRecyclerAdapter<Any>(
            listOf(
                AppGridStrokeItemFactory(
                    requireActivity(),
                    viewLifecycleOwner,
                    dividerParamsViewMode.dividerParamsData
                ),
                ListSeparatorItemFactory(requireActivity(), hideDivider = true)
            )
        )
        val footerLoadStateAdapter =
            AssemblySingleDataRecyclerAdapter(LoadStateItemFactory(requireActivity()))
        binding.recyclerDividerVerRecycler.apply {
            adapter = ConcatAdapter(appsOverviewAdapter, recyclerAdapter, footerLoadStateAdapter)
            dividerParamsViewMode.dividerParamsData.observe(viewLifecycleOwner) { dividerParams ->
                dividerParams ?: return@observe

                layoutManager = newAssemblyGridLayoutManager(dividerParams.getSpanCount(true)) {
                    itemSpanByPosition(dividerParams.spanSizeByPosition ?: emptyMap())
                    itemSpanByItemFactory(
                        AppsOverviewItemFactory::class to ItemSpan.fullSpan(),
                        ListSeparatorItemFactory::class to ItemSpan.fullSpan(),
                        LoadStateItemFactory::class to ItemSpan.fullSpan()
                    )
                }

                if (itemDecorationCount > 0) {
                    removeItemDecorationAt(0)
                }
                addItemDecoration(buildDividerItemDecoration(dividerParams))
                adapter?.notifyDataSetChanged() // The item width needs to be recalculated and refreshed to take effect
            }
        }

        val appsOverviewObserver = Observer<AppsOverview> {
            appsOverviewAdapter.data = it
        }
        val observer = Observer<List<Any>> {
            recyclerAdapter.submitList(it)
            footerLoadStateAdapter.data = LoadState.NotLoading(true)
        }
        dividerParamsViewMode.dividerParamsData.observe(viewLifecycleOwner) { dividerParams ->
            dividerParams ?: return@observe

            appsOverviewViewModel.appsOverviewData.removeObserver(appsOverviewObserver)
            if (dividerParams.isShowAppsOverview) {
                appsOverviewViewModel.appsOverviewData
                    .observe(viewLifecycleOwner, appsOverviewObserver)
            } else {
                appsOverviewAdapter.data = null
            }

            pinyinFlatAppListViewModel.pinyinFlatAppListData.removeObserver(observer)
            appListViewModel.appListData.removeObserver(observer)
            if (dividerParams.isShowListSeparator) {
                pinyinFlatAppListViewModel.pinyinFlatAppListData
                    .observe(viewLifecycleOwner, observer)
            } else {
                appListViewModel.appListData.observe(viewLifecycleOwner, observer)
            }
        }
    }

    private fun buildDividerItemDecoration(dividerParams: GridDividerParams): RecyclerView.ItemDecoration {
        val size = dividerParams.dividerSize
        val insets = Insets.allOf(dividerParams.dividerInsetsSize)
        return requireContext().newAssemblyGridDividerItemDecoration {
            disableDefaultDivider()
            if (dividerParams.isShowDivider) {
                divider(Divider.colorRes(R.color.divider, size, insets)) {
                    personaliseByItemFactoryClass(
                        ListSeparatorItemFactory::class,
                        Divider.colorRes(R.color.divider_personalise, size, insets)
                    )
                    disableByItemFactoryClass(AppsOverviewItemFactory::class)
                }
            }
            if (dividerParams.isShowHeaderDivider) {
                headerDivider(
                    Divider.colorRes(R.color.divider_header, size, insets)
                )
            }
            if (dividerParams.isShowFooterDivider) {
                footerDivider(
                    Divider.colorRes(R.color.divider_header, size, insets)
                )
            }

            if (dividerParams.isShowSideDivider) {
                sideDivider(Divider.colorRes(R.color.sideDivider, size, insets))
                if (dividerParams.isShowSideHeaderDivider) {
                    sideHeaderDivider(
                        Divider.colorRes(R.color.sideDivider_header, size, insets)
                    ) {
                        personaliseByItemFactoryClass(
                            ListSeparatorItemFactory::class,
                            Divider.colorRes(R.color.sideDivider_personalise, size, insets)
                        )
                        disableByItemFactoryClass(AppsOverviewItemFactory::class)
                    }
                }
                if (dividerParams.isShowSideFooterDivider) {
                    sideFooterDivider(
                        Divider.colorRes(R.color.sideDivider_header, size, insets)
                    ) {
                        personaliseByItemFactoryClass(
                            ListSeparatorItemFactory::class,
                            Divider.colorRes(R.color.sideDivider_personalise, size, insets)
                        )
                        disableByItemFactoryClass(AppsOverviewItemFactory::class)
                    }
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
                    1, 3, 3,
                    if (dividerParams.isShowSideDivider)
                        "Hide Side Divider" else "Show Side Divider"
                ).apply {
                    setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER)
                    setOnMenuItemClickListener {
                        dividerParams.isShowSideDivider = !dividerParams.isShowSideDivider
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
                        if (dividerParams.isShowSideDivider) {
                            dividerParams.isShowSideHeaderDivider =
                                !dividerParams.isShowSideHeaderDivider
                            dividerParamsViewMode.dividerParamsData.postValue(dividerParams)
                        } else {
                            Toast.makeText(
                                requireContext(),
                                "Side Divider must be displayed first",
                                Toast.LENGTH_LONG
                            ).show()
                        }
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
                        if (dividerParams.isShowSideDivider) {
                            dividerParams.isShowSideFooterDivider =
                                !dividerParams.isShowSideFooterDivider
                            dividerParamsViewMode.dividerParamsData.postValue(dividerParams)
                        } else {
                            Toast.makeText(
                                requireContext(),
                                "Side Divider must be displayed first",
                                Toast.LENGTH_LONG
                            ).show()
                        }
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

                add(
                    3, 8, 8,
                    if (dividerParams.isShowListSeparator)
                        "Hide List Separator" else "Show List Separator"
                ).apply {
                    setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER)
                    setOnMenuItemClickListener {
                        if (dividerParams.compactMode) {
                            dividerParams.isShowListSeparator = !dividerParams.isShowListSeparator
                            dividerParamsViewMode.dividerParamsData.postValue(dividerParams)
                        } else {
                            Toast.makeText(
                                requireContext(),
                                "Please close Span Size Demo Mode first",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                        true
                    }
                }

                add(
                    3, 9, 9,
                    if (dividerParams.isLessSpan) "Many Span" else "Less Span"
                ).apply {
                    setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER)
                    setOnMenuItemClickListener {
                        if (dividerParams.compactMode) {
                            dividerParams.isLessSpan = !dividerParams.isLessSpan
                            dividerParamsViewMode.dividerParamsData.postValue(dividerParams)
                        } else {
                            Toast.makeText(
                                requireContext(),
                                "Please close Span Size Demo Mode first",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                        true
                    }
                }

                add(
                    3, 10, 10,
                    if (dividerParams.compactMode) "Close Compact Mode" else "Open Compact Mode"
                ).apply {
                    setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER)
                    setOnMenuItemClickListener {
                        dividerParams.compactMode = !dividerParams.compactMode
                        dividerParamsViewMode.dividerParamsData.postValue(dividerParams)
                        true
                    }
                }
            }
        }
    }
}