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

import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.RecyclerView

class ConcatAdapterWrapperAdaptersCache {

    private var mainAdapter: RecyclerView.Adapter<*>? = null
    private var adapterCache: AdaptersCache? = null

    private val anyAdapterDataObserver = AnyAdapterDataObserver {
        reset()
    }

    fun getAdapterCache(adapter: RecyclerView.Adapter<*>): AdaptersCache {
        val oldMainAdapter = mainAdapter
        val adapterCache = adapterCache
        return if (adapterCache == null || oldMainAdapter == null || oldMainAdapter !== adapter) {
            reset()
            mainAdapter = adapter.apply {
                try {
                    registerAdapterDataObserver(anyAdapterDataObserver)
                } catch (e: Exception) {
                }
            }
            AdaptersCache.buildCache(adapter).apply {
                this@ConcatAdapterWrapperAdaptersCache.adapterCache = this
            }
        } else {
            adapterCache
        }
    }

    fun reset() {
        try {
            mainAdapter?.unregisterAdapterDataObserver(anyAdapterDataObserver)
        } catch (e: Exception) {
        }
        adapterCache = null
    }

    sealed interface AdaptersCache {

        val adapter: RecyclerView.Adapter<*>
        val itemCount: Int

        companion object {
            fun buildCache(adapter: RecyclerView.Adapter<*>): AdaptersCache {
                return if (adapter is ConcatAdapter) {
                    ConcatAdapterCache(adapter, adapter.adapters.map {
                        buildCache(it)
                    })
                } else {
                    NormalAdapterCache(adapter)
                }
            }
        }
    }

    class ConcatAdapterCache(
        override val adapter: RecyclerView.Adapter<*>,
        val adapters: List<AdaptersCache>
    ) : AdaptersCache {
        override val itemCount: Int
            get() = adapter.itemCount
    }

    class NormalAdapterCache(override val adapter: RecyclerView.Adapter<*>) :
        AdaptersCache {
        override val itemCount: Int
            get() = adapter.itemCount
    }

    private class AnyAdapterDataObserver(val onAnyChanged: () -> Unit) :
        RecyclerView.AdapterDataObserver() {
        override fun onChanged() {
            super.onChanged()
            onAnyChanged()
        }

        override fun onItemRangeChanged(positionStart: Int, itemCount: Int) {
            super.onItemRangeChanged(positionStart, itemCount)
            onAnyChanged()
        }

        override fun onItemRangeChanged(positionStart: Int, itemCount: Int, payload: Any?) {
            super.onItemRangeChanged(positionStart, itemCount, payload)
            onAnyChanged()
        }

        override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
            super.onItemRangeInserted(positionStart, itemCount)
            onAnyChanged()
        }

        override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {
            super.onItemRangeRemoved(positionStart, itemCount)
            onAnyChanged()
        }

        override fun onItemRangeMoved(fromPosition: Int, toPosition: Int, itemCount: Int) {
            super.onItemRangeMoved(fromPosition, toPosition, itemCount)
            onAnyChanged()
        }
    }
}