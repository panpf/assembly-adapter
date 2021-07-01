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

import java.util.Comparator

interface DatasAdapter<DATA> {

    val dataCount: Int

    val dataListSnapshot: List<DATA>

    fun getData(position: Int): DATA

    fun setDataList(datas: List<DATA>?)

    fun addData(data: DATA): Boolean

    fun addData(index: Int, data: DATA)

    fun addAllData(datas: Collection<DATA>): Boolean

    fun addAllData(index: Int, datas: Collection<DATA>): Boolean

    fun removeData(data: DATA): Boolean

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

    fun clearData()

    fun sortData(comparator: Comparator<DATA>)
}