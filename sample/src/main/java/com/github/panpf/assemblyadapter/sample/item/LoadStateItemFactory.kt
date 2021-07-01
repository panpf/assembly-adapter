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
package com.github.panpf.assemblyadapter.sample.item

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.paging.LoadState
import com.github.panpf.assemblyadapter.BindingItemFactory
import com.github.panpf.assemblyadapter.sample.databinding.ItemLoadStateBinding

class LoadStateItemFactory(private val activity: Activity) :
    BindingItemFactory<LoadState, ItemLoadStateBinding>() {

    override fun matchData(data: Any): Boolean = data is LoadState

    override fun createItemViewBinding(
        context: Context, inflater: LayoutInflater, parent: ViewGroup
    ): ItemLoadStateBinding = ItemLoadStateBinding.inflate(inflater, parent, false)

    override fun initItem(context: Context, binding: ItemLoadStateBinding, item: Item<LoadState>) {
        super.initItem(context, binding, item)

        binding.root.setOnLongClickListener {
            AlertDialog.Builder(activity).apply {
                setMessage(buildString {
                    append("LoadState").appendLine()
                    appendLine()
                    append("bindingAdapterPosition: ${item.bindingAdapterPosition}").appendLine()
                    append("absoluteAdapterPosition: ${item.absoluteAdapterPosition}")
                })
            }.show()
            true
        }
    }

    override fun bindItemData(
        context: Context,
        binding: ItemLoadStateBinding,
        item: Item<LoadState>,
        bindingAdapterPosition: Int,
        data: LoadState
    ) {
        binding.loadStateItemLoadingLayout.isVisible = data is LoadState.Loading
        binding.loadStateItemErrorText.isVisible = data is LoadState.Error
        binding.loadStateItemEndText.isVisible =
            data is LoadState.NotLoading && data.endOfPaginationReached
    }
}