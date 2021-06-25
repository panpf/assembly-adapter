package com.github.panpf.assemblyadapter.internal

import android.util.SparseArray
import com.github.panpf.assemblyadapter.ItemFactory
import java.util.*

class ItemFactoryStorage<ITEM_FACTORY : ItemFactory>(itemFactoryListParam: List<ITEM_FACTORY>) {

    val itemTypeCount: Int = itemFactoryListParam.size
    private val itemFactoryList: List<ITEM_FACTORY> = ArrayList(itemFactoryListParam)
    private val getItemFactoryByItemTypeArray: SparseArray<ITEM_FACTORY>
    private val getItemTypeByItemFactoryMap: MutableMap<ITEM_FACTORY, Int>

    init {
        require(itemFactoryList.isNotEmpty()) { "itemFactoryList Can not be empty" }

        getItemFactoryByItemTypeArray = SparseArray()
        getItemTypeByItemFactoryMap = HashMap()
        itemFactoryList.forEachIndexed { index, itemFactory ->
            getItemFactoryByItemTypeArray.append(index, itemFactory)
            getItemTypeByItemFactoryMap[itemFactory] = index
        }
    }

    fun getItemFactoryByData(data: Any?): ITEM_FACTORY {
        return itemFactoryList.find { it.match(data) }
            ?: throw IllegalStateException("Not found item factory by data: $data")
    }

    fun getItemFactoryByItemType(itemType: Int): ITEM_FACTORY {
        return getItemFactoryByItemTypeArray[itemType]
            ?: throw IllegalArgumentException("Unknown item type: $itemType")
    }

    fun getItemTypeByData(data: Any?): Int {
        val itemFactory = getItemFactoryByData(data)
        return getItemTypeByItemFactoryMap[itemFactory]
            ?: throw IllegalStateException("Not found item type by item factory: $itemFactory")
    }
}