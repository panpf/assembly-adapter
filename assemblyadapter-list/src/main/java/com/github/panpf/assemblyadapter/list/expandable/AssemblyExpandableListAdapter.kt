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
import com.github.panpf.assemblyadapter.internal.ItemDataStorage
import com.github.panpf.assemblyadapter.internal.ItemFactoryStorage
import com.github.panpf.assemblyadapter.list.R
import com.github.panpf.assemblyadapter.list.internal.AdapterDataObservable
import kotlin.reflect.KClass

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
open class AssemblyExpandableListAdapter<GROUP_DATA, CHILD_DATA: Any>(
    itemFactoryList: List<ItemFactory<out Any>>,
    initDataList: List<GROUP_DATA>? = null,
) : BaseExpandableListAdapter(), AssemblyAdapter<GROUP_DATA, ItemFactory<out Any>> {

    private val itemFactoryStorage = ItemFactoryStorage(
        itemFactoryList, "ItemFactory", "AssemblyExpandableListAdapter", "itemFactoryList"
    )
    private val itemDataStorage = ItemDataStorage(initDataList) { _, _ -> notifyDataSetChanged() }
    private var hasStableIds = false
    private val adapterDataObservable = AdapterDataObservable()

    /**
     * Get the current list. If a null list is submitted through [submitList], or no list is submitted, an empty list will be returned.
     * The returned list may not change-changes to the content must be passed through [submitList].
     */
    val currentList: List<GROUP_DATA>
        get() = itemDataStorage.readOnlyList

    var childSelectable: ((
        adapter: AssemblyExpandableListAdapter<GROUP_DATA, CHILD_DATA>, groupPosition: Int, childPosition: Int
    ) -> Boolean)? = null

    init {
        require(itemFactoryList.isNotEmpty()) { "itemFactoryList Can not be empty" }
    }

    /**
     * Set the new list to be displayed.
     */
    open fun submitList(list: List<GROUP_DATA>?) {
        itemDataStorage.submitList(list)
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


    val itemCount: Int
        get() = itemDataStorage.dataCount

    fun getItemData(position: Int): GROUP_DATA {
        return itemDataStorage.getData(position)
    }

    override fun getGroupCount(): Int = itemDataStorage.dataCount

    override fun getGroup(groupPosition: Int): GROUP_DATA = itemDataStorage.getData(groupPosition)

    override fun getGroupId(groupPosition: Int): Long {
        return if (hasStableIds()) {
            val groupData = getGroup(groupPosition) ?: Placeholder
            if (groupData is ItemId) groupData.itemId else groupData.hashCode().toLong()
        } else {
            -1
        }
    }

    override fun getGroupTypeCount(): Int = itemFactoryStorage.itemTypeCount

    override fun getGroupType(groupPosition: Int): Int {
        val groupData = getGroup(groupPosition) ?: Placeholder
        return itemFactoryStorage.getItemTypeByData(groupData)
    }

    override fun getGroupView(
        groupPosition: Int, isExpanded: Boolean, convertView: View?, parent: ViewGroup
    ): View {
        val groupData = getGroup(groupPosition) ?: Placeholder
        val groupItemView = convertView ?: itemFactoryStorage.getItemFactoryByData(groupData)
            .dispatchCreateItem(parent).apply {
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
        val groupData = getGroup(groupPosition)
        return if (groupData is ExpandableGroup) groupData.getChildCount() else 0
    }

    override fun getChild(groupPosition: Int, childPosition: Int): CHILD_DATA {
        val groupData = getGroup(groupPosition)!!
        if (groupData is ExpandableGroup) {
            @Suppress("UNCHECKED_CAST")
            return groupData.getChild(childPosition) as CHILD_DATA
        } else {
            throw IllegalArgumentException("group item must implement ExpandableGroup interface. '${groupData.javaClass.name}'")
        }
    }

    override fun getChildId(groupPosition: Int, childPosition: Int): Long {
        return if (hasStableIds) {
            val childData = getChild(groupPosition, childPosition)
            if (childData is ItemId) childData.itemId else childData.hashCode().toLong()
        } else {
            -1
        }
    }

    override fun getChildTypeCount(): Int = itemFactoryStorage.itemTypeCount

    override fun getChildType(groupPosition: Int, childPosition: Int): Int {
        val childData = getChild(groupPosition, childPosition)
        return itemFactoryStorage.getItemTypeByData(childData)
    }

    override fun getChildView(
        groupPosition: Int, childPosition: Int, isLastChild: Boolean,
        convertView: View?, parent: ViewGroup
    ): View {
        val groupData = getGroup(groupPosition)!!
        val childData = getChild(groupPosition, childPosition)
        val childItemView = convertView ?: itemFactoryStorage.getItemFactoryByData(childData)
            .dispatchCreateItem(parent).apply {
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
        return childSelectable?.invoke(this, groupPosition, childPosition) == true
    }


    override fun getItemFactoryByPosition(position: Int): ItemFactory<Any> {
        val groupData = getGroup(position) ?: Placeholder
        return itemFactoryStorage.getItemFactoryByData(groupData) as ItemFactory<Any>
    }

    /**
     * Get the ItemFactory of the specified [childPosition]
     *
     * @throws IndexOutOfBoundsException If the [groupPosition] or [childPosition] is out of range
     * @throws NotFoundMatchedItemFactoryException No ItemFactory can match the data corresponding to [childPosition]
     */
    fun getItemFactoryByChildPosition(groupPosition: Int, childPosition: Int): ItemFactory<Any> {
        val childData = getChild(groupPosition, childPosition)
        return itemFactoryStorage.getItemFactoryByData(childData) as ItemFactory<Any>
    }

    override fun getItemFactoryByData(data: GROUP_DATA): ItemFactory<Any> {
        return itemFactoryStorage.getItemFactoryByData(data ?: Placeholder) as ItemFactory<Any>
    }

    fun getItemFactoryByChildData(data: CHILD_DATA): ItemFactory<Any> {
        return itemFactoryStorage.getItemFactoryByData(data) as ItemFactory<Any>
    }

    override fun <T : ItemFactory<out Any>> getItemFactoryByItemFactoryClass(itemFactoryClass: KClass<T>): T {
        return itemFactoryStorage.getItemFactoryByItemFactoryClass(itemFactoryClass.java)
    }

    override fun <T : ItemFactory<out Any>> getItemFactoryByItemFactoryClass(itemFactoryClass: Class<T>): T {
        return itemFactoryStorage.getItemFactoryByItemFactoryClass(itemFactoryClass)
    }

    override fun registerDataSetObserver(observer: DataSetObserver) {
        super.registerDataSetObserver(observer)
        adapterDataObservable.registerObserver(observer)
    }

    override fun unregisterDataSetObserver(observer: DataSetObserver) {
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