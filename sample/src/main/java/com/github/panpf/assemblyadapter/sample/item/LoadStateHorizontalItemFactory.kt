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
import com.github.panpf.assemblyadapter.sample.databinding.ItemLoadStateHorizontalBinding

class LoadStateHorizontalItemFactory(private val activity: Activity) :
    BindingItemFactory<LoadState, ItemLoadStateHorizontalBinding>(LoadState::class) {

    override fun createItemViewBinding(
        context: Context, inflater: LayoutInflater, parent: ViewGroup
    ): ItemLoadStateHorizontalBinding = ItemLoadStateHorizontalBinding.inflate(inflater, parent, false)

    override fun initItem(
        context: Context,
        binding: ItemLoadStateHorizontalBinding,
        item: BindingItem<LoadState, ItemLoadStateHorizontalBinding>
    ) {
        binding.root.setOnLongClickListener {
            AlertDialog.Builder(activity).apply {
                setMessage(buildString {
                    append("LoadState").appendLine()
                    appendLine()
                    append("bindingAdapterPosition: ${item.bindingAdapterPosition}").appendLine()
                    append("absoluteAdapterPosition: ${item.absoluteAdapterPosition}").appendLine()
                })
            }.show()
            true
        }
    }

    override fun bindItemData(
        context: Context,
        binding: ItemLoadStateHorizontalBinding,
        item: BindingItem<LoadState, ItemLoadStateHorizontalBinding>,
        bindingAdapterPosition: Int,
        absoluteAdapterPosition: Int,
        data: LoadState
    ) {
        binding.loadStateHorizontalItemLoadingLayout.isVisible = data is LoadState.Loading
        binding.loadStateHorizontalItemErrorText.isVisible = data is LoadState.Error
        binding.loadStateHorizontalItemEndText.isVisible =
            data is LoadState.NotLoading && data.endOfPaginationReached
    }
}