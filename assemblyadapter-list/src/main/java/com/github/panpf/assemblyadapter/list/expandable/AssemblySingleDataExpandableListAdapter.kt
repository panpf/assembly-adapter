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

import com.github.panpf.assemblyadapter.ItemFactory

/**
 * Single data version of [AssemblyExpandableListAdapter]
 *
 * @param itemFactoryList The collection of [ItemFactory] or [ExpandableGroupItemFactory] or [ExpandableChildItemFactory] passed in from outside, cannot be empty.
 * Each type of data in the data set must have a matching [ItemFactory], otherwise an exception will be thrown
 * @param initData Initial data
 * @see ItemFactory
 * @see ExpandableGroupItemFactory
 * @see ExpandableChildItemFactory
 */
open class AssemblySingleDataExpandableListAdapter<GROUP_DATA : Any, CHILD_DATA : Any>(
    itemFactoryList: List<ItemFactory<out Any>>,
    initData: GROUP_DATA? = null,
) : AssemblyExpandableListAdapter<GROUP_DATA, CHILD_DATA>(itemFactoryList) {

    var data: GROUP_DATA?
        set(value) {
            if (value != null) {
                super.submitList(listOf(value))
            } else {
                super.submitList(null)
            }
        }
        get() = if (itemCount > 0) getItemData(0) else null

    constructor(
        itemFactory: ItemFactory<GROUP_DATA>,
        initData: GROUP_DATA? = null,
    ) : this(listOf(itemFactory), initData)

    init {
        if (initData != null) {
            this.data = initData
        }
    }

    override fun submitList(list: List<GROUP_DATA>?) {
        require(list?.size ?: 0 <= 1) {
            "Cannot submit a list with size greater than 1"
        }
        super.submitList(list)
    }
}