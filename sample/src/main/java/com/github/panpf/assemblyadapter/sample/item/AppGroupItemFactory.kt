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

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import com.github.panpf.assemblyadapter.list.expandable.BindingExpandableItemFactory
import com.github.panpf.assemblyadapter.sample.R
import com.github.panpf.assemblyadapter.sample.bean.AppGroup
import com.github.panpf.assemblyadapter.sample.databinding.ItemAppGroupBinding

open class AppGroupItemFactory :
    BindingExpandableItemFactory<AppGroup, ItemAppGroupBinding>() {

    override fun match(data: Any): Boolean {
        return data is AppGroup
    }

    override fun createItemViewBinding(
        context: Context, inflater: LayoutInflater, parent: ViewGroup
    ): ItemAppGroupBinding {
        return ItemAppGroupBinding.inflate(inflater, parent, false)
    }

    override fun bindItemData(
        context: Context,
        binding: ItemAppGroupBinding,
        item: ExpandableItem<AppGroup>,
        bindingAdapterPosition: Int,
        data: AppGroup
    ) {
        binding.appGroupItemTitleText.text = data.title
        binding.appGroupItemTitleText.setCompoundDrawablesWithIntrinsicBounds(
            0,
            0,
            if (item.isExpanded) R.drawable.ic_arrow_up else R.drawable.ic_arrow_down,
            0
        )
    }
}
