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
import com.github.panpf.assemblyadapter.internal.OnClickListenerWrapper
import com.github.panpf.assemblyadapter.internal.OnLongClickListenerWrapper
import com.github.panpf.assemblyadapter.list.expandable.*
import com.github.panpf.assemblyadapter.list.expandable.internal.ChildOnClickListenerWrapper
import com.github.panpf.assemblyadapter.list.expandable.internal.ChildOnLongClickListenerWrapper
import com.github.panpf.tools4j.reflect.ktx.callMethod
import com.github.panpf.tools4j.reflect.ktx.getFieldValue
import com.github.panpf.tools4j.test.ktx.assertThrow
import org.junit.Assert
import org.junit.Test

class ExpandableChildItemFactoryTest {

    @Test
    fun testMethodMatchData() {
        val testItemFactory = TextExpandableChildItemFactory()

        Assert.assertFalse(testItemFactory.matchData(1))
        Assert.assertFalse(testItemFactory.matchData(false))
        Assert.assertTrue(testItemFactory.matchData(Text("hello")))
    }

    @Test
    fun testMethodDispatchCreateItem() {
        val context = InstrumentationRegistry.getInstrumentation().context
        val testItemFactory = TextExpandableChildItemFactory()

        val item = testItemFactory.dispatchCreateItem(FrameLayout(context))
        Assert.assertTrue(item is TextExpandableChildItem)
    }

    @Test
    fun testMethodSetOnViewClickListener() {
        val context = InstrumentationRegistry.getInstrumentation().context
        TextExpandableChildItemFactory().apply {
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

        TextExpandableChildItemFactory().apply {
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
            Assert.assertTrue(itemOnClickListener is OnClickListenerWrapper<*>)
            Assert.assertTrue(itemOnLongClickListener is OnLongClickListenerWrapper<*>)
            Assert.assertTrue(viewOnClickListener is OnClickListenerWrapper<*>)
            Assert.assertTrue(viewOnLongClickListener is OnLongClickListenerWrapper<*>)
        }

        TextExpandableChildItemFactory().apply {
            setOnChildItemClickListener(TestOnChildClickListener())
            setOnChildItemLongClickListener(TestOnChildLongClickListener())
            setOnChildViewClickListener(R.id.aa_tag_clickBindItem, TestOnChildClickListener())
            setOnChildViewLongClickListener(
                R.id.aa_tag_clickBindItem,
                TestOnChildLongClickListener()
            )
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
            Assert.assertTrue(itemOnClickListener is ChildOnClickListenerWrapper<*, *>)
            Assert.assertTrue(itemOnLongClickListener is ChildOnLongClickListenerWrapper<*, *>)
            Assert.assertTrue(viewOnClickListener is ChildOnClickListenerWrapper<*, *>)
            Assert.assertTrue(viewOnLongClickListener is ChildOnLongClickListenerWrapper<*, *>)
        }

        assertThrow(IllegalArgumentException::class) {
            TextExpandableChildItemFactory().apply {
                setOnViewClickListener(R.id.aa_tag_absoluteAdapterPosition, TestOnClickListener())
            }.dispatchCreateItem(FrameLayout(context))
        }

        assertThrow(IllegalArgumentException::class) {
            TextExpandableChildItemFactory().apply {
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

    private class TextExpandableChildItemFactory :
        ExpandableChildItemFactory<TextGroup, Text>(Text::class) {
        override fun createExpandableChildItem(parent: ViewGroup): ExpandableChildItem<TextGroup, Text> {
            return TextExpandableChildItem(FrameLayout(parent.context).apply {
                addView(TextView(parent.context).apply {
                    id = R.id.aa_tag_clickBindItem
                })
            })
        }
    }

    private class TextExpandableChildItem(itemView: View) :
        ExpandableChildItem<TextGroup, Text>(itemView) {

        override fun bindData(
            groupBindingAdapterPosition: Int,
            groupAbsoluteAdapterPosition: Int,
            groupData: TextGroup,
            isLastChild: Boolean,
            bindingAdapterPosition: Int,
            absoluteAdapterPosition: Int,
            data: Text
        ) {

        }
    }

    private class TestOnClickListener : OnClickListener<Text> {
        override fun onClick(
            context: Context,
            view: View,
            bindingAdapterPosition: Int,
            absoluteAdapterPosition: Int,
            data: Text
        ) {

        }
    }

    private class TestOnLongClickListener : OnLongClickListener<Text> {
        override fun onLongClick(
            context: Context,
            view: View,
            bindingAdapterPosition: Int,
            absoluteAdapterPosition: Int,
            data: Text
        ): Boolean {
            return false
        }
    }

    private class TestOnChildClickListener : OnChildClickListener<TextGroup, Text> {
        override fun onClick(
            context: Context,
            view: View,
            groupBindingAdapterPosition: Int,
            groupAbsoluteAdapterPosition: Int,
            groupData: TextGroup,
            isLastChild: Boolean,
            bindingAdapterPosition: Int,
            absoluteAdapterPosition: Int,
            data: Text
        ) {

        }
    }

    private class TestOnChildLongClickListener : OnChildLongClickListener<TextGroup, Text> {
        override fun onLongClick(
            context: Context,
            view: View,
            groupBindingAdapterPosition: Int,
            groupAbsoluteAdapterPosition: Int,
            groupData: TextGroup,
            isLastChild: Boolean,
            bindingAdapterPosition: Int,
            absoluteAdapterPosition: Int,
            data: Text
        ): Boolean = false
    }
}