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
import com.github.panpf.assemblyadapter.recycler.divider.internal.ItemDecorateHolderImpl
import com.github.panpf.assemblyadapter.recycler.divider.internal.ItemDecorate
import com.github.panpf.assemblyadapter.recycler.divider.internal.ItemDecorateHolder

class DecorateConfig(
    private val decorate: Decorate,
    private val disableDecorateArray: SparseArrayCompat<Boolean>?,
    private val personaliseDecorateArray: SparseArrayCompat<Decorate>?,
) {

    fun toItemDecorateHolder(context: Context): ItemDecorateHolder {
        val personaliseItemDecorateArray = personaliseDecorateArray?.let { oldArray ->
            SparseArrayCompat<ItemDecorate>().apply {
                0.until(oldArray.size()).forEach { index ->
                    put(oldArray.keyAt(index), oldArray.valueAt(index).createItemDecorate(context))
                }
            }
        }
        return ItemDecorateHolderImpl(
            decorate.createItemDecorate(context),
            disableDecorateArray,
            personaliseItemDecorateArray,
        )
    }

    class Builder(val decorate: Decorate) {
        private var disableDecorateArray: SparseArrayCompat<Boolean>? = null
        private var personaliseDecorateArray: SparseArrayCompat<Decorate>? = null

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

        fun build(): DecorateConfig {
            return DecorateConfig(decorate, disableDecorateArray, personaliseDecorateArray)
        }
    }
}