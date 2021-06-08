package com.github.panpf.assemblyadapter

import android.view.View
import androidx.annotation.IdRes
import java.util.*

class ClickListenerManager<DATA> {

    private val holders: MutableList<Any> = LinkedList()

    fun add(@IdRes viewId: Int, onClickListener: OnClickListener<DATA>) {
        holders.add(ClickListenerHolder(viewId, onClickListener))
    }

    fun add(onClickListener: OnClickListener<DATA>) {
        holders.add(ClickListenerHolder(onClickListener))
    }

    fun add(@IdRes viewId: Int, onClickListener: OnLongClickListener<DATA>) {
        holders.add(LongClickListenerHolder(viewId, onClickListener))
    }

    fun add(onClickListener: OnLongClickListener<DATA>) {
        holders.add(LongClickListenerHolder(onClickListener))
    }

    fun register(item: Item<DATA>, itemView: View) {
        for (holder in holders) {
            if (holder is ClickListenerHolder<*>) {
                @Suppress("UNCHECKED_CAST")
                val clickListenerHolder = holder as ClickListenerHolder<DATA>
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
                    val bindItem = targetView.getTag(R.id.aa_tag_item) as Item<DATA>
                    clickListenerHolder.listener.onViewClick(
                        view.context, view, bindItem.getPosition(), bindItem.getData()
                    )
                }
            } else if (holder is LongClickListenerHolder<*>) {
                @Suppress("UNCHECKED_CAST")
                val longClickListenerHolder = holder as LongClickListenerHolder<DATA>
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
                    val bindItem = targetView.getTag(R.id.aa_tag_item) as Item<DATA>
                    longClickListenerHolder.listener.onViewLongClick(
                        view.context, view, bindItem.getPosition(), bindItem.getData()
                    )
                }
            }
        }
    }

    class ClickListenerHolder<DATA>(
        @field:IdRes @param:IdRes @get:IdRes val viewId: Int,
        val listener: OnClickListener<DATA>
    ) {
        constructor(listener: OnClickListener<DATA>) : this(0, listener)
    }

    class LongClickListenerHolder<DATA>(
        @field:IdRes @param:IdRes @get:IdRes val viewId: Int,
        val listener: OnLongClickListener<DATA>
    ) {
        constructor(listener: OnLongClickListener<DATA>) : this(0, listener)
    }
}