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

import com.github.panpf.assemblyadapter.internal.Matchable
import kotlin.reflect.KClass

/**
 * The adapter that implements the [AssemblyAdapter] interface can easily support any multi type of adapters by cooperating with the predefined ItemFactory
 */
interface AssemblyAdapter<DATA, ITEM_FACTORY : Matchable<out Any>> {

    /**
     * Get the ItemFactory of the specified [position]
     *
     * @throws IndexOutOfBoundsException If the [position] is out of range (position < 0 || index >= count())
     * @throws NotFoundMatchedItemFactoryException No ItemFactory can match the data corresponding to [position]
     */
    fun getItemFactoryByPosition(position: Int): ITEM_FACTORY

    /**
     * Get the ItemFactory of the specified [data]
     *
     * @throws NotFoundMatchedItemFactoryException No ItemFactory can match the [data]
     */
    fun getItemFactoryByData(data: DATA): ITEM_FACTORY

    /**
     * Get the ItemFactory of the specified [itemFactoryClass]
     *
     * @throws NotFoundMatchedItemFactoryException No ItemFactory can match the [itemFactoryClass]
     */
    fun <T : ITEM_FACTORY> getItemFactoryByItemFactoryClass(itemFactoryClass: KClass<T>): T

    /**
     * Get the ItemFactory of the specified [itemFactoryClass]
     *
     * @throws NotFoundMatchedItemFactoryException No ItemFactory can match the [itemFactoryClass]
     */
    fun <T : ITEM_FACTORY> getItemFactoryByItemFactoryClass(itemFactoryClass: Class<T>): T
}