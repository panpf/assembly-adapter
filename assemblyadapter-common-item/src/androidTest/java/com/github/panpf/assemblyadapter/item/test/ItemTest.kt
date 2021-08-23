package com.github.panpf.assemblyadapter.item.test

import android.view.View
import android.widget.TextView
import androidx.test.platform.app.InstrumentationRegistry
import com.github.panpf.assemblyadapter.Item
import com.github.panpf.tools4j.test.ktx.assertThrow
import org.junit.Assert
import org.junit.Test

class ItemTest {

    @Test
    @Suppress("RemoveExplicitTypeArguments")
    fun test() {
        val context = InstrumentationRegistry.getInstrumentation().context
        val testExtraItem = TestItem<String>(TextView(context))

        Assert.assertNotNull(testExtraItem.context)
        Assert.assertNull(testExtraItem.dataOrNull)
        assertThrow(NullPointerException::class) {
            testExtraItem.dataOrThrow
        }
        Assert.assertEquals(-1, testExtraItem.bindingAdapterPosition)
        Assert.assertEquals(-1, testExtraItem.absoluteAdapterPosition)

        testExtraItem.dispatchBindData(4, 5, "testData")
        Assert.assertEquals("testData", testExtraItem.dataOrNull)
        Assert.assertEquals("testData", testExtraItem.dataOrThrow)
        Assert.assertEquals(4, testExtraItem.bindingAdapterPosition)
        Assert.assertEquals(5, testExtraItem.absoluteAdapterPosition)
    }

    private class TestItem<DATA : Any>(itemView: View) : Item<DATA>(itemView) {
        override fun bindData(
            bindingAdapterPosition: Int,
            absoluteAdapterPosition: Int,
            data: DATA
        ) {

        }
    }
}