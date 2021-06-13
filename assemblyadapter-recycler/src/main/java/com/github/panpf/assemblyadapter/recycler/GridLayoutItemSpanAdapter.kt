package com.github.panpf.assemblyadapter.recycler

import com.github.panpf.assemblyadapter.internal.BaseItemFactory

interface GridLayoutItemSpanAdapter<ITEM_FACTORY : BaseItemFactory> {

    fun getItemFactoryByPosition(position: Int): ITEM_FACTORY

    fun setGridLayoutItemSpan(
        itemFactoryClass: Class<out ITEM_FACTORY>, itemSpan: ItemSpan
    ): GridLayoutItemSpanAdapter<ITEM_FACTORY>

    fun setGridLayoutItemSpanMap(
        itemSpanMap: Map<Class<out ITEM_FACTORY>, ItemSpan>?
    ): GridLayoutItemSpanAdapter<ITEM_FACTORY>

    fun getGridLayoutItemSpanMap(): Map<Class<out ITEM_FACTORY>, ItemSpan>?
}