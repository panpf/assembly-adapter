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
package com.github.panpf.assemblyadapter.recycler.paging

import android.view.ViewGroup
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.github.panpf.assemblyadapter.AssemblyAdapter
import com.github.panpf.assemblyadapter.Item
import com.github.panpf.assemblyadapter.ItemFactory
import com.github.panpf.assemblyadapter.internal.ItemFactoryStorage
import com.github.panpf.assemblyadapter.recycler.internal.FullSpanSupportByPosition
import com.github.panpf.assemblyadapter.recycler.internal.RecyclerViewHolderWrapper

/**
 * An implementation of [LoadStateAdapter], Realize the display of [LoadState] through standardized [ItemFactory].
 *
 * @param itemFactory Must be a [ItemFactory] that can match [LoadState]
 * @param alwaysShowWhenEndOfPaginationReached If true, it will still be displayed at the end of pagination reached
 */
open class AssemblyLoadStateAdapter(
    itemFactory: ItemFactory<LoadState>,
    private val alwaysShowWhenEndOfPaginationReached: Boolean = false,
) : LoadStateAdapter<RecyclerView.ViewHolder>(), AssemblyAdapter<LoadState, ItemFactory<out Any>> {

    private val itemFactoryStorage = ItemFactoryStorage<ItemFactory<out Any>>(
        listOf(itemFactory), "ItemFactory", "AssemblyLoadStateAdapter", "itemFactory"
    )

    private var parent: RecyclerView? = null

    fun getItemData(position: Int): LoadState {
        val count = itemCount
        if (position < 0 || position >= count) {
            throw IndexOutOfBoundsException("Index: $position, Size: $count")
        }
        return loadState
    }

    override fun onCreateViewHolder(
        parent: ViewGroup, loadState: LoadState
    ): RecyclerView.ViewHolder {
        val itemFactory = itemFactoryStorage.getItemFactoryByData(loadState)
        val item = itemFactory.dispatchCreateItem(parent)
        return RecyclerViewHolderWrapper(item)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, loadState: LoadState) {
        if (holder is RecyclerViewHolderWrapper<*>) {
            @Suppress("UNCHECKED_CAST")
            val item = holder.wrappedItem as Item<Any>
            val absoluteAdapterPosition =
                holder.absoluteAdapterPosition.takeIf { it != -1 } ?: holder.position
            item.dispatchBindData(0, absoluteAdapterPosition, loadState)

            val layoutManager = parent?.layoutManager
            if (layoutManager is StaggeredGridLayoutManager
                && layoutManager is FullSpanSupportByPosition
            ) {
                (holder.itemView.layoutParams as StaggeredGridLayoutManager.LayoutParams)
                    .isFullSpan = layoutManager.isFullSpanByPosition(absoluteAdapterPosition)
            }
        } else {
            throw IllegalArgumentException("holder must be RecyclerViewHolderWrapper")
        }
    }

    override fun displayLoadStateAsItem(loadState: LoadState): Boolean {
        return loadState is LoadState.Loading
                || loadState is LoadState.Error
                || (alwaysShowWhenEndOfPaginationReached && loadState is LoadState.NotLoading && loadState.endOfPaginationReached)
    }


    override fun getItemFactoryByPosition(position: Int): ItemFactory<Any> {
        val data = getItemData(position)
        return itemFactoryStorage.getItemFactoryByData(data) as ItemFactory<Any>
    }

    override fun getItemFactoryByData(data: LoadState): ItemFactory<Any> {
        return itemFactoryStorage.getItemFactoryByData(data) as ItemFactory<Any>
    }

    override fun <T : ItemFactory<out Any>> getItemFactoryByClass(itemFactoryClass: Class<T>): T {
        return itemFactoryStorage.getItemFactoryByClass(itemFactoryClass)
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        parent = recyclerView
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        parent = null
        super.onDetachedFromRecyclerView(recyclerView)
    }
}