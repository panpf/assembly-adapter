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

import com.github.panpf.assemblyadapter.ItemFactory

/**
 * Single data version of [AssemblyListAdapter]
 *
 * @param itemFactory Can match [data]'s [ItemFactory]
 * @param initData Initial data
 * @see ItemFactory
 */
open class AssemblySingleDataListAdapter<DATA : Any>(
    itemFactory: ItemFactory<DATA>,
    initData: DATA? = null,
) : AssemblyListAdapter<DATA>(listOf(itemFactory)) {

    var data: DATA?
        set(value) {
            if (value != null) {
                super.submitList(listOf(value))
            } else {
                super.submitList(null)
            }
        }
        get() = if (itemCount > 0) getItemData(0) else null

    init {
        if (initData != null) {
            this.data = initData
        }
    }

    override fun submitList(list: List<DATA>?) {
        require(list?.size ?: 0 <= 1) {
            "Cannot submit a list with size greater than 1"
        }
        super.submitList(list)
    }
}