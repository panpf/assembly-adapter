package me.panpf.adapter.sample.adapter

import me.panpf.adapter.AssemblyRecyclerAdapter
import me.panpf.recycler.sticky.StickyRecyclerAdapter

class AssemblyStickyRecyclerAdapter : AssemblyRecyclerAdapter(), StickyRecyclerAdapter {
    override fun isStickyItemByType(type: Int): Boolean {
        return getItemFactoryByViewType(type) is StickyItemFactory
    }

    interface StickyItemFactory
}