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
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.viewpager.widget.ViewPager
import com.github.panpf.assemblyadapter.pager.ArrayPagerAdapter
import com.github.panpf.assemblyadapter.sample.base.BaseBindingFragment
import com.github.panpf.assemblyadapter.sample.databinding.FragmentPagerBinding
import com.github.panpf.assemblyadapter.sample.item.pager.AppGroupPagerItemFactory
import com.github.panpf.assemblyadapter.sample.vm.PagerPinyinGroupAppsViewModel

class PagerViewArrayFragment : BaseBindingFragment<FragmentPagerBinding>() {

    private val viewModel by viewModels<PagerPinyinGroupAppsViewModel>()

    override fun createViewBinding(
        inflater: LayoutInflater, parent: ViewGroup?
    ): FragmentPagerBinding {
        return FragmentPagerBinding.inflate(inflater, parent, false)
    }

    override fun onInitData(binding: FragmentPagerBinding, savedInstanceState: Bundle?) {
        binding.pagerPager.apply {
            addOnPageChangeListener(object : ViewPager.SimpleOnPageChangeListener() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    updatePageNumber(binding)
                }
            })
        }

        viewModel.loadingData.observe(viewLifecycleOwner) {
            binding.pagerProgressBar.isVisible = it == true
            binding.pagerPageNumberText.isVisible = it != true
        }

        viewModel.pinyinGroupAppListData.observe(viewLifecycleOwner) {
            val itemFactory = AppGroupPagerItemFactory(requireActivity())
            val viewList = 0.until(it.size).map { position ->
                itemFactory.dispatchCreateItemView(
                    requireContext(), binding.pagerPager, position, position, it[position]
                )
            }
            binding.pagerPager.adapter = ArrayPagerAdapter(viewList)
            updatePageNumber(binding)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun updatePageNumber(binding: FragmentPagerBinding) {
        val pager = binding.pagerPager
        binding.pagerPageNumberText.text =
            "${pager.currentItem + 1}/${pager.adapter?.count ?: 0}"
    }
}
