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

class LinearItemDividerProvider(
    private val dividerConfig: ItemDividerConfig,
    private val firstDividerConfig: ItemDividerConfig?,
    private val lastDividerConfig: ItemDividerConfig?,
    private val firstSideDividerConfig: ItemDividerConfig?,
    private val lastSideDividerConfig: ItemDividerConfig?,
) {

    fun getItemDivider(
        view: View,
        parent: RecyclerView,
        itemCount: Int,
        position: Int,
        verticalOrientation: Boolean,
        dividerType: ItemDivider.Type,
    ): ItemDivider? {
        if (itemCount == 0) return null
        val isFirst = position == 0
        val isLast = position == itemCount - 1
        return if (verticalOrientation) {
            when (dividerType) {
                ItemDivider.Type.START -> firstSideDividerConfig
                ItemDivider.Type.TOP -> if (isFirst) firstDividerConfig else null
                ItemDivider.Type.END -> lastSideDividerConfig
                ItemDivider.Type.BOTTOM -> if (isLast) lastDividerConfig else dividerConfig
            }
        } else {
            when (dividerType) {
                ItemDivider.Type.START -> if (isFirst) firstDividerConfig else null
                ItemDivider.Type.TOP -> firstSideDividerConfig
                ItemDivider.Type.END -> if (isLast) lastDividerConfig else dividerConfig
                ItemDivider.Type.BOTTOM -> lastSideDividerConfig
            }
        }?.get(parent, position, 0)
    }
}