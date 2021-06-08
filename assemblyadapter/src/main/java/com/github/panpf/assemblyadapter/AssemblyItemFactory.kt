package com.github.panpf.assemblyadapter

import android.view.ViewGroup
import androidx.annotation.IdRes

abstract class AssemblyItemFactory<DATA> : ItemFactory<DATA> {

    private var clickListenerManager: ClickListenerManager<DATA>? = null

    override fun dispatchCreateItem(parent: ViewGroup): AssemblyItem<DATA> {
        val item = createItem(parent)
        clickListenerManager?.register(item, item.itemView)
        return item
    }

    abstract fun createItem(parent: ViewGroup): AssemblyItem<DATA>

    override fun setOnViewClickListener(
        @IdRes viewId: Int,
        onClickListener: OnClickListener<DATA>
    ): AssemblyItemFactory<DATA> {
        getClickListenerManagerOrCreate().add(viewId, onClickListener)
        return this
    }

    override fun setOnViewLongClickListener(
        @IdRes viewId: Int,
        onLongClickListener: OnLongClickListener<DATA>
    ): AssemblyItemFactory<DATA> {
        getClickListenerManagerOrCreate().add(viewId, onLongClickListener)
        return this
    }

    override fun setOnItemClickListener(onClickListener: OnClickListener<DATA>): AssemblyItemFactory<DATA> {
        getClickListenerManagerOrCreate().add(onClickListener)
        return this
    }

    override fun setOnItemLongClickListener(onLongClickListener: OnLongClickListener<DATA>): AssemblyItemFactory<DATA> {
        getClickListenerManagerOrCreate().add(onLongClickListener)
        return this
    }

    private fun getClickListenerManagerOrCreate(): ClickListenerManager<DATA> {
        return (clickListenerManager ?: (ClickListenerManager<DATA>().apply {
            this@AssemblyItemFactory.clickListenerManager = this
        }))
    }
}