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
import android.widget.BaseExpandableListAdapter
import com.github.panpf.assemblyadapter.ItemFactory
import kotlin.reflect.KClass

/**
 * Specially used in the [BaseExpandableListAdapter.getGroupView] method, the isExpanded parameter is added on the basis of the normal [ItemFactory]
 *
 * It is not recommended to directly inherit [ExpandableGroupItemFactory],
 * you can inherit [BindingExpandableGroupItemFactory] and [SimpleExpandableGroupItemFactory] to implement your own ItemFactory
 *
 * @see BindingExpandableGroupItemFactory
 * @see SimpleExpandableGroupItemFactory
 * @see ViewExpandableGroupItemFactory
 */
abstract class ExpandableGroupItemFactory<DATA : ExpandableGroup>(dataClass: KClass<DATA>) :
    ItemFactory<DATA>(dataClass) {

    abstract override fun createItem(parent: ViewGroup): ExpandableGroupItem<DATA>

    abstract class ExpandableGroupItem<DATA : Any> : Item<DATA> {

        private var _isExpanded = false

        val isExpanded: Boolean
            get() = _isExpanded

        constructor(itemView: View) : super(itemView)

        constructor(itemLayoutId: Int, parent: ViewGroup) : super(itemLayoutId, parent)

        fun dispatchGroupBindData(
            isExpanded: Boolean,
            bindingAdapterPosition: Int,
            absoluteAdapterPosition: Int,
            data: DATA,
        ) {
            this._isExpanded = isExpanded
            super.dispatchBindData(bindingAdapterPosition, absoluteAdapterPosition, data)
        }

        final override fun bindData(
            bindingAdapterPosition: Int,
            absoluteAdapterPosition: Int,
            data: DATA
        ) {
            bindData(
                isExpanded,
                bindingAdapterPosition,
                absoluteAdapterPosition,
                data,
            )
        }

        protected abstract fun bindData(
            isExpanded: Boolean,
            bindingAdapterPosition: Int,
            absoluteAdapterPosition: Int,
            data: DATA,
        )
    }
}