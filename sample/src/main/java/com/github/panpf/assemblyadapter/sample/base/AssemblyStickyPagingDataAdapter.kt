package com.github.panpf.assemblyadapter.sample.base

import androidx.recyclerview.widget.DiffUtil
import com.github.panpf.assemblyadapter.ItemFactory
import com.github.panpf.assemblyadapter.paging.AssemblyPagingDataAdapter
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import me.panpf.recycler.sticky.StickyRecyclerAdapter

class AssemblyStickyPagingDataAdapter<DATA : Any>(
    itemFactoryList: List<ItemFactory<*>>,
    diffCallback: DiffUtil.ItemCallback<DATA>,
    mainDispatcher: CoroutineDispatcher = Dispatchers.Main,
    workerDispatcher: CoroutineDispatcher = Dispatchers.Default
) : AssemblyPagingDataAdapter<DATA>(
    itemFactoryList, diffCallback, mainDispatcher, workerDispatcher
), StickyRecyclerAdapter {

    override fun isStickyItemByType(type: Int): Boolean {
        return getItemFactoryByItemType(type) is StickyItemFactory
    }
}