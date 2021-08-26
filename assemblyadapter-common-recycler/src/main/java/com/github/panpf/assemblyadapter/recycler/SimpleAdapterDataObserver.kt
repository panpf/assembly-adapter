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

class SimpleAdapterDataObserver(private val onDataChanged: ((type: Type) -> Unit)? = null) :
    RecyclerView.AdapterDataObserver() {

    override fun onChanged() {
        super.onChanged()
        onDataChanged?.invoke(Changed)
    }

    override fun onItemRangeChanged(positionStart: Int, itemCount: Int) {
        super.onItemRangeChanged(positionStart, itemCount)
        onDataChanged?.invoke(RangeChanged(positionStart, itemCount, null))
    }

    override fun onItemRangeChanged(positionStart: Int, itemCount: Int, payload: Any?) {
        super.onItemRangeChanged(positionStart, itemCount, payload)
        onDataChanged?.invoke(RangeChanged(positionStart, itemCount, payload))
    }

    override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
        super.onItemRangeInserted(positionStart, itemCount)
        onDataChanged?.invoke(RangeInserted(positionStart, itemCount))
    }

    override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {
        super.onItemRangeRemoved(positionStart, itemCount)
        onDataChanged?.invoke(RangeRemoved(positionStart, itemCount))
    }

    override fun onItemRangeMoved(fromPosition: Int, toPosition: Int, itemCount: Int) {
        super.onItemRangeMoved(fromPosition, toPosition, itemCount)
        onDataChanged?.invoke(RangeMoved(fromPosition, toPosition, itemCount))
    }

    sealed interface Type

    object Changed : Type
    data class RangeChanged(val positionStart: Int, val itemCount: Int, val payload: Any?) : Type
    data class RangeInserted(val positionStart: Int, val itemCount: Int) : Type
    data class RangeRemoved(val positionStart: Int, val itemCount: Int) : Type
    data class RangeMoved(val fromPosition: Int, val toPosition: Int, val itemCount: Int) : Type
}