/*
 * Copyright (C) 2021 panpf <panpfpanpf@outlook.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.panpf.assemblyadapter.list.test.expandable

import android.view.View
import android.widget.TextView
import androidx.test.platform.app.InstrumentationRegistry
import com.github.panpf.assemblyadapter.list.expandable.ExpandableGroup
import com.github.panpf.assemblyadapter.list.expandable.ExpandableGroupItem
import com.github.panpf.tools4j.test.ktx.assertThrow
import org.junit.Assert
import org.junit.Test

class ExpandableGroupItemTest {

    @Test
    @Suppress("RemoveExplicitTypeArguments")
    fun test() {
        val context = InstrumentationRegistry.getInstrumentation().context
        val testExpandableGroupItem = TextExpandableGroupItem(TextView(context))

        Assert.assertNotNull(testExpandableGroupItem.context)
        Assert.assertNull(testExpandableGroupItem.dataOrNull)
        assertThrow(NullPointerException::class) {
            testExpandableGroupItem.dataOrThrow
        }
        Assert.assertEquals(-1, testExpandableGroupItem.bindingAdapterPosition)
        Assert.assertEquals(-1, testExpandableGroupItem.absoluteAdapterPosition)
        Assert.assertFalse(testExpandableGroupItem.isExpanded)

        testExpandableGroupItem.dispatchGroupBindData(true, 4, 5, TextGroup("hello", "world"))
        Assert.assertTrue(testExpandableGroupItem.isExpanded)
        Assert.assertEquals("[hello, world]", testExpandableGroupItem.dataOrNull?.listJoinToString)
        Assert.assertEquals("[hello, world]", testExpandableGroupItem.dataOrThrow.listJoinToString)
        Assert.assertEquals(4, testExpandableGroupItem.bindingAdapterPosition)
        Assert.assertEquals(5, testExpandableGroupItem.absoluteAdapterPosition)
    }

    private data class Text(val text: String)

    private data class TextGroup(val list: List<Text>) : ExpandableGroup {

        @Suppress("unused")
        val listJoinToString: String
            get() = list.joinToString(prefix = "[", postfix = "]") { it.text }

        @Suppress("unused")
        constructor(vararg texts: String) : this(texts.map { Text(it) }.toList())

        override fun getChildCount(): Int = list.size

        override fun getChild(childPosition: Int): Any {
            // Shield the differences in exceptions thrown by different versions of the ArrayList get method
            return list.getOrNull(childPosition)
                ?: throw IndexOutOfBoundsException("Index: $childPosition, Size: ${list.size}")
        }
    }

    private class TextExpandableGroupItem(itemView: View) :
        ExpandableGroupItem<TextGroup>(itemView) {

        override fun bindData(
            isExpanded: Boolean,
            bindingAdapterPosition: Int,
            absoluteAdapterPosition: Int,
            data: TextGroup
        ) {
        }
    }
}