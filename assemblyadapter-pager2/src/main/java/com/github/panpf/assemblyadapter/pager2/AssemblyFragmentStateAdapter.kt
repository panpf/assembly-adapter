/*
 * Copyright (C) 2021 panpf <panpfpanpf@outlook.com>
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
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.github.panpf.assemblyadapter.AssemblyAdapter
import com.github.panpf.assemblyadapter.DatasAdapter
import com.github.panpf.assemblyadapter.Placeholder
import com.github.panpf.assemblyadapter.internal.ItemDataStorage
import com.github.panpf.assemblyadapter.internal.ItemFactoryStorage
import com.github.panpf.assemblyadapter.pager.fragment.FragmentItemFactory

open class AssemblyFragmentStateAdapter<DATA>(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle,
    itemFactoryList: List<FragmentItemFactory<*>>,
    dataList: List<DATA>? = null
) : FragmentStateAdapter(fragmentManager, lifecycle), AssemblyAdapter<FragmentItemFactory<*>>,
    DatasAdapter<DATA> {

    private val itemFactoryStorage = ItemFactoryStorage(itemFactoryList)
    private val itemDataStorage = ItemDataStorage(dataList) { notifyDataSetChanged() }
    private var recyclerView: RecyclerView? = null
    private var concatAdapterAbsoluteHelper: ConcatAdapterAbsoluteHelper? = null

    constructor(
        fragmentActivity: FragmentActivity,
        itemFactoryList: List<FragmentItemFactory<*>>,
        dataList: List<DATA>? = null
    ) : this(
        fragmentActivity.supportFragmentManager,
        fragmentActivity.lifecycle,
        itemFactoryList,
        dataList
    )

    constructor(
        fragment: Fragment,
        itemFactoryList: List<FragmentItemFactory<*>>,
        dataList: List<DATA>? = null
    ) : this(
        fragment.childFragmentManager,
        fragment.lifecycle,
        itemFactoryList,
        dataList
    )

    init {
        require(itemFactoryList.isNotEmpty()) { "itemFactoryList Can not be empty" }
    }


    override fun getItemCount(): Int {
        return itemDataStorage.dataCount
    }

    override fun createFragment(position: Int): Fragment {
        val data = itemDataStorage.getData(position) ?: Placeholder

        @Suppress("UnnecessaryVariable") val bindingAdapterPosition = position
        val parentAdapter = recyclerView?.adapter
        val absoluteAdapterPosition = if (parentAdapter is ConcatAdapter) {
            (concatAdapterAbsoluteHelper ?: ConcatAdapterAbsoluteHelper().apply {
                concatAdapterAbsoluteHelper = this
            }).findAbsoluteAdapterPosition(
                parentAdapter, this, position
            )
        } else {
            bindingAdapterPosition
        }

        @Suppress("UNCHECKED_CAST")
        val itemFactory = itemFactoryStorage.getItemFactoryByData(
            data, "FragmentItemFactory", "AssemblyFragmentStateAdapter", "itemFactoryList"
        ) as FragmentItemFactory<Any>
        return itemFactory.dispatchCreateFragment(
            bindingAdapterPosition, absoluteAdapterPosition, data
        )
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

    override fun removeDataAt(index: Int): DATA {
        return itemDataStorage.removeDataAt(index)
    }

    override fun removeAllData(datas: Collection<DATA>): Boolean {
        return itemDataStorage.removeAllData(datas)
    }

    override fun removeDataIf(filter: (DATA) -> Boolean): Boolean {
        return itemDataStorage.removeDataIf(filter)
    }

    override fun clearData() {
        itemDataStorage.clearData()
    }

    override fun sortData(comparator: Comparator<DATA>) {
        itemDataStorage.sortData(comparator)
    }


    override fun getItemFactoryByPosition(position: Int): FragmentItemFactory<*> {
        val data = itemDataStorage.getData(position) ?: Placeholder
        return itemFactoryStorage.getItemFactoryByData(
            data, "FragmentItemFactory", "AssemblyFragmentStateAdapter", "itemFactoryList"
        )
    }


    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        this.recyclerView = recyclerView
        this.concatAdapterAbsoluteHelper = null
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)
        this.recyclerView = null
        this.concatAdapterAbsoluteHelper = null
    }
}