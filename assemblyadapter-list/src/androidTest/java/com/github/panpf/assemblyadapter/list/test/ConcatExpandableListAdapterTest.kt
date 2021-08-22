package com.github.panpf.assemblyadapter.list.test

import android.widget.BaseExpandableListAdapter
import android.widget.ListView
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.github.panpf.assemblyadapter.Item
import com.github.panpf.assemblyadapter.ViewItemFactory
import com.github.panpf.assemblyadapter.list.*
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import java.util.*

@RunWith(AndroidJUnit4::class)
class ConcatExpandableListAdapterTest {

    data class DateChild(val date: Date = Date())

    data class DateGroup(val childDataList: List<DateChild> = listOf(DateChild())) :
        ExpandableGroup {
        override fun getChildCount(): Int = childDataList.size

        override fun getChild(childPosition: Int): Any = childDataList[childPosition]
    }

    class DateExpandableGroupItemFactory : ViewExpandableGroupItemFactory<DateGroup>(
        DateGroup::class, android.R.layout.activity_list_item
    )

    class DateExpandableChildItemFactory : ViewExpandableChildItemFactory<DateGroup, DateChild>(
        DateChild::class, android.R.layout.activity_list_item
    )

    class DateItemFactory : ViewItemFactory<Date>(
        Date::class, android.R.layout.activity_list_item
    )

