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
package com.github.panpf.assemblyadapter.pager2

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import com.github.panpf.assemblyadapter.pager.FragmentItemFactory

/**
 * Single data version of [AssemblyFragmentStateAdapter]
 *
 * @param itemFactory Can match [data]'s [FragmentItemFactory]
 * @param initData Initial data
 * @see FragmentItemFactory
 */
open class AssemblySingleDataFragmentStateAdapter<DATA : Any>(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle,
    val itemFactory: FragmentItemFactory<DATA>,
    initData: DATA? = null
) : AssemblyFragmentStateAdapter<DATA>(fragmentManager, lifecycle, listOf(itemFactory)) {

    /**
     * The only data of the current adapter, notifyItem\* will be triggered when the data changes
     */
    var data: DATA?
        set(value) {
            if (value != null) {
                super.submitList(listOf(value))
            } else {
                super.submitList(null)
            }
        }
        get() = if (itemCount > 0) getItemData(0) else null

    /**
     * Get [FragmentManager] and [Lifecycle] from [FragmentActivity] to create [AssemblySingleDataFragmentStateAdapter]
     *
     * @param itemFactory Can match [data]'s [FragmentItemFactory]
     * @param initData Initial data
     */
    constructor(
        fragmentActivity: FragmentActivity,
        itemFactory: FragmentItemFactory<DATA>,
        initData: DATA? = null
    ) : this(
        fragmentActivity.supportFragmentManager,
        fragmentActivity.lifecycle,
        itemFactory,
        initData
    )

    /**
     * Get [FragmentManager] and [Lifecycle] from [Fragment] to create [AssemblySingleDataFragmentStateAdapter]
     *
     * @param itemFactory Can match [data]'s [FragmentItemFactory]
     * @param initData Initial data
     */
    constructor(
        fragment: Fragment,
        itemFactory: FragmentItemFactory<DATA>,
        initData: DATA? = null
    ) : this(fragment.childFragmentManager, fragment.lifecycle, itemFactory, initData)

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

    override fun onDataListChanged(oldList: List<DATA>, newList: List<DATA>) {
        val oldItem = oldList.firstOrNull() != null
        val newItem = newList.firstOrNull() != null
        if (oldItem && !newItem) {
            notifyItemRemoved(0)
        } else if (newItem && !oldItem) {
            notifyItemInserted(0)
        } else if (oldItem && newItem) {
            notifyItemChanged(0)
        }
    }
}