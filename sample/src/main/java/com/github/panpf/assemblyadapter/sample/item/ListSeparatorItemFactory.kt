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
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.view.updateLayoutParams
import com.github.panpf.assemblyadapter.BindingItemFactory
import com.github.panpf.assemblyadapter.sample.bean.ListSeparator
import com.github.panpf.assemblyadapter.sample.databinding.ItemListSeparatorBinding

open class ListSeparatorItemFactory(
    private val activity: Activity,
    private val hideStartMargin: Boolean = false
) : BindingItemFactory<ListSeparator, ItemListSeparatorBinding>() {

    override fun matchData(data: Any): Boolean {
        return data is ListSeparator
    }

    override fun createItemViewBinding(
        context: Context, inflater: LayoutInflater, parent: ViewGroup
    ): ItemListSeparatorBinding {
        return ItemListSeparatorBinding.inflate(inflater, parent, false)
    }

    override fun initItem(
        context: Context,
        binding: ItemListSeparatorBinding,
        item: Item<ListSeparator>
    ) {
        super.initItem(context, binding, item)
        if (hideStartMargin) {
            binding.listSeparatorItemTitleText.updateLayoutParams<ViewGroup.MarginLayoutParams> {
                leftMargin = 0
                rightMargin = 0
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    marginStart = 0
                    marginEnd = 0
                }
            }
        }

        binding.root.setOnClickListener {
            val data = item.dataOrNull ?: return@setOnClickListener
            val bindingAdapterPosition = item.bindingAdapterPosition
            val absoluteAdapterPosition = item.absoluteAdapterPosition
            Toast.makeText(
                context,
                "${data}: $bindingAdapterPosition/$absoluteAdapterPosition",
                Toast.LENGTH_LONG
            ).show()
        }

        binding.root.setOnLongClickListener {
            val data = item.dataOrNull ?: return@setOnLongClickListener false
            AlertDialog.Builder(activity).apply {
                setMessage(buildString {
                    append("Group（${data.title}）").appendLine()
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
        binding: ItemListSeparatorBinding,
        item: Item<ListSeparator>,
        bindingAdapterPosition: Int,
        data: ListSeparator
    ) {
        binding.listSeparatorItemTitleText.text = data.title
    }
}
