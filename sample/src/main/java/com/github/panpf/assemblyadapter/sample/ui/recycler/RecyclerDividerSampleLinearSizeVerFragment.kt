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
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.github.panpf.assemblyadapter.recycler.AssemblyRecyclerAdapter
import com.github.panpf.assemblyadapter.sample.base.BaseBindingFragment
import com.github.panpf.assemblyadapter.sample.databinding.FragmentDividerSampleLinearSizeVerBinding
import com.github.panpf.assemblyadapter.sample.item.AppGridItemFactory
import com.github.panpf.assemblyadapter.sample.vm.AppListViewModel

class RecyclerDividerSampleLinearSizeVerFragment :
    BaseBindingFragment<FragmentDividerSampleLinearSizeVerBinding>() {

    private val appListViewModel by viewModels<AppListViewModel>()

    override fun createViewBinding(
        inflater: LayoutInflater, parent: ViewGroup?
    ): FragmentDividerSampleLinearSizeVerBinding {
        return FragmentDividerSampleLinearSizeVerBinding.inflate(inflater, parent, false)
    }

    override fun onInitViews(
        binding: FragmentDividerSampleLinearSizeVerBinding,
        savedInstanceState: Bundle?
    ) {
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onInitData(
        binding: FragmentDividerSampleLinearSizeVerBinding,
        savedInstanceState: Bundle?
    ) {
        binding.dividerSampleLinearSizeVerRecycler1.apply {
            val recyclerAdapter = AssemblyRecyclerAdapter<Any>(
                listOf(AppGridItemFactory(requireActivity()))
            )
            appListViewModel.appListData.observe(viewLifecycleOwner, {
                recyclerAdapter.submitList(it)
            })
            adapter = recyclerAdapter
        }

        binding.dividerSampleLinearSizeVerRecycler2.apply {
            val recyclerAdapter = AssemblyRecyclerAdapter<Any>(
                listOf(AppGridItemFactory(requireActivity()))
            )
            appListViewModel.appListData.observe(viewLifecycleOwner, {
                recyclerAdapter.submitList(it)
            })
            adapter = recyclerAdapter
        }
    }
}