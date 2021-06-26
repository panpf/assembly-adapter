package com.github.panpf.assemblyadapter.test

import com.github.panpf.assemblyadapter.internal.ItemDataStorage
import org.junit.Assert
import org.junit.Test

class ItemDataStorageTest {

    @Test
    fun testAddRemove() {
        val itemDataStorage = ItemDataStorage<Int> {}

        Assert.assertEquals("", itemDataStorage.dataListSnapshot.joinToString())
        itemDataStorage.addData(1)
        Assert.assertEquals("1", itemDataStorage.dataListSnapshot.joinToString())
        itemDataStorage.addData(3)
        Assert.assertEquals("1, 3", itemDataStorage.dataListSnapshot.joinToString())
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