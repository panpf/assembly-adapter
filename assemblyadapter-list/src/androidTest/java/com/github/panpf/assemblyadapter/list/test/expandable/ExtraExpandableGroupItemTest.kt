package com.github.panpf.assemblyadapter.list.test.expandable

import android.view.View
import android.widget.TextView
import androidx.test.platform.app.InstrumentationRegistry
import com.github.panpf.assemblyadapter.list.expandable.ExpandableGroup
import com.github.panpf.assemblyadapter.list.expandable.ExtraExpandableGroupItem
import com.github.panpf.tools4j.test.ktx.assertThrow
import org.junit.Assert
import org.junit.Test

class ExtraExpandableGroupItemTest {

    @Test
    @Suppress("RemoveExplicitTypeArguments")
    fun test() {
        val context = InstrumentationRegistry.getInstrumentation().context
        val testExtraItem = TestExtraItem<Strings>(TextView(context))

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

    private data class Strings(val name: String = "") : ExpandableGroup {

        override fun getChildCount(): Int = name.length

        override fun getChild(childPosition: Int): Any {
            return name[childPosition].toString()
        }
    }

    private class TestExtraItem<DATA : ExpandableGroup>(itemView: View) :
        ExtraExpandableGroupItem<DATA>(itemView) {
        override fun bindData(
            isExpanded: Boolean,
            bindingAdapterPosition: Int,
            absoluteAdapterPosition: Int,
            data: DATA
        ) {

        }
    }
}