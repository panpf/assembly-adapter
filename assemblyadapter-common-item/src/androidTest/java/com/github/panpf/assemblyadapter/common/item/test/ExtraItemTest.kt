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
import com.github.panpf.assemblyadapter.ExtraItem
import com.github.panpf.tools4j.test.ktx.assertThrow
import org.junit.Assert
import org.junit.Test

class ExtraItemTest {

    @Test
    @Suppress("RemoveExplicitTypeArguments")
    fun test() {
        val context = InstrumentationRegistry.getInstrumentation().context
        val testExtraItem = TestExtraItem<String>(TextView(context))

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

    private class TestExtraItem<DATA : Any>(itemView: View) : ExtraItem<DATA>(itemView) {
        override fun bindData(
            bindingAdapterPosition: Int,
            absoluteAdapterPosition: Int,
            data: DATA
        ) {

        }
    }
}