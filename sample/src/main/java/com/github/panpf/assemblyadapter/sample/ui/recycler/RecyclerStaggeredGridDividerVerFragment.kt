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
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.navigation.fragment.navArgs
import androidx.paging.LoadState
import androidx.recyclerview.widget.ConcatAdapter
import com.github.panpf.assemblyadapter.recycler.AssemblyRecyclerAdapter
import com.github.panpf.assemblyadapter.recycler.AssemblySingleDataRecyclerAdapter
import com.github.panpf.assemblyadapter.recycler.AssemblyStaggeredGridLayoutManager
import com.github.panpf.assemblyadapter.recycler.divider.*
import com.github.panpf.assemblyadapter.sample.R
import com.github.panpf.assemblyadapter.sample.base.ToolbarFragment
import com.github.panpf.assemblyadapter.sample.bean.GridDividerParams
import com.github.panpf.assemblyadapter.sample.databinding.FragmentRecyclerDividerVerBinding
import com.github.panpf.assemblyadapter.sample.item.AppGridStrokeItemFactory
import com.github.panpf.assemblyadapter.sample.item.AppsOverviewItemFactory
import com.github.panpf.assemblyadapter.sample.item.ListSeparatorItemFactory
import com.github.panpf.assemblyadapter.sample.item.LoadStateItemFactory
import com.github.panpf.assemblyadapter.sample.vm.PinyinFlatAppsViewModel

class RecyclerStaggeredGridDividerVerFragment :
    ToolbarFragment<FragmentRecyclerDividerVerBinding>() {

    private val args: RecyclerStaggeredGridDividerVerFragmentArgs by navArgs()
    private val viewModel by viewModels<PinyinFlatAppsViewModel>()
    private val dividerParamsData = MutableLiveData(GridDividerParams())

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
        toolbar.menu.apply {
            dividerParamsData.observe(viewLifecycleOwner) { dividerParams ->
                clear()
                dividerParams ?: return@observe

                add(
                    0, 0, 0,
                    if (dividerParams.isShowDivider) "Hide Divider" else "Show Divider"
                ).apply {
                    setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER)
                    setOnMenuItemClickListener {
                        dividerParams.isShowDivider = !dividerParams.isShowDivider
                        dividerParamsData.postValue(dividerParams)
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
                        dividerParamsData.postValue(dividerParams)
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
                        dividerParamsData.postValue(dividerParams)
                        true
                    }
                }

                add(
                    0, 3, 3,
                    if (dividerParams.isShowSideDivider)
                        "Hide Side Divider" else "Show Side Divider"
                ).apply {
                    setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER)
                    setOnMenuItemClickListener {
                        dividerParams.isShowSideDivider = !dividerParams.isShowSideDivider
                        dividerParamsData.postValue(dividerParams)
                        true
                    }
                }

                add(
                    0, 4, 4,
                    if (dividerParams.isShowSideHeaderDivider)
                        "Hide Side Header Divider" else "Show Side Header Divider"
                ).apply {
                    setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER)
                    setOnMenuItemClickListener {
                        dividerParams.isShowSideHeaderDivider =
                            !dividerParams.isShowSideHeaderDivider
                        dividerParamsData.postValue(dividerParams)
                        true
                    }
                }

                add(
                    0, 5, 5,
                    if (dividerParams.isShowSideFooterDivider)
                        "Hide Side Footer Divider" else "Show Side Footer Divider"
                ).apply {
                    setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER)
                    setOnMenuItemClickListener {
                        dividerParams.isShowSideFooterDivider =
                            !dividerParams.isShowSideFooterDivider
                        dividerParamsData.postValue(dividerParams)
                        true
                    }
                }

                add(
                    0, 6, 6,
                    if (dividerParams.isBigDivider) "Small Divider" else "Big Divider"
                ).apply {
                    setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER)
                    setOnMenuItemClickListener {
                        dividerParams.isBigDivider = !dividerParams.isBigDivider
                        dividerParamsData.postValue(dividerParams)
                        true
                    }
                }

                add(
                    0, 7, 7,
                    if (dividerParams.isShowDividerInsets)
                        "Hide Divider Insets" else "Show Divider Insets"
                ).apply {
                    setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER)
                    setOnMenuItemClickListener {
                        dividerParams.isShowDividerInsets = !dividerParams.isShowDividerInsets
                        dividerParamsData.postValue(dividerParams)
                        true
                    }
                }
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onInitData(
        toolbar: Toolbar,
        binding: FragmentRecyclerDividerVerBinding,
        savedInstanceState: Bundle?
    ) {
        toolbar.title = args.title
        toolbar.subtitle = args.subtitle
        val appsOverviewAdapter =
            AssemblySingleDataRecyclerAdapter(AppsOverviewItemFactory(requireActivity()))
        val recyclerAdapter = AssemblyRecyclerAdapter<Any>(
            listOf(
                AppGridStrokeItemFactory(requireActivity(), viewLifecycleOwner, dividerParamsData),
                ListSeparatorItemFactory(requireActivity(), hideDivider = true)
            )
        )
        val footerLoadStateAdapter =
            AssemblySingleDataRecyclerAdapter(LoadStateItemFactory(requireActivity()))
        binding.recyclerDividerVerRecycler.apply {
            adapter = ConcatAdapter(appsOverviewAdapter, recyclerAdapter, footerLoadStateAdapter)
            layoutManager =
                AssemblyStaggeredGridLayoutManager(
                    4,
                    listOf(
                        AppsOverviewItemFactory::class,
                        ListSeparatorItemFactory::class,
                        LoadStateItemFactory::class
                    )
                )
            dividerParamsData.observe(viewLifecycleOwner) { dividerParams ->
                dividerParams ?: return@observe
                if (itemDecorationCount > 0) {
                    removeItemDecorationAt(0)
                }
                val size = dividerParams.dividerSize
                val insets = Insets.allOf(dividerParams.dividerInsetsSize)
                addAssemblyGridDividerItemDecoration {
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
                    }
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