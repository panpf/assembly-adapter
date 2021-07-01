package com.github.panpf.assemblyadapter.list.expandable

import android.view.View
import android.view.ViewGroup
import com.github.panpf.assemblyadapter.ItemFactory

/**
 * It is not recommended to directly inherit [ExpandableItemFactory], you can inherit [BindingExpandableItemFactory] and [SimpleExpandableItemFactory] to implement your own ItemFactory
 * @see BindingExpandableItemFactory
 * @see SimpleExpandableItemFactory
 * @see ViewExpandableItemFactory
 */
abstract class ExpandableItemFactory<DATA> : ItemFactory<DATA>() {

    abstract override fun createItem(parent: ViewGroup): ExpandableItem<DATA>

    abstract class ExpandableItem<DATA> : Item<DATA> {

        private var _groupBindingAdapterPosition = -1
        private var _groupAbsoluteAdapterPosition = -1
        private var _childPosition = -1
        private var _isExpanded = false
        private var _isLastChild = false

        val groupBindingAdapterPosition: Int
            get() = _groupBindingAdapterPosition
        val groupAbsoluteAdapterPosition: Int
            get() = _groupAbsoluteAdapterPosition
        val childPosition: Int
            get() = _childPosition
        val isExpanded: Boolean
            get() = _isExpanded
        val isLastChild: Boolean
            get() = _isLastChild

        val isGroup: Boolean
            get() = childPosition == -1
        val isChild: Boolean
            get() = childPosition != -1

        constructor(itemView: View) : super(itemView)

        constructor(itemLayoutId: Int, parent: ViewGroup) : super(itemLayoutId, parent)

        open fun dispatchExpandableBindData(
            bindingAdapterPosition: Int,
            absoluteAdapterPosition: Int,
            groupBindingAdapterPosition: Int,
            groupAbsoluteAdapterPosition: Int,
            childPosition: Int,
            isExpanded: Boolean,
            isLastChild: Boolean,
            data: DATA
        ) {
            this._groupBindingAdapterPosition = groupBindingAdapterPosition
            this._groupAbsoluteAdapterPosition = groupAbsoluteAdapterPosition
            this._childPosition = childPosition
            this._isExpanded = isExpanded
            this._isLastChild = isLastChild
            super.dispatchBindData(bindingAdapterPosition, absoluteAdapterPosition, data)
        }
    }
}