package com.github.panpf.assemblyadapter.sample.base

import com.github.panpf.assemblyadapter.ItemFactory
import com.github.panpf.assemblyadapter.recycler.AssemblyRecyclerAdapter
import com.github.panpf.assemblyadapter.sample.ui.sticky.StickyRecyclerAdapter

class AssemblyStickyRecyclerAdapter<DATA> : AssemblyRecyclerAdapter<DATA>, StickyRecyclerAdapter {

    constructor(itemFactoryList: List<ItemFactory<*>>) : super(itemFactoryList)

    constructor(itemFactoryList: List<ItemFactory<*>>, dataList: List<DATA>?)
            : super(itemFactoryList, dataList)

    override fun isStickyItemByPosition(position: Int): Boolean {
        return getItemFactoryByPosition(position) is StickyItemFactory
    }
}