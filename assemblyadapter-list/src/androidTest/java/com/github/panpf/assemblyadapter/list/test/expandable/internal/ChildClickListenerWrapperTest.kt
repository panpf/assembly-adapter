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
package com.github.panpf.assemblyadapter.list.test.expandable.internal

import android.view.View
import android.widget.TextView
import androidx.test.platform.app.InstrumentationRegistry
import com.github.panpf.assemblyadapter.common.item.R
import com.github.panpf.assemblyadapter.list.expandable.ExpandableChildItem
import com.github.panpf.assemblyadapter.list.expandable.ExpandableGroup
import com.github.panpf.assemblyadapter.list.expandable.OnChildClickListener
import com.github.panpf.assemblyadapter.list.expandable.internal.ChildClickListenerWrapper
import org.junit.Assert
import org.junit.Test

class ChildClickListenerWrapperTest {

    private data class TextGroup(val name: String) : ExpandableGroup {
        override fun getChildCount(): Int = name.length

        override fun getChild(childPosition: Int): Any = name[childPosition]
    }

    private class TestItem(itemView: View) : ExpandableChildItem<TextGroup, String>(itemView) {
        override fun bindData(
            groupBindingAdapterPosition: Int,
            groupAbsoluteAdapterPosition: Int,
            groupData: TextGroup,
            isLastChild: Boolean,
            bindingAdapterPosition: Int,
            absoluteAdapterPosition: Int,
            data: String
        ) {

        }
    }

    @Test
    fun test() {
        var groupBindingAdapterPosition: Int? = null
        var groupAbsoluteAdapterPosition: Int? = null
        var groupData: TextGroup? = null
        var isLastChild: Boolean? = null
        var bindingAdapterPosition: Int? = null
        var absoluteAdapterPosition: Int? = null
        var data: String? = null
        val childClickListener =
            OnChildClickListener<TextGroup, String> { _, _,
                                                      _groupBindingAdapterPosition,
                                                      _groupAbsoluteAdapterPosition,
                                                      _groupData,
                                                      _isLastChild,
                                                      _bindingAdapterPosition,
                                                      _absoluteAdapterPosition,
                                                      _data ->
                groupBindingAdapterPosition = _groupBindingAdapterPosition
                groupAbsoluteAdapterPosition = _groupAbsoluteAdapterPosition
                groupData = _groupData
                isLastChild = _isLastChild
                bindingAdapterPosition = _bindingAdapterPosition
                absoluteAdapterPosition = _absoluteAdapterPosition
                data = _data
            }

        Assert.assertNull(groupBindingAdapterPosition)
        Assert.assertNull(groupAbsoluteAdapterPosition)
        Assert.assertNull(groupData)
        Assert.assertNull(isLastChild)
        Assert.assertNull(bindingAdapterPosition)
        Assert.assertNull(absoluteAdapterPosition)
        Assert.assertNull(data)

        val context = InstrumentationRegistry.getInstrumentation().context
        val itemView = TextView(context).apply {
            setTag(R.id.aa_tag_clickBindItem, TestItem(this).apply {
                dispatchChildBindData(4, 9, TextGroup("world"), true, 2, 7, "hello")
            })
        }
        ChildClickListenerWrapper(childClickListener).onClick(itemView)

        Assert.assertEquals(4, groupBindingAdapterPosition)
        Assert.assertEquals(9, groupAbsoluteAdapterPosition)
        Assert.assertEquals(TextGroup("world"), groupData)
        Assert.assertEquals(true, isLastChild)
        Assert.assertEquals(2, bindingAdapterPosition)
        Assert.assertEquals(7, absoluteAdapterPosition)
        Assert.assertEquals("hello", data)
    }
}