/*
 * Copyright (C) 2021 panpf <panpfpanpf@oulook.com>
 *
 * Licensed under the Apache License, Horsion 2.0 (the "License");
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
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.github.panpf.assemblyadapter.recycler.AssemblyRecyclerAdapter
import com.github.panpf.assemblyadapter.sample.base.BaseBindingFragment
import com.github.panpf.assemblyadapter.sample.databinding.FragmentDividerSampleLinearInsetsHorBinding
import com.github.panpf.assemblyadapter.sample.item.AppHorItemFactory
import com.github.panpf.assemblyadapter.sample.vm.AppListViewModel

class RecyclerDividerSampleLinearInsetsHorFragment :
    BaseBindingFragment<FragmentDividerSampleLinearInsetsHorBinding>() {

    private val appListViewModel by viewModels<AppListViewModel>()

    override fun createViewBinding(
        inflater: LayoutInflater, parent: ViewGroup?
    ): FragmentDividerSampleLinearInsetsHorBinding {
        return FragmentDividerSampleLinearInsetsHorBinding.inflate(inflater, parent, false)
    }

    override fun onInitViews(
        binding: FragmentDividerSampleLinearInsetsHorBinding,
        savedInstanceState: Bundle?
    ) {
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onInitData(
        binding: FragmentDividerSampleLinearInsetsHorBinding,
        savedInstanceState: Bundle?
    ) {
        binding.dividerSampleLinearInsetsHorRecycler1.apply {
            val recyclerAdapter = AssemblyRecyclerAdapter<Any>(
                listOf(AppHorItemFactory(requireActivity()))
            )
            appListViewModel.appListData.observe(viewLifecycleOwner, {
                recyclerAdapter.submitList(it)
            })
            adapter = recyclerAdapter
        }

        binding.dividerSampleLinearInsetsHorRecycler2.apply {
            val recyclerAdapter = AssemblyRecyclerAdapter<Any>(
                listOf(AppHorItemFactory(requireActivity()))
            )
            appListViewModel.appListData.observe(viewLifecycleOwner, {
                recyclerAdapter.submitList(it)
            })
            adapter = recyclerAdapter
        }
    }
}