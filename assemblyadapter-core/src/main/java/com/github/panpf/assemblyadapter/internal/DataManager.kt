package com.github.panpf.assemblyadapter.internal

import java.util.*

class DataManager<DATA>(private val onDataListChanged: () -> Unit) {

    private val dataList: MutableList<DATA> = ArrayList()

    val dataCount: Int
        get() = dataList.size
    val dataListSnapshot: List<DATA>
        get() = ArrayList(dataList)

    fun setDataList(datas: List<DATA>?) {
        dataList.clear()
        if (datas != null && datas.isNotEmpty()) {
            dataList.addAll(datas)
        }
        onDataListChanged()
    }

    fun addData(data: DATA): Boolean {
        val result = dataList.add(data)
        if (result) {
            onDataListChanged()
        }
        return result
    }

    fun addData(index: Int, data: DATA) {
        dataList.add(index, data)
        onDataListChanged()
    }

    fun addAllData(datas: Collection<DATA>?): Boolean {
        var result = false
        if (datas != null && datas.isNotEmpty()) {
            result = dataList.addAll(datas)
        }
        if (result) {
            onDataListChanged()
        }
        return result
    }

    @SafeVarargs
    fun addAllData(vararg datas: DATA): Boolean {
        var result = false
        if (datas.isNotEmpty()) {
            Collections.addAll(dataList, *datas)
            result = true
        }
        if (result) {
            onDataListChanged()
        }
        return result
    }

    fun removeData(data: DATA): Boolean {
        val result: Boolean = dataList.remove(data)
        if (result) {
            onDataListChanged()
        }
        return result
    }

    fun removeData(index: Int): DATA? {
        val data: DATA? = dataList.removeAt(index)
        onDataListChanged()
        return data
    }

    fun removeAllData(datas: Collection<DATA>?): Boolean {
        var result = false
        if (datas != null && datas.isNotEmpty()) {
            result = dataList.removeAll(datas)
        }
        if (result) {
            onDataListChanged()
        }
        return result
    }

    fun clearData() {
        dataList.clear()
        onDataListChanged()
    }

    fun sortData(comparator: Comparator<DATA>) {
        Collections.sort(dataList, comparator)
        onDataListChanged()
    }

    fun getData(position: Int): DATA? {
        return dataList.getOrNull(position)
    }
}