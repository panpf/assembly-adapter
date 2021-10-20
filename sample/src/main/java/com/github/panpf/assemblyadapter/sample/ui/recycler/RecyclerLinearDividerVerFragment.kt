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
import androidx.recyclerview.widget.LinearLayoutManager
import com.fondesa.recyclerviewdivider.dividerBuilder
import com.github.panpf.assemblyadapter.recycler.AssemblyRecyclerAdapter
import com.github.panpf.assemblyadapter.recycler.AssemblySingleDataRecyclerAdapter
import com.github.panpf.assemblyadapter.recycler.divider.*
import com.github.panpf.assemblyadapter.sample.R
import com.github.panpf.assemblyadapter.sample.base.ToolbarFragment
import com.github.panpf.assemblyadapter.sample.databinding.FragmentRecyclerDividerVerBinding
import com.github.panpf.assemblyadapter.sample.item.*
import com.github.panpf.assemblyadapter.sample.util.ThreeCombineMediatorLiveData
import com.github.panpf.assemblyadapter.sample.vm.PinyinFlatAppsViewModel
import com.github.panpf.tools4a.dimen.ktx.dp2px

class RecyclerLinearDividerVerFragment :
    ToolbarFragment<FragmentRecyclerDividerVerBinding>() {

    private val args: RecyclerLinearDividerVerFragmentArgs by navArgs()
    private val viewModel by viewModels<PinyinFlatAppsViewModel>()

    private val dividerSizeDpData = MutableLiveData(5f)
    private val dividerInsetsDpData = MutableLiveData(0f)
    private val useSelfDividerItemDecoration = MutableLiveData(true)
    private val dividerParamsData = ThreeCombineMediatorLiveData(
        dividerSizeDpData,
        dividerInsetsDpData,
        useSelfDividerItemDecoration,
        initValue = true
    )

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
            add(
                0, 0, 0,
                if (dividerSizeDpData.value!! >= 5f) "Small Divider" else "Big Divider"
            ).apply {
                setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER)
                setOnMenuItemClickListener {
                    val newDividerSize = if (dividerSizeDpData.value!! >= 5f) 2f else 5f
                    dividerSizeDpData.postValue(newDividerSize)
                    it.title =
                        if (newDividerSize >= 5f) "Small Divider" else "Big Divider"
                    true
                }
            }

            add(
                0, 1, 1,
                if (dividerInsetsDpData.value!! > 0f) "Disable Divider Insets" else "Enable Divider Insets"
            ).apply {
                setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER)
                setOnMenuItemClickListener {
                    val newDividerInsets = if (dividerInsetsDpData.value!! > 0f) 0f else 2f
                    dividerInsetsDpData.postValue(newDividerInsets)
                    it.title =
                        if (newDividerInsets > 0f) "Disable Divider Insets" else "Enable Divider Insets"
                    true
                }
            }

            add(
                0, 2, 2,
                if (useSelfDividerItemDecoration.value!!) "Use Other DividerItemDecoration" else "Use Self DividerItemDecoration"
            ).apply {
                setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER)
                setOnMenuItemClickListener {
                    val newValue = !useSelfDividerItemDecoration.value!!
                    useSelfDividerItemDecoration.postValue(newValue)
                    it.title =
                        if (newValue) "Use Other DividerItemDecoration" else "Use Self DividerItemDecoration"
                    true
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
                AppStrokeItemFactory(requireActivity(), viewLifecycleOwner, dividerSizeDpData, dividerInsetsDpData),
                ListSeparatorItemFactory(requireActivity(), hideDivider = true)
            )
        )
        val footerLoadStateAdapter =
            AssemblySingleDataRecyclerAdapter(LoadStateItemFactory(requireActivity()))
        binding.recyclerDividerVerRecycler.apply {
            adapter = ConcatAdapter(appsOverviewAdapter, recyclerAdapter, footerLoadStateAdapter)
            layoutManager = LinearLayoutManager(requireContext())
            dividerParamsData.observe(viewLifecycleOwner) {
                it ?: return@observe
                val dividerInsets = dividerInsetsDpData.value!!.dp2px
                val dividerSize = dividerSizeDpData.value!!.dp2px
                if (itemDecorationCount > 0) {
                    removeItemDecorationAt(0)
                }
                if (useSelfDividerItemDecoration.value!!) {
                    addAssemblyLinearDividerItemDecoration {
                        val insets = Insets.allOf(dividerInsets)
                        divider(Divider.colorRes(R.color.divider, dividerSize, insets)) {
                            personaliseByItemFactoryClass(
                                ListSeparatorItemFactory::class,
                                Divider.colorRes(R.color.divider_personalise, dividerSize, insets)
                            )
                            disableByItemFactoryClass(AppsOverviewItemFactory::class)
                        }
                        headerAndFooterDivider(
                            Divider.colorRes(R.color.divider_header, dividerSize, insets)
                        )

                        sideHeaderAndFooterDivider(
                            Divider.colorRes(R.color.sideDivider_header, dividerSize, insets)
                        ) {
                            personaliseByItemFactoryClass(
                                ListSeparatorItemFactory::class,
                                Divider.colorRes(
                                    R.color.sideDivider_personalise,
                                    dividerSize,
                                    insets
                                )
                            )
                            disableByItemFactoryClass(AppsOverviewItemFactory::class)
                        }
                    }
                } else {
                    addItemDecoration(requireContext().dividerBuilder().apply {
                        colorRes(R.color.divider)
                        size(dividerSize)
                        showFirstDivider()
                        showLastDivider()
                        showSideDividers()
                        insets(dividerInsets, dividerInsets)
                    }.build())
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