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

import android.database.DataSetObserver
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import com.github.panpf.assemblyadapter.*
import com.github.panpf.assemblyadapter.internal.ItemFactoryStorage
import com.github.panpf.assemblyadapter.ItemId
import com.github.panpf.assemblyadapter.list.R
import com.github.panpf.assemblyadapter.list.internal.AdapterDataObservable

/**
 * Single data version of [AssemblyExpandableListAdapter]
 *
 * @param itemFactoryList The collection of [ItemFactory] or [ExpandableGroupItemFactory] or [ExpandableChildItemFactory] passed in from outside, cannot be empty.
 * Each type of data in the data set must have a matching [ItemFactory], otherwise an exception will be thrown
 * @param initData Initial data
 * @see ItemFactory
 * @see ExpandableGroupItemFactory
 * @see ExpandableChildItemFactory
 */
open class AssemblySingleDataExpandableListAdapter<GROUP_DATA : Any, CHILD_DATA>(
    itemFactoryList: List<ItemFactory<*>>,
    initData: GROUP_DATA? = null,
) : BaseExpandableListAdapter(), AssemblyAdapter<ItemFactory<*>> {

    private val itemFactoryStorage = ItemFactoryStorage(itemFactoryList)
    private var hasStableIds = false
    private val adapterDataObservable = AdapterDataObservable()

    var data: GROUP_DATA? = initData
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    var isChildSelectable: ((groupPosition: Int, childPosition: Int) -> Boolean)? = null

    constructor(
        itemFactory: ItemFactory<GROUP_DATA>,
        initData: GROUP_DATA? = null,
    ) : this(listOf(itemFactory), initData)

    init {
        require(itemFactoryList.isNotEmpty()) { "itemFactoryList Can not be empty" }
    }

    /**
     * Indicates whether each item in the data set can be represented with a unique identifier
     * of type [java.lang.Long].
     *
     * @param hasStableIds Whether items in data set have unique identifiers or not.
     * @see hasStableIds
     * @see getGroupId
     * @see getChildId
     */
    fun setHasStableIds(hasStableIds: Boolean) {
        if (hasObservers()) {
            throw IllegalStateException(
                "Cannot change whether this adapter has "
                        + "stable IDs while the adapter has registered observers."
            )
        }
        this.hasStableIds = hasStableIds
    }

    override fun hasStableIds(): Boolean = hasStableIds


    override fun getGroupCount(): Int = if (data != null) 1 else 0

    override fun getGroup(groupPosition: Int): GROUP_DATA {
        val groupCount = groupCount
        if (groupPosition < 0 || groupPosition >= groupCount) {
            throw IndexOutOfBoundsException("Index: $groupPosition, Size: $groupCount")
        }
        return data!!
    }

    override fun getGroupId(groupPosition: Int): Long {
        return if (hasStableIds()) {
            val data = getGroup(groupPosition)
            if (data is ItemId) data.itemId else data.hashCode().toLong()
        } else {
            -1
        }
    }

    override fun getGroupTypeCount(): Int = itemFactoryStorage.itemTypeCount

    override fun getGroupType(groupPosition: Int): Int {
        val groupCount = groupCount
        if (groupPosition < 0 || groupPosition >= groupCount) {
            throw IndexOutOfBoundsException("Index: $groupPosition, Size: $groupCount")
        }
        return itemFactoryStorage.getItemTypeByData(
            data!!, "ItemFactory", "AssemblySingleDataExpandableListAdapter", "itemFactoryList"
        )
    }

    override fun getGroupView(
        groupPosition: Int, isExpanded: Boolean, convertView: View?, parent: ViewGroup
    ): View {
        val groupCount = groupCount
        if (groupPosition < 0 || groupPosition >= groupCount) {
            throw IndexOutOfBoundsException("Index: $groupPosition, Size: $groupCount")
        }

        val groupData = data!!
        val groupItemView = convertView ?: itemFactoryStorage.getItemFactoryByData(
            groupData, "ItemFactory", "AssemblySingleDataExpandableListAdapter", "itemFactoryList"
        ).dispatchCreateItem(parent).apply {
            itemView.setTag(R.id.aa_tag_item, this)
        }.itemView

        @Suppress("UnnecessaryVariable") val groupBindingAdapterPosition = groupPosition
        val groupAbsolutePositionObject = parent.getTag(R.id.aa_tag_absoluteAdapterPosition)
        // set tag absoluteAdapterPosition null to support ConcatExpandableListAdapter nesting
        parent.setTag(R.id.aa_tag_absoluteAdapterPosition, null)
        val groupAbsoluteAdapterPosition =
            (groupAbsolutePositionObject as Int?) ?: groupBindingAdapterPosition

        @Suppress("UNCHECKED_CAST")
        val groupItem = groupItemView.getTag(R.id.aa_tag_item) as Item<Any>
        when (groupItem) {
            is ExpandableGroupItem<*> -> {
                @Suppress("UNCHECKED_CAST")
                (groupItem as ExpandableGroupItem<ExpandableGroup>)
                    .dispatchGroupBindData(
                        isExpanded,
                        groupBindingAdapterPosition,
                        groupAbsoluteAdapterPosition,
                        groupData as ExpandableGroup,
                    )
            }
            is ExpandableChildItem<*, *> -> {
                throw IllegalArgumentException("groupData '${groupData.javaClass.name}' can not match ExpandableChildItemFactory")
            }
            else -> {
                groupItem.dispatchBindData(
                    groupBindingAdapterPosition, groupAbsoluteAdapterPosition, groupData
                )
            }
        }
        return groupItemView
    }


    override fun getChildrenCount(groupPosition: Int): Int {
        val groupCount = groupCount
        if (groupPosition < 0 || groupPosition >= groupCount) {
            throw IndexOutOfBoundsException("Index: $groupPosition, Size: $groupCount")
        }
        val groupData = data!!
        return if (groupData is ExpandableGroup) groupData.getChildCount() else 0
    }

    override fun getChild(groupPosition: Int, childPosition: Int): CHILD_DATA {
        val groupCount = groupCount
        if (groupPosition < 0 || groupPosition >= groupCount) {
            throw IndexOutOfBoundsException("Index: $groupPosition, Size: $groupCount")
        }

        val groupData = data!!
        if (groupData is ExpandableGroup) {
            @Suppress("UNCHECKED_CAST")
            return groupData.getChild(childPosition) as CHILD_DATA
        } else {
            throw IllegalArgumentException("group item must implement ExpandableGroup interface. '${groupData.javaClass.name}'")
        }
    }

    override fun getChildId(groupPosition: Int, childPosition: Int): Long {
        return if (hasStableIds) {
            val data = getChild(groupPosition, childPosition)!!
            if (data is ItemId) data.itemId else data.hashCode().toLong()
        } else {
            -1
        }
    }

    override fun getChildTypeCount(): Int = itemFactoryStorage.itemTypeCount

    override fun getChildType(groupPosition: Int, childPosition: Int): Int {
        val childData = getChild(groupPosition, childPosition)!!
        return itemFactoryStorage.getItemTypeByData(
            childData, "ItemFactory", "AssemblySingleDataExpandableListAdapter", "itemFactoryList"
        )
    }

    override fun getChildView(
        groupPosition: Int, childPosition: Int, isLastChild: Boolean,
        convertView: View?, parent: ViewGroup
    ): View {
        val groupCount = groupCount
        if (groupPosition < 0 || groupPosition >= groupCount) {
            throw IndexOutOfBoundsException("Index: $groupPosition, Size: $groupCount")
        }
        val groupData = data!!
        val childData = getChild(groupPosition, childPosition)!!
        val childItemView = convertView ?: itemFactoryStorage.getItemFactoryByData(
            childData, "ItemFactory", "AssemblySingleDataExpandableListAdapter", "itemFactoryList"
        ).dispatchCreateItem(parent).apply {
            itemView.setTag(R.id.aa_tag_item, this)
        }.itemView

        @Suppress("UnnecessaryVariable") val groupBindingAdapterPosition = groupPosition
        val groupAbsolutePositionObject = parent.getTag(R.id.aa_tag_absoluteAdapterPosition)
        // set tag absoluteAdapterPosition null to support ConcatExpandableListAdapter nesting
        parent.setTag(R.id.aa_tag_absoluteAdapterPosition, null)
        val groupAbsoluteAdapterPosition =
            (groupAbsolutePositionObject as Int?) ?: groupBindingAdapterPosition

        @Suppress("UNCHECKED_CAST")
        val childItem = childItemView.getTag(R.id.aa_tag_item) as Item<Any>
        when (childItem) {
            is ExpandableChildItem<*, *> -> {
                @Suppress("UNCHECKED_CAST")
                (childItem as ExpandableChildItem<ExpandableGroup, Any>)
                    .dispatchChildBindData(
                        groupBindingAdapterPosition,
                        groupAbsoluteAdapterPosition,
                        groupData as ExpandableGroup,
                        isLastChild,
                        childPosition,
                        childPosition,
                        childData
                    )
            }
            is ExpandableGroupItem<*> -> {
                throw IllegalArgumentException("childData '${childData.javaClass.name}' can not match ExpandableGroupItemFactory")
            }
            else -> {
                childItem.dispatchBindData(childPosition, childPosition, childData)
            }
        }
        return childItemView
    }


    override fun isChildSelectable(groupPosition: Int, childPosition: Int): Boolean {
        val groupCount = groupCount
        if (groupPosition < 0 || groupPosition >= groupCount) {
            throw IndexOutOfBoundsException("Index: $groupPosition, Size: $groupCount")
        }
        return isChildSelectable?.invoke(groupPosition, childPosition) == true
    }


    override fun getItemFactoryByPosition(position: Int): ItemFactory<*> {
        val data = getGroup(position)
        return itemFactoryStorage.getItemFactoryByData(
            data, "ItemFactory", "AssemblySingleDataExpandableListAdapter", "itemFactoryList"
        )
    }

    /**
     * Get the ItemFactory of the specified [childPosition]
     *
     * @throws IndexOutOfBoundsException If the [groupPosition] or [childPosition] is out of range
     * @throws NotFoundMatchedItemFactoryException No ItemFactory can match the data corresponding to [childPosition]
     */
    fun getItemFactoryByChildPosition(groupPosition: Int, childPosition: Int): ItemFactory<*> {
        val data = getChild(groupPosition, childPosition) ?: Placeholder
        return itemFactoryStorage.getItemFactoryByData(
            data, "ItemFactory", "AssemblyExpandableListAdapter", "itemFactoryList"
        )
    }

    override fun registerDataSetObserver(observer: DataSetObserver?) {
        super.registerDataSetObserver(observer)
        adapterDataObservable.registerObserver(observer)
    }

    override fun unregisterDataSetObserver(observer: DataSetObserver?) {
        super.unregisterDataSetObserver(observer)
        adapterDataObservable.unregisterObserver(observer)
    }

    /**
     * Returns true if one or more observers are attached to this adapter.
     *
     * @return true if this adapter has observers
     */
    fun hasObservers(): Boolean {
        return adapterDataObservable.hasObservers()
    }
}