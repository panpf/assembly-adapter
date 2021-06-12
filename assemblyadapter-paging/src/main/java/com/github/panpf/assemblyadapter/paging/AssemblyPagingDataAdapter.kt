package com.github.panpf.assemblyadapter.paging

import android.view.View
import android.view.ViewGroup
import androidx.paging.*
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.github.panpf.assemblyadapter.*
import com.github.panpf.assemblyadapter.internal.AssemblyRecyclerItem
import com.github.panpf.assemblyadapter.internal.ItemManager
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import java.util.*

open class AssemblyPagingDataAdapter<DATA : Any> @JvmOverloads constructor(
    itemFactoryList: List<ItemFactory<*>>,
    diffCallback: DiffUtil.ItemCallback<DATA>,
    mainDispatcher: CoroutineDispatcher = Dispatchers.Main,
    workerDispatcher: CoroutineDispatcher = Dispatchers.Default,
) : PagingDataAdapter<DATA, RecyclerView.ViewHolder>(
    diffCallback, mainDispatcher, workerDispatcher
), GridLayoutItemSpanAdapter {

    private val itemManager = ItemManager(itemFactoryList)
    private var gridLayoutItemSpanMap: MutableMap<Class<out ItemFactory<*>>, ItemSpan>? = null

    override fun getItemViewType(position: Int): Int {
        return itemManager.getItemTypeByData(peek(position))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val itemFactory = itemManager.getItemFactoryByItemType(viewType)
        val item = itemFactory.dispatchCreateItem(parent)
        val recyclerItem: AssemblyRecyclerItem<*> = AssemblyRecyclerItem(item)
        applyGridLayoutItemSpan(parent, itemFactory, recyclerItem)
        return recyclerItem
    }

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        if (viewHolder is AssemblyRecyclerItem<*>) {
            @Suppress("UNCHECKED_CAST")
            (viewHolder as AssemblyRecyclerItem<Any?>).dispatchBindData(position, getItem(position))
        }
    }

    fun getItemFactoryByItemType(itemType: Int): ItemFactory<*> {
        return itemManager.getItemFactoryByItemType(itemType)
    }

    override fun setGridLayoutItemSpan(
        itemFactoryClass: Class<out ItemFactory<*>>, itemSpan: ItemSpan
    ): AssemblyPagingDataAdapter<DATA> {
        val gridLayoutItemSpanMap =
            (gridLayoutItemSpanMap ?: (HashMap<Class<out ItemFactory<*>>, ItemSpan>().apply {
                this@AssemblyPagingDataAdapter.gridLayoutItemSpanMap = this
            }))
        gridLayoutItemSpanMap[itemFactoryClass] = itemSpan
        return this
    }

    override fun setGridLayoutItemSpanMap(
        itemSpanMap: Map<Class<out ItemFactory<*>>, ItemSpan>?
    ): AssemblyPagingDataAdapter<DATA> {
        val gridLayoutItemSpanMap =
            (gridLayoutItemSpanMap ?: (HashMap<Class<out ItemFactory<*>>, ItemSpan>().apply {
                this@AssemblyPagingDataAdapter.gridLayoutItemSpanMap = this
            }))
        gridLayoutItemSpanMap.clear()
        if (itemSpanMap != null) {
            gridLayoutItemSpanMap.putAll(itemSpanMap)
        }
        return this
    }

    override fun getGridLayoutItemSpanMap(): Map<Class<out ItemFactory<*>>, ItemSpan>? {
        return gridLayoutItemSpanMap
    }

    private fun applyGridLayoutItemSpan(
        parent: ViewGroup,
        recyclerItemFactory: ItemFactory<*>,
        recyclerItem: AssemblyRecyclerItem<*>
    ) {
        val gridLayoutItemSpanMap = gridLayoutItemSpanMap
        if (gridLayoutItemSpanMap?.isNotEmpty() == true && parent is RecyclerView) {
            val layoutManager = parent.layoutManager
            if (layoutManager is AssemblyGridLayoutManager) {
                // No need to do
            } else if (layoutManager is AssemblyStaggeredGridLayoutManager) {
                val itemSpan = gridLayoutItemSpanMap[recyclerItemFactory.javaClass]
                if (itemSpan != null && itemSpan.span < 0) {
                    val itemView: View = recyclerItem.getItemView()
                    val layoutParams = itemView.layoutParams
                    if (layoutParams is StaggeredGridLayoutManager.LayoutParams) {
                        layoutParams.isFullSpan = true
                        itemView.layoutParams = layoutParams
                    }
                }
            } else {
                throw IllegalArgumentException("Since itemSpan is set, the layoutManager of RecyclerView must be AssemblyGridLayoutManager or AssemblyStaggeredGridLayoutManager")
            }
        }
    }

    override fun getItemFactoryByPosition(position: Int): ItemFactory<*> {
        return getItemFactoryByItemType(itemManager.getItemTypeByData(peek(position)))
    }
}