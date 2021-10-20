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

class StaggeredGridItemDividerProvider constructor(
    val dividerConfig: ItemDividerConfig,
    val headerDividerConfig: ItemDividerConfig?,
    val footerDividerConfig: ItemDividerConfig?,
    val sideDividerConfig: ItemDividerConfig?,
    val sideHeaderAndFooterDividerConfig: ItemDividerConfig?,
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
        isVerticalOrientation: Boolean,
        dividerType: ItemDivider.Type,
        fromDraw: Boolean
    ): ItemDivider? {
        // 由于在 StaggeredGridLayoutManager 中相邻两个 item 的高度可能不一样，
        // 所以需要在绘制时让 item 的 start 和 end 都绘制 divider，这样相邻的且高度不一致的两个 item 中间始终会绘制一条较高的 divider
        return if (isVerticalOrientation) {
            when (dividerType) {
                ItemDivider.Type.START -> if (isFirstSpan) sideHeaderAndFooterDividerConfig else (if (fromDraw) sideDividerConfig else null)
                ItemDivider.Type.TOP -> if (isColumnFirst) headerDividerConfig else null
                ItemDivider.Type.END -> if (isLastSpan) sideHeaderAndFooterDividerConfig else sideDividerConfig
                ItemDivider.Type.BOTTOM -> if (isColumnEnd) footerDividerConfig else dividerConfig
            }
        } else {
            when (dividerType) {
                ItemDivider.Type.START -> if (isColumnFirst) headerDividerConfig else null
                ItemDivider.Type.TOP -> if (isFirstSpan) sideHeaderAndFooterDividerConfig else (if (fromDraw) sideDividerConfig else null)
                ItemDivider.Type.END -> if (isColumnEnd) footerDividerConfig else dividerConfig
                ItemDivider.Type.BOTTOM -> if (isLastSpan) sideHeaderAndFooterDividerConfig else sideDividerConfig
            }
        }?.get(parent, position, spanIndex)
    }

    fun hasHeaderOrFooterDivider(): Boolean {
        return headerDividerConfig != null || footerDividerConfig != null
    }
}