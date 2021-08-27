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

class GridItemDividerProvider(
    val dividerConfig: ItemDividerConfig,
    val headerDividerConfig: ItemDividerConfig?,
    val footerDividerConfig: ItemDividerConfig?,
    val sideDividerConfig: ItemDividerConfig?,
    val headerSideDividerConfig: ItemDividerConfig?,
    val footerSideDividerConfig: ItemDividerConfig?,
) {

    fun getItemDivider(
        view: View,
        parent: RecyclerView,
        itemCount: Int,
        position: Int,
        spanCount: Int,
        spanSize: Int,
        spanIndex: Int,
        isFullSpan: Boolean,
        isFirstSpan: Boolean,
        isLastSpan: Boolean,
        spanGroupCount: Int,
        spanGroupIndex: Int,
        isFirstGroup: Boolean,
        isLastGroup: Boolean,
        isVerticalOrientation: Boolean,
        dividerType: ItemDivider.Type,
    ): ItemDivider? {
        return if (isVerticalOrientation) {
            when (dividerType) {
                ItemDivider.Type.START -> if (isFirstSpan) headerSideDividerConfig else null
                ItemDivider.Type.TOP -> if (isFirstGroup) headerDividerConfig else null
                ItemDivider.Type.END -> if (isLastSpan) footerSideDividerConfig else sideDividerConfig
                ItemDivider.Type.BOTTOM -> if (isLastGroup) footerDividerConfig else dividerConfig
            }
        } else {
            when (dividerType) {
                ItemDivider.Type.START -> if (isFirstGroup) headerDividerConfig else null
                ItemDivider.Type.TOP -> if (isFirstSpan) headerSideDividerConfig else null
                ItemDivider.Type.END -> if (isLastGroup) footerDividerConfig else dividerConfig
                ItemDivider.Type.BOTTOM -> if (isLastSpan) footerSideDividerConfig else sideDividerConfig
            }
        }?.get(parent, position, spanIndex)
    }
}