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

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.github.panpf.assemblyadapter.DataAdapter
import com.github.panpf.assemblyadapter.ItemFactory
import com.github.panpf.assemblyadapter.internal.DataManager
import com.github.panpf.assemblyadapter.internal.ItemManager
import com.github.panpf.assemblyadapter.recycler.internal.AssemblyRecyclerItem
import java.util.*

open class AssemblyRecyclerAdapter<DATA>(itemFactoryList: List<ItemFactory<*>>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>(), DataAdapter<DATA>, GridLayoutItemSpanAdapter<ItemFactory<*>> {

    private val itemManager = ItemManager(itemFactoryList)
    private val dataManager = DataManager<DATA> { notifyDataSetChanged() }
    private var gridLayoutItemSpanMap: MutableMap<Class<out ItemFactory<*>>, ItemSpan>? = null

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
        val recyclerItem: AssemblyRecyclerItem<*> = AssemblyRecyclerItem(item)
        applyGridLayoutItemSpan(parent, itemFactory, recyclerItem)
        return recyclerItem
    }

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        if (viewHolder is AssemblyRecyclerItem<*>) {
            @Suppress("UNCHECKED_CAST")
            (viewHolder as AssemblyRecyclerItem<Any?>).dispatchBindData(
                position,
                dataManager.getData(position)
            )
        }
    }

    override fun setGridLayoutItemSpan(
        itemFactoryClass: Class<out ItemFactory<*>>, itemSpan: ItemSpan
    ): AssemblyRecyclerAdapter<DATA> {
        val gridLayoutItemSpanMap =
            (gridLayoutItemSpanMap ?: (HashMap<Class<out ItemFactory<*>>, ItemSpan>().apply {
                this@AssemblyRecyclerAdapter.gridLayoutItemSpanMap = this
            }))
        gridLayoutItemSpanMap[itemFactoryClass] = itemSpan
        return this
    }

    override fun setGridLayoutItemSpanMap(
        itemSpanMap: Map<Class<out ItemFactory<*>>, ItemSpan>?
    ): AssemblyRecyclerAdapter<DATA> {
        val gridLayoutItemSpanMap =
            (gridLayoutItemSpanMap ?: (HashMap<Class<out ItemFactory<*>>, ItemSpan>().apply {
                this@AssemblyRecyclerAdapter.gridLayoutItemSpanMap = this
            }))
        gridLayoutItemSpanMap.clear()
        if (itemSpanMap != null) {
            gridLayoutItemSpanMap.putAll(itemSpanMap)
        }
        return this
    }

    override fun getGridLayoutItemSpanMap(): Map<Class<out ItemFactory<*>>, ItemSpan>? {
        return gridLayoutItemSpanMap
    }

    private fun applyGridLayoutItemSpan(
        parent: ViewGroup,
        recyclerItemFactory: ItemFactory<*>,
        recyclerItem: AssemblyRecyclerItem<*>
    ) {
        val gridLayoutItemSpanMap = gridLayoutItemSpanMap
        if (gridLayoutItemSpanMap?.isNotEmpty() == true && parent is RecyclerView) {
            val layoutManager = parent.layoutManager
            if (layoutManager is AssemblyGridLayoutManager) {
                // No need to do
            } else if (layoutManager is AssemblyStaggeredGridLayoutManager) {
                val itemSpan = gridLayoutItemSpanMap[recyclerItemFactory.javaClass]
                if (itemSpan != null && itemSpan.span < 0) {
                    val itemView: View = recyclerItem.getItemView()
                    val layoutParams = itemView.layoutParams
                    if (layoutParams is StaggeredGridLayoutManager.LayoutParams) {
                        layoutParams.isFullSpan = true
                        itemView.layoutParams = layoutParams
                    }
                }
            } else {
                throw IllegalArgumentException("Since itemSpan is set, the layoutManager of RecyclerView must be AssemblyGridLayoutManager or AssemblyStaggeredGridLayoutManager")
            }
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


    fun getItemFactoryByItemType(itemType: Int): ItemFactory<*> {
        return itemManager.getItemFactoryByItemType(itemType)
    }

    override fun getItemFactoryByPosition(position: Int): ItemFactory<*> {
        return itemManager.getItemFactoryByData(dataManager.getData(position))
    }
}