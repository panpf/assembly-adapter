package com.github.panpf.assemblyadapter.recycler.paging

import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.github.panpf.assemblyadapter.AssemblyAdapter
import com.github.panpf.assemblyadapter.AssemblyItem
import com.github.panpf.assemblyadapter.AssemblyItemFactory
import com.github.panpf.assemblyadapter.internal.ItemFactoryStorage
import com.github.panpf.assemblyadapter.recycler.internal.AssemblyItemViewHolderWrapper
import com.github.panpf.assemblyadapter.recycler.internal.FullSpanStaggeredGridLayoutManager
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

open class AssemblyPagingDataAdapter<DATA : Any> @JvmOverloads constructor(
    itemFactoryList: List<AssemblyItemFactory<*>>,
    diffCallback: DiffUtil.ItemCallback<DATA>,
    mainDispatcher: CoroutineDispatcher = Dispatchers.Main,
    workerDispatcher: CoroutineDispatcher = Dispatchers.Default,
) : PagingDataAdapter<DATA, RecyclerView.ViewHolder>(
    diffCallback, mainDispatcher, workerDispatcher
), AssemblyAdapter {

    private val itemFactoryStorage = ItemFactoryStorage(itemFactoryList)

    override fun getItemViewType(position: Int): Int {
        return itemFactoryStorage.getItemTypeByData(peek(position))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val itemFactory = itemFactoryStorage.getItemFactoryByItemType(viewType)
        val item = itemFactory.dispatchCreateItem(parent)
        return AssemblyItemViewHolderWrapper(item).apply {
            val layoutManager =
                (parent.takeIf { it is RecyclerView } as RecyclerView?)?.layoutManager
            if (layoutManager is FullSpanStaggeredGridLayoutManager) {
                layoutManager.setFullSpan(itemView, itemFactory)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is AssemblyItemViewHolderWrapper<*>) {
            @Suppress("UNCHECKED_CAST")
            val item = holder.wrappedItem as AssemblyItem<Any>
            item.dispatchBindData(position, holder.position, getItem(position))
        } else {
            throw IllegalArgumentException("holder must be AssemblyItemViewHolderWrapper")
        }
    }


    override fun getItemFactoryByPosition(position: Int): AssemblyItemFactory<*> {
        return itemFactoryStorage.getItemFactoryByData(peek(position))
    }
}