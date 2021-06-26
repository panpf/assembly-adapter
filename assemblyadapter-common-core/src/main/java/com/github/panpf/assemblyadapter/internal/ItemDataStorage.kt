package com.github.panpf.assemblyadapter.internal

import java.util.*

class ItemDataStorage<DATA>(
    _dataList: List<DATA>?,
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