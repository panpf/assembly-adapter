package com.github.panpf.assemblyadapter.sample.base

import androidx.recyclerview.widget.DiffUtil
import com.github.panpf.assemblyadapter.AssemblyItemFactory
import com.github.panpf.assemblyadapter.AssemblyPlaceholderItemFactory
import com.github.panpf.assemblyadapter.recycler.paging.AssemblyPagingDataAdapter
import com.github.panpf.assemblyadapter.sample.base.sticky.StickyRecyclerAdapter
import kotlinx.coroutines.CoroutineDispatcher

class AssemblyStickyPagingDataAdapter<DATA : Any> : AssemblyPagingDataAdapter<DATA>,
    StickyRecyclerAdapter {

    constructor(
        itemFactoryList: List<AssemblyItemFactory<*>>,
        diffCallback: DiffUtil.ItemCallback<DATA>,
        placeholderItemFactory: AssemblyPlaceholderItemFactory?,
        mainDispatcher: CoroutineDispatcher,
        workerDispatcher: CoroutineDispatcher
    ) : super(
        itemFactoryList,
        diffCallback,
        placeholderItemFactory,
        mainDispatcher,
        workerDispatcher
    )

    constructor(
        itemFactoryList: List<AssemblyItemFactory<*>>,
        diffCallback: DiffUtil.ItemCallback<DATA>,
        placeholderItemFactory: AssemblyPlaceholderItemFactory
    ) : super(itemFactoryList, diffCallback, placeholderItemFactory)

    constructor(
        itemFactoryList: List<AssemblyItemFactory<*>>,
        diffCallback: DiffUtil.ItemCallback<DATA>
    ) : super(itemFactoryList, diffCallback)


    override fun isStickyItemByPosition(position: Int): Boolean {
        return getItemFactoryByPosition(position) is StickyItemFactory
    }
}