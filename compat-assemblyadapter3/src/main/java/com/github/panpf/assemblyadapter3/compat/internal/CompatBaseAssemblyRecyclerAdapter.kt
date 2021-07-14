/*
 * Copyright (C) 2017 Peng fei Pan <sky@panpf.me>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.panpf.assemblyadapter3.compat.internal

import android.view.ViewGroup
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.RecyclerView
import com.github.panpf.assemblyadapter.ItemFactory
import com.github.panpf.assemblyadapter.recycler.AssemblySingleDataRecyclerAdapter
import com.github.panpf.assemblyadapter3.compat.*
import com.github.panpf.assemblyadapter3.compat.*
import com.github.panpf.assemblyadapter3.compat.internal.*
import com.github.panpf.assemblyadapter3.compat.CompatMoreFixedItem
import java.util.*

abstract class CompatBaseAssemblyRecyclerAdapter<T> :
    RecyclerView.Adapter<RecyclerView.ViewHolder>(), CompatAssemblyAdapter,
    CompatSpanSizeAdapter {

    protected val itemManager = CompatItemManager(object : CompatItemManager.Callback {
        override fun getDataCount(): Int {
            return this@CompatBaseAssemblyRecyclerAdapter.dataCount
        }

        override fun getData(position: Int): Any? {
            return this@CompatBaseAssemblyRecyclerAdapter.getData(position)
        }
    })
    protected var concatAdapter: ConcatAdapter? = null
    private val adapterDataObserverWrapper = AdapterDataObserverWrapper(this)

    protected fun tryResetConcatAdapter() {
        val oldConcatAdapter = this.concatAdapter
        if (oldConcatAdapter != null) {
            oldConcatAdapter.unregisterAdapterDataObserver(adapterDataObserverWrapper)
            this.concatAdapter = createConcatAdapter().apply {
                registerAdapterDataObserver(adapterDataObserverWrapper)
            }
            notifyDataSetChanged()
        }
    }

    private fun createConcatAdapter(): ConcatAdapter {
        val hasStableIds = hasStableIds()
        val strategy = stateRestorationPolicy

        val headerAdapters = itemManager.headerItemManager.itemList.map { fixedItem ->
            val itemFactoryCompat = CompatItemFactory(fixedItem.itemFactory)
            AssemblySingleDataRecyclerAdapter(itemFactoryCompat).apply {
                data = if (fixedItem.isEnabled) fixedItem.data else null
                fixedItem.callback = CompatFixedItem.Callback {
                    data = if (fixedItem.isEnabled) fixedItem.data else null
                }
            }
        }

        val footerAdapters = itemManager.footerItemManager.itemList.map { fixedItem ->
            val itemFactoryCompat = CompatItemFactory(fixedItem.itemFactory)
            AssemblySingleDataRecyclerAdapter(itemFactoryCompat).apply {
                data = if (fixedItem.isEnabled) fixedItem.data else null
                fixedItem.callback = CompatFixedItem.Callback {
                    data = if (fixedItem.isEnabled) fixedItem.data else null
                }
            }
        }

        val bodyItemFactoryList = itemManager.itemFactoryList.map { itemFactory ->
            CompatItemFactory(itemFactory)
        }
        val bodyAdapter = createBodyAdapter(bodyItemFactoryList)

        val moreFixedItem = itemManager.moreFixedItem
        val moreAdapter = if (moreFixedItem != null) {
            val moreItemFactoryCompat = CompatItemFactory(moreFixedItem.itemFactory)
            AssemblySingleDataRecyclerAdapter(moreItemFactoryCompat).apply {
                data =
                    if (moreFixedItem.isEnabled && bodyAdapter.itemCount > 0) moreFixedItem.data else null
                moreFixedItem.callback = CompatMoreFixedItem.Callback {
                    data =
                        if (moreFixedItem.isEnabled && bodyAdapter.itemCount > 0) moreFixedItem.data else null
                }
                bodyAdapter.registerAdapterDataObserver(AnyAdapterDataObserver {
                    data = if (moreFixedItem.isEnabled && bodyAdapter.itemCount > 0) {
                        moreFixedItem.data
                    } else null
                })
            }
        } else {
            null
        }

        val adapters = ArrayList<RecyclerView.Adapter<*>>().apply {
            addAll(headerAdapters)
            add(bodyAdapter)
            addAll(footerAdapters)
            if (moreAdapter != null) {
                add(moreAdapter)
            }
        }

        adapters.firstOrNull()?.stateRestorationPolicy = strategy

        val config = ConcatAdapter.Config.Builder().setStableIdMode(
            if (hasStableIds) {
                ConcatAdapter.Config.StableIdMode.ISOLATED_STABLE_IDS
            } else {
                ConcatAdapter.Config.StableIdMode.NO_STABLE_IDS
            }
        ).build()
        return ConcatAdapter(config, adapters)
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        if (concatAdapter == null) {
            concatAdapter = createConcatAdapter().apply {
                registerAdapterDataObserver(adapterDataObserverWrapper)
            }
        }
    }

    protected abstract fun createBodyAdapter(bodyItemFactoryList: List<ItemFactory<*>>): RecyclerView.Adapter<*>

    override fun getItemCount(): Int {
        return concatAdapter?.itemCount
            ?: throw IllegalStateException("Please call this method after setAdapter")
    }

    override fun getItemId(position: Int): Long {
        return concatAdapter?.getItemId(position)
            ?: throw IllegalStateException("Please call this method after setAdapter")
    }

    override fun getItemViewType(position: Int): Int {
        return concatAdapter?.getItemViewType(position)
            ?: throw IllegalStateException("Please call this method after setAdapter")
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return concatAdapter?.onCreateViewHolder(parent, viewType)
            ?: throw IllegalStateException("Please call this method after setAdapter")
    }

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        return concatAdapter?.onBindViewHolder(viewHolder, position) ?: throw IllegalStateException(
            "Please call this method after setAdapter"
        )
    }


    override fun <DATA> addItemFactory(itemFactory: CompatAssemblyItemFactory<DATA>) {
        require(concatAdapter == null) { "item factory list locked" }
        itemManager.addItemFactory(itemFactory, this)
    }

    override val itemFactoryList: List<CompatAssemblyItemFactory<*>>
        get() = itemManager.itemFactoryList


    override fun <DATA> addHeaderItem(fixedItem: CompatFixedItem<DATA>): CompatFixedItem<DATA> {
        require(concatAdapter == null) { "item factory list locked" }
        return itemManager.addHeaderItem(fixedItem, this)
    }

    override fun <DATA> addHeaderItem(
        itemFactory: CompatAssemblyItemFactory<DATA>,
        data: DATA?
    ): CompatFixedItem<DATA> {
        require(concatAdapter == null) { "item factory list locked" }
        return itemManager.addHeaderItem(itemFactory, data, this)
    }

    override fun <DATA> addHeaderItem(itemFactory: CompatAssemblyItemFactory<DATA>): CompatFixedItem<DATA> {
        require(concatAdapter == null) { "item factory list locked" }
        return itemManager.addHeaderItem(itemFactory, this)
    }

    override fun <DATA> getHeaderItemByFactoryClass(
        clazz: Class<out CompatAssemblyItemFactory<DATA>?>,
        number: Int
    ): CompatFixedItem<DATA> {
        return itemManager.headerItemManager.getItemByFactoryClass(clazz, number)
    }

    override fun <DATA> getHeaderItemByFactoryClass(clazz: Class<out CompatAssemblyItemFactory<DATA>?>): CompatFixedItem<DATA> {
        return itemManager.headerItemManager.getItemByFactoryClass(clazz)
    }

    override fun getHeaderItem(positionInHeaderItemList: Int): CompatFixedItem<*> {
        return itemManager.headerItemManager.getItem(positionInHeaderItemList)
    }

    override fun getHeaderItemData(positionInHeaderItemList: Int): Any? {
        return itemManager.headerItemManager.getItem(positionInHeaderItemList).data
    }

    override fun setHeaderItemData(positionInHeaderItemList: Int, data: Any?) {
        itemManager.headerItemManager.setItemData(positionInHeaderItemList, data)
    }

    override fun isHeaderItemEnabled(positionInHeaderItemList: Int): Boolean {
        return itemManager.headerItemManager.isItemEnabled(positionInHeaderItemList)
    }

    override fun setHeaderItemEnabled(positionInHeaderItemList: Int, enabled: Boolean) {
        itemManager.headerItemManager.setItemEnabled(positionInHeaderItemList, enabled)
    }

    override val headerCount: Int
        get() = itemManager.headerItemManager.availableCount

    override fun getHeaderData(positionInHeaderList: Int): Any? {
        return itemManager.headerItemManager.getItemInAvailableList(positionInHeaderList).data
    }


    override fun <DATA> addFooterItem(fixedItem: CompatFixedItem<DATA>): CompatFixedItem<DATA> {
        require(concatAdapter == null) { "item factory list locked" }
        return itemManager.addFooterItem(fixedItem, this)
    }

    override fun <DATA> addFooterItem(
        itemFactory: CompatAssemblyItemFactory<DATA>,
        data: DATA?
    ): CompatFixedItem<DATA> {
        require(concatAdapter == null) { "item factory list locked" }
        return itemManager.addFooterItem(itemFactory, data, this)
    }

    override fun <DATA> addFooterItem(itemFactory: CompatAssemblyItemFactory<DATA>): CompatFixedItem<DATA> {
        require(concatAdapter == null) { "item factory list locked" }
        return itemManager.addFooterItem(itemFactory, this)
    }

    override fun <DATA> getFooterItemByFactoryClass(
        clazz: Class<out CompatAssemblyItemFactory<DATA>?>,
        number: Int
    ): CompatFixedItem<DATA> {
        return itemManager.footerItemManager.getItemByFactoryClass(clazz, number)
    }

    override fun <DATA> getFooterItemByFactoryClass(clazz: Class<out CompatAssemblyItemFactory<DATA>?>): CompatFixedItem<DATA> {
        return itemManager.footerItemManager.getItemByFactoryClass(clazz)
    }

    override fun getFooterItem(positionInFooterItemList: Int): CompatFixedItem<*> {
        return itemManager.footerItemManager.getItem(positionInFooterItemList)
    }

    override fun getFooterItemData(positionInFooterItemList: Int): Any? {
        return itemManager.footerItemManager.getItem(positionInFooterItemList).data
    }

    override fun setFooterItemData(positionInFooterItemList: Int, data: Any?) {
        itemManager.footerItemManager.setItemData(positionInFooterItemList, data)
    }

    override fun isFooterItemEnabled(positionInFooterItemList: Int): Boolean {
        return itemManager.footerItemManager.isItemEnabled(positionInFooterItemList)
    }

    override fun setFooterItemEnabled(positionInFooterItemList: Int, enabled: Boolean) {
        itemManager.footerItemManager.setItemEnabled(positionInFooterItemList, enabled)
    }

    override val footerCount: Int
        get() = itemManager.footerItemManager.availableCount

    override fun getFooterData(positionInFooterList: Int): Any? {
        return itemManager.footerItemManager.getItemInAvailableList(positionInFooterList).data
    }


    override fun setMoreItem(itemFactory: CompatAssemblyMoreItemFactory): CompatMoreFixedItem {
        require(concatAdapter == null) { "item factory list locked" }
        return itemManager.setMoreItem(itemFactory, this)
    }

    override fun setMoreItem(moreFixedItem: CompatMoreFixedItem): CompatMoreFixedItem {
        require(concatAdapter == null) { "item factory list locked" }
        return itemManager.setMoreItem(moreFixedItem, this)
    }

    override val moreItem: CompatMoreFixedItem?
        get() = itemManager.moreFixedItem

    override val hasMoreFooter: Boolean
        get() = itemManager.hasMoreFooter()

    override fun setMoreItemEnabled(enabled: Boolean) {
        itemManager.moreFixedItem?.isEnabled = enabled
    }

    override fun loadMoreFinished(end: Boolean) {
        itemManager.moreFixedItem?.loadMoreFinished(end)
    }

    override fun loadMoreFailed() {
        itemManager.moreFixedItem?.loadMoreFailed()
    }


    abstract override val dataCount: Int

    abstract override fun getData(positionInDataList: Int): T?


    override fun getItem(position: Int): Any? {
        return itemManager.getItemDataByPosition(position)
    }

    override fun getPositionInPart(position: Int): Int {
        return itemManager.getPositionInPart(position)
    }

    override fun isHeaderItem(position: Int): Boolean {
        return itemManager.isHeaderItem(position)
    }

    override fun isBodyItem(position: Int): Boolean {
        return itemManager.isBodyItem(position)
    }

    override fun isFooterItem(position: Int): Boolean {
        return itemManager.isFooterItem(position)
    }

    override fun isMoreFooterItem(position: Int): Boolean {
        return itemManager.isMoreFooterItem(position)
    }

    override fun getItemFactoryByPosition(position: Int): CompatAssemblyItemFactory<*> {
        return itemManager.getItemFactoryByPosition(position)
    }

    override fun getSpanSizeByPosition(position: Int): Int {
        return itemManager.getItemFactoryByPosition(position).spanSize
    }

    private class AdapterDataObserverWrapper(private val adapter: RecyclerView.Adapter<*>) :
        RecyclerView.AdapterDataObserver() {

        override fun onChanged() {
            super.onChanged()
            adapter.notifyDataSetChanged()
        }

        override fun onItemRangeChanged(positionStart: Int, itemCount: Int) {
            super.onItemRangeChanged(positionStart, itemCount)
            adapter.notifyItemRangeChanged(positionStart, itemCount)
        }

        override fun onItemRangeChanged(positionStart: Int, itemCount: Int, payload: Any?) {
            super.onItemRangeChanged(positionStart, itemCount, payload)
            adapter.notifyItemRangeChanged(positionStart, itemCount, payload)
        }

        override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
            super.onItemRangeInserted(positionStart, itemCount)
            adapter.notifyItemRangeInserted(positionStart, itemCount)
        }

        override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {
            super.onItemRangeRemoved(positionStart, itemCount)
            adapter.notifyItemRangeRemoved(positionStart, itemCount)
        }

        override fun onItemRangeMoved(fromPosition: Int, toPosition: Int, itemCount: Int) {
            super.onItemRangeMoved(fromPosition, toPosition, itemCount)
            adapter.notifyItemMoved(fromPosition, toPosition)
        }
    }

    private class AnyAdapterDataObserver(val onAnyChanged: () -> Unit) :
        RecyclerView.AdapterDataObserver() {
        override fun onChanged() {
            super.onChanged()
            onAnyChanged()
        }

        override fun onItemRangeChanged(positionStart: Int, itemCount: Int) {
            super.onItemRangeChanged(positionStart, itemCount)
            onAnyChanged()
        }

        override fun onItemRangeChanged(positionStart: Int, itemCount: Int, payload: Any?) {
            super.onItemRangeChanged(positionStart, itemCount, payload)
            onAnyChanged()
        }

        override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
            super.onItemRangeInserted(positionStart, itemCount)
            onAnyChanged()
        }

        override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {
            super.onItemRangeRemoved(positionStart, itemCount)
            onAnyChanged()
        }

        override fun onItemRangeMoved(fromPosition: Int, toPosition: Int, itemCount: Int) {
            super.onItemRangeMoved(fromPosition, toPosition, itemCount)
            onAnyChanged()
        }
    }
}