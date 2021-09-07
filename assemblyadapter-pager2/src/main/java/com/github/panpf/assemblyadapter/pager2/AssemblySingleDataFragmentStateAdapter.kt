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
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.github.panpf.assemblyadapter.AssemblyAdapter
import com.github.panpf.assemblyadapter.internal.ItemFactoryStorage
import com.github.panpf.assemblyadapter.pager.FragmentItemFactory
import com.github.panpf.assemblyadapter.recycler.ConcatAdapterAbsoluteHelper

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
    itemFactory: FragmentItemFactory<DATA>,
    initData: DATA? = null
) : FragmentStateAdapter(fragmentManager, lifecycle), AssemblyAdapter<FragmentItemFactory<*>> {

    private var recyclerView: RecyclerView? = null
    private val concatAdapterAbsoluteHelper = ConcatAdapterAbsoluteHelper()
    private val itemFactoryStorage = ItemFactoryStorage(listOf(itemFactory))

    /**
     * The only data of the current adapter, notifyItem\* will be triggered when the data changes
     */
    var data: DATA? = initData
        set(value) {
            val oldItem = field != null
            val newItem = value != null
            field = value
            if (oldItem && !newItem) {
                notifyItemRemoved(0)
            } else if (newItem && !oldItem) {
                notifyItemInserted(0)
            } else if (oldItem && newItem) {
                notifyItemChanged(0)
            }
        }

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

    override fun getItemCount(): Int = if (data != null) 1 else 0

    fun getItemData(position: Int): DATA {
        val count = itemCount
        if (position < 0 || position >= count) {
            throw IndexOutOfBoundsException("Index: $position, Size: $count")
        }
        return data!!
    }

    override fun getItemViewType(position: Int): Int {
        val data = getItemData(position)
        return itemFactoryStorage.getItemTypeByData(
            data, "FragmentItemFactory", "AssemblySingleDataFragmentStateAdapter", "itemFactory"
        )
    }

    override fun createFragment(position: Int): Fragment {
        val data = getItemData(position)
        @Suppress("UnnecessaryVariable") val bindingAdapterPosition = position
        val parentAdapter = recyclerView?.adapter
        val absoluteAdapterPosition = if (parentAdapter != null) {
            concatAdapterAbsoluteHelper.findAbsoluteAdapterPosition(parentAdapter, this, position)
        } else {
            bindingAdapterPosition
        }
        return getItemFactoryByPosition(position).dispatchCreateFragment(
            bindingAdapterPosition, absoluteAdapterPosition, data
        )
    }


    override fun getItemFactoryByPosition(position: Int): FragmentItemFactory<DATA> {
        val data = getItemData(position)
        return itemFactoryStorage.getItemFactoryByData(
            data, "FragmentItemFactory", "AssemblySingleDataFragmentStateAdapter", "itemFactory"
        )
    }


    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        this.recyclerView = recyclerView
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)
        this.recyclerView = null
    }
}