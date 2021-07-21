/*
 * Copyright (C) 2017 Peng fei Pan <sky@panpf.me>
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
package com.github.panpf.assemblyadapter3.compat

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

abstract class CompatAssemblyMoreItemFactory(private var listener: CompatOnLoadMoreListener?) :
    CompatAssemblyItemFactory<CompatMoreState>() {

    internal var onLoadCallback: OnLoadCallback? = null

    override fun match(data: Any?): Boolean {
        return data is CompatMoreState
    }

    abstract override fun createAssemblyItem(parent: ViewGroup): CompatAssemblyMoreItem

    internal fun startLoad() {
        val adapter = adapter ?: return
        onLoadCallback?.onStartLoad()
        listener?.onLoadMore(adapter)
    }

    override fun setSpanSize(spanSize: Int): CompatAssemblyMoreItemFactory {
        super.setSpanSize(spanSize)
        return this
    }

    override fun fullSpan(recyclerView: RecyclerView): CompatAssemblyMoreItemFactory {
        super.fullSpan(recyclerView)
        return this
    }

    override fun fullSpan(): CompatAssemblyMoreItemFactory {
        super.fullSpan()
        return this
    }

    override fun setOnViewClickListener(
        viewId: Int,
        onClickListener: CompatOnClickListener<CompatMoreState>
    ): CompatAssemblyMoreItemFactory {
        super.setOnViewClickListener(viewId, onClickListener)
        return this
    }

    override fun setOnItemClickListener(onClickListener: CompatOnClickListener<CompatMoreState>): CompatAssemblyMoreItemFactory {
        super.setOnItemClickListener(onClickListener)
        return this
    }

    override fun setOnViewLongClickListener(
        viewId: Int,
        onClickListener: CompatOnLongClickListener<CompatMoreState>
    ): CompatAssemblyMoreItemFactory {
        super.setOnViewLongClickListener(viewId, onClickListener)
        return this
    }

    override fun setOnItemLongClickListener(onClickListener: CompatOnLongClickListener<CompatMoreState>): CompatAssemblyMoreItemFactory {
        super.setOnItemLongClickListener(onClickListener)
        return this
    }

    fun interface OnLoadCallback {
        fun onStartLoad()
    }
}