    @Test
    fun testNestedAdapterPosition() {
        val count1Adapter =
            AssemblySingleDataExpandableListAdapter<Any, Any>(
                listOf(DateExpandableGroupItemFactory(), DateExpandableChildItemFactory()),
                DateGroup()
            )
        val count3Adapter = AssemblyExpandableListAdapter<Any, Any>(
            listOf(
                DateExpandableGroupItemFactory(),
                DateExpandableChildItemFactory(),
                DateItemFactory()
            ),
            listOf(DateGroup(), Date(), DateGroup())
        )
        val count5Adapter = AssemblyExpandableListAdapter<Any, Any>(
            listOf(
                DateExpandableGroupItemFactory(),
                DateExpandableChildItemFactory(),
                DateItemFactory()
            ),
            listOf(DateGroup(), Date(), DateGroup(), Date(), DateGroup())
        )
        val count7Adapter = AssemblyExpandableListAdapter<Any, Any>(
            listOf(
                DateExpandableGroupItemFactory(),
                DateExpandableChildItemFactory(),
                DateItemFactory()
            ),
            listOf(
                DateGroup(), Date(), DateGroup(), Date(),
                DateGroup(), Date(), DateGroup()
            )
        )
        val concatCount9Adapter =
            ConcatExpandableListAdapter(count1Adapter, count3Adapter, count5Adapter)
        val concatCount11Adapter =
            ConcatExpandableListAdapter(count1Adapter, count3Adapter, count7Adapter)
        val concatCount13Adapter =
            ConcatExpandableListAdapter(count1Adapter, count5Adapter, count7Adapter)
        val concatNestingCount16Adapter = ConcatExpandableListAdapter(
            count1Adapter, ConcatExpandableListAdapter(count3Adapter, count5Adapter), count7Adapter
        )

        Assert.assertEquals("count1Adapter.groupCount", 1, count1Adapter.groupCount)
        Assert.assertEquals("count3Adapter.groupCount", 3, count3Adapter.groupCount)
        Assert.assertEquals("count5Adapter.groupCount", 5, count5Adapter.groupCount)
        Assert.assertEquals("count7Adapter.groupCount", 7, count7Adapter.groupCount)
        Assert.assertEquals("count7Adapter.groupCount", 9, concatCount9Adapter.groupCount)
        Assert.assertEquals("count7Adapter.groupCount", 11, concatCount11Adapter.groupCount)
        Assert.assertEquals("count12Adapter.groupCount", 13, concatCount13Adapter.groupCount)
        Assert.assertEquals("count15Adapter.groupCount", 16, concatNestingCount16Adapter.groupCount)

        val context = InstrumentationRegistry.getInstrumentation().context
        val parent = ListView(context)
        val verifyAdapterPosition: (BaseExpandableListAdapter, Int, Int, Int) -> Unit =
            { adapter, groupPosition, expectedBindingAdapterPosition, expectedAbsoluteAdapterPosition ->
                val groupItemView = adapter.getGroupView(groupPosition, false, null, parent)
                val groupItem = groupItemView.getTag(R.id.aa_tag_item) as Item<*>
                Assert.assertEquals(
                    "count${adapter.groupCount}Adapter. groupPosition(${groupPosition}). item.bindingAdapterPosition",
                    expectedBindingAdapterPosition, groupItem.bindingAdapterPosition
                )
                Assert.assertEquals(
                    "count${adapter.groupCount}Adapter. groupPosition(${groupPosition}). item.absoluteAdapterPosition",
                    expectedAbsoluteAdapterPosition, groupItem.absoluteAdapterPosition
                )

                if (adapter.getChildrenCount(groupPosition) > 0) {
                    val childItemView =
                        adapter.getChildView(groupPosition, 0, false, null, parent)
                    val childItem =
                        childItemView.getTag(R.id.aa_tag_item) as ExpandableChildItem<*, *>
                    Assert.assertEquals(
                        "count${adapter.groupCount}Adapter. groupPosition(${groupPosition}), childPosition(0). item.groupBindingAdapterPosition",
                        expectedBindingAdapterPosition, childItem.groupBindingAdapterPosition
                    )
                    Assert.assertEquals(
                        "count${adapter.groupCount}Adapter. groupPosition(${groupPosition}), childPosition(0). item.groupAbsoluteAdapterPosition",
                        expectedAbsoluteAdapterPosition, childItem.groupAbsoluteAdapterPosition
                    )
                    Assert.assertEquals(
                        "count${adapter.groupCount}Adapter. groupPosition(${groupPosition}), childPosition(0). item.bindingAdapterPosition",
                        0, childItem.bindingAdapterPosition
                    )
                    Assert.assertEquals(
                        "count${adapter.groupCount}Adapter. groupPosition(${groupPosition}), childPosition(0). item.absoluteAdapterPosition",
                        0, childItem.absoluteAdapterPosition
                    )
                }
            }

        /* adapter, position, bindingAdapterPosition, absoluteAdapterPosition */
        verifyAdapterPosition(count1Adapter, 0, 0, 0)

        verifyAdapterPosition(count3Adapter, 0, 0, 0)
        verifyAdapterPosition(count3Adapter, 1, 1, 1)
        verifyAdapterPosition(count3Adapter, 2, 2, 2)

        verifyAdapterPosition(count5Adapter, 0, 0, 0)
        verifyAdapterPosition(count5Adapter, 1, 1, 1)
        verifyAdapterPosition(count5Adapter, 2, 2, 2)
        verifyAdapterPosition(count5Adapter, 3, 3, 3)
        verifyAdapterPosition(count5Adapter, 4, 4, 4)

        verifyAdapterPosition(count7Adapter, 0, 0, 0)
        verifyAdapterPosition(count7Adapter, 1, 1, 1)
        verifyAdapterPosition(count7Adapter, 2, 2, 2)
        verifyAdapterPosition(count7Adapter, 3, 3, 3)
        verifyAdapterPosition(count7Adapter, 4, 4, 4)
        verifyAdapterPosition(count7Adapter, 5, 5, 5)
        verifyAdapterPosition(count7Adapter, 6, 6, 6)

        verifyAdapterPosition(concatCount9Adapter, 0, 0, 0)
        verifyAdapterPosition(concatCount9Adapter, 1, 0, 1)
        verifyAdapterPosition(concatCount9Adapter, 2, 1, 2)
        verifyAdapterPosition(concatCount9Adapter, 3, 2, 3)
        verifyAdapterPosition(concatCount9Adapter, 4, 0, 4)
        verifyAdapterPosition(concatCount9Adapter, 5, 1, 5)
        verifyAdapterPosition(concatCount9Adapter, 6, 2, 6)
        verifyAdapterPosition(concatCount9Adapter, 7, 3, 7)
        verifyAdapterPosition(concatCount9Adapter, 8, 4, 8)

        verifyAdapterPosition(concatCount11Adapter, 0, 0, 0)
        verifyAdapterPosition(concatCount11Adapter, 1, 0, 1)
        verifyAdapterPosition(concatCount11Adapter, 2, 1, 2)
        verifyAdapterPosition(concatCount11Adapter, 3, 2, 3)
        verifyAdapterPosition(concatCount11Adapter, 4, 0, 4)
        verifyAdapterPosition(concatCount11Adapter, 5, 1, 5)
        verifyAdapterPosition(concatCount11Adapter, 6, 2, 6)
        verifyAdapterPosition(concatCount11Adapter, 7, 3, 7)
        verifyAdapterPosition(concatCount11Adapter, 8, 4, 8)
        verifyAdapterPosition(concatCount11Adapter, 9, 5, 9)
        verifyAdapterPosition(concatCount11Adapter, 10, 6, 10)

        verifyAdapterPosition(concatCount13Adapter, 0, 0, 0)
        verifyAdapterPosition(concatCount13Adapter, 1, 0, 1)
        verifyAdapterPosition(concatCount13Adapter, 2, 1, 2)
        verifyAdapterPosition(concatCount13Adapter, 3, 2, 3)
        verifyAdapterPosition(concatCount13Adapter, 4, 3, 4)
        verifyAdapterPosition(concatCount13Adapter, 5, 4, 5)
        verifyAdapterPosition(concatCount13Adapter, 6, 0, 6)
        verifyAdapterPosition(concatCount13Adapter, 7, 1, 7)
        verifyAdapterPosition(concatCount13Adapter, 8, 2, 8)
        verifyAdapterPosition(concatCount13Adapter, 9, 3, 9)
        verifyAdapterPosition(concatCount13Adapter, 10, 4, 10)
        verifyAdapterPosition(concatCount13Adapter, 11, 5, 11)
        verifyAdapterPosition(concatCount13Adapter, 12, 6, 12)

        verifyAdapterPosition(concatNestingCount16Adapter, 0, 0, 0)
        verifyAdapterPosition(concatNestingCount16Adapter, 1, 0, 1)
        verifyAdapterPosition(concatNestingCount16Adapter, 2, 1, 2)
        verifyAdapterPosition(concatNestingCount16Adapter, 3, 2, 3)
        verifyAdapterPosition(concatNestingCount16Adapter, 4, 0, 4)
        verifyAdapterPosition(concatNestingCount16Adapter, 5, 1, 5)
        verifyAdapterPosition(concatNestingCount16Adapter, 6, 2, 6)
        verifyAdapterPosition(concatNestingCount16Adapter, 7, 3, 7)
        verifyAdapterPosition(concatNestingCount16Adapter, 8, 4, 8)
        verifyAdapterPosition(concatNestingCount16Adapter, 9, 0, 9)
        verifyAdapterPosition(concatNestingCount16Adapter, 10, 1, 10)
        verifyAdapterPosition(concatNestingCount16Adapter, 11, 2, 11)
        verifyAdapterPosition(concatNestingCount16Adapter, 12, 3, 12)
        verifyAdapterPosition(concatNestingCount16Adapter, 13, 4, 13)
        verifyAdapterPosition(concatNestingCount16Adapter, 14, 5, 14)
        verifyAdapterPosition(concatNestingCount16Adapter, 15, 6, 15)
    }
}