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
package com.github.panpf.assemblyadapter.item.test.internal

import android.view.View
import android.widget.TextView
import androidx.test.platform.app.InstrumentationRegistry
import com.github.panpf.assemblyadapter.Item
import com.github.panpf.assemblyadapter.OnClickListener
import com.github.panpf.assemblyadapter.common.item.R
import com.github.panpf.assemblyadapter.internal.ClickListenerWrapper
import org.junit.Assert
import org.junit.Test

class ClickListenerWrapperTest {

    private class TestItem(itemView: View) : Item<String>(itemView) {
        override fun bindData(
            bindingAdapterPosition: Int,
            absoluteAdapterPosition: Int,
            data: String
        ) {

        }
    }

    @Test
    fun test() {
        var bindingAdapterPosition: Int? = null
        var absoluteAdapterPosition: Int? = null
        var data: String? = null
        val clickListener =
            OnClickListener<String> { _, _, _bindingAdapterPosition, _absoluteAdapterPosition, _data ->
                bindingAdapterPosition = _bindingAdapterPosition
                absoluteAdapterPosition = _absoluteAdapterPosition
                data = _data
            }

        Assert.assertNull(bindingAdapterPosition)
        Assert.assertNull(absoluteAdapterPosition)
        Assert.assertNull(data)

        val context = InstrumentationRegistry.getInstrumentation().context
        val itemView = TextView(context).apply {
            setTag(R.id.aa_tag_clickBindItem, TestItem(this).apply {
                dispatchBindData(2, 7, "hello")
            })
        }
        ClickListenerWrapper(clickListener).onClick(itemView)

        Assert.assertEquals(2, bindingAdapterPosition)
        Assert.assertEquals(7, absoluteAdapterPosition)
        Assert.assertEquals("hello", data)
    }
}