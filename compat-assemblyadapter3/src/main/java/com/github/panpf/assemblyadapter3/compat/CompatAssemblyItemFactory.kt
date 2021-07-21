package com.github.panpf.assemblyadapter3.compat

import android.view.ViewGroup
import androidx.annotation.IdRes
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.github.panpf.assemblyadapter3.compat.internal.CompatClickListenerManager

abstract class CompatAssemblyItemFactory<DATA> {
    var adapter: CompatAssemblyAdapter? = null
        private set
    var spanSize = 1
        private set
    private var clickListenerManager: CompatClickListenerManager<DATA>? = null

    abstract fun match(data: Any?): Boolean

    fun dispatchCreateItem(parent: ViewGroup): CompatAssemblyItem<DATA> {
        val item = createAssemblyItem(parent)
        if (spanSize < 0) {
            val layoutParams = item.itemView.layoutParams
            if (layoutParams is StaggeredGridLayoutManager.LayoutParams) {
                layoutParams.isFullSpan = true
                item.itemView.layoutParams = layoutParams
            }
        }
        item.onInit(parent.context)
        clickListenerManager?.register(this, item, item.itemView)
        return item
    }

    abstract fun createAssemblyItem(parent: ViewGroup): CompatAssemblyItem<DATA>

    open fun attachToAdapter(adapter: CompatAssemblyAdapter) {
        this.adapter = adapter
    }

    open fun setSpanSize(spanSize: Int): CompatAssemblyItemFactory<DATA> {
        this.spanSize = spanSize
        return this
    }

    open fun fullSpan(recyclerView: RecyclerView): CompatAssemblyItemFactory<DATA> {
        this.spanSize = -1
        return this
    }

    open fun fullSpan(): CompatAssemblyItemFactory<DATA> {
        this.spanSize = -1
        return this
    }

    open fun setOnViewClickListener(
        @IdRes viewId: Int,
        onClickListener: CompatOnClickListener<DATA>
    ): CompatAssemblyItemFactory<DATA> {
        if (clickListenerManager == null) {
            clickListenerManager = CompatClickListenerManager()
        }
        clickListenerManager!!.add(viewId, onClickListener)
        return this
    }

    open fun setOnItemClickListener(onClickListener: CompatOnClickListener<DATA>): CompatAssemblyItemFactory<DATA> {
        if (clickListenerManager == null) {
            clickListenerManager = CompatClickListenerManager()
        }
        clickListenerManager!!.add(onClickListener)
        return this
    }

    open fun setOnViewLongClickListener(
        @IdRes viewId: Int,
        onClickListener: CompatOnLongClickListener<DATA>
    ): CompatAssemblyItemFactory<DATA> {
        if (clickListenerManager == null) {
            clickListenerManager = CompatClickListenerManager()
        }
        clickListenerManager!!.add(viewId, onClickListener)
        return this
    }

    open fun setOnItemLongClickListener(onClickListener: CompatOnLongClickListener<DATA>): CompatAssemblyItemFactory<DATA> {
        if (clickListenerManager == null) {
            clickListenerManager = CompatClickListenerManager()
        }
        clickListenerManager!!.add(onClickListener)
        return this
    }
}