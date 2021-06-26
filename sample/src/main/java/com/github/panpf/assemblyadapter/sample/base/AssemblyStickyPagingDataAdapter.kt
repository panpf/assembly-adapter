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
        placeholderItemFactory: ItemFactory<Placeholder>?,
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
        itemFactoryList: List<ItemFactory<*>>,
        diffCallback: DiffUtil.ItemCallback<DATA>,
        placeholderItemFactory: ItemFactory<Placeholder>
    ) : super(itemFactoryList, diffCallback, placeholderItemFactory)

    constructor(
        itemFactoryList: List<ItemFactory<*>>,
        diffCallback: DiffUtil.ItemCallback<DATA>
    ) : super(itemFactoryList, diffCallback)


    override fun isStickyItemByPosition(position: Int): Boolean {
        return getItemFactoryByPosition(position) is StickyItemFactory
    }
}