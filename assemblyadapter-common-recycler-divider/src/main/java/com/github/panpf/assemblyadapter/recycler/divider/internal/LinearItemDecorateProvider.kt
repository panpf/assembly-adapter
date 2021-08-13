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

class LinearItemDecorateProvider(
    private val dividerItemDecorateHolder: ItemDecorateHolder,
    private val firstDividerItemDecorateHolder: ItemDecorateHolder?,
    private val lastDividerItemDecorateHolder: ItemDecorateHolder?,
    private val startSideItemDecorateHolder: ItemDecorateHolder?,
    private val endSideItemDecorateHolder: ItemDecorateHolder?,
) {

    fun getItemDecorate(
        view: View,
        parent: RecyclerView,
        itemCount: Int,
        position: Int,
        verticalOrientation: Boolean,
        decorateType: ItemDecorate.Type,
    ): ItemDecorate? {
        if (itemCount == 0) return null
        val isFirst = position == 0
        val isLast = position == itemCount - 1
        return if (verticalOrientation) {
            when (decorateType) {
                ItemDecorate.Type.START -> startSideItemDecorateHolder
                ItemDecorate.Type.TOP -> if (isFirst) firstDividerItemDecorateHolder else null
                ItemDecorate.Type.END -> endSideItemDecorateHolder
                ItemDecorate.Type.BOTTOM -> if (isLast) lastDividerItemDecorateHolder else dividerItemDecorateHolder
            }
        } else {
            when (decorateType) {
                ItemDecorate.Type.START -> if (isFirst) firstDividerItemDecorateHolder else null
                ItemDecorate.Type.TOP -> startSideItemDecorateHolder
                ItemDecorate.Type.END -> if (isLast) lastDividerItemDecorateHolder else dividerItemDecorateHolder
                ItemDecorate.Type.BOTTOM -> endSideItemDecorateHolder
            }
        }?.get(parent, position, 0)
    }
}