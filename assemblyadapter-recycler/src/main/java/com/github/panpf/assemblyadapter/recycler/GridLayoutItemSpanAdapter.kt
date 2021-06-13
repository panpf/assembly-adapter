package com.github.panpf.assemblyadapter.recycler

import com.github.panpf.assemblyadapter.ItemFactory

interface GridLayoutItemSpanAdapter {

    fun getItemFactoryByPosition(position: Int): ItemFactory<*>

    fun setGridLayoutItemSpan(
        itemFactoryClass: Class<out ItemFactory<*>>, itemSpan: ItemSpan
    ): GridLayoutItemSpanAdapter

    fun setGridLayoutItemSpanMap(
        itemSpanMap: Map<Class<out ItemFactory<*>>, ItemSpan>?
    ): GridLayoutItemSpanAdapter

    fun getGridLayoutItemSpanMap(): Map<Class<out ItemFactory<*>>, ItemSpan>?
}