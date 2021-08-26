package com.github.panpf.assemblyadapter.list.test

import android.view.View
import android.widget.TextView
import androidx.test.platform.app.InstrumentationRegistry
import com.github.panpf.assemblyadapter.list.ExpandableGroup
import com.github.panpf.assemblyadapter.list.ExtraExpandableChildItem
import com.github.panpf.tools4j.test.ktx.assertThrow
import org.junit.Assert
import org.junit.Test

class ExtraExpandableChildItemTest {

    @Test
    @Suppress("RemoveExplicitTypeArguments")
    fun test() {
        val context = InstrumentationRegistry.getInstrumentation().context
        val testExtraItem = TestExtraItem<TestExpandableGroup, String>(TextView(context))

        Assert.assertNull(testExtraItem.getExtraOrNull<String>("testKey"))
        Assert.assertEquals(
            "getExtraOrElse",
            testExtraItem.getExtraOrElse<String>("testKey") { "getExtraOrElse" }
        )
        Assert.assertEquals(
            "getExtraOrDefault",
            testExtraItem.getExtraOrDefault<String>("testKey", "getExtraOrDefault")
        )
        assertThrow(Exception::class) {
            testExtraItem.getExtraOrThrow<String>("testKey")
        }
        Assert.assertEquals(
            "getExtraOrPut",
            testExtraItem.getExtraOrPut<String>("testKey") { "getExtraOrPut" }
        )
        Assert.assertEquals(
            "getExtraOrPut",
            testExtraItem.getExtraOrNull<String>("testKey")
        )
        testExtraItem.putExtra("testKey", null)
        Assert.assertNull(testExtraItem.getExtraOrNull<String>("testKey"))

        testExtraItem.putExtra("testKey", "testValue")
        Assert.assertEquals(
            "testValue",
            testExtraItem.getExtraOrElse<String>("testKey") { "getExtraOrElse" }
        )
        Assert.assertEquals(
            "testValue",
            testExtraItem.getExtraOrDefault<String>("testKey", "getExtraOrDefault")
        )
        Assert.assertEquals(
            "testValue",
            testExtraItem.getExtraOrThrow<String>("testKey")
        )
        Assert.assertEquals(
            "testValue",
            testExtraItem.getExtraOrPut<String>("testKey") { "getExtraOrPut" }
        )
    }

    private class TestExtraItem<GROUP : ExpandableGroup, CHILD : Any>(itemView: View) :
        ExtraExpandableChildItem<GROUP, CHILD>(itemView) {
        override fun bindData(
            groupBindingAdapterPosition: Int,
            groupAbsoluteAdapterPosition: Int,
            groupData: GROUP,
            isLastChild: Boolean,
            bindingAdapterPosition: Int,
            absoluteAdapterPosition: Int,
            data: CHILD
        ) {

        }
    }
}