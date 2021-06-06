//package com.github.panpf.assemblyadapter.test
//
//import com.github.panpf.assemblyadapter.Adapter
//import com.github.panpf.assemblyadapter.ItemFactory
//import java.util.*
//import kotlin.collections.ArrayList
//import kotlin.collections.MutableCollection
//import kotlin.collections.MutableList
//
//class TestAssemblyAdapter(private val dataCountConfig: Int = 0) : Adapter {
//
//    override fun <DATA : Any?> addItemFactory(itemFactory: ItemFactory<DATA>) {
//
//    }
//
//    override fun getItemFactoryList(): MutableList<ItemFactory<Any>> {
//        return ArrayList()
//    }
//
//    override fun getDataList(): MutableList<Any?> {
//        throw UnsupportedOperationException("getHeaderItem")
//    }
//
//    override fun setDataList(dataList: MutableList<Any?>?) {
//
//    }
//
//    override fun addAll(collection: MutableCollection<Any?>?) {
//
//    }
//
//    override fun addAll(vararg items: Any?) {
//
//    }
//
//    override fun insert(`object`: Any, index: Int) {
//
//    }
//
//    override fun remove(`object`: Any) {
//
//    }
//
//    override fun clear() {
//
//    }
//
//    override fun sort(comparator: Comparator<*>) {
//
//    }
//
//    override fun getDataCount(): Int = dataCountConfig
//
//    override fun getData(positionInDataList: Int): Any? {
//        throw UnsupportedOperationException("getHeaderItem")
//    }
//
//    override fun getItemCount(): Int {
//        throw UnsupportedOperationException("getHeaderItem")
//    }
//
//    override fun getItem(position: Int): Any? {
//        throw UnsupportedOperationException("getHeaderItem")
//    }
//
//    override fun isHeaderItem(position: Int): Boolean {
//        throw UnsupportedOperationException("getHeaderItem")
//    }
//
//    override fun isBodyItem(position: Int): Boolean {
//        throw UnsupportedOperationException("getHeaderItem")
//    }
//
//    override fun isFooterItem(position: Int): Boolean {
//        throw UnsupportedOperationException("getHeaderItem")
//    }
//
//    override fun isMoreFooterItem(position: Int): Boolean {
//        throw UnsupportedOperationException("getHeaderItem")
//    }
//
//    override fun getPositionInPart(position: Int): Int {
//        throw UnsupportedOperationException("getHeaderItem")
//    }
//
//    override fun getSpanSize(position: Int): Int {
//        throw UnsupportedOperationException("getHeaderItem")
//    }
//
//    override fun getItemFactoryByPosition(position: Int): ItemFactory<*> {
//        throw UnsupportedOperationException("getHeaderItem")
//    }
//
//    override fun getItemFactoryByViewType(viewType: Int): ItemFactory<*> {
//        throw UnsupportedOperationException("getHeaderItem")
//    }
//
//    override fun isNotifyOnChange(): Boolean {
//        throw UnsupportedOperationException("getHeaderItem")
//    }
//
//    override fun setNotifyOnChange(notifyOnChange: Boolean) {
//
//    }
//
//    override fun notifyDataSetChanged() {
//
//    }
//}