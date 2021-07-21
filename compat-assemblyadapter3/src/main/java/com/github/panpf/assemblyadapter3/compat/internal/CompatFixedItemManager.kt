package com.github.panpf.assemblyadapter3.compat.internal

import com.github.panpf.assemblyadapter3.compat.CompatAssemblyItemFactory
import com.github.panpf.assemblyadapter3.compat.CompatFixedItem
import java.util.*

class CompatFixedItemManager {

    var itemList = ArrayList<CompatFixedItem<*>>(0)

    val availableCount: Int
        get() = itemList.count { it.isEnabled && it.data != null }

    fun add(fixedItem: CompatFixedItem<*>) {
        itemList.add(fixedItem)
    }

    fun getItem(index: Int): CompatFixedItem<*> = itemList[index]

    fun <DATA> getItemByFactoryClass(
        clazz: Class<out CompatAssemblyItemFactory<DATA>?>,
        number: Int
    ): CompatFixedItem<DATA> {
        var currentNumber = 0
        for (fixedItem in itemList) {
            if (clazz == fixedItem.itemFactory.javaClass) {
                if (currentNumber == number) {
                    @Suppress("UNCHECKED_CAST")
                    return fixedItem as CompatFixedItem<DATA>
                } else {
                    currentNumber++
                }
            }
        }
        throw IllegalArgumentException("Not found FixedItem by class=$clazz and number=$number")
    }

    fun <DATA> getItemByFactoryClass(clazz: Class<out CompatAssemblyItemFactory<DATA>?>): CompatFixedItem<DATA> {
        return getItemByFactoryClass(clazz, 0)
    }

    fun setItemData(index: Int, data: Any?) {
        @Suppress("UNCHECKED_CAST")
        (getItem(index) as CompatFixedItem<Any>).data = data
    }

    fun isItemEnabled(index: Int): Boolean {
        return getItem(index).isEnabled
    }

    fun setItemEnabled(index: Int, enabled: Boolean) {
        getItem(index).isEnabled = enabled
    }

    fun getItemInAvailableList(index: Int): CompatFixedItem<*> {
        var currentNumber = 0
        for (fixedItem in itemList) {
            if (fixedItem.isEnabled && fixedItem.data != null) {
                if (currentNumber == index) {
                    @Suppress("UNCHECKED_CAST")
                    return fixedItem
                } else {
                    currentNumber++
                }
            }
        }
        throw IndexOutOfBoundsException("Index: $index, Size: $availableCount")
    }
}