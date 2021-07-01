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
import com.github.panpf.assemblyadapter.*
import com.github.panpf.assemblyadapter.internal.ItemDataStorage
import com.github.panpf.assemblyadapter.internal.ItemFactoryStorage
import com.github.panpf.assemblyadapter.list.R
import java.util.*

open class AssemblyExpandableListAdapter<GROUP_DATA, CHILD_DATA>(
    itemFactoryList: List<ItemFactory<*>>,
    placeholderItemFactory: ItemFactory<Placeholder>? = null,
    dataList: List<GROUP_DATA>? = null
) : BaseExpandableListAdapter(), AssemblyAdapter, DatasAdapter<GROUP_DATA> {

    private val itemFactoryStorage = ItemFactoryStorage(
        if (placeholderItemFactory != null) itemFactoryList.plus(placeholderItemFactory) else itemFactoryList
    )
    private val itemDataStorage = ItemDataStorage(dataList) { notifyDataSetChanged() }

    var hasStableIds = false
    var isChildSelectable: ((groupPosition: Int, childPosition: Int) -> Boolean)? = null

    constructor(
        itemFactoryList: List<ItemFactory<*>>,
        placeholderItemFactory: ItemFactory<Placeholder>?,
    ) : this(itemFactoryList, placeholderItemFactory, null)

    constructor(
        itemFactoryList: List<ItemFactory<*>>,
        dataList: List<GROUP_DATA>?
    ) : this(itemFactoryList, null, dataList)

    constructor(itemFactoryList: List<ItemFactory<*>>) : this(itemFactoryList, null, null)

    init {
        placeholderItemFactory?.apply {
            if (!matchData(Placeholder)) {
                throw IllegalArgumentException("'${placeholderItemFactory::class.java.name}' 's match(Any) method must return true when passing in Placeholder")
            }
            if (matchData(0)) {
                throw IllegalArgumentException("'${placeholderItemFactory::class.java.name}' 's match(Any) method must return false when passing in non Placeholder")
            }
        }
    }


    override fun getGroupCount(): Int = itemDataStorage.dataCount

    override fun getGroup(groupPosition: Int): GROUP_DATA = itemDataStorage.getData(groupPosition)

    override fun getGroupId(groupPosition: Int): Long = groupPosition.toLong()

    override fun getGroupTypeCount(): Int = itemFactoryStorage.itemTypeCount

    override fun getGroupType(groupPosition: Int): Int {
        val data = itemDataStorage.getData(groupPosition) ?: Placeholder
        return itemFactoryStorage.getItemTypeByData(data)
    }

    override fun getGroupView(
        groupPosition: Int, isExpanded: Boolean, convertView: View?, parent: ViewGroup
    ): View {
        val groupData = itemDataStorage.getData(groupPosition) ?: Placeholder
        val groupItemView =
            convertView ?: itemFactoryStorage.getItemFactoryByData(groupData)
                .dispatchCreateItem(parent).apply {
                    itemView.setTag(R.id.aa_tag_item, this)
                }.itemView

        @Suppress("UnnecessaryVariable")
        val groupBindingAdapterPosition = groupPosition
        val groupAbsolutePositionObject = parent.getTag(R.id.aa_tag_absoluteAdapterPosition)
        val groupAbsoluteAdapterPosition =
            (groupAbsolutePositionObject as Int?) ?: groupBindingAdapterPosition

        @Suppress("UNCHECKED_CAST")
        when (val groupItem = groupItemView.getTag(R.id.aa_tag_item) as ItemFactory.Item<Any>) {
            is ExpandableItemFactory.ExpandableItem<Any> -> groupItem.dispatchExpandableBindData(
                groupBindingAdapterPosition,
                groupAbsoluteAdapterPosition,
                groupBindingAdapterPosition,
                groupAbsoluteAdapterPosition,
                -1,
                isExpanded,
                false,
                groupData
            )
            else -> groupItem.dispatchBindData(
                groupBindingAdapterPosition,
                groupAbsoluteAdapterPosition,
                groupData
            )
        }
        return groupItemView
    }


    override fun getChildrenCount(groupPosition: Int): Int {
        val group = itemDataStorage.getData(groupPosition)
        return if (group is ExpandableGroup) group.getChildCount() else 0
    }

    override fun getChild(groupPosition: Int, childPosition: Int): CHILD_DATA {
        val group = itemDataStorage.getData(groupPosition)!!
        if (group is ExpandableGroup) {
            @Suppress("UNCHECKED_CAST")
            return group.getChild(childPosition) as CHILD_DATA
        }
        throw IllegalArgumentException("group item must implement ExpandableGroup interface. '${group::class.java.name}'")
    }

    override fun getChildId(groupPosition: Int, childPosition: Int): Long = childPosition.toLong()

    override fun getChildTypeCount(): Int = itemFactoryStorage.itemTypeCount

    override fun getChildType(groupPosition: Int, childPosition: Int): Int {
        return itemFactoryStorage.getItemTypeByData(getChild(groupPosition, childPosition)!!)
    }

    override fun getChildView(
        groupPosition: Int, childPosition: Int, isLastChild: Boolean,
        convertView: View?, parent: ViewGroup
    ): View {
        val childData = getChild(groupPosition, childPosition)!!
        val childItemView = convertView ?: itemFactoryStorage.getItemFactoryByData(childData)
            .dispatchCreateItem(parent).apply {
                itemView.setTag(R.id.aa_tag_item, this)
            }.itemView

        @Suppress("UnnecessaryVariable") val groupBindingAdapterPosition = groupPosition
        val groupAbsolutePositionObject = parent.getTag(R.id.aa_tag_absoluteAdapterPosition)
        val groupAbsoluteAdapterPosition =
            (groupAbsolutePositionObject as Int?) ?: groupBindingAdapterPosition

        @Suppress("UNCHECKED_CAST")
        val childItem = childItemView.getTag(R.id.aa_tag_item) as ItemFactory.Item<Any>
        if (childItem is ExpandableItemFactory.ExpandableItem<Any>) {
            childItem.dispatchExpandableBindData(
                childPosition,
                childPosition,
                groupBindingAdapterPosition,
                groupAbsoluteAdapterPosition,
                childPosition,
                false,
                isLastChild,
                childData
            )
        } else {
            childItem.dispatchBindData(childPosition, childPosition, childData)
        }
        return childItemView
    }


    override fun hasStableIds(): Boolean = hasStableIds

    override fun isChildSelectable(groupPosition: Int, childPosition: Int): Boolean {
        return isChildSelectable?.invoke(groupPosition, childPosition) == true
    }


    override val dataCount: Int
        get() = itemDataStorage.dataCount

    override val dataListSnapshot: List<GROUP_DATA>
        get() = itemDataStorage.dataListSnapshot

    override fun getData(position: Int): GROUP_DATA {
        return itemDataStorage.getData(position)
    }

    override fun setDataList(datas: List<GROUP_DATA>?) {
        itemDataStorage.setDataList(datas)
    }

    override fun addData(data: GROUP_DATA): Boolean {
        return itemDataStorage.addData(data)
    }

    override fun addData(index: Int, data: GROUP_DATA) {
        itemDataStorage.addData(index, data)
    }

    override fun addAllData(datas: Collection<GROUP_DATA>): Boolean {
        return itemDataStorage.addAllData(datas)
    }

    override fun addAllData(index: Int, datas: Collection<GROUP_DATA>): Boolean {
        return itemDataStorage.addAllData(index, datas)
    }

    override fun removeData(data: GROUP_DATA): Boolean {
        return itemDataStorage.removeData(data)
    }

    override fun removeData(index: Int): GROUP_DATA {
        return itemDataStorage.removeData(index)
    }

    override fun removeAllData(datas: Collection<GROUP_DATA>): Boolean {
        return itemDataStorage.removeAllData(datas)
    }

    override fun clearData() {
        itemDataStorage.clearData()
    }

    override fun sortData(comparator: Comparator<GROUP_DATA>) {
        itemDataStorage.sortData(comparator)
    }


    override fun getItemFactoryByPosition(position: Int): ItemFactory<*> {
        val data = itemDataStorage.getData(position) ?: Placeholder
        return itemFactoryStorage.getItemFactoryByData(data)
    }


    class Builder<GROUP_DATA, CHILD_DATA>(private val itemFactoryList: List<ItemFactory<*>>) {

        private var dataList: List<GROUP_DATA>? = null
        private var placeholderItemFactory: ItemFactory<Placeholder>? = null

        fun setDataList(dataList: List<GROUP_DATA>?) {
            this.dataList = dataList
        }

        fun setPlaceholderItemFactory(placeholderItemFactory: ItemFactory<Placeholder>?) {
            this.placeholderItemFactory = placeholderItemFactory
        }

        fun build(): AssemblyExpandableListAdapter<GROUP_DATA, CHILD_DATA> {
            return AssemblyExpandableListAdapter(itemFactoryList, placeholderItemFactory, dataList)
        }
    }
}