package com.github.panpf.assemblyadapter3.compat.internal

import com.github.panpf.assemblyadapter3.compat.*
import java.util.*

open class CompatItemManager(private val callback: Callback) {

    val headerItemManager = CompatFixedItemManager()
    val itemFactoryList = ArrayList<CompatAssemblyItemFactory<*>>()
    val footerItemManager = CompatFixedItemManager()
    var moreFixedItem: CompatMoreFixedItem? = null
        private set

    fun addItemFactory(itemFactory: CompatAssemblyItemFactory<*>, adapter: CompatAssemblyAdapter) {
        itemFactory.attachToAdapter(adapter)
        itemFactoryList.add(itemFactory)
    }

    fun <DATA> addHeaderItem(
        fixedItem: CompatFixedItem<DATA>,
        adapter: CompatAssemblyAdapter
    ): CompatFixedItem<DATA> {
        headerItemManager.add(fixedItem)
        fixedItem.itemFactory.attachToAdapter(adapter)
        return fixedItem
    }

    fun <DATA> addHeaderItem(
        itemFactory: CompatAssemblyItemFactory<DATA>,
        data: DATA?,
        adapter: CompatAssemblyAdapter
    ): CompatFixedItem<DATA> {
        return addHeaderItem(CompatFixedItem(itemFactory, data), adapter)
    }

    fun <DATA> addHeaderItem(
        itemFactory: CompatAssemblyItemFactory<DATA>,
        adapter: CompatAssemblyAdapter
    ): CompatFixedItem<DATA> {
        return addHeaderItem(CompatFixedItem(itemFactory, null), adapter)
    }

    fun <DATA> addFooterItem(
        fixedItem: CompatFixedItem<DATA>,
        adapter: CompatAssemblyAdapter
    ): CompatFixedItem<DATA> {
        footerItemManager.add(fixedItem)
        fixedItem.itemFactory.attachToAdapter(adapter)
        return fixedItem
    }

    fun <DATA> addFooterItem(
        itemFactory: CompatAssemblyItemFactory<DATA>,
        data: DATA?,
        adapter: CompatAssemblyAdapter
    ): CompatFixedItem<DATA> {
        return addFooterItem(CompatFixedItem(itemFactory, data), adapter)
    }

    fun <DATA> addFooterItem(
        itemFactory: CompatAssemblyItemFactory<DATA>,
        adapter: CompatAssemblyAdapter
    ): CompatFixedItem<DATA> {
        return addFooterItem(CompatFixedItem(itemFactory, null), adapter)
    }

    fun setMoreItem(
        fixedItem: CompatMoreFixedItem,
        adapter: CompatAssemblyAdapter
    ): CompatMoreFixedItem {
        fixedItem.itemFactory.attachToAdapter(adapter)
        moreFixedItem = fixedItem
        return fixedItem
    }

    fun setMoreItem(
        itemFactory: CompatAssemblyMoreItemFactory,
        adapter: CompatAssemblyAdapter
    ): CompatMoreFixedItem {
        return setMoreItem(CompatMoreFixedItem(itemFactory), adapter)
    }

    fun hasMoreFooter(): Boolean {
        return moreFixedItem != null && moreFixedItem!!.isEnabled
    }

    fun getItemFactoryByPosition(position: Int): CompatAssemblyItemFactory<*> {
        val headerItemCount = headerItemManager.availableCount
        val headerStartPosition = 0
        val headerEndPosition = headerItemCount - 1

        // header
        @Suppress("ConvertTwoComparisonsToRangeCheck")
        if (position >= headerStartPosition && position <= headerEndPosition && headerItemCount > 0) {
            return headerItemManager.getItemInAvailableList(position).itemFactory
        }

        // data
        val dataCount = callback.getDataCount()
        val dataStartPosition = headerEndPosition + 1
        val dataEndPosition = headerEndPosition + dataCount
        @Suppress("ConvertTwoComparisonsToRangeCheck")
        if (position >= dataStartPosition && position <= dataEndPosition && dataCount > 0) {
            val positionInDataList = position - headerItemCount
            val dataObject = callback.getData(positionInDataList)
            for (itemFactory in itemFactoryList) {
                if (itemFactory.match(dataObject)) {
                    return itemFactory
                }
            }
            throw IllegalStateException(
                String.format(
                    "Didn't find suitable AssemblyItemFactory. positionInDataList=%d, dataObject=%s",
                    positionInDataList, dataObject?.toString()
                )
            )
        }

        // footer
        val footerItemCount = footerItemManager.availableCount
        val footerStartPosition = dataEndPosition + 1
        val footerEndPosition = dataEndPosition + footerItemCount
        @Suppress("ConvertTwoComparisonsToRangeCheck")
        if (position >= footerStartPosition && position <= footerEndPosition && footerItemCount > 0) {
            val positionInFooterList = position - headerItemCount - dataCount
            return footerItemManager.getItemInAvailableList(positionInFooterList).itemFactory
        }

        // more footer
        if (hasMoreFooter()) {
            val morePosition =
                headerItemCount + dataCount + footerItemCount + (if (dataCount > 0) 1 else 0) - 1
            if (moreFixedItem != null && dataCount > 0 && position == morePosition) {
                return moreFixedItem!!.itemFactory
            }
        }
        throw IllegalStateException("Not found AssemblyItemFactory by position: $position")
    }

