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
package com.github.panpf.assemblyadapter.sample.ui.pager

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.os.bundleOf
import com.github.panpf.assemblyadapter.sample.R
import com.github.panpf.assemblyadapter.sample.base.BaseBindingFragment
import com.github.panpf.assemblyadapter.sample.bean.AppsOverview
import com.github.panpf.assemblyadapter.sample.databinding.FragmentAppsOverviewBinding

class AppsOverviewFragment : BaseBindingFragment<FragmentAppsOverviewBinding>() {

    companion object {
        fun createInstance(appsOverview: AppsOverview) = AppsOverviewFragment().apply {
            arguments = bundleOf("appsOverview" to appsOverview)
        }
    }

    override fun createViewBinding(
        inflater: LayoutInflater, parent: ViewGroup?
    ): FragmentAppsOverviewBinding {
        return FragmentAppsOverviewBinding.inflate(inflater, parent, false)
    }

    override fun onInitData(binding: FragmentAppsOverviewBinding, savedInstanceState: Bundle?) {
        val data = arguments?.getParcelable<AppsOverview>("appsOverview")
        binding.appsOverviewContentText.text = requireContext().getString(
            R.string.apps_overview,
            data?.count ?: 0,
            data?.userAppCount ?: 0,
            data?.groupCount ?: 0
        )
    }
}