package com.github.panpf.assemblyadapter.pager.test

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.runner.AndroidJUnit4
import androidx.viewpager.widget.PagerAdapter
import com.github.panpf.assemblyadapter.pager.AssemblyPagerAdapter
import com.github.panpf.assemblyadapter.pager.AssemblySingleDataPagerAdapter
import com.github.panpf.assemblyadapter.pager.ConcatPagerAdapter
import com.github.panpf.assemblyadapter.pager.ViewPagerItemFactory
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import java.util.*

@RunWith(AndroidJUnit4::class)
class ConcatPagerAdapterTest {

    class DatePagerItemFactory : ViewPagerItemFactory<Date>(
        Date::class, android.R.layout.activity_list_item
    ) {
        override fun createItemView(
            context: Context,
            parent: ViewGroup,
            bindingAdapterPosition: Int,
            absoluteAdapterPosition: Int,
            data: Date
        ): View {
            return super.createItemView(
                context,
                parent,
                bindingAdapterPosition,
                absoluteAdapterPosition,
                data
            ).apply {
                setTag(R.id.test_tag_bindingAdapterPosition, bindingAdapterPosition)
                setTag(R.id.test_tag_absoluteAdapterPosition, absoluteAdapterPosition)
            }
        }
    }

    @Test
    fun testNestedAdapterPosition() {
        val count1Adapter = AssemblySingleDataPagerAdapter(DatePagerItemFactory(), Date())
        val count3Adapter = AssemblyPagerAdapter(
            listOf(DatePagerItemFactory()),
            listOf(Date(), Date(), Date())
        )
        val count5Adapter = AssemblyPagerAdapter(
            listOf(DatePagerItemFactory()),
            listOf(Date(), Date(), Date(), Date(), Date())
        )
        val count7Adapter = AssemblyPagerAdapter(
            listOf(DatePagerItemFactory()),
            listOf(Date(), Date(), Date(), Date(), Date(), Date(), Date())
        )
        val concatCount9Adapter = ConcatPagerAdapter(count1Adapter, count3Adapter, count5Adapter)
        val concatCount11Adapter = ConcatPagerAdapter(count1Adapter, count3Adapter, count7Adapter)
        val concatCount13Adapter = ConcatPagerAdapter(count1Adapter, count5Adapter, count7Adapter)
        val concatNestingCount16Adapter = ConcatPagerAdapter(
            count1Adapter, ConcatPagerAdapter(count3Adapter, count5Adapter), count7Adapter
        )

        Assert.assertEquals("count1Adapter.count", 1, count1Adapter.count)
        Assert.assertEquals("count3Adapter.count", 3, count3Adapter.count)
        Assert.assertEquals("count5Adapter.count", 5, count5Adapter.count)
        Assert.assertEquals("count7Adapter.count", 7, count7Adapter.count)
        Assert.assertEquals("count7Adapter.count", 9, concatCount9Adapter.count)
        Assert.assertEquals("count7Adapter.count", 11, concatCount11Adapter.count)
        Assert.assertEquals("count12Adapter.count", 13, concatCount13Adapter.count)
        Assert.assertEquals("count15Adapter.count", 16, concatNestingCount16Adapter.count)

        val context = InstrumentationRegistry.getInstrumentation().context
        val parent = FrameLayout(context)
        val verifyAdapterPosition: (PagerAdapter, Int, Int, Int) -> Unit =
            { adapter, position, expectedBindingAdapterPosition, expectedAbsoluteAdapterPosition ->
                val itemView = adapter.instantiateItem(parent, position) as View
                val bindingAdapterPosition =
                    itemView.getTag(R.id.test_tag_bindingAdapterPosition) as Int
                val absoluteAdapterPosition =
                    itemView.getTag(R.id.test_tag_absoluteAdapterPosition) as Int
                Assert.assertEquals(
                    "count${adapter.count}Adapter. position(${position}). itemView.bindingAdapterPosition",
                    expectedBindingAdapterPosition, bindingAdapterPosition
                )
                Assert.assertEquals(
                    "count${adapter.count}Adapter. position(${position}). itemView.absoluteAdapterPosition",
                    expectedAbsoluteAdapterPosition, absoluteAdapterPosition
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