package com.github.panpf.assemblyadapter.internal

import android.util.SparseArray
import com.github.panpf.assemblyadapter.MatchItemFactory
import com.github.panpf.assemblyadapter.Placeholder
import java.util.*

class ItemFactoryStorage<ITEM_FACTORY : MatchItemFactory>(
    private val itemFactoryList: List<ITEM_FACTORY>,
) {

    private val getItemFactoryByItemTypeArray = SparseArray<ITEM_FACTORY>().apply {
        itemFactoryList.forEachIndexed { index, itemFactory ->
            append(index, itemFactory)
        }
    }
    private val getItemTypeByItemFactoryMap = HashMap<ITEM_FACTORY, Int>().apply {
        itemFactoryList.forEachIndexed { index, itemFactory ->
            this[itemFactory] = index
        }
    }

    val itemTypeCount = itemFactoryList.size

    init {
        require(this.itemFactoryList.isNotEmpty()) { "itemFactoryList Can not be empty" }
    }

    fun getItemFactoryByData(data: Any): ITEM_FACTORY {
        val itemFactory = itemFactoryList.find { it.match(data) }
        if (itemFactory != null) {
            return itemFactory
        }
        if (data is Placeholder) {
            throw IllegalArgumentException("Need to set the placeholderItemFactory property of Assembly*Adapter")
        } else {
            throw IllegalArgumentException("Not found matching item factory by data: $data")
        }
    }

    fun getItemTypeByData(data: Any): Int {
        val itemFactory = getItemFactoryByData(data)
        return getItemTypeByItemFactoryMap[itemFactory]
            ?: throw IllegalArgumentException("Not found matching item type by item factory: $itemFactory")
    }

    fun getItemFactoryByItemType(itemType: Int): ITEM_FACTORY {
        return getItemFactoryByItemTypeArray[itemType]
            ?: throw IllegalArgumentException("Unknown item type: $itemType")
    }
}