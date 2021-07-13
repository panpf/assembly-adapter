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
import com.github.panpf.assemblyadapter.diff.DiffKey
import com.github.panpf.assemblyadapter.diff.KeyDiffItemCallback
import com.github.panpf.assemblyadapter.internal.ItemFactoryStorage
import com.github.panpf.assemblyadapter.pager.FragmentItemFactory
import com.github.panpf.assemblyadapter.pager2.internal.ConcatAdapterAbsoluteHelper
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

open class AssemblyPagingFragmentStateAdapter<DATA : Any>(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle,
    itemFactoryList: List<FragmentItemFactory<*>>,
    diffCallback: DiffUtil.ItemCallback<DATA> = KeyDiffItemCallback(),
    mainDispatcher: CoroutineDispatcher = Dispatchers.Main,
    workerDispatcher: CoroutineDispatcher = Dispatchers.Default,
) : PagingFragmentStateAdapter<DATA, RecyclerView.ViewHolder>(
    fragmentManager,
    lifecycle,
    diffCallback,
    mainDispatcher,
    workerDispatcher
), AssemblyAdapter<FragmentItemFactory<*>> {

    private val itemFactoryStorage = ItemFactoryStorage(itemFactoryList)
    private var recyclerView: RecyclerView? = null
    private var concatAdapterAbsoluteHelper: ConcatAdapterAbsoluteHelper? = null

    constructor(
        fragmentActivity: FragmentActivity,
        itemFactoryList: List<FragmentItemFactory<*>>,
        diffCallback: DiffUtil.ItemCallback<DATA> = KeyDiffItemCallback(),
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

    constructor(
        fragment: Fragment,
        itemFactoryList: List<FragmentItemFactory<*>>,
        diffCallback: DiffUtil.ItemCallback<DATA> = KeyDiffItemCallback(),
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
        if (diffCallback is KeyDiffItemCallback) {
            itemFactoryList.forEach { itemFactory ->
                val dataClass = itemFactory.dataClass
                if (!DiffKey::class.java.isAssignableFrom(dataClass.java)) {
                    throw IllegalArgumentException(
                        "Because you use KeyDiffItemCallback, FragmentItemFactory's dataClass " +
                                "'${dataClass.qualifiedName}' must implement the DiffKey interface"
                    )
                }
            }
        }
    }


    override fun createFragment(position: Int): Fragment {
        // Here you must use the getItem method to trigger append load
        val data = getItem(position) ?: Placeholder

        @Suppress("UnnecessaryVariable") val bindingAdapterPosition = position
        val parentAdapter = recyclerView?.adapter
        val absoluteAdapterPosition = if (parentAdapter is ConcatAdapter) {
            (concatAdapterAbsoluteHelper ?: ConcatAdapterAbsoluteHelper().apply {
                concatAdapterAbsoluteHelper = this
            }).findAbsoluteAdapterPosition(
                parentAdapter, this, position
            )
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
        this.concatAdapterAbsoluteHelper = null
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)
        this.recyclerView = null
        this.concatAdapterAbsoluteHelper = null
    }
}