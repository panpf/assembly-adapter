package me.panpf.adapter.sample.adapter

import me.panpf.adapter.AssemblyRecyclerAdapter
import me.panpf.recycler.sticky.StickyRecyclerAdapter

class AssemblyStickyRecyclerAdapter : AssemblyRecyclerAdapter(), StickyRecyclerAdapter {
    override fun isStickyItemByType(type: Int): Boolean {
        if (headerItemList?.find { it.itemFactory.itemType == type && it.itemFactory is StickyItemFactory } != null) {
            return true
        }
        if (itemFactoryList?.find { it.itemType == type && it is StickyItemFactory } != null) {
            return true
        }
        if (footerItemList?.find { it.itemFactory.itemType == type && it.itemFactory is StickyItemFactory } != null) {
            return true
        }
        return false
    }

    interface StickyItemFactory
}