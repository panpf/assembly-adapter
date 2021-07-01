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
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.github.panpf.assemblyadapter.*
import com.github.panpf.assemblyadapter.internal.ItemFactoryStorage
import com.github.panpf.assemblyadapter.recycler.internal.AssemblyItemViewHolderWrapper
import com.github.panpf.assemblyadapter.recycler.FullSpanStaggeredGridLayoutManager
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

open class AssemblyPagingDataAdapter<DATA : Any>(
    itemFactoryList: List<ItemFactory<*>>,
    diffCallback: DiffUtil.ItemCallback<DATA>,
    placeholderItemFactory: ItemFactory<Placeholder>? = null,
    mainDispatcher: CoroutineDispatcher = Dispatchers.Main,
    workerDispatcher: CoroutineDispatcher = Dispatchers.Default,
) : PagingDataAdapter<DATA, RecyclerView.ViewHolder>(
    diffCallback, mainDispatcher, workerDispatcher
), AssemblyAdapter {

    private val itemFactoryStorage = ItemFactoryStorage(
        if (placeholderItemFactory != null) itemFactoryList.plus(placeholderItemFactory) else itemFactoryList
    )

    constructor(
        itemFactoryList: List<ItemFactory<*>>,
        diffCallback: DiffUtil.ItemCallback<DATA>,
        placeholderItemFactory: ItemFactory<Placeholder>,
    ) : this(
        itemFactoryList,
        diffCallback,
        placeholderItemFactory,
        Dispatchers.Main,
        Dispatchers.Default
    )

    constructor(
        itemFactoryList: List<ItemFactory<*>>,
        diffCallback: DiffUtil.ItemCallback<DATA>,
    ) : this(
        itemFactoryList,
        diffCallback,
        null,
        Dispatchers.Main,
        Dispatchers.Default
    )

    init {
        placeholderItemFactory?.apply {
            if (!match(Placeholder)) {
                throw IllegalArgumentException("'${placeholderItemFactory::class.java.name}' 's match(Any) method must return true when passing in Placeholder")
            }
            if (match(0)) {
                throw IllegalArgumentException("'${placeholderItemFactory::class.java.name}' 's match(Any) method must return false when passing in non Placeholder")
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        val data = peek(position) ?: Placeholder
        return itemFactoryStorage.getItemTypeByData(data)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val itemFactory = itemFactoryStorage.getItemFactoryByItemType(viewType)
        val item = itemFactory.dispatchCreateItem(parent)
        return AssemblyItemViewHolderWrapper(item).apply {
            val layoutManager =
                (parent.takeIf { it is RecyclerView } as RecyclerView?)?.layoutManager
            if (layoutManager is FullSpanStaggeredGridLayoutManager) {
                layoutManager.setFullSpan(itemView, itemFactory)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is AssemblyItemViewHolderWrapper<*>) {
            @Suppress("UNCHECKED_CAST")
            val item = holder.wrappedItem as ItemFactory.Item<Any>
            // Here you must use the getItem method to trigger append load
            val data = getItem(position) ?: Placeholder
            item.dispatchBindData(position, holder.position, data)
        } else {
            throw IllegalArgumentException("holder must be AssemblyItemViewHolderWrapper")
        }
    }


    override fun getItemFactoryByPosition(position: Int): ItemFactory<*> {
        val data = peek(position) ?: Placeholder
        return itemFactoryStorage.getItemFactoryByData(data)
    }


    class Builder<DATA : Any>(
        private val itemFactoryList: List<ItemFactory<*>>,
        private val diffCallback: DiffUtil.ItemCallback<DATA>
    ) {
        private var placeholderItemFactory: ItemFactory<Placeholder>? = null
        private var mainDispatcher: CoroutineDispatcher = Dispatchers.Main
        private var workerDispatcher: CoroutineDispatcher = Dispatchers.Default

        fun setPlaceholderItemFactory(placeholderItemFactory: ItemFactory<Placeholder>?) {
            this.placeholderItemFactory = placeholderItemFactory
        }

        fun setMainDispatcher(mainDispatcher: CoroutineDispatcher) {
            this.mainDispatcher = mainDispatcher
        }

        fun setWorkerDispatcher(workerDispatcher: CoroutineDispatcher) {
            this.workerDispatcher = workerDispatcher
        }

        fun build(): AssemblyPagingDataAdapter<DATA> {
            return AssemblyPagingDataAdapter(
                itemFactoryList,
                diffCallback,
                placeholderItemFactory,
                mainDispatcher,
                workerDispatcher
            )
        }
    }
}