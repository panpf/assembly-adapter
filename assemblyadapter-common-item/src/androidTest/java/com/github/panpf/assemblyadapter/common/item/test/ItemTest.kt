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
package com.github.panpf.assemblyadapter.common.item.test

import android.view.View
import android.widget.TextView
import androidx.test.platform.app.InstrumentationRegistry
import com.github.panpf.assemblyadapter.Item
import com.github.panpf.tools4j.test.ktx.assertThrow
import org.junit.Assert
import org.junit.Test

class ItemTest {

    @Test
    @Suppress("RemoveExplicitTypeArguments")
    fun test() {
        val context = InstrumentationRegistry.getInstrumentation().context
        val testItem = TestItem<String>(TextView(context))

        Assert.assertNotNull(testItem.context)
        Assert.assertNull(testItem.dataOrNull)
        assertThrow(NullPointerException::class) {
            testItem.dataOrThrow
        }
        Assert.assertEquals(-1, testItem.bindingAdapterPosition)
        Assert.assertEquals(-1, testItem.absoluteAdapterPosition)

        testItem.dispatchBindData(4, 5, "testData")
        Assert.assertEquals("testData", testItem.dataOrNull)
        Assert.assertEquals("testData", testItem.dataOrThrow)
        Assert.assertEquals(4, testItem.bindingAdapterPosition)
        Assert.assertEquals(5, testItem.absoluteAdapterPosition)
    }

    private class TestItem<DATA : Any>(itemView: View) : Item<DATA>(itemView) {
        override fun bindData(
            bindingAdapterPosition: Int,
            absoluteAdapterPosition: Int,
            data: DATA
        ) {

        }
    }
}