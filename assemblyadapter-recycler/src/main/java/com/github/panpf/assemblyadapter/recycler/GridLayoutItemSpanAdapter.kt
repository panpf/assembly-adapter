package com.github.panpf.assemblyadapter.recycler

import com.github.panpf.assemblyadapter.internal.BaseItemFactory

interface GridLayoutItemSpanAdapter<ITEM_FACTORY : BaseItemFactory> {

    fun getItemSpanByPosition(position: Int): ItemSpan?

    fun getItemSpanByItemType(itemType: Int): ItemSpan?

    fun setGridLayoutItemSpan(
        itemFactoryClass: Class<out ITEM_FACTORY>, itemSpan: ItemSpan
    ): GridLayoutItemSpanAdapter<ITEM_FACTORY>

    fun setGridLayoutItemSpanMap(
        itemSpanMap: Map<Class<out ITEM_FACTORY>, ItemSpan>?
    ): GridLayoutItemSpanAdapter<ITEM_FACTORY>
}