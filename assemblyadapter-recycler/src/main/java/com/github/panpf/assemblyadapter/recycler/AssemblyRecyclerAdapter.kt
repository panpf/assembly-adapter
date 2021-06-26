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
import com.github.panpf.assemblyadapter.*
import com.github.panpf.assemblyadapter.internal.ItemDataStorage
import com.github.panpf.assemblyadapter.internal.ItemFactoryStorage
import com.github.panpf.assemblyadapter.recycler.internal.AssemblyItemViewHolderWrapper
import com.github.panpf.assemblyadapter.recycler.internal.FullSpanStaggeredGridLayoutManager
import java.util.*

open class AssemblyRecyclerAdapter<DATA>(
    itemFactoryList: List<ItemFactory<*>>,
    placeholderItemFactory: PlaceholderItemFactory? = null,
    dataList: List<DATA>? = null
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>(), AssemblyAdapter, DatasAdapter<DATA> {

    private val itemFactoryStorage = ItemFactoryStorage(
        if (placeholderItemFactory != null) itemFactoryList.plus(placeholderItemFactory) else itemFactoryList
    )
    private val itemDataStorage = ItemDataStorage(dataList) { notifyDataSetChanged() }

    constructor(
        itemFactoryList: List<ItemFactory<*>>,
        placeholderItemFactory: PlaceholderItemFactory?,
    ) : this(itemFactoryList, placeholderItemFactory, null)

    constructor(
        itemFactoryList: List<ItemFactory<*>>,
        dataList: List<DATA>?
    ) : this(itemFactoryList, null, dataList)

    constructor(itemFactoryList: List<ItemFactory<*>>) : this(itemFactoryList, null, null)

    override fun getItemCount(): Int {
        return itemDataStorage.dataCount
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        val matchData = itemDataStorage.getData(position) ?: Placeholder
        return itemFactoryStorage.getItemTypeByData(matchData)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val itemFactory = itemFactoryStorage.getItemFactoryByItemType(viewType)
        val item = itemFactory.dispatchCreateItem(parent)
        return AssemblyItemViewHolderWrapper(item).apply {
            val layoutManager =
                (parent.takeIf { it is RecyclerView } as RecyclerView?)?.layoutManager
            if (layoutManager is FullSpanStaggeredGridLayoutManager) {
                layoutManager.setFullSpan(itemView, itemFactory)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is AssemblyItemViewHolderWrapper<*>) {
            @Suppress("UNCHECKED_CAST")
            val item = holder.wrappedItem as Item<Any>
            if (item is PlaceholderItem) {
                item.dispatchBindData(position, holder.position, Placeholder)
            } else {
                val data = itemDataStorage.getData(position)!!
                item.dispatchBindData(position, holder.position, data)
            }
        } else {
            throw IllegalArgumentException("holder must be AssemblyItemViewHolderWrapper")
        }
    }


    override val dataCount: Int
        get() = itemDataStorage.dataCount

    override val dataListSnapshot: List<DATA>
        get() = itemDataStorage.dataListSnapshot

    override fun getData(position: Int): DATA {
        return itemDataStorage.getData(position)
    }

    override fun setDataList(datas: List<DATA>?) {
        itemDataStorage.setDataList(datas)
    }

    override fun addData(data: DATA): Boolean {
        return itemDataStorage.addData(data)
    }

    override fun addData(index: Int, data: DATA) {
        itemDataStorage.addData(index, data)
    }

    override fun addAllData(datas: Collection<DATA>): Boolean {
        return itemDataStorage.addAllData(datas)
    }

    override fun addAllData(index: Int, datas: Collection<DATA>): Boolean {
        return itemDataStorage.addAllData(index, datas)
    }

    override fun removeData(data: DATA): Boolean {
        return itemDataStorage.removeData(data)
    }

    override fun removeData(index: Int): DATA {
        return itemDataStorage.removeData(index)
    }

    override fun removeAllData(datas: Collection<DATA>): Boolean {
        return itemDataStorage.removeAllData(datas)
    }

    override fun clearData() {
        itemDataStorage.clearData()
    }

    override fun sortData(comparator: Comparator<DATA>) {
        itemDataStorage.sortData(comparator)
    }


    override fun getItemFactoryByPosition(position: Int): ItemFactory<*> {
        val matchData = itemDataStorage.getData(position) ?: Placeholder
        return itemFactoryStorage.getItemFactoryByData(matchData)
    }


    class Builder<DATA>(private val itemFactoryList: List<ItemFactory<*>>) {

        private var dataList: List<DATA>? = null
        private var placeholderItemFactory: PlaceholderItemFactory? = null

        fun setDataList(dataList: List<DATA>?) {
            this.dataList = dataList
        }

        fun setPlaceholderItemFactory(placeholderItemFactory: PlaceholderItemFactory?) {
            this.placeholderItemFactory = placeholderItemFactory
        }

        fun build(): AssemblyRecyclerAdapter<DATA> {
            return AssemblyRecyclerAdapter(itemFactoryList, placeholderItemFactory, dataList)
        }
    }
}