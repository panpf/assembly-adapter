package com.github.panpf.assemblyadapter.sample.base.sticky

import android.view.ViewGroup
import com.github.panpf.assemblyadapter.AssemblyAdapter
import com.github.panpf.assemblyadapter.ItemFactory
import com.github.panpf.assemblyadapter.sample.base.sticky.StickyRecyclerItemDecoration.Callback
import kotlin.reflect.KClass

class AssemblyStickyRecyclerItemDecoration(
    stickyItemContainer: ViewGroup,
    private val stickyItemFactoryList: List<KClass<out ItemFactory<*>>>
) : StickyRecyclerItemDecoration(stickyItemContainer, Callback { adapter, position ->
    if (adapter is AssemblyAdapter<*>) {
        stickyItemFactoryList.contains(adapter.getItemFactoryByPosition(position)::class)
    } else false
}) {
    constructor(
        stickyItemContainer: ViewGroup,
        vararg itemFactoryClass: KClass<out ItemFactory<*>>
    ) : this(stickyItemContainer, itemFactoryClass.toList())
}