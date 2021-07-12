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
import android.widget.BaseExpandableListAdapter
import com.github.panpf.assemblyadapter.AssemblyAdapter
import com.github.panpf.assemblyadapter.ItemFactory
import com.github.panpf.assemblyadapter.internal.ItemFactoryStorage
import com.github.panpf.assemblyadapter.list.R

open class AssemblySingleDataExpandableListAdapter<GROUP_DATA : Any, CHILD_DATA>(
    itemFactoryList: List<ItemFactory<*>>,
    initData: GROUP_DATA? = null
) : BaseExpandableListAdapter(), AssemblyAdapter<ItemFactory<*>> {

    private val itemFactoryStorage = ItemFactoryStorage(itemFactoryList)

    var data: GROUP_DATA? = initData
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    var hasStableIds = false
    var isChildSelectable: ((groupPosition: Int, childPosition: Int) -> Boolean)? = null

    constructor(
        itemFactory: ItemFactory<GROUP_DATA>,
        initData: GROUP_DATA? = null
    ) : this(listOf(itemFactory), initData)

    init {
        require(itemFactoryList.isNotEmpty()) { "itemFactoryList Can not be empty" }
    }


    override fun getGroupCount(): Int = if (data != null) 1 else 0

    override fun getGroup(groupPosition: Int): GROUP_DATA? = data

    override fun getGroupId(groupPosition: Int): Long = groupPosition.toLong()

    override fun getGroupTypeCount(): Int = itemFactoryStorage.itemTypeCount

    override fun getGroupType(groupPosition: Int): Int {
        val groupData = data!!
        return itemFactoryStorage.getItemTypeByData(
            groupData, "ItemFactory", "AssemblySingleDataExpandableListAdapter", "itemFactoryList"
        )
    }

    override fun getGroupView(
        groupPosition: Int, isExpanded: Boolean, convertView: View?, parent: ViewGroup
    ): View {
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
        val groupItem = groupItemView.getTag(R.id.aa_tag_item) as ItemFactory.Item<Any>
        when (groupItem) {
            is GroupItemFactory.GroupItem<*> -> {
                @Suppress("UNCHECKED_CAST")
                (groupItem as GroupItemFactory.GroupItem<ExpandableGroup>)
                    .dispatchGroupBindData(
                        isExpanded,
                        groupBindingAdapterPosition,
                        groupAbsoluteAdapterPosition,
                        groupData as ExpandableGroup,
                    )
            }
            is ChildItemFactory.ChildItem<*, *> -> {
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
        val groupData = data
        return if (groupData is ExpandableGroup) groupData.getChildCount() else 0
    }

    override fun getChild(groupPosition: Int, childPosition: Int): CHILD_DATA {
        val groupData = data!!
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
            childData, "ItemFactory", "AssemblySingleDataExpandableListAdapter", "itemFactoryList"
        )
    }

    override fun getChildView(
        groupPosition: Int, childPosition: Int, isLastChild: Boolean,
        convertView: View?, parent: ViewGroup
    ): View {
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
        val childItem = childItemView.getTag(R.id.aa_tag_item) as ItemFactory.Item<Any>
        when (childItem) {
            is ChildItemFactory.ChildItem<*, *> -> {
                @Suppress("UNCHECKED_CAST")
                (childItem as ChildItemFactory.ChildItem<ExpandableGroup, Any>)
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
            is GroupItemFactory.GroupItem<*> -> {
                throw IllegalArgumentException("childData '${childData.javaClass.name}' need a matching ChildItemFactory, not GroupItemFactory")
            }
            else -> {
                childItem.dispatchBindData(childPosition, childPosition, childData)
            }
        }
        return childItemView
    }


    override fun hasStableIds(): Boolean = hasStableIds

    override fun isChildSelectable(groupPosition: Int, childPosition: Int): Boolean {
        return isChildSelectable?.invoke(groupPosition, childPosition) == true
    }


    override fun getItemFactoryByPosition(position: Int): ItemFactory<*> {
        val groupData = data!!
        return itemFactoryStorage.getItemFactoryByData(
            groupData, "ItemFactory", "AssemblySingleDataExpandableListAdapter", "itemFactoryList"
        )
    }
}