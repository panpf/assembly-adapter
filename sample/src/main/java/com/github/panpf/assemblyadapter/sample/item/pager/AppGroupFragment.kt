/*
 * Copyright (C) 2021 panpf <panpfpanpf@outlook.com>
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
package com.github.panpf.assemblyadapter.sample.item.pager

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.GridLayoutManager
import com.github.panpf.assemblyadapter.recycler.AssemblyRecyclerAdapter
import com.github.panpf.assemblyadapter.recycler.assemblyGridDividerItemDecorationBuilder
import com.github.panpf.assemblyadapter.recycler.divider.Decorate
import com.github.panpf.assemblyadapter.sample.R
import com.github.panpf.assemblyadapter.sample.base.BaseBindingFragment
import com.github.panpf.assemblyadapter.sample.bean.AppGroup
import com.github.panpf.assemblyadapter.sample.databinding.FragmentAppGroupBinding
import com.github.panpf.assemblyadapter.sample.item.AppCardGridItemFactory
import com.github.panpf.tools4a.dimen.ktx.dp2px

class AppGroupFragment : BaseBindingFragment<FragmentAppGroupBinding>() {

    companion object {
        fun createInstance(appGroup: AppGroup) = AppGroupFragment().apply {
            arguments = bundleOf("appGroup" to appGroup)
        }
    }

    private val appGroup by lazy { arguments?.getParcelable<AppGroup>("appGroup") }

    override fun createViewBinding(
        inflater: LayoutInflater, parent: ViewGroup?
    ): FragmentAppGroupBinding {
        return FragmentAppGroupBinding.inflate(inflater, parent, false)
    }

    override fun onInitData(binding: FragmentAppGroupBinding, savedInstanceState: Bundle?) {
        val data = appGroup
        binding.appGroupTitleText.text =
            requireContext().getString(R.string.app_group_title, data?.title, data?.appList?.size)
        binding.appGroupRecycler.apply {
            adapter = AssemblyRecyclerAdapter<Any>(
                listOf(AppCardGridItemFactory(requireActivity())),
                data?.appList
            )
            layoutManager = GridLayoutManager(context, 3)
            addItemDecoration(
                assemblyGridDividerItemDecorationBuilder()
                    .divider(Decorate.space(20.dp2px)).showFirstAndLastDivider()
                    .side(Decorate.space(20.dp2px)).showFirstAndLastSide()
                    .build()
            )
        }
    }
}