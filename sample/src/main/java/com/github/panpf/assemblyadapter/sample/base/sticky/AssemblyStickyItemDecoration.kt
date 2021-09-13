package com.github.panpf.assemblyadapter.sample.base.sticky

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.github.panpf.assemblyadapter.AssemblyAdapter
import com.github.panpf.assemblyadapter.ItemFactory
import kotlin.reflect.KClass

class AssemblyStickyItemDecoration :
    StickyItemDecoration {

    private val stickyItemFactoryList: List<Class<out ItemFactory<out Any>>>
    private var findItemFactoryClassByPosition: (adapter: RecyclerView.Adapter<*>, position: Int) -> Class<*>? =
        { adapter, position ->
            if (adapter is AssemblyAdapter<*, *>) {
                adapter.getItemFactoryByPosition(position).javaClass
            } else {
                null
            }
        }

    constructor(
        stickyItemContainer: ViewGroup,
        stickyItemFactoryList: List<KClass<out ItemFactory<out Any>>>
    ) : super(stickyItemContainer) {
        this.stickyItemFactoryList = stickyItemFactoryList.map { it.java }
    }

    constructor(
        stickyItemContainer: ViewGroup,
        vararg itemFactoryClass: KClass<out ItemFactory<out Any>>
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