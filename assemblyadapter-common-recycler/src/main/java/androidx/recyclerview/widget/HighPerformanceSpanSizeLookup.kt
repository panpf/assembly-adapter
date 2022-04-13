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
package androidx.recyclerview.widget

import android.util.SparseIntArray
import androidx.recyclerview.widget.GridLayoutManager.SpanSizeLookup

class HighPerformanceSpanSizeLookup(private val wrapper: SpanSizeLookup) : SpanSizeLookup() {

    val mSpanSizeCache = SparseIntArray()

    init {
        isSpanGroupIndexCacheEnabled = true
        isSpanIndexCacheEnabled = true
    }

    override fun getSpanSize(position: Int): Int {
        var spanSize = mSpanSizeCache.get(position, -1)
        if (spanSize == -1) {
            spanSize = wrapper.getSpanSize(position)
            mSpanSizeCache.put(position, spanSize)
        }
        return spanSize
    }

    override fun getSpanGroupIndex(adapterPosition: Int, spanCount: Int): Int {
        var spanGroupIndex = mSpanGroupIndexCache.get(adapterPosition, -1)
        if (spanGroupIndex == -1) {
            spanGroupIndex = super.getSpanGroupIndex(adapterPosition, spanCount)
            mSpanGroupIndexCache.put(adapterPosition, spanGroupIndex)
        }
        return spanGroupIndex
    }

    override fun getSpanIndex(position: Int, spanCount: Int): Int {
        var spanIndex = mSpanIndexCache.get(position, -1)
        if (spanIndex == -1) {
            spanIndex = super.getSpanIndex(position, spanCount)
            mSpanIndexCache.put(position, spanIndex)
        }
        return spanIndex
    }

    override fun invalidateSpanGroupIndexCache() {
        super.invalidateSpanGroupIndexCache()
        mSpanSizeCache.clear()
    }
}