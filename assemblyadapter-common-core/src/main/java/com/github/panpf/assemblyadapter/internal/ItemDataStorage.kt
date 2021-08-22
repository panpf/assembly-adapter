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

import java.util.*

/**
 * Responsible for managing data sets
 */
class ItemDataStorage<DATA>(
    initDataList: List<DATA>? = null,
    private val onDataListChanged: () -> Unit
) {

    var readOnlyList: List<DATA> =
        initDataList?.run { Collections.unmodifiableList(this) } ?: Collections.emptyList()

    /**
     * Get the size of data list
     */
    val dataCount: Int
        get() = readOnlyList.size

    /**
     * Get the data item associated with the specified [position] in the data set.
     *
     * @param position Position of the item whose data we want within the adapter's data set.
     * @return The data at the specified position.
     * @throws IndexOutOfBoundsException If the [position] is out of range (position < 0 || index >= dataCount)
     */
    fun getData(position: Int): DATA {
        if (position < 0 || position >= readOnlyList.size) {
            throw IndexOutOfBoundsException("Index: " + position + ", Size: " + readOnlyList.size)
        }
        return readOnlyList[position]
    }

    /**
     * Set up a new data list. Will copy the data of [list], pass in null to clear the data set
     */
    fun submitList(list: List<DATA>?) {
        readOnlyList =
            list?.run { Collections.unmodifiableList(this) } ?: Collections.emptyList()
        onDataListChanged()
    }
}