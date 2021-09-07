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
package com.github.panpf.assemblyadapter.pager2.paging

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.paging.LoadState
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.RecyclerView
import com.github.panpf.assemblyadapter.AssemblyAdapter
import com.github.panpf.assemblyadapter.internal.ItemFactoryStorage
import com.github.panpf.assemblyadapter.pager.FragmentItemFactory
import com.github.panpf.assemblyadapter.recycler.ConcatAdapterAbsoluteHelper

/**
 * An implementation of [LoadStateFragmentStateAdapter], Realize the display of [LoadState] through standardized [FragmentItemFactory].
 *
 * @param itemFactory Must be a [FragmentItemFactory] that can match [LoadState]
 * @param alwaysShowWhenEndOfPaginationReached If true, it will still be displayed at the end of pagination reached
 */
open class AssemblyLoadStateFragmentStateAdapter(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle,
    itemFactory: FragmentItemFactory<LoadState>,
    private val alwaysShowWhenEndOfPaginationReached: Boolean = false,
) : LoadStateFragmentStateAdapter(fragmentManager, lifecycle),
    AssemblyAdapter<FragmentItemFactory<*>> {

    private var recyclerView: RecyclerView? = null
    private val concatAdapterAbsoluteHelper = ConcatAdapterAbsoluteHelper()
    private val itemFactoryStorage = ItemFactoryStorage(listOf(itemFactory))


    /**
     * Get [FragmentManager] and [Lifecycle] from [FragmentActivity] to create [AssemblyLoadStateFragmentStateAdapter]
     *
     * @param itemFactory Must be a [FragmentItemFactory] that can match [LoadState]
     * @param alwaysShowWhenEndOfPaginationReached If true, it will still be displayed at the end of pagination reached
     */
    constructor(
        fragmentActivity: FragmentActivity,
        itemFactory: FragmentItemFactory<LoadState>,
        alwaysShowWhenEndOfPaginationReached: Boolean = false,
    ) : this(
        fragmentActivity.supportFragmentManager,
        fragmentActivity.lifecycle,
        itemFactory,
        alwaysShowWhenEndOfPaginationReached
    )

    /**
     * Get [FragmentManager] and [Lifecycle] from [Fragment] to create [AssemblyLoadStateFragmentStateAdapter]
     *
     * @param itemFactory Must be a [FragmentItemFactory] that can match [LoadState]
     * @param alwaysShowWhenEndOfPaginationReached If true, it will still be displayed at the end of pagination reached
     */
    constructor(
        fragment: Fragment,
        itemFactory: FragmentItemFactory<LoadState>,
        alwaysShowWhenEndOfPaginationReached: Boolean = false,
    ) : this(
        fragment.childFragmentManager,
        fragment.lifecycle,
        itemFactory,
        alwaysShowWhenEndOfPaginationReached
    )

    fun getItemData(position: Int): LoadState {
        val count = itemCount
        if (position < 0 || position >= count) {
            throw IndexOutOfBoundsException("Index: $position, Size: $count")
        }
        return loadState
    }

    override fun displayLoadStateAsItem(loadState: LoadState): Boolean {
        return loadState is LoadState.Loading
                || loadState is LoadState.Error
                || (alwaysShowWhenEndOfPaginationReached && loadState is LoadState.NotLoading && loadState.endOfPaginationReached)
    }

    override fun onCreateFragment(position: Int, loadState: LoadState): Fragment {
        val data = getItemData(position)
        val itemFactory = itemFactoryStorage.getItemFactoryByData(
            loadState, "FragmentItemFactory", "AssemblyLoadStateFragmentStateAdapter", "itemFactory"
        )
        @Suppress("UnnecessaryVariable") val bindingAdapterPosition = position
        val parentAdapter = recyclerView?.adapter
        val absoluteAdapterPosition = if (parentAdapter is ConcatAdapter) {
            concatAdapterAbsoluteHelper.findAbsoluteAdapterPosition(parentAdapter, this, position)
        } else {
            bindingAdapterPosition
        }

        return itemFactory.dispatchCreateFragment(
            bindingAdapterPosition, absoluteAdapterPosition, data
        )
    }


    override fun getItemFactoryByPosition(position: Int): FragmentItemFactory<LoadState> {
        val data = getItemData(position)
        return itemFactoryStorage.getItemFactoryByData(
            data, "FragmentItemFactory", "AssemblyLoadStateFragmentStateAdapter", "itemFactory"
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