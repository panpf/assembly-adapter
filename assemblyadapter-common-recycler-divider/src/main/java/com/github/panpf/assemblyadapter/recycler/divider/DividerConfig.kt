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
import androidx.collection.SparseArrayCompat
import com.github.panpf.assemblyadapter.recycler.divider.internal.ItemDivider
import com.github.panpf.assemblyadapter.recycler.divider.internal.ItemDividerConfig

/**
 * Used to define the divider configuration of the item.
 * The divider can be disabled based on information such as position or spanIndex or provide a more personalized divider
 */
open class DividerConfig(
    protected val divider: Divider,
    protected val disableByPositionArray: SparseArrayCompat<Boolean>?,
    protected val disableBySpanIndexArray: SparseArrayCompat<Boolean>?,
    protected val personaliseByPositionArray: SparseArrayCompat<Divider>?,
    protected val personaliseBySpanIndexArray: SparseArrayCompat<Divider>?,
) {

    open fun toItemDividerConfig(context: Context): ItemDividerConfig {
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
        return ItemDividerConfig(
            divider.toItemDivider(context),
            disableByPositionArray,
            disableBySpanIndexArray,
            personaliseByPositionItemDividerArray,
            personaliseBySpanIndexItemDividerArray,
        )
    }

    open class Builder(val divider: Divider) {
        protected var disableByPositionArray: SparseArrayCompat<Boolean>? = null
        protected var disableBySpanIndexArray: SparseArrayCompat<Boolean>? = null
        protected var personaliseByPositionArray: SparseArrayCompat<Divider>? = null
        protected var personaliseBySpanIndexArray: SparseArrayCompat<Divider>? = null

        /**
         * The item with the specified position disables this divider
         */
        open fun disableByPosition(position: Int): Builder {
            (disableByPositionArray ?: SparseArrayCompat<Boolean>().apply {
                this@Builder.disableByPositionArray = this
            }).put(position, true)
            return this
        }

        /**
         * The item with the specified span index disables this divider
         */
        open fun disableBySpanIndex(spanIndex: Int): Builder {
            (disableBySpanIndexArray ?: SparseArrayCompat<Boolean>().apply {
                this@Builder.disableBySpanIndexArray = this
            }).put(spanIndex, true)
            return this
        }

        /**
         * Provide a personalized divider for the item with the specified position
         */
        open fun personaliseByPosition(position: Int, divider: Divider): Builder {
            (personaliseByPositionArray ?: SparseArrayCompat<Divider>().apply {
                this@Builder.personaliseByPositionArray = this
            }).put(position, divider)
            return this
        }

        /**
         * Provide a personalized divider for the item with the specified span index
         */
        open fun personaliseBySpanIndex(spanIndex: Int, divider: Divider): Builder {
            (personaliseBySpanIndexArray ?: SparseArrayCompat<Divider>().apply {
                this@Builder.personaliseBySpanIndexArray = this
            }).put(spanIndex, divider)
            return this
        }

        open fun build(): DividerConfig {
            return DividerConfig(
                divider,
                disableByPositionArray,
                disableBySpanIndexArray,
                personaliseByPositionArray,
                personaliseBySpanIndexArray
            )
        }
    }
}