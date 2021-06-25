package com.github.panpf.assemblyadapter

import android.view.ViewGroup
import androidx.annotation.IdRes
import com.github.panpf.assemblyadapter.common.item.R
import com.github.panpf.assemblyadapter.internal.ClickListenerManager

abstract class AssemblyItemFactory<DATA> : ItemFactory {

    private var clickListenerManager: ClickListenerManager<DATA>? = null

    abstract override fun match(data: Any?): Boolean

    open fun dispatchCreateItem(parent: ViewGroup): AssemblyItem<DATA> {
        return createItem(parent).apply {
            registerItemClickListener(this)
        }
    }

    abstract fun createItem(parent: ViewGroup): AssemblyItem<DATA>

    fun setOnViewClickListener(
        @IdRes viewId: Int,
        onClickListener: OnClickListener<DATA>
    ): AssemblyItemFactory<DATA> {
        getClickListenerManagerOrCreate().add(viewId, onClickListener)
        return this
    }

    fun setOnViewLongClickListener(
        @IdRes viewId: Int,
        onLongClickListener: OnLongClickListener<DATA>
    ): AssemblyItemFactory<DATA> {
        getClickListenerManagerOrCreate().add(viewId, onLongClickListener)
        return this
    }

    fun setOnItemClickListener(onClickListener: OnClickListener<DATA>): AssemblyItemFactory<DATA> {
        getClickListenerManagerOrCreate().add(onClickListener)
        return this
    }

    fun setOnItemLongClickListener(onLongClickListener: OnLongClickListener<DATA>): AssemblyItemFactory<DATA> {
        getClickListenerManagerOrCreate().add(onLongClickListener)
        return this
    }

    private fun getClickListenerManagerOrCreate(): ClickListenerManager<DATA> {
        return (clickListenerManager ?: (ClickListenerManager<DATA>().apply {
            this@AssemblyItemFactory.clickListenerManager = this
        }))
    }

    private fun registerItemClickListener(item: AssemblyItem<DATA>) {
        val clickListenerManager = clickListenerManager ?: return
        val itemView = item.itemView
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
                    val bindItem = view.getTag(R.id.aa_tag_item) as AssemblyItem<DATA>
                    clickListenerHolder.listener.onClick(
                        view.context,
                        view,
                        bindItem.bindingAdapterPosition,
                        bindItem.absoluteAdapterPosition,
                        bindItem.data
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
                    val bindItem = view.getTag(R.id.aa_tag_item) as AssemblyItem<DATA>
                    longClickListenerHolder.listener.onLongClick(
                        view.context,
                        view,
                        bindItem.bindingAdapterPosition,
                        bindItem.absoluteAdapterPosition,
                        bindItem.data
                    )
                }
            }
        }
    }
}