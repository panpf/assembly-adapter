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
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.paging.LoadState
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.panpf.assemblyadapter.recycler.AssemblyLinearDividerItemDecoration
import com.github.panpf.assemblyadapter.recycler.AssemblyRecyclerAdapter
import com.github.panpf.assemblyadapter.recycler.AssemblySingleDataRecyclerAdapter
import com.github.panpf.assemblyadapter.recycler.divider.Decorate
import com.github.panpf.assemblyadapter.recycler.divider.Insets
import com.github.panpf.assemblyadapter.sample.R
import com.github.panpf.assemblyadapter.sample.base.BaseBindingFragment
import com.github.panpf.assemblyadapter.sample.databinding.FragmentRecyclerDividerVerticalBinding
import com.github.panpf.assemblyadapter.sample.item.AppItemFactory
import com.github.panpf.assemblyadapter.sample.item.AppsOverviewItemFactory
import com.github.panpf.assemblyadapter.sample.item.ListSeparatorItemFactory
import com.github.panpf.assemblyadapter.sample.item.LoadStateItemFactory
import com.github.panpf.assemblyadapter.sample.vm.MenuViewModel
import com.github.panpf.assemblyadapter.sample.vm.PinyinFlatAppsViewModel
import com.github.panpf.tools4a.dimen.ktx.dp2px

class RecyclerLinearDividerVerFragment :
    BaseBindingFragment<FragmentRecyclerDividerVerticalBinding>() {

    private val viewModel by viewModels<PinyinFlatAppsViewModel>()
    private val menuViewModel by activityViewModels<MenuViewModel>()

    private var openedInsets = false

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
            AssemblySingleDataRecyclerAdapter(AppsOverviewItemFactory(requireActivity()))
        val recyclerAdapter = AssemblyRecyclerAdapter<Any>(
            listOf(
                AppItemFactory(requireActivity(), showBg = true),
                ListSeparatorItemFactory(requireActivity(), hideDivider = true)
            )
        )
        val footerLoadStateAdapter =
            AssemblySingleDataRecyclerAdapter(LoadStateItemFactory(requireActivity()))
        binding.recyclerDividerVerticalRecycler.apply {
            adapter = ConcatAdapter(appsOverviewAdapter, recyclerAdapter, footerLoadStateAdapter)
            layoutManager = LinearLayoutManager(requireContext())

            addItemDecoration(buildItemDecoration())
            menuViewModel.menuInfoListData.postValue(listOf(buildMenuInfo()))
            menuViewModel.menuClickEvent.listen(viewLifecycleOwner) {
                binding.recyclerDividerVerticalRecycler.apply {
                    if (it?.id == R.id.insets_switch) {
                        openedInsets = !openedInsets
                        menuViewModel.menuInfoListData.postValue(listOf(buildMenuInfo()))
                        removeItemDecorationAt(0)
                        addItemDecoration(buildItemDecoration())
                    }
                }
            }
        }

        viewModel.appsOverviewData.observe(viewLifecycleOwner) {
            appsOverviewAdapter.data = it
        }
        viewModel.pinyinFlatAppListData.observe(viewLifecycleOwner) {
            recyclerAdapter.submitDataList(it)
            footerLoadStateAdapter.data = LoadState.NotLoading(true)
        }
    }

    private fun buildMenuInfo(): MenuViewModel.MenuInfo {
        return MenuViewModel.MenuInfo(
            R.id.insets_switch,
            if (openedInsets) "DISABLE INSETS" else "ENABLE INSETS"
        )
    }

    private fun buildItemDecoration(): RecyclerView.ItemDecoration {
        return AssemblyLinearDividerItemDecoration.Builder(requireContext()).apply {
            val insets = Insets.allOf((if (openedInsets) 2.5f else 0f).dp2px)
            val size = 5.dp2px
            divider(
                Decorate.color(0x88FF0000.toInt(), size, insets)
            )
            firstAndLastDivider(
                Decorate.color(0xFFFF0000.toInt(), size, insets)
            )
            personaliseDivider(
                ListSeparatorItemFactory::class,
                Decorate.color(0x8800FF00.toInt(), size, insets)
            )
            disableDivider(AppsOverviewItemFactory::class)

            startAndEndSide(
                Decorate.color(0xFF0000FF.toInt(), size, insets)
            )
            personaliseStartAndEndSide(
                ListSeparatorItemFactory::class,
                Decorate.color(0xFF00FF00.toInt(), size, insets)
            )
            disableStartAndEndSide(AppsOverviewItemFactory::class)
        }.build()
    }
}