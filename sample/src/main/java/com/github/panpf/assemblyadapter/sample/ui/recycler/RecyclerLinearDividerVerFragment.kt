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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.panpf.assemblyadapter.recycler.AssemblyRecyclerAdapter
import com.github.panpf.assemblyadapter.recycler.AssemblySingleDataRecyclerAdapter
import com.github.panpf.assemblyadapter.recycler.divider.AssemblyLinearDividerItemDecoration
import com.github.panpf.assemblyadapter.recycler.divider.Divider
import com.github.panpf.assemblyadapter.recycler.divider.Insets
import com.github.panpf.assemblyadapter.sample.base.ToolbarFragment
import com.github.panpf.assemblyadapter.sample.databinding.FragmentRecyclerDividerVerBinding
import com.github.panpf.assemblyadapter.sample.item.AppItemFactory
import com.github.panpf.assemblyadapter.sample.item.AppsOverviewItemFactory
import com.github.panpf.assemblyadapter.sample.item.ListSeparatorItemFactory
import com.github.panpf.assemblyadapter.sample.item.LoadStateItemFactory
import com.github.panpf.assemblyadapter.sample.vm.PinyinFlatAppsViewModel
import com.github.panpf.tools4a.dimen.ktx.dp2px

class RecyclerLinearDividerVerFragment :
    ToolbarFragment<FragmentRecyclerDividerVerBinding>() {

    private val args: RecyclerLinearDividerVerFragmentArgs by navArgs()

    private val viewModel by viewModels<PinyinFlatAppsViewModel>()

    private var openedInsets = false
    private var thickDivider = true

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
        initMenu(toolbar, binding.recyclerDividerVerRecycler)
    }

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
                AppItemFactory(requireActivity(), showBg = true),
                ListSeparatorItemFactory(requireActivity(), hideDivider = true)
            )
        )
        val footerLoadStateAdapter =
            AssemblySingleDataRecyclerAdapter(LoadStateItemFactory(requireActivity()))
        binding.recyclerDividerVerRecycler.apply {
            adapter = ConcatAdapter(appsOverviewAdapter, recyclerAdapter, footerLoadStateAdapter)
            layoutManager = LinearLayoutManager(requireContext())
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
        return AssemblyLinearDividerItemDecoration.Builder(requireContext()).apply {
            val insets = Insets.allOf((if (openedInsets) 2.5f else 0f).dp2px)
            val size = if (thickDivider) 5.dp2px else 1.dp2px
            divider(Divider.color(0x88FF0000.toInt(), size, insets)) {
                personaliseByItemFactoryClass(
                    ListSeparatorItemFactory::class,
                    Divider.color(0x8800FF00.toInt(), size, insets)
                )
                disableByItemFactoryClass(AppsOverviewItemFactory::class)
            }
            headerAndFooterDivider(Divider.color(0xFFFF0000.toInt(), size, insets))

            sideHeaderAndFooterDivider(Divider.color(0xFF0000FF.toInt(), size, insets)) {
                personaliseByItemFactoryClass(
                    ListSeparatorItemFactory::class,
                    Divider.color(0xFF00FF00.toInt(), size, insets)
                )
                disableByItemFactoryClass(AppsOverviewItemFactory::class)
            }
        }.build()
    }
}