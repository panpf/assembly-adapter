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
package com.github.panpf.assemblyadapter.recycler.divider.internal

import androidx.collection.ArrayMap
import androidx.collection.SparseArrayCompat
import androidx.recyclerview.widget.RecyclerView
import com.github.panpf.assemblyadapter.recycler.divider.FindItemFactoryClassSupport

class AssemblyItemDividerConfig(
    private val itemDivider: ItemDivider,
    private val disableByPositionArray: SparseArrayCompat<Boolean>?,
    private val disableBySpanIndexArray: SparseArrayCompat<Boolean>?,
    private val disableByItemFactoryClassMap: ArrayMap<Class<*>, Boolean>?,
    private val personaliseByPositionArray: SparseArrayCompat<ItemDivider>?,
    private val personaliseBySpanIndexArray: SparseArrayCompat<ItemDivider>?,
    private val personaliseByItemFactoryClassMap: ArrayMap<Class<*>, ItemDivider>?,
    private val findItemFactoryClassSupport: FindItemFactoryClassSupport,
) : ItemDividerConfig(
    itemDivider,
    disableByPositionArray,
    disableBySpanIndexArray,
    personaliseByPositionArray,
    personaliseBySpanIndexArray
) {

    override fun get(parent: RecyclerView, position: Int, spanIndex: Int): ItemDivider? {
        if (disableByPositionArray?.get(position, false) == true) {
            return null
        }

        if (disableBySpanIndexArray?.get(spanIndex, false) == true) {
            return null
        }

        var itemFactoryClass: Class<*>? = null
        if (disableByItemFactoryClassMap != null) {
            if (itemFactoryClass == null) {
                itemFactoryClass = parent.adapter?.let {
                    findItemFactoryClassSupport.findItemFactoryClassByPosition(it, position)
                }
            }
            if (itemFactoryClass != null) {
                if (disableByItemFactoryClassMap[itemFactoryClass] == true) {
                    return null
                }
            }
        }

        val personaliseByPositionItemDivider = personaliseByPositionArray?.get(position)
        if (personaliseByPositionItemDivider != null) {
            return personaliseByPositionItemDivider
        }

        val personaliseBySpanIndexItemDivider = personaliseBySpanIndexArray?.get(spanIndex)
        if (personaliseBySpanIndexItemDivider != null) {
            return personaliseBySpanIndexItemDivider
        }

        if (personaliseByItemFactoryClassMap != null) {
            if (itemFactoryClass == null) {
                itemFactoryClass = parent.adapter?.let {
                    findItemFactoryClassSupport.findItemFactoryClassByPosition(it, position)
                }
            }
            if (itemFactoryClass != null) {
                val personaliseItemDivider =
                    personaliseByItemFactoryClassMap[itemFactoryClass]
                if (personaliseItemDivider != null) {
                    return personaliseItemDivider
                }
            }
        }

        return itemDivider
    }
}