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
import androidx.recyclerview.widget.RecyclerView

class StaggeredGridItemDividerProvider(
    private val dividerConfig: ItemDividerConfig,
    private val firstDividerConfig: ItemDividerConfig?,
    private val lastDividerConfig: ItemDividerConfig?,
    private val sideDividerConfig: ItemDividerConfig?,
    private val firstSideDividerConfig: ItemDividerConfig?,
    private val lastSideDividerConfig: ItemDividerConfig?,
) {

    fun getItemDivider(
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
        dividerType: ItemDivider.Type,
    ): ItemDivider? {
        return if (vertical) {
            when (dividerType) {
                ItemDivider.Type.START -> if (isFirstSpan) firstSideDividerConfig else sideDividerConfig
                ItemDivider.Type.TOP -> if (isColumnFirst) firstDividerConfig else null
                ItemDivider.Type.END -> if (isLastSpan) lastSideDividerConfig else sideDividerConfig
                ItemDivider.Type.BOTTOM -> if (isColumnEnd) lastDividerConfig else dividerConfig
            }
        } else {
            when (dividerType) {
                ItemDivider.Type.START -> if (isColumnFirst) firstDividerConfig else null
                ItemDivider.Type.TOP -> if (isFirstSpan) firstSideDividerConfig else sideDividerConfig
                ItemDivider.Type.END -> if (isColumnEnd) lastDividerConfig else dividerConfig
                ItemDivider.Type.BOTTOM -> if (isLastSpan) lastSideDividerConfig else sideDividerConfig
            }
        }?.get(parent, position, spanIndex)
    }

    fun hasFirstOrLastDivider(): Boolean {
        return firstDividerConfig != null || lastDividerConfig != null
    }
}