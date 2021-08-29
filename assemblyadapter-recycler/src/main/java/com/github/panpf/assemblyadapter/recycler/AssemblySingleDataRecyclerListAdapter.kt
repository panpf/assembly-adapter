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
package com.github.panpf.assemblyadapter.recycler

import android.view.ViewGroup
import androidx.recyclerview.widget.*
import com.github.panpf.assemblyadapter.AssemblyAdapter
import com.github.panpf.assemblyadapter.Item
import com.github.panpf.assemblyadapter.ItemFactory
import com.github.panpf.assemblyadapter.Placeholder
import com.github.panpf.assemblyadapter.recycler.internal.FullSpanSupport
import com.github.panpf.assemblyadapter.recycler.internal.RecyclerViewHolderWrapper

/**
 * Single data version of [AssemblyRecyclerListAdapter]
 */
open class AssemblySingleDataRecyclerListAdapter<DATA : Any> :
    ListAdapter<DATA, RecyclerView.ViewHolder>, AssemblyAdapter<ItemFactory<*>> {

    private val itemFactory: ItemFactory<DATA>

    /**
     * The only data of the current adapter, [notifyDataSetChanged] will be triggered when the data changes
     */
    var data: DATA?
        set(value) {
            if (value != null) {
                super.submitList(listOf(value))
            } else {
                super.submitList(null)
            }
        }
        get() = if (itemCount > 0) getItem(0) else null

    /**
     * Create an AssemblySingleDataRecyclerListAdapter that provides DiffUtil.ItemCallback externally
     *
     *
     * @param itemFactory Can match data's [ItemFactory]
     * @param initData Initial data
     * @param diffCallback DiffUtil comparison data callback, the default is [KeyEqualsDiffItemCallback]
     * @see ItemFactory
     * @see KeyEqualsDiffItemCallback
     */
    constructor(
        itemFactory: ItemFactory<DATA>,
        initData: DATA? = null,
        diffCallback: DiffUtil.ItemCallback<DATA> = KeyEqualsDiffItemCallback(),
    ) : super(diffCallback) {
        this.itemFactory = itemFactory
        data = initData
        if (diffCallback is KeyEqualsDiffItemCallback) {
            KeyEqualsDiffItemCallback.checkDataClass(
                listOf(itemFactory.dataClass).filter { it.java != Placeholder::class.java }
            )
        }
    }

    /**
     * Create an AssemblySingleDataRecyclerListAdapter that provides DiffUtil.ItemCallback externally
     *
     *
     * @param itemFactory Can match data's [ItemFactory]
     * @param diffCallback DiffUtil comparison data callback, the default is [KeyEqualsDiffItemCallback]
     * @see ItemFactory
     * @see KeyEqualsDiffItemCallback
     */
    constructor(
        itemFactory: ItemFactory<DATA>,
        diffCallback: DiffUtil.ItemCallback<DATA> = KeyEqualsDiffItemCallback(),
    ) : super(diffCallback) {
        this.itemFactory = itemFactory
        if (diffCallback is KeyEqualsDiffItemCallback) {
            KeyEqualsDiffItemCallback.checkDataClass(
                listOf(itemFactory.dataClass).filter { it.java != Placeholder::class.java }
            )
        }
    }

    /**
     * Create an AssemblySingleDataRecyclerListAdapter that provides AsyncDifferConfig externally
     *
     * @param itemFactory Can match data's [ItemFactory]
     * @param initData Initial data
     * @param config AsyncDifferConfig
     * @see ItemFactory
     * @see AsyncDifferConfig
     */
    constructor(
        itemFactory: ItemFactory<DATA>,
        initData: DATA?,
        config: AsyncDifferConfig<DATA>,
    ) : super(config) {
        this.itemFactory = itemFactory
        data = initData
    }

    /**
     * Create an AssemblySingleDataRecyclerListAdapter that provides AsyncDifferConfig externally
     *
     * @param itemFactory Can match data's [ItemFactory]
     * @param config AsyncDifferConfig
     * @see ItemFactory
     * @see AsyncDifferConfig
     */
    constructor(
        itemFactory: ItemFactory<DATA>,
        config: AsyncDifferConfig<DATA>,
    ) : super(config) {
        this.itemFactory = itemFactory
    }

    override fun submitList(list: List<DATA>?) {
        require(list?.size ?: 0 > 1) {
            "Cannot submit a list with size greater than 1"
        }
        super.submitList(list)
    }

    override fun submitList(list: List<DATA>?, commitCallback: Runnable?) {
        require(list?.size ?: 0 > 1) {
            "Cannot submit a list with size greater than 1"
        }
        super.submitList(list, commitCallback)
    }

    override fun getItemId(position: Int): Long {
        // todo 不重写 getItemId
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        val count = itemCount
        if (position < 0 || position >= count) {
            throw IndexOutOfBoundsException("Index: $position, Size: $count")
        }
        return 0
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val item = itemFactory.dispatchCreateItem(parent)
        return RecyclerViewHolderWrapper(item).apply {
            val layoutManager =
                (parent.takeIf { it is RecyclerView } as RecyclerView?)?.layoutManager
            if (layoutManager is StaggeredGridLayoutManager && layoutManager is FullSpanSupport) {
                (itemView.layoutParams as StaggeredGridLayoutManager.LayoutParams)
                    .isFullSpan = layoutManager.isFullSpanByItemFactory(itemFactory)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val count = itemCount
        if (position < 0 || position >= count) {
            throw IndexOutOfBoundsException("Index: $position, Size: $count")
        }
        if (holder is RecyclerViewHolderWrapper<*>) {
            @Suppress("UNCHECKED_CAST")
            val item = holder.wrappedItem as Item<Any>
            item.dispatchBindData(position, holder.absoluteAdapterPosition, data!!)
        } else {
            throw IllegalArgumentException("holder must be RecyclerViewHolderWrapper")
        }
    }


    override fun getItemFactoryByPosition(position: Int): ItemFactory<*> {
        val count = itemCount
        if (position < 0 || position >= count) {
            throw IndexOutOfBoundsException("Index: $position, Size: $count")
        }
        return itemFactory
    }
}