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
package com.github.panpf.assemblyadapter.sample.ui.pager2

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.paging.LoadState
import androidx.recyclerview.widget.ConcatAdapter
import androidx.viewpager2.widget.ViewPager2
import com.github.panpf.assemblyadapter.Placeholder
import com.github.panpf.assemblyadapter.pager.ViewFragmentItemFactory
import com.github.panpf.assemblyadapter.pager2.AssemblyFragmentStateAdapter
import com.github.panpf.assemblyadapter.pager2.AssemblySingleDataFragmentStateAdapter
import com.github.panpf.assemblyadapter.sample.R
import com.github.panpf.assemblyadapter.sample.base.BaseBindingFragment
import com.github.panpf.assemblyadapter.sample.databinding.FragmentPager2Binding
import com.github.panpf.assemblyadapter.sample.item.pager.AppGroupFragmentItemFactory
import com.github.panpf.assemblyadapter.sample.item.pager.AppsOverviewFragmentItemFactory
import com.github.panpf.assemblyadapter.sample.item.pager.LoadStateFragmentItemFactory
import com.github.panpf.assemblyadapter.sample.vm.PagerPinyinGroupAppsViewModel

class Pager2FragmentPlaceholderFragment : BaseBindingFragment<FragmentPager2Binding>() {

    private val viewModel by viewModels<PagerPinyinGroupAppsViewModel>()
    private var registered = false

    override fun createViewBinding(
        inflater: LayoutInflater, parent: ViewGroup?
    ): FragmentPager2Binding {
        return FragmentPager2Binding.inflate(inflater, parent, false)
    }

    override fun onInitData(binding: FragmentPager2Binding, savedInstanceState: Bundle?) {
        val appsOverviewAdapter = AssemblySingleDataFragmentStateAdapter(
            this,
            AppsOverviewFragmentItemFactory()
        )
        val fragmentStateAdapter = AssemblyFragmentStateAdapter(
            this,
            listOf(
                AppGroupFragmentItemFactory(),
                ViewFragmentItemFactory(
                    Placeholder::class,
                    R.layout.fragment_app_group_placeholder
                ),
            ),
            arrayOfNulls<Any?>(20).toList()
        )
        val footerLoadStateAdapter = AssemblySingleDataFragmentStateAdapter(
            this,
            LoadStateFragmentItemFactory()
        )
        binding.pager2Pager.apply {
            adapter = ConcatAdapter(
                ConcatAdapter.Config.Builder()
                    .setIsolateViewTypes(true)
                    .setStableIdMode(ConcatAdapter.Config.StableIdMode.SHARED_STABLE_IDS)
                    .build(),
                appsOverviewAdapter,
                fragmentStateAdapter,
                footerLoadStateAdapter
            )
            registerOnPageChangeCallback(object :
                ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    updatePageNumber(binding)
                }
            })
        }
        updatePageNumber(binding)

        registered = false
        binding.pager2PageNumberText.setOnClickListener {
            if (!registered) {
                registered = true

                viewModel.loadingData.observe(viewLifecycleOwner) {
                    binding.pager2ProgressBar.isVisible = it == true
                    binding.pager2PageNumberText.isVisible = it != true
                }

                viewModel.appsOverviewData.observe(viewLifecycleOwner) {
                    appsOverviewAdapter.data = it
                    updatePageNumber(binding)
                }

                viewModel.pinyinGroupAppListData.observe(viewLifecycleOwner) {
                    fragmentStateAdapter.setDataList(it)
                    footerLoadStateAdapter.data = LoadState.NotLoading(true)
                    updatePageNumber(binding)
                }
            }
        }

        Toast.makeText(
            requireContext(),
            "Click page number to load real data",
            Toast.LENGTH_LONG
        ).show()
    }

    @SuppressLint("SetTextI18n")
    private fun updatePageNumber(binding: FragmentPager2Binding) {
        val pager = binding.pager2Pager
        binding.pager2PageNumberText.text =
            "${pager.currentItem + 1}/${pager.adapter?.itemCount ?: 0}"
    }
}