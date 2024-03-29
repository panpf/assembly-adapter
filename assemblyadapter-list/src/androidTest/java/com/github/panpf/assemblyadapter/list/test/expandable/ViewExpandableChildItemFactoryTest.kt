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
package com.github.panpf.assemblyadapter.list.test.expandable

import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.TextView
import androidx.test.platform.app.InstrumentationRegistry
import com.github.panpf.assemblyadapter.list.expandable.ExpandableGroup
import com.github.panpf.assemblyadapter.list.expandable.SimpleExpandableChildItemFactory
import com.github.panpf.assemblyadapter.list.expandable.ViewExpandableChildItemFactory
import com.github.panpf.assemblyadapter.list.test.R
import org.junit.Assert
import org.junit.Test

class ViewExpandableChildItemFactoryTest {

    @Test
    fun test() {
        val context = InstrumentationRegistry.getInstrumentation().context
        val parent = FrameLayout(context)

        val itemFactory =
            ViewExpandableChildItemFactory<TextGroup, String>(
                String::class,
                R.layout.item_test
            )
        val item =
            itemFactory.dispatchCreateItem(parent) as SimpleExpandableChildItemFactory.SimpleExpandableChildItem<TextGroup, String>

        Assert.assertEquals(
            "",
            item.itemView.findViewById<TextView>(R.id.testItemTitleText).text
        )
        Assert.assertEquals(
            14f,
            item.itemView.findViewById<TextView>(R.id.testItemTitleText).textSize
        )

        item.dispatchChildBindData(0, 0, TextGroup("hello", "world"), true, 3, 4, "world")
        Assert.assertEquals(
            "",
            item.itemView.findViewById<TextView>(R.id.testItemTitleText).text
        )


        val itemFactory2 = ViewExpandableChildItemFactory<TextGroup, String>(
            String::class,
            LayoutInflater.from(context).inflate(R.layout.item_test, parent, false)
        )
        val item2 =
            itemFactory2.dispatchCreateItem(parent) as SimpleExpandableChildItemFactory.SimpleExpandableChildItem<TextGroup, String>

        Assert.assertEquals(
            "",
            item2.itemView.findViewById<TextView>(R.id.testItemTitleText).text
        )
        Assert.assertEquals(
            14f,
            item2.itemView.findViewById<TextView>(R.id.testItemTitleText).textSize
        )

        item2.dispatchChildBindData(
            0, 0, TextGroup("hello", "world"), true, 3, 4, "world"
        )
        Assert.assertEquals(
            "",
            item2.itemView.findViewById<TextView>(R.id.testItemTitleText).text
        )


        val itemFactory3 =
            ViewExpandableChildItemFactory<TextGroup, String>(String::class) { _, inflater, _ ->
                inflater.inflate(R.layout.item_test, parent, false)
            }
        val item3 =
            itemFactory3.dispatchCreateItem(parent) as SimpleExpandableChildItemFactory.SimpleExpandableChildItem<TextGroup, String>

        Assert.assertEquals(
            "",
            item3.itemView.findViewById<TextView>(R.id.testItemTitleText).text
        )
        Assert.assertEquals(
            14f,
            item3.itemView.findViewById<TextView>(R.id.testItemTitleText).textSize
        )

        item3.dispatchChildBindData(
            0, 0, TextGroup("hello", "world"), true, 3, 4, "world"
        )
        Assert.assertEquals(
            "",
            item3.itemView.findViewById<TextView>(R.id.testItemTitleText).text
        )
    }

    private data class Text(val text: String)

    private data class TextGroup(val list: List<Text>) : ExpandableGroup {

        @Suppress("unused")
        val listJoinToString: String
            get() = list.joinToString(prefix = "[", postfix = "]") { it.text }

        constructor(vararg texts: String) : this(texts.map { Text(it) }.toList())

        override fun getChildCount(): Int = list.size

        override fun getChild(childPosition: Int): Any {
            // Shield the differences in exceptions thrown by different versions of the ArrayList get method
            return list.getOrNull(childPosition)
                ?: throw IndexOutOfBoundsException("Index: $childPosition, Size: ${list.size}")
        }
    }
}