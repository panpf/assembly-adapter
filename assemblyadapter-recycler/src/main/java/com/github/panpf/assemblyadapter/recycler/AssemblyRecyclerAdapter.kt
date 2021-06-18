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
package com.github.panpf.assemblyadapter.recycler

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.github.panpf.assemblyadapter.AssemblyAdapter
import com.github.panpf.assemblyadapter.DataAdapter
import com.github.panpf.assemblyadapter.ItemFactory
import com.github.panpf.assemblyadapter.internal.DataManager
import com.github.panpf.assemblyadapter.internal.ItemManager
import com.github.panpf.assemblyadapter.recycler.internal.AssemblyRecyclerItem
import com.github.panpf.assemblyadapter.recycler.internal.FullSpanStaggeredGridLayoutManager
import java.util.*

open class AssemblyRecyclerAdapter<DATA>(itemFactoryList: List<ItemFactory<*>>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>(), AssemblyAdapter, DataAdapter<DATA> {

    private val itemManager = ItemManager(itemFactoryList)
    private val dataManager = DataManager<DATA> { notifyDataSetChanged() }

    constructor(
        itemFactoryList: List<ItemFactory<*>>,
        dataList: List<DATA>?
    ) : this(itemFactoryList) {
        dataManager.setDataList(dataList)
    }

    override fun getItemCount(): Int {
        return dataManager.dataCount
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return itemManager.getItemTypeByData(dataManager.getData(position))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val itemFactory = itemManager.getItemFactoryByItemType(viewType)
        val item = itemFactory.dispatchCreateItem(parent)
        return AssemblyRecyclerItem(item).apply {
            val layoutManager =
                (parent.takeIf { it is RecyclerView } as RecyclerView?)?.layoutManager
            if (layoutManager is FullSpanStaggeredGridLayoutManager) {
                layoutManager.setFullSpan(itemView, itemFactory)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is AssemblyRecyclerItem<*>) {
            @Suppress("UNCHECKED_CAST")
            (holder as AssemblyRecyclerItem<Any?>).dispatchBindData(
                position,
                dataManager.getData(position)
            )
        }
    }


    override val dataCount: Int
        get() = dataManager.dataCount

    override val dataListSnapshot: List<DATA>
        get() = dataManager.dataListSnapshot

    override fun getData(position: Int): DATA? {
        return dataManager.getData(position)
    }

    override fun setDataList(datas: List<DATA>?) {
        dataManager.setDataList(datas)
    }

    override fun addData(data: DATA): Boolean {
        return dataManager.addData(data)
    }

    override fun addData(index: Int, data: DATA) {
        dataManager.addData(index, data)
    }

    override fun addAllData(datas: Collection<DATA>?): Boolean {
        return dataManager.addAllData(datas)
    }

    @SafeVarargs
    override fun addAllData(vararg datas: DATA): Boolean {
        return dataManager.addAllData(*datas)
    }

    override fun removeData(data: DATA): Boolean {
        return dataManager.removeData(data)
    }

    override fun removeData(index: Int): DATA? {
        return dataManager.removeData(index)
    }

    override fun removeAllData(datas: Collection<DATA>): Boolean {
        return dataManager.removeAllData(datas)
    }

    override fun clearData() {
        dataManager.clearData()
    }

    override fun sortData(comparator: Comparator<DATA>) {
        dataManager.sortData(comparator)
    }


    override fun getItemFactoryByPosition(position: Int): ItemFactory<*> {
        return itemManager.getItemFactoryByData(dataManager.getData(position))
    }
}