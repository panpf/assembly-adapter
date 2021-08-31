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

import android.content.Context
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import androidx.test.platform.app.InstrumentationRegistry
import com.github.panpf.assemblyadapter.list.expandable.ExpandableGroup
import com.github.panpf.assemblyadapter.list.expandable.SimpleExpandableChildItemFactory
import com.github.panpf.assemblyadapter.list.test.R
import org.junit.Assert
import org.junit.Test

class SimpleExpandableChildItemFactoryTest {

    @Test
    fun test() {
        val context = InstrumentationRegistry.getInstrumentation().context
        val parent = FrameLayout(context)

        val itemFactory = TestSimpleExpandableChildItemFactory()
        val item =
            itemFactory.dispatchCreateItem(parent) as SimpleExpandableChildItemFactory.SimpleExpandableChildItem<Strings, String>

        Assert.assertEquals(
            "",
            item.itemView.findViewById<TextView>(R.id.testItemTitleText).text
        )
        Assert.assertEquals(
            30f,
            item.itemView.findViewById<TextView>(R.id.testItemTitleText).textSize
        )

        item.dispatchChildBindData(0, 0, Strings("test_data"), true, 3, 4, "test_value")
        Assert.assertEquals(
            "test_value",
            item.itemView.findViewById<TextView>(R.id.testItemTitleText).text
        )
    }

    private data class Strings(val name: String = "") : ExpandableGroup {

        override fun getChildCount(): Int = name.length

        override fun getChild(childPosition: Int): Any {
            return name[childPosition].toString()
        }
    }

    private class TestSimpleExpandableChildItemFactory :
        SimpleExpandableChildItemFactory<Strings, String>(String::class) {

        override fun createItemView(
            context: Context,
            inflater: LayoutInflater,
            parent: ViewGroup
        ): View = inflater.inflate(R.layout.item_test, parent, false)

        override fun initItem(
            context: Context,
            itemView: View,
            item: SimpleExpandableChildItem<Strings, String>
        ) {
            itemView.findViewById<TextView>(R.id.testItemTitleText)
                .setTextSize(TypedValue.COMPLEX_UNIT_PX, 30f)
        }

        override fun bindItemData(
            context: Context,
            itemView: View,
            item: SimpleExpandableChildItem<Strings, String>,
            groupBindingAdapterPosition: Int,
            groupAbsoluteAdapterPosition: Int,
            groupData: Strings,
            isLastChild: Boolean,
            bindingAdapterPosition: Int,
            absoluteAdapterPosition: Int,
            data: String
        ) {
            itemView.findViewById<TextView>(R.id.testItemTitleText).text = data
        }
    }
}