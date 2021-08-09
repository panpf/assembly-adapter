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

class StaggeredGridItemDecorateProviderImpl(
    private val dividerItemDecorate: ItemDecorate,
    private val firstDividerItemDecorate: ItemDecorate?,
    private val lastDividerItemDecorate: ItemDecorate?,
    private val sideItemDecorate: ItemDecorate?,
    private val firstSideItemDecorate: ItemDecorate?,
    private val lastSideItemDecorate: ItemDecorate?,
) : StaggeredGridItemDecorateProvider {

    override fun getItemDecorate(
        view: View,
        parent: RecyclerView,
        itemCount: Int,
        position: Int,
        spanCount: Int,
        isFullSpan: Boolean,
        spanIndex: Int,
        verticalOrientation: Boolean,
        decorateType: ItemDecorate.Type,
    ): ItemDecorate? {
        if (itemCount == 0) return null
//        val isFirstGroup = spanGroupIndex == 0
//        val isLastGroup = spanGroupIndex == spanGroupCount - 1
        val isFirstGroup = false
        val isLastGroup = false
        val isFirstSpan = isFullSpan || spanIndex == 0
        val isLastSpan = isFullSpan || spanIndex == spanCount - 1
        return if (verticalOrientation) {
            when (decorateType) {
                ItemDecorate.Type.START -> if (isFirstSpan) firstSideItemDecorate else sideItemDecorate
                ItemDecorate.Type.TOP -> if (isFirstGroup) firstDividerItemDecorate else null
                ItemDecorate.Type.END -> if (isLastSpan) lastSideItemDecorate else sideItemDecorate
                ItemDecorate.Type.BOTTOM -> if (isLastGroup) lastDividerItemDecorate else dividerItemDecorate
            }
        } else {
            when (decorateType) {
                ItemDecorate.Type.START -> if (isFirstGroup) firstDividerItemDecorate else null
                ItemDecorate.Type.TOP -> if (isFirstSpan) firstSideItemDecorate else sideItemDecorate
                ItemDecorate.Type.END -> if (isLastGroup) lastDividerItemDecorate else dividerItemDecorate
                ItemDecorate.Type.BOTTOM -> if (isLastSpan) lastSideItemDecorate else sideItemDecorate
            }
        }
    }
}