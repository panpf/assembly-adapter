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
package com.github.panpf.assemblyadapter.pager.test.internal

import android.widget.TextView
import androidx.test.platform.app.InstrumentationRegistry
import com.github.panpf.assemblyadapter.OnLongClickListener
import com.github.panpf.assemblyadapter.pager.internal.PagerLongClickListenerWrapper
import org.junit.Assert
import org.junit.Test

class PagerLongClickListenerWrapperTest {

    @Test
    fun test() {
        var bindingAdapterPosition: Int? = null
        var absoluteAdapterPosition: Int? = null
        var data: String? = null
        val clickListener =
            OnLongClickListener<String> { _, _, _bindingAdapterPosition, _absoluteAdapterPosition, _data ->
                bindingAdapterPosition = _bindingAdapterPosition
                absoluteAdapterPosition = _absoluteAdapterPosition
                data = _data
                false
            }

        Assert.assertNull(bindingAdapterPosition)
        Assert.assertNull(absoluteAdapterPosition)
        Assert.assertNull(data)

        val context = InstrumentationRegistry.getInstrumentation().context
        val itemView = TextView(context)
        PagerLongClickListenerWrapper(clickListener, 2, 7, "hello").onLongClick(itemView)

        Assert.assertEquals(2, bindingAdapterPosition)
        Assert.assertEquals(7, absoluteAdapterPosition)
        Assert.assertEquals("hello", data)
    }
}