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
package com.github.panpf.assemblyadapter

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.github.panpf.assemblyadapter.internal.AssemblyRecyclerItem
import com.github.panpf.assemblyadapter.internal.DataManager
import com.github.panpf.assemblyadapter.internal.ItemManager
import java.util.*

open class AssemblyRecyclerAdapter<DATA>(
    itemFactoryList: List<ItemFactory<*>>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val itemManager = ItemManager(itemFactoryList)
    private val dataManager = DataManager<DATA> { tryNotifyDataSetChanged() }
    private var gridLayoutItemSpanMap: MutableMap<Class<out ItemFactory<*>>, ItemSpan>? = null

    var stopNotifyDataSetChanged = false
    val dataListSnapshot: List<DATA?>
        get() = dataManager.dataListSnapshot

    constructor(
        itemFactoryList: List<ItemFactory<*>>,
        dataList: List<DATA>?
    ) : this(itemFactoryList) {
        dataManager.setDataList(dataList)
    }

    override fun getItemCount(): Int {
        return dataManager.dataCount
    }

    fun getItem(position: Int): DATA? {
        return dataManager.getData(position)
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return itemManager.getItemTypeByData(getItem(position))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val itemFactory = itemManager.getItemFactoryByItemType(viewType)
        val item = itemFactory.dispatchCreateItem(parent)
        val recyclerItem: AssemblyRecyclerItem<*> = AssemblyRecyclerItem(item)
        applyItemSpanInGridLayoutManager(parent, itemFactory, recyclerItem)
        return recyclerItem
    }

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        if (viewHolder is AssemblyRecyclerItem<*>) {
            @Suppress("UNCHECKED_CAST")
            (viewHolder as AssemblyRecyclerItem<Any?>).dispatchBindData(position, getItem(position))
        }
    }

    fun setGridLayoutItemSpan(
        itemFactoryClass: Class<out ItemFactory<*>>, itemSpan: ItemSpan
    ): AssemblyRecyclerAdapter<DATA> {
        val gridLayoutItemSpanMap =
            (gridLayoutItemSpanMap ?: (HashMap<Class<out ItemFactory<*>>, ItemSpan>().apply {
                this@AssemblyRecyclerAdapter.gridLayoutItemSpanMap = this
            }))
        gridLayoutItemSpanMap[itemFactoryClass] = itemSpan
        tryNotifyDataSetChanged()
        return this
    }

    fun setGridLayoutItemSpanMap(itemSpanMap: Map<Class<out ItemFactory<*>>, ItemSpan>?): AssemblyRecyclerAdapter<DATA> {
        val gridLayoutItemSpanMap =
            (gridLayoutItemSpanMap ?: (HashMap<Class<out ItemFactory<*>>, ItemSpan>().apply {
                this@AssemblyRecyclerAdapter.gridLayoutItemSpanMap = this
            }))
        gridLayoutItemSpanMap.clear()
        if (itemSpanMap != null) {
            gridLayoutItemSpanMap.putAll(itemSpanMap)
        }
        tryNotifyDataSetChanged()
        return this
    }

    fun getGridLayoutItemSpanMap(): Map<Class<out ItemFactory<*>>, ItemSpan>? {
        return gridLayoutItemSpanMap
    }

    private fun applyItemSpanInGridLayoutManager(
        parent: ViewGroup,
        recyclerItemFactory: ItemFactory<*>,
        recyclerItem: AssemblyRecyclerItem<*>
    ) {
        val itemSpanMapInGridLayoutManager = gridLayoutItemSpanMap
        if (itemSpanMapInGridLayoutManager?.isNotEmpty() == true && parent is RecyclerView) {
            val layoutManager = parent.layoutManager
            if (layoutManager is AssemblyGridLayoutManager) {
                // No need to do
            } else if (layoutManager is AssemblyStaggeredGridLayoutManager) {
                val itemSpan = itemSpanMapInGridLayoutManager[recyclerItemFactory.javaClass]
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

    fun setDataList(datas: List<DATA>?) {
        dataManager.setDataList(datas)
    }

    fun addData(data: DATA?): Boolean {
        return dataManager.addData(data)
    }

    fun addData(index: Int, data: DATA?) {
        dataManager.addData(index, data)
    }

    fun addAllData(datas: Collection<DATA?>?): Boolean {
        return dataManager.addAllData(datas)
    }

    @SafeVarargs
    fun addAllData(vararg datas: DATA?): Boolean {
        return dataManager.addAllData(*datas)
    }

    fun removeData(data: DATA?): Boolean {
        return dataManager.removeData(data)
    }

    fun removeData(index: Int): DATA? {
        return dataManager.removeData(index)
    }

    fun removeAllData(datas: Collection<DATA?>): Boolean {
        return dataManager.removeAllData(datas)
    }

    fun clearData() {
        dataManager.clearData()
    }

    fun sortData(comparator: Comparator<DATA?>) {
        dataManager.sortData(comparator)
    }

    fun getItemFactoryByItemType(itemType: Int): ItemFactory<*> {
        return itemManager.getItemFactoryByItemType(itemType)
    }

    fun getItemFactoryByPosition(position: Int): ItemFactory<*> {
        return getItemFactoryByItemType(itemManager.getItemTypeByData(getItem(position)))
    }

    private fun tryNotifyDataSetChanged() {
        if (!stopNotifyDataSetChanged) {
            notifyDataSetChanged()
        }
    }
}