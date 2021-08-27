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
package com.github.panpf.assemblyadapter.list.expandable.test

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import androidx.test.platform.app.InstrumentationRegistry
import com.github.panpf.assemblyadapter.OnClickListener
import com.github.panpf.assemblyadapter.OnLongClickListener
import com.github.panpf.assemblyadapter.common.item.R
import com.github.panpf.assemblyadapter.list.expandable.ExpandableChildItem
import com.github.panpf.assemblyadapter.list.expandable.ExpandableChildItemFactory
import com.github.panpf.assemblyadapter.list.expandable.ExpandableGroup
import com.github.panpf.tools4j.reflect.ktx.callMethod
import com.github.panpf.tools4j.reflect.ktx.getFieldValue
import com.github.panpf.tools4j.test.ktx.assertThrow
import org.junit.Assert
import org.junit.Test
import kotlin.reflect.KClass

class ExpandableChildItemFactoryTest {

    @Test
    fun testMethodMatchData() {
        val testItemFactory =
            TestExpandableChildItemFactory<TestExpandableGroup, String>(String::class)

        Assert.assertFalse(testItemFactory.matchData(1))
        Assert.assertFalse(testItemFactory.matchData(false))
        Assert.assertTrue(testItemFactory.matchData("string"))
    }

    @Test
    fun testMethodDispatchCreateItem() {
        val context = InstrumentationRegistry.getInstrumentation().context
        val testItemFactory =
            TestExpandableChildItemFactory<TestExpandableGroup, String>(String::class)

        val item = testItemFactory.dispatchCreateItem(FrameLayout(context))
        Assert.assertTrue(item is TestExpandableChildItem<*, *>)
    }

    @Test
    fun testMethodSetOnViewClickListener() {
        val context = InstrumentationRegistry.getInstrumentation().context
        TestExpandableChildItemFactory<TestExpandableGroup, String>(String::class).apply {
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

        TestExpandableChildItemFactory<TestExpandableGroup, String>(String::class).apply {
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
            TestExpandableChildItemFactory<TestExpandableGroup, String>(String::class).apply {
                setOnViewClickListener(R.id.aa_tag_absoluteAdapterPosition, TestOnClickListener())
            }.dispatchCreateItem(FrameLayout(context))
        }

        assertThrow(IllegalArgumentException::class) {
            TestExpandableChildItemFactory<TestExpandableGroup, String>(String::class).apply {
                setOnViewLongClickListener(
                    R.id.aa_tag_absoluteAdapterPosition,
                    TestOnLongClickListener()
                )
            }.dispatchCreateItem(FrameLayout(context))
        }
    }


    private class TestExpandableChildItemFactory<DATA : ExpandableGroup, CHILD : Any>(dataClass: KClass<CHILD>) :
        ExpandableChildItemFactory<DATA, CHILD>(dataClass) {
        override fun createItem(parent: ViewGroup): ExpandableChildItem<DATA, CHILD> {
            return TestExpandableChildItem(FrameLayout(parent.context).apply {
                addView(TextView(parent.context).apply {
                    id = R.id.aa_tag_clickBindItem
                })
            })
        }
    }

    private class TestExpandableChildItem<DATA : ExpandableGroup, CHILD : Any>(itemView: View) :
        ExpandableChildItem<DATA, CHILD>(itemView) {

        override fun bindData(
            groupBindingAdapterPosition: Int,
            groupAbsoluteAdapterPosition: Int,
            groupData: DATA,
            isLastChild: Boolean,
            bindingAdapterPosition: Int,
            absoluteAdapterPosition: Int,
            data: CHILD
        ) {

        }
    }

    private class TestOnClickListener : OnClickListener<String> {
        override fun onClick(
            context: Context,
            view: View,
            bindingAdapterPosition: Int,
            absoluteAdapterPosition: Int,
            data: String
        ) {

        }
    }

    private class TestOnLongClickListener : OnLongClickListener<String> {
        override fun onLongClick(
            context: Context,
            view: View,
            bindingAdapterPosition: Int,
            absoluteAdapterPosition: Int,
            data: String
        ): Boolean {
            return false
        }
    }
}