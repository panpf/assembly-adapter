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
package com.github.panpf.assemblyadapter.sample.ui.pager

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.fragment.app.viewModels
import androidx.paging.LoadState
import androidx.viewpager.widget.ViewPager
import com.github.panpf.assemblyadapter.Placeholder
import com.github.panpf.assemblyadapter.pager.AssemblyFragmentStatePagerAdapter
import com.github.panpf.assemblyadapter.pager.AssemblySingleDataFragmentStatePagerAdapter
import com.github.panpf.assemblyadapter.pager.ConcatFragmentStatePagerAdapter
import com.github.panpf.assemblyadapter.pager.ViewFragmentItemFactory
import com.github.panpf.assemblyadapter.sample.R
import com.github.panpf.assemblyadapter.sample.base.BaseBindingFragment
import com.github.panpf.assemblyadapter.sample.databinding.FragmentPagerBinding
import com.github.panpf.assemblyadapter.sample.item.pager.AppGroupFragmentItemFactory
import com.github.panpf.assemblyadapter.sample.item.pager.AppsOverviewFragmentItemFactory
import com.github.panpf.assemblyadapter.sample.item.pager.LoadStateFragmentItemFactory
import com.github.panpf.assemblyadapter.sample.vm.PagerPinyinGroupOverviewAppsViewModel

class PagerFragmentPlaceholderFragment : BaseBindingFragment<FragmentPagerBinding>() {

    private val viewModel by viewModels<PagerPinyinGroupOverviewAppsViewModel>()
    private var registered = false

    override fun createViewBinding(
        inflater: LayoutInflater, parent: ViewGroup?
    ): FragmentPagerBinding {
        return FragmentPagerBinding.inflate(inflater, parent, false)
    }

    override fun onInitData(binding: FragmentPagerBinding, savedInstanceState: Bundle?) {
        val appsOverviewAdapter = AssemblySingleDataFragmentStatePagerAdapter(
            childFragmentManager,
            FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT,
            AppsOverviewFragmentItemFactory()
        )
        val pagerAdapter = AssemblyFragmentStatePagerAdapter(
            childFragmentManager,
            FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT,
            listOf(
                AppGroupFragmentItemFactory(),
                ViewFragmentItemFactory(
                    Placeholder::class,
                    R.layout.fragment_app_group_placeholder
                ),
            ),
            arrayOfNulls<Any?>(20).toList()
        )
        val footerLoadStateAdapter = AssemblySingleDataFragmentStatePagerAdapter(
            childFragmentManager,
            FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT,
            LoadStateFragmentItemFactory()
        )
        binding.pagerPager.apply {
            adapter = ConcatFragmentStatePagerAdapter(
                childFragmentManager,
                FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT,
                appsOverviewAdapter,
                pagerAdapter,
                footerLoadStateAdapter
            )
            addOnPageChangeListener(object : ViewPager.SimpleOnPageChangeListener() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    updatePageNumber(binding)
                }
            })
        }

        binding.pagerTabLayout.setupWithViewPager(binding.pagerPager, true)

        updatePageNumber(binding)

        registered = false
        binding.pagerPageNumberText.setOnClickListener {
            if (!registered) {
                registered = true

                viewModel.loadingData.observe(viewLifecycleOwner) {
                    binding.pagerProgressBar.isVisible = it == true
                    binding.pagerPageNumberText.isVisible = it != true
                }

                viewModel.appsOverviewData.observe(viewLifecycleOwner) {
                    appsOverviewAdapter.data = it
                    appsOverviewAdapter.currentPageTitle = "OVERVIEW"
                    updatePageNumber(binding)
                }

                viewModel.pinyinGroupAppListData.observe(viewLifecycleOwner) { list ->
                    pagerAdapter.submitList(list)
                    pagerAdapter.submitPageTitleList(list.map { it.title })
                    footerLoadStateAdapter.data = LoadState.NotLoading(true)
                    footerLoadStateAdapter.currentPageTitle = "END"
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
    private fun updatePageNumber(binding: FragmentPagerBinding) {
        val pager = binding.pagerPager
        binding.pagerPageNumberText.text =
            "${pager.currentItem + 1}/${pager.adapter?.count ?: 0}"
    }
}
