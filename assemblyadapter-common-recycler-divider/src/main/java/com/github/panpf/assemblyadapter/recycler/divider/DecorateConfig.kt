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
import com.github.panpf.assemblyadapter.recycler.divider.internal.ItemDecorateHolder
import com.github.panpf.assemblyadapter.recycler.divider.internal.ItemDecorateHolderImpl

class DecorateConfig(
    private val decorate: Decorate,
    private val disableByPositionArray: SparseArrayCompat<Boolean>?,
    private val disableBySpanIndexArray: SparseArrayCompat<Boolean>?,
    private val personaliseByPositionArray: SparseArrayCompat<Decorate>?,
    private val personaliseBySpanIndexArray: SparseArrayCompat<Decorate>?,
) {

    fun toItemDecorateHolder(context: Context): ItemDecorateHolder {
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
        return ItemDecorateHolderImpl(
            decorate.createItemDecorate(context),
            disableByPositionArray,
            disableBySpanIndexArray,
            personaliseByPositionItemDecorateArray,
            personaliseBySpanIndexItemDecorateArray,
        )
    }

    class Builder(val decorate: Decorate) {
        private var disableByPositionArray: SparseArrayCompat<Boolean>? = null
        private var disableBySpanIndexArray: SparseArrayCompat<Boolean>? = null
        private var personaliseByPositionDecorateArray: SparseArrayCompat<Decorate>? = null
        private var personaliseBySpanIndexDecorateArray: SparseArrayCompat<Decorate>? = null

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

        fun personaliseByPosition(position: Int, decorate: Decorate): Builder {
            (personaliseByPositionDecorateArray ?: SparseArrayCompat<Decorate>().apply {
                this@Builder.personaliseByPositionDecorateArray = this
            }).put(position, decorate)
            return this
        }

        fun personaliseBySpanIndex(spanIndex: Int, decorate: Decorate): Builder {
            (personaliseBySpanIndexDecorateArray ?: SparseArrayCompat<Decorate>().apply {
                this@Builder.personaliseBySpanIndexDecorateArray = this
            }).put(spanIndex, decorate)
            return this
        }

        fun build(): DecorateConfig {
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