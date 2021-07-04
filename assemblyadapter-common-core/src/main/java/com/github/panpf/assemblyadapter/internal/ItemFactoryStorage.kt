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

import android.util.SparseArray
import com.github.panpf.assemblyadapter.MatchItemFactory
import com.github.panpf.assemblyadapter.Placeholder
import java.util.*

class ItemFactoryStorage<ITEM_FACTORY : MatchItemFactory>(
    itemFactoryList: List<ITEM_FACTORY>,
) {

    private val _itemFactoryList: List<ITEM_FACTORY> = itemFactoryList.toList()

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
        require(itemFactoryList.isNotEmpty()) { "itemFactoryList Can not be empty" }
    }

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