package com.github.panpf.assemblyadapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.IdRes
import com.github.panpf.assemblyadapter.common.item.R
import com.github.panpf.assemblyadapter.internal.ClickListenerManager

/**
 * It is not recommended to directly inherit [ItemFactory], you can inherit [BindingItemFactory] and [SimpleItemFactory] to implement your own ItemFactory
 * @see BindingItemFactory
 * @see SimpleItemFactory
 * @see ViewItemFactory
 */
abstract class ItemFactory<DATA> : MatchItemFactory {

    private var clickListenerManager: ClickListenerManager<DATA>? = null

    abstract override fun match(data: Any): Boolean

    open fun dispatchCreateItem(parent: ViewGroup): Item<DATA> {
        return createItem(parent).apply {
            registerItemClickListener(this)
        }
    }

    protected abstract fun createItem(parent: ViewGroup): Item<DATA>

    open fun setOnViewClickListener(
        @IdRes viewId: Int,
        onClickListener: OnClickListener<DATA>
    ): ItemFactory<DATA> {
        getClickListenerManagerOrCreate().add(viewId, onClickListener)
        return this
    }

    open fun setOnViewLongClickListener(
        @IdRes viewId: Int,
        onLongClickListener: OnLongClickListener<DATA>
    ): ItemFactory<DATA> {
        getClickListenerManagerOrCreate().add(viewId, onLongClickListener)
        return this
    }

    open fun setOnItemClickListener(onClickListener: OnClickListener<DATA>): ItemFactory<DATA> {
        getClickListenerManagerOrCreate().add(onClickListener)
        return this
    }

    open fun setOnItemLongClickListener(onLongClickListener: OnLongClickListener<DATA>): ItemFactory<DATA> {
        getClickListenerManagerOrCreate().add(onLongClickListener)
        return this
    }

    private fun getClickListenerManagerOrCreate(): ClickListenerManager<DATA> {
        return (clickListenerManager ?: (ClickListenerManager<DATA>().apply {
            this@ItemFactory.clickListenerManager = this
        }))
    }

    private fun registerItemClickListener(item: Item<DATA>) {
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
                    val bindItem = view.getTag(R.id.aa_tag_item) as Item<DATA>
                    clickListenerHolder.listener.onClick(
                        view.context,
                        view,
                        bindItem.bindingAdapterPosition,
                        bindItem.absoluteAdapterPosition,
                        bindItem.requireData
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
                        view.context,
                        view,
                        bindItem.bindingAdapterPosition,
                        bindItem.absoluteAdapterPosition,
                        bindItem.requireData
                    )
                }
            }
        }
    }

    abstract class Item<DATA>(val itemView: View) {

        private var _data: DATA? = null
        private var _bindingAdapterPosition: Int = -1
        private var _absoluteAdapterPosition: Int = -1

        val context: Context = itemView.context

        val dataOrNull: DATA?
            get() = _data
        val requireData: DATA
            get() = _data!!
        val bindingAdapterPosition: Int
            get() = _bindingAdapterPosition
        val absoluteAdapterPosition: Int
            get() = _absoluteAdapterPosition

        constructor(itemLayoutId: Int, parent: ViewGroup) : this(
            LayoutInflater.from(parent.context).inflate(itemLayoutId, parent, false)
        )

        open fun dispatchBindData(
            bindingAdapterPosition: Int,
            absoluteAdapterPosition: Int,
            data: DATA
        ) {
            this._data = data
            this._bindingAdapterPosition = bindingAdapterPosition
            this._absoluteAdapterPosition = absoluteAdapterPosition
            bindData(_absoluteAdapterPosition, data)
        }

        protected abstract fun bindData(bindingAdapterPosition: Int, data: DATA)
    }
}