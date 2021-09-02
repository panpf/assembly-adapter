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
package com.github.panpf.assemblyadapter.recycler.divider

import android.content.Context
import androidx.collection.ArrayMap
import androidx.collection.SparseArrayCompat
import com.github.panpf.assemblyadapter.recycler.divider.internal.AssemblyItemDividerConfig
import com.github.panpf.assemblyadapter.recycler.divider.internal.ItemDivider
import com.github.panpf.assemblyadapter.recycler.divider.internal.ItemDividerConfig
import kotlin.reflect.KClass

/**
 * Used to define the divider configuration of the item.
 * The divider can be disabled based on information such as position or spanIndex or ItemFactory class or provide a more personalized divider
 */
class AssemblyDividerConfig constructor(
    divider: Divider,
    disableByPositionArray: SparseArrayCompat<Boolean>?,
    disableBySpanIndexArray: SparseArrayCompat<Boolean>?,
    private val disableByItemFactoryClassMap: ArrayMap<Class<*>, Boolean>?,
    personaliseByPositionArray: SparseArrayCompat<Divider>?,
    personaliseBySpanIndexArray: SparseArrayCompat<Divider>?,
    private val personaliseByItemFactoryClassMap: ArrayMap<Class<*>, Divider>?
) : DividerConfig(
    divider,
    disableByPositionArray,
    disableBySpanIndexArray,
    personaliseByPositionArray,
    personaliseBySpanIndexArray
) {

    @Deprecated(message = "Please use 'toAssemblyItemDividerConfig(Context, FindItemFactoryClassByPosition)' method instead")
    override fun toItemDividerConfig(context: Context): ItemDividerConfig {
        throw UnsupportedOperationException("Please use 'toAssemblyItemDividerConfig(Context, FindItemFactoryClassByPosition)' method instead")
    }

    fun toAssemblyItemDividerConfig(
        context: Context,
        findItemFactoryClassSupport: FindItemFactoryClassSupport
    ): AssemblyItemDividerConfig {
        val personaliseByPositionItemDividerArray = personaliseByPositionArray?.let { oldArray ->
            SparseArrayCompat<ItemDivider>().apply {
                0.until(oldArray.size()).forEach { index ->
                    put(oldArray.keyAt(index), oldArray.valueAt(index).toItemDivider(context))
                }
            }
        }
        val personaliseBySpanIndexItemDividerArray = personaliseBySpanIndexArray?.let { oldArray ->
            SparseArrayCompat<ItemDivider>().apply {
                0.until(oldArray.size()).forEach { index ->
                    put(oldArray.keyAt(index), oldArray.valueAt(index).toItemDivider(context))
                }
            }
        }
        val personaliseByItemFactoryClassMap = personaliseByItemFactoryClassMap?.let { oldMap ->
            ArrayMap<Class<*>, ItemDivider>().apply {
                oldMap.forEach {
                    put(it.key, it.value.toItemDivider(context))
                }
            }
        }
        return AssemblyItemDividerConfig(
            divider.toItemDivider(context),
            disableByPositionArray,
            disableBySpanIndexArray,
            disableByItemFactoryClassMap,
            personaliseByPositionItemDividerArray,
            personaliseBySpanIndexItemDividerArray,
            personaliseByItemFactoryClassMap,
            findItemFactoryClassSupport
        )
    }

    class Builder(divider: Divider) : DividerConfig.Builder(divider) {
        private var disableByItemFactoryClassMap: ArrayMap<Class<*>, Boolean>? = null
        private var personaliseByItemFactoryClassMap: ArrayMap<Class<*>, Divider>? = null

        override fun disableByPosition(position: Int): Builder {
            super.disableByPosition(position)
            return this
        }

        override fun disableBySpanIndex(spanIndex: Int): Builder {
            super.disableBySpanIndex(spanIndex)
            return this
        }

        /**
         * The item with the specified ItemFactory Class disables this divider
         */
        fun disableByItemFactoryClass(itemFactoryClass: KClass<*>): Builder {
            (disableByItemFactoryClassMap ?: ArrayMap<Class<*>, Boolean>().apply {
                this@Builder.disableByItemFactoryClassMap = this
            })[itemFactoryClass.java] = true
            return this
        }

        override fun personaliseByPosition(position: Int, divider: Divider): Builder {
            super.personaliseByPosition(position, divider)
            return this
        }

        override fun personaliseBySpanIndex(spanIndex: Int, divider: Divider): Builder {
            super.personaliseBySpanIndex(spanIndex, divider)
            return this
        }

        /**
         * Provide a personalized divider for the item with the specified ItemFactory Class
         */
        fun personaliseByItemFactoryClass(
            itemFactoryClass: KClass<*>,
            divider: Divider
        ): Builder {
            (personaliseByItemFactoryClassMap ?: ArrayMap<Class<*>, Divider>().apply {
                this@Builder.personaliseByItemFactoryClassMap = this
            })[itemFactoryClass.java] = divider
            return this
        }

        override fun build(): AssemblyDividerConfig {
            return AssemblyDividerConfig(
                divider,
                disableByPositionArray,
                disableBySpanIndexArray,
                disableByItemFactoryClassMap,
                personaliseByPositionArray,
                personaliseBySpanIndexArray,
                personaliseByItemFactoryClassMap
            )
        }
    }
}