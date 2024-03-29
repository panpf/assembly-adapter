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
import com.github.panpf.assemblyadapter.Item
import com.github.panpf.assemblyadapter.ItemFactory
import kotlin.reflect.KClass

/**
 * Specially used in the [BaseExpandableListAdapter.getGroupView] method
 *
 * It is not recommended to directly inherit [ExpandableGroupItemFactory],
 * you can inherit [BindingExpandableGroupItemFactory] and [SimpleExpandableGroupItemFactory] to implement your own ItemFactory
 *
 * @see ExpandableGroupItem
 * @see BindingExpandableGroupItemFactory
 * @see SimpleExpandableGroupItemFactory
 * @see ViewExpandableGroupItemFactory
 */
abstract class ExpandableGroupItemFactory<DATA : ExpandableGroup>(
    dataClass: KClass<DATA>
) : ItemFactory<DATA>(dataClass) {

    final override fun createItem(parent: ViewGroup): Item<DATA> {
        return createExpandableGroupItem(parent)
    }

    abstract fun createExpandableGroupItem(parent: ViewGroup): ExpandableGroupItem<DATA>
}