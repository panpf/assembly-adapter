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

import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import androidx.test.platform.app.InstrumentationRegistry
import com.github.panpf.assemblyadapter.IntactItemFactory
import org.junit.Assert
import org.junit.Test

class IntactPagerItemFactoryTest {

    @Test
    fun test() {
        val context = InstrumentationRegistry.getInstrumentation().context
        val parent = FrameLayout(context)

        IntactItemFactory().dispatchCreateItem(parent).apply {
            Assert.assertEquals(ViewGroup.LayoutParams.MATCH_PARENT, itemView.layoutParams.width)
            Assert.assertEquals(ViewGroup.LayoutParams.WRAP_CONTENT, itemView.layoutParams.height)
        }

        IntactItemFactory(horizontal = true).dispatchCreateItem(parent).apply {
            Assert.assertEquals(ViewGroup.LayoutParams.WRAP_CONTENT, itemView.layoutParams.width)
            Assert.assertEquals(ViewGroup.LayoutParams.MATCH_PARENT, itemView.layoutParams.height)
        }

        IntactItemFactory().dispatchCreateItem(parent).apply {
            val view = TextView(context)
            dispatchBindData(0, 1, view)
            Assert.assertSame((this.itemView as FrameLayout).getChildAt(0), view)

            dispatchBindData(1, 2, view)
            Assert.assertSame((this.itemView as FrameLayout).getChildAt(0), view)
        }
    }
}