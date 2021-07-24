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
package com.github.panpf.assemblyadapter.recycler

import androidx.recyclerview.widget.RecyclerView

class ConcatAdapterLocalHelper {

    private val concatAdapterWrapperAdaptersCache = ConcatAdapterWrapperAdaptersCache()

    fun findLocalAdapterAndPosition(
        adapter: RecyclerView.Adapter<*>, position: Int
    ): Pair<RecyclerView.Adapter<*>, Int> {
        val adaptersCache = concatAdapterWrapperAdaptersCache.getAdapterCache(adapter)
        var nextAdapter = adaptersCache
        var nextPosition = position
        while (nextAdapter is ConcatAdapterWrapperAdaptersCache.ConcatAdapterCache) {
            val wrapperAdapters = nextAdapter.adapters
            var childAdapterStartPosition = 0
            val childAdapter = wrapperAdapters.find { childAdapter ->
                val childAdapterEndPosition =
                    childAdapterStartPosition + childAdapter.itemCount - 1
                @Suppress("ConvertTwoComparisonsToRangeCheck")
                if (position >= childAdapterStartPosition && position <= childAdapterEndPosition) {
                    true
                } else {
                    childAdapterStartPosition = childAdapterEndPosition + 1
                    false
                }
            }
            if (childAdapter != null) {
                nextAdapter = childAdapter
                nextPosition -= childAdapterStartPosition
            } else {
                throw IndexOutOfBoundsException("Index: $nextPosition, Size: ${nextAdapter.itemCount}")
            }
        }
        return nextAdapter.adapter to nextPosition
    }

    fun reset() {
        concatAdapterWrapperAdaptersCache.reset()
    }
}