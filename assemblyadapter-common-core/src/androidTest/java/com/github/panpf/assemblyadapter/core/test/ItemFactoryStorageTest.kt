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

import com.github.panpf.assemblyadapter.NotFoundMatchedItemFactoryException
import com.github.panpf.assemblyadapter.Placeholder
import com.github.panpf.assemblyadapter.internal.ItemFactoryStorage
import com.github.panpf.assemblyadapter.internal.Matchable
import com.github.panpf.tools4j.test.ktx.assertNoThrow
import com.github.panpf.tools4j.test.ktx.assertThrow
import org.junit.Assert
import org.junit.Test
import kotlin.reflect.KClass

class ItemFactoryStorageTest {

    @Test
    fun testConstructor() {
        assertNoThrow {
            ItemFactoryStorage(listOf())
        }

        assertNoThrow {
            ItemFactoryStorage(listOf(TestItemFactory(String::class)))
        }
    }

    @Test
    fun testPropertyItemTypeCount() {
        Assert.assertEquals(
            0,
            ItemFactoryStorage(listOf()).itemTypeCount
        )
        Assert.assertEquals(
            1,
            ItemFactoryStorage(listOf(IntItemFactory())).itemTypeCount
        )
        Assert.assertEquals(
            2,
            ItemFactoryStorage(
                listOf(
                    IntItemFactory(),
                    StringItemFactory(),
                )
            ).itemTypeCount
        )
        Assert.assertEquals(
            3,
            ItemFactoryStorage(
                listOf(
                    IntItemFactory(),
                    StringItemFactory(),
                    BooleanItemFactory()
                )
            ).itemTypeCount
        )
    }

    @Test
    fun testMethodGetItemFactoryByData() {
        val itemFactoryStorage = ItemFactoryStorage(
            listOf(
                IntItemFactory(),
                StringItemFactory(),
                BooleanItemFactory()
            )
        )

        Assert.assertEquals(
            IntItemFactory::class,
            itemFactoryStorage.getItemFactoryByData(
                3, "TestItemFactory", "TestAdapter", "itemFactoryList"
            )::class
        )

        Assert.assertEquals(
            StringItemFactory::class,
            itemFactoryStorage.getItemFactoryByData(
                "3", "TestItemFactory", "TestAdapter", "itemFactoryList"
            )::class
        )

        Assert.assertEquals(
            BooleanItemFactory::class,
            itemFactoryStorage.getItemFactoryByData(
                true, "TestItemFactory", "TestAdapter", "itemFactoryList"
            )::class
        )

        assertThrow(NotFoundMatchedItemFactoryException::class) {
            itemFactoryStorage.getItemFactoryByData(
                3L, "TestItemFactory", "TestAdapter", "itemFactoryList"
            )::class
        }

        assertThrow(NotFoundMatchedItemFactoryException::class) {
            itemFactoryStorage.getItemFactoryByData(
                Placeholder, "TestItemFactory", "TestAdapter", "itemFactoryList"
            )
        }
    }

    @Test
    fun testMethodGetItemFactoryByItemType() {
        val itemFactoryStorage = ItemFactoryStorage(
            listOf(
                IntItemFactory(),
                StringItemFactory(),
                BooleanItemFactory()
            )
        )

        Assert.assertEquals(
            IntItemFactory::class,
            itemFactoryStorage.getItemFactoryByItemType(0)::class
        )

        Assert.assertEquals(
            StringItemFactory::class,
            itemFactoryStorage.getItemFactoryByItemType(1)::class
        )

        Assert.assertEquals(
            BooleanItemFactory::class,
            itemFactoryStorage.getItemFactoryByItemType(2)::class
        )

        assertThrow(IllegalArgumentException::class) {
            itemFactoryStorage.getItemFactoryByItemType(-1)
        }

        assertThrow(IllegalArgumentException::class) {
            itemFactoryStorage.getItemFactoryByItemType(3)
        }
    }

    @Test
    fun testMethodGetItemTypeByData() {
        val itemFactoryStorage = ItemFactoryStorage(
            listOf(
                IntItemFactory(),
                StringItemFactory(),
                BooleanItemFactory()
            )
        )

        Assert.assertEquals(
            0,
            itemFactoryStorage.getItemTypeByData(
                3, "TestItemFactory", "TestAdapter", "itemFactoryList"
            )
        )

        Assert.assertEquals(
            1,
            itemFactoryStorage.getItemTypeByData(
                "3", "TestItemFactory", "TestAdapter", "itemFactoryList"
            )
        )

        Assert.assertEquals(
            2,
            itemFactoryStorage.getItemTypeByData(
                true, "TestItemFactory", "TestAdapter", "itemFactoryList"
            )
        )

        assertThrow(NotFoundMatchedItemFactoryException::class) {
            itemFactoryStorage.getItemTypeByData(
                3L, "TestItemFactory", "TestAdapter", "itemFactoryList"
            )
        }

        assertThrow(NotFoundMatchedItemFactoryException::class) {
            itemFactoryStorage.getItemTypeByData(
                Placeholder, "TestItemFactory", "TestAdapter", "itemFactoryList"
            )
        }
    }

    open class TestItemFactory<DATA : Any>(override val dataClass: KClass<DATA>) : Matchable<DATA>

    class StringItemFactory : TestItemFactory<String>(String::class)

    class IntItemFactory : TestItemFactory<Int>(Int::class)

    class BooleanItemFactory : TestItemFactory<Boolean>(Boolean::class)
}