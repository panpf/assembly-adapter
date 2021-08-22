package com.github.panpf.assemblyadapter.recycler.test

import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.github.panpf.assemblyadapter.ViewItemFactory
import com.github.panpf.assemblyadapter.recycler.AssemblyRecyclerAdapter
import com.github.panpf.assemblyadapter.recycler.AssemblySingleDataRecyclerAdapter
import com.github.panpf.assemblyadapter.recycler.internal.RecyclerViewHolderWrapper
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import java.util.*

@RunWith(AndroidJUnit4::class)
class ConcatAdapterTest {

    class DateItemFactory : ViewItemFactory<Date>(Date::class, android.R.layout.activity_list_item)

    @Test
    fun testNestedAdapterPosition() {
        val count1Adapter = AssemblySingleDataRecyclerAdapter(DateItemFactory(), Date())
        val count3Adapter = AssemblyRecyclerAdapter(
            listOf(DateItemFactory()),
            listOf(Date(), Date(), Date())
        )
        val count5Adapter = AssemblyRecyclerAdapter(
            listOf(DateItemFactory()),
            listOf(Date(), Date(), Date(), Date(), Date())
        )
        val count7Adapter = AssemblyRecyclerAdapter(
            listOf(DateItemFactory()),
            listOf(Date(), Date(), Date(), Date(), Date(), Date(), Date())
        )
        val concatCount9Adapter = ConcatAdapter(count1Adapter, count3Adapter, count5Adapter)
        val concatCount11Adapter = ConcatAdapter(count1Adapter, count3Adapter, count7Adapter)
        val concatCount13Adapter = ConcatAdapter(count1Adapter, count5Adapter, count7Adapter)
        val concatNestingCount16Adapter = ConcatAdapter(
            count1Adapter, ConcatAdapter(count3Adapter, count5Adapter), count7Adapter
        )

        assertEquals("count1Adapter.itemCount", 1, count1Adapter.itemCount)
        assertEquals("count3Adapter.itemCount", 3, count3Adapter.itemCount)
        assertEquals("count5Adapter.itemCount", 5, count5Adapter.itemCount)
        assertEquals("count7Adapter.itemCount", 7, count7Adapter.itemCount)
        assertEquals("count7Adapter.itemCount", 9, concatCount9Adapter.itemCount)
        assertEquals("count7Adapter.itemCount", 11, concatCount11Adapter.itemCount)
        assertEquals("count12Adapter.itemCount", 13, concatCount13Adapter.itemCount)
        assertEquals("count15Adapter.itemCount", 16, concatNestingCount16Adapter.itemCount)

        val context = InstrumentationRegistry.getInstrumentation().context
        val parent = RecyclerView(context).apply {
            layoutManager = LinearLayoutManager(context)
        }
        val verifyAdapterPosition: (RecyclerView.Adapter<RecyclerView.ViewHolder>, Int, Int, Int) -> Unit =
            { adapter, position, expectedBindingAdapterPosition, expectedAbsoluteAdapterPosition ->
                val itemType = adapter.getItemViewType(position)
                val viewHolder = adapter.createViewHolder(parent, itemType)
                adapter.bindViewHolder(viewHolder, position)
                val item = (viewHolder as RecyclerViewHolderWrapper<*>).wrappedItem
                assertEquals(
                    "count${adapter.itemCount}Adapter. position(${position}). item.bindingAdapterPosition",
                    expectedBindingAdapterPosition, item.bindingAdapterPosition
                )
                assertEquals(
                    "count${adapter.itemCount}Adapter. position(${position}). item.absoluteAdapterPosition",
                    expectedAbsoluteAdapterPosition, item.absoluteAdapterPosition
                )
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