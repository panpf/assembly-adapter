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
import com.github.panpf.assemblyadapter.recycler.divider.internal.ItemDecorate
import com.github.panpf.assemblyadapter.recycler.divider.internal.ItemDecorateConfig

open class DecorateConfig(
    protected val decorate: Decorate,
    protected val disableByPositionArray: SparseArrayCompat<Boolean>?,
    protected val disableBySpanIndexArray: SparseArrayCompat<Boolean>?,
    protected val personaliseByPositionArray: SparseArrayCompat<Decorate>?,
    protected val personaliseBySpanIndexArray: SparseArrayCompat<Decorate>?,
) {

    open fun toItemDecorateHolder(context: Context): ItemDecorateConfig {
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
        return ItemDecorateConfig(
            decorate.createItemDecorate(context),
            disableByPositionArray,
            disableBySpanIndexArray,
            personaliseByPositionItemDecorateArray,
            personaliseBySpanIndexItemDecorateArray,
        )
    }

    open class Builder(val decorate: Decorate) {
        private var disableByPositionArray: SparseArrayCompat<Boolean>? = null
        private var disableBySpanIndexArray: SparseArrayCompat<Boolean>? = null
        private var personaliseByPositionDecorateArray: SparseArrayCompat<Decorate>? = null
        private var personaliseBySpanIndexDecorateArray: SparseArrayCompat<Decorate>? = null

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

        open fun personaliseByPosition(position: Int, decorate: Decorate): Builder {
            (personaliseByPositionDecorateArray ?: SparseArrayCompat<Decorate>().apply {
                this@Builder.personaliseByPositionDecorateArray = this
            }).put(position, decorate)
            return this
        }

        open fun personaliseBySpanIndex(spanIndex: Int, decorate: Decorate): Builder {
            (personaliseBySpanIndexDecorateArray ?: SparseArrayCompat<Decorate>().apply {
                this@Builder.personaliseBySpanIndexDecorateArray = this
            }).put(spanIndex, decorate)
            return this
        }

        open fun build(): DecorateConfig {
            return DecorateConfig(
                decorate,
                disableByPositionArray,
                disableBySpanIndexArray,
                personaliseByPositionDecorateArray,
                personaliseBySpanIndexDecorateArray
            )
        }
    }
}