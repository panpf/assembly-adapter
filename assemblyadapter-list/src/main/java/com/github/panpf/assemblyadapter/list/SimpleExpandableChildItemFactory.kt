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
import android.view.View
import android.view.ViewGroup
import kotlin.reflect.KClass

abstract class SimpleExpandableChildItemFactory<GROUP_DATA : ExpandableGroup, CHILD_DATA : Any>(
    dataClass: KClass<CHILD_DATA>
) : ExpandableChildItemFactory<GROUP_DATA, CHILD_DATA>(dataClass) {

    override fun createItem(parent: ViewGroup): ChildItem<GROUP_DATA, CHILD_DATA> {
        val context = parent.context
        val itemView = createItemView(context, LayoutInflater.from(context), parent)
        return SimpleChildItem(this, itemView).apply {
            initItem(parent.context, itemView, this)
        }
    }

    protected abstract fun createItemView(
        context: Context, inflater: LayoutInflater, parent: ViewGroup
    ): View

    protected open fun initItem(
        context: Context,
        itemView: View,
        item: ChildItem<GROUP_DATA, CHILD_DATA>
    ) {
    }

    protected abstract fun bindItemData(
        context: Context,
        itemView: View,
        item: ChildItem<GROUP_DATA, CHILD_DATA>,
        groupBindingAdapterPosition: Int,
        groupAbsoluteAdapterPosition: Int,
        groupData: GROUP_DATA,
        isLastChild: Boolean,
        bindingAdapterPosition: Int,
        absoluteAdapterPosition: Int,
        data: CHILD_DATA,
    )

    private class SimpleChildItem<GROUP_DATA : ExpandableGroup, CHILD_DATA : Any>(
        private val factory: SimpleExpandableChildItemFactory<GROUP_DATA, CHILD_DATA>, itemView: View
    ) : ChildItem<GROUP_DATA, CHILD_DATA>(itemView) {

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
                itemView,
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