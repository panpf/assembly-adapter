package com.github.panpf.assemblyadapter.sample.base

import androidx.recyclerview.widget.DiffUtil
import com.github.panpf.assemblyadapter.AssemblyItemFactory
import com.github.panpf.assemblyadapter.recycler.paging.AssemblyPagingDataAdapter
import com.github.panpf.assemblyadapter.sample.base.sticky.StickyRecyclerAdapter
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

class AssemblyStickyPagingDataAdapter<DATA : Any>(
    itemFactoryList: List<AssemblyItemFactory<*>>,
    diffCallback: DiffUtil.ItemCallback<DATA>,
    mainDispatcher: CoroutineDispatcher = Dispatchers.Main,
    workerDispatcher: CoroutineDispatcher = Dispatchers.Default
) : AssemblyPagingDataAdapter<DATA>(
    itemFactoryList, diffCallback, mainDispatcher, workerDispatcher
), StickyRecyclerAdapter {

    override fun isStickyItemByPosition(position: Int): Boolean {
        return getItemFactoryByPosition(position) is StickyItemFactory
    }
}