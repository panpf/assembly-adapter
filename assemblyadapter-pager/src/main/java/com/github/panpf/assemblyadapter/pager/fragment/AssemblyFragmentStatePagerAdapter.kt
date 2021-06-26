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
package com.github.panpf.assemblyadapter.pager.fragment

import androidx.annotation.IntDef
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.github.panpf.assemblyadapter.AssemblyAdapter
import com.github.panpf.assemblyadapter.DatasAdapter
import com.github.panpf.assemblyadapter.Placeholder
import com.github.panpf.assemblyadapter.internal.ItemDataStorage
import com.github.panpf.assemblyadapter.internal.ItemFactoryStorage
import java.util.*

@Deprecated(
    message = "Switch to 'androidx.viewpager2.widget.ViewPager2' and use 'com.github.panpf.assemblyadapter.pager2.AssemblyFragmentStateAdapter' instead.",
    replaceWith = ReplaceWith(
        "AssemblyFragmentStateAdapter(itemFactoryList)",
        "com.github.panpf.assemblyadapter.pager2.AssemblyFragmentStateAdapter"
    )
)
class AssemblyFragmentStatePagerAdapter<DATA>(
    fm: FragmentManager,
    @Behavior behavior: Int,
    itemFactoryList: List<FragmentItemFactory<*>>,
    placeholderItemFactory: FragmentPlaceholderItemFactory? = null,
    dataList: List<DATA>? = null
) : FragmentStatePagerAdapter(fm, behavior), AssemblyAdapter, DatasAdapter<DATA> {

    private val itemFactoryStorage = ItemFactoryStorage(
        if (placeholderItemFactory != null) itemFactoryList.plus(placeholderItemFactory) else itemFactoryList
    )
    private val itemDataStorage = ItemDataStorage(dataList) { notifyDataSetChanged() }
    private var refreshHelper: FragmentPagerAdapterRefreshHelper? =
        FragmentPagerAdapterRefreshHelper()

    var isDisableItemRefreshWhenDataSetChanged: Boolean
        get() = refreshHelper != null
        set(disable) {
            if (disable != isDisableItemRefreshWhenDataSetChanged) {
                refreshHelper = if (disable) null else FragmentPagerAdapterRefreshHelper()
            }
        }

    constructor(
        fm: FragmentManager,
        @Behavior behavior: Int,
        itemFactoryList: List<FragmentItemFactory<*>>,
        placeholderItemFactory: FragmentPlaceholderItemFactory? = null
    ) : this(fm, behavior, itemFactoryList, placeholderItemFactory, null)

    constructor(
        fm: FragmentManager,
        @Behavior behavior: Int,
        itemFactoryList: List<FragmentItemFactory<*>>,
        dataList: List<DATA>? = null
    ) : this(fm, behavior, itemFactoryList, null, dataList)

    constructor(
        fm: FragmentManager,
        @Behavior behavior: Int,
        itemFactoryList: List<FragmentItemFactory<*>>
    ) : this(fm, behavior, itemFactoryList, null, null)

    @Deprecated(
        """use {@link #AssemblyFragmentPagerAdapter(FragmentManager, int, AssemblyFragmentPlaceholderItemFactory)} with
      {@link #BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT}"""
    )
    constructor(
        fm: FragmentManager,
        itemFactoryList: List<FragmentItemFactory<*>>,
        placeholderItemFactory: FragmentPlaceholderItemFactory? = null
    ) : this(fm, BEHAVIOR_SET_USER_VISIBLE_HINT, itemFactoryList, placeholderItemFactory, null)

    @Deprecated(
        """use {@link #AssemblyFragmentPagerAdapter(FragmentManager, int, List<AssemblyFragmentItemFactory<*>>, List<DATA>)} with
      {@link #BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT}"""
    )
    constructor(
        fm: FragmentManager,
        itemFactoryList: List<FragmentItemFactory<*>>,
        dataList: List<DATA>? = null
    ) : this(fm, BEHAVIOR_SET_USER_VISIBLE_HINT, itemFactoryList, null, dataList)

    @Deprecated(
        """use {@link #AssemblyFragmentPagerAdapter(FragmentManager, int, List<AssemblyFragmentItemFactory<*>>)} with
      {@link #BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT}"""
    )
    constructor(
        fm: FragmentManager,
        itemFactoryList: List<FragmentItemFactory<*>>
    ) : this(fm, BEHAVIOR_SET_USER_VISIBLE_HINT, itemFactoryList, null, null)

    override fun getCount(): Int {
        return itemDataStorage.dataCount
    }

    override fun getItem(position: Int): Fragment {
        val data = itemDataStorage.getData(position)
        val matchData = data ?: Placeholder

        @Suppress("UNCHECKED_CAST")
        val itemFactory =
            itemFactoryStorage.getItemFactoryByData(matchData) as FragmentItemFactory<Any>
        return if (itemFactory is FragmentPlaceholderItemFactory) {
            itemFactory.dispatchCreateFragment(position, Placeholder)
        } else {
            itemFactory.dispatchCreateFragment(position, data!!)
        }.apply {
            refreshHelper?.bindNotifyDataSetChangedNumber(this)
        }
    }

    override fun notifyDataSetChanged() {
        refreshHelper?.onNotifyDataSetChanged()
        super.notifyDataSetChanged()
    }

    override fun getItemPosition(item: Any): Int {
        if (refreshHelper?.isItemPositionChanged(item as Fragment) == true) {
            return POSITION_NONE
        }
        return super.getItemPosition(item)
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


    override fun getItemFactoryByPosition(position: Int): FragmentItemFactory<*> {
        val matchData = itemDataStorage.getData(position) ?: Placeholder
        return itemFactoryStorage.getItemFactoryByData(matchData)
    }


    @kotlin.annotation.Retention(AnnotationRetention.SOURCE)
    @IntDef(BEHAVIOR_SET_USER_VISIBLE_HINT, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT)
    private annotation class Behavior


    class Builder<DATA>(
        private val fragmentManager: FragmentManager,
        @Behavior private val behavior: Int,
        private val itemFactoryList: List<FragmentItemFactory<*>>,
    ) {

        private var dataList: List<DATA>? = null
        private var placeholderItemFactory: FragmentPlaceholderItemFactory? = null

        fun setDataList(dataList: List<DATA>?) {
            this.dataList = dataList
        }

        fun setPlaceholderItemFactory(placeholderItemFactory: FragmentPlaceholderItemFactory?) {
            this.placeholderItemFactory = placeholderItemFactory
        }

        fun build(): AssemblyFragmentStatePagerAdapter<DATA> {
            return AssemblyFragmentStatePagerAdapter(
                fragmentManager,
                behavior,
                itemFactoryList,
                placeholderItemFactory,
                dataList
            )
        }
    }
}