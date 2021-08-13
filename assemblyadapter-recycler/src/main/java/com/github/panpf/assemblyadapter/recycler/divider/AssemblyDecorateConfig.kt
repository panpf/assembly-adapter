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
import com.github.panpf.assemblyadapter.recycler.divider.internal.AssemblyItemDecorateHolder
import com.github.panpf.assemblyadapter.recycler.divider.internal.ItemDecorate
import kotlin.reflect.KClass

class AssemblyDecorateConfig constructor(
    private val decorate: Decorate,
    private val disableByPositionArray: SparseArrayCompat<Boolean>?,
    private val disableBySpanIndexArray: SparseArrayCompat<Boolean>?,
    private val disableByItemFactoryClassMap: ArrayMap<Class<*>, Boolean>?,
    private val personaliseByPositionArray: SparseArrayCompat<Decorate>?,
    private val personaliseBySpanIndexArray: SparseArrayCompat<Decorate>?,
    private val personaliseByItemFactoryClassMap: ArrayMap<Class<*>, Decorate>?
) {

    fun toItemDecorateHolder(
        context: Context,
        findItemFactoryClassByPosition: FindItemFactoryClassByPosition
    ): AssemblyItemDecorateHolder {
        val defaultItemDecorateConfig = DecorateConfig(
            decorate,
            disableByPositionArray,
            disableBySpanIndexArray,
            personaliseByPositionArray,
            personaliseBySpanIndexArray
        ).toItemDecorateHolder(context)
        val personaliseByItemFactoryClassMap = personaliseByItemFactoryClassMap?.let { oldMap ->
            ArrayMap<Class<*>, ItemDecorate>().apply {
                oldMap.forEach {
                    put(it.key, it.value.createItemDecorate(context))
                }
            }
        }
        return AssemblyItemDecorateHolder(
            defaultItemDecorateConfig,
            disableByItemFactoryClassMap,
            personaliseByItemFactoryClassMap,
            findItemFactoryClassByPosition
        )
    }

    class Builder(val decorate: Decorate) {
        private var disableByPositionArray: SparseArrayCompat<Boolean>? = null
        private var disableBySpanIndexArray: SparseArrayCompat<Boolean>? = null
        private var disableByItemFactoryClassMap: ArrayMap<Class<*>, Boolean>? = null
        private var personaliseByPositionArray: SparseArrayCompat<Decorate>? = null
        private var personaliseBySpanIndexArray: SparseArrayCompat<Decorate>? = null
        private var personaliseByItemFactoryClassMap: ArrayMap<Class<*>, Decorate>? = null

        fun disableByPosition(position: Int): Builder {
            (disableByPositionArray ?: SparseArrayCompat<Boolean>().apply {
                this@Builder.disableByPositionArray = this
            }).put(position, true)
            return this
        }

        fun disableBySpanIndex(spanIndex: Int): Builder {
            (disableBySpanIndexArray ?: SparseArrayCompat<Boolean>().apply {
                this@Builder.disableBySpanIndexArray = this
            }).put(spanIndex, true)
            return this
        }

        fun disableByItemFactoryClass(itemFactoryClass: KClass<*>) {
            (disableByItemFactoryClassMap ?: ArrayMap<Class<*>, Boolean>().apply {
                this@Builder.disableByItemFactoryClassMap = this
            })[itemFactoryClass.java] = true
        }

        fun personaliseByPosition(position: Int, decorate: Decorate): Builder {
            (personaliseByPositionArray ?: SparseArrayCompat<Decorate>().apply {
                this@Builder.personaliseByPositionArray = this
            }).put(position, decorate)
            return this
        }

        fun personaliseBySpanIndex(spanIndex: Int, decorate: Decorate): Builder {
            (personaliseBySpanIndexArray ?: SparseArrayCompat<Decorate>().apply {
                this@Builder.personaliseBySpanIndexArray = this
            }).put(spanIndex, decorate)
            return this
        }

        fun personaliseByItemFactoryClass(itemFactoryClass: KClass<*>, decorate: Decorate) {
            (personaliseByItemFactoryClassMap ?: ArrayMap<Class<*>, Decorate>().apply {
                this@Builder.personaliseByItemFactoryClassMap = this
            })[itemFactoryClass.java] = decorate
        }

        fun build(): AssemblyDecorateConfig {
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