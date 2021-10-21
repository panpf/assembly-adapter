/*
 * Copyright (c) 2020 Giorgio Antonioli
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.github.panpf.assemblyadapter.recycler.divider.internal

import androidx.annotation.Px
import kotlin.math.ceil
import kotlin.math.floor

/**
 * From https://github.com/fondesa/recycler-view-divider
 */

@Px
internal fun normalizedOffsetFromSize(
    dividerSide: ItemDivider.Type,
    @Px sideDividerSize: Int,
    spanCount: Int,
    columnIndex: Int,
    areSideDividersVisible: Boolean
): Int {
    val multiplier = sideDividerSize.toFloat() / spanCount
    return when (dividerSide) {
        ItemDivider.Type.START, ItemDivider.Type.TOP -> {
            floor(multiplier * (spanCount - columnIndex)).toInt()
        }
        ItemDivider.Type.END, ItemDivider.Type.BOTTOM -> {
            val cellSideOffset = multiplier * (spanCount + 1)
            ceil((cellSideOffset) - (multiplier * (spanCount - columnIndex))).toInt()
        }
    }
}