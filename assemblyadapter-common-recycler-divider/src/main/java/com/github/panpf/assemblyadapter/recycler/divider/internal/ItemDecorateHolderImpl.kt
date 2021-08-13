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

import androidx.collection.SparseArrayCompat
import androidx.recyclerview.widget.RecyclerView

open class ItemDecorateHolderImpl(
    private val itemDecorate: ItemDecorate,
    private val disableByPositionArray: SparseArrayCompat<Boolean>?,
    private val disableBySpanIndexArray: SparseArrayCompat<Boolean>?,
    private val personaliseByPositionArray: SparseArrayCompat<ItemDecorate>?,
    private val personaliseBySpanIndexArray: SparseArrayCompat<ItemDecorate>?,
) : ItemDecorateHolder {

    override fun get(parent: RecyclerView, position: Int, spanIndex: Int): ItemDecorate? {
        if (disableBySpanIndexArray?.get(spanIndex, false) == true) {
            return null
        }
        if (disableByPositionArray?.get(position, false) == true) {
            return null
        }

        val personaliseBySpanIndexItemDecorate = personaliseBySpanIndexArray?.get(position)
        if (personaliseBySpanIndexItemDecorate != null) {
            return personaliseBySpanIndexItemDecorate
        }

        val personaliseByPositionItemDecorate = personaliseByPositionArray?.get(position)
        if (personaliseByPositionItemDecorate != null) {
            return personaliseByPositionItemDecorate
        }

        return itemDecorate
    }
}