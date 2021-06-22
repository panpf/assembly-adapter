package com.github.panpf.assemblyadapter.list.expandable

import android.view.ViewGroup
import androidx.annotation.IdRes
import com.github.panpf.assemblyadapter.*
import com.github.panpf.assemblyadapter.common.item.R
import com.github.panpf.assemblyadapter.internal.ClickListenerManager

abstract class AssemblyExpandableItemFactory<DATA> : ItemFactory<DATA> {

    private var clickListenerManager: ClickListenerManager<DATA>? = null

    override fun dispatchCreateItem(parent: ViewGroup): AssemblyExpandableItem<DATA> {
        return createItem(parent).apply {
            registerItemClickListener(this)
        }
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

    private fun registerItemClickListener(item: Item<DATA>) {
        val clickListenerManager = clickListenerManager ?: return
        val itemView = item.getItemView()
        for (holder in clickListenerManager.holders) {
            if (holder is ClickListenerManager.ClickListenerHolder<*>) {
                @Suppress("UNCHECKED_CAST")
                val clickListenerHolder = holder as ClickListenerManager.ClickListenerHolder<DATA>
                val viewId = clickListenerHolder.viewId
                val targetView = if (viewId > 0) {
                    itemView.findViewById(viewId)
                        ?: throw IllegalArgumentException("Not found click bind target view by id $viewId")
                } else {
                    itemView
                }
                targetView.setTag(R.id.aa_tag_item, item)
                targetView.setOnClickListener { view ->
                    @Suppress("UNCHECKED_CAST")
                    val bindItem = view.getTag(R.id.aa_tag_item) as Item<DATA>
                    clickListenerHolder.listener.onClick(
                        view.context, view, bindItem.getPosition(), bindItem.getData()
                    )
                }
            } else if (holder is ClickListenerManager.LongClickListenerHolder<*>) {
                @Suppress("UNCHECKED_CAST")
                val longClickListenerHolder =
                    holder as ClickListenerManager.LongClickListenerHolder<DATA>
                val viewId = longClickListenerHolder.viewId
                val targetView = if (viewId > 0) {
                    itemView.findViewById(viewId)
                        ?: throw IllegalArgumentException("Not found long click bind target view by id $viewId")
                } else {
                    itemView
                }
                targetView.setTag(R.id.aa_tag_item, item)
                targetView.setOnLongClickListener { view ->
                    @Suppress("UNCHECKED_CAST")
                    val bindItem = view.getTag(R.id.aa_tag_item) as Item<DATA>
                    longClickListenerHolder.listener.onLongClick(
                        view.context, view, bindItem.getPosition(), bindItem.getData()
                    )
                }
            }
        }
    }
}