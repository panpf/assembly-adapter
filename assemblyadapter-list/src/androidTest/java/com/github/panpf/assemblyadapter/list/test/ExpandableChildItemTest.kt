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
package com.github.panpf.assemblyadapter.list.test

import android.view.View
import android.widget.TextView
import androidx.test.platform.app.InstrumentationRegistry
import com.github.panpf.assemblyadapter.list.ExpandableChildItem
import com.github.panpf.assemblyadapter.list.ExpandableGroup
import com.github.panpf.tools4j.test.ktx.assertThrow
import org.junit.Assert
import org.junit.Test

class ExpandableChildItemTest {

    @Test
    @Suppress("RemoveExplicitTypeArguments")
    fun test() {
        val context = InstrumentationRegistry.getInstrumentation().context
        val testExpandableChildItem =
            TestExpandableChildItem<TestExpandableGroup, Any>(TextView(context))

        Assert.assertNotNull(testExpandableChildItem.context)
        Assert.assertNull(testExpandableChildItem.groupDataOrNull)
        assertThrow(NullPointerException::class) {
            testExpandableChildItem.groupDataOrThrow
            Assert.assertEquals(-1, testExpandableChildItem.groupAbsoluteAdapterPosition)
            Assert.assertEquals(-1, testExpandableChildItem.groupBindingAdapterPosition)
        }
        Assert.assertFalse(testExpandableChildItem.isLastChild)
        Assert.assertEquals(-1, testExpandableChildItem.bindingAdapterPosition)
        Assert.assertEquals(-1, testExpandableChildItem.absoluteAdapterPosition)
        Assert.assertNull(testExpandableChildItem.dataOrNull)
        assertThrow(NullPointerException::class) {
            testExpandableChildItem.dataOrThrow
        }

        testExpandableChildItem.dispatchChildBindData(
            2, 3, TestExpandableGroup("testData"), true, 4, 5, "hello"
        )
        Assert.assertEquals("testData", testExpandableChildItem.groupDataOrNull?.name)
        Assert.assertEquals("testData", testExpandableChildItem.groupDataOrThrow.name)
        Assert.assertEquals(2, testExpandableChildItem.groupBindingAdapterPosition)
        Assert.assertEquals(3, testExpandableChildItem.groupAbsoluteAdapterPosition)
        Assert.assertTrue(testExpandableChildItem.isLastChild)
        Assert.assertEquals(4, testExpandableChildItem.bindingAdapterPosition)
        Assert.assertEquals(5, testExpandableChildItem.absoluteAdapterPosition)
        Assert.assertEquals("hello", testExpandableChildItem.dataOrNull)
        Assert.assertEquals("hello", testExpandableChildItem.dataOrThrow)
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
}