/*
 * Copyright (C) 2017 Peng fei Pan <sky@panpf.me>
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
import com.github.panpf.assemblyadapter.DataAdapter
import com.github.panpf.assemblyadapter.Item
import com.github.panpf.assemblyadapter.ItemFactory
import com.github.panpf.assemblyadapter.internal.DataManager
import com.github.panpf.assemblyadapter.internal.ItemManager
import java.util.*

class AssemblyExpandableListAdapter<GROUP_DATA, CHILD_DATA>(
    itemFactoryList: List<ItemFactory<*>>
) : BaseExpandableListAdapter(), AssemblyAdapter, DataAdapter<GROUP_DATA> {

    private val itemManager = ItemManager(itemFactoryList)
    private val dataManager = DataManager<GROUP_DATA> { notifyDataSetChanged() }
    private var callback: Callback? = null

    constructor(
        itemFactoryList: List<ItemFactory<*>>,
        dataList: List<GROUP_DATA>?
    ) : this(itemFactoryList) {
        dataManager.setDataList(dataList)
    }

    override fun getGroupCount(): Int {
        return dataManager.dataCount
    }

    override fun getGroup(groupPosition: Int): GROUP_DATA? {
        return dataManager.getData(groupPosition)
    }

    override fun getGroupId(groupPosition: Int): Long {
        return groupPosition.toLong()
    }

    override fun getGroupTypeCount(): Int {
        return itemManager.itemTypeCount
    }

    override fun getGroupType(groupPosition: Int): Int {
        return itemManager.getItemTypeByData(getGroup(groupPosition))
    }

    override fun getChildrenCount(groupPosition: Int): Int {
        val group = getGroup(groupPosition)
        return if (group is AssemblyExpandableGroup) group.getChildCount() else 0
    }

    override fun getChild(groupPosition: Int, childPosition: Int): CHILD_DATA? {
        val group = getGroup(groupPosition)
        @Suppress("UNCHECKED_CAST")
        return if (group is AssemblyExpandableGroup) group.getChild(childPosition) as CHILD_DATA? else null
    }

    override fun getChildId(groupPosition: Int, childPosition: Int): Long {
        return childPosition.toLong()
    }

    override fun getChildTypeCount(): Int {
        return itemManager.itemTypeCount
    }

    override fun getChildType(groupPosition: Int, childPosition: Int): Int {
        return itemManager.getItemTypeByData(getChild(groupPosition, childPosition))
    }

    override fun hasStableIds(): Boolean {
        return callback != null && callback!!.hasStableIds()
    }

    override fun isChildSelectable(groupPosition: Int, childPosition: Int): Boolean {
        return callback != null && callback!!.isChildSelectable(groupPosition, childPosition)
    }

    override fun getGroupView(
        groupPosition: Int, isExpanded: Boolean, convertView: View?, parent: ViewGroup
    ): View {
        val groupData = getGroup(groupPosition)
        val groupItemView = convertView ?: itemManager.getItemFactoryByData(groupData)
            .dispatchCreateItem(parent).apply {
                getItemView().setTag(R.id.aa_tag_item, this)
            }.getItemView()
        @Suppress("UNCHECKED_CAST")
        val groupItem = groupItemView.getTag(R.id.aa_tag_item) as Item<Any>
        if (groupItem is AssemblyExpandableItem<*>) {
            groupItem.groupPosition = groupPosition
            groupItem.isExpanded = isExpanded
            groupItem.childPosition = -1
            groupItem.isLastChild = false
        }
        groupItem.dispatchBindData(groupPosition, groupData)
        return groupItemView
    }

    override fun getChildView(
        groupPosition: Int, childPosition: Int, isLastChild: Boolean,
        convertView: View?, parent: ViewGroup
    ): View {
        val childData = getChild(groupPosition, childPosition)
        val childItemView = convertView ?: itemManager.getItemFactoryByData(childData)
            .dispatchCreateItem(parent).apply {
                getItemView().setTag(R.id.aa_tag_item, this)
            }.getItemView()
        @Suppress("UNCHECKED_CAST")
        val childItem = childItemView.getTag(R.id.aa_tag_item) as Item<Any>
        if (childItem is AssemblyExpandableItem<*>) {
            childItem.groupPosition = groupPosition
            childItem.isExpanded = false
            childItem.childPosition = childPosition
            childItem.isLastChild = isLastChild
        }
        childItem.dispatchBindData(childPosition, childData)
        return childItemView
    }

    fun setCallback(callback: Callback?) {
        this.callback = callback
    }


    override val dataCount: Int
        get() = dataManager.dataCount

    override val dataListSnapshot: List<GROUP_DATA>
        get() = dataManager.dataListSnapshot

    override fun getData(position: Int): GROUP_DATA? {
        return dataManager.getData(position)
    }

    override fun setDataList(datas: List<GROUP_DATA>?) {
        dataManager.setDataList(datas)
    }

    override fun addData(data: GROUP_DATA): Boolean {
        return dataManager.addData(data)
    }

    override fun addData(index: Int, data: GROUP_DATA) {
        dataManager.addData(index, data)
    }

    override fun addAllData(datas: Collection<GROUP_DATA>?): Boolean {
        return dataManager.addAllData(datas)
    }

    @SafeVarargs
    override fun addAllData(vararg datas: GROUP_DATA): Boolean {
        return dataManager.addAllData(*datas)
    }

    override fun removeData(data: GROUP_DATA): Boolean {
        return dataManager.removeData(data)
    }

    override fun removeData(index: Int): GROUP_DATA? {
        return dataManager.removeData(index)
    }

    override fun removeAllData(datas: Collection<GROUP_DATA>): Boolean {
        return dataManager.removeAllData(datas)
    }

    override fun clearData() {
        dataManager.clearData()
    }

    override fun sortData(comparator: Comparator<GROUP_DATA>) {
        dataManager.sortData(comparator)
    }


    override fun getItemFactoryByItemType(itemType: Int): ItemFactory<*> {
        return itemManager.getItemFactoryByItemType(itemType)
    }

    override fun getItemFactoryByPosition(position: Int): ItemFactory<*> {
        return itemManager.getItemFactoryByData(dataManager.getData(position))
    }


    interface Callback {
        fun hasStableIds(): Boolean
        fun isChildSelectable(groupPosition: Int, childPosition: Int): Boolean
    }
}