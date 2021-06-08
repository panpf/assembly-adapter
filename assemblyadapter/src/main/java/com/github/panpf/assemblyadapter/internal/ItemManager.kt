package com.github.panpf.assemblyadapter.internal

import android.util.SparseArray
import com.github.panpf.assemblyadapter.ItemFactory
import java.util.*

class ItemManager<FACTORY : ItemFactory<*>>(itemFactoryListParam: List<FACTORY>) {

    val itemTypeCount: Int = itemFactoryListParam.size
    private val itemFactoryList: List<FACTORY> = ArrayList(itemFactoryListParam)
    private val getItemFactoryByItemTypeArray: SparseArray<FACTORY>
    private val getItemTypeByItemFactoryMap: MutableMap<FACTORY, Int>

    init {
        require(itemFactoryList.isNotEmpty()) { "itemFactoryList Can not be empty" }

        getItemFactoryByItemTypeArray = SparseArray()
        getItemTypeByItemFactoryMap = HashMap()
        itemFactoryList.forEachIndexed { index, itemFactory ->
            getItemFactoryByItemTypeArray.append(index, itemFactory)
            getItemTypeByItemFactoryMap[itemFactory] = index
        }
    }

    fun matchItemFactoryByData(data: Any?): FACTORY {
        return itemFactoryList.find { it.match(data) }
            ?: throw IllegalStateException("Not found item factory by data: $data")
    }

    fun getItemFactoryByItemType(itemType: Int): FACTORY {
        return getItemFactoryByItemTypeArray[itemType]
            ?: throw IllegalArgumentException("Unknown item type: $itemType")
    }

    fun getItemTypeByData(data: Any?): Int {
        val itemFactory = matchItemFactoryByData(data)
        return getItemTypeByItemFactoryMap[itemFactory]
            ?: throw IllegalStateException("Not found item type by item factory: $itemFactory")
    }

    fun getItemTypeByItemFactory(itemFactory: FACTORY): Int {
        return getItemTypeByItemFactoryMap[itemFactory]
            ?: throw IllegalStateException("Not found item type by item factory: $itemFactory")
    }
}