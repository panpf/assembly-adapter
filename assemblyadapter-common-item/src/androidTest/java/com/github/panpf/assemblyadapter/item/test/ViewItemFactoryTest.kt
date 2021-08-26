package com.github.panpf.assemblyadapter.item.test

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import androidx.test.platform.app.InstrumentationRegistry
import com.github.panpf.assemblyadapter.SimpleItemFactory
import com.github.panpf.assemblyadapter.ViewItemFactory
import com.github.panpf.assemblyadapter.common.item.test.R
import org.junit.Assert
import org.junit.Test

class ViewItemFactoryTest {

    @Test
    fun test() {
        val context = InstrumentationRegistry.getInstrumentation().context
        val parent = FrameLayout(context)

        val itemFactory = ViewItemFactory(String::class, R.layout.item_test)
        val item = itemFactory.dispatchCreateItem(parent) as SimpleItemFactory.SimpleItem<String>

        Assert.assertEquals(
            "",
            item.itemView.findViewById<TextView>(R.id.testItemTitleText).text
        )
        Assert.assertEquals(
            14f,
            item.itemView.findViewById<TextView>(R.id.testItemTitleText).textSize
        )

        item.dispatchBindData(0, 0, "test_data")
        Assert.assertEquals(
            "",
            item.itemView.findViewById<TextView>(R.id.testItemTitleText).text
        )


        val itemFactory2 = ViewItemFactory(
            String::class,
            LayoutInflater.from(context).inflate(R.layout.item_test, parent, false)
        )
        val item2 = itemFactory2.dispatchCreateItem(parent) as SimpleItemFactory.SimpleItem<String>

        Assert.assertEquals(
            "",
            item2.itemView.findViewById<TextView>(R.id.testItemTitleText).text
        )
        Assert.assertEquals(
            14f,
            item2.itemView.findViewById<TextView>(R.id.testItemTitleText).textSize
        )

        item2.dispatchBindData(0, 0, "test_data")
        Assert.assertEquals(
            "",
            item2.itemView.findViewById<TextView>(R.id.testItemTitleText).text
        )


        val itemFactory3 = ViewItemFactory(String::class) { _, inflater, parent: ViewGroup ->
            inflater.inflate(R.layout.item_test, parent, false)
        }
        val item3 = itemFactory3.dispatchCreateItem(parent) as SimpleItemFactory.SimpleItem<String>

        Assert.assertEquals(
            "",
            item3.itemView.findViewById<TextView>(R.id.testItemTitleText).text
        )
        Assert.assertEquals(
            14f,
            item3.itemView.findViewById<TextView>(R.id.testItemTitleText).textSize
        )

        item3.dispatchBindData(0, 0, "test_data")
        Assert.assertEquals(
            "",
            item3.itemView.findViewById<TextView>(R.id.testItemTitleText).text
        )
    }
}