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
package com.github.panpf.assemblyadapter.sample.base

import androidx.recyclerview.widget.DiffUtil
import com.github.panpf.assemblyadapter.ItemFactory
import com.github.panpf.assemblyadapter.Placeholder
import com.github.panpf.assemblyadapter.recycler.paging.AssemblyPagingDataAdapter
import com.github.panpf.assemblyadapter.sample.base.sticky.StickyRecyclerAdapter
import kotlinx.coroutines.CoroutineDispatcher

class AssemblyStickyPagingDataAdapter<DATA : Any> : AssemblyPagingDataAdapter<DATA>,
    StickyRecyclerAdapter {

    constructor(
        itemFactoryList: List<ItemFactory<*>>,
        diffCallback: DiffUtil.ItemCallback<DATA>,
        mainDispatcher: CoroutineDispatcher,
        workerDispatcher: CoroutineDispatcher
    ) : super(itemFactoryList, diffCallback, mainDispatcher, workerDispatcher)

    constructor(
        itemFactoryList: List<ItemFactory<*>>,
        diffCallback: DiffUtil.ItemCallback<DATA>
    ) : super(itemFactoryList, diffCallback)


    override fun isStickyItemByPosition(position: Int): Boolean {
        return getItemFactoryByPosition(position) is StickyItemFactory
    }
}