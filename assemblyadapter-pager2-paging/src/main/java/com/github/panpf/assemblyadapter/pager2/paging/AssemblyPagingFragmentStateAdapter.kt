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
import com.github.panpf.assemblyadapter.AssemblyAdapter
import com.github.panpf.assemblyadapter.Placeholder
import com.github.panpf.assemblyadapter.internal.ItemFactoryStorage
import com.github.panpf.assemblyadapter.pager.fragment.AssemblyFragmentItemFactory
import com.github.panpf.assemblyadapter.pager.fragment.AssemblyFragmentPlaceholderItemFactory
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

class AssemblyPagingFragmentStateAdapter<DATA : Any>(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle,
    diffCallback: DiffUtil.ItemCallback<DATA>,
    itemFactoryList: List<AssemblyFragmentItemFactory<*>>,
    placeholderItemFactory: AssemblyFragmentPlaceholderItemFactory? = null,
    mainDispatcher: CoroutineDispatcher = Dispatchers.Main,
    workerDispatcher: CoroutineDispatcher = Dispatchers.Default,
) : PagingFragmentStateAdapter<DATA, RecyclerView.ViewHolder>(
    fragmentManager,
    lifecycle,
    diffCallback,
    mainDispatcher,
    workerDispatcher
), AssemblyAdapter {

    private val itemFactoryStorage = ItemFactoryStorage(
        if (placeholderItemFactory != null) itemFactoryList.plus(placeholderItemFactory) else itemFactoryList
    )

    constructor(
        fragmentManager: FragmentManager,
        lifecycle: Lifecycle,
        diffCallback: DiffUtil.ItemCallback<DATA>,
        itemFactoryList: List<AssemblyFragmentItemFactory<*>>,
        placeholderItemFactory: AssemblyFragmentPlaceholderItemFactory?,
    ) : this(
        fragmentManager,
        lifecycle,
        diffCallback,
        itemFactoryList,
        placeholderItemFactory,
        Dispatchers.Main,
        Dispatchers.Default,
    )

    constructor(
        fragmentManager: FragmentManager,
        lifecycle: Lifecycle,
        diffCallback: DiffUtil.ItemCallback<DATA>,
        itemFactoryList: List<AssemblyFragmentItemFactory<*>>,
    ) : this(
        fragmentManager,
        lifecycle,
        diffCallback,
        itemFactoryList,
        null,
        Dispatchers.Main,
        Dispatchers.Default,
    )

    constructor(
        fragmentActivity: FragmentActivity,
        diffCallback: DiffUtil.ItemCallback<DATA>,
        itemFactoryList: List<AssemblyFragmentItemFactory<*>>,
        placeholderItemFactory: AssemblyFragmentPlaceholderItemFactory? = null,
        mainDispatcher: CoroutineDispatcher = Dispatchers.Main,
        workerDispatcher: CoroutineDispatcher = Dispatchers.Default,
    ) : this(
        fragmentActivity.supportFragmentManager,
        fragmentActivity.lifecycle,
        diffCallback,
        itemFactoryList,
        placeholderItemFactory,
        mainDispatcher,
        workerDispatcher
    )

    constructor(
        fragmentActivity: FragmentActivity,
        diffCallback: DiffUtil.ItemCallback<DATA>,
        itemFactoryList: List<AssemblyFragmentItemFactory<*>>,
        placeholderItemFactory: AssemblyFragmentPlaceholderItemFactory?,
    ) : this(
        fragmentActivity.supportFragmentManager,
        fragmentActivity.lifecycle,
        diffCallback,
        itemFactoryList,
        placeholderItemFactory,
        Dispatchers.Main,
        Dispatchers.Default,
    )

    constructor(
        fragmentActivity: FragmentActivity,
        diffCallback: DiffUtil.ItemCallback<DATA>,
        itemFactoryList: List<AssemblyFragmentItemFactory<*>>,
    ) : this(
        fragmentActivity.supportFragmentManager,
        fragmentActivity.lifecycle,
        diffCallback,
        itemFactoryList,
        null,
        Dispatchers.Main,
        Dispatchers.Default,
    )

    constructor(
        fragment: Fragment,
        diffCallback: DiffUtil.ItemCallback<DATA>,
        itemFactoryList: List<AssemblyFragmentItemFactory<*>>,
        placeholderItemFactory: AssemblyFragmentPlaceholderItemFactory? = null,
        mainDispatcher: CoroutineDispatcher = Dispatchers.Main,
        workerDispatcher: CoroutineDispatcher = Dispatchers.Default,
    ) : this(
        fragment.childFragmentManager,
        fragment.lifecycle,
        diffCallback,
        itemFactoryList,
        placeholderItemFactory,
        mainDispatcher,
        workerDispatcher
    )

    constructor(
        fragment: Fragment,
        diffCallback: DiffUtil.ItemCallback<DATA>,
        itemFactoryList: List<AssemblyFragmentItemFactory<*>>,
        placeholderItemFactory: AssemblyFragmentPlaceholderItemFactory?,
    ) : this(
        fragment.childFragmentManager,
        fragment.lifecycle,
        diffCallback,
        itemFactoryList,
        placeholderItemFactory,
        Dispatchers.Main,
        Dispatchers.Default,
    )

    constructor(
        fragment: Fragment,
        diffCallback: DiffUtil.ItemCallback<DATA>,
        itemFactoryList: List<AssemblyFragmentItemFactory<*>>,
    ) : this(
        fragment.childFragmentManager,
        fragment.lifecycle,
        diffCallback,
        itemFactoryList,
        null,
        Dispatchers.Main,
        Dispatchers.Default,
    )

    override fun createFragment(position: Int): Fragment {
        // Here you must use the getItem method to trigger append load
        // And must be placed in this position
        val data = getItem(position)
        val matchData = data ?: Placeholder

        @Suppress("UNCHECKED_CAST")
        val itemFactory =
            itemFactoryStorage.getItemFactoryByData(matchData) as AssemblyFragmentItemFactory<Any>
        return if (itemFactory is AssemblyFragmentPlaceholderItemFactory) {
            itemFactory.dispatchCreateFragment(position, Placeholder)
        } else {
            itemFactory.dispatchCreateFragment(position, data!!)
        }
    }


    override fun getItemFactoryByPosition(position: Int): AssemblyFragmentItemFactory<*> {
        val matchData = peek(position) ?: Placeholder
        return itemFactoryStorage.getItemFactoryByData(matchData)
    }


    class Builder<DATA : Any>(
        private val fragmentManager: FragmentManager,
        private val lifecycle: Lifecycle,
        private val diffCallback: DiffUtil.ItemCallback<DATA>,
        private val itemFactoryList: List<AssemblyFragmentItemFactory<*>>,
    ) {

        private var placeholderItemFactory: AssemblyFragmentPlaceholderItemFactory? = null
        private var mainDispatcher: CoroutineDispatcher = Dispatchers.Main
        private var workerDispatcher: CoroutineDispatcher = Dispatchers.Default

        fun setPlaceholderItemFactory(placeholderItemFactory: AssemblyFragmentPlaceholderItemFactory?) {
            this.placeholderItemFactory = placeholderItemFactory
        }

        fun setMainDispatcher(mainDispatcher: CoroutineDispatcher) {
            this.mainDispatcher = mainDispatcher
        }

        fun setWorkerDispatcher(workerDispatcher: CoroutineDispatcher) {
            this.workerDispatcher = workerDispatcher
        }

        fun build(): AssemblyPagingFragmentStateAdapter<DATA> {
            return AssemblyPagingFragmentStateAdapter(
                fragmentManager,
                lifecycle,
                diffCallback,
                itemFactoryList,
                placeholderItemFactory,
                mainDispatcher,
                workerDispatcher
            )
        }
    }
}