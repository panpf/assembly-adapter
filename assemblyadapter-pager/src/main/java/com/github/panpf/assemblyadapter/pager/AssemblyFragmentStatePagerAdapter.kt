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

import androidx.annotation.IntDef
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.github.panpf.assemblyadapter.AssemblyAdapter
import com.github.panpf.assemblyadapter.DataAdapter
import com.github.panpf.assemblyadapter.internal.DataManager
import com.github.panpf.assemblyadapter.internal.ItemManager
import java.util.*

@Deprecated("Switch to {@link androidx.viewpager2.widget.ViewPager2} and use {@link com.github.panpf.assemblyadapter.pager2.AssemblyFragmentStateAdapter} instead.")
class AssemblyFragmentStatePagerAdapter<DATA> :
    FragmentStatePagerAdapter, AssemblyAdapter, DataAdapter<DATA> {

    private val itemManager: ItemManager<AssemblyFragmentItemFactory<*>>
    private val dataManager = DataManager<DATA> { notifyDataSetChanged() }
    private val notifyCountHelper = PagerAdapterNotifyCountHelper()

    var isEnabledPositionNoneOnNotifyDataSetChanged: Boolean
        get() = notifyCountHelper.isEnabledPositionNoneOnNotifyDataSetChanged
        set(enabled) {
            notifyCountHelper.isEnabledPositionNoneOnNotifyDataSetChanged = enabled
        }

    constructor(
        fm: FragmentManager, @Behavior behavior: Int,
        itemFactoryList: List<AssemblyFragmentItemFactory<*>>
    ) : super(fm, behavior) {
        itemManager = ItemManager(itemFactoryList)
    }

    @Deprecated(
        """use {@link #AssemblyFragmentPagerAdapter(FragmentManager, int)} with
      {@link #BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT}"""
    )
    constructor(
        fm: FragmentManager, itemFactoryList: List<AssemblyFragmentItemFactory<*>>
    ) : super(fm) {
        itemManager = ItemManager(itemFactoryList)
    }

    constructor(
        fm: FragmentManager, @Behavior behavior: Int,
        itemFactoryList: List<AssemblyFragmentItemFactory<*>>, dataList: List<DATA>?
    ) : super(fm, behavior) {
        itemManager = ItemManager(itemFactoryList)
        dataManager.setDataList(dataList)
    }

    @Deprecated(
        """use {@link #AssemblyFragmentPagerAdapter(FragmentManager, int, List)} with
      {@link #BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT}"""
    )
    constructor(
        fm: FragmentManager,
        itemFactoryList: List<AssemblyFragmentItemFactory<*>>, dataList: List<DATA>?
    ) : super(fm) {
        itemManager = ItemManager(itemFactoryList)
        dataManager.setDataList(dataList)
    }

    override fun getCount(): Int {
        return dataManager.dataCount
    }

    override fun getItem(position: Int): Fragment {
        val data = dataManager.getData(position)

        @Suppress("UNCHECKED_CAST")
        val itemFactory = itemManager.getItemFactoryByData(data) as AssemblyFragmentItemFactory<Any>
        return itemFactory.dispatchCreateFragment(position, data)
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


    override fun getItemFactoryByPosition(position: Int): AssemblyFragmentItemFactory<*> {
        return itemManager.getItemFactoryByData(dataManager.getData(position))
    }


    @kotlin.annotation.Retention(AnnotationRetention.SOURCE)
    @IntDef(BEHAVIOR_SET_USER_VISIBLE_HINT, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT)
    private annotation class Behavior
}