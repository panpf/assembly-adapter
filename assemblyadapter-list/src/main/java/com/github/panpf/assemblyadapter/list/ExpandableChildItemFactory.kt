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
package com.github.panpf.assemblyadapter.list

import android.view.View
import android.view.ViewGroup
import com.github.panpf.assemblyadapter.ItemFactory
import kotlin.reflect.KClass

/**
 * It is not recommended to directly inherit [ExpandableChildItemFactory],
 * you can inherit [BindingExpandableChildItemFactory] and [SimpleExpandableChildItemFactory] to implement your own ItemFactory
 *
 * @see BindingExpandableChildItemFactory
 * @see SimpleExpandableChildItemFactory
 * @see ViewExpandableChildItemFactory
 */
abstract class ExpandableChildItemFactory<GROUP_DATA : ExpandableGroup, CHILD_DATA : Any>(dataClass: KClass<CHILD_DATA>) :
    ItemFactory<CHILD_DATA>(dataClass) {

    abstract override fun createItem(parent: ViewGroup): ExpandableChildItem<GROUP_DATA, CHILD_DATA>

    abstract class ExpandableChildItem<GROUP_DATA : ExpandableGroup, CHILD_DATA : Any> :
        Item<CHILD_DATA> {

        private var _groupBindingAdapterPosition = -1
        private var _groupAbsoluteAdapterPosition = -1
        private var _groupData: GROUP_DATA? = null
        private var _isLastChild = false

        val groupBindingAdapterPosition: Int
            get() = _groupBindingAdapterPosition
        val groupAbsoluteAdapterPosition: Int
            get() = _groupAbsoluteAdapterPosition
        val groupDataOrNull: GROUP_DATA?
            get() = _groupData
        val groupDataOrThrow: GROUP_DATA
            get() = _groupData!!
        val isLastChild: Boolean
            get() = _isLastChild

        constructor(itemView: View) : super(itemView)

        constructor(itemLayoutId: Int, parent: ViewGroup) : super(itemLayoutId, parent)

        fun dispatchChildBindData(
            groupBindingAdapterPosition: Int,
            groupAbsoluteAdapterPosition: Int,
            groupData: GROUP_DATA,
            isLastChild: Boolean,
            bindingAdapterPosition: Int,
            absoluteAdapterPosition: Int,
            data: CHILD_DATA,
        ) {
            this._groupBindingAdapterPosition = groupBindingAdapterPosition
            this._groupAbsoluteAdapterPosition = groupAbsoluteAdapterPosition
            this._groupData = groupData
            this._isLastChild = isLastChild
            super.dispatchBindData(bindingAdapterPosition, absoluteAdapterPosition, data)
        }

        final override fun bindData(
            bindingAdapterPosition: Int,
            absoluteAdapterPosition: Int,
            data: CHILD_DATA
        ) {
            bindData(
                groupBindingAdapterPosition,
                groupAbsoluteAdapterPosition,
                groupDataOrThrow,
                isLastChild,
                bindingAdapterPosition,
                absoluteAdapterPosition,
                data,
            )
        }

        protected abstract fun bindData(
            groupBindingAdapterPosition: Int,
            groupAbsoluteAdapterPosition: Int,
            groupData: GROUP_DATA,
            isLastChild: Boolean,
            bindingAdapterPosition: Int,
            absoluteAdapterPosition: Int,
            data: CHILD_DATA,
        )
    }
}