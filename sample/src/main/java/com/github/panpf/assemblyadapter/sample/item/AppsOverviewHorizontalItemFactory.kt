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
import androidx.core.view.updatePadding
import com.github.panpf.assemblyadapter.BindingItemFactory
import com.github.panpf.assemblyadapter.sample.R
import com.github.panpf.assemblyadapter.sample.bean.AppsOverview
import com.github.panpf.assemblyadapter.sample.databinding.ItemAppsOverviewHorizontalBinding

class AppsOverviewHorizontalItemFactory(
    private val activity: Activity,
    private val hideStartMargin: Boolean = false
) :
    BindingItemFactory<AppsOverview, ItemAppsOverviewHorizontalBinding>(AppsOverview::class) {

    override fun createItemViewBinding(
        context: Context, inflater: LayoutInflater, parent: ViewGroup
    ): ItemAppsOverviewHorizontalBinding {
        return ItemAppsOverviewHorizontalBinding.inflate(inflater, parent, false)
    }

    override fun initItem(
        context: Context,
        binding: ItemAppsOverviewHorizontalBinding,
        item: BindingItem<AppsOverview, ItemAppsOverviewHorizontalBinding>
    ) {
        if (hideStartMargin) {
            binding.root.updatePadding(left = 0, right = 0)
        }

        binding.root.setOnLongClickListener {
            AlertDialog.Builder(activity).apply {
                setMessage(buildString {
                    append("AppsOverview").appendLine()
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
        binding: ItemAppsOverviewHorizontalBinding,
        item: BindingItem<AppsOverview, ItemAppsOverviewHorizontalBinding>,
        bindingAdapterPosition: Int,
        absoluteAdapterPosition: Int,
        data: AppsOverview
    ) {
        binding.appsOverviewHorizontalItemContentText.text = context.getString(
            R.string.apps_overview_item, data.count, data.userAppCount, data.groupCount
        )
    }
}
