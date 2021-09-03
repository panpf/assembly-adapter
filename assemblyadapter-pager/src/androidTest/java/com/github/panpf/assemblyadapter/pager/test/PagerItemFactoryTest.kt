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
package com.github.panpf.assemblyadapter.pager.test

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import androidx.test.platform.app.InstrumentationRegistry
import com.github.panpf.assemblyadapter.OnClickListener
import com.github.panpf.assemblyadapter.OnLongClickListener
import com.github.panpf.assemblyadapter.pager.PagerItemFactory
import com.github.panpf.assemblyadapter.pager.internal.PagerClickListenerWrapper
import com.github.panpf.assemblyadapter.pager.internal.PagerLongClickListenerWrapper
import com.github.panpf.tools4j.reflect.ktx.callMethod
import com.github.panpf.tools4j.reflect.ktx.getFieldValue
import com.github.panpf.tools4j.test.ktx.assertThrow
import org.junit.Assert
import org.junit.Test

class PagerItemFactoryTest {

    companion object {
        const val aa_tag_clickBindItem = 1101
    }

    private class StringPagerItemFactory :
        PagerItemFactory<String>(String::class) {

        override fun createItemView(
            context: Context,
            parent: ViewGroup,
            bindingAdapterPosition: Int,
            absoluteAdapterPosition: Int,
            data: String
        ): View = FrameLayout(parent.context).apply {
            addView(TextView(parent.context).apply {
                id = aa_tag_clickBindItem
            })
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

    @Test
    fun testMethodMatchData() {
        val testItemFactory = StringPagerItemFactory()

        Assert.assertFalse(testItemFactory.matchData(1))
        Assert.assertFalse(testItemFactory.matchData(false))
        Assert.assertTrue(testItemFactory.matchData("hello"))
    }

    @Test
    fun testMethodDispatchCreateItem() {
        val context = InstrumentationRegistry.getInstrumentation().context
        val testItemFactory = StringPagerItemFactory()

        testItemFactory.dispatchCreateItemView(context, FrameLayout(context), 0, 1, "hello").apply {
            Assert.assertTrue(this is FrameLayout)
        }
    }
    
    @Test
    fun testMethodSetOnViewClickListener() {
        val context = InstrumentationRegistry.getInstrumentation().context
        StringPagerItemFactory().apply {
            val rootView = dispatchCreateItemView(context, FrameLayout(context), 0, 1, "hello")
            val childView = rootView.findViewById<TextView>(aa_tag_clickBindItem)
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

        StringPagerItemFactory().apply {
            setOnItemClickListener(TestOnClickListener())
            setOnItemLongClickListener(TestOnLongClickListener())
            setOnViewClickListener(aa_tag_clickBindItem, TestOnClickListener())
            setOnViewLongClickListener(aa_tag_clickBindItem, TestOnLongClickListener())
            val rootView = dispatchCreateItemView(context, FrameLayout(context), 0, 1, "hello")
            val childView = rootView.findViewById<TextView>(aa_tag_clickBindItem)
            val itemOnClickListener = rootView.callMethod<Any>("getListenerInfo")!!
                .getFieldValue<View.OnClickListener>("mOnClickListener")
            val itemOnLongClickListener = rootView.callMethod<Any>("getListenerInfo")!!
                .getFieldValue<View.OnLongClickListener>("mOnLongClickListener")
            val viewOnClickListener = childView.callMethod<Any>("getListenerInfo")!!
                .getFieldValue<View.OnClickListener>("mOnClickListener")
            val viewOnLongClickListener = childView.callMethod<Any>("getListenerInfo")!!
                .getFieldValue<View.OnLongClickListener>("mOnLongClickListener")
            Assert.assertTrue(itemOnClickListener is PagerClickListenerWrapper<*>)
            Assert.assertTrue(itemOnLongClickListener is PagerLongClickListenerWrapper<*>)
            Assert.assertTrue(viewOnClickListener is PagerClickListenerWrapper<*>)
            Assert.assertTrue(viewOnLongClickListener is PagerLongClickListenerWrapper<*>)
        }

        assertThrow(IllegalArgumentException::class) {
            StringPagerItemFactory().apply {
                setOnViewClickListener(R.id.aa_tag_absoluteAdapterPosition, TestOnClickListener())
            }.dispatchCreateItemView(context, FrameLayout(context), 0, 1, "hello")
        }

        assertThrow(IllegalArgumentException::class) {
            StringPagerItemFactory().apply {
                setOnViewLongClickListener(
                    R.id.aa_tag_absoluteAdapterPosition,
                    TestOnLongClickListener()
                )
            }.dispatchCreateItemView(context, FrameLayout(context), 0, 1, "hello")
        }
    }
}