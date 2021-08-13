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
import com.github.panpf.assemblyadapter.recycler.divider.internal.ItemDecorate
import com.github.panpf.assemblyadapter.recycler.divider.internal.AssemblyItemDecorateHolder
import kotlin.reflect.KClass

class AssemblyDecorateConfig constructor(
    private val decorate: Decorate,
    private val disableDecorateArray: SparseArrayCompat<Boolean>?,
    private val personaliseDecorateArray: SparseArrayCompat<Decorate>?,
    private val disableDecorateMap: ArrayMap<Class<*>, Boolean>?,
    private val personaliseDecorateMap: ArrayMap<Class<*>, Decorate>?
) {

    fun toItemDecorateHolder(
        context: Context,
        findItemFactoryClassByPosition: FindItemFactoryClassByPosition
    ): AssemblyItemDecorateHolder {
        val defaultItemDecorateConfig = DecorateConfig(
            decorate, disableDecorateArray, personaliseDecorateArray
        ).toItemDecorateHolder(context)
        val personaliseItemDecorateMap = personaliseDecorateMap?.let { oldMap ->
            ArrayMap<Class<*>, ItemDecorate>().apply {
                oldMap.forEach {
                    put(it.key, it.value.createItemDecorate(context))
                }
            }
        }
        return AssemblyItemDecorateHolder(
            defaultItemDecorateConfig,
            disableDecorateMap,
            personaliseItemDecorateMap,
            findItemFactoryClassByPosition
        )
    }

    class Builder(val decorate: Decorate) {
        private var disableDecorateArray: SparseArrayCompat<Boolean>? = null
        private var personaliseDecorateArray: SparseArrayCompat<Decorate>? = null
        private var disableDecorateMap: ArrayMap<Class<*>, Boolean>? = null
        private var personaliseDecorateMap: ArrayMap<Class<*>, Decorate>? = null

        fun disable(position: Int): Builder {
            (disableDecorateArray ?: SparseArrayCompat<Boolean>().apply {
                this@Builder.disableDecorateArray = this
            }).put(position, true)
            return this
        }

        fun personalise(position: Int, decorate: Decorate): Builder {
            (personaliseDecorateArray ?: SparseArrayCompat<Decorate>().apply {
                this@Builder.personaliseDecorateArray = this
            }).put(position, decorate)
            return this
        }

        fun disable(itemFactoryClass: KClass<*>) {
            (disableDecorateMap ?: ArrayMap<Class<*>, Boolean>().apply {
                this@Builder.disableDecorateMap = this
            })[itemFactoryClass.java] = true
        }

        fun personalise(itemFactoryClass: KClass<*>, decorate: Decorate) {
            (personaliseDecorateMap ?: ArrayMap<Class<*>, Decorate>().apply {
                this@Builder.personaliseDecorateMap = this
            })[itemFactoryClass.java] = decorate
        }

        fun build(): AssemblyDecorateConfig {
            return AssemblyDecorateConfig(
                decorate,
                disableDecorateArray,
                personaliseDecorateArray,
                disableDecorateMap,
                personaliseDecorateMap
            )
        }
    }
}