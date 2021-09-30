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
import android.view.MenuItem
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.paging.LoadState
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.github.panpf.assemblyadapter.recycler.AssemblyRecyclerAdapter
import com.github.panpf.assemblyadapter.recycler.AssemblySingleDataRecyclerAdapter
import com.github.panpf.assemblyadapter.recycler.AssemblyStaggeredGridLayoutManager
import com.github.panpf.assemblyadapter.recycler.divider.AssemblyStaggeredGridDividerItemDecoration
import com.github.panpf.assemblyadapter.recycler.divider.Divider
import com.github.panpf.assemblyadapter.recycler.divider.Insets
import com.github.panpf.assemblyadapter.sample.base.ToolbarFragment
import com.github.panpf.assemblyadapter.sample.databinding.FragmentRecyclerDividerHorBinding
import com.github.panpf.assemblyadapter.sample.item.AppGridHorItemFactory
import com.github.panpf.assemblyadapter.sample.item.AppsOverviewHorItemFactory
import com.github.panpf.assemblyadapter.sample.item.ListSeparatorHorItemFactory
import com.github.panpf.assemblyadapter.sample.item.LoadStateHorItemFactory
import com.github.panpf.assemblyadapter.sample.vm.PinyinFlatAppsViewModel
import com.github.panpf.tools4a.dimen.ktx.dp2px

class RecyclerStaggeredGridDividerHorFragment :
    ToolbarFragment<FragmentRecyclerDividerHorBinding>() {

    private val args: RecyclerStaggeredGridDividerHorFragmentArgs by navArgs()

    private val viewModel by viewModels<PinyinFlatAppsViewModel>()

    private var openedInsets = false
    private var thickDivider = true

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
        initMenu(toolbar, binding.recyclerDividerHorRecycler)
    }

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
                AppGridHorItemFactory(requireActivity()),
                ListSeparatorHorItemFactory(requireActivity(), hideDivider = true)
            )
        )
        val footerLoadStateAdapter =
            AssemblySingleDataRecyclerAdapter(LoadStateHorItemFactory(requireActivity()))
        binding.recyclerDividerHorRecycler.apply {
            adapter = ConcatAdapter(appsOverviewAdapter, recyclerAdapter, footerLoadStateAdapter)
            layoutManager =
                AssemblyStaggeredGridLayoutManager(
                    6,
                    StaggeredGridLayoutManager.HORIZONTAL,
                    listOf(
                        AppsOverviewHorItemFactory::class,
                        ListSeparatorHorItemFactory::class,
                        LoadStateHorItemFactory::class
                    )
                )
            addItemDecoration(buildItemDecoration())
        }

        viewModel.appsOverviewData.observe(viewLifecycleOwner) {
            appsOverviewAdapter.data = it
        }

        viewModel.pinyinFlatAppListData.observe(viewLifecycleOwner) {
            recyclerAdapter.submitList(it)
            footerLoadStateAdapter.data = LoadState.NotLoading(true)
        }
    }

    private fun initMenu(toolbar: Toolbar, recyclerView: RecyclerView) {
        toolbar.menu.add(
            0, 1, 0,
            if (openedInsets) "Disable Insets" else "Enable Insets"
        ).apply {
            setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER)
        }.setOnMenuItemClickListener {
            openedInsets = !openedInsets
            recyclerView.removeItemDecorationAt(0)
            recyclerView.addItemDecoration(buildItemDecoration())
            it.title =
                if (openedInsets) "Disable Insets" else "Enable Insets"
            true
        }

        toolbar.menu.add(
            0, 2, 1,
            if (thickDivider) "Thin Divider" else "Thick Divider"
        ).apply {
            setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER)
        }.setOnMenuItemClickListener {
            thickDivider = !thickDivider
            recyclerView.removeItemDecorationAt(0)
            recyclerView.addItemDecoration(buildItemDecoration())
            it.title = if (thickDivider) "Thin Divider" else "Thick Divider"
            true
        }
    }

    private fun buildItemDecoration(): RecyclerView.ItemDecoration {
        return AssemblyStaggeredGridDividerItemDecoration.Builder(requireContext()).apply {
            val insets = Insets.allOf((if (openedInsets) 2.5f else 0f).dp2px)
            val size = if (thickDivider) 5.dp2px else 1.dp2px
            divider(Divider.color(0x88FF0000.toInt(), size, insets)) {
                personaliseByItemFactoryClass(
                    ListSeparatorHorItemFactory::class,
                    Divider.color(0x8800FF00.toInt(), size, insets)
                )
                disableByItemFactoryClass(AppsOverviewHorItemFactory::class)
            }
            headerAndFooterDivider(Divider.color(0xFFFF0000.toInt(), size, insets))

            sideDivider(Divider.color(0x880000FF.toInt(), size, insets))
            sideHeaderAndFooterDivider(Divider.color(0xFF0000FF.toInt(), size, insets)) {
                personaliseByItemFactoryClass(
                    ListSeparatorHorItemFactory::class,
                    Divider.color(0xFF00FF00.toInt(), size, insets)
                )
                disableByItemFactoryClass(AppsOverviewHorItemFactory::class)
            }
        }.build()
    }
}