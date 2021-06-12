package com.github.panpf.assemblyadapter.list

import android.view.ViewGroup
import androidx.annotation.IdRes
import com.github.panpf.assemblyadapter.ClickListenerManager
import com.github.panpf.assemblyadapter.ItemFactory
import com.github.panpf.assemblyadapter.OnClickListener
import com.github.panpf.assemblyadapter.OnLongClickListener

abstract class AssemblyExpandableItemFactory<DATA> : ItemFactory<DATA> {

    private var clickListenerManager: ClickListenerManager<DATA>? = null

    override fun dispatchCreateItem(parent: ViewGroup): AssemblyExpandableItem<DATA> {
        val item = createItem(parent)
        clickListenerManager?.register(item, item.getItemView())
        return item
    }

    abstract fun createItem(parent: ViewGroup): AssemblyExpandableItem<DATA>

    override fun setOnViewClickListener(
        @IdRes viewId: Int,
        onClickListener: OnClickListener<DATA>
    ): AssemblyExpandableItemFactory<DATA> {
        getClickListenerManagerOrCreate().add(viewId, onClickListener)
        return this
    }

    override fun setOnViewLongClickListener(
        @IdRes viewId: Int,
        onLongClickListener: OnLongClickListener<DATA>
    ): AssemblyExpandableItemFactory<DATA> {
        getClickListenerManagerOrCreate().add(viewId, onLongClickListener)
        return this
    }

    override fun setOnItemClickListener(onClickListener: OnClickListener<DATA>): AssemblyExpandableItemFactory<DATA> {
        getClickListenerManagerOrCreate().add(onClickListener)
        return this
    }

    override fun setOnItemLongClickListener(onLongClickListener: OnLongClickListener<DATA>): AssemblyExpandableItemFactory<DATA> {
        getClickListenerManagerOrCreate().add(onLongClickListener)
        return this
    }

    private fun getClickListenerManagerOrCreate(): ClickListenerManager<DATA> {
        return (clickListenerManager ?: (ClickListenerManager<DATA>().apply {
            this@AssemblyExpandableItemFactory.clickListenerManager = this
        }))
    }
}