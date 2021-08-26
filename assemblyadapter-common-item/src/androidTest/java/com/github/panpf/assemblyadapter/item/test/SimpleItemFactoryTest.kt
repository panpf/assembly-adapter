package com.github.panpf.assemblyadapter.item.test

import android.content.Context
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import androidx.test.platform.app.InstrumentationRegistry
import com.github.panpf.assemblyadapter.SimpleItemFactory
import com.github.panpf.assemblyadapter.common.item.test.R
import org.junit.Assert
import org.junit.Test

class SimpleItemFactoryTest {

    @Test
    fun test() {
        val context = InstrumentationRegistry.getInstrumentation().context
        val parent = FrameLayout(context)

        val itemFactory = TestSimpleItemFactory()
        val item =
            itemFactory.dispatchCreateItem(parent) as SimpleItemFactory.SimpleItem<String>

        Assert.assertEquals(
            "",
            item.itemView.findViewById<TextView>(R.id.testItemTitleText).text
        )
        Assert.assertEquals(
            30f,
            item.itemView.findViewById<TextView>(R.id.testItemTitleText).textSize
        )

        item.dispatchBindData(0, 0, "test_data")
        Assert.assertEquals(
            "test_data",
            item.itemView.findViewById<TextView>(R.id.testItemTitleText).text
        )
    }

    private class TestSimpleItemFactory :
        SimpleItemFactory<String>(String::class) {

        override fun createItemView(
            context: Context,
            inflater: LayoutInflater,
            parent: ViewGroup
        ): View = inflater.inflate(R.layout.item_test, parent, false)

        override fun initItem(context: Context, itemView: View, item: SimpleItem<String>) {
            itemView.findViewById<TextView>(R.id.testItemTitleText)
                .setTextSize(TypedValue.COMPLEX_UNIT_PX, 30f)
        }

        override fun bindItemData(
            context: Context,
            itemView: View,
            item: SimpleItem<String>,
            bindingAdapterPosition: Int,
            absoluteAdapterPosition: Int,
            data: String
        ) {
            itemView.findViewById<TextView>(R.id.testItemTitleText).text = data
        }
    }
}