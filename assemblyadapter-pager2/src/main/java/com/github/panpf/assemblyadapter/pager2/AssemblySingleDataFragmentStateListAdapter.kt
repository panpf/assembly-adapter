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
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.DiffUtil
import com.github.panpf.assemblyadapter.pager.FragmentItemFactory
import com.github.panpf.assemblyadapter.recycler.KeyEqualsDiffItemCallback

/**
 * Single data version of [FragmentStateListAdapter]
 *
 * @see FragmentItemFactory
 */
open class AssemblySingleDataFragmentStateListAdapter<DATA : Any> :
    AssemblyFragmentStateListAdapter<DATA> {

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
     * Use [FragmentManager] and [Lifecycle] to create [AssemblySingleDataFragmentStateListAdapter]
     *
     * @param itemFactory Can match [data]'s [FragmentItemFactory]
     * @param initData Initial data
     */
    constructor(
        fragmentManager: FragmentManager,
        lifecycle: Lifecycle,
        itemFactory: FragmentItemFactory<DATA>,
        initData: DATA? = null,
        diffCallback: DiffUtil.ItemCallback<DATA> = KeyEqualsDiffItemCallback()
    ) : super(fragmentManager, lifecycle, listOf(itemFactory), null, diffCallback) {
        if (initData != null) {
            this.data = initData
        }
    }

    /**
     * Get [FragmentManager] and [Lifecycle] from [FragmentActivity] to create [AssemblySingleDataFragmentStateListAdapter]
     *
     * @param itemFactory Can match [data]'s [FragmentItemFactory]
     * @param initData Initial data
     */
    constructor(
        fragmentActivity: FragmentActivity,
        itemFactory: FragmentItemFactory<DATA>,
        initData: DATA? = null,
        diffCallback: DiffUtil.ItemCallback<DATA> = KeyEqualsDiffItemCallback()
    ) : this(
        fragmentActivity.supportFragmentManager,
        fragmentActivity.lifecycle,
        itemFactory,
        initData,
        diffCallback
    )

    /**
     * Get [FragmentManager] and [Lifecycle] from [Fragment] to create [AssemblySingleDataFragmentStateListAdapter]
     *
     * @param itemFactory Can match [data]'s [FragmentItemFactory]
     * @param initData Initial data
     */
    constructor(
        fragment: Fragment,
        itemFactory: FragmentItemFactory<DATA>,
        initData: DATA? = null,
        diffCallback: DiffUtil.ItemCallback<DATA> = KeyEqualsDiffItemCallback()
    ) : this(fragment.childFragmentManager, fragment.lifecycle, itemFactory, initData, diffCallback)

    /**
     * Use [FragmentManager] and [Lifecycle] to create [AssemblySingleDataFragmentStateListAdapter]
     *
     * @param itemFactory Can match [data]'s [FragmentItemFactory]
     * @param initData Initial data
     */
    constructor(
        fragmentManager: FragmentManager,
        lifecycle: Lifecycle,
        itemFactory: FragmentItemFactory<DATA>,
        initData: DATA? = null,
        config: AsyncDifferConfig<DATA>
    ) : super(fragmentManager, lifecycle, listOf(itemFactory), null, config) {
        if (initData != null) {
            this.data = initData
        }
    }

    /**
     * Get [FragmentManager] and [Lifecycle] from [FragmentActivity] to create [AssemblySingleDataFragmentStateListAdapter]
     *
     * @param itemFactory Can match [data]'s [FragmentItemFactory]
     * @param initData Initial data
     */
    constructor(
        fragmentActivity: FragmentActivity,
        itemFactory: FragmentItemFactory<DATA>,
        initData: DATA? = null,
        config: AsyncDifferConfig<DATA>
    ) : this(
        fragmentActivity.supportFragmentManager,
        fragmentActivity.lifecycle,
        itemFactory,
        initData,
        config
    )

    /**
     * Get [FragmentManager] and [Lifecycle] from [Fragment] to create [AssemblySingleDataFragmentStateListAdapter]
     *
     * @param itemFactory Can match [data]'s [FragmentItemFactory]
     * @param initData Initial data
     */
    constructor(
        fragment: Fragment,
        itemFactory: FragmentItemFactory<DATA>,
        initData: DATA? = null,
        config: AsyncDifferConfig<DATA>
    ) : this(fragment.childFragmentManager, fragment.lifecycle, itemFactory, initData, config)

    override fun submitList(list: List<DATA>?) {
        require(list?.size ?: 0 <= 1) {
            "Cannot submit a list with size greater than 1"
        }
        super.submitList(list)
    }

    override fun submitList(list: List<DATA>?, commitCallback: Runnable?) {
        require(list?.size ?: 0 <= 1) {
            "Cannot submit a list with size greater than 1"
        }
        super.submitList(list, commitCallback)
    }
}