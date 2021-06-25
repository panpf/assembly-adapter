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
package com.github.panpf.assemblyadapter.list.expandable

import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import com.github.panpf.assemblyadapter.AssemblyAdapter
import com.github.panpf.assemblyadapter.AssemblyItem
import com.github.panpf.assemblyadapter.AssemblyItemFactory
import com.github.panpf.assemblyadapter.list.R

class AssemblySingleDataExpandableListAdapter<GROUP_DATA> @JvmOverloads constructor(
    private val itemFactory: AssemblyItemFactory<GROUP_DATA>,
    initData: GROUP_DATA? = null
) : BaseExpandableListAdapter(), AssemblyAdapter {

    var data: GROUP_DATA? = initData
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    var hasStableIds = false
    var isChildSelectable: ((groupPosition: Int, childPosition: Int) -> Boolean)? = null


    override fun getGroupCount(): Int = if (data != null) 1 else 0

    override fun getGroup(groupPosition: Int): GROUP_DATA? = data

    override fun getGroupId(groupPosition: Int): Long = groupPosition.toLong()

    override fun getGroupTypeCount(): Int = 1

    override fun getGroupType(groupPosition: Int): Int = 0

    override fun getGroupView(
        groupPosition: Int, isExpanded: Boolean, convertView: View?, parent: ViewGroup
    ): View {
        val groupData = getGroup(groupPosition)
        val groupItemView = convertView ?: itemFactory
            .dispatchCreateItem(parent).apply {
                itemView.setTag(R.id.aa_tag_item, this)
            }.itemView

        @Suppress("UnnecessaryVariable") val groupBindingAdapterPosition = groupPosition
        val groupAbsolutePositionObject = parent.getTag(R.id.aa_tag_absoluteAdapterPosition)
        val groupAbsoluteAdapterPosition =
            (groupAbsolutePositionObject as Int?) ?: groupBindingAdapterPosition

        @Suppress("UNCHECKED_CAST")
        val groupItem = groupItemView.getTag(R.id.aa_tag_item) as AssemblyItem<Any>
        if (groupItem is AssemblyExpandableItem<Any>) {
            groupItem.dispatchBindData(
                groupBindingAdapterPosition,
                groupAbsoluteAdapterPosition,
                groupBindingAdapterPosition,
                groupAbsoluteAdapterPosition,
                -1,
                isExpanded,
                false,
                groupData
            )
        } else {
            groupItem.dispatchBindData(
                groupBindingAdapterPosition,
                groupAbsoluteAdapterPosition,
                groupData
            )
        }
        return groupItemView
    }


    override fun getChildrenCount(groupPosition: Int): Int = 0

    override fun getChild(groupPosition: Int, childPosition: Int): Any? = null

    override fun getChildId(groupPosition: Int, childPosition: Int): Long = childPosition.toLong()

    override fun getChildTypeCount(): Int = 1

    override fun getChildType(groupPosition: Int, childPosition: Int): Int = 0

    override fun getChildView(
        groupPosition: Int, childPosition: Int, isLastChild: Boolean,
        convertView: View?, parent: ViewGroup
    ): View {
        // todo 支持 childView
        throw UnsupportedOperationException("getChildView unsupported in AssemblySingleDataExpandableListAdapter")
    }


    override fun hasStableIds(): Boolean = hasStableIds

    override fun isChildSelectable(groupPosition: Int, childPosition: Int): Boolean {
        return isChildSelectable?.invoke(groupPosition, childPosition) == true
    }


    override fun getItemFactoryByPosition(position: Int): AssemblyItemFactory<*> = itemFactory
}