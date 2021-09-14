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
package com.github.panpf.assemblyadapter.common.core.test.internal

import com.github.panpf.assemblyadapter.NotFoundMatchedItemFactoryException
import com.github.panpf.assemblyadapter.Placeholder
import com.github.panpf.assemblyadapter.internal.ItemFactoryStorage
import com.github.panpf.assemblyadapter.internal.Matchable
import com.github.panpf.tools4j.test.ktx.assertNoThrow
import com.github.panpf.tools4j.test.ktx.assertThrow
import org.junit.Assert
import org.junit.Test
import java.util.*
import kotlin.reflect.KClass

class ItemFactoryStorageTest {

    open class TestItemFactory<DATA : Any>(override val dataClass: KClass<DATA>) : Matchable<DATA>

    class StringItemFactory : TestItemFactory<String>(String::class)

    class IntItemFactory : TestItemFactory<Int>(Int::class)

    class BooleanItemFactory : TestItemFactory<Boolean>(Boolean::class)

    class DateItemFactory : TestItemFactory<Date>(Date::class)

    @Test
    fun testConstructor() {
        assertNoThrow {
            ItemFactoryStorage(listOf(), "ItemFactory", "TestAdapter", "itemFactoryList")
        }

        assertNoThrow {
            ItemFactoryStorage(
                listOf(TestItemFactory(String::class)),
                "ItemFactory", "TestAdapter", "itemFactoryList"
            )
        }
    }

    @Test
    fun testPropertyItemTypeCount() {
        Assert.assertEquals(
            0,
            ItemFactoryStorage(
                listOf(),
                "ItemFactory", "TestAdapter", "itemFactoryList"
            ).itemTypeCount
        )
        Assert.assertEquals(
            1,
            ItemFactoryStorage(
                listOf(IntItemFactory()),
                "ItemFactory", "TestAdapter", "itemFactoryList"
            ).itemTypeCount
        )
        Assert.assertEquals(
            2,
            ItemFactoryStorage(
                listOf(IntItemFactory(), StringItemFactory()),
                "ItemFactory", "TestAdapter", "itemFactoryList"
            ).itemTypeCount
        )
        Assert.assertEquals(
            3,
            ItemFactoryStorage(
                listOf(IntItemFactory(), StringItemFactory(), BooleanItemFactory()),
                "ItemFactory", "TestAdapter", "itemFactoryList"
            ).itemTypeCount
        )
    }

    @Test
    fun testMethodGetItemFactoryByData() {
        val itemFactoryStorage = ItemFactoryStorage(
            listOf(IntItemFactory(), StringItemFactory(), BooleanItemFactory()),
            "ItemFactory", "TestAdapter", "itemFactoryList"
        )

        Assert.assertEquals(
            IntItemFactory::class,
            itemFactoryStorage.getItemFactoryByData(3)::class
        )

        Assert.assertEquals(
            StringItemFactory::class,
            itemFactoryStorage.getItemFactoryByData("3")::class
        )

        Assert.assertEquals(
            BooleanItemFactory::class,
            itemFactoryStorage.getItemFactoryByData(true)::class
        )

        assertThrow(NotFoundMatchedItemFactoryException::class) {
            itemFactoryStorage.getItemFactoryByData(3L)::class
        }

        assertThrow(NotFoundMatchedItemFactoryException::class) {
            itemFactoryStorage.getItemFactoryByData(Placeholder)
        }
    }

    @Test
    fun testMethodGetItemFactoryByClass() {
        val intItemFactory = IntItemFactory()
        val stringItemFactory = StringItemFactory()
        val booleanItemFactory = BooleanItemFactory()
        val itemFactoryStorage = ItemFactoryStorage(
            listOf(intItemFactory, stringItemFactory, booleanItemFactory),
            "ItemFactory", "TestAdapter", "itemFactoryList"
        )

        Assert.assertSame(
            intItemFactory,
            itemFactoryStorage.getItemFactoryByClass(IntItemFactory::class.java)
        )

        Assert.assertSame(
            stringItemFactory,
            itemFactoryStorage.getItemFactoryByClass(StringItemFactory::class.java)
        )

        Assert.assertSame(
            booleanItemFactory,
            itemFactoryStorage.getItemFactoryByClass(BooleanItemFactory::class.java)
        )

        assertThrow(NotFoundMatchedItemFactoryException::class) {
            itemFactoryStorage.getItemFactoryByClass(DateItemFactory::class.java)
        }
    }

    @Test
    fun testMethodGetItemFactoryByItemType() {
        val itemFactoryStorage = ItemFactoryStorage(
            listOf(IntItemFactory(), StringItemFactory(), BooleanItemFactory()),
            "ItemFactory", "TestAdapter", "itemFactoryList"
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
            listOf(IntItemFactory(), StringItemFactory(), BooleanItemFactory()),
            "ItemFactory", "TestAdapter", "itemFactoryList"
        )

        Assert.assertEquals(0, itemFactoryStorage.getItemTypeByData(3))

        Assert.assertEquals(1, itemFactoryStorage.getItemTypeByData("3"))

        Assert.assertEquals(2, itemFactoryStorage.getItemTypeByData(true))

        assertThrow(NotFoundMatchedItemFactoryException::class) {
            itemFactoryStorage.getItemTypeByData(3L)
        }

        assertThrow(NotFoundMatchedItemFactoryException::class) {
            itemFactoryStorage.getItemTypeByData(Placeholder)
        }
    }
}