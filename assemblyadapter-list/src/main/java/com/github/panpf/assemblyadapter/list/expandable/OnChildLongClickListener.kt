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
import android.view.View

/**
 * View long click listener, used to easily set long click listener on the View in Item in ItemFactory
 */
fun interface OnChildLongClickListener<GROUP_DATA : ExpandableGroup, CHILD_DATA : Any> {

    /**
     * Long press View
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
     */
    fun onLongClick(
        context: Context,
        view: View,groupBindingAdapterPosition: Int,
        groupAbsoluteAdapterPosition: Int,
        groupData: GROUP_DATA,
        isLastChild: Boolean,
        bindingAdapterPosition: Int,
        absoluteAdapterPosition: Int,
        data: CHILD_DATA
    ): Boolean
}