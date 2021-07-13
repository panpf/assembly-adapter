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
package com.github.panpf.assemblyadapter.test

import com.github.panpf.assemblyadapter.internal.ItemDataStorage
import org.junit.Assert
import org.junit.Test

class ItemDataStorageTest {

    @Test
    fun testAddRemove() {
        val itemDataStorage = ItemDataStorage<Int> {}

        Assert.assertEquals("", itemDataStorage.unmodifiableDataListSnapshot.joinToString())
        itemDataStorage.addData(1)
        Assert.assertEquals("1", itemDataStorage.unmodifiableDataListSnapshot.joinToString())
        itemDataStorage.addData(3)
        Assert.assertEquals("1, 3", itemDataStorage.unmodifiableDataListSnapshot.joinToString())
    }

    @Test
    fun testAddRemoveAll() {

    }

    @Test
    fun testDataCount() {

    }

    @Test
    fun testClearData() {

    }

    @Test
    fun testSortData() {

    }

    @Test
    fun testGetData() {

    }

    @Test
    fun testDataListSnapshot() {

    }

    @Test
    fun testSetDataList() {

    }

    @Test
    fun testNullable() {

    }
}