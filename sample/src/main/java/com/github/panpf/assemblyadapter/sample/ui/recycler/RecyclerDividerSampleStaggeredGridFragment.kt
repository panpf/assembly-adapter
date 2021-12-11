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
import androidx.appcompat.widget.Toolbar
import androidx.navigation.fragment.navArgs
import com.github.panpf.assemblyadapter.pager2.ArrayFragmentStateAdapter
import com.github.panpf.assemblyadapter.sample.base.ToolbarFragment
import com.github.panpf.assemblyadapter.sample.databinding.FragmentDividerSampleBinding
import com.google.android.material.tabs.TabLayoutMediator

class RecyclerDividerSampleStaggeredGridFragment :
    ToolbarFragment<FragmentDividerSampleBinding>() {

    private val args: RecyclerDividerSampleGridFragmentArgs by navArgs()

    override fun createViewBinding(
        inflater: LayoutInflater, parent: ViewGroup?
    ): FragmentDividerSampleBinding {
        return FragmentDividerSampleBinding.inflate(inflater, parent, false)
    }

    override fun onInitViews(
        toolbar: Toolbar,
        binding: FragmentDividerSampleBinding,
        savedInstanceState: Bundle?
    ) {
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onInitData(
        toolbar: Toolbar,
        binding: FragmentDividerSampleBinding,
        savedInstanceState: Bundle?
    ) {
        toolbar.title = args.title
        toolbar.subtitle = args.subtitle

        val pagerAdapter = ArrayFragmentStateAdapter(
            this,
            listOf(
                RecyclerDividerSampleStaggeredGridDrawableVerFragment(),
                RecyclerDividerSampleStaggeredGridDrawableHorFragment(),
                RecyclerDividerSampleStaggeredGridInsetsVerFragment(),
                RecyclerDividerSampleStaggeredGridInsetsHorFragment(),
                RecyclerDividerSampleStaggeredGridSizeVerFragment(),
                RecyclerDividerSampleStaggeredGridSizeHorFragment(),
            )
        )
        binding.dividerSamplePager.apply {
            adapter = pagerAdapter
        }

        TabLayoutMediator(
            binding.dividerSampleTabLayout,
            binding.dividerSamplePager
        ) { tab, position ->
            tab.text = when (position) {
                0 -> "Drawable_Ver"
                1 -> "Drawable_Hor"
                2 -> "Insets_Ver"
                3 -> "Insets_Hor"
                4 -> "Size_Ver"
                5 -> "Size_Hor"
                else -> null
            }
        }.attach()
    }
}