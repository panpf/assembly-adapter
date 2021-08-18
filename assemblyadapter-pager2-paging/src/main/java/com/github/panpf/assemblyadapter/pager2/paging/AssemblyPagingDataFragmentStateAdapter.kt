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
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.github.panpf.assemblyadapter.AssemblyAdapter
import com.github.panpf.assemblyadapter.Placeholder
import com.github.panpf.assemblyadapter.internal.ItemFactoryStorage
import com.github.panpf.assemblyadapter.pager.FragmentItemFactory
import com.github.panpf.assemblyadapter.recycler.ConcatAdapterAbsoluteHelper
import com.github.panpf.assemblyadapter.recycler.KeyEqualsDiffItemCallback
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

/**
 * An implementation of [PagingDataFragmentStateAdapter], which implements multi-type adapters through standardized [FragmentItemFactory].
 * [AssemblyPagingDataFragmentStateAdapter] will use the data corresponding to position to find a matching [FragmentItemFactory] (cannot find an exception will be thrown),
 * and then use [FragmentItemFactory] to create an [Fragment]
 *
 * @param itemFactoryList The collection of [FragmentItemFactory] passed in from outside, cannot be empty.
 * Each type of data in the data set must have a matching [FragmentItemFactory], otherwise an exception will be thrown
 * @param diffCallback DiffUtil comparison data callback, the default is [KeyEqualsDiffItemCallback]
 * @see FragmentItemFactory
 */
open class AssemblyPagingDataFragmentStateAdapter<DATA : Any>(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle,
    itemFactoryList: List<FragmentItemFactory<*>>,
    diffCallback: DiffUtil.ItemCallback<DATA> = KeyEqualsDiffItemCallback(),
    mainDispatcher: CoroutineDispatcher = Dispatchers.Main,
    workerDispatcher: CoroutineDispatcher = Dispatchers.Default,
) : PagingDataFragmentStateAdapter<DATA, RecyclerView.ViewHolder>(
    fragmentManager,
    lifecycle,
    diffCallback,
    mainDispatcher,
    workerDispatcher
), AssemblyAdapter<FragmentItemFactory<*>> {

    private val itemFactoryStorage = ItemFactoryStorage(itemFactoryList)
    private var recyclerView: RecyclerView? = null
    private val concatAdapterAbsoluteHelper = ConcatAdapterAbsoluteHelper()

    /**
     * Get [FragmentManager] and [Lifecycle] from [FragmentActivity] to create [AssemblyPagingDataFragmentStateAdapter]
     *
     * @param itemFactoryList The collection of [FragmentItemFactory] passed in from outside, cannot be empty.
     * Each type of data in the data set must have a matching [FragmentItemFactory], otherwise an exception will be thrown
     * @param diffCallback DiffUtil comparison data callback, the default is [KeyEqualsDiffItemCallback]
     */
    constructor(
        fragmentActivity: FragmentActivity,
        itemFactoryList: List<FragmentItemFactory<*>>,
        diffCallback: DiffUtil.ItemCallback<DATA> = KeyEqualsDiffItemCallback(),
        mainDispatcher: CoroutineDispatcher = Dispatchers.Main,
        workerDispatcher: CoroutineDispatcher = Dispatchers.Default,
    ) : this(
        fragmentActivity.supportFragmentManager,
        fragmentActivity.lifecycle,
        itemFactoryList,
        diffCallback,
        mainDispatcher,
        workerDispatcher,
    )

    /**
     * Get [FragmentManager] and [Lifecycle] from [Fragment] to create [AssemblyPagingDataFragmentStateAdapter]
     *
     * @param itemFactoryList The collection of [FragmentItemFactory] passed in from outside, cannot be empty.
     * Each type of data in the data set must have a matching [FragmentItemFactory], otherwise an exception will be thrown
     * @param diffCallback DiffUtil comparison data callback, the default is [KeyEqualsDiffItemCallback]
     */
    constructor(
        fragment: Fragment,
        itemFactoryList: List<FragmentItemFactory<*>>,
        diffCallback: DiffUtil.ItemCallback<DATA> = KeyEqualsDiffItemCallback(),
        mainDispatcher: CoroutineDispatcher = Dispatchers.Main,
        workerDispatcher: CoroutineDispatcher = Dispatchers.Default,
    ) : this(
        fragment.childFragmentManager,
        fragment.lifecycle,
        itemFactoryList,
        diffCallback,
        mainDispatcher,
        workerDispatcher,
    )

    init {
        require(itemFactoryList.isNotEmpty()) { "itemFactoryList Can not be empty" }

        if (diffCallback is KeyEqualsDiffItemCallback) {
            KeyEqualsDiffItemCallback.checkDataClass(
                itemFactoryList.map { it.dataClass }.filter { it.java != Placeholder::class.java }
            )
        }
    }


    override fun createFragment(position: Int): Fragment {
        // Here you must use the getItem method to trigger append load
        val data = getItem(position) ?: Placeholder

        @Suppress("UnnecessaryVariable") val bindingAdapterPosition = position
        val parentAdapter = recyclerView?.adapter
        val absoluteAdapterPosition = if (parentAdapter is ConcatAdapter) {
            concatAdapterAbsoluteHelper.findAbsoluteAdapterPosition(parentAdapter, this, position)
        } else {
            bindingAdapterPosition
        }

        @Suppress("UNCHECKED_CAST")
        val itemFactory = itemFactoryStorage.getItemFactoryByData(
            data, "FragmentItemFactory", "AssemblyPagingFragmentStateAdapter", "itemFactoryList"
        ) as FragmentItemFactory<Any>
        return itemFactory.dispatchCreateFragment(
            bindingAdapterPosition, absoluteAdapterPosition, data
        )
    }


    override fun getItemFactoryByPosition(position: Int): FragmentItemFactory<*> {
        val data = peek(position) ?: Placeholder
        return itemFactoryStorage.getItemFactoryByData(
            data, "FragmentItemFactory", "AssemblyPagingFragmentStateAdapter", "itemFactoryList"
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