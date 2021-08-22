package com.github.panpf.assemblyadapter3.compat.internal

import android.view.View
import androidx.annotation.IdRes
import com.github.panpf.assemblyadapter3.compat.*
import java.util.*

class CompatClickListenerManager<DATA> {
    private val holders: MutableList<Any> = LinkedList()

    fun add(@IdRes viewId: Int, onClickListener: CompatOnClickListener<DATA>) {
        holders.add(ClickListenerHolder(viewId, onClickListener))
    }

    fun add(onClickListener: CompatOnClickListener<DATA>) {
        holders.add(ClickListenerHolder(onClickListener))
    }

    fun add(@IdRes viewId: Int, onClickListener: CompatOnLongClickListener<DATA>) {
        holders.add(LongClickListenerHolder(viewId, onClickListener))
    }

    fun add(onClickListener: CompatOnLongClickListener<DATA>) {
        holders.add(LongClickListenerHolder(onClickListener))
    }

    fun register(
        itemFactory: CompatAssemblyItemFactory<DATA>,
        item: CompatAssemblyItem<DATA>,
        itemView: View
    ) {
        for (holder in holders) {
            if (holder is ClickListenerHolder<*>) {
                @Suppress("UNCHECKED_CAST") val clickListenerHolder =
                    holder as ClickListenerHolder<DATA>
                val viewId = clickListenerHolder.viewId
                val targetView = if (viewId > 0) (itemView.findViewById(viewId)
                    ?: throw IllegalArgumentException("Not found target view by id $viewId")) else itemView
                targetView.setTag(R.id.aa_item_holder, item)
                targetView.setOnClickListener { v: View ->
                    @Suppress("UNCHECKED_CAST") val item1 =
                        targetView.getTag(R.id.aa_item_holder) as CompatAssemblyItem<DATA>
                    val position = item1.adapterPosition
                    val adapter = itemFactory.adapter
                    val positionInPart = adapter?.getPositionInPart(position) ?: position
                    clickListenerHolder.listener.onViewClick(
                        v.context, v, position, positionInPart, item1.data
                    )
                }
            } else if (holder is LongClickListenerHolder<*>) {
                @Suppress("UNCHECKED_CAST") val longClickListenerHolder =
                    holder as LongClickListenerHolder<DATA>
                val viewId = longClickListenerHolder.viewId
                val targetView = if (viewId > 0) (itemView.findViewById(viewId)
                    ?: throw IllegalArgumentException("Not found target view by id $viewId")) else itemView
                targetView.setTag(R.id.aa_item_holder, item)
                targetView.setOnLongClickListener { v: View ->
                    @Suppress("UNCHECKED_CAST") val item12 =
                        targetView.getTag(R.id.aa_item_holder) as CompatAssemblyItem<DATA>
                    val position = item12.adapterPosition
                    val adapter = itemFactory.adapter
                    val positionInPart = adapter?.getPositionInPart(position) ?: position
                    longClickListenerHolder.listener.onViewLongClick(
                        v.context, v, position, positionInPart, item12.data
                    )
                }
            }
        }
    }

    class ClickListenerHolder<DATA> {
        val viewId: Int
        val listener: CompatOnClickListener<DATA>

        constructor(@IdRes viewId: Int, listener: CompatOnClickListener<DATA>) {
            this.viewId = viewId
            this.listener = listener
        }

        constructor(listener: CompatOnClickListener<DATA>) {
            this.viewId = 0
            this.listener = listener
        }
    }

    class LongClickListenerHolder<DATA> {
        val viewId: Int
        val listener: CompatOnLongClickListener<DATA>

        constructor(@IdRes viewId: Int, listener: CompatOnLongClickListener<DATA>) {
            this.viewId = viewId
            this.listener = listener
        }

        constructor(listener: CompatOnLongClickListener<DATA>) {
            this.viewId = 0
            this.listener = listener
        }
    }
}