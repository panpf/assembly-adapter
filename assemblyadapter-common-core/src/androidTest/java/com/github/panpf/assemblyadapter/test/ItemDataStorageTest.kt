package com.github.panpf.assemblyadapter.test

import androidx.test.runner.AndroidJUnit4
import com.github.panpf.assemblyadapter.DataManager
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ItemDataStorageTest {

    @Test
    fun testConstructor() {
        val callback = DataManager.Callback { }
        Assert.assertEquals(0, DataManager<Any>(callback).dataCount)

        Assert.assertEquals(3, DataManager(
            callback,
            listOf("a", "c", "d")
        ).dataCount)
        Assert.assertEquals(4, DataManager(
            callback,
            listOf("a", "c", "d", "a")
        ).dataCount)

        Assert.assertEquals(3, DataManager(
            callback,
            arrayOf("a", "c", "d")
        ).dataCount)
        Assert.assertEquals(4, DataManager(
            callback,
            arrayOf("a", "c", "d", "a")
        ).dataCount)
    }

//    @Test
//    fun testDataManager() {
//        val dataManager = DataManager()
//    }
}