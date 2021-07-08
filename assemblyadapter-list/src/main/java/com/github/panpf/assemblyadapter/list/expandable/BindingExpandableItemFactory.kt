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
package com.github.panpf.assemblyadapter.list.expandable

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import kotlin.reflect.KClass

abstract class BindingExpandableItemFactory<DATA : Any, VIEW_BINDING : ViewBinding>(
    dataClass: KClass<DATA>
) : ExpandableItemFactory<DATA>(dataClass) {

    override fun createItem(parent: ViewGroup): ExpandableItem<DATA> {
        val context = parent.context
        val binding = createItemViewBinding(context, LayoutInflater.from(context), parent)
        return BindingExpandableItem(this, binding).apply {
            initItem(parent.context, binding, this)
        }
    }

    protected abstract fun createItemViewBinding(
        context: Context, inflater: LayoutInflater, parent: ViewGroup
    ): VIEW_BINDING

    protected open fun initItem(
        context: Context, binding: VIEW_BINDING, item: ExpandableItem<DATA>
    ) {
    }

    protected abstract fun bindItemData(
        context: Context,
        binding: VIEW_BINDING,
        item: ExpandableItem<DATA>,
        bindingAdapterPosition: Int,
        absoluteAdapterPosition: Int,
        data: DATA
    )

    private class BindingExpandableItem<DATA : Any, VIEW_BINDING : ViewBinding>(
        private val factory: BindingExpandableItemFactory<DATA, VIEW_BINDING>,
        private val binding: VIEW_BINDING
    ) : ExpandableItem<DATA>(binding.root) {

        override fun bindData(
            bindingAdapterPosition: Int, absoluteAdapterPosition: Int, data: DATA
        ) {
            factory.bindItemData(
                context, binding, this, bindingAdapterPosition, absoluteAdapterPosition, data
            )
        }
    }
}