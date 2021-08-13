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

class StaggeredGridItemDecorateProvider(
    private val dividerItemDecorate: ItemDecorateHolder,
    private val firstDividerItemDecorate: ItemDecorateHolder?,
    private val lastDividerItemDecorate: ItemDecorateHolder?,
    private val sideItemDecorate: ItemDecorateHolder?,
    private val firstSideItemDecorate: ItemDecorateHolder?,
    private val lastSideItemDecorate: ItemDecorateHolder?,
) {

    fun getItemDecorate(
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
        decorateType: ItemDecorate.Type,
    ): ItemDecorate? {
        return if (vertical) {
            when (decorateType) {
                ItemDecorate.Type.START -> if (isFirstSpan) firstSideItemDecorate else sideItemDecorate
                ItemDecorate.Type.TOP -> if (isColumnFirst) firstDividerItemDecorate else null
                ItemDecorate.Type.END -> if (isLastSpan) lastSideItemDecorate else sideItemDecorate
                ItemDecorate.Type.BOTTOM -> if (isColumnEnd) lastDividerItemDecorate else dividerItemDecorate
            }
        } else {
            when (decorateType) {
                ItemDecorate.Type.START -> if (isColumnFirst) firstDividerItemDecorate else null
                ItemDecorate.Type.TOP -> if (isFirstSpan) firstSideItemDecorate else sideItemDecorate
                ItemDecorate.Type.END -> if (isColumnEnd) lastDividerItemDecorate else dividerItemDecorate
                ItemDecorate.Type.BOTTOM -> if (isLastSpan) lastSideItemDecorate else sideItemDecorate
            }
        }?.get(
            view, parent, itemCount, position, spanCount, spanIndex, isFullSpan, isFirstSpan,
            isLastSpan, isColumnFirst, isColumnEnd, vertical, decorateType
        )
    }

    fun hasFirstOrLastDivider(): Boolean {
        return firstDividerItemDecorate != null || lastDividerItemDecorate != null
    }
}