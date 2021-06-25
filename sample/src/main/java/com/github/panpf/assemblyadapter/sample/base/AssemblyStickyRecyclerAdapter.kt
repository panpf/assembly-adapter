package com.github.panpf.assemblyadapter.sample.base

import com.github.panpf.assemblyadapter.AssemblyItemFactory
import com.github.panpf.assemblyadapter.recycler.AssemblyRecyclerAdapter
import com.github.panpf.assemblyadapter.sample.base.sticky.StickyRecyclerAdapter

class AssemblyStickyRecyclerAdapter<DATA> : AssemblyRecyclerAdapter<DATA>, StickyRecyclerAdapter {

    constructor(itemFactoryList: List<AssemblyItemFactory<*>>) : super(itemFactoryList)

    constructor(itemFactoryList: List<AssemblyItemFactory<*>>, dataList: List<DATA>?)
            : super(itemFactoryList, dataList)

    override fun isStickyItemByPosition(position: Int): Boolean {
        return getItemFactoryByPosition(position) is StickyItemFactory
    }
}