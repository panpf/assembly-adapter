package com.github.panpf.assemblyadapter.sample.base

import com.github.panpf.assemblyadapter.ItemFactory
import com.github.panpf.assemblyadapter.PlaceholderItemFactory
import com.github.panpf.assemblyadapter.recycler.AssemblyRecyclerAdapter
import com.github.panpf.assemblyadapter.sample.base.sticky.StickyRecyclerAdapter

class AssemblyStickyRecyclerAdapter<DATA> : AssemblyRecyclerAdapter<DATA>, StickyRecyclerAdapter {

    constructor(
        itemFactoryList: List<ItemFactory<*>>,
        placeholderItemFactory: PlaceholderItemFactory?,
        dataList: List<DATA>?
    ) : super(itemFactoryList, placeholderItemFactory, dataList)

    constructor(
        itemFactoryList: List<ItemFactory<*>>,
        placeholderItemFactory: PlaceholderItemFactory?
    ) : super(itemFactoryList, placeholderItemFactory)

    constructor(itemFactoryList: List<ItemFactory<*>>, dataList: List<DATA>?) : super(
        itemFactoryList,
        dataList
    )

    constructor(itemFactoryList: List<ItemFactory<*>>) : super(itemFactoryList)


    override fun isStickyItemByPosition(position: Int): Boolean {
        return getItemFactoryByPosition(position) is StickyItemFactory
    }
}