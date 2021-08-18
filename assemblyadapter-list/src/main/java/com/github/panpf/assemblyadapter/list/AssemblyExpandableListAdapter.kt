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
import android.widget.BaseExpandableListAdapter
import com.github.panpf.assemblyadapter.AssemblyAdapter
import com.github.panpf.assemblyadapter.Item
import com.github.panpf.assemblyadapter.ItemFactory
import com.github.panpf.assemblyadapter.Placeholder
import com.github.panpf.assemblyadapter.internal.ItemDataStorage
import com.github.panpf.assemblyadapter.internal.ItemFactoryStorage

/**
 * An implementation of [BaseExpandableListAdapter], which implements multi-type adapters through standardized [ItemFactory] or [ExpandableGroupItemFactory] or [ExpandableChildItemFactory].
 * [AssemblyExpandableListAdapter] will use the data corresponding to position to find a matching [ItemFactory] (cannot find an exception will be thrown),
 * and then use [ItemFactory] to create an item view and bind the data.
 *
 * If you need [BaseExpandableListAdapter] specific data such as isExpanded, isLastChild, etc., then you have to use [ExpandableGroupItemFactory] and [ExpandableChildItemFactory]
 *
 * @param itemFactoryList The collection of [ItemFactory] or [ExpandableGroupItemFactory] or [ExpandableChildItemFactory] passed in from outside, cannot be empty.
 * Each type of data in the data set must have a matching [ItemFactory], otherwise an exception will be thrown
 * @param initDataList Initial data set
 * @see ItemFactory
 * @see ExpandableGroupItemFactory
 * @see ExpandableChildItemFactory
 */
open class AssemblyExpandableListAdapter<GROUP_DATA, CHILD_DATA>(
    itemFactoryList: List<ItemFactory<*>>,
    initDataList: List<GROUP_DATA>? = null
) : BaseExpandableListAdapter(), AssemblyAdapter<ItemFactory<*>> {

    private val itemFactoryStorage = ItemFactoryStorage(itemFactoryList)
    private val itemDataStorage = ItemDataStorage(initDataList) { notifyDataSetChanged() }

    /**
     * Get the current list. If a null list is submitted through [submitList], or no list is submitted, an empty list will be returned.
     * The returned list may not change-changes to the content must be passed through [submitList].
     */
    val currentList: List<GROUP_DATA>
        get() = itemDataStorage.readOnlyList

    init {
        require(itemFactoryList.isNotEmpty()) { "itemFactoryList Can not be empty" }
    }

    /**
     * Set the new list to be displayed.
     */
    fun submitList(list: List<GROUP_DATA>?) {
        itemDataStorage.submitList(list)
    }


    override fun getGroupCount(): Int = itemDataStorage.dataCount

    override fun getGroup(groupPosition: Int): GROUP_DATA = itemDataStorage.getData(groupPosition)

    override fun getGroupId(groupPosition: Int): Long = groupPosition.toLong()

    override fun getGroupTypeCount(): Int = itemFactoryStorage.itemTypeCount

    override fun getGroupType(groupPosition: Int): Int {
        val data = itemDataStorage.getData(groupPosition) ?: Placeholder
        return itemFactoryStorage.getItemTypeByData(
            data, "ItemFactory", "AssemblyExpandableListAdapter", "itemFactoryList"
        )
    }

    override fun getGroupView(
        groupPosition: Int, isExpanded: Boolean, convertView: View?, parent: ViewGroup
    ): View {
        val groupData = itemDataStorage.getData(groupPosition) ?: Placeholder
        val groupItemView = convertView ?: itemFactoryStorage.getItemFactoryByData(
            groupData, "ItemFactory", "AssemblyExpandableListAdapter", "itemFactoryList"
        ).dispatchCreateItem(parent).apply {
            itemView.setTag(R.id.aa_tag_item, this)
        }.itemView

        @Suppress("UnnecessaryVariable")
        val groupBindingAdapterPosition = groupPosition
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
                throw IllegalArgumentException("groupData '${groupData.javaClass.name}' need a matching GroupItemFactory, not ChildItemFactory")
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
        val groupData = itemDataStorage.getData(groupPosition)
        return if (groupData is ExpandableGroup) groupData.getChildCount() else 0
    }

    override fun getChild(groupPosition: Int, childPosition: Int): CHILD_DATA {
        val groupData = itemDataStorage.getData(groupPosition)!!
        if (groupData is ExpandableGroup) {
            @Suppress("UNCHECKED_CAST")
            return groupData.getChild(childPosition) as CHILD_DATA
        } else {
            throw IllegalArgumentException("group item must implement ExpandableGroup interface. '${groupData.javaClass.name}'")
        }
    }

    override fun getChildId(groupPosition: Int, childPosition: Int): Long = childPosition.toLong()

    override fun getChildTypeCount(): Int = itemFactoryStorage.itemTypeCount

    override fun getChildType(groupPosition: Int, childPosition: Int): Int {
        val childData = getChild(groupPosition, childPosition)!!
        return itemFactoryStorage.getItemTypeByData(
            childData, "ItemFactory", "AssemblyExpandableListAdapter", "itemFactoryList"
        )
    }

    override fun getChildView(
        groupPosition: Int, childPosition: Int, isLastChild: Boolean,
        convertView: View?, parent: ViewGroup
    ): View {
        val groupData = itemDataStorage.getData(groupPosition)
        val childData = getChild(groupPosition, childPosition)!!
        val childItemView = convertView ?: itemFactoryStorage.getItemFactoryByData(
            childData, "ItemFactory", "AssemblyExpandableListAdapter", "itemFactoryList"
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
                throw IllegalArgumentException("childData '${childData.javaClass.name}' need a matching ChildItemFactory, not GroupItemFactory")
            }
            else -> {
                childItem.dispatchBindData(childPosition, childPosition, childData)
            }
        }
        return childItemView
    }


    override fun hasStableIds(): Boolean = false

    override fun isChildSelectable(groupPosition: Int, childPosition: Int): Boolean {
        return false
    }


    override fun getItemFactoryByPosition(position: Int): ItemFactory<*> {
        val data = itemDataStorage.getData(position) ?: Placeholder
        return itemFactoryStorage.getItemFactoryByData(
            data, "ItemFactory", "AssemblyExpandableListAdapter", "itemFactoryList"
        )
    }
}