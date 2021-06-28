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
import com.github.panpf.assemblyadapter.*
import com.github.panpf.assemblyadapter.internal.ItemDataStorage
import com.github.panpf.assemblyadapter.internal.ItemFactoryStorage
import java.util.*

open class AssemblyListAdapter<DATA>(
    itemFactoryList: List<ItemFactory<*>>,
    placeholderItemFactory: ItemFactory<Placeholder>? = null,
    dataList: List<DATA>? = null
) : BaseAdapter(), AssemblyAdapter, DatasAdapter<DATA> {

    private val itemFactoryStorage = ItemFactoryStorage(
        if (placeholderItemFactory != null) itemFactoryList.plus(placeholderItemFactory) else itemFactoryList
    )
    private val itemDataStorage = ItemDataStorage(dataList) { notifyDataSetChanged() }

    constructor(
        itemFactoryList: List<ItemFactory<*>>,
        placeholderItemFactory: ItemFactory<Placeholder>?,
    ) : this(itemFactoryList, placeholderItemFactory, null)

    constructor(
        itemFactoryList: List<ItemFactory<*>>,
        dataList: List<DATA>?
    ) : this(itemFactoryList, null, dataList)

    constructor(itemFactoryList: List<ItemFactory<*>>) : this(itemFactoryList, null, null)

    init {
        placeholderItemFactory?.apply {
            if (!match(Placeholder)) {
                throw IllegalArgumentException("'${placeholderItemFactory::class.java.name}' 's match(Any) method must return true when passing in Placeholder")
            }
            if (match(0)) {
                throw IllegalArgumentException("'${placeholderItemFactory::class.java.name}' 's match(Any) method must return false when passing in non Placeholder")
            }
        }
    }

    override fun getItem(position: Int): DATA {
        return itemDataStorage.getData(position)
    }

    override fun getCount(): Int {
        return itemDataStorage.dataCount
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getViewTypeCount(): Int {
        return itemFactoryStorage.itemTypeCount
    }

    override fun getItemViewType(position: Int): Int {
        val data = itemDataStorage.getData(position) ?: Placeholder
        return itemFactoryStorage.getItemTypeByData(data)
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val data = itemDataStorage.getData(position) ?: Placeholder
        val itemView = convertView ?: itemFactoryStorage.getItemFactoryByData(data)
            .dispatchCreateItem(parent).apply {
                itemView.setTag(R.id.aa_tag_item, this)
            }.itemView

        @Suppress("UnnecessaryVariable") val bindingAdapterPosition = position
        val absolutePositionObject = parent.getTag(R.id.aa_tag_absoluteAdapterPosition)
        val absoluteAdapterPosition = (absolutePositionObject as Int?) ?: bindingAdapterPosition

        @Suppress("UNCHECKED_CAST")
        val item = itemView.getTag(R.id.aa_tag_item) as ItemFactory.Item<Any>
        item.dispatchBindData(bindingAdapterPosition, absoluteAdapterPosition, data)

        return itemView
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
        val data = itemDataStorage.getData(position) ?: Placeholder
        return itemFactoryStorage.getItemFactoryByData(data)
    }


    class Builder<DATA>(private val itemFactoryList: List<ItemFactory<*>>) {

        private var dataList: List<DATA>? = null
        private var placeholderItemFactory: ItemFactory<Placeholder>? = null

        fun setDataList(dataList: List<DATA>?) {
            this.dataList = dataList
        }

        fun setPlaceholderItemFactory(placeholderItemFactory: ItemFactory<Placeholder>?) {
            this.placeholderItemFactory = placeholderItemFactory
        }

        fun build(): AssemblyListAdapter<DATA> {
            return AssemblyListAdapter(itemFactoryList, placeholderItemFactory, dataList)
        }
    }
}