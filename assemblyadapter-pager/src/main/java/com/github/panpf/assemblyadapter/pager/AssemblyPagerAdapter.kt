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
package com.github.panpf.assemblyadapter.pager

import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter
import com.github.panpf.assemblyadapter.AssemblyAdapter
import com.github.panpf.assemblyadapter.DataAdapter
import com.github.panpf.assemblyadapter.internal.ItemDataStorage
import com.github.panpf.assemblyadapter.internal.ItemFactoryStorage
import java.util.*

class AssemblyPagerAdapter<DATA>(itemFactoryList: List<AssemblyPagerItemFactory<*>>) :
    PagerAdapter(), AssemblyAdapter, DataAdapter<DATA> {

    private val itemFactoryStorage = ItemFactoryStorage(itemFactoryList)
    private val itemDataStorage = ItemDataStorage<DATA> { notifyDataSetChanged() }
    private var refreshHelper: PagerAdapterRefreshHelper? = null

    var isEnabledPositionNoneOnNotifyDataSetChanged: Boolean
        get() = refreshHelper != null
        set(enabled) {
            refreshHelper = if (enabled) PagerAdapterRefreshHelper() else null
        }

    constructor(
        itemFactoryList: List<AssemblyPagerItemFactory<*>>,
        dataList: List<DATA>?
    ) : this(itemFactoryList) {
        itemDataStorage.setDataList(dataList)
    }

    override fun getCount(): Int {
        return itemDataStorage.dataCount
    }

    override fun isViewFromObject(view: View, item: Any): Boolean {
        return view === item
    }

    override fun destroyItem(container: ViewGroup, position: Int, item: Any) {
        container.removeView(item as View)
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val data = itemDataStorage.getData(position)

        @Suppress("UNCHECKED_CAST")
        val itemFactory = itemFactoryStorage.getItemFactoryByData(data) as AssemblyPagerItemFactory<Any>
        val itemView = itemFactory.dispatchCreateView(container.context, container, position, data)
        container.addView(itemView)
        return itemView
    }

    override fun notifyDataSetChanged() {
        refreshHelper?.onNotifyDataSetChanged()
        super.notifyDataSetChanged()
    }

    override fun getItemPosition(item: Any): Int {
        if (refreshHelper?.isItemPositionChanged(item) == true) {
            return POSITION_NONE
        }
        return super.getItemPosition(item)
    }


    override val dataCount: Int
        get() = itemDataStorage.dataCount

    override val dataListSnapshot: List<DATA>
        get() = itemDataStorage.dataListSnapshot

    override fun getData(position: Int): DATA? {
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

    override fun addAllData(datas: Collection<DATA>?): Boolean {
        return itemDataStorage.addAllData(datas)
    }

    override fun addAllData(index: Int, datas: Collection<DATA>?): Boolean {
        return itemDataStorage.addAllData(index, datas)
    }

    override fun removeData(data: DATA): Boolean {
        return itemDataStorage.removeData(data)
    }

    override fun removeData(index: Int): DATA? {
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


    override fun getItemFactoryByPosition(position: Int): AssemblyPagerItemFactory<*> {
        return itemFactoryStorage.getItemFactoryByData(itemDataStorage.getData(position))
    }
}