    fun getItemDataByPosition(position: Int): Any? {
        // header
        val headerItemCount = headerItemManager.availableCount
        val headerStartPosition = 0
        val headerEndPosition = headerItemCount - 1
        @Suppress("ConvertTwoComparisonsToRangeCheck")
        if (position >= headerStartPosition && position <= headerEndPosition && headerItemCount > 0) {
            return headerItemManager.getItemInAvailableList(position).data
        }

        // body
        val dataCount = callback.getDataCount()
        val dataStartPosition = headerEndPosition + 1
        val dataEndPosition = headerEndPosition + dataCount
        @Suppress("ConvertTwoComparisonsToRangeCheck")
        if (position >= dataStartPosition && position <= dataEndPosition && dataCount > 0) {
            val positionInDataList = position - headerItemCount
            return callback.getData(positionInDataList)
        }

        // footer
        val footerItemCount = footerItemManager.availableCount
        val footerStartPosition = dataEndPosition + 1
        val footerEndPosition = dataEndPosition + footerItemCount
        @Suppress("ConvertTwoComparisonsToRangeCheck")
        if (position >= footerStartPosition && position <= footerEndPosition && footerItemCount > 0) {
            val positionInFooterList = position - headerItemCount - dataCount
            return footerItemManager.getItemInAvailableList(positionInFooterList).data
        }

        // more footer
        if (hasMoreFooter()) {
            val morePosition =
                headerItemCount + dataCount + footerItemCount + (if (dataCount > 0) 1 else 0) - 1
            if (dataCount > 0 && position == morePosition) {
                return if (moreFixedItem != null) moreFixedItem!!.data else null
            }
        }
        throw IllegalArgumentException("Not found item data by position: $position")
    }

    fun getPositionInPart(position: Int): Int {
        // header
        val headerItemCount = headerItemManager.availableCount
        val headerStartPosition = 0
        val headerEndPosition = headerItemCount - 1
        @Suppress("ConvertTwoComparisonsToRangeCheck")
        if (position >= headerStartPosition && position <= headerEndPosition && headerItemCount > 0) {
            return position
        }

        // body
        val dataCount = callback.getDataCount()
        val dataStartPosition = headerEndPosition + 1
        val dataEndPosition = headerEndPosition + dataCount
        @Suppress("ConvertTwoComparisonsToRangeCheck")
        if (position >= dataStartPosition && position <= dataEndPosition && dataCount > 0) {
            return position - headerItemCount
        }

        // footer
        val footerItemCount = footerItemManager.availableCount
        val footerStartPosition = dataEndPosition + 1
        val footerEndPosition = dataEndPosition + footerItemCount
        @Suppress("ConvertTwoComparisonsToRangeCheck")
        if (position >= footerStartPosition && position <= footerEndPosition && footerItemCount > 0) {
            return position - headerItemCount - dataCount
        }

        // more footer
        if (hasMoreFooter()) {
            val morePosition =
                headerItemCount + dataCount + footerItemCount + (if (dataCount > 0) 1 else 0) - 1
            if (dataCount > 0 && position == morePosition) {
                return 0
            }
        }
        throw IllegalArgumentException("Illegal position: $position")
    }

    fun isHeaderItem(position: Int): Boolean {
        val headerItemCount = headerItemManager.availableCount
        val headerStartPosition = 0
        val headerEndPosition = headerItemCount - 1
        @Suppress("ConvertTwoComparisonsToRangeCheck")
        return position >= headerStartPosition && position <= headerEndPosition && headerItemCount > 0
    }

    fun isBodyItem(position: Int): Boolean {
        val headerItemCount = headerItemManager.availableCount
        val headerEndPosition = headerItemCount - 1
        val dataCount = callback.getDataCount()
        val dataStartPosition = headerEndPosition + 1
        val dataEndPosition = headerEndPosition + dataCount
        @Suppress("ConvertTwoComparisonsToRangeCheck")
        return position >= dataStartPosition && position <= dataEndPosition && dataCount > 0
    }

    fun isFooterItem(position: Int): Boolean {
        val headerItemCount = headerItemManager.availableCount
        val headerEndPosition = headerItemCount - 1
        val dataCount = callback.getDataCount()
        val dataEndPosition = headerEndPosition + dataCount
        val footerItemCount = footerItemManager.availableCount
        val footerStartPosition = dataEndPosition + 1
        val footerEndPosition = dataEndPosition + footerItemCount
        @Suppress("ConvertTwoComparisonsToRangeCheck")
        return position >= footerStartPosition && position <= footerEndPosition && footerItemCount > 0
    }

    fun isMoreFooterItem(position: Int): Boolean {
        return if (hasMoreFooter()) {
            val headerItemCount = headerItemManager.availableCount
            val dataCount = callback.getDataCount()
            val footerItemCount = footerItemManager.availableCount
            val morePosition =
                headerItemCount + dataCount + footerItemCount + (if (dataCount > 0) 1 else 0) - 1
            dataCount > 0 && position == morePosition
        } else {
            false
        }
    }

    interface Callback {
        fun getDataCount(): Int
        fun getData(position: Int): Any?
    }
}