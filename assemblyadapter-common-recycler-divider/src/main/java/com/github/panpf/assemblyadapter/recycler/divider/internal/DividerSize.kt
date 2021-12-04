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

/**
 * Define the size of the Divider
 */
interface DividerSize {
    fun getWidth(isStartOrEnd: Boolean): Int
    fun getHeight(isStartOrEnd: Boolean): Int

    companion object {
        fun clearly(width: Int, height: Int): DividerSize = ClearlyDividerSize(width, height)
        fun vague(size: Int): DividerSize = VagueDividerSize(size)
    }
}

/**
 * Clear divider size, the size of the divider is always determined by width and height, whether it is a horizontal or vertical layout manager
 */
data class ClearlyDividerSize(private val width: Int, private val height: Int) : DividerSize {

    override fun getWidth(isStartOrEnd: Boolean): Int = width

    override fun getHeight(isStartOrEnd: Boolean): Int = height
}

/**
 * Fuzzy divider size. When the divider is at the start or end side, size represents the width of the divider. At this time, the height of the divider defaults to the height of the full item; the opposite is the case when the divider is at the top or bottom side.
 */
data class VagueDividerSize(private val size: Int) : DividerSize {
    override fun getWidth(isStartOrEnd: Boolean): Int = if (isStartOrEnd) size else -1

    override fun getHeight(isStartOrEnd: Boolean): Int = if (isStartOrEnd) -1 else size
}