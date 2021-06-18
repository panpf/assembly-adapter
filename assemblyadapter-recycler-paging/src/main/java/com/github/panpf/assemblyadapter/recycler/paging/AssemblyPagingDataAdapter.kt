package com.github.panpf.assemblyadapter.recycler.paging

import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.github.panpf.assemblyadapter.AssemblyAdapter
import com.github.panpf.assemblyadapter.ItemFactory
import com.github.panpf.assemblyadapter.internal.ItemManager
import com.github.panpf.assemblyadapter.recycler.internal.FullSpanStaggeredGridLayoutManager
import com.github.panpf.assemblyadapter.recycler.internal.AssemblyRecyclerItem
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

open class AssemblyPagingDataAdapter<DATA : Any> @JvmOverloads constructor(
    itemFactoryList: List<ItemFactory<*>>,
    diffCallback: DiffUtil.ItemCallback<DATA>,
    mainDispatcher: CoroutineDispatcher = Dispatchers.Main,
    workerDispatcher: CoroutineDispatcher = Dispatchers.Default,
) : PagingDataAdapter<DATA, RecyclerView.ViewHolder>(
    diffCallback, mainDispatcher, workerDispatcher
), AssemblyAdapter {

    private val itemManager = ItemManager(itemFactoryList)

    override fun getItemViewType(position: Int): Int {
        return itemManager.getItemTypeByData(peek(position))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val itemFactory = itemManager.getItemFactoryByItemType(viewType)
        val item = itemFactory.dispatchCreateItem(parent)
        return AssemblyRecyclerItem(item).apply {
            val layoutManager =
                (parent.takeIf { it is RecyclerView } as RecyclerView?)?.layoutManager
            if (layoutManager is FullSpanStaggeredGridLayoutManager) {
                layoutManager.setFullSpan(itemView, itemFactory)
            }
        }
    }

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        if (viewHolder is AssemblyRecyclerItem<*>) {
            @Suppress("UNCHECKED_CAST")
            (viewHolder as AssemblyRecyclerItem<Any?>).dispatchBindData(position, getItem(position))
        }
    }


    override fun getItemFactoryByPosition(position: Int): ItemFactory<*> {
        return itemManager.getItemFactoryByData(peek(position))
    }
}