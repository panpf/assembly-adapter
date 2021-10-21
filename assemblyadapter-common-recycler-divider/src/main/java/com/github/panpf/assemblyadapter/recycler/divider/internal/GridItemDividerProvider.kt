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

class GridItemDividerProvider constructor(
    val dividerConfig: ItemDividerConfig?,
    val headerDividerConfig: ItemDividerConfig?,
    val footerDividerConfig: ItemDividerConfig?,
    val sideDividerConfig: ItemDividerConfig?,
    val sideHeaderDividerConfig: ItemDividerConfig?,
    val sideFooterDividerConfig: ItemDividerConfig?,
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
        fromOffset: Boolean
    ): ItemDivider? {
        val finalDividerType = if (isVerticalOrientation) {
            dividerType
        } else {
            when (dividerType) {
                ItemDivider.Type.START -> ItemDivider.Type.TOP
                ItemDivider.Type.END -> ItemDivider.Type.BOTTOM
                ItemDivider.Type.TOP -> ItemDivider.Type.START
                ItemDivider.Type.BOTTOM -> ItemDivider.Type.END
            }
        }
        val dividerConfig = if (isOnlySideHeader) {
            when (finalDividerType) {
                ItemDivider.Type.START -> if (isFirstSpan) sideHeaderDividerConfig else sideDividerConfig
                ItemDivider.Type.END -> null
                ItemDivider.Type.TOP -> if (isFirstGroup) headerDividerConfig else null
                ItemDivider.Type.BOTTOM -> if (isLastGroup) footerDividerConfig else dividerConfig
            }
        } else if (isNoSideHeaderAndFooter && fromOffset) {
            when (finalDividerType) {
                ItemDivider.Type.START -> if (isFirstSpan) null else sideDividerConfig
                ItemDivider.Type.END -> if (isLastSpan) null else sideDividerConfig
                ItemDivider.Type.TOP -> if (isFirstGroup) headerDividerConfig else null
                ItemDivider.Type.BOTTOM -> if (isLastGroup) footerDividerConfig else dividerConfig
            }
        } else {
            when (finalDividerType) {
                ItemDivider.Type.START -> if (isFirstSpan) sideHeaderDividerConfig else null
                ItemDivider.Type.END -> if (isLastSpan) sideFooterDividerConfig else sideDividerConfig
                ItemDivider.Type.TOP -> if (isFirstGroup) headerDividerConfig else null
                ItemDivider.Type.BOTTOM -> if (isLastGroup) footerDividerConfig else dividerConfig
            }
        }
        return dividerConfig?.get(parent, position, spanIndex)
    }

    val isHaveSide: Boolean =
        sideDividerConfig != null

    val isNoSide: Boolean =
        sideDividerConfig == null

    val isOnlySideHeader: Boolean =
        sideHeaderDividerConfig != null && sideFooterDividerConfig == null

    val isOnlySideFooter: Boolean =
        sideHeaderDividerConfig != null && sideFooterDividerConfig == null

    val isHaveSideHeaderAndFooter: Boolean =
        sideHeaderDividerConfig != null && sideFooterDividerConfig != null

    val isNoSideHeaderAndFooter: Boolean =
        sideHeaderDividerConfig == null && sideFooterDividerConfig == null
}