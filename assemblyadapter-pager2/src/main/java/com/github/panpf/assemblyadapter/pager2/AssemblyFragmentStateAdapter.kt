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
package com.github.panpf.assemblyadapter.pager2

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.github.panpf.assemblyadapter.AssemblyAdapter
import com.github.panpf.assemblyadapter.DataAdapter
import com.github.panpf.assemblyadapter.internal.DataManager
import com.github.panpf.assemblyadapter.internal.ItemManager
import com.github.panpf.assemblyadapter.pager.AssemblyFragmentItemFactory

class AssemblyFragmentStateAdapter<DATA>(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle,
    itemFactoryList: List<AssemblyFragmentItemFactory<*>>
) : FragmentStateAdapter(fragmentManager, lifecycle), AssemblyAdapter, DataAdapter<DATA> {

    private val itemManager = ItemManager(itemFactoryList)
    private val dataManager = DataManager<DATA> { notifyDataSetChanged() }

    constructor(
        fragmentActivity: FragmentActivity, itemFactoryList: List<AssemblyFragmentItemFactory<*>>
    ) : this(fragmentActivity.supportFragmentManager, fragmentActivity.lifecycle, itemFactoryList)

    constructor(
        fragment: Fragment, itemFactoryList: List<AssemblyFragmentItemFactory<*>>
    ) : this(fragment.childFragmentManager, fragment.lifecycle, itemFactoryList)

    override fun getItemCount(): Int {
        return dataManager.dataCount
    }

    override fun createFragment(position: Int): Fragment {
        val data = dataManager.getData(position)

        @Suppress("UNCHECKED_CAST")
        val itemFactory = itemManager.getItemFactoryByData(data) as AssemblyFragmentItemFactory<Any>
        return itemFactory.dispatchCreateFragment(position, data)
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


    override fun getItemFactoryByItemType(itemType: Int): AssemblyFragmentItemFactory<*> {
        return itemManager.getItemFactoryByItemType(itemType)
    }

    override fun getItemFactoryByPosition(position: Int): AssemblyFragmentItemFactory<*> {
        return itemManager.getItemFactoryByData(dataManager.getData(position))
    }
}