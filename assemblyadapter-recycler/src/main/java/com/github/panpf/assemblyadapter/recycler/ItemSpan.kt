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

/**
 * Used to set spanSize for ItemFactory in AssemblyGridLayoutManager and AssemblyStaggeredGridLayoutManager
 */
data class ItemSpan(
    /**
     * Less than 0 means fullSpan, AssemblyGridLayoutManager will use spanCount as spanSize; AssemblyStaggeredGridLayoutManager will set isFullSpan to true
     */
    val size: Int
) {

    fun isFullSpan(): Boolean = size < 0

    companion object {

        @JvmStatic
        private val FULL_SPAN = ItemSpan(-1)

        @JvmStatic
        fun fullSpan(): ItemSpan {
            return FULL_SPAN
        }

        @JvmStatic
        fun span(span: Int): ItemSpan {
            return ItemSpan(span)
        }
    }
}