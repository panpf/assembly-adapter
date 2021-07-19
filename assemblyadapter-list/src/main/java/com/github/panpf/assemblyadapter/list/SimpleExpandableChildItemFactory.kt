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

/**
 * Simplified version of [ExpandableGroupItemFactory]. Users do not need to define Item,
 * which can greatly simplify the implementation logic of custom [ExpandableGroupItemFactory]
 *
 * @param GROUP_DATA Define the type of group data
 * @param CHILD_DATA Define the type of matching data
 * @param dataClass The class of data that can be matched. By default, as long as the given data is an instance of this class,
 * it is considered a match. You can also override the [exactMatchData] method to achieve exact matching
 */
abstract class SimpleExpandableChildItemFactory<GROUP_DATA : ExpandableGroup, CHILD_DATA : Any>(
    dataClass: KClass<CHILD_DATA>
) : ExpandableChildItemFactory<GROUP_DATA, CHILD_DATA>(dataClass) {

    override fun createItem(parent: ViewGroup): ExpandableChildItem<GROUP_DATA, CHILD_DATA> {
        val context = parent.context
        val itemView = createItemView(context, LayoutInflater.from(context), parent)
        return SimpleExpandableChildItem(this, itemView).apply {
            initItem(parent.context, itemView, this)
        }
    }

    /**
     * Create the view required for the item
     */
    protected abstract fun createItemView(
        context: Context, inflater: LayoutInflater, parent: ViewGroup
    ): View

    /**
     * Initialize the item, this method is only executed once when the item is created
     */
    protected open fun initItem(
        context: Context,
        itemView: View,
        item: ExpandableChildItem<GROUP_DATA, CHILD_DATA>
    ) {
    }

    /**
     * Binding item data, this method will be executed frequently
     *
     * @param groupBindingAdapterPosition The bindingAdapterPosition of the group to which the current child item belongs
     * @param groupAbsoluteAdapterPosition The absoluteAdapterPosition of the group to which the current child item belongs
     * @param groupData The group to which the current child item belongs
     * @param isLastChild Whether the child is the last child within the group
     * @param bindingAdapterPosition The position of the current item in its directly bound adapter.
     * For its specific meaning, please refer to the RecyclerView.ViewHolder.getBindingAdapterPosition() method.
     * This value will be different when using Concat*Adapter
     * @param absoluteAdapterPosition The position of the current item in the RecyclerView.adapter adapter.
     * For the specific meaning, please refer to the RecyclerView.ViewHolder.getAbsoluteAdapterPosition() method.
     * This value will be different when using Concat*Adapter
     * @param data Data to be bound
     */
    protected abstract fun bindItemData(
        context: Context,
        itemView: View,
        item: SimpleExpandableChildItem<GROUP_DATA, CHILD_DATA>,
        groupBindingAdapterPosition: Int,
        groupAbsoluteAdapterPosition: Int,
        groupData: GROUP_DATA,
        isLastChild: Boolean,
        bindingAdapterPosition: Int,
        absoluteAdapterPosition: Int,
        data: CHILD_DATA,
    )

    class SimpleExpandableChildItem<GROUP_DATA : ExpandableGroup, CHILD_DATA : Any>(
        val factory: SimpleExpandableChildItemFactory<GROUP_DATA, CHILD_DATA>,
        itemView: View
    ) : ExpandableChildItem<GROUP_DATA, CHILD_DATA>(itemView) {

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