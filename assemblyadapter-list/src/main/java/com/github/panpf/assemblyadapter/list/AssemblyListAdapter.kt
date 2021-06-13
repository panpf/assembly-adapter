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
import android.widget.BaseAdapter
import com.github.panpf.assemblyadapter.Item
import com.github.panpf.assemblyadapter.ItemFactory
import com.github.panpf.assemblyadapter.internal.DataManager
import com.github.panpf.assemblyadapter.internal.ItemManager
import java.util.*

class AssemblyListAdapter<DATA>(itemFactoryList: List<ItemFactory<*>>) : BaseAdapter() {

    private val itemManager = ItemManager(itemFactoryList)
    private val dataManager = DataManager<DATA> {
        if (!stopNotifyDataSetChanged) {
            notifyDataSetChanged()
        }
    }

    var stopNotifyDataSetChanged = false
    val dataListSnapshot: List<DATA>
        get() = dataManager.dataListSnapshot

    constructor(
        itemFactoryList: List<ItemFactory<*>>,
        dataList: List<DATA>?
    ) : this(itemFactoryList) {
        dataManager.setDataList(dataList)
    }

    fun getItemCount(): Int {
        return dataManager.dataCount
    }

    override fun getItem(position: Int): DATA? {
        return dataManager.getData(position)
    }

    override fun getCount(): Int {
        return dataManager.dataCount
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getViewTypeCount(): Int {
        return itemManager.itemTypeCount
    }

    override fun getItemViewType(position: Int): Int {
        return itemManager.getItemTypeByData(getItem(position))
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val data = getItem(position)
        val itemView = convertView ?: itemManager.matchItemFactoryByData(data)
            .dispatchCreateItem(parent).apply {
                getItemView().setTag(R.id.aa_tag_item, this)
            }.getItemView()

        @Suppress("UNCHECKED_CAST")
        val item = itemView.getTag(R.id.aa_tag_item) as Item<Any>
        item.dispatchBindData(position, data)
        return itemView
    }

    fun setDataList(datas: List<DATA>?) {
        dataManager.setDataList(datas)
    }

    fun addData(data: DATA): Boolean {
        return dataManager.addData(data)
    }

    fun addData(index: Int, data: DATA) {
        dataManager.addData(index, data)
    }

    fun addAllData(datas: Collection<DATA>?): Boolean {
        return dataManager.addAllData(datas)
    }

    @SafeVarargs
    fun addAllData(vararg datas: DATA): Boolean {
        return dataManager.addAllData(*datas)
    }

    fun removeData(data: DATA): Boolean {
        return dataManager.removeData(data)
    }

    fun removeData(index: Int): DATA? {
        return dataManager.removeData(index)
    }

    fun removeAllData(datas: Collection<DATA>): Boolean {
        return dataManager.removeAllData(datas)
    }

    fun clearData() {
        dataManager.clearData()
    }

    fun sortData(comparator: Comparator<DATA>) {
        dataManager.sortData(comparator)
    }

    fun getItemFactoryByItemType(itemType: Int): ItemFactory<*> {
        return itemManager.getItemFactoryByItemType(itemType)
    }

    fun getItemFactoryByPosition(position: Int): ItemFactory<*> {
        return getItemFactoryByItemType(itemManager.getItemTypeByData(getItem(position)))
    }

}