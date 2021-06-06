package com.github.panpf.assemblyadapter.sample.base

import com.github.panpf.assemblyadapter.AssemblyItemFactory
import com.github.panpf.assemblyadapter.ItemFactory
import com.github.panpf.assemblyadapter.recycler.AssemblyRecyclerAdapter
import me.panpf.recycler.sticky.StickyRecyclerAdapter

class StickyAssemblyRecyclerAdapter<DATA> : AssemblyRecyclerAdapter<DATA>, StickyRecyclerAdapter {

    constructor(itemFactoryList: List<AssemblyItemFactory<*>>) : super(itemFactoryList)

    constructor(
        itemFactoryList: List<ItemFactory<*>>, dataList: MutableList<DATA>?
    ) : super(itemFactoryList, dataList)

    constructor(
        itemFactoryList: List<ItemFactory<*>>, dataArray: Array<out DATA>?
    ) : super(itemFactoryList, dataArray)

    override fun isStickyItemByType(type: Int): Boolean {
        return getItemFactoryByItemType(type) is StickyItemFactory
    }

    interface StickyItemFactory
}