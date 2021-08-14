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

import kotlin.reflect.KClass

interface Matchable<DATA : Any> {

    /**
     * The class of the data to be matched. The [matchData] method will match the data with the dataClass class
     * @see matchData
     */
    val dataClass: KClass<DATA>

    /**
     * If it returns true, it means that this [Matchable] can handle this [data].
     * By default, this method only checks whether [data] is an instance of [dataClass].
     * If you need to match data more accurately, you can override the [exactMatchData] method
     * @see dataClass
     * @see exactMatchData
     */
    fun matchData(data: Any): Boolean {
        @Suppress("UNCHECKED_CAST")
        return dataClass.isInstance(data) && exactMatchData(data as DATA)
    }

    /**
     * Exactly match this [data], such as checking the value of a specific attribute
     * @see matchData
     */
    fun exactMatchData(data: DATA): Boolean = true
}