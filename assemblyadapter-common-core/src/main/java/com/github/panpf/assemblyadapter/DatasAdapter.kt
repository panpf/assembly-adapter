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
package com.github.panpf.assemblyadapter

import java.util.*

/**
 * Adapter that supports data hosting, can add, delete, modify, and check the hosted data
 */
interface DatasAdapter<DATA> {

    /**
     * Get the size of data list
     */
    val dataCount: Int

    /**
     * Get a snapshot of the data set, the snapshot is unmodifiable
     */
    val dataListSnapshot: List<DATA>

    /**
     * Get the data item associated with the specified [position] in the data set.
     *
     * @param position Position of the item whose data we want within the adapter's data set.
     * @return The data at the specified position.
     * @throws IndexOutOfBoundsException If the [position] is out of range (position < 0 || index >= dataCount)
     */
    fun getData(position: Int): DATA

    /**
     * Set up a new data list. Will copy the data of [datas], pass in null to clear the data set
     */
    fun setDataList(datas: List<DATA>?)

    /**
     * Add a new [data] item to the end of the data set
     *
     * @return `true` because the list is always modified as the result of this operation.
     */
    fun addData(data: DATA): Boolean

    /**
     * Inserts a new [data] item at the specified [index]
     */
    fun addData(index: Int, data: DATA)

    /**
     * Adds all of the elements of the specified collection to the end of this list.
     * The elements are appended in the order they appear in the [datas] collection.
     *
     * @return `true` if the list was changed as the result of the operation.
     */
    fun addAllData(datas: Collection<DATA>): Boolean

    /**
     * Inserts all of the elements of the specified collection [datas] into this list at the specified [index].
     *
     * @return `true` if the list was changed as the result of the operation.
     */
    fun addAllData(index: Int, datas: Collection<DATA>): Boolean

    /**
     * Removes the specified [data] item from the data set
     *
     * @return `true` if the list was changed as the result of the operation.
     */
    fun removeData(data: DATA): Boolean

    /**
     * Removes the data item at the specified [index] from the data set
     *
     * @return the element that has been removed.
     */
    fun removeDataAt(index: Int): DATA

    /**
     * Removes all of the elements of the specified collection [datas] from the data set
     *
     * @return `true` if the list was changed as the result of the operation.
     */
    fun removeAllData(datas: Collection<DATA>): Boolean

    /**
     * Removes all of the elements of this collection that satisfy the given
     * predicate.  Errors or runtime exceptions thrown during iteration or by
     * the predicate are relayed to the caller.
     *
     * @param filter a predicate which returns true for elements to be removed
     * @return `true` if the list was changed as the result of the operation.
     */
    fun removeDataIf(filter: (DATA) -> Boolean): Boolean

    /**
     * Empty the data set
     */
    fun clearData()

    /**
     * Sort the data set using the specified [comparator]
     */
    fun sortData(comparator: Comparator<DATA>)
}