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
import androidx.viewpager.widget.PagerAdapter
import com.github.panpf.assemblyadapter.AssemblyAdapter
import com.github.panpf.assemblyadapter.DataAdapter
import com.github.panpf.assemblyadapter.internal.ItemDataStorage
import com.github.panpf.assemblyadapter.internal.ItemFactoryStorage
import java.util.*

@Deprecated("Switch to {@link androidx.viewpager2.widget.ViewPager2} and use {@link com.github.panpf.assemblyadapter.pager2.AssemblyFragmentStateAdapter} instead.")
class AssemblyFragmentStatePagerAdapter<DATA> :
    FragmentStatePagerAdapter, AssemblyAdapter, DataAdapter<DATA> {

    private val itemFactoryStorage: ItemFactoryStorage<AssemblyFragmentItemFactory<*>>
    private val itemDataStorage = ItemDataStorage<DATA> { notifyDataSetChanged() }
    private val itemPositionChangedHelper = PagerAdapterItemPositionChangedHelper()

    var isEnabledPositionNoneOnNotifyDataSetChanged: Boolean
        get() = itemPositionChangedHelper.isEnabledPositionNoneOnNotifyDataSetChanged
        set(enabled) {
            itemPositionChangedHelper.isEnabledPositionNoneOnNotifyDataSetChanged = enabled
        }

    constructor(
        fm: FragmentManager, @Behavior behavior: Int,
        itemFactoryList: List<AssemblyFragmentItemFactory<*>>
    ) : super(fm, behavior) {
        itemFactoryStorage = ItemFactoryStorage(itemFactoryList)
    }

    @Deprecated(
        """use {@link #AssemblyFragmentPagerAdapter(FragmentManager, int)} with
      {@link #BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT}"""
    )
    constructor(
        fm: FragmentManager, itemFactoryList: List<AssemblyFragmentItemFactory<*>>
    ) : super(fm) {
        itemFactoryStorage = ItemFactoryStorage(itemFactoryList)
    }

    constructor(
        fm: FragmentManager, @Behavior behavior: Int,
        itemFactoryList: List<AssemblyFragmentItemFactory<*>>, dataList: List<DATA>?
    ) : super(fm, behavior) {
        itemFactoryStorage = ItemFactoryStorage(itemFactoryList)
        itemDataStorage.setDataList(dataList)
    }

    @Deprecated(
        """use {@link #AssemblyFragmentPagerAdapter(FragmentManager, int, List)} with
      {@link #BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT}"""
    )
    constructor(
        fm: FragmentManager,
        itemFactoryList: List<AssemblyFragmentItemFactory<*>>, dataList: List<DATA>?
    ) : super(fm) {
        itemFactoryStorage = ItemFactoryStorage(itemFactoryList)
        itemDataStorage.setDataList(dataList)
    }

    override fun getCount(): Int {
        return itemDataStorage.dataCount
    }

    override fun getItem(position: Int): Fragment {
        val data = itemDataStorage.getData(position)

        @Suppress("UNCHECKED_CAST")
        val itemFactory =
            itemFactoryStorage.getItemFactoryByData(data) as AssemblyFragmentItemFactory<Any>
        return itemFactory.dispatchCreateFragment(position, data)
    }

    override fun notifyDataSetChanged() {
        itemPositionChangedHelper.onNotifyDataSetChanged()
        super.notifyDataSetChanged()
    }

    override fun getItemPosition(item: Any): Int {
        return if (itemPositionChangedHelper.isItemPositionChanged(item)) {
            PagerAdapter.POSITION_NONE
        } else {
            super.getItemPosition(item)
        }
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


    override fun getItemFactoryByPosition(position: Int): AssemblyFragmentItemFactory<*> {
        return itemFactoryStorage.getItemFactoryByData(itemDataStorage.getData(position))
    }


    @kotlin.annotation.Retention(AnnotationRetention.SOURCE)
    @IntDef(BEHAVIOR_SET_USER_VISIBLE_HINT, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT)
    private annotation class Behavior
}