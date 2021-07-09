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
import com.github.panpf.assemblyadapter.BindingItemFactory
import com.github.panpf.assemblyadapter.sample.base.FragmentContainerActivity
import com.github.panpf.assemblyadapter.sample.bean.Link
import com.github.panpf.assemblyadapter.sample.databinding.ItemLinkBinding

class LinkItemFactory(private val activity: Activity) :
    BindingItemFactory<Link, ItemLinkBinding>(Link::class) {

    override fun createItemViewBinding(
        context: Context, inflater: LayoutInflater, parent: ViewGroup
    ): ItemLinkBinding {
        return ItemLinkBinding.inflate(inflater, parent, false)
    }

    override fun initItem(context: Context, binding: ItemLinkBinding, item: Item<Link>) {
        super.initItem(context, binding, item)
        binding.root.setOnClickListener {
            val data = item.dataOrThrow
            val title = data.title.substringBefore(" - ", data.title)
            val subTitle = data.title.substringAfter(" - ", "")
            context.startActivity(
                FragmentContainerActivity.createIntent(context, title, subTitle, data.fragment)
            )
        }

        binding.root.setOnLongClickListener {
            val data = item.dataOrThrow
            AlertDialog.Builder(activity).apply {
                setMessage(buildString {
                    append("Item（${data.title}）").appendLine()
                    appendLine()
                    append("bindingAdapterPosition: ${item.bindingAdapterPosition}").appendLine()
                    append("absoluteAdapterPosition: ${item.absoluteAdapterPosition}").appendLine()
                    append("data: ${item.dataOrThrow.title}").appendLine()
                })
            }.show()
            true
        }
    }

    override fun bindItemData(
        context: Context,
        binding: ItemLinkBinding,
        item: Item<Link>,
        bindingAdapterPosition: Int,
        absoluteAdapterPosition: Int,
        data: Link
    ) {
        binding.linkItemTitleText.text = data.title
    }
}