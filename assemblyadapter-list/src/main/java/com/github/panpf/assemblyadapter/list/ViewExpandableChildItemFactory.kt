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

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import kotlin.reflect.KClass

open class ViewExpandableChildItemFactory<GROUP_DATA : ExpandableGroup, CHILD_DATA : Any>(
    dataClazz: KClass<CHILD_DATA>,
    private val viewFactory: (context: Context, inflater: LayoutInflater, parent: ViewGroup) -> View
) : SimpleExpandableChildItemFactory<GROUP_DATA, CHILD_DATA>(dataClazz) {

    constructor(dataClazz: KClass<CHILD_DATA>, @LayoutRes layoutResId: Int) : this(
        dataClazz,
        { _, inflater, parent -> inflater.inflate(layoutResId, parent, false) }
    )

    constructor(dataClazz: KClass<CHILD_DATA>, view: View) : this(dataClazz, { _, _, _ -> view })

    override fun createItemView(
        context: Context, inflater: LayoutInflater, parent: ViewGroup
    ): View {
        return viewFactory(context, inflater, parent)
    }

    override fun bindItemData(
        context: Context,
        itemView: View,
        item: ExpandableChildItem<GROUP_DATA, CHILD_DATA>,
        groupBindingAdapterPosition: Int,
        groupAbsoluteAdapterPosition: Int,
        groupData: GROUP_DATA,
        isLastChild: Boolean,
        bindingAdapterPosition: Int,
        absoluteAdapterPosition: Int,
        data: CHILD_DATA,
    ) {
    }
}