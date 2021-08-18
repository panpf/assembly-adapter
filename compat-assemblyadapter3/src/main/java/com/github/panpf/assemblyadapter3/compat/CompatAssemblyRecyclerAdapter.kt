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
package com.github.panpf.assemblyadapter3.compat

import androidx.recyclerview.widget.RecyclerView
import com.github.panpf.assemblyadapter.ItemFactory
import com.github.panpf.assemblyadapter.recycler.AssemblyRecyclerAdapter
import com.github.panpf.assemblyadapter3.compat.internal.CompatBaseAssemblyRecyclerAdapter
import com.github.panpf.assemblyadapter3.compat.internal.CompatDataManager
import java.util.*

/**
 * Combined [RecyclerView.Adapter], support to combine multiple items, support head, tail and load more
 */
open class CompatAssemblyRecyclerAdapter : CompatBaseAssemblyRecyclerAdapter<Any> {

    private val dataManager: CompatDataManager

    constructor() {
        dataManager = CompatDataManager()
    }

    constructor(dataList: List<Any?>?) {
        dataManager = CompatDataManager(dataList)
    }

    constructor(dataArray: Array<Any?>?) {
        dataManager = CompatDataManager(dataArray)
    }

    override fun createBodyAdapter(bodyItemFactoryList: List<ItemFactory<*>>): RecyclerView.Adapter<*> {
        return AssemblyRecyclerAdapter<Any?>(bodyItemFactoryList).apply {
            submitList(dataManager.getDataList())
            dataManager.addCallback {
                submitList(dataManager.getDataList())
            }
        }
    }

    override fun setStateRestorationPolicy(strategy: StateRestorationPolicy) {
        super.setStateRestorationPolicy(strategy)
        tryResetConcatAdapter()
    }

    override fun setHasStableIds(hasStableIds: Boolean) {
        super.setHasStableIds(hasStableIds)
        tryResetConcatAdapter()
    }


    override val dataCount: Int
        get() = dataManager.getDataCount()

    override fun getData(positionInDataList: Int): Any? {
        return dataManager.getData(positionInDataList)
    }

    override var dataList: List<Any?>?
        get() = dataManager.getDataList()
        set(value) {
            dataManager.setDataList(value)
        }

    override fun addAll(collection: Collection<Any?>?) {
        dataManager.addAll(collection)
    }

    override fun addAll(vararg items: Any?) {
        dataManager.addAll(*items)
    }

    override fun insert(`object`: Any, index: Int) {
        dataManager.insert(`object`, index)
    }

    override fun remove(`object`: Any) {
        dataManager.remove(`object`)
    }

    override fun clear() {
        dataManager.clear()
    }

    override fun sort(comparator: Comparator<Any?>) {
        dataManager.sort(comparator)
    }
}