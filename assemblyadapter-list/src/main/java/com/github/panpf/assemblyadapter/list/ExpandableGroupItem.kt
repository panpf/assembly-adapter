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

import android.view.View
import android.view.ViewGroup
import com.github.panpf.assemblyadapter.Item

/**
 * Specially used in the [AssemblyExpandableListAdapter.getGroupView] method, the [isExpanded] parameter is added on the basis of the normal [Item]
 *
 * Need to be used with [ExpandableGroupItemFactory]
 *
 * @see ExpandableGroupItemFactory
 */
abstract class ExpandableGroupItem<DATA : Any> : Item<DATA> {

    private var _isExpanded = false

    /**
     * Whether the group is expanded or collapsed
     */
    val isExpanded: Boolean
        get() = _isExpanded

    constructor(itemView: View) : super(itemView)

    constructor(itemLayoutId: Int, parent: ViewGroup) : super(itemLayoutId, parent)

    /**
     * Bind data to item view. [AssemblyExpandableListAdapter] dedicated
     *
     * @param isExpanded whether the group is expanded or collapsed
     * @param bindingAdapterPosition The position of the current item in its directly bound adapter.
     * For its specific meaning, please refer to the RecyclerView.ViewHolder.getBindingAdapterPosition() method.
     * This value will be different when using Concat*Adapter
     * @param absoluteAdapterPosition The position of the current item in the RecyclerView.adapter adapter.
     * For the specific meaning, please refer to the RecyclerView.ViewHolder.getAbsoluteAdapterPosition() method.
     * This value will be different when using Concat*Adapter
     * @param data Data to be bound
     */
    fun dispatchGroupBindData(
        isExpanded: Boolean,
        bindingAdapterPosition: Int,
        absoluteAdapterPosition: Int,
        data: DATA,
    ) {
        this._isExpanded = isExpanded
        super.dispatchBindData(bindingAdapterPosition, absoluteAdapterPosition, data)
    }

    final override fun bindData(
        bindingAdapterPosition: Int,
        absoluteAdapterPosition: Int,
        data: DATA
    ) {
        bindData(
            isExpanded,
            bindingAdapterPosition,
            absoluteAdapterPosition,
            data,
        )
    }

    /**
     * Bind data to item view
     *
     * @param isExpanded whether the group is expanded or collapsed
     * @param bindingAdapterPosition The position of the current item in its directly bound adapter.
     * For its specific meaning, please refer to the RecyclerView.ViewHolder.getBindingAdapterPosition() method.
     * This value will be different when using Concat*Adapter
     * @param absoluteAdapterPosition The position of the current item in the RecyclerView.adapter adapter.
     * For the specific meaning, please refer to the RecyclerView.ViewHolder.getAbsoluteAdapterPosition() method.
     * This value will be different when using Concat*Adapter
     * @param data Data to be bound
     */
    protected abstract fun bindData(
        isExpanded: Boolean,
        bindingAdapterPosition: Int,
        absoluteAdapterPosition: Int,
        data: DATA,
    )
}