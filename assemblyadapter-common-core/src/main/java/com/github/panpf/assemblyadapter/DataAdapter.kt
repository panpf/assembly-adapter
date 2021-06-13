package com.github.panpf.assemblyadapter

import java.util.Comparator

interface DataAdapter<DATA> {

    val dataCount: Int

    val dataListSnapshot: List<DATA>

    fun getData(position: Int): DATA?

    fun setDataList(datas: List<DATA>?)

    fun addData(data: DATA): Boolean

    fun addData(index: Int, data: DATA)

    fun addAllData(datas: Collection<DATA>?): Boolean

    @SafeVarargs
    fun addAllData(vararg datas: DATA): Boolean

    fun removeData(data: DATA): Boolean

    fun removeData(index: Int): DATA?

    fun removeAllData(datas: Collection<DATA>): Boolean

    fun clearData()

    fun sortData(comparator: Comparator<DATA>)
}