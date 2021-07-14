package com.github.panpf.assemblyadapter3.compat

import java.util.*

/**
 * Support to combine multiple items, support head, tail and load more
 */
interface CompatAssemblyAdapter {

    fun <DATA> addItemFactory(itemFactory: CompatAssemblyItemFactory<DATA>)

    val itemFactoryList: List<CompatAssemblyItemFactory<*>>


    fun <DATA> addHeaderItem(fixedItem: CompatFixedItem<DATA>): CompatFixedItem<*>

    fun <DATA> addHeaderItem(
        itemFactory: CompatAssemblyItemFactory<DATA>,
        data: DATA?
    ): CompatFixedItem<DATA>

    fun <DATA> addHeaderItem(itemFactory: CompatAssemblyItemFactory<DATA>): CompatFixedItem<DATA>

    fun <DATA> getHeaderItemByFactoryClass(
        clazz: Class<out CompatAssemblyItemFactory<DATA>?>, number: Int
    ): CompatFixedItem<DATA>

    fun <DATA> getHeaderItemByFactoryClass(clazz: Class<out CompatAssemblyItemFactory<DATA>?>): CompatFixedItem<DATA>

    fun getHeaderItem(positionInHeaderItemList: Int): CompatFixedItem<*>

    fun getHeaderItemData(positionInHeaderItemList: Int): Any?

    fun setHeaderItemData(positionInHeaderItemList: Int, data: Any?)

    fun isHeaderItemEnabled(positionInHeaderItemList: Int): Boolean

    fun setHeaderItemEnabled(positionInHeaderItemList: Int, enabled: Boolean)

    val headerCount: Int

    fun getHeaderData(positionInHeaderList: Int): Any?


    fun <DATA> addFooterItem(fixedItem: CompatFixedItem<DATA>): CompatFixedItem<DATA>

    fun <DATA> addFooterItem(
        itemFactory: CompatAssemblyItemFactory<DATA>,
        data: DATA?
    ): CompatFixedItem<DATA>

    fun <DATA> addFooterItem(itemFactory: CompatAssemblyItemFactory<DATA>): CompatFixedItem<DATA>

    fun <DATA> getFooterItemByFactoryClass(
        clazz: Class<out CompatAssemblyItemFactory<DATA>?>, number: Int
    ): CompatFixedItem<DATA>

    fun <DATA> getFooterItemByFactoryClass(clazz: Class<out CompatAssemblyItemFactory<DATA>?>): CompatFixedItem<DATA>

    fun getFooterItem(positionInFooterItemList: Int): CompatFixedItem<*>

    fun getFooterItemData(positionInFooterItemList: Int): Any?

    fun setFooterItemData(positionInFooterItemList: Int, data: Any?)

    fun isFooterItemEnabled(positionInFooterItemList: Int): Boolean

    fun setFooterItemEnabled(positionInFooterItemList: Int, enabled: Boolean)

    val footerCount: Int

    fun getFooterData(positionInFooterList: Int): Any?


    fun setMoreItem(itemFactory: CompatAssemblyMoreItemFactory): CompatMoreFixedItem

    fun setMoreItem(moreFixedItem: CompatMoreFixedItem): CompatMoreFixedItem

    val hasMoreFooter: Boolean

    val moreItem: CompatMoreFixedItem?

    fun setMoreItemEnabled(enabled: Boolean)

    fun loadMoreFinished(end: Boolean)

    fun loadMoreFailed()


    fun addAll(collection: Collection<Any?>?)

    fun addAll(vararg items: Any?)

    fun insert(`object`: Any, index: Int)

    fun remove(`object`: Any)

    fun clear()

    fun sort(comparator: Comparator<Any?>)

    val dataCount: Int

    fun getData(positionInDataList: Int): Any?

    var dataList: List<Any?>?


    fun getItemCount(): Int

    fun getItem(position: Int): Any?

    fun isHeaderItem(position: Int): Boolean

    fun isBodyItem(position: Int): Boolean

    fun isFooterItem(position: Int): Boolean

    fun isMoreFooterItem(position: Int): Boolean

    fun getPositionInPart(position: Int): Int

    fun getItemFactoryByPosition(position: Int): Any
}