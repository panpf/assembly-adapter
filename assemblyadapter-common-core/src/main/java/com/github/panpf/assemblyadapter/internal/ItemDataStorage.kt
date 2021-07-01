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
package com.github.panpf.assemblyadapter.internal

import java.util.*

class ItemDataStorage<DATA>(
    _dataList: List<DATA>? = null,
    private val onDataListChanged: () -> Unit
) {

    private val dataList = ArrayList<DATA>()

    init {
        if (_dataList != null) {
            dataList.addAll(_dataList)
        }
    }

    val dataCount: Int
        get() = dataList.size
    val dataListSnapshot: List<DATA>
        get() = Collections.unmodifiableList(dataList)

    fun setDataList(datas: List<DATA>?) {
        dataList.clear()
        if (datas != null) {
            dataList.addAll(datas)
        }
        onDataListChanged()
    }

    fun addData(data: DATA): Boolean {
        return dataList.add(data).apply {
            if (this) {
                onDataListChanged()
            }
        }
    }

    fun addData(index: Int, data: DATA) {
        dataList.add(index, data)
        onDataListChanged()
    }

    fun addAllData(datas: Collection<DATA>): Boolean {
        return dataList.addAll(datas).apply {
            if (this) {
                onDataListChanged()
            }
        }
    }

    fun addAllData(index: Int, datas: Collection<DATA>): Boolean {
        return dataList.addAll(index, datas).apply {
            if (this) {
                onDataListChanged()
            }
        }
    }

    @SafeVarargs
    fun addAllData(vararg datas: DATA): Boolean {
        return Collections.addAll(dataList, *datas).apply {
            if (this) {
                onDataListChanged()
            }
        }
    }

    fun removeData(data: DATA): Boolean {
        return dataList.remove(data).apply {
            if (this) {
                onDataListChanged()
            }
        }
    }

    fun removeData(index: Int): DATA {
        return dataList.removeAt(index).apply {
            onDataListChanged()
        }
    }

    fun removeAllData(datas: Collection<DATA>): Boolean {
        return dataList.removeAll(datas).apply {
            if (this) {
                onDataListChanged()
            }
        }
    }

    fun clearData() {
        dataList.clear()
        onDataListChanged()
    }

    fun sortData(comparator: Comparator<DATA>) {
        Collections.sort(dataList, comparator)
        onDataListChanged()
    }

    fun getData(position: Int): DATA {
        return dataList[position]
    }
}