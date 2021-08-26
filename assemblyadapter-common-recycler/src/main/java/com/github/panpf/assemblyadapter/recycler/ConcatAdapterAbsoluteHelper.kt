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

class ConcatAdapterAbsoluteHelper {

    private val concatAdapterWrapperAdaptersCache = ConcatAdapterWrapperAdaptersCache()

    fun findAbsoluteAdapterPosition(
        parentAdapter: RecyclerView.Adapter<*>,
        localAdapter: RecyclerView.Adapter<*>,
        localPosition: Int
    ): Int {
        val localAdapterItemCount = localAdapter.itemCount
        if (localPosition < 0 || localPosition >= localAdapterItemCount) {
            throw IndexOutOfBoundsException("Index: $localPosition, Size: $localAdapterItemCount")
        }

        val parentAdapterCache = concatAdapterWrapperAdaptersCache.getAdapterCache(parentAdapter)
        return findAbsoluteAdapterPositionRecursive(
            parentAdapterCache, localAdapter, localPosition
        ) ?: throw IllegalArgumentException("localAdapter not found in parentAdapter")
    }

    private fun findAbsoluteAdapterPositionRecursive(
        parentAdapterCache: ConcatAdapterWrapperAdaptersCache.AdapterCache,
        localAdapter: RecyclerView.Adapter<*>,
        localPosition: Int
    ): Int? = when {
        localAdapter === parentAdapterCache.adapter -> {
            localPosition
        }
        parentAdapterCache is ConcatAdapterWrapperAdaptersCache.ConcatAdapterCache -> {
            var childAdapterStartPosition = 0
            parentAdapterCache.childAdapterCaches.forEach { childAdapterCache ->
                val childPosition = findAbsoluteAdapterPositionRecursive(
                    childAdapterCache, localAdapter, localPosition
                )
                if (childPosition != null) {
                    return childAdapterStartPosition + childPosition
                } else {
                    childAdapterStartPosition += childAdapterCache.itemCount
                }
            }
            null
        }
        else -> {
            null
        }
    }

    fun reset() {
        concatAdapterWrapperAdaptersCache.reset()
    }
}