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
import com.github.panpf.assemblyadapter.DataAdapter
import com.github.panpf.assemblyadapter.internal.DataManager
import com.github.panpf.assemblyadapter.internal.ItemManager
import java.util.*

class AssemblyPagerAdapter<DATA>(itemFactoryList: List<AssemblyPagerItemFactory<*>>) :
    PagerAdapter(), DataAdapter<DATA> {

    private val itemManager = ItemManager(itemFactoryList)
    private val dataManager = DataManager<DATA> { notifyDataSetChanged() }
    private val notifyCountHelper = PagerAdapterNotifyCountHelper()

    var isEnabledPositionNoneOnNotifyDataSetChanged: Boolean
        get() = notifyCountHelper.isEnabledPositionNoneOnNotifyDataSetChanged
        set(enabled) {
            notifyCountHelper.isEnabledPositionNoneOnNotifyDataSetChanged = enabled
        }

    constructor(
        itemFactoryList: List<AssemblyPagerItemFactory<*>>,
        dataList: List<DATA>?
    ) : this(itemFactoryList) {
        dataManager.setDataList(dataList)
    }

    override fun getCount(): Int {
        return dataManager.dataCount
    }

    override fun isViewFromObject(view: View, item: Any): Boolean {
        return view === item
    }

    override fun destroyItem(container: ViewGroup, position: Int, item: Any) {
        container.removeView(item as View)
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val data = dataManager.getData(position)
        @Suppress("UNCHECKED_CAST")
        val itemFactory = itemManager.getItemFactoryByData(data) as AssemblyPagerItemFactory<Any>
        val itemView = itemFactory.dispatchCreateView(container.context, container, position, data)
        container.addView(itemView)
        return itemView
    }

    override fun notifyDataSetChanged() {
        notifyCountHelper.onNotifyDataSetChanged()
        super.notifyDataSetChanged()
    }

    override fun getItemPosition(item: Any): Int {
        return notifyCountHelper.getItemPosition(this, item)
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


    fun getItemFactoryByItemType(itemType: Int): AssemblyPagerItemFactory<*> {
        return itemManager.getItemFactoryByItemType(itemType)
    }

    fun getItemFactoryByPosition(position: Int): AssemblyPagerItemFactory<*> {
        return itemManager.getItemFactoryByData(dataManager.getData(position))
    }
}