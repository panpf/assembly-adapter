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
import com.github.panpf.assemblyadapter.DatasAdapter
import com.github.panpf.assemblyadapter.Placeholder
import com.github.panpf.assemblyadapter.internal.ItemDataStorage
import com.github.panpf.assemblyadapter.internal.ItemFactoryStorage
import com.github.panpf.assemblyadapter.pager.fragment.AssemblyFragmentItemFactory
import com.github.panpf.assemblyadapter.pager.fragment.AssemblyFragmentPlaceholderItemFactory

class AssemblyFragmentStateAdapter<DATA>(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle,
    itemFactoryList: List<AssemblyFragmentItemFactory<*>>,
    placeholderItemFactory: AssemblyFragmentPlaceholderItemFactory? = null,
    dataList: List<DATA>? = null
) : FragmentStateAdapter(fragmentManager, lifecycle), AssemblyAdapter, DatasAdapter<DATA> {

    private val itemFactoryStorage = ItemFactoryStorage(
        if (placeholderItemFactory != null) itemFactoryList.plus(placeholderItemFactory) else itemFactoryList
    )
    private val itemDataStorage = ItemDataStorage(dataList) { notifyDataSetChanged() }

    constructor(
        fragmentActivity: FragmentActivity,
        itemFactoryList: List<AssemblyFragmentItemFactory<*>>,
        placeholderItemFactory: AssemblyFragmentPlaceholderItemFactory? = null,
        dataList: List<DATA>? = null
    ) : this(
        fragmentActivity.supportFragmentManager,
        fragmentActivity.lifecycle,
        itemFactoryList,
        placeholderItemFactory,
        dataList
    )

    constructor(
        fragmentActivity: FragmentActivity,
        itemFactoryList: List<AssemblyFragmentItemFactory<*>>,
        placeholderItemFactory: AssemblyFragmentPlaceholderItemFactory?,
    ) : this(
        fragmentActivity.supportFragmentManager,
        fragmentActivity.lifecycle,
        itemFactoryList,
        placeholderItemFactory,
        null
    )

    constructor(
        fragmentActivity: FragmentActivity,
        itemFactoryList: List<AssemblyFragmentItemFactory<*>>,
        dataList: List<DATA>?
    ) : this(
        fragmentActivity.supportFragmentManager,
        fragmentActivity.lifecycle,
        itemFactoryList,
        null,
        dataList
    )

    constructor(
        fragmentActivity: FragmentActivity,
        itemFactoryList: List<AssemblyFragmentItemFactory<*>>,
    ) : this(
        fragmentActivity.supportFragmentManager,
        fragmentActivity.lifecycle,
        itemFactoryList,
        null,
        null
    )

    constructor(
        fragment: Fragment,
        itemFactoryList: List<AssemblyFragmentItemFactory<*>>,
        placeholderItemFactory: AssemblyFragmentPlaceholderItemFactory? = null,
        dataList: List<DATA>? = null
    ) : this(
        fragment.childFragmentManager,
        fragment.lifecycle,
        itemFactoryList,
        placeholderItemFactory,
        dataList
    )

    constructor(
        fragment: Fragment,
        itemFactoryList: List<AssemblyFragmentItemFactory<*>>,
        placeholderItemFactory: AssemblyFragmentPlaceholderItemFactory? = null,
    ) : this(
        fragment.childFragmentManager,
        fragment.lifecycle,
        itemFactoryList,
        placeholderItemFactory,
        null
    )

    constructor(
        fragment: Fragment,
        itemFactoryList: List<AssemblyFragmentItemFactory<*>>,
        dataList: List<DATA>? = null
    ) : this(
        fragment.childFragmentManager,
        fragment.lifecycle,
        itemFactoryList,
        null,
        dataList
    )

    constructor(
        fragment: Fragment,
        itemFactoryList: List<AssemblyFragmentItemFactory<*>>,
    ) : this(
        fragment.childFragmentManager,
        fragment.lifecycle,
        itemFactoryList,
        null,
        null
    )

    override fun getItemCount(): Int {
        return itemDataStorage.dataCount
    }

    override fun createFragment(position: Int): Fragment {
        val data = itemDataStorage.getData(position)
        val matchData = data ?: Placeholder

        @Suppress("UNCHECKED_CAST")
        val itemFactory =
            itemFactoryStorage.getItemFactoryByData(matchData) as AssemblyFragmentItemFactory<Any>
        return if (itemFactory is AssemblyFragmentPlaceholderItemFactory) {
            itemFactory.dispatchCreateFragment(position, Placeholder)
        } else {
            itemFactory.dispatchCreateFragment(position, data!!)
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


    override fun getItemFactoryByPosition(position: Int): AssemblyFragmentItemFactory<*> {
        val matchData = itemDataStorage.getData(position) ?: Placeholder
        return itemFactoryStorage.getItemFactoryByData(matchData)
    }


    class Builder<DATA>(
        private val fragmentManager: FragmentManager,
        private val lifecycle: Lifecycle,
        private val itemFactoryList: List<AssemblyFragmentItemFactory<*>>,
    ) {
        constructor(
            fragmentActivity: FragmentActivity,
            itemFactoryList: List<AssemblyFragmentItemFactory<*>>,
        ) : this(
            fragmentActivity.supportFragmentManager,
            fragmentActivity.lifecycle,
            itemFactoryList,
        )
        constructor(
            fragment: Fragment,
            itemFactoryList: List<AssemblyFragmentItemFactory<*>>,
        ) : this(
            fragment.childFragmentManager,
            fragment.lifecycle,
            itemFactoryList,
        )

        private var dataList: List<DATA>? = null
        private var placeholderItemFactory: AssemblyFragmentPlaceholderItemFactory? = null

        fun setDataList(dataList: List<DATA>?) {
            this.dataList = dataList
        }

        fun setPlaceholderItemFactory(placeholderItemFactory: AssemblyFragmentPlaceholderItemFactory?) {
            this.placeholderItemFactory = placeholderItemFactory
        }

        fun build(): AssemblyFragmentStateAdapter<DATA> {
            return AssemblyFragmentStateAdapter(
                fragmentManager,
                lifecycle,
                itemFactoryList,
                placeholderItemFactory,
                dataList
            )
        }
    }
}