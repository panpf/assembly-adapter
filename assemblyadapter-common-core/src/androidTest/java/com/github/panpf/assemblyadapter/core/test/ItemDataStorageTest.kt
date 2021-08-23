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
package com.github.panpf.assemblyadapter.core.test

import com.github.panpf.assemblyadapter.internal.ItemDataStorage
import com.github.panpf.tools4j.test.ktx.assertThrow
import org.junit.Assert
import org.junit.Test

class ItemDataStorageTest {

    @Test
    @Suppress("RemoveExplicitTypeArguments")
    fun testConstructor() {
        /*
         * test initDataList property
         */
        Assert.assertEquals(0, ItemDataStorage<Int> {}.dataCount)
        Assert.assertEquals(0, ItemDataStorage<Int>(listOf()) {}.dataCount)
        Assert.assertEquals(1, ItemDataStorage<Int>(listOf(10)) {}.dataCount)
        Assert.assertEquals(3, ItemDataStorage<Int>(listOf(10, 7, 8)) {}.dataCount)

        /*
         * test onDataListChanged property
         */
        var onDataListChangedResult = ""
        var itemDataStorage: ItemDataStorage<Int>? = null
        val onDataListChanged: (() -> Unit) = {
            onDataListChangedResult = itemDataStorage!!.readOnlyList
                .joinToString { it.toString() }
        }
        itemDataStorage = ItemDataStorage<Int>(null, onDataListChanged)
        Assert.assertEquals("", onDataListChangedResult)

        itemDataStorage.submitList(listOf(3, 8, 1, 6))
        Assert.assertEquals("3, 8, 1, 6", onDataListChangedResult)

        itemDataStorage.submitList(listOf(4, 1))
        Assert.assertEquals("4, 1", onDataListChangedResult)
    }

    @Test
    fun testPropertyReadOnlyList() {
        val itemDataStorage = ItemDataStorage<Int> {}
        Assert.assertEquals(
            "",
            itemDataStorage.readOnlyList.joinToString { it.toString() }
        )

        val newList = listOf(6, 3, 0)
        itemDataStorage.submitList(newList)
        Assert.assertEquals(
            "6, 3, 0",
            itemDataStorage.readOnlyList.joinToString { it.toString() }
        )

        val getList = itemDataStorage.readOnlyList
        Assert.assertNotSame(newList, getList.toList())

        assertThrow(UnsupportedOperationException::class) {
            (getList as java.util.List<Int>).add(7)
        }
    }

    @Test
    fun testPropertyDatCount() {
        val itemDataStorage = ItemDataStorage<Int> {}
        Assert.assertEquals(0, itemDataStorage.dataCount)

        itemDataStorage.submitList(listOf(6, 3, 0))
        Assert.assertEquals(3, itemDataStorage.dataCount)

        itemDataStorage.submitList(listOf())
        Assert.assertEquals(0, itemDataStorage.dataCount)
    }

    @Test
    fun testMethodGetData() {
        val itemDataStorage = ItemDataStorage<Int> {}
        assertThrow(IndexOutOfBoundsException::class) {
            itemDataStorage.getData(0)
        }

        itemDataStorage.submitList(listOf(6, 3, 0))
        Assert.assertEquals(6, itemDataStorage.getData(0))
        Assert.assertEquals(3, itemDataStorage.getData(1))
        Assert.assertEquals(0, itemDataStorage.getData(2))
        assertThrow(IndexOutOfBoundsException::class) {
            itemDataStorage.getData(-1)
        }
        assertThrow(IndexOutOfBoundsException::class) {
            itemDataStorage.getData(3)
        }
    }

    @Test
    fun testMethodSubmitList() {
        val itemDataStorage = ItemDataStorage<Int> {}
        Assert.assertEquals(
            "",
            itemDataStorage.readOnlyList.joinToString { it.toString() }
        )

        itemDataStorage.submitList(listOf(6, 3, 0))
        Assert.assertEquals(
            "6, 3, 0",
            itemDataStorage.readOnlyList.joinToString { it.toString() }
        )

        itemDataStorage.submitList(listOf(3))
        Assert.assertEquals(
            "3",
            itemDataStorage.readOnlyList.joinToString { it.toString() }
        )

        itemDataStorage.submitList(listOf())
        Assert.assertEquals(
            "",
            itemDataStorage.readOnlyList.joinToString { it.toString() }
        )
    }
}