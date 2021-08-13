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

class GridItemDecorateProvider(
    private val dividerItemDecorateHolder: ItemDecorateHolder,
    private val firstDividerItemDecorateHolder: ItemDecorateHolder?,
    private val lastDividerItemDecorateHolder: ItemDecorateHolder?,
    private val sideItemDecorateHolder: ItemDecorateHolder?,
    private val firstSideItemDecorateHolder: ItemDecorateHolder?,
    private val lastSideItemDecorateHolder: ItemDecorateHolder?,
) {

    fun getItemDecorate(
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
        vertical: Boolean,
        decorateType: ItemDecorate.Type,
    ): ItemDecorate? {
        return if (vertical) {
            when (decorateType) {
                ItemDecorate.Type.START -> if (isFirstSpan) firstSideItemDecorateHolder else null
                ItemDecorate.Type.TOP -> if (isFirstGroup) firstDividerItemDecorateHolder else null
                ItemDecorate.Type.END -> if (isLastSpan) lastSideItemDecorateHolder else sideItemDecorateHolder
                ItemDecorate.Type.BOTTOM -> if (isLastGroup) lastDividerItemDecorateHolder else dividerItemDecorateHolder
            }
        } else {
            when (decorateType) {
                ItemDecorate.Type.START -> if (isFirstGroup) firstDividerItemDecorateHolder else null
                ItemDecorate.Type.TOP -> if (isFirstSpan) firstSideItemDecorateHolder else null
                ItemDecorate.Type.END -> if (isLastGroup) lastDividerItemDecorateHolder else dividerItemDecorateHolder
                ItemDecorate.Type.BOTTOM -> if (isLastSpan) lastSideItemDecorateHolder else sideItemDecorateHolder
            }
        }?.get(parent, position)
    }
}