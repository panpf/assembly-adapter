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
 * 用来定义 item 的 divider 配置。一个 divider 可以根据 position 或 spanIndex 等信息禁用或者提供更个性化的 divider
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
        private var disableByPositionArray: SparseArrayCompat<Boolean>? = null
        private var disableBySpanIndexArray: SparseArrayCompat<Boolean>? = null
        private var personaliseByPositionDividerArray: SparseArrayCompat<Divider>? = null
        private var personaliseBySpanIndexDividerArray: SparseArrayCompat<Divider>? = null

        open fun disableByPosition(position: Int): Builder {
            (disableByPositionArray ?: SparseArrayCompat<Boolean>().apply {
                this@Builder.disableByPositionArray = this
            }).put(position, true)
            return this
        }

        open fun disableBySpanIndex(spanIndex: Int): Builder {
            (disableBySpanIndexArray ?: SparseArrayCompat<Boolean>().apply {
                this@Builder.disableBySpanIndexArray = this
            }).put(spanIndex, true)
            return this
        }

        open fun personaliseByPosition(position: Int, divider: Divider): Builder {
            (personaliseByPositionDividerArray ?: SparseArrayCompat<Divider>().apply {
                this@Builder.personaliseByPositionDividerArray = this
            }).put(position, divider)
            return this
        }

        open fun personaliseBySpanIndex(spanIndex: Int, divider: Divider): Builder {
            (personaliseBySpanIndexDividerArray ?: SparseArrayCompat<Divider>().apply {
                this@Builder.personaliseBySpanIndexDividerArray = this
            }).put(spanIndex, divider)
            return this
        }

        open fun build(): DividerConfig {
            return DividerConfig(
                divider,
                disableByPositionArray,
                disableBySpanIndexArray,
                personaliseByPositionDividerArray,
                personaliseBySpanIndexDividerArray
            )
        }
    }
}