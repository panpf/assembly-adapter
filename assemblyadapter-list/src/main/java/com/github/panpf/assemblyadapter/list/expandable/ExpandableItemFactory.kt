/*
 * Copyright (C) 2021 panpf <panpfpanpf@outlook.com>
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
package com.github.panpf.assemblyadapter.list.expandable

import android.view.View
import android.view.ViewGroup
import com.github.panpf.assemblyadapter.ItemFactory
import kotlin.reflect.KClass

/**
 * It is not recommended to directly inherit [ExpandableItemFactory], you can inherit [BindingExpandableItemFactory] and [SimpleExpandableItemFactory] to implement your own ItemFactory
 * @see BindingExpandableItemFactory
 * @see SimpleExpandableItemFactory
 * @see ViewExpandableItemFactory
 */
abstract class ExpandableItemFactory<DATA : Any>(dataClass: KClass<DATA>) :
    ItemFactory<DATA>(dataClass) {

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