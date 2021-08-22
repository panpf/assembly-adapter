package com.github.panpf.assemblyadapter3.compat.internal

import java.util.*

class CompatDataManager : CompatItemManager.Callback {

    private val callbackList: MutableList<Callback> = ArrayList()
    private var dataList: MutableList<Any?>

    constructor() {
        dataList = ArrayList<Any?>()
    }

    constructor(dataList: List<Any?>?) {
        this.dataList = if (dataList != null && dataList.isNotEmpty()) {
            ArrayList(dataList)
        } else {
            ArrayList<Any?>()
        }
    }

    constructor(dataArray: Array<Any?>?) : this(dataArray?.toList())

    fun addCallback(callback: Callback) {
        callbackList.add(callback)
    }

    fun removeCallback(callback: Callback) {
        callbackList.remove(callback)
    }

    fun getDataList(): List<Any?>? {
        return if (dataList.size > 0) Collections.unmodifiableList(dataList) else null
    }

    fun setDataList(dataList: List<Any?>?) {
        synchronized(this) {
            this.dataList = if (dataList != null && dataList.isNotEmpty()) {
                ArrayList(dataList)
            } else {
                ArrayList<Any?>()
            }
        }
        for (callback in callbackList) {
            callback.onDataChanged()
        }
    }

    fun addAll(collection: Collection<*>?) {
        if (collection == null || collection.isEmpty()) {
            return
        }
        synchronized(this) {
            dataList.addAll(collection)
        }
        for (callback in callbackList) {
            callback.onDataChanged()
        }
    }

    fun addAll(vararg items: Any?) {
        if (items.isEmpty()) {
            return
        }
        synchronized(this) { Collections.addAll(dataList, *items) }
        for (callback in callbackList) {
            callback.onDataChanged()
        }
    }

    fun insert(`object`: Any?, index: Int) {
        if (`object` == null) {
            return
        }
        synchronized(this) { dataList.add(index, `object`) }
        for (callback in callbackList) {
            callback.onDataChanged()
        }
    }

    fun remove(`object`: Any) {
        synchronized(this) { dataList.remove(`object`) }
        for (callback in callbackList) {
            callback.onDataChanged()
        }
    }

    fun clear() {
        synchronized(this) { dataList.clear() }
        for (callback in callbackList) {
            callback.onDataChanged()
        }
    }

    fun sort(comparator: Comparator<Any?>) {
        synchronized(this) {
            Collections.sort(dataList, comparator)
        }
        for (callback in callbackList) {
            callback.onDataChanged()
        }
    }

    override fun getDataCount(): Int {
        return dataList.size
    }

    override fun getData(position: Int): Any? {
        return if (position >= 0 && position < dataList.size) dataList[position] else null
    }

    fun interface Callback {
        fun onDataChanged()
    }
}