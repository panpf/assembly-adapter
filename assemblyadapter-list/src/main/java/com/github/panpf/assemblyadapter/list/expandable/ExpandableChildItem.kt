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

import android.view.View
import android.view.ViewGroup
import com.github.panpf.assemblyadapter.Item

/**
 * Specially used in the [AssemblyExpandableListAdapter.getChildView] method, adding [groupBindingAdapterPosition],
 * [groupAbsoluteAdapterPosition], [groupDataOrThrow], [isLastChild] parameters on the basis of the ordinary [Item].
 *
 * Need to be used with [ExpandableChildItemFactory]
 *
 * @see ExpandableChildItemFactory
 */
abstract class ExpandableChildItem<GROUP_DATA : ExpandableGroup, CHILD_DATA : Any> :
    Item<CHILD_DATA> {

    private var _groupBindingAdapterPosition = -1
    private var _groupAbsoluteAdapterPosition = -1
    private var _groupData: GROUP_DATA? = null
    private var _isLastChild = false

    /**
     * The bindingAdapterPosition of the group to which the current child item belongs
     */
    val groupBindingAdapterPosition: Int
        get() = _groupBindingAdapterPosition

    /**
     * The absoluteAdapterPosition of the group to which the current child item belongs
     */
    val groupAbsoluteAdapterPosition: Int
        get() = _groupAbsoluteAdapterPosition

    /**
     * The group to which the current child item belongs, or null if there is none
     */
    val groupDataOrNull: GROUP_DATA?
        get() = _groupData

    /**
     * The group to which the current child item belongs, throw an exception if there is none
     */
    val groupDataOrThrow: GROUP_DATA
        get() = _groupData!!

    /**
     * Whether the child is the last child within the group
     */
    val isLastChild: Boolean
        get() = _isLastChild

    constructor(itemView: View) : super(itemView)

    constructor(itemLayoutId: Int, parent: ViewGroup) : super(itemLayoutId, parent)

    /**
     * Binding item data, this method will be executed frequently. [AssemblyExpandableListAdapter] dedicated
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
    fun dispatchChildBindData(
        groupBindingAdapterPosition: Int,
        groupAbsoluteAdapterPosition: Int,
        groupData: GROUP_DATA,
        isLastChild: Boolean,
        bindingAdapterPosition: Int,
        absoluteAdapterPosition: Int,
        data: CHILD_DATA,
    ) {
        this._groupBindingAdapterPosition = groupBindingAdapterPosition
        this._groupAbsoluteAdapterPosition = groupAbsoluteAdapterPosition
        this._groupData = groupData
        this._isLastChild = isLastChild
        super.dispatchBindData(bindingAdapterPosition, absoluteAdapterPosition, data)
    }

    final override fun bindData(
        bindingAdapterPosition: Int,
        absoluteAdapterPosition: Int,
        data: CHILD_DATA
    ) {
        bindData(
            groupBindingAdapterPosition,
            groupAbsoluteAdapterPosition,
            groupDataOrThrow,
            isLastChild,
            bindingAdapterPosition,
            absoluteAdapterPosition,
            data,
        )
    }


    /**
     * Bind data to item view
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
    protected abstract fun bindData(
        groupBindingAdapterPosition: Int,
        groupAbsoluteAdapterPosition: Int,
        groupData: GROUP_DATA,
        isLastChild: Boolean,
        bindingAdapterPosition: Int,
        absoluteAdapterPosition: Int,
        data: CHILD_DATA,
    )
}