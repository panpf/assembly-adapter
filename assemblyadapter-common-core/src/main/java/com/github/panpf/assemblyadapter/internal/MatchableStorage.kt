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

import com.github.panpf.assemblyadapter.Matchable
import com.github.panpf.assemblyadapter.NotFoundMatchedItemFactoryException
import com.github.panpf.assemblyadapter.Placeholder
import java.util.*

/**
 * Responsible for managing itemType and matching ItemFactory according to data and itemType
 */
class MatchableStorage<MATCHABLE : Matchable>(
    initMatchableList: List<MATCHABLE>,
) {

    private val matchableList: List<MATCHABLE> = initMatchableList.toList()

    private val getItemTypeByMatchableMap = HashMap<MATCHABLE, Int>().apply {
        initMatchableList.forEachIndexed { index, itemFactory ->
            this[itemFactory] = index
        }
    }

    val matchableCount = initMatchableList.size

    /**
     * Get the [Matchable] that can process the specified [data],
     * usually the matchData(data) method of [Matchable] returns true to indicate that it can be processed
     *
     * @throws NotFoundMatchedItemFactoryException No [Matchable] that can support [data] is found in the Adapter’s itemFactoryList
     */
    fun getMatchableByData(
        data: Any, itemFactoryName: String, adapterName: String, itemFactoryPropertyName: String
    ): MATCHABLE {
        val matchable = matchableList.find { it.matchData(data) }
        return when {
            matchable != null -> matchable
            data is Placeholder -> throw NotFoundMatchedItemFactoryException(
                "Because there are null elements in your data set, so need to add an $itemFactoryName " +
                        "that supports '${data::class.java.name}' to the $adapterName's $itemFactoryPropertyName property"
            )
            else -> throw NotFoundMatchedItemFactoryException(
                "Need to add an $itemFactoryName that supports '${data::class.java.name}' to the $adapterName's $itemFactoryPropertyName property"
            )
        }
    }

    /**
     * Get the [Matchable] corresponding to the specified [itemType]
     *
     * @throws IllegalArgumentException Could not find it
     */
    fun getMatchableByItemType(itemType: Int): MATCHABLE {
        require(itemType >= 0 && itemType < matchableList.size) {
            "Unknown item type: $itemType"
        }
        return matchableList[itemType]
    }

    /**
     * Get the itemType corresponding to the specified [data]
     *
     * @throws IllegalArgumentException Could not find it
     */
    fun getItemTypeByData(
        data: Any, itemFactoryName: String, adapterName: String, itemFactoryPropertyName: String
    ): Int {
        val itemFactory =
            getMatchableByData(data, itemFactoryName, adapterName, itemFactoryPropertyName)
        return getItemTypeByMatchableMap[itemFactory]!!
    }
}