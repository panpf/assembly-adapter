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
import com.github.panpf.assemblyadapter.recycler.divider.internal.AssemblyItemDecorateConfig
import com.github.panpf.assemblyadapter.recycler.divider.internal.ItemDecorate
import com.github.panpf.assemblyadapter.recycler.divider.internal.ItemDecorateConfig
import kotlin.reflect.KClass

class AssemblyDecorateConfig constructor(
    decorate: Decorate,
    disableByPositionArray: SparseArrayCompat<Boolean>?,
    disableBySpanIndexArray: SparseArrayCompat<Boolean>?,
    private val disableByItemFactoryClassMap: ArrayMap<Class<*>, Boolean>?,
    personaliseByPositionArray: SparseArrayCompat<Decorate>?,
    personaliseBySpanIndexArray: SparseArrayCompat<Decorate>?,
    private val personaliseByItemFactoryClassMap: ArrayMap<Class<*>, Decorate>?
) : DecorateConfig(
    decorate,
    disableByPositionArray,
    disableBySpanIndexArray,
    personaliseByPositionArray,
    personaliseBySpanIndexArray
) {

    @Deprecated(message = "Please use 'toItemDecorateHolder(Context, FindItemFactoryClassByPosition)' method instead")
    override fun toItemDecorateHolder(context: Context): ItemDecorateConfig {
        throw UnsupportedOperationException("Please use 'toItemDecorateHolder(Context, FindItemFactoryClassByPosition)' method instead")
    }

    fun toItemDecorateHolder(
        context: Context,
        findItemFactoryClassByPosition: FindItemFactoryClassByPosition
    ): AssemblyItemDecorateConfig {
        val personaliseByPositionItemDecorateArray = personaliseByPositionArray?.let { oldArray ->
            SparseArrayCompat<ItemDecorate>().apply {
                0.until(oldArray.size()).forEach { index ->
                    put(oldArray.keyAt(index), oldArray.valueAt(index).createItemDecorate(context))
                }
            }
        }
        val personaliseBySpanIndexItemDecorateArray = personaliseBySpanIndexArray?.let { oldArray ->
            SparseArrayCompat<ItemDecorate>().apply {
                0.until(oldArray.size()).forEach { index ->
                    put(oldArray.keyAt(index), oldArray.valueAt(index).createItemDecorate(context))
                }
            }
        }
        val personaliseByItemFactoryClassMap = personaliseByItemFactoryClassMap?.let { oldMap ->
            ArrayMap<Class<*>, ItemDecorate>().apply {
                oldMap.forEach {
                    put(it.key, it.value.createItemDecorate(context))
                }
            }
        }
        return AssemblyItemDecorateConfig(
            decorate.createItemDecorate(context),
            disableByPositionArray,
            disableBySpanIndexArray,
            disableByItemFactoryClassMap,
            personaliseByPositionItemDecorateArray,
            personaliseBySpanIndexItemDecorateArray,
            personaliseByItemFactoryClassMap,
            findItemFactoryClassByPosition
        )
    }

    class Builder(decorate: Decorate) : DecorateConfig.Builder(decorate) {
        private var disableByPositionArray: SparseArrayCompat<Boolean>? = null
        private var disableBySpanIndexArray: SparseArrayCompat<Boolean>? = null
        private var disableByItemFactoryClassMap: ArrayMap<Class<*>, Boolean>? = null
        private var personaliseByPositionArray: SparseArrayCompat<Decorate>? = null
        private var personaliseBySpanIndexArray: SparseArrayCompat<Decorate>? = null
        private var personaliseByItemFactoryClassMap: ArrayMap<Class<*>, Decorate>? = null

        override fun disableByPosition(position: Int): Builder {
            super.disableByPosition(position)
            return this
        }

        override fun disableBySpanIndex(spanIndex: Int): Builder {
            super.disableBySpanIndex(spanIndex)
            return this
        }

        fun disableByItemFactoryClass(itemFactoryClass: KClass<*>): Builder {
            (disableByItemFactoryClassMap ?: ArrayMap<Class<*>, Boolean>().apply {
                this@Builder.disableByItemFactoryClassMap = this
            })[itemFactoryClass.java] = true
            return this
        }

        override fun personaliseByPosition(position: Int, decorate: Decorate): Builder {
            super.personaliseByPosition(position, decorate)
            return this
        }

        override fun personaliseBySpanIndex(spanIndex: Int, decorate: Decorate): Builder {
            super.personaliseBySpanIndex(spanIndex, decorate)
            return this
        }

        fun personaliseByItemFactoryClass(
            itemFactoryClass: KClass<*>,
            decorate: Decorate
        ): Builder {
            (personaliseByItemFactoryClassMap ?: ArrayMap<Class<*>, Decorate>().apply {
                this@Builder.personaliseByItemFactoryClassMap = this
            })[itemFactoryClass.java] = decorate
            return this
        }

        override fun build(): AssemblyDecorateConfig {
            return AssemblyDecorateConfig(
                decorate,
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