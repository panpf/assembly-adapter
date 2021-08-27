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
import com.github.panpf.assemblyadapter.list.ExpandableGroup
import com.github.panpf.assemblyadapter.list.ExpandableGroupItem
import com.github.panpf.tools4j.test.ktx.assertThrow
import org.junit.Assert
import org.junit.Test

class ExpandableGroupItemTest {

    @Test
    @Suppress("RemoveExplicitTypeArguments")
    fun test() {
        val context = InstrumentationRegistry.getInstrumentation().context
        val testExpandableGroupItem = TestExpandableGroupItem<TestExpandableGroup>(TextView(context))

        Assert.assertNotNull(testExpandableGroupItem.context)
        Assert.assertNull(testExpandableGroupItem.dataOrNull)
        assertThrow(NullPointerException::class) {
            testExpandableGroupItem.dataOrThrow
        }
        Assert.assertEquals(-1, testExpandableGroupItem.bindingAdapterPosition)
        Assert.assertEquals(-1, testExpandableGroupItem.absoluteAdapterPosition)
        Assert.assertFalse(testExpandableGroupItem.isExpanded)

        testExpandableGroupItem.dispatchGroupBindData(true, 4, 5, TestExpandableGroup("testData"))
        Assert.assertTrue(testExpandableGroupItem.isExpanded)
        Assert.assertEquals("testData", testExpandableGroupItem.dataOrNull?.name)
        Assert.assertEquals("testData", testExpandableGroupItem.dataOrThrow.name)
        Assert.assertEquals(4, testExpandableGroupItem.bindingAdapterPosition)
        Assert.assertEquals(5, testExpandableGroupItem.absoluteAdapterPosition)
    }

    private class TestExpandableGroupItem<DATA : ExpandableGroup>(itemView: View) :
        ExpandableGroupItem<DATA>(itemView) {

        override fun bindData(
            isExpanded: Boolean,
            bindingAdapterPosition: Int,
            absoluteAdapterPosition: Int,
            data: DATA
        ) {
        }
    }
}