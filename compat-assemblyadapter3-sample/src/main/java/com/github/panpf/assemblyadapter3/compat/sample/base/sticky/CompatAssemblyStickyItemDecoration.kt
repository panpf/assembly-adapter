package com.github.panpf.assemblyadapter3.compat.sample.base.sticky

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.github.panpf.assemblyadapter3.compat.CompatAssemblyAdapter
import com.github.panpf.assemblyadapter3.compat.CompatAssemblyItemFactory
import kotlin.reflect.KClass

class CompatAssemblyStickyItemDecoration : StickyItemDecoration {

    private val stickyItemFactoryList: List<Class<out CompatAssemblyItemFactory<*>>>
    private var findItemFactoryClassByPosition: (adapter: RecyclerView.Adapter<*>, position: Int) -> Class<*>? =
        { adapter, position ->
            if (adapter is CompatAssemblyAdapter) {
                adapter.getItemFactoryByPosition(position).javaClass
            } else {
                null
            }
        }

    constructor(
        stickyItemContainer: ViewGroup,
        stickyItemFactoryList: List<KClass<out CompatAssemblyItemFactory<*>>>
    ) : super(stickyItemContainer) {
        this.stickyItemFactoryList = stickyItemFactoryList.map { it.java }
    }

    constructor(
        stickyItemContainer: ViewGroup,
        vararg itemFactoryClass: KClass<out CompatAssemblyItemFactory<*>>
    ) : super(stickyItemContainer) {
        this.stickyItemFactoryList = itemFactoryClass.map { it.java }
    }

    override fun isStickyItemByPositionReal(
        adapter: RecyclerView.Adapter<*>,
        position: Int
    ): Boolean {
        val itemFactoryClass = findItemFactoryClassByPosition(adapter, position)
        return if (itemFactoryClass != null) {
            stickyItemFactoryList.contains(itemFactoryClass)
        } else {
            super.isStickyItemByPositionReal(adapter, position)
        }
    }
}