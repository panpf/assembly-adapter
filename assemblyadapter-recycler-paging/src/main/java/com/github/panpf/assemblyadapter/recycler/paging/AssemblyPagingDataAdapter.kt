package com.github.panpf.assemblyadapter.recycler.paging

import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.github.panpf.assemblyadapter.*
import com.github.panpf.assemblyadapter.internal.ItemFactoryStorage
import com.github.panpf.assemblyadapter.recycler.internal.AssemblyItemViewHolderWrapper
import com.github.panpf.assemblyadapter.recycler.internal.FullSpanStaggeredGridLayoutManager
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

open class AssemblyPagingDataAdapter<DATA : Any>(
    itemFactoryList: List<ItemFactory<*>>,
    diffCallback: DiffUtil.ItemCallback<DATA>,
    placeholderItemFactory: PlaceholderItemFactory? = null,
    mainDispatcher: CoroutineDispatcher = Dispatchers.Main,
    workerDispatcher: CoroutineDispatcher = Dispatchers.Default,
) : PagingDataAdapter<DATA, RecyclerView.ViewHolder>(
    diffCallback, mainDispatcher, workerDispatcher
), AssemblyAdapter {

    constructor(
        itemFactoryList: List<ItemFactory<*>>,
        diffCallback: DiffUtil.ItemCallback<DATA>,
        placeholderItemFactory: PlaceholderItemFactory,
    ) : this(
        itemFactoryList,
        diffCallback,
        placeholderItemFactory,
        Dispatchers.Main,
        Dispatchers.Default
    )

    constructor(
        itemFactoryList: List<ItemFactory<*>>,
        diffCallback: DiffUtil.ItemCallback<DATA>,
    ) : this(
        itemFactoryList,
        diffCallback,
        null,
        Dispatchers.Main,
        Dispatchers.Default
    )

    private val itemFactoryStorage = ItemFactoryStorage(
        if (placeholderItemFactory != null) itemFactoryList.plus(placeholderItemFactory) else itemFactoryList
    )

    override fun getItemViewType(position: Int): Int {
        val matchData = peek(position) ?: Placeholder
        return itemFactoryStorage.getItemTypeByData(matchData)
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
            val item = holder.wrappedItem as Item<Any>
            // Here you must use the getItem method to trigger append load
            // And must be placed in this position
            val data = getItem(position)
            if (item is PlaceholderItem) {
                item.dispatchBindData(position, holder.position, Placeholder)
            } else {
                item.dispatchBindData(position, holder.position, data!!)
            }
        } else {
            throw IllegalArgumentException("holder must be AssemblyItemViewHolderWrapper")
        }
    }


    override fun getItemFactoryByPosition(position: Int): ItemFactory<*> {
        val matchData = peek(position) ?: Placeholder
        return itemFactoryStorage.getItemFactoryByData(matchData)
    }


    class Builder<DATA : Any>(
        private val itemFactoryList: List<ItemFactory<*>>,
        private val diffCallback: DiffUtil.ItemCallback<DATA>
    ) {
        private var placeholderItemFactory: PlaceholderItemFactory? = null
        private var mainDispatcher: CoroutineDispatcher = Dispatchers.Main
        private var workerDispatcher: CoroutineDispatcher = Dispatchers.Default

        fun setPlaceholderItemFactory(placeholderItemFactory: PlaceholderItemFactory?) {
            this.placeholderItemFactory = placeholderItemFactory
        }

        fun setMainDispatcher(mainDispatcher: CoroutineDispatcher) {
            this.mainDispatcher = mainDispatcher
        }

        fun setWorkerDispatcher(workerDispatcher: CoroutineDispatcher) {
            this.workerDispatcher = workerDispatcher
        }

        fun build(): AssemblyPagingDataAdapter<DATA> {
            return AssemblyPagingDataAdapter(
                itemFactoryList,
                diffCallback,
                placeholderItemFactory,
                mainDispatcher,
                workerDispatcher
            )
        }
    }
}