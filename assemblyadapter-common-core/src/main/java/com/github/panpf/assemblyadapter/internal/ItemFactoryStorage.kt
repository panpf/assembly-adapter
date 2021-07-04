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

import com.github.panpf.assemblyadapter.MatchItemFactory
import com.github.panpf.assemblyadapter.Placeholder
import java.util.*

/**
 * Responsible for managing itemType and matching ItemFactory according to data and itemType
 */
class ItemFactoryStorage<ITEM_FACTORY : MatchItemFactory>(
    itemFactoryList: List<ITEM_FACTORY>,
) {

    private val _itemFactoryList: List<ITEM_FACTORY> = itemFactoryList.toList()

    private val getItemTypeByItemFactoryMap = HashMap<ITEM_FACTORY, Int>().apply {
        itemFactoryList.forEachIndexed { index, itemFactory ->
            this[itemFactory] = index
        }
    }

    val itemTypeCount = itemFactoryList.size

    init {
        require(itemFactoryList.isNotEmpty()) { "itemFactoryList Can not be empty" }
    }

    /**
     * Get the [MatchItemFactory] that can process the specified [data],
     * usually the matchData(data) method of [MatchItemFactory] returns true to indicate that it can be processed
     *
     * @throws IllegalArgumentException Could not find it
     */
    fun getItemFactoryByData(data: Any): ITEM_FACTORY {
        val itemFactory = _itemFactoryList.find { it.matchData(data) }
        return when {
            itemFactory != null -> itemFactory
            data is Placeholder -> throw IllegalArgumentException(
                "Because there are null elements in your data set, so need to add an MatchItemFactory " +
                        "that supports PlaceHolder to the Assembly*Adapter's itemFactoryList property"
            )
            else -> throw IllegalArgumentException("Not found matching item factory by data: $data")
        }
    }

    /**
     * Get the [MatchItemFactory] corresponding to the specified [itemType]
     *
     * @throws IllegalArgumentException Could not find it
     */
    fun getItemFactoryByItemType(itemType: Int): ITEM_FACTORY {
        require(itemType >= 0 && itemType < _itemFactoryList.size) {
            "Unknown item type: $itemType"
        }
        return _itemFactoryList[itemType]
    }

    /**
     * Get the itemType corresponding to the specified [data]
     *
     * @throws IllegalArgumentException Could not find it
     */
    fun getItemTypeByData(data: Any): Int {
        val itemFactory = getItemFactoryByData(data)
        return getItemTypeByItemFactoryMap[itemFactory]
            ?: throw IllegalArgumentException("Not found matching item type by item factory: $itemFactory")
    }
}