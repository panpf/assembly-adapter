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
package com.github.panpf.assemblyadapter.list

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import kotlin.reflect.KClass

abstract class BindingExpandableChildItemFactory<GROUP_DATA : ExpandableGroup, CHILD_DATA : Any, VIEW_BINDING : ViewBinding>(
    dataClass: KClass<CHILD_DATA>
) : ExpandableChildItemFactory<GROUP_DATA, CHILD_DATA>(dataClass) {

    override fun createItem(parent: ViewGroup): ChildItem<GROUP_DATA, CHILD_DATA> {
        val context = parent.context
        val binding = createItemViewBinding(context, LayoutInflater.from(context), parent)
        return BindingChildItem(this, binding).apply {
            initItem(parent.context, binding, this)
        }
    }

    protected abstract fun createItemViewBinding(
        context: Context, inflater: LayoutInflater, parent: ViewGroup
    ): VIEW_BINDING

    protected open fun initItem(
        context: Context, binding: VIEW_BINDING, item: ChildItem<GROUP_DATA, CHILD_DATA>
    ) {
    }

    protected abstract fun bindItemData(
        context: Context,
        binding: VIEW_BINDING,
        item: ChildItem<GROUP_DATA, CHILD_DATA>,
        groupBindingAdapterPosition: Int,
        groupAbsoluteAdapterPosition: Int,
        groupData: GROUP_DATA,
        isLastChild: Boolean,
        bindingAdapterPosition: Int,
        absoluteAdapterPosition: Int,
        data: CHILD_DATA,
    )

    private class BindingChildItem<GROUP_DATA : ExpandableGroup, CHILD_DATA : Any, VIEW_BINDING : ViewBinding>(
        private val factory: BindingExpandableChildItemFactory<GROUP_DATA, CHILD_DATA, VIEW_BINDING>,
        private val binding: VIEW_BINDING
    ) : ChildItem<GROUP_DATA, CHILD_DATA>(binding.root) {

        override fun bindData(
            groupBindingAdapterPosition: Int,
            groupAbsoluteAdapterPosition: Int,
            groupData: GROUP_DATA,
            isLastChild: Boolean,
            bindingAdapterPosition: Int,
            absoluteAdapterPosition: Int,
            data: CHILD_DATA,
        ) {
            factory.bindItemData(
                context,
                binding,
                this,
                groupBindingAdapterPosition,
                groupAbsoluteAdapterPosition,
                groupDataOrThrow,
                isLastChild,
                bindingAdapterPosition,
                absoluteAdapterPosition,
                data,
            )
        }
    }
}