package com.github.panpf.assemblyadapter3.compat.sample.base.sticky

import android.view.ViewGroup
import com.github.panpf.assemblyadapter3.compat.CompatAssemblyAdapter
import com.github.panpf.assemblyadapter3.compat.CompatAssemblyItemFactory
import com.github.panpf.assemblyadapter3.compat.sample.base.sticky.StickyRecyclerItemDecoration.Callback
import kotlin.reflect.KClass

class CompatAssemblyStickyRecyclerItemDecoration(
    stickyItemContainer: ViewGroup,
    private val stickyItemFactoryList: List<KClass<out CompatAssemblyItemFactory<*>>>
) : StickyRecyclerItemDecoration(
    stickyItemContainer,
    Callback { adapter, position ->
        if (adapter is CompatAssemblyAdapter) {
            stickyItemFactoryList.contains(adapter.getItemFactoryByPosition(position)::class)
        } else {
            false
        }
    }
)