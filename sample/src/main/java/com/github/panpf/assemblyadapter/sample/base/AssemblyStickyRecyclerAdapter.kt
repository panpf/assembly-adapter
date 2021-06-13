package com.github.panpf.assemblyadapter.sample.base

import com.github.panpf.assemblyadapter.recycler.AssemblyRecyclerAdapter
import com.github.panpf.assemblyadapter.ItemFactory
import me.panpf.recycler.sticky.StickyRecyclerAdapter

class AssemblyStickyRecyclerAdapter<DATA> : AssemblyRecyclerAdapter<DATA>, StickyRecyclerAdapter {

    constructor(itemFactoryList: List<ItemFactory<*>>) : super(itemFactoryList)

    constructor(itemFactoryList: List<ItemFactory<*>>, dataList: List<DATA>?)
            : super(itemFactoryList, dataList)

    override fun isStickyItemByType(type: Int): Boolean {
        return getItemFactoryByItemType(type) is StickyItemFactory
    }
}