/*
 * Copyright (C) 2017 Peng fei Pan <sky@panpf.me>
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
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.github.panpf.assemblyadapter.internal.ItemManager
import com.github.panpf.assemblyadapter.pager.AssemblyFragmentItemFactory
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

class AssemblyPagingFragmentStateAdapter<DATA : Any>(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle,
    diffCallback: DiffUtil.ItemCallback<DATA>,
    itemFactoryList: List<AssemblyFragmentItemFactory<*>>,
    mainDispatcher: CoroutineDispatcher = Dispatchers.Main,
    workerDispatcher: CoroutineDispatcher = Dispatchers.Default,
) : PagingFragmentStateAdapter<DATA, RecyclerView.ViewHolder>(
    fragmentManager,
    lifecycle,
    diffCallback,
    mainDispatcher,
    workerDispatcher
) {

    private val itemManager = ItemManager(itemFactoryList)

    constructor(
        fragmentActivity: FragmentActivity,
        diffCallback: DiffUtil.ItemCallback<DATA>,
        itemFactoryList: List<AssemblyFragmentItemFactory<*>>,
        mainDispatcher: CoroutineDispatcher = Dispatchers.Main,
        workerDispatcher: CoroutineDispatcher = Dispatchers.Default,
    ) : this(
        fragmentActivity.supportFragmentManager,
        fragmentActivity.lifecycle,
        diffCallback,
        itemFactoryList,
        mainDispatcher,
        workerDispatcher
    )

    constructor(
        fragment: Fragment,
        diffCallback: DiffUtil.ItemCallback<DATA>,
        itemFactoryList: List<AssemblyFragmentItemFactory<*>>,
        mainDispatcher: CoroutineDispatcher = Dispatchers.Main,
        workerDispatcher: CoroutineDispatcher = Dispatchers.Default,
    ) : this(
        fragment.childFragmentManager,
        fragment.lifecycle,
        diffCallback,
        itemFactoryList,
        mainDispatcher,
        workerDispatcher
    )

    override fun createFragment(position: Int): Fragment {
        val data = getItem(position)

        @Suppress("UNCHECKED_CAST")
        val itemFactory = itemManager.getItemFactoryByData(data) as AssemblyFragmentItemFactory<Any>
        return itemFactory.dispatchCreateFragment(position, data)
    }


    fun getItemFactoryByItemType(itemType: Int): AssemblyFragmentItemFactory<*> {
        return itemManager.getItemFactoryByItemType(itemType)
    }

    fun getItemFactoryByPosition(position: Int): AssemblyFragmentItemFactory<*> {
        return itemManager.getItemFactoryByData(peek(position))
    }
}