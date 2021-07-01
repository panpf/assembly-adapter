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

    fun removeData(index: Int): DATA

    fun removeAllData(datas: Collection<DATA>): Boolean

    fun clearData()

    fun sortData(comparator: Comparator<DATA>)
}