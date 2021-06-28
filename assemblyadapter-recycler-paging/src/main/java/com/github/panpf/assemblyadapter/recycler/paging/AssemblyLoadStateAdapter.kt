package com.github.panpf.assemblyadapter.recycler.paging

import android.view.ViewGroup
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.github.panpf.assemblyadapter.AssemblyAdapter
import com.github.panpf.assemblyadapter.ItemFactory
import com.github.panpf.assemblyadapter.recycler.internal.AssemblyItemViewHolderWrapper
import com.github.panpf.assemblyadapter.recycler.FullSpanStaggeredGridLayoutManager

open class AssemblyLoadStateAdapter(
    private val itemFactory: ItemFactory<LoadState>,
    private val alwaysShowWhenEndOfPaginationReached: Boolean = false,
) : LoadStateAdapter<RecyclerView.ViewHolder>(), AssemblyAdapter {

    override fun onCreateViewHolder(
        parent: ViewGroup, loadState: LoadState
    ): RecyclerView.ViewHolder {
        val item = itemFactory.dispatchCreateItem(parent)
        return AssemblyItemViewHolderWrapper(item).apply {
            val layoutManager =
                (parent.takeIf { it is RecyclerView } as RecyclerView?)?.layoutManager
            if (layoutManager is FullSpanStaggeredGridLayoutManager) {
                layoutManager.setFullSpan(itemView, itemFactory)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, loadState: LoadState) {
        if (holder is AssemblyItemViewHolderWrapper<*>) {
            @Suppress("UNCHECKED_CAST")
            val item = holder.wrappedItem as ItemFactory.Item<Any>
            item.dispatchBindData(0, holder.position, loadState)
        } else {
            throw IllegalArgumentException("holder must be AssemblyItemViewHolderWrapper")
        }
    }

    override fun displayLoadStateAsItem(loadState: LoadState): Boolean {
        return loadState is LoadState.Loading
                || loadState is LoadState.Error
                || (alwaysShowWhenEndOfPaginationReached && loadState is LoadState.NotLoading && loadState.endOfPaginationReached)
    }


    override fun getItemFactoryByPosition(position: Int): ItemFactory<*> {
        return itemFactory
    }
}