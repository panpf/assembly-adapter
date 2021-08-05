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
import com.github.panpf.assemblyadapter.AssemblyAdapter
import com.github.panpf.assemblyadapter.Item
import com.github.panpf.assemblyadapter.ItemFactory
import com.github.panpf.assemblyadapter.recycler.internal.FullSpanStaggeredGridLayoutManager
import com.github.panpf.assemblyadapter.recycler.internal.RecyclerViewHolderWrapper

/**
 * An implementation of [LoadStateAdapter], Realize the display of [LoadState] through standardized [ItemFactory].
 *
 * @param itemFactory Must be a [ItemFactory] that can match [LoadState]
 * @param alwaysShowWhenEndOfPaginationReached If true, it will still be displayed at the end of pagination reached
 */
open class AssemblyLoadStateAdapter(
    private val itemFactory: ItemFactory<LoadState>,
    private val alwaysShowWhenEndOfPaginationReached: Boolean = false,
) : LoadStateAdapter<RecyclerView.ViewHolder>(), AssemblyAdapter<ItemFactory<*>> {

    override fun onCreateViewHolder(
        parent: ViewGroup, loadState: LoadState
    ): RecyclerView.ViewHolder {
        val item = itemFactory.dispatchCreateItem(parent)
        return RecyclerViewHolderWrapper(item).apply {
            val layoutManager =
                (parent.takeIf { it is RecyclerView } as RecyclerView?)?.layoutManager
            if (layoutManager is FullSpanStaggeredGridLayoutManager) {
                layoutManager.setFullSpan(itemView, itemFactory)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, loadState: LoadState) {
        if (holder is RecyclerViewHolderWrapper<*>) {
            @Suppress("UNCHECKED_CAST")
            val item = holder.wrappedItem as Item<Any>
            item.dispatchBindData(0, holder.absoluteAdapterPosition, loadState)
        } else {
            throw IllegalArgumentException("holder must be RecyclerViewHolderWrapper")
        }
    }

    override fun displayLoadStateAsItem(loadState: LoadState): Boolean {
        return loadState is LoadState.Loading
                || loadState is LoadState.Error
                || (alwaysShowWhenEndOfPaginationReached && loadState is LoadState.NotLoading && loadState.endOfPaginationReached)
    }


    override fun getItemFactoryByPosition(position: Int): ItemFactory<*> {
        return itemFactory
    }
}