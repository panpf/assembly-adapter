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

import androidx.collection.ArrayMap
import com.github.panpf.assemblyadapter.NotFoundMatchedItemFactoryException
import com.github.panpf.assemblyadapter.Placeholder

/**
 * Matching ItemFactory by data or itemType
 */
class ItemFactoryStorage<ITEM_FACTORY : Matchable<*>> constructor(
    initItemFactoryList: List<ITEM_FACTORY>,
    private val itemFactoryName: String,
    private val adapterName: String,
    private val itemFactoryPropertyName: String
) {

    private val itemFactoryList: List<ITEM_FACTORY> = initItemFactoryList.toList()
    private val itemTypeBindMap = ArrayMap<ITEM_FACTORY, Int>().apply {
        initItemFactoryList.forEachIndexed { index, itemFactory ->
            this[itemFactory] = index
        }
    }
    private val itemFactoryClassMap by lazy {
        ArrayMap<Class<out ITEM_FACTORY>, ITEM_FACTORY>().apply {
            initItemFactoryList.forEach { itemFactory ->
                this[itemFactory.javaClass] = itemFactory
            }
        }
    }

    val itemTypeCount = initItemFactoryList.size

    fun getItemFactoryByData(data: Any): ITEM_FACTORY {
        val itemFactory = itemFactoryList.find { it.matchData(data) }
        return when {
            itemFactory != null -> itemFactory
            data is Placeholder -> throw NotFoundMatchedItemFactoryException(
                "Because there are null elements in your data set, so need to add an $itemFactoryName " +
                        "that supports '${data.javaClass.name}' to the $adapterName's $itemFactoryPropertyName property"
            )
            else -> throw NotFoundMatchedItemFactoryException(
                "Need to add an $itemFactoryName that supports '${data.javaClass.name}' to the $adapterName's $itemFactoryPropertyName property"
            )
        }
    }

    fun <T : ITEM_FACTORY> getItemFactoryByClass(itemFactoryClass: Class<T>): T {
        return (itemFactoryClassMap[itemFactoryClass]
            ?: throw NotFoundMatchedItemFactoryException(
                "Need to add an '${itemFactoryClass.name}' to the $adapterName's $itemFactoryPropertyName property"
            )) as T
    }

    fun getItemFactoryByItemType(itemType: Int): ITEM_FACTORY {
        require(itemType >= 0 && itemType < itemFactoryList.size) {
            "Unknown item type: $itemType"
        }
        return itemFactoryList[itemType]
    }

    fun getItemTypeByData(data: Any): Int {
        val itemFactory = getItemFactoryByData(data)
        return itemTypeBindMap[itemFactory]!!
    }
}