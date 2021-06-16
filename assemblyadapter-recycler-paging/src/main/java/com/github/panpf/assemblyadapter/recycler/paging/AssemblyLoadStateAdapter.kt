package com.github.panpf.assemblyadapter.recycler.paging

import android.view.ViewGroup
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.github.panpf.assemblyadapter.ItemFactory
import com.github.panpf.assemblyadapter.internal.BaseItemFactory
import com.github.panpf.assemblyadapter.recycler.AssemblyStaggeredGridLayoutManager
import com.github.panpf.assemblyadapter.recycler.GridLayoutItemSpanAdapter
import com.github.panpf.assemblyadapter.recycler.ItemSpan
import com.github.panpf.assemblyadapter.recycler.internal.AssemblyRecyclerItem

open class AssemblyLoadStateAdapter(
    private val itemFactory: ItemFactory<LoadState>,
    private val itemSpan: ItemSpan = ItemSpan.fullSpan()
) : LoadStateAdapter<RecyclerView.ViewHolder>(), GridLayoutItemSpanAdapter<BaseItemFactory> {

    override fun onCreateViewHolder(
        parent: ViewGroup, loadState: LoadState
    ): RecyclerView.ViewHolder {
        val item = itemFactory.dispatchCreateItem(parent)
        return AssemblyRecyclerItem(item).apply {
            applyGridLayoutItemSpan(parent, this, getStateViewType(loadState))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, loadState: LoadState) {
        if (holder is AssemblyRecyclerItem<*>) {
            @Suppress("UNCHECKED_CAST")
            (holder as AssemblyRecyclerItem<Any?>).dispatchBindData(0, loadState)
        }
    }

    override fun getItemSpanByPosition(position: Int): ItemSpan? {
        return itemSpan
    }

    override fun getItemSpanByItemType(itemType: Int): ItemSpan? {
        return itemSpan
    }

    override fun setGridLayoutItemSpan(
        itemFactoryClass: Class<out BaseItemFactory>,
        itemSpan: ItemSpan
    ): GridLayoutItemSpanAdapter<BaseItemFactory> {
        throw UnsupportedOperationException("MyLoadStateAdapter does not support setGridLayoutItemSpan() method")
    }

    override fun setGridLayoutItemSpanMap(
        itemSpanMap: Map<Class<out BaseItemFactory>, ItemSpan>?
    ): GridLayoutItemSpanAdapter<BaseItemFactory> {
        throw UnsupportedOperationException("MyLoadStateAdapter does not support setGridLayoutItemSpanMap() method")
    }

    private fun applyGridLayoutItemSpan(
        parent: ViewGroup, recyclerItem: AssemblyRecyclerItem<*>, itemType: Int,
    ) {
        if (parent is RecyclerView) {
            val layoutManager = parent.layoutManager
            if (layoutManager is AssemblyStaggeredGridLayoutManager) {
                layoutManager.setSpanSize(this, recyclerItem, itemType)
            }
        }
    }


    fun getItemFactoryByItemType(itemType: Int): ItemFactory<*> {
        return itemFactory
    }

    fun getItemFactoryByPosition(position: Int): ItemFactory<*> {
        return itemFactory
    }
}