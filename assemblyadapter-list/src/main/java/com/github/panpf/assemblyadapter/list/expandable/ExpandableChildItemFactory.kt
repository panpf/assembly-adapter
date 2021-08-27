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

import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import com.github.panpf.assemblyadapter.ItemFactory
import kotlin.reflect.KClass

/**
 * Specially used in the [BaseExpandableListAdapter.getChildView] method
 *
 * It is not recommended to directly inherit [ExpandableChildItemFactory],
 * you can inherit [BindingExpandableChildItemFactory] and [SimpleExpandableChildItemFactory] to implement your own ItemFactory
 *
 * @see ExpandableChildItem
 * @see BindingExpandableChildItemFactory
 * @see SimpleExpandableChildItemFactory
 * @see ViewExpandableChildItemFactory
 */
abstract class ExpandableChildItemFactory<GROUP_DATA : ExpandableGroup, CHILD_DATA : Any>(
    dataClass: KClass<CHILD_DATA>
) : ItemFactory<CHILD_DATA>(dataClass) {

    abstract override fun createItem(parent: ViewGroup): ExpandableChildItem<GROUP_DATA, CHILD_DATA>
}