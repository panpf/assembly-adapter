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

import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.TextView
import androidx.test.platform.app.InstrumentationRegistry
import com.github.panpf.assemblyadapter.pager.ViewPagerItemFactory
import org.junit.Assert
import org.junit.Test

class ViewPagerItemFactoryTest {

    @Test
    fun test() {
        val context = InstrumentationRegistry.getInstrumentation().context
        val parent = FrameLayout(context)

        val itemFactory = ViewPagerItemFactory(String::class, R.layout.item_test)
        val itemView = itemFactory.dispatchCreateItemView(context, parent, 0, 1, "hello")

        Assert.assertEquals(
            "",
            itemView.findViewById<TextView>(R.id.testItemTitleText).text
        )
        Assert.assertEquals(
            14f,
            itemView.findViewById<TextView>(R.id.testItemTitleText).textSize
        )


        val itemFactory2 = ViewPagerItemFactory(
            String::class,
            LayoutInflater.from(context).inflate(R.layout.item_test, parent, false)
        )
        val itemView2 = itemFactory2.dispatchCreateItemView(context, parent, 0, 1, "hello")

        Assert.assertEquals(
            "",
            itemView2.findViewById<TextView>(R.id.testItemTitleText).text
        )
        Assert.assertEquals(
            14f,
            itemView2.findViewById<TextView>(R.id.testItemTitleText).textSize
        )


        val itemFactory3 =
            ViewPagerItemFactory(String::class) { _, inflater, parent, bindingAdapterPosition, absoluteAdapterPosition, data ->
                inflater.inflate(R.layout.item_test, parent, false).apply {
                    findViewById<TextView>(R.id.testItemTitleText).text = data
                }
            }
        val itemView3 = itemFactory3.dispatchCreateItemView(context, parent, 0, 1, "hello")

        Assert.assertEquals(
            "hello",
            itemView3.findViewById<TextView>(R.id.testItemTitleText).text
        )
        Assert.assertEquals(
            14f,
            itemView3.findViewById<TextView>(R.id.testItemTitleText).textSize
        )
    }
}