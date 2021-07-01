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
import androidx.core.view.isVisible
import androidx.paging.LoadState
import com.github.panpf.assemblyadapter.sample.base.BaseBindingFragment
import com.github.panpf.assemblyadapter.sample.bean.LoadStateParcelable
import com.github.panpf.assemblyadapter.sample.databinding.FragmentLoadStateBinding

class LoadStateFragment : BaseBindingFragment<FragmentLoadStateBinding>() {

    companion object {
        fun createInstance(loadState: LoadState) = LoadStateFragment().apply {
            arguments = bundleOf("loadStateParcelable" to LoadStateParcelable(loadState))
        }
    }

    override fun createViewBinding(
        inflater: LayoutInflater, parent: ViewGroup?
    ): FragmentLoadStateBinding {
        return FragmentLoadStateBinding.inflate(inflater, parent, false)
    }

    override fun onInitData(binding: FragmentLoadStateBinding, savedInstanceState: Bundle?) {
        val data =
            arguments?.getParcelable<LoadStateParcelable>("loadStateParcelable")?.toLoadState()
        binding.loadStateLoadingLayout.isVisible = data is LoadState.Loading
        binding.loadStateErrorText.isVisible = data is LoadState.Error
        binding.loadStateEndText.isVisible = data is LoadState.NotLoading && data.endOfPaginationReached
    }
}