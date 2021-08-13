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

import android.view.View
import androidx.collection.SparseArrayCompat
import androidx.recyclerview.widget.RecyclerView

open class ItemDecorateHolderImpl(
    private val itemDecorate: ItemDecorate,
    private val disableItemDecorateArray: SparseArrayCompat<Boolean>?,
    private val personaliseItemDecorateArray: SparseArrayCompat<ItemDecorate>?,
) : ItemDecorateHolder {

    override fun get(
        view: View,
        parent: RecyclerView,
        itemCount: Int,
        position: Int,
        spanCount: Int,
        spanIndex: Int,
        isFullSpan: Boolean,
        isFirstSpan: Boolean,
        isLastSpan: Boolean,
        isColumnFirst: Boolean,
        isColumnEnd: Boolean,
        vertical: Boolean,
        decorateType: ItemDecorate.Type
    ): ItemDecorate? {
        if (disableItemDecorateArray?.get(position, false) == true) {
            return null
        }

        val personaliseItemDecorate = personaliseItemDecorateArray?.get(position)
        if (personaliseItemDecorate != null) {
            return personaliseItemDecorate
        }

        return itemDecorate
    }
}