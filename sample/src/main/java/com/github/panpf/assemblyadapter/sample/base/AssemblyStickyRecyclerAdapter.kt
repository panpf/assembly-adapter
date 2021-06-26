package com.github.panpf.assemblyadapter.sample.base

import com.github.panpf.assemblyadapter.AssemblyItemFactory
import com.github.panpf.assemblyadapter.AssemblyPlaceholderItemFactory
import com.github.panpf.assemblyadapter.recycler.AssemblyRecyclerAdapter
import com.github.panpf.assemblyadapter.sample.base.sticky.StickyRecyclerAdapter

class AssemblyStickyRecyclerAdapter<DATA> : AssemblyRecyclerAdapter<DATA>, StickyRecyclerAdapter {

    constructor(
        itemFactoryList: List<AssemblyItemFactory<*>>,
        placeholderItemFactory: AssemblyPlaceholderItemFactory?,
        dataList: List<DATA>?
    ) : super(itemFactoryList, placeholderItemFactory, dataList)

    constructor(
        itemFactoryList: List<AssemblyItemFactory<*>>,
        placeholderItemFactory: AssemblyPlaceholderItemFactory?
    ) : super(itemFactoryList, placeholderItemFactory)

    constructor(itemFactoryList: List<AssemblyItemFactory<*>>, dataList: List<DATA>?) : super(
        itemFactoryList,
        dataList
    )

    constructor(itemFactoryList: List<AssemblyItemFactory<*>>) : super(itemFactoryList)


    override fun isStickyItemByPosition(position: Int): Boolean {
        return getItemFactoryByPosition(position) is StickyItemFactory
    }
}