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
    private val headerDividerConfig: ItemDividerConfig?,
    private val footerDividerConfig: ItemDividerConfig?,
    private val headerSideDividerConfig: ItemDividerConfig?,
    private val footerSideDividerConfig: ItemDividerConfig?,
) {

    fun getItemDivider(
        view: View,
        parent: RecyclerView,
        itemCount: Int,
        position: Int,
        isFirst: Boolean,
        isLast: Boolean,
        isVerticalOrientation: Boolean,
        dividerType: ItemDivider.Type,
    ): ItemDivider? {
        return if (isVerticalOrientation) {
            when (dividerType) {
                ItemDivider.Type.START -> headerSideDividerConfig
                ItemDivider.Type.TOP -> if (isFirst) headerDividerConfig else null
                ItemDivider.Type.END -> footerSideDividerConfig
                ItemDivider.Type.BOTTOM -> if (isLast) footerDividerConfig else dividerConfig
            }
        } else {
            when (dividerType) {
                ItemDivider.Type.START -> if (isFirst) headerDividerConfig else null
                ItemDivider.Type.TOP -> headerSideDividerConfig
                ItemDivider.Type.END -> if (isLast) footerDividerConfig else dividerConfig
                ItemDivider.Type.BOTTOM -> footerSideDividerConfig
            }
        }?.get(parent, position, 0)
    }
}