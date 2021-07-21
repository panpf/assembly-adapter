package com.github.panpf.assemblyadapter3.compat.internal

import com.github.panpf.assemblyadapter3.compat.CompatAssemblyAdapter
import com.github.panpf.assemblyadapter3.compat.CompatAssemblyGroup
import com.github.panpf.assemblyadapter3.compat.CompatAssemblyItemFactory
import java.util.*

class CompatExpandableItemManager(callback: Callback) : CompatItemManager(callback) {

    val groupItemFactoryList = ArrayList<CompatAssemblyItemFactory<out CompatAssemblyGroup>>()

    val childItemFactoryList = ArrayList<CompatAssemblyItemFactory<*>>()

    /**
     * 添加一个用来处理并显示 dataList 中的 group 数据的 [CompatAssemblyItemFactory]
     */
    fun <DATA: CompatAssemblyGroup> addGroupItemFactory(childItemFactory: CompatAssemblyItemFactory<DATA>, adapter: CompatAssemblyAdapter) {
        groupItemFactoryList.add(childItemFactory)
        childItemFactory.attachToAdapter(adapter)
    }

    /**
     * 添加一个用来处理并显示 dataList 中的 child 数据的 [CompatAssemblyItemFactory]
     */
    fun addChildItemFactory(childItemFactory: CompatAssemblyItemFactory<*>, adapter: CompatAssemblyAdapter) {
        childItemFactoryList.add(childItemFactory)
        childItemFactory.attachToAdapter(adapter)
    }

//    val childTypeCount: Int
//        get() {
//            if (!childViewTypeManager.isLocked) {
//                childViewTypeManager.lock()
//            }
//            return childViewTypeManager.count
//        }
//
//    fun getChildItemFactoryByViewType(viewType: Int): AssemblyItemFactory<*> {
//        val itemFactory = childViewTypeManager[viewType]!!
//        return itemFactory
//            ?: throw IllegalArgumentException("Unknown child viewType. viewType=$viewType")
//    }
//
//    fun getChildrenCount(groupPosition: Int): Int {
//        val groupObject = getItemDataByPosition(groupPosition)
//        return if (groupObject is AssemblyGroup) {
//            groupObject.getChildCount()
//        } else 0
//    }
//
//    fun getChildDataByPosition(groupPosition: Int, childPosition: Int): Any? {
//        val groupDataObject = getItemDataByPosition(groupPosition)
//            ?: throw IllegalArgumentException("Not found group item data by group position: $groupPosition")
//        require(groupDataObject is AssemblyGroup) {
//            String.format(
//                "group object must implements AssemblyGroup interface. groupPosition=%d, groupDataObject=%s",
//                groupPosition, groupDataObject.javaClass.name
//            )
//        }
//        return groupDataObject.getChild(childPosition)
//    }
//
//    fun getChildViewType(groupPosition: Int, childPosition: Int): Int {
//        check(childItemFactoryList.size > 0) { "You need to configure AssemblyItemFactory use addChildItemFactory method" }
//        val childDataObject = getChildDataByPosition(groupPosition, childPosition)
//        var childItemFactory: AssemblyItemFactory<*>
//        var w = 0
//        val size = childItemFactoryList.size
//        while (w < size) {
//            childItemFactory = childItemFactoryList[w]
//            if (childItemFactory.match(childDataObject)) {
//                return childItemFactory.viewType
//            }
//            w++
//        }
//        throw IllegalStateException(
//            String.format(
//                "Didn't find suitable AssemblyItemFactory. groupPosition=%d, childPosition=%d, childDataObject=%s",
//                groupPosition, childPosition, childDataObject?.javaClass?.name ?: "null"
//            )
//        )
//    }
}