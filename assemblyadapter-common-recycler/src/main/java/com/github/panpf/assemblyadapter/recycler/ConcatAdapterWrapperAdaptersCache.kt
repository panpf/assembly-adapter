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
    private var adapterCache: AdapterCache? = null

    private val simpleAdapterDataObserver = SimpleAdapterDataObserver {
        reset()
    }

    fun getAdapterCache(adapter: RecyclerView.Adapter<*>): AdapterCache {
        val oldMainAdapter = mainAdapter
        val oldAdapterCache = adapterCache
        return if (oldAdapterCache == null || oldMainAdapter == null || oldMainAdapter !== adapter) {
            reset()
            try {
                adapter.registerAdapterDataObserver(simpleAdapterDataObserver)
            } catch (e: Exception) {
            }
            mainAdapter = adapter
            AdapterCache.buildCache(adapter).apply {
                this@ConcatAdapterWrapperAdaptersCache.adapterCache = this
            }
        } else {
            oldAdapterCache
        }
    }

    fun reset() {
        try {
            mainAdapter?.unregisterAdapterDataObserver(simpleAdapterDataObserver)
        } catch (e: Exception) {
        }
        adapterCache = null
    }

    sealed interface AdapterCache {

        val adapter: RecyclerView.Adapter<*>
        val itemCount: Int

        companion object {
            fun buildCache(adapter: RecyclerView.Adapter<*>): AdapterCache {
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
        val childAdapterCaches: List<AdapterCache>
    ) : AdapterCache {
        override val itemCount: Int
            get() = adapter.itemCount
    }

    class NormalAdapterCache(override val adapter: RecyclerView.Adapter<*>) :
        AdapterCache {
        override val itemCount: Int
            get() = adapter.itemCount
    }
}