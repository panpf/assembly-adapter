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

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import androidx.test.platform.app.InstrumentationRegistry
import com.github.panpf.assemblyadapter.OnClickListener
import com.github.panpf.assemblyadapter.OnLongClickListener
import com.github.panpf.assemblyadapter.common.item.R
import com.github.panpf.assemblyadapter.list.expandable.ExpandableGroup
import com.github.panpf.assemblyadapter.list.expandable.ExpandableGroupItem
import com.github.panpf.assemblyadapter.list.expandable.ExpandableGroupItemFactory
import com.github.panpf.tools4j.reflect.ktx.callMethod
import com.github.panpf.tools4j.reflect.ktx.getFieldValue
import com.github.panpf.tools4j.test.ktx.assertThrow
import org.junit.Assert
import org.junit.Test

class ExpandableGroupItemFactoryTest {

    @Test
    fun testMethodMatchData() {
        val testItemFactory = TextExpandableGroupItemFactory()

        Assert.assertFalse(testItemFactory.matchData(1))
        Assert.assertFalse(testItemFactory.matchData(false))
        Assert.assertTrue(testItemFactory.matchData(TextGroup("string")))
    }

    @Test
    fun testMethodDispatchCreateItem() {
        val context = InstrumentationRegistry.getInstrumentation().context
        val testItemFactory = TextExpandableGroupItemFactory()

        val item = testItemFactory.dispatchCreateItem(FrameLayout(context))
        Assert.assertTrue(item is TextExpandableGroupItem)
    }

    @Test
    fun testMethodSetOnViewClickListener() {
        val context = InstrumentationRegistry.getInstrumentation().context
        TextExpandableGroupItemFactory().apply {
            val item = dispatchCreateItem(FrameLayout(context))
            val rootView = item.itemView
            val childView = item.itemView.findViewById<TextView>(R.id.aa_tag_clickBindItem)
            val itemOnClickListener = rootView.callMethod<Any>("getListenerInfo")!!
                .getFieldValue<View.OnClickListener>("mOnClickListener")
            val itemOnLongClickListener = rootView.callMethod<Any>("getListenerInfo")!!
                .getFieldValue<View.OnLongClickListener>("mOnLongClickListener")
            val viewOnClickListener = childView.callMethod<Any>("getListenerInfo")!!
                .getFieldValue<View.OnClickListener>("mOnClickListener")
            val viewOnLongClickListener = childView.callMethod<Any>("getListenerInfo")!!
                .getFieldValue<View.OnLongClickListener>("mOnLongClickListener")
            Assert.assertNull(itemOnClickListener)
            Assert.assertNull(itemOnLongClickListener)
            Assert.assertNull(viewOnClickListener)
            Assert.assertNull(viewOnLongClickListener)
        }

        TextExpandableGroupItemFactory().apply {
            setOnItemClickListener(TestOnClickListener())
            setOnItemLongClickListener(TestOnLongClickListener())
            setOnViewClickListener(R.id.aa_tag_clickBindItem, TestOnClickListener())
            setOnViewLongClickListener(R.id.aa_tag_clickBindItem, TestOnLongClickListener())
            val item = dispatchCreateItem(FrameLayout(context))
            val rootView = item.itemView
            val childView = item.itemView.findViewById<TextView>(R.id.aa_tag_clickBindItem)
            val itemOnClickListener = rootView.callMethod<Any>("getListenerInfo")!!
                .getFieldValue<View.OnClickListener>("mOnClickListener")
            val itemOnLongClickListener = rootView.callMethod<Any>("getListenerInfo")!!
                .getFieldValue<View.OnLongClickListener>("mOnLongClickListener")
            val viewOnClickListener = childView.callMethod<Any>("getListenerInfo")!!
                .getFieldValue<View.OnClickListener>("mOnClickListener")
            val viewOnLongClickListener = childView.callMethod<Any>("getListenerInfo")!!
                .getFieldValue<View.OnLongClickListener>("mOnLongClickListener")
            Assert.assertNotNull(itemOnClickListener)
            Assert.assertNotNull(itemOnLongClickListener)
            Assert.assertNotNull(viewOnClickListener)
            Assert.assertNotNull(viewOnLongClickListener)
        }

        assertThrow(IllegalArgumentException::class) {
            TextExpandableGroupItemFactory().apply {
                setOnViewClickListener(R.id.aa_tag_absoluteAdapterPosition, TestOnClickListener())
            }.dispatchCreateItem(FrameLayout(context))
        }

        assertThrow(IllegalArgumentException::class) {
            TextExpandableGroupItemFactory().apply {
                setOnViewLongClickListener(
                    R.id.aa_tag_absoluteAdapterPosition,
                    TestOnLongClickListener()
                )
            }.dispatchCreateItem(FrameLayout(context))
        }
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

    private class TextExpandableGroupItemFactory :
        ExpandableGroupItemFactory<TextGroup>(TextGroup::class) {
        override fun createItem(parent: ViewGroup): ExpandableGroupItem<TextGroup> {
            return TextExpandableGroupItem(FrameLayout(parent.context).apply {
                addView(TextView(parent.context).apply {
                    id = R.id.aa_tag_clickBindItem
                })
            })
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

    private class TestOnClickListener : OnClickListener<TextGroup> {
        override fun onClick(
            context: Context,
            view: View,
            bindingAdapterPosition: Int,
            absoluteAdapterPosition: Int,
            data: TextGroup
        ) {

        }
    }

    private class TestOnLongClickListener : OnLongClickListener<TextGroup> {
        override fun onLongClick(
            context: Context,
            view: View,
            bindingAdapterPosition: Int,
            absoluteAdapterPosition: Int,
            data: TextGroup
        ): Boolean {
            return false
        }
    }
}