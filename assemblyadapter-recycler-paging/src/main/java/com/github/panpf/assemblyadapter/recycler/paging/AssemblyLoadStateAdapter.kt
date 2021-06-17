package com.github.panpf.assemblyadapter.recycler.paging

import android.view.ViewGroup
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.github.panpf.assemblyadapter.AssemblyAdapter
import com.github.panpf.assemblyadapter.ItemFactory
import com.github.panpf.assemblyadapter.recycler.internal.FullSpanStaggeredGridLayoutManager
import com.github.panpf.assemblyadapter.recycler.internal.AssemblyRecyclerItem

open class AssemblyLoadStateAdapter(
    private val itemFactory: ItemFactory<LoadState>,
) : LoadStateAdapter<RecyclerView.ViewHolder>(), AssemblyAdapter {

    override fun onCreateViewHolder(
        parent: ViewGroup, loadState: LoadState
    ): RecyclerView.ViewHolder {
        val item = itemFactory.dispatchCreateItem(parent)
        return AssemblyRecyclerItem(item).apply {
            val layoutManager =
                (parent.takeIf { it is RecyclerView } as RecyclerView?)?.layoutManager
            if (layoutManager is FullSpanStaggeredGridLayoutManager) {
                layoutManager.setFullSpan(itemView, itemFactory)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, loadState: LoadState) {
        if (holder is AssemblyRecyclerItem<*>) {
            @Suppress("UNCHECKED_CAST")
            (holder as AssemblyRecyclerItem<Any?>).dispatchBindData(0, loadState)
        }
    }


    override fun getItemFactoryByItemType(itemType: Int): ItemFactory<*> {
        return itemFactory
    }

    override fun getItemFactoryByPosition(position: Int): ItemFactory<*> {
        return itemFactory
    }
}