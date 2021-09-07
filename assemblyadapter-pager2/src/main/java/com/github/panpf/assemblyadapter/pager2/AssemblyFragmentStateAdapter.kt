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
import com.github.panpf.assemblyadapter.Placeholder
import com.github.panpf.assemblyadapter.internal.ItemDataStorage
import com.github.panpf.assemblyadapter.internal.ItemFactoryStorage
import com.github.panpf.assemblyadapter.pager.FragmentItemFactory
import com.github.panpf.assemblyadapter.recycler.ConcatAdapterAbsoluteHelper

/**
 * An implementation of [FragmentStateAdapter], which implements multi-type adapters through standardized [FragmentItemFactory].
 * [AssemblyFragmentStateAdapter] will use the data corresponding to position to find a matching [FragmentItemFactory] (cannot find an exception will be thrown),
 * and then use [FragmentItemFactory] to create an [Fragment]
 *
 * @param itemFactoryList The collection of [FragmentItemFactory] passed in from outside, cannot be empty.
 * Each type of data in the data set must have a matching [FragmentItemFactory], otherwise an exception will be thrown
 * @param initDataList Initial data set
 * @see FragmentItemFactory
 */
open class AssemblyFragmentStateAdapter<DATA>(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle,
    itemFactoryList: List<FragmentItemFactory<*>>,
    initDataList: List<DATA>? = null
) : FragmentStateAdapter(fragmentManager, lifecycle), AssemblyAdapter<FragmentItemFactory<*>> {

    private val itemFactoryStorage = ItemFactoryStorage(itemFactoryList)
    private val itemDataStorage = ItemDataStorage(initDataList) { notifyDataSetChanged() }
    private var recyclerView: RecyclerView? = null
    private val concatAdapterAbsoluteHelper = ConcatAdapterAbsoluteHelper()

    /**
     * Get the current list. If a null list is submitted through [submitList], or no list is submitted, an empty list will be returned.
     * The returned list may not change-changes to the content must be passed through [submitList].
     */
    val currentList: List<DATA>
        get() = itemDataStorage.readOnlyList

    /**
     * Get [FragmentManager] and [Lifecycle] from [FragmentActivity] to create [AssemblyFragmentStateAdapter]
     *
     * @param itemFactoryList The collection of [FragmentItemFactory] passed in from outside, cannot be empty.
     * Each type of data in the data set must have a matching [FragmentItemFactory], otherwise an exception will be thrown
     * @param initDataList Initial data set
     */
    constructor(
        fragmentActivity: FragmentActivity,
        itemFactoryList: List<FragmentItemFactory<*>>,
        initDataList: List<DATA>? = null
    ) : this(
        fragmentActivity.supportFragmentManager,
        fragmentActivity.lifecycle,
        itemFactoryList,
        initDataList
    )

    /**
     * Get [FragmentManager] and [Lifecycle] from [Fragment] to create [AssemblyFragmentStateAdapter]
     *
     * @param itemFactoryList The collection of [FragmentItemFactory] passed in from outside, cannot be empty.
     * Each type of data in the data set must have a matching [FragmentItemFactory], otherwise an exception will be thrown
     * @param initDataList Initial data set
     */
    constructor(
        fragment: Fragment,
        itemFactoryList: List<FragmentItemFactory<*>>,
        initDataList: List<DATA>? = null
    ) : this(
        fragment.childFragmentManager,
        fragment.lifecycle,
        itemFactoryList,
        initDataList
    )

    init {
        require(itemFactoryList.isNotEmpty()) { "itemFactoryList Can not be empty" }
    }

    /**
     * Set the new list to be displayed.
     */
    fun submitList(list: List<DATA>?) {
        itemDataStorage.submitList(list)
    }


    override fun getItemCount(): Int {
        return itemDataStorage.dataCount
    }

    fun getItemData(position: Int): DATA {
        return itemDataStorage.getData(position)
    }

    override fun getItemViewType(position: Int): Int {
        val data = getItemData(position) ?: Placeholder
        return itemFactoryStorage.getItemTypeByData(
            data, "FragmentItemFactory", "AssemblyFragmentStateAdapter", "itemFactoryList"
        )
    }

    override fun createFragment(position: Int): Fragment {
        val data = getItemData(position) ?: Placeholder

        @Suppress("UnnecessaryVariable") val bindingAdapterPosition = position
        val parentAdapter = recyclerView?.adapter
        val absoluteAdapterPosition = if (parentAdapter != null) {
            concatAdapterAbsoluteHelper.findAbsoluteAdapterPosition(parentAdapter, this, position)
        } else {
            bindingAdapterPosition
        }

        @Suppress("UNCHECKED_CAST")
        val itemFactory = itemFactoryStorage.getItemFactoryByData(
            data, "FragmentItemFactory", "AssemblyFragmentStateAdapter", "itemFactoryList"
        ) as FragmentItemFactory<Any>
        return itemFactory.dispatchCreateFragment(
            bindingAdapterPosition, absoluteAdapterPosition, data
        )
    }


    override fun getItemFactoryByPosition(position: Int): FragmentItemFactory<*> {
        val data = getItemData(position) ?: Placeholder
        return itemFactoryStorage.getItemFactoryByData(
            data, "FragmentItemFactory", "AssemblyFragmentStateAdapter", "itemFactoryList"
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